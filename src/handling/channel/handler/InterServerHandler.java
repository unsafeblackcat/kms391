package handling.channel.handler;

import client.*;
import client.inventory.AuctionItem;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.KoreaCalendar;
import constants.ServerConstants;
import constants.programs.ControlUnit;
import handling.RecvPacketOpcode;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.*;
import handling.world.guild.MapleGuild;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import scripting.NPCScriptManager;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.maps.FieldLimitType;
import server.maps.MapleMap;
import server.quest.MapleQuest;
import server.quest.QuestCompleteStatus;
import tools.*;
import tools.data.LittleEndianAccessor;
import tools.packet.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import server.Timer.EtcTimer;

public class InterServerHandler {

	public static final void EnterCS(MapleClient c, MapleCharacter chr) {
		chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
		chr.getClient().getSession().writeAndFlush(CField.UIPacket.openUI(1334)); // 152
	}

	public static final void EnterCS(MapleClient c, MapleCharacter chr, boolean npc) {
		if (npc) {
			chr.getClient().removeClickedNPC();
			NPCScriptManager.getInstance().dispose(chr.getClient());
			chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			NPCScriptManager.getInstance().start(c, ServerConstants.csNpc);
		} else {
			if (chr.getMap() == null || chr.getEventInstance() != null || c.getChannelServer() == null) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (c.getPlayer().getMapId() != ServerConstants.warpMap) {
				c.getPlayer().dropMessage(1, "現金商店只能在主城使用.");
				return;
			}
			if (World.getPendingCharacterSize() >= 10) {
				chr.dropMessage(1, "當前服務器擁擠，無法移動.");
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			ChannelServer ch = ChannelServer.getInstance(c.getChannel());
			chr.changeRemoval();
			if (chr.getMessenger() != null) {
				MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
				World.Messenger.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
			}
			PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
			PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
			World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), -10);
			ch.removePlayer(chr);
			c.updateLoginState(3, c.getSessionIPAddress());
			chr.saveToDB(false, false);
			chr.getMap().removePlayer(chr);
			c.getSession()
					.writeAndFlush(CField.getChannelChange(c, Integer.parseInt(CashShopServer.getIP().split(":")[1])));
			c.setPlayer(null);
			c.setReceiving(false);
		}
	}

	public static final void Loggedin(int playerid, MapleClient c) {
		try {
			MapleCharacter player;
			ChannelServer channelServer = c.getChannelServer();
			CharacterTransfer transfer = channelServer.getPlayerStorage().getPendingCharacter(playerid);
			if (transfer == null) {
				player = MapleCharacter.loadCharFromDB(playerid, c, true);
				Pair<String, String> ip = LoginServer.getLoginAuth(playerid);
				String s = c.getSessionIPAddress();
				if (ip == null || !s.substring(s.indexOf('/') + 1).equals(ip.left)) {
					if (ip != null) {
						LoginServer.putLoginAuth(playerid, (String) ip.left, (String) ip.right);
					}
					c.disconnect(true, false, false);
					c.getSession().close();
					return;
				}
				if (c.getAccID() != player.getAccountID()) {
					c.disconnect(true, false, false);
					c.getSession().close();
					return;
				}
				c.setTempIP((String) ip.right);
				if (World.Find.findChannel(playerid) >= 0) {
					c.disconnect(true, false, false);
					c.getSession().close();
					return;
				}
			} else {
				player = MapleCharacter.ReconstructChr(transfer, c, true);
			}

			c.setPlayer(player);
			c.setAccID(player.getAccountID());

			if (GameConstants.isZero(c.getPlayer().getJob())) {
				if (c.getPlayer().getGender() == 1) {
					c.getPlayer().setGender((byte) 0);
					c.getPlayer().setSecondGender((byte) 1);
				} else {
					c.getPlayer().setGender((byte) 0);
					c.getPlayer().setSecondGender((byte) 1);
				}
			}

			c.loadKeyValues();
			c.loadCustomDatas();

			if (!c.CheckIPAddress()) {
				c.disconnect(true, false, false);
				c.getSession().close();
				return;
			}

			channelServer.removePlayer(player);
			World.isCharacterListConnected(player.getName(), c.loadCharacterNames(c.getWorld()));

			c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
			channelServer.addPlayer(player);
			player.giveCoolDowns(PlayerBuffStorage.getCooldownsFromStorage(player.getId()));

			// sendOpcodeEncryption(c);
			if (player.getInnerSkills().get(0).size() == 0) {
				player.getInnerSkills().get(0)
						.add(new InnerSkillValueHolder(70000015, (byte) 1, (byte) 1, (byte) 0, (byte) 0));
				player.getInnerSkills().get(0)
						.add(new InnerSkillValueHolder(70000015, (byte) 1, (byte) 1, (byte) 0, (byte) 0));
				player.getInnerSkills().get(0)
						.add(new InnerSkillValueHolder(70000015, (byte) 1, (byte) 1, (byte) 0, (byte) 0));
			}

			if (player.getInnerSkills().get(1).size() == 0) {
				player.getInnerSkills().get(1)
						.add(new InnerSkillValueHolder(70000015, (byte) 1, (byte) 1, (byte) 0, (byte) 1));
				player.getInnerSkills().get(1)
						.add(new InnerSkillValueHolder(70000015, (byte) 1, (byte) 1, (byte) 0, (byte) 1));
				player.getInnerSkills().get(1)
						.add(new InnerSkillValueHolder(70000015, (byte) 1, (byte) 1, (byte) 0, (byte) 1));
			}

			if (player.getInnerSkills().get(2).size() == 0) {
				player.getInnerSkills().get(2)
						.add(new InnerSkillValueHolder(70000015, (byte) 1, (byte) 1, (byte) 0, (byte) 2));
				player.getInnerSkills().get(2)
						.add(new InnerSkillValueHolder(70000015, (byte) 1, (byte) 1, (byte) 0, (byte) 2));
				player.getInnerSkills().get(2)
						.add(new InnerSkillValueHolder(70000015, (byte) 1, (byte) 1, (byte) 0, (byte) 2));
			}

			if (c.getPlayer().getKeyValue(7293, "damage_skin") == -1) { // 데미지 스킨 기본 착용 값 설정
				c.getPlayer().setKeyValue(7293, "damage_skin", 0 + "");
			}

			if (c.getPlayer().getKeyValue(13191, "skinroom") == -1) { // 데미지 스킨 슬롯 최대 설정
				c.getPlayer().setKeyValue(13191, "skinroom", 96 + "");
			}

			ControlUnit.동접추가(c.getPlayer().getName());

			c.getSession().writeAndFlush(CField.getCharInfo(player));

			if (c.getPlayer().getLevel() >= 260) {
				if (c.getPlayer().getQuestStatus(1488) != 2) {
					c.getPlayer().forceCompleteQuest(1488);

					c.getPlayer().setSolErda(0);
					c.getPlayer().setSolErdaStrength(0);

					c.getPlayer().changeSkillLevel(GameConstants.getHexaOriginFirst(c.getPlayer().getJob()), (byte) 1,
							(byte) 1);
				}

				c.getSession().writeAndFlush(CField.activeHexaSkill());
			}

			if (!c.getPlayer().getBuffedValue(80000757)) { // 환생 전용 버프
				SkillFactory.getSkill(80000757).getEffect(1).applyTo(c.getPlayer());
			}

			if (c.getPlayer().getBuffedValue(80000783)) { // 유일 전용 버프
				SkillFactory.getSkill(80000783).getEffect(1).applyTo(c.getPlayer());
			}

			if (c.getPlayer().getOneItemEquippedCount() > 0) { // 유일 전용 버프
				SkillFactory.getSkill(80000783).getEffect(1).applyTo(c.getPlayer());
			}

			c.getSession().writeAndFlush(CWvsContext.updateInnerPreset((byte) c.getPlayer().getInnerSkillsPreset()));

			int[] bossquests = { 38214, 36013, 33565, 31851, 31833, 3496, 3470, 30007, 3170, 31179, 3521, 31152, 34015,
					33294, 34330, 34585, 35632, 35731, 35815, 34478, 34331, 34478, 100114, 16013, 34120, 34218, 34330,
					34331, 34478, 34269, 34272, 34585, 34586, 6500, 1465, 1466, 26607, 1484, 39921, 16059, 16015, 16013,
					100114, 34128, 39013, 34772, 39034, 34269, 34271, 34272, 34243, 39204, 15417, 34377, 34477, 34450,
					38401, 38404 };
			for (int questid : bossquests) {
				if (player.getQuestStatus(questid) != 2) {
					if (questid == 1465 || questid == 1466) {
						if (player.getLevel() >= 200) {
							MapleQuest.getInstance(questid).forceComplete(player, 0, false);
						}
					} else if (questid == 38404) {
						if (player.getLevel() >= 275) {// 275完成38404原初徽章任務 包括所有槽位 改爲脚本開放
							// MapleQuest.getInstance(questid).forceComplete(player, 0, false);
						}
					} else {
						MapleQuest.getInstance(questid).forceComplete(player, 0, false);
					}
				}
			}

			if (c.getPlayer().getKeyValue(39160, "start") <= 0) {
				c.getPlayer().setKeyValue(39160, "start", "1");
				c.getPlayer().setKeyValue(39165, "start", "1");
			}
			if (c.getPlayer().getKeyValueStr(34271, "02") == null) {
				c.getPlayer().setKeyValue(34271, "02", "h0");
				c.getPlayer().setKeyValue(34271, "20", "h0");
				c.getPlayer().setKeyValue(34271, "30", "h0");
				c.getPlayer().setKeyValue(34271, "21", "h0");
				c.getPlayer().setKeyValue(34271, "31", "h0");
				c.getPlayer().setKeyValue(34271, "23", "h0");
				c.getPlayer().setKeyValue(34271, "32", "h1");
				c.getPlayer().setKeyValue(34271, "33", "h0");
				c.getPlayer().setKeyValue(34271, "52", "h0");
				c.getPlayer().setKeyValue(34271, "34", "h0");
				c.getPlayer().setKeyValue(34271, "35", "h0");
				c.getPlayer().setKeyValue(34271, "53", "h1");
				c.getPlayer().setKeyValue(34271, "36", "h0");
				c.getPlayer().setKeyValue(34271, "18", "h0");
				c.getPlayer().setKeyValue(34271, "34", "h0");
				c.getPlayer().setKeyValue(34271, "54", "h0");
				c.getPlayer().setKeyValue(34271, "28", "h0");
				c.getPlayer().setKeyValue(34271, "29", "h0");
			}
			if (player.getQuestStatus(30023) != 1 && player.getQuestStatus(30024) != 1
					&& player.getQuestStatus(30025) != 1 && player.getQuestStatus(30026) != 1) {
				MapleQuest.getInstance(30023).forceStart(player, 0, "10");
				MapleQuest.getInstance(30024).forceStart(player, 0, "10");
				MapleQuest.getInstance(30025).forceStart(player, 0, "10");
				MapleQuest.getInstance(30026).forceStart(player, 0, "10");
			}
			if (GameConstants.isEvan(player.getJob())) {
				if (player.getQuestStatus(22130) != 2 && player.getJob() != 2001) {
					MapleQuest.getInstance(22130).forceComplete(player, 0);
				}
			} else if (GameConstants.isEunWol(player.getJob())) {
				if (player.getQuestStatus(1542) != 2) {
					MapleQuest.getInstance(1542).forceComplete(player, 0);
				}
			} else if (GameConstants.isArk(player.getJob())) {
				for (int k = 34940; k < 34960; k++) {
					MapleQuest.getInstance(k).forceComplete(player, 0);
				}
			} else if (GameConstants.isKadena(player.getJob())) {
				for (int k = 34600; k < 34650; k++) {
					MapleQuest.getInstance(k).forceComplete(player, 0);
				}
			}
			if (player.getClient().getKeyValue("LevelUpGive") == null) {
				player.getClient().setKeyValue("LevelUpGive", "0000000000000000");
			} else {
				String[] str = player.getClient().getKeyValue("LevelUpGive").split("");
				for (int k = 0; k < 16; k++) {
					if (Integer.parseInt(str[k]) == 1) {
						int questid = 60000 + k;
						if (player.getQuestStatus(questid) != 2) {
							player.forceCompleteQuest(questid);
						}
					}
				}
			}
			if (player.getClient().getKeyValue("GrowQuest") == null) {
				player.getClient().setKeyValue("GrowQuest", "0000000000");
			} else {
				String[] str = player.getClient().getKeyValue("GrowQuest").split("");
				for (int k = 0; k < 10; k++) {
					int questid = 50000 + k;
					if (Integer.parseInt(str[k]) == 1) {
						if (player.getQuestStatus(questid) != 1 && player.getQuestStatus(questid) != 2) {
							MapleQuest.getInstance(questid).forceStart(c.getPlayer(), 0, "");
						}
					} else if (Integer.parseInt(str[k]) == 2 && player.getQuestStatus(questid) != 2) {
						player.forceCompleteQuest(questid);
					}
				}
			}

			if (player.getClient().getKeyValue("BloomingTuto") != null) {
				if (player.getClient().getKeyValue("BloomingSkill") != null) {
					String str = player.getClient().getKeyValue("BloomingSkill");
					String[] ab = str.split("");

					int skillid = 80003036;

					for (int a = 0; a < ab.length; a++) {
						player.setKeyValue(501378, a + "", ab[a] + "");
						if (Integer.parseInt(ab[a]) > 0) {
							player.changeSkillLevel(skillid + a, (byte) Integer.parseInt(ab[a]), (byte) 3);
						}
					}
				}
				if (player.getClient().getKeyValue("Bloomingbloom") != null) {
					player.setKeyValue(501367, "bloom", player.getClient().getKeyValue("Bloomingbloom"));
				}

				if (player.getClient().getKeyValue("BloomingSkillPoint") != null) {
					player.setKeyValue(501378, "sp", player.getClient().getKeyValue("BloomingSkillPoint"));
				}

				if (player.getClient().getKeyValue("BloomingReward") != null) {
					player.setKeyValue(501367, "reward", player.getClient().getKeyValue("BloomingReward"));
				}

				if (player.getClient().getKeyValue("week") != null) {
					player.setKeyValue(501367, "week", player.getClient().getKeyValue("week"));
				}

				if (player.getClient().getKeyValue("getReward") != null) {
					player.setKeyValue(501367, "getReward", player.getClient().getKeyValue("getReward"));
				}

				if (player.getClient().getKeyValue("BloomingSkilltuto") != null) {
					player.setKeyValue(501378, "tuto", "1");
				}

				if (player.getClient().getKeyValue("BloominggiveSun") != null) {
					player.setKeyValue(501367, "giveSun", "1");
				}

				if (player.getClient().getKeyValue("Bloomingflower") != null) {
					player.setKeyValue(501387, "flower", player.getClient().getKeyValue("Bloomingflower"));
				}

				if (Integer.parseInt(player.getClient().getKeyValue("BloomingTuto")) > 1
						&& player.getQuestStatus(501394) != 2) {
					player.forceCompleteQuest(501394);
				}

				if (Integer.parseInt(player.getClient().getKeyValue("BloomingTuto")) > 2) {
					if (player.getQuestStatus(501375) != 2) {
						player.forceCompleteQuest(501375);
					}

					if (player.getKeyValue(501375, "start") != 1L) {
						player.setKeyValue(501375, "start", "1");
					}
				}

				if (Integer.parseInt(player.getClient().getKeyValue("BloomingTuto")) > 3) {
					if (player.getQuestStatus(501376) != 2) {
						player.forceCompleteQuest(501376);
					}

					if (player.getKeyValue(501376, "start") != 1L) {
						player.setKeyValue(501376, "start", "1");
					}
				}
			}

			for (Iterator<Integer> iterator = QuestCompleteStatus.completeQuests.iterator(); iterator.hasNext();) {
				int questid = ((Integer) iterator.next()).intValue();

				if (player.getQuestStatus(questid) != 2) {
					MapleQuest.getInstance(questid).forceComplete(player, 0, false);
				}
			}
			if (c.getPlayer().getKeyValue(125, "date") != GameConstants.getCurrentDate_NoTime()) {
				c.getPlayer().setKeyValue(125, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
				int pirodo = 0;
				switch (1) {
				case 1: {
					pirodo = 100;
					break;
				}
				case 2: {
					pirodo = 120;
					break;
				}
				case 3: {
					pirodo = 160;
					break;
				}
				case 4: {
					pirodo = 200;
					break;
				}
				case 5: {
					pirodo = 240;
					break;
				}
				case 6: {
					pirodo = 280;
					break;
				}
				case 7: {
					pirodo = 320;
					break;
				}
				case 8: {
					pirodo = 360;
					break;
				}
				}
				c.getPlayer().setKeyValue(123, "pp", String.valueOf(pirodo));
			}

			FileoutputUtil.log(FileoutputUtil. 登入日誌, "[連線] 帳號編號 : " + player.getClient().getAccID()  + " | "
	                + player.getName()  + "(" + player.getId()  + ") 已成功連線");

			MatrixHandler.gainMatrix(c.getPlayer(), false);

			if (player.getKeyValue(18771, "rank") == -1L || player.getKeyValue(18771, "rank") == 100L) {
				player.setKeyValue(18771, "rank", "101");
			}

			if (c.getKeyValue("rank") == null) {
				c.setKeyValue("rank", String.valueOf(player.getKeyValue(18771, "rank")));
			}

			if (Integer.parseInt(c.getKeyValue("rank")) < player.getKeyValue(18771, "rank")) {
				c.setKeyValue("rank", String.valueOf(player.getKeyValue(18771, "rank")));
			}

			if (Integer.parseInt(c.getKeyValue("rank")) > player.getKeyValue(18771, "rank")) {
				player.setKeyValue(18771, "rank", c.getKeyValue("rank"));
			}

			player.LoadPlatformerRecords();
			player.giveCoolDowns(PlayerBuffStorage.getCooldownsFromStorage(player.getId()));

			if (player.choicepotential != null && player.memorialcube != null) {
				Item ordinary = player.getInventory(MapleInventoryType.EQUIP)
						.getItem(player.choicepotential.getPosition());
				if (ordinary != null) {
					player.choicepotential.setInventoryId(ordinary.getInventoryId());
				}
			}

			if (c.getKeyValue("PNumber") == null) {
				c.setKeyValue("PNumber", "0");
			}

			if (player.returnscroll != null) {
				Item ordinary = player.getInventory(MapleInventoryType.EQUIP)
						.getItem(player.returnscroll.getPosition());
				if (ordinary != null) {
					player.returnscroll.setInventoryId(ordinary.getInventoryId());
				}
			}
			player.showNote();
			player.showsendNote();

			c.getSession().writeAndFlush(CSPacket.enableCSUse());
			c.getSession().writeAndFlush(SLFCGPacket.SetupZodiacInfo());

			if (player.getKeyValue(190823, "grade") == -1) {
				player.setKeyValue(190823, "grade", "0");
			}

			c.getSession().writeAndFlush(LoginPacket.debugClient());

			if (c.getPlayer().checkFiveSecond == null) {
				c.getPlayer().checkFiveSecond = EtcTimer.getInstance().register(new Runnable() {

					@Override
					public void run() {
						c.getSession().writeAndFlush(CWvsContext.checkFiveSecond());
					}
				}, 5000L);
			}

			if (c.getPlayer().ingamePing == null) {
				c.getPlayer().ingamePing = EtcTimer.getInstance().register(new Runnable() {

					@Override
					public void run() {
						c.getSession().writeAndFlush(LoginPacket.getIngamePing());
					}
				}, 10000L);
			}

			if (c.getPlayer().debugClient == null) {
				c.getPlayer().debugClient = EtcTimer.getInstance().register(new Runnable() {

					@Override
					public void run() {
						c.getSession().writeAndFlush(LoginPacket.debugClient2(false));
					}
				}, 15000L);
			}

			c.getSession().writeAndFlush(LoginPacket.debugClient2(true));

			c.getSession().writeAndFlush(LoginPacket.startCheckFiveMinute());

			c.getSession().writeAndFlush(LoginPacket.ingameNeeded2());
			c.getSession().writeAndFlush(LoginPacket.ingameNeeded3());
			c.getSession().writeAndFlush(LoginPacket.ingameNeeded1());

			List<Pair<Integer, Integer>> list = new ArrayList<>();// 修改記錄？？
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8500002), Integer.valueOf(3655)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8500012), Integer.valueOf(3656)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8500022), Integer.valueOf(3657)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8644612), Integer.valueOf(0)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8644650), Integer.valueOf(3680)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8644655), Integer.valueOf(3682)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8645009), Integer.valueOf(3681)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8645066), Integer.valueOf(3683)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8800002), Integer.valueOf(3654)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8800022), Integer.valueOf(6994)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8800102), Integer.valueOf(15166)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8810018), Integer.valueOf(3789)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8810122), Integer.valueOf(3790)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8810214), Integer.valueOf(3651)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8820001), Integer.valueOf(3652)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8820212), Integer.valueOf(3653)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8840000), Integer.valueOf(3794)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8840007), Integer.valueOf(3793)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8840014), Integer.valueOf(3795)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8850005), Integer.valueOf(31095)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8850006), Integer.valueOf(31096)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8850007), Integer.valueOf(31097)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8850008), Integer.valueOf(31098)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8850009), Integer.valueOf(31099)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8850011), Integer.valueOf(31196)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8850111), Integer.valueOf(31199)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8860000), Integer.valueOf(3792)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8860005), Integer.valueOf(3791)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8870000), Integer.valueOf(3649)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8870100), Integer.valueOf(3650)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880000), Integer.valueOf(3992)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880002), Integer.valueOf(3993)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880010), Integer.valueOf(3996)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880100), Integer.valueOf(3663)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880101), Integer.valueOf(34018)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880110), Integer.valueOf(3662)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880111), Integer.valueOf(34017)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880140), Integer.valueOf(3659)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880141), Integer.valueOf(3660)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880142), Integer.valueOf(3684)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880150), Integer.valueOf(34354)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880151), Integer.valueOf(3661)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880153), Integer.valueOf(34356)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880155), Integer.valueOf(3685)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880156), Integer.valueOf(34349)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880167), Integer.valueOf(34368)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880177), Integer.valueOf(34369)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880200), Integer.valueOf(3591)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880301), Integer.valueOf(3666)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880302), Integer.valueOf(3658)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880303), Integer.valueOf(3664)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880304), Integer.valueOf(3665)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880341), Integer.valueOf(3669)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880342), Integer.valueOf(3670)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880343), Integer.valueOf(3667)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880344), Integer.valueOf(3668)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880400), Integer.valueOf(3671)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880405), Integer.valueOf(3672)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880410), Integer.valueOf(3673)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880415), Integer.valueOf(3674)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880502), Integer.valueOf(3676)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880503), Integer.valueOf(3677)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880505), Integer.valueOf(3675)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880518), Integer.valueOf(3679)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880519), Integer.valueOf(3678)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880600), Integer.valueOf(3686)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880602), Integer.valueOf(3687)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8880614), Integer.valueOf(3687)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8881000), Integer.valueOf(0)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8900000), Integer.valueOf(30043)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8900001), Integer.valueOf(30043)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8900002), Integer.valueOf(30043)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8900003), Integer.valueOf(30043)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8900100), Integer.valueOf(30032)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8900101), Integer.valueOf(30032)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8900102), Integer.valueOf(30032)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8900103), Integer.valueOf(0)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8910000), Integer.valueOf(30044)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8910100), Integer.valueOf(30039)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8920000), Integer.valueOf(30045)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8920001), Integer.valueOf(30045)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8920002), Integer.valueOf(30045)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8920003), Integer.valueOf(30045)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8920006), Integer.valueOf(30045)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8920100), Integer.valueOf(30033)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8920101), Integer.valueOf(30033)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8920102), Integer.valueOf(30033)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8920103), Integer.valueOf(30033)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8920106), Integer.valueOf(30033)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8930000), Integer.valueOf(30046)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8930100), Integer.valueOf(30041)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8950000), Integer.valueOf(33261)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8950001), Integer.valueOf(33262)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8950002), Integer.valueOf(33263)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8950100), Integer.valueOf(33301)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8950101), Integer.valueOf(33302)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(8950102), Integer.valueOf(33303)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(9101078), Integer.valueOf(15172)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(9101190), Integer.valueOf(0)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(9309200), Integer.valueOf(0)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(9309201), Integer.valueOf(0)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(9309203), Integer.valueOf(0)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(9309205), Integer.valueOf(0)));
			list.add(new Pair<Integer, Integer>(Integer.valueOf(9309207), Integer.valueOf(0)));

			c.send(CField.BossMatchingChance(list));
			player.updateLinkSkillPacket();
			player.silentGiveBuffs(PlayerBuffStorage.getBuffsFromStorage(player.getId()));

			if (player.getCooldownSize() > 0) {
				int[] bufflist = { 80002282, 2321055, 2023661, 2023662, 2023663, 2023664, 2023665, 2023666, 2450064,
						2450134, 2450038, 2450124, 2450147, 2450148, 2450149, 2023558, 2003550, 2023556, 2003551 };
				int arrayOfInt1[], k;
				byte b;
				for (arrayOfInt1 = bufflist, k = arrayOfInt1.length, b = 0; b < k;) {
					Integer a = Integer.valueOf(arrayOfInt1[b]);
					if (!player.getBuffedValue(a.intValue())) {
						for (MapleCoolDownValueHolder mapleCoolDownValueHolder : player.getCooldowns()) {
							if (mapleCoolDownValueHolder.skillId == a.intValue()) {
								if (MapleItemInformationProvider.getInstance().getItemEffect(a.intValue()) != null) {
									MapleItemInformationProvider.getInstance().getItemEffect(a.intValue())
											.applyTo(player, false, (int) (mapleCoolDownValueHolder.length
													+ mapleCoolDownValueHolder.startTime - System.currentTimeMillis()));
									break;
								}
								SkillFactory.getSkill(a.intValue()).getEffect(player.getSkillLevel(a.intValue()))
										.applyTo(player, false, (int) (mapleCoolDownValueHolder.length
												+ mapleCoolDownValueHolder.startTime - System.currentTimeMillis()));
								break;
							}
						}
					}
					b++;
				}
			}

			// c.getSession().writeAndFlush(CWvsContext.updateMaplePoint(player));
			if (player.returnscroll != null) {
				c.getSession().writeAndFlush(CWvsContext.returnEffectConfirm(player.returnscroll, player.returnSc));
				c.getSession().writeAndFlush(CWvsContext.returnEffectModify(player.returnscroll, player.returnSc));
			}

			if (player.choicepotential != null && player.memorialcube != null) {
				c.getSession()
						.writeAndFlush(CField.getBlackCubeStart(player, (Item) player.choicepotential, false,
								player.memorialcube.getItemId(), player.memorialcube.getPosition(),
								player.getItemQuantity(5062010, false), 1));
			}

			if (GameConstants.isBlaster(player.getJob())) {
				player.Cylinder(0);
			}

			if (!player.isGM() || !player.getBuffedValue(9001004))
				;
			if (GameConstants.isZero(player.getJob())) {
				int[] ZeroQuest = { 31686, 31198, 41908, 41909, 41907, 32550, 33565, 3994, 6000, 39001, 40000, 40001,
						7049, 40002, 40003, 40004, 40100, 40101, 6995, 40102, 40103, 40104, 40105, 40106, 40107, 40200,
						40108, 40201, 40109, 40202, 40110, 40203, 40111, 40204, 40050, 40112, 40205, 40051, 40206,
						40052, 40207, 40300, 40704, 40053, 40208, 40301, 7783, 40054, 40209, 40302, 40705, 40055, 40210,
						40303, 40056, 40304, 7600, 40800, 40057, 40305, 40801, 40058, 40306, 40059, 40307, 40400, 40060,
						40308, 40401, 40061, 40309, 40402, 40960, 40062, 40310, 40403, 40930, 40961, 40063, 40404,
						40900, 40931, 40962, 40405, 7887, 40901, 40932, 40963, 40406, 40902, 40933, 40964, 40407, 40500,
						40903, 40934, 40408, 40501, 40904, 40409, 40502, 7860, 40905, 40503, 7892, 7707, 40504, 40505,
						40970, 40506, 40940, 40971, 41250, 41312, 40600, 40910, 40941, 40972, 41251, 40601, 40911,
						40942, 40973, 41252, 40602, 40912, 40943, 40974, 41253, 41315, 41408, 40603, 40913, 40944,
						41254, 41316, 40604, 40914, 41255, 41317, 40605, 41256, 40606, 41257, 41350, 40607, 40700,
						41103, 41258, 41351, 40701, 40980, 41104, 41352, 40702, 40950, 41105, 41353, 40703, 40920,
						40951, 41106, 41261, 41354, 40921, 40952, 40922, 40953, 41263, 40923, 40954, 41264, 41357,
						40924, 41358, 41111, 41359, 41050, 41360, 41114, 41269, 41300, 41115, 41270, 41301, 41363,
						41302, 41364, 41303, 41365, 41055, 41304, 41366, 41305, 41925, 41306, 41926, 41307, 41400,
						41370, 41401 };
				for (int questid : ZeroQuest) {
					if (questid != 41907 && player.getQuestStatus(questid) != 2) {
						MapleQuest.getInstance(questid).forceComplete(player, 0);
					}
					if (questid == 41907 && player.getQuestStatus(questid) != 1) {
						MapleQuest quest = MapleQuest.getInstance(41907);
						MapleQuestStatus queststatus = new MapleQuestStatus(quest, 1);
						queststatus.setCustomData("0");
						player.updateQuest(queststatus, true);
					}
				}

				if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11) != null) {
					Equip eq = (Equip) player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
					Equip eq2 = (Equip) player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
					if (eq.getPotential1() != eq2.getPotential1()) {
						eq.setPotential1(eq2.getPotential1());
					}
					if (eq.getPotential2() != eq2.getPotential2()) {
						eq.setPotential2(eq2.getPotential2());
					}
					if (eq.getPotential3() != eq2.getPotential3()) {
						eq.setPotential3(eq2.getPotential3());
					}
					if (eq.getPotential4() != eq2.getPotential4()) {
						eq.setPotential4(eq2.getPotential4());
					}
					if (eq.getPotential5() != eq2.getPotential5()) {
						eq.setPotential5(eq2.getPotential5());
					}
					if (eq.getPotential6() != eq2.getPotential6()) {
						eq.setPotential6(eq2.getPotential6());
					}
				}
			}
			player.addKV("bossPractice", "0");

			if (player.getKeyValue(1477, "count") == -1L) {
				player.setKeyValue(1477, "count", "0");
			}

			if (player.getKeyValue(19019, "id") == -1L) {
				player.setKeyValue(19019, "id", "0");
			}

			if (player.getKeyValue(7293, "damage_skin") == -1L) {
				player.setKeyValue(7293, "damage_skin", "2438159");
			}

			if (player.getKeyValue(501619, "count") == -1L) {
				player.setKeyValue(501619, "count", "999");
			}

			if (player.getKeyValue(100711, "point") == -1L) {
				player.setKeyValue(100711, "point", "0");
				player.setKeyValue(100711, "sum", "0");
				player.setKeyValue(100711, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
				player.setKeyValue(100711, "today", "0");
				player.setKeyValue(100711, "total", "0");
				player.setKeyValue(100711, "lock", "0");
			}

			if (player.getKeyValue(100711, "date") != GameConstants.getCurrentDate_NoTime()) {
				player.setKeyValue(100711, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
				player.setKeyValue(100711, "today", "0");
				player.setKeyValue(100711, "lock", "0");
			}

			if (player.getKeyValue(100712, "point") == -1L) {
				player.setKeyValue(100712, "point", "0");
				player.setKeyValue(100712, "sum", "0");
				player.setKeyValue(100712, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
				player.setKeyValue(100712, "today", "0");
				player.setKeyValue(100712, "total", "0");
				player.setKeyValue(100712, "lock", "0");
			}

			if (player.getKeyValue(16700, "date") != GameConstants.getCurrentDate_NoTime()) {
				player.setKeyValue(16700, "count", "0");
				player.setKeyValue(16700, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
			}

			if (player.getKeyValue(100712, "date") != GameConstants.getCurrentDate_NoTime()) {
				player.setKeyValue(100712, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
				player.setKeyValue(100712, "today", "0");
				player.setKeyValue(100712, "lock", "0");
			}

			if (player.getKeyValue(501215, "point") == -1L) {
				player.setKeyValue(501215, "point", "0");
				player.setKeyValue(501215, "sum", "0");
				player.setKeyValue(501215, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
				player.setKeyValue(501215, "week", "0");
				player.setKeyValue(501215, "total", "0");
				player.setKeyValue(501215, "lock", "0");
			}

			if (player.getKeyValue(501045, "point") == -1L) {
				player.setKeyValue(501045, "point", "0");
				player.setKeyValue(501045, "lv", "1");
				player.setKeyValue(501045, "sp", "0");
				player.setKeyValue(501045, "reward0", "0");
				player.setKeyValue(501045, "reward1", "0");
				player.setKeyValue(501045, "reward2", "0");
				player.setKeyValue(501045, "mapTuto", "2");
				player.setKeyValue(501045, "skillTuto", "1");
				player.setKeyValue(501045, "payTuto", "1");
			}
			player.setKeyValue(501092, "lv", "9");

			if (player.getKeyValue(501046, "start") == -1L) {
				player.setKeyValue(501046, "start", "1");
				for (int k = 0; k < 9; k++) {
					player.setKeyValue(501046, String.valueOf(k), "0");
				}
			}

			if (c.getKeyValue("dailyGiftDay") == null) {
				c.setKeyValue("dailyGiftDay", "0");
			}

			if (c.getKeyValue("dailyGiftComplete") == null) {
				c.setKeyValue("dailyGiftComplete", "0");
			}

			if (player.getKeyValue(501385, "date") != GameConstants.getCurrentDate_NoTime()) {
				player.setKeyValue(501385, "count", "0");
				player.setKeyValue(501385, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
			}

			for (AuctionItem auctionItem : AuctionServer.getItems().values()) {
				if (auctionItem.getAccountId() == c.getAccID() && auctionItem.getState() == 3
						&& auctionItem.getHistory().getState() == 3) {
					player.getClient().getSession().writeAndFlush(CWvsContext.AlarmAuction(0, auctionItem));
				}
			}

			if (player.getKeyValue(19019, "id") > 0L && !player.haveItem((int) player.getKeyValue(19019, "id"))) {
				player.setKeyValue(19019, "id", "0");
				player.getMap().broadcastMessage(player, CField.showTitle(player.getId(), 0), false);
			}

			if (player.getKeyValue(210416, "TotalDeadTime") > 0L) {
				player.getClient().send(CField.ExpDropPenalty(true, (int) player.getKeyValue(210416, "TotalDeadTime"),
						(int) player.getKeyValue(210416, "NowDeadTime"), 80, 80));
			}

			if (player.getClient().getKeyValue("UnionQuest1") != null) {
				int questid = 16011;

				if (Integer.parseInt(player.getClient().getKeyValue("UnionQuest1")) == 1) {
					player.forceCompleteQuest(16011);
				} else {
					MapleQuest quest = MapleQuest.getInstance(questid);
					player.getQuest_Map().remove(quest);
					MapleQuestStatus queststatus = new MapleQuestStatus(quest, 0);
					queststatus.setStatus((byte) 0);
					queststatus.setCustomData("");
					player.getClient().send(CWvsContext.InfoPacket.updateQuest(queststatus));
				}
			}
			if (player.getClient().getKeyValue("UnionQuest2") != null) {
				int questid = 16012;
				if (Integer.parseInt(player.getClient().getKeyValue("UnionQuest2")) == 1) {
					player.forceCompleteQuest(questid);
				} else {
					MapleQuest quest = MapleQuest.getInstance(questid);
					player.getQuest_Map().remove(quest);
					MapleQuestStatus queststatus = new MapleQuestStatus(quest, 0);
					queststatus.setStatus((byte) 0);
					queststatus.setCustomData("");
					player.getClient().send(CWvsContext.InfoPacket.updateQuest(queststatus));
				}
			}
			boolean itemexppendent = false;
			KoreaCalendar kc = new KoreaCalendar();
			String nowtime = (kc.getYeal() % 100) + kc.getMonths() + kc.getDays() + kc.getHours() + kc.getMins()
					+ kc.getMins();
			Item item = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -17);
			Item item2 = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -31);
			Item exppen = null;
			if (item != null) {
				// [SEX] 1122334(준비된 정령의 펜던트) 추가
				itemexppendent = (item.getItemId() == 1122334);
				if (itemexppendent) {
					exppen = item;
				}
			}
			if (item2 != null && !itemexppendent) {
				itemexppendent = (item2.getItemId() == 1122334);
				if (itemexppendent) {
					exppen = item2;
				}
			}
			if (itemexppendent) {
				if (kc.getDayt() == player.getKeyValue(27040, "equipday")
						&& kc.getMonth() == player.getKeyValue(27040, "equipmonth")) {
					long runnigtime = player.getKeyValue(27040, "runnigtime");
					int levels = (int) runnigtime / 3600;
					if (levels >= 2) {
						levels = 2;
					}
					int expplus = (levels == 2) ? 30 : ((levels == 1) ? 20 : 10), todaytime = (int) runnigtime / 60;
					long outtime = ((System.currentTimeMillis() - player.getKeyValue(27040, "firstequiptimemil")))
							/ 1000L - player.getKeyValue(27040, "runnigtime");
					player.updateInfoQuest(27039, exppen.getInventoryId() + "="
							+ player.getKeyValue(27040, "firstequiptime") + "|" + nowtime + "|" + outtime + "|0|0");
					player.getClient()
							.send(CWvsContext.SpritPandent(exppen.getPosition(), true, levels, expplus, todaytime));
					player.updateInfoQuest(27039,
							exppen.getInventoryId() + "=" + player.getKeyValue(27040, "firstequiptime") + "|" + nowtime
									+ "|" + outtime + "|" + expplus + "|" + todaytime + "");
				} else {
					player.removeKeyValue(27040);
					player.setKeyValue(27040, "runnigtime", "1");
					player.setKeyValue(27040, "firstequiptime", nowtime);
					player.setKeyValue(27040, "firstequiptimemil", System.currentTimeMillis() + "");
					player.setKeyValue(27040, "equipday", "" + kc.getDayt() + "");
					player.setKeyValue(27040, "equipmonth", "" + kc.getMonth() + "");
					player.updateInfoQuest(27039, exppen.getInventoryId() + "=" + nowtime + "|" + nowtime + "|0|0|0");
					player.getClient().send(CWvsContext.SpritPandent(exppen.getPosition(), true, 0, 10, 0));
					player.updateInfoQuest(27039, exppen.getInventoryId() + "=" + nowtime + "|" + nowtime + "|0|10|0");
				}
			}

			if (player.getV("EnterDay") == null) {
				player.addKV("EnterDay", kc.getYears() + kc.getMonths() + kc.getDays());
			} else {
				player.checkRestDay(false, false);
			}
			if (player.getV("EnterDayWeek") == null) {
				player.addKV("EnterDayWeek", kc.getYears() + kc.getMonths() + kc.getDays());
			} else {
				player.checkRestDay(true, false);
			}
			if (player.getV("EnterDayWeekMonday") == null) {
				player.addKV("EnterDayWeekMonday", kc.getYears() + kc.getMonths() + kc.getDays());
			} else {
				player.checkRestDayMonday();
			}
			if (player.getClient().getKeyValue("EnterDay") == null) {
				player.getClient().setKeyValue("EnterDay", kc.getYears() + kc.getMonths() + kc.getDays());
			} else {
				player.checkRestDay(false, true);
			}
			if (player.getClient().getKeyValue("EnterDayWeek") == null) {
				player.getClient().setKeyValue("EnterDayWeek", kc.getYears() + kc.getMonths() + kc.getDays());
			} else {
				player.checkRestDay(true, true);
			}
			if (player.getClient().getKeyValue("WishCoin") == null) {
				String bosslist = "";
				for (int k = 0; k < ServerConstants.NeoPosList.size(); k++) {
					bosslist = bosslist + "0";
				}
				player.getClient().setKeyValue("WishCoin", bosslist);
				player.getClient().setKeyValue("WishCoinWeekGain", "0");
				player.getClient().setKeyValue("WishCoinGain", "0");
			}

			c.setCabiNet(new ArrayList<MapleCabinet>());
			c.loadCabinet();
			if (!c.getCabiNet().isEmpty()) {
				c.send(CField.getMapleCabinetList(c.getCabiNet(), false, 0, true));
			}

			List<Triple<Integer, Integer, String>> eventInfo = new ArrayList<>();
			eventInfo.add(new Triple<Integer, Integer, String>(Integer.valueOf(100662), Integer.valueOf(993187300),
					"HundredShooting"));
			eventInfo.add(new Triple<Integer, Integer, String>(Integer.valueOf(100661), Integer.valueOf(993074000),
					"jumping"));
			eventInfo.add(new Triple<Integer, Integer, String>(Integer.valueOf(100796), Integer.valueOf(993192500),
					"BloomingRace"));
			eventInfo.add(new Triple<Integer, Integer, String>(Integer.valueOf(100199), Integer.valueOf(993026900),
					"NewYear"));

			c.send(SLFCGPacket.EventInfoPut(eventInfo));

			c.setShopLimit(new ArrayList<MapleShopLimit>());
			c.loadShopLimit();

			player.RefreshUnionRaid(true);

			if (player.getClient().getKeyValue("유니온코인") != null) {
				int coin = Integer.parseInt(player.getClient().getKeyValue("유니온코인"));
				if (coin != player.getKeyValue(500629, "point")) {
					player.setKeyValue(500629, "point", coin + "");
				}
			}

			if (player.getClient().getKeyValue("presetNo") != null) {
				int preset = Integer.parseInt(player.getClient().getKeyValue("presetNo"));
				if (preset != player.getKeyValue(500630, "presetNo")) {
					player.setKeyValue(500630, "presetNo", preset + "");
				}
			}

			for (int i = 3; i < 6; i++) {
				if (player.getClient().getKeyValue("prisetOpen" + i) != null) {
					int preset = Integer.parseInt(player.getClient().getKeyValue("prisetOpen" + i));
					if (preset == 1) {
						player.SetUnionPriset(i);
					}
				}
			}

			for (int i = 0; i < 8; i++) {
				int id = (int) (500627L + player.getKeyValue(500630, "presetNo"));
				if (player.getKeyValue(18791, i + "") == -1L && player.getClient().getCustomData(id, i + "") != null) {
					player.setKeyValue(id, i + "", player.getClient().getCustomData(id, i + ""));
				}
			}

			player.gainEmoticon(1008);
			player.gainEmoticon(1009);
			player.gainEmoticon(1010);

			if (player.getKeyValue(51351, "startquestid") > 0L) {
				player.gainSuddenMission((int) player.getKeyValue(51351, "startquestid"),
						(int) player.getKeyValue(51351, "midquestid"), false);
			}

			if (player.getV("GuildBless") != null) {
				long keys = Long.parseLong(player.getV("GuildBless"));
				Calendar clear = new GregorianCalendar((int) keys / 10000, (int) (keys % 10000L / 100L) - 1,
						(int) keys % 100);
				Calendar ocal = Calendar.getInstance();
				int yeal = clear.get(1), days = clear.get(5), day2 = clear.get(7), maxday = clear.getMaximum(5),
						month = clear.get(2);
				int check = (day2 == 7) ? 2
						: ((day2 == 6) ? 3
								: ((day2 == 5) ? 4
										: ((day2 == 4) ? 5
												: ((day2 == 3) ? 6 : ((day2 == 2) ? 7 : ((day2 == 1) ? 1 : 0))))));
				int afterday = days + check;

				if (afterday > maxday) {
					afterday -= maxday;
					month++;
				}
				if (month > 12) {
					yeal++;
					month = 1;
				}
				Calendar after = new GregorianCalendar(yeal, month, afterday);
				if (after.getTimeInMillis() < System.currentTimeMillis()) {
					MapleQuest quest = MapleQuest.getInstance(26000);
					player.getQuest_Map().remove(quest);
					MapleQuestStatus queststatus = new MapleQuestStatus(quest, 0);
					queststatus.setStatus((byte) 0);
					queststatus.setCustomData("");
					player.getClient().send(CWvsContext.InfoPacket.updateQuest(queststatus));
				}
			}

			if (ServerConstants.feverTime || Calendar.getInstance().get(7) == 7) {
				ServerConstants.feverTime = true;
				player.FeverTime(true, false);
			} else {
				ServerConstants.feverTime = false;
			}

			if (player.getClient().getCustomData(501368, "spoint") != null) {
				int coin = Integer.parseInt(player.getClient().getCustomData(501368, "spoint"));
				if (coin != player.getKeyValue(501368, "point")) {
					player.setKeyValue(501368, "point", coin + "");
				}
			}

			c.getPlayer().loadPremium();

			StringBuilder sb3 = new StringBuilder();
			sb3.append(CurrentTime.getYear());
			sb3.append(StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getMonth()), '0', 2));
			sb3.append(StringUtil.getLeftPaddedStr(String.valueOf(CurrentTime.getDate()), '0', 2));

			if (player.haveItem(2438697)) {
				if (player.getV("d_day_t") == null) {
					player.addKV("d_day_t", "0");
				}
				if (player.getV("d_daycheck") == null) {
					player.addKV("d_daycheck", "0");
				}
				if (Long.parseLong(player.getV("d_day_t")) < Long.parseLong(sb3.toString())) {
					player.addKV("d_day_t", sb3.toString());
					player.addKV("d_daycheck", "" + (Long.parseLong(player.getV("d_daycheck")) + 1L));
				}
			}

			if (GameConstants.isYeti(player.getJob()) || GameConstants.isPinkBean(player.getJob())) {
				MapleQuest quest = MapleQuest.getInstance(7291);
				MapleQuestStatus queststatus = new MapleQuestStatus(quest, 1);
				String skinString = String.valueOf(GameConstants.isPinkBean(player.getJob()) ? 292 : 293);
				queststatus.setCustomData((skinString == null) ? "0" : skinString);
				player.updateQuest(queststatus, true);
				player.setKeyValue(7293, "damage_skin",
						GameConstants.isPinkBean(player.getJob()) ? "2633220" : "2633218");
				if (player.getSkillLevel(80000602) <= 0) {
					player.changeSkillLevel(80000602, (byte) 1, (byte) 1);
				}
			}

			Item weapon = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
			if (weapon != null && player.getBuffedEffect(SecondaryStat.SoulMP) == null) {
				player.setSoulMP((Equip) weapon);
			}

			boolean cgr = false;
			for (MapleGuild mg : World.Guild.getGuilds(1, 999, 1, 999, 1, 999)) {
				if (mg.getRequest(player.getId()) != null) {
					c.getPlayer().setKeyValue(26015, "name", mg.getName());
					cgr = true;
				}
			}
			if (!cgr || c.getPlayer().getGuild() != null) {
				c.getPlayer().setKeyValue(26015, "name", "");
			}
			if (player.getKeyValue(333333, "quick0") > 0L) {
				c.getSession().writeAndFlush(CField.quickSlot(player));
			}
			if (c.getCustomData(252, "count") == null) {
				c.setCustomData(252, "count", "0");
			}
			if (c.getCustomData(252, "T") == null || c.getCustomData(252, "T").equals("0")) {
				c.setCustomData(252, "count", "0");
				c.setCustomData(252, "T", GameConstants.getCurrentFullDate());
			} else {
				String bTime = c.getCustomData(252, "T");
				String cTime = GameConstants.getCurrentFullDate();
				int bH = Integer.parseInt(bTime.substring(8, 10));
				int bM = Integer.parseInt(bTime.substring(10, 12));
				int cH = Integer.parseInt(cTime.substring(8, 10));
				int cM = Integer.parseInt(cTime.substring(10, 12));
				if ((cH - bH == 1 && cM >= bM) || cH - bH > 1) {
					c.setCustomData(252, "count", "3600");
				}
			}

			if (c.getCustomData(253, "day") == null) {
				c.setCustomData(253, "day", "0");
			}

			if (c.getCustomData(253, "complete") == null) {
				c.setCustomData(253, "complete", "0");
			}

			if (c.getCustomData(253, "bMaxDay") == null) {
				c.setCustomData(253, "bMaxDay", "135");
			}

			if (c.getCustomData(253, "cMaxDay") == null) {
				c.setCustomData(253, "cMaxDay", "135");
			}

			if (c.getCustomData(253, "lastDate") == null) {
				c.setCustomData(253, "lastDate", "20/12/31");
			}

			if (c.getCustomData(253, "passCount") == null) {
				c.setCustomData(253, "passCount", "135");
			}

			if (player.getKeyValue(0, "Boss_Level") < 0) {
				player.setKeyValue(0, "Boss_Level", "0");
			}

			if (player.getKeyValue(100, "medal") < 0) {
				player.setKeyValue(100, "medal", "1");
			}

			if (player.getKeyValue(100, "title") < 0) {
				player.setKeyValue(100, "title", "1");
			}

			player.updateInfoQuest(101149,
					"1007=" + player.getKeyValue(100, "medal") + ";1009=" + player.getKeyValue(100, "title"));

			if (player.getKeyValue(3, "dojo") < 0) {
				player.setKeyValue(3, "dojo", "0");
			}

			if (player.getKeyValue(3, "dojo_time") < 0) {
				player.setKeyValue(3, "dojo_time", "0");
			}

			if (c.getCustomData(254, "passDate") == null) {
				StringBuilder str = new StringBuilder();
				for (int k = 0; k < 63; k++) {
					str.append("0");
				}
				c.setCustomData(254, "passDate", str.toString());
			}

			MatrixHandler.calcSkillLevel(player, -1);

			int linkSkill = GameConstants.getMyLinkSkill(player.getJob());

			if (linkSkill > 0 && player.getSkillLevel(linkSkill) != ((player.getLevel() >= 120) ? 2 : 1)) {
				player.changeSkillLevel(linkSkill, (byte) ((player.getLevel() >= 120) ? 2 : 1), (byte) 2);
			}

			if (c.getCustomData(238, "T") == null || c.getCustomData(238, "T").equals("0")) {
				c.setCustomData(238, "count", "0");
				c.setCustomData(238, "T", GameConstants.getCurrentFullDate());
			}

			if (GameConstants.isWildHunter(player.getJob())) {
				boolean change = false;
				for (int a = 9304000; a <= 9304008; a++) {
					int jaguarid = GameConstants.getJaguarType(a);
					String info = player.getInfoQuest(23008);

					for (int k = 0; k <= 8; k++) {
						if (!info.contains(k + "=1")) {
							if (k == jaguarid) {
								info = info + k + "=1;";
							}
						}
					}
					player.updateInfoQuest(23008, info);
					player.updateInfoQuest(123456, String.valueOf(jaguarid * 10));
					change = true;
				}
				if (change) {
					c.getSession().writeAndFlush(CWvsContext.updateJaguar(player));
				}
				if (player.getKeyValue(190823, "grade") == 0) {
					player.setKeyValue(190823, "grade", "1");
				}
			}

			MapleQuestStatus stat = player.getQuestNoAdd(MapleQuest.getInstance(122700));

			c.getSession().writeAndFlush(CWvsContext.pendantSlot(true));
			c.getSession().writeAndFlush(CWvsContext.temporaryStats_Reset());

			// 주중 이벤트
			LocalDate currentDate = LocalDate.now();
			DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
			String text = "<今日週間加成>\r\n\r\n";
			switch (currentDayOfWeek) {
			case MONDAY:
				text += "道具掉率增加15%！" + "\r\n經驗值獲得率增加25%！" + "\r\n星力強化折扣5%！" + "\r\n金幣獲得率增加25%！";
				break;
			case TUESDAY:
				text += "道具掉率增加15%！" + "\r\n經驗值獲得率增加10%！" + "\r\n星力強化折扣5%！" + "\r\n金幣獲得率增加15%！";
				break;
			case WEDNESDAY:
				text += "道具掉率增加15%！" + "\r\n經驗值獲得率增加15%！" + "\r\n星力強化折扣5%！" + "\r\n金幣獲得率增加10%！";
				break;
			case THURSDAY:
				text += "道具掉率增加15%！" + "\r\n經驗值獲得率增加10%！" + "\r\n星力強化折扣5%！" + "\r\n金幣獲得率增加15%！";
				break;
			case FRIDAY:
				text += "道具掉率增加15%！" + "\r\n經驗值獲得率增加20%！" + "\r\n星力強化折扣7%！" + "\r\n金幣獲得率增加20%！";
				break;
			case SATURDAY:
				text += "道具掉率增加20%！" + "\r\n經驗值獲得率增加30%！" + "\r\n星力強化折扣5%！" + "\r\n金幣獲得率增加30%！";
				break;
			case SUNDAY:
				text += "道具掉率增加25%！" + "\r\n經驗值獲得率增加40%！" + "\r\n星力強化折扣10%！" + "\r\n金幣獲得率增加40%！";
				break;
			}
			c.send(SLFCGPacket.BlackLabel(text, 50, 5000, 4, 0, -200, 1, 4));

			player.getMap().addPlayer(player);

			c.getSession().writeAndFlush(CWvsContext.setBossReward(player));
			c.getSession().writeAndFlush(CWvsContext.onSessionValue("kill_count", "0"));
			c.getSession().writeAndFlush(CWvsContext.updateDailyGift("count=" + c.getKeyValue("dailyGiftComplete")
					+ ";day=" + c.getKeyValue("dailyGiftDay") + ";date=" + c.getPlayer().getKeyValue(16700, "date")));
			c.getSession().writeAndFlush(CField.dailyGift(player, 1, 0));

			try {
				int[] buddyIds = player.getBuddylist().getBuddyIds();
				World.Buddy.loggedOn(player.getName(), player.getId(), c.getChannel(), c.getAccID(), buddyIds);
				if (player.getParty() != null) {
					MapleParty party = player.getParty();
					World.Party.updateParty(party.getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));
				}

				AccountIdChannelPair[] onlineBuddies = World.Find.multiBuddyFind(player.getBuddylist(), player.getId(),
						buddyIds);

				for (AccountIdChannelPair onlineBuddy : onlineBuddies) {
					player.getBuddylist().get(onlineBuddy.getAcountId()).setChannel(onlineBuddy.getChannel());
				}

				player.getBuddylist().setChanged(true);
				c.getSession().writeAndFlush(CWvsContext.BuddylistPacket
						.updateBuddylist(player.getBuddylist().getBuddies(), null, (byte) 21));

				MapleMessenger messenger = player.getMessenger();

				if (messenger != null) {
					World.Messenger.silentJoinMessenger(messenger.getId(), new MapleMessengerCharacter(c.getPlayer()));
					World.Messenger.updateMessenger(messenger.getId(), c.getPlayer().getName(), c.getChannel());
				}

				if (player.getGuildId() > 0) {
					MapleGuild gs = World.Guild.getGuild(player.getGuildId());

					if (gs != null) {
						if (gs.getLastResetDay() == 0) {
							gs.setLastResetDay(Integer.parseInt(kc.getYears() + kc.getMonths() + kc.getDays()));
						}

						World.Guild.setGuildMemberOnline(player.getMGC(), true, c.getChannel());

						/*
						 * List<byte[]> packetList = World.Alliance.getAllianceInfo(gs.getAllianceId(),
						 * true); if (packetList != null) { for (byte[] pack : packetList) { if (pack !=
						 * null) { c.getSession().writeAndFlush(pack); } } }
						 */
					} else {
						player.setGuildId(0);
						player.setGuildRank((byte) 5);
						player.setAllianceRank((byte) 5);
						player.saveGuildStatus();
					}
				}

				c.getSession().writeAndFlush(CWvsContext.GuildPacket.guildLoad1(player));
				c.getSession().writeAndFlush(CWvsContext.GuildPacket.guildLoadAattendance());
				c.getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildInfo(player));
				c.getSession().writeAndFlush(CWvsContext.GuildPacket.newGuildHash());
			} catch (Exception e) {
				e.printStackTrace();
			}

			CharacterNameAndId pendingBuddyRequest = player.getBuddylist().pollPendingRequest();

			if (pendingBuddyRequest != null) {
				player.getBuddylist()
						.put(new BuddylistEntry(pendingBuddyRequest.getName(), pendingBuddyRequest.getRepName(),
								pendingBuddyRequest.getAccId(), pendingBuddyRequest.getId(),
								pendingBuddyRequest.getGroupName(), -1, false, pendingBuddyRequest.getLevel(),
								pendingBuddyRequest.getJob(), pendingBuddyRequest.getMemo()));
				c.getSession()
						.writeAndFlush(CWvsContext.BuddylistPacket.requestBuddylistAdd(pendingBuddyRequest.getId(),
								pendingBuddyRequest.getAccId(), pendingBuddyRequest.getName(),
								pendingBuddyRequest.getLevel(), pendingBuddyRequest.getJob(), c,
								pendingBuddyRequest.getGroupName(), pendingBuddyRequest.getMemo()));
			}

			player.getClient().getSession()
					.writeAndFlush(CWvsContext.serverMessage("", channelServer.getServerMessage()));
			player.sendMacros();
			player.updatePartyMemberHP();
			player.startFairySchedule(false);
			player.gainDonationSkills();
			c.getSession().writeAndFlush(CField.getKeymap(player.getKeyLayout(), c.getPlayer()));
			c.getSession().writeAndFlush(CWvsContext.OnClaimSvrStatusChanged(true));
			player.updatePetAuto();
			player.expirationTask(true, (transfer == null));
			c.getSession().writeAndFlush(CWvsContext.setUnion(c));

			player.getStat().recalcLocalStats(player);

			for (int j = 0; j < 5; j++) {
				c.getSession().writeAndFlush(CWvsContext.unionFreeset(c, j));
			}

			c.getPlayer().updateSingleStat(MapleStat.FATIGUE, c.getPlayer().getFatigue());

			if ((player.getStat()).equippedSummon > 0) {
				SkillFactory.getSkill((player.getStat()).equippedSummon).getEffect(1).applyTo(player, true);
			}

			c.getSession().writeAndFlush(CField.HeadTitle(player.HeadTitle()));

			PetHandler.updatePetSkills(player, null);

			c.getSession().writeAndFlush(CWvsContext.initSecurity());
			c.getSession().writeAndFlush(CWvsContext.updateSecurity());

			String towerchair = c.getPlayer().getInfoQuest(7266);
			if (!towerchair.equals("")) {
				c.getPlayer().updateInfoQuest(7266, towerchair);
			}
			c.getSession().writeAndFlush(CField.getGameMessage(5, "多項活動進行中，請查看粉絲Discord頻道。"));
			c.getSession().writeAndFlush(CField.getGameMessage(8, "Fancy伺服器目前正在進行1.5倍經驗值狂歡時間！"));
			c.getSession().writeAndFlush(CField.getGameMessage(6, "請遵守Fancy的使用規則進行遊戲，請保持禮貌遊玩。"));

			// 這個數據包會導致地圖限制（卡頓/加載異常）
			// c.getSession().writeAndFlush(SLFCGPacket.StarDustUI(501661,
			// "UI/UIWindowEvent.img/starDust_chuchu", player.getKeyValue(501661, "e"),
			// c.getPlayer().getKeyValue(501661, "point")));
			if (player.getClient().isFirstLogin() && !player.isGM() && !player.getName().equals("\uc624\ube0c")) {
				player.getClient().setLogin(false);
			}

			if (c.getPlayer().getPremiumPeriod().longValue() > LocalDateTime.now().toInstant(ZoneOffset.UTC)
					.getEpochSecond()) {
				Skill skill = SkillFactory.getSkill(c.getPlayer().getPremiumBuff());
				if (skill != null) {
					if (player.getSkillLevel(skill) < 1) {
						c.getPlayer().changeSingleSkillLevel(skill, skill.getMaxLevel(), (byte) skill.getMasterLevel());
					}
					long td = (c.getPlayer().getPremiumPeriod().longValue()
							- LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond()) / 86400000L;
					c.getPlayer().dropMessage(5, "프리미엄 버프가 " + td + "일 남았습니다.");
					SkillFactory.getSkill(c.getPlayer().getPremiumBuff()).getEffect(1).applyTo(player, false);
				}
			}

			if (player.getKeyValue(501045, "point") == -1) {
				player.setKeyValue(501045, "point", "0");
				player.setKeyValue(501045, "lv", "1");
				player.setKeyValue(501045, "sp", "0");
				player.setKeyValue(501045, "reward0", "0");
				player.setKeyValue(501045, "reward1", "0");
				player.setKeyValue(501045, "reward2", "0");
				player.setKeyValue(501045, "mapTuto", "2");
				player.setKeyValue(501045, "skillTuto", "1");
				player.setKeyValue(501045, "payTuto", "1");
			}

			if (player.getKeyValue(501092, "point") == -1) {
				player.setKeyValue(501092, "lv", "9");
				player.setKeyValue(501092, "point", "0");
				player.setKeyValue(501092, "sp", "0");
				player.setKeyValue(501092, "mapTuto", "2");
				player.setKeyValue(501092, "skillTuto", "1");
				player.setKeyValue(501092, "payTuto", "1");
			}
			player.setKeyValue(501092, "lv", "9");
			player.setKeyValue(501092, "point", "" + ChannelServer.getInstance(1).getFireWorks().getSuns());

			for (int sm = 0; sm <= 7; sm++) {
				if (c.getPlayer().getKeyValue(2018207, "medalSkill_" + sm) == 1L) {
					Skill skill = SkillFactory.getSkill(80001535 + sm);
					if (skill != null) {
						if (player.getSkillLevel(skill) < 1) {
							c.getPlayer().changeSingleSkillLevel(skill, skill.getMaxLevel(),
									(byte) skill.getMasterLevel());
						}
						SkillFactory.getSkill(80001535 + sm).getEffect(1).applyTo(player, false);
					}
				}
			}

			int[][] medals = { { 1143507, 80001543 }, { 1143508, 80001544 }, { 1143509, 80001545 } };

			for (int m = 0; m < medals.length; m++) {
				if (c.getPlayer().haveItem(medals[m][0])) {
					Skill skill = SkillFactory.getSkill(medals[m][1]);
					if (skill != null) {
						if (player.getSkillLevel(skill) < 1) {
							c.getPlayer().changeSingleSkillLevel(skill, skill.getMaxLevel(),
									(byte) skill.getMasterLevel());
						}
						SkillFactory.getSkill(medals[m][1]).getEffect(1).applyTo(player, false);
					}
				}
			}
			player.changeSkillLevel(80003023, (byte) 1, (byte) 1);
			if (c.getPlayer().getKeyValue(20210113, "orgelonoff") == 1) {
				c.getPlayer().setKeyValue(20210113, "orgelonoff", "0");
				c.getPlayer().updateInfoQuest(100720, "count=0;fever=0;");
			}
			// player.getClient().send(CField.UIPacket.closeUI(3));
			// c.getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false,
			// false, false));
			// c.getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true,
			// false, false));

			if (ServerConstants.ServerTest) {
				// c.send(CField.NPCPacket.getNPCTalk(9010061, (byte) 0, "#e루나스토리에 오신 것을
				// 환영합니다#n\r\n이 곳은 서버 정식 오픈 전에 각종 버그, 오류들을 미리 테스트 하기 위해 만들어 진 곳이며 다음과 같은 사항이
				// 제한됩니다.\r\n\r\n#b1. 테스트 월드에서는 테스트를 위하여 캐릭터 성장이 쉽게 설정되어있습니다. 이러한 모든 정보는 정식 월드에는
				// 적용되지 않습니다.\r\n2. 테스트 월드의 캐릭터 정보는 정식 월드와 절대 공유되지 않습니다.\r\n3. 테스트 월드의 캐릭터 정보는
				// 이용자의 동의를 받지 않고 아무런 공지없이 리셋될 수 있습니다.\r\n4. 테스트 월드의 서비스가 아무런 공지없이 중단될 수
				// 있습니다.\r\n5. 이 곳에서 테스트 된 내용이 정식 월드에 반드시 적용되지는 않습니다.\r\n6. 테스트 된 내용이 정식 월드에 추가될
				// 때에는 세부 수치가 변경될 수 있습니다.", "00 00", (byte) 0, c.getPlayer().getId()));
				if (!player.haveItem(2431138)) {
					player.gainItem(2431138, 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void ChangeChannel(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr, boolean room) {
		try {
			if (chr == null || chr.getEventInstance() != null || chr.getMap() == null
					|| FieldLimitType.ChannelSwitch.check(chr.getMap().getFieldLimit())) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (World.getPendingCharacterSize() >= 10) {
				chr.dropMessage(1, "頻道移動中的人很多。 請稍後再試.");
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			int chc = slea.readByte() + 1;
			int mapid = 0;
			if (room) {
				mapid = slea.readInt();
			}
			slea.readInt();
			if (!World.isChannelAvailable(chc)) {
				chr.dropMessage(1, "現時該頻道擁擠，無法移動.");
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (room && (mapid < 910000001 || mapid > 910000022)) {
				chr.dropMessage(1, "現時該頻道擁擠，無法移動.");
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (room) {
				if (chr.getMapId() == mapid) {
					if (c.getChannel() == chc) {
						c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					} else {
						chr.changeChannel(chc);
					}
				} else {
					if (c.getChannel() != chc) {
						chr.changeChannel(chc);
					}
					MapleMap warpz = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(mapid);
					if (warpz != null) {
						chr.changeMap(warpz, warpz.getPortal("out00"));
					} else {
						chr.dropMessage(1, "現時該頻道擁擠，無法移動.");
						c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					}
				}
			} else {
				chr.changeChannel(chc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getGameQuitRequest(RecvPacketOpcode header, LittleEndianAccessor rh, MapleClient c) {
		String account = null;
		if (header == RecvPacketOpcode.GAME_EXIT) {
			rh.skip(8);
		} else {
			account = rh.readMapleAsciiString();
		}
		if (account == null || account.equals("")) {
			account = c.getAccountName();
		}
		if (c == null) {
			return;
		}
		if (account == null || header == RecvPacketOpcode.GAME_EXIT) {
			c.disconnect(true, false, false);
			c.getSession().close();
			return;
		}
		if (!c.isLoggedIn() && !c.getAccountName().equals(account)) {
			c.disconnect(true, false, false);
			c.getSession().close();
			return;
		}
		c.disconnect(true, false, false);
		c.getSession().writeAndFlush(LoginPacket.getKeyGuardResponse(account + "," + c.getPassword(account)));
	}

	public static void sendOpcodeEncryption(MapleClient c) {
		c.mEncryptedOpcode.clear();

		byte[] aKey = new byte[24];

		String key = "G0dD@mnN#H@ckEr!";

		for (int i = 0; i < key.length(); i++) {
			aKey[i] = (byte) key.charAt(i);
		}

		System.arraycopy(aKey, 0, aKey, 16, 8);

		int startOpcode = 208;
		int opcodeSize = 1430;
		int endOpcode = (startOpcode + opcodeSize);

		String sOpcode = opcodeSize + "";

		List<Integer> aUsed = new ArrayList<>();

		for (int i = startOpcode; i < endOpcode; i++) {
			int nNum = Randomizer.rand(startOpcode, 99999);

			while (aUsed.contains(nNum)) {
				nNum = Randomizer.rand(startOpcode, 99999);
			}

			String sNum = String.format("%d", nNum);

			c.mEncryptedOpcode.put(nNum, i);

			aUsed.add(nNum);

			sOpcode += ("|" + sNum);
		}

		aUsed.clear();

		TripleDESCipher pCipher = new TripleDESCipher(aKey);

		try {
			byte[] aEncrypt = pCipher.Encrypt(sOpcode.getBytes());

			c.getSession().writeAndFlush(LoginPacket.onOpcodeEncryption(opcodeSize, aEncrypt));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

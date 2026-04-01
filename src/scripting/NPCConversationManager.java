package scripting;

import client.*;
import client.inventory.*;
import constants.GameConstants;
import constants.KoreaCalendar;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.channel.handler.InterServerHandler;
import handling.channel.handler.PlayersHandler;
import handling.login.LoginInformationProvider;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import handling.world.guild.MapleGuild;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.*;
import server.Timer;
import server.enchant.EnchantFlag;
import server.enchant.EquipmentEnchant;
import server.enchant.StarForceStats;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MonsterDropEntry;
import server.maps.Event_DojoAgent;
import server.maps.MapleMap;
import server.marriage.MarriageDataEntry;
import server.quest.MapleQuest;
import server.quest.party.MapleNettPyramid;
import server.shops.MapleShopFactory;
import server.shops.MapleShopItem;
import tools.FileoutputUtil;
import tools.Pair;
import tools.StringUtil;
import tools.Triple;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.PacketHelper;
import tools.packet.SLFCGPacket;

import javax.script.Invocable;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;

public class NPCConversationManager extends AbstractPlayerInteraction {

	private String getText;
	private byte lastMsg = -1;
	private String script;
	private byte type;
	public boolean pendingDisposal = false;
	private Invocable iv;

	public NPCConversationManager(MapleClient c, int npc, int questid, byte type, Invocable iv, String script) {
		super(c, npc, questid);
		this.type = type;
		this.iv = iv;
		this.script = script;
	}

	public void sendConductExchange(String text) {
		if (this.lastMsg > -1) {
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCConductExchangeTalk(this.id, text));
		this.lastMsg = 0;
	}

	public void sendPacket(short a, String b) {
		this.c.getSession().writeAndFlush(SLFCGPacket.SendPacket(a, b));
	}

	public Invocable getIv() {
		return this.iv;
	}

	public String getScript() {
		return this.script;
	}

	public int getNpc() {
		return this.id;
	}

	public int getQuest() {
		return this.id2;
	}

	public byte getType() {
		return this.type;
	}

	public void safeDispose() {
		this.pendingDisposal = true;
	}

	public void dispose() {
		NPCScriptManager.getInstance().dispose(this.c);
	}

	public void askMapSelection(String sel) {
		if (this.lastMsg > -1) {
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getMapSelection(this.id, sel));
		this.lastMsg = 17;
	}

	public void sendNext(String text) {
		sendNext(text, this.id);
	}

	public void sendNext(String text, int id) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 0, text, "00 01", (byte) 0));
		this.lastMsg = 0;
	}

	public void sendPlayerToNpc(String text) {
		sendNextS(text, (byte) 3, this.id);
	}

	public void StartSpiritSavior() {
	}

	public void StartBlockGame() {
		MapleClient c = getClient();
		c.getSession().writeAndFlush(CField.onUserTeleport(c.getPlayer(), 65535, 0));
		c.getSession().writeAndFlush(CField.UIPacket.IntroDisableUI(true));
		c.getSession().writeAndFlush(CField.UIPacket.IntroLock(true));

		Timer.EventTimer.getInstance().schedule(() -> {
			c.getSession().writeAndFlush(SLFCGPacket.BlockGameCommandPacket(1));
			c.getSession().writeAndFlush(SLFCGPacket.BlockGameCommandPacket(2));
			c.getSession().writeAndFlush(SLFCGPacket.BlockGameControlPacket(100, 10));
		}, 2000L);
	}

	public boolean setZodiacGrade(int grade) {
		if (c.getPlayer().getKeyValue(190823, "grade") >= grade) {
			return false;
		}
		c.getPlayer().setKeyValue(190823, "grade", String.valueOf(grade));
		c.getPlayer().getMap().broadcastMessage(c.getPlayer(), SLFCGPacket.ZodiacRankInfo(c.getPlayer().getId(), grade),
				true);
		c.getSession().writeAndFlush(SLFCGPacket.playSE("Sound/MiniGame.img/Result_Yut"));
		showEffect(false, "Effect/CharacterEff.img/gloryonGradeup");
		return true;
	}

	public void sendNextNoESC(String text) {
		sendNextS(text, (byte) 1, this.id);
	}

	public void sendNextNoESC(String text, int id) {
		sendNextS(text, (byte) 1, id);
	}

	public void sendNextS(String text, byte type) {
		sendNextS(text, type, 0);
	}

	public void sendNextS(String text, byte type, int id, int idd) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimpleS(text, type);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 0, text, "00 01", type, idd));
		this.lastMsg = 0;
	}

	public void sendNextS(String text, byte type, int idd) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimpleS(text, type);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 0, text, "00 01", type, idd));
		this.lastMsg = 0;
	}

	public void sendCustom(String text, int type, int idd) {
		if (this.lastMsg > -1) {
			return;
		}
		this.c.getSession()
				.writeAndFlush(CField.NPCPacket.getNPCTalks(this.id, (byte) 0, text, "00 01", (byte) type, idd));
		this.lastMsg = 0;
	}

	public void sendPrev(String text) {
		sendPrev(text, this.id);
	}

	public void sendPrev(String text, int id) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 0, text, "01 00", (byte) 0));
		this.lastMsg = 0;
	}

	public void sendPrevS(String text, byte type) {
		sendPrevS(text, type, 0);
	}

	public void sendPrevS(String text, byte type, int idd) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimpleS(text, type);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 0, text, "01 00", type, idd));
		this.lastMsg = 0;
	}

	public void sendNextPrev(String text) {
		sendNextPrev(text, this.id);
	}

	public void sendNextPrev(String text, int id) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 0, text, "01 01", (byte) 0));
		this.lastMsg = 0;
	}

	public void EnterCS() {
		InterServerHandler.EnterCS(this.c, this.c.getPlayer(), false);
	}

	public void PlayerToNpc(String text) {
		sendNextPrevS(text, (byte) 3);
	}

	public void sendNextPrevS(String text) {
		sendNextPrevS(text, (byte) 3);
	}

	public String getMobName(int mobid) {
		MapleData data = null;
		MapleDataProvider dataProvider = MapleDataProviderFactory
				.getDataProvider(new File(System.getProperty("wz") + "/String.wz"));
		String ret = "";
		List<String> retMobs = new ArrayList<>();
		data = dataProvider.getData("Mob.img");
		List<Pair<Integer, String>> mobPairList = new LinkedList<>();
		for (MapleData mobIdData : data.getChildren()) {
			mobPairList.add(new Pair(Integer.valueOf(Integer.parseInt(mobIdData.getName())),
					MapleDataTool.getString(mobIdData.getChildByPath("name"), "NO-NAME")));
		}
		for (Pair<Integer, String> mobPair : mobPairList) {
			if (((Integer) mobPair.getLeft()).intValue() == mobid) {
				ret = (String) mobPair.getRight();
			}
		}
		return ret;
	}

	public void sendNextPrevS(String text, byte type) {
		sendNextPrevS(text, type, 0);
	}

	public void sendNextPrevS(String text, byte type, int id, int idd) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimpleS(text, type);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 0, text, "01 01", type, idd));
		this.lastMsg = 0;
	}

	public void sendNextPrevS(String text, byte type, int idd) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimpleS(text, type);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 0, text, "01 01", type, idd));
		this.lastMsg = 0;
	}

	public void sendDimensionGate(String text) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 19, text, "00 00", (byte) 0));
		this.lastMsg = 0;
	}

	public void sendOk(String text) {
		sendOk(text, this.id);
	}

	public void sendOk(String text, int id) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 0, text, "00 00", (byte) 0));
		this.lastMsg = 0;
	}

	public void sendOkS(String text, byte type) {
		sendOkS(text, type, 0);
	}

	public void sendOkS(String text, byte type, int idd) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimpleS(text, type);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(idd, (byte) 0, text, "00 00", type, idd));
		this.lastMsg = 0;
	}

	public void sendYesNo(String text) {
		sendYesNo(text, this.id);
	}

	public void sendYesNo(String text, int id) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 3, text, "", (byte) 0));
		this.lastMsg = 2;
	}

	public void sendYesNoS(String text, byte type) {
		sendYesNoS(text, type, 0);
	}

	public void sendYesNoS(String text, byte type, int idd) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimpleS(text, type);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 3, text, "", type, idd));
		this.lastMsg = 2;
	}

	public void sendAcceptDecline(String text) {
		askAcceptDecline(text);
	}

	public void sendAcceptDeclineNoESC(String text) {
		askAcceptDeclineNoESC(text);
	}

	public void askAcceptDecline(String text) {
		askAcceptDecline(text, this.id);
	}

	public void askAcceptDecline(String text, int id) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.lastMsg = 16;
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, this.lastMsg, text, "", (byte) 0));
	}

	public void askPraticeReplace(String text) {
		askPraticeReplace(text, this.id);
	}

	public void askPraticeReplace(String text, int id) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.lastMsg = 3;
		this.c.getSession().writeAndFlush(CField.NPCPacket.getPraticeReplace(id, this.lastMsg, text, "", (byte) 0, 1));
	}

	public void askAcceptDeclineNoESC(String text) {
		askAcceptDeclineNoESC(text, this.id);
	}

	public void askAcceptDeclineNoESC(String text, int id) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.lastMsg = 16;
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, this.lastMsg, text, "", (byte) 1));
	}

	public void sendStyle(String text, int... args) {
		askAvatar(text, args);
	}

	public void askCustomMixHairAndProb(String text) {
		this.c.getSession()
				.writeAndFlush(CField.NPCPacket.getNPCTalkMixStyle(this.id, text,
						GameConstants.isZero(this.c.getPlayer().getJob()) ? ((this.c.getPlayer().getGender() == 1))
								: false,
						GameConstants.isAngelicBuster(this.c.getPlayer().getJob()) ? (this.c.getPlayer().getDressup())
								: false));
		this.lastMsg = 44;
	}

	public void askAvatar(String text, int... args) {
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkStyle(this.c.getPlayer(), this.id, text, args));
		this.lastMsg = 10;
	}

	public void askAvatar(String text, int[] args1, int[] args2) {
		if (this.lastMsg > -1) {
			return;
		}
		if (GameConstants.isZero(this.c.getPlayer().getJob())) {
			this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkStyleZero(this.id, text, args1, args2));
		} else {
			this.c.getSession()
					.writeAndFlush(CField.NPCPacket.getNPCTalkStyle(this.c.getPlayer(), this.id, text, args1));
		}
		this.lastMsg = 10;
	}

	public void askCoupon(int itemid, int... args) {
		this.c.send(CWvsContext.UseMakeUpCoupon(this.c.getPlayer(), itemid, args));
	}

	public void askAvatarAndroid(String text, int... args) {
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkStyleAndroid(this.id, text, args));
	}

	public void sendSimple(String text) {
		sendSimple(text, this.id);
	}

	public void sendSimple(String text, int id) {
		if (this.lastMsg > -1) {
			return;
		}

		if (!text.contains("#L")) {
			sendNext(text);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 6, text, "", (byte) 0));
		this.lastMsg = 5;
	}

	public void sendSimpleS(String text, byte type) {
		sendSimpleS(text, type, 0);
	}

	public void sendSimpleS(String text, byte type, int idd) {
		if (this.lastMsg > -1) {
			return;
		}
		if (!text.contains("#L")) {
			sendNextS(text, type);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 6, text, "", type, idd));
		this.lastMsg = 5;
	}

	public void sendSimpleS(String text, byte type, int id, int idd) {
		if (this.lastMsg > -1) {
			return;
		}
		if (!text.contains("#L")) {
			sendNextS(text, type);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(id, (byte) 6, text, "", type, idd));
		this.lastMsg = 5;
	}

	public void sendStyle(String text, int[] styles1, int[] styles2) {
		if (this.lastMsg > -1) {
			return;
		}
		if (GameConstants.isZero(this.c.getPlayer().getJob())) {
			this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkStyleZero(this.id, text, styles1, styles2));
			this.lastMsg = 32;
		} else {
			this.c.getSession()
					.writeAndFlush(CField.NPCPacket.getNPCTalkStyle(this.c.getPlayer(), this.id, text, styles1));
			this.lastMsg = 10;
		}
	}

	public void sendIllustYesNo(String text, int face, boolean isLeft) {
		if (lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendIllustSimple(text, face, isLeft);
			return;
		}
		c.getSession()
				.writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 28, text, "", (byte) 0, face, true, isLeft));
		lastMsg = 28;
	}

	public void sendIllustSimple(String text, int face, boolean isLeft) {
		if (lastMsg > -1) {
			return;
		}
		if (!text.contains("#L")) {
			sendIllustNext(text, face, isLeft);
			return;
		}
		c.getSession()
				.writeAndFlush(CField.NPCPacket.getNPCTalk(this.id, (byte) 30, text, "", (byte) 0, face, true, isLeft));
		lastMsg = 30;
	}

	public void sendIllustNext(String text, int face, boolean isLeft) {
		if (lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendIllustSimple(text, face, isLeft);
			return;
		}
		c.getSession().writeAndFlush(
				CField.NPCPacket.getNPCTalk(this.id, (byte) 26, text, "00 01", (byte) 0, face, true, isLeft));
		lastMsg = 26;
	}

	public void sendIllustPrev(String text, int face, boolean isLeft) {
		if (lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendIllustSimple(text, face, isLeft);
			return;
		}
		c.getSession().writeAndFlush(
				CField.NPCPacket.getNPCTalk(this.id, (byte) 26, text, "01 00", (byte) 0, face, true, isLeft));
		lastMsg = 26;
	}

	public void sendIllustNextPrev(String text, int face, boolean isLeft) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendIllustSimple(text, face, isLeft);
			return;
		}
		this.c.getSession().writeAndFlush(
				CField.NPCPacket.getNPCTalk(this.id, (byte) 26, text, "01 01", (byte) 0, face, true, isLeft));
		this.lastMsg = 26;
	}

	public void sendIllustOk(String text, int face, boolean isLeft) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendIllustSimple(text, face, isLeft);
			return;
		}
		this.c.getSession().writeAndFlush(
				CField.NPCPacket.getNPCTalk(this.id, (byte) 26, text, "00 00", (byte) 0, face, true, isLeft));
		this.lastMsg = 26;
	}

	public void sendGetNumber(int id, String text, int def, int min, int max) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkNum(id, text, def, min, max));
		this.lastMsg = 4;
	}

	public void sendFriendsYesNo(String text, int id) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.c.send(CField.NPCPacket.getNPCTalk(id, (byte) 3, text, "", (byte) 36));
		this.lastMsg = 3;
	}

	public void sendGetNumber(String text, int def, int min, int max) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkNum(this.id, text, def, min, max));
		this.lastMsg = 4;
	}

	public void sendGetText(String text) {
		sendGetText(text, this.id);
	}

	public void sendGetText(String text, int id) {
		if (this.lastMsg > -1) {
			return;
		}
		if (text.contains("#L")) {
			sendSimple(text);
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkText(id, text));
		this.lastMsg = 3;
	}

	public void setGetText(String text) {
		this.getText = text;
	}

	public String getText() {
		return this.getText;
	}

	public void setZeroSecondHair(int hair) {
		getPlayer().setSecondHair(hair);
		getPlayer().updateZeroStats();
		getPlayer().equipChanged();
	}

	public void setZeroSecondFace(int face) {
		getPlayer().setSecondFace(face);
		getPlayer().updateZeroStats();
		getPlayer().equipChanged();
	}

	public void setZeroSecondSkin(int color) {
		getPlayer().setSecondSkinColor((byte) color);
		getPlayer().updateZeroStats();
		getPlayer().equipChanged();
	}

	public void setAngelicSecondHair(int hair) {
		getPlayer().setSecondHair(hair);
		getPlayer().updateAngelicStats();
		getPlayer().equipChanged();
	}

	public void setAngelicSecondFace(int face) {
		getPlayer().setSecondFace(face);
		getPlayer().updateAngelicStats();
		getPlayer().equipChanged();
	}

	public void setAngelicSecondSkin(int color) {
		getPlayer().setSecondSkinColor((byte) color);
		getPlayer().updateAngelicStats();
		getPlayer().equipChanged();
	}

	public void setHair(int hair) {
		getPlayer().setHair(hair);
		getPlayer().updateSingleStat(MapleStat.HAIR, hair);
		getPlayer().equipChanged();
	}

	public void setFace(int face) {
		getPlayer().setFace(face);
		getPlayer().updateSingleStat(MapleStat.FACE, face);
		getPlayer().equipChanged();
	}

	public void setSkin(int color) {
		getPlayer().setSkinColor((byte) color);
		getPlayer().updateSingleStat(MapleStat.SKIN, color);
		getPlayer().equipChanged();
	}

	public int setRandomAvatar(int ticket, int... args_all) {
		gainItem(ticket, (short) -1);

		int args = args_all[Randomizer.nextInt(args_all.length)];
		if (args < 100) {
			c.getPlayer().setSkinColor((byte) args);
			c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
		} else if (args < 30000) {
			c.getPlayer().setFace(args);
			c.getPlayer().updateSingleStat(MapleStat.FACE, args);
		} else {
			c.getPlayer().setHair(args);
			c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
		}
		c.getPlayer().equipChanged();
		return 1;
	}

	public static Map<Integer, String> hairlist = new HashMap<>();
	public static Map<Integer, String> facelist = new HashMap<>();

	public int setAvatar(int ticket, int args) {
		if (hairlist.isEmpty() || facelist.isEmpty()) {
			for (Pair<Integer, String> itemPair : (Iterable<Pair<Integer, String>>) MapleItemInformationProvider
					.getInstance().getAllItems()) {
				if (((String) itemPair.getRight()).toLowerCase().contains("헤어")
						|| ((String) itemPair.getRight()).toLowerCase().contains("머리")) {
					hairlist.put(itemPair.getLeft(), itemPair.getRight());
					continue;
				}
				if (((String) itemPair.getRight()).toLowerCase().contains("얼굴")) {
					facelist.put(itemPair.getLeft(), itemPair.getRight());
				}
			}
		}

		int mixranze = 0;

		if (args > 99999) {
			mixranze = args % 1000;
			args /= 1000;
		}
		if (hairlist.containsKey(Integer.valueOf(args))) {
			if (c.getPlayer().getDressup() == true) {
				setAngelicSecondHair(args);
			} else {
				c.getPlayer().setHair(args);
				c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
			}
		} else if (facelist.containsKey(Integer.valueOf(args))) {
			if (mixranze > 0) {
				String sum = args + "" + mixranze;
				args = Integer.parseInt(sum);
			}
			if (c.getPlayer().getDressup() == true) {
				setAngelicSecondFace(args);
			} else {
				c.getPlayer().setFace(args);
				c.getPlayer().updateSingleStat(MapleStat.FACE, args);
			}
		} else {
			if (args < 0) {
				args = 0;
			}
			if (c.getPlayer().getDressup() == true) {
				setAngelicSecondSkin(args);
			} else {
				c.getPlayer().setSkinColor((byte) args);
				c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
			}
		}
		c.getPlayer().equipChanged();
		return 1;
	}

	public int setZeroAvatar(int ticket, int args1, int args2) {
		int mixranze = 0, mixranze2 = 0;
		if (args1 > 99999) {
			mixranze = args1 % 1000;
			args1 /= 1000;
		}
		if (args2 > 99999) {
			mixranze2 = args2 % 1000;
			args2 /= 1000;
		}
		if (hairlist.containsKey(Integer.valueOf(args1)) || hairlist.containsKey(Integer.valueOf(args2))) {
			if (ticket == 0) {
				this.c.getPlayer().setHair(args1);
				this.c.getPlayer().updateSingleStat(MapleStat.HAIR, args1);
			} else {
				this.c.getPlayer().setSecondHair(args2);
				this.c.getPlayer().updateSingleStat(MapleStat.HAIR, args2);
				this.c.getPlayer().fakeRelog();
			}
		} else if (facelist.containsKey(Integer.valueOf(args1)) || facelist.containsKey(Integer.valueOf(args2))) {
			if (mixranze > 0) {
				String sum = args1 + "" + mixranze;
				args1 = Integer.parseInt(sum);
			}
			if (mixranze2 > 0) {
				String sum = args2 + "" + mixranze;
				args2 = Integer.parseInt(sum);
			}
			if (ticket == 0) {
				this.c.getPlayer().setFace(args1);
				this.c.getPlayer().updateSingleStat(MapleStat.FACE, args1);
			} else {
				this.c.getPlayer().setSecondFace(args2);
				this.c.getPlayer().updateSingleStat(MapleStat.FACE, args2);
				this.c.getPlayer().fakeRelog();
			}
		} else if (ticket == 0) {
			this.c.getPlayer().setSkinColor((byte) args1);
			this.c.getPlayer().updateSingleStat(MapleStat.SKIN, args1);
		} else {
			this.c.getPlayer().setSecondSkinColor((byte) args2);
			this.c.getPlayer().updateSingleStat(MapleStat.SKIN, args2);
			this.c.getPlayer().fakeRelog();
		}
		this.c.getPlayer().equipChanged();
		return 1;
	}

	public void setFaceAndroid(int faceId) {
		this.c.getPlayer().getAndroid().setFace(faceId);
		this.c.getPlayer().updateAndroid();
	}

	public void setHairAndroid(int hairId) {
		this.c.getPlayer().getAndroid().setHair(hairId);
		this.c.getPlayer().updateAndroid();
	}

	public void setSkinAndroid(int color) {

		this.c.getPlayer().getAndroid().setSkin(color);

		this.c.getPlayer().updateAndroid();
	}

	public void sendStorage() {

		this.c.getPlayer().setStorageNPC(this.id);

		this.c.getSession().writeAndFlush(CField.NPCPacket.getStorage((byte) 0));
	}

	public void openShop(int idd) {
		if (MapleShopFactory.getInstance().getShop(idd).getRechargeShop() == 1) {
			boolean active = false, save = false;
			Calendar ocal = Calendar.getInstance();

			for (MapleShopItem item : MapleShopFactory.getInstance().getShop(idd).getItems()) {
				int maxday = ocal.getActualMaximum(5);
				int month = ocal.get(2) + 1;
				int day = ocal.get(5);

				if (item.getReCharge() > 0) {
					for (MapleShopLimit shl : this.c.getShopLimit()) {
						if (shl.getLastBuyMonth() > 0 && shl.getLastBuyDay() > 0) {
							Calendar baseCal = new GregorianCalendar(ocal.get(1), shl.getLastBuyMonth(),
									shl.getLastBuyDay());
							Calendar targetCal = new GregorianCalendar(ocal.get(1), month, day);
							long diffSec = (targetCal.getTimeInMillis() - baseCal.getTimeInMillis()) / 1000L;
							long diffDays = diffSec / 86400L;

							if (shl.getItemid() == item.getItemId()
									&& shl.getShopId() == MapleShopFactory.getInstance().getShop(idd).getId()
									&& shl.getPosition() == item.getPosition() && diffDays >= 0L) {
								shl.setLastBuyMonth(0);
								shl.setLastBuyDay(0);
								shl.setLimitCountAcc(0);
								shl.setLimitCountChr(0);
								save = true;
								break;
							}
						}
					}
					if (item.getReChargeDay() <= day && item.getReChargeMonth() <= month) {
						active = true;
						int afterday = day + item.getReCharge();
						if (afterday > maxday) {
							afterday -= maxday;
							month++;
							if (month > 12) {
								month = 1;
							}
						}
						Connection con = null;
						PreparedStatement ps = null;
						try {
							con = DatabaseConnection.getConnection();
							ps = con.prepareStatement(
									"UPDATE shopitems SET rechargemonth = ?, rechargeday = ?, resetday = ? WHERE position = ? AND itemid = ? AND tab = ?");
							ps.setInt(1, month);
							ps.setInt(2, afterday);
							ps.setInt(3, day);
							ps.setInt(4, item.getPosition());
							ps.setInt(5, item.getItemId());
							ps.setByte(6, item.getTab());

							ps.executeUpdate();
							ps.close();
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
						} finally {
							try {
								if (con != null) {
									con.close();
								}
								if (ps != null) {
									ps.close();
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			if (active) {
				MapleShopFactory.getInstance().clear();
				MapleShopFactory.getInstance().getShop(this.id);
			} else if (save) {
				this.c.saveShopLimit(this.c.getShopLimit());
			}
		}
		MapleShopFactory.getInstance().getShop(idd).sendShop(this.c);
	}

	public int gainGachaponItem(int id, int quantity) {
		return gainGachaponItem(id, quantity, this.c.getPlayer().getMap().getStreetName());
	}

	public int gainGachaponItem(int id, int quantity, String msg) {
		try {
			if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
				return -1;
			}
			Item item = MapleInventoryManipulator.addbyId_Gachapon(this.c, id, (short) quantity);
			if (item == null) {
				return -1;
			}
			byte rareness = GameConstants.gachaponRareItem(item.getItemId());
			if (rareness > 0) {
				World.Broadcast.broadcastMessage(
						CWvsContext.getGachaponMega(this.c.getPlayer().getName(), " : got a(n)", item, rareness, msg));
			}
			this.c.getSession().writeAndFlush(CField.EffectPacket.showCharmEffect(this.c.getPlayer(), id, 1, true, ""));
			return item.getItemId();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public void changeJob(int job) {
		this.c.getPlayer().changeJob(job);
	}

	public void startQuest(int idd) {
		MapleQuest.getInstance(idd).start(getPlayer(), this.id);
	}

	public void completeQuest(int idd) {
		MapleQuest.getInstance(idd).complete(getPlayer(), this.id);
	}

	public void forfeitQuest(int idd) {
		MapleQuest.getInstance(idd).forfeit(getPlayer());
	}

	public void forceStartQuest() {
		MapleQuest.getInstance(this.id2).forceStart(getPlayer(), getNpc(), null);
	}

	public void forceStartQuest(int idd) {
		MapleQuest.getInstance(idd).forceStart(getPlayer(), getNpc(), null);
	}

	public void forceStartQuest(String customData) {
		MapleQuest.getInstance(this.id2).forceStart(getPlayer(), getNpc(), customData);
	}

	public void forceCompleteQuest() {
		MapleQuest.getInstance(this.id2).forceComplete(getPlayer(), getNpc());
	}

	public void forceCompleteQuest(int idd) {
		MapleQuest.getInstance(idd).forceComplete(getPlayer(), getNpc());
	}

	public String getQuestCustomData(int id2) {
		return this.c.getPlayer().getQuestNAdd(MapleQuest.getInstance(id2)).getCustomData();
	}

	public void setQuestCustomData(int id2, String customData) {
		getPlayer().getQuestNAdd(MapleQuest.getInstance(id2)).setCustomData(customData);
	}

	public long getMeso() {
		return getPlayer().getMeso();
	}

	public void gainAp(int amount) {
		this.c.getPlayer().gainAp((short) amount);
	}

	public void expandInventory(byte type, int amt) {
		this.c.getPlayer().expandInventory(type, amt);
	}

	public void gainItemInStorages(int id) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		int itemid = 0;
		int str = 0, dex = 0, int_ = 0, luk = 0, watk = 0, matk = 0;
		int hp = 0, upg = 0, slot = 0;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM cashstorages WHERE id = ? and charid = ?");
			ps.setInt(1, id);
			ps.setInt(2, this.c.getPlayer().getId());
			rs = ps.executeQuery();

			if (rs.next()) {
				itemid = rs.getInt("itemid");
				str = rs.getInt("str");
				dex = rs.getInt("dex");
				int_ = rs.getInt("int_");
				luk = rs.getInt("luk");
				watk = rs.getInt("watk");
				matk = rs.getInt("matk");
				hp = rs.getInt("maxhp");
				upg = rs.getInt("upg");
				slot = rs.getInt("slot");
			}
			ps.close();
			rs.close();

			ps2 = con.prepareStatement("DELETE FROM cashstorages WHERE id = ?");
			ps2.setInt(1, id);
			ps2.executeUpdate();
			ps2.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps2 != null) {
				try {
					ps2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		Equip item = (Equip) ii.getEquipById(itemid);
		item.setStr((short) str);
		item.setDex((short) dex);
		item.setInt((short) int_);
		item.setLuk((short) luk);
		item.setWatk((short) watk);
		item.setMatk((short) matk);
		item.setHp((short) hp);
		item.setUpgradeSlots((byte) slot);
		item.setLevel((byte) upg);
		MapleInventoryManipulator.addbyItem(this.c, (Item) item);
	}

	public void StoreInStorages(int charid, int itemid, int str, int dex, int int_, int luk, int watk, int matk) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement(
					"INSERT INTO cashstorages (charid, itemid, str, dex, int_, luk, watk, matk) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setInt(1, charid);
			ps.setInt(2, itemid);
			ps.setInt(3, str);
			ps.setInt(4, dex);
			ps.setInt(5, int_);
			ps.setInt(6, luk);
			ps.setInt(7, watk);
			ps.setInt(8, matk);
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getCashStorages(int charid) {
		String ret = "";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM cashstorages WHERE charid = ?");
			ps.setInt(1, charid);
			rs = ps.executeQuery();

			if (rs.next()) {
				ret = ret + "#L" + rs.getInt("id") + "##i" + rs.getInt("itemid") + "##z" + rs.getInt("itemid")
						+ "#\r\n";
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	public String getCharacterList(int accountid) {
		String ret = "";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM characters WHERE accountid = ?");
			ps.setInt(1, accountid);
			rs = ps.executeQuery();
			while (rs.next()) {
				ret = ret + "#L" + rs.getInt("id") + "#" + rs.getString("name") + "\r\n";
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	public final void clearSkills() {
		Map<Skill, SkillEntry> skills = new HashMap<>(getPlayer().getSkills());
		Map<Skill, SkillEntry> newList = new HashMap<>();
		for (Map.Entry<Skill, SkillEntry> skill : skills.entrySet()) {
			newList.put(skill.getKey(), new SkillEntry(0, (byte) 0, -1L));
		}
		getPlayer().changeSkillsLevel(newList);
		newList.clear();
		skills.clear();
	}

	public final void skillmaster() {
		MapleData data = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz"))
				.getData(StringUtil.getLeftPaddedStr("" + this.c.getPlayer().getJob(), '0', 3) + ".img");
		for (MapleData skill : data) {
			if (skill != null) {
				for (MapleData skillId : skill.getChildren()) {
					if (!skillId.getName().equals("icon")) {
						byte maxLevel = (byte) MapleDataTool.getIntConvert("maxLevel", skillId.getChildByPath("common"),
								0);
						if (maxLevel < 0) {
							maxLevel = 1;
						}
						if (MapleDataTool.getIntConvert("invisible", skillId, 0) == 0
								&& this.c.getPlayer().getLevel() >= MapleDataTool.getIntConvert("reqLev", skillId, 0)) {
							this.c.getPlayer().changeSingleSkillLevel(
									SkillFactory.getSkill(Integer.parseInt(skillId.getName())), maxLevel, maxLevel);
						}
					}
				}
			}
		}

		if (GameConstants.isZero(this.c.getPlayer().getJob())) {
			int[] jobs = { 10000, 10100, 10110, 10111, 10112 };
			for (int job : jobs) {
				data = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz"))
						.getData(job + ".img");
				for (MapleData skill : data) {
					if (skill != null) {
						for (MapleData skillId : skill.getChildren()) {
							if (!skillId.getName().equals("icon")) {
								byte maxLevel = (byte) MapleDataTool.getIntConvert("maxLevel",
										skillId.getChildByPath("common"), 0);
								if (maxLevel < 0) {
									maxLevel = 1;
								}
								if (MapleDataTool.getIntConvert("invisible", skillId, 0) == 0 && this.c.getPlayer()
										.getLevel() >= MapleDataTool.getIntConvert("reqLev", skillId, 0)) {
									this.c.getPlayer().changeSingleSkillLevel(
											SkillFactory.getSkill(Integer.parseInt(skillId.getName())), maxLevel,
											maxLevel);
								}
							}
						}
					}
				}
				if (this.c.getPlayer().getLevel() >= 200) {
					this.c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(100001005), 1, (byte) 1);
				}
			}
		}
		if (GameConstants.isKOC(this.c.getPlayer().getJob()) && this.c.getPlayer().getLevel() >= 100) {
			this.c.getPlayer().changeSkillLevel(11121000, (byte) 30, (byte) 30);
			this.c.getPlayer().changeSkillLevel(12121000, (byte) 30, (byte) 30);
			this.c.getPlayer().changeSkillLevel(13121000, (byte) 30, (byte) 30);
			this.c.getPlayer().changeSkillLevel(14121000, (byte) 30, (byte) 30);
			this.c.getPlayer().changeSkillLevel(15121000, (byte) 30, (byte) 30);
		}
	}

	public static void writeLog(String path, String data, boolean writeafterend) {
		try {
			File fFile = new File(path);

			if (!fFile.exists()) {
				fFile.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(path, true);
			long time = System.currentTimeMillis();
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String str = dayTime.format(new Date(time));
			String msg = "\r\n" + str + " | " + data;
			out.write(msg.getBytes());
			out.close();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addBoss(String boss) {

		if (this.c.getPlayer().getParty() != null) {
			// System.err.println("3214234");
			this.c.getPlayer().removeV_boss("bossPractice");
			KoreaCalendar kc = new KoreaCalendar();
			String today = (kc.getYeal() % 100) + "/" + kc.getMonths() + "/" + kc.getDays();
			for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
				MapleCharacter ch = this.c.getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
				if (ch != null) {
					ch.removeV_boss("bossPractice");
					ch.removeV_boss(boss);
					ch.addKV_boss(boss, (System.currentTimeMillis() + 1800000L) + "");
					FileoutputUtil.log(FileoutputUtil. 首領入場日誌,
					        "[入場] 帳號編號 : " + this.c.getAccID()  + " | 隊伍編號 : " + chr.getPlayer().getParty().getId() 
					                + " | 角色 : " + ch.getName()  + "(" + ch.getId()  + ") | 入場首領 : " + boss);
					switch (boss) {
					case "Normal_Zakum":
						MapleQuest.getInstance(7003).forceStart(ch, 0, today);
						MapleQuest.getInstance(7004).forceStart(ch, 0, "1");
						break;
					case "Normal_Hillah":
						ch.updateInfoQuest(3981, "eNum=1;lastDate=" + today);
						break;
					case "Normal_Kawoong":
						MapleQuest.getInstance(3590).forceStart(ch, 0, today);
						MapleQuest.getInstance(3591).forceStart(ch, 0, "1");
						break;
					case "Easy_Populatus":
					case "Normal_Populatus":
						MapleQuest.getInstance(7200).forceStart(ch, 0, today);
						MapleQuest.getInstance(7201).forceStart(ch, 0, "1");
						break;
					case "Easy_VonLeon":
					case "Normal_VonLeon":
					case "Hard_VonLeon":
						ch.updateInfoQuest(7850, "eNum=1;lastDate=" + today);
						break;
					case "Normal_Horntail":
					case "Chaos_Horntail":
						ch.updateInfoQuest(7312, "eNum=1;lastDate=" + today);
						break;
					case "Easy_Arkarium":
					case "Normal_Arkarium":
						ch.updateInfoQuest(7851, "eNum=1;lastDate=" + today);
						break;
					case "Normal_Pinkbean":
						ch.setKeyValue(7403, "eNum", "1");
						ch.setKeyValue(7403, "lastDate", today);
						break;
					case "Chaos_Pinkbean":
						ch.setKeyValue(7403, "eNumC", "1");
						ch.setKeyValue(7403, "lastDateC", today);
						break;
					case "Normal_Lotus":
						ch.setKeyValue(33303, "eNum", "1");
						ch.setKeyValue(33303, "lastDate", today);
						break;
					case "Hard_Lotus":
						ch.setKeyValue(33126, "eNum", "1");
						ch.setKeyValue(33126, "lastDate", today);
						break;
					case "Normal_Demian":
						ch.setKeyValue(34017, "eNum", "1");
						ch.setKeyValue(34017, "lastDate", today);
						break;
					case "Hard_Demian":
						ch.setKeyValue(34016, "eNum", "1");
						ch.setKeyValue(34016, "lastDate", today);
						break;
					case "Easy_Lucid":
						ch.setKeyValue(34364, "eNumE", "1");
						ch.setKeyValue(34364, "lastDateE", today);
						break;
					case "Normal_Lucid":
						ch.setKeyValue(34364, "eNum", "1");
						ch.setKeyValue(34364, "lastDate", today);
						break;
					case "Hard_Lucid":
						ch.setKeyValue(34364, "eNumH", "1");
						ch.setKeyValue(34364, "lastDateH", today);
						break;
					case "Normal_Will":
						ch.setKeyValue(35100, "lastDateN", "1");
						ch.setKeyValue(35100, "lastDateN", today);
						break;
					case "Hard_Will":
						ch.setKeyValue(35100, "lastDate", "1");
						ch.setKeyValue(35100, "lastDate", today);
						break;
					case "Normal_Dusk":
						ch.setKeyValue(35137, "lastDateN", "1");
						ch.setKeyValue(35137, "lastDateN", today);
						break;
					case "Chaos_Dusk":
						ch.setKeyValue(35139, "lastDateH", "1");
						ch.setKeyValue(35139, "lastDateH", today);
						break;
					case "Extreme_Dusk":
						ch.setKeyValue(35137, "lastDateH", "1");
						ch.setKeyValue(35137, "lastDateH", today);
						break;

					case "Normal_JinHillah":
						ch.setKeyValue(35260, "lastDate", "1");
						ch.setKeyValue(35260, "lastDate", today);
						break;

					case "Extreme_JinHillah":
						ch.setKeyValue(35260, "lastDate", "1");
						ch.setKeyValue(35260, "lastDate", today);
						break;
					case "Hell_JinHillah":
						ch.setKeyValue(35260, "lastDate", "1");
						ch.setKeyValue(35260, "lastDate", today);
						break;

					case "Normal_Dunkel":
						ch.setKeyValue(35138, "lastDateN", "1");
						ch.setKeyValue(35138, "lastDateN", today);
						break;
					case "Hard_Dunkel":
						ch.setKeyValue(35140, "lastDateH", "1");
						ch.setKeyValue(35140, "lastDateH", today);
						break;
					case "Black_Mage":
						ch.setKeyValue(35377, "lastDate", "1");
						ch.setKeyValue(35377, "lastDate", today);
						break;
					case "Hard_Seren":
						ch.setKeyValue(39932, "lastDate", "1");
						ch.setKeyValue(39932, "lastDate", today);
						break;
					case "Chaos_Karlos":
						ch.setKeyValue(40009, "lastDate", today);
						break;
					case "Normal_GES":
						ch.setKeyValue(3688, "lastDate", today);
					case "Chaos_Karing":
						ch.getClient().setCustomKeyValue(338401, "lastDate", today);
						break;
					}
				}
			}
		}
	}

	public void addBossPractice(String boss) {
		if (this.c.getPlayer().getParty() != null) {
			for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
				MapleCharacter ch = this.c.getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
				if (ch != null) {
					ch.addKV_boss("bossPractice", "1");
					FileoutputUtil.log(FileoutputUtil.首領入場日誌,
							"[연습모드 입장] 계정 번호 : " + this.c.getAccID() + " | 파티번호 : " + chr.getPlayer().getParty().getId()
									+ " | 캐릭터 : " + this.c.getPlayer().getName() + "(" + this.c.getPlayer().getId()
									+ ") | 입장 보스 : " + boss);
				}
			}
		}
	}

	public Object[] BossNotAvailableChrList(String boss, int limit) {
		Object[] arr = new Object[0];
		if (this.c.getPlayer().getParty() != null) {
			for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
				for (ChannelServer channel : ChannelServer.getAllInstances()) {
					MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
					if (ch != null && ch.getGMLevel() < 6) {
						String k = ch.getV(boss);
						int key = (k == null) ? 0 : Integer.parseInt(ch.getV(boss));
						if (key >= limit - 1) {
							arr = add(arr, new Object[] { ch.getName() });
						}
					}
				}
			}
		}
		return arr;
	}

	public Object[] LevelNotAvailableChrList(int level) {
		Object[] arr = new Object[0];
		if (this.c.getPlayer().getParty() != null) {
			for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
				for (ChannelServer channel : ChannelServer.getAllInstances()) {
					MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
					if (ch != null && ch.getGMLevel() < 6 && ch.getLevel() < level) {
						arr = add(arr, new Object[] { ch.getName() });
					}
				}
			}
		}
		return arr;
	}

	public static Object[] add(Object[] arr, Object... elements) {
		Object[] tempArr = new Object[arr.length + elements.length];
		System.arraycopy(arr, 0, tempArr, 0, arr.length);
		for (int i = 0; i < elements.length; i++) {
			tempArr[arr.length + i] = elements[i];
		}
		return tempArr;
	}

	public boolean partyhaveItem(final int itemid, final int qty) {
		if (this.c.getPlayer().getParty() != null) {
			for (final MaplePartyCharacter chr : this.getPlayer().getParty().getMembers()) {
				for (final ChannelServer channel : ChannelServer.getAllInstances()) {
					final MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
					if (ch != null && ch.getGMLevel() <= 6) {
						final int getqty = ch.itemQuantity(itemid);
						if (getqty < qty) {
							return false;
						}
						continue;
					}
				}
			}
			return true;
		}
		return false;
	}

	public void asd123(int itemid) {
		if (this.c.getPlayer().getParty() != null) {
			for (final MaplePartyCharacter chr : this.getPlayer().getParty().getMembers()) {
				System.err.println(chr.getPlayer().getName());
				chr.getPlayer().removeItem(itemid, -1);

			}
		}
	}

	public boolean isBossAvailable(String boss, int limit) {
		if (this.c.getPlayer().getParty() != null) {
			KoreaCalendar kc = new KoreaCalendar();
			String today = (kc.getYeal() % 100) + "/" + kc.getMonths() + "/" + kc.getDays();
			Iterator<MaplePartyCharacter> iterator = getPlayer().getParty().getMembers().iterator();
			if (iterator.hasNext()) {
				MaplePartyCharacter chr = iterator.next();
				for (ChannelServer channel : ChannelServer.getAllInstances()) {
					MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
					if (ch != null && !ch.isGM()) {
						boolean enter = false, weekly = false;
						String CheckS = "lastDate";
						int checkenterquestid = 0, checkclearquestid = 0;
						boolean accCheck = false;
						switch (boss) {
						case "GuildBoss":
							checkenterquestid = 7002;
							checkclearquestid = 7001;
							weekly = true;
							break;
						case "Normal_Zakum":
							checkenterquestid = 7003;
							break;
						case "Chaos_Zakum":
							checkclearquestid = 15166;
							// weekly = true;
							break;
						case "Easy_Magnus":
						case "Normal_Magnus":
							checkclearquestid = 3993;
							break;
						case "Hard_Magnus":
							checkclearquestid = 3992;
							// weekly = true;
							break;
						case "Normal_Hillah":
							checkenterquestid = 3981;
							break;
						case "Hard_Hillah":
							checkclearquestid = 3650;
							// weekly = true;
							break;
						case "Normal_Kawoong":
							checkenterquestid = 3590;
							break;
						case "Easy_Populatus":
						case "Normal_Populatus":
							checkenterquestid = 7200;
							break;
						case "Chaos_Populatus":
							checkclearquestid = 3657;
							// weekly = true;
							break;
						case "Normal_Pierre":
							checkclearquestid = 30032;
							break;
						case "Chaos_Pierre":
							checkclearquestid = 30043;
							// weekly = true;
							break;
						case "Normal_VonBon":
							checkclearquestid = 30039;
							break;
						case "Chaos_VonBon":
							checkclearquestid = 30044;
							// weekly = true;
							break;
						case "Normal_BloodyQueen":
							checkclearquestid = 30033;
							break;
						case "Chaos_BloodyQueen":
							checkclearquestid = 30045;
							// weekly = true;
							break;
						case "Normal_Vellum":
							checkclearquestid = 30041;
							break;
						case "Chaos_Vellum":
							checkclearquestid = 30046;
							// weekly = true;
							break;
						case "Easy_VonLeon":
						case "Normal_VonLeon":
						case "Hard_VonLeon":
							checkenterquestid = 7850;
							break;
						case "Normal_Horntail":
						case "Chaos_Horntail":
							checkenterquestid = 7312;
							break;
						case "Easy_Arkarium":
						case "Normal_Arkarium":
							checkenterquestid = 7851;
							break;
						case "Normal_Pinkbean":
							checkenterquestid = 7403;
							checkclearquestid = 3652;
							break;
						case "Chaos_Pinkbean":
							checkenterquestid = 7403;
							checkclearquestid = 3653;
							// weekly = true;
							CheckS = "lastDateC";
							break;
						case "Easy_Cygnus":
						case "Normal_Cygnus":
							checkclearquestid = 31199;
							// weekly = true;
							break;
						case "Normal_Lotus":
							checkclearquestid = 33303;
							// weekly = true;
							break;
						case "Hard_Lotus":
							checkenterquestid = 33126;
							checkclearquestid = 33303;
							// weekly = true;
							break;
						case "Normal_Demian":
							checkclearquestid = 34017;
							// weekly = true;
							break;
						case "Hard_Demian":
							checkenterquestid = 34016;
							checkclearquestid = 34017;
							// weekly = true;
							break;
						case "Easy_Lucid":
							checkenterquestid = 34364;
							checkclearquestid = 3685;
							CheckS = "lastDateE";
							// weekly = true;
							break;
						case "Normal_Lucid":
							checkenterquestid = 34364;
							checkclearquestid = 3685;
							// weekly = true;
							break;
						case "Hard_Lucid":
							checkenterquestid = 34364;
							checkclearquestid = 3685;
							CheckS = "lastDateH";
							// weekly = true;
							break;
						case "Normal_Will":
							checkenterquestid = 35100;
							checkclearquestid = 3658;
							// weekly = true;
							break;
						case "Hard_Will":
							checkenterquestid = 35100;
							checkclearquestid = 3658;
							CheckS = "lastDateN";
							// weekly = true;
							break;
						case "Normal_Dusk":
							checkenterquestid = 35137;
							checkclearquestid = 3680;
							CheckS = "lastDateN";
							// weekly = true;
							break;

						case "Extreme_Dusk":
							checkenterquestid = 35137;
							checkclearquestid = 3680;
							CheckS = "lastDateN";
							// weekly = true;
							break;

						case "Chaos_Dusk":
							checkenterquestid = 35139;
							checkclearquestid = 3680;
							CheckS = "lastDateH";
							// weekly = true;
							break;
						case "Normal_JinHillah":
							checkenterquestid = 35260;
							checkclearquestid = 3673;
							// weekly = true;
							break;

						case "Extreme_JinHillah":
							checkenterquestid = 35260;
							checkclearquestid = 3673;
							// weekly = true;
							break;

						case "Hell_JinHillah":
							checkenterquestid = 35260;
							checkclearquestid = 3673;
							// weekly = true;
							break;

						case "Normal_Dunkel":
							checkenterquestid = 35138;
							checkclearquestid = 3681;
							CheckS = "lastDateN";
							// weekly = true;
							break;
						case "Hard_Dunkel":
							checkenterquestid = 35140;
							checkclearquestid = 3681;
							CheckS = "lastDateH";
							// weekly = true;
							break;
						case "Black_Mage":
							checkenterquestid = 35377;
							checkclearquestid = 3679;
							break;
						case "Hard_Seren":
							checkenterquestid = 39932;
							checkclearquestid = 3687;
							break;
						case "Chaos_Karlos":
							checkenterquestid = 40009;
							checkclearquestid = 3689;
							break;
						case "Normal_GES":
							checkclearquestid = 3688;
							weekly = false;
							break;
						case "Chaos_Karing":
							checkenterquestid = 338401;
							checkclearquestid = 3384010;
							weekly = false;
							break;
						}

						if (checkenterquestid > 0) {
							if (chr.getPlayer().getKeyValueStr(checkenterquestid, CheckS) != null
									&& chr.getPlayer().getKeyValueStr(checkenterquestid, CheckS).equals(today)) {
								enter = true;
							}
						}
						if (!enter) {
							MapleQuestStatus quests = (MapleQuestStatus) chr.getPlayer().getQuest_Map()
									.get(MapleQuest.getInstance(checkenterquestid));
							if (quests != null && quests.getCustomData() != null
									&& quests.getCustomData().equals(today)) {
								enter = true;
							}
						}
						if (checkclearquestid > 0 && !enter) {
							if (weekly) {
								if (chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime") != null) {
									String[] array = chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime")
											.split("/");
									Calendar clear = new GregorianCalendar(Integer.parseInt("20" + array[0]),
											Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
									Calendar ocal = Calendar.getInstance();
									int yeal = clear.get(1), days = clear.get(5), day = ocal.get(7),
											day2 = clear.get(7), maxday = clear.getMaximum(5), month = clear.get(2);
									int check = (day2 == 5) ? 7 : ((day2 == 6) ? 6 : ((day2 == 7) ? 5 : 0));
									if (check == 0) {
										for (int i = day2; i < 5; i++) {
											check++;
										}
									}
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

									if (after.getTimeInMillis() > System.currentTimeMillis()) {
										enter = true;
									}
								}
							} else if (chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime") != null
									&& chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime").equals(today)) {
								enter = true;
							}
							if (!enter) {
								if (ch.getV(boss) != null
										&& Long.parseLong(ch.getV(boss)) - System.currentTimeMillis() >= 0L) {
									enter = true;
								}
							}
						}
						if (enter) {
							return false;
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean isBossEnterAmountCheck(String boss, int limit) { // 보스입장횟수
		Calendar ocal = Calendar.getInstance();
		String years = "" + ocal.get(ocal.YEAR) + "";
		String months = "" + (ocal.get(ocal.MONTH) + 1) + "";
		String days = "" + ocal.get(ocal.DAY_OF_MONTH) + "";
		String hours = "" + ocal.get(ocal.HOUR_OF_DAY) + "";
		String mins = "" + ocal.get(ocal.MINUTE) + "";
		String secs = "" + ocal.get(ocal.SECOND) + "";
		int yeal = ocal.get(ocal.YEAR);
		int month = ocal.get(ocal.MONTH) + 1;
		int dayt = ocal.get(ocal.DAY_OF_MONTH);
		int hour = ocal.get(ocal.HOUR_OF_DAY);
		int min = ocal.get(ocal.MINUTE);
		int sec = ocal.get(ocal.SECOND);
		if (month < 10) {
			months = "0" + "" + month;
		}
		if (dayt < 10) {
			days = "0" + "" + dayt;
		}
		if (hour < 10) {
			hours = "0" + "" + hour;
		}
		if (min < 10) {
			mins = "0" + "" + min;
		}
		if (sec < 10) {
			secs = "0" + "" + sec;
		}
		int date = Integer.parseInt(years + "" + months + "" + days);
		if (c.getPlayer().getParty() != null) {
			for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
				for (ChannelServer channel : ChannelServer.getAllInstances()) {
					MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
					if (ch != null && !ch.isGM()) {
						long getKey = ch.getKeyValue(date, boss);
						ch.dropMessage(5, "해당 보스는 하루에 " + limit + "번 입장하실 수 있습니다.");
						if (getKey > limit - 1) {
							return false;
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	public String isBossString(String boss) {

		String txt = "파티원 중 #r입장 조건#k을 충족하지 못하는 파티원이 있습니다.\r\n모든 파티원이 조건을 충족해야 입장이 가능합니다.\r\n\r\n";

		if (this.c.getPlayer().getParty() != null) {
			KoreaCalendar kc = new KoreaCalendar();
			String today = (kc.getYeal() % 100) + "/" + kc.getMonths() + "/" + kc.getDays();
			for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
				for (ChannelServer channel : ChannelServer.getAllInstances()) {
					MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
					if (ch != null && !ch.isGM()) {
						boolean enter = false, weekly = false;
						String CheckS = "lastDate";
						int checkenterquestid = 0, checkclearquestid = 0;
						boolean accCheck = false;
						switch (boss) {

						case "Normal_Zakum":
							checkenterquestid = 7003;
							break;
						case "Chaos_Zakum":
							checkclearquestid = 15166;
							// weekly = true;
							break;
						case "Easy_Magnus":
						case "Normal_Magnus":
							checkclearquestid = 3993;
							break;
						case "Hard_Magnus":
							checkclearquestid = 3992;
							// weekly = true;
							break;
						case "Normal_Hillah":
							checkenterquestid = 3981;
							break;
						case "Hard_Hillah":
							checkclearquestid = 3650;
							// weekly = true;
							break;
						case "Normal_Kawoong":
							checkenterquestid = 3590;
							break;
						case "Easy_Populatus":
						case "Normal_Populatus":
							checkenterquestid = 7200;
							break;
						case "Chaos_Populatus":
							checkclearquestid = 3657;
							// weekly = true;
							break;
						case "Normal_Pierre":
							checkclearquestid = 30032;
							break;
						case "Chaos_Pierre":
							checkclearquestid = 30043;
							// weekly = true;
							break;
						case "Normal_VonBon":
							checkclearquestid = 30039;
							break;
						case "Chaos_VonBon":
							checkclearquestid = 30044;
							// weekly = true;
							break;
						case "Normal_BloodyQueen":
							checkclearquestid = 30033;
							break;
						case "Chaos_BloodyQueen":
							checkclearquestid = 30045;
							// weekly = true;
							break;
						case "Normal_Vellum":
							checkclearquestid = 30041;
							break;
						case "Chaos_Vellum":
							checkclearquestid = 30046;
							// weekly = true;
							break;
						case "Easy_VonLeon":
						case "Normal_VonLeon":
						case "Hard_VonLeon":
							checkenterquestid = 7850;
							break;
						case "Normal_Horntail":
						case "Chaos_Horntail":
							checkenterquestid = 7312;
							break;
						case "Easy_Arkarium":
						case "Normal_Arkarium":
							checkenterquestid = 7851;
							break;
						case "Normal_Pinkbean":
							checkenterquestid = 7403;
							checkclearquestid = 3652;
							break;
						case "Chaos_Pinkbean":
							checkenterquestid = 7403;
							checkclearquestid = 3653;
							// weekly = true;
							CheckS = "lastDateC";
							break;
						case "Easy_Cygnus":
						case "Normal_Cygnus":
							checkclearquestid = 31199;
							// weekly = true;
							break;
						case "Normal_Lotus":
							checkclearquestid = 33303;
							// weekly = true;
							break;
						case "Hard_Lotus":
							checkenterquestid = 33126;
							checkclearquestid = 33303;
							// weekly = true;
							break;
						case "Normal_Demian":
							checkclearquestid = 34017;
							// weekly = true;
							break;
						case "Hard_Demian":
							checkenterquestid = 34016;
							checkclearquestid = 34017;
							// weekly = true;
							break;
						case "Easy_Lucid":
							checkenterquestid = 34364;
							checkclearquestid = 3685;
							CheckS = "lastDateE";
							// weekly = true;
							break;
						case "Normal_Lucid":
							checkenterquestid = 34364;
							checkclearquestid = 3685;
							// weekly = true;
							break;
						case "Hard_Lucid":
							checkenterquestid = 34364;
							checkclearquestid = 3685;
							CheckS = "lastDateH";
							// weekly = true;
							break;
						case "Normal_Will":
							checkenterquestid = 35100;
							checkclearquestid = 3658;
							CheckS = "lastDateN";
							// weekly = true;
							break;
						case "Hard_Will":
							checkenterquestid = 35100;
							checkclearquestid = 3658;
							// weekly = true;
							break;
						case "Normal_Dusk":
							checkenterquestid = 35137;
							checkclearquestid = 3680;
							CheckS = "lastDateN";
							// weekly = true;
							break;
						case "Chaos_Dusk":
							checkenterquestid = 35139;
							checkclearquestid = 3680;
							CheckS = "lastDateH";
							// weekly = true;
							break; // 더스크까지 추가된듯여? 더스크 확인점요
						case "Normal_JinHillah":
							checkenterquestid = 35260;
							checkclearquestid = 3673;
							weekly = true;
							break;
						case "Normal_Dunkel":
							checkenterquestid = 35138;
							checkclearquestid = 3681;
							CheckS = "lastDateN";
							// weekly = true;
							break;
						case "Hard_Dunkel":
							checkenterquestid = 35140;
							checkclearquestid = 3681;
							CheckS = "lastDateH";
							break;
						case "Black_Mage":
							checkenterquestid = 35377;
							checkclearquestid = 3679;
							break;
						case "Hard_Seren":
							checkenterquestid = 39932;
							checkclearquestid = 3687;
							break;
						case "Chaos_Karlos":
							checkenterquestid = 40009;
							checkclearquestid = 3689;
							break;
						case "Normal_GES":
							checkclearquestid = 3688;
							weekly = false;
							break;
						case "Chaos_Karing":
							checkenterquestid = 338401;
							checkclearquestid = 3384010;
							weekly = false;
							break;
						// 보스 추가 시 스크립트 상의 setting 안에 있는 영어(Key)를
						// case "영어":
						// checkenterquestid = 커스텀숫자
						// checkclearquestid = 커스텀숫자2
						// weekly = true (아마 주간보스)
						// break;
						// 이런식으로 추가할 것

						}

						if (checkenterquestid > 0) {
							if (chr.getPlayer().getKeyValueStr(checkenterquestid, CheckS) != null
									&& chr.getPlayer().getKeyValueStr(checkenterquestid, CheckS).equals(today)) {
								enter = true;
								txt = txt + "#b" + chr.getName() + "#k님 입장 가능 횟수 초과 하였습니다.\r\n";
							}

							if (!enter) {
								MapleQuestStatus quests = (MapleQuestStatus) chr.getPlayer().getQuest_Map()
										.get(MapleQuest.getInstance(checkenterquestid));
								if (quests != null && quests.getCustomData() != null
										&& quests.getCustomData().equals(today)) {
									enter = true;
									txt = txt + "#b" + chr.getName() + "#k님 입장 가능 횟수 초과 하였습니다.\r\n";
								}
							}
						}
						if (checkclearquestid > 0 && !enter) {
							if (weekly) {
								if (chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime") != null) {
									String[] array = chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime")
											.split("/");
									Calendar clear = new GregorianCalendar(Integer.parseInt("20" + array[0]),
											Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
									Calendar ocal = Calendar.getInstance();

									int yeal = clear.get(1), days = clear.get(5), day = ocal.get(7),
											day2 = clear.get(7), maxday = clear.getMaximum(5), month = clear.get(2);
									int check = (day2 == 5) ? 7 : ((day2 == 6) ? 6 : ((day2 == 7) ? 5 : 0));

									if (check == 0) {
										for (int i = day2; i < 5; i++) {
											check++;
										}
									}
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
									if (after.getTimeInMillis() > System.currentTimeMillis()) {
										enter = true;
										txt = txt + "#b" + chr.getName() + "#k님 입장 가능 횟수 초과 하였습니다.\r\n";
									}
								}
							} else if (chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime") != null
									&& chr.getPlayer().getKeyValueStr(checkclearquestid, "lasttime").equals(today)) {
								enter = true;
								txt = txt + "#b" + chr.getName() + "#k님 입장 가능 횟수 초과 하였습니다.\r\n";
							}
							if (!enter) {
								if (Long.parseLong(ch.getV(boss)) - System.currentTimeMillis() >= 0L) {
									enter = true;
									txt = txt + "#b" + chr.getName() + "#k님 #e#r"
											+ ((Long.parseLong(ch.getV(boss)) - System.currentTimeMillis()) / 1000L
													/ 60L)
											+ "분" + ((Long.parseLong(ch.getV(boss)) - System.currentTimeMillis())
													/ 1000L % 60L)
											+ "초#k#n 뒤에 입장 가능합니다.\r\n";
								}
							}
						}
					}
				}
			}
		}
		return txt;
	}

	public boolean isLevelAvailable(int level) {
		if (this.c.getPlayer().getParty() != null) {
			for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
				for (ChannelServer channel : ChannelServer.getAllInstances()) {
					MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
					if (ch != null && ch.getGMLevel() <= 6 && ch.getLevel() < level) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public boolean hasSkill(int skillid) {
		Skill theSkill = SkillFactory.getSkill(skillid);

		if (theSkill != null) {
			return (this.c.getPlayer().getSkillLevel(theSkill) > 0);
		}
		return false;
	}

	public void showEffect(boolean broadcast, String effect) {
		if (broadcast) {
			c.getPlayer().getMap().broadcastMessage(CField.showEffect(effect));
		} else {
			c.getSession().writeAndFlush(CField.showEffect(effect));
		}
	}

	public void playSound(boolean broadcast, String sound) {
		if (broadcast) {
			c.getPlayer().getMap().broadcastMessage(CField.playSound(sound));
		} else {

			c.getSession().writeAndFlush(CField.playSound(sound));
		}
	}

	public void environmentChange(boolean broadcast, String env) {
		if (broadcast) {
			c.getPlayer().getMap().broadcastMessage(CField.environmentChange(env, 2));
		} else {
			c.getSession().writeAndFlush(CField.environmentChange(env, 2));
		}
	}

	public void updateBuddyCapacity(int capacity) {
		c.getPlayer().setBuddyCapacity((byte) capacity);
	}

	public int getBuddyCapacity() {
		return c.getPlayer().getBuddyCapacity();
	}

	public int partyMembersInMap() {
		int inMap = 0;

		if (getPlayer().getParty() == null) {
			return inMap;
		}

		for (MapleCharacter char2 : getPlayer().getMap().getCharactersThreadsafe()) {
			if (char2.getParty() != null && char2.getParty().getId() == getPlayer().getParty().getId()) {
				inMap++;
			}
		}
		return inMap;
	}

	public List<MapleCharacter> getPartyMembers() {
		if (getPlayer().getParty() == null) {
			return null;
		}
		List<MapleCharacter> chars = new LinkedList<>();
		for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
			for (ChannelServer channel : ChannelServer.getAllInstances()) {
				MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
				if (ch != null) {
					chars.add(ch);
				}
			}
		}
		return chars;
	}

	public void warpPartyWithExp(int mapId, int exp) {
		if (getPlayer().getParty() == null) {
			warp(mapId, 0);
			gainExp(exp);
			return;
		}
		MapleMap target = getMap(mapId);

		for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
			MapleCharacter curChar = this.c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
			if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null)
					|| curChar.getEventInstance() == getPlayer().getEventInstance()) {
				curChar.changeMap(target, target.getPortal(0));
				curChar.gainExp(exp, true, false, true);
			}
		}
	}

	public void warpPartyWithExpMeso(int mapId, int exp, int meso) {
		if (getPlayer().getParty() == null) {
			warp(mapId, 0);
			gainExp(exp);
			gainMeso(meso);
			return;
		}
		MapleMap target = getMap(mapId);

		for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
			MapleCharacter curChar = this.c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
			if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null)
					|| curChar.getEventInstance() == getPlayer().getEventInstance()) {
				curChar.changeMap(target, target.getPortal(0));
				curChar.gainExp(exp, true, false, true);
				curChar.gainMeso(meso, true);
			}
		}
	}

	public MapleCharacter getChar(int id) {
		MapleCharacter chr = null;
		for (ChannelServer cs : ChannelServer.getAllInstances()) {
			chr = cs.getPlayerStorage().getCharacterById(id);

			if (chr != null) {
				return chr;
			}
		}
		return null;
	}

	public void makeRing(int itemid, MapleCharacter chr) {
		try {
			MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

			Item item = ii.getEquipById(itemid);
			Item item1 = ii.getEquipById(itemid);
			item.setUniqueId(MapleInventoryIdentifier.getInstance());
			item1.setUniqueId(MapleInventoryIdentifier.getInstance());
			MapleRing.makeRing(itemid, chr, item.getUniqueId(), item1.getUniqueId());
			MapleRing.makeRing(itemid, getPlayer(), item1.getUniqueId(), item.getUniqueId());
			MapleInventoryManipulator.addbyItem(getClient(), item);
			MapleInventoryManipulator.addbyItem(chr.getClient(), item1);

			chr.reloadChar();
			c.getPlayer().reloadChar();

			sendOk("선택하신 반지를 제작 완료 하였습니다. 인벤토리를 확인해 봐주시길 바랍니다.");
			chr.dropMessage(5, getPlayer().getName() + "님으로 부터 반지가 도착 하였습니다. 인벤토리를 확인해 주시길 바랍니다.");
		} catch (Exception ex) {
			sendOk("반지를 제작하는데 오류가 발생 하였습니다.");
		}
	}

	public void makeRingRC(int itemid, MapleCharacter chr) {
		try {
			MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

			Item item = ii.getEquipById(itemid);
			Item item1 = ii.getEquipById(itemid);

			item.setUniqueId(MapleInventoryIdentifier.getInstance());
			Equip eitem = (Equip) item;

			eitem.setStr((short) 300);
			eitem.setDex((short) 300);
			eitem.setInt((short) 300);
			eitem.setLuk((short) 300);
			eitem.setWatk((short) 300);
			eitem.setMatk((short) 300);

			item1.setUniqueId(MapleInventoryIdentifier.getInstance());
			Equip eitem1 = (Equip) item1;

			eitem1.setStr((short) 300);
			eitem1.setDex((short) 300);
			eitem1.setInt((short) 300);
			eitem1.setLuk((short) 300);
			eitem1.setWatk((short) 300);
			eitem1.setMatk((short) 300);

			MapleRing.makeRing(itemid, chr, eitem.getUniqueId(), eitem1.getUniqueId());
			MapleRing.makeRing(itemid, getPlayer(), eitem1.getUniqueId(), eitem.getUniqueId());

			MapleInventoryManipulator.addbyItem(getClient(), item);
			MapleInventoryManipulator.addbyItem(chr.getClient(), item1);

			chr.reloadChar();
			c.getPlayer().reloadChar();
			sendOk("선택하신 반지를 제작 완료 하였습니다. 인벤토리를 확인해 봐주시길 바랍니다.");
			chr.dropMessage(5, getPlayer().getName() + "님으로 부터 반지가 도착 하였습니다. 인벤토리를 확인해 주시길 바랍니다.");
		} catch (Exception ex) {
			sendOk("반지를 제작하는데 오류가 발생 하였습니다.");
		}
	}

	public void makeRingHB(int itemid, MapleCharacter chr) {
		try {
			int asd = 300;
			int asd2 = 300;

			MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

			Item item = ii.getEquipById(itemid);
			Item item1 = ii.getEquipById(itemid);

			item.setUniqueId(MapleInventoryIdentifier.getInstance());

			Equip eitem = (Equip) item;

			eitem.setStr((short) asd);
			eitem.setDex((short) asd);
			eitem.setInt((short) asd);
			eitem.setLuk((short) asd);
			eitem.setWatk((short) asd2);
			eitem.setMatk((short) asd2);

			item1.setUniqueId(MapleInventoryIdentifier.getInstance());

			Equip eitem1 = (Equip) item1;

			eitem1.setStr((short) asd);
			eitem1.setDex((short) asd);
			eitem1.setInt((short) asd);
			eitem1.setLuk((short) asd);
			eitem1.setWatk((short) asd2);
			eitem1.setMatk((short) asd2);

			MapleRing.makeRing(itemid, chr, eitem.getUniqueId(), eitem1.getUniqueId());
			MapleRing.makeRing(itemid, getPlayer(), eitem1.getUniqueId(), eitem.getUniqueId());

			MapleInventoryManipulator.addbyItem(getClient(), item);
			MapleInventoryManipulator.addbyItem(chr.getClient(), item1);

			chr.reloadChar();
			c.getPlayer().reloadChar();
			sendOk("선택하신 반지를 제작 완료 하였습니다. 인벤토리를 확인해 봐주시길 바랍니다.");
			chr.dropMessage(5, getPlayer().getName() + "님으로 부터 반지가 도착 하였습니다. 인벤토리를 확인해 주시길 바랍니다.");
		} catch (Exception ex) {
			sendOk("반지를 제작하는데 오류가 발생 하였습니다.");
		}
	}

	public void MapiaStart(final MapleCharacter player, int time, final int morningmap, final int citizenmap1,
			final int citizenmap2, final int citizenmap3, final int citizenmap4, final int citizenmap5,
			final int citizenmap6, final int mapiamap, final int policemap, final int drmap, final int after,
			final int night, final int vote, int bating) {
		String[] job = { "미남", "미녀", "장미칼", "짜잔", "유리멘탈", "부처맨탈", "주인공", "[Happy oz!] 히로인", "[Happy oz!] 엑스트라",
				"[Happy oz!] 신", "[Happy oz!] 어그로", "[Happy oz!] 프라이데이", "[Happy oz!] 동정", "[Happy oz!] 히키코모리",
				"[Happy oz!] 간지인", "[Happy oz!] 멋쟁이", "[Happy oz!]야생의" };
		String name = "";
		String mapia = "";
		String police = "";

		int playernum = 0;
		int citizennumber = 0;

		final MapleMap map = ChannelServer.getInstance(getClient().getChannel()).getMapFactory().getMap(morningmap);
		for (MapleCharacter chr : player.getMap().getCharacters()) {
			playernum++;
		}
		int[] iNumber = new int[playernum];
		int i;

		for (i = 1; i <= iNumber.length; i++) {
			iNumber[i - 1] = i;
		}
		for (i = 0; i < iNumber.length; i++) {
			int iRandom = (int) (Math.random() * playernum);
			int t = iNumber[0];
			iNumber[0] = iNumber[iRandom];
			iNumber[iRandom] = t;
		}
		for (i = 0; i < iNumber.length; i++) {
			System.out.print(iNumber[i] + ",");
		}

		int jo = 0;

		map.names = "";
		map.mbating = bating * playernum;

		for (MapleCharacter chr : player.getMap().getCharacters()) {
			chr.warp(morningmap);
			map.names += chr.getName() + ",";
			chr.mapiajob = job[iNumber[jo] - 1];
			if (chr.mapiajob.equals("마피아")) {
				mapia = mapia + chr.getName() + ",";
			} else if (chr.mapiajob.equals("경찰")) {
				police = police + chr.getName() + ",";
			} else if (chr.mapiajob.equals("시민")) {
				citizennumber++;
			}
			chr.dropMessage(5, "잠시 후 마피아 게임이 시작됩니다. 총 배팅금은 " + (bating * playernum) + "메소 입니다.");
			chr.dropMessage(5, "당신의 직업은 " + job[iNumber[jo] - 1] + " 입니다.");
			chr.dropMessage(-1, time + "초 후 마피아 게임이 시작됩니다.");

			jo++;
		}
		final String mapialist = mapia;
		final String policelist = police;
		int citizennum = citizennumber;
		final int playernuma = playernum;
		final java.util.Timer m_timer = new java.util.Timer();

		TimerTask m_task = new TimerTask() {
			public void run() {
				for (MapleCharacter chr : player.getMap().getCharacters()) {
					if (chr.mapiajob == "마피아") {
						chr.isMapiaVote = true;
						chr.dropMessage(6, "마피아인 당신 동료는 " + mapialist + " 들이 있습니다. 밤이되면 같이 의논하여 암살할 사람을 선택해 주시기 바랍니다.");
					} else if (chr.mapiajob == "경찰") {
						chr.isPoliceVote = true;
						chr.dropMessage(6,
								"경찰인 당신 동료는 " + policelist + " 들이 있습니다. 밤이되면 마피아같다는 사람을 지목하면 마피아인지 아닌지를 알 수 있습니다.");
					} else if (chr.mapiajob == "의사") {
						chr.isDrVote = true;
						chr.dropMessage(6,
								"당신은 하나밖에 없는 의사입니다. 당신에게 부여된 임무는 시민과 경찰을 살리는 것입니다. 밤이되면 마피아가 지목했을것 같은 사람을 선택하면 살리실 수 있습니다.");

					} else if (chr.mapiajob == "시민") {
						chr.dropMessage(6, "당신은 시민입니다. 낮이되면 대화를 통해 마피아를 찾아내 투표로 처형시키면 됩니다.");
					}
					chr.getmapiavote = 0;
					chr.voteamount = 0;
					chr.getpolicevote = 0;
					chr.isDead = false;
					chr.isDrVote = true;
					chr.isMapiaVote = true;
					chr.isPoliceVote = true;
					chr.getdrvote = 0;
					chr.isVoting = false;
				}

				map.broadcastMessage(CWvsContext.serverNotice(1, "",
						"진행자>>낮이 되었습니다. 마피아를 찾아내 모두 처형하면 시민의 승리이며, 마피아가 경찰 또는 시민을 모두 죽일시 마피아의 승리입니다.(직업 : 시민,경찰,마피아,의사)"));

				map.playern = playernuma;
				map.morningmap = morningmap;

				map.aftertime = after;
				map.nighttime = night;
				map.votetime = vote;

				map.citizenmap1 = citizenmap1;
				map.citizenmap2 = citizenmap2;
				map.citizenmap3 = citizenmap3;
				map.citizenmap4 = citizenmap4;
				map.citizenmap5 = citizenmap5;
				map.citizenmap6 = citizenmap6;

				map.MapiaIng = true;

				map.mapiamap = mapiamap;
				map.policemap = policemap;
				map.drmap = drmap;
				m_timer.cancel();
				map.MapiaMorning(player);
				map.MapiaChannel = player.getClient().getChannel();
			}
		};
		m_timer.schedule(m_task, (time * 1000));
	}

	public void resetReactors() {
		getPlayer().getMap().resetReactors(c);
	}

	public void genericGuildMessage(int code) {
		c.getSession().writeAndFlush(CWvsContext.GuildPacket.genericGuildMessage((byte) code));
	}

	public void disbandGuild() {
		int gid = c.getPlayer().getGuildId();
		if (gid <= 0 || c.getPlayer().getGuildRank() != 1) {
			return;
		}
		World.Guild.disbandGuild(gid);
	}

	public void increaseGuildCapacity(boolean trueMax) {
		if (c.getPlayer().getMeso() < 500000 && !trueMax) {
			c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "500,000 메소가 필요합니다."));
			return;
		}
		final int gid = c.getPlayer().getGuildId();
		if (gid <= 0) {
			return;
		}
		if (World.Guild.increaseGuildCapacity(gid, trueMax)) {
			if (!trueMax) {
				c.getPlayer().gainMeso(-500000, true, true);
			}
			sendNext("증가되었습니다.");
		} else if (!trueMax) {
			sendNext("이미 한계치입니다. (Limit: 100)");
		} else {
			sendNext("이미 한계치입니다. (Limit: 200)");
		}
	}

	public void displayGuildRanks() {
		c.getSession().writeAndFlush(CWvsContext.GuildPacket.guildRankingRequest());
	}

	public boolean removePlayerFromInstance() {
		if (c.getPlayer().getEventInstance() != null) {
			c.getPlayer().getEventInstance().removePlayer(this.c.getPlayer());
			return true;
		}
		return false;
	}

	public boolean isPlayerInstance() {
		if (c.getPlayer().getEventInstance() != null) {
			return true;
		}
		return false;
	}

	public void changeStat(byte slot, int type, int amount) {
		Equip sel = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) slot);
		switch (type) {
		case 0:
			sel.setStr((short) amount);
			break;
		case 1:
			sel.setDex((short) amount);
			break;
		case 2:
			sel.setInt((short) amount);
			break;
		case 3:
			sel.setLuk((short) amount);
			break;
		case 4:
			sel.setHp((short) amount);
			break;
		case 5:
			sel.setMp((short) amount);
			break;
		case 6:
			sel.setWatk((short) amount);
			break;
		case 7:
			sel.setMatk((short) amount);
			break;
		case 8:
			sel.setWdef((short) amount);
			break;
		case 9:
			sel.setMdef((short) amount);
			break;
		case 10:
			sel.setAcc((short) amount);
			break;
		case 11:
			sel.setAvoid((short) amount);
			break;
		case 12:
			sel.setHands((short) amount);
			break;
		case 13:
			sel.setSpeed((short) amount);
			break;
		case 14:
			sel.setJump((short) amount);
			break;
		case 15:
			sel.setUpgradeSlots((byte) amount);
			break;
		case 16:
			sel.setViciousHammer((byte) amount);
			break;
		case 17:
			sel.setLevel((byte) amount);
			break;
		case 18:
			sel.setEnhance((byte) amount);
			break;
		case 19:
			sel.setPotential1(amount);
			break;
		case 20:
			sel.setPotential2(amount);
			break;
		case 21:
			sel.setPotential3(amount);
			break;
		case 22:
			sel.setPotential4(amount);
			break;
		case 23:
			sel.setPotential5(amount);
			break;
		case 24:
			sel.setOwner(getText());
			break;
		}
		c.getPlayer().equipChanged();
		c.getPlayer().fakeRelog();
	}

	public String searchCashItem(String t) {
		Pattern name2Pattern = Pattern.compile("^[가-힣a-zA-Z0-9]*$");

		if (!name2Pattern.matcher(t).matches()) {
			return "검색할 수 없는 아이템입니다.";
		}
		StringBuilder sb = new StringBuilder();

		for (Pair<Integer, String> item : (Iterable<Pair<Integer, String>>) MapleItemInformationProvider.getInstance()
				.getAllEquips()) {
			if (((String) item.right).contains(t)
					&& MapleItemInformationProvider.getInstance().isCash(((Integer) item.left).intValue())) {
				sb.append("#b#L" + item.left + "# #i" + item.left + "##t" + item.left + "##l\r\n");
			}
		}
		return sb.toString();
	}

	public void changeDamageSkin(int skinnum) {
		MapleQuest quest = MapleQuest.getInstance(7291);
		MapleQuestStatus queststatus = new MapleQuestStatus(quest, 1);

		String skinString = String.valueOf(skinnum);
		queststatus.setCustomData((skinString == null) ? "0" : skinString);

		getPlayer().updateQuest(queststatus, true);
		getPlayer().dropMessage(5, "데미지 스킨이 변경되었습니다.");
		getPlayer().getMap().broadcastMessage(getPlayer(), CField.showForeignDamageSkin(getPlayer(), skinnum), false);
	}

	public void openDuey() {
		c.getPlayer().setConversation(2);
		c.getSession().writeAndFlush(CField.sendDuey((byte) 9, null, null));
	}

	public void sendUI(int op) {
		c.getSession().writeAndFlush(CField.UIPacket.openUI(op));
	}

	public void sendRepairWindow() {
		c.getSession().writeAndFlush(CField.UIPacket.openUIOption(33, this.id));
	}

	public void sendNameChangeWindow() {
		c.getSession().writeAndFlush(CField.UIPacket.openUIOption(1110, 4034803));
	}

	public void sendProfessionWindow() {
		c.getSession().writeAndFlush(CField.UIPacket.openUI(42));
	}

	public final int getDojoPoints() {
		return dojo_getPts();
	}

	public final int getDojoRecord() {
		return c.getPlayer().getIntNoRecord(150101);
	}

	public void setDojoRecord(boolean reset) {
		if (reset) {
			c.getPlayer().getQuestNAdd(MapleQuest.getInstance(150101)).setCustomData("0");
			c.getPlayer().getQuestNAdd(MapleQuest.getInstance(150100)).setCustomData("0");
		} else {
			c.getPlayer().getQuestNAdd(MapleQuest.getInstance(150101))
					.setCustomData(String.valueOf(this.c.getPlayer().getIntRecord(150101) + 1));
		}
	}

	public boolean start_DojoAgent(boolean dojo, boolean party) {
		if (dojo) {
			return Event_DojoAgent.warpStartDojo(c.getPlayer(), party);
		}
		return Event_DojoAgent.warpStartAgent(c.getPlayer(), party);
	}

	public final short getKegs() {
		return c.getChannelServer().getFireWorks().getKegsPercentage();
	}

	public void giveKegs(int kegs) {
		c.getChannelServer().getFireWorks().giveKegs(c.getPlayer(), kegs);
	}

	public final short getSunshines() {
		return c.getChannelServer().getFireWorks().getSunsPercentage();
	}

	public void addSunshines(int kegs) {
		this.c.getChannelServer().getFireWorks().giveSuns(this.c.getPlayer(), kegs);
	}

	public final short getDecorations() {
		return this.c.getChannelServer().getFireWorks().getDecsPercentage();
	}

	public void addDecorations(int kegs) {
		try {
			this.c.getChannelServer().getFireWorks().giveDecs(this.c.getPlayer(), kegs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void maxStats() {
		Map<MapleStat, Long> statup = new EnumMap<>(MapleStat.class);

		(c.getPlayer().getStat()).str = Short.MAX_VALUE;
		(c.getPlayer().getStat()).dex = Short.MAX_VALUE;
		(c.getPlayer().getStat()).int_ = Short.MAX_VALUE;
		(c.getPlayer().getStat()).luk = Short.MAX_VALUE;

		int overrDemon = GameConstants.isDemonSlayer(c.getPlayer().getJob()) ? GameConstants.getMPByJob(c.getPlayer())
				: 500000;

		(c.getPlayer().getStat()).maxhp = 500000L;
		(c.getPlayer().getStat()).maxmp = overrDemon;
		c.getPlayer().getStat().setHp(500000L, this.c.getPlayer());
		c.getPlayer().getStat().setMp(overrDemon, this.c.getPlayer());

		statup.put(MapleStat.STR, Long.valueOf(32767L));
		statup.put(MapleStat.DEX, Long.valueOf(32767L));
		statup.put(MapleStat.LUK, Long.valueOf(32767L));
		statup.put(MapleStat.INT, Long.valueOf(32767L));
		statup.put(MapleStat.HP, Long.valueOf(500000L));
		statup.put(MapleStat.MAXHP, Long.valueOf(500000L));
		statup.put(MapleStat.MP, Long.valueOf(overrDemon));
		statup.put(MapleStat.MAXMP, Long.valueOf(overrDemon));

		c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
		c.getSession().writeAndFlush(CWvsContext.updatePlayerStats(statup, c.getPlayer()));
	}

	public boolean getSR(Triple<String, Map<Integer, String>, Long> ma, int sel) {
		if (((Map) ma.mid).get(Integer.valueOf(sel)) == null
				|| ((String) ((Map) ma.mid).get(Integer.valueOf(sel))).length() <= 0) {
			dispose();
			return false;
		}
		sendOk((String) ((Map) ma.mid).get(Integer.valueOf(sel)));
		return true;
	}

	public String getAllItem() {
		StringBuilder string = new StringBuilder();
		for (Item item : this.c.getPlayer().getInventory(MapleInventoryType.EQUIP).list()) {
			string.append("#L" + item.getUniqueId() + "##i " + item.getItemId() + "#\r\n");
		}
		return string.toString();
	}

	public Equip getEquip(int itemid) {
		return (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemid);
	}

	public void setExpiration(Object statsSel, long expire) {
		if (statsSel instanceof Equip) {
			((Equip) statsSel).setExpiration(System.currentTimeMillis() + expire * 24L * 60L * 60L * 1000L);
		}
	}

	public void setLock(Object statsSel) {
		if (statsSel instanceof Equip) {
			Equip eq = (Equip) statsSel;
			if (eq.getExpiration() == -1L) {
				eq.setFlag(eq.getFlag() | ItemFlag.LOCK.getValue());
			} else {
				eq.setFlag(eq.getFlag() | ItemFlag.UNTRADEABLE.getValue());
			}
		}
	}

	public boolean addFromDrop(Object statsSel) {
		if (statsSel instanceof Item) {
			Item it = (Item) statsSel;
			return (MapleInventoryManipulator.checkSpace(getClient(), it.getItemId(), it.getQuantity(), it.getOwner())
					&& MapleInventoryManipulator.addFromDrop(getClient(), it, false));
		}
		return false;
	}

	public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type) {
		return replaceItem(slot, invType, statsSel, offset, type, false);
	}

	public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type, boolean takeSlot) {

		MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);

		if (inv == null) {
			return false;
		}

		Item item = getPlayer().getInventory(inv).getItem((short) slot);

		if (item == null || statsSel instanceof Item) {
			item = (Item) statsSel;
		}

		if (offset > 0) {
			if (inv != MapleInventoryType.EQUIP) {
				return false;
			}

			Equip eq = (Equip) item;

			if (takeSlot) {
				if (eq.getUpgradeSlots() < 1) {
					return false;
				}
				eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() - 1));
				if (eq.getExpiration() == -1L) {
					eq.setFlag(eq.getFlag() | ItemFlag.LOCK.getValue());
				} else {
					eq.setFlag(eq.getFlag() | ItemFlag.UNTRADEABLE.getValue());
				}
			}
			if (type.equalsIgnoreCase("Slots")) {
				eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() + offset));
				eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
			} else if (type.equalsIgnoreCase("Level")) {
				eq.setLevel((byte) (eq.getLevel() + offset));
			} else if (type.equalsIgnoreCase("Hammer")) {
				eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
			} else if (type.equalsIgnoreCase("STR")) {
				eq.setStr((short) (eq.getStr() + offset));
			} else if (type.equalsIgnoreCase("DEX")) {
				eq.setDex((short) (eq.getDex() + offset));
			} else if (type.equalsIgnoreCase("INT")) {
				eq.setInt((short) (eq.getInt() + offset));
			} else if (type.equalsIgnoreCase("LUK")) {
				eq.setLuk((short) (eq.getLuk() + offset));
			} else if (type.equalsIgnoreCase("HP")) {
				eq.setHp((short) (eq.getHp() + offset));
			} else if (type.equalsIgnoreCase("MP")) {
				eq.setMp((short) (eq.getMp() + offset));
			} else if (type.equalsIgnoreCase("WATK")) {
				eq.setWatk((short) (eq.getWatk() + offset));
			} else if (type.equalsIgnoreCase("MATK")) {
				eq.setMatk((short) (eq.getMatk() + offset));
			} else if (type.equalsIgnoreCase("WDEF")) {
				eq.setWdef((short) (eq.getWdef() + offset));
			} else if (type.equalsIgnoreCase("MDEF")) {
				eq.setMdef((short) (eq.getMdef() + offset));
			} else if (type.equalsIgnoreCase("ACC")) {
				eq.setAcc((short) (eq.getAcc() + offset));
			} else if (type.equalsIgnoreCase("Avoid")) {
				eq.setAvoid((short) (eq.getAvoid() + offset));
			} else if (type.equalsIgnoreCase("Hands")) {
				eq.setHands((short) (eq.getHands() + offset));
			} else if (type.equalsIgnoreCase("Speed")) {
				eq.setSpeed((short) (eq.getSpeed() + offset));
			} else if (type.equalsIgnoreCase("Jump")) {
				eq.setJump((short) (eq.getJump() + offset));
			} else if (type.equalsIgnoreCase("ItemEXP")) {
				eq.setItemEXP(eq.getItemEXP() + offset);
			} else if (type.equalsIgnoreCase("Expiration")) {
				eq.setExpiration(eq.getExpiration() + offset);
			} else if (type.equalsIgnoreCase("Flag")) {
				eq.setFlag(eq.getFlag() + offset);
			}
			item = eq.copy();
		}
		MapleInventoryManipulator.removeFromSlot(getClient(), inv, (short) slot, item.getQuantity(), false);
		return MapleInventoryManipulator.addFromDrop(getClient(), item, false);
	}

	public boolean replaceItem(int slot, int invType, Object statsSel, int upgradeSlots) {
		return replaceItem(slot, invType, statsSel, upgradeSlots, "Slots");
	}

	public boolean isCash(int itemId) {
		return MapleItemInformationProvider.getInstance().isCash(itemId);
	}

	public int getTotalStat(int itemId) {
		return MapleItemInformationProvider.getInstance()
				.getTotalStat((Equip) MapleItemInformationProvider.getInstance().getEquipById(itemId));
	}

	public int getReqLevel(int itemId) {
		return MapleItemInformationProvider.getInstance().getReqLevel(itemId);
	}

	public SecondaryStatEffect getEffect(int buff) {
		return MapleItemInformationProvider.getInstance().getItemEffect(buff);
	}

	public void giveBuff(int skillid) {
		SkillFactory.getSkill(skillid).getEffect(1).applyTo(this.c.getPlayer());
	}

	public void buffGuild(int buff, int duration, String msg) {
		MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		if (ii.getItemEffect(buff) != null && getPlayer().getGuildId() > 0) {
			SecondaryStatEffect mse = ii.getItemEffect(buff);
			for (ChannelServer cserv : ChannelServer.getAllInstances()) {
				for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters().values()) {
					if (chr.getGuildId() == getPlayer().getGuildId()) {
						mse.applyTo(chr, chr, true, chr.getTruePosition(), duration, (byte) 0, true);
						chr.dropMessage(5, "Your guild has gotten a " + msg + " buff.");
					}
				}
			}
		}
	}

	public long getRemainPremium(int accid) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		long ret = 0L;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM premium WHERE accid = ?");
			ps.setInt(1, accid);
			rs = ps.executeQuery();
			if (rs.next()) {
				ret = rs.getLong("period");
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception exception) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception exception) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception exception) {
				}
			}
		}
		return ret;
	}

	public boolean existPremium(int aci) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		boolean ret = false;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM premium WHERE accid = ?");
			ps.setInt(1, aci);
			rs = ps.executeQuery();
			ret = rs.next();

			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception exception) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception exception) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception exception) {
				}
			}
		}
		return ret;
	}

	public void gainAllAccountPremium(int v3, int v4) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Integer> chrs = new ArrayList<>();

		Date adate = new Date();
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM accounts");
			rs = ps.executeQuery();

			while (rs.next()) {
				chrs.add(Integer.valueOf(rs.getInt("id")));
			}
			rs.close();
			ps.close();

			for (int i = 0; i < chrs.size(); i++) {
				if (existPremium(((Integer) chrs.get(i)).intValue())) {
					if (getRemainPremium(((Integer) chrs.get(i)).intValue()) > adate.getTime()) {
						ps = con.prepareStatement("UPDATE premium SET period = ? WHERE accid = ?");
						ps.setLong(1,
								getRemainPremium(((Integer) chrs.get(i)).intValue()) + (v3 * 24 * 60 * 60 * 1000));
						ps.setInt(2, ((Integer) chrs.get(i)).intValue());
						ps.executeUpdate();
						ps.close();
					} else {
						ps = con.prepareStatement(
								"UPDATE premium SET period = ? and `name` = ? and `buff` = ? WHERE accid = ?");
						ps.setLong(1, adate.getTime() + (v3 * 24 * 60 * 60 * 1000));
						ps.setString(2, "일반");
						ps.setInt(3, 80001535);
						ps.setInt(4, ((Integer) chrs.get(i)).intValue());
						ps.executeUpdate();
						ps.close();
					}
				} else {
					ps = con.prepareStatement("INSERT INTO premium(accid, name, buff, period) VALUES (?, ?, ?, ?)");
					ps.setInt(1, ((Integer) chrs.get(i)).intValue());
					ps.setString(2, "일반");
					ps.setInt(3, 80001535);
					ps.setLong(4, adate.getTime() + (v3 * 24 * 60 * 60 * 1000));
					ps.executeUpdate();
					ps.close();
				}
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception exception) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception exception) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception exception) {
				}
			}
		}
	}

	public void gainAccountPremium(String acc, int v3, boolean v4) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Date adate = new Date();

		int accid = 0;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM accounts WHERE name = ?");
			ps.setString(1, acc);
			rs = ps.executeQuery();

			if (rs.next()) {
				accid = rs.getInt("id");
			}

			rs.close();
			ps.close();

			if (existPremium(accid)) {
				if (getRemainPremium(accid) > adate.getTime()) {
					ps = con.prepareStatement("UPDATE premium SET period = ? WHERE accid = ?");
					if (v4) {
						ps.setLong(1, getRemainPremium(accid) + (v3 * 24 * 60 * 60 * 1000));
					} else {
						ps.setLong(1, getRemainPremium(accid) - (v3 * 24 * 60 * 60 * 1000));
					}
					ps.setInt(2, accid);
					ps.executeUpdate();
					ps.close();
				} else if (v4) {
					ps = con.prepareStatement(
							"UPDATE premium SET period = ? and `name` = ? and `buff` = ? WHERE accid = ?");
					ps.setLong(1, adate.getTime() + (v3 * 24 * 60 * 60 * 1000));
					ps.setString(2, "일반");
					ps.setInt(3, 80001535);
					ps.setInt(4, accid);
					ps.executeUpdate();
					ps.close();
				}
			} else if (v4) {
				ps = con.prepareStatement("INSERT INTO premium(accid, name, buff, period) VALUES (?, ?, ?, ?)");
				ps.setInt(1, accid);
				ps.setString(2, "일반");
				ps.setInt(3, 80001535);
				ps.setLong(4, adate.getTime() + (v3 * 24 * 60 * 60 * 1000));
				ps.executeUpdate();
				ps.close();
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception exception) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception exception) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception exception) {
				}
			}
		}
	}

	public boolean createAlliance(String alliancename) {
		MapleParty pt = this.c.getPlayer().getParty();
		MapleCharacter otherChar = this.c.getChannelServer().getPlayerStorage()
				.getCharacterById(pt.getMemberByIndex(1).getId());

		if (otherChar == null || otherChar.getId() == this.c.getPlayer().getId()) {
			return false;
		}
		try {
			return World.Alliance.createAlliance(alliancename, this.c.getPlayer().getId(), otherChar.getId(),
					this.c.getPlayer().getGuildId(), otherChar.getGuildId());
		} catch (Exception re) {
			re.printStackTrace();
			return false;
		}
	}

	public boolean addCapacityToAlliance() {
		try {
			MapleGuild gs = World.Guild.getGuild(this.c.getPlayer().getGuildId());
			if (gs != null && this.c.getPlayer().getGuildRank() == 1 && this.c.getPlayer().getAllianceRank() == 1
					&& World.Alliance.getAllianceLeader(gs.getAllianceId()) == this.c.getPlayer().getId()
					&& World.Alliance.changeAllianceCapacity(gs.getAllianceId())) {
				gainMeso(-10000000L);
				return true;
			}
		} catch (Exception re) {
			re.printStackTrace();
		}
		return false;
	}

	public boolean disbandAlliance() {
		try {
			MapleGuild gs = World.Guild.getGuild(this.c.getPlayer().getGuildId());
			if (gs != null && this.c.getPlayer().getGuildRank() == 1 && this.c.getPlayer().getAllianceRank() == 1
					&& World.Alliance.getAllianceLeader(gs.getAllianceId()) == this.c.getPlayer().getId()
					&& World.Alliance.disbandAlliance(gs.getAllianceId())) {
				return true;
			}
		} catch (Exception re) {
			re.printStackTrace();
		}
		return false;
	}

	public byte getLastMsg() {
		return this.lastMsg;
	}

	public final void setLastMsg(byte last) {
		this.lastMsg = last;
	}

	public final void maxAllSkills() {
		HashMap<Skill, SkillEntry> sa = new HashMap<>();
		for (Skill skil : SkillFactory.getAllSkills()) {
			if (GameConstants.isApplicableSkill(skil.getId()) && skil.getId() < 90000000) {
				sa.put(skil, new SkillEntry((byte) skil.getMaxLevel(), (byte) skil.getMaxLevel(),
						SkillFactory.getDefaultSExpiry(skil)));
			}
		}
		getPlayer().changeSkillsLevel(sa);
	}

	public final void resetStats(int str, int dex, int z, int luk) {
		this.c.getPlayer().resetStats(str, dex, z, luk);
	}

	public final boolean dropItem(int slot, int invType, int quantity) {
		MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
		if (inv == null) {
			return false;
		}
		return MapleInventoryManipulator.drop(this.c, inv, (short) slot, (short) quantity, true);
	}

	public final void setQuestRecord(Object ch, int questid, String data) {
		((MapleCharacter) ch).getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(data);
	}

	public final void doWeddingEffect(Object ch) {
		final MapleCharacter chr = (MapleCharacter) ch;
		final MapleCharacter player = getPlayer();
		getMap().broadcastMessage(CWvsContext.yellowChat(player.getName() + ", do you take " + chr.getName()
				+ " as your wife and promise to stay beside her through all downtimes, crashes, and lags?"));
		Timer.CloneTimer.getInstance().schedule(new Runnable() {
			public void run() {
				if (chr == null || player == null) {
					NPCConversationManager.this.warpMap(680000500, 0);
				} else {
					chr.getMap().broadcastMessage(CWvsContext.yellowChat(chr.getName() + ", do you take "
							+ player.getName()
							+ " as your husband and promise to stay beside him through all downtimes, crashes, and lags?"));
				}
			}
		}, 10000L);
		Timer.CloneTimer.getInstance().schedule(new Runnable() {
			public void run() {
				if (chr == null || player == null) {
					if (player != null) {
						NPCConversationManager.this.setQuestRecord(player, 160001, "3");
						NPCConversationManager.this.setQuestRecord(player, 160002, "0");
					} else if (chr != null) {
						NPCConversationManager.this.setQuestRecord(chr, 160001, "3");
						NPCConversationManager.this.setQuestRecord(chr, 160002, "0");
					}
					NPCConversationManager.this.warpMap(680000500, 0);
				} else {
					NPCConversationManager.this.setQuestRecord(player, 160001, "2");
					NPCConversationManager.this.setQuestRecord(chr, 160001, "2");
					NPCConversationManager.this.sendNPCText(
							player.getName() + " and " + chr.getName() + ", I wish you two all the best on your "
									+ chr.getClient().getChannelServer().getServerName() + " journey together!",
							9201002);
					chr.getMap().startExtendedMapEffect("You may now kiss the bride, " + player.getName() + "!",
							5120006);
					if (chr.getGuildId() > 0) {
						World.Guild.guildPacket(chr.getGuildId(), CWvsContext.sendMarriage(false, chr.getName()));
					}
					if (player.getGuildId() > 0) {
						World.Guild.guildPacket(player.getGuildId(), CWvsContext.sendMarriage(false, player.getName()));
					}
				}
			}
		}, 20000L);
	}

	public void putKey(int key, int type, int action) {
		getPlayer().changeKeybinding(key, (byte) type, action);
		getClient().getSession().writeAndFlush(CField.getKeymap(getPlayer().getKeyLayout(), getPlayer()));
	}

	public void logDonator(String log, int previous_points) {
		StringBuilder logg = new StringBuilder();

		logg.append(MapleCharacterUtil.makeMapleReadable(getPlayer().getName()));
		logg.append(" [CID: ").append(getPlayer().getId()).append("] ");
		logg.append(" [Account: ").append(MapleCharacterUtil.makeMapleReadable(getClient().getAccountName()))
				.append("] ");
		logg.append(log);
		logg.append(" [Previous: " + previous_points + "] [Now: " + getPlayer().getPoints() + "]");

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("INSERT INTO donorlog VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, MapleCharacterUtil.makeMapleReadable(getClient().getAccountName()));
			ps.setInt(2, getClient().getAccID());
			ps.setString(3, MapleCharacterUtil.makeMapleReadable(getPlayer().getName()));
			ps.setInt(4, getPlayer().getId());
			ps.setString(5, log);
			ps.setString(6, FileoutputUtil.CurrentReadable_Time());
			ps.setInt(7, previous_points);
			ps.setInt(8, getPlayer().getPoints());
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		FileoutputUtil.log("Log_Donator.rtf", logg.toString());
	}

	public void doRing(String name, int itemid) {
		PlayersHandler.DoRing(getClient(), name, itemid);
	}

	public int getNaturalStats(int itemid, String it) {
		Map<String, Integer> eqStats = MapleItemInformationProvider.getInstance().getEquipStats(itemid);
		if (eqStats != null && eqStats.containsKey(it)) {
			return ((Integer) eqStats.get(it)).intValue();
		}
		return 0;
	}

	public boolean isEligibleName(String t) {
		return (MapleCharacterUtil.canCreateChar(t, getPlayer().isGM())
				&& (!LoginInformationProvider.getInstance().isForbiddenName(t) || getPlayer().isGM()));
	}

	public String checkDrop(int mobId) {
		List<MonsterDropEntry> ranks = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobId);
		if (ranks != null && ranks.size() > 0) {
			int num = 0, itemId = 0, ch = 0;
			StringBuilder name = new StringBuilder();
			for (int i = 0; i < ranks.size(); i++) {
				MonsterDropEntry de = ranks.get(i);
				if (de.chance > 0 && (de.questid <= 0
						|| (de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0))) {
					itemId = de.itemId;
					if (num == 0) {
						name.append("Drops for #o" + mobId + "#\r\n");
						name.append("--------------------------------------\r\n");
					}
					String namez = "#z" + itemId + "#";

					if (itemId == 0) {
						itemId = 4031041;
						namez = (de.Minimum * getClient().getChannelServer().getMesoRate()) + " to "
								+ (de.Maximum * getClient().getChannelServer().getMesoRate()) + " meso";
					}
					ch = de.chance * getClient().getChannelServer().getDropRate();
					name.append((num + 1) + ") #v" + itemId + "#" + namez + " - "
							+ (Integer.valueOf((ch >= 999999) ? 1000000 : ch).doubleValue() / 10000.0D) + "% chance. "
							+ ((de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0)
									? ("Requires quest " + MapleQuest.getInstance(de.questid).getName()
											+ " to be started.")
									: "")
							+ "\r\n");
					num++;
				}
			}
			if (name.length() > 0) {
				return name.toString();
			}
		}
		return "No drops was returned.";
	}

	public String getLeftPadded(String in, char padchar, int length) {
		return StringUtil.getLeftPaddedStr(in, padchar, length);
	}

	public void handleDivorce() {
		if (getPlayer().getMarriageId() <= 0) {
			sendNext("Please make sure you have a marriage.");
			return;
		}
		int chz = World.Find.findChannel(getPlayer().getMarriageId());
		if (chz == -1) {
			Connection con = null;
			PreparedStatement ps = null;
			try {
				con = DatabaseConnection.getConnection();
				ps = con.prepareStatement(
						"UPDATE queststatus SET customData = ? WHERE characterid = ? AND (quest = ? OR quest = ?)");
				ps.setString(1, "0");
				ps.setInt(2, getPlayer().getMarriageId());
				ps.setInt(3, 160001);
				ps.setInt(4, 160002);
				ps.executeUpdate();
				ps.close();
				ps = con.prepareStatement("UPDATE characters SET marriageid = ? WHERE id = ?");
				ps.setInt(1, 0);
				ps.setInt(2, getPlayer().getMarriageId());
				ps.executeUpdate();
				ps.close();
				con.close();
			} catch (SQLException e) {
				outputFileError(e);
				return;
			} finally {
				try {
					if (con != null) {
						con.close();
					}
					if (ps != null) {
						ps.close();
					}

				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			setQuestRecord(getPlayer(), 160001, "0");
			setQuestRecord(getPlayer(), 160002, "0");
			getPlayer().setMarriageId(0);
			sendNext("You have been successfully divorced...");
			return;
		}
		if (chz < -1) {
			sendNext("Please make sure your partner is logged on.");
			return;
		}
		MapleCharacter cPlayer = ChannelServer.getInstance(chz).getPlayerStorage()
				.getCharacterById(getPlayer().getMarriageId());

		if (cPlayer != null) {
			cPlayer.dropMessage(1, "Your partner has divorced you.");
			cPlayer.setMarriageId(0);
			setQuestRecord(cPlayer, 160001, "0");
			setQuestRecord(getPlayer(), 160001, "0");
			setQuestRecord(cPlayer, 160002, "0");
			setQuestRecord(getPlayer(), 160002, "0");
			getPlayer().setMarriageId(0);
			sendNext("You have been successfully divorced...");
		} else {
			sendNext("An error occurred...");
		}
	}

	public String getReadableMillis(long startMillis, long endMillis) {
		return StringUtil.getReadableMillis(startMillis, endMillis);
	}

	public void sendUltimateExplorer() {
		getClient().getSession().writeAndFlush(CWvsContext.ultimateExplorer());
	}

	public void sendPendant(boolean b) {
		this.c.getSession().writeAndFlush(CWvsContext.pendantSlot(b));
	}

	public int getCompensation(String id) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM compensationlog_confirmed WHERE chrname = ?");
			ps.setString(1, id);
			rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt("value");
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			FileoutputUtil.outputFileError("Log_Script_Except.txt", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return 0;
	}

	public boolean deleteCompensation(String id) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("DELETE FROM compensationlog_confirmed WHERE chrname = ?");
			ps.setString(1, id);
			ps.executeUpdate();
			ps.close();
			con.close();
			return true;
		} catch (SQLException e) {
			FileoutputUtil.outputFileError("Log_Script_Except.txt", e);
			return false;
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}

			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public void gainAPS(int gain) {
		getPlayer().gainAPS(gain);
	}

	public void forceCompleteQuest(MapleCharacter chr, int idd) {
		MapleQuest.getInstance(idd).forceComplete(chr, getNpc());
	}

	public void openAuctionUI() {
		this.c.getSession().writeAndFlush(CField.UIPacket.openUI(161));
	}

	public void gainSponserItem(int item, String name, short allstat, short damage, byte upgradeslot) {
		if (GameConstants.isEquip(item)) {
			Equip Item = (Equip) MapleItemInformationProvider.getInstance().getEquipById(item);
			Item.setOwner(name);
			Item.setStr(allstat);
			Item.setDex(allstat);
			Item.setInt(allstat);
			Item.setLuk(allstat);
			Item.setWatk(damage);
			Item.setMatk(damage);
			Item.setUpgradeSlots(upgradeslot);
			MapleInventoryManipulator.addFromDrop(this.c, (Item) Item, false);
		} else {
			gainItem(item, allstat, damage);
		}
	}

	public void askAvatar(String text, List<Integer> args) {
		this.c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkStyle(this.id, text, args));
		this.lastMsg = 10;
	}

	public void SearchItem(String text, int type) {
		NPCConversationManager cm = this;
		if ((text.getBytes()).length < 4) {
			cm.sendOk("검색어는 두글자 이상으로 해주세요.");
			cm.dispose();
			return;
		}
		if (text.contains("헤어") || text.contains("얼굴")) {
			cm.sendOk("헤어, 얼굴 단어는 생략하고 검색해주세요.");
			cm.dispose();
			return;
		}
		String kk = "";
		String chat = "";
		String nchat = "";
		MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		int i = 0;
		for (Pair<Integer, String> item : (Iterable<Pair<Integer, String>>) ii.getAllEquips()) {
			if (((String) item.getRight()).toLowerCase().contains(text.toLowerCase())) {
				String color = "#b";
				String isuse = "";

				if (cm.getPlayer().getCashWishList().contains(item.getLeft())) {
					color = "#Cgray#";
					isuse = " (선택된 항목)";
				}
				if (type == 1 && ii.isCash(((Integer) item.getLeft()).intValue())
						&& ((Integer) item.getLeft()).intValue() >= 1000000
						&& ((Integer) item.getLeft()).intValue() / 1000000 == 1) {
					chat = chat + "\r\n" + color + "#L" + item.getLeft() + "##i" + item.getLeft() + " ##z"
							+ item.getLeft() + "#" + isuse;
					i++;
					continue;

				}
				if (type == 0 && ((Integer) item.getLeft()).intValue() / 10000 >= 2
						&& ((Integer) item.getLeft()).intValue() / 10000 < 3) {
					chat = chat + "\r\n" + color + "#L" + item.getLeft() + "##i" + item.getLeft() + " ##z"
							+ item.getLeft() + "#" + isuse;
					i++;
					continue;

				}
				if (type == 2 && ((Integer) item.getLeft()).intValue() / 10000 >= 3
						&& ((Integer) item.getLeft()).intValue() / 10000 <= 5) {
					chat = chat + "\r\n" + color + "#L" + item.getLeft() + "##i" + item.getLeft() + " ##z"
							+ item.getLeft() + "#" + isuse;
					i++;
				}
			}
		}
		if (i != 0) {
			kk = kk + "총 " + i + "개 검색되었습니다. 추가 하실 항목을 선택해주세요.";
			kk = kk + "\r\n#L0#항목 선택을 마칩니다.  \r\n#L1#항목을 재검색합니다.";
			nchat = kk + chat;
			cm.sendSimple(nchat);
		} else {
			kk = kk + "검색된 아이템이 없습니다.";
			cm.sendOk(kk);
			cm.dispose();
		}
	}

	public void sendPacket(String args) {
		this.c.getSession().writeAndFlush(PacketHelper.sendPacket(args));
	}

	public void enableMatrix() {
		MapleQuest quest = MapleQuest.getInstance(1465);
		MapleQuestStatus qs = this.c.getPlayer().getQuest(quest);

		if (quest != null && qs.getStatus() != 2) {
			qs.setStatus((byte) 2);
			this.c.getPlayer().updateQuest(this.c.getPlayer().getQuest(quest), true);
		}
	}

	public void gainCorebit(int g) {
		getPlayer().setKeyValue(1477, "count", String.valueOf(getPlayer().getKeyValue(1477, "count") + g));
	}

	public long getCorebit() {
		return getPlayer().getKeyValue(1477, "count");
	}

	public void setDeathcount(byte de) {
		c.getPlayer().setDeathCount(de);
		c.getSession().writeAndFlush(CField.getDeathCount(de));
	}

	public void UserSoulHandle(int selection) {
		for (List<Pair<Integer, MapleCharacter>> souls : (Iterable<List<Pair<Integer, MapleCharacter>>>) this.c
				.getChannelServer().getSoulmatch()) {
			this.c.getPlayer().dropMessageGM(6, "1");
			if (souls.size() == 1 && ((Integer) ((Pair) souls.get(0)).left).intValue() == 0 && selection == 0) {
				souls.add(new Pair(Integer.valueOf(selection), this.c.getPlayer()));
				this.c.getPlayer().dropMessageGM(6, "2 : " + souls.size());
				this.c.getSession().writeAndFlush(CWvsContext.onUserSoulMatching(selection, souls));
				return;
			}
		}
		this.c.getPlayer().dropMessageGM(6, "3");
		List<Pair<Integer, MapleCharacter>> chrs = new ArrayList<>();
		chrs.add(new Pair(Integer.valueOf(selection), this.c.getPlayer()));
		this.c.getSession().writeAndFlush(CWvsContext.onUserSoulMatching(selection, chrs));
		if (selection == 0) {
			this.c.getPlayer().dropMessageGM(6, "4");
			this.c.getChannelServer().getSoulmatch().add(chrs);
		}
	}

	public void startExpRate(int hour) {
		this.c.getSession().writeAndFlush(CField.getClock(hour * 60 * 60));

		ExpRating();

		Timer.MapTimer.getInstance().schedule(new Runnable() {
			public void run() {
				NPCConversationManager.this.warp(1000000);
			}
		}, (hour * 60 * 60 * 1000));
	}

	public void ExpRating() {
		Timer.BuffTimer.getInstance().schedule(new Runnable() {
			public void run() {
				if (NPCConversationManager.this.c.getPlayer().getMapId() == 925080000) {
					NPCConversationManager.this.c.getPlayer().gainExp(
							GameConstants.getExpNeededForLevel(NPCConversationManager.this.c.getPlayer().getLevel())
									/ 100L,
							true, false, false);
					NPCConversationManager.this.ExpRating();
				} else {
					NPCConversationManager.this.stopExpRate();
				}
			}
		}, 6000L);
	}

	public void stopExpRate() {
		c.getSession().writeAndFlush(CField.getClock(-1));
	}

	public int getFrozenMobCount() {
		return getPlayer().getLinkMobCount();
	}

	public void addFrozenMobCount(int a1) {
		int val = (getFrozenMobCount() + a1 > 9999) ? 9999 : (getFrozenMobCount() + a1);

		getPlayer().setLinkMobCount(val);
		getClient().getSession().writeAndFlush(SLFCGPacket.FrozenLinkMobCount(val));
		getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(1052230, 3500, "#face1# 몬스터수를 충전했어!", ""));
	}

	public void openWeddingPresent(int type, int gender) {
		MarriageDataEntry dataEntry = getMarriageAgent().getDataEntry();

		if (dataEntry != null) {
			if (type == 1) {
				List<String> wishes;
				c.getPlayer().setWeddingGive(gender);

				if (gender == 0) {
					wishes = dataEntry.getGroomWishList();
				} else {
					wishes = dataEntry.getBrideWishList();
				}
				c.getSession().writeAndFlush(CWvsContext.showWeddingWishGiveDialog(wishes));

			} else if (type == 2) {
				List<Item> gifts;
				if (gender == 0) {
					gifts = dataEntry.getGroomPresentList();
				} else {
					gifts = dataEntry.getBridePresentList();
				}
				c.getSession().writeAndFlush(CWvsContext.showWeddingWishRecvDialog(gifts));
			}
		}
	}

	public void ShowDreamBreakerRanking() {
		c.getSession().writeAndFlush(SLFCGPacket.DreamBreakerRanking(this.c.getPlayer().getName()));
	}

	public void gainDonationSkill(int skillid) {
		if (c.getPlayer().getKeyValue(201910, "DonationSkill") < 0) {
			c.getPlayer().setKeyValue(201910, "DonationSkill", "0");
		}

		MapleDonationSkill dskill = MapleDonationSkill.getBySkillId(skillid);
		if (dskill != null && (c.getPlayer().getKeyValue(201910, "DonationSkill") & dskill.getValue()) == 0) {
			int data = (int) c.getPlayer().getKeyValue(201910, "DonationSkill");
			data |= dskill.getValue();
			c.getPlayer().setKeyValue(201910, "DonationSkill", data + "");
			SkillFactory.getSkill(skillid).getEffect(SkillFactory.getSkill(skillid).getMaxLevel())
					.applyTo(c.getPlayer(), 0);
		}
	}

	public boolean hasDonationSkill(int skillid) {
		if (c.getPlayer().getKeyValue(201910, "DonationSkill") < 0) {
			c.getPlayer().setKeyValue(201910, "DonationSkill", "0");
		}

		MapleDonationSkill dskill = MapleDonationSkill.getBySkillId(skillid);
		if (dskill == null) {
			return false;
		} else if ((c.getPlayer().getKeyValue(201910, "DonationSkill") & dskill.getValue()) == 0) {
			return false;
		}
		return true;
	}

	public String getItemNameById(int itemid) {
		String itemname = "";
		for (Pair<Integer, String> itemPair : (Iterable<Pair<Integer, String>>) MapleItemInformationProvider
				.getInstance().getAllItems()) {
			if (((Integer) itemPair.getLeft()).intValue() == itemid) {
				itemname = (String) itemPair.getRight();
			}
		}
		return itemname;
	}

	public long getFWolfMeso() {
		if (this.c.getPlayer().getFWolfAttackCount() > 15) {
			long BaseMeso = 10000000L;
			long FWolfMeso = 0L;

			if (this.c.getPlayer().getFWolfDamage() >= 900000000000L) {
				FWolfMeso = BaseMeso * 100L;
			} else {
				float ratio = (float) (900000000000L / this.c.getPlayer().getFWolfDamage() * 100L);

				FWolfMeso = (long) ((float) BaseMeso * ratio);
			}
			return FWolfMeso;
		}
		return (100000 * this.c.getPlayer().getFWolfAttackCount());
	}

	public long getFWolfEXP() {
		long expneed = GameConstants.getExpNeededForLevel(this.c.getPlayer().getLevel());
		long exp = 0L;

		if (this.c.getPlayer().getFWolfDamage() >= 37500000000000L) {
			exp = (long) (expneed * 0.25D);
		} else if (this.c.getPlayer().getFWolfDamage() >= 6250000000000L) {
			exp = (long) (expneed * 0.2D);
		} else if (this.c.getPlayer().getFWolfDamage() >= 625000000000L) {
			exp = (long) (expneed * 0.15D);
		} else {
			exp = (long) (expneed * 0.1D);
		}
		if (this.c.getPlayer().isFWolfKiller()) {
			exp = (long) (expneed * 0.5D);
		}
		return exp;
	}

	public void showDimentionMirror() {
		c.getSession().writeAndFlush(CField.dimentionMirror(ServerConstants.mirrors));
	}

	public void warpNettPyramid(boolean hard) {
		MapleNettPyramid.warpNettPyramid(this.c.getPlayer(), hard);
	}

	public void startDamageMeter() {
		c.getPlayer().setDamageMeter(0);
		MapleMap map = c.getChannelServer().getMapFactory().getMap(120000102);
		map.killAllMonsters(false);
		warp(120000102);
		c.getSession().writeAndFlush(CField.getClock(30));
		c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9063152, 3000, "20초에 허수아비가 소환되고 측정이 시작됩니다.", ""));

		MapleMonster mob = MapleLifeFactory.getMonster(9305653);
		Timer.MapTimer.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				map.spawnMonsterOnGroundBelow(mob, new Point(-140, 150));
			}
		}, 5 * 1000);
		Timer.MapTimer.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				c.getPlayer().dropMessage(5, "누적 데미지 : " + c.getPlayer().getDamageMeter());
				updateDamageMeter(c.getPlayer(), c.getPlayer().getDamageMeter());
				warp(993236200);
			}
		}, 25 * 1000);
	}

	public static void updateDamageMeter(MapleCharacter chr, long damage) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("DELETE FROM damagemeter WHERE cid = ?");
			ps.setInt(1, chr.getId());
			ps.executeUpdate();
			ps.close();

			ps = con.prepareStatement("INSERT INTO damagemeter(cid, name, damage) VALUES (?, ?, ?)");
			ps.setInt(1, chr.getId());
			ps.setString(2, chr.getName());
			ps.setLong(3, damage);
			ps.executeUpdate();
			ps.close();
			con.close();
			chr.setDamageMeter(0);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException ex) {
			}
		}
	}

	public String getDamageMeterRank(int limit) {
		String text = "#fn나눔고딕 Extrabold##fs13# ";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM damagemeter ORDER BY damage DESC LIMIT " + limit);
			rs = ps.executeQuery();
			int i = 1;
			while (rs.next()) {
				text += (i != 10 ? " " : "") + i + "위 " + rs.getString("name") + " #r" + Comma(rs.getLong("damage"))
						+ "#e\r\n";
				i++;
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
			}
		}
		if (text.equals("#b")) {
			text = "#r아직까지 딜량 미터기를 갱신한 유저가 없습니다.";
		}
		return text;
	}

	public String DamageMeterRank() {
		String text = "#b";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM damagemeter ORDER BY damage DESC LIMIT 10");
			rs = ps.executeQuery();
			int i = 1;
			while (rs.next()) {
				text += "#r#e" + (i != 10 ? "0" : "") + i + "#n#b위 #r닉네임#b " + rs.getString("name") + " #r누적 데미지#b "
						+ Comma(rs.getLong("damage")) + "\r\n";
				i++;
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
			}
		}
		if (text.equals("#b")) {
			text = "#r아직까지 딜량 미터기를 갱신한 유저가 없습니다.";
		}
		return text;
	}

	public boolean isDamageMeterRanker(int cid) {
		boolean value = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM damagemeter ORDER BY damage DESC LIMIT 1");
			rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getInt("cid") == cid) {
					value = true;
				}
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
			}
		}
		return value;
	}

	public String Comma(long r) {
		String re = "";
		for (int i = String.valueOf(r).length(); i >= 1; i--) {
			if (i != 1 && i != String.valueOf(r).length() && i % 3 == 0) {
				re += ",";
			}
			re += String.valueOf(r).charAt(i - 1);

		}
		return new StringBuilder().append(re).reverse().toString();
	}

	public int getDamageMeterRankerId() {
		int value = -1;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM damagemeter ORDER BY damage DESC LIMIT 1");
			rs = ps.executeQuery();
			if (rs.next()) {
				value = rs.getInt("cid");
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
			}
		}
		return value;
	}

	public final String getDiscord() {
		return c.getDiscord();
	}

	public void Entertuto(boolean black) {
		Entertuto(black, true, false);
	}

	public void Entertuto(boolean black, boolean effect) {
		Entertuto(black, effect, false);
	}

	public void Entertuto(boolean black, boolean effect, boolean blackflame) {
		this.c.send(CField.UIPacket.getDirectionStatus(true));
		this.c.send(SLFCGPacket.SetIngameDirectionMode(true, blackflame, false, false));
		if (effect) {
			Timer.EtcTimer.getInstance().schedule(
					() -> this.c.getSession()
							.writeAndFlush(CField.showSpineScreen(false, false, false,
									"Effect/Direction18.img/effect/adele/spine/etc/7/skeleton", "new", 0, true, "00")),
					1000L);
		}
		Timer.EtcTimer.getInstance().schedule(() -> {
			if (black) {
				this.c.send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 1000, 0));
			} else {
				this.c.send(SLFCGPacket.MakeBlind(1, 200, 0, 0, 0, 1000, 0));
			}
		}, effect ? 4300L : 1000L);
		Timer.EtcTimer.getInstance().schedule(() -> {
			if (effect) {
				this.c.send(CField.showSpineScreen(false, true, false,
						"Effect/Direction18.img/effect/adele/spine/etc/5/skeleton", "new", 0, true, "5"));
				this.c.send(CField.showSpineScreen(false, true, false,
						"Effect/Direction18.img/effect/adele/spine/etc/6/skeleton", "new", 0, true, "6"));
			}
			this.c.send(SLFCGPacket.InGameDirectionEvent("", 1, 1000));
		}, effect ? 5000L : 1000L);

		Timer.EventTimer.getInstance().schedule(() -> {
			if (c.getPlayer() != null) {

				this.c.send(CField.UIPacket.getDirectionStatus(false));
				this.c.send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
			}
		}, 5000L);
	}

	public void Endtuto() {
		Endtuto(true);
	}

	public void Endtuto(boolean effect) {
		if (effect) {
			this.c.send(CField.endscreen("5"));
			this.c.send(CField.endscreen("6"));
			this.c.getSession().writeAndFlush(CField.showSpineScreen(false, false, false,
					"Effect/Direction18.img/effect/adele/spine/etc/7/skeleton", "new", 0, true, "00"));
		}
		Timer.EtcTimer.getInstance().schedule(() -> this.c.send(SLFCGPacket.MakeBlind(1, 0, 0, 0, 0, 1300, 0)),
				effect ? 3500L : 1000L);
		Timer.EtcTimer.getInstance().schedule(() -> {
			this.c.send(CField.UIPacket.getDirectionStatus(false));
			this.c.send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
		}, effect ? 5000L : 2000L);
	}

	public void sendScreenText(String str, boolean newwrite) {
		c.send(SLFCGPacket.InGameDirectionEvent(str, new int[] { 12, (newwrite == true) ? 1 : 0 }));
	}

	public void EnterMonsterPark(int mapid) {
		int count = 0;

		for (int i = mapid; i < mapid + 500; i += 100) {
			count += this.c.getChannelServer().getMapFactory().getMap(i).getNumSpawnPoints();
		}
		c.getPlayer().setMparkcount(count);
	}

	public void moru(Equip item, Equip item2) {
		if (item.getMoru() != 0) {
			item2.setMoru(item.getMoru());
		} else {
			String lol = Integer.valueOf(item.getItemId()).toString();
			String ss = lol.substring(3, 7);
			item2.setMoru(Integer.parseInt(ss));
		}
		c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getFusionAnvil(true, 5062400, 2028093));
		c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateEquipSlot((Item) item2));
	}

	public String EqpItem() {
		String info = "";
		int i = 0;

		for (Item item : this.c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)) {
			Equip Eqp = (Equip) item;
			if (Eqp != null) {
				if (Eqp.getMoru() > 0) {
					int itemid = Eqp.getItemId() / 10000 * 10000 + Eqp.getMoru();
					info = info + "#L" + Eqp.getItemId() + "# #i" + Eqp.getItemId() + "#  [ #i" + itemid + "# ]  #t"
							+ Eqp.getItemId() + "# #r(모루)#k#b\r\n";
				}
				i++;
			}
		}
		return info;
	}

	public void sendJobIlust(int type, boolean lumi) {
		if (this.lastMsg > -1) {
			return;
		}
		this.c.getSession().writeAndFlush(CField.NPCPacket.getIlust(this.id, type, lumi));
	}

	public boolean checkDayItem(String s, int type) {
		MapleCharacter chr = this.c.getPlayer();
		KoreaCalendar kc = new KoreaCalendar();
		String today = (kc.getYeal() % 100) + "/" + kc.getMonths() + "/" + kc.getDays();

		if (type == 0) {
			chr.addKV(s, today);
			return true;
		}
		if (type == 1) {
			if (chr.getV(s) != null) {
				String[] array = chr.getV(s).split("/");
				Calendar clear = new GregorianCalendar(Integer.parseInt("20" + array[0]),
						Integer.parseInt(array[1]) - 1, Integer.parseInt(array[2]));
				Calendar ocal = Calendar.getInstance();
				int yeal = clear.get(1), days = clear.get(5), day = ocal.get(7), day2 = clear.get(7),
						maxday = clear.getMaximum(5), month = clear.get(2);
				int check = (day2 == 5) ? 7 : ((day2 == 6) ? 6 : ((day2 == 7) ? 5 : 0));

				if (check == 0) {
					for (int i = day2; i < 5; i++) {
						check++;
					}
				}
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
				if (after.getTimeInMillis() > System.currentTimeMillis()) {
					return false;
				}
			}
		}
		return true;
	}

	public long ExpPocket(int type) {
		long t = 0L;
		long time = (System.currentTimeMillis() - Long.parseLong(this.c.getCustomData(247, "lastTime"))) / 1000L;
		if (time > 43200L) {
			time = 43200L;
		}
		long gainexp = time / 10L * GameConstants.ExpPocket(this.c.getPlayer().getLevel());
		if (type == 1) {
			t = time;
		} else {
			t = gainexp;
		}
		return t;
	}

	public void SelectQuest(String quest, int quest1, int quest2, int count) {
		List<Integer> QuestList = new ArrayList<>();
		List<Integer> SelectQuest = new ArrayList<>();

		for (int i = quest1; i < quest2; i++) {
			QuestList.add(Integer.valueOf(i));
		}

		while (SelectQuest.size() < count) {
			int questid = ((Integer) QuestList.get(Randomizer.rand(0, QuestList.size() - 1))).intValue();
			boolean no = false;

			switch (questid) {
			case 35566:
			case 35567:
			case 35568:
			case 35569:
			case 35583:
			case 35584:
			case 35585:
			case 35586:
			case 35587:
			case 35588:
			case 35589:
			case 39109:
			case 39110:
			case 39120:
			case 39128:
			case 39129:
			case 39130:
			case 39137:
			case 39138:
			case 39139:
			case 39140:
				no = true;
				break;
			default:
				no = false;
				break;
			}

			while (SelectQuest.contains(Integer.valueOf(questid)) || no) {
				questid = ((Integer) QuestList.get(Randomizer.rand(0, QuestList.size() - 1))).intValue();
				switch (questid) {
				case 35566:
				case 35567:
				case 35568:
				case 35569:
				case 35583:
				case 35584:
				case 35585:
				case 35586:
				case 35587:
				case 35588:
				case 35589:
				case 39109:
				case 39110:
				case 39120:
				case 39128:
				case 39129:
				case 39130:
				case 39137:
				case 39138:
				case 39139:
				case 39140:
					no = true;
					continue;
				}
				no = false;
			}
			SelectQuest.add(Integer.valueOf(questid));
		}
		String q = "";
		for (int j = 0; j < SelectQuest.size(); j++) {
			q = q + SelectQuest.get(j) + "";
			if (j != SelectQuest.size() - 1) {
				q = q + ",";
			}
		}
		c.getPlayer().addKV(quest, q);
	}

	public int ReplaceQuest(String quest, int quest1, int quest2, int anotherquest) {
		List<Integer> QuestList = new ArrayList<>();
		List<Integer> SelectQuest = new ArrayList<>();
		List<Integer> MyQuest = new ArrayList<>();

		for (int i = quest1; i < quest2; i++) {
			QuestList.add(Integer.valueOf(i));
		}

		String[] KeyValue = this.c.getPlayer().getV(quest).split(",");
		int j;

		for (j = 0; j < KeyValue.length; j++) {
			MyQuest.add(Integer.valueOf(Integer.parseInt(KeyValue[j])));
		}

		for (j = 0; j < KeyValue.length; j++) {
			if (Integer.parseInt(KeyValue[j]) != anotherquest) {
				SelectQuest.add(Integer.valueOf(Integer.parseInt(KeyValue[j])));
			}
		}
		int questid = ((Integer) QuestList.get(Randomizer.rand(0, QuestList.size() - 1))).intValue();
		while (true) {
			questid = ((Integer) QuestList.get(Randomizer.rand(0, QuestList.size() - 1))).intValue();
			boolean no = false;
			switch (questid) {
			case 35566:
			case 35567:
			case 35568:
			case 35569:
			case 35583:
			case 35584:
			case 35585:
			case 35586:
			case 35587:
			case 35588:
			case 35589:
			case 39109:
			case 39110:
			case 39120:
			case 39128:
			case 39129:
			case 39130:
			case 39137:
			case 39138:
			case 39139:
			case 39140:
				no = true;
				break;
			default:
				no = false;
				break;
			}
			if (questid != anotherquest && !SelectQuest.contains(Integer.valueOf(questid)) && !no) {
				SelectQuest.add(Integer.valueOf(questid));
				String q = "";
				for (int k = 0; k < SelectQuest.size(); k++) {
					q = q + SelectQuest.get(k) + "";
					if (k != SelectQuest.size() - 1) {
						q = q + ",";
					}
				}
				c.getPlayer().addKV(quest, q);
				return questid;
			}
		}
	}

	public void println(String msg) {
		System.out.println(msg);
	}

	public void cancelSkillsbuff() {
		for (Pair<SecondaryStat, SecondaryStatValueHolder> data : (Iterable<Pair<SecondaryStat, SecondaryStatValueHolder>>) this.c
				.getPlayer().getEffects()) {
			SecondaryStatValueHolder mbsvh = (SecondaryStatValueHolder) data.right;
			if (SkillFactory.getSkill(mbsvh.effect.getSourceId()) != null && mbsvh.effect.getSourceId() != 80002282
					&& mbsvh.effect.getSourceId() != 2321055) {
				this.c.getPlayer().cancelEffect(mbsvh.effect,
						Arrays.asList(new SecondaryStat[] { (SecondaryStat) data.left }));
			}
		}
	}

	// [othello]
	public void EnterFairy(boolean black) {
		EnterFairy(black, true, false);
	}

	public void EnterFairy(boolean black, boolean effect, boolean blackflame) {
		this.c.send(CField.UIPacket.getDirectionStatus(true));
		this.c.send(SLFCGPacket.SetIngameDirectionMode(true, blackflame, false, false)); // 모드변경
		if (effect) {
			Timer.EtcTimer.getInstance()
					.schedule(
							() -> this.c.getPlayer().getClient().getSession().writeAndFlush(CField.showSpineScreen(
									false, false, true, "Back/fairyAcademy.img/back/5", "animation", 0, false, "")),
							1000L);
		}
		Timer.EtcTimer.getInstance().schedule(() -> { // 타이머
			if (black) { // 화면 어둡게
				this.c.send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 1000, 0));
			} else {
				this.c.send(SLFCGPacket.MakeBlind(1, 200, 0, 0, 0, 1000, 0));

			}

		}, effect ? 1000L : 100L); // 시간초

		Timer.EtcTimer.getInstance().schedule(() -> {
			if (effect) { // 이미지 여기서 다 수정
				this.c.getPlayer().getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true,
						"Back/fairyAcademy.img/back/5", "animation", 0, false, ""));
			}
			this.c.send(SLFCGPacket.InGameDirectionEvent("", 1, 1000));
		}, effect ? 1000L : 1000L); // 시간초

		Timer.EventTimer.getInstance().schedule(() -> {
			if (c.getPlayer() != null) {

				this.c.send(CField.UIPacket.getDirectionStatus(false));
				this.c.send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
				c.getPlayer().warp(101070000); // 여기로 간다.
			}
		}, 5000L);

	}

	public void getJobName() {

	}

	public void sendPQRanking(byte type) {
		List<String> info = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			if (type == 0) {

				ps = con.prepareStatement("SELECT * FROM characters WHERE gm = 0 ORDER BY level DESC LIMIT 100");
				rs = ps.executeQuery();

				while (rs.next()) {
					info.add(rs.getString("name") + "," + "레벨 / " + rs.getString("level") + "," + "계승 레벨 / "
							+ rs.getString("fame") + "," + "("
							+ GameConstants.getJobNameById(Integer.parseInt(rs.getString("job"))) + ")");
				}
				ps.close();
				rs.close();
			}

			con.close();

		} catch (SQLException e) {
			System.err.println("Error while unbanning" + e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		c.getSession().writeAndFlush(CField.PartyRankingInfo(info));
	}

	public void StarForceEnchant25(Equip equip) {
		Equip nEquip = (Equip) equip;
		Equip zeroEquip = null;
		if (GameConstants.isAlphaWeapon(nEquip.getItemId())) {
			zeroEquip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
		} else if (GameConstants.isBetaWeapon(nEquip.getItemId())) {
			zeroEquip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
		}

		int maxEnhance, max = GameConstants.isStarForceScroll(2049372);
		boolean isSuperiol = ((MapleItemInformationProvider.getInstance().isSuperial(nEquip.getItemId())).left != null);
		int reqLevel = getReqLevel(nEquip.getItemId());
		if (reqLevel < 95) {
			maxEnhance = isSuperiol ? 3 : 5;
		} else if (reqLevel <= 107) {
			maxEnhance = isSuperiol ? 5 : 8;
		} else if (reqLevel <= 119) {
			maxEnhance = isSuperiol ? 8 : 10;
		} else if (reqLevel <= 129) {
			maxEnhance = isSuperiol ? 10 : 15;
		} else if (reqLevel <= 139) {
			maxEnhance = isSuperiol ? 12 : 20;
		} else {
			maxEnhance = isSuperiol ? 15 : 25;
		}

		if (maxEnhance < max) {
			max = maxEnhance;
		}

		while (nEquip.getEnhance() < max) {
			StarForceStats starForceStats = EquipmentEnchant.starForceStats(nEquip);
			nEquip.setEnchantBuff((short) 0);
			nEquip.setEnhance((byte) (nEquip.getEnhance() + 1));
			for (Pair<EnchantFlag, Integer> stat : starForceStats.getStats()) {
				if (EnchantFlag.Watk.check(((EnchantFlag) stat.left).getValue())) {
					nEquip.setEnchantWatk((short) (nEquip.getEnchantWatk() + ((Integer) stat.right).intValue()));
					if (zeroEquip != null) {
						zeroEquip.setEnchantWatk(
								(short) (zeroEquip.getEnchantWatk() + ((Integer) stat.right).intValue()));
					}
				}
				if (EnchantFlag.Matk.check(((EnchantFlag) stat.left).getValue())) {
					nEquip.setEnchantMatk((short) (nEquip.getEnchantMatk() + ((Integer) stat.right).intValue()));
					if (zeroEquip != null) {
						zeroEquip.setEnchantMatk(
								(short) (zeroEquip.getEnchantMatk() + ((Integer) stat.right).intValue()));
					}
				}
				if (EnchantFlag.Str.check(((EnchantFlag) stat.left).getValue())) {
					nEquip.setEnchantStr((short) (nEquip.getEnchantStr() + ((Integer) stat.right).intValue()));
					if (zeroEquip != null) {
						zeroEquip
								.setEnchantStr((short) (zeroEquip.getEnchantStr() + ((Integer) stat.right).intValue()));
					}
				}
				if (EnchantFlag.Dex.check(((EnchantFlag) stat.left).getValue())) {
					nEquip.setEnchantDex((short) (nEquip.getEnchantDex() + ((Integer) stat.right).intValue()));
					if (zeroEquip != null) {
						zeroEquip
								.setEnchantDex((short) (zeroEquip.getEnchantDex() + ((Integer) stat.right).intValue()));
					}
				}
				if (EnchantFlag.Int.check(((EnchantFlag) stat.left).getValue())) {
					nEquip.setEnchantInt((short) (nEquip.getEnchantInt() + ((Integer) stat.right).intValue()));
					if (zeroEquip != null) {
						zeroEquip
								.setEnchantInt((short) (zeroEquip.getEnchantInt() + ((Integer) stat.right).intValue()));
					}
				}
				if (EnchantFlag.Luk.check(((EnchantFlag) stat.left).getValue())) {
					nEquip.setEnchantLuk((short) (nEquip.getEnchantLuk() + ((Integer) stat.right).intValue()));
					if (zeroEquip != null) {
						zeroEquip
								.setEnchantLuk((short) (zeroEquip.getEnchantLuk() + ((Integer) stat.right).intValue()));
					}
				}
				if (EnchantFlag.Wdef.check(((EnchantFlag) stat.left).getValue())) {
					nEquip.setEnchantWdef((short) (nEquip.getEnchantWdef() + ((Integer) stat.right).intValue()));
					if (zeroEquip != null) {
						zeroEquip.setEnchantWdef(
								(short) (zeroEquip.getEnchantWdef() + ((Integer) stat.right).intValue()));
					}
				}
				if (EnchantFlag.Mdef.check(((EnchantFlag) stat.left).getValue())) {
					nEquip.setEnchantMdef((short) (nEquip.getEnchantMdef() + ((Integer) stat.right).intValue()));
					if (zeroEquip != null) {
						zeroEquip.setEnchantMdef(
								(short) (zeroEquip.getEnchantMdef() + ((Integer) stat.right).intValue()));
					}
				}
				if (EnchantFlag.Hp.check(((EnchantFlag) stat.left).getValue())) {
					nEquip.setEnchantHp((short) (nEquip.getEnchantHp() + ((Integer) stat.right).intValue()));
					if (zeroEquip != null) {
						zeroEquip.setEnchantHp((short) (zeroEquip.getEnchantHp() + ((Integer) stat.right).intValue()));
					}
				}
				if (EnchantFlag.Mp.check(((EnchantFlag) stat.left).getValue())) {
					nEquip.setEnchantMp((short) (nEquip.getEnchantMp() + ((Integer) stat.right).intValue()));
					if (zeroEquip != null) {
						zeroEquip.setEnchantMp((short) (zeroEquip.getEnchantMp() + ((Integer) stat.right).intValue()));
					}
				}
				if (EnchantFlag.Acc.check(((EnchantFlag) stat.left).getValue())) {
					nEquip.setEnchantAcc((short) (nEquip.getEnchantAcc() + ((Integer) stat.right).intValue()));
					if (zeroEquip != null) {
						zeroEquip
								.setEnchantAcc((short) (zeroEquip.getEnchantAcc() + ((Integer) stat.right).intValue()));
					}
				}
				if (EnchantFlag.Avoid.check(((EnchantFlag) stat.left).getValue())) {
					nEquip.setEnchantAvoid((short) (nEquip.getEnchantAvoid() + ((Integer) stat.right).intValue()));
					if (zeroEquip != null) {
						zeroEquip.setEnchantAvoid(
								(short) (zeroEquip.getEnchantAvoid() + ((Integer) stat.right).intValue()));
					}
				}
			}
		}

		EquipmentEnchant.checkEquipmentStats(c.getPlayer().getClient(), nEquip);

		if (zeroEquip != null) {
			EquipmentEnchant.checkEquipmentStats(c.getPlayer().getClient(), zeroEquip);
		}

		c.getPlayer().forceReAddItem(nEquip, MapleInventoryType.getByType((byte) 1));

		if (zeroEquip != null) {
			c.getPlayer().forceReAddItem(zeroEquip, MapleInventoryType.getByType((byte) 1));
		}
	}

	public String World_boss_team_check(int charid) {
		String teamname = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM world_boss_team WHERE characterid = ?");
			ps.setInt(1, charid);
			rs = ps.executeQuery();
			if (rs.next()) {
				teamname = rs.getString("teamname");
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			FileoutputUtil.outputFileError("Log_Script_Except.txt", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return teamname;
	}

	public void World_boss_team_insert(int teamid, String teamname, int accid, int charid, String charname) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement(
					"INSERT INTO world_boss_team(teamid, teamname, accid, characterid, charname) VALUES (?, ?, ?, ?, ?)");
			ps.setInt(1, teamid);
			ps.setString(2, teamname);
			ps.setInt(3, accid);
			ps.setInt(4, charid);
			ps.setString(5, charname);
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (SQLException e) {
			FileoutputUtil.outputFileError("Log_Script_Except.txt", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public void World_boss_team_update(int teamid, String teamname, int accid, int charid, String charname) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement(
					"UPDATE world_boss_team SET teamid = ?, teamname = ? where accid = ? and characterid = ? and charname = ?");
			ps.setInt(1, teamid);
			ps.setString(2, teamname);
			ps.setInt(3, accid);
			ps.setInt(4, charid);
			ps.setString(5, charname);
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (SQLException e) {
			FileoutputUtil.outputFileError("Log_Script_Except.txt", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

}

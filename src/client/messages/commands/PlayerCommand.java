package client.messages.commands;

import client.*;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.handler.MatrixHandler;
import handling.world.World;
import scripting.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.SecondaryStatEffect;
import server.life.MapleMonster;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.Pair;
import tools.StringUtil;
import tools.packet.CWvsContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PlayerCommand {

	public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
		return ServerConstants.PlayerGMRank.NORMAL;
	}

	public static class str extends DistributeStatCommands {
		public str() {
			stat = MapleStat.STR;
		}
	}

	public static class dex extends DistributeStatCommands {
		public dex() {
			stat = MapleStat.DEX;
		}
	}

	public static class ini extends DistributeStatCommands {
		public ini() {
			stat = MapleStat.INT;
		}
	}

	public static class luk extends DistributeStatCommands {
		public luk() {
			stat = MapleStat.LUK;
		}
	}

	public static class ap extends DistributeStatCommands {
		public ap() {
			stat = MapleStat.AVAILABLEAP;
		}
	}

	public static abstract class DistributeStatCommands extends CommandExecute {

		protected MapleStat stat = null;
		private static int statLim = 32767;

		private void setStat(MapleCharacter player, int amount) {
			switch (this.stat) {
			case STR:
				player.getStat().setStr((short) amount, player);
				player.updateSingleStat(MapleStat.STR, player.getStat().getStr());
				break;
			case DEX:
				player.getStat().setDex((short) amount, player);
				player.updateSingleStat(MapleStat.DEX, player.getStat().getDex());
				break;
			case INT:
				player.getStat().setInt((short) amount, player);
				player.updateSingleStat(MapleStat.INT, player.getStat().getInt());
				break;
			case LUK:
				player.getStat().setLuk((short) amount, player);
				player.updateSingleStat(MapleStat.LUK, player.getStat().getLuk());
				break;
			case AVAILABLEAP:
				player.setRemainingAp((short) 0);
				player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
				break;
			}
		}

		private int getStat(MapleCharacter player) {
			switch (this.stat) {
			case STR:
				return player.getStat().getStr();
			case DEX:
				return player.getStat().getDex();
			case INT:
				return player.getStat().getInt();
			case LUK:
				return player.getStat().getLuk();
			}
			throw new RuntimeException();
		}

		@Override
		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(-8, "資訊不正確.");
				return 0;
			}
			int change = 0;
			try {
				change = Integer.parseInt(splitted[1]);
			} catch (NumberFormatException nfe) {
				c.getPlayer().dropMessage(-8, "輸入錯誤.");
				return 0;
			}
			if (change <= 0) {
				c.getPlayer().dropMessage(-8, "必須輸入大於0的數位.");
				return 0;
			}
			if (c.getPlayer().getRemainingAp() < change) {
				c.getPlayer().dropMessage(-8, "必須輸入小於AP點的數位.");
				return 0;
			}
			if (getStat(c.getPlayer()) + change > statLim) {
				c.getPlayer().dropMessage(-8, statLim + " 您不能在以上内容上投資ap.");
				return 0;
			}
			setStat(c.getPlayer(), getStat(c.getPlayer()) + change);
			c.getPlayer().setRemainingAp((short) (c.getPlayer().getRemainingAp() - change));
			c.getPlayer().updateSingleStat(MapleStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
			c.getPlayer().dropMessage(-8,
					StringUtil.makeEnumHumanReadable(this.stat.name()) + " 新增了 " + change + " 内容.");
			return 1;
		}

	}

	public static abstract class OpenNPCCommand extends CommandExecute {

		protected int npc = -1;

		public int execute(MapleClient c, String[] splitted) {
			NPCScriptManager.getInstance().start(c, npcs[this.npc]);
			return 1;
		}

		private static int[] npcs = new int[] { 9000162, 9000000, 9010000 };
	}

	public static class pcc extends CommandExecute {// 统计并显示当前连接到服务器的玩家数量

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dropMessage(-8, "[通知] 正在連接到Fancy的淸單.");
			int ret = 0;
			int cashshop = 0;
			for (ChannelServer csrv : ChannelServer.getAllInstances()) {
				int a = csrv.getPlayerStorage().getAllCharacters().size();
				ret += a;
				c.getPlayer().dropMessage(6, csrv.getChannel() + "頻道 : " + a + "人\r\n");
			}
			ret += CashShopServer.getPlayerStorage().getAllCharacters().size();
			c.getPlayer().dropMessage(6,
					"現金商店 : " + CashShopServer.getPlayerStorage().getAllCharacters().size() + "人\r\n");
			ret += AuctionServer.getPlayerStorage().getAllCharacters().size();
			c.getPlayer().dropMessage(6,
					"拍賣場 : " + AuctionServer.getPlayerStorage().getAllCharacters().size() + "人\r\n");
			c.getPlayer().dropMessage(-8, "[Fancy] 用戶連接總數 : " + ret);
			return 1;
		}
	}

	public static class gwxx extends CommandExecute {

		public int execute(MapleClient c, String[] splitted) {
			MapleMonster mob = null;
			for (MapleMapObject monstermo : c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(),
					100000.0D, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
				mob = (MapleMonster) monstermo;
				if (mob.isAlive()) {
					c.getPlayer().dropMessage(6, "怪物資訊 :  " + mob.toString());
					break;
				}
			}
			if (mob == null) {
				c.getPlayer().dropMessage(6, "周圍沒有怪物.");
			}
			return 1;
		}
	}

	public static class grs extends CommandExecute {// 向玩家显示他们所在公会本周获得的公路分数

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dropMessage(6, "本周獲得的公會數量分數 : " + c.getPlayer().getGuild().getGuildScore());
			return 1;
		}
	}

	public static class gsp extends CommandExecute {// 公会技能

		public int execute(MapleClient c, String[] splitted) {
			if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() != 1) {
				c.getPlayer().dropMessage(1, "沒有行會或不是主行會.");
				return 1;
			}
			if (c.getPlayer().getGuild().getGuildScore() < 300) {
				c.getPlayer().dropMessage(1, "分數不足.");
				return 1;
			}
			c.getPlayer().getGuild().setGuildScore(c.getPlayer().getGuild().getGuildScore() - 300);
			Skill skilli = SkillFactory.getSkill(91001022);
			if (c.getPlayer().getGuildId() <= 0 || skilli == null) {
				return 1;
			}
			int eff = World.Guild.getSkillLevel(c.getPlayer().getGuildId(), skilli.getId());
			if (eff > skilli.getMaxLevel()) {
				return 1;
			}
			SecondaryStatEffect skillid = skilli.getEffect(eff);
			if (skillid.getReqGuildLevel() < 0) {
				return 1;
			}
			if (World.Guild.purchaseSkill(c.getPlayer().getGuildId(), skillid.getSourceId(), c.getPlayer().getName(),
					c.getPlayer().getId())) {
			}

			skilli = SkillFactory.getSkill(91001023);
			if (c.getPlayer().getGuildId() <= 0 || skilli == null) {
				return 1;
			}
			eff = World.Guild.getSkillLevel(c.getPlayer().getGuildId(), skilli.getId());
			if (eff > skilli.getMaxLevel()) {
				return 1;
			}
			skillid = skilli.getEffect(eff);
			if (skillid.getReqGuildLevel() < 0) {
				return 1;
			}
			if (World.Guild.purchaseSkill(c.getPlayer().getGuildId(), skillid.getSourceId(), c.getPlayer().getName(),
					c.getPlayer().getId())) {
			}
			skilli = SkillFactory.getSkill(91001024);
			if (c.getPlayer().getGuildId() <= 0 || skilli == null) {
				return 1;
			}
			eff = World.Guild.getSkillLevel(c.getPlayer().getGuildId(), skilli.getId());
			if (eff > skilli.getMaxLevel()) {
				return 1;
			}
			skillid = skilli.getEffect(eff);
			if (skillid.getReqGuildLevel() < 0) {
				return 1;
			}
			if (World.Guild.purchaseSkill(c.getPlayer().getGuildId(), skillid.getSourceId(), c.getPlayer().getName(),
					c.getPlayer().getId())) {
			}
			skilli = SkillFactory.getSkill(91001025);
			if (c.getPlayer().getGuildId() <= 0 || skilli == null) {
				return 1;
			}
			eff = World.Guild.getSkillLevel(c.getPlayer().getGuildId(), skilli.getId());
			if (eff > skilli.getMaxLevel()) {
				return 1;
			}
			skillid = skilli.getEffect(eff);
			if (skillid.getReqGuildLevel() < 0) {
				return 1;
			}
			if (World.Guild.purchaseSkill(c.getPlayer().getGuildId(), skillid.getSourceId(), c.getPlayer().getName(),
					c.getPlayer().getId())) {
			}

			return 1;
		}
	}

	public static class bosscs extends CommandExecute {// boss传送

		public int execute(MapleClient c, String[] splitted) {
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			NPCScriptManager.getInstance().start(c, 9062608, null);
			return 1;
		}
	}

	public static class mesoai extends OpenNPCCommand {// 显示玩家金币获取倍率

		public int execute(MapleClient c, String[] splitted) {
			StringBuilder String = new StringBuilder();
			int tear = (int) c.getPlayer().getKeyValue(9919, "MesoTear");
			if (tear < 0) {
				tear = 0;
			}
			int mesoR = 0;
			if (tear > 0) {
				mesoR += tear == 8 ? 300
						: tear == 7 ? 180
								: tear == 6 ? 120
										: tear == 5 ? 80 : tear == 4 ? 60 : tear == 3 ? 40 : tear == 2 ? 30 : 10;
			}
			String.append("金幣獲得量資訊（最高300.0%）：當前獲得量 ");
			String.append(c.getPlayer().getStat().mesoBuff);
			String.append("%                        默認爲100.0%");
			c.getPlayer().dropMessage(-8, String.toString());
			c.getPlayer().dropMessage(-8, "當前適用的奬勵方法獲得率 : " + mesoR);
			return 1;
		}
	}

	public static class dropt extends OpenNPCCommand {// 显示玩家的物品获取倍率

		public int execute(MapleClient c, String[] splitted) {
			StringBuilder String = new StringBuilder();
			int tear = (int) c.getPlayer().getKeyValue(9919, "DropTear");
			;
			if (tear < 0) {
				tear = 0;
			}
			int dropR = 0;
			if (tear > 0) {
				dropR += tear == 8 ? 300
						: tear == 7 ? 260
								: tear == 6 ? 220
										: tear == 5 ? 180 : tear == 4 ? 150 : tear == 3 ? 120 : tear == 2 ? 80 : 40;
			}
			String.append("物品獲得量資訊（最大400.0%）：當前");
			double dropBuff = c.getPlayer().getStat().dropBuff;
			if (!c.getPlayer().getBuffedValue(80002282)) {
				dropBuff -= c.getPlayer().getMap().getRuneCurseDecrease();
			}

			String.append(dropBuff);
			String.append("%                                  （默認爲100.0%，超過400.0%也無法收到效果。對BOSS怪物最多只適用300%）");
			c.getPlayer().dropMessage(-8, String.toString());
			c.getPlayer().dropMessage(-8, "當前適用的奬勵道具獲得率 : " + dropR);
			return 1;
		}
	}

	public static class jk extends CommandExecute {// 解卡

		public int execute(MapleClient c, String[] splitted) {
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			c.getPlayer().dropMessage(-8, "已重置.");
			c.getPlayer().setKeyValue(16700, "count", String.valueOf(300));
			// c.getPlayer().getClient().getSession().writeAndFlush(CField.UIPacket.detailShowInfo("의자에
			// 앉아 있을시 1분마다 5포인트가 자동으로 수급됩니다.", false));
			return 1;
		}
	}

	public static class usw extends CommandExecute {// 解除玩家装备的副武器 解除輔助武器

		public int execute(MapleClient c, String[] splitted) {
			Equip equip = null;
			equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
			if (equip == null) {
				c.getPlayer().dropMessage(1, "安裝中的輔助武器不存在.");
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return 1;
			}
			if (GameConstants.isZero(c.getPlayer().getJob())) {
				c.getPlayer().dropMessage(1, "Zero無法解鎖副武器.");
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return 1;
			}
			c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).removeSlot((short) -10);
			c.getSession().writeAndFlush(
					CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIPPED, equip));
			return 1;
		}
	}

	public static class esw extends CommandExecute {// 给玩家装备副手武器輔助武器

		public int execute(MapleClient c, String[] splitted) {
			int itemid = 0;
			switch (c.getPlayer().getJob()) {
			case 5100:
				itemid = 1098000;
				break;
			case 3100:
			case 3101:
				itemid = 1099000;
				break;
			case 6100:
				itemid = 1352500;
				break;
			case 6500:
				itemid = 1352600;
				break;
			}
			if (itemid != 0) {
				Item item = MapleInventoryManipulator.addId_Item(c, itemid, (short) 1, "", null, -1L, "", false);
				if (item != null) {
					MapleInventoryManipulator.equip(c, item.getPosition(), (short) -10, MapleInventoryType.EQUIP);
				} else {
					c.getPlayer().dropMessage(1, "發生錯誤.");
				}
			} else {
				c.getPlayer().dropMessage(1, "這是一個無法裝備副武器的職業組.");
			}
			return 1;
		}
	}

	public static class spd extends CommandExecute {// 保存玩家资料

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().saveToDB(false, false);
			c.getPlayer().dropMessage(-8, "已保存.");
			return 1;
		}
	}

	public static class ii extends CommandExecute {// 实现初始化（清空）玩家库存的功能

		public int execute(MapleClient c, String[] splitted) {
			Map<Pair<Short, Short>, MapleInventoryType> eqs = new HashMap<>();
			if (splitted[1].equals("7")) {
				for (MapleInventoryType type : MapleInventoryType.values()) {
					for (Item item : c.getPlayer().getInventory(type)) {
						eqs.put(new Pair<>(Short.valueOf(item.getPosition()), Short.valueOf(item.getQuantity())), type);
					}
				}
			} else if (splitted[1].equals("8")) {
				for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)) {
					eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())),
							MapleInventoryType.EQUIPPED);
				}
			} else if (splitted[1].equals("1")) {
				for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.EQUIP)) {
					eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())),
							MapleInventoryType.EQUIP);
				}
			} else if (splitted[1].equals("2")) {
				for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.USE)) {
					eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())),
							MapleInventoryType.USE);
				}
			} else if (splitted[1].equals("4")) {
				for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.SETUP)) {
					eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())),
							MapleInventoryType.SETUP);
				}
			} else if (splitted[1].equals("3")) {
				for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.ETC)) {
					eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())),
							MapleInventoryType.ETC);
				}
			} else if (splitted[1].equals("5")) {
				for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.CASH)) {
					eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())),
							MapleInventoryType.CASH);
				}
			} else if (splitted[1].equals("6")) {
				for (Item item2 : c.getPlayer().getInventory(MapleInventoryType.CODY)) {
					eqs.put(new Pair<>(Short.valueOf(item2.getPosition()), Short.valueOf(item2.getQuantity())),
							MapleInventoryType.CODY);
				}
			} else {
				c.getPlayer().dropMessage(6, "@ii [ 1 淸空裝備/2 淸空消費/3 淸空其他/4 淸空裝飾/5 淸空特殊/6 淸空現金/7 淸空穿戴/8 淸空全部]");
			}
			for (Map.Entry<Pair<Short, Short>, MapleInventoryType> eq : eqs.entrySet()) {
				MapleInventoryManipulator.removeFromSlot(c, eq.getValue(),
						((Short) ((Pair) eq.getKey()).left).shortValue(),
						((Short) ((Pair) eq.getKey()).right).shortValue(), false, false);
			}
			return 1;
		}
	}

	public static class ds extends CommandExecute {// 用于处理玩家捐赠技能（后援技能）的施放和取消

		@Override
		public int execute(MapleClient c, String[] splitted) {
			if (c.getPlayer().hasDonationSkill(5321054)) {
				if (!c.getPlayer().getBuffedValue(5321054)) {
					SkillFactory.getSkill(5321054).getEffect(SkillFactory.getSkill(5321054).getMaxLevel())
							.applyTo(c.getPlayer(), Integer.MAX_VALUE);
				} else {
					c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.Buckshot);
				}
			}
			if (c.getPlayer().hasDonationSkill(5121009)) {
				if (!c.getPlayer().getBuffedValue(5121009)) {
					SkillFactory.getSkill(5121009).getEffect(SkillFactory.getSkill(5121009).getMaxLevel())
							.applyTo(c.getPlayer(), Integer.MAX_VALUE);
				} else {
					c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.PartyBooster);
				}
			}
			if (c.getPlayer().hasDonationSkill(3121002)) {
				if (!c.getPlayer().getBuffedValue(3121002)) {
					SkillFactory.getSkill(3121002).getEffect(SkillFactory.getSkill(3121002).getMaxLevel())
							.applyTo(c.getPlayer(), Integer.MAX_VALUE);
				} else {
					c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.SharpEyes);
				}
			}
			if (c.getPlayer().hasDonationSkill(2311003)) {
				if (!c.getPlayer().getBuffedValue(2311003)) {
					SkillFactory.getSkill(2311003).getEffect(SkillFactory.getSkill(2311003).getMaxLevel())
							.applyTo(c.getPlayer(), Integer.MAX_VALUE);
				} else {
					c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.HolySymbol);
				}
			}
			if (c.getPlayer().hasDonationSkill(1311015)) {
				if (!c.getPlayer().getBuffedValue(1311015)) {
					SkillFactory.getSkill(1311015).getEffect(SkillFactory.getSkill(1311015).getMaxLevel())
							.applyTo(c.getPlayer(), Integer.MAX_VALUE);
				} else {
					c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.CrossOverChain);
				}
			}
			if (c.getPlayer().hasDonationSkill(4341002)) {
				if (!c.getPlayer().getBuffedValue(4341002)) {
					SkillFactory.getSkill(4341002).getEffect(SkillFactory.getSkill(4341002).getMaxLevel())
							.applyTo(c.getPlayer(), Integer.MAX_VALUE);
				} else {
					c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.FinalCut);
				}
			}
			c.getPlayer().dropMessage(-8, "擁有的贊助技能已設置.");
			return 1;
		}
	}

	public static class ar extends CommandExecute {// 自动寻路开关

		public int execute(MapleClient c, String[] splitted) {
			if (c.getPlayer().getKeyValue(12345, "AutoRoot") >= 0) {
				if (c.getPlayer().getKeyValue(12345, "AutoRoot") == 1) {
					c.getPlayer().setKeyValue(12345, "AutoRoot", "0");
					c.getPlayer().dropMessage(-8, "自動尋路 OFF");
				} else {
					c.getPlayer().setKeyValue(12345, "AutoRoot", "1");
					c.getPlayer().dropMessage(-8, "自動尋路 ON");
				}
			} else {
				c.getPlayer().dropMessage(-8, "您沒有許可權.");
			}
			return 1;
		}
	}

	public static class npc extends CommandExecute {// 打开npc 3005560

		public int execute(MapleClient c, String[] splitted) {
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			NPCScriptManager.getInstance().start(c, 9062008, null);
			return 1;
		}
	}

	public static class npc2 extends CommandExecute {// 打开npc 3005560

		public int execute(MapleClient c, String[] splitted) {
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			NPCScriptManager.getInstance().start(c, 3005560, "9062178");
			return 1;
		}
	}

	public static class npc1 extends CommandExecute {// 打开商店NPC

		public int execute(MapleClient c, String[] splitted) {
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			NPCScriptManager.getInstance().start(c, 3005560, "9062277");
			return 1;
		}
	}

	public static class npc3 extends CommandExecute {// 打开赞助npc

		public int execute(MapleClient c, String[] splitted) {
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			NPCScriptManager.getInstance().start(c, 9062884, null);
			return 1;
		}
	}

	public static class npc4 extends CommandExecute { // 打开NPC 9062884

		public int execute(MapleClient c, String[] splitted) {
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			NPCScriptManager.getInstance().start(c, 9062884, "3003167");
			return 1;
		}
	}

	public static class npc5 extends CommandExecute {// 打开仓库NPC

		public int execute(MapleClient c, String[] splitted) {
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			NPCScriptManager.getInstance().start(c, 9031016, null);
			return 1;
		}
	}

	public static class zy extends CommandExecute {

		public int execute(MapleClient c, String[] splitted) {
			if (GameConstants.isContentsMap(c.getPlayer().getMapId())) {
				c.getPlayer().dropMessage(-8, "該地圖無法移動.");
				return 0;
			}
			MapleMap mapz = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(ServerConstants.warpMap);
			c.getPlayer().setDeathCount((byte) 0);
			c.getPlayer().changeMap(mapz, mapz.getPortal(0));
			c.getPlayer().dispelDebuffs();
			(c.getPlayer()).Stigma = 0;
			Map<SecondaryStat, Pair<Integer, Integer>> dds = new HashMap<>();
			dds.put(SecondaryStat.Stigma, new Pair<>(Integer.valueOf((c.getPlayer()).Stigma), Integer.valueOf(0)));
			c.getSession().writeAndFlush(CWvsContext.BuffPacket.cancelBuff(dds, c.getPlayer()));
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
					CWvsContext.BuffPacket.cancelForeignBuff(c.getPlayer(), dds), false);
			c.getPlayer().addKV("bossPractice", "0");
			c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.DebuffIncHp);
			c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.FireBomb);
			return 1;
		}
	}

	public static class jy extends CommandExecute {// 去监狱

		public int execute(MapleClient c, String[] splitted) {
			if (GameConstants.isContentsMap(c.getPlayer().getMapId())) {
				c.getPlayer().dropMessage(-8, "該地圖無法移動.");
				return 0;
			}
			MapleMap mapz = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(993215603);
			c.getPlayer().setDeathCount((byte) 0);
			c.getPlayer().changeMap(mapz, mapz.getPortal(0));
			c.getPlayer().dispelDebuffs();
			(c.getPlayer()).Stigma = 0;
			Map<SecondaryStat, Pair<Integer, Integer>> dds = new HashMap<>();
			dds.put(SecondaryStat.Stigma, new Pair<>(Integer.valueOf((c.getPlayer()).Stigma), Integer.valueOf(0)));
			c.getSession().writeAndFlush(CWvsContext.BuffPacket.cancelBuff(dds, c.getPlayer()));
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
					CWvsContext.BuffPacket.cancelForeignBuff(c.getPlayer(), dds), false);
			c.getPlayer().addKV("bossPractice", "0");
			c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.DebuffIncHp);
			c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.FireBomb);
			return 1;
		}
	}

	public static class MaxSkill extends CommandExecute {// 技能大师 所有技能都掌握

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().skillMaster();
			return 1;
		}
	}

	public static class Couuuuuter extends CommandExecute {

		public int execute(MapleClient c, String[] splitted) {
			return 1;
		}
	}

	public static class Instruction extends help {
	}

	public static class help extends CommandExecute {

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dropMessage(-8, "@ii   [淸空 1 裝備/2 消費/3 其他/4 裝飾/5 特殊/6 現金/7 穿戴/8 全部]");
			c.getPlayer().dropMessage(-8, "@str  @dex  @ini  @luk  @ap [手動加點]");
			c.getPlayer().dropMessage(-8, "@jk        [用於NPC假死、物品等墨水匣解ka]");
			c.getPlayer().dropMessage(-8, "@ar        [自動尋路 ON，OFF.]");
			c.getPlayer().dropMessage(-8, "@zy        [前往村莊自由]");
			c.getPlayer().dropMessage(-8, "@spd       [保存當前角色]");
			c.getPlayer().dropMessage(-8, "@grs       [以計數公會確認分數]");
			c.getPlayer().dropMessage(-8, "@gsp       [設定公會技能]");
			c.getPlayer().dropMessage(-8, "@pcc       [確認當前連接到服務器的用戶數]");
			c.getPlayer().dropMessage(-8, "@npc       [加載贊助NPC]");
			c.getPlayer().dropMessage(-8, "@npc1      [加載商店NPC]");
			c.getPlayer().dropMessage(-8, "@npc2      [加載轉世NPC]]");
			c.getPlayer().dropMessage(-8, "@npc3      [加載後援NPC]");
			c.getPlayer().dropMessage(-8, "@npc4      [加載文宣NPC]");
			c.getPlayer().dropMessage(-8, "@npc5      [裝入倉庫NPC]");
			c.getPlayer().dropMessage(-8, "@dropt     [査看物品槪率]");
			c.getPlayer().dropMessage(-8, "@mesoai    [査看金幣槪率]");
			c.getPlayer().dropMessage(-8, "@MaxSkill  [所有技能都將掌握]");
			c.getPlayer().dropMessage(-8, "~할말       [全盤聊天]");
			// c.getPlayer().dropMessage(-8, "@명성치알림 [명성치 알림을 끄고 킵니다.]關閉並打開@聲望通知[聲望通知]");
			return 1;
		}
	}
}

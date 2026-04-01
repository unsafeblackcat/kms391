package client.messages.commands;

import client.*;
import client.inventory.*;
import client.messages.CommandProcessorUtil;
import constants.GameConstants;
import constants.ServerConstants;
import handling.MapleSaveHandler;
import handling.channel.ChannelServer;
import handling.channel.handler.BossUIHandler;
import handling.world.World;
import scripting.EventInstanceManager;
import scripting.EventManager;
import scripting.NPCConversationManager;
import scripting.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.events.MapleEvent;
import server.events.MapleEventType;
import server.life.MapleMonster;
import server.life.MobSkillFactory;
import server.life.Spawns;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleReactor;
import server.maps.MapleRune;
import server.polofritto.MapleRandomPortal;
import server.shops.MapleShopFactory;
import tools.FileoutputUtil;
import tools.StringUtil;
import tools.packet.CField;
import tools.packet.CWvsContext;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class GMCommand {

	public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
		return ServerConstants.PlayerGMRank.GM;
	}

	public static class SpawnRune extends CommandExecute {// 地图上生成一个指定ID的符文

		public int execute(MapleClient c, String[] splitted) {
			if (splitted[1] == null) {
				c.getPlayer().dropMessage(6, "語法: !SpawnRune <符文id>");
				return 0;
			}
			int id = Integer.parseInt(splitted[1]);
			int sppoint = Randomizer.rand(0, (c.getPlayer().getMap()).monsterSpawn.size() - 1);
			MapleReactor ract = c.getPlayer().getMap().getAllReactor().get(0);
			if (ract != null) {
				while (sppoint == ract.getSpawnPointNum()) {
					sppoint = Randomizer.rand(0, (c.getPlayer().getMap()).monsterSpawn.size() - 1);
				}
			}
			Point poss = ((Spawns) (c.getPlayer().getMap()).monsterSpawn.get(sppoint)).getPosition();
			MapleRune rune = new MapleRune(id, poss.x, poss.y, c.getPlayer().getMap());
			rune.setSpawnPointNum(sppoint);
			c.getPlayer().getMap().spawnRune(rune);
			return 1;
		}
	}

	public static class Buckshot extends CommandExecute {// 霰弹枪效果

		@Override
		public int execute(MapleClient c, String[] splitted) {
			if (c.getPlayer().getBuffedEffect(SecondaryStat.Buckshot) != null) {
				c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.Buckshot);
			} else {
				SkillFactory.getSkill(5321054).getEffect(1).applyTo(c.getPlayer(), 0);
			}
			return 0;
		}
	}

	public static class TTServer extends CommandExecute {// 测试服务器的连接器设置

		public int execute(MapleClient c, String[] splitted) {
			ServerConstants.ConnectorSetting = !ServerConstants.ConnectorSetting;
			System.out.println("測試服務器 " + (ServerConstants.ConnectorSetting ? "禁用" : "啟用") + " 完成.");
			return 1;
		}
	}

	public static class CashItem extends CommandExecute {// 道具现金类型查询指令

		public int execute(MapleClient c, String[] splitted) {
			MapleCharacter player = c.getPlayer();
			if (splitted.length < 1) {
				c.getPlayer().dropMessage(6, "語法: !CashItem <itemid>");
				return 0;
			}
			MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
			player.dropMessage(6, "該道具 " + (ii.isCash(Integer.parseInt(splitted[1])) ? "是現金道具." : "不是現金道具."));
			return 1;
		}
	}

	public static class Fame extends CommandExecute {// 增加玩家人氣度

		public int execute(MapleClient c, String[] splitted) {
			MapleCharacter player = c.getPlayer();
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(6, "語法: !fame <玩家> <數值>");
				return 0;
			}
			MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			int fame = 0;
			try {
				fame = Integer.parseInt(splitted[2]);
			} catch (NumberFormatException nfe) {
				c.getPlayer().dropMessage(6, "Invalid Number...");
				return 0;
			}
			if (victim != null && player.allowedToTarget(victim)) {
				victim.addFame(fame);
				victim.updateSingleStat(MapleStat.FAME, victim.getFame());
			}
			return 1;
		}
	}

	public static class wd1 extends CommandExecute {// 无敌状态切换指令

		public int execute(MapleClient c, String[] splitted) {
			MapleCharacter player = c.getPlayer();
			if (player.isInvincible()) {
				player.setInvincible(false);
				player.dropMessage(6, "已停用無敵.");
			} else {
				player.setInvincible(true);
				player.dropMessage(6, "已啟動無敵.");
			}
			return 1;
		}
	}

	// 在游戏内实时测试（GM权限）
	public static class BossUI extends CommandExecute {
		@Override
		public int execute(MapleClient c, String[] args) {// 語法: !BossUI <BOSS编号>
			int bossNum = Integer.parseInt(args[1]);
			NPCScriptManager.debugBossUI(c, bossNum);
			return 1;
		}
	}

	public static class SP extends CommandExecute {// 技能点（SP）调整指令

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().setRemainingSp(CommandProcessorUtil.getOptionalIntArg(splitted, 1, 1));
			c.getPlayer().updateSingleStat(MapleStat.AVAILABLESP, 0L);
			return 1;
		}
	}

	public static class ChangeJob extends CommandExecute {// 更改玩家职业变更指令

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().changeJob(Integer.parseInt(splitted[1]));
			return 1;
		}
	}

	public static class openshop extends CommandExecute {// 打开指定ID的商店

		public int execute(MapleClient c, String[] splitted) {
			MapleShopFactory shop = MapleShopFactory.getInstance();
			int shopId = Integer.parseInt(splitted[1]);
			if (shop.getShop(shopId) != null) {
				shop.getShop(shopId).sendShop(c);
			}
			return 1;
		}
	}

	public static class SkillLevel extends CommandExecute {// 技能等级调整

		public int execute(MapleClient c, String[] splitted) {
			Skill skill = SkillFactory.getSkill(Integer.parseInt(splitted[1]));
			byte level = (byte) CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);
			byte masterlevel = (byte) CommandProcessorUtil.getOptionalIntArg(splitted, 3, 1);
			if (level > skill.getMaxLevel()) {
				level = (byte) skill.getMaxLevel();
			}
			if (masterlevel > skill.getMasterLevel()) {
				masterlevel = (byte) skill.getMasterLevel();
			}
			c.getPlayer().changeSkillLevel(skill, level, masterlevel);
			return 1;
		}
	}

	public static class sj extends CommandExecute {// 角色升级指令

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length == 1) {
				if (c.getPlayer().getLevel() < 300) {
					c.getPlayer().gainExp(GameConstants.getExpNeededForLevel(c.getPlayer().getLevel()), true, false,
							true);
				}
			} else if (splitted.length == 2) {
				int lvup = Integer.parseInt(splitted[1]);
				for (int i = 0; i < lvup && c.getPlayer().getLevel() < 300; i++) {
					c.getPlayer().gainExp(GameConstants.getExpNeededForLevel(c.getPlayer().getLevel()), true, false,
							true);
				}
			}
			return 1;
		}
	}

	public static class SW extends CommandExecute {// 背包物品生成指令

		public int execute(MapleClient c, String[] splitted) {
			try {
				int itemId = Integer.parseInt(splitted[1]);
				short quantity = (short) CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);
				if (!c.getPlayer().isAdmin()) {
					for (int i : GameConstants.itemBlock) {
						if (itemId == i) {
							c.getPlayer().dropMessage(5, "該物品因GM等級不足而被遮罩.");
							return 0;
						}
					}
				}
				MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
				if (!ii.itemExists(itemId)) {
					c.getPlayer().dropMessage(5, itemId + "物品ID不存在.");
				} else {
					Item item;
					if (GameConstants.isPet(itemId)) {
						MapleInventoryManipulator.addId(c, itemId, (short) 1, "", MaplePet.createPet(itemId, -1L), 1L,
								"", false);
						String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
						NPCConversationManager.writeLog("Log/Command_Item.log", 
						    "[" + timestamp + "] " + c.getPlayer().getName()  + " | " + 
						    itemId + " (x" + quantity + ") 透過物品指令獲得\r\n",
						    true);
						return 1;
					}
					MapleInventoryType type = GameConstants.getInventoryType(itemId);
					if (type == MapleInventoryType.EQUIP || type == MapleInventoryType.CODY) {
						item = ii.getEquipById(itemId);
					} else {
						item = new Item(itemId, (short) 0, quantity, 0);
					}
					int flag = item.getFlag();
					if (ii.isCash(itemId)) {
						if (type == MapleInventoryType.EQUIP || type == MapleInventoryType.CODY) {
							flag |= ItemFlag.KARMA_EQUIP.getValue();
						} else {
							flag |= ItemFlag.KARMA_USE.getValue();
						}
						item.setUniqueId(MapleInventoryIdentifier.getInstance());
					}
					item.setFlag(flag);
					if (!c.getPlayer().isAdmin()) {
						item.setOwner(c.getPlayer().getName());
						item.setGMLog(c.getPlayer().getName() + " 語法 : !wp <物品ID>");
					}
					MapleInventoryManipulator.addbyItem(c,  item);
					NPCConversationManager.writeLog("Log/Command_Item.log", 
					    c.getPlayer().getName()  + " | " + itemId + " (x" + quantity + ") 透過指令獲得物品。\r\n",
					    true);
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 1;
		}
	}

	public static class dj extends CommandExecute {// 角色等级直接设置指令

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().setLevel(Short.parseShort(splitted[1]));
			c.getPlayer().levelUp();
			if (c.getPlayer().getExp() < 0L) {
				c.getPlayer().gainExp(-c.getPlayer().getExp(), false, false, true);
			}
			return 1;
		}
	}

	public static class StartRandomEvent extends CommandExecute {// 随机事件系统启动指令

		public int execute(MapleClient c, String[] splitted) {
			EventManager em = c.getChannelServer().getEventSM().getEventManager("AutomatedEvent");
			if (em != null) {
				em.scheduleRandomEvent();
			}
			return 1;
		}
	}

	public static class SetEvent extends CommandExecute {// 事件系统触发指令 啓動事件指令

		public int execute(MapleClient c, String[] splitted) {
			MapleEvent.onStartEvent(c.getPlayer());
			return 1;
		}
	}

	public static class StartEvent extends CommandExecute {// 事件地图开关控制指令

		public int execute(MapleClient c, String[] splitted) {
			if (c.getChannelServer().getEvent() == c.getPlayer().getMapId()) {
				MapleEvent.setEvent(c.getChannelServer(), false);
				c.getPlayer().dropMessage(5, "事件開始並結束");
				return 1;
			}
			c.getPlayer().dropMessage(5, "未執行!StartEvent指令預先設定事件地圖.");
			return 0;
		}
	}

	public static class ScheduleEvent extends CommandExecute {// 調試事件

		public int execute(MapleClient c, String[] splitted) {
			MapleEventType type = MapleEventType.getByString(splitted[1]);
			if (type == null) {
				StringBuilder sb = new StringBuilder("Wrong syntax: ");
				for (MapleEventType t : MapleEventType.values()) {
					sb.append(t.name()).append(",");
				}
				c.getPlayer().dropMessage(5, sb.toString().substring(0, sb.toString().length() - 1));
				return 0;
			}
			String msg = MapleEvent.scheduleEvent(type, c.getChannelServer());
			if (msg.length() > 0) {
				c.getPlayer().dropMessage(5, msg);
				return 0;
			}
			return 1;
		}
	}

	public static class RemoveItem extends CommandExecute {// 移除目标玩家背包内所有指定物品

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 3) {
				c.getPlayer().dropMessage(6, "語法：!RemoveItem <玩家名> <itemid>");
				return 0;
			}
			MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (chr == null) {
				c.getPlayer().dropMessage(6, "此玩家不存在");
				return 0;
			}
			chr.removeAll(Integer.parseInt(splitted[2]), false);
			c.getPlayer().dropMessage(6, "ID爲 " + splitted[2] + " 的所有項目已從 " + splitted[1] + "庫存中删除.");
			return 1;
		}
	}

	public static class KillMap extends CommandExecute {// 殺死全地图玩家 清除指令

		public int execute(MapleClient c, String[] splitted) {
			for (MapleCharacter map : c.getPlayer().getMap().getCharactersThreadsafe()) {
				if (map != null && !map.isGM()) {
					map.getStat().setHp(0L, map);
					map.getStat().setMp(0L, map);
					map.updateSingleStat(MapleStat.HP, 0L);
					map.updateSingleStat(MapleStat.MP, 0L);
				}
			}
			return 1;
		}
	}

	public static class SpeakMega extends CommandExecute {// 全服广播指令（超级喇叭）

		public int execute(MapleClient c, String[] splitted) {
			MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			World.Broadcast.broadcastSmega(
					CWvsContext.serverNotice(3, (victim == null) ? c.getChannel() : victim.getClient().getChannel(),
							(victim == null) ? "" : victim.getName(), (victim == null) ? splitted[1]
									: (victim.getName() + " : " + StringUtil.joinStringFrom(splitted, 2)),
							true));
			return 1;
		}
	}

	public static class Speak extends CommandExecute {// 模拟指定玩家发言指令

		public int execute(MapleClient c, String[] splitted) {
			MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (victim == null) {
				c.getPlayer().dropMessage(5, "找不到'" + splitted[1]);
				return 0;
			}
			victim.getMap().broadcastMessage(
					CField.getChatText(victim, StringUtil.joinStringFrom(splitted, 2), victim.isGM(), 0, null, -1, -1));
			return 1;
		}
	}

	public static class ApplyDebuff extends CommandExecute {// 玩家添加一个特定的减益效果（Debuff）

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().giveDebuff(SecondaryStat.Darkness, MobSkillFactory.getMobSkill(121, 1));
			return 1;
		}
	}

	public static class SetInstanceProperty extends CommandExecute {// 通过!SetInstanceProperty <事件名> <属性名> <属性值>动态修改副本属性

		public int execute(MapleClient c, String[] splitted) {
			EventManager em = c.getChannelServer().getEventSM().getEventManager(splitted[1]);
			if (em == null || em.getInstances().size() <= 0) {
				c.getPlayer().dropMessage(5, "none");
			} else {
				em.setProperty(splitted[2], splitted[3]);
				for (EventInstanceManager eim : em.getInstances()) {
					eim.setProperty(splitted[2], splitted[3]);
				}
			}
			return 1;
		}
	}

	public static class ListInstanceProperty extends CommandExecute {// 副本属性查询指令

		public int execute(MapleClient c, String[] splitted) {
			EventManager em = c.getChannelServer().getEventSM().getEventManager(splitted[1]);
			if (em == null || em.getInstances().size() <= 0) {
				c.getPlayer().dropMessage(5, "none");
			} else {
				for (EventInstanceManager eim : em.getInstances()) {
					c.getPlayer().dropMessage(5, "Event " + eim.getName() + ", eventManager: " + em.getName()
							+ " iprops: " + eim.getProperty(splitted[2]) + ", eprops: " + em.getProperty(splitted[2]));
				}
			}
			return 0;
		}
	}

	public static class LeaveInstance extends CommandExecute {// 强制退出副本的指令

		public int execute(MapleClient c, String[] splitted) {
			if (c.getPlayer().getEventInstance() == null) {
				c.getPlayer().dropMessage(5, "You are not in one");
			} else {
				c.getPlayer().getEventInstance().unregisterPlayer(c.getPlayer());
			}
			return 1;
		}
	}

	public static class WhosThere extends CommandExecute {// 列出当前地图上的所有玩家

		public int execute(MapleClient c, String[] splitted) {
			StringBuilder builder = (new StringBuilder("Players on Map: "))
					.append(c.getPlayer().getMap().getCharactersThreadsafe().size()).append(", ");
			for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
				if (builder.length() > 150) {
					builder.setLength(builder.length() - 2);
					c.getPlayer().dropMessage(6, builder.toString());
					builder = new StringBuilder();
				}
				builder.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
				builder.append(", ");
			}
			builder.setLength(builder.length() - 2);
			c.getPlayer().dropMessage(6, builder.toString());
			return 1;
		}
	}

	public static class StartInstance extends CommandExecute {// 将玩家加入到指定的事件实例中

		public int execute(MapleClient c, String[] splitted) {
			if (c.getPlayer().getEventInstance() != null) {
				c.getPlayer().dropMessage(5, "You are in one");
			} else if (splitted.length > 2) {
				EventManager em = c.getChannelServer().getEventSM().getEventManager(splitted[1]);
				if (em == null || em.getInstance(splitted[2]) == null) {
					c.getPlayer().dropMessage(5, "Not exist");
				} else {
					em.getInstance(splitted[2]).registerPlayer(c.getPlayer());
				}
			} else {
				c.getPlayer().dropMessage(5, "!startinstance [事件管理器名] [副本名]");
			}
			return 1;
		}
	}

	public static class ResetMobs extends CommandExecute {// 地图怪物重置指令

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getMap().killAllMonsters(false);
			return 1;
		}
	}

	public static class KillMobID extends CommandExecute {// 击杀指定怪物 精准击杀指定怪物 !KillMobID <mobID>

		public int execute(MapleClient c, String[] splitted) {
			MapleMap map = c.getPlayer().getMap();
			int targetId = Integer.parseInt(splitted[1]);
			MapleMonster monster = map.getMonsterByOid(targetId);
			if (monster != null) {
				map.killMonster(monster, c.getPlayer(), false, false, (byte) 1);
			}
			return 1;
		}
	}

	public static class ResetMapNpc extends CommandExecute {// 重置当前地图所有NPC

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getMap().resetNPCs();
			return 1;
		}
	}

	public static class NoticeUtils extends CommandExecute {// 公告类型解析工具方法 返回对应的公告类型编号

		protected static int getNoticeType(String typestring) {
			if (typestring.equals("n")) {
				return 0;
			}
			if (typestring.equals("p")) {
				return 1;
			}
			if (typestring.equals("l")) {
				return 2;
			}
			if (typestring.equals("nv")) {
				return 5;
			}
			if (typestring.equals("v")) {
				return 5;
			}
			if (typestring.equals("b")) {
				return 6;
			}
			return -1;
		}

		public int execute(MapleClient c, String[] splitted) {
			int joinmod = 1;
			int range = -1;
			if (splitted[1].equals("m")) {
				range = 0;
			} else if (splitted[1].equals("c")) {
				range = 1;
			} else if (splitted[1].equals("w")) {
				range = 2;
			}
			int tfrom = 2;
			if (range == -1) {
				range = 2;
				tfrom = 1;
			}
			int type = getNoticeType(splitted[tfrom]);
			if (type == -1) {
				type = 0;
				joinmod = 0;
			}
			StringBuilder sb = new StringBuilder();
			if (splitted[tfrom].equals("nv")) {
				sb.append("[Notice]");
			} else {
				sb.append("");
			}
			joinmod += tfrom;
			sb.append(StringUtil.joinStringFrom(splitted, joinmod));
			World.Broadcast.broadcastMessage(CWvsContext.serverNotice(type, "", sb.toString()));
			World.Broadcast.broadcastMessage(CField.UIPacket.detailShowInfo(sb.toString(), false));
			return 1;
		}
	}

	public static class WhatsMyIP extends CommandExecute {// 获取玩家客户端的IP地址

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dropMessage(5, "IP: " + c.getSession().remoteAddress().toString().split(":")[0]);
			return 1;
		}
	}

	public static class TDrops extends CommandExecute {// 地图掉落物开关指令

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getMap().toggleDrops();
			return 1;
		}
	}

	public static class rb extends CommandExecute {// 角色重生指令

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().doReborn();
			return 1;
		}
	}

	public static class ReactorDebug extends CommandExecute {// 显示地图上所有反應堆

		public int execute(MapleClient c, String[] splitted) {
			for (MapleMapObject reactor1l : c.getPlayer().getMap().getAllReactorsThreadsafe()) {
				MapleReactor reactor2l = (MapleReactor) reactor1l;
				c.getPlayer().dropMessage(5,
						"Reactor: oID: " + reactor2l.getObjectId() + " reactorID: " + reactor2l.getReactorId()
								+ " Position: " + reactor2l.getPosition().toString() + " State: " + reactor2l.getState()
								+ " Name: " + reactor2l.getName());
			}
			return 0;
		}
	}

	public static class RewardSuns extends CommandExecute {// 通过!RewardSuns <范围值> 为玩家添加经验增益效果
		public int execute(MapleClient c, String[] splitted) {
			int irange = Integer.parseInt(splitted[1]);
			ChannelServer.getInstance(1).getFireWorks().giveSuns(c.getPlayer(), irange);
			return 1;
		}
	}

	public static class CopyEqu extends CommandExecute {// 装备属性复制指令
		public int execute(MapleClient c, String[] splitted) {

			Equip nEquip2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 2);
			if (nEquip2 == null) {
				c.getPlayer().dropMessage(5, "沒有可複製的裝備.");
				return 1;
			}
			Equip nEquip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1);
			Equip nEquip3 = nEquip;
			Equip neq = (Equip) nEquip3.copy();
			if (nEquip != null) {
				neq.setItemId(nEquip2.getItemId());
				neq.setPosition((short) c.getPlayer().getInventory((byte) 1).getNumFreeSlot());
				MapleInventoryManipulator.addFromDrop(c, neq, true);

			} else {
				c.getPlayer().dropMessage(5, "沒有裝備道具.");
			}

			return 1;
		}
	}

	public static class CopyEquipPT extends CommandExecute {// 装备潜能属性复制指令
		public int execute(MapleClient c, String[] splitted) {

			Equip nEquip2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 2);
			if (nEquip2 == null) {
				c.getPlayer().dropMessage(5, "沒有可複製的裝備.");
				return 1;
			}
			Equip nEquip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1);
			if (nEquip != null) {
				nEquip2.setState(nEquip.getState());
				nEquip2.setPotential1(nEquip.getPotential1());
				nEquip2.setPotential2(nEquip.getPotential2());
				nEquip2.setPotential3(nEquip.getPotential3());
				nEquip2.setPotential4(nEquip.getPotential4());
				nEquip2.setPotential5(nEquip.getPotential5());
				nEquip2.setPotential6(nEquip.getPotential6());
				c.getPlayer().forceReAddItem(nEquip2, MapleInventoryType.EQUIP);
			} else {
				c.getPlayer().dropMessage(5, "沒有裝備道具.");
			}

			return 1;
		}
	}

	public static class CopyEquipment extends CommandExecute {// 装备复制指令 检查装备栏第2格是否为空（非空则报错） 将第1格装备复制到背包空闲位置
		public int execute(MapleClient c, String[] splitted) {

			Equip nEquip2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 2);
			if (nEquip2 != null) {
				c.getPlayer().dropMessage(5, "裝備2格不是空槽位.");
				return 1;
			}
			Equip nEquip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1);
			if (nEquip != null) {
				MapleInventoryManipulator.addFromDrop(c, nEquip, true);
			} else {
				c.getPlayer().dropMessage(5, "裝備1格沒有裝備道具.");
			}

			return 1;
		}
	}

	public static class ToggleSkillEffect extends CommandExecute {// 最大伤害（Max Damage）BUFF的开关指令

		public int execute(MapleClient c, String[] splitted) {
			if (c.getPlayer().getBuffedEffect(80002924) != null) {
				c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(80002924));
			} else {
				SkillFactory.getSkill(80002924).getEffect(1).applyTo(c.getPlayer(), 0);
				c.getPlayer().dropMessage(5, "[技能設定]");
			}
			return 0;
		}
	}

	public static class wd extends CommandExecute {// 無敵

		public int execute(MapleClient c, String[] splitted) {
			if (c.getPlayer().getBuffedValue(1221054)) {
				c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.IndieNotDamaged, 1221054);
			} else {
				SkillFactory.getSkill(1221054).getEffect(1).applyTo(c.getPlayer(), 0);
			}
			return 0;
		}
	}

	public static class SetAttackSkill extends CommandExecute {// 设置玩家的攻击技能为某个特定的buff

		public int execute(MapleClient c, String[] splitted) {
			int getBuff = Integer.parseInt(splitted[1]);
			c.getPlayer().setAttackerSkill(getBuff);
			return 0;
		}
	}

	public static class SpawnRPortal extends CommandExecute {// 在玩家的当前地图上生成一个随机传送门

		public int execute(MapleClient c, String[] splitted) {
			MapleRandomPortal portal = new MapleRandomPortal(Integer.parseInt(splitted[1]),
					c.getPlayer().getTruePosition(), c.getPlayer().getMapId(), c.getPlayer().getId(),
					Randomizer.nextBoolean());
			c.getPlayer().getMap().spawnRandomPortal(portal);
			return 0;
		}
	}

	public static class TChatBan extends CommandExecute {// 玩家聊天禁言管理指令

		public int execute(MapleClient c, String[] splitted) {
			String name = splitted[1];
			MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
			if (chr != null) {
				chr.canTalk(!chr.getCanTalk());
			}
			c.getPlayer().dropMessage(6, "대상 채팅 " + (chr.getCanTalk() ? "금지" : "해제") + " 완료.");
			chr.dropMessage(1, c.getPlayer().getName() + "帳號因違規被限制聊天功能.");
			return 0;
		}
	}

	public static class RACSaves extends CommandExecute {// 全频道数据重置指令

		public int execute(MapleClient c, String[] splitted) {
			Iterator<ChannelServer> channels = ChannelServer.getAllInstances().iterator();
			MapleSaveHandler.reset(channels);
			return 1;
		}
	}

	public static class TRRR extends CommandExecute {// 彩虹拉什無敵

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().setNoDeadRush(!c.getPlayer().isNoDeadRush());
			c.getPlayer().dropMessageGM(6, "彩虹拉什無敵 " + (c.getPlayer().isNoDeadRush() ? "應用" : "禁用") + "成功.");
			return 1;
		}
	}

	public static class TRR extends CommandExecute {// 彩虹冲刺（Rainbow Rush）技能触发指令

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().runRainBowRush();
			return 1;
		}
	}

	public static class BanC1 extends InternCommand.BanC {
	}

	public static class TempBan1 extends InternCommand.TempBan {
	}
}

package client.messages.commands;

import client.*;
import client.inventory.*;
import client.messages.CommandProcessorUtil;
import constants.GameConstants;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.RecvPacketOpcode;
import handling.SendPacketOpcode;
import handling.channel.ChannelServer;
import handling.channel.handler.InterServerHandler;
import handling.login.handler.AutoRegister;
import handling.world.World;
import io.netty.buffer.Unpooled;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.NPCScriptManager;
import scripting.PortalScriptManager;
import scripting.ReactorScriptManager;
import server.*;
import server.Timer;
import server.games.BattleGroundGameHandler;
import server.games.BloomingRace;
import server.life.*;
import server.maps.*;
import server.quest.MapleQuest;
import server.shops.MapleShopFactory;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.StringUtil;
import tools.Triple;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;
import tools.packet.PacketHelper;
import tools.wztosql.DumpQuests;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.*;

public class SuperGMCommand {

	public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
		return ServerConstants.PlayerGMRank.SUPERGM;
	}

	public static class BanC2 extends InternCommand.BanC {
	}

	public static class Unban2 extends Unban {
	}

	public static class Unban extends CommandExecute {// 解封指定玩家

		protected boolean hellban = false;

		private String getCommand() {
			if (this.hellban) {
				return "영구밴해제";
			}
			return "밴해제";
		}

		public int execute(MapleClient c, String[] splitted) {
			byte ret;
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(6, "[語法] !" + getCommand() + " <IGN>");
				return 0;
			}
			if (this.hellban) {
				ret = MapleClient.unHellban(splitted[1]);
			} else {
				ret = MapleClient.unban(splitted[1]);
			}
			if (ret == -2) {
				c.getPlayer().dropMessage(6, "[" + getCommand() + "] SQL錯誤.");
				return 0;
			}
			if (ret == -1) {
				c.getPlayer().dropMessage(6, "[" + getCommand() + "] 角色不存在.");
				return 0;
			}
			c.getPlayer().dropMessage(6, "[" + getCommand() + "] 成功解除封鎖!");
			byte ret_ = MapleClient.unbanIPMacs(splitted[1]);
			if (ret_ == -2) {
				c.getPlayer().dropMessage(6, "[解禁IP] SQL錯誤.");
			} else if (ret_ == -1) {
				c.getPlayer().dropMessage(6, "[解禁IP] 角色不存在.");
			} else if (ret_ == 0) {
				c.getPlayer().dropMessage(6, "[解禁IP] 不存在的IP或Mac!");
			} else if (ret_ == 1) {
				c.getPlayer().dropMessage(6, "[解禁IP] IP/Mac - 其中一個已被找到並解禁.");
			} else if (ret_ == 2) {
				c.getPlayer().dropMessage(6, "[解禁IP] IP 和 Mac 均已解禁.");
			}
			return (ret_ > 0) ? 1 : 0;
		}
	}

	public static class GrantAdminRights extends CommandExecute {// 给指定的玩家设置管理员等级

		public int execute(MapleClient c, String[] splitted) {
			for (ChannelServer cserv : ChannelServer.getAllInstances()) {
				MapleCharacter player = null;
				player = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
				if (player != null) {
					byte number = Byte.parseByte(splitted[2]);
					player.setGMLevel(number);
					player.dropMessage(5, "[通知] " + splitted[1] + " 玩家設定為GM " + splitted[2] + "級.");
				}
				c.getPlayer().dropMessage(5, "[通知] " + splitted[1] + " 玩家被設定為GM " + splitted[2] + " 級.");
			}
			return 1;
		}
	}

	public static class LieDetector extends CommandExecute {// 向指定玩家发送一个谎话探测器

		public int execute(MapleClient c, String[] splitted) {
			for (ChannelServer cserv : ChannelServer.getAllInstances()) {
				MapleCharacter player = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
				if (player != null) {
					c.getPlayer().dropMessage(5, "[通知] 已向" + splitted[1] + "發送測謊儀.");
					c.sendLieDetoctor(player, false);
				} else {
					c.getPlayer().dropMessage(5, "[通知] 無法向 " + splitted[1] + "發送測謊儀.");
				}

			}
			return 1;
		}
	}

	public static class UnbanIp extends CommandExecute {// 解除对玩家IP或MAC地址的封禁

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(6, "[語法] !UnbanIp <IGN>");
				return 0;
			}
			byte ret = MapleClient.unbanIPMacs(splitted[1]);
			if (ret == -2) {
				c.getPlayer().dropMessage(6, "[解禁IP] SQL錯誤.");
			} else if (ret == -1) {
				c.getPlayer().dropMessage(6, "[解禁IP] 該角色不存在.");
			} else if (ret == 0) {
				c.getPlayer().dropMessage(6, "[解禁IP]不存在具有該字元的IP或Mac!");
			} else if (ret == 1) {
				c.getPlayer().dropMessage(6, "[解禁IP] IP/Mac - 其中一個已被找到並解禁.");
			} else if (ret == 2) {
				c.getPlayer().dropMessage(6, "[解禁IP] IP 位址和 Mac 均已解禁.");
			}
			if (ret > 0) {
				return 1;
			}
			return 0;
		}
	}

	public static class DispelDebuffs extends CommandExecute {// 移除玩家身上的所有负面效果（Debuffs）

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dispelDebuffs();
			return 1;
		}
	}

	public static class CharacterTeleport extends CommandExecute {// 将指定玩家移动到

		public int execute(MapleClient c, String[] splitted) {
			for (ChannelServer cserv : ChannelServer.getAllInstances()) {
				MapleCharacter player = null;
				player = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
				if (player != null) {
					player.getClient().getSession().writeAndFlush(
							CField.onUserTeleport(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3])));
				}
			}
			return 1;
		}
	}

	public static class Jzcz extends CommandExecute {// 修改鍵值操作

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 5) {
				c.getPlayer().dropMessage(6, "!Jzcz  <玩家名稱> <類型編號> <屬性名稱> <屬性值>");
				return 0;
			}
			MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (chrs == null) {
				c.getPlayer().dropMessage(6, "같은 채널에 없는듯?");
			} else {
				int t = Integer.parseInt(splitted[2]);
				String key = splitted[3];
				String value = splitted[4];
				chrs.setKeyValue(t, key, value);
			}
			return 1;
		}
	}

	public static class AVMP extends CommandExecute {// 修改指定玩家账户的某些键值 !AVMP <昵称> <键> <值>修改目标玩家的账户键值

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 5) {
				c.getPlayer().dropMessage(6, "語法：!AVMP <玩家名字> <鍵> <值>");
				return 0;
			}
			MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (chrs == null) {
				c.getPlayer().dropMessage(6, "不在同一頻道?");
			} else {
				String key = splitted[2];
				String value = splitted[3];
				chrs.getClient().setKeyValue(key, value);
			}
			return 1;
		}
	}

	public static class SaveAllData extends CommandExecute {// 全服/分频道数据保存指令 語法：!SaveAllData [頻道]

		public int execute(MapleClient c, String[] splitted) {
			World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "", "語法：!SaveAllData [頻道]"));
			int saved = 0;
			if (splitted[1] == "-1") {
				for (ChannelServer cs : ChannelServer.getAllInstances()) {
					for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
						chr.saveToDB(false, false);
						chr.dropMessage(5, "已保存.");
						saved++;
					}
				}
			} else {
				ChannelServer ch = ChannelServer.getInstance(Integer.parseInt(splitted[1]));
				if (ch != null) {
					for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters().values()) {
						chr.saveToDB(false, false);
						chr.dropMessage(5, "已保存.");
						saved++;
					}
				} else {
					c.getPlayer().dropMessageGM(6, "不存在的頻道.");
				}
			}
			World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "", "共存儲了名為  " + saved + "的數據."));
			return 1;
		}
	}

	public static class MaxSkill extends CommandExecute {// 全技能满级指令 !MaxSkill <職業ID>

		public int execute(MapleClient c, String[] splitted) {
			try {
				MapleData data = MapleDataProviderFactory
						.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz"))
						.getData(StringUtil.getLeftPaddedStr(splitted[1], '0', 3) + ".img");
				byte maxLevel = 0;
				for (MapleData skill : data) {
					if (skill != null) {
						for (MapleData skillId : skill.getChildren()) {
							if (!skillId.getName().equals("icon")) {
								maxLevel = (byte) MapleDataTool.getIntConvert("maxLevel",
										skillId.getChildByPath("common"), 0);
								if (MapleDataTool.getIntConvert("invisible", skillId, 0) == 0) {
									c.getPlayer().changeSingleSkillLevel(
											SkillFactory.getSkill(Integer.parseInt(skillId.getName())), maxLevel,
											SkillFactory.getSkill(Integer.parseInt(skillId.getName())).isFourthJob()
													? maxLevel
													: 0);
								}

							}
						}
					}

				}
				c.getPlayer().dropMessage(-1, "該職業的技能全部提升到最高等級.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 1;
		}
	}

	public static class MaxSkillas extends CommandExecute {// 无差别提升所有技能至满级 38

		public int execute(MapleClient c, String[] splitted) {
			for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File("wz/String.wz"))
					.getData("Skill.img").getChildren()) {
				try {
					Skill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
					if (skill.getId() < 1009 || skill.getId() > 1011)
						;
					c.getPlayer().changeSkillLevel(skill, (byte) skill.getMaxLevel(), (byte) skill.getMaxLevel());
				} catch (NumberFormatException nfe) {
					break;
				} catch (NullPointerException npe) {
				}
			}
			return 1;
		}
	}

	public static class SkillGive extends CommandExecute {// 给指定玩家添加或修改技能 用法：!SkillGive <玩家> <技能ID> [技能等级] [精通等级]

		public int execute(MapleClient c, String[] splitted) {
			MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			Skill skill = SkillFactory.getSkill(Integer.parseInt(splitted[2]));
			byte level = (byte) CommandProcessorUtil.getOptionalIntArg(splitted, 3, 1);
			byte masterlevel = (byte) CommandProcessorUtil.getOptionalIntArg(splitted, 4, 1);
			if (level > skill.getMaxLevel()) {
				level = (byte) skill.getMaxLevel();
			}
			if (masterlevel > skill.getMaxLevel()) {
				masterlevel = (byte) skill.getMaxLevel();
			}
			victim.changeSingleSkillLevel(skill, level, masterlevel);
			return 1;
		}
	}

	public static class InventoryLockRelease extends CommandExecute {// 解除玩家装备锁定或不可交易物品

		public int execute(MapleClient c, String[] splitted) {
			Map<Item, MapleInventoryType> eqs = new HashMap<>();
			boolean add = false;
			if (splitted.length < 2 || splitted[1].equals("1")) {
				for (MapleInventoryType type : MapleInventoryType.values()) {
					for (Item item : c.getPlayer().getInventory(type)) {
						if (ItemFlag.LOCK.check(item.getFlag())) {
							item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
							add = true;
						}
						if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
							item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
							add = true;
						}
						if (add) {
							eqs.put(item, type);
						}
						add = false;
					}
				}
			} else if (splitted[1].equals("2")) {
				for (Item item : c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).newList()) {
					if (ItemFlag.LOCK.check(item.getFlag())) {
						item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
						add = true;
					}
					if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
						item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
						add = true;
					}
					if (add) {
						eqs.put(item, MapleInventoryType.EQUIP);
					}
					add = false;
				}
			} else if (splitted[1].equals("3")) {
				for (Item item : c.getPlayer().getInventory(MapleInventoryType.EQUIP)) {
					if (ItemFlag.LOCK.check(item.getFlag())) {
						item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
						add = true;
					}
					if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
						item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
						add = true;
					}
					if (add) {
						eqs.put(item, MapleInventoryType.EQUIP);
					}
					add = false;
				}
			} else if (splitted[1].equals("4")) {
				for (Item item : c.getPlayer().getInventory(MapleInventoryType.USE)) {
					if (ItemFlag.LOCK.check(item.getFlag())) {
						item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
						add = true;
					}
					if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
						item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
						add = true;
					}
					if (add) {
						eqs.put(item, MapleInventoryType.USE);
					}
					add = false;
				}
			} else if (splitted[1].equals("5")) {
				for (Item item : c.getPlayer().getInventory(MapleInventoryType.SETUP)) {
					if (ItemFlag.LOCK.check(item.getFlag())) {
						item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
						add = true;
					}
					if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
						item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
						add = true;
					}
					if (add) {
						eqs.put(item, MapleInventoryType.SETUP);
					}
					add = false;
				}
			} else if (splitted[1].equals("6")) {
				for (Item item : c.getPlayer().getInventory(MapleInventoryType.ETC)) {
					if (ItemFlag.LOCK.check(item.getFlag())) {
						item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
						add = true;
					}
					if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
						item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
						add = true;
					}
					if (add) {
						eqs.put(item, MapleInventoryType.ETC);
					}
					add = false;
				}
			} else if (splitted[1].equals("7")) {
				for (Item item : c.getPlayer().getInventory(MapleInventoryType.CASH)) {
					if (ItemFlag.LOCK.check(item.getFlag())) {
						item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
						add = true;
					}
					if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
						item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
						add = true;
					}
					if (add) {
						eqs.put(item, MapleInventoryType.CASH);
					}
					add = false;
				}
			} else {
				c.getPlayer().dropMessage(6,
						"請使用格式：!InventoryLockRelease <玩家> <數位1-7>  [1 全部/2 安裝設備/3 設備/4 消耗/5 安裝/6 其他/7 現金]");
			}
			for (Map.Entry<Item, MapleInventoryType> eq : eqs.entrySet()) {
				c.getPlayer().forceReAddItem_NoUpdate(((Item) eq.getKey()).copy(), eq.getValue());
			}
			return 1;
		}
	}

	public static class drop extends CommandExecute {

		public int execute(MapleClient c, String[] splitted) {
			int itemId = Integer.parseInt(splitted[1]);
			short quantity = (short) CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);
			MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
			if (splitted.length < 3) {
				c.getPlayer().dropMessage(6, "語法：!drop [物品ID] [數量]");
				return 0;
			}
			if (GameConstants.isPet(itemId)) {
				c.getPlayer().dropMessage(5, "請從現金商店購買寵物.");
			} else if (!ii.itemExists(itemId)) {
				c.getPlayer().dropMessage(5, itemId + " 這個物品不存在");
			} else {
				Item toDrop;
				if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
					toDrop = ii.getEquipById(itemId);
				} else {
					toDrop = new Item(itemId, (short) 0, quantity, 0);
				}
				if (!c.getPlayer().isAdmin()) {
					toDrop.setGMLog(c.getPlayer().getName() + " 使用 !drop");
					toDrop.setOwner(c.getPlayer().getName());
				}
				c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(),
						true, true);
			}
			return 1;
		}
	}

	public static class Marriage extends CommandExecute {// 结婚

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 3) {
				c.getPlayer().dropMessage(6, "請使用格式：!Marriage <玩家> <itemid>");
				return 0;
			}
			int itemId = Integer.parseInt(splitted[2]);
			if (!GameConstants.isEffectRing(itemId)) {
				c.getPlayer().dropMessage(6, "無效的 itemID.");
			} else {
				MapleCharacter fff = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
				if (fff == null) {
					c.getPlayer().dropMessage(6, "玩家不在線上");
				} else {
					long[] ringID = { MapleInventoryIdentifier.getInstance(), MapleInventoryIdentifier.getInstance() };
					try {
						MapleCharacter[] chrz = { fff, c.getPlayer() };
						for (int i = 0; i < chrz.length; i++) {
							Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemId,
									ringID[i]);
							if (eq == null) {
								c.getPlayer().dropMessage(6, "無效的 itemID.");
								return 0;
							}
							MapleInventoryManipulator.addbyItem(chrz[i].getClient(), eq.copy());
							chrz[i].dropMessage(6, "與成功結婚 " + chrz[(i == 0) ? 1 : 0].getName());
						}
						MapleRing.addToDB(itemId, c.getPlayer(), fff.getName(), fff.getId(), ringID);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			return 1;
		}
	}

	public static class Points extends CommandExecute {// 支付积分

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 3) {
				c.getPlayer().dropMessage(6, "請使用格式：!Points <玩家> <數量>.");
				return 0;
			}
			MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (chrs == null) {
				c.getPlayer().dropMessage(6, "確保他們處於正確的頻道中");
			} else {
				chrs.setPoints(chrs.getPoints() + Integer.parseInt(splitted[2]));
				c.getPlayer().dropMessage(6,
						splitted[1] + " 給予 " + splitted[2] + " Points, 給予後 " + chrs.getPoints() + "Points.");
			}
			return 1;
		}
	}

	public static class HPointGive extends CommandExecute {// 发放推廣積分

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 3) {
				c.getPlayer().dropMessage(6, "請使用格式：!HPointGive <玩家> <數量>.");
				return 0;
			}
			MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (chrs == null) {
				c.getPlayer().dropMessage(6, "確保他們處於正確的頻道中");
			} else {
				chrs.gainHPoint(Integer.parseInt(splitted[2]));
				c.getPlayer().dropMessage(6,
						splitted[1] + " 給予 " + splitted[2] + " 推廣點, 給予後 " + chrs.getHPoint() + "推廣積分.");
			}
			return 1;
		}
	}

	public static class VPointGive extends CommandExecute {// 支付VPoint積分

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 3) {
				c.getPlayer().dropMessage(6, "請使用格式：!VPointGive <玩家> <數量>.");
				return 0;
			}
			MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (chrs == null) {
				c.getPlayer().dropMessage(6, "確保他們處於正確的頻道中");
			} else {
				chrs.setVPoints(chrs.getVPoints() + Integer.parseInt(splitted[2]));
				c.getPlayer().dropMessage(6,
						splitted[1] + " 給予 " + splitted[2] + " vpoints，給予後 " + chrs.getVPoints() + "vpoints.");
			}
			return 1;
		}
	}

	public static class ResetOther extends CommandExecute {// 放弃指定任务

		public int execute(MapleClient c, String[] splitted) {
			MapleQuest.getInstance(Integer.parseInt(splitted[2]))
					.forfeit(c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]));
			return 1;
		}
	}

	public static class FStartOther extends CommandExecute {// 强制其他玩家开始指定任务 ，玩家名称、任务ID或任务进度

		public int execute(MapleClient c, String[] splitted) {
			MapleQuest.getInstance(Integer.parseInt(splitted[2])).forceStart(
					c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]),
					Integer.parseInt(splitted[3]), (splitted.length > 4) ? splitted[4] : null);
			return 1;
		}
	}

	public static class FCompleteOther extends CommandExecute {// 强制玩家完成指定任务 ，玩家名称、任务ID或任务进度

		public int execute(MapleClient c, String[] splitted) {
			MapleQuest.getInstance(Integer.parseInt(splitted[2])).forceComplete(
					c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]),
					Integer.parseInt(splitted[3]));
			return 1;
		}
	}

	public static class ResetBoss extends CommandExecute {

		public int execute(MapleClient c, String[] splitted) {
			MapleLifeFactory.getMonsterStats().clear();
			// Setting.setting();
			// Setting.setting2();
			return 1;
		}
	}

	public static class SpawnReactor extends CommandExecute {// 生成临时反应堆

		public int execute(MapleClient c, String[] splitted) {
			MapleReactor reactor = new MapleReactor(MapleReactorFactory.getReactor(Integer.parseInt(splitted[1])),
					Integer.parseInt(splitted[1]));
			reactor.setDelay(-1);
			c.getPlayer().getMap().spawnReactorOnGroundBelow(reactor,
					new Point((c.getPlayer().getTruePosition()).x, (c.getPlayer().getTruePosition()).y - 20));
			return 1;
		}
	}

	public static class MapNPC extends CommandExecute { // 列出地图所有NPC

		public int execute(MapleClient c, String[] splitted) {
			// 获取玩家当前所在的地图
			MapleMap map = c.getPlayer().getMap();

			// 向玩家发送消息，说明将列出当前地图上的NPC列表
			c.getPlayer().dropMessage(6, "當前地圖上的NPC淸單如下："); // 使用英文消息以提高通用性

			// 遍历当前地图上的所有NPC对象
			for (MapleMapObject mo : map.getAllNPCs()) {
				// 检查对象是否为NPC类型
				if (mo instanceof MapleNPC) {
					MapleNPC npc = (MapleNPC) mo;
					// 向玩家发送每个NPC的ID和名称
					c.getPlayer().dropMessage(6, npc.getId() + " : " + npc.getName());
				}
			}

			return 1; // 返回1表示命令执行成功
		}
	}

	public static class MobDamageOID extends CommandExecute {// 指定怪物对象ID（OID）造成伤害

		public int execute(MapleClient c, String[] splitted) {
			MapleMap map = c.getPlayer().getMap();
			int targetId = Integer.parseInt(splitted[1]);
			int damage = Integer.parseInt(splitted[2]);
			MapleMonster monster = map.getMonsterByOid(targetId);
			if (monster != null) {
				map.broadcastMessage(MobPacket.damageMonster(targetId, damage, false));
				monster.damage(c.getPlayer(), damage, false);
			}
			return 1;
		}
	}

	public static class MapDamage extends CommandExecute {// 地图指定伤害

		public int execute(MapleClient c, String[] splitted) {
			MapleMap map = c.getPlayer().getMap();
			double range = Double.POSITIVE_INFINITY;
			if (splitted.length > 1) {
				int irange = Integer.parseInt(splitted[1]);
				if (splitted.length <= 2) {
					range = (irange * irange);
				} else {
					map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
				}
			}
			if (map == null) {
				c.getPlayer().dropMessage(6, "請使用格式：!MapZdDamage <傷害 > [地圖ID]");
				return 0;
			}
			int damage = Integer.parseInt(splitted[1]);
			for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range,
					Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
				MapleMonster mob = (MapleMonster) monstermo;
				map.broadcastMessage(MobPacket.damageMonster(mob.getObjectId(), damage, false));
				mob.damage(c.getPlayer(), damage, false);
			}
			return 1;
		}
	}

	public static class MobDamage extends CommandExecute {// 怪物指定伤害 請使用格式：!MapDamage <傷害值> [怪物ID]

		public int execute(MapleClient c, String[] splitted) {
			MapleMap map = c.getPlayer().getMap();
			double range = Double.POSITIVE_INFINITY;
			int damage = Integer.parseInt(splitted[1]);
			for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range,
					Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
				MapleMonster mob = (MapleMonster) monstermo;
				if (mob.getId() == Integer.parseInt(splitted[2])) {
					map.broadcastMessage(MobPacket.damageMonster(mob.getObjectId(), damage, false));
					mob.damage(c.getPlayer(), damage, false);
				}
			}
			return 1;
		}
	}

	public static class dqdt extends CommandExecute {// 當前地圖信息

		public int execute(MapleClient c, String[] splitted) {
			MapleMap map = c.getPlayer().getMap();
			c.getPlayer().dropMessage(6,
					"當前地圖 : " + map.getId() + " - " + map.getStreetName() + " : " + map.getMapName());
			return 1;
		}
	}

	public static class MonsterSO extends CommandExecute {// 怪物召喚個體 怪物召喚對象

		public int execute(MapleClient c, String[] splitted) {
			MapleMap map = c.getPlayer().getMap();
			double range = Double.POSITIVE_INFINITY;
			if (splitted.length > 1) {
				int irange = Integer.parseInt(splitted[1]);
				if (splitted.length <= 2) {
					range = (irange * irange);
				} else {
					map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
				}
			}
			if (map == null) {
				c.getPlayer().dropMessage(6, "맵이 존재하지 않습니다.");
				return 1;
			}
			c.getPlayer()
					.dropMessage(6,
							"남아있는 몹 수 : " + map
									.getMapObjectsInRange(c.getPlayer().getPosition(), range,
											Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))
									.size());
			for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range,
					Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
				MapleMonster mob = (MapleMonster) monstermo;
				c.getPlayer().dropMessage(6, "몬스터 " + mob.toString());
				if (mob.getStats().isBoss()) {
					for (MobSkill msi : mob.getSkills()) {
						c.getPlayer().dropMessageGM(5, msi.getSkillId() + "스킬레벨 : " + msi.getSkillLevel() + "  체력 : "
								+ msi.getHP() + "  한번만" + msi.isOnlyOtherSkill() + " 다른스킬 : " + msi.isOnlyFsm());
					}
				}
			}
			return 1;
		}
	}

	public static class qg extends CommandExecute {// 清怪

		public int execute(MapleClient c, String[] splitted) {
			MapleMap map = c.getPlayer().getMap();
			double range = Double.POSITIVE_INFINITY;
			if (splitted.length > 1) {
				int irange = Integer.parseInt(splitted[1]);
				if (splitted.length <= 2) {
					range = (irange * irange);
				} else {
					map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
				}
			}
			if (map == null) {
				c.getPlayer().dropMessage(6, "地圖不存在有效怪物.");
				return 0;
			}
			for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range,
					Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
				MapleMonster mob = (MapleMonster) monstermo;
				map.killMonster(mob, c.getPlayer(), true, false, (byte) 1);
			}
			return 1;
		}
	}

	public static class GenerateNPC extends CommandExecute {// 生成NPC 临时

		@Override
		public int execute(MapleClient client, String[] parameters) {
			if (parameters.length < 2) {
				client.getPlayer().dropMessage(6, "請輸入有效的NPC id。");
				return 0;
			}

			try {
				int npcId = Integer.parseInt(parameters[1]);
				MapleNPC npc = MapleLifeFactory.getNPC(npcId);

				if (npc != null && !"MISSINGNO".equals(npc.getName())) {
					Point position = client.getPlayer().getPosition();
					npc.setPosition(position);
					npc.setCy(position.y);
					npc.setRx0(position.x);
					npc.setRx1(position.x);
					npc.setFh(client.getPlayer().getMap().getFootholds().findBelow(position).getId());
					npc.setCustom(true);

					client.getPlayer().getMap().addMapObject(npc);
					client.getPlayer().getMap().broadcastMessage(CField.NPCPacket.spawnNPC(npc, true));
				} else {
					client.getPlayer().dropMessage(6, "WZ數據中不存在的NPC。");
					return 0;
				}
			} catch (NumberFormatException e) {
				client.getPlayer().dropMessage(6, "請輸入有效的數位作爲NPC代碼。");
				return 0;
			}

			return 1;
		}
	}

	public static class CharacterInfo extends CommandExecute {// 角色信息显示

		public int execute(MapleClient c, String[] splitted) {
			MapleCharacter chr = null;
			if (splitted.length <= 1) {
				chr = c.getPlayer();
			} else {
				chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			}
			for (Skill sk : chr.getSkills().keySet()) {
				if (!PacketHelper.jobskill(c.getPlayer(), sk.getId())) {
					System.out.println(sk.getName() + "(" + sk.getId() + ") : " + chr.getSkillLevel(sk.getId()));
				}
			}
			if (chr != null) {
				String info = "#fs15##b" + chr.getName() + "#k的資訊.\r\n\r\n";
				info = info + "最大HP : " + chr.getStat().getCurrentMaxHp() + "\r\n";
				info = info + "最大MP : " + chr.getStat().getCurrentMaxMp(chr) + "\r\n";
				info = info + "力量 : " + chr.getStat().getTotalStr() + "\r\n";
				info = info + "敏捷 : " + chr.getStat().getTotalDex() + "\r\n";
				info = info + "智力 : " + chr.getStat().getTotalInt() + "\r\n";
				info = info + "運氣 : " + chr.getStat().getTotalLuk() + "\r\n";
				info = info + "武器熟練度 : " + chr.getStat().getMastery() + "  攻擊速度 : "
						+ GameConstants.getJobAttackSpeed(c.getPlayer()) + " 階段\r\n";
				info = info + "攻擊力 : " + chr.getStat().getTotalWatk() + "   魔力 : " + chr.getStat().getTotalMagic()
						+ "\r\n\r\n";
				info = info + " 怪物攻擊 #e" + chr.getStat().BeforeStatWatk(chr) + " ~ " + chr.getStat().AfterStatWatk(chr)
						+ "#n\r\n";
				info = info + "傷害 : #d" + chr.getStat().getDamagePercent() + "%#k  BOSS傷害 : #d"
						+ chr.getStat().getBossDamage() + "%#k\r\n";
				info = info + "最終傷害 : #d" + chr.getStat().getFinalDamage() + "%#k  無視防御 : #d"
						+ chr.getStat().getIgnoreMobpdpR() + "%#k\r\n";
				info = info + "暴擊槪率 : #d" + chr.getStat().getCritical_rate() + "%#k\r\n";
				info = info + "暴擊傷害 : #d" + chr.getStat().getCritical_damage() + "%#k\r\n";
				info = info + "異常狀態抗性 : #d" + chr.getStat().getASR() + "#k  穩如泰山 : #d" + chr.getStat().getStance()
						+ "#k\r\n";
				info = info + "星力 : #d" + chr.getStat().getStarforce() + "#k  神秘之力 : #d" + chr.getStat().getArc()
						+ "#k\r\n ";
				c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkText(9062240, info));
			}
			return 1;
		}
	}

	public static class FixNPC extends CommandExecute {// 注册固定NPC 永久

		@Override
		public int execute(MapleClient client, String[] parameters) {
			// 检查参数数量
			if (parameters.length < 2) {
				client.getPlayer().dropMessage(6, "参数不足。");
				return 0;
			}

			int npcId;
			try {
				npcId = Integer.parseInt(parameters[1]);
			} catch (NumberFormatException e) {
				client.getPlayer().dropMessage(6, "无效的NPC ID格式。");
				return 0;
			}

			// 获取NPC实例
			MapleNPC npc = MapleLifeFactory.getNPC(npcId);
			if (npc != null && !"MISSINGNO".equals(npc.getName())) {
				Point playerPosition = client.getPlayer().getPosition();
				npc.setPosition(playerPosition);
				// 通常NPC的cy, rx0, rx1会由setPosition设置，这里显式设置以防万一
				npc.setCy(playerPosition.y);
				npc.setRx0(playerPosition.x);
				npc.setRx1(playerPosition.x); // 假设rx1与rx0相同，如果需要不同请调整
				npc.setFh(client.getPlayer().getMap().getFootholds().findBelow(playerPosition).getId());

				// 将NPC添加到地图并广播NPC生成消息
				client.getPlayer().getMap().addMapObject(npc);
				client.getPlayer().getMap().broadcastMessage(CField.NPCPacket.spawnNPC(npc, true));
			} else {
				client.getPlayer().dropMessage(6, "输入了WZ中不存在的NPC。");
				return 0;
			}

			// 数据库连接和插入操作
			Connection con = null;
			PreparedStatement ps = null;
			try {
				String sql = "INSERT INTO `spawn`(`lifeid`, `rx0`, `rx1`, `cy`, `fh`, `type`, `dir`, `mapid`, `mobTime`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				con = DatabaseConnection.getConnection();
				ps = con.prepareStatement(sql);
				ps.setInt(1, npcId);
				Point position = client.getPlayer().getPosition();
				// 调整NPC的插入位置，这里假设在玩家位置的前后各偏移50单位
				ps.setInt(2, position.x - 50);
				ps.setInt(3, position.x + 50);
				ps.setInt(4, position.y);
				ps.setInt(5, client.getPlayer().getMap().getFootholds().findBelow(position).getId());
				ps.setString(6, "n");
				ps.setInt(7, client.getPlayer().getFacingDirection() == 1 ? 0 : 1);
				ps.setInt(8, client.getPlayer().getMapId());
				ps.setInt(9, 0);
				ps.executeUpdate();
			} catch (SQLException e) {
				System.err.println("[错误]无法注册固定NPC。");
				e.printStackTrace(); // 在实际项目中应使用日志框架记录错误
				client.getPlayer().dropMessage(6, "注册NPC时发生错误。");
				return 0;
			} finally {
				// 关闭资源
				try {
					if (ps != null) {
						ps.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					se.printStackTrace(); // 在实际项目中应适当处理此异常（例如记录日志）
				}
			}

			return 1; // 命令执行成功
		}
	}

	public static class FixReactor extends CommandExecute {// 注册固定反应堆 永久

		public int execute(MapleClient c, String[] splitted) {
			int npcId = Integer.parseInt(splitted[1]);
			MapleReactor reactor = null;
			try {
				reactor = new MapleReactor(MapleReactorFactory.getReactor(Integer.parseInt(splitted[1])),
						Integer.parseInt(splitted[1]));
			} catch (Exception e) {
				c.getPlayer().dropMessage(5, "此反應堆不存在。.");
				return 1;
			}
			if (splitted[2] == null) {
				c.getPlayer().dropMessage(5, "請輸入反應器再生埴 預設埴 : -1");
				return 1;
			}
			reactor.setDelay(-1);
			c.getPlayer().getMap().spawnReactorOnGroundBelow(reactor,
					new Point((c.getPlayer().getTruePosition()).x, (c.getPlayer().getTruePosition()).y));
			Connection con = null;
			PreparedStatement ps = null;
			try {
				String sql = "INSERT INTO `spawn`(`lifeid`, `rx0`, `rx1`, `cy`, `fh`, `type`, `dir`, `mapid`, `mobTime`) VALUES (? ,? ,? ,? ,? ,? ,? ,? ,?)";
				con = DatabaseConnection.getConnection();
				ps = con.prepareStatement(sql);
				ps.setInt(1, npcId);
				ps.setInt(2, (c.getPlayer().getPosition()).x);
				ps.setInt(3, (c.getPlayer().getPosition()).x);
				ps.setInt(4, (c.getPlayer().getPosition()).y);
				ps.setInt(5, c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
				ps.setString(6, "r");
				ps.setInt(7, (c.getPlayer().getFacingDirection() == 1) ? 0 : 1);
				ps.setInt(8, c.getPlayer().getMapId());
				ps.setInt(9, Integer.parseInt(splitted[2]));
				ps.executeUpdate();
				ps.close();
				con.close();
			} catch (Exception e) {
				System.err.println("[錯誤]無法注冊固定反應堆.");
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
			return 1;
		}
	}

	public static class CPNPC extends CommandExecute {// 创建在线玩家NPC 不可用

		public int execute(MapleClient c, String[] splitted) {
			try {
				c.getPlayer().dropMessage(6, "正在製作玩家NPC.");
				MapleCharacter chhr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
				if (chhr == null) {
					c.getPlayer().dropMessage(6, splitted[1] + "玩家不在線上,或者不存在的名字.請檢査狀態後重試.");
					return 0;
				}
				PlayerNPC npc = new PlayerNPC(chhr, Integer.parseInt(splitted[2]), c.getPlayer().getMap(),
						c.getPlayer());
				npc.addToServer();
				c.getPlayer().dropMessage(6, "完成!");
			} catch (Exception e) {
				c.getPlayer().dropMessage(6, "製作玩家NPC失敗. : " + e.getMessage());
				e.printStackTrace();
			}
			return 1;
		}
	}

	public static class COPNPC extends CommandExecute {// 创建离线玩家NPC

		public int execute(MapleClient c, String[] splitted) {
			try {
				c.getPlayer().dropMessage(6, "正在製作非玩家NPC.");
				MapleClient cs = new MapleClient(c.getSession(), null, null);
				MapleCharacter chhr = MapleCharacter.loadCharFromDB(MapleCharacterUtil.getIdByName(splitted[1]), cs,
						false);
				if (chhr == null) {
					c.getPlayer().dropMessage(6, splitted[1] + "玩家不存在.");
					return 0;
				}
				PlayerNPC npc = new PlayerNPC(chhr, Integer.parseInt(splitted[2]), c.getPlayer().getMap(),
						c.getPlayer());
				npc.addToServer();
				c.getPlayer().dropMessage(6, "完成!");
			} catch (Exception e) {
				c.getPlayer().dropMessage(6, "製作非玩家NPC失敗. : " + e.getMessage());
				e.printStackTrace();
			}
			return 1;
		}
	}

	public static class DeleteNPC extends CommandExecute {// 删除指定NPC

		public int execute(MapleClient c, String[] splitted) {
			try {
				c.getPlayer().dropMessage(6, "指定NPC 正在刪除.");
				MapleNPC npc = c.getPlayer().getMap().getNPCByOid(Integer.parseInt(splitted[1]));
				((PlayerNPC) npc).destroy(true);
				c.getPlayer().dropMessage(6, "完成!");
			} catch (Exception e) {
				c.getPlayer().dropMessage(6, "刪除NPC失敗. : " + e.getMessage());
				e.printStackTrace();
			}
			return 1;
		}
	}

	public static class ServerMessage extends CommandExecute {// 游戏置顶滑动公告

		public int execute(MapleClient c, String[] splitted) {
			String outputMessage = StringUtil.joinStringFrom(splitted, 1);
			for (ChannelServer cserv : ChannelServer.getAllInstances()) {
				cserv.setServerMessage(outputMessage);
			}
			return 1;
		}
	}

	public static class Spawnmob extends CommandExecute {// 召唤怪物

		public int execute(MapleClient c, String[] splitted) {
			MapleMonster onemob;
			int mid = Integer.parseInt(splitted[1]);
			int num = Math.min(CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1), 500);
			Integer level = CommandProcessorUtil.getNamedIntArg(splitted, 1, "lvl");
			Long hp = CommandProcessorUtil.getNamedLongArg(splitted, 1, "hp");
			Long exp = CommandProcessorUtil.getNamedLongArg(splitted, 1, "exp");
			Double php = CommandProcessorUtil.getNamedDoubleArg(splitted, 1, "php");
			Double pexp = CommandProcessorUtil.getNamedDoubleArg(splitted, 1, "pexp");
			try {
				onemob = MapleLifeFactory.getMonster(mid);
			} catch (RuntimeException e) {
				c.getPlayer().dropMessage(5, "錯誤: " + e.getMessage());
				return 0;
			}
			if (onemob == null) {
				c.getPlayer().dropMessage(5, "沒有這個怪物");
				return 0;
			}
			long newhp = 0L;
			long newexp = 0L;
			if (hp != null) {
				newhp = hp.longValue();
			} else if (php != null) {
				newhp = (long) (onemob.getMobMaxHp() * php.doubleValue() / 100.0D);
			} else {
				newhp = onemob.getMobMaxHp();
			}
			if (exp != null) {
				newexp = exp.longValue();
			} else if (pexp != null) {
				newexp = (long) (onemob.getMobExp() * pexp.doubleValue() / 100.0D);
			} else {
				newexp = onemob.getMobExp();
			}
			if (newhp < 1L) {
				newhp = 1L;
			}
			OverrideMonsterStats overrideStats = new OverrideMonsterStats(newhp, onemob.getMobMaxMp(), newexp, false);
			for (int i = 0; i < num; i++) {
				MapleMonster mob = MapleLifeFactory.getMonster(mid);
				mob.setHp(newhp);
				if (level != null) {
					mob.changeLevel(level.intValue(), false);
				} else {
					mob.setOverrideStats(overrideStats);
				}
				c.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, c.getPlayer().getPosition());
				c.getPlayer().dropMessage(6, "oid : " + mob.getObjectId());
			}
			return 1;
		}
	}

	public static class xjsc extends CommandExecute {// 進入現金商城

		public int execute(MapleClient c, String[] splitted) {
			InterServerHandler.EnterCS(c, c.getPlayer(), false);
			return 1;
		}
	}

	public static class ResetCashShop extends CommandExecute {// 重置现金商城

		@Override
		public int execute(MapleClient client, String[] parameters) {
			try {
				// 假设Setting是一个可用的类，并且有一个静态方法CashShopSetting来处理现金商店设置
				Setting.CashShopSetting();

				// 获取CashItemFactory的实例，并调用它的initialize方法来初始化现金商店的物品
				CashItemFactory.getInstance().initialize();

				// 给玩家发送消息，确认现金商店已重置
				// 注意：你需要确保-8是一个有效的消息类型，或者根据需要调整它
				client.getPlayer().dropMessage(-8, "[GM通知] 主商城重置已完成。");

				return 1; // 命令执行完成

			} catch (Exception e) {
				// 如果在重置过程中发生异常，则给玩家发送错误消息，并记录异常信息
				client.getPlayer().dropMessage(5, "重置現金商店時發生异常。");
				e.printStackTrace(); // 在实际项目中，应该使用更合适的日志记录方式
				return 0;
			}
		}
	}

	public static class SJG1 extends CommandExecute {// 花之庆典活动邀请功能

		public int execute(MapleClient c, String[] splitted) {
			BloomingRace.RaceIinviTation();
			return 1;
		}
	}

	public static class GroundJob extends CommandExecute {// 指定职业

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().setBattleGrondJobName("" + splitted[1]);
			c.getPlayer().dropMessage(5, "職業：已指定為 : " + splitted[1] + "職業.");
			return 1;
		}
	}

	public static class SJG extends CommandExecute {// 花之庆典活动邀请指令

		public int execute(MapleClient c, String[] splitted) {
			BattleGroundGameHandler.BattleGroundIinviTation();
			return 1;
		}
	}

	public static class map extends CommandExecute {// 简易地图传送

		public int execute(MapleClient c, String[] splitted) {
			MapleMap target = null;
			ChannelServer cserv = c.getChannelServer();
			target = cserv.getMapFactory().getMap(Integer.parseInt(splitted[1]));
			MaplePortal targetPortal = null;
			if (splitted.length > 2) {
				try {
					targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
				} catch (IndexOutOfBoundsException e) {
					c.getPlayer().dropMessage(5, "所選ID無效.");
				} catch (NumberFormatException numberFormatException) {
				}
			}
			if (targetPortal == null) {
				targetPortal = target.getPortal(0);
			}
			c.getPlayer().changeMap(target, targetPortal);
			return 1;
		}
	}

	public static class ReloadMap extends CommandExecute {// 重载地图

		public int execute(MapleClient c, String[] splitted) {
			int mapId = Integer.parseInt(splitted[1]);
			for (ChannelServer cserv : ChannelServer.getAllInstances()) {
				if (cserv.getMapFactory().isMapLoaded(mapId)
						&& cserv.getMapFactory().getMap(mapId).getCharactersSize() > 0) {
					c.getPlayer().dropMessage(5, "頻道上存在玩家 " + cserv.getChannel());
					return 0;
				}
			}
			for (ChannelServer cserv : ChannelServer.getAllInstances()) {
				if (cserv.getMapFactory().isMapLoaded(mapId)) {
					cserv.getMapFactory().removeMap(mapId);
				}
			}
			return 1;
		}
	}

	public static class Respad extends CommandExecute {// 刷新当前地图并给玩家增加1168點AP

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getMap().respawn(true);
			c.getPlayer().gainAp((short) 1168);
			return 1;
		}
	}

	public static class MaxMeso999 extends CommandExecute {// 获得金币9999999999L

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().gainMeso(9999999999L - c.getPlayer().getMeso(), true);
			return 1;
		}
	}

	public static class GrantMeso extends CommandExecute {// 获得金币 !GrantMeso [金幣數量]

		@Override
		public int execute(MapleClient client, String[] parameters) {
			// 检查参数数量
			if (parameters.length < 2) {
				client.getPlayer().dropMessage(5, "語法：!GrantMeso [金幣數量]");
				return 0;
			}

			// 尝试将第二个参数解析为长整型
			try {
				long mesoAmount = Long.parseLong(parameters[1]);

				// 给玩家增加金币
				// 注意：这里假设gainMeso方法是MapleCharacter类的一个方法，用于增加金币
				// 第一个参数是金币的数量，第二个参数true可能表示某种标记或选项（这个也需要根据你的项目文档来确定）
				client.getPlayer().gainMeso(mesoAmount, true);

				// 给玩家发送消息，确认金币已增加
				client.getPlayer().dropMessage(1, "已成功發放 " + mesoAmount + " 金幣。");

			} catch (NumberFormatException e) {
				// 如果无法解析为长整型，则给玩家发送错误消息
				client.getPlayer().dropMessage(6, "無效的數量參數：" + parameters[1]);
			}

			return 1; // 命令执行完成
		}
	}

	public static class DebugLcd extends CommandExecute { // 测试路西德

		public int execute(MapleClient c, String[] splitted) {
			List<Integer> laserIntervals;
			Point pos = new Point(665, -490);
			MapleMonster monster = MapleLifeFactory.getMonster(8880151);
			laserIntervals = new ArrayList<Integer>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					for (int i = 0; i < 15; ++i) {
						this.add(500);
					}
				}
			};
			monster.setCustomInfo(23807, 0, 10000);
			c.getPlayer().warp(450004250);
			c.getPlayer().getMap().spawnMonsterOnGroundBelow(monster, pos);
			c.getPlayer().getMap().broadcastMessage(MobPacket.BossLucid.doLaserRainSkill(4500, laserIntervals));
			c.getPlayer().getMap().broadcastMessage(CField.enforceMSG("路西德要使用强力攻擊!", 222, 2000));
			c.getPlayer().dropMessageGM(6, "[DEBUG] 루시드 레이저 패턴");
			return -1;
		}
	}

	// =========================================================================================================================================

	public static class DBhmf extends CommandExecute {

		@Override
		public int execute(MapleClient c, String[] splitted) {
			// 1. 参数校验（确保GM权限）
			if (splitted.length < 2) {
				c.getPlayer().dropMessageGM(6, "用法: !DBhmf <1-红色闪电 2-黑暗之球 3-护盾阶段> [参数]");
				return 0;
			}

			// 2. 初始化黑魔法师BOSS
			MapleMap map = c.getPlayer().getMap();
			Point spawnPos = new Point(0, 88); // 地图中心坐标
			MapleMonster blackMage = MapleLifeFactory.getMonster(8880502); // 黑魔法师ID

			// 3. 根据技能类型执行不同逻辑
			switch (splitted[1]) {
			case "1": // 红色闪电（全屏攻击）
				executeRedLightning(map, blackMage, spawnPos);
				break;

			case "2": // 黑暗之球（追踪弹）
				executeDarkBall(map, spawnPos, splitted.length > 2 ? Integer.parseInt(splitted[2]) : 5); // 默认生成5个
				break;

			case "3": // 护盾阶段
				executeShieldPhase(map, blackMage);
				break;

			default:
				c.getPlayer().dropMessageGM(6, "无效技能类型: redlightning/darkball/shield");
				return 0;
			}
			return -1; // 执行成功
		}

		private void executeRedLightning(MapleMap map, MapleMonster blackMage, Point pos) {// 測試地圖：!map 450013300 // ---
																							// 子技能实现 ---

			map.spawnMonsterOnGroundBelow(blackMage, pos);// 生成BOSS并设置技能标记
			int randt = 2;
			map.broadcastMessage(CField.enforceMSG("黑魔法師的紅色閃電覆蓋了所有的地方。 我們得探個地方藏起來.", 265, 4000));
			Timer.MapTimer.getInstance().schedule(() -> {
				map.broadcastMessage(MobPacket.ShowBlackMageSkill(randt - 1));
				map.getMonsterById(8880502).setSkillForbid(true);
			}, 5000L);
			Timer.MapTimer.getInstance().schedule(() -> {
				map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880516), new Point(-7, 88));
				map.getMonsterById(8880502).setSkillForbid(false);
				// map.broadcastMessage(MobPacket.mobBarrierEffect(map.getMonsterById(8880502).getObjectId(),
				// "UI/UIWindow8.img/BlackMageShield/mobEffect",
				// "Sound/Etc.img/BlackMageShield",
				// "UI/UIWindow8.img/BlackMageShield/mobEffect0"));
				map.getMonsterById(8880502).gainShield(3000000000000L, true, 30);
			}, 16000L);
		}

		private void executeDarkBall(MapleMap map, Point centerPos, int count) {// 生成多个追踪弹（ID:8880516）

			map.setCustomInfo(45001313, 0, Randomizer.rand(38000, 43000));
			map.broadcastMessage(CField.enforceMSG("毁滅之眼開始追逐敵人.", 265, 4000));
			for (MapleMonster mob : map.getAllMonster()) {
				if (mob.getId() == 8880502) {
					map.broadcastMessage(MobPacket.UseSkill(mob.getObjectId(), 5));
					break;
				}
			}
			map.broadcastMessage(CField.getFieldSkillAdd(100012, Randomizer.rand(1, 2), false));
		}

		private void executeShieldPhase(MapleMap map, MapleMonster blackMage) {

		}
	}

//=========================================================================================================================================    

	public static class SponsorPoints extends CommandExecute {// 增加玩家赞助积分

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(5, "Need amount.");
				return 0;
			}
			MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (victim == null) {
				c.getPlayer().dropMessage(1, splitted[1] + " 角色未連接.");
				return 0;
			}
			int Dpoint = Integer.parseInt(splitted[2]);
			c.getPlayer().dropMessage(1, splitted[1] + "支付了 " + splitted[2] + " 的贊助積分.");
			victim.getPlayer().gainDPoint(Dpoint);
			victim.sendNote(splitted[1], "楓葉GM",
					"" + splitted[1] + "勇士！感謝勇士的寶貴贊助，我們將努力不辜負勇士的期待。我們為勇士支付了贊助積分 " + splitted[2] + "積分。請確認積分.", 0, 6, 0);
			return 1;
		}
	}

	public static class PublicizePoints extends CommandExecute {// 增加玩家宣传积分

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(5, "Need amount.");
				return 0;
			}
			MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (victim == null) {
				c.getPlayer().dropMessage(1, splitted[1] + "角色未連接.");
				return 0;
			}
			int hpoint = Integer.parseInt(splitted[2]);

			c.getPlayer().dropMessage(1, splitted[1] + "支付了 " + splitted[2] + " 的文宣積分.");

			victim.getPlayer().gainHPoint(hpoint);
			victim.sendNote(splitted[1], "楓葉GM",
					"" + splitted[1] + "勇士！感謝勇士的寶貴文宣，我們會努力不辜負勇士的期待。我們給勇士發了文宣積分 " + splitted[2] + "請確認積分.", 0, 6, 0);
			return 1;
		}
	}

	public static class ECG extends CommandExecute {// 快速现金点 !ECG <数量>

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(5, "語法：!ECG <数量>.");
				return 0;
			}
			c.getPlayer().modifyCSPoints(1, Integer.parseInt(splitted[1]), true);
			return 1;
		}
	}

	public static class MMPoints extends CommandExecute {// Maple Points（枫叶点）修改指令 !MMPoints <数量>

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(5, "語法：!MMPoints <数量>.");
				return 0;
			}
			c.getPlayer().modifyCSPoints(2, Integer.parseInt(splitted[1]), true);
			return 1;
		}
	}

	public static class GivePoints extends CommandExecute {// Maple Points（枫叶点）修改指令 !GivePoints <数量>

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(5, "語法：!GivePoints <数量>.");
				return 0;
			}
			c.getPlayer().setPoints(c.getPlayer().getPoints() + Integer.parseInt(splitted[1]));
			return 1;
		}
	}

	public static class VPointss extends CommandExecute {// V点数（VIP点数）修改指令 !VPointss <数量>

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(5, "語法：!VPointss <数量>.");
				return 0;
			}
			c.getPlayer().setVPoints(c.getPlayer().getVPoints() + Integer.parseInt(splitted[1]));
			return 1;
		}
	}

	public static class ResetOpcode extends CommandExecute {// 重载数据包

		@Override
		public int execute(MapleClient client, String[] parameters) {
			// 确保SendPacketOpcode和RecvPacketOpcode类存在并且reloadValues方法可以被安全调用
			try {
				// 重新加载发送数据包的操作码
				SendPacketOpcode.reloadValues();
				client.getPlayer().dropMessage(5, "發送數据包的操作碼已重新加載。");

				// 重新加载接收数据包的操作码
				RecvPacketOpcode.reloadValues();
				client.getPlayer().dropMessage(5, "接收數据包的操作碼已重新加載。");

			} catch (Exception e) {
				// 记录异常信息并给玩家发送错误消息
				client.getPlayer().dropMessage(6, "重新加載操作碼時發生錯誤：" + e.getMessage());
				// 这里也可以添加日志记录代码，以便进一步调试
				// 例如：Logger.getLogger(OpcodeResetCommand.class.getName()).log(Level.SEVERE,
				// null, e);
			}

			return 1; // 命令执行完成（尽管可能发生了错误）
		}
	}

	public static class ResetDrop extends CommandExecute {// 重置掉落

		@Override
		public int execute(MapleClient client, String[] parameters) {
			// 确保MapleMonsterInformationProvider和ReactorScriptManager实例存在并且clearDrops方法可以被安全调用
			try {
				MapleMonsterInformationProvider monsterInfoProvider = MapleMonsterInformationProvider.getInstance();
				if (monsterInfoProvider != null) {
					monsterInfoProvider.clearDrops();
					client.getPlayer().dropMessage(5, "所有怪物的掉落物已被重置。");
				} else {
					client.getPlayer().dropMessage(6, "無法獲取MapleMonsterInformationProvider實例。");
				}

				ReactorScriptManager reactorScriptManager = ReactorScriptManager.getInstance();
				if (reactorScriptManager != null) {
					reactorScriptManager.clearDrops();
					client.getPlayer().dropMessage(5, "所有反應器的掉落物已被重置。");
				} else {
					client.getPlayer().dropMessage(6, "无法获取ReactorScriptManager实例。");
				}

			} catch (Exception e) {
				// 记录异常信息并给玩家发送错误消息
				client.getPlayer().dropMessage(6, "重置掉落物時發生錯誤：" + e.getMessage());
				// 这里也可以添加日志记录代码，以便进一步调试
			}

			return 1; // 命令执行完成（尽管可能发生了错误）
		}
	}

	public static class ResetPortal extends CommandExecute {// 重置传送门脚本

		public int execute(MapleClient client, String[] parameters) {
			// 确保PortalScriptManager实例存在并且clearScripts方法可以被安全调用
			PortalScriptManager portalScriptManager = PortalScriptManager.getInstance();
			if (portalScriptManager != null) {
				portalScriptManager.clearScripts();
				client.getPlayer().dropMessage(5, "所有傳送門脚本已被重置。");
			} else {
				client.getPlayer().dropMessage(6, "無法獲取PortalScriptManager實例。");
			}

			return 1; // 命令执行成功
		}
	}

	public static class ResetNpc extends CommandExecute {// 重置NPC

		public int execute(MapleClient c, String[] splitted) {
			NPCScriptManager.getInstance().scriptClear();
			return 1;
		}
	}

	public static class ResetShop extends CommandExecute {// 重置商店

		public int execute(MapleClient c, String[] splitted) {
			MapleShopFactory.getInstance().clear();
			return 1;
		}
	}

	public static class ResetEvent extends CommandExecute {// 重置事件

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dropMessage(6, "開始重置事件");
			c.getChannelServer().reloadEvents();
			c.getPlayer().dropMessage(6, "事件重置結束");
			return 1;
		}
	}

	public static class ResetMap extends CommandExecute {// 重置地图

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getMap().resetFully();
			return 1;
		}
	}

	public static class ResetQuest extends CommandExecute {// 重置任务

		public int execute(MapleClient c, String[] splitted) {
			MapleQuest.getInstance(Integer.parseInt(splitted[1])).forfeit(c.getPlayer());
			return 1;
		}
	}

	public static class OpenQuest extends CommandExecute {// 開始任務

		public int execute(MapleClient c, String[] splitted) {
			MapleQuest.getInstance(Integer.parseInt(splitted[1])).start(c.getPlayer(), Integer.parseInt(splitted[2]));
			return 1;
		}
	}

	public static class CompleteQuest extends CommandExecute {// 让玩家完成指定的任务

		public int execute(MapleClient c, String[] splitted) {
			MapleQuest.getInstance(Integer.parseInt(splitted[1])).complete(c.getPlayer(), Integer.parseInt(splitted[2]),
					Integer.valueOf(Integer.parseInt(splitted[3])), false);
			return 1;
		}
	}

	public static class StartQuest extends CommandExecute {// 让指定玩家开始一个任务

		public int execute(MapleClient c, String[] splitted) {
			MapleQuest.getInstance(Integer.parseInt(splitted[1])).forceStart(c.getPlayer(),
					Integer.parseInt(splitted[2]), (splitted.length >= 4) ? splitted[3] : null);
			return 1;
		}
	}

	public static class CompleteQuestF extends CommandExecute {// 完成F任务 F퀘스트완료

		public int execute(MapleClient c, String[] splitted) {
			MapleQuest.getInstance(Integer.parseInt(splitted[1])).forceComplete(c.getPlayer(),
					Integer.parseInt(splitted[2]));
			return 1;
		}
	}

	public static class ReactorDamage extends CommandExecute {// 对指定的反应器造成伤害

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getMap().getReactorByOid(Integer.parseInt(splitted[1])).hitReactor(c);
			return 1;
		}
	}

	public static class CreateAccount extends CommandExecute {// 创建一个新的游戏账号。

		public int execute(MapleClient c, String[] splitted) {
			String id = splitted[1];
			String pw = splitted[2];
			if (AutoRegister.getAccountExists(id) == true) {
				c.getPlayer().dropMessage(6, "此帳戶已存在.");
			} else if (AutoRegister.createAccount(id, pw, "/" + id + ":")) {
				c.getPlayer().dropMessage(6, "帳戶創建成功.");
			} else {
				c.getPlayer().dropMessage(6, "帳戶建立失敗.");
			}
			return 1;
		}
	}

	public static class DeleteReactor extends CommandExecute {// 删除反应堆

		public int execute(MapleClient c, String[] splitted) {
			MapleMap map = c.getPlayer().getMap();
			List<MapleMapObject> reactors = map.getMapObjectsInRange(c.getPlayer().getPosition(),
					Double.POSITIVE_INFINITY, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.REACTOR }));
			if (splitted[1].equals("all")) {
				for (MapleMapObject reactorL : reactors) {
					MapleReactor reactor2l = (MapleReactor) reactorL;
					c.getPlayer().getMap().destroyReactor(reactor2l.getObjectId());
				}
			} else {
				c.getPlayer().getMap().destroyReactor(Integer.parseInt(splitted[1]));
			}
			return 1;
		}
	}

	public static class ResetReactors extends CommandExecute {// 重置反应堆

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getMap().resetReactors(c);
			c.getPlayer().getMap().reloadReactors();
			return 1;
		}
	}

	public static class SendAllNote extends CommandExecute {// 向所有人发送纸条

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length >= 1) {
				String text = StringUtil.joinStringFrom(splitted, 1);
				for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharacters().values()) {
					mch.sendNote("[Fancy]", text, 6, 0);
				}
			} else {
				c.getPlayer().dropMessage(6, "這樣使用它: !SendAllNote <text>");
				return 0;
			}
			return 1;
		}
	}

	public static class BuffSkill extends CommandExecute {// 施加增益技能

		public int execute(MapleClient c, String[] splitted) {
			SkillFactory.getSkill(Integer.parseInt(splitted[1])).getEffect(Integer.parseInt(splitted[2]))
					.applyTo(c.getPlayer(), true);
			return 0;
		}
	}

	public static class UseBuffItem extends CommandExecute {// 使用指定的Buff道具并给玩家应用相应的效果

		public int execute(MapleClient c, String[] splitted) {
			MapleItemInformationProvider.getInstance().getItemEffect(Integer.parseInt(splitted[1]))
					.applyTo(c.getPlayer(), true);
			return 0;
		}
	}

	public static class UseBuffItemEX extends CommandExecute {// 使用指定的扩展Buff道具并给玩家应用相应的效果

		public int execute(MapleClient c, String[] splitted) {
			MapleItemInformationProvider.getInstance().getItemEffectEX(Integer.parseInt(splitted[1]))
					.applyTo(c.getPlayer(), true);
			return 0;
		}
	}

	public static class ShowItem extends CommandExecute {// 显示所有物品數量

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dropMessage(6, "所有物品數量: " + MapleItemInformationProvider.getInstance().getAllItems().size());
			return 0;
		}
	}

	public static class SSIAOUI extends CommandExecute {// 向客户端发送一个什么都没做的UI界面 !SSIAOUI [total数值] [exp数值]

		public int execute(MapleClient c, String[] splitted) {
			c.getSession().writeAndFlush(CWvsContext.InfoPacket.updateClientInfoQuest(217, "reward="
					+ GameConstants.getCurrentDate() + ";count=0;uDate=" + FileoutputUtil.CurrentReadable_Date()
					+ ";qState=2;logis=0CL=9;T=20190114093031;use=47;total=" + splitted[1] + ";exp=" + splitted[2]));
			c.getSession().writeAndFlush(CField.UIPacket.openUI(1207));
			return 0;
		}
	}

	public static class openui extends CommandExecute {
		public openui() {

		}

		@Override
		public int execute(MapleClient c, String[] splitted) {
			// 参数校验：至少需要UI编号
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(6, "語法: !openui [UI編號] ");
				return 0;
			}

			try {
				int uiId = Integer.parseInt(splitted[1]);

				// 基礎UI打開
				c.getSession().writeAndFlush(CField.UIPacket.openUI(uiId));

				// Boss专属参数
				if (splitted.length > 2) {
					String extraData = String.join("  ", Arrays.copyOfRange(splitted, 2, splitted.length));
					c.getSession().writeAndFlush(CWvsContext.InfoPacket.updateClientInfoQuest(uiId, extraData));
				}

				c.getPlayer().dropMessage(6, "已打開 " + uiId + " UI");
			} catch (NumberFormatException e) {
				c.getPlayer().dropMessage(6, "錯誤: UI編號須爲整數");
			}
			return 0;
		}
	}

	// ======================================================================================

	public static class DumpItem extends CommandExecute { // 修改記錄？？ 更新數據庫wz_itemdata表 表示每次新增加的物品需要Dump一次

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length > 1) {
				c.getPlayer().dropMessage(6, "[語法] !UpdateItemInfo");
				return 0;
			}
			// 獲取 MapleItemInformationProvider 實例
			MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
			// 運行更新方法
			ii.runEtc();
			ii.runItems();

			c.getPlayer().dropMessage(6, "[更新物品信息] 物品信息已成功更新.");
			return 1;
		}
	}

	public static class DumpQuest extends CommandExecute { // 修改記錄？？ 更新數據庫wz_questdata表 表示每次新增加的任務需要Dump一次
		public int execute(MapleClient c, String[] splitted) {// 旧的任务数据不需要Dump
			try {
				DatabaseConnection.init(); // 初始化数据库连接
				boolean update = false; // 默认不更新
				for (String file : splitted) {
					if (file.equalsIgnoreCase("-update")) {
						update = true;
					}
				}
				DumpQuests dq = new DumpQuests(update); // 初始化 DumpQuests 类
				System.out.println("Dumping quests");
				dq.dumpQuests(); // 执行任务数据转储
				c.getPlayer().dropMessage(5, "任务数据转储完成");
			} catch (Exception e) {
				e.printStackTrace();
				c.getPlayer().dropMessage(6, "任务数据转储出错: " + e.getMessage());
			}
			return 1;
		}
	}

	// ==================================================================================
	// ==================================================================================

	public static class Scp extends CommandExecute {// 發送封包

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 3) {
				System.out.println(" 语法错误: !Scp [协议头(16进制)] [HEX数据]");
				System.out.println(" 示例: !Scp 0x1234 1A 2B 3C");
				return 0;
			}

			try {
				int header = splitted[1].startsWith("0x") ? Integer.parseInt(splitted[1].substring(2), 16)
						: Integer.parseInt(splitted[1]);

				byte[] payload = HexTool.getByteArrayFromHexString(StringUtil.joinStringFrom(splitted, 2));

				// 构造完整封包（协议头 + 数据）
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				baos.write(header >> 8); // 高位字节
				baos.write(header & 0xFF); // 低位字节
				baos.write(payload);

				// 发送封包
				c.getSession().write(baos.toByteArray());

				// 控制台输出
				System.out.printf("[ 封包发送] 协议头: 0x%04X | 长度: %d字节%n", header, baos.size());
				System.out.println(" 原始数据: " + HexTool.toString(baos.toByteArray()));

				// DEBUG模式额外日志
				if (ServerConstants.DEBUG_SEND) {
					System.out.println("[DEBUG]  封包已写入网络流");
				}

			} catch (NumberFormatException e) {
				System.out.println(" 错误: 协议头必须是整数 - " + e.getMessage());
			} catch (Exception e) {
				System.out.println(" 封包构造失败: " + e.getMessage());
				e.printStackTrace(); // 打印完整堆栈
			}
			return 1;
		}
	}

	public static class SP extends CommandExecute { // 發送封包
		@Override
		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(6, "格式: !SP [HEX数据]");
				return 0;
			}

			try {
				byte[] data = HexTool.getByteArrayFromHexString(StringUtil.joinStringFrom(splitted, 1));
				c.getSession().write(data);
				c.getPlayer().dropMessage(6, "成功發送 " + data.length + " 字節封包");
			} catch (Exception e) {
				c.getPlayer().dropMessage(6, "封包格式錯誤: " + e.getMessage());
			}
			return 1;
		}
	}

	public static class Ts extends CommandExecute {

		@Override
		public int execute(MapleClient c, String[] splitted) {
			ServerConstants.DEBUG_RECEIVE = !ServerConstants.DEBUG_RECEIVE;// 切换接收调试
			ServerConstants.DEBUG_SEND = !ServerConstants.DEBUG_SEND; // 切换发送调试
			return 0;
		}
	}

	public static class Trd extends CommandExecute {

		@Override
		public int execute(MapleClient c, String[] splitted) {
			ServerConstants.DEBUG_RECEIVE = !ServerConstants.DEBUG_RECEIVE; // 仅切换接收调试
			return 0;
		}
	}

	public static class Tsd extends CommandExecute {// 切換封包調試模式true ↔ false

		@Override
		public int execute(MapleClient c, String[] splitted) {
			ServerConstants.DEBUG_SEND = !ServerConstants.DEBUG_SEND;
			c.getPlayer().dropMessage(0, "DEBUG_SEND:  " + ServerConstants.DEBUG_SEND);
			return 0;
		}
	}

	public static class Tdm extends CommandExecute {// 控制封包调试模式指令

		public int execute(MapleClient c, String[] splitted) {
			if (splitted[1].equals("s") || splitted[1].equals("ㄴ")) {
				ServerConstants.DEBUG_SEND = !ServerConstants.DEBUG_SEND;
			} else if (splitted[1].equals("r") || splitted[1].equals("ㄱ")) {
				ServerConstants.DEBUG_RECEIVE = !ServerConstants.DEBUG_RECEIVE;
			}
			c.getPlayer().dropMessage(6, "SEND : " + ServerConstants.DEBUG_SEND + "");
			c.getPlayer().dropMessage(6, "RECV : " + ServerConstants.DEBUG_RECEIVE + "");
			return 0;
		}
	}

	// ======================================================================================

}

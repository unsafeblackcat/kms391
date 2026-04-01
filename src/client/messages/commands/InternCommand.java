package client.messages.commands;

import client.*;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.messages.CommandProcessorUtil;
import constants.ServerConstants;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.world.CheaterData;
import handling.world.World;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.EventInstanceManager;
import scripting.NPCScriptManager;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.enchant.EnchantFlag;
import server.enchant.EquipmentEnchant;
import server.enchant.StarForceStats;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleNPC;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleReactor;
import server.quest.MapleQuest;
import tools.Pair;
import tools.StringUtil;
import tools.packet.CField;
import tools.packet.CWvsContext;

import java.awt.*;
import java.io.File;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.*;

public class InternCommand {

	public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
		return ServerConstants.PlayerGMRank.INTERN;
	}

	public static class AHSkill extends CommandExecute {// 隐身技能触发指令

		public int execute(MapleClient c, String[] splitted) {
			SkillFactory.getSkill(9001004).getEffect(1).applyTo(c.getPlayer(), true);
			return 0;
		}
	}

	public static class hpmpcsh extends CommandExecute {// 满hpmp

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getStat().setHp(1L, c.getPlayer());
			c.getPlayer().updateSingleStat(MapleStat.HP, 1L);
			c.getPlayer().updateSingleStat(MapleStat.MP, 1L);
			return 0;
		}
	}

	public static class npc extends CommandExecute {// 打开npc 3005560,9063145

		public int execute(MapleClient c, String[] splitted) {
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			NPCScriptManager.getInstance().start(c, 9062008, null);
			return 1;
		}
	}

	public static class Heal extends CommandExecute {// 治疗

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getStat().heal(c.getPlayer());
			return 0;
		}
	}

	public static class Goldencarriage extends CommandExecute {// 黄金马车 38

		public int execute(MapleClient c, String[] splitted) {
			LocalDate startDate = LocalDate.of(2021, 2, 25);
			LocalDate finishDate = LocalDate.of(2021, 6, 16);
			// Date startDate = new Date(2020, 03, 01);
			// Date finishDate = new Date(2020, 11, 31);
			// 100432
			ChariotData chariot = c.getChariotData();

			// chariot.setLastAttendenceTime(20210226000000L);

			if (chariot.getIsReadyForAttendence()) {
				c.setCustomData(252, "count", "3600");
			}
			/*
			 * chariot.setComplete(false); chariot.setCurrentDay(8);
			 * chariot.setGoldenPassCount(0); chariot.setBeforeMaxDay(135);
			 * chariot.setCurrentMaxDay(135);
			 */

			// c.getSession().writeAndFlush(CField.getGameMessage(7, "골든패스는 후원포인트 3000을 소모하여
			// 대기시간 없이 출석 가능한 시스템입니다."));

			chariot.refreshChariotInfo();
			c.getSession().writeAndFlush(CField.onUIEventSet(100208, 1254));
			c.getSession().writeAndFlush(CField.onUIEventInfo(c, true));
			return 1;
		}
	}

	public static class HealMap extends CommandExecute {// 全图玩家HP/MP恢复至最大值

		public int execute(MapleClient c, String[] splitted) {
			MapleCharacter player = c.getPlayer();
			for (MapleCharacter mch : player.getMap().getCharacters()) {
				if (mch != null) {
					mch.getStat().setHp(mch.getStat().getMaxHp(), mch);
					mch.updateSingleStat(MapleStat.HP, mch.getStat().getMaxHp());
					mch.getStat().setMp(mch.getStat().getMaxMp(), mch);
					mch.updateSingleStat(MapleStat.MP, mch.getStat().getMaxMp());
				}
			}
			return 1;
		}
	}

	public static class XLZK extends CommandExecute {// 星之力（Star Force）强化折扣调整指令

		public int execute(MapleClient c, String[] splitted) {
			Integer sale = Integer.valueOf(splitted[1]);
			if (sale == null || sale.intValue() < 0 || sale.intValue() > 100) {
				c.getPlayer().dropMessage(6, "語法：!xlzk <折扣率>.");
				return 0;
			}
			ServerConstants.starForceSalePercent = sale.intValue();
			c.getPlayer().dropMessage(6, "星力折叩已調整爲“ " + sale + "%.");
			return 1;
		}
	}

	public static class TempBan extends CommandExecute {// 临时封禁系统 通过!기간밴 <玩家名> <原因编号> <天数>执行临时封禁

		protected boolean ipBan = false;

		private String[] types = new String[] { "핵", "봇", "AD", "HARASS", "CURSE", "SCAM", "MISCONDUCT", "SELL",
				"ICASH", "TEMP", "GM", "IPROGRAM", "메가폰" };

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 4) {
				c.getPlayer().dropMessage(6, "語法：!Tempban [玩家名] [原因編號] [天數]");
				StringBuilder s = new StringBuilder("Tempban reasons: ");
				for (int i = 0; i < this.types.length; i++) {
					s.append(i + 1).append(" - ").append(this.types[i]).append(", ");
				}
				c.getPlayer().dropMessage(6, s.toString());
				return 0;
			}
			MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			int reason = Integer.parseInt(splitted[2]);
			int numDay = Integer.parseInt(splitted[3]);
			Calendar cal = Calendar.getInstance();
			cal.add(5, numDay);
			DateFormat df = DateFormat.getInstance();
			if (victim == null || reason < 0 || reason >= this.types.length) {
				c.getPlayer().dropMessage(6,
						"Unable to find character or reason was not valid, type tempban to see reasons");
				return 0;
			}
			victim.tempban("Temp banned by " + c.getPlayer().getName() + " for " + this.types[reason] + " reason", cal,
					reason, this.ipBan);
			c.getPlayer().dropMessage(6, "The character " + splitted[1] + " has been successfully tempbanned till "
					+ df.format(cal.getTime()));
			return 1;
		}
	}

	public static class BanC extends CommandExecute {// 永久封禁

		protected boolean hellban = false, ipBan = false;

		private String getCommand() {
			if (this.hellban) {
				return "永久封禁";
			}
			return "封禁";
		}

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 3) {
				c.getPlayer().dropMessage(5, "[Syntax] !" + getCommand() + " <IGN> <Reason>");
				return 0;
			}
			StringBuilder sb = new StringBuilder();
			if (this.hellban) {
				sb.append("Banned ").append(splitted[1]).append(": ").append(StringUtil.joinStringFrom(splitted, 2));
			} else {
				sb.append(c.getPlayer().getName()).append(" banned ").append(splitted[1]).append(": ")
						.append(StringUtil.joinStringFrom(splitted, 2));
			}
			MapleCharacter target = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (target != null) {
				if (c.getPlayer().getGMLevel() > target.getGMLevel() || c.getPlayer().isAdmin()) {
					sb.append(" (IP: ").append(target.getClient().getSessionIPAddress()).append(")");
					c.getPlayer().dropMessage(6, "[" + getCommand() + "] Failed to ban.");
					return 0;
				}
				c.getPlayer().dropMessage(6, "[" + getCommand() + "] May not ban GMs...");
				return 1;
			}
			if (MapleCharacter.ban(splitted[1], sb.toString(), false,
					c.getPlayer().isAdmin() ? 250 : c.getPlayer().getGMLevel(), this.hellban)) {
				c.getPlayer().dropMessage(6, "[" + getCommand() + "] Successfully offline banned " + splitted[1] + ".");
				return 1;
			}
			c.getPlayer().dropMessage(6, "[" + getCommand() + "] Failed to ban " + splitted[1]);
			return 0;
		}
	}

	public static class CC extends CommandExecute {// 频道切换指令

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().changeChannel(Integer.parseInt(splitted[1]));
			return 1;
		}
	}

	public static class CCPlayer extends CommandExecute {// 智能频道切换指令

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().changeChannel(World.Find.findChannel(splitted[1]));
			return 1;
		}
	}

	public static class DC extends CommandExecute {// 强制断开玩家连接指令

		public int execute(MapleClient c, String[] splitted) {
			MapleCharacter victim = c.getChannelServer().getPlayerStorage()
					.getCharacterByName(splitted[splitted.length - 1]);
			if (victim != null && c.getPlayer().getGMLevel() >= victim.getGMLevel()) {
				victim.getClient().getSession().close();
				victim.getClient().disconnect(true, false);
				return 1;
			}
			c.getPlayer().dropMessage(6, "受害者不存在.");
			return 0;
		}
	}

	public static class SkillRC extends CommandExecute {// 技能初始化

		@Override
		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().skillReset();
			return 1;
		}
	}

	public static class KVMC extends CommandExecute {// 键值对数据操作指令

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 5) {
				c.getPlayer().dropMessage(6, "語法: !KVMC [玩家名] [類型] [鍵] [值]");
				return 0;
			}
			MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (victim != null) {
				victim.setKeyValue(Integer.parseInt(splitted[2]), splitted[3], splitted[4]);
				c.getPlayer().dropMessage(6, "鍵類修改完成");
				return 1;
			}
			c.getPlayer().dropMessage(6, "玩家不存在.");
			return 0;
		}
	}

	public static class DPCommand extends CommandExecute {// 全服务器玩家强制断开指令

		public int execute(MapleClient c, String[] splitted) {
			String name = splitted[splitted.length - 1];
			for (ChannelServer cserv : ChannelServer.getAllInstances()) {
				for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters().values()) {
					if (player != null && player.getName().contains(splitted[splitted.length - 1])) {
						player.getClient().getChannelServer().getPlayerStorage()
								.deregisterPendingPlayer(player.getId());
						player.getClient().getSession().close();
						player.getClient().disconnect(player, true, false, true);
						c.getPlayer().dropMessage(6, "" + player.getName() + " 連接已斷開.");
					}
				}
			}
			for (MapleCharacter csplayer : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
				if (csplayer != null && csplayer.getName() != null && csplayer.getName().equals(name)) {
					csplayer.getWorldGMMsg(csplayer, "現金商店斷開連接");
					CashShopServer.getPlayerStorage().deregisterPlayer(csplayer);
					csplayer.getClient().disconnect(csplayer, true, true, false);
					csplayer.getClient().getSession().close();
				}
			}
			for (MapleCharacter csplayer : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
				if (csplayer != null && csplayer.getName() != null && csplayer.getName().equals(name)) {
					csplayer.getWorldGMMsg(csplayer, "拍賣行斷開連接");
					AuctionServer.getPlayerStorage().deregisterPlayer(csplayer);
					csplayer.getClient().disconnect(csplayer, true, true, false);
					csplayer.getClient().getSession().close();
				}
			}
			return 0;
		}
	}

	public static class killp extends CommandExecute {// 玩家强制击杀指令

		public int execute(MapleClient c, String[] splitted) {
			MapleCharacter player = c.getPlayer();
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(6, "語法：!killp <列表玩家名稱>");
				return 0;
			}
			MapleCharacter victim = null;
			for (int i = 1; i < splitted.length; i++) {
				try {
					victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[i]);
				} catch (Exception e) {
					c.getPlayer().dropMessage(6, "Player " + splitted[i] + " not found.");
				}
				if (player.allowedToTarget(victim) && player.getGMLevel() >= victim.getGMLevel()) {
					victim.getStat().setHp(0L, victim);
					victim.updateSingleStat(MapleStat.HP, victim.getStat().getHp());
				}
			}
			return 1;
		}
	}

	public static class WhereAmI extends CommandExecute {// 在地图哪里

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dropMessage(5, "在地圖 " + c.getPlayer().getMap().getId());
			return 1;
		}
	}

	public static class itemcheck extends CommandExecute {// 物品检查

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 3 || splitted[1] == null || splitted[1].equals("") || splitted[2] == null
					|| splitted[2].equals("")) {
				c.getPlayer().dropMessage(6, "用法: !itemcheck <玩家名称> <物品ID>");
				return 0;
			}
			int item = Integer.parseInt(splitted[2]);
			MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			int itemamount = chr.getItemQuantity(item, true);
			if (itemamount > 0) {
				c.getPlayer().dropMessage(6, chr.getName() + " 有 " + itemamount + " (" + item + ").");
			} else {
				c.getPlayer().dropMessage(6, chr.getName() + " 沒有這個 (" + item + ")");
			}
			return 1;
		}
	}

	public static class MapMusic extends CommandExecute {// 全地图背景音乐切换指令 !MapMusic <musicID>

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getMap().broadcastMessage(CField.musicChange(splitted[1]));
			return 1;
		}
	}

	public static class CPPoints extends CommandExecute {// 玩家点数检查 !CPPoints <玩家名>

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(6, "語法：!CPPoints <玩家名>.");
				return 0;
			}
			MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (chrs == null) {
				c.getPlayer().dropMessage(6, "確保他們在正確的頻道");
			} else {
				c.getPlayer().dropMessage(6, chrs.getName() + " 有 " + chrs.getPoints() + " 點.");
			}
			return 1;
		}
	}

	public static class VPoints extends CommandExecute {// V矩阵点数查询指令

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(6, "語法：!VPoints <玩家名>.");
				return 0;
			}
			MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (chrs == null) {
				c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
			} else {
				c.getPlayer().dropMessage(6, chrs.getName() + " has " + chrs.getVPoints() + " vpoints.");
			}
			return 1;
		}
	}

	public static class CharacterInfo extends CommandExecute {// 角色信息

		public int execute(MapleClient c, String[] splitted) {
			StringBuilder builder = new StringBuilder();
			MapleCharacter other = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (other == null) {
				builder.append("沒有這個玩家");
				c.getPlayer().dropMessage(6, builder.toString());
				return 0;
			}
			if (other.getClient().getLastPing() <= 0L)
				;
			builder.append(MapleClient.getLogMessage(other, ""));
			builder.append(" at ").append((other.getPosition()).x);
			builder.append(" /").append((other.getPosition()).y);
			builder.append(" || HP : ");
			builder.append(other.getStat().getHp());
			builder.append(" /");
			builder.append(other.getStat().getCurrentMaxHp());
			builder.append(" || MP : ");
			builder.append(other.getStat().getMp());
			builder.append(" /");
			builder.append(other.getStat().getCurrentMaxMp(other));
			builder.append(" || BattleshipHP : ");
			builder.append(other.currentBattleshipHP());
			builder.append(" || WATK : ");
			builder.append(other.getStat().getTotalWatk());
			builder.append(" || MATK : ");
			builder.append(other.getStat().getTotalMagic());
			builder.append(" || MAXDAMAGE : ");
			builder.append(other.getStat().getCurrentMaxBaseDamage());
			builder.append(" || DAMAGE% : ");
			builder.append((other.getStat()).dam_r);
			builder.append(" || BOSSDAMAGE% : ");
			builder.append((other.getStat()).bossdam_r);
			builder.append(" || CRIT CHANCE : ");
			builder.append((other.getStat()).critical_rate);
			builder.append(" || CRIT DAMAGE : ");
			builder.append((other.getStat()).critical_damage);
			builder.append(" || STR : ");
			builder.append(other.getStat().getStr());
			builder.append(" || DEX : ");
			builder.append(other.getStat().getDex());
			builder.append(" || INT : ");
			builder.append(other.getStat().getInt());
			builder.append(" || LUK : ");
			builder.append(other.getStat().getLuk());
			builder.append(" || Total STR : ");
			builder.append(other.getStat().getTotalStr());
			builder.append(" || Total DEX : ");
			builder.append(other.getStat().getTotalDex());
			builder.append(" || Total INT : ");
			builder.append(other.getStat().getTotalInt());
			builder.append(" || Total LUK : ");
			builder.append(other.getStat().getTotalLuk());
			builder.append(" || EXP : ");
			builder.append(other.getExp());
			builder.append(" || MESO : ");
			builder.append(other.getMeso());
			builder.append(" || party : ");
			builder.append((other.getParty() == null) ? -1 : other.getParty().getId());
			builder.append(" || hasTrade: ");
			builder.append((other.getTrade() != null));
			builder.append(" || Latency: ");
			builder.append(other.getClient().getLatency());
			builder.append(" || PING: ");
			builder.append(other.getClient().getLastPing());
			builder.append(" || PONG: ");
			builder.append(other.getClient().getLastPong());
			builder.append(" || remoteAddress: ");
			other.getClient().DebugMessage(builder);
			c.getPlayer().dropMessage(6, builder.toString());
			return 1;
		}
	}

	public static class ReportCC extends CommandExecute {// 举报记录查询指令

		public int execute(MapleClient c, String[] splitted) {
			List<CheaterData> cheaters = World.getReports();
			for (int x = cheaters.size() - 1; x >= 0; x--) {
				CheaterData cheater = cheaters.get(x);
				c.getPlayer().dropMessage(6, cheater.getInfo());
			}
			return 1;
		}
	}

	public static class OnlineUserCount extends CommandExecute {// 在线人数

		public int execute(MapleClient c, String[] splitted) {
			Map<Integer, Integer> connected = World.getConnected();
			StringBuilder conStr = new StringBuilder("已連接的用戶端: ");
			boolean first = true;
			for (Iterator<Integer> iterator = connected.keySet().iterator(); iterator.hasNext();) {
				int i = ((Integer) iterator.next()).intValue();
				if (!first) {
					conStr.append(", ");
				} else {
					first = false;
				}
				if (i == 0) {
					conStr.append("總共: ");
					conStr.append(connected.get(Integer.valueOf(i)));
					continue;
				}
				conStr.append("頻道");
				conStr.append(i);
				conStr.append(": ");
				conStr.append(connected.get(Integer.valueOf(i)));
			}
			c.getPlayer().dropMessage(6, conStr.toString());
			return 1;
		}
	}

	public static class FJ extends CommandExecute {// 最近的传送门

		@Override
		public int execute(MapleClient client, String[] parameters) {
			// 检查输入参数（尽管这个命令可能不需要额外的参数）
			if (parameters.length < 1) {
				client.getPlayer().dropMessage(6, "用法: !fj (無需額外參數)");
				return 0; // 返回0表示输入参数不足或命令使用错误
			}

			// 获取玩家当前位置和所在地图
			Point position = client.getPlayer().getTruePosition();
			MapleMap map = client.getPlayer().getMap();

			// 查找最近的传送门
			MaplePortal portal = map.findClosestPortal(position);

			// 检查是否找到了传送门
			if (portal == null) {
				client.getPlayer().dropMessage(6, "在當前位置附近找不到傳送門。");
				return 0; // 返回0表示没有找到传送门
			}

			// 显示传送门的信息
			client.getPlayer().dropMessage(6, "傳送門名稱: " + portal.getName() + " (傳送門ID: " + portal.getId() + ", 傳送門脚本: "
					+ portal.getScriptName() + ")");

			// 返回1表示命令执行成功（尽管通常返回0表示成功更常见，但这里为了与你的原始代码保持一致而返回1）
			return 1;
		}
	}

	public static class DebugSpawn extends CommandExecute {// 地图怪物数量

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dropMessage(6, c.getPlayer().getMap().spawnDebug());
			return 1;
		}
	}

	public static class SCRCommand extends CommandExecute {// 模拟角色重登录 角色模拟重登录指令

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().fakeRelog();
			return 1;
		}
	}

	public static class DropClear extends CommandExecute {// 删除地图掉落物品

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dropMessage(5, "刪除了 " + c.getPlayer().getMap().getNumItems() + " 個物品");
			c.getPlayer().getMap().removeDrops();
			return 1;
		}
	}

	public static class UptimeCommand extends CommandExecute {// 服务器运行时间

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dropMessage(6,
					"服務器已啓動" + StringUtil.getReadableMillis(ChannelServer.serverStartTime, System.currentTimeMillis()));
			return 1;
		}
	}

	public static class EventInstance extends CommandExecute {// 获取事件信息

		public int execute(MapleClient c, String[] splitted) {
			if (c.getPlayer().getEventInstance() == null) {
				c.getPlayer().dropMessage(5, "附近沒有事件資訊");
			} else {
				EventInstanceManager eim = c.getPlayer().getEventInstance();
				c.getPlayer().dropMessage(5,
						"Event " + eim.getName() + ", charSize: " + eim.getPlayers().size() + ", dcedSize: "
								+ eim.getDisconnected().size() + ", mobSize: " + eim.getMobs().size()
								+ ", eventManager: " + eim.getEventManager().getName() + ", timeLeft: "
								+ eim.getTimeLeft() + ", iprops: " + eim.getProperties().toString() + ", eprops: "
								+ eim.getEventManager().getProperties().toString());
			}
			return 1;
		}
	}

	public static class dtsymob extends CommandExecute {// 地图上所有怪物信息

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
				c.getPlayer().dropMessage(6, "地圖不存在 怪物");
				return 0;
			}
			for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range,
					Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
				MapleMonster mob = (MapleMonster) monstermo;
				c.getPlayer().dropMessage(6, "怪物 " + mob.toString());
			}
			return 1;
		}
	}

	public static class NPCInfo extends CommandExecute {// npc信息

		@Override
		public int execute(MapleClient client, String[] parameters) {
			// 检查输入参数（尽管这个命令可能不需要额外的参数）
			if (parameters.length < 1) {
				client.getPlayer().dropMessage(5, "用法：!npcinfo（不需要其他參數）");
				return 0; // 返回0表示输入参数不足或命令使用错误
			}

			// 获取玩家当前所在的地图
			MapleMap map = client.getPlayer().getMap();

			// 遍历地图上的所有NPC，并显示信息
			for (MapleNPC npc : map.getAllNPCsThreadsafe()) {
				// 无需创建npc的额外引用，直接使用npc变量即可
				Point position = npc.getPosition();
				client.getPlayer().dropMessage(5, "NPCoID: " + npc.getObjectId() + ", NPCID: " + npc.getId()
						+ ", NPC位置: (" + position.getX() + ", " + position.getY() + ")" + ", NPC名字: " + npc.getName());
			}

			// 返回0通常表示命令执行成功（根据惯例）
			return 0;
		}
	}

	public static class ReactorInfo extends CommandExecute {// 反应堆信息

		@Override
		public int execute(MapleClient client, String[] parameters) {
			// 检查输入参数（尽管这个命令可能不需要额外的参数）
			if (parameters.length < 1) {
				client.getPlayer().dropMessage(5, "用法: !reactorinfo (無需額外參數)");
				return 0; // 返回0表示输入参数不足或命令使用错误
			}

			// 获取玩家当前所在的地图
			MapleMap map = client.getPlayer().getMap();

			// 遍历地图上的所有Reactor，并显示信息
			for (MapleMapObject reactorObj : map.getAllReactorsThreadsafe()) {
				MapleReactor reactor = (MapleReactor) reactorObj;
				Point position = reactor.getPosition();
				client.getPlayer().dropMessage(5,
						"反應堆: oID: " + reactor.getObjectId() + ", 反應堆ID: " + reactor.getReactorId() + ", 反應堆位置: ("
								+ position.getX() + ", " + position.getY() + ")" + ", 反應堆狀態: " + reactor.getState()
								+ ", 反應堆名稱: " + (reactor.getName() != null ? reactor.getName() : "null"));
			}

			// 返回0通常表示命令执行成功（根据惯例）
			return 0;
		}
	}

	public static class PortalInfo extends CommandExecute {// 所有传送门信息

		public int execute(MapleClient c, String[] splitted) {
			for (MaplePortal portal : c.getPlayer().getMap().getPortals()) {
				c.getPlayer().dropMessage(5,
						"傳送門ID: " + portal.getId() + " 傳送門脚本: " + portal.getScriptName() + " 傳送門名稱: " + portal.getName()
								+ " 傳送門位置: " + (portal.getPosition()).x + "," + (portal.getPosition()).y + " 傳送目標: "
								+ portal.getTargetMapId() + " / " + portal.getTarget());
			}
			return 0;
		}
	}

	public static class zb extends CommandExecute {

		public int execute(MapleClient c, String[] splitted) {
			Point pos = c.getPlayer().getPosition();
			c.getPlayer().dropMessage(6, "X: " + pos.x + " | Y: " + pos.y + " | RX0: " + (pos.x + 50) + " | RX1: "
					+ (pos.x - 50) + " | FH: " + c.getPlayer().getFH() + " | MapId: " + c.getPlayer().getMapId());
			return 1;
		}
	}

	public static class ShowClock extends CommandExecute {// 全地图时钟广播指令 !ShowClock [秒数]

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getMap()
					.broadcastMessage(CField.getClock(CommandProcessorUtil.getOptionalIntArg(splitted, 1, 60)));
			return 1;
		}
	}

	public static class Clock60 extends CommandExecute {// 当前地图创建时钟60秒

		public int execute(MapleClient c, String[] splitted) {
			MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (victim != null) {
				if (c.getPlayer().inPVP() || (!c.getPlayer().isGM() && victim.isGM())) {
					c.getPlayer().dropMessage(5, "잠시후에 다시시도 해주세요.");
					return 0;
				}
				victim.changeMap(c.getPlayer().getMap(),
						c.getPlayer().getMap().findClosestPortal(c.getPlayer().getTruePosition()));
			} else {
				int ch = World.Find.findChannel(splitted[1]);
				if (ch < 0) {
					c.getPlayer().dropMessage(5, "캐릭터를 찾을 수 없습니다.");
					return 0;
				}
				victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
				if (victim == null || victim.inPVP()) {
					c.getPlayer().dropMessage(5, "잠시후에 다시시도 해주세요.");
					return 0;
				}
				c.getPlayer().dropMessage(5, "채널을 변경하여 소환합니다.");
				victim.changeChannel(c.getChannel());
			}
			return 1;
		}
	}

	public static class ChannelMap extends CommandExecute {

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length >= 3) {
				c.getPlayer().changeChannelMap(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]));
			} else {
				c.getPlayer().dropMessage(1, "用法: !ChannelMap 頻道號 地圖ID。");
			}
			return 1;
		}
	}

	public static class Warp extends CommandExecute {// 地图传送2

		public int execute(MapleClient c, String[] splitted) {
			MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			if (victim != null && c.getPlayer().getGMLevel() >= victim.getGMLevel() && !victim.inPVP()
					&& !c.getPlayer().inPVP()) {
				if (splitted.length == 2) {
					c.getPlayer().changeMap(victim.getMap(),
							victim.getMap().findClosestSpawnpoint(victim.getTruePosition()));
				} else {
					MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory()
							.getMap(Integer.parseInt(splitted[2]));
					if (target == null) {
						c.getPlayer().dropMessage(6, "맵이 존재하지 않습니다.");
						return 0;
					}
					MaplePortal targetPortal = null;
					if (splitted.length > 3)
						try {
							targetPortal = target.getPortal(Integer.parseInt(splitted[3]));
						} catch (IndexOutOfBoundsException e) {
							c.getPlayer().dropMessage(5, "Invalid portal selected.");
						} catch (NumberFormatException numberFormatException) {
						}
					if (targetPortal == null) {
						targetPortal = target.getPortal(0);
					}
					victim.changeMap(target, targetPortal);
				}
			} else {
				try {
					victim = c.getPlayer();
					int ch = World.Find.findChannel(splitted[1]);
					if (ch < 0) {
						MapleMap target = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[1]));
						if (target == null) {
							c.getPlayer().dropMessage(6, "맵이 존재하지 않습니다.");
							return 0;
						}
						MaplePortal targetPortal = null;
						if (splitted.length > 2)
							try {
								targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
							} catch (IndexOutOfBoundsException e) {
								c.getPlayer().dropMessage(5, "Invalid portal selected.");
							} catch (NumberFormatException numberFormatException) {
							}
						if (targetPortal == null) {
							targetPortal = target.getPortal(0);
						}
						c.getPlayer().changeMap(target, targetPortal);
					} else {
						victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
						c.getPlayer().dropMessage(6, "채널을 변경하였습니다. 잠시만 기달려주세요.");
						if (victim.getMapId() != c.getPlayer().getMapId()) {
							MapleMap mapp = c.getChannelServer().getMapFactory().getMap(victim.getMapId());
							c.getPlayer().changeMap(mapp, mapp.findClosestPortal(victim.getTruePosition()));
						}
						c.getPlayer().changeChannel(ch);
					}
				} catch (Exception e) {
					c.getPlayer().dropMessage(6, "Something went wrong " + e.getMessage());
					return 0;
				}
			}
			return 1;
		}
	}

	public static class Jail extends CommandExecute {// 监狱

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 3) {
				c.getPlayer().dropMessage(6, "用法: !jail [玩家名稱] [分鐘,0=永久]");
				return 0;
			}
			MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
			int minutes = Math.max(0, Integer.parseInt(splitted[2]));
			if (victim != null && c.getPlayer().getGMLevel() >= victim.getGMLevel()) {
				MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(180000002);
				victim.getQuestNAdd(MapleQuest.getInstance(123456)).setCustomData(String.valueOf(minutes * 60));
				victim.changeMap(target, target.getPortal(0));
			} else {
				c.getPlayer().dropMessage(6, "Please be on their channel.");
				return 0;
			}
			return 1;
		}
	}

	public static class GlobalMessage extends CommandExecute {// 全服喊话

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length > 1) {
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				if (!c.getPlayer().isGM()) {
					sb.append("Intern ");
				}
				sb.append(c.getPlayer().getName());
				sb.append("] ");
				sb.append(StringUtil.joinStringFrom(splitted, 1));
				World.Broadcast.broadcastMessage(
						CWvsContext.serverNotice(c.getPlayer().isGM() ? 6 : 5, c.getPlayer().getName(), sb.toString()));
			} else {
				c.getPlayer().dropMessage(6, "語法: !GlobalMessage <內容>");
				return 0;
			}
			return 1;
		}
	}

	public static class letter extends CommandExecute {

		public int execute(MapleClient c, String[] splitted) {
			int start, nstart;
			if (splitted.length < 3) {
				c.getPlayer().dropMessage(6, "語法: !letter <Yan色 (green/red)> <單詞>");
				return 0;
			}
			if (splitted[1].equalsIgnoreCase("green")) {
				start = 3991026;
				nstart = 3990019;
			} else if (splitted[1].equalsIgnoreCase("red")) {
				start = 3991000;
				nstart = 3990009;
			} else {
				c.getPlayer().dropMessage(6, "未知Yan色!");
				return 0;
			}
			String splitString = StringUtil.joinStringFrom(splitted, 2);
			List<Integer> chars = new ArrayList<>();
			splitString = splitString.toUpperCase();
			for (int i = 0; i < splitString.length(); i++) {
				char chr = splitString.charAt(i);
				if (chr == ' ') {
					chars.add(Integer.valueOf(-1));
				} else if (chr >= 'A' && chr <= 'Z') {
					chars.add(Integer.valueOf(chr));
				} else if (chr >= '0' && chr <= '9') {
					chars.add(Integer.valueOf(chr + 200));
				}
			}
			int w = 32;
			int dStart = (c.getPlayer().getPosition()).x - splitString.length() / 2 * 32;
			for (Integer integer : chars) {
				if (integer.intValue() == -1) {
					dStart += 32;
					continue;
				}
				if (integer.intValue() < 200) {
					int val = start + integer.intValue() - 65;
					Item item = new Item(val, (short) 0, (short) 1);
					c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), item,
							new Point(dStart, (c.getPlayer().getPosition()).y), false, false);
					dStart += 32;
					continue;
				}
				if (integer.intValue() >= 200 && integer.intValue() <= 300) {
					int val = nstart + integer.intValue() - 48 - 200;
					Item item = new Item(val, (short) 0, (short) 1);
					c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), item,
							new Point(dStart, (c.getPlayer().getPosition()).y), false, false);
					dStart += 32;
				}
			}
			return 1;
		}
	}

	public static class SearchCmd extends CommandExecute {// 全游戏数据搜索指令

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length == 1) {
				c.getPlayer().dropMessage(6, splitted[0] + ": <엔피시> <몹> <아이템> <맵> <스킬> <퀘스트> <스크립트>");
			} else if (splitted.length == 2) {
				c.getPlayer().dropMessage(6, "검색어를 입력하지 않았습니다.");
			} else {
				String type = splitted[1];
				String search = StringUtil.joinStringFrom(splitted, 2);
				MapleData data = null;
				MapleDataProvider dataProvider = MapleDataProviderFactory
						.getDataProvider(new File(System.getProperty("wz") + "/String.wz"));
				c.getPlayer().dropMessage(6, "<<타입: " + type + " | 검색어: " + search + ">>");
				if (type.equalsIgnoreCase("엔피시")) {
					List<String> retNpcs = new ArrayList<>();
					data = dataProvider.getData("Npc.img");
					List<Pair<Integer, String>> npcPairList = new LinkedList<>();
					for (MapleData npcIdData : data.getChildren()) {
						npcPairList.add(new Pair<>(Integer.valueOf(Integer.parseInt(npcIdData.getName())),
								MapleDataTool.getString(npcIdData.getChildByPath("name"), "NO-NAME")));
					}
					for (Pair<Integer, String> npcPair : npcPairList) {
						if (((String) npcPair.getRight()).toLowerCase().contains(search.toLowerCase())) {
							retNpcs.add((new StringBuilder()).append(npcPair.getLeft()).append(" - ")
									.append(npcPair.getRight()).toString());
						}
					}
					if (retNpcs != null && retNpcs.size() > 0) {
						for (String singleRetNpc : retNpcs) {
							c.getPlayer().dropMessage(6, singleRetNpc);
						}
					} else {
						c.getPlayer().dropMessage(6, "입력한 엔피시코드를 찾을 수 없습니다.");
					}
				} else if (type.equalsIgnoreCase("맵")) {
					List<String> retMaps = new ArrayList<>();
					data = dataProvider.getData("Map.img");
					List<Pair<Integer, String>> mapPairList = new LinkedList<>();
					for (MapleData mapAreaData : data.getChildren()) {
						for (MapleData mapIdData : mapAreaData.getChildren()) {
							mapPairList.add(new Pair<>(Integer.valueOf(Integer.parseInt(mapIdData.getName())),
									MapleDataTool.getString(mapIdData.getChildByPath("streetName"), "NO-NAME") + " - "
											+ MapleDataTool.getString(mapIdData.getChildByPath("mapName"), "NO-NAME")));
						}
					}
					for (Pair<Integer, String> mapPair : mapPairList) {
						if (((String) mapPair.getRight()).toLowerCase().contains(search.toLowerCase())) {
							retMaps.add((new StringBuilder()).append(mapPair.getLeft()).append(" - ")
									.append(mapPair.getRight()).toString());
						}
					}
					if (retMaps != null && retMaps.size() > 0) {
						for (String singleRetMap : retMaps) {
							c.getPlayer().dropMessage(6, singleRetMap);
						}
					} else {
						c.getPlayer().dropMessage(6, "입력한 맵코드를 찾을 수 없습니다.");
					}
				} else if (type.equalsIgnoreCase("몹")) {
					List<String> retMobs = new ArrayList<>();
					data = dataProvider.getData("Mob.img");
					List<Pair<Integer, String>> mobPairList = new LinkedList<>();
					for (MapleData mobIdData : data.getChildren()) {
						mobPairList.add(new Pair<>(Integer.valueOf(Integer.parseInt(mobIdData.getName())),
								MapleDataTool.getString(mobIdData.getChildByPath("name"), "NO-NAME")));
					}
					for (Pair<Integer, String> mobPair : mobPairList) {
						if (((String) mobPair.getRight()).toLowerCase().contains(search.toLowerCase())) {
							retMobs.add((new StringBuilder()).append(mobPair.getLeft()).append(" - ")
									.append(mobPair.getRight()).toString());
						}
					}
					if (retMobs != null && retMobs.size() > 0) {
						for (String singleRetMob : retMobs) {
							c.getPlayer().dropMessage(6, singleRetMob);
						}
					} else {
						c.getPlayer().dropMessage(6, "입력한 몹코드를 찾을 수 없습니다.");
					}
				} else if (type.equalsIgnoreCase("아이템")) {
					List<String> retItems = new ArrayList<>();
					for (Pair<Integer, String> itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
						if (((String) itemPair.getRight()).toLowerCase().contains(search.toLowerCase())) {
							retItems.add((new StringBuilder()).append(itemPair.getLeft()).append(" - ")
									.append(itemPair.getRight()).toString());
						}
					}
					if (retItems != null && retItems.size() > 0) {
						for (String singleRetItem : retItems) {
							c.getPlayer().dropMessage(6, singleRetItem);
						}
					} else {
						c.getPlayer().dropMessage(6, "입력한 아이템코드를 찾을 수 없습니다.");
					}
				} else if (type.equalsIgnoreCase("퀘스트")) {
					List<String> retItems = new ArrayList<>();
					for (MapleQuest itemPair : MapleQuest.getAllInstances()) {
						if (itemPair.getName().length() > 0
								&& itemPair.getName().toLowerCase().contains(search.toLowerCase())) {
							retItems.add(itemPair.getId() + " - " + itemPair.getName());
						}
					}
					if (retItems != null && retItems.size() > 0) {
						for (String singleRetItem : retItems) {
							c.getPlayer().dropMessage(6, singleRetItem);
						}
					} else {
						c.getPlayer().dropMessage(6, "입력한 퀘스트코드를 찾을 수 없습니다.");
					}
				} else if (type.equalsIgnoreCase("스킬")) {
					List<String> retSkills = new ArrayList<>();
					for (Skill skil : SkillFactory.getAllSkills()) {
						if (skil.getName() != null && skil.getName().toLowerCase().contains(search.toLowerCase())) {
							retSkills.add(skil.getId() + " - " + skil.getName());
						}
					}
					if (retSkills != null && retSkills.size() > 0) {
						for (String singleRetSkill : retSkills) {
							c.getPlayer().dropMessage(6, singleRetSkill);
						}
					} else {
						c.getPlayer().dropMessage(6, "입력한 스킬코드를 찾을 수 없습니다.");
					}
				} else if (type.equalsIgnoreCase("스크립트")) {
					List<String> retScripts = new ArrayList<>();
					for (Map.Entry<Integer, String> scri : MapleLifeFactory.getNpcScripts().entrySet()) {
						if (((String) scri.getValue()).toLowerCase().contains(search.toLowerCase())) {
							retScripts.add((new StringBuilder()).append(scri.getKey()).append(" - ")
									.append(scri.getValue()).toString());
						}
					}
					if (retScripts != null && retScripts.size() > 0) {
						for (String singleRetSkill : retScripts) {
							c.getPlayer().dropMessage(6, singleRetSkill);
						}
					} else {
						c.getPlayer().dropMessage(6, "입력한 스크립트를 찾을 수 없습니다.");
					}
				} else {
					c.getPlayer().dropMessage(6, "검색을 할 수 없습니다, 검색타입을 확인후 다시시도 해주세요.");
				}
			}
			return 0;
		}
	}

	public static class StarForce extends CommandExecute {// 强化第一格装备

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(5, "!StarForce <幾星？> - 裝備標籤第一個物品强化.");
			} else {
				Equip nEquip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1);
				if (nEquip != null) {
					while (nEquip.getEnhance() < Integer.parseInt(splitted[1])) {
						StarForceStats statz = EquipmentEnchant.starForceStats(nEquip);
						nEquip.setEnhance((byte) (nEquip.getEnhance() + 1));
						for (Pair<EnchantFlag, Integer> stat : statz.getStats()) {
							if (EnchantFlag.Watk.check(((EnchantFlag) stat.left).getValue())) {
								nEquip.setWatk((short) (nEquip.getWatk() + ((Integer) stat.right).intValue()));
							}
							if (EnchantFlag.Matk.check(((EnchantFlag) stat.left).getValue())) {
								nEquip.setMatk((short) (nEquip.getMatk() + ((Integer) stat.right).intValue()));
							}
							if (EnchantFlag.Str.check(((EnchantFlag) stat.left).getValue())) {
								nEquip.setStr((short) (nEquip.getStr() + ((Integer) stat.right).intValue()));
							}
							if (EnchantFlag.Dex.check(((EnchantFlag) stat.left).getValue())) {
								nEquip.setDex((short) (nEquip.getDex() + ((Integer) stat.right).intValue()));
							}
							if (EnchantFlag.Int.check(((EnchantFlag) stat.left).getValue())) {
								nEquip.setInt((short) (nEquip.getInt() + ((Integer) stat.right).intValue()));
							}
							if (EnchantFlag.Luk.check(((EnchantFlag) stat.left).getValue())) {
								nEquip.setLuk((short) (nEquip.getLuk() + ((Integer) stat.right).intValue()));
							}
							if (EnchantFlag.Wdef.check(((EnchantFlag) stat.left).getValue())) {
								nEquip.setWdef((short) (nEquip.getWdef() + ((Integer) stat.right).intValue()));
							}
							if (EnchantFlag.Mdef.check(((EnchantFlag) stat.left).getValue())) {
								nEquip.setMdef((short) (nEquip.getMdef() + ((Integer) stat.right).intValue()));
							}
							if (EnchantFlag.Hp.check(((EnchantFlag) stat.left).getValue())) {
								nEquip.setHp((short) (nEquip.getHp() + ((Integer) stat.right).intValue()));
							}
							if (EnchantFlag.Mp.check(((EnchantFlag) stat.left).getValue())) {
								nEquip.setMp((short) (nEquip.getMp() + ((Integer) stat.right).intValue()));
							}
							if (EnchantFlag.Acc.check(((EnchantFlag) stat.left).getValue())) {
								nEquip.setAcc((short) (nEquip.getAcc() + ((Integer) stat.right).intValue()));
							}
							if (EnchantFlag.Avoid.check(((EnchantFlag) stat.left).getValue())) {
								nEquip.setAvoid((short) (nEquip.getAvoid() + ((Integer) stat.right).intValue()));
							}
						}
					}
				}
			}
			return 1;
		}
	}

	public static class SFRC extends CommandExecute {// 星之力强化解除指令 星之力量釋放 !스타포스해제 <装备位置>

		public int execute(MapleClient c, String[] splitted) {
			Equip item = null;
			item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(Short.parseShort(splitted[1]));
			if (item != null) {
				while (item.getEnhance() > 0) {
					item.setEnhance((byte) (item.getEnhance() - 1));
					StarForceStats stats = EquipmentEnchant.starForceStats(item);
					for (Pair<EnchantFlag, Integer> stat : stats.getStats()) {
						if (EnchantFlag.Watk.check(((EnchantFlag) stat.left).getValue())) {
							int watk = item.getWatk();
							if (watk / 50 != (item.getWatk() - ((Integer) stat.right).intValue()) / 50) {
								item.setWatk((short) (item.getWatk() - ((Integer) stat.right).intValue() + 1));
							} else {
								item.setWatk((short) (item.getWatk() - ((Integer) stat.right).intValue()));
							}
						}
						if (EnchantFlag.Matk.check(((EnchantFlag) stat.left).getValue())) {
							int matk = item.getMatk();
							if (matk / 50 != (item.getMatk() - ((Integer) stat.right).intValue()) / 50) {
								item.setMatk((short) (item.getMatk() - ((Integer) stat.right).intValue() + 1));
							} else {
								item.setMatk((short) (item.getMatk() - ((Integer) stat.right).intValue()));
							}
						}
						if (EnchantFlag.Str.check(((EnchantFlag) stat.left).getValue())) {
							item.setStr((short) (item.getStr() - ((Integer) stat.right).intValue()));
						}
						if (EnchantFlag.Dex.check(((EnchantFlag) stat.left).getValue())) {
							item.setDex((short) (item.getDex() - ((Integer) stat.right).intValue()));
						}
						if (EnchantFlag.Int.check(((EnchantFlag) stat.left).getValue())) {
							item.setInt((short) (item.getInt() - ((Integer) stat.right).intValue()));
						}
						if (EnchantFlag.Luk.check(((EnchantFlag) stat.left).getValue())) {
							item.setLuk((short) (item.getLuk() - ((Integer) stat.right).intValue()));
						}
						if (EnchantFlag.Wdef.check(((EnchantFlag) stat.left).getValue())) {
							item.setWdef((short) (item.getWdef() - ((Integer) stat.right).intValue()));
						}
						if (EnchantFlag.Mdef.check(((EnchantFlag) stat.left).getValue())) {
							item.setMdef((short) (item.getMdef() - ((Integer) stat.right).intValue()));
						}
						if (EnchantFlag.Hp.check(((EnchantFlag) stat.left).getValue())) {
							item.setHp((short) (item.getHp() - ((Integer) stat.right).intValue()));
						}
						if (EnchantFlag.Mp.check(((EnchantFlag) stat.left).getValue())) {
							item.setMp((short) (item.getMp() - ((Integer) stat.right).intValue()));
						}
						if (EnchantFlag.Acc.check(((EnchantFlag) stat.left).getValue())) {
							item.setAcc((short) (item.getAcc() - ((Integer) stat.right).intValue()));
						}
						if (EnchantFlag.Avoid.check(((EnchantFlag) stat.left).getValue())) {
							item.setAvoid((short) (item.getAvoid() - ((Integer) stat.right).intValue()));
						}
					}
				}
				c.getPlayer().forceReAddItem(item, MapleInventoryType.EQUIP);
			} else {
				c.getPlayer().dropMessage(1, "語法：!스타포스해제 <裝備位置>。");
			}
			return 1;
		}
	}

	public static class SkillMapMob extends CommandExecute {// 杀死地图所有怪物

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
			byte animation = 1;
			if (map == null) {
				c.getPlayer().dropMessage(6, "語法: !SkillMapMob");
				return 0;
			}
			for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range,
					Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
				MapleMonster mob = (MapleMonster) monstermo;
				if (!mob.getStats().isBoss() || mob.getStats().isPartyBonus() || c.getPlayer().isGM()) {
					map.killMonster(mob, c.getPlayer(), true, false, animation);
				}
			}
			return 1;
		}
	}

	public static class HuntingUsers extends CommandExecute {// 查看狩猎玩家

		public int execute(MapleClient c, String[] splitted) {
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for (MapleCharacter chr : World.getAllCharacters()) {
				if (chr.getMap().isSpawnPoint()) {
					sb.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
					sb.append(",");
					i++;
				}
			}
			c.getPlayer().dropMessage(6, sb.toString());
			c.getPlayer().dropMessage(6, "目前正在狩獵的玩家人數爲 : " + i + "名.");
			return 1;
		}
	}

	public static class TotalOnline extends CommandExecute {// 显示所有频道玩家数量

		public int execute(MapleClient c, String[] splitted) {
			for (ChannelServer cs : ChannelServer.getAllInstances()) {
				c.getPlayer().dropMessage(6, cs.getChannel() + "頻道 : " + cs.getPlayerStorage().getOnlinePlayers(true));
			}
			return 1;
		}
	}

	public static class EmailCmd extends CommandExecute {// 邮件认证信息修改指令 !EmailCmd <账号> <密码>

		public int execute(MapleClient c, String[] splitted) {
			ServerConstants.mailid = splitted[1];
			ServerConstants.mailpw = splitted[2];
			c.getPlayer().dropMessage(5, "電子郵件ID : " + ServerConstants.mailid);
			c.getPlayer().dropMessage(5, "電子郵件密碼 : " + ServerConstants.mailpw);
			c.getPlayer().dropMessage(5, "電子郵件身份驗證身份更改完成.");
			return 1;
		}
	}
}

package client.messages.commands;

import client.*;
import client.messages.CommandProcessorUtil;
import constants.GameConstants;
import constants.KoreaCalendar;
import constants.ServerConstants;
import handling.auction.AuctionServer;
import handling.channel.ChannelServer;
import server.ShutdownServer;
import server.Timer;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import tools.CPUSampler;
import tools.Pair;
import tools.packet.CField;
import tools.packet.CWvsContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import server.MapleItemInformationProvider;
import server.quest.MapleQuest;
import tools.HexTool;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

public class AdminCommand {

	public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
		return ServerConstants.PlayerGMRank.ADMIN;
	}

	public static class BuffTest extends CommandExecute {// BUFF效果测试指令

		public int execute(MapleClient c, String[] splitted) {

			int count = Integer.parseInt(splitted[1]);
			byte[] msg = HexTool.getByteArrayFromHexString(
					"14 00 00 00 00 00 15 00 00 00 00 00 1A 00 00 00 00 00 21 00 00 00 00 00 29 00 00 00 00 00 2B 00 00 00 00 00 2E 00 00 00 00 00 33 04 2D 00 00 00 3B 04 27 00 00 00 3C 04 D0 00 00 00 3D 04 D1 00 00 00 3E 04 1B 00 00 00 3F 04 16 00 00 00 40 04 19 00 00 00 41 04 31 00 00 00 42 04 2A 00 00 00 43 04 23 00 00 00 44 04 0E 00 00 00 57 04 09 00 00 00");
			LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(msg));
			List<Byte> key = new ArrayList<>();
			List<Byte> type = new ArrayList<>();
			List<Integer> action = new ArrayList<>();
			for (int i = 0; i < 19; i++) {
				key.add(slea.readByte());
				type.add(slea.readByte());
				action.add(slea.readInt());
			}
			System.out.println("key : " + key);

			// c.getPlayer().setKeyValue(1477, "count",
			// String.valueOf(c.getPlayer().getKeyValue(1477, "count") + count));
			// int rate = Integer.parseInt(splitted[1]);
			// c.getSession().writeAndFlush((Object)
			// CField.environmentChange("Dojang/clear", 7));
			return 1;
		}
	}

	public static class MesoEveryone extends CommandExecute {

		public int execute(MapleClient c, String[] splitted) {
			for (ChannelServer cserv : ChannelServer.getAllInstances()) {
				for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters().values()) {
					mch.gainMeso(Integer.parseInt(splitted[1]), true);
				}
			}
			return 1;
		}
	}

	public static class ExpRate extends CommandExecute {// 設置經驗賠率

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length > 1) {
				int rate = Integer.parseInt(splitted[1]);
				if (splitted.length > 2 && splitted[2].equalsIgnoreCase("all")) {
					for (ChannelServer cserv : ChannelServer.getAllInstances()) {
						cserv.setExpRate(rate);
					}
				} else {
					c.getChannelServer().setExpRate(rate);
				}
				c.getPlayer().dropMessage(6, "Exprate has been changed to " + rate + "x");
			} else {
				c.getPlayer().dropMessage(6, "語法: !exprate <倍率> [頻道]");
			}
			return 1;
		}
	}

	public static class LinkSummon extends CommandExecute {// 連接召喚怪物

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().setLinkMobCount(100);
			for (int i = 0; i < 10; i++) {
				MapleMonster m = MapleLifeFactory.getMonster(100100);
				m.setHp(m.getStats().getHp());
				m.getStats().setHp(m.getStats().getHp());
				m.setOwner(c.getPlayer().getId());
				c.getPlayer().getMap().spawnMonsterOnGroundBelow(m, c.getPlayer().getTruePosition());
			}
			return 1;
		}
	}

	public static class AuctionStorage extends CommandExecute {// 保存拍賣

		public int execute(MapleClient c, String[] Splitted) {
			AuctionServer.saveItems();
			c.getPlayer().dropMessage(6, "已保存拍賣行數據.");
			return 1;
		}
	}

	public static class MesoRate extends CommandExecute {

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length > 1) {
				int rate = Integer.parseInt(splitted[1]);
				if (splitted.length > 2 && splitted[2].equalsIgnoreCase("all")) {
					for (ChannelServer cserv : ChannelServer.getAllInstances()) {
						cserv.setMesoRate(rate);
					}
				} else {
					c.getChannelServer().setMesoRate(rate);
				}
				c.getPlayer().dropMessage(6, "Meso Rate has been changed to " + rate + "x");
			} else {
				c.getPlayer().dropMessage(6, "Syntax: !mesorate <number> [all]");
			}
			return 1;
		}
	}

	public static class DCAll extends CommandExecute {

		public int execute(MapleClient c, String[] splitted) {
			int range = -1;
			if (splitted[1].equals("m")) {
				range = 0;
			} else if (splitted[1].equals("c")) {
				range = 1;
			} else if (splitted[1].equals("w")) {
				range = 2;
			}
			if (range == -1) {
				range = 1;
			}
			if (range == 0) {
				c.getPlayer().getMap().disconnectAll();
			} else if (range == 1) {
				c.getChannelServer().getPlayerStorage().disconnectAll(true);
			} else if (range == 2) {
				for (ChannelServer cserv : ChannelServer.getAllInstances()) {
					cserv.getPlayerStorage().disconnectAll(true);
				}
			}
			return 1;
		}
	}

	public static class Shutdown extends CommandExecute {

		protected static Thread t = null;

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dropMessage(6, "Shutting down...");
			if (t == null || !t.isAlive()) {
				t = new Thread(ShutdownServer.getInstance());
				ShutdownServer.getInstance().shutdown();
				t.start();
			} else {
				c.getPlayer().dropMessage(6,
						"A shutdown thread is already in progress or shutdown has not been done. Please wait.");
			}
			return 1;
		}
	}

	public static class RebootTimer extends Shutdown {// 關閉服務器

		private static ScheduledFuture<?> ts = null;

		private int minutesLeft = 0;

		public int execute(MapleClient c, String[] splitted) {
			this.minutesLeft = Integer.parseInt(splitted[1]);
			KoreaCalendar kc = new KoreaCalendar();
			int hour = kc.getHour();
			int min = kc.getMin() + this.minutesLeft;
			if (min >= 60) {
				hour++;
				min -= 60;
				if (hour >= 24) {
					hour = 0;
				}
			}
			String am = (hour >= 12) ? "下午" : "上午";
			String type = (Integer.parseInt(splitted[2]) == 1) ? "補丁程式" : "檢查";
			for (ChannelServer cserv : ChannelServer.getAllInstances()) {
				cserv.setServerMessage("大家好，我是Fancy. 稍後將進行 " + am + " " + hour + "時 " + min + "分鐘到服務器" + type
						+ "為了順利進行.請立即關閉連接.抱歉給您的使用帶來不便.");
			}
			if (ts == null && (t == null || !t.isAlive())) {
				t = new Thread(ShutdownServer.getInstance());
				ts = Timer.EventTimer.getInstance().register(new Runnable() {
					public void run() {
						if (RebootTimer.this.minutesLeft == 0) {
							ShutdownServer.getInstance().shutdown();
							Shutdown.t.start();
							RebootTimer.ts.cancel(false);
							return;
						}
						RebootTimer.this.minutesLeft--;
					}
				}, 60000L);
			} else {
				c.getPlayer().dropMessage(6, "已有正在運行的倒數計時.");
			}
			return 1;
		}
	}

	public static class StartProfiling extends CommandExecute {// 性能分析

		public int execute(MapleClient c, String[] splitted) {
			CPUSampler sampler = CPUSampler.getInstance();
			sampler.addIncluded("client");
			sampler.addIncluded("connector");
			sampler.addIncluded("constants");
			sampler.addIncluded("database");
			sampler.addIncluded("handling");
			sampler.addIncluded("log");
			sampler.addIncluded("provider");
			sampler.addIncluded("scripting");
			sampler.addIncluded("server");
			sampler.addIncluded("tools");
			sampler.start();
			c.getPlayer().dropMessageGM(-5, "開始效能分析.");
			return 1;
		}
	}

	public static class StopProfiling extends CommandExecute {// 性能分析停止与数据导出命令

		public int execute(MapleClient c, String[] splitted) {
			CPUSampler sampler = CPUSampler.getInstance();
			try {
				String filename = "CPUStopProfiling.txt";
				if (splitted.length > 1) {
					filename = splitted[1];
				}
				File file = new File(filename);
				if (file.exists()) {
					c.getPlayer().dropMessage(6, "檔案已存在，請删除或更改檔名.");
					return 0;
				}
				sampler.stop();
				FileWriter fw = new FileWriter(file);
				sampler.save(fw, 1, 10);
				fw.close();
				sampler.reset();
				c.getPlayer().dropMessage(6, "檔案已保存.");
			} catch (IOException e) {
				System.err.println("保存設定檔時出錯" + e);
			}
			return 1;
		}
	}

	public static class CooldownReset extends CommandExecute {// 清除玩家的所有技能冷却时间

		@Override
		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().clearAllCooldowns();
			return 1;
		}
	}

	public static class CSP extends CommandExecute {// 游戏点券（Cash Shop Points）发放的GM指令

		@Override
		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(5, "Need amount.");
				return 0;
			}
			c.getPlayer().modifyCSPoints(1, Integer.parseInt(splitted[1]), true);
			return 1;
		}
	}

	public static class maxsx extends CommandExecute {// 加滿屬性

		@Override
		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getStat().setDex((short) 32767, c.getPlayer());
			c.getPlayer().getStat().setInt((short) 32767, c.getPlayer());
			c.getPlayer().getStat().setLuk((short) 32767, c.getPlayer());
			c.getPlayer().getStat().setMaxHp(500000, c.getPlayer());
			if (!GameConstants.isZero(c.getPlayer().getJob())) {
				c.getPlayer().getStat().setMaxMp(500000, c.getPlayer());
				c.getPlayer().getStat().setMp(500000, c.getPlayer());
			}
			c.getPlayer().getStat().setHp(500000, c.getPlayer());
			c.getPlayer().getStat().setStr((short) 32767, c.getPlayer());
			c.getPlayer().updateSingleStat(MapleStat.STR, 32767);
			c.getPlayer().updateSingleStat(MapleStat.DEX, 32767);
			c.getPlayer().updateSingleStat(MapleStat.INT, 32767);
			c.getPlayer().updateSingleStat(MapleStat.LUK, 32767);
			c.getPlayer().updateSingleStat(MapleStat.MAXHP, 500000);
			if (!GameConstants.isZero(c.getPlayer().getJob())) {
				c.getPlayer().updateSingleStat(MapleStat.MAXMP, 500000);
				c.getPlayer().updateSingleStat(MapleStat.MP, 500000);
			}
			c.getPlayer().updateSingleStat(MapleStat.HP, 500000);
			return 1;
		}
	}

	public static class usx extends CommandExecute {// 屬性初始化

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getStat().setStr((short) 100, c.getPlayer());
			c.getPlayer().getStat().setDex((short) 100, c.getPlayer());
			c.getPlayer().getStat().setInt((short) 100, c.getPlayer());
			c.getPlayer().getStat().setLuk((short) 100, c.getPlayer());
			c.getPlayer().getStat().setMaxHp(10000L, c.getPlayer());
			if (!GameConstants.isZero(c.getPlayer().getJob())) {
				c.getPlayer().getStat().setMaxMp(10000L, c.getPlayer());
				c.getPlayer().getStat().setMp(10000L, c.getPlayer());
			}
			c.getPlayer().getStat().setHp(10000L, c.getPlayer());
			c.getPlayer().updateSingleStat(MapleStat.STR, 100L);
			c.getPlayer().updateSingleStat(MapleStat.DEX, 100L);
			c.getPlayer().updateSingleStat(MapleStat.INT, 100L);
			c.getPlayer().updateSingleStat(MapleStat.LUK, 100L);
			c.getPlayer().updateSingleStat(MapleStat.MAXHP, 10000L);
			if (!GameConstants.isZero(c.getPlayer().getJob())) {
				c.getPlayer().updateSingleStat(MapleStat.MAXMP, 10000L);
				c.getPlayer().updateSingleStat(MapleStat.MP, 10000L);
			}
			c.getPlayer().updateSingleStat(MapleStat.HP, 10000L);
			return 1;
		}
	}

	public static class BroadcastTime extends CommandExecute {// 全地图时间广播指令

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().getMap()
					.broadcastMessage(CField.getClock(CommandProcessorUtil.getOptionalIntArg(splitted, 1, 60)));
			return 1;
		}
	}

	public static class IICT extends CommandExecute {// 调整玩家的网吧累计时长

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(5, "Need amount.");
				return 0;
			}
			c.getPlayer().setInternetCafeTime(c.getPlayer().getInternetCafeTime() + Integer.parseInt(splitted[1]));
			c.getPlayer().dropMessage(6, "PC방 정량제를 " + Integer.parseInt(splitted[1]) + "분 늘렸습니다.");
			return 1;
		}
	}

	public static class CClear extends CommandExecute {// 清除玩家所有技能（包括物品）的冷却状态

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().clearAllCooldowns();
			return 1;
		}
	}

	public static class DBuff extends CommandExecute {// 状态查询指令 玩家当前BUFF 增益效果

		public int execute(MapleClient c, String[] splitted) {
			c.getPlayer().dropMessage(5, "當前BUFF效果列表.");
			for (Pair<SecondaryStat, SecondaryStatValueHolder> effect : c.getPlayer().getEffects()) {
				c.getPlayer().dropMessage(-8,
						((SecondaryStat) effect.left).name() + " : "
								+ ((SecondaryStatValueHolder) effect.right).effect.getSourceId() + " / "
								+ ((SecondaryStatValueHolder) effect.right).localDuration);
			}
			return 1;
		}
	}

	public static class BSTest extends CommandExecute {// 给玩家应用一个指定的增益效果（buff）

		public int execute(MapleClient c, String[] splitted) {
			if (splitted.length < 2) {
				c.getPlayer().dropMessage(5, "請選擇BUFF類型.");
				return 0;
			}
			int type = Integer.parseInt(splitted[1]);
			if (type > SecondaryStat.getUnkBuffStats().size()) {
				c.getPlayer().dropMessage(5, "最大尺寸 : " + SecondaryStat.getUnkBuffStats().size());
				return 0;
			}
			SecondaryStat stat = SecondaryStat.getUnkBuffStats().get(type);
			Map<SecondaryStat, Pair<Integer, Integer>> dds = new HashMap<>();
			dds.put(stat, new Pair<>(Integer.valueOf(1), Integer.valueOf(0)));
			c.getSession().writeAndFlush(
					CWvsContext.BuffPacket.giveBuff(dds, SkillFactory.getSkill(2121004).getEffect(20), c.getPlayer()));
			c.getPlayer().dropMessage(5, "應用的增益效果 : " + stat.name());
			return 1;
		}
	}
}

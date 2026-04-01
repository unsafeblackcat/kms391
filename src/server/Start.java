package server;

import client.DreamBreakerRank;
import client.MapleCharacter;
import client.SkillFactory;
import client.inventory.MapleInventoryIdentifier;
import constants.Connector.KoreaServer;
import constants.GameConstants;
import constants.ServerConstants;
import constants.programs.ControlUnit;
import database.DatabaseConnection;
import handling.MapleSaveHandler;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.channel.handler.MatrixHandler;
import handling.channel.handler.UnionHandler;
import handling.login.LoginInformationProvider;
import handling.login.LoginServer;
import handling.world.World;
import server.control.MapleEtcControl;
import server.control.MapleIndexTimer;
import server.events.MapleOxQuizFactory;
import server.field.boss.FieldSkillFactory;
import server.field.boss.lucid.Butterfly;
import server.field.boss.will.SpiderWeb;
import server.life.*;
import server.maps.MapleMap;
import server.marriage.MarriageManager;
import server.quest.MapleQuest;
import server.quest.QuestCompleteStatus;
import tools.CMDCommand;
import tools.packet.BossRewardMeso;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import security.connector.ConnectorServer;
import security.crc.CRCServer;

public class Start {

	public static transient ScheduledFuture<?> boss;
	public static long startTime = System.currentTimeMillis();

	public static final Start instance = new Start();

	public static AtomicInteger CompletedLoadingThreads = new AtomicInteger(0);

	public void run() throws InterruptedException {

		// 初始化数据库连接
		DatabaseConnection.init();

		// 加载服务器配置
		loadServerConfiguration();

		// 设置管理员模式
		if (Boolean.parseBoolean(ServerProperties.getProperty("world.admin"))) {
			ServerConstants.Use_Fixed_IV = false;
			System.out.println("[!!! Admin Only Mode Active !!!]");
		}

		// 设置WZ文件路径
		System.setProperty("wz", "wz");

		// 重置所有账户的登录状态
		resetAllAccountLoginStatus();

		// 初始化世界服务器
		World.init();

		// 启动各类计时器
		startTimers();

		// 设置维度镜像菜单
		setupDimensionMirrors();

		// 加载各种游戏数据
		try {
			DreamBreakerRank.LoadRank();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		JamsuPoint();
		Butterfly.load();
		SpiderWeb.load();
		Setting.CashShopSetting();

		// 启动多线程加载器
		AllLoding allLoding = new AllLoding();
		allLoding.start();

		// 启动服务器组件
		System.out.println("[正在加載登入]");
		LoginServer.run_startup_configurations();

		System.out.println("[加載通道]");
		ChannelServer.startChannel_Main();

		System.out.println("[加載現金商店]");
		CashShopServer.run_startup_configurations();

		// 添加关闭钩子
		Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));

		// 加载玩家NPC
		PlayerNPC.loadAll();

		// 启用登录服务器
		LoginServer.setOn();

		// 注册定时任务
		Timer.WorldTimer.getInstance().register(new MapleEtcControl(), 1000);
		EliteMonsterGradeInfo.loadFromWZData();
		AffectedOtherSkillInfo.loadFromWZData();
		InnerAbillity.getInstance().load();
		Setting.settingGoldApple();
		Setting.settingNeoPos();
		BossRewardMeso.Setting();
		Timer.WorldTimer.getInstance().register(new MapleIndexTimer(), 1000);
		Timer.WorldTimer.getInstance().register(new MapleSaveHandler(), 10000L);
		Timer.WorldTimer.getInstance().register(new ResetTask(), 1000L);

		// 启动连接器服务器
		startConnectorServers();

		// 启动控制单元
		new ControlUnit().setVisible(true);

		// 标记服务器为开放状态
		ServerConstants.isOpen = true;
	}

	private void loadServerConfiguration() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM auth_server_channel_ip");
			rs = ps.executeQuery();

			while (rs.next()) {
				ServerProperties.setProperty(rs.getString("name") + rs.getInt("channelid"), rs.getString("value"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("Failed to load server configuration. Exiting...");
			System.exit(0);
		} finally {
			// 确保资源正确关闭
			closeResources(con, ps, rs);
		}
	}

	private void resetAllAccountLoginStatus() {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE accounts SET loggedin = 0, allowed = 0");
			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("[EXCEPTION] Please check if the SQL server is active.", ex);
		} finally {
			// 确保资源正确关闭
			closeResources(con, ps, null);
		}
	}

	private void startTimers() {
		Timer.WorldTimer.getInstance().start();
		Timer.EtcTimer.getInstance().start();
		Timer.MapTimer.getInstance().start();
		Timer.MobTimer.getInstance().start();
		Timer.CloneTimer.getInstance().start();
		Timer.EventTimer.getInstance().start();
		Timer.BuffTimer.getInstance().start();
		Timer.PingTimer.getInstance().start();
		Timer.ShowTimer.getInstance().start();
	}

	private void setupDimensionMirrors() {
		ServerConstants.mirrors.add(new DimentionMirrorEntry("후원 / 홍보", "", 260, 1, 1, "1530054", new ArrayList<>()));
		ServerConstants.mirrors.add(new DimentionMirrorEntry("랭킹", "", 260, 2, 2, "9076004", new ArrayList<>()));
		ServerConstants.mirrors.add(new DimentionMirrorEntry("강화", "", 260, 3, 3, "9062882", new ArrayList<>()));
		ServerConstants.mirrors.add(new DimentionMirrorEntry("성장 시스템", "", 260, 4, 4, "9063040", new ArrayList<>()));
		ServerConstants.mirrors.add(new DimentionMirrorEntry("유니온", "", 260, 6, 6, "9010106", new ArrayList<>()));
		ServerConstants.mirrors.add(new DimentionMirrorEntry("닉변", "", 260, 7, 7, "9062010", new ArrayList<>()));
		ServerConstants.mirrors.add(new DimentionMirrorEntry("직변", "", 260, 8, 8, "9062583", new ArrayList<>()));
		ServerConstants.mirrors.add(new DimentionMirrorEntry("전투", "", 260, 9, 9, "9000197", new ArrayList<>()));

		ServerConstants.WORLD_UI = ServerProperties.getProperty("login.serverUI");
		ServerConstants.ChangeMapUI = Boolean.parseBoolean(ServerProperties.getProperty("login.ChangeMapUI"));
	}

	private void startConnectorServers() {
		// 启动Korea服务器
		KoreaServer.run_startup_configurations();

		// 启动连接器服务器
		try {
			ConnectorServer.run_startup_configurations();
		} catch (Exception ex) {
			System.err.println("Failed to start Connector Server: " + ex.getMessage());
			ex.printStackTrace();
		}

		// 启动CRC服务器
		try {
			CRCServer.run_startup_configurations();
		} catch (Exception ex) {
			System.err.println("Failed to start CRC Server: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void closeResources(Connection con, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	public static void JamsuPoint() { // 潛水點
		Timer.WorldTimer tMan = Timer.WorldTimer.getInstance();
		Runnable r = new Runnable() {
			public void run() {
				for (ChannelServer cserv : ChannelServer.getAllInstances()) {
					for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters().values()) {
						if (mch.getMapId() == 910000000 || mch.getMapId() == 101050000) {
							if (!mch.isFirst) {
								mch.getClient().send(CField.UIPacket.detailShowInfo("開始積累潛水點數.", 3, 20, 20));
								mch.getClient().getSession()
										.writeAndFlush(SLFCGPacket.playSE("Sound/MiniGame.img/14thTerra/reward"));
								mch.isFirst = true;
							}
							if (mch.getClient().getKeyValue("jamsupoint") == null) {
								mch.getClient().setKeyValue("jamsupoint", "0");
							}
							mch.JamsuTime++;
							if (mch.JamsuTime >= 60) { // 900 = sec
								mch.JamsuTime = 0;
								mch.Jamsu5m++;
								long point = mch.getPlayer().getKeyValue(501368, "point");
								point += 2;
								mch.getPlayer().setKeyValue(501368, "point", point + "");
								if (mch.Jamsu5m >= 2) {
									mch.getClient()
											.send(CField.UIPacket.detailShowInfo(
													"積累了潛水積分. 潛水點 : " + mch.getPlayer().getKeyValue(501368, "point"), 3,
													20, 20));
									mch.Jamsu5m = 0;
								}
							}
						} else {
							mch.JamsuTime = 0;
							mch.isFirst = false;
						}
					}
				}
			}
		};
		tMan.register(r, 1000);
	}

	private class AllLoding extends Thread {

		private AllLoding() {
		}

		public void run() {
			LoadingThread SkillLoader = new LoadingThread(() -> SkillFactory.load(), "SkillLoader", this);
			LoadingThread QuestLoader = new LoadingThread(() -> {
				MapleQuest.initQuests();
				MapleLifeFactory.loadQuestCounts();
			}, "QuestLoader", this);
			LoadingThread QuestCustomLoader = new LoadingThread(() -> {
				MapleLifeFactory.loadNpcScripts();
				QuestCompleteStatus.run();
			}, "QuestCustomLoader", this);
			LoadingThread ItemLoader = new LoadingThread(() -> {
				MapleInventoryIdentifier.getInstance();
				CashItemFactory.getInstance().initialize();
				MapleItemInformationProvider.getInstance().runEtc();
				MapleItemInformationProvider.getInstance().runItems();
				AuctionServer.run_startup_configurations();
			}, "ItemLoader", this);
			LoadingThread GuildRankingLoader = new LoadingThread(() -> MapleGuildRanking.getInstance().load(),
					"GuildRankingLoader", this);
			LoadingThread EtcLoader = new LoadingThread(() -> {
				LoginInformationProvider.getInstance();
				RandomRewards.load();
				MapleOxQuizFactory.getInstance();
				UnionHandler.loadUnion();
			}, "EtcLoader", this);
			LoadingThread MonsterLoader = new LoadingThread(() -> {
				MobSkillFactory.getInstance();
				FieldSkillFactory.getInstance();
				MobAttackInfoFactory.getInstance();
			}, "MonsterLoader", this);
			LoadingThread EmoticonLoader = new LoadingThread(() -> ChatEmoticon.LoadEmoticon(), "EmoticonLoader", this);
			LoadingThread MatrixLoader = new LoadingThread(() -> MatrixHandler.loadCore(), "MatrixLoader", this);
			LoadingThread MarriageLoader = new LoadingThread(() -> MarriageManager.getInstance(), "MarriageLoader",
					this);

			LoadingThread[] LoadingThreads = { SkillLoader, QuestLoader, QuestCustomLoader, ItemLoader,
					GuildRankingLoader, EtcLoader, MonsterLoader, MatrixLoader, MarriageLoader, EmoticonLoader };

			// 启动所有加载线程
			for (Thread t : LoadingThreads) {
				t.start();
			}

			// 等待所有线程完成
			synchronized (this) {
				try {
					// 等待所有线程完成加载
					while (Start.CompletedLoadingThreads.get() != LoadingThreads.length) {
						wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}

			// 加载公会数据和设置世界BOSS
			World.Guild.load();
			timeBossHottime();

			GameConstants.isOpen = true;
			System.out.println("[服务端启动完成，在 " + ((System.currentTimeMillis() - Start.startTime) / 1000L) + " 秒內完全初始化]");

			// 启动命令行工具（如果未使用连接器设置）
			if (!ServerConstants.ConnectorSetting) {
				CMDCommand.main();
			}
		}
	}

	private static class LoadingThread extends Thread {

		protected String LoadingThreadName;

		private LoadingThread(Runnable r, String t, Object o) {
			super(new NotifyingRunnable(r, o, t));
			this.LoadingThreadName = t;
		}

		public synchronized void start() {
			System.out.println("[正在加載…] 開始 " + this.LoadingThreadName + " 線程");
			super.start();
		}
	}

	private static class NotifyingRunnable implements Runnable {

		private String LoadingThreadName;
		private long StartTime;
		private Runnable WrappedRunnable;
		private final Object ToNotify;

		private NotifyingRunnable(Runnable r, Object o, String name) {
			this.WrappedRunnable = r;
			this.ToNotify = o;
			this.LoadingThreadName = name;
		}

		public void run() {
			this.StartTime = System.currentTimeMillis();

			try {
				// 执行包装的任务
				this.WrappedRunnable.run();
			} catch (Exception e) {
				System.err.println("Error in " + this.LoadingThreadName + " thread: " + e.getMessage());
				e.printStackTrace();
			}

			// 打印加载完成信息
			System.out.println(
					"[加載已完成] " + this.LoadingThreadName + " | 使用了 " + (System.currentTimeMillis() - this.StartTime)
							+ " 毫秒. (" + (Start.CompletedLoadingThreads.get() + 1) + "/10)");

			// 通知等待的线程
			synchronized (this.ToNotify) {
				Start.CompletedLoadingThreads.incrementAndGet();
				this.ToNotify.notify();
			}
		}
	}

	public static class Shutdown implements Runnable {

		public void run() {
			ShutdownServer.getInstance().run();
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		instance.run();
		// AddCashShop.main(args);
	}

	public static void timeBossHottime() {
		final int[] hour = ServerConstants.hour;
		final int pice = ServerConstants.pice;

		if (boss == null) {
			Calendar cal = Calendar.getInstance();
			long time = cal.getTimeInMillis();
			long schedulewait = 0;

			// 计算下一个整点前10分钟的时间
			if (time > System.currentTimeMillis()) {
				schedulewait = time - System.currentTimeMillis();
			} else {
				// 寻找下一个符合条件的时间点
				while (true) {
					cal.add(Calendar.SECOND, 1);
					for (int ho : hour) {
						if (cal.getTimeInMillis() > System.currentTimeMillis() && cal.getTime().getHours() == (ho - 1)
								&& cal.getTime().getMinutes() >= 50 && cal.getTime().getSeconds() == 0) {
							schedulewait = cal.getTimeInMillis() - System.currentTimeMillis();
							break;
						}
					}
					if (schedulewait > 0) {
						break;
					}
				}
			}

			// 注册世界BOSS定时任务
			boss = Timer.WorldTimer.getInstance().register(new Runnable() {
				public void run() {
					Date nowtime = new Date();

					// 遍历所有设置的小时时间点
					for (int ho : hour) {
						if (ho <= 0) {
							ho = 24;
						}

						// 50-59分钟：准备阶段
						if (nowtime.getMinutes() >= 50 && nowtime.getMinutes() <= 59
								&& nowtime.getHours() == (ho - 1)) {
							for (ChannelServer ch : ChannelServer.getAllInstances()) {
								for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters().values()) {
									chr.sethottimeboss(true);
								}
							}

							// 整点：生成BOSS
						} else if (nowtime.getMinutes() == 0 && nowtime.getHours() == (ho == 24 ? 0 : ho)) {
							for (ChannelServer ch : ChannelServer.getAllInstances()) {
								// 在多个频道生成BOSS
								if (ch.getChannel() >= 1 && ch.getChannel() <= 4) {
									MapleMonster mob = MapleLifeFactory.getMonster(ServerConstants.worldboss);
									ch.getMapFactory().getMap(ServerConstants.worldbossmap)
											.spawnMonsterOnGroundBelow(mob, new Point(-161, -181));
								}

								// 重置玩家状态
								for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters().values()) {
									chr.sethottimeboss(false);
									chr.sethottimebossattackcheck(false);
									chr.sethottimebosslastattack(false);
								}
							}

							// 30分钟：清理阶段
						} else if (nowtime.getMinutes() == 30 && nowtime.getHours() == (ho == 24 ? 0 : ho)) {
							// 获取BOSS地图
							MapleMap map = ChannelServer.getInstance(1).getMapFactory()
									.getMap(ServerConstants.worldbossmap);

							if (map != null) {
								// 遍历地图上的所有玩家
								for (MapleCharacter chr : map.getCharactersThreadsafe()) {
									map.resetFully();

									// 如果玩家在BOSS地图内，重置状态并给予奖励
									if (ServerConstants.worldbossmap == chr.getMapId()) {
										chr.sethottimeboss(false);
										chr.getClient().getSession()
												.writeAndFlush(CField.sendDuey((byte) 28, null, null));
									}
								}
							}

							// 重置地图NPC
							ChannelServer.getInstance(1).getMapFactory().getMap(ServerConstants.worldbossmap)
									.resetNPCs();
						}
					}
				}
			}, 1000 * 60, schedulewait); // 每分钟检查一次，初始延迟为计算得到的时间
		}
	}
}
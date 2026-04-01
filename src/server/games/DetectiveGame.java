package server.games;

import client.MapleCharacter;
import client.inventory.MapleInventoryType;
import handling.channel.ChannelServer;
import server.Randomizer;
import server.Timer;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

public class DetectiveGame {

	// 游戏阶段，初始为 1
	private int Stage = 1;
	// 消息发送剩余次数
	private int MessageTime = 3;
	// 排名列表，记录玩家排名顺序
	private List<MapleCharacter> Rank = new ArrayList<>();
	// 失败玩家列表
	private List<MapleCharacter> Fail = new ArrayList<>();
	// 玩家及其尝试次数的映射
	private Map<MapleCharacter, Integer> Players = new HashMap<>();
	// 玩家及其答案的映射
	private Map<MapleCharacter, Integer> Answers = new HashMap<>();
	// 游戏定时器，用于控制游戏流程
	private ScheduledFuture<?> DetectiveTimer = null;
	// 游戏发起者
	private MapleCharacter Owner = null;
	// 游戏是否正在运行的标志
	public static boolean isRunning = false;

	// 构造函数，初始化游戏并广播招募消息
	public DetectiveGame(MapleCharacter owner, boolean isByAdmin) {
		isRunning = true;
		this.Owner = owner;
		// 遍历所有频道服务器
		for (ChannelServer cs : ChannelServer.getAllInstances()) {
			// 遍历每个频道服务器中的所有玩家
			for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
				if (isByAdmin) {
					// 如果是管理员发起的游戏，发送管理员招募消息
					chr.getClient().getSession()
							.writeAndFlush(SLFCGPacket.OnYellowDlg(3003501, 7000, "#face0#運營者正在招募密碼推理遊戲參與者", ""));
				} else {
					// 否则，发送玩家发起的招募消息
					chr.getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(3003156, 7000,
							"#face0##b#e" + this.Owner.getName() + "#k#n正在招募參與密碼推理遊戲的人!", ""));
				}
			}
		}
	}

	// 发送招募消息并减少消息发送次数
	public void sendMessage() {
		for (ChannelServer cs : ChannelServer.getAllInstances()) {
			for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
				if (this.Owner.isGM()) {
					// 如果发起者是管理员，发送管理员招募消息并显示当前等待人数
					chr.getClient().getSession()
							.writeAndFlush(SLFCGPacket.OnYellowDlg(3003501, 5000, "#face0#運營者正在招募密碼推理遊戲參與者！\r\n現在 #r"
									+ this.Owner.getMap().getCharacters().size() + "名#k在待機室！", ""));
				} else {
					// 否则，发送玩家发起的招募消息并显示当前等待人数
					chr.getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(3003156, 5000,
							"#face0##e#b" + this.Owner.getName() + "#k#n正在招募密碼推理遊戲參與者！\r\n現在 #r"
									+ this.Owner.getMap().getCharacters().size() + "名#k在待機室！",
							""));
				}
			}
		}
		this.MessageTime--;
	}

	// 注册玩家到游戏中
	public void RegisterPlayers(List<MapleCharacter> chars) {
		for (MapleCharacter chr : chars) {
			// 初始尝试次数为 1
			this.Players.put(chr, 1);
		}
	}

	// 增加玩家的尝试次数，并检查是否失败
	public void addAttempt(MapleCharacter chr) {
		int attempts = this.Players.get(chr);
		attempts++;
		this.Players.put(chr, attempts);
		if (attempts == 15) {
			// 如果尝试次数达到 15 次，将玩家标记为失败
			this.Fail.add(chr);
		}
	}

	// 获取玩家的答案
	public int getAnswer(MapleCharacter chr) {
		return this.Answers.get(chr);
	}

	// 获取排名列表
	public List<MapleCharacter> getRanking() {
		return this.Rank;
	}

	// 获取游戏发起者
	public MapleCharacter getOwner() {
		return this.Owner;
	}

	// 获取所有参与游戏的玩家
	public Set<MapleCharacter> getPlayers() {
		return this.Players.keySet();
	}

	// 添加玩家到排名列表，并处理排名满 30 人或所有玩家都有结果的情况
	public void addRank(MapleCharacter a1) {
		if (!this.Rank.contains(a1)) {
			this.Rank.add(a1);
			// 广播新玩家进入排名的消息
			a1.getMap().broadcastMessage(SLFCGPacket.HundredDetectiveGameAddRank(a1.getId(), a1.getName()));
			if (this.Rank.size() == 30 || this.Rank.size() + this.Fail.size() == this.Players.size()) {
				// 取消游戏定时器
				this.DetectiveTimer.cancel(true);
				for (MapleCharacter chr : this.Players.keySet()) {
					// 发送游戏控制消息和重新启用消息
					chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameControl(4, this.Stage));
					chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveReEnable(16));
					// 重置玩家的尝试次数为 1
					this.Players.put(chr, 1);
				}
				if (this.Stage == 3) {
					// 如果已经是第 3 阶段，停止游戏
					StopGame();
				} else {
					// 否则，进入下一阶段
					this.Stage++;
					// 清空答案、排名和失败列表
					this.Answers.clear();
					this.Rank.clear();
					this.Fail.clear();
					for (MapleCharacter chr : this.Players.keySet()) {
						// 为每个玩家生成新的答案
						int Answer = GetRandomNumber();
						this.Answers.put(chr, Answer);
					}
					// 延迟 5 秒后发送游戏准备和控制消息
					Timer.EventTimer.getInstance().schedule(() -> {
						for (MapleCharacter chr : this.Players.keySet()) {
							chr.getClient().getSession()
									.writeAndFlush(SLFCGPacket.HundredDetectiveGameReady(this.Stage));
							chr.getClient().getSession()
									.writeAndFlush(SLFCGPacket.HundredDetectiveGameControl(2, this.Stage));
							chr.getClient().getSession()
									.writeAndFlush(SLFCGPacket.HundredDetectiveGameControl(3, this.Stage));
						}
					}, 5000L);
					for (MapleCharacter chr : this.Rank) {
						if (chr != null) {
							int ranknumber = this.Rank.indexOf(chr) + 1;
							if (ranknumber == 1) {
								// 第一名玩家，检查设置栏位是否有至少 10 个空位
								if (chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() >= 10) {
									// 可在此处添加第一名玩家的奖励逻辑
								}
							} else if (ranknumber >= 2 && ranknumber <= 10) {
								// 第 2 - 10 名玩家，检查设置栏位是否有至少 5 个空位
								if (chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() >= 5) {
									// 可在此处添加第 2 - 10 名玩家的奖励逻辑
								}
							} else if (ranknumber >= 11 && ranknumber <= 20) {
								// 第 11 - 20 名玩家，检查设置栏位是否有至少 4 个空位
								if (chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() >= 4) {
									// 可在此处添加第 11 - 20 名玩家的奖励逻辑
								}
							} else {
								// 其他排名玩家，检查设置栏位是否有至少 3 个空位
								if (chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() >= 3) {
									// 可在此处添加其他排名玩家的奖励逻辑
								}
							}
						}
					}
					for (MapleCharacter chr : this.Players.keySet()) {
						if (chr != null && !this.Rank.contains(chr)
								&& chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() > 0) {
							// 未进入排名且设置栏位有空位的玩家，可在此处添加相应逻辑
						}
					}
				}
			}
		}
	}

	// 生成一个 3 位不重复且非零数字的随机数，且该数字不能与已有的答案重复
	private int GetRandomNumber() {
		List<Integer> temp = new ArrayList<>();
		while (temp.size() != 3) {
			int a = 0;
			while (a == 0) {
				a = Randomizer.nextInt(10);
			}
			if (!temp.contains(a)) {
				temp.add(a);
			}
		}
		int num = temp.get(0) * 100 + temp.get(1) * 10 + temp.get(2);
		while (this.Answers.containsValue(num)) {
			num = GetRandomNumber();
		}
		return num;
	}

	// 开始游戏，发送游戏音乐和说明，延迟 40 秒后发送游戏准备和控制消息
	public void StartGame() {
		for (MapleCharacter chr : this.Players.keySet()) {
			chr.getClient().getSession().writeAndFlush(CField.musicChange("BgmEvent2/adventureIsland"));
			chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameExplain());
			int Answer = GetRandomNumber();
			this.Answers.put(chr, Answer);
		}
		Timer.EventTimer.getInstance().schedule(() -> {
			for (MapleCharacter chr : this.Players.keySet()) {
				chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameReady(this.Stage));
				chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameControl(2, this.Stage));
				chr.getClient().getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameControl(3, this.Stage));
			}
		}, 40000L);
	}

	// 停止游戏，延迟 5 秒后将所有玩家传送至指定地图，并标记游戏停止
	public void StopGame() {
		Timer.EventTimer.getInstance().schedule(() -> {
			for (MapleCharacter chr : this.Players.keySet()) {
				if (chr != null) {
					chr.warp(993022200);
					chr.setDetectiveGame(null);
				}
			}
			isRunning = false;
		}, 5000L);
	}
}
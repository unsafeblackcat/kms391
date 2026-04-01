package handling.channel.handler.boss;

import client.MapleCharacter;
import client.MapleClient;
import java.awt.Point;
import java.util.ArrayList;
import scripting.NPCScriptManager;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import tools.Pair;
import tools.data.LittleEndianAccessor;
import tools.packet.CWvsContext;

public class BossHandler {

	public static void bossUIEnter(LittleEndianAccessor lea, MapleClient c) {

		if (c.getPlayer().getParty() == null) {// 隊伍驗證
			c.getPlayer().dropMessage(1, "隊伍不存在");
			return;
		}

		if (!c.getPlayer().isLeader()) {// 隊長權限檢查
			c.getPlayer().dropMessage(1, "非隊伍隊長無法入場");
			return;
		}

		// 從封包讀取BOSS編號
		int bossNumber = lea.readByte();
		lea.skip(5); // 跳過5位元組保留欄位

		// 讀取難度設定
		int bossDifficulty = lea.readByte();
		lea.skip(5); // 跳過5位元組保留欄位

		// 初始化BOSS戰參數
		boolean isReactorBoss = false; // 是否為反應器觸發型BOSS
		long screenDuration = -1L; // 過場動畫時長（毫秒，-1表示預設值）

		// 地圖與怪物配置
		int mapId = -1; // 目標地圖ID
		int minute = -1; // 挑戰時間限制（分鐘）
		byte deathCount = -1; // 允許死亡次數
		int bossId = -1; // BOSS怪物ID
		Point pos = null; // BOSS生成座標
		ArrayList<Pair<Integer, Point>> bonusMonsterIdAndPositions = new ArrayList<>(); // 附加怪物列表

		/*
		 * 简单：0 普通：1 困難：2 混沌：3 極限：4
		 */
		switch (bossNumber) {
		case 0: { // 蝙蝠魔
			if (bossDifficulty == 0) { // 簡單模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 1061017, "Baluoke"); // 使用NPC ID作為腳本名
			}
			break;
		}
		case 1: { // 扎昆
			if (bossDifficulty == 0) { // 簡單模式
				// c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "當前簡單扎昆維護中"));
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer())); // 解鎖玩家操作
				NPCScriptManager.getInstance().start(c, 2030008, "Zakum0"); // 載入普通難度腳本

			} else if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer())); // 解鎖玩家操作
				NPCScriptManager.getInstance().start(c, 2030008, "Zakum1"); // 載入普通難度腳本

			} else if (bossDifficulty == 3) { // 混沌模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2030008, "Zakum2"); // 載入混沌難度腳本
			}
			break;
		}
		case 2: { // 闇黑龍王
			if (bossDifficulty == 0) { // 簡單模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2083004, "heilong1"); // 載入簡單腳本
			} else if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2083004, "heilong2");
			} else if (bossDifficulty == 2) { // 困難模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2083004, "heilong3");
			} else if (bossDifficulty == 3) { // 混沌
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2083004, "heilong4");
			}
			break;
		}
		case 3: { // 希拉
			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer())); // 解鎖玩家操作
				NPCScriptManager.getInstance().start(c, 2184000, "xila1"); // 載入普通難度腳本
			} else if (bossDifficulty == 2) { // 困難模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2184001, "xila2"); // 載入困難難度腳本
			}
			break;
		}
		case 4: { // 皮耶爾
			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer())); // 解除操作鎖定
				NPCScriptManager.getInstance().start(c, 2184001, "1064012"); // 載入普通腳本
			} else if (bossDifficulty == 3) { // 混沌模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2184001, "1064012a"); // 載入混沌腳本（追加'a'後綴）
			}
			break;
		}
		case 5: { // 班班
			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2184001, "1064013"); // 使用獨立腳本編號
			} else if (bossDifficulty == 3) { // 混沌模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2184001, "1064013a");
			}
			break;
		}
		case 6: { // 血腥皇后
			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 9040009, "1064014a"); // 特殊NPC ID配置
			} else if (bossDifficulty == 3) { // 混沌模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 9040009, "1064014"); // 腳本命名反序
			}
			break;
		}
		case 7: { // 貝倫
			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer())); // 解除操作鎖定
				NPCScriptManager.getInstance().start(c, 9040009, "1064015"); // 載入普通腳本
			} else if (bossDifficulty == 3) { // 混沌模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 9040009, "1064015a"); // 載入混沌腳本
			}
			break;
		}
		case 8: { // 班.雷昂
			if (bossDifficulty == 0) { // 簡單模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2159310, "von_in"); // 載入簡單腳本
				pos = new Point(-3, -181); // 設定BOSS生成座標
			} else if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2159310, "von_in1");
				pos = new Point(-3, -181);
			} else if (bossDifficulty == 2) { // 困難模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2159310, "von_in2");
			}
			break;
		}
		case 9: { // 阿卡伊農
			if (bossDifficulty == 0) { // 簡單模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3003405, "ark_in"); // 載入簡單腳本
			} else if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3003405, "ark_in1");
			}
			break;
		}
		case 10: { // 梅格耐斯
			if (bossDifficulty == 0) { // 簡單模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3000173, "magnus_in"); // 載入簡單腳本
			} else if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3000173, "magnus_in1"); // 普通腳本
			} else if (bossDifficulty == 2) { // 困難模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3000173, "magnus_in2"); // 困難腳本
			}
			break;
		}
		case 11: { // 皮卡啾
			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2141001, "Pingkebing1"); // 普通腳本
			} else if (bossDifficulty == 3) { // 混沌模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2141001, "Pingkebing2"); // 普通腳本
			}
			break;
		}
		case 12: { // 西格諾斯
			if (bossDifficulty == 0) { // 簡單模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 1540556, "in_cygnus1"); // 簡單腳本
			} else if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 1540556, "in_cygnus2"); // 普通腳本
			}
			break;
		}
		case 13: { // 史烏
			screenDuration = 9000L; // 設定過場動畫時長(9秒)

			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 9075000, "blackHeaven_boss");
			} else if (bossDifficulty == 2) { // 困難模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 9075000, "blackHeaven_boss1");
			} else if (bossDifficulty == 4) { // 極限模式
				c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "當前極限史烏維護中"));
			}
			break;
		}

		case 15: { // 戴米安
			screenDuration = 9000L; // 設定過場動畫時長(9秒)

			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 1540809, "fallenWT_boss"); // 載入墮落世界樹副本腳本
			} else if (bossDifficulty == 2) { // 困難模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 1540809, "fallenWT_boss1"); // 困難模式專用腳本
			}
			break;
		}
		case 19: { // 露希妲
			screenDuration = 9000L; // 固定9秒過場動畫

			if (bossDifficulty == 0) { // 簡單模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3003208, "Luxide1"); // 載入墮落世界樹副本腳本
			} else if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3003208, "Luxide2"); // 載入墮落世界樹副本腳本
			} else if (bossDifficulty == 2) { // 困難模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3003208, "Luxide3"); // 載入墮落世界樹副本腳本
			}
			break;
		}
		case 21: { // 卡熊
			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2052044, "kaxiong1"); // 使用NPC ID作為腳本名
			} else if (bossDifficulty == 2) { // 困难模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2052044, "kaxiong2");
			}
			break;
		}
		case 22: { // 帕普拉圖斯（座標可能異常）
			isReactorBoss = true; // 標記為反應器觸發型BOSS

			if (bossDifficulty == 0) { // 簡單模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2041024, "Populatus00");
				pos = new Point(1, 179); // 強制設定生成座標
			} else if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2041024, "Populatus01");
			} else if (bossDifficulty == 3) { // 混沌模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2041024, "Populatus02");
			}
			break;
		}
		case 23: { // 威爾
			screenDuration = 6000L; // 設定6秒過場動畫

			if (bossDifficulty == 0) { // 簡單模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3003560, "Weier1");
			} else if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3003560, "Weier2");
			} else if (bossDifficulty == 2) { // 困難模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3003560, "Weier3");
			}
			break;
		}
		case 24: { // 真希拉
			screenDuration = 15000L; // 15秒專屬過場動畫

			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3005305, "jinHillah_enter"); // 載入普通腳本
			} else if (bossDifficulty == 2) { // 困難模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3005305, "jinHillah_enter1"); // 載入困難腳本
			}
			break;
		}
		case 25: { // 黑色魔法師
			screenDuration = 6000L; // 6秒劇情動畫

			if (bossDifficulty == 2) { // 困難模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2550000, "bossBlackMage_pt"); // 載入PT副本腳本
			} else if (bossDifficulty == 4) { // 極限模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2550000, "bossBlackMage_pt1"); // 極限專用腳本
			}
			break;
		}
		case 26: { // 至暗魔晶
			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2159442, "dusk_enter"); // 載入黃昏聖殿入口腳本
			} else if (bossDifficulty == 3) { // 混沌模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2159442, "dusk_enter2"); // 混沌專用腳本
			}
			break;
		}
		case 27: { // 頓凱爾
			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2570110, "dunkel_enter"); // 暗影迴廊入口腳本
			} else if (bossDifficulty == 2) { // 困難模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2570110, "dunkel_enter2"); // 追加傷害檢測機制
			}
			break;
		}
		case 28: { // 被選中的塞伦
			screenDuration = 12000L; // 12秒專屬劇情動畫

			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3004542, "Sailun1"); // 追加傷害檢測機制
			} else if (bossDifficulty == 2) { // 困難模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3004542, "Sailun2"); // 追加傷害檢測機制
			} else if (bossDifficulty == 4) { // 極限模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3004542, "Sailun3"); // 追加傷害檢測機制
			}
			break;
		}
		case 29: { // 守護天使史萊姆
			screenDuration = 6000L; // 6秒過場動畫

			if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 1013500, "Tslsl"); // 模式
			} else if (bossDifficulty == 3) { // 混沌模式
				// 待實裝邏輯
			}
			break;
		}
		case 30: { // 監視者卡洛斯（未獲取怪物代碼）
			screenDuration = 6500L; // 6.5秒專屬動畫

			if (bossDifficulty == 0) { // 簡單模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3005267, "Kaluosi1"); // 模式
			} else if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3005267, "Kaluosi2"); // 模式
			} else if (bossDifficulty == 3) { // 混沌模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3005267, "Kaluosi3"); // 模式
			} else if (bossDifficulty == 4) { // 極限模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3005267, "Kaluosi4"); // 模式
			}
			break;
		}
		case 31: { // 卡琳（未獲取生成座標）
			screenDuration = 7500L; // 7.5秒劇情演出

			if (bossDifficulty == 0) { // 簡單模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3006193, "kalin1"); // 模式
			} else if (bossDifficulty == 1) { // 普通模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3006193, "kalin2"); // 模式
			} else if (bossDifficulty == 3) { // 混沌模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3006193, "kalin3"); // 模式
			} else if (bossDifficulty == 4) { // 極限模式
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 3006193, "kalin4"); // 模式
			}
			break;
		}
		}

		// 檢查地圖是否已存在玩家
		if (c.getChannelServer().getMapFactory().getMap(mapId) != null) {
			if (c.getChannelServer().getMapFactory().getMap(mapId).getCharactersSize() > 0) {
				c.getPlayer().dropMessage(1, "當前已有其他隊伍在挑戰該BOSS");
				return; // 中止傳送流程
			}
		}

		// 驗證所有必要參數後執行隊伍傳送
		if (mapId != -1 && minute != -1 && deathCount != -1 && bossId != -1 && pos != null
				&& bonusMonsterIdAndPositions != null) {

			// 遍歷隊伍成員進行傳送
			for (MapleCharacter member : c.getPlayer().getPartyMembers()) {
				if (member != null) {
					member.bossTimeMoveMap(bossNumber, // BOSS編號
							isReactorBoss, // 是否為反應器觸發型
							screenDuration, // 過場動畫時長(毫秒)
							mapId, // 目標地圖ID
							minute, // 挑戰時間限制(分鐘)
							deathCount, // 允許死亡次數
							bossId, // BOSS怪物ID
							pos, // 生成座標
							bonusMonsterIdAndPositions // 附加怪物配置
					);
				}
			}
		}
	}

	public static void bossInit(MapleClient c) {
		switch (c.getPlayer().getMap().getId()) { // 個別處理
		case 410006020: { // 卡洛斯第一階段（卡羅特：卡羅特摩天樓）
			MapleMonster monster = MapleLifeFactory.getMonster(8881010); // 卡洛斯第一階段主怪物
			MapleMonster subMonster = MapleLifeFactory.getMonster(8881011); // 副怪物

			// 清除現有怪物
			c.getPlayer().getMap().killMonster(monster.getId());
			c.getPlayer().getMap().killMonster(subMonster.getId());

			// 在指定座標重新生成怪物
			c.getPlayer().getMap().spawnMonsterOnGroundBelow(monster, new Point(462, 398));
			c.getPlayer().getMap().spawnMonsterOnGroundBelow(subMonster, new Point(462, 398));

			// 初始化卡洛斯戰鬥模式
			c.getPlayer().getParty().kalosPattern = new KalosPattern(monster);
			c.getPlayer().getParty().kalosPattern.kalosEnter();
		}
			break;
		}
	}

	public static void bossUIClose(LittleEndianAccessor lea, MapleClient c) {
		c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));

	}
}

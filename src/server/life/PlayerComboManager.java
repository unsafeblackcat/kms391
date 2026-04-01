package server.life;

import java.util.HashMap;
import java.util.Map;

// 玩家连击管理类
public class PlayerComboManager {
	// 使用一个 Map 来存储每个玩家的连击计数，键为玩家 ID，值为连击计数
	private static final Map<Integer, Integer> comboCounts = new HashMap<>();

	// 增加指定玩家的连击计数
	public static void increaseComboCount(int playerId) {
		// 如果该玩家已经有连击计数，将其连击计数加 1
		if (comboCounts.containsKey(playerId)) {
			int currentCount = comboCounts.get(playerId);
			comboCounts.put(playerId, currentCount + 1);
		} else {
			// 如果该玩家还没有连击计数，将其连击计数初始化为 1
			comboCounts.put(playerId, 1);
		}
	}

	// 获取指定玩家的连击计数
	public static int getComboCount(int playerId) {
		// 如果该玩家有连击计数，返回其连击计数；否则返回 0
		return comboCounts.getOrDefault(playerId, 0);
	}

	// 重置指定玩家的连击计数
	public static void resetComboCount(int playerId) {
		// 将指定玩家的连击计数重置为 0
		comboCounts.put(playerId, 0);
	}

	// 重置所有玩家的连击计数
	public static void resetAllComboCounts() {
		// 清空存储连击计数的 Map
		comboCounts.clear();
	}
}
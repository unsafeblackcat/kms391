package maplecontents.edenCustom;

import client.MapleCharacter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author MerryBytes
 */
public class EdenContentsByItem {

	// 훈장이름, Pair <아이템 코드, 추가경험치>
	public static Map<Integer, Integer> ExpBuffMedal = new HashMap<>();
	public static Map<Integer, Integer> ExpBuffPendent = new HashMap<>();

	static {
		ExpBuffMedal.put(1143825, 99);
		ExpBuffMedal.put(1143826, 30);
		ExpBuffMedal.put(1143827, 30);
		ExpBuffMedal.put(1143828, 30);
		ExpBuffMedal.put(1143829, 30);
		ExpBuffMedal.put(1143830, 5);
		ExpBuffMedal.put(1143821, 35);
		ExpBuffMedal.put(1143822, 40);
		ExpBuffMedal.put(1143823, 45);
		ExpBuffPendent.put(1122334, 30);
	}

	public static int getEquipItemByExpIncreasePercent(MapleCharacter character, int itemid) {
		int incPercent = ExpBuffMedal.get(itemid);
		return incPercent;
	}

	public static int getEquipItemByExpIncreasePercent_Pendent(MapleCharacter character, int itemid) {
		int incPercent = ExpBuffPendent.get(itemid);
		return incPercent;
	}

}

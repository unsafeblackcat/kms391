package maplecontents.Custom;

import client.MapleCharacter;
import client.SkillFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Merrybytes (Ryu)
 */
public class MapleExtremeBoss {

	public static Map<Integer, Integer> damReduceByMapID = new HashMap<>();
	public static Integer damageReduceBuff = 80002636;

	static {
		/* Extreme Seren Setting */
		damReduceByMapID.put(410002100, 30); // Extreme Seren Enter Map
		damReduceByMapID.put(410002120, 30); // Extreme Seren 1 Phase
		damReduceByMapID.put(410002140, 30); // Extreme Seren 1 Phase -> 2 Phase Enter Map
		damReduceByMapID.put(410002160, 30); // Extreme Seren 2 Phase
		damReduceByMapID.put(410002180, 30); // Extreme Seren 3 Phase
	}

	/**
	 * @param mapId - Characters MapId
	 * @return DamageReducePercent By Extreme Boss Map
	 */
	public static Integer getDamReduceByMap(int mapId) {
		int reduceValue = damReduceByMapID.get(mapId);
		return reduceValue;
	}

	/**
	 * @param character - On MapleCharacter in getPlayer Instance
	 *
	 */
	public static void setExtremeBossDamageReduce(MapleCharacter character) {
		Integer characterMapID = character.getMapId();
		boolean isExtremeMode = (character.isExtremeMode()) ? true : false;
		boolean mapCheck = damReduceByMapID.containsKey(characterMapID) ? true : false;
		boolean alreadyCharacterHasExtremeBuff = (character.getBuffedValue(damageReduceBuff)) ? true : false;
		if (isExtremeMode) {
			if (mapCheck) {
				if (!alreadyCharacterHasExtremeBuff) {
					SkillFactory.getSkill(damageReduceBuff).getEffect(1).applyTo(character);
					character.dropMessage(5,
							"익스트림 난이도 보스에 입장하여 입히는 데미지가 " + getDamReduceByMap(characterMapID) + "% 감소합니다.");
				}
			} else {
				character.setExtremeMode(false);
				if (alreadyCharacterHasExtremeBuff)
					character.cancelEffect(character.getBuffedEffect(damageReduceBuff));
				character.dropMessage(5, "익스트림 보스를 퇴장하여 데미지 감소 버프가 해제됩니다.");
			}
		}
	}
}
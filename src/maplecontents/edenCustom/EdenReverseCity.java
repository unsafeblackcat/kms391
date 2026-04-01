package maplecontents.edenCustom;

import client.MapleCharacter;

/**
 * @Author Ryu (Merrybytes)
 */
public class EdenReverseCity {

	public static void main(String[] args) {
	}

	public static boolean alreadyCharacterHasQuestType(MapleCharacter character, int type, String key) {
		boolean alreadyHasValue = character.getKeyValue(type, key) == 1;
		return alreadyHasValue;
	}
}

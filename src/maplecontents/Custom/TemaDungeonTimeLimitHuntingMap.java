package maplecontents.Custom;

import client.MapleCharacter;
import maplecontents.nexon.PinkbeanHapplyNewWeek;
import tools.Triple;

import java.util.*;

public class TemaDungeonTimeLimitHuntingMap {
	// GoldReachStorage

	public static class GoldReachStorage {
		private static int eventMapId = 910160000;
		private static int eventMobId = 9302039;
		private static long gainExpAmountByMonster = 1500000;

		public static int getEventMapId() {
			return eventMapId;
		}

		public static int getEventMobId() {
			return eventMobId;
		}

		public static long getGainExpAmountByMonster() {
			return gainExpAmountByMonster;
		}
	}
}

package maplecontents.nexon;

public enum MapleWongStraunt {
	INC_WATK(0), INC_BOSS_DMG(1), INC_IGNORE_DEF(2), INC_ALLSTAT(3), INC_HP_MP(4), INC_BUFF_DURATION(5),
	INC_CRITICAL(6), INC_DAMAGE_TO_NORMAL_MONSTER(7), INC_ARCAIN_FORCE(8), INC_GAIN_EXP(9);

	int i = 0;

	private MapleWongStraunt(int i) {
		this.i = i;
	}

	private int getValue() {
		return this.i;
	}

}

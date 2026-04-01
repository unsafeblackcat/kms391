package client.inventory;

public enum EquipSpecialStat {
	SLOTS(0x1), LEVEL(0x2), FLAG(0x4), INC_SKILL(0x8), ITEM_LEVEL(0x10), ITEM_EXP(0x20), DURABILITY(0x40),
	VICIOUS_HAMMER(0x80), PVP_DAMAGE(0x100), DOWNLEVEL(0x100), ENHANCT_BUFF(0x200), DURABILITY_SPECIAL(0x400),
	REQUIRED_LEVEL(0x800), YGGDRASIL_WISDOM(0x1000), FINAL_STRIKE(0x2000), IndieBdr(0x4000), IGNORE_PDR(0x8000),
	TOTAL_DAMAGE(0x10000), ALL_STAT(0x20000), KARMA_COUNT(0x40000), REBIRTH_FIRE(0x80000), EQUIPMENT_TYPE(0x100000);

	private final int value;

	EquipSpecialStat(int value) {
		this.value = value;
	}

	public final int getValue() {
		return this.value;
	}
}

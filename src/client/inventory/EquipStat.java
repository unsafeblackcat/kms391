package client.inventory;

public enum EquipStat {
	STR(0x1), DEX(0x2), INT(0x4), LUK(0x8), MHP(0x10), MMP(0x20), WATK(0x40), MATK(0x80), WDEF(0x100), MDEF(0x200),
	ACC(0x200), AVOID(0x200), HANDS(0x200), SPEED(0x400), JUMP(0x800);

	private final int value;

	EquipStat(int value) {
		this.value = value;
	}

	public final int getValue() {
		return this.value;
	}

	public final boolean check(int flag) {
		return ((flag & this.value) != 0);
	}

	public enum EnchantBuff {
		UPGRADE_TIER(1), NO_DESTROY(2), SCROLL_SUCCESS(4);

		private final int value;

		EnchantBuff(int value) {
			this.value = value;
		}

		public final byte getValue() {
			return (byte) this.value;
		}

		public final boolean check(int flag) {
			return ((flag & this.value) != 0);
		}
	}
}

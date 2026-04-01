package client.inventory;

public enum ItemSortType {
	EQUIP(1), CONSUME(2), ETC(3), INSTALL(4), CASH(5), CODY(6), NONE_VALUE(0), EQUIP_LOCK(0x800), CONSUME_LOCK(0x1000),
	ETC_LOCK(0x1000), INSTALL_LOCK(0x1000), CASH_LOCK(0x100), CODY_LOCK(0x800);

	int value;

	ItemSortType(int type) {
		this.value = type;
	}

	public int getValue() {
		return this.value;
	}
}

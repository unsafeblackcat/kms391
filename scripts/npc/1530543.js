var enter = "\r\n";
var seld = -1;
var seld2 = -1;

var ic;
function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		var msg ="#fs15#안녕하세요 #b#h 0##k님! 잠재능력이 없는 캐시 아이템에 잠재능력 부여가 가능하다는 사실을 알고 계시나요? #fs15##b"+enter;
		msg += "#L1#캐시 아이템 잠재능력 부여"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		count = 0;
		var msg ="#fs15#캐시 아이템에 잠재 옵션을 개방할수있습니다.#k"+enter;
		for (i = 0; i < cm.getInventory(6).getSlotLimit(); i++) {
			if (cm.getInventory(6).getItem(i) != null && cm.isCash(cm.getInventory(6).getItem(i).getItemId())) {
				msg += "#L" + i + "# #i" + cm.getInventory(6).getItem(i).getItemId() + "# #b#z" + cm.getInventory(6).getItem(i).getItemId() + "##k#l\r\n";
				count++;
			}
		}
		if (count > 0)
			cm.sendSimple(msg);
		else {
			cm.sendOk("부여할 아이템이 없습니다.");
			cm.dispose();
		}
	} else if (status == 2) {
		item = cm.getInventory(6).getItem(sel);

		if (item == null) {
			cm.dispose();
			return;
		}
		item.setState(20); // 20 레전드리 
		item.setLines(6);
		item.setPotential1(40557); // 잠재 1코드
		item.setPotential2(40557); // 잠재 2코드
		item.setPotential3(40557); // 잠재 3코드 
		item.setPotential4(40557);
		item.setPotential5(40557);
		item.setPotential6(40557);
		cm.sendOk("#fs15#아이템에 잠재능력을 부여했습니다. 이용해 주셔서 감사합니다.");
		cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(6));
		cm.dispose();
	}
}
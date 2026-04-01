
var status = -1;

var price = 2436893;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, sel) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
		var txt = "#fs15#장비 등급 재설정 횟수를 초기화를 하기 위해선\r\n\r\n"
		txt += "#b#i" + price + "##z" + price + "# 1개#k가 필요합니다.\r\n\r\n";
		txt += "1. 최하급 ~ 최상급 중 하나의 등급이 #b존재#k해야 합니다.\r\n"
		txt += "2. #b주문서 작#k이 되어 있지 않아야 합니다.\r\n\r\n";
		txt += "#fs15##r재설정 횟수를 초기화 하실 장비를 선택해 주세요.#k\r\n\r\n";

		for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
			var item = cm.getInventory(1).getItem(i);

			if (item == null) {
				continue;
			}

			var check = item.getOwner() == "미확인" || item.getOwner() == "최하급" || item.getOwner() == "하급" || item.getOwner() == "중급" || item.getOwner() == "상급" || item.getOwner() == "최상급" || item.getOwner() == "유일";
			if (check && item.getLevel() <= 0) {
				txt += "#L" + i + "#" + "#i" + item.getItemId() + "##b" + "#t" + item.getItemId() + "##l";
				txt += "     #k남은 횟수 : " + (10 - item.getCoption3()) + "회\r\n";
			}
		}

		cm.sendSimple(txt);
    } else if (status == 1) {
		var item = cm.getInventory(1).getItem(sel);

		var check = item.getOwner() == "미확인" || item.getOwner() == "최하급" || item.getOwner() == "하급" || item.getOwner() == "중급" || item.getOwner() == "상급" || item.getOwner() == "최상급" || item.getOwner() == "유일";

		if (!check) {
			cm.sendOk("알 수 없는 오류가 발생 했습니다. (Error. 1)");
			cm.dispose();
			return;
		}

		if (item.getLevel() > 0) {
			cm.sendOk("알 수 없는 오류가 발생 했습니다. (Error. 2)");
			cm.dispose();
			return;
		}

		if (!cm.haveItem(price, 1)) {
			cm.sendOk("#fs15##fc0xFF000000#초기화를 하기 위해선 #b#z" + price + "##fc0xFF000000# 1개가 필요합니다.");
			cm.dispose();
			return;
		}
		if (item.getCoption3() == 0) {
			cm.sendOk("#fs15#선택하신 아이템의 재설정 횟수가 #r10회#k 남으신거 같은데요?");
			cm.dispose();
			return;
		}

		if (item == null) {
			return;
		}

	cm.gainItem(price, -1);
	item.setCoption3(0);
	cm.sendOk("재설정 횟수 초기화가 완료 되었습니다.");
	cm.dispose();
    }
}

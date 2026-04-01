importPackage(Packages.constants);

var maxCount = 10;

var price = 4033370;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, sel) {
	if (mode == 1) {
		status++;
		if (status == 2 && sel == 0) {
        		status = 0;
		}
      	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		var txt = "#fs15#등급 재설정이 가능한 아이템의 조건은 아래와 같습니다.\r\n\r\n";
		txt += "1. 최하급 ~ 최상급 중 하나의 등급이 #b존재#k해야 합니다.\r\n"
		txt += "2. #b주문서 작#k이 되어 있지 않아야 합니다.\r\n";
		txt += "3. #b[미확인] #k아이템의 경우 재설정을 해야 옵션이 부여됩니다.\r\n\r\n";
		txt += "#fs15#원하시는 아이템의 등급을 #r재설정#k 하실 수 있습니다. \r\n\r\n#r재설정 하실 장비를 선택해 주세요.#k\r\n\r\n";

		for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
			var item = cm.getInventory(1).getItem(i);

			if (item == null) {
				continue;
			}

			var check = item.getOwner() == "미확인" || item.getOwner() == "최하급" || item.getOwner() == "하급" || item.getOwner() == "중급" || item.getOwner() == "상급" || item.getOwner() == "최상급" || item.getOwner() == "유일";
			if (check && item.getLevel() <= 0) {
				txt += "#L" + i + "#" + "#i" + item.getItemId() + "##b" + "#t" + item.getItemId() + "##l";
				txt += "     #k남은 횟수 : " + (maxCount - item.getCoption3()) + "회\r\n";
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
			cm.sendOk("#fs15##fc0xFF000000#강화하기 위해선 #b#z" + price + "##fc0xFF000000#이 필요합니다.");
			cm.dispose();
			return;
		}

		if (item == null) {
			return;
		}

		if (item.getCoption3() >= maxCount) {
			cm.sendOk("이 아이템은 재설정 횟수를 모두 소모하였습니다.");
			cm.dispose();
			return;
		}

		cm.gainItem(price, -1);

		item.setCoption3(item.getCoption3() + 1);

		item.setRandomStat();

		cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(1));

		var chat = "";
		chat += "#fs15#스탯부여에 성공했습니다. \r\n\r\n";
		chat += "#r남은 재설정 가능 횟수 : " + (maxCount - item.getCoption3()) + "#k\r\n\r\n";
		chat += "#b등급 : " + item.getOwner() + "\r\n";
		chat += "#bSTR : " + item.getStr() + "\r\n";
		chat += "#bDEX : " + item.getDex() + "\r\n";
		chat += "#bINT : " + item.getInt() + "\r\n";
		chat += "#bLUK  : " + item.getLuk() + "\r\n";
		chat += "#b공격력 : " + item.getWatk() + "\r\n";
		chat += "#b마력 : " + item.getMatk()+ "\r\n";
		chat += "#L0# 한번 더 재설정";

		cm.sendSimple(chat);
	} else if (status == 2) {
		if (sel == 0) {

		} else {
			cm.dispose();
		}
	}
}

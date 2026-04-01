var enter = "\r\n";
var seld = -1, seld2 = -1;
var max = -1;
var item = 0;
var itemArray = [ 2633915, 2633924, 2630782, 2633914, 2633923, 2635400]; //아케인무기상자/방어구상자
var 코인 = 4310325;
var 코인개수 = 5;
var noItem = true;

enter = "\r\n";

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
		var msg = "#fs15##b#h0##k님! 안녕하세요!\r\n아케인상자를 특별한 재화로 바꿔드리고 있습니다.\r\n\r\n현재 가지고 있는 아이템 물품입니다." + enter + enter;
		for (i = 0; i < itemArray.length; i++) {
			if(cm.haveItem(itemArray[i])) {
			msg += "#L" + i + "##i" + itemArray[i] + "#  #z" + itemArray[i] + "#" + enter;
			noItem = !noItem;
			}
		}
		if(noItem) {
			msg += "#r판매할 수 있는 아이템이 없습니다.";
		}
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		max = itemArray[seld] < 2000000 ? 1 : 999;
		var msg = "#fs15#선택하신 아이템은 #i"+itemArray[seld]+"##b#z"+itemArray[seld]+"##k입니다."+enter;
		msg += "최대 #b"+max+"#k개 판매하실 수 있습니다."+enter+enter;
		msg += "몇 개 판매하시겠습니까?"+enter+enter;
		msg += "#r※판매하실 아이템을 반드시 다시 확인해주세요. 어떤 경우에서도 복구해드리지 않습니다."+enter;
		cm.sendGetNumber(msg, 1, 1, max);
	} else if (status == 2) {
		seld2 = sel;
		if (seld2 > max) {
			cm.dispose();
			return;
		}

		var msg = "#fs15#선택하신 아이템은 #i"+itemArray[seld]+"##b#z"+itemArray[seld]+"##k입니다."+enter;
		msg += "판매하려고 하는 개수가 #b"+seld2+"#k개 맞다면 '예'를 눌러주세요."+enter+enter;
		msg += "총 #i" + 코인 + "##z" + 코인 + "#  #b"+(코인개수 * seld2)+"#k 개가 지급됩니다."+enter+enter;
		msg += "#r※판매하실 아이템을 반드시 다시 확인해주세요. 어떤 경우에서도 복구해드리지 않습니다."+enter;
		cm.sendYesNo(msg);
	} else if (status == 3) {
		if (seld2 > max) {
			cm.dispose();
			return;
		}

		if (!cm.haveItem(itemArray[seld], seld2)) {
			cm.sendOk("가지고 있는 아이템보다 많은 수를 입력했습니다.");
			cm.dispose();
			return;
		}

		if(max == 1) {
			cm.getPlayer().removeItem(itemArray[seld], -1);
		} else {
			cm.gainItem(itemArray[seld], -seld2);
		}
		cm.gainItem(코인,코인개수 * seld2);
		cm.sendOk("교환이 완료되었습니다.");
		cm.dispose();
	}
}
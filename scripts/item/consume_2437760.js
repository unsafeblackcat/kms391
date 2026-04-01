box = 2437760;
var seld = -1;

function start() {
    St = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        St++;
    }
	if (St == 0) {
	  var text = "#fs15#請選擇想要交換的 #b神秘徽章#k . 可批量兌換.\r\n";
	  text += "#L1##b#i1712001##z1712001#\r\n";
	  text += "#L2##b#i1712002##z1712002#\r\n";
	  text += "#L3##b#i1712003##z1712003#\r\n";
	  text += "#L4##b#i1712004##z1712004#\r\n";
	  text += "#L5##b#i1712005##z1712005#\r\n";
	  text += "#L6##b#i1712006##z1712006##k#n#l\r\n\r\n";
	  cm.sendYesNo(text);
	} else if (St == 1) {
		seld = selection;
		cm.sendGetNumber("#fs15#請選擇您要使用的物品數量.", 1, 1, 100);
	} else if (St == 2) {
		if(seld <= 6) {
			if (!cm.canHold(1712000+seld, seld)) {
				cm.sendOk("#fs15#抱歉，好像沒有足够的庫存空間。#b請清空裝備欄#k的庫存空間.");
				cm.dispose();
				return;
			}
			if (!cm.haveItem(2437760, selection)) {
				cm.sendOk("#fs15#使用的物品不足.");
				cm.dispose();
				return;
			}
			cm.gainItem(1712000+seld,selection);
			cm.gainItem(2437760,-selection);
			cm.sendOk("#fs15#交換已完成.");
			cm.dispose();
		}
	}
}
box = 2633336;
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
	  var text = "#fs15#請選擇想要交換的 #b原初符文#k,同一物品可兌換5個.\r\n#b[請檢測裝備欄後使用]\r\n#r不補倉.\r\n";
	  text += "#L0##b#i1713000##z1713000# 5個\r\n";
	  text += "#L1##b#i1713001##z1713001# 5個\r\n";
	  text += "#L2##b#i1713002##z1713002# 5個\r\n";
	  text += "#L3##b#i1713003##z1713003# 5個\r\n";
	  text += "#L4##b#i1713004##z1713004# 5個\r\n";
	  text += "#L5##b#i1713005##z1713005# 5個\r\n";
	  cm.sendYesNo(text);
	} else if (St == 1) {
		seld = selection;
		cm.sendGetNumber("#fs15#請選擇您要使用的物品數量.", 1, 1, 100);
	} else if (St == 2) {
		if (!cm.canHold(1713000+seld, 5*selection)) {
			cm.sendOk("#fs15#抱歉，好像沒有足够的庫存空間。 請清空#b裝備#k#b消耗#k的庫存空間 .");
			cm.dispose();
			return;
		}
		if (!cm.haveItem(box, selection)) {
			cm.sendOk("#fs15#使用的物品不足.");
			cm.dispose();
			return;
		}
		cm.gainItem(1713000+seld,5*selection);
		cm.gainItem(box,-1*selection);
		cm.sendOk("#fs15#交換已完成.");
		cm.dispose();
	}
}
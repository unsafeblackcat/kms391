importPackage(Packages.client.inventory);
var need = 2636375;
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
	  var text = "#fs15#請選擇想要交換的 #r原初徽章#k 同一物品可獲得1個. 可批量兌換.\r\n";
	  text += "#L0##b#i1713000#";
	  text += "#L1##b#i1713001#";
	  text += "#L2##b#i1713002#\r\n";
	  text += "#L3##b#i1713003#";
	  text += "#L4##b#i1713004#";
	  text += "#L5##b#i1713005#\r\n";
	  cm.sendYesNo(text);
	} else if (St == 1) {
		seld = selection;
		cm.sendGetNumber("#fs15#請選擇要使用的項目數。 \r\n#r剩餘的裝備欄欄 : " + cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot(), 1, 1, 100);
	} else if (St == 2) {
        	if (cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < selection) {
            	cm.sendOk("#fs15#好像沒有足够的庫存空間。 \r\n#b請清空裝備欄#k的庫存空間.");
            	cm.dispose();
            	return;
        	}
		if (!cm.haveItem(need, selection)) {
			cm.sendOk("#fs15#兌換券不足.");
			cm.dispose();
			return;
		}
		cm.gainItem(1713000+seld,1*selection);
		cm.gainItem(need,-1*selection);
		cm.sendOk("#fs15#交換已完成.");
		cm.dispose();
	}
}
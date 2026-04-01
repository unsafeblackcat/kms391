importPackage(Packages.client.inventory);
var need = 2636135;
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
	  var text = "#fs15#請選擇想交換的  #r神秘符文#k 將發放1個相同物品.\r\n";
	  text += "#L0##b#i1712001#";
	  text += "#L1##b#i1712002#";
	  text += "#L2##b#i1712003#\r\n";
	  text += "#L3##b#i1712004#";
	  text += "#L4##b#i1712005#";
	  text += "#L5##b#i1712006#\r\n";

	  cm.sendYesNo(text);
	} else if (St == 1) {
		seld = selection;
		cm.sendGetNumber("#fs15#請選擇您要使用的物品數量. \r\n#r剩餘裝備格 : " + cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot(), 1, 1, 100);
	} else if (St == 2) {
        	if (cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < selection) {
            	cm.sendOk("#fs15#好像沒有足够的庫存空間。\r\n#b請清空裝備#k欄上的庫存空間。.");
            	cm.dispose();
            	return;
        	}
		if (!cm.haveItem(need, selection)) {
			cm.sendOk("#fs15#兌換券不足.");
			cm.dispose();
			return;
		}
		cm.gainItem(1712001+seld,1*selection);
		cm.gainItem(need,-1*selection);
		cm.sendOk("#fs15#交換已完成.");
		cm.dispose();
	}
}
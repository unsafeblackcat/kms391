importPackage(Packages.client.inventory);
var need = 2636136;
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
	  var text = "#fs20#請選擇想要獲得的滿級 #r神秘徽章#k .\r\n";
	  text += "#L0##b#i1712001#";
	  text += "#L1##b#i1712002#";
	  text += "#L2##b#i1712003#";
	  text += "#L3##b#i1712004#";
	  text += "#L4##b#i1712005#";
	  text += "#L5##b#i1712006#\r\n";
	  cm.sendYesNoS(text,0x86);
	} else if (St == 1) {
		seld = selection;
		cm.sendYesNo("#fs15#是您選擇的單品嗎?\r\n\r\n" + "#b#i" + (1712001 + seld) + "##z"+ (1712001 + seld) +"#");
	} else if (St == 2) {
        	if (cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < selection) {
            	cm.sendOk("好像沒有足够的庫存空間。 \r\n#b請清空裝備欄#k的庫存空間.");
            	cm.dispose();
            	return;
        	}
		if (!cm.haveItem(need, selection)) {
			cm.sendOk("兌換券不足.");
			cm.dispose();
			return;
		}
		item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1712001+seld);
		item.setArcLevel(20);
		item.setStr(2200);
		item.setDex(2200);
		item.setInt(2200);
		item.setLuk(2200);
        item.setArc(220);
		Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
		cm.gainItem(need,-1);
		cm.sendOk("#b獲得滿級 #i"+ (1712001+seld) +"##t" + (1712001+seld) + "##b 1個#fc0xFF000000#.");
		cm.dispose();
	}
}
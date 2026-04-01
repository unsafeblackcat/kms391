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
	  var text = "#fs11#교환 하고싶은 만렙 #r아케인 심볼#k 을 선택해주세요.\r\n";
	  text += "#L0##b#i1712001##z1712001#\r\n";
	  text += "#L1##b#i1712002##z1712002#\r\n";
	  text += "#L2##b#i1712003##z1712003#\r\n";
	  text += "#L3##b#i1712004##z1712004#\r\n";
	  text += "#L4##b#i1712005##z1712005#\r\n";
	  text += "#L5##b#i1712006##z1712006#\r\n";
	  cm.sendYesNo(text);
	} else if (St == 1) {
		seld = selection;
		cm.sendYesNo("#fs11#선택하신 아이템이 맞으신가요?\r\n\r\n" + "#b#i" + (1712001 + seld) + "##z"+ (1712001 + seld) +"#");
	} else if (St == 2) {
        	if (cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < selection) {
            	cm.sendOk("인벤토리 공간이 충분하지 않으신 것 같네요. \r\n#b장비#k탭의 인벤토리 공간을 비워주세요.");
            	cm.dispose();
            	return;
        	}
		if (!cm.haveItem(need, selection)) {
			cm.sendOk("교환권이 부족합니다.");
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
		cm.sendOk("#b만렙 #i"+ (1712001+seld) +"##t" + (1712001+seld) + "##b 1개#fc0xFF000000#를 획득 하였습니다.");
		cm.dispose();
	}
}
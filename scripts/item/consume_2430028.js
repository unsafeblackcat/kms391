
var status = -1;
importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
function start() {
    status = -1;
    action(1, 0, 0);
}
검정 = "#fc0xFF191919#"
초록 = "#fc0xFF47C83E#"
파랑 = "#fc0xFF4641D9#"
보라 = "#fc0xFF8041D9#"
검보 = "#fc0xFF3F0099#"
function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 = "#fs15#" + 검정 + "스네이크 아이템은 " + 초록 + "에디셔널 잠재능력#k" + 검정 + "에 특수한 옵션이 달려있다네! 원하는 아이템을 선택해보게나!\r\n"
        말 += "#fc0xFFD5D5D5#───────────────────────────#k" + 검정 + "\r\n";
        말 += "#L0##i1113087# " + 파랑 + "#z1113087##k" + 검정 + " 아이템을 받겠습니다.#l\r\n"
        말 += "#L1##i1122219# " + 파랑 + "#z1122219##k" + 검정 + " 아이템을 받겠습니다.#l\r\n"
        cm.sendOkS(말, 0x04, 9000030);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 2) {
            cm.sendOkS("#fs15##r장비창 2 칸 이상을 확보하게.", 0x04, 9000030);
            cm.dispose();
            return;
        }
        if (selection == 0) {
            말 = "#fs15#" + 검정 + "자네는 " + 파랑 + "#z1113087##k" + 검정 + "을 선택했군! " + 초록 + "에디셔널 잠재능력#k" + 검정 + "에 부여될 옵션을 선택해보게! 크크.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k" + 검정 + "\r\n";
            말 += "#L0#" + 보라 + "아이템 획득량 60%#k" + 검정 + "를 선택하겠습니다.#l\r\n"
            말 += "#L1#" + 보라 + "메소 획득랑 60%" + 검정 + "를 선택하겠습니다.#l\r\n"
            cm.sendOkS(말, 0x04, 9000030);
        } else if (selection == 1) {
            말 = "#fs15#" + 검정 + "자네는 " + 파랑 + "#z1122219##k" + 검정 + "를 선택했군! " + 초록 + "에디셔널 잠재능력#k" + 검정 + "에 부여될 옵션을 선택해보게! 크크.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k" + 검정 + "\r\n";
            말 += "#L2#" + 보라 + "아이템 획득량 60%#k" + 검정 + "를 선택하겠습니다.#l\r\n"
            말 += "#L3#" + 보라 + "메소 획득랑 60%" + 검정 + "를 선택하겠습니다.#l\r\n"
            cm.sendOkS(말, 0x04, 9000030);
        }
    } else if (status == 2) {
        if (selection == 0) {
            말 = "#fs15#" + 검정 + "" + 파랑 + "#z1113087##k" + 검정 + " 아이템에 " + 초록 + "에디셔널 잠재능력#k" + 검정 + "을 부여했네! 인벤토리에서 확인해보게!\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i1113087# #b#z1113087##k\r\n"
            말 += "" + 검정 + "잠재능력 등급 : " + 검보 + "레전드리\r\n"
            말 += "" + 초록 + "첫번째 에디셔널 : 아이템 획득량 20%\r\n"
            말 += "두번째 에디셔널 : 아이템 획득량 20%\r\n"
            말 += "세번째 에디셔널 : 아이템 획득량 20%\r\n"
            cm.sendOkS(말, 0x04, 9000030);
            cm.gainItem(2430028, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1113087);
            inz.setState(20);
            inz.setPotential1(40501);
            inz.setPotential2(40501);
            inz.setPotential4(40656);
            inz.setPotential5(40656);
            inz.setPotential6(40656);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (selection == 1) {
            말 = "#fs15#" + 검정 + "" + 파랑 + "#z1113087##k" + 검정 + " 아이템에 " + 초록 + "에디셔널 잠재능력#k" + 검정 + "을 부여했네! 인벤토리에서 확인해보게!\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i1113087# #b#z1113087##k\r\n"
            말 += "" + 검정 + "잠재능력 등급 : " + 검보 + "레전드리\r\n"
            말 += "" + 초록 + "첫번째 에디셔널 : 메소 획득량 20%\r\n"
            말 += "두번째 에디셔널 : 메소 획득량 20%\r\n"
            말 += "세번째 에디셔널 : 메소 획득량 20%\r\n"
            cm.sendOkS(말, 0x04, 9000030);
            cm.gainItem(2430028, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1113087);
            inz.setState(20);
            inz.setPotential1(40501);
            inz.setPotential2(40501);
            inz.setPotential4(40650);
            inz.setPotential5(40650);
            inz.setPotential6(40650);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (selection == 2) {
            말 = "#fs15#" + 검정 + "" + 파랑 + "#z1122219##k" + 검정 + " 아이템에 " + 초록 + "에디셔널 잠재능력#k" + 검정 + "을 부여했네! 인벤토리에서 확인해보게!\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i1122219# #b#z1122219##k\r\n"
            말 += "" + 검정 + "잠재능력 등급 : " + 검보 + "레전드리\r\n"
            말 += "" + 초록 + "첫번째 에디셔널 : 아이템 획득량 20%\r\n"
            말 += "두번째 에디셔널 : 아이템 획득량 20%\r\n"
            말 += "세번째 에디셔널 : 아이템 획득량 20%\r\n"
            cm.sendOkS(말, 0x04, 9000030);
            cm.gainItem(2430028, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1122219);
            inz.setState(20);
            inz.setPotential1(40501);
            inz.setPotential2(40501);
            inz.setPotential4(40656);
            inz.setPotential5(40656);
            inz.setPotential6(40656);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (selection == 3) {
            말 = "#fs15#" + 검정 + "" + 파랑 + "#z1122219##k" + 검정 + " 아이템에 " + 초록 + "에디셔널 잠재능력#k" + 검정 + "을 부여했네! 인벤토리에서 확인해보게!\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i1122219# #b#z1122219##k\r\n"
            말 += "" + 검정 + "잠재능력 등급 : " + 검보 + "레전드리\r\n"
            말 += "" + 초록 + "첫번째 에디셔널 : 메소 획득량 20%\r\n"
            말 += "두번째 에디셔널 : 메소 획득량 20%\r\n"
            말 += "세번째 에디셔널 : 메소 획득량 20%\r\n"
            cm.sendOkS(말, 0x04, 9000030);
            cm.gainItem(2430028, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1122219);
            inz.setState(20);
            inz.setPotential1(40501);
            inz.setPotential2(40501);
            inz.setPotential4(40650);
            inz.setPotential5(40650);
            inz.setPotential6(40650);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        }
    }
}

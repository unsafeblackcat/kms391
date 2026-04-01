
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
상자 = 2437574;
아이템 = 0;
윗잠 = 0;
에디 = 0;

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
        말 = "#fs15#" + 검정 + "스네이크 아이템은 " + 초록 + "잠재능력#k" + 검정 + "과" + 초록 + " 에디셔널#k" + 검정 +  "에 특수한 옵션이 달려있다네! 원하는 아이템을 선택해보게나!\r\n"
        말 += "#fc0xFFD5D5D5#───────────────────────────#k" + 검정 + "\r\n";
        말 += "#L1113087##i1113087# " + 파랑 + "#z1113087##k" + 검정 + " 아이템을 받겠습니다.#l\r\n"
        말 += "#L1122219##i1122219# " + 파랑 + "#z1122219##k" + 검정 + " 아이템을 받겠습니다.#l\r\n"
        cm.sendOkS(말, 0x04, 9000030);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 2) {
            cm.sendOkS("#fs15##r장비창 2 칸 이상을 확보하게.", 0x04, 9000030);
            cm.dispose();
            return;
        }
            아이템 = selection;
            말 = "#fs15#" + 검정 + "자네는 " + 파랑 + "#z1113087##k" + 검정 + "을 선택했군! " + 초록 + "잠재능력#k" + 검정 + "에 부여될 옵션을 선택해보게! 크크.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k" + 검정 + "\r\n";
            말 += "#L40656#" + 보라 + "아이템 획득량 60%#k" + 검정 + "를 선택하겠습니다.#l\r\n"
            말 += "#L40650#" + 보라 + "메소 획득랑 60%" + 검정 + "를 선택하겠습니다.#l\r\n"
            cm.sendOkS(말, 0x04, 9000030);
    } else if (status == 2) {
            윗잠 = selection;
            말 = "#fs15#" + 검정 + "자네는 " + 파랑 + "#z1122219##k" + 검정 + "를 선택했군! " + 초록 + "에디셔널 잠재능력#k" + 검정 + "에 부여될 옵션을 선택해보게! 크크.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k" + 검정 + "\r\n";
            말 += "#L40656#" + 보라 + "아이템 획득량 60%#k" + 검정 + "를 선택하겠습니다.#l\r\n"
            말 += "#L40650#" + 보라 + "메소 획득랑 60%" + 검정 + "를 선택하겠습니다.#l\r\n"
            cm.sendOkS(말, 0x04, 9000030);
    } else if (status == 3) {
            에디 = selection;
            말 = "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i1113087# #b#z1113087##k\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k" + 검정 + "\r\n";
            if(윗잠 == 40656) {
            말 += "" + 초록 + "첫번째 잠재능력 : 아이템 획득량 20%\r\n"
            말 += "두번째 잠재능력 : 아이템 획득량 20%\r\n"
            말 += "세번째 잠재능력 : 아이템 획득량 20%\r\n";
	    } else if (윗잠 == 40650) {
            말 += "" + 초록 + "첫번째 잠재능력 : 메소 획득량 20%\r\n"
            말 += "두번째 잠재능력 : 메소 획득량 20%\r\n"
            말 += "세번째 잠재능력 : 메소 획득량 20%\r\n";
	    }
            말 += "#fc0xFFD5D5D5#───────────────────────────#k" + 검정 + "\r\n";
            if(에디 == 40656) {
            말 += "" + 초록 + "첫번째 에디셔널 : 아이템 획득량 20%\r\n"
            말 += "두번째 에디셔널 : 아이템 획득량 20%\r\n"
            말 += "세번째 에디셔널 : 아이템 획득량 20%\r\n";
	    } else if (에디 == 40650) {
            말 += "" + 초록 + "첫번째 에디셔널 : 메소 획득량 20%\r\n"
            말 += "두번째 에디셔널 : 메소 획득량 20%\r\n"
            말 += "세번째 에디셔널 : 메소 획득량 20%\r\n";
	    }
            말 += "#fc0xFFD5D5D5#───────────────────────────#k" + 검정 + "\r\n";
            cm.sendOkS(말, 0x04, 9000030);
            cm.gainItem(상자, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(아이템);
            inz.setState(20);
            inz.setPotential1(윗잠);
            inz.setPotential2(윗잠);
            inz.setPotential3(윗잠);
            inz.setPotential4(에디);
            inz.setPotential5(에디);
            inz.setPotential6(에디);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
	
    }
}




/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    ★ 에 의해 만들어 졌습니다.

    엔피시아이디 : 9000210

    엔피시 이름 : 병아리

    엔피시가 있는 맵 : 헤네시스 : 헤네시스 (100000000)

    엔피시 설명 : MISSINGNO


*/

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
검정 = "#fc0xFF191919#"
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

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
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot();
        if (leftslot < 1) {
            cm.sendOk("#fs15##r確保特殊和時裝至少有一格.");
            cm.dispose();
            return;
        }
        말 = "#fs15#" + 검정 + "選擇一個想要的奇幻寵物!\r\n";
        말 += "#L0#我想獲得#i5002561# #b#z5002561##k \r\n";
        말 += "#L1#我想獲得#i5002562# #b#z5002562##k \r\n";
        말 += "#L2#我想獲得#i5002563# #b#z5002563##k \r\n";
        //말 += "#L3#我想獲得#i5002199# #b#z5002199##k \r\n";
        cm.sendSimpleS(말, 0x04, 9010061);
    } else if (status == 1) {
        if (selection == 0) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002561, 1, "", Packages.client.inventory.MaplePet.createPet(5002561, -1), 9000, "", false);
            cm.sendOk("#fs15#獲得了" + 검정 + " #i5002561# #b#z5002561#" + 검정 + " 請檢査背包的特殊和時裝.");
			cm.gainItem(1802969, 1);
            cm.gainItem(2630127, -1);
            cm.dispose();
        } else if (selection == 1) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002562, 1, "", Packages.client.inventory.MaplePet.createPet(5002562, -1), 9000, "", false);
            cm.sendOk("#fs15#獲得了" + 검정 + " #i5002562# #b#z5002562#" + 검정 + "  請檢査背包的特殊和時裝.");
			cm.gainItem(1802970, 1);
            cm.gainItem(2630127, -1);
            cm.dispose();
        } else if (selection == 2) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002563, 1, "", Packages.client.inventory.MaplePet.createPet(5002563, -1), 9000, "", false);
            cm.sendOk("#fs15#獲得了" + 검정 + " #i5002563# #b#z5002563#" + 검정 + "  請檢査背包的特殊和時裝.");
			cm.gainItem(1802971, 1);
            cm.gainItem(2630127, -1);
            cm.dispose();
        /*} else if (selection == 3) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002080, 1, "", Packages.client.inventory.MaplePet.createPet(5002080, -1), 30, "", false);
            cm.sendOk("#fs15#" + 검정 + " #5002199# #b#5002199#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2630127, -1);
            cm.dispose();*/
        }
    }
}

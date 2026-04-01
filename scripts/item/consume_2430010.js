


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
            cm.sendOk("#fs15##r캐시창 1 칸 이상을 확보하게.");
            cm.dispose();
            return;
        }
        말 = "#fs15#" + 검정 + "받고싶은 펫을 하나 선택해보게!\r\n";
        말 += "#L0##i5000930# #b#z5000930##k 받기\r\n"; //1기
        /*말 += "#L1##i5000931# #b#z5000931##k 받기\r\n";
        말 += "#L2##i5000932# #b#z5000932##k 받기\r\n";
        말 += "#L3##i5002079# #b#z5002079##k 받기\r\n"; //2기
        말 += "#L4##i5002080# #b#z5002080##k 받기\r\n";
        말 += "#L5##i5002081# #b#z5002081##k 받기\r\n";
        말 += "#L6##i5002197# #b#z5002197##k 받기\r\n"; //3기
        말 += "#L7##i5002198# #b#z5002198##k 받기\r\n";
        말 += "#L8##i5002199# #b#z5002199##k 받기\r\n";
        말 += "#L9##i5002356# #b#z5002356##k 받기\r\n"; //4기
        말 += "#L10##i5002357# #b#z5002357##k 받기\r\n";
        말 += "#L11##i5002358# #b#z5002358##k 받기\r\n";*/
        cm.sendSimpleS(말, 0x04, 9062004);
    } else if (status == 1) {
        if (selection == 0) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 2439527, 1, "", Packages.client.inventory.MaplePet.createPet(2439527, -1), 365, "", false);
            cm.sendOk("#fs15#" + 검정 + " #i5000930# #b#z5000930#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2430068, -1);
            cm.dispose();
        } else if (selection == 1) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5000931, 1, "", Packages.client.inventory.MaplePet.createPet(5000931, -1), 365, "", false);
            cm.sendOk("#fs15#" + 검정 + " #i5000931# #b#z5000931#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2430068, -1);
            cm.dispose();
        } else if (selection == 2) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5000932, 1, "", Packages.client.inventory.MaplePet.createPet(5000932, -1), 365, "", false);
            cm.sendOk("#fs15#" + 검정 + " #i5000932# #b#z5000932#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2430068, -1);
            cm.dispose();
        } else if (selection == 3) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002079, 1, "", Packages.client.inventory.MaplePet.createPet(5002079, -1), 365, "", false);
            cm.sendOk("#fs15#" + 검정 + " #i5002079# #b#z5002079#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2430068, -1);
            cm.dispose();
        } else if (selection == 4) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002080, 1, "", Packages.client.inventory.MaplePet.createPet(5002080, -1), 365, "", false);
            cm.sendOk("#fs15#" + 검정 + " #i5002080# #b#z5002080#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2430068, -1);
            cm.dispose();
        } else if (selection == 5) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002081, 1, "", Packages.client.inventory.MaplePet.createPet(5002081, -1), 365, "", false);
            cm.sendOk("#fs15#" + 검정 + " #i5002081# #b#z5002081#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2430068, -1);
            cm.dispose();
        } else if (selection == 6) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002197, 1, "", Packages.client.inventory.MaplePet.createPet(5002197, -1), 365, "", false);
            cm.sendOk("#fs15#" + 검정 + " #i5002197# #b#z5002197#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2430068, -1);
            cm.dispose();
        } else if (selection == 7) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002198, 1, "", Packages.client.inventory.MaplePet.createPet(5002198, -1), 365, "", false);
            cm.sendOk("#fs15#" + 검정 + " #i5002198# #b#z5002198#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2430068, -1);
            cm.dispose();
        } else if (selection == 8) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002199, 1, "", Packages.client.inventory.MaplePet.createPet(5002199, -1), 365, "", false);
            cm.sendOk("#fs15#" + 검정 + " #i5002199# #b#z5002199#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2430068, -1);
            cm.dispose();
        } else if (selection == 9) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002356, 1, "", Packages.client.inventory.MaplePet.createPet(5002356, -1), 365, "", false);
            cm.sendOk("#fs15#" + 검정 + " #i5002356# #b#z5002356#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2430068, -1);
            cm.dispose();
        } else if (selection == 10) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002357, 1, "", Packages.client.inventory.MaplePet.createPet(5002357, -1), 365, "", false);
            cm.sendOk("#fs15#" + 검정 + " #i5002357# #b#z5002357#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2430068, -1);
            cm.dispose();
        } else if (selection == 11) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002358, 1, "", Packages.client.inventory.MaplePet.createPet(5002358, -1), 365, "", false);
            cm.sendOk("#fs15#" + 검정 + " #i5002358# #b#z5002358#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.");
            cm.gainItem(2430068, -1);
            cm.dispose();
        }
    }
}

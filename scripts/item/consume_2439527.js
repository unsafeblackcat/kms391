
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
    action (1, 0, 0);
}
검정 = "#fc0xFF191919#"
var day = 1; //유효기간 옵션 설정(일)
function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 = "#fs15#"+검정+"정말 이 캐릭터로 보상을 받으시겠습니까?\r\n"
        말 += "출석 특별상자 는 #r하루 1회#k"+검정+"만 받을 수 있습니다! (꼭 인벤토리의 공간을 여유롭게남겨두세요)"
        cm.sendYesNoS(말, 0x04, 9000030);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        leftslot1 = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 2 && leftslot1 < 2) {
           cm.sendOk("#fs15##r장비창과 기타창 2 칸 이상을 확보하게.");
           cm.dispose();
           return;
        }
        말 = "#fs15#"+검정+"선물이 지급되었습니다. 인벤토리를 확인해주세요.\r\n\r\n"
        cm.gainItem(4001083, 1); //젬
        cm.gainItem(4033304, 1); //경코젬
        cm.gainItem(4032002, 1); //코인
        cm.gainItem(4033670, 1); //원기베리
        cm.gainItem(4033578, 1); //놀장
        cm.gainItem(4001084, 1); //크리스탈
        cm.gainItem(4032301, 1); //카르마 17성
        cm.gainItem(4033047, 1); //메소
        cm.gainItem(4001034, 1); //애플
        cm.gainItem(4036456, 1); //젬
        cm.gainItem(4000362, 1); //경코젬
        cm.gainItem(4001877, 1); //코인
        cm.gainItem(4031662, 1); //원기베리
        cm.gainItem(4031569, 1); //놀장
        cm.gainItem(4320003, 1); //크리스탈
        cm.gainItem(4032434, 1); //카르마 17성
        cm.gainItem(4036315, 1); //메소
        cm.gainItem(4036166, 1); //애플
        cm.gainItem(4033644, 1); //젬
        cm.gainItem(4032811, 1); //경코젬
        cm.gainItem(4031857, 1); //코인
        cm.gainItem(4033441, 1); //원기베리
        cm.gainItem(4033450, 1); //놀장
        cm.gainItem(4036454, 1); //크리스탈
        cm.gainItem(4033115, 1); //카르마 17성
        cm.gainItem(4032927, 1); //메소
        cm.gainItem(4310210, 1); //출석코인
        cm.gainItem(2439527, -1); //회수
        cm.sendOkS(말, 0x04, 9000030);
        cm.dispose();
    }
}

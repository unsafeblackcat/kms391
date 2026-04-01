
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
var day = 14; //유효기간 옵션 설정(일)
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
        말 += "선물 상자는 #r계정당 1회#k"+검정+"만 받을 수 있습니다! (꼭 인벤토리의 공간을 여유롭게남겨두세요)"
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
        var inz = MapleItemInformationProvider.getInstance().getEquipById(1113087); //장비템
        inz.setExpiration((new Date()).getTime() + (1000 * 60 * 60 * 24 * day)); //기간
        inz.setState(20);
        inz.setPotential1(40650); //잠재
        inz.setPotential2(40650);
        inz.setPotential3(40650);
        inz.setPotential4(40656);
        inz.setPotential5(40656);
        inz.setPotential6(40656);
        MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
        cm.gainItem(5062010, 300); //블랙큐브
        cm.gainItem(5062503, 50); //화이트에디셔널
        cm.gainItem(2049704, 3); //레잠
        cm.gainItem(2450064, 10); //경쿠
        cm.gainItem(5121104, 3); //경뿌
        //cm.gainItem(1114312, 1); //딥다크링
        //cm.gainItem(1113087, 1); //구미호
        //cm.gainItem(2634291, 1); //샤벳
        cm.gainItem(4001715, 5); //메소
        cm.gainItem(2435719, 100); //젬
        cm.gainItem(2631527, 10); //경코젬
        cm.gainItem(2437760, 10); //심볼
        cm.gainItem(2437127, -1); //회수
        cm.sendOkS(말, 0x04, 9000030);
        cm.dispose();
    }
}

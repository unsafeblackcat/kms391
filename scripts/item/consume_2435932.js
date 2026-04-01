
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
var day = 7; //유효기간 옵션 설정(일)
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
        말 = "#fs15#"+검정+"안녕하세요! #h #님\r\n"
        말 += "#fc0xFFFF5AD9##e하드스우 격파 이벤트#n#k 보상을 수령하시겠습니까?"
        cm.sendYesNoS(말, 0x04, 9000030);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 2) {
           cm.sendOk("#fs15##r기타창 2 칸 이상을 확보해주세요.");
           cm.dispose();
           return;
        }
        말 = "#fs15#"+검정+"#fc0xFFFF5AD9##e하드 스우 격파 이벤트 보상#n#k이 지급되었습니다!\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
        말 += "#i4001716# #b10 개#k\r\n"
        말 += "#i4310248# #b10000 개#k\r\n"		
        cm.gainItem(4001716, 10);
        cm.gainItem(4310248, 9999);	
        cm.gainItem(4310248, 1);		
        cm.gainItem(2435932, -1);		
        cm.sendOkS(말, 0x04, 9000030);
        cm.dispose();
    }
}

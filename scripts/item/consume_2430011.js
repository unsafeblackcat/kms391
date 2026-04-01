
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
        말 = "#fs15#"+검정+"정말 이 캐릭터로 보상을 받을텐가?\r\n"
        말 += "선물 상자는 #r계정당 1회#k"+검정+"만 지급받는거라네!"
        cm.sendYesNoS(말, 0x04, 9062004);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        leftslot1 = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 2 && leftslot1 < 2) {
           cm.sendOk("#fs15##r장비창과 기타창 2 칸 이상을 확보하게.");
           cm.dispose();
           return;
        }
        말 = "#fs15#"+검정+"선물을 지급했네! 인벤토리를 확인해보게나. 크크\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
        말 += "#i1113001# #b#z1113001##k\r\n"
        말 += "#i4310291# #b#z4310291# 100개#k\r\n"
        말 += "#i5200002# #b10,000,000 메소#k\r\n"
        var inz = MapleItemInformationProvider.getInstance().getEquipById(1113001);
        inz.setExpiration((new Date()).getTime() + (1000 * 60 * 60 * 24 * day));
        inz.setState(20);
        inz.setPotential1(40086);
        inz.setPotential2(40086);
        inz.setPotential4(40656);
        inz.setPotential5(40656);
        inz.setPotential6(40656);
        MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
        cm.gainItem(4310291, 100);
        cm.gainMeso(10000000);
        cm.gainItem(2430011, -1);
        cm.sendOkS(말, 0x04, 9062004);
        cm.dispose();
    }
}

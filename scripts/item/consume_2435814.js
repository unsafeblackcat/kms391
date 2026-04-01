
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
        말 = "#fn分享哥特式##fs15#"+검정+"真的要用這個角色獲得獎勵嗎？\r\n"
        말 += "(一定要留出充足的庫存空間。)"
        cm.sendYesNo(말);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        leftslot1 = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 2 && leftslot1 < 2) {
           cm.sendOk("#fs15##r確保裝備視窗和其他視窗2格以上.");
           cm.dispose();
           return;
        }
        말 = "#fn分享哥特式##fs15#"+검정+"禮物已發放。 請確認庫存.\r\n\r\n"
		cm.getPlayer().gainHPoint(300000);
        cm.getPlayer().saveToDB(true, false);
        cm.gainItem(2435814, -1); //회수
        cm.sendOk(말);
        cm.dispose();
    }
}

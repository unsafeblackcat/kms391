
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
        말 = "#fs15#"+검정+"真的要用這個角色獲得獎勵嗎？?\r\n"
        말 += "服務器開啟禮盒 #r每個帳戶一次#k"+검정+"只能接受！ （一定要留出充足的庫存空間）"
        cm.sendYesNoS(말, 0x04, 9000030);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        leftslot1 = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 2 && leftslot1 < 2) {
           cm.sendOk("#fs15##r確保裝備視窗和其他視窗2格以上.");
           cm.dispose();
           return;
        }
        말 = "#fs15#"+검정+"禮物已發放。 請確認庫存。\r\n\r\n"
        cm.gainItem(5062009, 100); //紅色方塊
        cm.gainItem(5062010, 100); //黑色方塊
        cm.gainItem(5062500, 100); //附加方塊
        cm.gainItem(5121104, 4); //MVP追加經驗值50%天氣效果
        cm.gainItem(1114324, 1); //플레임링
        cm.gainItem(2049704, 5); //傳説潛在能力卷軸40%
        cm.gainItem(4001715, 20); //定居金1億楓幣
        cm.gainItem(2435719, 500); //核心寶石
        cm.gainItem(2437750, 200); //選擇祕法符文  5個兌換券
        cm.gainItem(2450064, 4); //經驗值2倍券(30分鐘)
        cm.gainItem(2048716, 100); //強力的輪迴星火
        cm.gainItem(2048717, 100); //永遠的輪迴星火
        cm.gainItem(2633912, 1); //航海師武器箱
        cm.gainItem(2633913, 4); //航海師防具箱
        cm.gainItem(2633617, 2); //(200～209)成長秘藥
        cm.gainItem(2633618, 2); //(200～219)成長秘藥 
        cm.gainItem(2633619, 2); //(200～229)成長秘藥
        cm.gainItem(2633620, 2); //200～239颱風成長秘藥 沒有圖片？？
        cm.gainItem(2436501, -1); //特別禮物盒
        cm.sendOkS(말, 0x04, 9000030);
        cm.dispose();
    }
}

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
黑色 = "#fc0xFF191919#"
var day = 7; //有效期限選項設定(天)
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
        對話 = "#fs15#"+黑色+"確定要用這個角色領取獎勵嗎？\r\n"
        對話 += "禮物箱是 #r每個帳號限領1次#k"+黑色+"的！"
        cm.sendYesNoS( 對話, 0x04, 9062004);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot(); 
        if (leftslot < 2) {
           cm.sendOk("#fs15##r 請確保其他欄位有2格以上空間。");
           cm.dispose(); 
           return;
        }
        對話 = "#fs15#"+黑色+"已經發放禮物了！檢查你的背包吧。呵呵\r\n\r\n"
        對話 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
        對話 += "#i4310291# #b#z4310291# 250個#k\r\n"
        對話 += "#i5200002# #b25,000,000 楓幣#k\r\n"
        對話 += "神器点数增加1#k\r\n"
        cm.gainItem(4310291,  1);
        cm.gainMeso(25000000); 
        cm.gainItem(2430014,  -1);
        cm.getPlayer().gainArtipactPoint(1, false);
        cm.getPlayer().gainArtipactExp(100000, false);
        cm.sendOkS( 對話, 0x04, 9062004);
        cm.dispose(); 
    }
}
importPackage(Packages.server); 
importPackage(Packages.database); 
importPackage(Packages.client); 
importPackage(java.lang); 
 
var status = -1;
var enter = "\r\n";
var tier_coin = 4310298;  // 階級幣道具ID
var talkType = 0x86;      // 對話框類型
 
var colorBlack = "#fc0xFF191919#";  // 黑色字體代碼
 
function start() {
    action(1, 0, 0);
}
 
function action(mode, type, sel) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    
    if (status == 0) {
        var chat = "#fs15#";
        chat += "#L0#" + "#e#r宣傳相關 GM 選項" + enter;
        chat += "#L1#" + "#e#b贊助相關 GM 選項" + enter;
 
        cm.sendSimple(chat); 
    } else if (status == 1) {
        switch (sel) {
            case 0:
                cm.dispose(); 
                cm.openNpc(3003167);   // 開啟宣傳NPC 
                break;
            case 1:
                cm.dispose(); 
                cm.openNpc(3003168);   // 開啟贊助NPC
                break;
        }
    }
}
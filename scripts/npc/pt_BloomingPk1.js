/* 
 
    * 此腳本由 Guardian 項目開發源碼自動生成系統創建 
 
    * (Guardian Project Development Source Script)
 
    由 캐논슈터 創建 
 
    NPC ID : 9062507 
 
    NPC 名稱 : 石之精靈 
 
    NPC 所在地圖 : 藍綻森林 : 幫助石之精靈入口 (993192700)
 
    NPC 說明 : 幫助石之精靈！
 
*/
 
var status = -1;
var sel = 0;
var higher = false;
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
        cm.dispose(); 
        return;
    }
    if (mode == 1) {
        status++;
    }
 
    if (status == 0) {
        if (cm.getPlayer().getMapId()  == 993192800) {
            cm.sendYesNoS("#b 真的...要離開嗎...？", 4, 9062507);
        } else {
            cm.dispose(); 
        }
    } else if (status == 1) {
        cm.warp(993192701,  0);
        cm.dispose(); 
    }
}
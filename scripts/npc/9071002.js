/* 
 
    * 此腳本由簡短NPC自動生成腳本系統製作 
    * (Guardian Project Development Source Script)
    由 White 製作 
    NPC ID : 9071002 
    NPC 名稱 : 瑪麗 
    NPC所在地圖 : 怪物公園 : 怪物公園 (951000000)
    NPC 功能 : 票券兌換員 
 
*/
 
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
        cm.sendNextS(" 嗨～你是來買票的嗎？\r\n#b怪物公園 REBORN#k 現在已經不需要入場券了喔～", 0x24);
    } else if (status == 1) {
        cm.sendOkS("#b 每天可以免費進入2次#k，\r\n之後每次入場需要消耗#b30個黑暗硬幣#k喔～", 0x24, 9071002);
        cm.dispose(); 
    }
}
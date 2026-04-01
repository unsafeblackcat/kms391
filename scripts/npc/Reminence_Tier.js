var status = -1;
var enter = "\r\n";
var tier_coin = 4310298;  // 階級硬幣代碼 
var talkType = 0x86;      // 對話框類型
 
function start() {
    action(1, 0 ,0);
}
 
function action(mode, type ,sel) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        var chat = "\r\n";
 
        chat += "說明" + enter;
        chat += "#L0#" + "階級晉升" + enter;
        chat += "#L1#" + "聽取階級內容說明" + enter;
 
        cm.sendSimpleS(chat,  talkType);
    }
}
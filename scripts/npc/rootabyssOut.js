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
        cm.sendOkS(" 既然已經使用鑰匙進來了，就努力通關到擊敗BOSS吧。", 2);
        cm.dispose(); 
        return;
    }
    if (mode == 1) {
        status++;
    }
 
    if (status == 0) {
        cm.sendYesNoS(" 已經用掉鑰匙了…與其浪費消耗的鑰匙直接離開，不如擊敗BOSS更划算…但還是要直接退出嗎？", 2);
    } else if (status == 1) {
        cm.dispose(); 
        if (mode == 1) {
            cm.warp(105200000); 
            cm.setDeathcount(0); 
        }
    }
}
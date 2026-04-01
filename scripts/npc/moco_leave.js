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
        cm.sendYesNo(" 確定要離開嗎？");
    } else if (status == 1) {
        cm.setDeathcount(0);   // 重置死亡計數 
        cm.warp(1000000);      // 傳送至地圖ID 1000000 
        cm.dispose();          // 關閉對話框 
    }
}
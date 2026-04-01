var status = 0;
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == 1) 
        status++;
    else 
        status--;
    if (status == 0) {
        if (cm.getMapId()  != 951000000) {
            cm.sendYesNo(" 親愛的玩家您好～是否要前往充滿新奇樂趣的「史匹格」怪物公園呢？");
        } else {
            cm.sendYesNo(" 您好！這裡是始終致力於玩家滿意度的怪物公園接駁車。要返回村莊嗎？");
        }
    } else if (status == 1) {
        if (cm.getMapId()  != 1000000000) {
            cm.warp(1000000000,  0); // 傳送至怪物公園
        } else {
            cm.warp(100000000,  0);  // 傳回弓箭手村 
        }
        cm.dispose(); 
    }
}
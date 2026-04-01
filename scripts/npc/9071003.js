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
            cm.sendYesNo(" #fs15#親愛的玩家您好。是否要前往充滿新奇樂趣的「超級怪物公園」？");
        } else {
            cm.sendYesNo(" #fs15#您好，這裡是始終致力於玩家滿意度的怪物公園接駁車。是否要返回村莊？");
        }
    } else if (status == 1) {
        if (cm.getMapId()  != 951000000) {
            cm.saveLocation("MONSTERPARK"); 
            cm.warp(951000000,0); 
        } else {
            cm.warp(cm.getSavedLocation("MONSTERPARK"),0); 
            cm.clearSavedLocation("MONSTERPARK"); 
        }
        cm.dispose(); 
    }
}
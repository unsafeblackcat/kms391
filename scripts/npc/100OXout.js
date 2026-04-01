var status = -1;
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose(); 
        return;
    }
    if (mode == 1) {
        status++;
    }
 
    if (status == 0) {
        cm.sendYesNo(" 現在離開將無法再次進入遊戲。確定要離開嗎？");
    } else if (status == 1) {
        cm.warp(910048200,  0);
        cm.getPlayer().cancelEffectFromBuffStat(Packages.client.MapleBuffStat.RideVehicle,  80001013);
        cm.dispose(); 
    }
}
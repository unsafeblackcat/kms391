var status = -1;

function start() {
    action(1, 0, 0);
}

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
        cm.sendYesNo("是否移動至等候室？點擊#b是#k將移動至等候室。");
    } else if (status == 1) {
        cm.dispose();
        cm.warp(910810000);
    }
}
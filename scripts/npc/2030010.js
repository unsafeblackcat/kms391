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
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        cm.sendYesNoS("#fs15#離開這裏的話，下次進來的時候要從新挑戰！", 0x24);
    } else if (status == 1) {
        cm.warp(211042300, "ps00");
        cm.dispose();
    }
}
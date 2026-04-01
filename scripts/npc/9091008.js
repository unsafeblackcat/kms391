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
        cm.sendNextS("#fs15#挑戰完成離開?", 4, 9091008);
    } else if (status == 1) {
        cm.warp(271040000, 0);
        cm.dispose();
    }
}
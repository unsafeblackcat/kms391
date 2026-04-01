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
        status--;
    }
    if (mode == 1) {
        status++;
    }



    if (status == 0) {
        cm.sendYesNo("무릉도장 으로 이동하겠나 ? #b예#k를 누르시면 무릉도장으로 이동됩니다.");
    } else if (status == 1) {
        cm.dispose();
        cm.warp(925020000);
    }
}
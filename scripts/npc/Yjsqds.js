var status;

var sel;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    } else {
        status++;
    }

    if (status == 0) {
        cm.getPlayer().gainArtipactPoint(1000, false);//神器點數
        cm.sendOkS("恭喜你獲得了1000神器點數", 4, 9000030);
        cm.dispose();
    }
}
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
        cm.getPlayer().gainArtipactExp(3000, false);//神器經驗
        cm.sendOkS("獲得了3000點神器經驗", 4, 9000030);
        cm.dispose();
    }
}
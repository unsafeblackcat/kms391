var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
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
        cm.sendOk("n0 我遭到詛咒，被封印在傳說之劍中.. n1 據說只有艾泰爾最強的戰士才能解開此詛咒... ");
        cm.dispose();
        return;
    }
}
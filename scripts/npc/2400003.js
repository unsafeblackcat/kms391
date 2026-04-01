var status = -1,
    sel = 0;

function start() {
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
        cm.dispose();
		cm.openNpcCustom(cm.getClient(), 2400003, "UI07");
    }
}
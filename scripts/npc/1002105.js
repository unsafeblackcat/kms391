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
	status--;
	cm.dispose();
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	cm.sendYesNo("是否通過次元門前往萬神殿？");
    } else if (status == 1) {
	cm.warp(400000001);
	cm.dispose();
    }
}
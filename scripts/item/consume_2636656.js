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
		cm.gainItem(2636656, -1);
		cm.getPlayer().gainSolErda(1);
		cm.dispose();
	}
}
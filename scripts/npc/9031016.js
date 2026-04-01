/*
제작자 : qudtlstorl79@nate.com
*/
검정 = "#fc0xFF191919#";
var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}


function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0 && (status == 0)) {
			cm.sendOkS("#fs15#또 들려주게나~", 0x04, 9401232);
			cm.dispose();
		}
		if (mode > 0)
			status++;
		else
			status--;
		if (status == 0) {
			if (cm.getPlayer().getLevel() >= 10) {
				cm.sendStorage();
				cm.dispose();

			} else {
				cm.sendOkS("#fs15#" + 검정 + "창고를 이용하기 위해선 10레벨 이상이 되어야 한다네.", 0x04, 9401232);
				cm.dispose();
			}
		}
	}
}

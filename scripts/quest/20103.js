/*
 * Cygnus 1st Job advancement - Wind Breaker
 */

var status = -1;

function end(mode, type, selection) {
    if (mode == 0) {
	if (status == 0) {
	    qm.sendNext("This is an important decision to make.");
	    qm.safeDispose();
	    return;
	}
	status--;
    } else {
	status++;
    }
    if (status == 0) {
	qm.sendOk("절대 후회없는 선택을 기대할게!");
    } else if (status == 1) {
	qm.sendNext("전직 완료");
	if (qm.getJob() != 1300) {
	    qm.gainItem(2060000, 2000);
	    qm.gainItem(1452051, 1);
	    qm.gainItem(1142066, 1);
	    qm.expandInventory(1, 4);
	    qm.expandInventory(4, 4);
	    qm.changeJob(1300);
	}
	qm.forceCompleteQuest();
	qm.safeDispose();
    }
}
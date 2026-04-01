var status = -1;

function start(mode, type, selection) {
	qm.sendNext("엘리니아에 가보시겠어요? 당신을 기다리는 사람이 있어요.");
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.sendNext("엘리니아에 가보시겠어요? 당신을 기다리는 사람이 있어요.");
	qm.forceCompleteQuest();
	qm.dispose();
}
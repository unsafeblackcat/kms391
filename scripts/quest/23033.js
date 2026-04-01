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
	qm.sendYesNo("좀더 강력한 배틀메이지로 거듭나고 싶나?");
    } else if (status == 1) {
	qm.sendNext("널 전직시켰어. 이제 더 이상 예전의 네가 아니야. 더 강력하고 화려한 스킬의 세계가 펼쳐질 거야. 순간이동하며 적을 끌고오며 순식간에 적을 제압하겠지. 쉽지 않겠지만, 겁날게 뭐가 있겠어? 그 어려운 임무도 해낸 너인데 말이야.");
	if (qm.getJob() == 3210) {
	    qm.changeJob(3211);
	}
	qm.forceCompleteQuest();
    } else if (status == 2) {
	qm.sendNextPrev("그럼 다음 수업에 보지. 그때까지 레지스탕스로서 멋지게 활약하길 기대하지.");
	qm.safeDispose();
    }
}
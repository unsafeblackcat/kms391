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
	qm.sendYesNo("좀더 강한 와일드 헌터로 거듭나고 싶어?");
    } else if (status == 1) {
	qm.sendNext("널 전직시켰어. 이제 더 이상 예전의 네가 아니야. 더 강력하고 빠르고 화려한 스킬의 세계가 펼쳐질 거야. 라이딩까지 타고 다니니 사용하기 쉽지 않겠지만, 겁날게 뭐가 있겠어? 너라면 식은죽 먹기인거 같은데!");
	if (qm.getJob() == 3310) {
	    qm.changeJob(3311);
	}
	qm.forceCompleteQuest();
    } else if (status == 2) {
	qm.sendNextPrev("그럼 다음 수업에 보자. 그때까지 레지스탕스로서 멋지게 활약하길 기대하지.");
	qm.safeDispose();
    }
}
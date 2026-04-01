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
	qm.sendYesNo("좀더 강한 메카닉으로 거듭나고 싶나요?");
    } else if (status == 1) {
	qm.sendNext("당신을 전직시켰습니다. 이제 더 이상 예전의 당신이 아닙니다. 더 다양하고 복잡한, 그만큼 강력한 스킬이 당신을 구성할 겁니다. 어려울 것 같다고요? 걱정 마십시오. 당신이라면 자유자재로 다룰 수 있을 겁니다.");
	if (qm.getJob() == 3500) {
	    qm.changeJob(3510);
	}
	qm.forceCompleteQuest();
    } else if (status == 2) {
	qm.sendNextPrev("그럼 다음 수업에 보도록 하죠. 그때까지 레지스탕스로서 멋지게 활약하길 기대합니다.");
	qm.safeDispose();
    }
}
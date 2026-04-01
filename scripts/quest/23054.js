var status = -1;

function start(mode, type, selection) {
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
	qm.sendYesNo("메카닉의 최고단계에 다가가기 원하나요?");
    } else if (status == 1) {
	qm.sendNext("당신을 전직시켰습니다. 동시에 제가 만들 수 있는 최고의 로봇들을 드렸습니다. 저로서도 다 통제할 수 없어 제대로 다루지 못하고 있었지만... 아무래도 당신이라면 어쩐지 해낼 것만 같군요. 레지스탕스 최고의 메카닉으로서 말입니다.");
	if (qm.getJob() == 3511) {
	    qm.changeJob(3512);
	    qm.forceCompleteQuest();
	}
    } else if (status == 2) {
	qm.sendNextPrev("이걸로 제 수업도 끝...에 가까워졌군요. 하지만 아직 끝은 아닙니다. 당신이 저보다 강해진건 사실이지만, 그래도 아직 제게 배울게 없지는 않을 테니까요. 그러니 다음 수업에 뵙시다. 언제 시작될지 알 수 없는 수업이지만... 그때까지 레지스탕스로서 멋지게 활약하길 기대하겠습니다.");
	qm.safeDispose();
    }
}

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
	qm.sendYesNo("메카닉의 최고단계에 다가가기 원하나요?");
    } else if (status == 1) {
	qm.sendNext("당신을 전직시켰습니다. 동시에 제가 만들 수 있는 최고의 로봇들을 드렸습니다. 저로서도 다 통제할 수 없어 제대로 다루지 못하고 있었지만... 아무래도 당신이라면 어쩐지 해낼 것만 같군요. 레지스탕스 최고의 메카닉으로서 말입니다.");
	if (qm.getJob() == 3511) {
	    qm.changeJob(3512);
	    qm.forceCompleteQuest();
	}
    } else if (status == 2) {
	qm.sendNextPrev("이걸로 제 수업도 끝...에 가까워졌군요. 하지만 아직 끝은 아닙니다. 당신이 저보다 강해진건 사실이지만, 그래도 아직 제게 배울게 없지는 않을 테니까요. 그러니 다음 수업에 뵙시다. 언제 시작될지 알 수 없는 수업이지만... 그때까지 레지스탕스로서 멋지게 활약하길 기대하겠습니다.");
	qm.safeDispose();
    }
}
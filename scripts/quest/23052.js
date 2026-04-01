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
	qm.sendYesNo("배틀메이지의 최고단계에 다가가기 원하나?");
    } else if (status == 1) {
	qm.sendNext("널 전직시켰어. 동시에 내가 알고 있던, 하지만 완벽히 다루지는 못한 최고의 스킬들을 전수했지. 나는 완성하지 못한 것들이지만 너라면 해낼 수 있을 거라고 믿어. 못할 게 뭐가 있겠어? 우리 레지스탕스 최고의 실력자인데 말이야!");
	if (qm.getJob() == 3211) {
	    qm.changeJob(3212);
	    qm.forceCompleteQuest();
	}
    } else if (status == 2) {
	qm.sendNextPrev("이걸로 내 수업도 마지막인가... 아니 그럴리가 없지. 이래봬도 나 꽤 유능한 레지스탕스인걸. 지금은 네가 나보다 강하지만, 그래도 나한테 배울게 없지는 않을걸? 그러니... 다음 수업에 보자 언제 할지는 알 수 없는 수업이지만. 그때까지 레지스탕스로서 멋지게 활약하길 기대하지.");
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
	qm.sendYesNo("배틀메이지의 최고단계에 다가가기 원하나?");
    } else if (status == 1) {
	qm.sendNext("널 전직시켰어. 동시에 내가 알고 있던, 하지만 완벽히 다루지는 못한 최고의 스킬들을 전수했지. 나는 완성하지 못한 것들이지만 너라면 해낼 수 있을 거라고 믿어. 못할 게 뭐가 있겠어? 우리 레지스탕스 최고의 실력자인데 말이야!");
	if (qm.getJob() == 3211) {
	    qm.changeJob(3212);
	    qm.forceCompleteQuest();
	}
    } else if (status == 2) {
	qm.sendNextPrev("이걸로 내 수업도 마지막인가... 아니 그럴리가 없지. 이래봬도 나 꽤 유능한 레지스탕스인걸. 지금은 네가 나보다 강하지만, 그래도 나한테 배울게 없지는 않을걸? 그러니... 다음 수업에 보자 언제 할지는 알 수 없는 수업이지만. 그때까지 레지스탕스로서 멋지게 활약하길 기대하지.");
	qm.safeDispose();
    }
}
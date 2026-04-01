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
	qm.sendYesNo("와일드 헌터의 최고단계에 다가가기 원하나?");
    } else if (status == 1) {
	qm.sendNext("널 전직시켰어. 동시에 내가 알고 있던, 하지만 나조차 자유자재로 다루지 못한 최고의 스킬들을 전수했지. 나는 해내지 못한 거지만 아무래도 너라면 할 수 있을 것 같거든. 뭐, 당연하잖아? 네가 우리 레지스탕스 최고의 실력자인데 말이야!");
	if (qm.getJob() == 3311) {
	    qm.changeJob(3312);
	    qm.forceCompleteQuest();
	}
    } else if (status == 2) {
	qm.sendNextPrev("이걸로 내 수업도 마지막...인 건 아냐. 이래봬도 아직 나 꽤 유능하다고? 네가 나보다 강해지긴 했지만, 그래도 나에게 배울 게 없지는 않을걸? 그러니...다음 수업에 보자. 언제 할지는 알 수 없는 수업이지만. 그때까지 레지스탕스로서 멋지게 활약하길 기대하지.");
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
	qm.sendYesNo("와일드 헌터의 최고단계에 다가가기 원하나?");
    } else if (status == 1) {
	qm.sendNext("널 전직시켰어. 동시에 내가 알고 있던, 하지만 나조차 자유자재로 다루지 못한 최고의 스킬들을 전수했지. 나는 해내지 못한 거지만 아무래도 너라면 할 수 있을 것 같거든. 뭐, 당연하잖아? 네가 우리 레지스탕스 최고의 실력자인데 말이야!");
	if (qm.getJob() == 3311) {
	    qm.changeJob(3312);
	    qm.forceCompleteQuest();
	}
    } else if (status == 2) {
	qm.sendNextPrev("이걸로 내 수업도 마지막...인 건 아냐. 이래봬도 아직 나 꽤 유능하다고? 네가 나보다 강해지긴 했지만, 그래도 나에게 배울 게 없지는 않을걸? 그러니...다음 수업에 보자. 언제 할지는 알 수 없는 수업이지만. 그때까지 레지스탕스로서 멋지게 활약하길 기대하지.");
	qm.safeDispose();
    }
}
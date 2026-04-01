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
    //if (!qm.getPlayer().isGM()) { //not available, sry
	//qm.dispose();
	//return;
    //}
    if (status == 0) {
	qm.sendYesNo("지금 바로 메카닉이 되겠습니까?");
    } else if (status == 1) {
	qm.sendNext("당신을 전직시켰습니다. 다양하고 복잡한, 그만큼 강력한 스킬이 당신을 구성할 겁니다. 어려울 것 같다고요? 걱정 마십시오. 당신이라면 자유자재로 다룰 수 있을 겁니다.");
	if (qm.getJob() == 3000) {
	    qm.gainItem(1492014,1); //1492065 desert eagle
	    qm.expandInventory(1, 4);
	    qm.expandInventory(2, 4);
	    qm.expandInventory(4, 4);
	    qm.changeJob(3500);
	    //30001061 = capture, 30001062 = call, 30001068 = mech dash
	    qm.teachSkill(30001068,1,0);
	}
	qm.forceCompleteQuest();
    } else if (status == 2) {
	qm.sendNextPrev("당신도 이제 레지스탕스의 일원입니다. 에델슈타인을 위해 아이들의 미래를 위해 같이 싸우게 되었습니다.");
    } else if (status == 3) {
	qm.sendNextPrev("그럼 다음 수업에 보도록 하죠. 그때까지 레지스탕스로서 멋지게 활약하길 기대합니다.");
	qm.safeDispose();
    }
}
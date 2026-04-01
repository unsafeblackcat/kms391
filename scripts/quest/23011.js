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
	qm.sendYesNo("배틀 메이지가 되기로 결심한 건가? 아직 선택을 번복할 기회는\n\r 있어. 대화를 멈추고 퀘스트를 포기한 후 다른 녀석에게 말을 걸면\n\r 돼. 잘 생각해봐. 정말 배틀 메이지를 택하겠어? 이 직업이 네 레지스탕스의 길에 어울린다고 생각해?");
    } else if (status == 1) {
	qm.sendNext("좋아! 정식 레지스탕스가 된 걸 환영하지. 지금부터 너는 배틀 메\n\r 이지다. 싸우는 마법사로서 광기를 품고 누구보다 앞에 나가서 적\n\r 과 맞서자고.");
	if (qm.getJob() == 3000) {
	    qm.gainItem(1382100,1);
	    qm.expandInventory(1, 4);
	    qm.expandInventory(2, 4);
	    qm.expandInventory(4, 4);
	    qm.changeJob(3200);
	}
	qm.forceCompleteQuest();
    } else if (status == 2) {
	qm.sendNextPrev("I have also expanded your inventory slot counts for your equipment and etc. inventory. Use those slots wisely and fill them up with items required for Resistance to carry.");
    } else if (status == 3) {
	qm.sendNextPrev("Now... I want you to go out there and show the world how the Resistance operate.");
	qm.safeDispose();
    }
}
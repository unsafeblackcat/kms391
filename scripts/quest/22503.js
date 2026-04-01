/*
	Description: 	Quest - A Bite of Pork
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendNext("태어난 지 얼마 되지도 않은 나를 굶기다니, 이건 용권침해야! 우에엥~");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("이런 거 말고 더 맛있는 거 없어? 풀은 나에게 안 맞아, 더 영양가 높은 게 필요해, 마스터!");
    } else if (status == 1) {
	qm.sendNextPrevS("#b음... 채식은 싫은 거야? 드래곤이니까 역시 육식을 좋아하는 걸지도 모르겠네. 그럼 돼지고기 같은 건 어 때?#k", 2);
    } else if (status == 2) {
	qm.askAcceptDecline("돼지고기가 뭔지 모르겠어~ 하지만 맛있는 거라면 뭐든 좋아. 얼른 아무거나 구해줘~ 풀은 빼고!");
    } else if (status == 3) {
	qm.forceStartQuest();
	qm.sendOkS("#b(그럼 미르에게 돼지고기를 주도록 하자. 농장에 있는 돼지들을 몇 마리만 잡으면 된다. 한 10개만 구하면 되겠지?)#k", 2);
	qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	qm.sendOk("오옷, 새로운 먹을 걸 구해온 거야? 이게 바로 마스터가 말하던 돼지고기? 어디 한 번 먹어볼까? 아앙~.");
    } else if (status == 1) {
	qm.gainExp(980);
	qm.gainItem(4032453, -10);
	qm.sendNext("(우물우물, 꿀꺽)");
	qm.forceCompleteQuest();
    } else if (status == 2) {
	qm.sendPrev("윽... 이거 맛은 나쁘지 않은데 너무 질겨. 소화가 안 될 것 같아. 나에게는 안 맞아");
	qm.dispose();
    }
}
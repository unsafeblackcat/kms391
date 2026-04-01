/*
	Description: 	Quest - Tasty Milk 1
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendNext("마스터 혼자 고민해 봐야 답이 안 나올 것 같아. #b마스터보다 지혜와 연륜이 있는 사람#k을 찾아보자!");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("히잉, 아무래도 안 되겠어. 다른 게 필요해. 풀도 고기도 아닌 다른 거... 뭐 없어? 마스터는 나보다 나이가 많으니 잘 알 거 아니야?");
    } else if (status == 1) {
	qm.sendNextPrevS("#b그, 그렇게 말해도 나도 잘 모르겠는데... 나이가 많다고 다 아는 건 아닌걸...", 2);
    } else if (status == 2) {
	qm.askAcceptDecline("그래도 나이가 많으면 세상을 조금이라도 더 많이 경험해 봤으니 지식도 더 많은 게 당연하잖아. 아, 그렇지! 마스터보다 더 나이 많은 사람에게 물어보면 알지도 몰라!");
    } else if (status == 3) {
	qm.forceStartQuest();
	qm.sendOkS("#b(아빠한테는 이미 한 번 물어봤는데... 그래도 혹시 모르니 다시 한 번 아빠에게 물어보자.)#k", 2);
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
	qm.sendOk("응? 아직도 도마뱀이 뭘 먹는지 찾지 못한 거니? 건초 한 줌도 안 먹고 돼지고기도 안 먹는다고? 까다로운 녀석이구나. 그 녀석만이 가지고 있는 특징 같은 건 없니? 응? 그 도마뱀이 아직 아기라고?");
    } else if (status == 1) {
	qm.gainExp(100);
	qm.forceCompleteQuest();
	qm.dispose();
    }
}
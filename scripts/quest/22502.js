/*
	Description: 	Quest - A Bite of Hay
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    qm.sendNext("흠... 그래? 건초는 안 먹을 것 같니? 하지만 혹시 모르잖니. 그 도마뱀 정도면 세상에 이X 일이에 나올 정도로 크니 건초를 먹을지도...");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.askAcceptDecline("혹시 도마뱀도 소처럼 #b건초 한 줌#k를 먹을 수도 있지 않을까? 주변에 #b건초더미#k들이 많이 있으니 궁굼하다면 한 번 먹여 보려무나. 안 먹으면 그 때 다른 먹이를 구해 주면 되잖니.");
    } else if (status == 1) {
	qm.forceStartQuest();
	qm.evanTutorial("UI/tutorial/evan/12/0", 1);
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
	qm.sendNext("Oh, I'm so hungry! Did you find something good for me to eat, master? Hmm... This looks like...grass. Can I really eat this? Okay master, I'll trust you.");
    } else if (status == 1) {
	qm.sendOk("Okay, here goes!");
    } else if (status == 2) {
	qm.gainExp(800);
	qm.gainItem(4032452, -3);
	qm.sendOk("Yuck! What is this? It's bitter and tought! Are you sure this is edible? Master, you eat it! I can't eat this! Find me something else!");
	qm.forceCompleteQuest();
	qm.dispose();
    }
}
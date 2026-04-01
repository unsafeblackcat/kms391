/*
	NPC Name: 		Cygnus
	Description: 		Quest - Encounter with the Young Queen
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendNext("Hmm, there is nothing to worry about. This will be a breeze for someone your level. Muster your courage and let me know when you're ready.");
	    qm.safeDispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("응? #p1101002#가 보냈다고? 아하! 이야, 이번에 새로 들어온 신입\n\r 인가? 반갑군. 반가워 내 이름은 #p1102000#. 자네 같은 노블레스들을 가\n\r 르치는 수련교관이지. 물론 인간은 아냐.");
    } else if (status == 1) {
	qm.sendNextPrev("우리는 피요라고 불리는 종족이야.어린 여제 곁에 있는 #p1101001#는 본\n\r 적이 있지? 피요는 #p1101001#와 같은 종족이야. 계열은 좀 다르지만...\n\r 비슷하지. 에레브에서만 살고 있으니 낮설겠지만 금방 익숙해질\n\r 거야.");
    } else if (status == 2) {
	qm.sendNextPrev("아, 혹시 알고 있어? 이 에레브에는 몬스터는 전혀 없어. 사악한\n\r 힘을 가진 존재는 에레브에 발도 들일 수 없거든. 그래도 걱정하진\n\r 마. 수련은 #p1101001#가 만들어낸 환상생물 티티를 대상으로 할 거니까.");
    } else if (status == 3) {
	qm.askAcceptDecline("기합이 제대로 들어갔군! 그럼... 자네의 수준을 보아하니, 티티들\n\r 중에서도 조금 상급의 티티를 잡아도 되겠군. #b#m130010100##k에 있는\n\r #r#o100122##k #r15 마리#k 정도면 충분 하겠는걸? 왼쪽의 포탈을 타고 #b수련의\n\r 숲2#k로 가서 사냥하도록 하게.");
    } else if (status == 4) {
	qm.summonMsg(12);
	qm.forceStartQuest(20020);
	qm.forceCompleteQuest(20100);
	qm.forceStartQuest();
	qm.dispose();
    }
}

function end(mode, type, selection) {
}
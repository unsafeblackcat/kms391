/*
	Description: 	Quest - Hungry Baby Dragon
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 3) {
	    qm.sendNext("우왁, 마스터가 밥 먹이는 걸 거절했어~ 말도 안돼! 이건 드래곤 학대다!");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("이봐, 마스터. 자, 내 능력은 보여줬으니 마스터도 어서 나에게 능력을 보여줘. 음식을 구하는 능력을! 응? 내 힘을 쓸 수 있게 되었으니 그만큼 날 돌봐줘야지.");
    } else if (status == 1) {
	qm.sendNextPrevS("으... 뭐가 뭔지 모르겠지만 일단 돌봐줘야 할 처지 같네. 맛있는 거라고? 뭐가 먹고 싶은데? 드래곤은 뭘 먹는지 감도 안와.", 2);
    } else if (status == 2) {
	qm.sendNextPrev("태어난 지 몇 분 되지도 않은 내가 그런 걸 알 리가 없잖아. 내 머릿속에 있는 거라고는 내가 드래곤이라는 거, 네가 마스터라는 거, 그리고 마스터는 무조건 나한테 잘 해줘야 한다는거밖에 없어!");
    } else if (status == 3) {
	qm.askAcceptDecline("그 외의 것들은 마스터와 함께 지내며 하나씩 배워가야 하는 모양이야~ 하지만 일단 지식보다 배를 채울 음식이 더 급해... 마스터, 맛있는 걸 구해줘!");
    } else if (status == 4) {
	qm.forceStartQuest();
	qm.sendOkS("#b(아기 드래곤 미르는 배가 무척 고픈 모양이다 뭐라도 먹여야 할 텐데... 그런데 드래곤은 뭘 먹을지 도무지 알 수 없다. 혹시 아빠는 알고 계시지 않을까? 한 번 아빠에게 물어보자.)", 2);
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
	qm.sendNext("뭇느 일이니, 에반? 뭔가 할 말이라도 있는 거니? 드래곤이 뭘 먹냐고? 갑자기 그런 건 왜 묻는 거니? 응? 네가 드래곤을 주웠다고?");
    } else if (status == 1) {
	qm.sendNextS("#b(아빠에게 미르를 보여드렸다.)#k", 2);
    } else if (status == 2) {
	qm.sendNextPrev("엥? 그게 드래곤이라고? ...혹시 커다란 도마뱀이나 뭐 그런거 아니니? 생명은 소중한 거니 키워도 좋긴 하다만...");
    } else if (status == 3) {
	qm.sendNextS("#b(아빠는 미르를 드래곤이라고 생각하지 않는 것 같다. 하긴, 이렇게 작으니까 믿기 어려울 수도 있겠다. 미르가 말하는 모습을 보여주면 믿으실까?)", 2);
    } else if (status == 4) {
	qm.sendNextPrev("진짜 드래곤이라면 무척 위험하니 키울 수 없단다. 불을 뿜을지도 모르잖니? 이 녀석은 드래곤은 아닌 것 같지만, 혹시 모르니 모험가들에게 부탁해 처치하도록 하는 게 좋을지도 모르겠구나.");
    } else if (status == 5) {
	qm.sendNextS("#b(헉?! 미르를 처치한다고?! 나쁜 짓을 한 것도 아닌데? 그럼 너무 불쌍하잖아...)", 2);
    } else if (status == 6) {
	qm.sendNextPrev("뭐, 드래곤일 리가 없지. 드래곤은 저 오시리아 대륙에 리프레에나 나온다니 말이다. 하하하..");
    } else if (status == 7) {
	qm.sendNextS("#그, 그렇죠? 역시 드래곤은 아니겠죠? 아마 도마뱀일 거에요!!#k", 2);
    } else if (status == 8) {
	qm.sendNextPrev("응. 역시 그렇지? 참 특이하게 생긴 도마뱀이구나. 딱히 위험해 보이진 않으니 길러도 좋다.");
    } else if (status == 9) {
	qm.sendNextS("#b(모험가들이 와서 미르를 처치해버릴지도 모르니, 미르가 드래곤이라는 사실은 되도록 아무에게도 알리지 않는 게 좋겠다.)#k", 2);
    } else if (status == 10) {
	qm.sendOk("아, 도마뱀의 먹이를 찾는 거랬지? 아빠는 농장에서 도마뱀을 길러본 적이 없어 뭘 먹는지 모르겠구나 잠깐만 생각해보마.");
    } else if (status == 11) {
	qm.gainExp(100);
	qm.forceCompleteQuest();
	qm.dispose();
    }
}
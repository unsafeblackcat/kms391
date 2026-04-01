var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("응? 유타에게는 말해주기 싫은 거니? 얘도 참, 형제끼리는 사\r\n이 좋게 지내야지.");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("잘 잤니, 에반?");
	else if (status == 1)
		qm.PlayerToNpc("#b네... 엄마도 잘 주무셨어요?#k");
	else if (status == 2)
		qm.sendNextPrev("그래... 그런데 넌 어째 잠을 제대로 자지 못한 얼굴이구나.\r\n어젯밤에 천둥하고 번개가 엄청나게 쳤지. 그래서 그런가?");
	else if (status == 3) 
		qm.PlayerToNpc("#b아뇨. 그게 아니라 간밤에 이상한 꿈을 꿔서요.#k");
	else if (status == 4)
		qm.sendNextPrev("이상한 꿈? 무슨 꿈인데 그러니?");
	else if (status == 5)
		qm.PlayerToNpc("#b그러니까...#k");
	else if (status == 6)
		qm.PlayerToNpc("#b(안개 속에서 드래곤을 만나는 꿈을 꿨다고 설명했다.)");
	else if (status == 7)
		qm.sendAcceptDecline("호호호호, 드래곤이라고? 그거 굉장한데? 잡아 먹히지 않아\r\n서 다행이구나. 재미있는 꿈이니 유타에게도 말해주렴. 분명\r\n좋아할 거야.");
	else if (status == 8){
		qm.forceStartQuest();
		qm.sendNext("#b유타#k는 불독에게 아침밥을 주러 #b앞마당#k에 나갔단다. 집 밖으\r\n로 나가면 바로 볼 수 있을 거야.");
   }else if (status == 9){
		qm.evanTutorial("UI/tutorial/evan/1/0", 1);
		qm.dispose();
	}
}

function end(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
		    qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("어, 일어났냐. 에반? 아침부터 눈 밑이 왜 그렇게 퀭해? 밤에\r\n못 잤어? 뭐? 이상한 꿈을 꿨다고? 무슨 꿈인데? 에엥? 드래\r\n곤이 나오는 꿈을 꿨단 말이야?");
	if (status == 1)
		qm.sendNextPrev("푸하하하하~ 드래곤이라고? 그거 굉장한데? 용꿈이잖아! 근\r\n데 혹시 그 꿈에 개는 한 마리 안 나왔냐? 하하하하~\r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 20 exp");
	if (status == 2){
		qm.gainExp(20);
		qm.evanTutorial("UI/tutorial/evan/2/0", 1);
		qm.forceCompleteQuest();
		qm.dispose();	
		}
	}
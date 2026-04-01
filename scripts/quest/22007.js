
var status = -1;

function end(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("싫어? 그럼 어쩔 수 없지 뭐. 부화기는 내가 가지고 있을게.");
		    qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("흠. 좋아. 부화기를 주지. 대신 일 하나만 해줘. 엄마가 달걀을 수거해 놓으라고 하셨는데 아직 안 했거든. 아~ 귀찮아서 말이야. 네가 달걀 수거를 해주면 부화기를 주도록 하지. 어때? 할 수 있겠어?");
	else if (status == 1) {
		qm.forceStartQuest();
                   qm.sendPrev("좋아. 그럼 바로 #b오른쪽에 있는 달걀통#k에게서 달걀을 가져와. 달걀통을 클릭하면 달걀을 얻을 수 있어. 너무 많이 가져오면 곤란하고 #b1개#k면 돼.");
         }else if (status == 2){
		qm.dispose();
}
	}

function end(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("음? 이상하네. 부화기 설치가 잘 안 됐어. 다시 한 번 시도해봐.");
		    qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("오, 달걀을 가져온 거야? 그럼 달걀을 건네줘. 그럼 부화기를 줄게.");
	if (status == 1)
qm.sendAcceptDecline("자, 받으라고. 도대체 어디에 쓰려는 건지는 모르겠지만...\r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 360 exp");
	if (status == 2){
		qm.forceCompleteQuest();
		qm.gainExp(360);
		if (qm.haveItem(4032451)) {
			qm.gainItem(4032451, -1);
		}
		qm.evanTutorial("UI/tutorial/evan/9/0" , 1);
		qm.dispose();
		}
	}
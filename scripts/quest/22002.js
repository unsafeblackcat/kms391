var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("어, 뭐야? 아침은 안 먹을 생각이야? 식사를 거르는 건 안 좋아. 먹을 생각이 들면 다시 말을 걸라고. 빨리 안 말하면 내가 먹어버릴거다?");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("#p1013102#에게 밥을 주고 온 거야? 그럼 에반 너도 아침밥 먹어. 오늘 아침밥은 #t2022620#야. 내가 가지고 나왔어. 히히. 사실은 #p1013102#에게 먹이주기를 안 도와주면 아침밥도 안 주려고 했는데.");
	else if (status == 1)
		qm.sendAcceptDecline("자, #b샌드위치#k를 줄 테니 #b다 먹으면 엄마에게 가봐#k. 뭔가 너한테 할 말이 있으시다는데?");
	else if (status == 2){
		qm.forceStartQuest();
		qm.PlayerToNpc("#b(할 말? 어쨌든 일단 #t2022620#부터 먹고 다시 집 안으로 들어가자.)#k");
		qm.gainItem(2022620, 1);
	}else if (status == 3){
		qm.evanTutorial("UI/tutorial/evan/3/0" , 1);
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
		qm.sendNext("아침밥은 잘 먹었니, 에반? 그럼 엄마 심부름 하나만 해주지 않으련?\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#i1003028#  #t1003028# 1개  \r\n#i2022621#  #t2022621# 5개 \r\n#i2022622#  #t2022622# 5개 \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 60 exp");
	 if (status == 1){
		qm.forceCompleteQuest();
		qm.evanTutorial("UI/tutorial/evan/4/0" , 1);
		qm.gainItem(1003028, 1);
		qm.gainItem(2022621, 5);
		qm.gainItem(2022622, 5);
		qm.gainExp(60);
		qm.dispose();
	}
}
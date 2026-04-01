var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("귀찮다고 심부름을 거절하면 못써요. 자, 착하지? 다시 한 번 말을 걸렴.");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendAcceptDecline("농장에 일하러 가신 #b아빠#k가 그만 도시락을 깜빡 잊고 가셨지 뭐니. 네가 #b농장 중심지#k에 계신 아빠에게 #b도시락을 배달#k해주렴. 착하지?");	
	else if (status == 1) {
		qm.forceStartQuest();
		qm.sendNext("후훗, 역시 에반은 착한 아이라니까~ 그럼 바로 #b집 밖으로 나가 왼쪽으로 쭈욱#k 가렴. 아마 무척 배가 고프실 테니 서둘러 주면 좋겠구나.");
	if(!qm.haveItem(4032448))
	qm.gainItem(4032448, 1);
	} else if (status == 2)
		qm.sendNextPrev("혹시 도시락을 잃어버리면 집으로 곧장 돌아오렴. 다시 싸 줄테니까.");
	 else if (status == 3){
		qm.evanTutorial("UI/tutorial/evan/5/0" , 1);
		qm.dispose();
	}
}

function end(mode, type, selection) {

}
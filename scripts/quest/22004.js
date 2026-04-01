var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("흠... 유타라면 이 정도 부탁은 들어줬을 텐데.");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("사실은 요새 농장의 돼지들이 좀 이상하단다. 괜히 화를 내거나 짜증을 부리는 일이 많아. 그게 걱정돼서 오늘도 일찍 나와봤는데, 아니나 다를까 돼지들 중 몇 마리가 밤새 울타리를 뚫고 밖으로 뛰쳐나간 모양이구나.");
	else if (status == 1)
		qm.sendAcceptDecline("돼지들을 찾아오기 전에 일단 망가진 울타리부터 고쳐놔야 하지 않겠니? 다행히 그렇게 많이 망가진 건 아니라 나무토막만 몇 개 있으면 고칠 수 있을 것 같구나. 에반이 #b나무토막을 3개#k만 구해다 주면 편할 텐데...");
	else if (status == 2){
		qm.forceStartQuest();
		qm.sendNext("오, 정말 고맙구나. #b나무토막#k은 주변에 있는 #r스텀프#k들에게 구할 수 있단다. 별로 강한 녀석들은 아니지만 혹시라도 방심하다 위험한 순간이 올 수도 있으니 스킬과 아이템을 잘 사용하도록 하렴.");
	}else if (status == 3){
		qm.evanTutorial("UI/tutorial/evan/6/0", 1);
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
			qm.sendNext("오, 나무토막은 다 구해온거니? 장하구나. 그럼 상으로 뭘주면 될까... 옳지, 그게 있었지. \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i3010097#  #t3010097# 1개 \r\n#i2022621#  #t2022621# 15개 \r\n#i2022622#  #t2022622# 15개 \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 210 exp");
	if (status == 1){
		qm.forceCompleteQuest();
		qm.gainItem(4032498, -3);
		qm.gainItem(3010097, 1);
		qm.gainItem(2022621, 15);
		qm.gainItem(2022622, 15);
		qm.gainExp(210);
		qm.sendNextPrev("자, 울타리를 만들고 남은 판자로 만든 새 의자란다. 모양은 그래도 튼튼해서 쓸만할 거야. 잘 쓰렴.");
		}
	if(status == 2){
		qm.evanTutorial("UI/tutorial/evan/7/0", 1);
		qm.dispose();
	}
}
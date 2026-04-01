
var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("너어, 귀찮다고 거절할래? 형이 꼭 개한테 물리는 걸 보고 싶\r\n냐? 얼른 다시 말을 걸어서 퀘스트를 수락해!");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("아침부터 한참 웃었네. 하하하. 자, 이상한 소리는 그만하고\r\n#p1013102#한테 아침밥이나 좀 줘.");
	else if (status == 1)
		qm.PlayerToNpc("#b엥? 그건 유타가 할 일이잖아?#k");
	else if (status == 2)
		qm.sendAcceptDecline("이 녀석! 형이라고 부르라니까!#p1013102# 이 나를 얼마나 싫어하는\r\n지는 너도 잘 알잖아. 다가가면 분명히 물리고 말 거야. 불독\r\n이 넌 좋아하니까 네가 가져다 줘.");
	else if (status == 3){
		qm.gainItem(4032447,1);
		qm.sendNext("얼른 #bleft#k으로 가서 #b#p1013102##k에게 사료를 주고 돌아와. 그 녀석\r\n아까부터 배가 고픈지 짖어대고 있어.");
		qm.forceStartQuest();
   }else if (status == 4){
		qm.sendPrev("#p1013102#에게 먹이를 준 다음에 빨리 돌아와.");
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
		qm.sendNext("#b(You place food in Bulldog's bowl.)#k");
	if (status == 1)
		qm.sendOk("#b(Bulldog is totally sweet. Utah is just a coward.)#k\r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 35 exp");
	if (status == 2){
		qm.forceCompleteQuest();
		qm.gainItem(4032447, -1);
		qm.gainExp(35);
		qm.sendOk("#b(Looks like Bulldog has finished eating. Return to Utah and let him know.)#k");
		qm.dispose();
		}
	}
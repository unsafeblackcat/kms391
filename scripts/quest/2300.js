/* ===========================================================
			Resonance
	NPC Name: 		Maple Administrator
	Description: 	Quest -  Kingdom of Mushroom in Danger
=============================================================
Version 1.0 - Script Done.(17/7/2010)
=============================================================
*/

var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			if(status == 0){
				qm.sendOk("정말요? 시간이 되신다면 다시 찾아와주세요. 긴급한 문제라고 하더군요.");
				qm.dispose();
				return;
			} else if(status == 3){
				qm.sendNext("Okay. In that case, I'll just give you the routes to the Kingdom of Mushroom. #bNear the west entrance of Henesys,#k you'll find an #bempty house#k. Enter the house, and turn left to enter#b<Themed Dungeon : Mushroom Castle>#k. That's the entrance to the Kingdom of Mushroom. There's not much time!");
				qm.forceStartQuest();
				return;
			}
		}
	}
	if(status == 0) 
		qm.sendAcceptDecline("이제 전직도 했고, 충분한 자격을 갖춘 것 같군. 자네에게 긴히 부탁 하고 싶은것이 있는데 도와줄 수 있겠나?");
	if(status == 1)
		qm.sendNext("다름이 아니라 #b머쉬킹 왕국#k에 지금 큰 위기가 닥쳤다네. 머쉬킹 왕국은 헤네시스 큰처에 위치한 버섯왕국으로써 선왕인 머쉬킹은 평화를 사랑하고 현명한 정치를 펼치는 분으로 명망이 높은 분이셨지. 그런데 최근에 건강이 악화되어 그의 하나 뿐인 딸 #b비올레타 공주#k에게 왕위를 물려주고 요양 중이라고 알고 있었는데 무슨 변고가 생긴 모양이네.");
	if(status == 2)
		qm.sendNext("무슨일인지 자세한 사정은 모르겠지만 큰 어려움이 닥친 것 같으니 한번 가서 사정을 들어보고 도와주는 게 좋을 것 같네. 자네 정도면 위기에 빠진 머쉬킹 왕국을 구해낼 수 있을 걸세. 자, 여기 #b추천서#k를 줄테니 어서 머쉬킹 왕국으로 가보게나. #b경호대장#k을 찾아가면 될걸세.\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v4032375# #t4032375#");
	if(status == 3)
		qm.sendYesNo("그런데, 자네 머쉬킹 왕국의 위치를 알고 있나? 혼자 찾아갈 수 있으면 상관 없지만, 괜찮다면 입구까지 보내주겠네.");
	if(status == 4){
		qm.gainItem(4032375, 1);
		qm.forceStartQuest();
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
	if(status == 0)
		qm.sendNext("Hmmm? Is that a #brecommendation letter from the job instructor#k??! What is this, are you the one that came to save us, the Kingdom of Mushroom?");
	if(status == 1)
		qm.sendNextPrev("Hmmm... okay. Since the letter is from the job instructor, I suppose you are really the one. I apologize for not introducing myself to you earlier. I'm the #bHead Security Officer#k in charge of protecting King Mush. As you can see, this temporary hideout is protected by the team of security and soldiers. Our situation may be dire, but nevertheless, welcome to Kingdom of Mushroom.");
	if(status == 2){
		qm.gainItem(4032375, -1);
		qm.forceCompleteQuest();
		qm.forceStartQuest(2312);
		qm.dispose();
	}
}

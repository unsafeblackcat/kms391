/*

	NPC Name: 		Nineheart
	Description: 		Quest - Do you know the black Magician?
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 8) {
	    qm.sendNext("Oh, do you still have some questions? Talk to me again and I'll explain it to you from the very beginning.");
	    qm.safeDispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("안녕하세요, #h0#씨. #p1101000# 기사단에 들어온 것을 환영합니다. 제 이름은 #p1101002# 폰 루비스타인. 어린 여제의 책사 역을 맡고 있습니다. 앞으로 자주 뵙게 될 테니, 이름 정도는 기억해 두 셔야 할 겁니다. 후훗...");
    } else if (status == 1) {
	qm.sendNextPrev("갑작스럽게 기사단의 일원이 되신 지라 아직은 모르는 게 더 많으\n\r 실 겁니다. 일단 하나씩 차근차근 설명 드리죠. 이곳은 어딘지 어\n\r 린 여제란 누구인지, 그리고 우리가 해야 할 일은 무엇인지...");
    } else if (status == 2) {
	qm.sendNextPrev("이 섬의 이름은 에레브. 어린 여제께서 다스리는 마법의 힘이 깃든\n\r 섬입니다. 지금도 하늘에 떠 있지요. 필요에 의해 이곳에 머물고\n\r 있지만 원래는 메이플 월드 전체를 돌아다니는 배 역활을 하고있\n\r 는 섬입니다.");
    } else if (status == 3) {
	qm.sendNextPrev("어린 여제는 말 그대로 황제, 서계를 다스리는 자입니다. 들어보지\n\r 못하셨다고요? 아아, 그럴 겁니다. 어린 여제는 군림하되 지배하\n\r 지는 않는 자. 에레브를 통해 메이플 월드를 떠돌며 바라보는 것만\n\r 이 그녀의 임무입니다. 평소라면 말입니다...");
    } else if (status == 4) {
	qm.sendNextPrev("하지만 상황은 변했습니다. 사악한 검은 마법사가 다시 깨어날 것\n\r 을 예고하는 움직임이 세계 곳곳에서 발견되고 있습니다. 오래 전\n\r 메이플 월드를 공포로 지배했던 멸망의 왕이 다시 한 번 나타나려\n\r 하고 있습니다.");
    } else if (status == 5) {
	qm.sendNextPrev("그러나 사람들은 그것을 모릅니다. 검은 마법사가 사라진지 이미\n\r 오래... 긴 세월 동안 위험을 잊은 사람들은 평화에 익숙해져 위험\n\r 을 느끼지 못하고 있습니다. 이대로라면 다시 한 번 메이플 월드는\n\r 검은 마법사의 손에 들어갈 겁니다.");
    } else if (status == 6) {
	qm.sendNextPrev("그렇기에 어린 여제께서 결심하셨습니다. 한 발 먼저 움직이기로...\n\r 검은 마법사가 깨어나기 전에 검은 마법사를 물리칠 기사들을 양\n\r 성하기로 말입니다. 이후의 일은 당신도 알고 계시겠죠? 기사가\n\r 되겠다고 말씀하셨으니 말입니다.");
    } else if (status == 7) {
	qm.sendNextPrev("우리가 해야 할 일은 간단합니다. 강해져서, 지금보다 훨씬 더 강\n\r 해져서 검은 마법사가 돌아왓을 때 그를 물리치는 것. 그가 메이플\n\r 월드를 멸망시키지 못하도록 막는 것. 그것이 우리의 목표이며 의\n\r 무이며, 당신께 주어질 임무입니다.");
    } else if (status == 8) {
	qm.askAcceptDecline("자, 설명은 여기까지입니다. 이해 하셨습니까? \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#fUI/UIWindow.img/QuestIcon/8/0# 380 exp");
    } else if (status == 9) {
	if (qm.getQuestStatus(20016) == 0) {
	    qm.gainExp(380);
	    qm.forceCompleteQuest();
	}
	qm.sendNext("이해하셨다니 다행입니다만... 알고 계십니까? 그 레벨로는 검은\r\n 마법사는 고사하고 그 밑에 수하, 아니 수하의 수하의 수하의 애\r\n 완동물도 퇴치할 수 없습니다? 이래가지고 메이플 월드를 지킬 수 있겠습니까?");
    } else if (status == 10) {
	qm.sendNextPrev("기사단의 일원이라고는 해도 아직 당신은 기사가 아닙니다. 정식\r\n 기사는 커녕 수련기사로도 임명 받지 못했는걸요. 시간이 흘러도\r\n 이대로라면 임무는 커녕 당신은 #p1101000# 기사단의 잡역부에 지나지 않을 겁니다...만.");
    } else if (status == 11) {
	qm.sendNextPrev("뭐, 처음부터 강한 자는 없는 법이니까요. 여제께서도 강한 자를\r\n 같은 편으로 하기보다는, 강한 자를 기르길 원하셨고... 일단 당신\r\n 은 좀 더 강해져서 수련기사부터 되는 게 좋겠습니다. 기사단원으\r\n 로써의 임무는 그 후에 이야기 하도록 하죠.");
    } else if (status == 12) {
	qm.sendPrev("왼편에 있는 포탈을 타고 쭉 가다 보면 수련의 숲이라는 곳에 갈\r\n 수 있을 겁니다. 그곳에 당신 같은 분을 위한 수련교관, #p1102000#가 기\r\n 다리고 있으니 그에게 가주세요. 레벨 10이 되기 전에는 서로 얼\r\n 굴 보지 않았으면 좋겠군요.");
	qm.safeDispose();
    }
}

function end(mode, type, selection) {
}
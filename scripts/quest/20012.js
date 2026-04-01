/*
	NPC Name: 		Kinu
	Description: 		Quest - Cygnus tutorial helper
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendNext("X");
	    qm.safeDispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("기다리고 있었습니다. #h0# 씨 제 이름은 #p1102006#. 형제 중 셋째 입니다. 일반공격에 대헤 배우셨다고요? 그럼 다음 단계는 당연히 짐작하고 계시겠죠? 바로 #b스킬#k입니다. 메이플 월드에서는 스킬공격이 아주 중요하답니다.");
    } else if (status == 1) {
	qm.sendNextPrev("레벨업을 하면 스킬 포인트를 얻을 수 있습니다. 당신에게도 어느\n\r 정도 스킬 포인트가 있을 텐데요, #bK 키#k를 눌러 스킬창에서 원하는\n\r 스킬에 적당히 스킬 포인트를 투자하여 스킬을 올려 보세요. 이렇\n\r 게 올린 #b스킬을 퀵슬롯에 올려 놓으면 훨씬 사용하기 편합니다.#k");
    } else if (status == 2) {
	qm.askAcceptDecline("자, 그럼 까먹기 전에 바로 실전연습입니다. 이 주변에는 #o100121#들이\n\r 많이 있는데요, #b달팽이 세마리 스킬을 사용해#k #r#o100121##k #r3마리#k를 사냥\n\r 한 후 그 증거로 #b#t4000483##k을 1개 가져와 주세요. 그럼 기다리겠\n\r 습니다.");
    } else if (status == 3) {
	qm.forceStartQuest();
	qm.summonMsg(8);
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
	qm.sendNext("#o100121#를 퇴치하고 #t4000483#을 가져오셨군요. 훌륭합니다. #b정식\n\r 으로 기사가 되면 레벨업을 할 때마다 스킬 포인트를 3씩#k 얻으실\n\r 수 있습니다. 그럼, 화살표를 따라 왼쪽으로 쭈욱 가면 다음 단계\n\r 를 담당하고 있는 #b#p1102007##k를 만나실 수 있을 겁니다. \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#fUI/UIWindow.img/QuestIcon/8/0# 40 exp");
    } else if (status == 1) {
	qm.gainItem(4000483, -1);
	qm.forceCompleteQuest();
	qm.gainExp(40);
	qm.dispose();
    }
}
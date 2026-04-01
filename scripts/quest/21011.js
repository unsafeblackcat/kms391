/*
 *
 * BTF 김민권(st_world@naver.com)
 * 사설 서버 & 판매용 목적 스크립트
 * 아란 튜토리얼 한글화
 *
 */

function start(mode, type, selection) {
    qm.dispose();
}
var status = -1;


function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 4) {
	    qm.sendNext("*sniff sniff* Isn''t this sword good enough for you, just for now? I''d be so honored...");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	if (qm.getQuestStatus(21011) == 0) {
	    qm.forceStartQuest();
	    qm.dispose();
	    return;
	}
	qm.sendNext("리, 리린님과 함께 계신 당신은 설마... 설마... 역시 영웅이신\n\r 가요? 리린님! 귀찮다는 듯이 고개만 끄덕이지 말고 확실히\n\r 말해주세요! 이분이 바로 영웅?!");
    } else if (status == 1) {
	qm.sendNextPrev("   #i4001171#");
    } else if (status == 2) {
	qm.sendNextPrev("..죄송합니다. 감동해서 그만 소리를 지르고 말았네요. 훌쩍.\n\r 하지만 정말 감동해서.. 아, 눈물이 나려고해.. 리린님께\n\r 서도 정말 기쁘시겠군요.");
    } else if (status == 3) {
	qm.sendNextPrev("그런데.. 영웅님께서는 무기를 안 들고 계시는군요. 영웅들\n\r 은 자신만의 무기가 있던 걸로 아는데.. 아, 검은 마법사와 싸우는\n\r 동안 잃어버리신 모양이군요.");
    } else if (status == 4) {
	qm.sendYesNo("대신이라고 하긴 너무 초라하지만, #일단 이 검이라도 들고 다\n\r 녀 주세요#k. 영웅님께 드리는 선물이예요. 영웅이 빈 손으로\n\r 다니는 건 너무 이상하니까요. \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i1302000# #t1302000# \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 35 exp");
    } else if (status == 5) {
	if (qm.getQuestStatus(21011) == 1) {
	    qm.gainItem(1302000, 1);
	    qm.gainExp(35);
	}
	qm.forceCompleteQuest();
	qm.sendNextPrevS("#b(스킬도 전혀 영웅답지 않았는데.. 검마저 매우 낮설다. 과거\n\r 의 난 검을 사용해 본 적이 있기는 한 걸까? 검은 어떻게 착\n\r 용하는 거지?)#k", 3);
    } else if (status == 6) {
	qm.summonMsg(16); // How to equip shiet
	qm.dispose();
    }
}
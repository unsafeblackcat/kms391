/*
 *
 * BTF 김민권(st_world@naver.com)
 * 사설 서버 & 판매용 목적 스크립트
 * 아란 튜토리얼 한글화
 *
 */

var status = -1;
function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 1) {
	    qm.sendNext("I'm sure it will come in handy during your journey. Please, don't decline my offer.");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendSimple("여, 영웅님.. 저, 정말 뵙고 싶었어요.  \r\n#b#L0#(수줍어하는 것 같다.)#l");
    } else if (status == 1) {
	qm.askAcceptDecline("저어, 아주 예전부터 영웅님을 만나면 선물하고 싶은 게 있었\n\r 는데.. 마을로 가느라 바쁘신 건 알지만.. 제 마음의 선물을\n\r 을 받아주시겠어요?");
    } else if (status == 2) {
	qm.forceStartQuest();
	qm.sendNextS("선물의 재료는 이 주변에 있는 상자 안에 넣어 두었어요. 번\n\r 거러우시겠지만 상자를 부순 후 그 안에서 재료인 #b#t4032309##k과 #b#t4032310##k를 가져다 주세여. 그럼 바로 조립해서 드릴게요.", 1);
    } else if (status == 3) {
	qm.summonMsg(18);
	qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    qm.sendNext("What? You don't want the potion?");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("재료를 모두 가져오셨군요? 그럼 잠시만요, 이렇게 저렇게\n\r 조립을 하면.. \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i3010062# #t3010062# \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 95 exp");
    } else if (status == 1) {
	if (qm.getQuestStatus(21013) == 1) {
	    qm.gainItem(3010062, 1);
	    qm.gainExp(95);
	    qm.forceCompleteQuest();
	}
	qm.sendNextPrevS("자, 의자 완성이에요! 헤헤, 아무리 영웅이라도 피곤할 때가\n\r 있을 거라고 생각해서 예전부터 영웅님에게 의자를 선물로\n\r 드리고 싶었어요.", 1);
    } else if (status == 2) {
	qm.sendNextPrevS("영웅이라고 해서 언제나 강할 수 있는 건 아니라고 생각해요.\n\r 영웅도 분명 지치고 힘들 때도 있고, 약해질 때도 있을 거예\n\r 요. 하지만 그것을 극복할 수 있기에 영웅이라는 거겠죠?", 1);
    } else if (status == 3) {
	qm.summonMsg(19);
	qm.dispose();
    }
}
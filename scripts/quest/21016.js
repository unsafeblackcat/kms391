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
	if (status == 0) {
	    qm.sendNext("Are you not ready to hunt the #o0100132#s yet? Always proceed if and only if you are fully ready. There''s nothing worse than engaging in battles without sufficient preparation.");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.askAcceptDecline("그럼 계속해서 기초 체력 단련을 할까요? 준비는 되셨어요?\n\r 검은 제대로 장비 하셨는지 스킬과 물약은 퀵슬롯에 올려 놓\n\r 였는지 다시 한 번 확인하신 후 수락해 주세요.");
    } else if (status == 1) {
	qm.forceStartQuest();
	qm.AranTutInstructionalBubble("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow3");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.dispose();
}

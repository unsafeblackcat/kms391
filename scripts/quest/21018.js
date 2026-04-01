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
	    qm.sendNext("Oh, is 5 not enough? If you feel the need to train further, please feel free to slay more than that. If you slay all of them, I''ll just have to look the other way even if it breaks my heart, since they will have been sacrificed for a good cause...");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("자, 그럼 이때까지 기초 체력을 한 번 테스트 해보도록 할게\n\r 요. 방법은 간단해요 이 섬에서 가장 강하고 흉폭한 몬스터.\n\r #o0100134#을 퇴치하면 돼요 한#r50마리#k 퇴치해 주시면 좋겠지만..");
    } else if (status == 1) {
	qm.askAcceptDecline("몇 마리 없는 무루쿤을 다 퇴치해 버리는 건 생태계에 좋지\n\r 않은 것 같으니 5마리만 퇴치하도록 할게요. 자연과 환경을\n\r 생각하는 단련! 아, 아름답기도 해라..");
    } else if (status == 2) {
	qm.forceStartQuest();
	qm.AranTutInstructionalBubble("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow1");
	qm.dispose();
    }
}
function end(mode, type, selection) {
    qm.dispose();
}

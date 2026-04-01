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
	    qm.sendNext("No, Aran... There's no point of leaving here if we're to just leave the kid here all by himself. I know it's a lot to ask... but please reconsider!");
	    qm.dispose();
	    return;
	}
	status--
    }
    if (status == 0) {
	qm.askAcceptDecline("이런! 아이 한 명이 숲에 남겨진 모양이예요! 아이를 두고 우리들만 도망칠 수는 없어요! 아란 .. 미안하지만 아이를 구하러 가 주세요 ! 부상까지 입은 당신께 이런 말을 해서는 안 되는 걸 알고 있지만 .. 당신밖에 없어요.");
    } else if (status == 1) {
	qm.forceStartQuest(21000, "..w?PGAE."); // Idk what data lol..
	qm.forceStartQuest(21000, "..w?PGAE."); // Twice, intended..
	qm.sendNext("#b아이는 아마도 숲 깊은 곳에 있을거예요#k! 검은 마법사가 이곳을 알아내기 전에 방주를 출발시켜야 하니 서둘러서 아이를 데려와 주셔야 해요!");
    } else if (status == 1) {
	qm.sendNextPrev("The most important thing right now is not to panic, Aran. If you want to see how far you've gone with your quest, press #bQ#k to open the quest window.");
    } else if (status == 2) {
	qm.sendNextPrev("부탁해요, 아란! 아이를 구해 주세요! 더이상 검은 마법사에게 누구도 희생시키고 싶지 않아요!");
    } else if (status == 3) {
	qm.AranTutInstructionalBubble("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow1");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.dispose();
}
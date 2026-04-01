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
	if (status == 5) {
	    qm.sendNextS("#b(You declined out of fear, but it''s not like you can run away like this. Take a big breath, calm down, and try again.)#k", 2);
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNextS("이제 얼추 몸이 풀리신 것 같네요. 이럴 때야말로 더욱 혹독\n\r 하게 단련을 해줘야 완벽한 기초 체력을 갖게 되는 법. 자, 그\n\r 럼 계속해서 기초 체력 단련을 해보죠.", 8);
    } else if (status == 1) {
	qm.sendNextPrevS("그럼 이번에는 #r#o0100133#s#k에 가서 #b#m140020200##k를 퇴치해\n\r 보죠. 한.. #r20마리#k 정도 퇴치하면 체력 단련에 도움이 될 거예\n\r 요. 자, 어서 가서.. 응? 뭔가 하시고 싶은 말씀이라도 있으\n\r 신가요?", 8);
    } else if (status == 2) {
	qm.sendNextPrevS("..왠지 점점 숫자가 늘어나고 있지 않아?", 2);
    } else if (status == 3) {
	qm.sendNextPrevS("늘어나고 있어요. 어머, 혹시 20마리로는 부족하신 건가요?\n\r 그럼 한 100마리쯤 퇴치해 볼까요? 아니, 아니지. 이왕 수련\n\r 하는 건데 슬리피우드의 누구처럼 999마리 잡기 정도는..", 8);
    } else if (status == 4) {
	qm.sendNextPrevS("아, 아니. 이대로도 충분하다.", 2);
    } else if (status == 5) {
	qm.askAcceptDecline("어머어머, 그렇게 사양하실 거 없어요. 빨리 강해지고 싶으신\n\r 영웅님의 마음이라면 충★분★히★ 알고 있는 걸요. 역시 영\n\r 웅님은 대단하시다니..");
    } else if (status == 6) {
	qm.forceStartQuest();
	qm.sendNextS('#b(더 이상 듣고 있다가는 정말 999마리 퇴치를 하게 될 것 같아 수\n\r 락해 버렸다..)#k', 2);
    } else if (status == 7) {
	qm.sendNextPrevS('그럼 #o0100133# 20마리 퇴치를 부탁 드릴게요.', 8);
    } else if (status == 8) {
	qm.AranTutInstructionalBubble("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow3");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.dispose();
}

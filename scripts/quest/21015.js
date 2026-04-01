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
	if (status == 2) {
	    qm.sendNext("What are you so hesitant about? You're a hero! You gotta strike while the iron is hot! Come on, let''s do this!");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("자, 그럼 이제 설명은 여기까지로 해 두고 다음 단계로 넘어\n\r 가죠, 다음 단계가 뭐냐고요? 방금 말씀 드렸잖아요. 검은 마\n\r 법사를 한 방에 해치울 수 있을 정도로강해질 때까지 수련하\n\r 는 거예요.");
    } else if (status == 1) {
	qm.sendNextPrev("당신이 과거에 영웅이었던 건 확실하지만 그거야 수백 년 전\n\r 애기, 검은 마법사의 저주가 아니래도. 얼음 속에 그렇게 오래\n\r 있었으니 몸이 굳었을 게 당연하잖아요? 일단은 그 굳은 몸\n\r 부터 풀어봐야겠어요. 어떻게 하느냐고요?");
    } else if (status == 2) {
	qm.askAcceptDecline("체력이 국력이다! 영웅도 기초 체력부터! ..라는 말도 모르세\n\r 요? 당연히 #b기초 체력 단련#k을 해야죠 ..아, 기억을잃었으니\n\r 모르시겠군요. 뭐 모르셔도 괜찮아요. 직접 해보시면 알 테\n\r 니까. 그럼 바로 기초 체력 단련에 들어갈까요?");
    } else if (status == 3) {
	qm.forceStartQuest();
	qm.AranTutInstructionalBubble("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow3");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.dispose();
}
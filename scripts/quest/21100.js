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
	if (status == 6) {
	    qm.sendNext("Oh, is 5 not enough? If you feel the need to train further, please feel free to slay more than that. If you slay all of them, I''ll just have to look the other way even if it breaks my heart, since they will have been sacrificed for a good cause...");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNextS("검은 마법사와 싸운 영웅들.. 그들에 대한 정보는 거의 남아\n\r 있지 않아요. 예언서에도 겨우 영웅이 다섯 명이라는 기록만\n\r 있을뿐, 외모에 관한 단서는 전혀 없어요. 혹시 뭔가 기억나\n\r 는 건 없으신가요?", 8);
    } else if (status == 1) {
	qm.sendNextPrevS("기억나는 것은 전혀 없는데..", 2);
    } else if (status == 2) {
	qm.sendNextPrevS("역시 그렇군요. 하긴 검은 마법사의 저주가 그리 호락호락할\n\r 리 없죠. 하지만 그렇다 해도 당신이 영웅이 확실한 이상\n\r 거의 접점이 어딘가 있을 것 같은데. 뭐가 있을까요? 전투\n\r 때문인지 무기나 옷도 모두 잃어버리셧고. 아, 그렇지 #b무기!#k", 8);
    } else if (status == 3) {
	qm.sendNextPrevS("무기?", 2);
    } else if (status == 4) {
	qm.sendNextPrevS("예전에 얼음 속에서 영웅들을 발굴하다가 굉장한 무기를 발\n\r 견했어요. 아마도 영웅이 사용하던 것으로 추정되어서 마을\n\r 중앙에 가져다 놓았죠. 지나가다가 보지 않으셨나요?\n\r #b아주 #p1201001##k을... \r\r#i4032372#\r\r이렇게 생긴 건데..", 8);
    } else if (status == 5) {
	qm.sendNextPrevS("그러고 보니 이상할 정도로 거대한 폴암이 마을에 있었지.", 2);
    } else if (status == 6) {
	qm.askAcceptDecline("네, 바로 그거예요. 기록에 의하면 영웅의 무기는 주인을 알\n\r 아본다고 해요. 만약 당신이 #p1201001#을 사용하던 영웅이\n\r 라면, #p1201001#을 잡았을때 뭔가 반응이 올 거예요. 어서\n\r 가서 #b#p1201001#을 클릭해 주세요.#k");
    } else if (status == 7) {
	if (qm.getQuestStatus(21100) == 0) {
	    qm.forceCompleteQuest();
	}
	qm.showWZEffect("Effect/Direction1.img/aranTutorial/ClickPoleArm");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.dispose();
}

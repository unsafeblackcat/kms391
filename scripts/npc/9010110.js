


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	제롱 에 의해 만들어 졌습니다.

	엔피시아이디 : 9010110

	엔피시 이름 : 메이플 운영자

	엔피시가 있는 맵 : 진실의 방 : 진실의 방 (993073000)

	엔피시 설명 : MISSINGNO


*/

var status = -1;
var sel = 0;
function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.sendSimpleS("#e#h ##n님의 #b진실의 방#k 누적 입장 횟수는 "+parseInt(cm.getClient().getKeyValue("LIE_DETECTOR_WARPCOUNT"))+"회입니다."
        + "\r\n\r\n#e#h ##n님의 #b진실의 방#k에 남은 시간은 " + cm.getClient().getKeyValue("LIE_DETECTOR_WARP") +"입니다."
       + "\r\n\r\n#L0##b진실의 방#k에 대한 설명을 듣는다."
       + "\r\n#L1#제가 #b진실의 방#k으로 온 이유는 무엇인가요?"
       + "\r\n#L2#제가 #b진실의 방#k을 나가고 싶습니다."  , 0x04, 9010110);
    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            cm.sendNext("#b진실의 방#k은 #r거짓말 탐지기에 오답을 입력#k하여 매크로 사용\r\n의심 캐릭터로 적발되면 이동하는 공간입니다.");
        } else if (sel == 1) {
            cm.sendSimpleS("#r거짓말 탐지기#k에 오답을 입력하셨네요.\r\n\r\n#L0##r거짓말 탐지기#k에 대해 자세한 설명을 듣는다.", 0x04, 9010110)
        } else if (sel == 2) {
            if (cm.getClient().getKeyValue("LIE_DETECTOR_WARP") <= 1) {
            cm.warp(1001);
            } else {
             cm.sendOk("아직 #b진실의 방#k에서 나가실 수 없습니다. #r거짓말 탐지기#k에 주의해주세요.");
             cm.dispose();
            }
        }
    } else if (status == 2) {
        if (sel == 0) {
            cm.sendNextPrev("#b진실의 방#k에 들어온 계정은 화면 상단 타이머에 표시되는\r\n#r남은 시간#k만큼 게임 접속을 유지해야 나갈 수 있습니다.");
        } else if (sel == 1) {
            cm.sendNext("#r거짓말 탐지기#k는 매크로 사용 의심 캐릭터에게 다른 유저들이 아이템을 사용하여 발동시키거나 자동으로 발동되는 매크로 탐지 시스템입니다.");
        }
    } else if (status == 3) {
        if (sel == 0) {
            cm.sendNextPrev("#b진실의 방#k 안에서는 아래 사항이 제한됩니다.\r\n\r\n#r- 이동 및 스킬 사용\r - 채팅\r - 아이템 사용\r - 캐시샵/메이플 옥션/농장 이동\r - 프리미엄 PC방 접속 이벤트 참여\r - 게임 내 컨텐츠 및 이벤트 참여#k");
        } else if (sel == 1) {
            cm.sendNextPrev("#i03801369#\r\n그림과 같이 #r거짓말 탐지기#k가 발동되면 제한 시간 내에 화면에 보이는 문구를 정확하게 입력하셔야 합니다.");
        }
    } else if (status == 4) {
        if (sel == 0) {
            cm.sendOk("앞으로 #b진실의 방#k에 오지 않도록 #r거짓말 탐지기#k에 주의해주세요.");
            cm.dispose();
        } else if (sel == 1) {
            cm.sendNextPrev("정답 입력 기화는 총 #fc0xFF191919##e4회#n#k 주어지니 주어진 시간 안에 정답을 입력할 수 있도록 집중해서 보셔야겠죠?");
        }
    } else if (status == 4) {
        if (sel == 1) {
            cm.sendNextPrev("위 내용을 숙지하신 후 앞으로는 #b진실의 방#k에 들어오지 않도록 주의해주세요.");
            cm.dispose();
        }
    }
}

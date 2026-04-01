


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	화이트 에 의해 만들어 졌습니다.

	엔피시아이디 : 9091002

	엔피시 이름 : 게이트

	엔피시가 있는 맵 :  :  (401060100)

	엔피시 설명 : MISSINGNO


*/

var status = -1;

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
        cm.sendNextS("이곳은 위험하다, 왔던 곳으로 되돌아가자.", 0x26, 3003561);
    } else if (status == 1) {
        cm.warp(450007240, 0);
        cm.dispose();
    }
}

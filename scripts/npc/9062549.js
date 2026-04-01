


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	히어로 에 의해 만들어 졌습니다.

	엔피시아이디 : 9062549

	엔피시 이름 : 매니저 프리토

	엔피시가 있는 맵 : 메이플 LIVE : LIVE 스튜디오 (993194000)

	엔피시 설명 : 메이플 ON AIR


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
        cm.sendNextS("인기 최고의 사냥 방송 크리에이터가 되고 싶어?", 4);
        cm.dispose();
        return;
    }
}

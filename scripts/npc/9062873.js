


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	GM루나 에 의해 만들어 졌습니다.

	엔피시아이디 : 9062873

	엔피시 이름 : 플라나

	엔피시가 있는 맵 : 메이프릴 아일랜드 : 꿈이 모이는 곳 (993225700)

	엔피시 설명 : 전투력 측정


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
        cm.sendOk("현재 준비중 입니다.");
        cm.dispose();
        return;
    }
}




/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	GM프레이 에 의해 만들어 졌습니다.

	엔피시아이디 : 9062879

	엔피시 이름 : 돌의 정령 동상

	엔피시가 있는 맵 : 프레이 월드 : 프레이 광장 (993225700)

	엔피시 설명 : 메소 상점


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
        cm.openShop(20);
        cm.dispose();
    }
}

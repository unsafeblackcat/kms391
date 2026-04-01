


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	칼라 에 의해 만들어 졌습니다.

	엔피시아이디 : 9063187

	엔피시 이름 : 골드리치

	엔피시가 있는 맵 : Fancy Festival : 행복한 꿈의 공간 (993236200)

	엔피시 설명 : 보스결정석 매입


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
        cm.sendOk("我還沒寫入任何功能。~");
        cm.dispose();
        return;
    }
}

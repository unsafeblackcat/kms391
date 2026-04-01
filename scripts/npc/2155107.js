


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	캐논슈터 에 의해 만들어 졌습니다.

	엔피시아이디 : 2155107

	엔피시 이름 : 정찰중인 로봇

	엔피시가 있는 맵 : 스카이라인 : 스카이라인1 (310070210)

	엔피시 설명 : 스카이라인 정찰병


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
        cm.sendOk("정찰중, 이상 무.");
        cm.dispose();
        return;
    }
}

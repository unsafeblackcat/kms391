


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	히이이잉 에 의해 만들어 졌습니다.

	엔피시아이디 : 1540011

	엔피시 이름 : 시에라 그레이스

	엔피시가 있는 맵 : 헤네시스 : 헤네시스 (100000000)

	엔피시 설명 : 스타플래닛의 스타


*/
importPackage(Packages.handling.channel.handler);

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
        talk = "로고\r\n";
        talk += "#L0# 캐시샵#l";
        cm.sendSimple(talk);
    } else if (status == 1) {
        if (selection == 0) {
            cm.dispose();
            InterServerHandler.EnterCS(cm.getClient(), cm.getPlayer(), false);
        }
    }
}




/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	캐논슈터 에 의해 만들어 졌습니다.

	엔피시아이디 : 9062528

	엔피시 이름 : 두나

	엔피시가 있는 맵 : 블루밍 포레스트 : 꽃 피는 숲 (993192000)

	엔피시 설명 : 푸른 꽃의 정령


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
        cm.sendOkS("히히! #b#e물을 쏴아아#n#k 뿌려주면 꽃씨가 잘 자랄 거야!", 4, 9062528);
        cm.dispose();
        return;
    }
}

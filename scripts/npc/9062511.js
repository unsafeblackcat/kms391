


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    신궁 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062511

    엔피시 이름 : 푸른 꽃

    엔피시가 있는 맵 : 블루밍 포레스트 : 꽃 피는 숲 (993192000)

    엔피시 설명 : MISSINGNO


*/

var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getKeyValue(501367, "bloom") > 5) {
            cm.sendOkS("푸른 꽃이 금방이라도 피어날 것처럼 빛나고 있다. \r\n\r\n#r ※ '[블루밍] 푸른색 꽃봉오리' 퀘스트를 시작할 수 있습니다.", 2);
            cm.dispose();
        } else {
            cm.sendOkS("#b#e시원한 힘#n#k이 느껴지는 푸른 꽃이다.", 2);
            cm.dispose();
        }
        return;
    }
}

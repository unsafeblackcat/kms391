


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	슈피겔만 에 의해 만들어 졌습니다.

	엔피시아이디 : 3003811

	엔피시 이름 : 로렐라이

	엔피시가 있는 맵 : The Black : Night Festival (100000051)

	엔피시 설명 : 운명의 파편 지급


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
        말 = "#fn돋움#보스초기화권을 교환하고싶어서 찾아왔어? \r\n\r\n"
        말 += "#fn돋움#재료1:#i2430029##z2430029# 10개\r\n"
        말 += "#fn맑은고딕#재료2:#i4001716##z4001716# 1개\r\n"
        말 += "\r\n"
        말 += "#i2430030##z2430030# 1개로 교환하시겠어요?\r\n"
        cm.sendYesNo(말);
    } else if (status == 1) {
        if (!cm.haveItem(2430029, 10) || !cm.haveItem(4001716, 1)) {
            cm.sendOk("#fn돋움##z2430030#을 교환하기 위한 재료가 부족해요.");
            cm.dispose();
            return;
        }
        cm.gainItem(2430029, -10);
        cm.gainItem(4001716, -1);
        cm.gainItem(2430030, 1);
        cm.sendOk("#fn돋움##i2430030##z2430030# 아이템으로 교환했어요.");
        cm.dispose();
    }
}

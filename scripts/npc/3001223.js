


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	GM루나 에 의해 만들어 졌습니다.

	엔피시아이디 : 3001223

	엔피시 이름 : 맹글로브

	엔피시가 있는 맵 : 빅토리아로드 : 엘리니아 (180000007)

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
	//cm.getClient().setKeyValue("firstSymbol", "0");
        cm.sendOk(cm.getClient().getKeyValue("firstSymbol") + " 입니다.");
        cm.dispose();
        return;
    }
}

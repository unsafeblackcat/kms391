


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	LiangXS 에 의해 만들어 졌습니다.

	엔피시아이디 : 1033111

	엔피시 이름 : 知識的殿堂

	엔피시가 있는 맵 : 精靈之林 : 櫻花處 (101050000)

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
        cm.sendOk("#fs15#是來打敗我的勇士們瑪…\r\n難道是對抗黑魔法師的人瑪…\r\n不管是娜一方都沒關係了。\r\n如果彼此的目的明確的話，就不用多說了…… \r\n來巴。 愚蠢的家藿們…");
        cm.dispose();
        return;
    }
}

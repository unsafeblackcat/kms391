


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	캐논슈터 에 의해 만들어 졌습니다.

	엔피시아이디 : 2155116

	엔피시 이름 : 부서진 감시탑

	엔피시가 있는 맵 : 스카이라인 : 스카이라인 가장자리 (310070220)

	엔피시 설명 : MISSINGNO


*/

var status = -1;
var item = 4034284;
var count = 50;
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
        if (cm.haveItem(item, count)) {
        cm.sendYesNo("수리에 필요한 재료가 충분하다.\r\n#r[수리 시도]#k\r\n필요 재료 : #i"+item+"# #b#t"+item+"# "+count+"개 이상#k\r\n\r\n지금 수리할까?");
        } else {
            cm.sendOkS("수리에 필요한 재료가 부족하다..\r\n#r추적자 안드로이드 레드#k를 처치한 후 #i"+item+"# #b#z"+item+"# "+count+"개#k를 모아오자.", 2);
            cm.dispose();
        }
    } else if (status == 1) {
        cm.sendOk("재료들을 적절히 활용해 말끔하게 수리했다.\r\n이 재료들을 그대로 가지고\r\n#b정찰중인 로봇에게 돌아가 알려주자.#k");
        cm.getPlayer().setKeyValue(39116, "success", "1");
        cm.dispose();
    }
}

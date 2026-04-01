


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    나로 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062551

    엔피시 이름 : 슈피겔라

    엔피시가 있는 맵 : 메이플 LIVE : 탕윤 식당 퇴장 (993194401)

    엔피시 설명 : 메이플 버라이어티


*/

var status = -1;
var clear = true;

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
        if (cm.getPlayer().getV("ClearTyKitchen") == null) {
            clear = false;
        } else {
            if (parseInt(cm.getPlayer().getV("ClearTyKitchen")) != 1) {
                clear = false;
            }
        }
        if (clear) {
            cm.sendNextNoESC("\r\n#b#e보상#n#k #r#e#i4310012#, 100개\r\n#i4310013# 1개\r\n#i4310025# 25개#k 를 드릴게요!\r\n(보상은 퇴장 시 지급됩니다)" + cm.getPlayer().getKeyValue(100863, "state"));
        } else {
            cm.sendNextNoESC("\r\n#e#r50,000 포인트#n#k을 벌지 못하고 나오셨다면\r\n보상을 지급해드릴 수 없답니다.\r\n\r\n다음엔 더 노력해주세요!\r\n\r\n#b※ 게임 도중 이탈한 경우, 보상을 지급 받을 수 없습니다.#k");
        }
    } else if (status == 1) {
        if (clear) {
            cm.getPlayer().removeV("ClearTyKitchen");
            if (cm.getPlayer().getV("TyKitchenReward") == null) {
                cm.getPlayer().addKV("TyKitchenReward", "0")
            }
            cm.getPlayer().addKV("TyKitchenReward", (parseInt(cm.getPlayer().getV("TyKitchenReward")) + 1) + "");
            if (parseInt(cm.getPlayer().getV("TyKitchenReward")) <= 2) {
                //보상지급해주셈
                cm.getPlayer().gainCabinetItem(4310012, 100);
                cm.getPlayer().gainCabinetItem(4310013, 1);
                cm.getPlayer().gainCabinetItem(4310025, 25);
            }
            if (cm.getPlayer().getKeyValue(100863, "state") < 2) {
                if (cm.getPlayer().getKeyValue(100863, "count") < 0) {
                    cm.getPlayer().setKeyValue(100863, "count", "0");
                }
                cm.getPlayer().setKeyValue(100863, "count", (cm.getPlayer().getKeyValue(100863, "count") + 1) + "");
            }
            if (cm.getClient().getCustomKeyValue(501555, "count") < 0) {
                cm.getClient().setCustomKeyValue(501555, "count", "0");
            }
            cm.getClient().setCustomKeyValue(501555, "count", (cm.getClient().getCustomKeyValue(501555, "count") + 1) + "");
        }
        cm.warp(993194000);
        cm.dispose();
    }
}

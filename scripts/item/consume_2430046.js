


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    ★ 에 의해 만들어 졌습니다.

    엔피시아이디 : 9000210

    엔피시 이름 : 병아리

    엔피시가 있는 맵 : 헤네시스 : 헤네시스 (100000000)

    엔피시 설명 : MISSINGNO


*/


노랑 = "#fc0xFFFFBB00#"
var status = -1;

importPackage(Packages.constants);

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
        if (cm.getPlayer().getLevel() > 200) {
            cm.sendOkS("#fs15#當前角色等級超過200級無法使用.", 0x04, 9000030);
            cm.dispose();
            return;
        }
        if (GameConstants.isYeti(cm.getPlayer().getJob()) || GameConstants.isPinkBean(cm.getPlayer().getJob())) {
            cm.sendOkS("#fs15#耶蒂職業群和粉色空職業群無法使用。.", 0x04, 9000030);
            cm.dispose();
            return;
        }
        cm.sendYesNoS("#fs15#真的要用當前角色使用 " + 노랑 + "#i2430046##z2430046##k 道具?", 0x04, 9000030);
        return;
    } else if (status == 1) {
        for (var i = cm.getPlayer().getLevel(); i < 200; i++)
            cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
        cm.gainItem(2430046, -1);
        cm.dispose();
    } else {
        cm.dispose();
    }
}

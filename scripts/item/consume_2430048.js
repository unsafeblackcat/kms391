


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
            cm.sendOkS("#fs15#현재 캐릭터는 200레벨이 넘어 사용이 불가능합니다.", 0x04, 9000030);
            cm.dispose();
            return;
        }
        cm.sendYesNoS("#fs15#정말 현재 캐릭터로 " + 노랑 + "#i2430048##z2430048##k 아이템을 사용하시겠습니까?", 0x04, 9000030);
        return;
    } else if (status == 1) {
        for (var i = cm.getPlayer().getLevel(); i < 200; i++)
            cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
        cm.gainItem(2430048, -1);
        cm.dispose();
    } else {
        cm.dispose();
    }
}

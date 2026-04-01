importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    블랙 에 의해 만들어 졌습니다.

    엔피시아이디 : 9110007

    엔피시 이름 : 로보

    엔피시가 있는 맵 : 몬스터파크 : 몬스터파크 (951000000)

    엔피시 설명 : 라면 요리사


*/

var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}
var sel = -1;
var a = 0;
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
        cm.getClient().send(CField.UIPacket.getDirectionStatus(true));
        cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, true, false, false));
        statusplus(2000);
    } else if (status == 1) {
        cm.sendJobIlust(281, false);
    } else if (status == 2) {
        sel = selection;
        if (selection == 0) {
            var sex = cm.getPlayer().getGender();
            cm.getPlayer().setHair(sex == 0 ? 36460 : 37450);
            cm.getPlayer().setFace(sex == 0 ? 20284 : 21280);
            cm.getPlayer().updateSingleStat(MapleStat.HAIR, cm.getPlayer().getHair());
            cm.getPlayer().updateSingleStat(MapleStat.FACE, cm.getPlayer().getFace());
            cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/Direction6.img/effect/tuto/avenger/" + sex, 2, 2820, cm.getPlayer().getPosition().x, 0, 0, 1, 0, 1, 0, 0));
            cm.addEquip(-5, sex == 0 ? 1050249 : 1051305, 0, 0, 0, 0, 0, 0);
            cm.addEquip(-7, sex == 0 ? 1070029 : 1071046, 0, 0, 0, 0, 0, 0);
            cm.addEquip(-9, 1102505, 0, 0, 0, 0, 0, 0);
            cm.addEquip(-10, 1099000, 0, 0, 0, 0, 0, 0);
            cm.addEquip(-11, 1232000, 0, 0, 0, 0, 0, 0);
            cm.getPlayer().setAutoJob(3120);
            cm.teachSkill(30010241, 1, 2);
            statusplus(2820);
        } else {
            cm.teachSkill(30010112, 1, 2);
            cm.addEquip(-10, 1099000, 0, 0, 0, 0, 0, 0);
            cm.getPlayer().setAutoJob(3110);
            statusplus(100);
        }
    } else if (status == 3) {
        var txt = "#fs28##fn나눔고딕 Extrabold#";
        cm.getClient().send(CField.UIPacket.getDirectionStatus(true));
        cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
        cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 1000, 0));
        if (Randomizer.isSuccess(25)) {
            txt += "근거 없는 #r유언비어 유포, 서버 비하 발언, 운영자 모욕#k 등은 제재 당하실 수 있습니다."
        } else if (Randomizer.isSuccess(25)) {
            txt += "서버 내 #r비인가 프로그램#k 사용 시, 제재 당하실 수 있습니다."
        } else if (Randomizer.isSuccess(25)) {
            txt += "서버 내 #r대리 행위#k는 금지되어 있으며, 적발 시 제재 처리 됩니다."
        } else if (Randomizer.isSuccess(25)) {
            txt += "#fc0xFF003399#블랙#k 페스티벌에선 #b1인당 1계정#k이 원칙이며, 어길 시 제재 처리 됩니다."
        } else if (Randomizer.isSuccess(25)) {
            txt += "서버 내에서 발생한 #r오류를 악용#k하시면 제재 처리 됩니다."
        } else if (Randomizer.isSuccess(25)) {
            txt += "#r분쟁#k으로 인한 #r서버의 분위기를 흐릴 경우#k 운영자 판단하에 제재 처리 됩니다."
        } else if (Randomizer.isSuccess(25)) {
            txt += "모두가 사용하는 공간에선 기본적인 #fc0xFF4374D9#매너#k를 지켜주세요."
        } else {
            txt += "#fc0xFF003399#블랙#k 운영 원칙에 어긋나는 행동을 할시 제재 처리 됩니다."
        }
        cm.getClient().send(SLFCGPacket.BlackLabelE(txt, 90, 3000, 4, 0, 0, 0, 4, 3, "SampleType00"));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, 12000));
    } else if (status == 4) {
        cm.gainItem(2000005, 100);
        cm.gainMeso(3000000);
        cm.gainItem(2430000, 1);
        cm.getPlayer().AutoTeachSkill();
        if (sel == 0) {
            cm.changeJob(3101);
        } else {
            cm.changeJob(3100);
        }
        for (var i = cm.getPlayer().getLevel(); i < 10; i++) {
            cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
        }
        cm.warp(100000051);
        cm.getPlayer().resetStats(4,4,4,4);
		cm.getClient().send(CField.UIPacket.getDirectionStatus(false));
        cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
        cm.JoinNubGuild();
        cm.dispose();
    }
}


function statusplus(millsecond) {
    cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}

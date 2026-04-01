


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    ★ 에 의해 만들어 졌습니다.

    엔피시아이디 : 9000210

    엔피시 이름 : 병아리

    엔피시가 있는 맵 : 헤네시스 : 헤네시스 (100000000)

    엔피시 설명 : MISSINGNO


*/
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.server.enchant);
importPackage(java.sql);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
노랑 = "#fc0xFFFFBB00#"
검정 = "#fc0xFF191919#"
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
        말 = "#fs15##b블랙 용사 (보스 데미지 증가)#k " + 검정 + "스킬을 획득하거나 업그레이드 할 수 있다네! 어떤 기능을 이용하겠나?\r\n"
        말 += "#fc0xFFD5D5D5#────────────────────────────#k\r\n";
        말 += "#L0# #b스킬을 레벨업 시키고 싶습니다.\r\n"
        말 += "#L1# #r스킬을 받고 싶습니다."
        cm.sendOkS(말, 0x04);
    } else if (status == 1) {
        if (!cm.haveItem(2430044, 1)) {
            cm.sendOkS("#fs15#" + 검정 + "기능을 이용하기 위한 #b#z2430044##k " + 검정 + "아이템이 없다네.", 0x04, 9000030);
            cm.dispose();
            return;
        }
        if (selection == 0) {
            if (cm.getPlayer().getSkillLevel(80001542) < 1) {
                cm.sendOkS("#fs15#" + 검정 + "자네는 스킬을 보유하고 있지 않네.", 0x04, 9000030);
                cm.dispose();
                return;
            }
            if (cm.getPlayer().getSkillLevel(80001542) > 19) {
                cm.sendOkS("#fs15#" + 검정 + "이미 마스터 레벨을 달성했다네.", 0x04, 9000030);
                cm.dispose();
                return;
            }
            레벨업 = 0
            cm.sendYesNoS("#fs15#" + 검정 + "정말로 #b#z2430044# (보스 데미지 증가)#k " + 검정 + "아이템을 사용하여 레벨업을 시키겠나?\r\n\r\n#r(레벨업 성공 확률은 15% 입니다)", 0x04);
        } else if (selection == 1) {
            if (cm.getPlayer().getSkillLevel(80001542) > 0) {
                cm.sendOkS("#fs15#" + 검정 + "이미 스킬을 보유하고 있는거 같네.", 0x04, 9000030);
                cm.dispose();
                return;
            }
            cm.teachSkill(80001542, 1, 1);
            cm.sendOkS("#fs15#" + 검정 + "자네 스킬창에 #s" + 80001542 + "# 블랙 용사 (보스 데미지 증가)" + 검정 + "을(를) 지급했다네!", 0x04, 9000030);
            cm.gainItem(2430044, -1);
            cm.dispose();
        }
    } else if (status == 2) {
        if (레벨업 == 0) {
            if (Randomizer.isSuccess(15)) {
                cm.teachSkill(80001542, cm.getPlayer().getSkillLevel(80001542) + 1, 20);
                cm.sendSimpleS("#fs15##s80001542# #b블랙 용사 (보스 데미지 증가)#k " + 검정 + "스킬 업그레이드에 성공했네!!\r\n", 0x04);
                cm.gainItem(2430044, -1);
                cm.dispose();
            } else {
                cm.sendSimpleS("#fs15#" + 검정 + "스킬 업그레이드에 실패했네..", 0x04);
                cm.gainItem(2430044, -1);
                cm.dispose();
            }
        }
    }
}

importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

스톤 = 4021007

일차원 = Randomizer.rand(1,5);
이차원 = Randomizer.rand(1,3);
삼차원 = Randomizer.rand(3,6);
사차원 = Randomizer.rand(5,10);
오차원 = Randomizer.rand(10,20);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
   if (status == 0) {
    말 = "#fs15##b차원의 스톤#k을 분해하러 온겐가?\r\n"
    말 += "분해하면 #r#z4021007##k을 #e랜덤#n으로 획득이 가능하다네.\r\n"
    말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
    말 += "#L0##i4310000# #b#z4310000##k을 분해하고 싶습니다.#l\r\n"
    //말 += "#L1##i4310001# #b#z4310001##k을 분해하고 싶습니다.#l\r\n"
   //말 += "#L2##i4310002# #b#z4310002##k을 분해하고 싶습니다.#l\r\n"
   //말 += "#L3##i4310003# #b#z4310003##k을 분해하고 싶습니다.#l\r\n"
   //말 += "#L4##i4310004# #b#z4310004##k을 분해하고 싶습니다.#l\r\n"
   cm.sendSimpleS(말, 0x04, 2192030);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 2) {
            cm.sendOkS("#fs15##r기타칸 2 칸 이상을 확보하고 다시 말을 걸어주게.", 0x04, 2192030);
            cm.dispose();
            return;
        }
        if (selection == 0) {
            if (!cm.haveItem(4310000, 1)) {
                cm.sendOkS("#fs15##i4310000# #b#z4310000##k 아이템이 부족하다네.", 0x04, 2192030);
                cm.dispose();
                return;
            }
            cm.gainItem(4310000, -1);
            cm.gainItem(스톤, 일차원);
            cm.sendSimpleS("#fs15#축하하네, 분해한 결과라네!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#b#i"+스톤+"##b#z"+스톤+"# "+일차원+"개#k\r\n\r\n#L0##b분해를 한번 더 진행 하겠습니다.", 0x04, 2192030);
        } else if (selection == 1) {
            if (!cm.haveItem(4310001, 1)) {
                cm.sendOkS("#fs15##i4310001# #b#z4310001##k 아이템이 부족하다네.", 0x04, 2192030);
                cm.dispose();
                return;
            }
            cm.gainItem(4310001, -1);
            cm.gainItem(스톤, 이차원);
            cm.sendSimpleS("#fs15#축하하네, 분해한 결과라네!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#b#i"+스톤+"##b#z"+스톤+"# "+이차원+"개#k\r\n\r\n#L0##b분해를 한번 더 진행 하겠습니다.", 0x04, 2192030);
        } else if (selection == 2) {
            if (!cm.haveItem(4310002, 1)) {
                cm.sendOkS("#fs15##i4310002# #b#z4310002##k 아이템이 부족하다네.", 0x04, 2192030);
                cm.dispose();
                return;
            }
            cm.gainItem(4310002, -1);
            cm.gainItem(스톤, 삼차원);
            cm.sendSimpleS("#fs15#축하하네, 분해한 결과라네!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#b#i"+스톤+"##b#z"+스톤+"# "+삼차원+"개#k\r\n\r\n#L0##b분해를 한번 더 진행 하겠습니다.", 0x04, 2192030);
        } else if (selection == 3) {
            if (!cm.haveItem(4310003, 1)) {
                cm.sendOkS("#fs15##i4310003# #b#z4310003##k 아이템이 부족하다네.", 0x04, 2192030);
                cm.dispose();
                return;
            }
            cm.gainItem(4310003, -1);
            cm.gainItem(스톤, 사차원);
            cm.sendSimpleS("#fs15#축하하네, 분해한 결과라네!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#b#i"+스톤+"##b#z"+스톤+"# "+사차원+"개#k\r\n\r\n#L0##b분해를 한번 더 진행 하겠습니다.", 0x04, 2192030);
        } else if (selection == 4) {
            if (!cm.haveItem(4310004, 1)) {
                cm.sendOkS("#fs15##i4310004# #b#z4310004##k 아이템이 부족하다네.", 0x04, 2192030);
                cm.dispose();
                return;
            }
            cm.gainItem(4310004, -1);
            cm.gainItem(스톤, 오차원);
            cm.sendSimpleS("#fs15#축하하네, 분해한 결과라네!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#b#i"+스톤+"##b#z"+스톤+"# "+오차원+"개#k\r\n\r\n#L0##b분해를 한번 더 진행 하겠습니다.", 0x04, 2192030);
        }
    } else if (status == 2) {
        cm.dispose();
        cm.openNpc(2074146);
    }
}

var status = -1;
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
function start() {
    status = -1;
    action(1, 0, 0);
}
var 검정 = "#fc0xFF191919#"
var 초록 = "#fc0xFF47C83E#"
var 연파랑 = "#fc0xFF4641D9#"
var 빨강 = "#fc0xFFF15F5F#"
var 노랑 = "#fc0xFFF2CB61#"
var 파랑 = "#fc0xFF4374D9#"
var 보라 = "#fc0xFF8041D9#"
var 검빨강 = "#fc0xFFCC3D3D#"

var 힘 = 0;
var 덱스 = 0;
var 인트 = 0;
var 럭 = 0;
var 제논 = 0;
var 데몬어벤져 = 0;
var 심볼 = 1713000;
var enter = "\r\n"
var i;
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
        말 = "#fs20#請選擇想要交換的滿級 #r原初符文#k?" + enter + enter
        for(i = 0; i < 6; i++) {
            말 += "#L" + i + "# #i" + (심볼 + i) + "#" 
        }
        cm.sendOkS(말, 0x86, 9000030);
    } else if (status == 1) {
        i = selection;
        말 = "#fs15#" + 연파랑 + "#z" + (심볼 + i)  + "##k " + 검정 + "請選擇要賦予道具的屬性.\r\n"
        말 += "#fc0xFFD5D5D5#───────────────────────────#k" + 검정 + "\r\n";
        말 += "#L0##i" + (심볼 + i) + "# 我想被賦予 " + 빨강 + "STR 屬性#k" + 검정 + "能力值.\r\n"
        말 += "#L1##i" + (심볼 + i) + "# 我想被賦予 " + 파랑 + "DEX 屬性#k" + 검정 + "能力值.\r\n"
        말 += "#L2##i" + (심볼 + i) + "# 我想被賦予 " + 노랑 + "INT 屬性#k" + 검정 + "能力值.\r\n"
        말 += "#L3##i" + (심볼 + i) + "# 我想被賦予 " + 초록 + "LUK 屬性" + 검정 + "能力值.\r\n"
        if (cm.getPlayer().getJob() == 3600 || cm.getPlayer().getJob() == 3610 || cm.getPlayer().
        getJob() == 3611 || cm.getPlayer().getJob() == 3612) {
            말 += "#L4##i" + (심볼 + i) + "# 我想被賦予 " + 보라 + "STR,DEX,LUK 屬性" + 검정 + "能力值.\r\n"
        }
        if (cm.getPlayer().getJob() == 3001 || cm.getPlayer().getJob() == 3120 || cm.getPlayer().getJob() == 3121 || cm.getPlayer().getJob() == 3122) {
            말 += "#L5##i" + (심볼 + i) + "# 我想被賦予 " + 검빨강 + "HP 屬性" + 검정 + "能力值.\r\n"
        } 
        cm.sendOkS(말, 0x86, 9000030);
    } else if (status == 2) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 2) {
            cm.sendOkS("#fs15##r確保裝備欄2格以上.", 0x86, 9000030);
            cm.dispose();
            return;
        }
        if (selection == 0) {
            힘 = 1;
            말 = "#fs35##fn나눔고딕 Extrabold#" + 빨강 + "'STR' #fs15#" + 검정 + " 確定選擇這個屬性?\r\n"
            cm.sendYesNoS(말, 0x86, 9000030);
        } else if (selection == 1) {
            덱스 = 1;
            말 = "#fs35##fn나눔고딕 Extrabold#" + 파랑 + "'DEX' #fs15#" + 검정 + " 確定選擇這個屬性?\r\n"
            cm.sendYesNoS(말, 0x86, 9000030);
        } else if (selection == 2) {
            인트 = 1;
            말 = "#fs35##fn나눔고딕 Extrabold#" + 노랑 + "'INT' #fs15#" + 검정 + " 確定選擇這個屬性?\r\n"
            cm.sendYesNoS(말, 0x86, 9000030);
        } else if (selection == 3) {
            럭 = 1;
            말 = "#fs35##fn나눔고딕 Extrabold#" + 초록 + "'LUK' #fs15#" + 검정 + " 確定選擇這個屬性?\r\n"
            cm.sendYesNoS(말, 0x86, 9000030);
        } else if (selection == 4) {
            제논 = 1;
            말 = "#fs28##fn나눔고딕 Extrabold#" + 보라 + "'STR,DEX,LUK' #fs15#" + 검정 + " 確定選擇這個屬性?\r\n"
            cm.sendYesNoS(말, 0x86, 9000030);
        } else if (selection == 5) {
            데몬어벤져 = 1;
            말 = "#fs35##fn나눔고딕 Extrabold#" + 검빨강 + "'HP' #fs15#" + 검정 + " 確定選擇這個屬性?\r\n"
            cm.sendYesNoS(말, 0x86, 9000030);
        }
    } else if (status == 3) {
        if (힘 == 1) {
            말 = "#fs15#" + 파랑 + "#z" + (심볼 + i) + "##k" + 검정 + "物品成長成功！ 去確認一下庫存吧。 哈哈.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i" + (심볼 + i) + "# #b#z" + (심볼 + i) + "##k\r\n"
            말 += "" + 검정 + "等級 : 11\r\n"
            말 += "" + 검정 + "强化 : 最大\r\n"
            말 += "" + 빨강 + "STR : +2500\r\n"
            말 += "" + 빨강 + "AUT : +110\r\n"
            cm.sendOkS(말, 0x86, 9000030);
            cm.gainItem(2633616, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(심볼 + i);
            inz.setStr(2500);
            inz.setArcLevel(11);
            inz.setArc(110);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (덱스 == 1) {
            말 = "#fs15#" + 파랑 + "#z" + (심볼 + i) + "##k" + 검정 + " 物品成長成功！ 去確認一下庫存吧。 哈哈.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i" + (심볼 + i) + "# #b#z" + (심볼 + i) + "##k\r\n"
            말 += "" + 검정 + "等級 : 11\r\n"
            말 += "" + 검정 + "强化 : 最大\r\n"
            말 += "" + 파랑 + "DEX : +2500\r\n"
            말 += "" + 파랑 + "AUT : +110\r\n"
            cm.sendOkS(말, 0x86, 9000030);
            cm.gainItem(2633616, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById((심볼 + i));
            inz.setDex(2500);
            inz.setArcLevel(11);
            inz.setArc(110);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (인트 == 1) {
            말 = "#fs15#" + 파랑 + "#z" + (심볼 + i) + "##k" + 검정 + " 物品成長成功！ 去確認一下庫存吧。 哈哈.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i" + (심볼 + i) + "# #b#z" + (심볼 + i) + "##k\r\n"
            말 += "" + 검정 + "等級 : 11\r\n"
            말 += "" + 검정 + "强化 : 最大\r\n"
            말 += "" + 노랑 + "INT : +2500\r\n"
            말 += "" + 노랑 + "AUT : +110\r\n"
            cm.sendOkS(말, 0x86, 9000030);
            cm.gainItem(2633616, -1);			
            var inz = MapleItemInformationProvider.getInstance().getEquipById((심볼 + i));
            inz.setInt(2500);
            inz.setArcLevel(11);
            inz.setArc(110);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (럭 == 1) {
            말 = "#fs15#" + 파랑 + "#z" + (심볼 + i) + "##k" + 검정 + " 物品成長成功！ 去確認一下庫存吧。 哈哈.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i" + (심볼 + i) + "# #b#z" + (심볼 + i) + "##k\r\n"
            말 += "" + 검정 + "等級 : 11\r\n"
            말 += "" + 검정 + "强化 : 最大\r\n"
            말 += "" + 초록 + "LUK : +2500\r\n"
            말 += "" + 초록 + "AUT : +110\r\n"
            cm.sendOkS(말, 0x86, 9000030);
            cm.gainItem(2633616, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById((심볼 + i));
            inz.setLuk(2500);
            inz.setArcLevel(11);
            inz.setArc(110);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (제논 == 1) {
            말 = "#fs15#" + 보라 + "#z" + (심볼 + i) + "##k" + 검정 + " 物品成長成功！ 去確認一下庫存吧。 哈哈.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i" + (심볼 + i) + "# #b#z" + (심볼 + i) + "##k\r\n"
            말 += "" + 검정 + "等級 : 11\r\n"
            말 += "" + 검정 + "强化 : 最大\r\n"
            말 += "" + 보라 + "STR : +975\r\n"
            말 += "" + 보라 + "DEX : +975\r\n"
            말 += "" + 보라 + "LUK : +975\r\n"
            말 += "" + 보라 + "ARC : +110\r\n"
            cm.sendOkS(말, 0x86, 9000030);
            cm.gainItem(2633616, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById((심볼 + i));
            inz.setStr(975);
            inz.setDex(975);
            inz.setLuk(975);
            inz.setArcLevel(11);
            inz.setArc(110);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (데몬어벤져 == 1) {
            말 = "#fs15#" + 검빨강 + "#z" + (심볼 + i) + "##k" + 검정 + " 成長成功！ 去確認一下庫存吧。 哈哈.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i" + (심볼 + i) + "# #b#z" + (심볼 + i) + "##k\r\n"
            말 += "" + 검정 + "等級 : 11\r\n"
            말 += "" + 검정 + "强化 : 最大\r\n"
            말 += "" + 검빨강 + "HP : +43750\r\n"
            말 += "" + 검빨강 + "ARC : +110\r\n"
            cm.sendOkS(말, 0x86, 9000030);
            cm.gainItem(2633616, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById((심볼 + i));
            inz.setHp(4375);
            inz.setArcLevel(11);
            inz.setArc(110);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        }
    }
}

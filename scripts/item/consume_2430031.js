
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
검정 = "#fc0xFF191919#"
초록 = "#fc0xFF47C83E#"
연파랑 = "#fc0xFF4641D9#"
빨강 = "#fc0xFFF15F5F#"
노랑 = "#fc0xFFF2CB61#"
파랑 = "#fc0xFF4374D9#"
보라 = "#fc0xFF8041D9#"
검빨강 = "#fc0xFFCC3D3D#"

힘 = 0;
덱스 = 0;
인트 = 0;
럭 = 0;
제논 = 0;
데몬어벤져 = 0;
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
        말 = "#fs11#" + 연파랑 + "#z1712001##k " + 검정 + "아이템에 부여될 스텟을 선택해보게나.\r\n"
        말 += "#fc0xFFD5D5D5#───────────────────────────#k" + 검정 + "\r\n";
        말 += "#L0##i1712001# 저는 " + 빨강 + "STR 스텟#k" + 검정 + "을 부여 받고 싶습니다.\r\n"
        말 += "#L1##i1712001# 저는 " + 파랑 + "DEX 스텟#k" + 검정 + "을 부여 받고 싶습니다.\r\n"
        말 += "#L2##i1712001# 저는 " + 노랑 + "INT 스텟#k" + 검정 + "을 부여 받고 싶습니다.\r\n"
        말 += "#L3##i1712001# 저는 " + 초록 + "LUK 스텟" + 검정 + "을 부여 받고 싶습니다.\r\n"
        if (cm.getPlayer().getJob() == 3600 || cm.getPlayer().getJob() == 3610 || cm.getPlayer().getJob() == 3611 || cm.getPlayer().getJob() == 3612) {
            말 += "#L4##i1712001# 저는 " + 보라 + "STR,DEX,LUK 스텟" + 검정 + "을 부여 받고 싶습니다.\r\n"
        }
        if (cm.getPlayer().getJob() == 3001 || cm.getPlayer().getJob() == 3120 || cm.getPlayer().getJob() == 3121 || cm.getPlayer().getJob() == 3122) {
            말 += "#L5##i1712001# 저는 " + 검빨강 + "HP 스텟" + 검정 + "을 부여 받고 싶습니다.\r\n"
        } 
        cm.sendOkS(말, 0x04, 9401232);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 2) {
            cm.sendOkS("#fs11##r장비창 2 칸 이상을 확보하게.", 0x04, 9401232);
            cm.dispose();
            return;
        }
        if (selection == 0) {
            힘 = 1;
            말 = "#fs35##fn나눔고딕 Extrabold#" + 빨강 + "'STR' #fs11#" + 검정 + " 스텟으로 받는게 확실한가?\r\n"
            cm.sendYesNoS(말, 0x04, 9401232);
        } else if (selection == 1) {
            덱스 = 1;
            말 = "#fs35##fn나눔고딕 Extrabold#" + 파랑 + "'DEX' #fs11#" + 검정 + " 스텟으로 받는게 확실한가?\r\n"
            cm.sendYesNoS(말, 0x04, 9401232);
        } else if (selection == 2) {
            인트 = 1;
            말 = "#fs35##fn나눔고딕 Extrabold#" + 노랑 + "'INT' #fs11#" + 검정 + " 스텟으로 받는게 확실한가?\r\n"
            cm.sendYesNoS(말, 0x04, 9401232);
        } else if (selection == 3) {
            럭 = 1;
            말 = "#fs35##fn나눔고딕 Extrabold#" + 초록 + "'LUK' #fs11#" + 검정 + " 스텟으로 받는게 확실한가?\r\n"
            cm.sendYesNoS(말, 0x04, 9401232);
        } else if (selection == 4) {
            제논 = 1;
            말 = "#fs28##fn나눔고딕 Extrabold#" + 보라 + "'STR,DEX,LUK' #fs11#" + 검정 + " 스텟으로 받는게 확실한가?\r\n"
            cm.sendYesNoS(말, 0x04, 9401232);
        } else if (selection == 5) {
            데몬어벤져 = 1;
            말 = "#fs35##fn나눔고딕 Extrabold#" + 검빨강 + "'HP' #fs11#" + 검정 + " 스텟으로 받는게 확실한가?\r\n"
            cm.sendYesNoS(말, 0x04, 9401232);
        }
    } else if (status == 2) {
        if (힘 == 1) {
            말 = "#fs11#" + 파랑 + "#z1712001##k" + 검정 + " 아이템이 성공적으로 성장되었네! 인벤토리를 확인해보게나. 크크.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i1712001# #b#z1712001##k\r\n"
            말 += "" + 검정 + "성장 레벨 : 20\r\n"
            말 += "" + 검정 + "성장치 : MAX\r\n"
            말 += "" + 빨강 + "STR : +2200\r\n"
            말 += "" + 빨강 + "ARC : +220\r\n"
            cm.sendOkS(말, 0x04, 9401232);
            cm.gainItem(2430031, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1712001);
            inz.setStr(2200);
            inz.setArcLevel(20);
            inz.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (덱스 == 1) {
            말 = "#fs11#" + 파랑 + "#z1712001##k" + 검정 + " 아이템이 성공적으로 성장되었네! 인벤토리를 확인해보게나. 크크.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i1712001# #b#z1712001##k\r\n"
            말 += "" + 검정 + "성장 레벨 : 20\r\n"
            말 += "" + 검정 + "성장치 : MAX\r\n"
            말 += "" + 파랑 + "DEX : +2200\r\n"
            말 += "" + 파랑 + "ARC : +220\r\n"
            cm.sendOkS(말, 0x04, 9401232);
            cm.gainItem(2430031, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1712001);
            inz.setDex(2200);
            inz.setArcLevel(20);
            inz.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (인트 == 1) {
            말 = "#fs11#" + 파랑 + "#z1712001##k" + 검정 + " 아이템이 성공적으로 성장되었네! 인벤토리를 확인해보게나. 크크.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i1712001# #b#z1712001##k\r\n"
            말 += "" + 검정 + "성장 레벨 : 20\r\n"
            말 += "" + 검정 + "성장치 : MAX\r\n"
            말 += "" + 노랑 + "INT : +2200\r\n"
            말 += "" + 노랑 + "ARC : +220\r\n"
            cm.sendOkS(말, 0x04, 9401232);
            cm.gainItem(2430031, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1712001);
            inz.setInt(2200);
            inz.setArcLevel(20);
            inz.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (럭 == 1) {
            말 = "#fs11#" + 파랑 + "#z1712001##k" + 검정 + " 아이템이 성공적으로 성장되었네! 인벤토리를 확인해보게나. 크크.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i1712001# #b#z1712001##k\r\n"
            말 += "" + 검정 + "성장 레벨 : 20\r\n"
            말 += "" + 검정 + "성장치 : MAX\r\n"
            말 += "" + 초록 + "LUK : +2200\r\n"
            말 += "" + 초록 + "ARC : +220\r\n"
            cm.sendOkS(말, 0x04, 9401232);
            cm.gainItem(2430031, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1712001);
            inz.setLuk(2200);
            inz.setArcLevel(20);
            inz.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (제논 == 1) {
            말 = "#fs11#" + 보라 + "#z1712001##k" + 검정 + " 아이템이 성공적으로 성장되었네! 인벤토리를 확인해보게나. 크크.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i1712001# #b#z1712001##k\r\n"
            말 += "" + 검정 + "성장 레벨 : 20\r\n"
            말 += "" + 검정 + "성장치 : MAX\r\n"
            말 += "" + 보라 + "STR : +858\r\n"
            말 += "" + 보라 + "DEX : +858\r\n"
            말 += "" + 보라 + "LUK : +858\r\n"
            말 += "" + 보라 + "ARC : +220\r\n"
            cm.sendOkS(말, 0x04, 9401232);
            cm.gainItem(2430031, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1712001);
            inz.setStr(858);
            inz.setDex(858);
            inz.setLuk(858);
            inz.setArcLevel(20);
            inz.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        } else if (데몬어벤져 == 1) {
            말 = "#fs11#" + 검빨강 + "#z1712001##k" + 검정 + " 아이템이 성공적으로 성장되었네! 인벤토리를 확인해보게나. 크크.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i1712001# #b#z1712001##k\r\n"
            말 += "" + 검정 + "성장 레벨 : 20\r\n"
            말 += "" + 검정 + "성장치 : MAX\r\n"
            말 += "" + 검빨강 + "HP : +38500\r\n"
            말 += "" + 검빨강 + "ARC : +220\r\n"
            cm.sendOkS(말, 0x04, 9401232);
            cm.gainItem(2430031, -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1712001);
            inz.setHp(3850);
            inz.setArcLevel(20);
            inz.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            cm.dispose();
            return;
        }
    }
}

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
黑色 = "#fc0xFF191919#"
綠色 = "#fc0xFF47C83E#"
淺藍 = "#fc0xFF4641D9#"
紅色 = "#fc0xFFF15F5F#"
黃色 = "#fc0xFFF2CB61#"
藍色 = "#fc0xFF4374D9#"
紫色 = "#fc0xFF8041D9#"
暗紅 = "#fc0xFFCC3D3D#"
 
力量 = 0;
敏捷 = 0;
智力 = 0;
幸運 = 0;
傑諾 = 0;
惡魔殺手 = 0;
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
        對話 = "#fs15#" + 淺藍 + "#z1712005##k " + 黑色 + "物品將賦予的屬性選擇一下吧。\r\n"
        對話 += "#fc0xFFD5D5D5#───────────────────────────#k" + 黑色 + "\r\n";
        對話 += "#L0##i1712005# 我想要 " + 紅色 + "力量(STR)屬性#k" + 黑色 + "。\r\n"
        對話 += "#L1##i1712005# 我想要 " + 藍色 + "敏捷(DEX)屬性#k" + 黑色 + "。\r\n"
        對話 += "#L2##i1712005# 我想要 " + 黃色 + "智力(INT)屬性#k" + 黑色 + "。\r\n"
        對話 += "#L3##i1712005# 我想要 " + 綠色 + "幸運(LUK)屬性" + 黑色 + "。\r\n"
        if (cm.getPlayer().getJob()  == 3600 || cm.getPlayer().getJob()  == 3610 || cm.getPlayer().getJob()  == 3611 || cm.getPlayer().getJob()  == 3612) {
            對話 += "#L4##i1712005# 我想要 " + 紫色 + "力量/敏捷/幸運(STR/DEX/LUK)屬性" + 黑色 + "。\r\n"
        }
        if (cm.getPlayer().getJob()  == 3001 || cm.getPlayer().getJob()  == 3120 || cm.getPlayer().getJob()  == 3121 || cm.getPlayer().getJob()  == 3122) {
            對話 += "#L5##i1712005# 我想要 " + 暗紅 + "HP屬性" + 黑色 + "。\r\n"
        }
        cm.sendOkS( 對話, 0x04, 9000030);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot(); 
        if (leftslot < 2) {
            cm.sendOkS("#fs15##r 請確保裝備欄有至少2格空間。", 0x04, 9000030);
            cm.dispose(); 
            return;
        }
        if (selection == 0) {
            力量 = 1;
            對話 = "#fs35##fn나눔고딕 Extrabold#" + 紅色 + "'力量(STR)' #fs15#" + 黑色 + " 確定選擇這個屬性嗎？\r\n"
            cm.sendYesNoS( 對話, 0x04, 9000030);
        } else if (selection == 1) {
            敏捷 = 1;
            對話 = "#fs35##fn나눔고딕 Extrabold#" + 藍色 + "'敏捷(DEX)' #fs15#" + 黑色 + " 確定選擇這個屬性嗎？\r\n"
            cm.sendYesNoS( 對話, 0x04, 9000030);
        } else if (selection == 2) {
            智力 = 1;
            對話 = "#fs35##fn나눔고딕 Extrabold#" + 黃色 + "'智力(INT)' #fs15#" + 黑色 + " 確定選擇這個屬性嗎？\r\n"
            cm.sendYesNoS( 對話, 0x04, 9000030);
        } else if (selection == 3) {
            幸運 = 1;
            對話 = "#fs35##fn나눔고딕 Extrabold#" + 綠色 + "'幸運(LUK)' #fs15#" + 黑色 + " 確定選擇這個屬性嗎？\r\n"
            cm.sendYesNoS( 對話, 0x04, 9000030);
        } else if (selection == 4) {
            傑諾 = 1;
            對話 = "#fs28##fn나눔고딕 Extrabold#" + 紫色 + "'力量/敏捷/幸運(STR/DEX/LUK)' #fs15#" + 黑色 + " 確定選擇這個屬性嗎？\r\n"
            cm.sendYesNoS( 對話, 0x04, 9000030);
        } else if (selection == 5) {
            惡魔殺手 = 1;
            對話 = "#fs35##fn나눔고딕 Extrabold#" + 暗紅 + "'HP' #fs15#" + 黑色 + " 確定選擇這個屬性嗎？\r\n"
            cm.sendYesNoS( 對話, 0x04, 9000030);
        }
    } else if (status == 2) {
        if (力量 == 1) {
            對話 = "#fs15#" + 淺藍 + "#z1712005##k" + 黑色 + " 物品已成功成長！請檢查你的背包。呵呵。\r\n\r\n"
            對話 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            對話 += "#i1712005# #b#z1712005##k\r\n"
            對話 += "" + 黑色 + "成長等級 : 20\r\n"
            對話 += "" + 黑色 + "成長值 : MAX\r\n"
            對話 += "" + 紅色 + "力量(STR) : +2200\r\n"
            對話 += "" + 紅色 + "ARC : +220\r\n"
            cm.sendOkS( 對話, 0x04, 9000030);
            cm.gainItem(2430051,  -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1712005); 
            inz.setStr(2200); 
            inz.setArcLevel(20); 
            inz.setArc(220); 
            MapleInventoryManipulator.addbyItem(cm.getClient(),  inz);
            cm.dispose(); 
            return;
        } else if (敏捷 == 1) {
            對話 = "#fs15#" + 淺藍 + "#z1712005##k" + 黑色 + " 物品已成功成長！請檢查你的背包。呵呵。\r\n\r\n"
            對話 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            對話 += "#i1712005# #b#z1712005##k\r\n"
            對話 += "" + 黑色 + "成長等級 : 20\r\n"
            對話 += "" + 黑色 + "成長值 : MAX\r\n"
            對話 += "" + 藍色 + "敏捷(DEX) : +2200\r\n"
            對話 += "" + 藍色 + "ARC : +220\r\n"
            cm.sendOkS( 對話, 0x04, 9000030);
            cm.gainItem(2430051,  -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1712005); 
            inz.setDex(2200); 
            inz.setArcLevel(20); 
            inz.setArc(220); 
            MapleInventoryManipulator.addbyItem(cm.getClient(),  inz);
            cm.dispose(); 
            return;
        } else if (智力 == 1) {
            對話 = "#fs15#" + 淺藍 + "#z1712005##k" + 黑色 + " 物品已成功成長！請檢查你的背包。呵呵。\r\n\r\n"
            對話 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            對話 += "#i1712005# #b#z1712005##k\r\n"
            對話 += "" + 黑色 + "成長等級 : 20\r\n"
            對話 += "" + 黑色 + "成長值 : MAX\r\n"
            對話 += "" + 黃色 + "智力(INT) : +2200\r\n"
            對話 += "" + 黃色 + "ARC : +220\r\n"
            cm.sendOkS( 對話, 0x04, 9000030);
            cm.gainItem(2430051,  -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1712005); 
            inz.setInt(2200); 
            inz.setArcLevel(20); 
            inz.setArc(220); 
            MapleInventoryManipulator.addbyItem(cm.getClient(),  inz);
            cm.dispose(); 
            return;
        } else if (幸運 == 1) {
            對話 = "#fs15#" + 淺藍 + "#z1712005##k" + 黑色 + " 物品已成功成長！請檢查你的背包。呵呵。\r\n\r\n"
            對話 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            對話 += "#i1712005# #b#z1712005##k\r\n"
            對話 += "" + 黑色 + "成長等級 : 20\r\n"
            對話 += "" + 黑色 + "成長值 : MAX\r\n"
            對話 += "" + 綠色 + "幸運(LUK) : +2200\r\n"
            對話 += "" + 綠色 + "ARC : +220\r\n"
            cm.sendOkS( 對話, 0x04, 9000030);
            cm.gainItem(2430051,  -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1712005); 
            inz.setLuk(2200); 
            inz.setArcLevel(20); 
            inz.setArc(220); 
            MapleInventoryManipulator.addbyItem(cm.getClient(),  inz);
            cm.dispose(); 
            return;
        } else if (傑諾 == 1) {
            對話 = "#fs15#" + 紫色 + "#z1712005##k" + 黑色 + " 物品已成功成長！請檢查你的背包。呵呵。\r\n\r\n"
            對話 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            對話 += "#i1712005# #b#z1712005##k\r\n"
            對話 += "" + 黑色 + "成長等級 : 20\r\n"
            對話 += "" + 黑色 + "成長值 : MAX\r\n"
            對話 += "" + 紫色 + "力量(STR) : +858\r\n"
            對話 += "" + 紫色 + "敏捷(DEX) : +858\r\n"
            對話 += "" + 紫色 + "幸運(LUK) : +858\r\n"
            對話 += "" + 紫色 + "ARC : +220\r\n"
            cm.sendOkS( 對話, 0x04, 9000030);
            cm.gainItem(2430051,  -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1712005); 
            inz.setStr(858); 
            inz.setDex(858); 
            inz.setLuk(858); 
            inz.setArcLevel(20); 
            inz.setArc(220); 
            MapleInventoryManipulator.addbyItem(cm.getClient(),  inz);
            cm.dispose(); 
            return;
        } else if (惡魔殺手 == 1) {
            對話 = "#fs15#" + 暗紅 + "#z1712005##k" + 黑色 + " 物品已成功成長！請檢查你的背包。呵呵。\r\n\r\n"
            對話 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            對話 += "#i1712005# #b#z1712005##k\r\n"
            對話 += "" + 黑色 + "成長等級 : 20\r\n"
            對話 += "" + 黑色 + "成長值 : MAX\r\n"
            對話 += "" + 暗紅 + "HP : +38500\r\n"
            對話 += "" + 暗紅 + "ARC : +220\r\n"
            cm.sendOkS( 對話, 0x04, 9000030);
            cm.gainItem(2430051,  -1);
            var inz = MapleItemInformationProvider.getInstance().getEquipById(1712005); 
            inz.setHp(3850); 
            inz.setArcLevel(20); 
            inz.setArc(220); 
            MapleInventoryManipulator.addbyItem(cm.getClient(),  inz);
            cm.dispose(); 
            return;
        }
    }
}
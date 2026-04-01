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
 
// 顏色定義 
黑色 = "#fc0xFF191919#";
綠色 = "#fc0xFF47C83E#";
淺藍 = "#fc0xFF4641D9#";
紅色 = "#fc0xFFF15F5F#";
黃色 = "#fc0xFFF2CB61#";
藍色 = "#fc0xFF4374D9#";
紫色 = "#fc0xFF8041D9#";
暗紅 = "#fc0xFFCC3D3D#";
 
// 屬性標記變數 
力量 = 0;
敏捷 = 0;
智力 = 0;
幸運 = 0;
傑諾 = 0;
惡魔復仇者 = 0;
 
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
        var 對話 = "#fs15#" + 淺藍 + "#z1712006##k " + 黑色 + "請選擇要賦予裝備的屬性類型\r\n"
        對話 += "#fc0xFFD5D5D5#───────────────────────────#k" + 黑色 + "\r\n";
        對話 += "#L0##i1712006# 我想要 " + 紅色 + "力量(STR)屬性#k" + 黑色 + "\r\n"
        對話 += "#L1##i1712006# 我想要 " + 藍色 + "敏捷(DEX)屬性#k" + 黑色 + "\r\n"
        對話 += "#L2##i1712006# 我想要 " + 黃色 + "智力(INT)屬性#k" + 黑色 + "\r\n"
        對話 += "#L3##i1712006# 我想要 " + 綠色 + "幸運(LUK)屬性" + 黑色 + "\r\n"
        
        // 傑諾職業專用選項 
        if (cm.getPlayer().getJob()  == 3600 || cm.getPlayer().getJob()  == 3610 || cm.getPlayer().getJob()  == 3611 || cm.getPlayer().getJob()  == 3612) {
            對話 += "#L4##i1712006# 我想要 " + 紫色 + "力量/敏捷/幸運(STR/DEX/LUK)屬性" + 黑色 + "\r\n"
        }
        
        // 惡魔復仇者職業專用選項 
        if (cm.getPlayer().getJob()  == 3001 || cm.getPlayer().getJob()  == 3120 || cm.getPlayer().getJob()  == 3121 || cm.getPlayer().getJob()  == 3122) {
            對話 += "#L5##i1712006# 我想要 " + 暗紅 + "HP屬性" + 黑色 + "\r\n"
        } 
        cm.sendOkS( 對話, 0x04, 9000030);
        
    } else if (status == 1) {
        // 檢查裝備欄空間 
        var 剩餘欄位 = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot(); 
        if (剩餘欄位 < 2) {
            cm.sendOkS("#fs15##r 請確保裝備欄至少有2格空位", 0x04, 9000030);
            cm.dispose(); 
            return;
        }
        
        if (selection == 0) {
            力量 = 1;
            確認訊息 = "#fs35##fn나눔고딕 Extrabold#" + 紅色 + "'力量(STR)' #fs15#" + 黑色 + "屬性確認，是否確定？\r\n"
            cm.sendYesNoS( 確認訊息, 0x04, 9000030);
        } else if (selection == 1) {
            敏捷 = 1;
            確認訊息 = "#fs35##fn나눔고딕 Extrabold#" + 藍色 + "'敏捷(DEX)' #fs15#" + 黑色 + "屬性確認，是否確定？\r\n"
            cm.sendYesNoS( 確認訊息, 0x04, 9000030);
        } else if (selection == 2) {
            智力 = 1;
            確認訊息 = "#fs35##fn나눔고딕 Extrabold#" + 黃色 + "'智力(INT)' #fs15#" + 黑色 + "屬性確認，是否確定？\r\n"
            cm.sendYesNoS( 確認訊息, 0x04, 9000030);
        } else if (selection == 3) {
            幸運 = 1;
            確認訊息 = "#fs35##fn나눔고딕 Extrabold#" + 綠色 + "'幸運(LUK)' #fs15#" + 黑色 + "屬性確認，是否確定？\r\n"
            cm.sendYesNoS( 確認訊息, 0x04, 9000030);
        } else if (selection == 4) {
            傑諾 = 1;
            確認訊息 = "#fs28##fn나눔고딕 Extrabold#" + 紫色 + "'力量/敏捷/幸運(STR/DEX/LUK)' #fs15#" + 黑色 + "屬性確認，是否確定？\r\n"
            cm.sendYesNoS( 確認訊息, 0x04, 9000030);
        } else if (selection == 5) {
            惡魔復仇者 = 1;
            確認訊息 = "#fs35##fn나눔고딕 Extrabold#" + 暗紅 + "'HP' #fs15#" + 黑色 + "屬性確認，是否確定？\r\n"
            cm.sendYesNoS( 確認訊息, 0x04, 9000030);
        }
        
    } else if (status == 2) {
        if (力量 == 1) {
            完成訊息 = "#fs15#" + 藍色 + "#z1712006##k" + 黑色 + " 裝備已成功強化！請檢查您的物品欄。\r\n\r\n"
            完成訊息 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            完成訊息 += "#i1712006# #b#z1712006##k\r\n"
            完成訊息 += "" + 黑色 + "強化等級 : 20\r\n"
            完成訊息 += "" + 黑色 + "強化值 : MAX\r\n"
            完成訊息 += "" + 紅色 + "力量(STR) : +2200\r\n"
            完成訊息 += "" + 紅色 + "ARC : +220\r\n"
            cm.sendOkS( 完成訊息, 0x04, 9000030);
            cm.gainItem(2430052,  -1);
            var 裝備 = MapleItemInformationProvider.getInstance().getEquipById(1712006); 
            裝備.setStr(2200);
            裝備.setArcLevel(20);
            裝備.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(),  裝備);
            cm.dispose(); 
            return;
            
        } else if (敏捷 == 1) {
            完成訊息 = "#fs15#" + 藍色 + "#z1712006##k" + 黑色 + " 裝備已成功強化！請檢查您的物品欄。\r\n\r\n"
            完成訊息 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            完成訊息 += "#i1712006# #b#z1712006##k\r\n"
            完成訊息 += "" + 黑色 + "強化等級 : 20\r\n"
            完成訊息 += "" + 黑色 + "強化值 : MAX\r\n"
            完成訊息 += "" + 藍色 + "敏捷(DEX) : +2200\r\n"
            完成訊息 += "" + 藍色 + "ARC : +220\r\n"
            cm.sendOkS( 完成訊息, 0x04, 9000030);
            cm.gainItem(2430052,  -1);
            var 裝備 = MapleItemInformationProvider.getInstance().getEquipById(1712006); 
            裝備.setDex(2200);
            裝備.setArcLevel(20);
            裝備.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(),  裝備);
            cm.dispose(); 
            return;
            
        } else if (智力 == 1) {
            完成訊息 = "#fs15#" + 藍色 + "#z1712006##k" + 黑色 + " 裝備已成功強化！請檢查您的物品欄。\r\n\r\n"
            完成訊息 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            完成訊息 += "#i1712006# #b#z1712006##k\r\n"
            完成訊息 += "" + 黑色 + "強化等級 : 20\r\n"
            完成訊息 += "" + 黑色 + "強化值 : MAX\r\n"
            完成訊息 += "" + 黃色 + "智力(INT) : +2200\r\n"
            完成訊息 += "" + 黃色 + "ARC : +220\r\n"
            cm.sendOkS( 完成訊息, 0x04, 9000030);
            cm.gainItem(2430052,  -1);
            var 裝備 = MapleItemInformationProvider.getInstance().getEquipById(1712006); 
            裝備.setInt(2200);
            裝備.setArcLevel(20);
            裝備.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(),  裝備);
            cm.dispose(); 
            return;
            
        } else if (幸運 == 1) {
            完成訊息 = "#fs15#" + 藍色 + "#z1712006##k" + 黑色 + " 裝備已成功強化！請檢查您的物品欄。\r\n\r\n"
            完成訊息 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            完成訊息 += "#i1712006# #b#z1712006##k\r\n"
            完成訊息 += "" + 黑色 + "強化等級 : 20\r\n"
            完成訊息 += "" + 黑色 + "強化值 : MAX\r\n"
            完成訊息 += "" + 綠色 + "幸運(LUK) : +2200\r\n"
            完成訊息 += "" + 綠色 + "ARC : +220\r\n"
            cm.sendOkS( 完成訊息, 0x04, 9000030);
            cm.gainItem(2430052,  -1);
            var 裝備 = MapleItemInformationProvider.getInstance().getEquipById(1712006); 
            裝備.setLuk(2200);
            裝備.setArcLevel(20);
            裝備.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(),  裝備);
            cm.dispose(); 
            return;
            
        } else if (傑諾 == 1) {
            完成訊息 = "#fs15#" + 紫色 + "#z1712006##k" + 黑色 + " 裝備已成功強化！請檢查您的物品欄。\r\n\r\n"
            完成訊息 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            完成訊息 += "#i1712006# #b#z1712006##k\r\n"
            完成訊息 += "" + 黑色 + "強化等級 : 20\r\n"
            完成訊息 += "" + 黑色 + "強化值 : MAX\r\n"
            完成訊息 += "" + 紫色 + "力量(STR) : +858\r\n"
            完成訊息 += "" + 紫色 + "敏捷(DEX) : +858\r\n"
            完成訊息 += "" + 紫色 + "幸運(LUK) : +858\r\n"
            完成訊息 += "" + 紫色 + "ARC : +220\r\n"
            cm.sendOkS( 完成訊息, 0x04, 9000030);
            cm.gainItem(2430052,  -1);
            var 裝備 = MapleItemInformationProvider.getInstance().getEquipById(1712006); 
            裝備.setStr(858);
            裝備.setDex(858);
            裝備.setLuk(858);
            裝備.setArcLevel(20);
            裝備.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(),  裝備);
            cm.dispose(); 
            return;
            
        } else if (惡魔復仇者 == 1) {
            完成訊息 = "#fs15#" + 暗紅 + "#z1712006##k" + 黑色 + " 裝備已成功強化！請檢查您的物品欄。\r\n\r\n"
            完成訊息 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            完成訊息 += "#i1712006# #b#z1712006##k\r\n"
            完成訊息 += "" + 黑色 + "強化等級 : 20\r\n"
            完成訊息 += "" + 黑色 + "強化值 : MAX\r\n"
            完成訊息 += "" + 暗紅 + "HP : +38500\r\n"
            完成訊息 += "" + 暗紅 + "ARC : +220\r\n"
            cm.sendOkS( 完成訊息, 0x04, 9000030);
            cm.gainItem(2430052,  -1);
            var 裝備 = MapleItemInformationProvider.getInstance().getEquipById(1712006); 
            裝備.setHp(3850);
            裝備.setArcLevel(20);
            裝備.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(),  裝備);
            cm.dispose(); 
            return;
        }
    }
}
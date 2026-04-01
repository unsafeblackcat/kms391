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
黑色 = "#fc0xFF191919#"
綠色 = "#fc0xFF47C83E#"
淺藍 = "#fc0xFF4641D9#"
紅色 = "#fc0xFFF15F5F#"
黃色 = "#fc0xFFF2CB61#"
藍色 = "#fc0xFF4374D9#"
紫色 = "#fc0xFF8041D9#"
暗紅 = "#fc0xFFCC3D3D#"
 
// 屬性標記 
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
        // 初始對話框 
        對話 = "#fs15#" + 淺藍 + "#z1712004##k " + 黑色 + "請選擇要賦予的裝備屬性\r\n"
        對話 += "#fc0xFFD5D5D5#───────────────────────────#k" + 黑色 + "\r\n";
        對話 += "#L0##i1712004# 我想要 " + 紅色 + "力量(STR)#k" + 黑色 + " 屬性\r\n"
        對話 += "#L1##i1712004# 我想要 " + 藍色 + "敏捷(DEX)#k" + 黑色 + " 屬性\r\n"
        對話 += "#L2##i1712004# 我想要 " + 黃色 + "智力(INT)#k" + 黑色 + " 屬性\r\n"
        對話 += "#L3##i1712004# 我想要 " + 綠色 + "幸運(LUK)#k" + 黑色 + " 屬性\r\n"
        
        // 傑諾專用選項 
        if (cm.getPlayer().getJob()  == 3600 || cm.getPlayer().getJob()  == 3610 || cm.getPlayer().getJob()  == 3611 || cm.getPlayer().getJob()  == 3612) {
            對話 += "#L4##i1712004# 我想要 " + 紫色 + "力量/敏捷/幸運#k" + 黑色 + " 屬性\r\n"
        }
        
        // 惡魔殺手專用選項 
        if (cm.getPlayer().getJob()  == 3001 || cm.getPlayer().getJob()  == 3120 || cm.getPlayer().getJob()  == 3121 || cm.getPlayer().getJob()  == 3122) {
            對話 += "#L5##i1712004# 我想要 " + 暗紅 + "HP#k" + 黑色 + " 屬性\r\n"
        } 
        cm.sendOkS( 對話, 0x04, 9000030);
    } 
    else if (status == 1) {
        // 檢查裝備欄空間 
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot(); 
        if (leftslot < 2) {
            cm.sendOkS("#fs15##r 請確保裝備欄有至少2格空間#k", 0x04, 9000030);
            cm.dispose(); 
            return;
        }
        
        // 根據選擇設定屬性標記 
        if (selection == 0) {
            力量 = 1;
            確認訊息 = "#fs35##fn나눔고딕 Extrabold#" + 紅色 + "'力量(STR)' #fs15#" + 黑色 + " 確定選擇這個屬性嗎?\r\n"
            cm.sendYesNoS( 確認訊息, 0x04, 9000030);
        } 
        else if (selection == 1) {
            敏捷 = 1;
            確認訊息 = "#fs35##fn나눔고딕 Extrabold#" + 藍色 + "'敏捷(DEX)' #fs15#" + 黑色 + " 確定選擇這個屬性嗎?\r\n"
            cm.sendYesNoS( 確認訊息, 0x04, 9000030);
        }
        else if (selection == 2) {
            智力 = 1;
            確認訊息 = "#fs35##fn나눔고딕 Extrabold#" + 黃色 + "'智力(INT)' #fs15#" + 黑色 + " 確定選擇這個屬性嗎?\r\n"
            cm.sendYesNoS( 確認訊息, 0x04, 9000030);
        }
        else if (selection == 3) {
            幸運 = 1;
            確認訊息 = "#fs35##fn나눔고딕 Extrabold#" + 綠色 + "'幸運(LUK)' #fs15#" + 黑色 + " 確定選擇這個屬性嗎?\r\n"
            cm.sendYesNoS( 確認訊息, 0x04, 9000030);
        }
        else if (selection == 4) {
            傑諾 = 1;
            確認訊息 = "#fs28##fn나눔고딕 Extrabold#" + 紫色 + "'力量/敏捷/幸運' #fs15#" + 黑色 + " 確定選擇這個屬性嗎?\r\n"
            cm.sendYesNoS( 確認訊息, 0x04, 9000030);
        }
        else if (selection == 5) {
            惡魔殺手 = 1;
            確認訊息 = "#fs35##fn나눔고딕 Extrabold#" + 暗紅 + "'HP' #fs15#" + 黑色 + " 確定選擇這個屬性嗎?\r\n"
            cm.sendYesNoS( 確認訊息, 0x04, 9000030);
        }
    } 
    else if (status == 2) {
        // 處理各屬性強化結果 
        if (力量 == 1) {
            結果訊息 = "#fs15#" + 藍色 + "#z1712004##k" + 黑色 + " 裝備已成功強化! 請檢查您的物品欄\r\n\r\n"
            結果訊息 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
            結果訊息 += "#i1712004# #b#z1712004##k\r\n"
            結果訊息 += "" + 黑色 + "強化等級 : 20\r\n"
            結果訊息 += "" + 黑色 + "成長值 : MAX\r\n"
            結果訊息 += "" + 紅色 + "力量 : +2200\r\n"
            結果訊息 += "" + 紅色 + "ARC : +220\r\n"
            cm.sendOkS( 結果訊息, 0x04, 9000030);
            cm.gainItem(2430049,  -1);
            var 裝備 = MapleItemInformationProvider.getInstance().getEquipById(1712004); 
            裝備.setStr(2200);
            裝備.setArcLevel(20);
            裝備.setArc(220);
            MapleInventoryManipulator.addbyItem(cm.getClient(),  裝備);
            cm.dispose(); 
            return;
        }
        // 其他屬性處理邏輯相同...
    }
} 
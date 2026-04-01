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
 
// 顏色代碼定義 
검정 = "#fc0xFF191919#";
초록 = "#fc0xFF47C83E#";
연파랑 = "#fc0xFF4641D9#";
빨강 = "#fc0xFFF15F5F#";
노랑 = "#fc0xFFF2CB61#";
파랑 = "#fc0xFF4374D9#";
보라 = "#fc0xFF8041D9#";
검빨강 = "#fc0xFFCC3D3D#";
 
// 屬性選擇標記 
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
 
    // 主選擇界面 
    if (status == 0) {
        var msg = "#fs15#" + 연파랑 + "#z1712005##k " + 검정 + "請選擇要賦予裝備的屬性：\r\n"
        msg += "#fc0xFFD5D5D5#───────────────────────────#k" + 검정 + "\r\n";
        msg += "#L0##i1712005# 我要賦予" + 빨강 + "力量(STR)屬性#k" + 검정 + "\r\n";
        msg += "#L1##i1712005# 我要賦予" + 파랑 + "敏捷(DEX)屬性#k" + 검정 + "\r\n";
        msg += "#L2##i1712005# 我要賦予" + 노랑 + "智力(INT)屬性#k" + 검정 + "\r\n";
        msg += "#L3##i1712005# 我要賦予" + 초록 + "幸運(LUK)屬性#k" + 검정 + "\r\n";
        
        // 職業專用選項 
        if (cm.getPlayer().getJob()  == 3600 || cm.getPlayer().getJob()  == 3610 || cm.getPlayer().getJob()  == 3611 || cm.getPlayer().getJob()  == 3612) {
            msg += "#L4##i1712005# 我要賦予" + 보라 + "STR/DEX/LUK複合屬性#k" + 검정 + "\r\n";
        }
        if (cm.getPlayer().getJob()  == 3001 || cm.getPlayer().getJob()  == 3120 || cm.getPlayer().getJob()  == 3121 || cm.getPlayer().getJob()  == 3122) {
            msg += "#L5##i1712005# 我要賦予" + 검빨강 + "HP屬性#k" + 검정 + "\r\n";
        } 
        cm.sendOkS(msg,  0x04, 9000030);
    } 
    // 確認界面
    else if (status == 1) {
        // 檢查裝備欄空間 
        var leftslot = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot(); 
        if (leftslot < 2) {
            cm.sendOkS("#fs15##r 請確保裝備欄至少有2格空間#k", 0x04, 9000030);
            cm.dispose(); 
            return;
        }
 
        // 根據選擇設定屬性標記
        switch(selection) {
            case 0:
                힘 = 1;
                msg = "#fs35##fn微軟正黑體#" + 빨강 + "'力量(STR)' #fs15#" + 검정 + "屬性確認\r\n";
                break;
            case 1:
                덱스 = 1;
                msg = "#fs35##fn微軟正黑體#" + 파랑 + "'敏捷(DEX)' #fs15#" + 검정 + "屬性確認\r\n";
                break;
            case 2:
                인트 = 1;
                msg = "#fs35##fn微軟正黑體#" + 노랑 + "'智力(INT)' #fs15#" + 검정 + "屬性確認\r\n";
                break;
            case 3:
                럭 = 1;
                msg = "#fs35##fn微軟正黑體#" + 초록 + "'幸運(LUK)' #fs15#" + 검정 + "屬性確認\r\n";
                break;
            case 4:
                제논 = 1;
                msg = "#fs28##fn微軟正黑體#" + 보라 + "'STR/DEX/LUK' #fs15#" + 검정 + "複合屬性確認\r\n";
                break;
            case 5:
                데몬어벤져 = 1;
                msg = "#fs35##fn微軟正黑體#" + 검빨강 + "'HP' #fs15#" + 검정 + "屬性確認\r\n";
                break;
        }
        cm.sendYesNoS(msg  + "確定要賦予這個屬性嗎？", 0x04, 9000030);
    }
    // 執行賦予
    else if (status == 2) {
        var itemId = 1712005;
        var arcaneLevel = 20;
        var arcanePower = 220;
        
        // 生成結果訊息
        var resultMsg = "#fs15#" + 파랑 + "#z" + itemId + "##k" + 검정 + " 已成功強化！請檢查您的裝備欄。\r\n\r\n";
        resultMsg += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"; 
        resultMsg += "#i" + itemId + "# #b#z" + itemId + "##k\r\n";
        resultMsg += 검정 + "成長等級 : " + arcaneLevel + "\r\n";
        resultMsg += 검정 + "成長值 : MAX\r\n";
 
        // 創建裝備實例
        var equip = MapleItemInformationProvider.getInstance().getEquipById(itemId); 
        equip.setArcLevel(arcaneLevel); 
        equip.setArc(arcanePower); 
 
        // 根據選擇賦予不同屬性 
        if (힘 == 1) {
            resultMsg += 빨강 + "STR : +2200\r\n";
            equip.setStr(2200); 
        } 
        else if (덱스 == 1) {
            resultMsg += 파랑 + "DEX : +2200\r\n";
            equip.setDex(2200); 
        }
        else if (인트 == 1) {
            resultMsg += 노랑 + "INT : +2200\r\n";
            equip.setInt(2200); 
        }
        else if (럭 == 1) {
            resultMsg += 초록 + "LUK : +2200\r\n";
            equip.setLuk(2200); 
        }
        else if (제논 == 1) {
            resultMsg += 보라 + "STR : +858\r\n";
            resultMsg += 보라 + "DEX : +858\r\n";
            resultMsg += 보라 + "LUK : +858\r\n";
            equip.setStr(858); 
            equip.setDex(858); 
            equip.setLuk(858); 
        }
        else if (데몬어벤져 == 1) {
            resultMsg += 검빨강 + "HP : +38500\r\n";
            equip.setHp(38500); 
        }
 
        resultMsg += 검정 + "ARC : +" + arcanePower + "\r\n";
        
        // 執行操作
        cm.gainItem(2430051,  -1); // 消耗材料
        MapleInventoryManipulator.addbyItem(cm.getClient(),  equip);
        cm.sendOkS(resultMsg,  0x04, 9000030);
        cm.dispose(); 
    }
}
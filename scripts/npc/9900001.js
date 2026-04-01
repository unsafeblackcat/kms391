importPackage(java.lang); 
var enter = "\r\n";
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose(); 
        return;
    }
    if (status == 0) {
        // GM權限檢查
        if (!cm.getPlayer().isGM())  {
            cm.sendOk(" 無權限操作");
            cm.dispose(); 
            return;
        }
        var t = "請選擇要強化的裝備類型：" + enter + enter;
        t += "#L1#裝備#l" + enter;
        t += "#L6#裝飾#l";
        cm.sendNext(t); 
    } else if (status == 1) {
        types = selection;
        var t = "請選擇要強化的物品：" + enter;
        // 遍歷對應背包欄位
        for (i = 0; i < cm.getInventory(types).getSlotLimit();  i++) {
            var item = cm.getInventory(types).getItem(i); 
            if (item != null) {
                t += "#L" + i + "# #i" + item.getItemId()  + "# #z" + item.getItemId()  + "#" + enter;
            }
        }
        cm.sendNext(t); 
    } else if (status == 2) {
        sel = selection;
        var item = cm.getInventory(types).getItem(sel); 
        var t = "#i" + item.getItemId()  + "# 要強化什麼屬性？" + enter + enter;
        t += "#L0#傳說6排潛能#l" + enter;
        t += "#L3#冷卻時間-2秒#l" + enter;
        t += "#L4#掉寶率6排#l" + enter;
        t += "#L5#楓幣獲得6排#l" + enter;
        t += "#L1#傷害100% BOSS傷100% 無視防禦100%#l" + enter;
        t += "#L2#傷害125% BOSS傷250% 無視防禦250%#l";
        cm.sendNext(t); 
    } else if (status == 3) {
        what = selection;
        var item = cm.getInventory(types).getItem(sel); 
        
        // 裝飾品設定等級要求 
        if (types == 6) {
            item.setReqLevel(100); 
        }
 
        // 強化選項處理 
        if (what == 0) {
            item.setState(20);  // 傳說級 
            item.setPotential1(40056);  // 傳說潛能ID 
            item.setPotential2(40056); 
            item.setPotential3(40056); 
            item.setPotential4(40056); 
            item.setPotential5(40056); 
            item.setPotential6(40056); 
        }
        else if (what == 3) {
            item.setState(20); 
            item.setPotential1(40557);  // 冷卻時間潛能ID
            item.setPotential2(40557); 
            item.setPotential3(40557); 
            item.setPotential4(40557); 
            item.setPotential5(40557); 
            item.setPotential6(40557); 
        }   
        else if (what == 5) {
            item.setState(20); 
            item.setPotential1(40656);  // 楓幣獲得潛能ID
            item.setPotential2(40656); 
            item.setPotential3(40656); 
            item.setPotential4(40656); 
            item.setPotential5(40656); 
            item.setPotential6(40656); 
        }    
        else if (what == 4) {
            item.setState(20); 
            item.setPotential1(40650);  // 掉寶率潛能ID
            item.setPotential2(40650); 
            item.setPotential3(40650); 
            item.setPotential4(40650); 
            item.setPotential5(40650); 
            item.setPotential6(40650); 
        }
        else if (what == 1) {
            item.setTotalDamage(100);   // 總傷害%
            item.setBossDamage(100);    // BOSS傷害%
            item.setIgnorePDR(100);     // 無視防禦%
        }
        else if (what == 2) {
            var intt = 250;
            item.setTotalDamage(125); 
            item.setBossDamage(intt); 
            item.setIgnorePDR(intt); 
        }
 
        // 重新添加物品到背包
        if (types == 1) {
            cm.getPlayer().forceReAddItem(item,  Packages.client.inventory.MapleInventoryType.EQUIP); 
        } else if (types == 6) {
            cm.getPlayer().forceReAddItem(item,  Packages.client.inventory.MapleInventoryType.DECORATION); 
        }
 
        var t = "強化完成";
        cm.sendNext(t); 
        cm.dispose(); 
    }
}
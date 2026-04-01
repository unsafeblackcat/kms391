var status = -1;
importPackage(Packages.server);  
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose(); 
        return;
    }
    if (mode == 0) status--;
    if (mode == 1) status++;
 
    // 階段0：裝備選擇界面 
    if (status == 0) {
        var validEquips = [];
        var txt = "#fs15##b#h 0##k，請選擇要強化的裝備：\r\n";
        txt += "#r※ 可強化條件：25星以上裝備 或 15星驚奇強化卷軸裝備#k\r\n\r\n";
        
        // 掃描裝備欄位並過濾 
        for (var i = 0; i < cm.getInventory(1).getSlotLimit();  i++) {
            var equip = cm.getInventory(1).getItem(i); 
            if (equip != null) {
                var isAmazing = equip.isAmazingequipscroll  ? equip.isAmazingequipscroll()  : false;
                var star = equip.getEnhance(); 
                
                // 修正判定條件：明確區分兩種強化途徑 
                if ((star >= 25 && star < 30) || (star >= 15 && isAmazing && star < 30)) {
                    validEquips.push(i); 
                    txt += "#L"+i+"##i"+equip.getItemId()+"#  #z"+equip.getItemId()+"#  ";
                    txt += "(當前:"+star+"星" + (isAmazing ? " 驚奇強化" : "") + ")#l\r\n";
                }
            }
        }
        
        if (validEquips.length  == 0) {
            cm.sendOkS("#fs15#未發現可强化裝備！\r\n需要：#b25星以上#k 或 #b15星驚奇强化裝備#k", 0x86);
            cm.dispose(); 
        } else {
            cm.sendSimpleS(txt,0x86); 
        }
    } 
    
    // 階段1：執行強化 
    else if (status == 1) {
        var item = cm.getInventory(1).getItem(selection); 
        if (item == null) {
            cm.dispose(); 
            return;
        }
        
        // 雙重驗證強化條件 
        var star = item.getEnhance(); 
        var isAmazing = item.isAmazingequipscroll  ? item.isAmazingequipscroll()  : false;
        var isValid = (star >= 25 && star < 30) || (star >= 15 && isAmazing && star < 30);
        
        if (!isValid) {
            cm.sendOkS(" 該裝備不符合強化條件！", 0x86);
        } 
        else if (star >= 30) {
            cm.sendOkS(" 該裝備已達30星上限", 0x86);
        }
        else if (!cm.haveItem(4001109))  {
            cm.sendOkS(" 需要 #v4001109##z4001109#\r\n獲取途徑：史烏/戴米安/露希妲等BOSS", 0x86);
        }
        else {
            // 強化效果賦予 
            item.addStr(30); 
            item.addDex(30); 
            item.addInt(30); 
            item.addLuk(30); 
            item.addHp(900); 
            item.addWatk(15); 
            item.addMatk(15); 
            item.setEnhance(star  + 1);
            
            // 消耗材料並更新裝備 
            cm.gainItem(4001109,  -1);
            cm.getPlayer().forceReAddItem(item,  Packages.client.inventory.MapleInventoryType.EQUIP); 
            cm.sendOkS("#b 強化成功！#k\r\n當前星力：#e"+item.getEnhance()+" 星#n\r\n獲得：#c全屬性+30 HP+900 攻魔+15#", 0x86);
        }
        cm.dispose(); 
    }
}
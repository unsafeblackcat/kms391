var status = -1;
importPackage(Packages.server); 
importPackage(Packages.constants); 
 
var 後援點 = 5000;
var 宣傳點 = 10000;
 
var p, point, need;
 
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
        var txt = "#fs15#您好 #b#h 0##k！您知道可以重置徽章、徽章和副武器（不包括刀片）的基本選項嗎？強化時 #b每個屬性最多30~150，HP 10000，攻擊力/魔力150#k。請選擇要重置選項的物品。\r\n";
        for (i = 0; i < cm.getInventory(1).getSlotLimit();  i++) {
            if (cm.getInventory(1).getItem(i)  != null) {
                if ((cm.getInventory(1).getItem(i).getItemId()  >= 1190000 && cm.getInventory(1).getItem(i).getItemId()  < 1200000) || 
                    (cm.getInventory(1).getItem(i).getItemId()  >= 1182000 && cm.getInventory(1).getItem(i).getItemId()  < 1183000) || 
                    (cm.getInventory(1).getItem(i).getItemId()  >= 1098000 && cm.getInventory(1).getItem(i).getItemId()  < 1100000) || 
                    GameConstants.isSecondaryWeapon(cm.getInventory(1).getItem(i).getItemId()))  {
                    txt += "#L"+i+"# #i"+cm.getInventory(1).getItem(i).getItemId()+"#  #b#z"+cm.getInventory(1).getItem(i).getItemId()+"#\r\n"; 
                }
            }
        }
        cm.sendSimple(txt); 
    } else if (status == 1) {
        var msg = "您想使用哪種點數來賦予潛在能力？\r\n";
        msg += "#L1#後援點 ("+後援點+"P)\r\n";
        msg += "#L2#宣傳點 ("+宣傳點+"P)\r\n";
        cm.sendSimple(msg); 
    } else if (status == 2) {
        sel = selection;
        p = sel;
        point = sel == 1 ? cm.getPlayer().getDonationPoint()  : sel == 2 ? cm.getPlayer().getHPoint()  : -1;
        need = sel == 1 ? 後援點 : sel == 2 ? 宣傳點 : -1;
 
        if (point < 0 || need < 0) {
            cm.dispose(); 
            return;
        }
        if (point < need) {
            cm.sendOk(" 點數不足。");
            cm.dispose(); 
            return;
        }
        item = cm.getInventory(1).getItem(selection); 
        if (item == null) {
            return;
        }
        item.setStr(Randomizer.rand(30,  150));
        item.setDex(Randomizer.rand(30,  150));
        item.setInt(Randomizer.rand(30,  150));
        item.setLuk(Randomizer.rand(30,  150));
        item.setHp(Randomizer.rand(3000,  10000));
        item.setWatk(Randomizer.rand(30,  150));
        item.setMatk(Randomizer.rand(30,  150));
        if (p == 1)
            cm.getPlayer().gainDonationPoint(-need); 
        else 
            cm.getPlayer().gainHPoint(-need); 
        cm.getPlayer().forceReAddItem(item,  Packages.client.inventory.MapleInventoryType.getByType(1)); 
        cm.sendOk(" 已重置物品選項。感謝您的使用。");
        cm.dispose(); 
    }
}
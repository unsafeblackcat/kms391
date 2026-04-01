var enter = "\r\n";
var seld = -1;
var seld2 = -1;

var 徽章1 = 30, 徽章2 = 10;

var 口袋1 = 30, 口袋2 = 10;

var 現金1 = 1, 現金2 = 1;

var p = -1, need = -1;
var pt = "";

function start() {
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, sel) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
        }
    if (status == 0) {
        var msg ="#fs15#我可以爲沒有潛在能力的口袋物品賦予潛在能力 #fs15##b"+enter;
        //msg += "#L1#徽章道具潛在能力賦予"+enter;
        msg += "#L2#口袋道具潛在能力賦予"+enter;
        //msg += "#L6#現金道具潛在能力賦予"+enter;
        cm.sendSimple(msg);
    } else if (status == 1) {
        seld = sel;
        var msg = "#fs15# 可以利用兩種積分賦予潛在能力\r\n";
        贊助 = sel == 1 ? 徽章1 : sel == 2 ? 口袋1 : 現金1;
        推廣 = sel == 1 ? 徽章2 : sel == 2 ? 口袋2 : 現金2;
        msg += "#L1#贊助點 ("+贊助+"P)\r\n";
        msg += "#L2#推廣點 ("+推廣+"P)\r\n";
        cm.sendSimple(msg);
    } else if (status == 2) {
        seld2 = sel;
        p = seld2 == 1 ? cm.getPlayer().getDonationPoint() : cm.getPlayer().getHPoint();
        pt = seld2 == 1 ? "贊助" : "推廣";
        need = (seld2 == 1 && seld == 1) ? 徽章1 : (seld2 == 1 && seld == 2) ? 口袋1 : (seld2 == 1 && seld == 3) ? 現金1 : (seld2 == 2 && seld == 1) ? 徽章2 : (seld2 == 2 && seld == 2) ? 口袋2 : 現金2;
        seldi = seld == 1 ? "徽章系统" : seld == 2 ? "口袋物品" : "现金道具";
        ic = seld == 1 ? 118 : 116;

        if (p < need) {
            cm.sendOk("#fs15#積分不足.");
            cm.dispose();
            return;
        }

        

        var msg ="#fs15#我可以賦予 #b#h 0##k! "+seldi+"潛在能力 請選擇想要賦予潛在能力的物品.\r\n#r(請將口袋放在裝備欄上)#k"+enter;
        msg += "#fs15#現在 #b#h 0##k #d"+pt+"積分#k是 "+p+"P .#fs15#"+enter+enter;
        switch (seld) {
            case 1:
                for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                    item = cm.getInventory(1).getItem(i);
                            if (item == null) continue;
                    if (Math.floor(item.getItemId() / 10000) == ic) msg += "#L"+i+"# #i"+item.getItemId()+"# #b#z"+item.getItemId()+"#\r\n";
                }
            break;
            case 2:
                for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                    item = cm.getInventory(1).getItem(i);
                            if (item == null) continue;
                    if (Math.floor(item.getItemId() / 10000) == ic) msg += "#L"+i+"# #i"+item.getItemId()+"# #b#z"+item.getItemId()+"#\r\n";
                }
            break;
            case 3:
                for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                    item = cm.getInventory(1).getItem(i);
                            if (item == null) continue;
                    if (cm.isCash(item.getItemId())) msg += "#L"+i+"# #i"+item.getItemId()+"# #b#z"+item.getItemId()+"#\r\n";
                }
            break;
        }
        cm.sendSimple(msg);
    } else if (status == 3) {
        if (p < need) {
            cm.sendOk("#fs15#積分不足.");
            cm.dispose();
            return;
        }
        if (seld2 == 1) cm.getPlayer().gainDonationPoint(-30);
        else cm.getPlayer().gainHPoint(-10);
        item = cm.getInventory(1).getItem(sel);

        if (item == null) {
            cm.dispose();
            return;
        }
        item.setState(17);
        item.setLines(3);
        item.setPotential1(10041);
        item.setPotential2(10042);
        item.setPotential3(10043);
        item.setPotential4(10041);
        item.setPotential5(10042);
        item.setPotential6(10043);
        cm.sendOk("#fs15#賦予物品潜在能力。 謝謝您的使用.");
        cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(1));
        cm.dispose();
    }
}
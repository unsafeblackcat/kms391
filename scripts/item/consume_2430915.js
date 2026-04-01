importPackage(Packages.client.inventory);
var need = 2430915;
var seld = -10;

function start() {
    St = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        St++;
    }
    if (St == 0) {
      var text = "#fs15#請選擇想要交換的數量#k.\r\n";
      text += "#L0##b#i5062500##z5062500#\r\n";

      cm.sendYesNo(text);
    } else if (St == 1) {
        seld = selection;
        cm.sendGetNumber("#fs15#請選擇您要使用的物品數量. \r\n#r剩餘特殊物品格 : " + cm.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot(), 1, 1, 100);
    } else if (St == 2) {
            if (cm.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot() < selection) {
                cm.sendOk("#fs15#好像沒有足够的庫存空間。\r\n#b請清空裝備#k欄上的庫存空間。.");
                cm.dispose();
                return;
            }
        if (!cm.haveItem(need, 10*selection)) {
            cm.sendOk("#fs15#兌換券不足.");
            cm.dispose();
            return;
        }
        cm.gainItem(5062500+seld,1*selection);
        cm.gainItem(need,-10*selection);
        cm.sendOk("#fs15#交換已完成.");
        cm.dispose();
    }
}
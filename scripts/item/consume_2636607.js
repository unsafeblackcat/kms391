importPackage(Packages.client.inventory);
var need = 2636607;
var seld = -2;

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
      text += "#L0##b#i2635747##z2635747#\r\n";

      cm.sendYesNo(text);
    } else if (St == 1) {
        seld = selection;
        cm.sendGetNumber("#fs15#請選擇想要使用的物品數量. \r\n當前還剩 : #r#e" + cm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() + "#k#n 個消耗物品空格", 1, 1, 100);
    } else if (St == 2) {
            if (cm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() < selection) {
                cm.sendOk("#fs15#好像沒有足够的庫存空間。\r\n#b請清空消耗物品#k欄上的庫存空間。.");
                cm.dispose();
                return;
            }
        if (!cm.haveItem(need, 10*selection)) {
            cm.sendOk("#fs15#兌換券不足.");
            cm.dispose();
            return;
        }
        cm.gainItem(2635747+seld,1*selection);
        cm.gainItem(need,-2*selection);
        cm.sendOk("#fs15#交換已完成.");
        cm.dispose();
    }
}
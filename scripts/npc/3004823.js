importPackage(Packages.server);
var status;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode <= 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        talk = "使用現金倉庫\r\n"
        talk += "#L0# 從現金倉庫取出道具\r\n"
        talk += "#L1# 將道具存入現金倉庫"
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        if (selection == 0) {
            talk = "請選擇要取出的道具。\r\n\r\n"
            storage = cm.getPlayer().getStorage();
            arr = storage.getItems();
            count = 0;
            for (i = 0; i < arr.length; i++) {
                if (cm.isCash(arr[i].getItemId())) {
                    count++;
                    talk += "#L" + i + "# #i" + arr[i].getItemId() + "##l";
                    if (i % 5 == 4) {
                        talk += "\r\n";
                    }
                }
            }
            if (count == 0) {
                cm.sendOk("沒有可取出的道具。");
                cm.dispose();
                return;
            } else {
                cm.sendSimple(talk);
            }
        } else {
            talk = "請選擇道具。\r\n\r\n"
            for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                if (cm.getInventory(1).getItem(i) != null && cm.isCash(cm.getInventory(1).getItem(i).getItemId())) {
                    talk += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "##k#l\r\n"
                }
            }
            cm.sendSimple(talk);
        }
    } else if (status == 2) {
        if (st == 0) {
            MapleInventoryManipulator.addbyItem(cm.getClient(), cm.getPlayer().getStorage().getItems().get(selection));
            cm.getPlayer().getStorage().takeOut(selection);
            cm.sendOk("已成功從現金倉庫取出道具。");
            cm.dispose();
            return;
        } else {
            cm.getPlayer().getStorage().store(cm.getInventory(1).getItem(selection));
            MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, selection, cm.getInventory(1).getItem(selection).getQuantity(), false);
            cm.sendOk("已成功存入現金倉庫。");
            cm.dispose();
            return;
        }
    }
}
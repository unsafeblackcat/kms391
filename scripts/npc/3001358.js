importPackage(Packages.constants);
var status = -1;

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
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        talk = "可以丟棄現金裝備道具及現金道具。請選擇您想要的選項。\r\n"
        talk += "#r(丟棄道具時不會掉落而會消失，且無法復原，請留意！)#k\r\n\r\n"
        talk += "#L0# #b現金裝備道具#l\r\n";
        talk += "#L1# 現金道具#k#l\r\n";
        cm.sendSimple(talk);
    } else if (status == 1) {
        st2 = selection;
        if (selection == 0) {
            talk = "請選擇您想丟棄的現金裝備道具。\r\n\r\n"
            for (i = 0; i < cm.getInventory(6).getSlotLimit(); i++) {
                if (cm.getInventory(6).getItem(i) != null && cm.isCash(cm.getInventory(6).getItem(i).getItemId())) {
                    talk += "#L" + i + "# #i" + cm.getInventory(6).getItem(i).getItemId() + "# #b#z" + cm.getInventory(6).getItem(i).getItemId() + "##k#l\r\n"
                }
            }
        } else {
            talk = "請選擇您想丟棄的現金道具。但寵物僅在#r解除裝備的狀態下#k才能從背包中移除。\r\n\r\n"
            for (i = 0; i < cm.getInventory(5).getSlotLimit(); i++) {
                if (cm.getInventory(5).getItem(i) != null) {
                    talk += "#L" + i + "# #i" + cm.getInventory(5).getItem(i).getItemId() + "# #b#z" + cm.getInventory(5).getItem(i).getItemId() + "##k#l\r\n"
                }
            }
        }
        cm.sendSimple(talk);
    } else if (status == 2) {
        st = selection;
        // 新增空值檢查
        var iv = st2 == 0 ? 6 : 5;  // 定義 iv 變量
        var item = cm.getInventory(iv).getItem(st);
        if (item == null) {
            cm.sendOk("您選擇的道具槽位為空，請重新操作。");
            cm.dispose();
            return;
        }
        
        talk = "確定要從背包中移除該道具嗎？\r\n"
        talk += "#r(丟棄道具時不會掉落而會消失，且無法復原，請留意！)#k\r\n\r\n"
        talk += "#i" + item.getItemId() + "# #b#z" + item.getItemId() + "##k"
        cm.sendYesNo(talk);
    } else if (status == 3) {
        // 新增空值檢查
        var iv = st2 == 0 ? 6 : 5;  // 確保 iv 變量在此處也被定義
        var item = cm.getInventory(iv).getItem(st);
        if (item == null) {
            cm.sendOk("您選擇的道具槽位已為空，可能已被其他操作移除。");
            cm.dispose();
            return;
        }
        
        if (st2 == 0) {
            Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.CODY, st, item.copy().getQuantity(), true);
        } else {
            if (GameConstants.isPet(item.getItemId())) {
                for (i = 0; i < cm.getPlayer().getPets().length; i++) {
                    if (cm.getPlayer().getPets()[i] != null) {
                        if (cm.getPlayer().getPets()[i].getInventoryPosition() == st) {
                            cm.sendOk("因爲該寵物處於裝備狀態，無法從背包中移除。");
                            cm.dispose();
                            return;
                        }
                    }
                }
            }
            Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.CASH, st, item.copy().getQuantity(), true);
        }
        cm.sendOk("已成功從背包中移除該道具。");
        cm.dispose();
        return;
    }
}
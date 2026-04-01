var status = -1;
var item;
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    status++;
 
    if (status == 0) {
        text = "#fc0xFFFF9933#是否要使用「回歸卷軸」效果，取消原本的咒文書使用？消失的咒文書將無法恢復。#k\r\n#b點擊<是>將物品恢復至使用咒文書前的狀態。#k\r\n\r\n";
        if (cm.getPlayer().returnscroll.getPosition()  < 0) {
            item = cm.getPlayer().getInventory(-1).getItem(cm.getPlayer().returnscroll.getPosition()); 
        } else {
            item = cm.getPlayer().getInventory(1).getItem(cm.getPlayer().returnscroll.getPosition()); 
        }
        if (cm.getPlayer().returnscroll.getStr()  - item.getStr()  != 0) {
            text += "#d#e變動的力量數值 : " + -(cm.getPlayer().returnscroll.getStr()  - item.getStr())  + "#n#k\r\n";
        }
        if (cm.getPlayer().returnscroll.getDex()  - item.getDex()  != 0) {
            text += "#d#e變動的敏捷數值 : " + -(cm.getPlayer().returnscroll.getDex()  - item.getDex())  + "#n#k\r\n";
        }
        if (cm.getPlayer().returnscroll.getInt()  - item.getInt()  != 0) {
            text += "#d#e變動的智力數值 : " + -(cm.getPlayer().returnscroll.getInt()  - item.getInt())  + "#n#k\r\n";
        }
        if (cm.getPlayer().returnscroll.getLuk()  - item.getLuk()  != 0) {
            text += "#d#e變動的幸運數值 : " + -(cm.getPlayer().returnscroll.getLuk()  - item.getLuk())  + "#n#k\r\n";
        }
        if (cm.getPlayer().returnscroll.getWatk()  - item.getWatk()  != 0) {
            text += "#d#e變動的攻擊力數值 : " + -(cm.getPlayer().returnscroll.getWatk()  - item.getWatk())  + "#n#k\r\n";
        }
        if (cm.getPlayer().returnscroll.getMatk()  - item.getMatk()  != 0) {
            text += "#d#e變動的魔力數值 : " + -(cm.getPlayer().returnscroll.getMatk()  - item.getMatk())  + "#n#k\r\n";
        }
        if (cm.getPlayer().returnscroll.getHp()  - item.getHp()  != 0) {
            text += "#d#e變動的體力數值 : " + -(cm.getPlayer().returnscroll.getHp()  - item.getHp())  + "#n#k\r\n";
        }
        if (cm.getPlayer().returnscroll.getMp()  - item.getMp()  != 0) {
            text += "#d#e變動的魔力值數值 : " + -(cm.getPlayer().returnscroll.getMp()  - item.getMp())  + "#n#k\r\n";
        }
        cm.sendYesNoS(text,  1);
    } else if (status == 1) {
        cm.getPlayer().returnscroll.setFlag(cm.getPlayer().returnscroll.getFlag()  - 0x8000);
        if (mode == 1) {
            item.set(cm.getPlayer().returnscroll); 
            cm.getPlayer().forceReAddItem(item,  Packages.client.inventory.MapleInventoryType.getByType(item.getPosition()  < 0 ? -1 : 1));
            cm.sendOk(" 已將物品恢復至使用咒文書前的狀態。");
        } else {
            item.setUpgradeSlots(item.getUpgradeSlots()  - 1);
            cm.getPlayer().forceReAddItem(item,  Packages.client.inventory.MapleInventoryType.getByType(item.getPosition()  < 0 ? -1 : 1));
            cm.sendOk(" 未恢復物品狀態。");
        }
        cm.getPlayer().returnscroll  = null;
        cm.dispose(); 
    }
}
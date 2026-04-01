importPackage(Packages.tools.packet); 
importPackage(java.lang); 
importPackage(java.util); 
importPackage(java.awt); 
importPackage(Packages.server); 
importPackage(Packages.constants); 
importPackage(Packages.client.inventory); 
var status = -1;
 
function start() {
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose(); 
        return;
    }
    if (mode == 1) {
        status++;
    }
    wList = [];
    wGain = [];
    if (status == 0) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot(); 
        if (leftslot < 1) {
            cm.sendOk(" #fs15#請清空背包後再與我對話。");
            cm.dispose(); 
            return;
        }
    if (!cm.haveItem(2634472,  10)) {
        cm.sendOk("#fs15##r 材料不足10個。#k");
        cm.dispose(); 
    }
        getItem();
        selStr = "\r\n#fs13##b#i2634472##z2634472##k。#r請從下方選擇想要的裝備。\r\n#r注意：選擇後將直接發放至背包。#k\r\n\r\n";
        for (i = 0; i < wList.length;  i++) {
            selStr += "#L" + i + "##i" + wList[i] + ":# #t" + wList[i] + ":##l\r\n";
        }
        cm.sendYesNo(selStr); 
 
    } else if (status == 1) {
    cm.gainItem(2634472,  -10);
        getItem();
    var item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(wList[selection]); 
    item.setRandomStat(); 
    Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(),  item);
 
        var selStr2 = "#fs15#已發放以下物品：\r\n\r\n";
        selStr2 += "#b#i" + wList[selection] + "##t" + wList[selection] + "#";
        
        cm.sendSimpleS(selStr2,  4, 2007);
        cm.dispose(); 
    }
}
 
function getItem() {
    wList.push(1005980), 
    wList.push(1005981), 
    wList.push(1005982), 
    wList.push(1005983), 
    wList.push(1005984), 
    wList.push(1042433), 
    wList.push(1042434), 
    wList.push(1042435), 
    wList.push(1042436), 
    wList.push(1042437), 
    wList.push(1062285), 
    wList.push(1062286), 
    wList.push(1062287), 
    wList.push(1062288), 
    wList.push(1062289); 
    wList.push(1152212); 
    wList.push(1152213); 
    wList.push(1152214); 
    wList.push(1152215); 
    wList.push(1152216); 
}
// 冒險島裝備箱兌換系統 - 繁體中文版
// 最後更新：2025年7月18日（農曆乙巳年六月廿四）
 
importPackage(java.util); 
importPackage(java.io); 
importPackage(Packages.provider); 
importPackage(Packages.server); 
importPackage(Packages.constants); 
importPackage(Packages.client.inventory); 
 
var line = "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
var enter = "\r\n";
var itemlist = [1113513, 1113512, 1113511, 1113510, 1002026, 1022073, 1012260, 1122058, 1122007, 1132145, 1042352, 1062000, 1072369, 1032160, 1152074, 1082145, 1102369, 1662183, 1672028, 1162004, 1190313, 1182263];
var etcitem = [];
var boxcode = 2435937;
 
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
        if (itemlist.length  <= 0) {
            cm.sendOk(" 請聯繫管理員處理異常情況。");
            cm.dispose(); 
            return;
        }
        
        // 檢查裝備欄空間 
        slot = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getNumFreeSlot(); 
        if (slot < itemlist.length)  {
            cm.sendOk(" 請預留至少 " + itemlist.length  + " 格裝備欄空間。");
            cm.dispose(); 
            return;
        }
        
        // 非GM需消耗兌換箱 
        if (!cm.getPlayer().isGM())  {
            cm.gainItem(boxcode,  -1);
        }
        
        // 生成獎勵列表訊息 
        var t = "已獲得以下裝備：" + enter;
        t += line;
        for (i = 0; i < itemlist.length;  i++) {
            t += "#i" + itemlist[i] + "# #b#e#z" + itemlist[i] + "##k#n" + enter;
            itemoption(itemlist[i], 40056, 40056, 40056, 40056, 40056, 40056);
        }
        
        // 發放其他道具（當前etcitem為空陣列）
        for (i = 0; i < etcitem.length;  i++) {
            t += "#i" + etcitem[i][0] + "# #b#e#z" + etcitem[i][0] + "# " + etcitem[i][1] + "個#k#n" + enter;
            cm.gainItem(etcitem[i][0],  etcitem[i][1]);
        }
        
        cm.sendOk(t); 
        cm.dispose(); 
    }
}
 
// 職業檢查函數（當前未實際使用）
function jobcheck(job) {
    if (GameConstants.isWarrior(job))  {
        itemlist.push(); 
    } else if (GameConstants.isMagician(job))  {
        itemlist.push(); 
    } else if (GameConstants.isArcher(job))  {
        itemlist.push(); 
    } else if (GameConstants.isThief(job))  {
        itemlist.push(); 
    } else if (GameConstants.isPirate(job))  {
        itemlist.push(); 
    } else {
        itemlist = [];
    }
}
 
// 裝備屬性設置函數 
function itemoption(itemid, pot1, pot2, pot3, pot4, pot5, pot6) {
    item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(itemid); 
    item.setState(20);   // 裝備狀態 
    item.setPotential1(30086);   // 潛在能力1 
    item.setPotential2(30086);   // 潛在能力2
    item.setPotential3(30086);   // 潛在能力3 
    item.setPotential4(20086);   // 附加潛能1
    item.setPotential5(20086);   // 附加潛能2 
    item.setPotential6(20086);   // 附加潛能3
    Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(),  item, false);
}
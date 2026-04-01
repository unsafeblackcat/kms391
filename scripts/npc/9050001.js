importPackage(java.lang); 
importPackage(Packages.server); 
 
var 價格 = 50000;
 
function start() {
    St = -1;
    action(1, 0, 0);
}
 
function action(M, T, S) {
    if(M != 1) {
        cm.dispose(); 
        return;
    }
 
    if(M == 1)
        St++;
 
    if(St == 0) {
        var 數量 = 0;
 
        var 對話 = "#fs15##b要賦予能力的寵物裝備#k請選擇\r\n";
            對話 += "#fs15##r每件物品需要消耗[5萬]贊助點數\r\n\r\n";
        for (i = 0; i < cm.getInventory(5).getSlotLimit();  i++) {
            if (cm.getInventory(6).getItem(i)  != null) {
                if (Math.floor(cm.getInventory(6).getItem(i).getItemId()  / 10000) == 180 && cm.getInventory(6).getItem(i).getState()  != 20) {
                    對話 += "#L" + i + "##e#b#i" + cm.getInventory(6).getItem(i).getItemId()  + "# #z" + cm.getInventory(6).getItem(i).getItemId()  + "# (" + i + "號欄位)#l\r\n";
                    數量++;
                }
            }
        }
        if (數量 <= 0) {
            cm.sendOk(" 請確認是否持有可賦予潛能的寵物裝備。");
            cm.dispose(); 
            return;
        }
        cm.sendSimple( 對話);
    } else if(St == 1){
        if(cm.getPlayer().getDonationPoint()  < 價格) {
            cm.sendOk(" 贊助點數不足！");
            cm.dispose(); 
            return;
        } else {
            vitem = cm.getInventory(6).getItem(S); 
            vitem.setStr(vitem.getStr()  + 50);
            vitem.setDex(vitem.getDex()  + 50);
            vitem.setInt(vitem.getInt()  + 50);
            vitem.setLuk(vitem.getLuk()  + 50);
            vitem.setWatk(vitem.getWatk()  + 50);
            vitem.setMatk(vitem.getMatk()  + 50);
            vitem.setState(20); 
            vitem.setPotential1(40086); 
            vitem.setPotential2(40086); 
            vitem.setPotential3(40086); 
            vitem.setPotential4(0); 
            vitem.setPotential5(0); 
            vitem.setPotential6(0); 
            cm.getPlayer().forceReAddItem(vitem,  Packages.client.inventory.MapleInventoryType.CODY); 
            cm.getPlayer().setDonationPoint(cm.getPlayer().getDonationPoint()  - 價格);
            cm.sendOk(" 該寵物裝備能力賦予已完成。");
            cm.dispose(); 
        }
    }
}
 
function 訊息提示(文字){
    cm.getPlayer().dropMessage(5,  文字);
}
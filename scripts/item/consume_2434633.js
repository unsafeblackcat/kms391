var status;
importPackage(Packages.server); 
importPackage(Packages.client.inventory); 
 
後援點數 = Randomizer.rand(20,  50); // 隨機生成20~50點 
 
function start() {
    status = -1;
    action(1, 1, 0);
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
        var msg = "從贊助錢包中獲得了 " + 後援點數 + " 點後援點數。";
        cm.getPlayer().dropMessage(-8,  msg);
        cm.getPlayer().gainDonationPoint( 後援點數);
        cm.gainItem(2434633,  -1); // 扣除消耗品 
        cm.sendOk("從贊助錢包中獲得了 " + 後援點數 + " 點後援點數.");
        cm.dispose(); 
    }
}
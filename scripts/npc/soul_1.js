importPackage(Packages.server); 
importPackage(java.lang); 
importPackage(Packages.handling.world); 
importPackage(Packages.tools.packet); 
 
var enter = "\r\n";
var seld = -1;
 
// 兌換設定 
var requiredItem = 2431710;  // 需求道具ID
var requiredAmount = 10;     // 需求數量 
 
var baseSoul = 2591171;      // 基礎靈魂石ID（最低階）
var specialSoul = 2591163;   // 特殊靈魂石ID（偉大的靈魂）
var specialChance = 100;     // 特殊機率 10%（100/1000）
 
var hasSpecialChance = false;
 
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
    
    // 階段0：檢查材料
    if (status == 0) {
        if (!cm.haveItem(requiredItem,  requiredAmount)) {
            cm.sendOk(" #fs15#請先收集 #b#i"+requiredItem+"##z"+requiredItem+"##k #b"+requiredAmount+"個#k 才能兌換靈魂石");
            cm.dispose(); 
            return;
        }
 
        // 機率判定（千分之一）
        var roll = Packages.server.Randomizer.rand(1,  1000);
        if (roll <= specialChance)
            hasSpecialChance = true;
            
        cm.sendYesNo(" #fs15#確定要消耗 #b#i"+requiredItem+"##z"+requiredItem+"##k "+requiredAmount+"個 兌換靈魂石嗎？");
    } 
    
    // 階段1：執行兌換
    else if (status == 1) {
        // 再次確認防呆
        if (!cm.haveItem(requiredItem,  requiredAmount)) {
            cm.sendOk(" #fs15#材料不足，請確認持有 #b"+requiredAmount+"個#k #i"+requiredItem+"#");
            cm.dispose(); 
            return;
        }
        
        cm.gainItem(requiredItem,  -requiredAmount); // 扣除材料 
        
        // 特殊靈魂石獲得 
        if (hasSpecialChance) {
            cm.gainItem(specialSoul,  1);
            cm.sendOk(" #fs15#喔喔！獲得了 #b#i"+specialSoul+"##z"+specialSoul+"##k！這比其他靈魂石更強大，請好好珍藏！");
            // 全服公告 
            Packages.handling.world.World.Broadcast.broadcastMessage( 
                Packages.tools.packet.CWvsContext.serverNotice(11,  
                cm.getPlayer().getName()+"  獲得了 "+cm.getItemName(specialSoul)+" ！"));
        } 
        // 普通靈魂石獲得 
        else {
            var soulType = Packages.server.Randomizer.rand(0,7); 
            var awardedSoul = baseSoul + soulType;
 
            cm.gainItem(awardedSoul,  1);
            cm.sendOk(" #fs15#噢～！獲得了 #b#i"+awardedSoul+"##z"+awardedSoul+"##k！好好利用它吧。呵呵呵...");
        }
        cm.dispose(); 
    }
}
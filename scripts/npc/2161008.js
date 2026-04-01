var status = -1;
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    name = ["Normal_VonLeon", "Hard_VonLeon"];
    mobid = [8840000, 8840014];
    
    if (mode == -1) {  // 对话强制关闭 
        cm.dispose(); 
        return;
    }
    if (mode == 0) {   // 玩家点击取消/拒绝 
        cm.dispose();    // 添加这行确保对话关闭 
        return;
    }
    if (mode == 1) {   // 玩家点击接受/继续 
        status++;
    }
 
    if (status == 0) {
        if (!cm.isLeader())  {
            cm.sendOk("#fs15# 只有遠征隊現場可以搭話");
            cm.dispose(); 
            return;
        }
        cm.sendAcceptDecline("#fs15#是來打敗我的勇士們嗎…\r\n難道是對抗黑魔法師的人嗎…\r\n不管是娜一方都沒關係了。\r\n如果彼此的目的明確的話，就不用多說了…… \r\n來巴。 愚蠢的家藿們…");
        cm.playSound(false,  "Sound/Voice.img/vonleon/0"); 
    } else if (status == 1) {
        var check = cm.getPlayer().getMapId()  == 211070100 ? 0 : 1;
        var em = cm.getEventManager(name[check]); 
        var eim = cm.getPlayer().getEventInstance(); 
        if (em == null || eim == null) {
            cm.getPlayer().dropMessage(5,  "發現异常值，導致BOSS實例終止.");
            cm.warp(993080000); 
            cm.dispose(); 
        }
        mob = em.getMonster(mobid[check]); 
        eim.registerMonster(mob); 
        eim.getMapInstance(0).spawnMonsterOnGroundBelow(mob,  new java.awt.Point(-8,  -181));
        cm.getPlayer().getMap().resetNPCs(); 
        cm.dispose(); 
        return;
    }
}

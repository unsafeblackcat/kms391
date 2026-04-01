var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    name = ["Normal_Pinkbean", "Chaos_Pinkbean"];
    mobid = [8820002, 8820003, 8820004, 8820005, 8820006, 8820008, 8820014]
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
   if (!cm.isLeader()) {
      cm.sendOk("#fs15#只有組隊現場可以搭話.");
      cm.dispose();
      return;
   }
        cm.askAcceptDecline("#fs15#只要有女神的鏡子… 可以再次召喚黑魔法師。!...\r\n"
        +"這，好奇怪… 不是黑魔法師？ 這是什氣息？ 與黑魔法師完全不同… 哈哈哈!\r\n\r\n"
        +"#b（把手放在基爾斯頓的肩膀上。）");
    } else if (status == 1) {
        var map = cm.getPlayer().getMapId()%10000;
        var check = map == 100 ? 0 : 1;
        var em = cm.getEventManager(name[check]);
        var eim = cm.getPlayer().getEventInstance();
        if (em == null || eim == null) {
            cm.getPlayer().dropMessage(5, "檢測到异常值，導致BOSS實例終止。");
            cm.warp(993080000);
            cm.dispose();
        }
        cm.getPlayer().getMap().killAllMonsters(false);
        for (i = 0; i < 7; i++) {
            var mobidss = mobid[i] + (check == 1 ? 100 : 0);
            mob = em.getMonster(mobidss);
            eim.registerMonster(mob);
            eim.getMapInstance(0).spawnMonsterWithEffectBelow(mob, new java.awt.Point(5, -42), -2);
            if (mob.getId() == 8820008 || mob.getId() == 8820108) {
                eim.getMapInstance(0).killMonster(mob);
            }
        }
        cm.getPlayer().getMap().resetNPCs();
        cm.dispose();
        return;
    }
}
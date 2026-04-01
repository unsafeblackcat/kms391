importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var bb = 0;
var time = 0;
var outmap = 410005005;

function init() {

}

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Extreme_Karlos" + a) != null) {
        a = Randomizer.nextInt();
    }
    bb = map;
    var eim = em.newInstance("Extreme_Karlos" + a);
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 20).resetFully();
    eim.setInstanceMap(map + 100).resetFully();
    eim.setInstanceMap(map + 60).resetFully();
    eim.setInstanceMap(map + 120).resetFully();
    eim.setInstanceMap(map + 120).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    player.setExtremeMode(true);
    eim.startEventTimer(1800000);
    eim.setProperty("stage", "0");
    var map = eim.getMapInstance(0);
    player.setDeathCount(5);
    player.changeMap(map, map.getPortal(0));
    eim.setProperty("stage", "1");
    if (player.getParty().getLeader().getId() == player.getId()) {
        eim.schedule("WarptoNextStage", 0);
    }
}

function spawnMonster(eim, instance, mobid, x, y) {
    var map = eim.getMapInstance(instance);
    var mob = em.getMonster(mobid, true, false);
    eim.registerMonster(mob);
    mob.setExtreme(true);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(x, y));
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    stage = parseInt(eim.getProperty("stage"));
    player.setExtremeMode(true);
    if (mapid != 410006000 && mapid != 410006020 && mapid != 410006060 && mapid != 410006080 && mapid != 410006100 && mapid != 410006120) {
        player.setDeathCount(0);
        player.setExtremeMode(false);
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
        player.dropMessageGM(5, "stage : " + stage);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    stage = parseInt(eim.getProperty("stage"));
    if (mobId == 8880820 && stage == 1) {
        eim.setProperty("stage", "2");
        eim.schedule("WarptoNextStage", 0);
        map = eim.getMapInstance(stage);
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
        map.broadcastMessage(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs32##r#e아직 심판은 끝나지 않았다.", 100, 1000, 4, 0, 0, 1, 4));
        map.killMonster(8880820);
    } else if (mobId == 8880820) {
        eim.restartEventTimer(300000);
        eim.setProperty("stage", "4");
        eim.schedule("WarptoNextStage", 0);
        map = eim.getMapInstance(stage);
    } else if (mobId == 8880806) {
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
        map.broadcastMessage(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs32##r#e여기까진가... 죄송합니다. 아버지...", 100, 1000, 4, 0, 0, 1, 4));
        eim.restartEventTimer(300000);
        eim.setProperty("stage", "5");
        eim.schedule("WarptoNextStage", 0);
        map = eim.getMapInstance(stage);
        map.killMonster(8880806);
    }
    return 1;
}

function WarptoNextStage(eim) {
    var iter = eim.getPlayers().iterator();
    var stage = parseInt(eim.getProperty("stage"));
    while (iter.hasNext()) {
        var player = iter.next();
        map = eim.getMapInstance(stage);
        player.changeMap(map.getId(), 0);
    }
    if (stage == 2) {
        eim.setProperty("stage", "3");
        eim.schedule("WarptoNextStage", 0);
    } else if (stage == 3) {
        spawnMonster(eim, stage, 8880806, 552, 399);
        eim.setProperty("stage", "4");
    } else if (stage == 1) {
        spawnMonster(eim, stage, 8880820, 104, 398);
    } else if (stage == 4) {
        eim.setProperty("stage", "5");
        spawnMonster(eim, stage, 8880808, -9, 305);
    } else if (stage == 5) {
        spawnMonster(eim, stage, 8880808, -9, 305);
    }
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    eim.disposeIfPlayerBelow(0, 0);
}

function end(eim) {
    eim.disposeIfPlayerBelow(100, outmap);
}

function clearPQ(eim) {
    end(eim);
}

function disposeAll(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        eim.unregisterPlayer(player);
        player.setDeathCount(0);
        player.changeMap(outmap, 0);
    }
    end(eim);
}

function allMonstersDead(eim) {

}

function leftParty(eim, player) {
    disposeAll(eim);
}

function disbandParty(eim) {
    disposeAll(eim);
}

function playerDead(eim, player) {

}

function cancelSchedule() {

}
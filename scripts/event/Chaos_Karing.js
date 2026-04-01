importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 410007025;
var time = 0;
var bb = 0;
function init() { }
//카링
function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Chaos_Karing" + a) != null) {
        a = Randomizer.nextInt();
    }
    bb = map;
    var eim = em.newInstance("Chaos_Karing" + a);
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 20).resetFully();
    eim.setInstanceMap(map + 40).resetFully();
    eim.setInstanceMap(map + 60).resetFully();
    eim.setInstanceMap(map + 80).resetFully();
    eim.setInstanceMap(map + 80).resetFully(); // 마지막맵
    return eim;
}

function playerEntry(eim, player) {
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
    var mob = em.getMonster(mobid);
    eim.registerMonster(mob);
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
    if (mapid != 410007240 && mapid != 410007260 && mapid != 410007280 && mapid != 410007300 && mapid != 410007320 && mapid != 410007340 && mapid != 410007360) {
        player.setDeathCount(0);
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
        player.dropMessageGM(5,"stage : " + stage);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    stage = parseInt(eim.getProperty("stage"));
    if (mobId == 8880837 && stage == 1) {
        eim.setProperty("stage", "2");
        eim.schedule("WarptoNextStage", 0);
        map = eim.getMapInstance(stage);
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
    map.broadcastMessage(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs32##r#e카링이 화가 났어요.", 100, 1000, 4, 0, 0, 1, 4));
        map.killMonster(8880837);
    } else if (mobId == 8880837) {
        eim.restartEventTimer(300000);
        eim.setProperty("stage", "4");
        eim.schedule("WarptoNextStage", 0);
        map = eim.getMapInstance(stage);
    } else if (mobId == 8880842) {
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
       map.broadcastMessage(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs32##r#e카링이 물러납니다.", 100, 1000, 4, 0, 0, 1, 4));
        eim.restartEventTimer(300000);
        eim.setProperty("stage", "5");
        eim.schedule("WarptoNextStage", 0);
        map = eim.getMapInstance(stage);
        map.killMonster(8880842);
    }
    return 1;
}

function WarptoNextStage(eim) {
    var stage = parseInt(eim.getProperty("stage"));
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        map = eim.getMapInstance(stage);
        player.changeMap(map.getId(), 0);
    }

    if (stage == 2) {
        eim.setProperty("stage", "3");
        eim.schedule("WarptoNextStage", 0);
    } else if (stage == 3) {
        //2페
        spawnMonster(eim, stage, 8880839, 352, 399);
        spawnMonster(eim, stage, 8880840, 152, 399);
        spawnMonster(eim, stage, 8880841, 52, 399);
        spawnMonster(eim, stage, 8880842, 552, 399);
        eim.setProperty("stage", "4");
    } else if (stage == 1) {
        //1페
        spawnMonster(eim, stage, 8880837, 104, 398);
    } else if (stage == 4) {
        eim.setProperty("stage", "5");
        //eim.schedule("WarptoNextStage", 9500);
        spawnMonster(eim, stage, 8880845, -9, 305);
    } else if (stage ==5) {
        spawnMonster(eim, stage, 8880845, -9, 305);
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
    //after ravana is dead nothing special should really happen
}

function leftParty(eim, player) {
    disposeAll(eim);
}

function disbandParty(eim) {
    disposeAll(eim);
}

function playerDead(eim, player) { }

function cancelSchedule() { }
importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.math);

var mob;

var time = 0;
var temphp = 0;
var outmap = 450009301;

function init() {

}

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Extreme_Dusk" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Extreme_Dusk" + a);
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 30).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1800000);
    eim.setProperty("stage", "0");
    var map = eim.getMapInstance(0);
    player.setDeathCount(5);
    player.changeMap(map, map.getPortal(0));
    player.setExtremeMode(true);
    player.getClient().send(CField.ImageTalkNpc(0, 4000, "촉수가 눈을 방해하고 있어 제대로\n된 피해를 주기 힘들겠군."));
    player.getClient().send(CField.enforceMSG("점차 공포가 차올라 있을 수 없는 것이 보이게 됩니다! 견디지 못하면 공포가 전이되니 주의하세요!", 250, 3000));
    player.removeSkillCustomInfo(8644651);
    if (map.getMobsSize(8644655) <= 0) {
        spawnMonster(eim);
    }
}

function spawnMonster(eim) {
    var map = eim.getMapInstance(0);
    var mob = em.getMonster(8644655, true, false);
    eim.registerMonster(mob);
    mob.setExtreme(true);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(0, -157));

    var mob2 = em.getMonster(8644659, true, false);
    eim.registerMonster(mob2);
    mob2.setExtreme(true);
    map.spawnMonsterOnGroundBelow(mob2, new java.awt.Point(-45, -157));
}

function spawnMonsters(eim, instance, mobid, x, y) {
    var map = eim.getMapInstance(instance);
    var mob = em.getMonster(mobid);
    eim.registerMonster(mob);
    mob.setExtreme(true);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(x, y));
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {

}

function changedMap(eim, player, mapid) {
    player.setExtremeMode(true);
    if (mapid != 450009450 && mapid != 450009480) {
        player.setExtremeMode(false);
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    player = eim.getPlayers().get(0);
    stage = parseInt(eim.getProperty("stage"));
    if (mobId == 8644655) {
        eim.setProperty("stage", "1");
        map = eim.getMapInstance(stage);
        eim.restartEventTimer(300000);
        eim.schedule("WarptoNextStage", 5000);
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
    if (stage == 1) {
        spawnMonsters(eim, stage, 8950117, -2669, -488);
    }
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    eim.disposeIfPlayerBelow(0, 0);
}

function end(eim) {
    var player = eim.getPlayers().get(0);
    eim.disposeIfPlayerBelow(100, outmap2);
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
importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);
/**
    @Author Merrybytes
*/
var eventMap = 910160000;
var outmap = 910160100;
var time = 30; // this is calculating by minutes

function init() {}

function setup(mapid) {
    map = parseInt(mapid);
    var eim = em.newInstance("GoldReachStorage");
    eim.setInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1000 * 60 * time);
    var map = eim.getMapInstance(0);
   player.changeMap(map, map.getPortal(0));
    player.dropMessage(-1, "나의 소중한 보물을 훔쳐가는 저 돼지들을 없애주게나.");
}

function spawnMonster(eim) {
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
}

function changedMap(eim, player, mapid) {
    if (mapid != eventMap) {
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    return 1;
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    eim.disposeIfPlayerBelow(0, 0);
}

function end(eim) {
    var player = eim.getPlayers().get(0);
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
        player.changeMap(eim.getMapInstance(1), eim.getMapInstance(1).getPortal(0));
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

function playerDead(eim, player) {}

function cancelSchedule() {}
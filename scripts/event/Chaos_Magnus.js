importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 401060000; //나가지는 맵 (대부분 입장맵)
var time = 0;

function init() {}

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Chaos_Magnus" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Chaos_Magnus" + a);
    eim.setInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1800000);
    var map = eim.getMapInstance(0);
    player.setDeathCount(10);
    player.changeMap(map, map.getPortal(0));
	if (player.getMap().getNumMonsters() == 0) {
		spawnMob(eim, 8880012, 742, 206);
	}
}

function spawnMonster(eim) {
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    if (mapid != 450011470) {
        player.setDeathCount(0);
        eim.unregisterPlayer(player);
        player.setDeathCount(0);
        eim.disposeIfPlayerBelow(0, 0);
    }
}

function spawnMob(eim, mobID, x, y) {
	var map = eim.getMapInstance(0);
	var mob = em.getMonster(mobID);
	eim.registerMonster(mob);
	mob.setCustomInfo(mob.getId(), 0, Randomizer.rand(20000, 40000));
	map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(x, y));
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    player = eim.getPlayers().get(0);
    if (mobId == 8880012) {
        eim.restartEventTimer(300000);
    }
    return 1;
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

function playerDead(eim, player) {}

function cancelSchedule() {}
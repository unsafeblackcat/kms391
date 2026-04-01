importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 100000000;
var time = 0;

function init() { }

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Normal_Slime" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Normal_Slime" + a);
    eim.setInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.setProperty("stage", "0")
    eim.startEventTimer(1800000);
    var map = eim.getMapInstance(0);
    player.setDeathCount(5);
    player.changeMap(map, map.getPortal(0));
}

function spawnMonster(eim) {
    var map = eim.getMapInstance(1);
    var mob = em.getMonster(8880700);
	if(map.getNumMonsters() == 0) {
		 map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(715, -1382));
	}
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    if (mapid != 160000000 && mapid != 160020000 && mapid != 160050000) {
        player.setDeathCount(0);
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}


function monsterValue(eim, mobId) {
    var stage = parseInt(eim.getProperty("stage"));
    if (mobId == 8880700 && stage == 1) {
        eim.setProperty("stage", "2");
        eim.restartEventTimer(10000);
        eim.schedule("WarptoNextStage", 6000);
    }
    return 1;
}

function playAnimation(player, eim) {
    var stage = parseInt(eim.getProperty("stage"));
    if (stage == 0) {
        eim.setProperty("stage", "1");
        player.getClient().getSession().writeAndFlush(CField.showSpineScreen(true, false, true, "Effect/Direction20.img/bossSlime/1pahse_spine/skeleton", "animation", 0, false, ""));
		player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    } else if (stage == 2) {
        eim.setProperty("stage", "3");
    }
    if (player.getParty().getLeader().getId() == player.getId()) {
        eim.schedule("WarptoNextStage", 7000);
    }
}

function WarptoNextStage(eim) {
    var stage = parseInt(eim.getProperty("stage"));
    var iter = eim.getPlayers().iterator();

    while (iter.hasNext()) {
        var pl = iter.next();
        map = eim.getMapInstance(stage);
        pl.changeMap(map.getId(), 0);
	if (stage == 2) {
            playAnimation(pl, eim);
        } else if (stage == 1) {
	    pl.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
		}
    }
    if (stage == 1) {
        spawnMonster(eim, stage, 8880700, 715, -1382);
    } else if (stage == 3) {
	eim.setProperty("stage", "4");
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

function playerDead(eim, player) {}

function cancelSchedule() {}
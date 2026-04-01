importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var EtcTimer = Packages.server.Timer.EtcTimer;

var outmap = 910002000;
var outmap2 = 993189200;
var time = 0;


function init() {}

function setup(mapid) {
    map = parseInt(mapid);
    var eim = em.newInstance("rnjPQ");
    eim.setInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    min = Math.ceil(2);
    max = Math.floor(28);
    var ran = Math.floor(Math.random() * (max - min) + min);
    //player.dropMessage(5, ""+ran);
    eim.setProperty("stage1_"+ran, 2);
    eim.startEventTimer(1200000);
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function monsterValue(eim, mobId) {
    var stage = parseInt(eim.getProperty("stage"));
    return 1;
}

function spawnMonster(eim) {
    return;
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

    

function changedMap(eim, player, mapid) {
    if (mapid != 926100000 && mapid != 926110000 && mapid != 926100001 && mapid != 926110001 && mapid != 926100100 && mapid != 926110100 && mapid != 926100200 && mapid != 926110200 && mapid != 926100201 && mapid != 926100202 && mapid != 926110201 && mapid != 926110202 && mapid != 926110201 && mapid != 926110202 && mapid != 926100203 && mapid != 926110203 && mapid != 926100300 && mapid != 926110300 && mapid != 926100301 && mapid != 926100302 && mapid != 926100303 && mapid != 926100304 && mapid != 926110301 && mapid != 926110302 && mapid != 926110303 && mapid != 926110304 && mapid != 926100400 && mapid != 926110400 && mapid != 926100401 && mapid != 926110401) {
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
        disposeAll(eim);
    }
}

function playerDisconnected(eim, player) {
    return 0;
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
    var map = eim.getMapInstance(0);
    while (iter.hasNext()) {
        var player = iter.next();
        eim.unregisterPlayer(player);
        player.changeMap(outmap, map.getPortal(0));
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
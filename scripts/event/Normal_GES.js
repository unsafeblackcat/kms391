importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 160000000;
var time = 0;

function init() { }

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Normal_GES" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Normal_GES" + a);
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 20000).resetFully();
    eim.setInstanceMap(map + 30000).resetFully();
    eim.setInstanceMap(map + 40000).resetFully();
    eim.setInstanceMap(map + 20000).resetFully();
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
    if (mapid != 160010000 && mapid != 160020000 && mapid != 160030000 && mapid != 160040000 && mapid != 160050000) {
        player.setDeathCount(0);
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    stage = parseInt(eim.getProperty("stage"));
    if (mobId == 8880700 && stage == 1) {
        eim.setProperty("stage", "2");
        eim.schedule("WarptoNextStage", 0);
        map = eim.getMapInstance(stage);
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
        map.killMonster(8880700);
    } else if (mobId == 8880602) {
        eim.restartEventTimer(300000);
        eim.setProperty("stage", "4");
        eim.schedule("WarptoNextStage", 0);
        map = eim.getMapInstance(stage);
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
        if (stage == 1) {

            player.getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(0, 3000, "왕관의 수호로 제대로 된 피해를 입힐 수 없겠군. 마스코트 슬라임에게 도움을 받아 마그마 슬라임을 날려버리자.", ""));
          //  player.getClient().getSession().writeAndFlush(MobPacket.BossSlime.SlimePacket(12, map));
           // player.getClient().getSession().writeAndFlush(MobPacket.BossSlime.SlimePacket(11, map));
              player.getClient().getSession().writeAndFlush(MobPacket.BossSlime.SlimePacket(9, map));
            player.getClient().getSession().writeAndFlush(MobPacket.BossSlime.SlimePacket(8, map));
             player.getClient().getSession().writeAndFlush(MobPacket.BossSlime.SlimePacket(7, map));
        } else if (stage == 2) {

        } else if (stage == 3) {

        }
    }
    if (stage == 2) {
        spawnMonster(eim, stage, 8880725, 100, 275);
    } else if (stage == 1) {
        //1페
   //      spawnMonster(eim, stage, 8880700, 400, -500);
       spawnMonster(eim, stage, 8880700, 400, -1395);
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
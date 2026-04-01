importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var time = 0;
var outmap = 450011990;

function init() {

}

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Hell_JinHillah" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Hell_JinHillah" + a);
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 100).resetFully();
    eim.setInstanceMap(map + 150).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1800000);
    eim.setProperty("stage", "0");
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
    player.setHellMode(true);
    playAnimation(player, eim);
}

function spawnMonster(eim, instance, mobid, x, y) {
    var map = eim.getMapInstance(instance);
    var mob = em.getMonster(mobid, true, false);
    eim.registerMonster(mob);
    mob.setHellMode(true);
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
    player.setHellMode(true);
    if (mapid != 450010500 && mapid != 450010400 && mapid != 450010550) {
        player.setDeathCount(0);
        player.setHellMode(false);
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
    if (mobId == 8880405) {
        eim.restartEventTimer(300000);
        eim.setProperty("stage", "2");
        map = eim.getMapInstance(stage);
        eim.schedule("WarptoNextStage", 5000);
        var iter = eim.getPlayers().iterator();
        while (iter.hasNext()) {
            var player = iter.next();
            player.getMap().killAllMonsters(true);
            player.getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(3003771, 3000, "#face4#내… 내 시간이 흘러가고 있어!", ""));
        }
    }
    return 1;
}

function playAnimation(player, eim) {
    player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));

    var stage = parseInt(eim.getProperty("stage"));
    if (stage == 0) {
        Packages.server.Timer.MapTimer.getInstance().schedule(function () {
            player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/effect/BM2_hillahAppear_spine/hillah", "animation", 0, true, "intro"));
            player.getClient().getSession().writeAndFlush(CField.playSound("Sound/SoundEff.img/BM2/hillahappear"));
        }, 500);


        Packages.server.Timer.MapTimer.getInstance().schedule(function () {
           // player.getClient().getSession().writeAndFlush(CField.removeIntro("intro", 100));
           // player.getClient().getSession().writeAndFlush(CField.removeBlackOutScreen("BlackOut", 100));
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
            eim.setProperty("stage", "1");
        }, 15000);

        if (player.getParty().getLeader().getId() == player.getId()) {
            eim.schedule("WarptoNextStage", 15500);
        }
    }
}

function WarptoNextStage(eim) {
    var stage = parseInt(eim.getProperty("stage"));
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        map = eim.getMapInstance(stage);
        player.resetDeathCounts();
        player.changeMap(map.getId(), 0);
        if (stage == 1) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
            player.getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(3003771, 3000, "#face0#어디… 네 영혼은 다른 것들과 어떻게 다른지 한번 볼까!", ""));
        }
    }
    if (stage == 1) {
        spawnMonster(eim, stage, 8880405, 0, 266);
        spawnMonster(eim, stage, 8880406, 0, 266);
        spawnMonster(eim, stage, 8880407, 0, 266);
    } else if (stage == 2) {
        spawnMonster(eim, stage, 8950120, 826, 206);
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
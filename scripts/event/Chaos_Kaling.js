importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var bb = 0;
var time = 0;
var outmap = 410007025;

function init() {

}

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Chaos_Kaling" + a) != null) {
        a = Randomizer.nextInt();
    }
    bb = map;

    var eim = em.newInstance("Chaos_Kaling" + a);
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 0).resetFully();
    eim.setInstanceMap(map + 80).resetFully();
    eim.setInstanceMap(map + 80).resetFully();
    eim.setInstanceMap(map + 40).resetFully();
    eim.setInstanceMap(map + 40).resetFully();
    eim.setInstanceMap(map + 120).resetFully();
    eim.setInstanceMap(map + 120).resetFully();
    eim.setInstanceMap(map + 160).resetFully();
    eim.setInstanceMap(map + 160).resetFully();
    eim.setInstanceMap(map + 180).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1800000);
    eim.setProperty("stage", "0");
    var map = eim.getMapInstance(0);
    player.setDeathCount(5);
    player.changeMap(map, map.getPortal(0));
    eim.setProperty("stage", "1");
    player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossKaring/1phase_goongi_spine/skeleton", "animation", 0, false, ""));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Sound/SoundEff.img/bossKalos/1phase"));
    if (player.getParty().getLeader().getId() == player.getId()) {
        eim.schedule("WarptoNextStage", 6850);
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
    if (mapid != 410007140 && mapid != 410007180 && mapid != 410007220 && mapid != 410007260 && mapid != 410007300 && mapid != 410007320) {
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
    if (mobId == 8880830 && stage == 1) {
        eim.setProperty("stage", "2");
        eim.schedule("WarptoNextStage", 6850);
        map = eim.getMapInstance(stage);
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
        map.killMonster(8880830);
    } else if (mobId == 8880832 && stage == 3) {
        eim.setProperty("stage", "4");
        eim.schedule("WarptoNextStage", 6850);
        map = eim.getMapInstance(stage);
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
        map.killMonster(8880832);
    } else if (mobId == 8880831 && stage == 5) {
        eim.setProperty("stage", "6");
        eim.schedule("WarptoNextStage", 6850);
        map = eim.getMapInstance(stage);
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
        map.killMonster(8880831);
    } else if (mobId == 8880837 && stage == 7) {
        eim.setProperty("stage", "8");
        eim.schedule("WarptoNextStage", 6850);
        map = eim.getMapInstance(stage);
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
        map.killMonster(8880837);
    } else if (mobId == 8880842 && stage == 9) {
        eim.setProperty("stage", "10");
        eim.schedule("WarptoNextStage", 6850);
        map = eim.getMapInstance(stage);
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
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

        if (stage == 1) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        } else if (stage == 2) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    	player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossKaring/1phase_hondon_spine/skeleton", "animation", 0, false, ""));
        } else if (stage == 3) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        } else if (stage == 4) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    	player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossKaring/1phase_dool_spine/skeleton", "animation", 0, false, ""));
        } else if (stage == 5) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        } else if (stage == 6) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    	player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossKaring/2phase_spine/skeleton", "animation", 0, false, ""));
        } else if (stage == 7) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        } else if (stage == 8) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    	player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossKaring/3phase_spine/skeleton", "animation", 0, false, ""));
        } else if (stage == 9) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        }
    }

    if (stage == 1) { // 1 페이즈
        spawnMonster(eim, stage, 8880830, 575, 405);
    } else if (stage == 2) { // 2 페이즈 (1)
        eim.setProperty("stage", "3");
        eim.schedule("WarptoNextStage", 8500);
    } else if (stage == 3) { // 2 페이즈 (2)
        spawnMonster(eim, stage, 8880832, 874, 106);
    } else if (stage == 4) { // 3 페이즈 (1)
        eim.setProperty("stage", "5");
        eim.schedule("WarptoNextStage", 8500);
    } else if (stage == 5) { // 3 페이즈 (2)
        spawnMonster(eim, stage, 8880831, 603, 405);
    } else if (stage == 6) { // 4 페이즈 (1)
        eim.setProperty("stage", "7");
        eim.schedule("WarptoNextStage", 8500);
    } else if (stage == 7) { // 4 페이즈 (2)
        spawnMonster(eim, stage, 8880837, 899, 106);
    } else if (stage == 8) { // 4 페이즈 (1)
        eim.setProperty("stage", "9");
        eim.schedule("WarptoNextStage", 8500);
    } else if (stage == 9) { // 4 페이즈 (2)
        spawnMonster(eim, stage, 8880842, -155, 398);
        spawnMonster(eim, stage, 8880839, -1604, 362);
        spawnMonster(eim, stage, 8880840, 385, 362);
        spawnMonster(eim, stage, 8880841, -608, 362);
    } else if (stage == 10) { // 5 페이즈
        spawnMonster(eim, stage, 8880845, 484, 105);
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
importPackage(Packages.tools.packet);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.dispose();
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getParty().getLeader().getId() == cm.getPlayer().getId()) {
            if (cm.getMap().getNumMonsters() > 0) {
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.getTopMsg("請將所有怪物消滅在和我對話."));
                cm.dispose();
            } else {
                cm.sendYesNoS("#fs15#那我們移動?", 2);
            }
        } else {
            cm.getPlayer().dropMessage(5, "只有隊長才能申請入場.");
            cm.dispose();
            return;
        }
    } else if (status == 1) {
        var eim = cm.getPlayer().getEventInstance();
        if (eim != null) {
            var map = eim.getMapInstance(1).getId();
            eim.setProperty("stage", "1");
            cm.warpParty(map);
            if ((cm.getPlayer().getMapId() >= 105200310 && cm.getPlayer().getMapId() <= 105200319) || (cm.getPlayer().getMapId() >= 105200710 && cm.getPlayer().getMapId() <= 105200719))
                cm.getPlayer().getMap().broadcastMessage(CField.startMapEffect("根沈睡的血腥女王搭話巴.", 5120025, true));
            if ((cm.getPlayer().getMapId() >= 105200110 && cm.getPlayer().getMapId() <= 105200119) || (cm.getPlayer().getMapId() >= 105200510 && cm.getPlayer().getMapId() <= 105200519))
                cm.getPlayer().getMap().broadcastMessage(CField.startMapEffect("從次元的縫隙中召喚半半巴.", 5120025, true));
            if ((cm.getPlayer().getMapId() >= 105200210 && cm.getPlayer().getMapId() <= 105200219) || (cm.getPlayer().getMapId() >= 105200610 && cm.getPlayer().getMapId() <= 105200616)) {
                cm.getPlayer().getClient().send(CField.environmentChange("rootabyss/firework", 19));
                cm.getPlayer().getMap().broadcastMessage(CField.startMapEffect("歡迎來到皮埃爾的茶派對!", 5120098, true));
            }
            cm.dispose();
        }
        cm.dispose();
    }
}
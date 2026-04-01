var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Easy_Cygnus", 1, 271041100, 170],
        ["Easy_Cygnus", 1, 271041100, 170],
        ["Normal_Cygnus", 1, 271040100, 170],
        ["Normal_Cygnus", 1, 271040100, 170]
    ]
    name = ["이지", "이지", "노멀", "노멀"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getMapId() == 271041100) {
            cm.sendYesNo("#fs15#戰鬥結束後，在希納斯的支持下退場瑪?");
        } else {
            talk = "#fs15#準備好對抗墮落的希納斯了瑪?\r\n\r\n"
            talk += "#L0# #b申請進入希納斯（簡單）.\r\n";
            cm.sendSimple(talk);
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() == 271041100) {
            cm.warp(271040000);
            cm.dispose();
            return;
        }
        st = selection;
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("#fs15#必須屬於1人以上的隊伍才能入場.");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("#fs15#只有隊長才能申請入場.");
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("#fs15#所有成員必須在同一地點.");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendNext("#fs15#已經有另一個隊伍進入裡面，正在挑戰希納斯.");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 1 && st != 3) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        } else if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中 #b#e"
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n您的等級不足,挑戰希納斯需要等級 " + setting[st][3] + "以上.";
        } else {
            if (st == 1 || st == 3) {
                cm.addBossPractice(setting[st][0]);
            } else {
                cm.addBoss(setting[st][0]);
            }
            em = cm.getEventManager(setting[st][0]);
            if (em != null) {
                cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
            }
            cm.dispose();
        }
    }
}
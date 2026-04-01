var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Hard_Magnus", 1, 401060100, 175],
        ["Normal_Magnus", 1, 401060200, 155],
        ["Easy_Magnus", 1, 401060300, 115]
    ]
    name = ["하드", "노멀", "이지"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "#fs15#爲了消滅麥格納斯，要前往曝君的王座瑪?\r\n"
        talk += "#L0##b移動到曝君的王座（困難）（等級175以上）#l\r\n"
        talk += "#L3#我再想想。#l"
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("#fs15#必須屬於1人以上的隊伍才能入場.");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("#fs15#只有隊長才能申請入場.");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOk("#fs15#已經有人在挑戰馬格納斯了.");
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("#fs15#所有成員必須在同一地點.");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            c = 1;
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        } else if (!cm.isLevelAvailable(setting[st][3])) {
            c = 2;
            cm.sendNext("暴君的王座(" + name[st] + " 模式)只能進入等級 " + setting[st][3] + "以上.");
            cm.dispose();
            return;
        } else {
            cm.addBoss(setting[st][0]);
            em = cm.getEventManager(setting[st][0]);
            if (em != null) {
                cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
            }
            cm.dispose();
        }
    } else if (status == 2) {
        talk = "隊員中 #b#e"
        if (c == 1) {
            for (i = 0; i < cm.BossNotAvailableChrList(setting[st][0], setting[st][1]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.BossNotAvailableChrList(setting[st][0], setting[st][1])[i] + ""
            }
        } else {
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
        }
        talk += "#k#n 您沒有進入的資格.";
        cm.sendNext(talk);
        cm.dispose();
    }
}
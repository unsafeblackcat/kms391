var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Horntail", 1, 240060000, 130],
        ["Chaos_Horntail", 1, 240060001, 135]
    ]
    name = ["노멀", "카오스"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("#fs15#只有舉行隊伍才能入場.");
            cm.dispose();
            return;
        }
        if (!cm.isLeader()) {
            cm.sendOk("#fs15#只有隊長才能申請入場.");
            cm.dispose();
            return;
        }
        talk = "#e#fs15#<BOSS：黑龍>#n\r\n"
        talk += "黑龍復活了。 如果放任不管的話，會引發火山爆發，把米納爾一帶變成地獄的.\r\n\r\n"
        talk += "#L0##b <BOSS：黑龍>申請入場."
        cm.sendSimple(talk);
    } else if (status == 1) {
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("#fs15#只有舉行隊伍才能入場.");
            cm.dispose();
            return;
        }
        if (!cm.isLeader()) {
            cm.sendOk("#fs15#只有隊長才能申請入場.");
            cm.dispose();
            return;
        }
        talk = "#e#fs15#<BOSS：黑龍>#n\r\n"
        talk += "選擇所需模式.\r\n\r\n"
       // talk += "#L0# 正常模式（等級130以上）#l\r\n"
        talk += "#L1# 混沌模式（等級135以上）#l"
        cm.sendSimple(talk);
    } else if (status == 2) {
        st = selection;
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("#fs15#只有舉行隊伍才能入場.");
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
        } else if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(setting[st][2] + 100) >= 1 || cm.getPlayerCount(setting[st][2] + 200) >= 1) {
            cm.sendOk("#fs15#已經有人在挑戰黑龍了.");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
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
            talk += "의#k#n 等級不够啊.那就進不去了.";
        } else {
            cm.addBoss(setting[st][0]);
            em = cm.getEventManager(setting[st][0]);
            if (em != null) {
                cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
            }
            cm.dispose();
        }
    }
}
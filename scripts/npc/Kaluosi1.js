var status = -1;


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Chaos_Karlos", 1, 410006000, 270],
    ]
    name = ["카오스"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "要爲了與監視者Ka洛斯戰斗而移動瑪？ (#r等級 "+ setting[0][3]+"以上#k 可進入)\r\n\r\n"
        talk += "#L0#申請進入<BOSS:監視者Ka洛斯> #l\r\n"
        talk += "#L1#不移動.#l\r\n"
        cm.sendSimpleS(talk, 0x26);
    } else if (status == 1) {
        if (selection == 2) {
            cm.dispose();
            return;
        }
        st = selection;

        if (cm.getParty() == null) {
            cm.sendOkS("#fs15#必須組成1人以上的隊伍才能進入。", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOkS("#fs15#已經有人正在挑戰Ka洛斯了。\r\n請利用其他頻道。", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("#fs15#只有隊長才能申請進入。", 0x26);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("#fs15#所有成員必須在同一地點。");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        } else if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中 "
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n的等級不足。 Ka洛斯 " + name[st] + "模式需要" + setting[st][3] + " 級以上才能進入。";
            cm.sendOkS(talk, 0x24);
            cm.dispose();
            return;
        } else {
            cm.addBoss(setting[st][0]);
            cm.dispose();
            em = cm.getEventManager(setting[st][0]);
            if (em != null) {
                cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
            }

        }

            cm.addBoss(setting[st][0]);
            cm.dispose();
            em = cm.getEventManager(setting[st][0]);
            if (em != null) {
                cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
            }
    } else if (status == 2) {
        cm.dispose();
        em = cm.getEventManager(setting[st][0]);
        if (em != null) {
            cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
        }
    }
}
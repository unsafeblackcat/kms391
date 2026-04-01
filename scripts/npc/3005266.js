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
        talk = "要為了與監視者卡洛斯戰鬥而移動嗎（#r等級 " + setting[0][3] + "以上#k 可入場）\r\n\r\n"
        talk += "#L0#<首領：監視者卡洛斯> 申請入場。#l\r\n"
        talk += "#L1#不進行移動。#l\r\n"
        cm.sendSimpleS(talk, 0x26);
    } else if (status == 1) {
        if (selection == 2) {
            cm.dispose();
            return;
        }
        st = selection;

        if (cm.getParty() == null) {
            cm.sendOkS("必須組建1人以上的隊伍才能入場。", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOkS("已經有人在挑戰卡洛斯了。\r\n請使用其他頻道。", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("只有隊長可以申請入場。", 0x26);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("所有隊員必須在同一地點。");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        } else if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中"
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += "，"
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n先生/女士的等級不足。卡洛斯 " + name[st] + "模式僅限 " + setting[st][3] + " 等級以上入場。";
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
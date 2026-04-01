var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Easy_Lucid", 1, 450003800, 220],
        ["Normal_Lucid", 1, 450004100, 220],
        ["Hard_Lucid", 1, 450004400, 220],
        ["Easy_Lucid", 1, 450003800, 220],
        ["Normal_Lucid", 1, 450004100, 220],
        ["Hard_Lucid", 1, 450004400, 220]
    ]
    name = ["簡易", "普通", "困難", "簡易練習", "普通練習", "困難練習"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "#e<首領: 露西德>#n\r\n"
        talk += "若不阻止露西德，將會發生可怕的事情。\r\n\r\n"
        //talk += "#L0# #b<首領: 露西德(簡易)> 申請入場。#l\r\n"
        talk += "#L1# #b<首領: 露西德(普通)> 申請入場。#l\r\n"
        //talk += "#L2# #b<首領: 露西德(困難)> 申請入場。#l\r\n"
        //talk += "#L3# #b<首領: 露西德(簡易練習)> 申請入場。#l\r\n"
        talk += "#L4# #b<首領: 露西德(普通練習)> 申請入場。#l\r\n"
        //talk += "#L5# #b<首領: 露西德(困難練習)> 申請入場。#l\r\n"
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOk("必須組隊才能入場。")
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("只有隊長可以申請入場。");
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("所有隊員必須在同一地點。");
            cm.dispose();
            return;
        }
        if (selection == 0 || selection == 3) {
            if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 40) || cm.getPlayerCount(Number(setting[st][2]) + 80) || cm.getPlayerCount(Number(setting[st][2]) + 120) || cm.getPlayerCount(Number(setting[st][2]) + 160) >= 1) {
                cm.sendOk("已有人在挑戰露西德。\r\n請使用其他頻道。");
                cm.dispose();
                return;
            }
        } else {
            if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 50) || cm.getPlayerCount(Number(setting[st][2]) + 100) || cm.getPlayerCount(Number(setting[st][2]) + 150) || cm.getPlayerCount(Number(setting[st][2]) + 200) >= 1) {
                cm.sendOk("已有人在挑戰露西德。\r\n請使用其他頻道。");
                cm.dispose();
                return;
            }
        }
        if (!cm.partyhaveItem(4032434, 1)) {
            talk = "隊員中存在未持有#i4032434# #b#z4032434##k的隊員。";
            cm.sendOkS(talk, 0x04, 9010061);
            cm.dispose();
            return;
        } else {
            cm.asd123(4032434);
        }
        /*if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 3 && st != 4 && st != 5) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        }*/
        if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中"
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += "，"
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n先生/女士的等級不足。露西德 " + name[st] + "模式僅限 " + setting[st][3] + " 等級以上入場。";
            cm.sendOk(talk);
            cm.dispose();
            return;
        }

        if (st == 3 || st == 4 || st == 5) {
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
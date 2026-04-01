var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Dusk", 1, 450009400, 245],
        ["Chaos_Dusk", 1, 450009450, 245]
    ]
    name = ["普通", "混沌"];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "不能放任由黑魔法師信念所構成的巨大怪物杜斯克存在。\r\n\r\n#L0##b空虛之眼(普通模式)#r(等級245以上)#k前往。#l\r\n";
        cm.sendSimpleS(talk, 0x26);
    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("必須組建1人以上的隊伍才能入場。", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOkS("已經有人在挑戰杜斯克了。\r\n請使用其他頻道。", 0x26);
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
        if (!cm.partyhaveItem(4032811, 1)) {
            talk = "隊員中存在未持有#i4032811# #b#z4032811##k的隊員。";
            cm.sendOkS(talk, 0x04, 9010061);
            cm.dispose();
            return;
        } else {
            cm.asd123(4032811);
        }
        /*if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        } else*/ if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中"
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += "，"
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n先生/女士的等級不足。杜斯克 " + name[st] + "模式僅限 " + setting[st][3] + " 等級以上入場。";
            cm.sendOkS(talk, 0x26);
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
    }
}
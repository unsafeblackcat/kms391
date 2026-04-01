var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Will", 1, 450008700, 235],
        ["Hard_Will", 1, 450008100, 235],
        ["Normal_Will", 1, 450008700, 235],
        ["Hard_Will", 1, 450008100, 235],
        ["Extreme_Will", 1, 450008100, 235]
    ]
    name = ["普通", "困難", "普通練習", "困難練習"];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "要為了阻止威爾而前往#b「悔悟迴廊」#k嗎?\r\n\r\n"
        //        if (cm.getPlayer().isGM()) {
        // talk+= "#L4##b悔悟迴廊(極限模式)前往。#k#r(等級235以上)#k#l\r\n"
        //      }
        //talk += "#L0#悔悟迴廊(#b普通模式#k)前往。#k#r(等級235以上)#k#l\r\n";
        talk += "#L1#悔悟迴廊(#b困難模式#k)前往。#k#r(等級235以上)#k#l\r\n";
        //talk += "#L2#悔悟迴廊(#b普通練習模式#k)前往。#k#r(等級235以上)#k#l\r\n";
        //talk += "#L3#悔悟迴廊(#b困難練習模式#k)前往。#k#r(等級235以上)#k#l\r\n";
        cm.sendSimpleS(talk, 0x86);
    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("必須組建1人以上的隊伍才能入場。", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(setting[st][2] + 50) >= 1 || cm.getPlayerCount(setting[st][2] + 100) >= 1 || cm.getPlayerCount(setting[st][2] + 150) >= 1 || cm.getPlayerCount(setting[st][2] + 200) >= 1 || cm.getPlayerCount(setting[st][2] + 250) >= 1) {
            cm.sendOkS("已有人在挑戰威爾。\r\n請使用其他頻道。", 0x26);
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
        a = 0;

        /*if (!cm.isBossAvailable(setting[st][0], setting[st][1] + a) && st != 2 && st != 3) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        }*/
        if (!cm.partyhaveItem(4033644, 1)) {
            talk = "隊員中存在未持有#i4033644# #b#z4033644##k的隊員。";
            cm.sendOkS(talk, 0x04, 9010061);
            cm.dispose();
            return;
        } else {
            cm.asd123(4033644);
        }
        if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中"
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += "，"
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n先生/女士的等級不足。威爾 " + name[st] + "模式僅限 " + setting[st][3] + " 等級以上入場。";
            cm.sendOkS(talk, 0x26);
            cm.dispose();
            return;
        }

        if (st == 2 || st == 3) {
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
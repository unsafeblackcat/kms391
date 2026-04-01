var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Demian", 1, 350160200, 190],
        ["Hard_Demian", 1, 350160100, 190],
        ["Normal_Demian", 1, 350160200, 190],
        ["Hard_Demian", 1, 350160100, 190],
        ["Extreme_Demian", 1, 350160200, 190]
    ]
    name = ["노멀", "하드", "노말 연습", "하드 연습"];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "#fs23#爲了打倒戴米安，是否會移動到“墮落世界的頂峰?\r\n\r\n"
        //        if (cm.getPlayer().isGM()) {
        //talk+= "#L4#타락한 세계수 정상(#b익스트림 모드#k)으로 이동 한다.(레벨 190이상)#l\r\n"
        //      }
        talk += "#L1#進入墮落的世界樹#b(困難模式)#k.(190級以上)#l\r\n";

        talk += "#L4#不移動.\r\n";
        cm.sendSimpleS(talk, 0x86);
    } else if (status == 1) {
        if (selection == 4) {
            cm.dispose();
            return;
        }
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("#fs15#1人以上組隊才能入場.", 0x24);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(setting[st][2] + 20) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 40) >= 1 || cm.getPlayerCount(setting[st][2] + 60) >= 1) {
            cm.sendOkS("#fs15#已經有人在挑戰戴米安了.\r\n請使用其他頻道.", 0x24);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("#fs15#只有隊長才能申請入場.", 0x24);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("#fs15#所有成員必須在同一地點.");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 2 && st != 3) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        }
        if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中 "
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n您的等級不足。 戴米安 " + name[st] + "模式只能進入 " + setting[st][3] + " 等級以上.";
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
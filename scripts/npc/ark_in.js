var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Easy_Arkarium", 1, 272020210, 140],
        ["Normal_Arkarium", 1, 272020200, 140],
    ]
    name = ["이지","노멀"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getMapId() == 272020110) {
            talk = "#e#fs15#<BOSS：阿喀伊勒>#n\r\n"
            talk += "偉大的勇士，做好了對抗黑魔法師邪惡軍團團長的準備了瑪?\r\n\r\n"
            talk += "#L0# #b<BOSS：阿喀伊勒> 申請入場.";
            cm.sendSimple(talk);
        } else {
            cm.sendYesNo("#fs15#是否在戰鬥結束後退出存檔祭壇?");
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() == 272020110) {
            talk = "#e#fs15#<BOSS：阿喀伊勒>#n\r\n"
            talk += "#L0#簡單模式（等級140以上）#l\r\n";
            cm.sendSimple(talk);
        } else {
            cm.warp(272020110);
            cm.dispose();
        }
    } else if (status == 2) {
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
        } else if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(setting[st][2] + 100) >= 1 || cm.getPlayerCount(setting[st][2] + 200) >= 1) {
            cm.sendNext("#fs15#其他隊伍已經進入裡面，正在挑戰BOSS.");
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
            talk += "#k#n的等級不足. <阿喀伊勒>只能挑戰等級 " + setting[st][3] + "以上.";
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
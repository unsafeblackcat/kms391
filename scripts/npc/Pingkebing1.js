var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Pinkbean", 1, 270050100, 160],
        ["Chaos_Pinkbean", 1, 270051100, 170],
        ["Chaos_Pinkbean", 1, 270051100, 170]
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
        if (cm.getPlayer().getMapId() == 270050000) {
            if (cm.getPlayer().getParty() == null) {
                cm.sendOk("#fs15#只有組隊才能入場.");
                cm.dispose();
                return;
            }
            if (!cm.isLeader()) {
                cm.sendOk("#fs15#只有隊長才能申請入場.");
                cm.dispose();
                return;
            }
            talk = "#e#fs15#<BOSS：粉紅彬>#n\r\n"
            talk += "入侵者好像走向了女神的祭壇。 如果不能快點封鎖他，會發生恐怖的事情。\r\n\r\n"
            talk += "#L0# #b<BOSS：粉紅彬> 申請入場.";
            cm.sendSimple(talk);
        } else {
            cm.sendYesNo("#fs15#戰鬥結束後退出粉紅彬的祭壇瑪?");
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() == 270050000) {
            talk = "#e#fs15#<BOSS：粉紅彬> #n\r\n"
            talk += "請選擇想要的模式.\r\n\r\n"
            talk += "#L0#普通模式（等級160以上）#l\r\n";
            //talk += "#L1#混沌模式（等級170以上）#l\r\n";
            //talk += "#L2#混沌練習模式（等級170以上）#l\r\n";
            cm.sendSimple(talk);
        } else {
            cm.warp(270050000);
            cm.dispose();
        }
    } else if (status == 2) {
        st = selection;
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("#fs15#只有組隊才能入場.");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("#fs15#只有隊長才能申請入場.");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendNext("#fs15#已經有另一個隊伍進入裡面，正在挑戰粉紅彬.");
            cm.dispose();
            return;
	} else if (!cm.allMembersHere()) {
	    cm.sendOk("#fs15#所有成員必須在同一地點.");
	    cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 2) {
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
            talk += "#k#n您的等級不足. <BOSS：粉紅彬>只能挑戰等級 " + setting[st][3] + "以上.";
        } else {
            if (st == 2) {
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
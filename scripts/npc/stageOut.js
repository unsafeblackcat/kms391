var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Dorothy", 1, 992050000, 100]
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getMapId() == 992049000) {
            talk = "#fs20#B - 50F#fs#\r\n\r\n"
            talk += "現在是最後一層，地下50層了...\r\n"
            talk += "能壓制她嗎...老實說，這個世界上沒有人能自信地這麼說，就連創造者我也一樣。\r\n"
            talk += "要小心啊，她的魔法、分身，甚至寵物都是。\r\n\r\n";
            talk += "#L0# 申請進入多蘿西戰場。"
            cm.sendSimple(talk);
        } else {
            cm.sendYesNo("是否退出？");
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() == 105200000) {
            st = selection;
            if (cm.getPlayer().getParty() == null) {
                cm.sendOk("必須加入至少1人的隊伍才能進入。");
                cm.dispose();
                return;
            } else if (!cm.isLeader()) {
                cm.sendOk("只有隊長才能申請進入。");
                cm.dispose();
                return;
            } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
                cm.sendNext("已有其他隊伍進入挑戰血腥女王，請稍後再試。");
                cm.dispose();
                return;
            } else if (!cm.allMembersHere()) {
                cm.sendOk("所有隊員必須在同一地點才能進入。");
                cm.dispose();
                return;
            }
            if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
                talk = "隊員中 #b#e"
                for (i = 0; i < cm.BossNotAvailableChrList(setting[st][0], setting[st][1]).length; i++) {
                    if (i != 0) {
                        talk += ", "
                    }
                    talk += "#b#e" + cm.BossNotAvailableChrList(setting[st][0], setting[st][1])[i] + ""
                }
                talk += "#k#n 今天已挑戰過。血腥女王每天僅可挑戰 " + setting[st][1] + " 次。";
                cm.sendOk(talk);
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
                talk += "#k#n 的等級不足。血腥女王僅開放給等級 " + setting[st][3] + " 以上的玩家挑戰。";
            } else {
                cm.addBoss(setting[st][0]);
                em = cm.getEventManager(setting[st][0]);
                if (em != null) {
                    cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
                }
                cm.dispose();
            }
        } else {
            cm.warp(105200000);
            cm.dispose();
        }
    }
}
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Kawoong", 1, 221030910, 180]
    ]
    name = ["노멀", "하드"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말  = "竟然膽大地來到這裡！ 要嘗嘗新改造的戰斗型机甲的味道瑪!\r\n"
        말 += "#L0# #b喀雄<普通模式> （隊友將同時移動。）#k#l\r\n\r\n"
        말 += "#r[BOSS]喀雄每天可以進入#e" + setting[0][1] + "次 #n,記錄將在 #e每天午夜#n初始化.#k\r\n"
        cm.sendSimpleS(말, 0x24);
    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOk("#fs15#只有一人以上的派對才能入場。");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOk("#fs15#經有人在挑戰喀雄了。\r\n請使用其他頻道。");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("#fs15#只有隊長才能申請入場。");
            cm.dispose();
            return;
	} else if (!cm.allMembersHere()) {
	    cm.sendOk("#fs15#所有成員都應該在同一個地方。");
	    cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        } else if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊友中有人為了挑戰喀雄等級不够。"
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                talk += "#b#e-" + cm.LevelNotAvailableChrList(setting[st][3])[i] + "\r\n"
            }
            cm.sendOk(talk);
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
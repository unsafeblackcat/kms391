var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    setting = [
        ["Easy_VonLeon", 1, 211070100, 125],
        ["Normal_VonLeon", 1, 211070102, 125],
        ["Hard_VonLeon", 1, 211070104, 125]
    ]
    name = ["이지", "노멀", "하드"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getMapId() == 211070000) {
            talk = "#e#fs15#<BOSS: 班.雷昻>#n\r\n"
            talk += "偉大的勇士，做好了對抗墮落獅子王的準備了瑪?\r\n\r\n"
            talk += "#L0# #b班.雷昻 申請遠征隊入場.";
            cm.sendSimple(talk);
        } else {
            cm.sendYesNo("#fs15#是否完成挑戰後退出?");
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() == 211070000) {
            talk = "#e#fs15#<BOSS: 班.雷昻>#n\r\n"
            talk += "#L1#普通模式（等級125以上）#l\r\n";
            cm.sendSimple(talk);
        } else {
            cm.warp(211070000);
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
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendNext("#fs15#已經有其他派對進去了，正在挑戰 獅子王班.雷昻.");
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("#fs15#所有成員必須在同一地點.");
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
            talk += "#k#n的等級不足. <BOSS: 班.雷昻>只能挑戰等級 " + setting[st][3] + "以上.";
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
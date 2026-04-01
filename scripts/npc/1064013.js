var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_VonBon", 1, 105200100, 125],
        ["Chaos_VonBon", 1, 105200500, 180],
        ["Chaos_VonBon", 1, 105200500, 180]
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
        talk = "#r#e#fs15#<魯塔比斯東側花園入口>#k#n\r\n"
        talk += "這是通往東邊封印守護者 #r半半#k 守護的庭院的門.\r\n#r普通記錄將在正常情況下於當天午夜初始化\r\n混沌記錄於每週四午夜初始化。#k#n\r\n\r\n"
        talk += "#L0#使用#i4033611#進入#r普通模式#k。 （125級以上）#l\r\n"
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("#fs15#必須屬於1人以上的隊伍才能入場.");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("#fs15#只有隊長才能申請入場.");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2] + 10) >= 1 || cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendNext("#fs15#已經有另一個隊伍進入裡面，正在挑戰一半。.");
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("#fs15#所有成員必須在同一地點.");
            cm.dispose();
            return;
        }
        if (cm.partyhaveItem(4033611, 1) == false) {
            talk = "#fs15#隊員中有沒有持有 #i4033611# #b#z4033611##k的隊員.";
            cm.sendOk(talk);
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
            talk += "#k#n您的等級不足。 半半只能挑戰等級 " + setting[st][3] + "以上。.";
        } else {
            cm.givePartyItems(4033611, -1);
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
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Hillah", 1, 262030100, 120],
        ["Hard_Hillah", 1, 262031100, 170]
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
        말 = "#e#fs15#<BOSS：希拉>#n\r\n"
        말 += "準備好消滅希拉，實現阿斯旺的真正解放了瑪？ 如果有其他地區的隊員，請大家都聚過來。\r\n\r\n"
        말 += "#L0# #b<BOSS：希拉> 申請入場."
        cm.sendSimple(말);
    } else if (status == 1) {
        말 = "#e#fs15#<BOSS：希拉>#n\r\n"
        말 += "請選擇想要的模式.\r\n\r\n"
        말 += "#L1# 困難模式（170級以上）"
        cm.sendSimple(말);
    } else if (status == 2) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOk("1人以上組隊才能入場.");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 100) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 200) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 210) >= 1) {
            cm.sendOk("#fs15#已經有人在挑戰希拉了.\r\n請使用其他頻道.");
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
        }
        /*if (!cm.partyhaveItem(4033304, 1)) {
            talk = "파티원 중 #i4033304# #b#z4033304##k를 소지하고 있지 않은 파티원이 있습니다.";
            cm.sendOkS(talk, 0x04, 9010061);
            cm.dispose();
            return;
        } else {
            cm.asd123(4033304);
        }*/
        /*if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        } else*/
             if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中 "
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n的等級不足. <BOSS：希拉>" + name[st] + "模式只能進入 " + setting[st][3] + " 等級以上.";
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
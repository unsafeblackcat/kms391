var status = -1;


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
    ["Chaos_Karing", 1, 410007240, 275]
    ]
    name = ["混沌"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
         talk = "是否要移動至與卡琳戰鬥的地點？（#r需達等級 " + setting[0][3] + "以上#k方可進入）\r\n\r\n"
         talk += "#L0#<首領: (混沌) 卡琳> 申請進入#l\r\n"
         talk += "#L1#不進行移動#l\r\n"
         cm.sendSimpleS(talk, 0x26);
    } else if (status == 1) {
        if (selection == 2) {
            cm.dispose();
            return;
        }
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("需組建至少1人的隊伍方可進入。", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOkS("已有人正在挑戰賽蓮。\r\n請使用其他頻道。", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("僅隊長可申請進入。", 0x26);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("所有隊員需位於同一地點。");
            cm.dispose();
            return;
        }
 /*       if (!cm.partyhaveItem(4032927, 1)) {
            talk = "隊員中有人未持有#i4032927# #b#z4032927##k。";
            cm.sendOkS(talk, 0x04, 9010061);
            cm.dispose();
            return;
        } else {
            cm.asd123(4032927);
        }*/
        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        } else if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中 "
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n的等級不足。混沌卡琳模式需達 " + setting[st][3] + " 級以上方可進入。";
            cm.sendOkS(talk, 0x24);
            cm.dispose();
            return;
        } else {
            cm.addBoss(setting[st][0]);
            cm.dispose();
            em = cm.getEventManager(setting[st][0]);
            if (em != null) {
                cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
            }

        }
    } else if (status == 2) {
        cm.dispose();
        em = cm.getEventManager(setting[st][0]);
        if (em != null) {
            cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
        }
    }
}
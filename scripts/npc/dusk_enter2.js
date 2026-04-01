var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
var setting = [
["Normal_Dusk", 1, 450009400, 245],
["Chaos_Dusk", 1, 450009450, 245],
["Extreme_Dusk", 1, 450009450, 245],
["Hell_Dusk", 1, 450009450, 245]
];
var name = ["노말", "카오스", "익스트림", "헬"];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "不能放任由黑暗魔法師的詛咒所形成的巨大怪物至暗魔晶不管。"
        talk +="\r\n#L2#移動到#b虛空之眼前方(#r極限模式#k).#r(等級245以上)#k#l";
        talk +="\r\n#L3#移動到#b虛空之眼前方(#r地獄模式#k).#r(等級245以上)#k#l\r\n";
        cm.sendSimpleS(talk, 0x26);
    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("必須組成1人以上的隊伍才能進入。", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOkS("已經有人正在挑戰至暗魔晶了。\r\n請利用其他頻道。", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("只有隊長才能申請進入。", 0x26);
            cm.dispose();
            return;
	} else if (!cm.allMembersHere()) {
	    cm.sendOk("所有成員必須在同一地點。");
	    cm.dispose();
            return;
        }
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
            talk += "#k#n等級不足。至暗魔晶  " + name[st] + "模式需要" + setting[st][3] + " 級以上才能進入.";
            cm.sendOkS(talk, 0x26);
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
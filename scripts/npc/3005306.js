var status = -1;


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Hard_Lotus", 1, 350060700, 190],
        ["Normal_Lotus", 1, 350060400, 190],
        ["Hard_Lotus", 1, 350060700, 190],
        ["Normal_Lotus", 1, 350060400, 190],
        ["Extreme_Lotus", 1, 350060700, 190],
    ]
    name = ["하드", "노멀", "하드練習", "노말練習"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "要為了擊倒斯烏而移動到黑天鵝核心嗎?\r\n\r\n"
        //        if (cm.getPlayer().isGM()) {
        //talk += "#L4##b黑天鵝核心(極限模式)#k로移動한다.(等級190以上)#l\r\n"
        //      }
        talk += "#L1#黑天鵝核心(#b普通模式#k)로移動한다.(等級190以上)#l\r\n"
        //talk += "#L0#黑天鵝核心(#b困難模式#k)로移動한다.(等級190以上)#l\r\n"
        //talk += "#L3#黑天鵝核心(#b普通練習模式#k)로移動한다.(等級190以上)#l\r\n"
        //talk += "#L2#黑天鵝核心(#b困難練習模式#k)로移動한다.(等級190以上)#l\r\n\r\n"
        talk += "#L4#不進行移動。\r\n"
        cm.sendSimpleS(talk, 0x86);
    } else if (status == 1) {
        if (selection == 4) {
            cm.dispose();
            return;
        }
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("必須組建1人以上的隊伍才能入場。", 0x24);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(Number(setting[st][2] + 100)) >= 1 || cm.getPlayerCount(Number(setting[st][2] + 200)) >= 1) {
            cm.sendOkS("已經有人在挑戰斯烏了。\r\n請使用其他頻道。", 0x24);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("只有隊長可以申請入場。", 0x24);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("所有隊員必須在同一地點。");
            cm.dispose();
            return;
        }
        if (!cm.partyhaveItem(4000362, 1)) {
            talk = "隊員中存在未持有#i4000362# #b#z4000362##k的隊員。";
            cm.sendOkS(talk, 0x04, 9010061);
            cm.dispose();
            return;
        } else {
            cm.asd123(4000362);
        }
        /*if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 2 && st != 3) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        } else*/ if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中"
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += "，"
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n先生/女士的等級不足。斯烏" + name[st] + "模式僅限" + setting[st][3] + "等級以上入場。";
            cm.sendOkS(talk, 0x26);
            cm.dispose();
            return;
        } else {
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
}
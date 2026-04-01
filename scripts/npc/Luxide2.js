var status = -1;
var enter = "\r\n";
var talkType = 0x86;
var NoramlPass = "Serenity_Noraml_Pass_Info";
var PrimeumPass = "Serenity_Premium_Pass_Info";
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Easy_Lucid",   1, 450003800, 220],
        ["Normal_Lucid", 1, 450004100, 220],
        ["Hard_Lucid",   1, 450004400, 220],
        ["Easy_Lucid",   1, 450003800, 220],
        ["Normal_Lucid", 1, 450004100, 220],
        ["Hard_Lucid",   1, 450004400, 220]
    ]
    name = ["이지", "노멀", "하드", "이지 연습", "노멀 연습", "하드 연습"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {

        talk = "#fs18##e<首領: 路西德>#n"+ enter;
        talk += "如果無法阻止路西德，將會發生恐怖的事情。"+ enter;
       // talk += "#L0#申請進入<首領: 路西德 (#b簡單#k) >。"+ enter;
        talk += "#L1#申請進入<首領: 路西德 (#b普通#k) >。"+ enter;
        //talk += "#L2#申請進入<首領: 路西德 (#b困難#k) >。"+ enter;
        cm.sendSimpleS(talk, talkType);
    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOk("#fs15#必須組隊才能進入。")
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("#fs15#只有隊長才能申請進入。");
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("#fs15#所有隊員必須在同一地點。");
            cm.dispose();
            return;
        }
        if (selection == 0 || selection == 3) {
            if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 40) || cm.getPlayerCount(Number(setting[st][2]) + 80) || cm.getPlayerCount(Number(setting[st][2]) + 120) || cm.getPlayerCount(Number(setting[st][2]) + 160) >= 1) {
                cm.sendOk("#fs15#已經有人在挑戰路西德了\r\n請使用其他頻道。");
                cm.dispose();
                return;
            }
        } else {
            if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 50) || cm.getPlayerCount(Number(setting[st][2]) + 100) || cm.getPlayerCount(Number(setting[st][2]) + 150) || cm.getPlayerCount(Number(setting[st][2]) + 200) >= 1) {
                cm.sendOk("#fs15#已經有人在挑戰路西德了\r\n請使用其他頻道。");
                cm.dispose();
                return;
            }
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 3 && st != 4 && st != 5) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        }
        if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中 "
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n的等級不足。 路西德 " + name[st] + "模式需要 " + setting[st][3] + " 級以上才能進入。";
            cm.sendOk(talk);
            cm.dispose();
            return;
        }

        if (st == 3 || st == 4 || st == 5) {
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
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Will", 1, 450008700, 235],
        ["Hard_Will", 1, 450008100, 235],
        ["Normal_Will", 1, 450008700, 235],
        ["Hard_Will", 1, 450008100, 235],
        ["Extreme_Will", 1, 450008100, 235]
    ]
    name = ["노멀", "하드", "노말 연습", "하드 연습"];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "#fs20#爲了打敗威爾，我們要移動到 #b'衍射的回廊'#k瑪?\r\n\r\n"
                if (cm.getPlayer().isGM()) {
         talk+= "#L4##b前往衍射的回廊(困難模式)#k#r(需等級235以上)#k#l\r\n\r\n"
              }
        //talk += "#L0#移動到衍射的回廊(#b簡單模式#k).#k#r(等級235以上)#k#l\r\n";
        //talk += "#L1#移動到衍射的回廊(#b普通模式#k).#k#r(等級235以上)#k#l\r\n";
        //talk += "#L2#移動到衍射的回廊(#b困難模式#k).#k#r(等級235以上)#k#l\r\n";
        //talk += "#L3#회절의 회랑(#b하드 연습 모드#k)으로 이동 한다.#k#r(레벨 235이상)#k#l\r\n";
        cm.sendSimpleS(talk, 0x86);
    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("必須組成1人以上的隊伍才能進入。", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(setting[st][2] + 50) >= 1 || cm.getPlayerCount(setting[st][2] + 100) >= 1 || cm.getPlayerCount(setting[st][2] + 150) >= 1 || cm.getPlayerCount(setting[st][2] + 200) >= 1 || cm.getPlayerCount(setting[st][2] + 250) >= 1) {
            cm.sendOkS("已經有人在挑戰威爾了。\r\n請使用其他頻道。", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("只有隊長才能申請進入。", 0x26);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("所有隊員必須在同一地點。");
            cm.dispose();
            return;
        }
        a = 0;
        /*if (!cm.partyhaveItem(4036166, 1)) {
            talk = "파티원 중 #i4036166# #b#z4036166##k를 소지하고 있지 않은 파티원이 있습니다.";
            cm.sendOkS(talk, 0x04, 9010061);
            cm.dispose();
            return;
        } else {
            cm.asd123(4036166);
        }*/

        /*if (!cm.isBossAvailable(setting[st][0], setting[st][1] + a) && st != 2 && st != 3) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        }*/
        if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中 "
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n的等級不足。 威爾 " + name[st] + "模式需要" + setting[st][3] + " 級以上才能進入。";
            cm.sendOkS(talk, 0x26);
            cm.dispose();
            return;
        }

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
var status = -1;


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Hard_Lotus", 2, 350060700, 190],
        ["Normal_Lotus", 2, 350060400, 190],
        ["Hard_Lotus", 2, 350060700, 190],
        ["Normal_Lotus", 2, 350060400, 190],
        ["Extreme_Lotus", 2, 350060700, 190],
    ]
    name = ["하드", "노멀", "하드 연습", "노말 연습"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "#fs23#爲了打倒斯烏，要不要移動到黑色天堂核心?\r\n\r\n"
        //        if (cm.getPlayer().isGM()) {
        //talk += "#L4##b블랙헤븐 코어(익스트림 모드)#k로 이동 한다.(레벨 190이상)#l\r\n"
        //      }
        talk += "#L0#前往黑色天堂核心(#b困難模式#k).(190級以上)#l\r\n"
        //talk += "#L3#블랙헤븐 코어(#b노멀 연습 모드#k)로 이동 한다.(레벨 190이상)#l\r\n"
        //talk += "#L2#블랙헤븐 코어(#b하드 연습 모드#k)로 이동 한다.(레벨 190이상)#l\r\n\r\n"
        talk += "#L4#不移動.\r\n"
        cm.sendSimpleS(talk, 0x86);
    } else if (status == 1) {
        if (selection == 4) {
            cm.dispose();
            return;
        }
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("#fs15#1人以上組隊才能入場.", 0x24);
            cm.getPlayer().dropMessage(6, "是這裡嗎? 1");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(Number(setting[st][2] + 100)) >= 1 || cm.getPlayerCount(Number(setting[st][2] + 200)) >= 1) {
            cm.sendOkS("#fs15#已經有人在挑戰斯烏了.\r\n請使用其他頻道.", 0x24);
            cm.getPlayer().dropMessage(6, "是這裡嗎? 2");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("#fs15#只有隊長才能申請入場.", 0x24);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("#fs15#所有成員必須在同一地點.");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 2 && st != 3) {
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
            talk += "#k#n您的等級不足. 斯烏 " + name[st] + "模式只能進入 " + setting[st][3] + " 等級以上.";
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
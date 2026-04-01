var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
 var setting = [
             ["Normal_Dunkel", 1, 450012210, 255],
             ["Hard_Dunkel", 1, 450012600, 255],
             ["Extreme_Dunkel", 1, 450012600, 255],
             ["Hell_Dunkel", 1, 450012600, 255]
         ];
 var name = ["노말", "하드" , "익스트림" , "헬"];

    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
         talk = "我們必須封鎖親衛隊長大將敦凱爾和他的親衛隊。\r\n\r\n";
         talk +="#L0#移動到接近終點的地方(#b普通模式#k).(等級255以上)#l\r\n"
         talk +="#L1#移動到接近終點的地方(#b困難模式#k).(等級255以上)#l\r\n"
         cm.sendSimpleS(talk, 0x26);

    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("必須舉辦1人以上的隊伍才能入場.", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOkS("已經有人在挑戰沙敦凱爾了.\r\n請換線重試.", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("只有隊長才能申請入場.", 0x26);
            cm.dispose();
            return;
	} else if (!cm.allMembersHere()) {
	    cm.sendOk("所有成員必須在同一地點.");
	    cm.dispose();
            return;
        }

        getData();
        time = new Date();
        year = time.getFullYear() % 100;
        month2 = time.getMonth() + 1;
        month = time.getMonth() + 1 < 10 ?  "0"+month2 : month2;
        date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
        date = year+"/"+month+"/"+date2;
        if (cm.getPlayer().getV_boss(""+setting[st][0]+"_" +date) == null) {
            cm.getPlayer().addKV_boss(""+setting[st][0]+"_" +date, "0");
        }
        var it = cm.getPlayer().getParty().getMembers().iterator();
            while (it.hasNext()) {
                   var chr = it.next();
                   var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
                       if (pchr.getPlayer().getV_boss(""+setting[st][0]+"_" +date) >= 1 && !cm.getPlayer().isGM()) {
                             cm.sendOk("請確認隊員的進入次數。");
                             cm.dispose();
                             return;
                         }
                   }

        if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中 "
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n等級不足。敦凱爾 " + name[st] + "模式需要" + setting[st][3] + " 級以上才能進入.";
            cm.sendOkS(talk, 0x28);
            cm.dispose();
            return;
        } else {
       if (st == 0 || st == 1) {
        var it = cm.getPlayer().getParty().getMembers().iterator();
        while (it.hasNext()) {
        var chr = it.next();
        var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
        // pchr.getClient().setKeyValue_Boss(setting[st][0] + "_" + date, 1);
        pchr.getPlayer().addKV_boss(""+setting[st][0]+"_" +date, 1);
            }
        em = cm.getEventManager(setting[st][0]);
        if (em != null) {
        cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
                    }
        cm.addBoss(setting[st][0]);
        cm.dispose();
            } else if (st == 2) {
                cm.sendYesNoS("選擇了進入極限模式。 在極限模式中，傷害將减少50%，但會比, #b#e掉落更强大的裝備獎勵，同時還會附帶額外選項.#n#k使用#b#e伊諾森特咒語書#k#n時，這些裝備的額外選項將會一同消失，並且無法恢復。您確定要進入嗎？", 4, 2007);
            } else if (st == 3) {
	    cm.sendYesNoS("您選擇了進入地獄模式。 在地獄模式中，傷害將减少90%，但會比#b#e掉落更强大的裝備獎勵，同時還會附帶額外選項。#n#k使用#b#e伊諾森特咒語書#k#n時，這些裝備的額外選項將會一同消失，並且無法恢復。", 4, 2007);
	            }
        }

    } else if (status == 2) {
        if (st == 2 || st == 3) {
        var it = cm.getPlayer().getParty().getMembers().iterator();
        while (it.hasNext()) {
        var chr = it.next();
        var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
        // pchr.getClient().setKeyValue_Boss(setting[st][0] + "_" + date, 1);
        pchr.getPlayer().addKV_boss(""+setting[st][0]+"_" +date, 1);
            }
        em = cm.getEventManager(setting[st][0]);
        if (em != null) {
        cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
                    }
        cm.addBoss(setting[st][0]);
        cm.dispose();
	        }
        }
    }

function getData() {
    time = new Date();
    year = time.getFullYear();
    month = time.getMonth() + 1;
    if (month < 10) {
        month = "0"+month;
    }
    date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
    date = year+""+month+""+date2;
    day = time.getDay();
    hour = time.getHours();
    minute = time.getMinutes();
}
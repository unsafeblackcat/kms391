var status = -1;

var setting = [["Hard_Seren", 2, 410002000, 265], ["Extreme_Seren", 2, 410002000, 265], ["Hell_Seren", 2, 410002000, 265]];
var name = ["하드", "익스트림", "헬"];

var hour, minute;
var year, month, date2, date, day;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
         talk = "要去#b王宮大廳#k與賽倫對決嗎？\r\n"
         talk += "#L0#移動到接近終點的地方(#Cblue#普通模式#k) (等級265以上)#k\r\n"
         //talk += "#L1#移動到接近終點的地方(#Cred#困難模式#k) (等級265以上)#k\r\n"
         //talk += "#L2#移動到接近終點的地方(#Cred#極限模式#k) (等級265以上)#k\r\n"
         talk += "#L3#我再想想.\r\n"
         cm.sendSimpleS(talk, 0x26);

    } else if (status == 1) {
        st = selection;
          if (st == 3) {
                    cm.dispose();
                    return;
                }
        if (cm.getParty() == null) {
            cm.sendOkS("#fs15#必須組成1人以上的隊伍才能進入。", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(410002000) >= 1 || cm.getPlayerCount(410002000) >= 1 ) {
            cm.sendOkS("#fs15#已經有人正在挑戰賽倫了。\r\n請利用其他頻道。", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("#fs15#只有隊長才能申請進入。", 0x26);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("#fs15#所有成員必須在同一地點。");
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
                             	    if (pchr.getPlayer().getV_boss(""+setting[st][0]+"_" +date) >= 3) {
                             	        cm.sendOk("#fs15#請確認隊員的入場次數。");
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
            talk += "#k#n的等級不足。 賽倫 " + name[st] + "模式需要" + setting[st][3] + " 級以上才能進入。";
            cm.sendOkS(talk, 0x28);
            cm.dispose();
            return;
        } else {
       if (st == 0 || st == 1) {
         cm.gainItem(4001895, -1);
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
                cm.sendYesNoS("#fs15#選擇了進入極限模式。 在極限模式中，傷害將减少50%，但會掉落比 #b#e更强大的裝備獎勵，且這些裝備還會附帶額外選項。 #n#k然而，當使用#b#e伊諾森特咒語書#k#n 時，這些道具將會消失，且無法恢復。 確定要進入嗎？", 4, 3004542);
            } else if (st == 3) {
	    cm.sendYesNoS("#fs15#選擇了進入地獄模式。 在地獄模式中，傷害將减少90%，但會掉落比 #b#e更强大的裝備獎勵，且這些裝備還會附帶額外選項。#n#k然而，當使用#b#e伊諾森特咒語書#k#n 時，這些道具將會消失，且無法恢復。 確定要進入嗎？", 4, 3004542);
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
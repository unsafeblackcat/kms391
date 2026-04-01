var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}
var hour, minute;
var year, month, date2, date, day

function action(mode, type, selection) {
var setting = [["Black_Mage", 1, 450013000, 255], ["Extreme_Black_Mage", 1, 450013000, 255], ["Hell_Black_Mage", 1, 450013000, 255]];
var name = ["하드", "익스트림", "헬"];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.showSpineScreen(false, false, false, "Effect/Direction20.img/bossBlackMage/start2_spine/black_space", "animation", 0, false, ""));
         talk = "爲了對抗 黑魔法師 消耗#i4001895#前往#b黑暗神殿#k?\r\n"
         talk += "#L1#前往黑暗神殿(#Cred#極限模式#k).(等級255以上) #k#l\r\n";
         talk += "#L2#前往黑暗神殿(#Cred#地獄模式#k). (等級255以上) #k#l\r\n";
         talk += "#L3#不移動.#l\r\n";
         cm.sendSimpleS(talk, 0x26);
    } else if (status == 1) {
        st = selection;
        if (st == 3) {
            cm.dispose();
            return;
        }
        if (!cm.partyhaveItem(4001895, 1)) {
            talk = "隊員中有不持有#i4001895##b#z4001895##k的隊員.";
            cm.sendOkS(talk, 0x04, 9010061);
            cm.dispose();
            return;
        }
        if (cm.getParty() == null) {
            cm.sendOkS("至少需要1人以上的組隊模式..", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(450010100) >= 1) {
            cm.sendOkS("好像已經有人在挑戰黑魔法師了… \r\n請使用其他頻道.", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("只有隊長才能申請入場..", 0x26);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("所有成員必須在同一地點.");
            cm.dispose();
            return;
        }
       if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
                cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
                cm.dispose();
                return;
            } else if (!cm.isLevelAvailable(setting[st][3])) {
                talk = "파티원 중 "
                for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                    if (i != 0) {
                        talk += ", "
                    }
                    talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
                }
                talk += "#k#n等級不足… " + name[st] + "黑魔法師 " + setting[st][3] + " 模式好像只有等級以上才能進入.";
                cm.sendOkS(talk, 0x26);
                cm.dispose();
                return;
            } else {
             if (st == 0) {
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
                        } else if (st == 1) {
                            cm.sendYesNoS("#fs15#選擇了進入極限模式。在極限模式中，傷害將降低50%，但會比平時#b#e掉落更强大的裝備奬勵，同時還會附帶額外選項.#n#k使用#i4001895#時，道具將會一同消失，且無法恢復。確定要進入瑪？", 4, 2007);
                        } else if (st == 2) {
            	    cm.sendYesNoS("#fs15#選擇了進入地獄模式。 在地獄模式中，傷害將降低90%，但會比平時#b#e掉落更强大的裝備奬勵，同時還會附帶額外選項。#n#k使用#i4001895#時，道具將會一同消失，且無法恢復。確定要進入瑪？", 4, 2007);
            	            }
                    }

                } else if (status == 2) {
                    if (st == 1|| st == 2) {
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
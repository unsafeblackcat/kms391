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
         talk += "#L0#前往黑暗神殿 (#Cblue#困難模式#k). (255級或以上) #k#l\r\n";
         talk += "#L3#我再想想.#l\r\n";
         cm.sendSimpleS(talk, 0x26);
    } else if (status == 1) {
        st = selection;
        if (st == 3) {
            cm.dispose();
            return;
        }
        if (!cm.partyhaveItem(4001895, 1)) {
            talk = "有一名同伴沒有#i4001895# #b#z4001895##k.";
            cm.sendOkS(talk, 0x04, 9010061);
            cm.dispose();
            return;
        }
        if (cm.getParty() == null) {
            cm.sendOkS("似乎只有在至少一個人的隊伍中才能進入..", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(450010100) >= 1) {
            cm.sendOkS("看起來有人已經在挑戰黑魔法師了...\r\n請使用其他頻道.", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("好像只有隊長才能申請進入..", 0x26);
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
                talk += "#k#n等級不足.. " + name[st] + "黑魔法師好像只有等級以上 " + setting[st][3] + "才能進入該模式.";
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
                            cm.sendYesNoS("你選擇進入極限模式，極限模式下，傷害降低50%，但比, #b#e更強的裝備獎勵會掉落，並附帶附加選項.#n#k 使用 #b#e命運碎片時，裝備的附加選項會消失，且無法恢復.#k#n 是否要進入?", 4, 2007);
                        } else if (st == 2) {
            	    cm.sendYesNoS("你選擇進入地獄模式，地獄模式下，傷害減少90%，但比#b#e更強的裝備獎勵會掉落，並附帶附加選項.#n#k 使用 #b#e命運碎片時，裝備的附加選項會消失，且無法恢復.#k#n 是否要進入?", 4, 2007);
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
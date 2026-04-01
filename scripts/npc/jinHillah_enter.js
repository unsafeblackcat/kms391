var status = -1;


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
var setting = [
             ["Normal_JinHillah", 1, 450010400, 250],
             ["Extreme_JinHillah", 1, 450010400, 250],
             ["Hell_JinHillah", 1, 450010400, 250]
            ];
var   name = ["노말", "익스트림", "헬"];

    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "現在我們要進入欲望的神殿，對抗眞·希拉.\r\n"
        talk += "#L0#移動到眞·希拉 (#Cblue#普通模式#k). #k#w(等級250以上)#k#l\r\n";
        talk += "#L3#不移動。\r\n"
         cm.sendSimpleS(talk, 0x26);
    } else if (status == 1) {
        if (selection == 3) {
            cm.dispose();
            return;
        }
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("必須組成1人以上的隊伍才能進入。", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOkS("已經有人在挑戰眞·希拉了。\r\n請使用其他頻道。", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("只有隊長才能申請進入.", 0x26);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("所有隊員必須在同一地點。");
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
                  talk += "#k#n的等級不足。 眞·希拉 " + name[st] + "模式需要 " + setting[st][3] + " 級以上才能進入。";
                  cm.sendOkS(talk, 0x28);
                  cm.dispose();
                  return;
              } else {
             if (st == 0) {
              var it = cm.getPlayer().getParty().getMembers().iterator();
              while (it.hasNext()) {
              var chr = it.next();
              var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
              pchr.getPlayer().addKV_boss(""+setting[st][0]+"_" +date, 1);
                  }
              em = cm.getEventManager(setting[st][0]);
              if (em != null) {
              cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
                          }
              cm.addBoss(setting[st][0]);
              cm.dispose();
                  } else if (st == 1) {
                      cm.sendYesNoS("#fs15#如果選擇了極端模式，進入后會降低50%的傷害，但會掉落比平時#b#e更强大的裝備奬勵以及附加選項。#n#k 使用 #b#e瞬移卷軸#k#n 時，該道具會一同消失，且无法恢複。確定要進入瑪?", 4, 2007);
                  } else if (st == 2) {
      	    cm.sendYesNoS("#fs15#如果選擇了地獄模式，進入后會降低90%的傷害，但會掉落比平時#b#e更强大的裝備奬勵以及附加選項。#n#k 使用 #b#e瞬移卷軸#k#n 時，該道具會一同消失，且无法恢複。確定要進入瑪?", 4, 2007);
      	            }
              }

          } else if (status == 2) {
              if (st == 1 || st == 2) {
              var it = cm.getPlayer().getParty().getMembers().iterator();
              while (it.hasNext()) {
              var chr = it.next();
              var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
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
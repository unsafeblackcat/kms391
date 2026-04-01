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
        talk = "이제 욕망의 제단으로 진입하여 진 힐라를 대적해야 한다.\r\n\r\n"
        talk += "#L0#진 힐라 (#Cblue#노말 모드#k)으로 이동한다. #k#w(레벨 250이상)#k#l\r\n";
        talk += "#L1#진 힐라 (#Cred#익스트림 모드#k)으로 이동한다. #k#w(레벨 250이상)#k#l\r\n";
        talk += "#L2#진 힐라 (#Cred#헬 모드#k)으로 이동한다. #k#w(레벨 250이상)#k#l\r\n";
        talk += "#L3#이동하지 않는다.\r\n"
         cm.sendSimpleS(talk, 0x26);
    } else if (status == 1) {
        if (selection == 3) {
            cm.dispose();
            return;
        }
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("1인 이상 파티를 맺어야만 입장할 수 있습니다.", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOkS("이미 누군가가 세렌 도전하고 있습니다.\r\n다른채널을 이용 해 주세요.", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("파티장만이 입장을 신청할 수 있습니다.", 0x26);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
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
                                   cm.sendOk("파티원의 입장 횟수를 확인해주시길 바랍니다.");
                                   cm.dispose();
                                   return;
                               }
                         }

              if (!cm.isLevelAvailable(setting[st][3])) {
                  talk = "파티원 중 "
                  for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                      if (i != 0) {
                          talk += ", "
                      }
                      talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
                  }
                  talk += "#k#n님의 레벨이 부족합니다. 세렌 " + name[st] + "모드는 " + setting[st][3] + " 레벨 이상만 입장 가능합니다.";
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
                      cm.sendYesNoS("익스트림 모드에 입장을 선택하셨습니다. 익스트림 모드에서는 데미지가 50% 감소하지만, #b#e보다 강력한 장비 보상이 추가옵션과 함께 드롭됩니다.#n#k 해당 장비의 추가옵션은 #b#e이노센트 주문서 이용 시 함께 사라지며, 이는 복구가 불가능합니다.#k#n 입장하시겠습니까?", 4, 2007);
                  } else if (st == 2) {
      	    cm.sendYesNoS("헬 모드에 입장을 선택하셨습니다. 헬 모드에서는 데미지가 90% 감소하지만, #b#e보다 강력한 장비 보상이 추가옵션과 함께 드롭됩니다.#n#k 해당 장비의 추가옵션은 #b#e이노센트 주문서 이용 시 함께 사라지며, 이는 복구가 불가능합니다.#k#n 입장하시겠습니까?", 4, 2007);
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
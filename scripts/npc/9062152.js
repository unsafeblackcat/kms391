var status = -1;
var year, month, date2, date, day;
var hour, minute;
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose(); 
        return;
    }
    switch (cm.getPlayer().getMapId())  {
        case 993080500: // 練習場退出確認 
            if (mode == 0) {
                if (status == 0 && type == 3) {
                    cm.sendOk(" 隨時想離開都可以再來找我");
                    cm.dispose(); 
                    return;
                }
                status--;
            }
            if (mode == 1) {
                status++;
            }
            if (status == 0) {
                cm.sendYesNo(" 看到你認真練習的樣子真不錯！\r\n那麼現在要離開嗎？");
            } else if (status == 1) {
                cm.warp(100000000,  1); // 傳回弓箭手村 
                cm.dispose(); 
            }
            break;
 
        case 993080400: // 冒險訓練結算地圖
            if (mode == 0) {
                if (status == 0 && type == 3) {
                    cm.sendOk(" 隨時想離開都可以再來找我");
                    cm.dispose(); 
                    return;
                }
                status--;
            }
            if (mode == 1) {
                status++;
            }
            if (status == 0) {
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.getClock(0)); 
                var em = cm.getEventManager("AdventureDrill"); 
                eim = cm.getPlayer().getEventInstance(); 
                if (eim == null) {
                    cm.dispose(); 
                    cm.getPlayer().dropMessage(5,  "因角色重新登入等原因，稻草人擊殺紀錄未被保存。請透過冒險訓練NPC重新挑戰。");
                    cm.warp(100000000,  1);
                    return;
                }
                mobcount = parseInt(em.getProperty("Monster")); 
                coinqty = (Math.floor((mobcount  - 1) / 4) + 1) * 10;
                if (mobcount == 0) {
                    cm.sendNext(" 你沒有擊殺任何稻草人，無法獲得獎勵。\r\n請多加訓練後再回來挑戰！");
                } else {
                    talk = "成功擊殺 #b#e" + mobcount + "隻#k#n 稻草人！\r\n"
                    talk += "可獲得 #b#e#i4009005##z4009005# " + coinqty + "個#k#n"
                    cm.sendNext(talk); 
                }
            } else if (status == 1) {
                if (mobcount == 0) {
                    eim.restartEventTimer(100); 
                    cm.dispose(); 
                    return;
                } else {
                    cm.sendNextPrev(" 若不滿意結果，可以 #e不領取獎勵直接退出#k#n");
                }
            } else if (status == 2) {
                cm.sendNextPrev(" 這樣 #e今日挑戰次數#n 不會被扣除，隨時可以重新挑戰");
            } else if (status == 3) {
                talk = "現在要領取獎勵並退出嗎？\r\n\r\n"
                talk += "#b#e- 擊殺稻草人 : " + mobcount + " / 40\r\n"
                talk += "- 可獲得 #b#i4009005##z4009005# : " + coinqty + "個#k\r\n\r\n"
                talk += "#L0#領取獎勵並退出#l\r\n"
                talk += "#L1#不領取獎勵直接退出#l\r\n\r\n"
                talk += "#r※ 獎勵每日每個角色僅能領取一次#k#n"
                cm.sendSimple(talk); 
            } else if (status == 4) {
                if (selection == 0) {
                    getData();
                    cm.setKeyValue(date,  "AdventureDrill", "1");
                    cm.gainItem(4009005,  coinqty);
                    eim.restartEventTimer(100); 
                    cm.dispose(); 
                    return;
                } else {
                    cm.sendNext(" 好的，歡迎隨時回來挑戰。");
                }
            } else if (status == 5) {
                eim.restartEventTimer(100); 
                cm.dispose(); 
                return;
            }
            break;
 
        case 993080200: // 訓練中退出確認
            if (mode == 0) {
                if (status == 0 && type == 3) {
                    cm.sendOk(" 那麼請繼續努力擊殺更多稻草人吧！");
                    cm.dispose(); 
                    return;
                }
                status--;
            }
            if (mode == 1) {
                status++;
            }
            if (status == 0) {
                cm.sendYesNo(" 還有剩餘時間！\r\n確定要現在退出嗎？");
            } else if (status == 1) {
                eim = cm.getPlayer().getEventInstance(); 
                eim.restartEventTimer(1); 
                cm.dispose(); 
            }
            break;
 
        default:
            if (mode == 0) {
                status--;
            }
            if (mode == 1) {
                status++;
            }
            cm.sendOk("[ 系統錯誤] 調用了不應被執行的預設值，請聯繫GM");
            cm.dispose(); 
            return;
    }
}
 
// 獲取當前日期
function getData() {
   time = new Date();
   year = time.getFullYear(); 
   month = time.getMonth()  + 1;
   if (month < 10) {
      month = "0"+month;
   }
   date2 = time.getDate()  < 10 ? "0"+time.getDate()  : time.getDate(); 
   date = year+""+month+""+date2;
   day = time.getDay(); 
   hour = time.getHours(); 
   minute = time.getMinutes(); 
}
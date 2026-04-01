var status = -1;
 
var year, month, date2, date, day;
var hour, minute;
// 冒險訓練 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    talk = ""
    for (i = 1; i <= 10; i++) {
        if (i != 1)
            talk += "\r\n";
        talk += "#e- " + i + " ~ " + (i * 4) + "隻 : #i4009005##z4009005# " + (i * 10) + "個#n"
    }
    dialogue = [
        ["nextprev", 0, "#b#e<冒險訓練>#k#n是在1分鐘內擊殺最多#e稻草人怪物#n的任務"],
        ["nextprev", 0, "總共10個平台分別配置4種不同的稻草人怪物"],
        ["nextprev", 0, "#b#e<普通稻草人>#k#n\r\n\r\n#fMob/9300799.img/stand/0#\r\n 首先是#e普通稻草人#n，沒有特殊特徵"],
        ["nextprev", 0, "#b#e<旺吉稻草人>#k#n\r\n\r\n#fMob/9305681.img/stand/0#\r\n 第二種是#e旺吉稻草人#n\r\n與普通稻草人不同，具有#e防禦率#n"],
        ["nextprev", 0, "#b#e<肯迪稻草人>#k#n\r\n\r\n#fMob/9305678.img/stand/0#\r\n 第三種是#e肯迪稻草人#n\r\n以#e高血量與BOSS怪物屬性#n聞名"],
        ["nextprev", 0, "#b#e<小弓稻草人>#k#n\r\n\r\n#fMob/9305679.img/stand/0#\r\n 最後是#e小弓稻草人#n\r\n越往上層平台#e等級#n會逐漸提高"],
        ["nextprev", 0, "當然每種稻草人隨著層數提升會擁有更強的血量與能力"],
        ["nextprev", 0, "根據1分鐘內擊殺的稻草人數量，會發放#b#e#i4009005##z4009005##k#n"],
        ["nextprev", 0, talk],
        ["nextprev", 0, "#b#e<冒險訓練>#k#n每個角色#r#e每日限挑戰一次#k#n，請注意"],
        ["nextprev", 0, "若對結果不滿意，可以#e放棄獎勵#n隨時#e重新挑戰#n"],
        ["nextprev", 0, "只有真正的強者才有資格享受新冒險！"],
        ["nextprev", 0, "去吧！#b" + cm.getPlayer().getName()  + "#k！\r\n征服#b#e<冒險訓練>#k#n後回來吧！"]
    ]
    if (mode == -1) {
        cm.dispose(); 
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        talk = "#e<冒險訓練>#n準備好了嗎？\r\n\r\n"
        talk += "#L0##e<冒險訓練>#n實戰模式入場#l\r\n"
        talk += "#L2##e<冒險訓練>#n再次聽取說明"
        cm.sendSimple(talk); 
    } else {
        if (status == 1) {
            st = selection;
        }
        if (st == 2) {
            if (status <= dialogue.length)  {
                sendByType(dialogue[status - 1][0], dialogue[status - 1][1], dialogue[status][2]);
            } else {
                cm.dispose(); 
            }
        } else {
            if (status == 1) {
                if (selection == 0) {
                    getData();
                    if (cm.getPlayer().getParty()  != null) {
                        cm.sendOk("#e< 冒險訓練>#n不可組隊入場");
                        cm.dispose(); 
                        return;
                    }
                    if (cm.getKeyValue(date,  "AdventureDrill") == 1) {
                        cm.sendNext(" 今天已經用#b#e" + cm.getPlayer().getName()  + "#k#n完成<冒險訓練>了！")
                    } else {
                        if (cm.getEventManager("AdventureDrill").getInstance("AdventureDrill")  != null) {
                            cm.sendOk(" 已經有其他玩家在挑戰#e<冒險訓練>#n，請換頻道");
                            cm.dispose(); 
                            return;
                        }
                        var event = cm.getEventManager("AdventureDrill").getInstance("AdventureDrill"); 
                        if (event == null) {
                            cm.getEventManager("AdventureDrill").startInstance_Solo(""  + 993080200, cm.getPlayer()); 
                            cm.dispose(); 
                        } else {
                            cm.sendOk(" 已經有其他玩家在挑戰#e<冒險訓練>#n，請換頻道");
                            cm.dispose(); 
                            return;
                        }
                    }
                } else {
                    if (cm.getPlayerCount(180000000)  != 0) {
                        cm.sendOk(" 已經有其他玩家在練習#e<冒險訓練>#n，請換頻道");
                        cm.dispose(); 
                        return;
                    }
                    cm.resetMap(180000000); 
                    cm.warp(180000000,  0);
                    var mobx = [45, 240, 440, 635];
                    var moby = [-357, -657, -957, -1257, -1557, -1857, -2157, -2457, -2757, -3057]
                    for (i = 0; i < mobx.length;  i++) {
                        for (j = 0; j < moby.length;  j++) {
                            var mobid = 9833338 + (i * 10) + j;
                            cm.spawnMob(mobid,  mobx[i], moby[j]);
                        }
                    }
                    cm.dispose(); 
                }
            } else if (status == 2) {
                cm.sendOk(" 明天再來挑戰吧！");
                cm.dispose(); 
            }
        }
    }
}
 
function sendByType(type, type2, text) {
    switch (type) {
        case "next":
            cm.sendNextS(text,  type2);
            break;
        case "nextprev":
            cm.sendNextPrevS(text,  type2);
            break;
    }
}
 
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
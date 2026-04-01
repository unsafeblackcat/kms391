/*
MelonK
*/

importPackage(Packages.tools.packet);
importPackage(java.util);
importPackage(java.lang);
var St = 0;

qnum = 4; // 퀘스트 고유번호 (겹치지만 않으면 됨); 내가 보낸거랑 퀘스트 번호도 다르넹
questlist = [
    [8644000, 30, "mob"],
    [8644001, 30, "mob"],
    [8644002, 30, "mob"],
    [8644003, 30, "mob"],
    [8644004, 30, "mob"],
    [8644005, 30, "mob"],
    [8644006, 30, "mob"],
    [8644007, 30, "mob"],
    [8644008, 30, "mob"],
    [8644009, 30, "mob"],
    [8644010, 30, "mob"]
]

qClear = [
[4033897, 500],
[4310237, 3000],
[4310228, 500],
]; 

//호텔 아르크스
//Don't Touch :D
count1 = 0;
count2 = 0;
isnewed = true;
qarr = [];
questarray = [];
color = ["b", "b", "b", "b", "b"];
var Stplus = true;

function start() {
    getData();
    time = new Date();
    year = time.getFullYear();
    month = time.getMonth() + 1;
    date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
    date = year+""+month+""+date2;
    St = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    a = cm.getPlayer().getV("arcane_quest_" + qnum + "_" + date);

    if (mode == -1 || mode == 0) {
        if (St == 0 && Stplus == true) {
            isnewed = false;
            St = 3;
        } else {
            cm.dispose();
            return;
        }
    }
    if (mode == 1) {
        if (St == -1) {
            b = a;
        }
        if (b < 0 && St == 2 && selection != 100) {
            if (color[selection] == "b") {
                color[selection] = "k";
            } else {
                color[selection] = "b";
            }
        } else {
            St++;
        }
    }

    if (b == null || b < 1) { // 일일퀘스트를 받지 않았을때


        if (St == 0) {
            for (i = 0; i < questlist.length; i++) {
                if (b == null || b < 0) {
                    clearquest(questlist[i][0]);
                }
                qarr.push(questlist[i]);
            }
            if (b == null || b < 0) {
                for (i = 0; i < 3; i++) {
                    rd = Math.floor(Math.random() * questlist.length)
                    cm.getPlayer().addKV("river_" + questlist[rd][0] + "_count", "0");
                    cm.getPlayer().addKV("river_" + questlist[rd][0] + "_" + questlist[rd][2] + "q", questlist[rd][1]);
                    cm.getPlayer().addKV("river_" + questlist[rd][0] + "_isclear", "0");
 cm.getPlayer().dropMessage(6, "replace");
                    questlist.splice(rd, 1); // 중복 방지
                }
                cm.getPlayer().addKV("arcane_quest_" + qnum + "_" + date, 0);
            } else {
                listed = 0;
                while (listed < 3) {
                    for (i = 0; i < questlist.length; i++) {
                        if (cm.getPlayer().getV("river_" + qarr[i][0] + "_" + qarr[i][2] + "q") > 0) {
                            questlist.splice(i, 1);
                            listed++;
                        }
                    }
                    if (listed < 3) {
                        break;
                    }
                }
            }
  
            if (cm.getPlayer().getClient().getKeyValue("check_" + qnum + "_" + date) == null) {
                cm.getPlayer().getClient().setKeyValue("check_" + qnum + "_" + date, "0");
            }
            dialogue = "#fs15#這週要拜託你的事情如下。\r\n\r\n"
            if (cm.getPlayer().getClient().getKeyValue("compensation_"  + qnum + "_" + date) == 1) {
                cm.sendOk("#fs15#今天的每日獎勵已經領取了。");
                cm.dispose(); 
                return;
            }
            for (i = 0; i < qarr.length;  i++) {
                if (cm.getPlayer().getV("river_"  + qarr[i][0] + "_mobq") > 0) {
                    dialogue += "#fs15##b#e[每日任務] #o" + qarr[i][0] + "# " + qarr[i][1] + "隻討伐 \r\n"
                    questarray.push(qarr[i]); 
                } else if (cm.getPlayer().getV("river_"  + qarr[i][0] + "_itemq") > 0) {
                    dialogue += "#fs15##b#e[每日任務] #z" + qarr[i][0] + "# " + qarr[i][1] + "個收集\r\n"
                    questarray.push(qarr[i]); 
                }
 
            }
            cm.sendConductExchange(dialogue);
        } else if (St == 1) {
            cm.sendYesNo("#fs15#現在就要開始執行嗎？\r\n\r\n#b(可以排除部分或全部任務並重新組成列表。)#k");
        } else if (St == 2) {
            newcheck = true;
            dialogue = "제외하고 싶은 임무를 골라주세요.\r\n\r\n"
            for (i = 0; i < questarray.length;  i++) {
                if (cm.getPlayer().getV("river_"  + questarray[i][0] + "_mobq") > 0) {
                    dialogue += "#L#fs15#" + i + "##" + color[i] + "#e[每日任務] #o" + questarray[i][0] + "# " + questarray[i][1] + "隻討伐#k#n#l\r\n"
                } else if (cm.getPlayer().getV("river_"  + questarray[i][0] + "_itemq") > 0) {
                    dialogue += "#L#fs15#" + i + "##" + color[i] + "#e[每日任務] #z" + questarray[i][0] + "# " + questarray[i][1] + "個收集#k#n#l\r\n"
                }
            }
            dialogue += "\r\n#L100##fs15##r#e沒有其他想排除的任務了。"
            cm.sendSimple(dialogue);
        } else if (St == 3) {
            for (i = 0; i < qarr.length; i++) {
                clearquest(qarr[i][0]);
            }
            talk = "";
            if (isnewed) {
                talk += "#fs15#已為你找到替代被排除任務的新任務。"
            }
            talk += "#fs15#這週要拜託你的事情如下。\r\n\r\n" + cm.getPlayer().getClient().getKeyValue("check_"  + qnum + "_" + date);
            for (i = 0; i < 3; i++) {
                if (color[i] == "k") { // 제외되었으면 (색으로 판단)
                    isnewed = true;
                    rd = Math.floor(Math.random() * questlist.length)
                    questarray[i] = questlist[rd];
                    questlist.splice(rd, 1); // 중복 방지 (questlist 배열의 rd번째를 제거)
                }
                isnew = color[i] == "k" ? "#r#e[NEW]#k#n" : "#k#n"
                cm.getPlayer().dropMessage(5, questarray[i]);
                if (questarray[i][2] == "mob") {
                    talk += "#fs15##b#e[每日任務] #o" + questarray[i][0] + "# " + questarray[i][1] + "隻討伐#k#n " + isnew + "\r\n"
                } else {
                    talk += "#fs15##b#e[每日任務] #z" + questarray[i][0] + "# " + questarray[i][1] + "個收集#k#n " + isnew + "\r\n"
                }
                cm.getPlayer().addKV("river_" + questarray[i][0] + "_count", 0);
                cm.getPlayer().addKV("river_" + questarray[i][0] + "_" + questarray[i][2] + "q", questarray[i][1]);
                cm.getPlayer().addKV("river_" + questarray[i][0] + "_isclear", 0);
            }
            cm.sendNext(talk);
            cm.getPlayer().addKV("arcane_quest_" + qnum + "_" + date, "1");
            cm.dispose();
        }
    } else {
        if (St == 0) {
            if (cm.getPlayer().getClient().getKeyValue("compensation_" + qnum + "_" + date) == 1) {
                cm.sendOk("#fs15#今天的每日獎勵已經領取了。");
                cm.dispose(); 
                return;
            }
 
            dialogue = "#fs15##b#e<每日任務>#k#n\r\n\r\n"
            dialogue2 = "";
            dialogue3 = "";

            if (cm.getPlayer().getClient().getKeyValue("compensation_" + qnum + "_" + date) == 1) {
                cm.sendOk("#fs15#今天的每日獎勵已經領取了。");
                cm.dispose();
                return;
            }
            for (i = 0; i < questlist.length;  i++) {
                if (cm.getPlayer().getV("river_"  + questlist[i][0] + "_mobq") > 0 && cm.getPlayer().getV("river_"  + questlist[i][0] + "_isclear") < 2) {
                    選項 = "#L#fs15#" + i + "##d[每日任務] #o" + questlist[i][0] + "# " + questlist[i][1] + "隻討伐#k"
                    if (cm.getPlayer().getV("river_"  + questlist[i][0] + "_count") >= Integer.parseInt(cm.getPlayer().getV("river_"  + questlist[i][0] + "_mobq"))) {
                        count1++;
                        dialogue2 += 選項 + " (可完成)\r\n"
                    } else {
                        count2++;
                        dialogue3 += 選項 + " (進行中)\r\n"
                    }
                } else if (cm.getPlayer().getV("river_"  + questlist[i][0] + "_itemq") > 0 && cm.getPlayer().getV("river_"  + questlist[i][0] + "_isclear") < 2) {
                    選項 = "#L#fs15#" + i + "##d[每日任務] #z" + questlist[i][0] + "# " + questlist[i][1] + "個收集#k"
                    if (cm.itemQuantity(questlist[i][0])  >= cm.getPlayer().getV("river_"  + questlist[i][0] + "_itemq")) {
                        count1++;
                        dialogue2 += 選項 + " (可完成)\r\n"
                    } else {
                        count2++;
                        dialogue3 += 選項 + " (進行中)\r\n"
                    }
                }
            }
            if (count1 >= 1) {
                dialogue += "\r\n#fUI/UIWindow2.img/UtilDlgEx/list3#\r\n" // 완료 가능한 퀘스트 UI
            }
            dialogue += dialogue2;
            dialogue += "\r\n"
            if (count2 >= 1) {
                dialogue += "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n" // 진행중 퀘스트 UI
            }
            dialogue += dialogue3;
            cm.sendSimple(dialogue);
        } else if (St == 1) {

            if (!cm.canHold(1713001, 3)) {
                cm.sendOk("#fs15#背包空間不足。");
                cm.dispose();
                return;
            }
            if ((cm.getPlayer().getV("river_" + questlist[selection][0] + "_mobq") > 0 && cm.getPlayer().getV("river_" + questlist[selection][0] + "_count") >= cm.getPlayer().getV("river_" + questlist[selection][0] + "_mobq")) ||
                (cm.getPlayer().getV("river_" + questlist[selection][0] + "_itemq") > 0 && cm.itemQuantity(questlist[selection][0]) >= cm.getPlayer().getV("river_" + questlist[selection][0] + "_itemq"))) {

                text2 = "#fs15#目前已完成 #b" + a + "#k 個任務。\r\n"

                if (cm.getPlayer().getV("river_" + questlist[selection][0] + "_itemq") > 0) {
                    cm.gainItem(questlist[selection][0], -cm.getPlayer().getV("river_" + questlist[selection][0] + "_itemq"));
                }

                cm.getPlayer().getClient().setKeyValue("check_" + qnum + "_" + date, Integer.parseInt(cm.getPlayer().getClient().getKeyValue("check_" + qnum + "_" + date)) + 1);
                cm.getPlayer().addKV("river_" + questlist[selection][0] + "_isclear", 2);
                cm.getPlayer().addKV("arcane_quest_" + qnum + "_" + date, parseInt(cm.getPlayer().getV("arcane_quest_" + qnum + "_" + date)) + 1);

                if (a >= 3 && cm.getPlayer().getClient().getKeyValue("check_" + qnum + "_" + date) == 3 && cm.getPlayer().getClient().getKeyValue("compensation_" + qnum + "_" + date) != 1) {
                    cm.getPlayer().getClient().setKeyValue("compensation_" + qnum + "_" + date, "1");
                    text2 += "#fs15#將發放以下物品。\r\n";
                    cm.sendOk(text2);
                   cm.getPlayer().setKeyValue(100592, "point", "" + (cm.getPlayer().getKeyValue(100592, "point") + 1));
                    for(var i = 0; i < qClear.length; i++) {
                        cm.gainItem(qClear[i][0], qClear[i][1]);
                    }
                    cm.dispose();
                    return;
                } else {
                    cm.sendOk(text2);
                }
                cm.dispose();
            } else {
                cm.sendOk("#fs15#尚未完成任務。");
            }
            cm.dispose();
            return;

        }
    }
}

function clearquest(paramint) {
    cm.getPlayer().addKV("river_" + paramint + "_count", -1);
    cm.getPlayer().addKV("river_" + paramint + "_mobq", -1);
    cm.getPlayer().addKV("river_" + paramint + "_itemq", -1);
    cm.getPlayer().addKV("river_" + paramint + "_isclear", -1);
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
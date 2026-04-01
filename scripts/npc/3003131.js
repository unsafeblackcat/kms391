/*
MelonK 
*/
 
importPackage(Packages.tools.packet); 
importPackage(java.util); 
importPackage(java.lang); 
var St = 0;
 
qnum = 1;
questlist = [
    [8641000, 50, "mob"],
    [8641001, 50, "mob"],
    [8641002, 50, "mob"],
    [8641003, 50, "mob"],
    [8641004, 50, "mob"],
    [8641005, 50, "mob"],
    [8641006, 50, "mob"],
    [8641007, 50, "mob"],
]
 
qClear = [
[4031788, 1000],
[4310009, 1],
]; 
 
//酒店阿爾克斯 
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
    month = time.getMonth()  + 1;
    date2 = time.getDate()  < 10 ? "0"+time.getDate()  : time.getDate(); 
    date = year+""+month+""+date2;
    St = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    a = cm.getPlayer().getV("arcane_quest_"  + qnum + "_" + date);
 
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
 
    if (b == null || b < 1) { // 未接受每日任務時 
 
        if (St == 0) {
            for (i = 0; i < questlist.length;  i++) {
                if (b == null || b < 0) {
                    clearquest(questlist[i][0]);
                }
                qarr.push(questlist[i]); 
            }
            if (b == null || b < 0) {
                for (i = 0; i < 3; i++) {
                    rd = Math.floor(Math.random()  * questlist.length) 
                    cm.getPlayer().addKV("river_"  + questlist[rd][0] + "_count", "0");
                    cm.getPlayer().addKV("river_"  + questlist[rd][0] + "_" + questlist[rd][2] + "q", questlist[rd][1]);
                    cm.getPlayer().addKV("river_"  + questlist[rd][0] + "_isclear", "0");
                    questlist.splice(rd,  1); // 防止重複 
                }
                cm.getPlayer().addKV("arcane_quest_"  + qnum + "_" + date, 0);
            } else {
                listed = 0;
                while (listed < 3) {
                    for (i = 0; i < questlist.length;  i++) {
                        if (cm.getPlayer().getV("river_"  + qarr[i][0] + "_" + qarr[i][2] + "q") > 0) {
                            questlist.splice(i,  1);
                            listed++;
                        }
                    }
                    if (listed < 3) {
                        break;
                    }
                }
            }
  
            if (cm.getPlayer().getClient().getKeyValue("check_"  + qnum + "_" + date) == null) {
                cm.getPlayer().getClient().setKeyValue("check_"  + qnum + "_" + date, "0");
            }
            dialogue = "#fs15#本週要委託你的事情如下。\r\n\r\n"
            if (cm.getPlayer().getClient().getKeyValue("compensation_"  + qnum + "_" + date) == 1) {
                cm.sendOk(" 您已經領取了今日的每日獎勵。");
                cm.dispose(); 
                return;
            }
            for (i = 0; i < qarr.length;  i++) {
                if (cm.getPlayer().getV("river_"  + qarr[i][0] + "_mobq") > 0) {
                    dialogue += "#b#e[每日任務] #o" + qarr[i][0] + "# 擊殺 " + qarr[i][1] + "隻\r\n"
                    questarray.push(qarr[i]); 
                } else if (cm.getPlayer().getV("river_"  + qarr[i][0] + "_itemq") > 0) {
                    dialogue += "#b#e[每日任務] #z" + qarr[i][0] + "# 收集 " + qarr[i][1] + "個\r\n"
                    questarray.push(qarr[i]); 
                }
 
            }
            cm.sendConductExchange(dialogue); 
        } else if (St == 1) {
            cm.sendYesNo(" 現在立刻開始執行嗎？\r\n\r\n#b(可以排除部分或全部任務並重新生成列表。)#k");
        } else if (St == 2) {
            newcheck = true;
            dialogue = "請選擇想要排除的任務。\r\n\r\n"
            for (i = 0; i < questarray.length;  i++) {
                if (cm.getPlayer().getV("river_"  + questarray[i][0] + "_mobq") > 0) {
                    dialogue += "#L" + i + "##" + color[i] + "#e[每日任務] #o" + questarray[i][0] + "# 擊殺 " + questarray[i][1] + "隻#k#n#l\r\n"
                } else if (cm.getPlayer().getV("river_"  + questarray[i][0] + "_itemq") > 0) {
                    dialogue += "#L" + i + "##" + color[i] + "#e[每日任務] #z" + questarray[i][0] + "# 收集 " + questarray[i][1] + "個#k#n#l\r\n"
                }
            }
            dialogue += "\r\n#L100##r#e沒有其他想排除的任務了。"
            cm.sendSimple(dialogue); 
        } else if (St == 3) {
            for (i = 0; i < qarr.length;  i++) {
                clearquest(qarr[i][0]);
            }
            talk = "";
            if (isnewed) {
                talk += "已為被排除的任務找到新任務。"
            }
            talk += "本週要委託你的事情如下。\r\n\r\n" + cm.getPlayer().getClient().getKeyValue("check_"  + qnum + "_" + date);
            for (i = 0; i < 3; i++) {
                if (color[i] == "k") { // 判斷是否被排除（根據顏色）
                    isnewed = true;
                    rd = Math.floor(Math.random()  * questlist.length) 
                    questarray[i] = questlist[rd];
                    questlist.splice(rd,  1); // 防止重複（移除questlist陣列中第rd個元素）
                }
                isnew = color[i] == "k" ? "#r#e[NEW]#k#n" : "#k#n"
                cm.getPlayer().dropMessage(5,  questarray[i]);
                if (questarray[i][2] == "mob") {
                    talk += "#b#e[每日任務] #o" + questarray[i][0] + "# 擊殺 " + questarray[i][1] + "隻#k#n " + isnew + "\r\n"
                } else {
                    talk += "#b#e[每日任務] #z" + questarray[i][0] + "# 收集 " + questarray[i][1] + "個#k#n " + isnew + "\r\n"
                }
                cm.getPlayer().addKV("river_"  + questarray[i][0] + "_count", 0);
                cm.getPlayer().addKV("river_"  + questarray[i][0] + "_" + questarray[i][2] + "q", questarray[i][1]);
                cm.getPlayer().addKV("river_"  + questarray[i][0] + "_isclear", 0);
            }
            cm.sendNext(talk); 
            cm.getPlayer().addKV("arcane_quest_"  + qnum + "_" + date, "1");
            cm.dispose(); 
        }
    } else {
        if (St == 0) {
            if (cm.getPlayer().getClient().getKeyValue("compensation_"  + qnum + "_" + date) == 1) {
                cm.sendOk(" 您已經領取了今日的每日獎勵。");
                cm.dispose(); 
                return;
            }
 
            dialogue = "#b#e<每日任務>#k#n\r\n\r\n"
            dialogue2 = "";
            dialogue3 = "";
 
            if (cm.getPlayer().getClient().getKeyValue("compensation_"  + qnum + "_" + date) == 1) {
                cm.sendOk(" 您已經領取了今日的每日獎勵。");
                cm.dispose(); 
                return;
            }
            for (i = 0; i < questlist.length;  i++) {
                if (cm.getPlayer().getV("river_"  + questlist[i][0] + "_mobq") > 0 && cm.getPlayer().getV("river_"  + questlist[i][0] + "_isclear") < 2) {
                    選項 = "#L" + i + "##d[每日任務] #o" + questlist[i][0] + "# 擊殺 " + questlist[i][1] + "隻#k"
                    if (cm.getPlayer().getV("river_"  + questlist[i][0] + "_count") >= Integer.parseInt(cm.getPlayer().getV("river_"  + questlist[i][0] + "_mobq"))) {
                        count1++; // cm.getPlayer().getV("river_"  + questlist[i][0] + "_mobq")
                        dialogue2 += 選項 + " (可完成)\r\n"
                    } else {
                        count2++;
                        dialogue3 += 選項 + " (進行中)\r\n"
                    }
                } else if (cm.getPlayer().getV("river_"  + questlist[i][0] + "_itemq") > 0 && cm.getPlayer().getV("river_"  + questlist[i][0] + "_isclear") < 2) {
                    選項 = "#L" + i + "##d[每日任務] #z" + questlist[i][0] + "# 收集 " + questlist[i][1] + "個#k"
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
                dialogue += "\r\n#fUI/UIWindow2.img/UtilDlgEx/list3#\r\n"  // 可完成任務UI 
            }
            dialogue += dialogue2;
            dialogue += "\r\n"
            if (count2 >= 1) {
                dialogue += "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n"  // 進行中任務UI 
            }
            dialogue += dialogue3;
            cm.sendSimple(dialogue); 
        } else if (St == 1) {
 
            if (!cm.canHold(1713001,  3)) {
                cm.sendOk(" 背包空間不足。");
                cm.dispose(); 
                return;
            }
            if ((cm.getPlayer().getV("river_"  + questlist[selection][0] + "_mobq") > 0 && cm.getPlayer().getV("river_"  + questlist[selection][0] + "_count") >= cm.getPlayer().getV("river_"  + questlist[selection][0] + "_mobq")) ||
                (cm.getPlayer().getV("river_"  + questlist[selection][0] + "_itemq") > 0 && cm.itemQuantity(questlist[selection][0])  >= cm.getPlayer().getV("river_"  + questlist[selection][0] + "_itemq"))) {
 
                text2 = "當前已完成 #b" + a + "#k 個任務。\r\n"
 
                if (cm.getPlayer().getV("river_"  + questlist[selection][0] + "_itemq") > 0) {
                    cm.gainItem(questlist[selection][0],  -cm.getPlayer().getV("river_"  + questlist[selection][0] + "_itemq"));
                }
 
                cm.getPlayer().getClient().setKeyValue("check_"  + qnum + "_" + date, Integer.parseInt(cm.getPlayer().getClient().getKeyValue("check_"  + qnum + "_" + date)) + 1);
                cm.getPlayer().addKV("river_"  + questlist[selection][0] + "_isclear", 2);
                cm.getPlayer().addKV("arcane_quest_"  + qnum + "_" + date, parseInt(cm.getPlayer().getV("arcane_quest_"  + qnum + "_" + date)) + 1);
 
                if (a >= 3 && cm.getPlayer().getClient().getKeyValue("check_"  + qnum + "_" + date) == 3 && cm.getPlayer().getClient().getKeyValue("compensation_"  + qnum + "_" + date) != 1) {
                    cm.getPlayer().getClient().setKeyValue("compensation_"  + qnum + "_" + date, "1");
                    text2 += "將發放以下物品：\r\n";
                    cm.sendOk(text2); 
                    cm.getPlayer().setKeyValue(100592,  "point", "" + (cm.getPlayer().getKeyValue(100592,  "point") + 1));
                    for(var i = 0; i < qClear.length;  i++) {
                        cm.gainItem(qClear[i][0],  qClear[i][1]);
                    }
                    cm.dispose(); 
                    return;
                } else {
                    cm.sendOk(text2); 
                }
                cm.dispose(); 
            } else {
                cm.sendOk(" 尚未完成任務。");
            }
            cm.dispose(); 
            return;
 
        }
    }
}
 
function clearquest(paramint) {
    cm.getPlayer().addKV("river_"  + paramint + "_count", -1);
    cm.getPlayer().addKV("river_"  + paramint + "_mobq", -1);
    cm.getPlayer().addKV("river_"  + paramint + "_itemq", -1);
    cm.getPlayer().addKV("river_"  + paramint + "_isclear", -1);
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
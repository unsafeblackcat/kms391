/*
MelonK
*/

importPackage(Packages.tools.packet);
importPackage(java.util);
importPackage(java.lang);
var St = 0;

qnum = 6; // 퀘스트 고유번호 (겹치지만 않으면 됨); 내가 보낸거랑 퀘스트 번호도 다르넹
questlist = [
    [8644500, 300, "mob"],
    [8644501, 300, "mob"],
    [8644502, 300, "mob"],
    [8644503, 300, "mob"],
    [8644504, 300, "mob"],
    [8644505, 300, "mob"],
    [8644506, 300, "mob"],
    [8644507, 300, "mob"],
    [8644508, 300, "mob"],
    [8644509, 300, "mob"],
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
            dialogue = "이번 주 자네에게 부탁할 일은 아래와 같다네.\r\n\r\n"
            if (cm.getPlayer().getClient().getKeyValue("compensation_" + qnum + "_" + date) == 1) {
                cm.sendOk("오늘의 일일 보상을 이미 받으셨습니다.");
                cm.dispose();
                return;
            }
            for (i = 0; i < qarr.length; i++) {
                if (cm.getPlayer().getV("river_" + qarr[i][0] + "_mobq") > 0) {
                    dialogue += "#b#e[일일 퀘스트] #o" + qarr[i][0] + "# " + qarr[i][1] + "마리 퇴치 \r\n"
                    questarray.push(qarr[i]);
                } else if (cm.getPlayer().getV("river_" + qarr[i][0] + "_itemq") > 0) {
                    dialogue += "#b#e[일일 퀘스트] #z" + qarr[i][0] + "# " + qarr[i][1] + "개 수집\r\n"
                    questarray.push(qarr[i]);
                }

            }
            cm.sendConductExchange(dialogue);
        } else if (St == 1) {
            cm.sendYesNo("지금 바로 수행하시겠나?\r\n\r\n#b(일부 임무 혹은 전체 임무를 제외시키고 목록을 재구성 합니다.)#k");
        } else if (St == 2) {
            newcheck = true;
            dialogue = "제외하고 싶은 임무를 골라주세요.\r\n\r\n"
            for (i = 0; i < questarray.length; i++) {
                if (cm.getPlayer().getV("river_" + questarray[i][0] + "_mobq") > 0) {
                    dialogue += "#L" + i + "##" + color[i] + "#e[일일 퀘스트] #o" + questarray[i][0] + "# " + questarray[i][1] + "마리 퇴치#k#n#l\r\n"
                } else if (cm.getPlayer().getV("river_" + questarray[i][0] + "_itemq") > 0) {
                    dialogue += "#L" + i + "##" + color[i] + "#e[일일 퀘스트] #z" + questarray[i][0] + "# " + questarray[i][1] + "개 수집#k#n#l\r\n"
                }
            }
            dialogue += "\r\n#L100##r#e더 이상 제외하고 싶은 임무는 없다."
            cm.sendSimple(dialogue);
        } else if (St == 3) {
            for (i = 0; i < qarr.length; i++) {
                clearquest(qarr[i][0]);
            }
            talk = "";
            if (isnewed) {
                talk += "제외된 임무 대신 새로운 임무를 찾았습니다."
            }
            talk += "이번 주 자네에게 부탁할 일은 아래와 같다네.\r\n\r\n" + cm.getPlayer().getClient().getKeyValue("check_" + qnum + "_" + date);
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
                    talk += "#b#e[일일 퀘스트] #o" + questarray[i][0] + "# " + questarray[i][1] + "마리 퇴치#k#n " + isnew + "\r\n"
                } else {
                    talk += "#b#e[일일 퀘스트] #z" + questarray[i][0] + "# " + questarray[i][1] + "개 수집#k#n " + isnew + "\r\n"
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
                cm.sendOk("오늘의 일일 보상을 이미 받으셨습니다.");
                cm.dispose();
                return;
            }

            dialogue = "#b#e<일일퀘스트>#k#n\r\n\r\n"
            dialogue2 = "";
            dialogue3 = "";

            if (cm.getPlayer().getClient().getKeyValue("compensation_" + qnum + "_" + date) == 1) {
                cm.sendOk("오늘의 일일 보상을 이미 받으셨습니다.");
                cm.dispose();
                return;
            }
            for (i = 0; i < questlist.length; i++) {
                if (cm.getPlayer().getV("river_" + questlist[i][0] + "_mobq") > 0 && cm.getPlayer().getV("river_" + questlist[i][0] + "_isclear") < 2) {
                    선택지 = "#L" + i + "##d[일일 퀘스트] #o" + questlist[i][0] + "# " + questlist[i][1] + "마리 퇴치#k"
                    if (cm.getPlayer().getV("river_" + questlist[i][0] + "_count") >= Integer.parseInt(cm.getPlayer().getV("river_" + questlist[i][0] + "_mobq"))) {
                        count1++;
                        dialogue2 += 선택지 + " (완료 가능)\r\n"
                    } else {
                        count2++;
                        dialogue3 += 선택지 + " (진행 중)\r\n"
                    }
                } else if (cm.getPlayer().getV("river_" + questlist[i][0] + "_itemq") > 0 && cm.getPlayer().getV("river_" + questlist[i][0] + "_isclear") < 2) {
                    선택지 = "#L" + i + "##d[일일 퀘스트] #z" + questlist[i][0] + "# " + questlist[i][1] + "개 수집#k"
                    if (cm.itemQuantity(questlist[i][0]) >= cm.getPlayer().getV("river_" + questlist[i][0] + "_itemq")) {
                        count1++;
                        dialogue2 += 선택지 + " (완료 가능)\r\n"
                    } else {
                        count2++;
                        dialogue3 += 선택지 + " (진행 중)\r\n"
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
                cm.sendOk("인벤토리에 공간이 부족합니다.");
                cm.dispose();
                return;
            }
            if ((cm.getPlayer().getV("river_" + questlist[selection][0] + "_mobq") > 0 && cm.getPlayer().getV("river_" + questlist[selection][0] + "_count") >= cm.getPlayer().getV("river_" + questlist[selection][0] + "_mobq")) ||
                (cm.getPlayer().getV("river_" + questlist[selection][0] + "_itemq") > 0 && cm.itemQuantity(questlist[selection][0]) >= cm.getPlayer().getV("river_" + questlist[selection][0] + "_itemq"))) {

                text2 = "현재 퀘스트를 #b" + a + "#k개 완료하였습니다.\r\n"

                if (cm.getPlayer().getV("river_" + questlist[selection][0] + "_itemq") > 0) {
                    cm.gainItem(questlist[selection][0], -cm.getPlayer().getV("river_" + questlist[selection][0] + "_itemq"));
                }

                cm.getPlayer().getClient().setKeyValue("check_" + qnum + "_" + date, Integer.parseInt(cm.getPlayer().getClient().getKeyValue("check_" + qnum + "_" + date)) + 1);
                cm.getPlayer().addKV("river_" + questlist[selection][0] + "_isclear", 2);
                cm.getPlayer().addKV("arcane_quest_" + qnum + "_" + date, parseInt(cm.getPlayer().getV("arcane_quest_" + qnum + "_" + date)) + 1);

                if (a >= 3 && cm.getPlayer().getClient().getKeyValue("check_" + qnum + "_" + date) == 3 && cm.getPlayer().getClient().getKeyValue("compensation_" + qnum + "_" + date) != 1) {
                    cm.getPlayer().getClient().setKeyValue("compensation_" + qnum + "_" + date, "1");
                    text2 += "아래의 아이템이 지급됩니다.\r\n";
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
                cm.sendOk("아직 임무를 완수하지 못하셨습니다.");
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
//importPackage(java.util); 
//importPackage(java.lang); 
 
var status = -1;
 
var data;
var day;
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function String(date) {
    switch (date) {
        case 1:
            return "創世星期一";
        case 2:
            return "強化星期二";
        case 3:
            return "傾向星期三";
        case 4:
            return "榮耀星期四";
        case 5:
            return "黃金星期五";
        case 6:
            return "慶典星期六";
        case 0:
            return "成長星期日";
    }
}
 
var map = new Array(953030000, 953040000, 953050000, 953060000, 953070000, 953080000, 953090000);
var mapname = new Array("苔蘚樹森林(Lv.115~124)", "天空森林訓練場(Lv.120~129)", "海盜團秘密基地(Lv.125~134)", "異界戰場(Lv.135~144)", "偏僻森林危險區域(Lv.140~149)", "被禁止的時間(Lv.145~154)", "隱藏遺跡(Lv.150~159)");
var exp = new Array(5989675, 7311630, 8129820, 11524015, 11953470, 13378390, 15311670);
var select;
var needitem = 4310012;
var needcount = 30;
var check = 0;
 
function action(mode, type, selection) {
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
        if (!(cm.getPlayer().getLevel()  > 114 && cm.getPlayer().getLevel()  < 160) && !cm.getPlayer().isGM())  { //中級
            cm.sendOk(" 中級副本僅限#b等級115以上，160以下#k的玩家\r\n可以使用。", 9071004);
            cm.dispose(); 
            return;
        }
        getData();
        if (cm.getClient().getKeyValue("mPark")  == null) {
            cm.getClient().setKeyValue("mPark",  "0");
        }
        if (cm.getClient().getKeyValue("mPark_t")  == null) {
            cm.getClient().setKeyValue("mPark_t",  "0");
        }
        var totalclear = (parseInt(cm.getClient().getKeyValue("mPark"))  + parseInt(cm.getClient().getKeyValue("mPark_t"))); 
        if (totalclear > 7) {
            totalclear = 7;
        }
        if (totalclear >= 7) {
            var text = "#e<今日通關次數 #b" + totalclear + " / 7次#k (當前世界基準)>\r\n#e<我的 #z" + needitem + "# 數量 : " + cm.itemQuantity(needitem)  + ">#n\r\n\r\n";
            text += "今日可通關次數7次已全部使用完畢。\r\n";
            cm.sendOk(text,  9071004);
            cm.dispose(); 
            return;
        }
        if (cm.getClient().getKeyValue("mPark")  >= 2 && !cm.haveItem(needitem,  needcount)) {
            var text = "#e<今日通關次數 #b" + totalclear + " / 7次#k (當前世界基準)>\r\n#e<我的 #z" + needitem + "# 數量 : " + cm.itemQuantity(needitem)  + ">#n\r\n\r\n";
            text += "今日免費通關次數2次已全部使用完畢。\r\n";
            text += "額外通關需要 #b#z" + needitem + "# " + needcount + "個#k。\r\n";
            cm.sendOk(text,  9071004);
            cm.dispose(); 
        } else if (cm.getClient().getKeyValue("mPark")  >= 2 && cm.haveItem(needitem,  needcount)) {
            var text = "#e<今天是 #b" + String(Packages.tools.CurrentTime.getDay())  + "#k>\r\n<今日通關次數 #b" + totalclear + " / 7次#k (當前世界基準)>\r\n#e<我的 #z" + needitem + "# 數量 : " + cm.itemQuantity(needitem)  + ">\r\n\r\n";
            text += "通關副本時將消耗 #b#z" + needitem + "# " + needcount + "個#k。#b#n\r\n\r\n"
            for (i = 0; i < map.length;  i++) {
                text += "#L" + i + "#" + mapname[i] + "\r\n";
            }
            check = 1;
            cm.sendSimple(text,  9071004);
        } else {
            var text = "#e<今天是 #b" + String(Packages.tools.CurrentTime.getDay())  + "#k>\r\n<今日通關次數 #b" + totalclear + " / 7次#k (當前世界基準)>\r\n\r\n#e今日剩餘免費通關次數 #b" + (2 - cm.getClient().getKeyValue("mPark"))  + "次#k。#n#b\r\n";
            for (i = 0; i < map.length;  i++) {
                text += "#L" + i + "#" + mapname[i] + "\r\n";
            }
            cm.sendSimple(text,  9071004);
        }
    } else if (status == 1) {
        select = selection;
        cm.sendYesNo("#e< 今天是 #b" + String(Packages.tools.CurrentTime.getDay())  + "#k>\r\n\r\n選擇副本 : #b" + mapname[select] + "#k\r\n\r\n#k是否要進入副本？#n", 9071004);
    } else if (status == 2) {
        cm.dispose(); 
        if (check == 1) {
            cm.getPlayer().setMparkCharged(true); 
        }
        for (i = 0; i < 6; i++) {
            if (cm.getClient().getChannelServer().getMapFactory().getMap(map[select]  + (i * 100)).getCharactersSize() > 0) {
                cm.sendOk(" 已經有其他玩家進入。", 9071004);
                return;
            }
        }
        cm.getPlayer().setSkillCustomInfo(9110,  0, 600000);
        cm.warp(map[select],  0);
        cm.resetMap(map[select]); 
        cm.getPlayer().setMparkcount(0); 
        cm.getPlayer().setMparkkillcount(0); 
        cm.EnterMonsterPark(map[select]);
        cm.getPlayer().setMparkexp(exp[select]); 
        //cm.writeLog("Log/ 怪物公園入場.log", cm.getPlayer().getName()+" 進入怪物公園。\r\n", true);
    }
}
 
function getData() {
    time = new Date();
    year = time.getFullYear(); 
    month = time.getMonth()  + 1;
    if (month < 10) {
        month = "0" + month;
    }
    date = time.getDate()  < 10 ? "0" + time.getDate()  : time.getDate(); 
    data = year + "" + month + "" + date;
    day = time.getDay(); 
}
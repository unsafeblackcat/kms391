var st = 0;
var status = -1;
importPackage(Packages.server.quest);
bossname = [
    ["스우", 13, 33126, "Normal_Lotus", 33303, "lasttime", "Hard_Lotus"],
    ["데미안", 15, 34016, "Normal_Demian", 34017, "lasttime", "Hard_Demian"],
    ["루시드", 19, 34364, "Easy_Lucid", 3685, "lasttime", "Hard_Lucid"],
    ["윌", 23, 35100, "Normal_Will", 3658, "lasttime", "Hard_Will"],
    ["더스크 ( 익스트림 ↓)", 26, 35137, "Normal_Dusk", 3680, "lasttime", 26, 35137, "Hard_Dusk", 3680, "lasttime"],
    ["더스크 ( 익스트림 ↑)", 26, 35137, "Extreme_Dusk", 3680, "lasttime", 26, 35137, "Hell_Dusk", 3680, "lasttime"],
    ["진 힐라 ( 노멀, 익스트림 )", 24, 35260, "Normal_JinHillah", 36731, "lasttime", 24, 35260, "Extreme_JinHillah", 36731, "lasttime"],
    ["진 힐라 ( 헬 모드 )", 24, 35260, "Hell_JinHillah", 36731, "lasttime"],
    ["듄켈 ( 익스트림 ↓)", 27, 35138, "Normal_Dunkel", 3681, "lasttime", 27, 35138, "Hard_Dunkel", 3681, "lasttime"],
	["듄켈 ( 익스트림 ↑)", 27, 35138, "Extreme_Dunkel", 3681, "lasttime", 27, 35138, "Hell_Dunkel", 3681, "lasttime"],

    ["검은마법사 ( 노멀,익스트림 )", 25, 35377, "Black_Mage", 3679, "lasttime", 25, 35377, "Extreme_Black_Mage", 3679, "lasttime", 25, 35377, "Hell_Black_Mage", 3679, "lasttime"],
    ["검은마법사 ( 헬 모드 )", 25, 35377, "Black_Mage", 3679, "lasttime", 25, 35377, "Extreme_Black_Mage", 3679, "lasttime", 25, 35377, "Hell_Black_Mage", 3679, "lasttime"],
	["세렌 ( 노멀,익스트림 )", 28, 39932, "Hard_Seren", 3687, "lasttime", 28, 39932, "Extreme_Seren", 3687, "lasttime"],
	["세렌 ( 헬 모드 )", 28, 39932, "Hell_Seren", 3687, "lasttime"],
	["칼로스", 30, 40009, "Chaos_Karlos", 2207110, "lasttime"],
           ]
function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 = "안녕하세요. #h #님! #b클리어 횟수#k를 초기화 시키고 싶은 #r보스#k를 선택하여 주세요.\r\n\r\n";
        for (var a = 0; a < bossname.length; a++) {
			if (a == 15) {
				말 += "\r\n#e< 해외 보스 >#n\r\n";
			}
            말 += "#L"+a+"##b#fUI/UIWindow2.img/UserList/Main/Boss/BossList/"+bossname[a][1]+"/Icon/normal/0# "+bossname[a][0]+"#k\r\n";
        }
        cm.sendSimpleS(말, 0x04, 2430028);
		
    } else if (status == 1) {
        st = selection;
        cm.sendYesNoS("정말 #r#e#fs17#"+bossname[st][0]+"#n#k #fs15#클리어 횟수 초기화를 진행 하시겠어요?\r\n\r\n#fs15##r※보스 클리어를 안했을 시에도 티켓 사용 횟수는 적용됩니다.", 4, 2430028);

    } else if (status == 2) {
        if (!cm.checkDayItem(bossname[st][0], 100) && !cm.getPlayer().isGM()) {
            cm.sendOkS("이런, #h #님 이번주에 #b"+bossname[st][0]+"#k 클리어 횟수 초기화를 하셨네요. 초기화 티켓은 보스마다 주일에 한번씩만 사용 가능하답니다.\r\n또한 #r매주 목요일 자정#k에 #e초기화 티켓 횟수#n가 초기화 되니 알아두시는게 좋을거에요.", 4, 2430028);
            cm.dispose();
            return;
        }





        cm.gainItem(2430030, -1);
        cm.checkDayItem(bossname[st][0], 0);
        if (bossname[st][4] != null) {
            //주중 클리어/입장이 같이 있는경우 여기도 같이 처리한다.
            if (cm.getPlayer().getKeyValueStr(bossname[st][4], bossname[st][5]) != null) {
                cm.getPlayer().removeKeyValue(bossname[st][4]);
            }
        }
        cm.getPlayer().removeKeyValue(bossname[st][2]);
        if (bossname[st][2] == 35137) {
            cm.getPlayer().removeKeyValue(35139);
        } else if (bossname[st][2] == 35138) {
            cm.getPlayer().removeKeyValue(35140);
        }
        cm.getPlayer().removeV(bossname[st][3]);
        if (bossname[st][4] != null) {
            //난이도가 같이 초기화 될 경우 둘 다 초기화
            cm.getPlayer().removeV(bossname[st][6]);
            if (bossname[st][7] != null) {
                //난이도가 같이 초기화 될 경우 셋 다 초기화
                cm.getPlayer().removeV(bossname[st][7]);
			if (bossname[st][3] != null) {
                //난이도가 같이 초기화 될 경우 넷 다 초기화
                cm.getPlayer().removeV(bossname[st][8]);
            }
        }
	}
                            time = new Date();
                            year = time.getFullYear() % 100;
                            month2 = time.getMonth() + 1;
                            month = time.getMonth() + 1 < 10 ?  "0"+month2 : month2;
                            date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
                            date = year+"/"+month+"/"+date2;
                             cm.getPlayer().addKV_boss(""+bossname[st][3]+"_" +date, 0);
                             cm.getPlayer().addKV_boss(""+bossname[st][8]+"_" +date, 0);
  cm.getPlayer().dropMessage(-1, " 보스 초기화 : == " + (cm.getPlayer().getV_boss(""+bossname[st][3]+"_" +date)) + " || " + bossname[st][0] );


        cm.sendOkS("#r#e#fs17#"+bossname[st][0]+"#n#k#fs15# 클리어 횟수 초기화가 완료 되었습니다.", 4, 2430028);
        cm.dispose();
    }
}

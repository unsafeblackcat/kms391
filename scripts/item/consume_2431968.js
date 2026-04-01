var st = 0;
var status = -1;
importPackage(Packages.server.quest);
bossname = [
    ["상위보스", 13, 33126, "Normal_Boss", 33303, "lasttime", "Hard_Boss", 1000000001, 1000000101, 1000000006, 1000000106, 1000000002, 1000000102, 1000000004, 1000000104, 1000000003, 1000000103, 1000000005, 1000000105, 1000000017, 1000000117, 1000000016, 1000000116, 1000000015, 1000000115],
    //["데미안", 15, 34016, "Normal_Demian", 34017, "lasttime", "Hard_Demian", 1000000006, 1000000106],
    //["루시드", 19, 34364, "Easy_Lucid", 3685, "lasttime", "Normal_Lucid", "Hard_Lucid", 1000000002, 1000000102],
    //["윌", 23, 35100, "Normal_Will", 3658, "lasttime", "Hard_Will", 1000000004, 1000000104],
    //["더스크", 26, 35137, "Normal_Dusk", 3680, "lasttime", "Chaos_Dusk", 1000000003, 1000000103],
    //["진 힐라", 24, 35260, "Normal_JinHillah", 3673, "lasttime", 1000000005, 1000000105],
    //["듄켈", 27, 35138, "Normal_Dunkel", 3681, "lasttime", "Hard_Dunkel", 1000000017, 1000000117],
    //["검은 마법사", 28, 35377, "Black_Mage", 3679, "lasttime", 1000000016, 1000000116],
    //["선택받은 세렌", 28, 39932, "Hard_Seren", 3687, "lasttime", 1000000015, 1000000115],
           ];

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
            말 += "#L"+a+"##b#fUI/UIWindow2.img/UserList/Main/Boss/BossList/"+bossname[a][1]+"/Icon/normal/0# "+bossname[a][0]+"#k\r\n";
        }
        cm.sendSimpleS(말, 0x04, 9000030);
    } else if (status == 1) {
        st = selection;
        cm.sendYesNoS("정말 #r#e#fs17#"+bossname[st][0]+"#n#k #fs15#클리어 횟수 초기화를 진행 하시겠어요?\r\n\r\n#fs15##r※보스 클리어를 안했을 시에도 티켓 사용 횟수는 적용됩니다.", 4, 9000030);
    } else if (status == 2) {
        cm.gainItem(2431968, -1);
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
            }
        }
	if (bossname[st][0] == "상위보스") {
	    cm.getQuestRecord(1000000001).setCustomData("0");
	    cm.getQuestRecord(1000000101).setCustomData("0");
	    cm.getQuestRecord(1000000002).setCustomData("0");
	    cm.getQuestRecord(1000000102).setCustomData("0");
	    cm.getQuestRecord(1000000003).setCustomData("0");
	    cm.getQuestRecord(1000000103).setCustomData("0");
	    cm.getQuestRecord(1000000004).setCustomData("0");
	    cm.getQuestRecord(1000000104).setCustomData("0");
	    cm.getQuestRecord(1000000005).setCustomData("0");
	    cm.getQuestRecord(1000000105).setCustomData("0");
	    cm.getQuestRecord(1000000006).setCustomData("0");
	    cm.getQuestRecord(1000000106).setCustomData("0");
	    cm.getQuestRecord(1000000015).setCustomData("0");
	    cm.getQuestRecord(1000000115).setCustomData("0");
	    cm.getQuestRecord(1000000016).setCustomData("0");
	    cm.getQuestRecord(1000000116).setCustomData("0");
	    cm.getQuestRecord(1000000017).setCustomData("0");
	    cm.getQuestRecord(1000000117).setCustomData("0");
	}
        cm.sendOkS("#r#e#fs17#"+bossname[st][0]+"#n#k#fs15# 클리어 횟수 초기화가 완료 되었습니다.", 4, 9000030);
        cm.dispose();
    }
}

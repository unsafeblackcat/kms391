var st = 0;
var status = -1;
importPackage(Packages.server.quest);
bossname = [["모든 보스", 7, 15166, 3992, 3993, 3650, 3657, 30043, 30044, 30045, 30046, 7403, 3653, 31199, "All_Boss"]]
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
        if (!cm.checkDayItem(bossname[st][0], 0) && !cm.getPlayer().isGM()) {
            cm.sendOkS("이런, #h #님 이번주에 #b"+bossname[st][0]+"#k 클리어 횟수 초기화를 하셨네요. 초기화 티켓은 보스마다 주일에 한번씩만 사용 가능하답니다.\r\n또한 #r매주 목요일 자정#k에 #e초기화 티켓 횟수#n가 초기화 되니 알아두시는게 좋을거에요.", 4, 9000030);
            cm.dispose();
            return;
        }
        cm.gainItem(2431973, -1);
        if (bossname[st][4] != null) {
            //주중 클리어/입장이 같이 있는경우 여기도 같이 처리한다.
            if (cm.getPlayer().getKeyValueStr(bossname[st][4], bossname[st][5]) != null) {
                cm.getPlayer().removeKeyValue(bossname[st][4]);
            }
        }
        cm.checkDayItem(bossname[st][0], 1);
        cm.getPlayer().removeKeyValue(bossname[st][2]);
        cm.getPlayer().removeKeyValue(bossname[st][3]);
        cm.getPlayer().removeKeyValue(bossname[st][4]);
        cm.getPlayer().removeKeyValue(bossname[st][5]);
        cm.getPlayer().removeKeyValue(bossname[st][6]);
        cm.getPlayer().removeKeyValue(bossname[st][7]);
        cm.getPlayer().removeKeyValue(bossname[st][8]);
        cm.getPlayer().removeKeyValue(bossname[st][9]);
        cm.getPlayer().removeKeyValue(bossname[st][10]);
        cm.getPlayer().removeKeyValue(bossname[st][11]);
        cm.getPlayer().removeKeyValue(bossname[st][12]);
        cm.getPlayer().removeV(bossname[st][13]);
        cm.sendOkS("#r#e#fs17#"+bossname[st][0]+"#n#k#fs15# 클리어 횟수 초기화가 완료 되었습니다.", 4, 9000030);
        cm.dispose();
    }
}

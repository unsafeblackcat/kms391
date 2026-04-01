var st = 0;
var status = -1;
importPackage(Packages.server.quest);
bossname = [["카오스 자쿰", 1, 15166, "Chaos_Zakum"], ["하드 매그너스", 10, 3992, "Hard_Magnus"], ["하드 힐라", 3, 3650, "Hard_Hillah"], ["카오스 파풀라투스", 22, 3657, "Chaos_Populatus"], ["카오스 피에르", 4, 30043, "Chaos_Pierre"], ["카오스 반반", 5, 30044, "Chaos_VonBon"], ["카오스 블러디 퀸", 6, 30045, "Chaos_BloodyQueen"], ["카오스 벨룸", 7, 30046, "Chaos_Vellum"], ["카오스 핑크빈", 11, 7403, "Chaos_Pinkbean", 3653, "lasttime"], ["노멀 시그너스", 12, 31199, "Normal_Cygnus"]]
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
        cm.sendSimpleS(말, 0x04, 2430028);
    } else if (status == 1) {
        st = selection;
        cm.sendYesNoS("정말 #r#e#fs17#"+bossname[st][0]+"#n#k #fs15#클리어 횟수 초기화를 진행 하시겠어요?\r\n\r\n#fs15##r※보스 클리어를 안했을 시에도 티켓 사용 횟수는 적용됩니다.", 4, 2430028);
    } else if (status == 2) {
        if (!cm.checkDayItem(bossname[st][0], 1) /*&& !cm.getPlayer().isGM()*/) {
            cm.sendOkS("이런, #h #님 이번주에 #b"+bossname[st][0]+"#k 클리어 횟수 초기화를 하셨네요. 초기화 티켓은 보스마다 주일에 한번씩만 사용 가능하답니다.\r\n또한 #r매주 목요일 자정#k에 #e초기화 티켓 횟수#n가 초기화 되니 알아두시는게 좋을거에요.", 4, 2430028);
            cm.dispose();
            return;
        }
        cm.gainItem(2430029, -1);
        if (bossname[st][4] != null) {
            //주중 클리어/입장이 같이 있는경우 여기도 같이 처리한다.
            if (cm.getPlayer().getKeyValueStr(bossname[st][4], bossname[st][5]) != null) {
                cm.getPlayer().removeKeyValue(bossname[st][4]);
            }
        }
        cm.checkDayItem(bossname[st][0], 0);
        cm.getPlayer().removeV(bossname[st][3]);
        cm.getPlayer().removeKeyValue(bossname[st][2]);
        cm.sendOkS("#r#e#fs17#"+bossname[st][0]+"#n#k#fs15# 클리어 횟수 초기화가 완료 되었습니다.", 4, 2430028);
        cm.dispose();
    }
}

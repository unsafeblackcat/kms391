


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    캐논슈터 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062507

    엔피시 이름 : 돌의 정령

    엔피시가 있는 맵 : 블루밍 포레스트 : 돌의 정령을 도와달람 입장 (993192700)

    엔피시 설명 : 돌의 정령을 도와달람!


*/

var status = -1;
var sel = 0, point = 0;
var higher = false;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        txt = "";
        if (cm.getPlayer().getMapId() == 993192701) {
            higher = parseInt(cm.getClient().getCustomData(100795, "point")) > parseInt(+cm.getClient().getCustomData(100795, "weekspoint"));
            txt += "#b#e<돌의 정령을 도와달람!>#n#k\r\n"
            if (higher) {
                txt += "대단하담!\r\n\r\n"
            } else if (parseInt(cm.getClient().getCustomData(100795, "point")) <= 0) {
                txt += "내 친구들을 많이 구하지 못했구남?\r\n다음에는 더 많이 구해줄 수 있을 것이담.\r\n\r\n"
            } else {
                txt += "아쉽담. 다음에는 친구들을 더 많이 구해줄 수 있을 것이담.\r\n\r\n"
            }
            txt += "#e- 획득 점수: #n#b#e" + cm.getClient().getCustomData(100795, "point") + "점#n#k"
            if (higher) {
                txt += " #r#e(금주 최고 기록 갱신!)#n#k"
            }
            txt += "\r\n"
            txt += "#e- 이번주 월드 내 최고 점수: #n#r#e" + cm.getClient().getCustomData(100795, "weekspoint") + "점#n#k #e/ 1500점#n\r\n"
            txt += "#e- 현재 보유한 스피릿 포인트: #r" + cm.getClient().getCustomData(501368, "spoint") + "점#k#n\r\n"
            if (higher) {

            } else if (parseInt(cm.getClient().getCustomData(100795, "point")) <= 0) {
                txt += "#r  (최소 1점은 얻어야 스피릿 포인트를 받을 수 있담.)#k#n\r\n"
            } else {
                txt += "#r  (이번 주차의 월드 내 최고 점수를 갱신하지 못했습니다.)#k#n\r\n"
            }
            if (higher) {
                txt += "#L0# #b보상을 받는다.#k #e(스피릿 포인트 " + parseInt(cm.getClient().getCustomData(100795, "point")) + "점)#n\r\n"
            }
            txt += "#L1# #b다시 도전한다.#k#l\r\n"
            txt += "#L2# #b#m993192000#으로 돌아간다.#k#l";
        } else {
            txt += "#b#e<돌의 정령을 도와달람!>#n#k\r\n 전투 준비는 완료되었남?\r\n\r\n"
            txt += "#e이번주 월드 내 최고 점수: #n#r#e" + cm.getClient().getCustomData(100795, "weekspoint") + "점#n#k #e/ 1500점#n\r\n"
            txt += "#e이번주 받은 스피릿 포인트: #n#r#e" + cm.getClient().getCustomData(100795, "weekspoint") + "점#n#k #e/ 1500점#n\r\n"
            txt += "#e현재 보유한 스피릿 포인트: #n#r#e" + cm.getClient().getCustomData(501368, "spoint") + "점#n\r\n"
            txt += "#L1# #b돌의 정령을 도와달람!에 입장한다.#k#l\r\n"
            txt += "#L2# #b#m993192000#으로 돌아간다.#k#l";
        }
        cm.sendNextS(txt, 4, 9062507);
    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            point = parseInt(cm.getClient().getCustomData(100795, "point"));
            if (parseInt(cm.getClient().getCustomData(100795, "weekspoint")) > 0) {
                point = parseInt(cm.getClient().getCustomData(100795, "point") - parseInt(cm.getClient().getCustomData(100795, "weekspoint")));
            }
            txt = "#b#e<돌의 정령을 도와달람!>\r\n\r\n"
            txt += parseInt(cm.getClient().getCustomData(100795, "point")) + "#n#k점을 이번주 #e#r월드 내 최고 점수#n#k로 기록하고\r\n"
            txt += "#e스피릿 포인트 #b" + point+ "#k점#n을 받아가겠냠?\r\n\r\n"
            txt += "#e#r※안내※#n#k\r\n"
            txt += "해당 점수가 #r이번주 월드 내 최고 점수#k로 기록됩니다.\r\n"
            txt += "#r최고 점수를 갱신#k하면 #b최대 1500점#k까지 추가로 보상을 받을 수 있습니다."
            cm.sendYesNoS(txt, 4, 9062507);
        } else if (sel == 1) {
            //입장
            cm.EnterPunchKing();
            cm.dispose();
        } else if (sel == 2) {
            cm.sendYesNoS("#b#m993192000##k으로 돌아가겠냠?", 4, 9062507)
        }
    } else if (status == 2) {
        if (sel == 0) {
            cm.sendNextS("#b스피릿 포인트 " + point + "점#k을 획득했담.\r\n그럼 #b#m993192000##k으로 보내주겠담.", 4, 9062507);
        } else if (sel == 2) {
            cm.warp(993192000, 0);
            cm.dispose();
        }
    } else if (status == 3) {
        if (higher) {
            cm.getPlayer().dropMessage(5, "금주의 월드 내 최고 점수를 갱신하였습니다.(이전 최고 점수:" + parseInt(cm.getClient().getCustomData(100795, "weekspoint")) + "점/달성 점수:" + parseInt(cm.getClient().getCustomData(100795, "point")) + "점)")
        }
        cm.getClient().setCustomData(501368, "spoint", (parseInt(cm.getClient().getCustomData(501368, "spoint")) + point) + "");
        cm.getClient().setCustomData(100795, "weekspoint", (parseInt(cm.getClient().getCustomData(100795, "weekspoint")) + point) + "");
        cm.getClient().setCustomData(100795, "point", "0");
        cm.getPlayer().dropMessage(5, "스피릿 포인트 " + point + "점을 얻었습니다.(보유한 스피릿 포인트:" + cm.getClient().getCustomData(501368, "spoint") + "점)");
        cm.warp(993192000, 0);
        cm.dispose();
    }
}

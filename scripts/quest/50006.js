/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#"
파랑 = "#fc0xFF4641D9#"
남색 = "#fc0xFF4641D9#"
주황 = "#fc0xFFEDA900#"
importPackage(java.lang);
importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);
var questt = "tuto6"; // jump_고유번호
var 서버이름 = "몽[夢]";
function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 3) {
            qm.sendOkS("이런.. 언제라도 마음이 바뀌면 다시 찾아오게나.", 4, 9401232);
            qm.dispose();
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 = "이봐 #h #! #b<"+서버이름+" 포인트>#k에 알고있나?\r\n"
        qm.sendNextS(말, 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextPrevS("#b<"+서버이름+" 포인트>로는 여러가지 아이템을 구매할 수 있다던데..", 0x04, 9401232);
    } else if (status == 2) {
        qm.sendYesNoS("지금 이럴게 아니라 한번 자네가 직접 <"+서버이름+" 포인트>의 획득 방법을 알아보게나!", 0x04, 9401232);
    } else if (status == 3) {
        qm.sendOkS("[상점 시스템 -> 포인트 상점 - > #b"+서버이름+" 포인트 상점 관련#k]을 클릭해 설명을 듣고 오자.", 2);
        qm.forceStartQuest(true);
        qm.dispose();
    }
}

status = -1;
function end(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 1) {
            qm.sendOkS("이런.. 언제라도 마음이 바뀌면 다시 찾아오게나.", 4, 9401232);
            qm.dispose();
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (qm.getPlayer().getQuestStatus(50006) == 2) {
            qm.dispose();
            return;
        }
        qm.sendNextS("크크. <"+서버이름+" 포인트>에 대해서 잘 알아 보았는가?", 0x04, 9401232);
    } else if (status == 1) {
        말 = ""+서버이름+" 포인트로 다양한 아이템들을 구입해보게나!\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i2633610:# #b#t2633610:# 50개\r\n";
        말 += "#i4310012:# #b#t4310012:# 300개#k\r\n";
        qm.sendOkS(말, 0x04, 9401232);
    } else if (status == 2) {
        if (qm.getClient().getKeyValue(questt) == null)
            qm.getClient().setKeyValue(questt, "0");

        if (Integer.parseInt(qm.getClient().getKeyValue(questt)) > 0) {
            qm.sendOk("#fs15#" + 검정 + "이미 보상을 받으셨습니다.", 9010061);
            qm.dispose();
            qm.forceCompleteQuest(true);
            return;
        }
        qm.gainItem(2633610, 50);
        qm.gainItem(4310012, 300);
        qm.forceCompleteQuest(true);
        qm.dispose();
    }
}


function GlowQuest(start) {
    var str = qm.getClient().getKeyValue("GrowQuest");
    var ab = str.split("");
    var fi = "";
    ab[6] = start+"";
    for (var a = 0; a < ab.length; a++) {
        fi += ab[a];
    }
    qm.getClient().setKeyValue("GrowQuest", fi);
}

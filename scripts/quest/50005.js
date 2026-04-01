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
var questt = "tuto5"; // jump_고유번호
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
        말 = "이봐 #h #! #b<메이플 LIVE>#k 소식 들었나?\r\n"
        qm.sendNextS(말, 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextPrevS("내가 듣기론 #b<슈퍼스타>#k가 되고싶은 용사를 구하고 있다던데...", 0x04, 9401232);
    } else if (status == 2) {
        qm.sendNextPrevS("자네가 한번 스타가 되 보는게 어떤가? 크크.", 0x04, 9401232);
    } else if (status == 3) {
        qm.sendOkS("왼쪽 별을 통해서 [메이플 LIVE] 선행 퀘스트를 진행해보게!", 0x04, 9401232);
        qm.forceStartQuest(true);
        if (qm.getClient().getQuestStatus(100825) == 2) {
            qm.getClient().setCustomKeyValue(50005, "1", "1");
        }
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
            qm.sendOkS("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
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
        qm.sendNextS("스타가 된 기분은 어떤가? 나도 어릴적 스타가 되는게 꿈이였다네.. 크크크.\r\n\r\n그 외에도 #b<메이플 LIVE>#k에서 준비한 #r다양한 아이템#k이 있다고 하니 천천히 둘러보게나!", 0x04, 9401232);
    } else if (status == 1) {
        말 = "화려한..조명이..나를..감싼다네!! 크크.\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i2633610:# #b#t2633610:# 50개\r\n";
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
        qm.forceCompleteQuest(true);
        qm.dispose();
    }
}


function GlowQuest(start) {
    var str = qm.getClient().getKeyValue("GrowQuest");
    var ab = str.split("");
    var fi = "";
    ab[5] = start+"";
    for (var a = 0; a < ab.length; a++) {
        fi += ab[a];
    }
    qm.getClient().setKeyValue("GrowQuest", fi);
}

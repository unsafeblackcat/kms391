/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#";
연파 = "#fc0xFF4374D9#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
파랑 = "#fc0xFF4641D9#";
빨강 = "#fc0xFFF15F5F#";
하늘 = "#fc0xFF0099CC#";

importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);

var 서버이름 = "몽[夢]";

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        var choose = "#fs15#" + 검정 + "#b"+서버이름+"#k" + 검정 + "에서 느낀 즐거움을 다른 사람에게도 나눠주게!\r\n";
        choose += "#fc0xFFD5D5D5#───────────────────────────#k#l#b\r\n";
        choose += "#L12#추천인 시스템을 이용하고 싶어요.\r\n"
        choose += "#L10#추천인 상점을 이용하고 싶어요.\r\n"
        choose += "#L11#시스템에 대해서 궁금해요.\r\n\r\n"
        choose += "#r#L2##r대화 그만하기"
        qm.sendSimpleS(choose, 0x04, 9401232);
    } else if (status == 1) {
        if (selection == 10) {
            if (qm.getClient().getKeyValue("RecommendPoint") != null) {
                qm.getPlayer().setKeyValue(501215, "point", qm.getClient().getKeyValue("RecommendPoint"));
            }
            qm.openShop(39);
            qm.dispose();
        } else if (selection == 11) {
            말 = "#fs15#" + 보라 + "추천인 시스템" + 검정 + "에 대해 알려주겠네.\r\n\r\n"
            말 += "우선, #b편의시스템#k" + 검정 + "에 있는 #b추천인 시스템#k" + 검정 + "을 통해서 추천 등록이 가능하다네.\r\n"
            말 += "추천은 #r계정당 1회#k" + 검정 + "만 가능하고 추천을 등록하면 "+서버이름+"에서 지급해주는 #b특별한 아이템#k" + 검정 + "을 받을 수 있지. 크크\r\n"
            말 += "자네가 추천을 받게 된다면 #r추천인 포인트가 1#k" + 검정 + "씩 오른다네.\r\n"
            말 += "받은 포인트로 상점에서 #b다양한 아이템#k" + 검정 + "을 구매할 수 있으니 "+서버이름+" 서버를 널리 알려주게. 크크크."
            qm.sendOkS(말, 0x04, 9401232);
            qm.dispose();
        } else if (selection == 12) {
            qm.dispose();
            qm.openNpc(1540052);
        } else if (selection == 2) {
            qm.dispose();
        }
    }
}
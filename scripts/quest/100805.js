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
        var choose = "#fs15#" + 검정 + "#b아이테르#k" + 검정 + "에서 느낀 즐거움을 다른 사람에게도 나눠주게!\r\n";
        choose += "#fc0xFFD5D5D5#───────────────────────────#k#l#b\r\n";
        choose += "#L09#데일리 코인을 지급받고 싶어요.\r\n"
        choose += "#L10#데일리 상점을 이용하고 싶어요.\r\n"
        choose += "#L11#시스템에 대해서 궁금해요.\r\n\r\n"
        choose += "#r#L2##r대화 그만하기"
        qm.sendSimpleS(choose, 0x04, 2192030);
    } else if (status == 1) {
        if (selection == 9) {
            qm.openNpc(9001197);
            qm.dispose();
            }
        if (selection == 10) {
            if (qm.getClient().getKeyValue("RecommendPoint") != null) {
                qm.getPlayer().setKeyValue(501215, "point", qm.getClient().getKeyValue("RecommendPoint"));
            }
            qm.openNpc(1540894);
            qm.dispose();
        } else if (selection == 11) {
            말 = "#fs15#" + 보라 + "데일리 시스템" + 검정 + "에 대해 알려주겠네.\r\n\r\n"
            말 += "우선, #b데일리#k" + 검정 + "서버에 기여한 #b[후원] 데일리 기프트#k" + 검정 + "을 통해서 지급받을수 있다네\r\n"
            말 += "매일 지급받는 특별한 코인으로#k" + 검정 + " #b특별한 아이템#k" + 검정 + "을 받을 수 있지. 크크\r\n"
            말 += "데일리코인은  #r하루에  5개코인을 #k" + 검정 + "30일간 받을수있다네\r\n"
            말 += "#b다양한 아이템#k" + 검정 + "을 구매할 수 있으니 아이테르 인원에게 널리 알려주게. 크크크."
            qm.sendOkS(말, 0x04, 2192030);
            qm.dispose();
        } else if (selection == 2) {
            qm.dispose();
        }
    }
}
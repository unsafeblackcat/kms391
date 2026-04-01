/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#"
파랑 = "#fc0xFF4641D9#"
남색 = "#fc0xFF4641D9#"

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
        var choose = "#fs15#자네가 받고싶은 칭호를 하나 선택해보게나!\r\n";
        choose += "#fc0xFFD5D5D5#─────────────────────────────#k#l\r\n\r\n";
        choose += "#L0# #i3700900# #b#z3700900# 칭호 받기\r\n"
        //choose += "#L1# #i3700901# #b#z3700901# 칭호 받기\r\n"
        qm.sendOkS(choose, 0x04, 9401232);
    } else if (status == 1) {
        if (selection == 0) {
            if (qm.haveItem(3700900, 1)) {
                qm.sendOkS("#fs15#" + 검정 + "흠, 자네는 이미 칭호를 가지고 있는것 같네. 만약 잃어버린다면 다시 지급해주도록 하겠네!", 0x04, 9401232);
                qm.dispose();
                return;
            }
            var choose = "#fs15#" + 검정 + " 칭호를 지급 했다네. 더 좋은 칭호를 얻기 위해 #fc0xFF050099#"+서버이름+" #k" + 검정 + "에서 열심히 즐겨보게나!\r\n";
            choose += "#fc0xFFD5D5D5#─────────────────────────────#k#l\r\n\r\n";
            choose += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            choose += "#i3700900# #b#z3700900##k"
            qm.gainItem(3700900, 1);
            qm.sendOkS(choose, 0x04, 9401232);
            qm.dispose();
        } else if (selection == 1) {
            if (qm.haveItem(3700901, 1)) {
                qm.sendOkS("#fs15#" + 검정 + "흠, 자네는 이미 칭호를 가지고 있는것 같네. 만약 잃어버린다면 다시 지급해주도록 하겠네!", 0x04, 9401232);
                qm.dispose();
                return;
            }
            var choose = "#fs15#" + 검정 + " 칭호를 지급 했다네. 더 좋은 칭호를 얻기 위해 #fc0xFF050099#"+서버이름+"#k" + 검정 + "에서 열심히 즐겨보게나!\r\n";
            choose += "#fc0xFFD5D5D5#─────────────────────────────#k#l\r\n\r\n";
            choose += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            choose += "#i3700901# #b#z3700901##k"
            qm.gainItem(3700901, 1);
            qm.sendOkS(choose, 0x04, 9401232);
            qm.dispose();
        }
    }
}
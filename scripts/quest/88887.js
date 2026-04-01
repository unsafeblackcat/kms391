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
        말 = "#fs15##b"+서버이름+" 페스티벌#k에 온 #e#h ##n용사여, 어떤게 궁금한가?#k\r\n"
        말 += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
        말 += "#L0##fs15##b"+서버이름+" 배율에 대해서 궁금해요.\r\n"
        말 += "#L1#기본적인 규칙에 대해 알려주세요.\r\n\r\n"
        말 += "#L2##r대화 그만하기"
        qm.sendSimpleS(말, 0x04, 9401232);
    } else if (status == 1) {
        if (selection == 0) {
            말 = "#fs15##e 레벨　　   경험치　　메소　　드랍#n\r\n"
            말 += "" + 파랑 + " 1~300　　　　　　　　  x 3　　　x 1\r\n"
            말 += "" + 파랑 + "001~199　　 x 1500\r\n"
            말 += "" + 파랑 + "200~210　　 x 2000\r\n"
            말 += "" + 파랑 + "210~220　　 x 1900\r\n"
            말 += "" + 파랑 + "220~230　　 x 1800\r\n"
            말 += "" + 파랑 + "230~240　　 x 1700\r\n"
            말 += "" + 파랑 + "240~250　　 x 1600\r\n"
            말 += "" + 파랑 + "250~260　　 x 500\r\n"
            말 += "" + 파랑 + "260~279　　 x 400\r\n"
            말 += "" + 파랑 + "270~280　　 x 300\r\n"
            말 += "" + 파랑 + "280~290　　 x 100\r\n"
            말 += "" + 파랑 + "290~300　　 x 100\r\n"
            qm.sendOk(말, 9401232);
            qm.dispose();
        } else if (selection == 1) {
            말 = "#fs15#" + 검정 + "정말 규칙을 확인하겠나?\r\n\r\n"
            말 += "#L0##b확인하고 싶습니다.\r\n"
            말 += "#L1##r아니요."
            qm.sendSimpleS(말, 0x04, 9401232);
        } else if (selection == 2) {
            qm.dispose();
        }
    } else if (status == 2) {
        if (selection == 0) {
            qm.dispose();
            qm.openNpc(2006, "ServerNotice_0");
        } else if (selection == 1) {
            qm.dispose();
        }
    }
}
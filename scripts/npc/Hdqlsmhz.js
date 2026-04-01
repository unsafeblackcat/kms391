importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);

파랑 = "#fc0xFF0054FF#";
연파 = "#fc0xFF6B66FF#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
검정 = "#fc0xFF191919#";
화이트 = "#fc0xFFFFFFFF#";

틀 = "#fs15##fUI/Basic.img/actMark/23#";
A = "#k 消亡的旅途 #r";
B = "#k 啾啾島 #r";
C = "#k 拉克蘭 #r";
D = "#k 阿爾卡納 #r";
E = "#k 莫拉斯 #r";
F = "#k 埃斯佩拉 #r";

var status = -1;

function start() {
    action(1, 0, 0);
}

/*
            cm.dispose();
            InterServerHandler.EnterCS(cm.getPlayer().getClient(),cm.getPlayer(), false); 캐시샵
*/

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var msg = "                    " + 틀 + "#l\r\n";
                msg += "#fc0xFFD5D5D5#──────────────────────────#l\r\n";
        msg += "#L0##fc0xFFFF3636#" + A + "#l\r\n";
        msg += "#L1##fc0xFFFF3636#" + B + "#l\r\n";
        msg += "#L2##fc0xFFFF3636#" + C + "#l\r\n";
        msg += "#L3##fc0xFFFF3636#" + D + "#l\r\n";
        msg += "#L4##fc0xFFFF3636#" + E + "#l\r\n";
        msg += "#L5##fc0xFFFF3636#" + F + "#l\r\n";
        cm.sendOk(msg);

    } else if (status == 1) {
        if (selection == 0) { // 사냥이벤트 아이템교환
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9063144, "Maxxmdlt");//Maxxwlt
        } else if (selection == 1) { // 사냥이벤트 아이템뽑기
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9063144, "Maxjjd");//
        } else if (selection == 2) { //핫타임보상
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9063144, "Maxlkl");//
        } else if (selection == 3) { // 일퀘
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9063144, "Maxaekn");//
        } else if (selection == 4) { // 후원룰렛
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9063144, "Maxmls");//
        } else if (selection == 5) { // 어빌리티
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9063144, "Maxaspl");//
        } else if (selection == 6) { //V 코어 매트릭스
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9063144, "Maxxwlt");//
        } else if (selection == 7) { //자유 전직
            cm.dispose();
            cm.sendUI(164);
        } else if (selection == 8) { //판타즈마
            cm.dispose();
            cm.openNpc(3003105);
        } else if (selection == 9) { //아라크노
            cm.dispose();
            cm.openNpc(9062178);
        } else if (selection == 10) { //자유전직
            cm.dispose();
            cm.openNpc(1540942);
        } else if (selection == 11) { //검은마법사 재료 교환
            cm.dispose();
            cm.openNpc(9050001);
        } else if (selection == 12) {
            cm.dispose();
            cm.openNpc(9900001);
        } else if (selection == 13) {
            cm.dispose();
           cm.openNpcCustom(cm.getClient(), 9010044, "닉네임변경");
        } else if (selection == 14) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9062294, "원클큐브");
        } else if (selection == 15) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9062294, "원클환불");
        } else if (selection == 16) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9062294, "CashStroage");
        } else if (selection == 17) {
            cm.dispose();
            cm.openNpc(9062543);
        } else if (selection == 20) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 1052206, "LevelReward");
//       cm.openNpcCustom(cm.getClient(), 1052206, "LevelReward");
         } else if (selection == 22) {
            cm.dispose();
         cm.openNpcCustom(cm.getClient(), 1052206, "SeasonPass");
        }  else if (selection == 18) {
        cm.dispose();
        cm.openNpcCustom(cm.getClient(), 1052206, "Stealskill");
         } else if (selection == 6974) {
                    cm.dispose();
                    cm.warp(101084400, 0);
         }
    } else if (status == 2) {
        if (selection == 0) {
            cm.dispose();
            cm.openNpc(2074149);
        } else if (selection == 1) {
            cm.dispose();
            cm.openNpc(2074150);
        }
    }
}
/*
제작자 : qudtlstorl79@nate.com
*/

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
틀 = "#fUI/Basic.img/actMark/30#";
버리기 = "#fUI/CashShop.img/CashItem_label/1#";
길드 = "#fUI/CashShop.img/CashItem_label/2#";
펫 = "#fUI/CashShop.img/CashItem_label/3#";
위장색 = "#fUI/CashShop.img/CashItem_label/4#";
창고 = "#fUI/CashShop.img/CashItem_label/5";
스킬학습 = "#fUI/GuildMark.img/Mark/Animal/00002000";
원클릭큐브 = "#fUI/CashShop.img/CashItem_label/7";
원클릭환불 = "#fUI/CashShop.img/CashItem_label/8";
원클릭강불 = "#fUI/CashShop.img/CashItem_label/2";
뽑기 = "#fUI/CashShop.img/CashItem_label/2";
택배 = "#fUI/CashShop.img/CashItem_label/2";
스틸스킬 = "#fUI/CashShop.img/CashItem_label/2";
편의 = "#fUI/CashShop.img/CashItem_label/2";
추천인 = "#fUI/CashShop.img/CashItem_label/2";
상점 = "#fUI/CashShop.img/CashItem_label/2";
검은마법사 = "#fUI/CashShop.img/CashItem_label/2";
위장색 = "#fUI/CashShop.img/CashItem_label/2";
정령 = "#fUI/CashShop.img/CashItem_label/2";
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
        var choose = "                    " + 틀 + "#l\r\n";
        choose += "#fs20##fc0xFFD5D5D5#──────────────────#k\r\n";
        choose += "#L7##fc0xFFFF3636#抽奬1#l";
        choose += "#L8##fc0xFFFF3636#抽奬2#l";
        choose += "#L9##fc0xFFFF3636#抽奬3#l\r\n\r\n";

        cm.sendOkS(choose, 0x86);

    } else if (status == 1) {
        if (selection == 1) { // 아이템 버리기
            cm.dispose();
            cm.openNpc(1012121);
        } else if (selection == 0) { // 각종 랭킹
            cm.dispose();
            cm.openNpc(1530330);
        } else if (selection == 5) { // 창고
            cm.dispose();
            cm.openNpc(9031016);
        } else if (selection == 3) { // 길드
            cm.dispose();
            cm.warp(200000301);
        } else if (selection == 4) { // 위장색
            cm.dispose();
         cm.openNpc(1105008);
        } else if (selection == 6) { // 펫 교환
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 1052224, "specialchair");
        } else if (selection == 9) { // 강환불
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 1052224, "Choujqt");
        } else if (selection == 100) { //제네무기제작
            cm.dispose();
            cm.openNpc(3004100);
        } else if (selection == 7) { //V 코어 매트릭스
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 1052224, "ChouJiang2");
        } else if (selection == 8) { //자유 전직
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9063138, "ChouJiang1");
        } else if (selection == 22) { //판타즈마
            cm.dispose();
            cm.openNpc(3003105);
        } else if (selection == 23) { //제작시스템
            cm.dispose();
            cm.openNpc(2200004);
        } else if (selection == 10) { //자유전직
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 1052206, "Stealskill");
        } else if (selection == 11) { //검은마법사 재료 교환
            cm.dispose();
            cm.openNpc(1052224);
        } else if (selection == 12) {
            cm.dispose();
            cm.openNpc(9010009);
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
         } else if (selection == 22) {
            cm.dispose();
         cm.openNpcCustom(cm.getClient(), 1052206, "ServerBackRE");
        }  else if (selection == 18) {
        cm.dispose();
        cm.openNpcCustom(cm.getClient(), 1052206, "Stealskill");
         } else if (selection == 900) {
        cm.dispose();
        cm.openNpc(3006188);
        } else if (selection == 101) {
            cm.dispose();
            cm.openNpc(3006189);
        } else if (selection == 102) {
            cm.dispose();
            cm.openNpc(9001228);
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
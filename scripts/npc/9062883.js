importPackage(Packages.handling.channel);
importPackage(java.text);
importPackage(Packages.handling.cashshop);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.handling.cashshop.handler);
importPackage(java.text);
importPackage(java.lang);
importPackage(Packages.tools.packet);

var status = -1;
var nf = NumberFormat.getInstance();


function start() {
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        choose = "#e#b#h ##n#k :: 각종 스킬들이다. 둘러볼까?#l\r\n";
        choose += "#L30##e[직업 : 전직업] #b비연 학습#k\r\n\r\n#l";
        choose += "#L31##e[직업 : 마법사] #b더블 점프 학습#k\r\n\r\n\r\n#l";
        choose += "#L32##e[직업 : 미하일] #b시그너스 나이츠#k\r\n\r\n\r\n#l";
        choose += "#r - 마법사 직업군이 아닌 타 직업군이 배울시 스킬 롤백이 불가능합니다#l";
        if (cm.getPlayer().hasGmLevel(11)){
            choose += "\r\n#l#k\r\n\r\n#b#e관리자 시스템#n #r(운영자만 보이는 메뉴)#k\r\n";
            choose += "#e#b#L300#후원제작#k";
            choose += "#e#g#L301#복구제작#k";
            choose += "#e#r#L302#운영자맵#k";
            choose += "#e#b#L303#유저정보#k\r\n";
            choose += "#e#b#L304#닉변하기#k";
            choose += "#e#g#L305#총메세지#k";
            choose += "#e#r#L306#비번번경#K";
        }
        cm.sendSimpleS(choose, 2);

    } else if (status == 1) {
        var s = selection;
        cm.dispose();
        if (s == 33) {
	cm.openShop(1);
        } else if (s == 2) {
               //InterServerHandler.EnterCS(cm.getClient(), cm.getPlayer(), false);
        } else if (s == 30) {
        cm.teachSkill(80003745, 30, 30);
cm.getPlayer().dropMessage(-8, "[팬시] 초기 지원 상자가 지급 되었습니다. 인벤토리를 확인해 주세요 !");
        } else if (s == 31) {
        cm.teachSkill(80001864, 30, 30);
        } else if (s == 32) {
        cm.teachSkill(51121005, 30, 30);
        } else if (s == 33) {
	cm.openShop(1540894);
        } else if (s == 34) {
	cm.openShop(4);
        } else if (s == 35) {
cm.openShop(15);
        } else if (s == 40) {
 	cm.openNpc(1530330);
        } else if (s == 41) {
cm.openShop(9001212);
        } else if (s == 42) {
cm.openShop(1540105);
        } else if (s == 8) {
 	cm.openNpc(1530707);
        } else if (s == 9) {
 	cm.openNpc(1540873);
        } else if (s == 10) {
 	cm.openNpc(1530110);
        } else if (s == 11) {
 	cm.openNpc(2040047);
        } else if (s == 12) {
 	cm.openNpc(2040048);
        } else if (s == 13) {
 	cm.openNpc(1540105);
        } else if (s == 14) {
 	cm.openNpc(2040045);
        } else if (s == 15) {
 	cm.openNpc(9001008);
        } else if (s == 16) {
 	cm.openNpc(9001009);
        } else if (s == 17) {
 	cm.openNpc(2040040);
        } else if (s == 20) {
 	cm.openNpc(1530706);
        } else if (s == 21) {
 	cm.openNpc(1540321);
        } else if (s == 22) {
 	cm.openNpc(1540205);
        } else if (s == 23) {
 	cm.openNpc(3003541);
        }
    }
}

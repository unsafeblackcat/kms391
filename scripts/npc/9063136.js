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

틀 = "#fUI/Basic.img/actMark/23#";
다이어리 = "#fUI/Basic.img/actMark/24#";
시즌패스 = "#fUI/Basic.img/actMark/25#";
일일임무 = "#fUI/Basic.img/actMark/26#";
레벨업 = "#fUI/Basic.img/actMark/27#";
랭크 = "#fUI/Basic.img/actMark/28#";
환생 = "#fUI/Basic.img/actMark/29#";

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
		msg += "#L1##fc0xFFFF3636#" + 다이어리 + "#l";
		msg += "#L2##fc0xFFFF3636#" + 시즌패스 + "#l";
                msg += "#L3##fc0xFFFF3636#" + 일일임무 + "#l\r\n";
		msg += "#L30##fc0xFFFF3636#" + 레벨업 + "#l";
		msg += "#L5##fc0xFFFF3636#" + 랭크 + "#l";
		msg += "#L6##fc0xFFFF3636#" + 환생 + "#l\r\n";
        cm.sendOk(msg);

    } else if (status == 1) {
        if (selection == 100) { // 사냥이벤트 아이템교환
            cm.dispose();
            cm.openNpc(9062710);
        } else if (selection == 1) { // 사냥이벤트 아이템뽑기
            cm.dispose();
            cm.openNpc(9000368);
        } else if (selection == 2) { //핫타임보상
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9063144, "9063144");
        } else if (selection == 30) {
            if(cm.getPlayer().getAllUnion() < 6000) {
                cm.sendOk("#fs11#유니온 레벨이 총합 [ #b6000#k ] 이상 인 [ #b캐릭터#k ] 에게 1회 지급되는 #r보상#k입니다. 충족 후 찾아와주세요.");
                cm.dispose();
                return;
            } else if(cm.getClient().getKeyValue("UnionReward6000") > 0) {
		cm.sendOk("#fs11#이미 지급받은 보상으로 지급이 불가능합니다.");
		cm.dispose();
	    } else if (cm.getClient().getKeyValue("UnionReward6000") == null) {
		cm.getClient().setKeyValue("UnionReward6000", "0");
		cm.dispose();
	    } else if (cm.getPlayer().getAllUnion() > 6000) {
		cm.getClient().setKeyValue("UnionReward6000", "1");
		cm.sendOk("#fs11#유니온 #r6000레벨#k 달성을 축하드립니다!\r\n\r\n#b#i4319999# 1000개와 10억 메소를 지급 해드렸어요.");
		cm.gainItem(4319999, 1000);
		cm.gainMeso(1000000000);
		cm.dispose();
	    }
        } else if (selection == 3) { // 일퀘
            cm.dispose();
            cm.openNpc(9062286);
        } else if (selection == 50) { // 후원룰렛
            cm.dispose();
            cm.openNpc(9010049);    
        } else if (selection == 4) { // 어빌리티
            if (cm.getPlayer().getInnerSkills().size() > 2) {
                cm.sendOk("#fs11#" + 검정 + "이미 어빌리티가 개방되어 있다네!#k", 9401232);
                cm.dispose();
                return;
            }
            cm.forceCompleteQuest(12394);
            cm.forceCompleteQuest(12395);
            cm.forceCompleteQuest(12396);
            cm.setInnerStats(1);
            cm.setInnerStats(2);
            cm.setInnerStats(3);
            cm.fakeRelog();
            cm.updateChar();
            cm.sendOk("#fs11#" + 검정 + "자네를 위해 어빌리티를 개방해주었다네!", 9401232);
            cm.dispose();
        } else if (selection == 5) { //V 코어 매트릭스
            cm.dispose();
            cm.openNpc(2530004);
        } else if (selection == 3123) { //자유 전직
            cm.dispose();
            cm.sendUI(164);
        } else if (selection == 22) { //판타즈마
            cm.dispose();
            cm.openNpc(3003105);
        } else if (selection == 6) { //아라크노
            cm.dispose();
            cm.openNpc(9062178);
        } else if (selection == 10) { //자유전직
            cm.dispose();
            cm.openNpc(1540942);
        } else if (selection == 11) { //검은마법사 재료 교환
            cm.dispose();
            cm.openNpc(3003811);
        } else if (selection == 12) {
            cm.dispose();
            cm.openNpc(9062518);
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
//		 cm.openNpcCustom(cm.getClient(), 1052206, "LevelReward");
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
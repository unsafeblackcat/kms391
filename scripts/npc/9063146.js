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
聯盟商店 = "#k 聯盟商店 #r";
每日任務 = "#k 每日任務 #r";
聯盟NPC = "#k潛 #r";
陞級 = "#k 聯盟等級 #r";
成就 = "#k 成就獎勵 #r";
奬勵 = "#k 聯盟奬勵 #r";

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
        msg += "#L2##fc0xFFFF3636#" + 聯盟NPC + "#l";        
        cm.sendOk(msg);

    } else if (status == 1) {
        if (selection == 100) { // 사냥이벤트 아이템교환
            cm.dispose();
            cm.openNpc(3004100);
        } else if (selection == 1) { // 사냥이벤트 아이템뽑기
            cm.dispose();
            cm.openShop(9010107);
        } else if (selection == 2) { //핫타임보상
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9063146, "1001100");
        } else if (selection == 30) {
            if(cm.getPlayer().getAllUnion() < 6000) {
                cm.sendOk("#fs15#聯盟等級總和 [ #b6000#k ] 以上 [ #b角色#k ] 發放一次的 #r獎勵#k請滿足後來找我。");
                cm.dispose();
                return;
            } else if(cm.getClient().getKeyValue("UnionReward6000") > 0) {
		cm.sendOk("#fs15#已获得的奖励无法支付.");
		cm.dispose();
	    } else if (cm.getClient().getKeyValue("UnionReward6000") == null) {
		cm.getClient().setKeyValue("UnionReward6000", "0");
		cm.dispose();
	    } else if (cm.getPlayer().getAllUnion() > 6000) {
		cm.getClient().setKeyValue("UnionReward6000", "1");
		cm.sendOk("#fs15#祝賀您達到 #rr6000等級#k 支付了!\r\n\r\n#b#i4319999# 1000個和10億金幣.");
		cm.gainItem(4319999, 1000);
		cm.gainMeso(1000000000);
		cm.dispose();
	    }
        } else if (selection == 3) { // 일퀘
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9063135, "LianMengJiangLi");
        } else if (selection == 50) { // 후원룰렛
            cm.dispose();
            cm.openNpc(9010049);    
        } else if (selection == 4) { // 어빌리티
            if (cm.getPlayer().getInnerSkills().size() > 2) {
                cm.openNpcCustom(cm.getClient(), 9010106, "UnionRE");
                cm.dispose();
            }
            cm.forceCompleteQuest(12394);
            cm.forceCompleteQuest(12395);
            cm.forceCompleteQuest(12396);
            cm.setInnerStats(1);
            cm.setInnerStats(2);
            cm.setInnerStats(3);
            cm.fakeRelog();
            cm.updateChar();
            cm.sendOk("#fs15#" + 검정 + "我為你開放了!", 9401232);
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
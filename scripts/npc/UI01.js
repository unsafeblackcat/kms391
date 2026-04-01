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

펫 = "#fUI/CashShop.img/CashItem_label/5#";
펫1 = "#fUI/CashShop.img/CashItem_label/8#";
길드 = "#fUI/Basic.img/theblackcoin/14#";
젬스톤 = "#fUI/Basic.img/theblackcoin/19#";
이름변경 = "#fUI/Basic.img/theblackcoin/20#";
자유전직 = "#fUI/Basic.img/theblackcoin/21#";
창고 = "#fUI/Basic.img/theblackcoin/13#";
어빌리티 = "#fUI/Basic.img/theblackcoin/22#";
포인트 = "#fUI/Basic.img/theblackcoin/7#";
포켓 = "#fUI/Basic.img/theblackcoin/23#";
랭킹 = "#fUI/Basic.img/theblackcoin/24#";
템버리기 = "#fUI/Basic.img/theblackcoin/25#";
편의 = "#fUI/Basic.img/theblack/2#";
추천인 = "#fUI/Basic.img/theblackcoin/41#";
상점 = "#fUI/Basic.img/theblack/1#";
검은마법사 = "#fUI/Basic.img/theblackcoin/42#";
위장색 = "#fUI/Basic.img/theblackcoin/43#";
정령 = "#fUI/UIWindow4.img/pointShop/100712/iconShop#";
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
		var choose = "#fc0xFFFF3636#" + 젬스톤 + "             [프레이 이벤트모음]#l\r\n";
        choose += "#L10##fc0xFFFF3636#" + 템버리기 + " [사냥] 이벤트 아이템 뽑기 이용하기#l\r\n";
        choose += "#L100##fc0xFFFF3636#" + 템버리기 + " [사냥] 이벤트 아이템 교환하기#l\r\n\r\n";
		choose += "#L6974##fc0xFFFF3636#" + 템버리기 + " [간담회 맵 이동]#l\r\n\r\n";
	//if(cm.getPlayer().isGM()) {
        //choose += "#L20##fc0xFFDF4D4D#" + 랭킹 + " [핫타임] 핫타임 보상받기(계정당 1회)#l\r\n\r\n";
	//}
        choose += "#L30##fc0xFFDF4D4D#" + 랭킹 + " [유니온] 달성 보상받기(계정당 1회)#l\r\n";
	choose += "    ";
        cm.sendOkS(choose, 0x4);

    } else if (status == 1) {
        if (selection == 100) { // 사냥이벤트 아이템교환
            cm.dispose();
            cm.openNpc(9062710);
        } else if (selection == 10) { // 사냥이벤트 아이템뽑기
            cm.dispose();
            cm.openNpc(9062670);
        } else if (selection == 20) { //핫타임보상
            cm.dispose();
            cm.openNpc(9062628, "HotTime");
        } else if (selection == 30) {
            if(cm.getPlayer().getAllUnion() < 8000) {
                cm.sendOk("유니온 레벨이 모자란거 같아요!");
                cm.dispose();
                return;
            } else if(cm.getClient().getKeyValue("UnionReward8000") > 0) {
		cm.sendOk("#fs15#받았잖아");
		cm.dispose();
	    } else if (cm.getClient().getKeyValue("UnionReward8000") == null) {
		cm.getClient().setKeyValue("UnionReward8000", "0");
		cm.dispose();
	    } else if (cm.getPlayer().getAllUnion() > 8000) {
		cm.getClient().setKeyValue("UnionReward8000", "1");
		cm.sendOk("#fs15#유니온 #r8000레벨#k 달성을 축하드립니다!\r\n\r\n#b#i2439614##zi2439614##k과 50억 메소를 지급 해드렸어요.");
		cm.gainItem(2439614, 1);
		cm.gainMeso(5000000000);
		cm.dispose();
	    }
        } else if (selection == 40) { // 검키우기
            cm.dispose();
            cm.openNpc(3005575);
        } else if (selection == 50) { // 후원룰렛
            cm.dispose();
            cm.openNpc(9010049);    
        } else if (selection == 4) { // 어빌리티
            if (cm.getPlayer().getInnerSkills().size() > 2) {
                cm.sendOk("#fs15#" + 검정 + "이미 어빌리티가 개방되어 있다네!#k", 9401232);
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
            cm.sendOk("#fs15#" + 검정 + "자네를 위해 어빌리티를 개방해주었다네!", 9401232);
            cm.dispose();
        } else if (selection == 6) { // 펫 교환
            말 = "#fs15#" + 검정 + "교환하고 싶은 라벨의 펫을 선택해보게.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#fs15##L0#" + 펫 + "#fc0xFF6B66FF# 라벨 펫#k" + 검정 + "을 교환하고 싶습니다.#l\r\n"
            말 += "#fs15##L1#" + 펫1 + "#fc0xFFA566FF# 라벨 펫#k" + 검정 + "을 교환하고 싶습니다.#l\r\n"
            cm.sendSimpleS(말, 0x04, 9401232)
        } else if (selection == 7) { //V 코어 매트릭스
            cm.dispose();
            cm.openNpc(1540945);
        } else if (selection == 8) { //자유 전직
            cm.dispose();
            cm.sendUI(164);
        } else if (selection == 22) { //판타즈마
            cm.dispose();
            cm.openNpc(3003105);
        } else if (selection == 23) { //아라크노
            cm.dispose();
            cm.openNpc(3003536);
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
		 cm.openNpcCustom(cm.getClient(), 1052206, "ServerBackRE");
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
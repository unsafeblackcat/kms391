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
틀 = "#fUI/Basic.img/actMark/0#";


icon = [
	["#fUI/UIWindow4.img/pointShop/100659/iconShop#", 9062352],
	["#fUI/UIWindow4.img/pointShop/100658/iconShop#", 9062356],
	["#fUI/UIWindow4.img/pointShop/100657/iconShop#", 9062353],
	["#fItem/Etc/0431.img/04310156/info/iconShop#", 2155009],
	["#fItem/Etc/0431.img/04310199/info/iconShop#", 1540893],
	["#fItem/Etc/0431.img/04310218/info/iconShop#", 3003105],
	["#fItem/Etc/0431.img/04310249/info/iconShop#", 3003105],
	["#fUI/UIWindow4.img/pointShop/100660/iconShop#", 9062356],
	["#fUI/UIWindow4.img/pointShop/100661/iconShop#", 9062353],
];

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
	choose += "#fc0xFFD5D5D5#──────────────────────────#k\r\n";
        choose += "#L12##fs11##b"+icon[1][0]+"#r[HOT]#k 매일 매일 엄청난 #r핫타임#k 보상!!#l\r\n";
        choose += "#L11##fs11##b"+icon[0][0]+"[NEW]#k 켄타의 종이조각 구해오기 #d( 반복 )#k#l\r\n";
        choose += "#L10##fs11##b"+icon[3][0]+"[NEW]#k 궁극의 비보 뽑기 이용하기#l\r\n";
        choose += "#L100##fc0xFFFF3636##b"+icon[2][0]+"[NEW]#k 여섯가지 비보 #d( 궁극의 비보 제작 )#k#l\r\n\r\n";
	choose += "#fc0xFFD5D5D5#──────────────────────────#k\r\n";
        choose += "#L15##fs11##d"+icon[7][0]+"[Fishing] 설귀도의 특별 낚시터 이동하기#l\r\n";
        choose += "#L16##fs11##d"+icon[7][0]+"[Fishing] 다양한 물고기 도감을 채워보자!#l\r\n\r\n";
        choose += "#L30##fc0xFFDF4D4D#[유니온] 8000 달성 보상받기 (계정당 1회)#l\r\n";
        choose += "#L20##fc0xFFDF4D4D#[뉴비지원] 레벨 달성 보상받기 (계정당 1회)#l\r\n";
        choose += "#L21##fc0xFFDF4D4D#[추천인] 추천인 조회 & 보상 받기#l\r\n";
	choose += "    ";
        cm.sendOk(choose);

    } else if (status == 1) {
        if (selection == 100) { // 사냥이벤트 아이템교환
            cm.dispose();
            cm.openNpc(9062710);
        } else if (selection == 15) { // 낚시
            cm.dispose();
            cm.warp(993000750);
        } else if (selection == 16) { // 낚시 다이어리
                cm.sendOk("#fs11# #b 낚시도감이 곧 활성화될 예정입니다.");
                cm.dispose();
                return;
        } else if (selection == 10) { // 사냥이벤트 아이템뽑기
            cm.dispose();
            cm.openNpc(9062670);
        } else if (selection == 11) { // 종이조각
            cm.dispose();
            cm.openNpc(9060000);
        } else if (selection == 12) { // 핫타임
            cm.dispose();
            cm.openNpc(9063146);
        } else if (selection == 30) {
            if(cm.getPlayer().getAllUnion() < 10000) {
                cm.sendOk("#fs11#유니온 레벨이 총합 [ #b10000#k ] 이상 인 [ #b캐릭터#k ] 에게 1회 한정지급되는 #r보상#k입니다. 충족 후 찾아와주세요.");
                cm.dispose();
                return;
            } else if(cm.getClient().getKeyValue("UnionReward10000") > 0) {
		cm.sendOk("#fs11#이미 지급 받은 이벤트보상입니다.");
		cm.dispose();
	    } else if (cm.getClient().getKeyValue("UnionReward10000") == null) {
		cm.getClient().setKeyValue("UnionReward10000", "0");
		cm.dispose();
	    } else if (cm.getPlayer().getAllUnion() > 10000) {
		cm.getClient().setKeyValue("UnionReward10000", "1");
		cm.sendOk("#fs11#유니온 #r10000레벨#k 달성을 축하드립니다!\r\n\r\n#b#i5060048# #r10개#k와 50억 메소를 지급 해드렸어요.");
		cm.gainItem(5060048, 10);
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
        } else if (selection == 6) { // 펫 교환
            말 = "#fs11#" + 검정 + "교환하고 싶은 라벨의 펫을 선택해보게.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#fs11##L0#" + 펫 + "#fc0xFF6B66FF# 라벨 펫#k" + 검정 + "을 교환하고 싶습니다.#l\r\n"
            말 += "#fs11##L1#" + 펫1 + "#fc0xFFA566FF# 라벨 펫#k" + 검정 + "을 교환하고 싶습니다.#l\r\n"
            cm.sendSimpleS(말, 0x04, 9401232)
        } else if (selection == 7) { //V 코어 매트릭스
            cm.dispose();
            cm.openNpc(1540945);
        } else if (selection == 8) { //자유 전직
            cm.dispose();
            cm.sendUI(164);
        } else if (selection == 21) { //추천인
            cm.dispose();
            cm.openNpc(3001931);
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
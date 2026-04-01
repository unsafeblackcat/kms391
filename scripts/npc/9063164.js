importPackage(Packages.handling.channel.handler);


파랑 = "#fc0xFF0054FF#";
연파 = "#fc0xFF6B66FF#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
검정 = "#fc0xFF191919#";
분홍 = "#fc0xFFFF5AD9#";

var white = "#fc0xFFFFFFFF#";
var enter = "\r\n";

틀 = "#fUI/Basic.img/actMark/2#";
장비 = "#fUI/Basic.img/actMark/3#";
소비 = "#fUI/Basic.img/actMark/4#";
제작 = "#fUI/Basic.img/actMark/5#";
코인 = "#fUI/Basic.img/actMark/6#";
결정석 = "#fUI/Basic.img/actMark/7#";
기타 = "#fUI/Basic.img/actMark/8#";

var enter = "\r\n";
var seld = -1, seld2 = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	var msg = "#fs11#"
	if (status == 0) {
        var choose = "                    " + 틀 + "#l\r\n";
                choose += "#fc0xFFD5D5D5#──────────────────────────#l\r\n";
		choose += "#L1##fc0xFFFF3636#" + 장비 + "#l";
		choose += "#L2##fc0xFFFF3636#" + 소비 + "#l";
                choose += "#L3##fc0xFFFF3636#" + 제작 + "#l\r\n";
		choose += "#L4##fc0xFFFF3636#" + 코인 + "#l";
		choose += "#L5##fc0xFFFF3636#" + 결정석 + "#l";
		choose += "#L6##fc0xFFFF3636#" + 기타 + "#l\r\n";
		cm.sendOk(choose);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {				
			case 1234:
				cm.dispose();
				cm.openShop(1234);
				break;

			case 1:
				msg += " #e#b[장비 상점]#k#n" + enter;
				msg += "#L4#보조장비#n #k아이템" + enter;
				msg += "#L0#악세사리#n #k아이템" + enter;
				msg += "#L1#타일런트#n #k아이템" + enter;
				msg += "#L2#앱솔랩스#n #k아이템" + enter;
				msg += "#L3#아케인셰이드#n #k아이템" + enter;
				cm.sendSimple(msg);
				break;

			case 2:
				cm.dispose();
				cm.openShop(1);//소모상점
				break;

			case 3:
				msg += " #e#fs11##b[제작 상점]#k#n" + enter;
				msg += "#L12##d기본 강화#n 상점 #r(주문서,큐브,인장)#k" + enter;
				msg += "#L13##b아이템 제작#n #k#r(다양한 아이템 제작)#k" + enter;
				msg += "#L14##b승급 훈장#n 제작 #r#k" + enter;
				msg += "#L15##b칠요의 뱃지#n 제작 #r#k" + enter;
				msg += "#L16##b제네시스무기#n 제작 #r#k" + enter;
				cm.sendSimple(msg);
				break;

			case 4:
				msg += " #e#fs11##b[코인 상점]#k#n" + enter;
				msg += "#L5##b몬스터 코어#n #k상점 #r( 스톤,코어,젬 )#k" + enter;
				msg += "#L6##b하얀 결정 - F#n #k상점" + enter;
				msg += "#L7##d출석코인#n #k상점" + enter;
				msg += "#L101##fc0xFFEDD200#멜로디 코인#n #k상점" + enter;
				msg += "#L100##fc0xFFEDD200#하모니 코인#n #k상점" + enter;
				cm.sendSimple(msg);
				break;

			case 5:
				cm.dispose();
				cm.openShop(9001212); //몬스터 코어(쿼리)
				break;

			case 6:
				msg += " #e#fs11##b[기타 상점]#k#n" + enter;
				msg += "#L8##b팬시 포인트#n #k상점#k" + enter;
				msg += "#L9##b잠수 포인트#n #k상점" + enter;
				msg += "#L10##d안드로이드#n #k상점" + enter;
				msg += "#L11##fc0xFFEDD200#일반 펫#n #k상점" + enter;
				cm.sendSimple(msg);
				break;

			case 7:
				cm.dispose();
				cm.openShop(20);// 강화상점 3004822
				break;

			case 8:
				cm.dispose();
				cm.openShop(26); 
				break;
			case 50:
				cm.dispose();
				cm.openNpc(9062453);
				break;
			case 55:
				cm.dispose();
				cm.openShop(9062547);
				break;
			case 57:
				cm.dispose();
				cm.openNpc(9062115);
				break;

		}
	} else if (status == 2) {
		seld2 = sel;
		switch (sel) {
			case 0:
				cm.dispose();
				cm.openShop(9001000); //쿼리 3003414
				break;
			case 1:
				cm.dispose();
				cm.openShop(27);
				break;
			case 2:
				cm.dispose();
				cm.openShop(11);
				break;
			case 3:
				cm.dispose();
				cm.openShop(16);
				break;
			case 4:
				cm.dispose();
				cm.openShop(2);//보조장비상점(쿼리)
				break;
			case 5:
				cm.dispose();
				cm.openShop(3004822); //몬스터 코어(쿼리)
				break;
			case 6:
				cm.dispose();
				cm.openShop(5);// 하얀 결정 - F 상점
				break;
			case 7:
				cm.dispose();
				cm.openShop(9062547);
				break;
			case 8:
				cm.dispose();
				cm.openNpc(9062115);
				break;
			case 9:
				cm.dispose();
				cm.openNpc(9062453);
				break;
			case 10:
				cm.dispose();
				cm.openShop(26);
				break;
			case 11:
				cm.dispose();
				cm.openNpc(1530330);
				break;
			case 12:
				cm.dispose();
				cm.openShop(20);
				break;
			case 13:
				cm.dispose();
				cm.openNpc(3006009);
				break;
			case 14:
				cm.dispose();
				cm.openNpc(3004142);
				break;
			case 15:
				cm.dispose();
				cm.openNpc(1052223);
				break;
			case 16:
				cm.dispose();
				cm.openNpc(3004100);
				break;
			case 100:
                cm.sendOk("#fs11# #b 준비중인 상점 입니다.");
                cm.dispose();
				break;
			case 101:
                cm.sendOk("#fs11# #b 준비중인 상점 입니다.");
                cm.dispose();
				break;


		}

	}
}
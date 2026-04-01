importPackage(Packages.handling.channel.handler);

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
		msg += "#fs11#어서오세요 #h0#님! 무엇을 이용하시겠어요?\r\n";
		msg += "#L1# #b소비 상점을 이용하고 싶어요.#n #k\r\n";
		msg += "#L7# #b강화 상점을 이용하고 싶어요.#n #k\r\n";
		//msg += "#L2# #b장비 상점을 이용하고 싶어요.#n #k\r\n";
		msg += "#L5# #b경험치 도핑상점을 이용하고 싶어요.#n #k\r\n";
		//msg += "#L3# #b엠블럼 상점을 이용하고 싶어요.#n #k\r\n";
		//msg += "#L4# #b보조 무기 상점을 이용하고 싶어요.#n #k\r\n";
		//msg += "#L8# #b안드로이드 상점을 이용하고 싶어요.#n #k\r\n";
		msg += "#L50# #b잠수 포인트를 교환하고 싶어요.#n #k\r\n";
        msg += "#L55# #b출석코인 상점을 이용하고 싶어요.#n #k\r\n";
		msg += "#L57# #b사냥포인트 상점을 이용하고 싶어요.#n #k\r\n";
		msg += "#L58# #b던전 상점을 이용하고 싶어요.#n #k\r\n";		
		msg += "#L59# #b길드 상점을 이용하고 싶어요.#n #k\r\n";	
		msg += "#L6# #b캐시샵을 이용하고 싶어요.#n #k\r\n";


		cm.sendSimpleS(msg, 4);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				cm.dispose();
				cm.openShop(1);
				break;

			case 2:
				msg += " #e#b[장비 상점]#k#n" + enter;
				msg += "#L0#악세사리#n #k아이템" + enter;
				msg += "#L1#타일런트#n #k아이템" + enter;
				msg += "#L2#앱솔랩스#n #k아이템" + enter;
				msg += "#L3#아케인셰이드#n #k아이템" + enter;
				cm.sendSimple(msg);
				break;

			case 3:
				cm.dispose();
				cm.openShop(5);
				break;

			case 4:
				cm.dispose();
				cm.openShop(2);
				break;

			case 5:
				cm.dispose();
				cm.openShop(3004822);
				break;
				break;

			case 6:
				InterServerHandler.EnterCS(cm.getPlayer().getClient(), cm.getPlayer(), false);
				cm.dispose();
				break;

			case 7:
				cm.dispose();
				cm.openShop(20);
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
			case 59:
				cm.dispose();
				cm.openNpc(1096006);	
				break;
			case 58:
				cm.dispose();
				cm.openShop(1032106);
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
				cm.openShop(9001000);
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
		}
	}
}
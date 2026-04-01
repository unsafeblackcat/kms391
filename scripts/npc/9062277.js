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
设备 = "#k 裝備商店 #r";
消费 = "#k 消耗商店 #r";
制作 = "#k 制作商店 #r";
硬币 = "#k 硬幣商店 #r";
測謊 = "#k 測機商店 #r";
其他 = "#k 其他商店 #r";

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
	var msg = "#fs15#"
	if (status == 0) {
        var choose = "                    " + 틀 + "#l\r\n";
                choose += "#fc0xFFD5D5D5#──────────────────────────#l\r\n";
		choose += "#L1##fc0xFFFF3636#" + 设备 + "#l";
		choose += "#L2##fc0xFFFF3636#" + 消费 + "#l";
                choose += "#L3##fc0xFFFF3636#" + 制作 + "#l\r\n";
		choose += "#L4##fc0xFFFF3636#" + 硬币 + "#l";
		choose += "#L5##fc0xFFFF3636#" + 測謊 + "#l";
		choose += "#L6##fc0xFFFF3636#" + 其他 + "#l\r\n";
		cm.sendOk(choose);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {				
			case 1234:
				cm.dispose();
				cm.openShop(1234);
				break;

			case 1:
				msg += " #e#b[装备商店]#k#n" + enter;
				msg += "#L4#輔助裝備#n #k物品" + enter;
				msg += "#L0#普通飾品#n #k物品" + enter;
				msg += "#L1#暴君飾品#n #k物品" + enter;
				msg += "#L2#航海套裝#n #k物品" + enter;
				msg += "#L3#神秘套裝#n #k物品" + enter;
				cm.sendSimple(msg);
				break;

			case 2:
				cm.dispose();
				cm.openShop(1);//소모상점
				break;

			case 3:
				msg += " #e#fs15##b[製作商店]#k#n" + enter;
				msg += "#L12##d基本增强#n 商店 #r(訂單，魔方，印章)#k" + enter;
				msg += "#L13##b製作物品#n #k#r(製作多種道具)#k" + enter;
				msg += "#L14##b陞級勳章#n 製作 #r#k" + enter;
				msg += "#L15##b七日徽章#n 製作 #r#k" + enter;
				msg += "#L16##b勞恩斯武器#n 製作 #r#k" + enter;
				cm.sendSimple(msg);
				break;

			case 4:
				msg += " #e#fs15##b[硬幣商店]#k#n" + enter;
				msg += "#L5##b怪物覈心 掉綫#n #k商店 #r( 石頭，覈心，傑姆 )#k" + enter;
				msg += "#L6##b白色結晶 掉綫#n #k商店" + enter;
				msg += "#L7##d簽到幣#n #k商店" + enter;
				msg += "#L101##fc0xFFEDD200#旋律幣#n #k商店" + enter;
				msg += "#L100##fc0xFFEDD200#和聲幣#n #k商店" + enter;
				cm.sendSimple(msg);
				break;

			case 5:
				cm.dispose();
				cm.openShop(9001212); //몬스터 코어(쿼리)
				break;

			case 6:
				msg += " #e#fs15##b[其他商店]#k#n" + enter;
				msg += "#L8##b狩獵積分#n #k商店#k" + enter;
				msg += "#L9##b潛水點#n #k商店" + enter;
				msg += "#L10##d安卓機器人#n #k商店" + enter;
				msg += "#L11##fc0xFFEDD200#普通寵物#n #k商店" + enter;
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
				cm.openShop(3003414); //몬스터 코어(쿼리)
				break;
			case 6:
				cm.dispose();
				cm.openShop(1540213);// 白色結晶
				break;
			case 7:
				cm.dispose();
				cm.openShop(1234);
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
			        cm.dispose();
                                cm.openShop(40);
				break;
			case 101:
                                cm.dispose();
                                cm.openShop(39);
				break;


		}

	}
}
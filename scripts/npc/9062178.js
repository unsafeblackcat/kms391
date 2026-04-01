var status;

var sel, sel2;

var maxReborn = 120; // 시즌 최대 환생 횟수
var increaseRebornPoint = 1000; // 환생 시 증가하는 환생 포인트 량

var itemList = [ // 아이템 코드, 아이템 갯수, 필요 포인트 량
	[4310007, 5000, 1000],
	[4001716, 20, 1000],
	[2636135, 500, 1000],
	[2636375, 300, 1000],
	[2636656, 10, 1000],
	[4009547, 500, 1000],
	[5062006, 10, 1000],
	[5681033, 1, 10000],
        [5680942, 1, 20000],
	[5060048, 5, 1000],
	[2049377, 1, 1000],

];

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}

	if (status == 0) {
		if (cm.getPlayer().getKeyValue(7777778, "Reborn") == -1) {
			cm.getPlayer().setKeyValue(7777778, "Reborn", 0 + "");
		}

		if (cm.getPlayer().getKeyValue(7777778, "Reborn Point") == -1) {
			cm.getPlayer().setKeyValue(7777778, "Reborn Point", 0 + "");
		}

		var chat = "#fs11#환생 시스템 입니다. 환생은 시즌 당 최대 #b" + maxReborn + "회 #k까지 가능합니다.\r\n";
		chat += "환생 가능한 레벨은 #b300 Lv#k이며, 환생 시 #b260 Lv#k로 돌아갑니다.\r\n\r\n";
                chat +="     ▼▼▼▼▼▼▼▼ 1회 환생시 얻는 보상 ▼▼▼▼▼▼▼▼\r\n\r\n";
                chat +="#fc0xFF191919#                           환생포인트 + #r1000 P \r\n";
                chat +="#fc0xFF191919#                                올스탯 + #r10 \r\n";
                chat +="#fc0xFF191919#                                공마 + #r10 \r\n";
		chat += "                     #d#fs14#환생 횟수 : #r" + cm.getPlayer().getKeyValue(7777778, "Reborn") + "회#r/" + maxReborn + "회\r\n";
		chat += "                 #d환생 포인트#r : " + cm.getPlayer().getKeyValue(7777778, "Reborn Point") + "P\r\n";
		chat += "#fs11##L0##b환생 하기#k#l\r\n";
		chat += "#L99##b(환생상점 오픈 준비중....)#k#l";
		cm.sendSimple(chat);
	} else if (status == 1) {
		sel = selection;

		if (sel == 0) {
			if (cm.getPlayer().getKeyValue(7777778, "Reborn") >= maxReborn) {
				cm.sendOk("이미 시즌 최대 환생 가능 횟수에 도달 하였습니다.");
				cm.dispose();
				return;
			}

			if (cm.getPlayer().getLevel() >= 300) {
				cm.getPlayer().setLevel(260);
				cm.getPlayer().levelUp();
				cm.getPlayer().setExp(0);

				var statup = new java.util.EnumMap(Packages.client.MapleStat.class);
				statup.put(Packages.client.MapleStat.EXP, cm.getPlayer().getExp());
				cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.updatePlayerStats(statup, cm.getPlayer()));

				cm.resetStats(4, 4, 4, 4);
				cm.getPlayer().gainAp(-cm.getPlayer().getRemainingAp());
				cm.getPlayer().gainAp(1300);

				cm.getPlayer().skillMaster();

				cm.getPlayer().setKeyValue(7777778, "Reborn", (cm.getPlayer().getKeyValue(7777778, "Reborn") + 1) + "");
				cm.getPlayer().setKeyValue(7777778, "Reborn Point", (cm.getPlayer().getKeyValue(7777778, "Reborn Point") + increaseRebornPoint) + "");

				if (cm.getPlayer().getBuffedValue(80000757)) {
					cm.giveBuff(80000757);
				}

				cm.giveBuff(80000757);

				cm.sendOk("환생이 완료 되었습니다.\r\n\r\n환생 횟수 : " + cm.getPlayer().getKeyValue(7777778, "Reborn") + "회/" + maxReborn + "회\r\n환생 포인트 : " + cm.getPlayer().getKeyValue(7777778, "Reborn Point") + "P");
				cm.dispose();
			} else {
				cm.sendOk("환생이 가능한 레벨이 아닙니다.");
				cm.dispose();
			}
		} else if (sel == 1) {
			var chat = "#fs11#환생포인트로 아이템을 구매 하실 수 있습니다.\r\n\r\n";
			chat += "#fs11##b보유 환생 포인트 : " + cm.getPlayer().getKeyValue(7777778, "Reborn Point") + "P\r\n\r\n";
			
			var size = itemList.length;
			for (var i = 0; i < size; i++) {
				chat += "#L" + i + "##i" + itemList[i][0] + "# #b#z" + itemList[i][0] + "# " + itemList[i][1] + "개#k #r(" + itemList[i][2] + " P)#k#l";
				if (i != (size - 1)) {
					chat += "\r\n";
				}
			}

			cm.sendSimple(chat);
		}
	} else if (status == 2) {
		sel2 = selection;

		if (sel == 1) {
			cm.sendYesNo("정말 #i" + itemList[sel2][0] + "# #b#z" + itemList[sel2][0] + "# " + itemList[sel2][1] + "개#k를 구매 하시겠습니까 ?");
		}
	} else if (status == 3) {
		if (sel == 1) {
			if (cm.getPlayer().getKeyValue(7777778, "Reborn Point") >= itemList[sel2][2]) {
				cm.getPlayer().setKeyValue(7777778, "Reborn Point", (cm.getPlayer().getKeyValue(7777778, "Reborn Point") - itemList[sel2][2]) + "");
				cm.gainItem(itemList[sel2][0], itemList[sel2][1]);
				cm.sendOk("#i" + itemList[sel2][0] + "# #b#z" + itemList[sel2][0] + "# " + itemList[sel2][1] + "개#k를 구매 했습니다.");
				cm.dispose();
			} else {
				cm.sendOk("환생 포인트가 부족합니다.");
				cm.dispose();
			}
		}
	}
}
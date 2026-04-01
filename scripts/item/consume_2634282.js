var status;

var 전사 = [1004422, 1102775, 1082636, 1052882, 1073030, 1152174];//戰士
var 도적 = [1004425, 1102796, 1082639, 1052889, 1073034, 1152178];//盗贼
var 해적 = [1004426, 1102797, 1082640, 1052890, 1073035, 1152179];//海盗
var 궁수 = [1004424, 1102795, 1082638, 1052888, 1073033, 1152177];//弓箭手
var 마법사 = [1004423, 1102794, 1082637, 1052887, 1073032, 1152176];//魔法師
var 보스장신구 = [1032241, 1012478, 1022231, 1113149, 1122254, 1132272, 1113282];//装饰
var 무기 = [1212115, 1213017, 1214017, 1222109, 1232109, 1242116, 1242120, 1262017, 1272016,
            1282016, 1292017, 1302333, 1312199, 1322250, 1332274, 1362135, 1372222, 1382259, 
            1402251, 1404017, 1412177, 1422184, 1432214, 1442268, 1452252, 1462239, 1472261, 
            1482216, 1492231, 1522138, 1532144, 1582017, 1592019];//武器
var 아케인심볼 = [1712001, 1712002, 1712003, 1712004, 1712005, 1712006];//神秘

var sel;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1 || mode == 0) {
		cm.dispose();
		return;
	} else {
		status++;
	}

	if (status == 0) {
		var chat = "#fs15#請選擇職業群 !\r\n\r\n#r* 因自己的失誤將無法得到修復。.#k\r\n\r\n";
		chat += "#L0##b戰士#k#l\r\n";
		chat += "#L1##b魔法師#k#l\r\n";
		chat += "#L2##b弓箭手#k#l\r\n";
		chat += "#L3##b盜賊#k#l\r\n";
		chat += "#L4##b海盜#k#l";
		cm.sendSimple(chat);
	} else if (status == 1) {
		sel = selection;
		var chat = "#fs15#請選擇適合職業群的武器。 !\r\n\r\n#r* 因自己的失誤將無法得到修復。.#k\r\n\r\n";
		
		chat += "#fUI/UIWindow2.img/QuestIcon/3/0#\r\n";

		var length = 무기.length;
		for (var i = 0; i < length; i++) {
			chat += "#L" + 무기[i] + "##i" + 무기[i] + "# #b#z" + 무기[i] + "##k#l\r\n";
		}

		cm.sendSimple(chat);
	} else if (status == 2) {
		var list;
		var arcStat;
		switch (sel) {
			case 0:
				list = 전사;
				arcStat = 1;
				break;
			case 1:
				list = 마법사;
				arcStat = 2;
				break;
			case 2:
				list = 궁수;
				arcStat = 3;
				break;
			case 3:
				list = 도적;
				arcStat = 4;
				break;
			case 4:
				list = 해적;
				arcStat = 5;
				break;
		}

		if (list == null) {
			cm.sendOk("#fs15#發生錯誤. 請與GM聯系.");
			cm.dispose();
			return;
		}


		// 航海师套装发放
		var length = list.length;
		for (var i = 0; i < length; i++) {
			var item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(list[i]);
		    	item.setState(20);
		    	item.setPotential1(30086);
		    	item.setPotential2(30086);
		    	item.setPotential3(30086);
		    	item.setPotential4(20086);
		    	item.setPotential5(20086);
		    	item.setPotential6(20086);
		    	item.setFlag(item.getFlag() + Packages.client.inventory.ItemFlag.UNTRADEABLE.getValue());
		    	item.setOwner("프레이");

		    	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item);
		}

		// BOSS飾品套裝發放
		var bossAcc = 보스장신구.length;
		for (var i = 0; i < bossAcc; i++) {
			var item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(보스장신구[i]);
		    	item.setState(20);
		    	item.setPotential1(30086);
		    	item.setPotential2(30086);
		    	item.setPotential3(30086);
		    	item.setPotential4(20086);
		    	item.setPotential5(20086);
		    	item.setPotential6(20086);
			item.setFlag(item.getFlag() + Packages.client.inventory.ItemFlag.UNTRADEABLE.getValue());
		    	item.setOwner("프레이");

		    	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item);
		}

		// 武器发放
		var item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(selection);


		var selectPotential = (item.getWatk() > item.getMatk() ? 20051 : 20052);

		item.setState(20);
		item.setPotential1(selectPotential);
		item.setPotential2(selectPotential);
		item.setPotential3(selectPotential);
		item.setPotential4(selectPotential);
		item.setPotential5(selectPotential);
		item.setPotential6(selectPotential);
		item.setFlag(item.getFlag() + Packages.client.inventory.ItemFlag.UNTRADEABLE.getValue());
		item.setOwner("프레이");

		Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item);


		cm.gainItem(1182087, 1); // 水晶溫特斯徽章
		cm.gainItem(1143304, 1); // 20週年紀念勳章
		cm.gainItem(1162025, 1); // 粉色聖杯
		cm.gainItem(2000054, 1); // 無限藥水
		cm.gainItem(3700510, 1); // 聯合勇士
		cm.gainItem(1112673, 1); // 傳說楓葉黃金戒
		cm.gainItem(1112679, 1); // 白金十字戒指
		cm.gainItem(1190302, 1); // 水晶楓葉徽章
		cm.gainItem(2438871, 1); // 基本傷害組織
		cm.gainItem(2437718, 1); // 机器人兑换卷
	    cm.gainItem(2636135, 6); // 神秘徽章自选
	    cm.gainItem(2636375, 6); // 原初徽章自选
	    cm.gainItem(5044000, 1); // 超級瞬移之石
	    cm.gainItem(2631880, 1); // 高級菜單
		cm.gainItem(2634282, -1);

		if (cm.getClient().getKeyValue("firstSymbol") == 1) {
			cm.sendOk("#fs15#初始支持奬勵發放。");
			cm.dispose();
			return;
		} else if (cm.getClient().getKeyValue("firstSymbol") == 0 || cm.getClient().getKeyValue("firstSymbol") == null) {
			cm.getClient().setKeyValue("firstSymbol", "1");
			cm.sendOk("#fs15#新手奬勵已發放完畢.");
			cm.dispose();
		}
	}
}
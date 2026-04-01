
importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);


var enter = "\r\n";
var year, month, date2, date, day
var hour, minute;

var questt = "boss_17"; // jump_고유번호
검정 = "#fc0xFF191919#"
var reward = [
	//{ 'itemid': 4036166, 'qty': 1 },
	{ 'itemid': 4036454, 'qty': 1 }

]

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
	if (status == 0) {
		if (cm.getClient().getKeyValue(questt) == null)
			cm.getClient().setKeyValue(questt, "0");

		if (parseInt(cm.getClient().getKeyValue(questt)) > 0) {
			cm.sendOk("#fs15#" + 검정 + "今天的獎勵已經收到了.");
			cm.dispose();
			return;
		}
		cm.sendYesNo("#fs15#" + 검정 + "請領取盧塔維斯入場券?");
	} else if (status == 1) {
		if (parseInt(cm.getClient().getKeyValue(questt)) > 0) {
			cm.sendOk("#fs15#" + 검정 + "每個帳號每天只能獲得一次獎勵.");
			cm.dispose();
			return;
		}

		cm.getClient().setKeyValue(questt, "1");
            itemid = 4001034;
            time = new Date();
            var inz = Packages.client.inventory.itemz = new Packages.client.inventory.Item(itemid, 0, 3);
            inz.setExpiration((new Date(time.getFullYear(), time.getMonth(), time.getDate(), 0, 0, 0, 0)).getTime() + (1000 * 60 * 60 * 24 * 1));
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            itemid = 4032301;
            time = new Date();
            var inz = Packages.client.inventory.itemz = new Packages.client.inventory.Item(itemid, 0, 3);
            inz.setExpiration((new Date(time.getFullYear(), time.getMonth(), time.getDate(), 0, 0, 0, 0)).getTime() + (1000 * 60 * 60 * 24 * 1));
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            itemid = 4033047;
            time = new Date();
            var inz = Packages.client.inventory.itemz = new Packages.client.inventory.Item(itemid, 0, 3);
            inz.setExpiration((new Date(time.getFullYear(), time.getMonth(), time.getDate(), 0, 0, 0, 0)).getTime() + (1000 * 60 * 60 * 24 * 1));
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
            itemid = 4036456;
            time = new Date();
            var inz = Packages.client.inventory.itemz = new Packages.client.inventory.Item(itemid, 0, 3);
            inz.setExpiration((new Date(time.getFullYear(), time.getMonth(), time.getDate(), 0, 0, 0, 0)).getTime() + (1000 * 60 * 60 * 24 * 1));
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
		말 = "#fs15#" + 검정 + "老闆入場券.\r\n\r\n"
		cm.sendOk(말);
		cm.dispose();
	}
}

function getData() {
	time = new Date();
	year = time.getFullYear();
	month = time.getMonth() + 1;
	if (month < 10) {
		month = "0" + month;
	}
	date2 = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
	date = year + "" + month + "" + date2;
	day = time.getDay();
	hour = time.getHours();
	minute = time.getMinutes();
}
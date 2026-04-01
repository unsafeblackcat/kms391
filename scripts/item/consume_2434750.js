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

var status;
function start() {
	status = -1;
	action(1, 1, 0);
}
주화 = 4310020;
주화개수 = Randomizer.rand(5, 20);

function action(mode, type, selection) {

	if (mode == -1) {
		cm.dispose();
		return;
	}
	if (mode == 0) {
		status--;
	}
	if (mode == 1) {
		status++;
	}
	if (status == 0) {
		if (Randomizer.isSuccess(15)) {
			cm.gainItem(1182197, 1);
		} else if (Randomizer.isSuccess(5)) {
			유니크 = 500000000
			cm.gainMeso(유니크);
			if (유니크 > 499999999) {
				World.Broadcast.broadcastMessage(CField.getGameMessage(8, "" + cm.getPlayer().getName() + "님이 황금의 금요일 상자에서 5억 메소를 획득하셨습니다!"));
			}
		} else {
			메소 = Randomizer.rand(1000000, 10000000);
			cm.gainMeso(메소);
		}
		cm.gainItem(주화, 주화개수);
		cm.gainItem(2434750, -1);
		cm.dispose();
	}
}

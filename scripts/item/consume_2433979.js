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

function start() { Status = -1; action(1, 0, 0); }

function action(M, T, S) {

	if (M == -1) { cm.dispose(); } else {
		if (M == 0) { cm.dispose(); return; }
		if (M == 1) Status++; else Status--; 


		if (Status == 0) {
			if (!cm.haveItem(2433979)) {
				cm.dispose();
			} else {
				if (Randomizer.isSuccess(1)) {
					itemSet = 2000000000;
				} else if (Randomizer.isSuccess(10, 10000)) { // ex) 0.1856 = Randomizer.isSuccess(1856, 10000)
					itemSet = 2000000000;
				} else
					itemSet = Randomizer.rand(1, 500000000);
			}
			if (itemSet > 19999999999) {
				World.Broadcast.broadcastMessage(CField.getGameMessage(9, ""+ cm.getPlayer().getName()+"님이 메소 럭키백에서 20억 메소를 뽑으셨습니다!!!"));
			} else if (itemSet > 1999999999) {
				World.Broadcast.broadcastMessage(CField.getGameMessage(9, ""+ cm.getPlayer().getName()+"님이 메소 럭키백에서 20억 메소를 뽑으셨습니다!"));
			}
			cm.gainItem(2433979, -1);
			cm.gainMeso(itemSet);
			cm.dispose();
		}
	}
}




function Rullet() {
	M = Math.floor(Math.random() * 10000);
	switch (M) {
		case 4873: case 9873: N = 4001208; Q = 1; W = "천만"; break;

		default: N = M * 1000; W = N; break;
	}
}
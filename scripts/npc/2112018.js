importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);

function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
		//cm.sendOk("d");
		//cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300152), new java.awt.Point(63, 150), 14);
		cm.getPlayer().getMap().spawnNpc(2112018, new java.awt.Point(55, 150));
        cm.dispose();
	}
}
var status;
importPackage(Packages.server);
importPackage(Packages.client.inventory);

후포 = Randomizer.rand(3000, 10000);

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }
        if (status == 0) {
	var msg = "주머니에서 " + 후포 + "만큼의 포인트를 획득했습니다."
	cm.getPlayer().dropMessage(-8, msg);
	cm.getPlayer().gainDonationPoint(후포);
	cm.gainItem(2434636,-1);
	cm.dispose();
	}
}

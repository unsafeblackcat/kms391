importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.tools.RandomStream);

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
	if (status == 0) {
	cm.sendYesNo("타이믹 코인 랜덤 상자를 정말로 까시겠습니까?");
	} else if (status == 1) {
	cm.gainItem(2430217, -1);
	cm.gainItem(4310085, Randomizer.rand(10,30));
	cm.dispose();
	}
}
}
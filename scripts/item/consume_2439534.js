var status;
importPackage(Packages.server);
importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.server.items);
one = Math.floor(Math.random() * 5) + 1 // 최소 10 최대 35 , 혼테일
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
		cm.gainItem(4319998, 5);
        cm.gainItem(4001780, 10);
        cm.gainItem(4001716, 15);
        cm.gainItem(4319997, 500);
	    cm.gainItem(4319994, 700);
        cm.gainItem(2635381, 2);
        cm.gainItem(5062005, 100);
        cm.gainItem(5062503, 100);
		cm.gainItem(4319999, 5000);
		cm.gainItem(2439534, -1);
		cm.dispose();
	}
}

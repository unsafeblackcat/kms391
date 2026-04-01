importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.server.enchant);
importPackage(java.sql);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);


var status = -1;

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
		var max = cm.itemQuantity(2432970);
		seld = selection;
	    cm.sendGetNumber(2004, "사용하실 스패셜 명예의 훈장의 수량을 적어주세요.\r\n\r\n", 1, 1, 100);
    } else if (status == 1) {
		if (!cm.haveItem(2432970, selection)) {
		cm.sendOk("스패셜 명예의 훈장이 부족합니다.");
		cm.dispose();
		return;
	}
	 cm.gainItem(2432970, -selection);
		 cm.getPlayer().gainHonor(selection*10000);
        cm.dispose();
    }
}
    


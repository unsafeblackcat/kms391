/*
제작 : shou
용도 : LUNA 팩 362ver
*/


importPackage(Packages.constants);
importPackage(Packages.tools.packet);
importPackage(Packages.tools.packet);
importPackage(Packages.constants.programs);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.handling.world)
importPackage(java.sql);
importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);
importPackage(java.awt);
importPackage(Packages.database);
importPackage(Packages.constants);
importPackage(Packages.client.items);
importPackage(Packages.client.inventory);
importPackage(Packages.server.items);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(Packages.server.Luna);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database.hikari);
importPackage(java.lang);
importPackage(Packages.handling.world)

var status;
var select;

var Nlist = [
	[2633616, 10, 10],
	[4001716, 20, 10],
	[2049164, 20, 10],
	[2046308, 15, 10],
	[2046309, 15, 10],
	[2046213, 15, 10],
	[2046214, 15, 10],
	[2046006, 10, 10],
	[2046007, 10, 10],
	[2046106, 10, 10],
	[2048051, 8, 10],
	[2048052, 8, 10],
	[5060048, 25, 10]
];
var Rlist = [
	[4310900, 750, 5]
];
var Plist = [[2635900, 1, 1],[2635901, 1,1],[2635902, 1,1]];

var sum = 0;
function start() {
	status = -1;
	action(1, 1, 0);
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
			var text = ""
			text += "사용할 뽑기 리스트를 선택해주세요!.#b\r\n"
			text += "#L1##b의자 뽑기#l\r\n";
			text += "#L2##b라이딩 뽑기#l\r\n";
			text += "#L3##b데미지스킨 뽑기#l\r\n";
			text += "#L4##b칭호 뽑기#l\r\n";
			text += "#L5##b훈장 뽑기#l\r\n";
			text += "#L0##d아니 안뽑을래...#l"
			cm.sendSimple(text)
		} else if (status == 1) {
				switch(selection) {
					case 0:
						cm.dispose();
					break;
					case 1:
						cm.dispose();
						cm.openNpcCustom(cm.getClient(), 1052014, "Chair");
					break;
					case 2:
						cm.dispose();
						cm.openNpcCustom(cm.getClient(), 1052014, "Riding");
					break;
					case 3:
						cm.dispose();
						cm.openNpcCustom(cm.getClient(), 1052014, "Damageskin");
					break;
					case 4:
						cm.dispose();
						cm.openNpcCustom(cm.getClient(), 1052014, "title");
					break;
					case 5:
						cm.dispose();
						cm.openNpcCustom(cm.getClient(), 1052014, "hoonjang");
					break;

						
				}
		} else if (status == 2) {

		}
	}
}
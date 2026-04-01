/*
제작 : 또깡
용도 : 또깡 팩 312
*/

importPackage(Packages.tools.packet);
importPackage(Packages.constants.programs);
importPackage(Packages.database);
importPackage(java.lang);
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
importPackage(Packages.constants);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

시드링 = [1113098, 1113099, 1113100, 1113101, 1113102, 1113103, 1113104, 1113105, 1113106, 1113107, 1113108, 1113109, 1113110, 1113111, 1113112, 1113113, 1113114, 1113115, 1113116, 1113117, 1113118, 1113119, 1113120, 1113121, 1113122, 1113123, 1113124, 1113125, 1113126]


코인 = 4310320;

개수 = 200;

status = -1;

function start() {
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1)
	status++;        
	else
	status--;
    if (status == 0) {
	말 = "#fs15##fc0xFF000000#각종 시드링이 준비되어 있는 것 같다.#b\r\n";
	말 += "랜덤 - #i4310320##z4310320# 200개\r\n\r\n";
	말 += "#L0##d랜덤으로 뽑아볼래.#l\r\n\r\n";
    말 += "#L1##d아이템 목록을 확인해본다.#l\r\n";
	cm.sendSimple(말);
    } else if (status == 1) {
	if (selection == 0) {
		cm.sendYesNo("#b#z"+코인+"# #r"+개수+" 개#k 를 사용하여 뽑아볼까?");
	} else if (selection == 1) {
		말 = "#fs15##b\r\n"
		for (i = 0; i < 시드링.length; i++) {
			말 += "#v"+시드링[i]+"# #d#z"+시드링[i]+"#\r\n";
 		}
		cm.sendOk(말);
		cm.dispose();
	}
    } else if (status == 2) {
	뽑은템 = 시드링[Randomizer.rand(0, 시드링.length)];
	//뽑은템 = 시드링[Randomizer.rand(0, (시드링.length - 1));
	if (cm.haveItem(코인, 개수)) {
		if (cm.canHold(뽑은템)) {
			cm.gainItem(뽑은템, 1);
			cm.gainItem(코인, -개수);
			cm.sendOk("#fs15#내가 뽑은 아이템은! : \r\n\r\n#i"+뽑은템+"# #d#z"+뽑은템+"#");
                        //cm.broadcastSmega(7,"[알림] "+ cm.getPlayer().getName()+" 님이 칭호 뽑기를 하셨습니다.")
			cm.dispose();
		} else {
			cm.sendOk("#fs15# #r소비#k창을 비워야겠군.");
			cm.dispose();
		}
	} else { 
		cm.sendOk("#fs15#뽑기에 필요한 재료가 없다.");
		cm.dispose()
	}
    }
}
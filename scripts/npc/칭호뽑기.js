/*
제작 : shou
용도 : LUNA 팩 362ver
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

칭호 = [3700548, 3700540, 3700539, 3700530, 3700529, 3700528, 3700526, 3700525, 3700524, 3700514, 3700513, 3700512, 3700511, 3700510, 3700492, 3700490, 3700489, 3700486, 3700478, 3700468, 3700468, 3700466, 3700465, 3700461, 3700458, 3700454, 3700447, 3700445, 3700444, 3700442, 3700440, 3700435, 3700434, 3700433, 3700432, 3700430, 3700429, 3700425, 3700422, 3700421, 3700420, 3700419, 3700418, 3700417, 3700403, 3700402,3700390, 3700390, 3700389, 3700388, 3700385, 3700380, 3700378, 3700377, 3700376, 3700356, 3700355, 3700354, 3700353, 3700352, 3700344, 3700339, 3700338, 3700337, 3700336, 3700335, 3700390, 3700322, 3700390, 3700321, 3700308, 3700306, 3700307, 3700000, 3700001, 3700002, 3700003, 3700004, 3700005, 3700006, 3700010, 3700011, 3700012, 3700013, 3700014, 3700015, 3700016, 3700017, 3700018, 3700019, 3700025, 3700026, 3700004, 3700035, 3700040, 3700045, 3700046, 3700063, 3700064, 3700065, 3700000, 3700068, 3700091, 3700090, 3700093, 3700096, 3700108, 3700118, 3700119, 3700176, 3700177, 3700178, 3700180, 3700220, 3700270, 3700228, 3700278, 3700279, 3700280, 3700283, 3700285, 3700286, 3700108]


코인 = 2632800;

개수 = 70;

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
	말 = "#fs11#\r\n";
	말 += "#L0##i3700514# #d칭호를 랜덤으로 뽑겠습니다.#l\r\n\r\n#y#　└ #b#i2632800##z2632800##k 80개가 필요합니다.\r\n";
	말 += "#L1##i3700514# #r모든 칭호 목록을 확인하고 싶어요.#l";
	cm.sendSimple(말);
    } else if (status == 1) {
	if (selection == 0) {
		cm.sendYesNo("정말 #b#z"+코인+"# #r"+개수+" 개#k 를 사용하여 뽑을건가요?");
	} else if (selection == 1) {
		말 = "#fs11##b＃. 다음과 같은 칭호를 획득할 수 있습니다.\r\n\r\n"
		for (i = 0; i < 칭호.length; i++) {
			말 += "#v"+칭호[i]+"# #d#z"+칭호[i]+"#\r\n";
 		}
		cm.sendOk(말);
		cm.dispose();
	}
    } else if (status == 2) {
	뽑은템 = 칭호[Randomizer.rand(0, 칭호.length)];
	if (cm.haveItem(코인, 개수)) {
		if (cm.canHold(뽑은템)) {
			cm.gainItem(뽑은템, 1);
			cm.gainItem(코인, -개수);
			cm.sendOk("#fs11#당신이 뽑은아이템은! : \r\n\r\n#i"+뽑은템+"# #d#z"+뽑은템+"#");
                        //cm.broadcastSmega(7,"[알림] "+ cm.getPlayer().getName()+" 님이 칭호 뽑기를 하셨습니다.")
			cm.dispose();
		} else {
			cm.sendOk("#fs11# #r설치#k창을 비워주세요.");
			cm.dispose();
		}
	} else { 
		cm.sendOk("코인이 부족합니다");
		cm.dispose()
	}
    }
}
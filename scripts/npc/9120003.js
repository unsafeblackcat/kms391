
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


월드보스 = "#fUI/Basic.img/theblackcoin/41#";
검정 = "#fc0xFF191919#";
노랑 = "#fc0xFFE0B94F#";
보라 = "#fc0xFFB677FF#";
보라색 = "#fc0xFF8041D9#"
블루 = "#fc0xFF4641D9#"
회색 = "#fc0xFF4C4C4C#"
연파랑 = "#fc0xFF6799FF#"
커플 = "#fUI/Basic.img/theblackcoin/35#";
결혼 = "#fUI/Basic.img/theblackcoin/36#";
검은마법사 = "#fUI/Basic.img/theblackcoin/42#";
유니온 = "#fUI/UIWindow4.img/pointShop/500629/iconShop#";
낚시 = "#fUI/Basic.img/theblackcoin/22#";

var enter = "\r\n";
var seld = -1, seld2 = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    }
	if (status == 0) {
		var msg = "    #d#h #님 안녕하세요! 해피서버 컨텐츠 시스템입니다. \r\n\r\n";
		msg += "#fs15##fc0xFFCCCCCC#―――――――――――――――――――――――――――――――――――#fs15##b";
		msg += "#L30#" + 커플 + " " + 보라 + "뽑기 시스템을 이용하겠습니다.\r\n#l";
		msg += "#L31#" + 결혼 + " #fc0xFFF361DC#컨텐츠#k를 즐기겠습니다.\r\n#l";
		msg += "#L32##fc0xFFDF4D4D#" + 검은마법사 + " #fc0xFF998A00#코디 시스템#k 을 이용하고 싶습니다.\r\n#l";
		msg += "#L33#" + 유니온 + " #fc0xFF998A00#유니온 및 일일 퀘스트#k 를 이용하고 싶습니다.\r\n#l";
		msg += "#L34#" + 낚시 + " #fc0xFF6B66FF#낚시터#k를 이용하고 싶습니다..\r\n#l";
		msg += "#L35#" + 월드보스 + " #fc0xFF191919#월드보스 레이드#k에 참가하겠습니다.\r\n\r\n#l";
		msg += "#L36#" + 유니온 + " #fc0xFF998A00#이벤트 퀘스트#k 를 이용하고 싶습니다.\r\n#l";
		msg += "#fs15##fc0xFFCCCCCC#―――――――――――――――――――――――――――――――――――\r\n#fs15##b";
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 30 : cm.dispose(); cm.openNpc(9062605); break; // 뽑기 시스템
			case 31 : cm.dispose(); cm.openNpc(9401240, "컨텐츠시스템"); break; // 컨텐츠 시스템
			case 32 : cm.dispose(); cm.openNpc(9076000); break; // 코디 시스템
			case 33 : cm.dispose(); cm.openNpc(9010015); break; // 일일 & 유니온
			case 34 : cm.dispose(); cm.warp(993000750); break; // 낚시터
			case 35 : cm.dispose(); cm.warp(680000700); break; // 월드보스
			case 36 : cm.dispose(); cm.openNpc(9076003); break; // 이벤트
		}
	} else {
		cm.dispose();
		return;
	}
}

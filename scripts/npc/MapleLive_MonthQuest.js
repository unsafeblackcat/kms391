importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.server.quest);
var status = -1;
var pinkbean = 0, upcount = 0;
var check = false;
var quest;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
		return;
	}
	if (mode == 0) {
		status--;
	}
	if (mode == 1) {
		d = status;
		status++;
	}

	if (status == 0) {
		sels = cm.getPlayer().getSkillCustomValue0(501378);
		quest = MapleQuest.getInstance((501497 + sels));
		switch (quest.getId()) {
			case 501497:
			case 501501:
			case 501506:
			case 501510:
			case 501514:
			case 501518:
				pinkbean = 50
				break;
			case 501498:
			case 501502:
			case 501507:
			case 501511:
			case 501515:
			case 501519:
				pinkbean = 100
				break;
			case 501499:
			case 501503:
			case 501508:
			case 501512:
			case 501516:
			case 501520:
				pinkbean = 150
				break;
			case 501500:
			case 501504:
			case 501509:
			case 501513:
			case 501517:
			case 501521:
				pinkbean = 200
				break;
			case 501505:
			case 501522:
				pinkbean = 250;
				break;
		}
		cm.sendYesNoS("#b#e< " + (quest.getName().replaceAll("\\]", "").replaceAll("\\[", "").replaceAll("메이플 LIVE ", "").replaceAll("완료 ", "")) + ">#n#k 방송 촬영을 완료했구나!\r\n지금 방송을 올리고 미션을 완료할래?\r\n\r\n#e[완료 보상]#n\r\n#r#e#i4310312# #z4310312# " + pinkbean + "개", 4, 9062570);
	} else if (status == 1) {
		cm.getPlayer().AddCoin(100828, pinkbean, true);
		cm.getClient().setCustomKeyValue(quest.getId(), "state", "2");
		cm.sendOkS("#b#e< " + (quest.getName().replaceAll("\\]", "").replaceAll("\\[", "").replaceAll("메이플 LIVE ", "").replaceAll("완료 ", "")) + ">#n#k 방송을 올렸어!", 4, 9062570);
		cm.dispose();
	}

}

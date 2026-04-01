importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.server.quest);
var status = -1;
var blackbean = 0, upcount = 0;
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
		quest = MapleQuest.getInstance((501471 + sels));

		if (quest.getId() <= 501474) {
			blackbean = 5;
			upcount = 5;
		} else if (quest.getId() <= 501478) {
			blackbean = 10;
			upcount = 10;
		} else if (quest.getId() <= 501480) {
			blackbean = 20;
			upcount = 20;
		} else if (quest.getId() <= 501484) {
			blackbean = 30;
			upcount = 30;
		} else if (quest.getId() <= 501488) {
			blackbean = 40;
			upcount = 40;
		} else if (quest.getId() <= 501491) {
			blackbean = 60;
			upcount = 60;
		} else if (quest.getId() <= 501496) {
			blackbean = 70;
			upcount = 70;
		}
		cm.sendYesNoS("#b#e< " + (quest.getName().replaceAll("\\]", "").replaceAll("\\[", "").replaceAll("메이플 LIVE ", "").replaceAll("완료 ", "")) + ">#n#k 방송 촬영을 완료했군\r\n지금 방송을 올리고 미션을 완료할거야?\r\n\r\n#e[완료 보상]#n\r\n#r#e#i4310313# #z4310313# " + blackbean + "개\r\n#i4310314# #z4310314# " + upcount + "명", 4, 9062558);
	} else if (status == 1) {
		if ((cm.getClient().getCustomKeyValue(501470, "weeklyF")) >= 400) {
			cm.sendOkS("어라!? 이번주는 충분히 #r#e방송#n#k을 올린 것 같군? 다음주 목요일에 다시 오라고!", 4, 9062558);
			cm.dispose();
			return;
		}
		cm.getPlayer().AddCoinAcc(501468, blackbean);
		cm.getClient().setCustomKeyValue(quest.getId(), "state", "2");
		cm.getClient().setCustomKeyValue(501501, "count", (cm.getClient().getCustomKeyValue(501501, "count") + 1) + "");
		if ((cm.getClient().getCustomKeyValue(501470, "weeklyF") + upcount) > 400) {
			upcount = (400 - cm.getClient().getCustomKeyValue(501470, "weeklyF"));
		}
		cm.getClient().setCustomKeyValue(501470, "follower", (cm.getClient().getCustomKeyValue(501470, "follower") + upcount) + "");
		cm.getClient().setCustomKeyValue(501470, "weeklyF", (cm.getClient().getCustomKeyValue(501470, "weeklyF") + upcount) + "");
		if (cm.getClient().getCustomKeyValue(501470, "weeklyF") >= 400) {
			cm.getClient().setCustomKeyValue(501470, "weeklyF", "400");
		}

		if (cm.getClient().getCustomKeyValue(501470, "grade") < 1 && cm.getClient().getCustomKeyValue(501470, "follower") > 6000) {
			cm.getClient().setCustomKeyValue(501470, "grade", "1");
		} else if (cm.getClient().getCustomKeyValue(501470, "grade") < 2 && cm.getClient().getCustomKeyValue(501470, "follower") > 12000) {
			cm.getClient().setCustomKeyValue(501470, "grade", "2");
		}
		cm.sendOkS("#b#e< " + (quest.getName().replaceAll("\\]", "").replaceAll("\\[", "").replaceAll("메이플 LIVE ", "").replaceAll("완료 ", "")) + ">#n#k 방송을 올렸어. 크크. 구독자가 점점 늘어나는군?", 4, 9062558);
		cm.dispose();
	}

}

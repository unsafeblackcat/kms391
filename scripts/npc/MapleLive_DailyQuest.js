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
		quest = MapleQuest.getInstance((100828 + sels));
		if (sels == 0) {
			quest = MapleQuest.getInstance(100829);
		} else if (sels == 1) {
			quest = MapleQuest.getInstance(100852);
		} else if (sels == 24) {
			quest = MapleQuest.getInstance(100853);
		}
		if (quest.getId() <= 100836 || quest.getId() == 100850 || quest.getId() == 100852) {
			pinkbean = 50;
			upcount = 50;
		} else if (quest.getId() <= 100849) {
			pinkbean = 20;
			upcount = 20;
		} else if (quest.getId() == 100851 || quest.getId() == 100853) {
			pinkbean = 100;
			upcount = 100;
		}
		cm.sendYesNoS("#b#e< " + (quest.getName().replaceAll("\\]", "").replaceAll("\\[", "").replaceAll("메이플 LIVE ", "").replaceAll("완료 ", "")) + ">#n#k 방송 촬영을 완료했구나!\r\n지금 방송을 올리고 미션을 완료할래?\r\n\r\n#e[완료 보상]#n\r\n#r#e#i4310312# #z4310312# " + pinkbean + "개\r\n#i4310314# #z4310314# " + upcount + "명", 4, 9062570);
	} else if (status == 1) {
		if (cm.getPlayer().getKeyValue(100828, "lock") == 1) {
			cm.sendOkS("어라!? 오늘은 충분히 #r#e방송#n#k을 올린 것 같은데? 내일 다시 와줘~!", 4, 9062570);
			cm.dispose();
			return;
		}
		cm.getPlayer().AddCoin(100828, pinkbean);
		cm.getPlayer().setKeyValue(quest.getId(), "state", "2");
		cm.getClient().setCustomKeyValue(501497, "count", (cm.getClient().getCustomKeyValue(501497, "count") + 1) + "");
		if ((cm.getClient().getCustomKeyValue(501470, "dailyF")) < 300) {
			if ((cm.getClient().getCustomKeyValue(501470, "dailyF") + upcount) > 300) {
				upcount = (300 - cm.getClient().getCustomKeyValue(501470, "dailyF"));
			}
			cm.getClient().setCustomKeyValue(501470, "follower", (cm.getClient().getCustomKeyValue(501470, "follower") + upcount) + "");
			cm.getClient().setCustomKeyValue(501470, "dailyF", (cm.getClient().getCustomKeyValue(501470, "dailyF") + upcount) + "");
			if (cm.getClient().getCustomKeyValue(501470, "dailyF") >= 300) {
				cm.getClient().setCustomKeyValue(501470, "dailyF", "300");
			}
		}

		if (cm.getClient().getCustomKeyValue(501470, "grade") < 1 && cm.getClient().getCustomKeyValue(501470, "follower") > 6000) {
			cm.getClient().setCustomKeyValue(501470, "grade", "1");
		} else if (cm.getClient().getCustomKeyValue(501470, "grade") < 2 && cm.getClient().getCustomKeyValue(501470, "follower") > 12000) {
			cm.getClient().setCustomKeyValue(501470, "grade", "2");
		}
		cm.sendOkS("#b#e< " + (quest.getName().replaceAll("\\]", "").replaceAll("\\[", "").replaceAll("메이플 LIVE ", "").replaceAll("완료 ", "")) + ">#n#k 방송을 올렸어!", 4, 9062570);
		cm.dispose();
	}

}

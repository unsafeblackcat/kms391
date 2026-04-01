var font = "#fn돋움##fs15#";
var con = [4036457, 1]; //입장재료 [아이템코드, 갯수]

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
		cm.dispose();
		return;
	}
	if (mode == 1) {
		status++;
	}

	var setting = [
		["Extreme_Black_Mage", 2, 450017000, 270]
	];
	var name = ["미쳐버린 검은 마법사"];


	switch (status) {
		case 0:
			var talk = font;
			talk += name[0] + "와 대적하기 위해 #b어둠의 신전#k으로 이동할까?\r\n\r\n";
			talk += "#L0##b어둠의 신전으로 이동한다.#r(레벨 " + setting[0][3] + "이상)#k#l\r\n";
			cm.sendSimpleS(talk, 0x26);
		break;
		case 1:
			sel = selection;
			if (cm.getParty() == null) {
				var talk = font;
				talk += "1인 이상의 파티를 맺어야만 입장할 수 있습니다.\r\n";
				cm.sendOkS(talk, 0x26);
				cm.dispose(); return;
			} else if (!cm.isLeader()) {
				var talk = font;
				talk += "파티장만이 입장을 신청할 수 있습니다.\r\n";
				cm.sendOkS(talk, 0x26);
				cm.dispose(); return;
			} else if (cm.getPlayerCount(setting[sel][2]) > 0) {
				var talk = font;
				talk += "이미 누군가가 " + name[sel] + "에 도전하고 있습니다.\r\n";
				talk += "잠시만 기다려 주시거나 다른 채널을 이용해 주세요.\r\n";
				cm.sendOkS(talk, 0x26);
				cm.dispose(); return;
			} else if (!cm.allMembersHere()) {
				var talk = font;
				talk += "모든 파티원이 같은 장소에 있어야 합니다.\r\n";
				cm.sendOkS(talk, 0x26);
				cm.dispose(); return;
			}

			if (checkBossLimit(con[0], con[1])) {
				giveBossLimit(con[0], -con[1]);
			} else {
				if (!cm.isBossAvailable(setting[sel][0], setting[sel][1])) {
					cm.sendOkS(font + cm.isBossString(setting[sel][0]), 0x04, 9010061);
					cm.dispose(); return;
				} else if (!cm.isLevelAvailable(setting[sel][3])) {
					var talk = font;
					talk += "파티원 중 ";
					var chrList = cm.LevelNotAvailableChrList(setting[sel][3]);
					for (var i = 0; i < chrList.length; i++) {
						if (i != 0) {
							talk += ", ";
						}
						talk += "#b#e" + chrList[i];
					}
					talk += "#k#n님의 레벨이 부족합니다. ";
					talk += name[sel] + "는 " + setting[sel][3] + "레벨 이상만 입장 가능합니다.\r\n";
					cm.sendOkS(talk, 0x26);
					cm.dispose(); return;
				}
			}
			cm.addBoss(setting[sel][0]);
			var em = cm.getEventManager(setting[sel][0]);
			if (em != null) {
				em.startInstance_Party(String(setting[sel][2]), cm.getPlayer());
			} else {
				var talk = font;
				talk += "지금은 " + name[sel] + "와 대적할 수 없습니다.\r\n";
				cm.sendOkS(talk, 0x26);
			}
			cm.dispose();
		break;
	}
}

function checkBossLimit(itemID, number) {
	var check = true;
	var party = cm.getParty();
	var mem = party.getMembers();
	var it = mem.iterator();
	while (it.hasNext()) {
		var partyMember = it.next();
		if (partyMember != null) {
			var chr = cm.getChannelServer().getPlayerStorage().getCharacterById(partyMember.getId());
			if (chr != null) {
				if (!chr.haveItem(itemID, number) || chr.itemQuantity(itemID) < number) {
					check = false;
				}
			}
		}
	}
	return check;
}

function giveBossLimit(itemID, number) {
	var party = cm.getParty();
	var mem = party.getMembers();
	var it = mem.iterator();
	while (it.hasNext()) {
		var partyMember = it.next();
		if (partyMember != null) {
			var chr = cm.getChannelServer().getPlayerStorage().getCharacterById(partyMember.getId());
			if (chr != null) {
				chr.removeItem(itemID, number);
				//chr.gainItem(itemID, number);
				//chr.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.InfoPacket.getShowItemGain(itemID, number, true));
			}
		}
	}
}

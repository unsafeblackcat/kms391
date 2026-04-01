var status;
importPackage(Packages.server);
importPackage(Packages.client.inventory);
sum = 0;
sum2 = 0;
function start() {
	status = -1;
	action(1, 1, 0);
}
코인 = "#fUI/Basic.img/theblackcoin/23#";
회색 = "#fc0xFFBDBDBD#"
검정 = "#fc0xFF191919#"

function action(mode, type, selection) {
	itemid = 4310005; // 스톤 조각
	개수 = Randomizer.rand(3, 6);
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		말 = "#fs15##b#z" + itemid + "##k #r15개#k로 " + 검정 + "차원의 스톤 제작#k이 가능하네.\r\n";
		말 += "#i4310000# " + 검정 + "#z4310000# #b제작 확률#k : #r50%#k\r\n";
		말 += "#i4310001# " + 검정 + "#z4310001# #b제작 확률#k : #r20%#k\r\n";
		말 += "#i4310002# " + 검정 + "#z4310002# #b제작 확률#k : #r10%#k\r\n";
		말 += "#i4310003# " + 검정 + "#z4310003# #b제작 확률#k : #r5%#k\r\n";
		말 += "#i4310004# " + 검정 + "#z4310004# #b제작 확률#k : #r3%#k\r\n";
		말 += "" + 회색 + "※ 제작 실패 시, #z" + itemid + "# 3~6개가 랜덤으로 지급됩니다.\r\n"
		말 += "#L0##b스톤을 제작하겠습니다.\r\n";
		cm.sendSimpleS(말, 0x04, 2192030);
	}
	if (status == 1) {
		if (selection == 0) {
			leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
			if (leftslot < 2) {
				cm.sendOkS("#fs15##r기타창 2칸 이상을 확보해두게.", 0x04, 2192030);
				cm.dispose();
				return;
			}
			if (!cm.haveItem(4310005, 15)) {
				cm.sendOkS("#r#i4310005##z4310005#이 부족하네.", 0x04, 2192030);
				cm.dispose();
				return;
			}
			if (Randomizer.isSuccess(60)) {
				lv1 = 4310000;
				cm.sendOkS("#fs15#차원의 스톤 제작에 성공했네!\r\n\r\n#k#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i" + lv1 + "# #b#z" + lv1 + "# ", 0x04, 2192030);
				cm.gainItem(lv1, 1);
			} else if (Randomizer.isSuccess(20)) {
				lv2 = 4310001;
				cm.sendOkS("#fs15#차원의 스톤 제작에 성공했네!\r\n\r\n#k#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i" + lv2 + "# #b#z" + lv2 + "# ", 0x04, 2192030);
				cm.gainItem(lv2, 1);
			} else if (Randomizer.isSuccess(5)) {
				lv3 = 4310002;
				cm.sendOkS("#fs15#차원의 스톤 제작에 성공했네!\r\n\r\n#k#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i" + lv3 + "# #b#z" + lv3 + "# ", 0x04, 2192030);
				cm.gainItem(lv3, 1);
			} else if (Randomizer.isSuccess(2)) {
				lv4 = 4310003;
				cm.sendOkS("#fs15#차원의 스톤 제작에 성공했네!\r\n\r\n#k#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i" + lv4 + "# #b#z" + lv4 + "# ", 0x04, 2192030);
				cm.gainItem(lv4, 1);
			} else if (Randomizer.isSuccess(1)) {
				lv5 = 4310004;
				cm.sendOkS("#fs15#차원의 스톤 제작에 성공했네!\r\n\r\n#k#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i" + lv5 + "# #b#z" + lv5 + "# ", 0x04, 2192030);
				cm.gainItem(lv5, 1);
			} else {
				stone = 4310005;
				cm.sendOkS("#fs15##r차원의 스톤 제작에 실패했다네..#k\r\n\r\n#b#i" + stone + "# #z" + stone + "##k " + 개수 + "개가 지급되었습니다.", 0x04, 2192030);
				cm.gainItem(stone, 개수);
			}
			cm.gainItem(4310005, -15);
			//cm.dispose();
		}
	}
}
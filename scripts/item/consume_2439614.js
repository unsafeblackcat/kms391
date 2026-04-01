importPackage(java.lang);

var enter = "\r\n";
var seld = -1;

var need = 2439614, qty = 1;

var items = [1404022, 1222122, 1232122, 1242139, 1242141, 1262051, 1272040, 1282040, 1292022, 1302355, 1312213, 1322264, 1332289, 1362149, 1372237, 1382274, 1402268, 1412189, 1422197, 1432227, 1442285, 1452266, 1462252, 1472275, 1482232, 1492245, 1522152, 1532157, 1582044, 1592022, 1213022];

var pot = [
	{'name' : "攻擊力 +12%", 'code' : 40051},
	{'name' : "魔力 +12%", 'code' : 40052},
	{'name' : "BOSS攻擊時傷害 +40%", 'code' : 40603},
	{'name' : "怪物防禦力無視 +40%", 'code' : 40292},
	{'name' : "道具取得機率 +20%", 'code' : 40656}
]
var a = 0;
var pots = [-1, -1, -1, -1, -1, -1];
var potn = ["", "", "", "", "", ""];
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
		var msg = "#fs15##e#b[創世武器發放]#k#n\r\n\r\n#r請選擇要領取的創世武器。#k"+enter;
		for (i = 0; i < items.length; i++)
			msg += "#L"+i+"##i"+items[i]+"##z"+items[i]+"#"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		selditem = items[seld];
		var msg = "#fs15##e#r[選擇的武器屬性如下]#n#k"+enter;
		msg += "道具 : #b#i"+selditem+"##z"+selditem+"# 1個#k"+enter;
		msg += "全屬性 : #b+500#k"+enter;
		msg += "攻擊力 : #b+700#k"+enter;
		msg += "魔　力 : #b+700#k"+enter;
		msg += "BOSS攻擊時傷害 : #b+50%#k"+enter;
		msg += "怪物防禦力無視 : #b+30%#k"+enter;
		msg += "星力 #b25階#k 强化適用"+enter;
		msg += "請確認選擇的武器是否正確。";
		cm.sendYesNo(msg);
	} else if (status >= 2 && status <= 7) {
		if (status > 2) {
			pots[status - 3] = pot[sel]['code'];
			potn[status - 3] = pot[sel]['name'];
		}

		var msg = "#fs15#第" + (status - 1) + "個潛在能力請選擇。#b"+enter;
		for (i = 0; i < 5; i++) 
			msg += "#L"+i+"#"+pot[i]['name']+enter;
		cm.sendSimple(msg);
	} else if (status == 8) {
		pots[5] = pot[sel]['code'];
		potn[5] = pot[sel]['name'];
		var msg = "#fs15##r#e請確認選擇的潛在能力是否正確。\r\n#n#k"+enter;
		for (i = 0; i < 6; i++) 
			msg += "#b"+potn[i]+"#k"+enter;
		msg += "#r#e\r\n若要重新設定，請按'否'，若要領取，請按'是'。#n"+enter;
		cm.sendYesNo(msg);
	} else if (status == 9) {
		item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(selditem);
		item.setStr(500);
		item.setDex(500);
		item.setInt(500);
		item.setLuk(500);
		item.setWatk(700);
		item.setMatk(700);
		item.setState(20);
		item.setLevel(item.getUpgradeSlots());
		item.setBossDamage(50);
		item.setIgnorePDR(30);
		item.setUpgradeSlots(0);
		item.setEnhance(25);
		item.setPotential1(pots[0]);
		item.setPotential2(pots[1]);
		item.setPotential3(pots[2]);
		item.setPotential4(pots[3]);
		item.setPotential5(pots[4]);
		item.setPotential6(pots[5]);
		cm.gainItem(need, -qty);
		Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, true);
	        cm.getPlayer().changeSingleSkillLevel(Packages.client.SkillFactory.getSkill(80002632), 1, 1);
	        cm.getPlayer().changeSingleSkillLevel(Packages.client.SkillFactory.getSkill(80002633), 1, 1);
		cm.sendOk("#fs15#創世武器發放完成。");
		cm.dispose();
	}
}

importPackage(java.lang);

var enter = "\r\n";
var seld = -1;

var need = 2632147, qty = 1;

var items = [1114318];

var pot = [
	{'name' : " STR +9%", 'code' : 30041},
	{'name' : " INT +9%", 'code' : 30043},
	{'name' : " LUK +9%", 'code' : 30044},
	{'name' : " DEX +9%", 'code' : 30042},
	{'name' : "올스텟 +6%", 'code' : 30086}
]
var a = 0;
var pots = [-1, -1, -1, -1];
var potn = ["", "", "", ""];
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
		var msg = "#e#b[어웨이크 링 지급]#k#n\r\n\r\n#r어웨이크 링를 선택해주세요.#k"+enter;
		for (i = 0; i < items.length; i++)
			msg += "#L"+i+"##i"+items[i]+"##z"+items[i]+"#"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		selditem = items[seld];
		var msg = "#e#r[선택하신 반지의 정보입니다.]#n#k"+enter;
		msg += "아이템 : #b#i"+selditem+"##z"+selditem+"# 1개#k"+enter;
		msg += "올스탯 : #b+70#k"+enter;
		msg += "공격력 : #b+50#k"+enter;
		msg += "마　력 : #b+50#k"+enter;
		msg += "선택하신 반지가 맞으신지 확인해주세요.";
		cm.sendYesNo(msg);
	} else if (status >= 2 && status <= 4) {
		if (status > 2) {
			pots[status - 3] = pot[sel]['code'];
			potn[status - 3] = pot[sel]['name'];
		}

		var msg = (status - 1) + "번째 잠재능력을 선택해주세요.#b"+enter;
		for (i = 0; i < 5; i++) 
			msg += "#L"+i+"#"+pot[i]['name']+enter;
		cm.sendSimple(msg);
	} else if (status == 5) {
		pots[2] = pot[sel]['code'];
		potn[2] = pot[sel]['name'];
		var msg = "#r#e선택하신 잠재능력이 맞는지 확인해주세요.\r\n#n#k"+enter;
		for (i = 0; i < 3; i++) 
			msg += "#b"+potn[i]+"#k"+enter;
		msg += "#r#e\r\n다시 설정 하시려면 '아니요', 지급받으시려면 '예'를 눌러주세요.#n"+enter;
		cm.sendYesNo(msg);
	} else if (status == 6) {
		item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(selditem);
		item.setStr(70);
		item.setDex(70);
		item.setInt(70);
		item.setLuk(70);
		item.setWatk(50);
		item.setMatk(50);
		item.setState(20);
		item.setLevel(item.getUpgradeSlots());
		item.setBossDamage(0);
		item.setIgnorePDR(0);
		item.setUpgradeSlots(3);
		item.setEnhance(0);
		item.setPotential1(pots[0]);
		item.setPotential2(pots[1]);
		item.setPotential3(pots[2]);
		cm.gainItem(need, -qty);
		Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, true);
		cm.sendOk("지급 완료 되었습니다.");
		cm.dispose();
	}
}
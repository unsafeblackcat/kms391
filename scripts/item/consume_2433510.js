
importPackage(java.lang);

var enter = "\r\n";
var seld = -1;

var need = 2433510, qty = 1;

var items = [1342087, 1352247,1352237,1352257,1352507,1352946,1352217,1352907,1352010,1352708,1352958,1352207,1352287,1352929,1352607,1352976,1352407,1352277,1352936,1352227,1352297,1352917,1352267,1353007,1098007,1099013,1352110,1353106,1353204,1353404,1353704,1352965,1354014,1354004,1353504,1353604,1354024,1353804];

var pot = [
	{'name' : "공격력 +12%", 'code' : 40051},
	{'name' : "마력 +12%", 'code' : 40052},
	{'name' : "보스 공격시 데미지 +40%", 'code' : 40603},
	{'name' : "몬스터 방어력 무시 +40%", 'code' : 40292},
            {'name' : "데미지 +12%", 'code' : 40070}
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
		var msg = "#e#b[후원 보조무기 무기 지급]#k#n\r\n\r\n#r지급 받으실 보조 무기를 선택해주세요.#k"+enter;
		for (i = 0; i < items.length; i++)
			msg += "#L"+i+"##i"+items[i]+"##z"+items[i]+"#"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		selditem = items[seld];
		var msg = "#e#r[선택하신 무기의 정보입니다.]#n#k"+enter;
		msg += "아이템 : #b#i"+selditem+"##z"+selditem+"# 1개#k"+enter;
		msg += "올스탯 : #b+1000#k"+enter;
		msg += "공격력 : #b+1000#k"+enter;
		msg += "마　력 : #b+1000#k"+enter;
		msg += "보스 공격시 데미지 : #b+50%#k"+enter;
		msg += "몬스터 방어력 무시 : #b+50%#k"+enter;
		msg += "데미지 : #b+50%#k"+enter;
		msg += "선택하신 보조 무기가 맞으신지 확인해주세요.";
		cm.sendYesNo(msg);
	} else if (status >= 2 && status <= 7) {
		if (status > 2) {
			pots[status - 3] = pot[sel]['code'];
			potn[status - 3] = pot[sel]['name'];
		}

		var msg = (status - 1) + "번째 잠재능력을 선택해주세요.#b"+enter;
		for (i = 0; i < 5; i++) 
			msg += "#L"+i+"#"+pot[i]['name']+enter;
		cm.sendSimple(msg);
	} else if (status == 8) {
		pots[5] = pot[sel]['code'];
		potn[5] = pot[sel]['name'];
		var msg = "#r#e선택하신 잠재능력이 맞는지 확인해주세요.\r\n#n#k"+enter;
		for (i = 0; i < 6; i++) 
			msg += "#b"+potn[i]+"#k"+enter;
		msg += "#r#e\r\n다시 설정 하시려면 '아니요', 지급받으시려면 '예'를 눌러주세요.#n"+enter;
		cm.sendYesNo(msg);
	} else if (status == 9) {
		item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(selditem);
		item.setStr(1000);
		item.setDex(1000);
		item.setInt(1000);
		item.setLuk(1000);
		item.setWatk(1000);
		item.setMatk(1000);
		item.setState(20);
		item.setEnhance(25);
		item.setLevel(item.getUpgradeSlots());
		item.setBossDamage(50);
		item.setIgnorePDR(50);
		item.setTotalDamage(50);
		item.setUpgradeSlots(0);
		item.setPotential1(pots[0]);
		item.setPotential2(pots[1]);
		item.setPotential3(pots[2]);
		item.setPotential4(pots[3]);
		item.setPotential5(pots[4]);
		item.setPotential6(pots[5]);
		cm.gainItem(need, -qty);
		Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, true);
		cm.sendOk("지급 완료 되었습니다.");
		cm.dispose();
	}
}

importPackage(java.lang);

var enter = "\r\n";
var seld = -1;
var allstat = 4190;

var need = 2439541, qty = 1;

var items = [1592999
             ,1214999
             ,1222999
             ,1232999
             ,1242998
             ,1242999
             ,1262999
             ,1272999
             ,1272999
             ,1292999
             ,1312999
             ,1322999
             ,1332999
             ,1342999
             ,1362999
             ,1372999
             ,1382999
             ,1402999
             ,1404999
             ,1422999
             ,1432999
             ,1442999
             ,1452999
             ,1462999
             ,1472999
             ,1482999
             ,1492999
             ,1522999
             ,1532999
             ,1582999
             ,1212999
             ,1213999];

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
		var msg = "#e#b하인즈 [아스칼론 무기 지급]#k#n\r\n\r\n#r지급받으실 아스칼론 무기를 선택해주세요.#k"+enter;
		for (i = 0; i < items.length; i++)
			msg += "#L"+i+"##i"+items[i]+"##z"+items[i]+"#"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		selditem = items[seld];
		var msg = "#e#r[선택하신 무기의 정보입니다.]#n#k"+enter;
		msg += "아이템 : #b#i"+selditem+"##z"+selditem+"# 1개#k"+enter;
		msg += "올스탯 : #b+" + allstat + "#k"+enter;
		msg += "공격력 : #b+" + allstat + "#k"+enter;
		msg += "마　력 : #b+" + allstat + "#k"+enter;
		msg += "보스 공격시 데미지 : #b+100%#k"+enter;
		msg += "몬스터 방어력 무시 : #b+100%#k"+enter;
		msg += "데미지 : #b+100%#k"+enter;
		msg += "스타포스 #b25성#k 강화 적용"+enter;
		msg += "선택하신 무기가 맞으신지 확인해주세요.";
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
		item.setStr(allstat);
		item.setDex(allstat);
		item.setInt(allstat);
		item.setLuk(allstat);
		item.setWatk(allstat);
		item.setMatk(allstat);
		item.setState(20);
		item.setLevel(item.getUpgradeSlots());
		item.setBossDamage(100);
		item.setIgnorePDR(100);
		item.setTotalDamage(100);
		item.setUpgradeSlots(0);
		item.setEnhance(25); //스타포스강화횟수
		item.setPotential1(pots[0]);
		item.setPotential2(pots[1]);
		item.setPotential3(pots[2]);
		item.setPotential4(pots[3]);
		item.setPotential5(pots[4]);
		item.setPotential6(pots[5]);
		cm.gainItem(need, -qty);
                        cm.gainItem(2439541,-1)
		Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, true);
	        cm.getPlayer().changeSingleSkillLevel(Packages.client.SkillFactory.getSkill(80002416), 10, 10); //앵무새
	        cm.getPlayer().changeSingleSkillLevel(Packages.client.SkillFactory.getSkill(80002632), 1, 1);
	        cm.getPlayer().changeSingleSkillLevel(Packages.client.SkillFactory.getSkill(80002633), 1, 1);
	         Packages.client.SkillFactory.getSkill(32121017).getEffect(cm.getPlayer().getSkillLevel(32121017));
            Packages.client.SkillFactory.getSkill(1221054).getEffect(cm.getPlayer().getSkillLevel(1221054));
		cm.sendOk("지급 완료 되었습니다.");
		cm.dispose();
	}
}
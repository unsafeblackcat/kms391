
importPackage(java.lang);

var enter = "\r\n";
var seld = -1;
var allstat = 4190;

var need = 2439999, qty = 1;

var items = [1143353 
		,1143354 
		,1143355
		,1143356 
		,1143357
		,1143358
		,1143359
		,1143360
		,1143361
		,1143362
		,1143363
		,1143364
		,1143365
		,1143366
		,1143367
		,1143368
		,1143369
		,1143370
		,1143371
		,1143373
		,1143374
		,1143375
		,1143376
		,1143377
		,1143378
		,1143379
		,1143380
		,1143381
		,1143382
		,1143383
		,1143384
		,1143385
		,1143386
		,1143387
		,1143388
		,1143389
		,1143390
		,1143391
		,1143392
		,1143393
		,1143394
		,1143395
		,1143396
		,1143397
		,1143398
		,1143399
		,1143400
		,1143401
		,1143402
		,1143403
		,1143404
		,1143405
		,1143406
		,1143407
		,1143408
		,1143409
		,1143410
		,1143411
		,1143412
		,1143413
		,1143414
		,1143415
		,1143416
		,1143417
		,1143418
		,1143419
		,1143420
		,1143421
		,1143422
		,1143423
		,1143424
		,1143426
		,1143427
		,1143428
		,1143429
		,1143430
		,1143431];

var pot = [
	{'name' : "공격력 +12%", 'code' : 40051},
	{'name' : "마력 +12%", 'code' : 40052},
	{'name' : "힘 +12%", 'code' : 40041},
	{'name' : "덱스 +12%", 'code' : 40042},
	{'name' : "인트 +12%", 'code' : 40043},
	{'name' : "럭 +12%", 'code' : 40044},
	{'name' : "올스텟 +20%", 'code' : 60002},
	{'name' : "보스 공격시 데미지 +40%", 'code' : 40603},
    {'name' : "크리티컬 데미지 +8%", 'code' : 40057}
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
		var msg = "#e#b아이리스 [이팩트 훈장 지급]#k#n\r\n\r\n#r지급받으실 이팩트 훈장를 선택해주세요.#k"+enter;
		for (i = 0; i < items.length; i++)
			msg += "#L"+i+"##i"+items[i]+"##z"+items[i]+"#"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		selditem = items[seld];
		var msg = "#e#r[선택하신 훈장의 정보입니다.]#n#k"+enter;
		msg += "아이템 : #b#i"+selditem+"##z"+selditem+"# 1개#k"+enter;
		msg += "올스탯 : #b+" + allstat + "#k"+enter;
		msg += "공격력 : #b+" + allstat + "#k"+enter;
		msg += "마　력 : #b+" + allstat + "#k"+enter;
		msg += "보스 공격시 데미지 : #b+100%#k"+enter;
		msg += "몬스터 방어력 무시 : #b+100%#k"+enter;
		msg += "데미지 : #b+100%#k"+enter;
		//msg += "스타포스 #b25성#k 강화 적용"+enter;
		msg += "선택하신 방어구가 맞으신지 확인해주세요.";
		cm.sendYesNo(msg);
	} /*else if (status >= 2 && status <= 7) {
		if (status > 2) {
			pots[status - 3] = pot[sel]['code'];
			potn[status - 3] = pot[sel]['name'];
		}

		var msg = (status - 1) + "번째 잠재능력을 선택해주세요.#b"+enter;
		for (i = 0; i < 9; i++) 
			msg += "#L"+i+"#"+pot[i]['name']+enter;
		cm.sendSimple(msg);
	} else if (status == 2) {
		pots[5] = pot[sel]['code'];
		potn[5] = pot[sel]['name'];
		/*var msg = "#r#e선택하신 잠재능력이 맞는지 확인해주세요.\r\n#n#k"+enter;
		for (i = 0; i < 6; i++) 
			msg += "#b"+potn[i]+"#k"+enter;
		msg += "#r#e\r\n다시 설정 하시려면 '아니요', 지급받으시려면 '예'를 눌러주세요.#n"+enter;
		cm.sendYesNo(msg);
	}*/ else if (status == 2) {
		item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(selditem);
		item.setStr(allstat);
		item.setDex(allstat);
		item.setInt(allstat);
		item.setLuk(allstat);
		item.setWatk(allstat);
		item.setMatk(allstat);
		//item.setState(20);
		item.setLevel(item.getUpgradeSlots());
		item.setBossDamage(100);
		item.setIgnorePDR(100);
		item.setTotalDamage(100);
		item.setUpgradeSlots(0);
		//item.setEnhance(25); //스타포스강화횟수
		//item.setPotential1(pots[0]);
		//item.setPotential2(pots[1]);
		//item.setPotential3(pots[2]);
		//item.setPotential4(pots[3]);
		//item.setPotential5(pots[4]);
		//item.setPotential6(pots[5]);
		cm.gainItem(need, -qty);
                        //cm.gainItem(2439549,-1)
		Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, true);
	        //cm.getPlayer().changeSingleSkillLevel(Packages.client.SkillFactory.getSkill(80002416), 10, 10); //앵무새
	        //cm.getPlayer().changeSingleSkillLevel(Packages.client.SkillFactory.getSkill(80002632), 1, 1);
	        //cm.getPlayer().changeSingleSkillLevel(Packages.client.SkillFactory.getSkill(80002633), 1, 1);
	         //Packages.client.SkillFactory.getSkill(32121017).getEffect(cm.getPlayer().getSkillLevel(32121017));
            //Packages.client.SkillFactory.getSkill(1221054).getEffect(cm.getPlayer().getSkillLevel(1221054));
		cm.sendOk("지급 완료 되었습니다.");
		cm.dispose();
	}
}
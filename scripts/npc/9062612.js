
importPackage(java.lang);
importPackage(Packages.client);

var enter = "\r\n";
var seld = -1;

var donation = [
	{'t' : "stat", 's' : " 力量 (Str)", 'q' : 50, 'price' : 15000, 'tt' : "str"},
	{'t' : "stat", 's' : "敏捷(Dex)", 'q' : 50, 'price' : 15000, 'tt' : "dex"},
	{'t' : "stat", 's' : "智力(Int)", 'q' : 50, 'price' : 15000, 'tt' : "int"},
	{'t' : "stat", 's' : "幸運(Luk)", 'q' : 50, 'price' : 15000, 'tt' : "luk"},
	{'t' : "stat", 's' : "HP(Hp)", 'q' : 1000, 'price' : 11000, 'tt' : "hp"},
	{'t' : "stat", 's' : "MP(Mp)", 'q' : 3000, 'price' : 11000, 'tt' : "mp"},
	
]

var skills = [
	["ww_magu", 2001002, "魔法防禦", 1000000, true],
	["ww_sharp",3121002, "會心之眼", 2000000, true], 
   ["ww_winb", 5121009, "最終極速", 1000000, true],
	["ww_holy", 2311003, "神聖祈禱", 2000000, true],
   ["ww_buck", 5321054, "壓制砲擊", 10000000, true],
   ["ww_crsca",1311015, "十字深鎖鏈", 4000000, true],
   ["ww_cut",  4341002, "絕殺刃", 5000000, true],
	["ww_Blessed", 400011052, "祝福之鎚", 16000000, true],
]

var skills1 = [
	["ww_tripling", 13100022, "風妖精之箭I", 150000, true],
	["ww_tripling", 99999, "三重奏追加", 100000, true]
]

var p, seld2;
var selditem;
var seldq;


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
		var msg ="#fs15##d現在 #h #的推廣點 : #r"+cm.getPlayer().getHPoint()+"P#b"+enter;
                //msg += "#L15# #d악의정수 뽑기 시스템#k (가챠)"+enter;
		//msg += "#L7# #d후원 포인트 상점#k (아이템)"+enter;
                msg += "#L2#推廣技能商店#k "+enter;
                //msg += "#L3# #d윔 배우기#k "+enter;
//msg += "#L9# #dVIP후원 스킬 상점#k "+enter;
                //msg += "#L4# #d후원 포인트 상점#k (캐시아이템 강화)"+enter;
                //msg += "#L5# #d후원 포인트 상점#k (뱃지,포켓,캐시아이템 잠재능력부여)"+enter;


		if (cm.getPlayer().getName().equals("GM"))
			msg += "#L3#디버그"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				var msg = "#b#fs15#"+enter;
				msg += " 當前 #h #的贊助積分 : #r"+cm.getPlayer().getDonationPoint()+"P#b"+enter;
				for (i = 0; i < donation.length; i++) {
					ditem = donation[i];
					if (ditem['t'].equals("stat")) msg += "購買#L"+i+"##i4001527#"+ditem['s']+" "+ditem['q']+"P  #e["+ditem['price']+"P]#n"+enter;
					else msg += "購買#L"+i+"##i"+ditem['itemid']+"##z"+ditem['itemid']+"# "+ditem['q']+" 個  #e["+ditem['price']+"P]#n"+enter;
				}
			break;
			case 2:
				var msg = "#fs15##fn나눔고딕 ExtraBold##fs15#推廣特別技能目錄#Cgray# #fs15##r(特殊技能)#k#fs15#"+enter;
				msg += "#fs15# 當前 #h #推廣點數 : #r"+cm.getPlayer().getHPoint()+"P#k"+enter;
				for (i = 0; i < skills.length; i++) {
					if (skills[i][1] == 99999) {
						msg += "#L"+i+"##s13100022# #e"+skills[i][2]+"#n 購買 #e#r("+skills[i][3]+"P)#k#n#l"+enter;
						continue;
					}
					if (skills[i][4])
						msg += "#L"+i+"##s"+skills[i][1]+"# #e"+skills[i][2]+"#n 購買 #e#r("+skills[i][3]+"P)#k#n#l"+enter;
					else if (!skills[i][4] && cm.getPlayer().isGM())
						msg += "#L"+i+"##s"+skills[i][1]+"# #e"+skills[i][2]+"#n 購買 #e#r("+skills[i][3]+"P)#k#n(GM만 보임)#l"+enter;
				}
				cm.sendSimple(msg);
			break;
			case 3:
				if (!cm.haveItem(4036804, 1)) {
					var msg = "#fs15#"
					msg += "#i4036804# #z4036804# 1個가 부족합니다"+enter;
					cm.sendSimple(msg);
					return;
				}

				cm.getPlayer().teachSkill(13101022, 30);
				cm.gainItem(4036804, -1);
				cm.getPlayer().teachSkill(13120003, 30);
				cm.getPlayer().setKeyValue(99999, "tripling", "1");
				
				var msg = "#fs15#"
				msg += "#s13120003# 윔 스킬을 습득했습니다."+enter;
				cm.sendSimple(msg);
				cm.dispose();
			break;
                        case 4:
				cm.dispose();
			        cm.openNpc(2192032);
			return;
                        case 5:
				cm.dispose();
			        cm.openNpc(2192031);
			return;
                        case 6:
				cm.dispose();
			        cm.openNpc(9000030);
			return;
                        case 7:
				cm.dispose();
			        cm.openNpc(9001046);
			return;
                        case 8:
				cm.dispose();
			        cm.openNpc(2002000);
			return;
                        case 9:
				var msg = "#fs15##fn나눔고딕 ExtraBold##fs15#贊助VIP技能製作#Cgray# #fs15##r(VIP Skill)#k#fs15#"+enter;
				msg += "當前 #h #的贊助點數 : #r"+cm.getPlayer().getDonationPoint()+"P#k#fs15#"+enter+enter;
				msg += "當前 #h #持有的追加攻擊獎勵 : #r"+cm.getPlayer().getKeyValue(99999, "triplingBonus")+" 次#k#fs15#"+enter+enter;
				msg += "黃金楓葉可通過#b邪念精華扭蛋#k獲取."+enter;
				msg += "贊助三重鞭撻技能與普通鞭撻不同 #r100%概率#k 生效，並可額外新增箭矢數量."+enter;
				msg += "注意：三重鞭撻技能可重複購買."+enter;
				for (i = 0; i < skills1.length; i++) {
					if (skills1[i][1] == 99999) {
						msg += "#L"+i+"##s13100022# #e"+skills1[i][2]+"#n 購買 #e#r("+skills1[i][3]+"P + #i4001168# 10個)#k#n#l"+enter;
						continue;
					}
					if (skills1[i][4])
						msg += "#L"+i+"##s"+skills1[i][1]+"# #e"+skills1[i][2]+"#n 製作 #e#r("+skills1[i][3]+"P + #i4001168# 10個)#k#n#l"+enter;
					else if (!skills1[i][4] && cm.getPlayer().isGM())
						msg += "#L"+i+"##s"+skills1[i][1]+"# #e"+skills1[i][2]+"#n 購買 #e#r("+skills1[i][3]+"P)#k#n(GM만 보임)#l"+enter;
				}
                                                msg += "#L100##s13100022# #e#n 추가타 제거(1타) #e#r#k#n#l"+enter;
				cm.sendSimple(msg);
			break;
                        case 15:
				cm.dispose();
			        cm.openNpc(1540326);
			return;
			case 99:
				buffs = ["ww_buck", "ww_crsca", "ww_magu", "ww_holy", "ww_sharp", "ww_winb", "ww_final"];
				skills = [5321054, 1311015, 2001002, 2311003, 3121002, 5121009, 4341002];
				for (i = 0; i < buffs.length; i++) {
                		     if (cm.getPlayer().getKeyValue(51384, buffs[i]) > 0 && !cm.hasDonationSkill(skills[i])) {
               			         cm.gainDonationSkill(skills[i]);
                		     }
                                }
				cm.sendOk("복구가 완료되었습니다.");
				cm.dispose();
			return;
		}
		cm.sendSimple(msg);
	} else if (status == 2) {
		seld2 = sel;
		switch (seld) {
			case 1:
				p = cm.getPlayer().getHPoint();
				selditem = donation[sel];
				sad = "홍보";
				if (p < selditem['price']) {
					cm.sendOk("#fs15# 홍보포인트가 부족합니다.");
					cm.dispose();
					return;
				}
				var msg = "";
				if (selditem['t'].equals("stat")) {
					msg += "#fUI/StatusBar.img/BtClaim/normal/0#選擇的物品是 #b"+selditem['s']+" "+selditem['q']+"P#k ."+enter;
					msg += "#fs15##fUI/StatusBar.img/BtClaim/normal/0#想購買幾次?";
					cm.sendGetNumber(msg, 1, 1, 100);
				} else {
					msg += "선택하신 아이템은 #i"+selditem['itemid']+"##b#z"+selditem['itemid']+"# "+selditem['q']+"個#k 입니다."+enter;
					msg += "#fs15#가격은 #b"+selditem['price']+"P#k이고 현재 #b#h #님의 "+sad+"포인트는 "+p+"P#k입니다."+enter;
					msg += "몇 個 구매하시겠습니까?";
					cm.sendGetNumber(msg, 1, 1, 100);
				}
			break;
			case 2:
				if (cm.getPlayer().getHPoint() < skills[seld2][3]) {
					cm.sendOk("후원 포인트가 모자란 것 같습니다.");
					cm.dispose();
					return;
				}

				var view = skills[seld2][1];

				if (view == 99999) {
					view = 13100022;
				}

				cm.sendYesNo("#fUI/StatusBar.img/BtClaim/normal/0##fs15#確定要購買 #s"+view+"# #e"+skills[seld2][2]+"#n碼?");
			break;
			case 9:
if (seld2 == 100) {
   cm.sendYesNo("정말로 추가타를 제거하시겠습니까?");
} else {


				if (cm.getPlayer().getDonationPoint() < skills1[seld2][3]) {
					cm.sendOk("후원 포인트가 모자란 것 같습니다.");
					cm.dispose();
					return;
				}

				var view = skills1[seld2][1];

				if (view == 99999) {
					view = 13100022;
				}

				cm.sendYesNo("정말로 #s"+view+"# #e"+skills1[seld2][2]+"#n을(를) 구매하시겠습니까?");
}
			break;
			case 3:
				switch (seld2) {
					case 1:
						if (!cm.getPlayer().getName().equals("혀구")) {
							return;
						}
						var msg = "얼마";
						cm.sendGetNumber(msg, 1, 1, 10000000);
					break;
				}
			break;
		}
	} else if (status == 3) {
		switch (seld) {
			case 1:
				if (p < selditem['price']) {
					cm.sendOk("#fs15#  후원포인트가 부족합니다.");
					cm.dispose();
					return;
				}

				if (selditem['t'].equals("item")) {
					seldq = sel;
					var msg = "선택하신 아이템은 #i"+selditem['itemid']+"##b#z"+selditem['itemid']+"# "+(selditem['q'] * seldq)+"個#k 입니다."+enter;
					msg += "#fs15#가격은 #b"+(selditem['price'] * seldq)+"P#k이고 현재 #b#h #님의 "+sad+"포인트는 "+p+"P#k입니다."+enter;
					msg += "정말 구매하시겠습니까?";
					cm.sendYesNo(msg);
				} else {
					seldq = sel;
					var msg = "선택하신 아이템은 #b"+selditem['s']+" "+selditem['q']+"P * "+seldq+"#k 입니다."+enter;
					msg += "#fs15#가격은 #b"+(selditem['price'] * seldq)+"P#k이고 현재 #b#h #님의 "+sad+"포인트는 "+p+"P#k입니다."+enter;
					msg += "정말 구매하시겠습니까?";
					cm.sendYesNo(msg);
						
				}


			break;
			case 2:
				if (cm.getPlayer().getHPoint() < skills[seld2][3]) {
					cm.sendOk("후원 포인트가 모자란 것 같습니다.");
					cm.dispose();
					return;
				}
				if (cm.hasDonationSkill(skills[seld2][1])) {
					cm.sendOk("이미 구매하신 스킬입니다.");
					cm.dispose();
					return;
				}
				cm.getPlayer().gainHPoint(-skills[seld2][3]);
				if (skills[seld2][1] == 3101009 || skills[seld2][1] == 13100022) {
					cm.getPlayer().teachSkill(skills[seld2][1], 30);
                                        cm.getPlayer().teachSkill(13120003, 30);
				}
				if (skills[seld2][1] != 99999) {
					cm.gainDonationSkill(skills[seld2][1]);
					cm.getPlayer().dropMessage(5, "" + skills[seld2][1]);
				} else {
					var bonusAttack = cm.getPlayer().getKeyValue(99999, "triplingBonus");

					if (bonusAttack == -1) {
						cm.getPlayer().setKeyValue(99999, "triplingBonus", "0");
					}
					
					cm.getPlayer().setKeyValue(99999, "triplingBonus", (cm.getPlayer().getKeyValue(99999, "triplingBonus") + 1) + "");
				}

				var view = skills[seld2][1];

				if (view == 99999) {
					view = 13100022;
				}

				cm.sendOk("#s"+view+"# #e"+skills[seld2][2]+"#n 스킬을 성공적으로 지급했습니다.1");
				cm.dispose();
			break;

                        case 9:
 if (seld2 == 100) {
                                               if (cm.getPlayer().getKeyValue(99999, "triplingBonus") > 0) {
                                                  cm.getPlayer().setKeyValue(99999, "triplingBonus", "" + (cm.getPlayer().getKeyValue(99999, "triplingBonus") - 1));
					cm.sendOk("추가타를 제거하였습니다. 현재 추가타 : " + cm.getPlayer().getKeyValue(99999, "triplingBonus"));
					cm.dispose();
					return;
                                               } else {
					cm.sendOk("추가타가 모자란 것 같습니다.");
					cm.dispose();
					return;
 }
}
				/*if (cm.hasDonationSkill(skills1[seld2][1])) {
      cm.getPlayer().setKeyValue(99999, "tripling", "1");
					cm.sendOk("이미 구매하신 스킬입니다.");
					cm.dispose();
					return;
				}*/
				if (cm.getPlayer().getDonationPoint() < skills1[seld2][3]) {
					cm.sendOk("후원 포인트가 모자란 것 같습니다.");
					cm.dispose();
					return;
				}
                                if (cm.itemQuantity(4001168) < 10){
					cm.sendOk("#i4001168# #z4001168#이 모자란 것 같습니다.");
					cm.dispose();
					return;
				}
 				cm.getPlayer().gainDonationPoint(-skills1[seld2][3]);
                                if (cm.haveItem(4001168, 10)) {
                                cm.gainItem(4001168, -10);
                                 } else {		         
		 cm.sendOk("아니야..");
		 cm.dispose();	
		 }
				
				if (skills1[seld2][1] != 99999) {
					cm.gainDonationSkill(skills1[seld2][1]);
                                         cm.getPlayer().setKeyValue(99999, "tripling", "1");
										 cm.getPlayer().setKeyValue(99999, "triplingBonus", "0");
				} else {
					
					
					cm.getPlayer().setKeyValue(99999, "triplingBonus", (cm.getPlayer().getKeyValue(99999, "triplingBonus") + 1) + "");
				}

				var view = skills1[seld2][1];

				if (view == 99999) {
					view = 13100022;
				}
             cm.getPlayer().setKeyValue(99999, "tripling", "1");
				cm.sendOk("#s"+view+"# #e"+skills1[seld2][2]+"#n 스킬을 성공적으로 지급했습니다.");
				cm.dispose();
			break;

			case 3:
				switch (seld2) {
					case 1:
						if (!cm.getPlayer().getName().equals("혀구")) {
							return;
						}
						cm.getPlayer().gainDonationPoint(sel);
						cm.sendOk("완료");
						cm.dispose();
					break;
				}
			break;
		}
	} else if (status == 4) {
		switch (seld) {
			case 1:
				if (seldq < 0) {
					cm.sendOk("오류입니다.");
					cm.writeLog("Log/음수값.log", cm.getPlayer().getName()+"가 엔피시 ID "+cm.getNpc()+"에서 음수값을 사용하려고 함.\r\n", true);
					cm.dispose();
					return;
				}
				if (p < selditem['price'] * seldq) {
					cm.sendOk("#fs15#  후원포인트가 부족합니다.");
					cm.dispose();
					return;
				}
				if (selditem['t'].equals("stat")) {
					gainStat(selditem['tt'], (selditem['q'] * seldq));
					cm.getPlayer().gainDonationPoint(-(selditem['price'] * seldq));
					cm.sendOk("거래가 완료되었습니다.");
					cm.dispose();
				} else {
					cm.gainItem(selditem['itemid'], (selditem['q'] * seldq));
					cm.getPlayer().gainDonationPoint(-(selditem['price'] * seldq));
					cm.sendOk("거래가 완료되었습니다.");
					cm.dispose();
				}
			break;
		}
	}
}

function gainStat(ty, q) {
	stat = cm.getPlayer().getStat();
	var st;
	var sq = 0;
	switch (ty) {
		case "str":
			st = MapleStat.STR;
			sq = Integer.parseInt(stat.getStr() + q);
			if (sq > 32767) {
				cm.sendOk("이 스탯은 구매할 수 없습니다.");
				cm.dispose();
				return;
			}
			stat.setStr(sq, cm.getPlayer());
		break;
		case "dex":
			st = MapleStat.DEX;
			sq = Integer.parseInt(stat.getDex() + q);
			if (sq > 32767) {
				cm.sendOk("이 스탯은 구매할 수 없습니다.");
				cm.dispose();
				return;
			}
			stat.setDex(sq, cm.getPlayer());
		break;
		case "int":
			st = MapleStat.INT;
			sq = Integer.parseInt(stat.getInt() + q);
			if (sq > 32767) {
				cm.sendOk("이 스탯은 구매할 수 없습니다.");
				cm.dispose();
				return;
			}
			stat.setInt(sq, cm.getPlayer());
		break;
		case "luk":
			st = MapleStat.LUK;
			sq = Integer.parseInt(stat.getLuk() + q);
			if (sq > 32767) {
				cm.sendOk("이 스탯은 구매할 수 없습니다.");
				cm.dispose();
				return;
			}
			stat.setLuk(sq, cm.getPlayer());
		break;
		case "hp":
			st = MapleStat.MAXHP;
			sq = Integer.parseInt(stat.getMaxHp() + q);
			if (sq > 500000) {
				cm.sendOk("이 스탯은 구매할 수 없습니다.");
				cm.dispose();
				return;
			}
			stat.setMaxHp(sq, cm.getPlayer());
		break;
		case "mp":
			st = MapleStat.MAXMP;
			sq = Integer.parseInt(stat.getMaxMp() + q);
			if (sq > 500000) {
				cm.sendOk("이 스탯은 구매할 수 없습니다.");
				cm.dispose();
				return;
			}
			stat.setMaxMp(sq, cm.getPlayer());
		break;
	}
	cm.getPlayer().updateSingleStat(st, sq);
}
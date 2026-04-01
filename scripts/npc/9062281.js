importPackage(Packages.server);
importPackage(Packages.database);
importPackage(Packages.client);
importPackage(java.lang);

var enter = "\r\n";
var seld = -1;
var seldreward = -1;
var seld2 = -1;
var seld3 = -1;

var name, comment, etc;
var donation;
var firstdon = false;

var year, month, date2, date, day

var reward = 0;
var modify = "";
var modifychr;
var seldgrade = 0;

var nreward = [ // 여기서 수정 
    {'ngrade' : 0, 'items' : [[4310024, 1],], 'select' : false}, // 일반
	{'ngrade' : 1, 'items' : [[2435795, 1],], 'select' : false}, // A
	{'ngrade' : 2, 'items' : [[2435796, 1],], 'select' : false}, // B
	{'ngrade' : 3, 'items' : [[2435797, 1],], 'select' : false}, // C
	{'ngrade' : 4, 'items' : [[2435798, 1],], 'select' : false}, // D
	{'ngrade' : 5, 'items' : [[2435799, 1],], 'select' : false}, // E
	{'ngrade' : 6, 'items' : [[2435814, 1],], 'select' : false}, // F
	{'ngrade' : 7, 'items' : [[2435815, 1],], 'select' : false}, // S
	{'ngrade' : 8, 'items' : [[2435816, 1],], 'select' : false}, // SS
	{'ngrade' : 9, 'items' : [[2435817, 1],], 'select' : false}, // SSS
	{'ngrade' : 10, 'items' : [[2435818, 1],], 'select' : false} // SSS+
	//{'ngrade' : 11, 'items' : [[2435819, 1],], 'select' : false}, // 로얄
	//{'ngrade' : 12, 'items' : [[2631756, 1],], 'select' : false} // 챌린져
	//{'ngrade' : 13, 'items' : [[2432206, 1],], 'select' : false} // 엔드
] // ngrade는 밑에 grade

var grade = [
	[0, "일반"],
	[1, "A"],
	[2, "B"],
	[3, "C"],
	[4, "D"],
	[5, "E"],
	[6, "F"],
	[7, "S"],
	[8, "SS"],
	[9, "SSS"],
	[10, "SSS+"],
	//[11, "로얄"],
//[12, "챌린져"],
	//[13, "엔드"]
]

var daily;

var daily_1 = [
	//[1149990, 1],  //훈장
	//[5060048, 5], // 골드애플 5게


] // A등급

var daily_2 = [
	//[1149991, 1],  //훈장
] // 다이아

var daily_3 = [
	//[1149992, 1],  //훈장
] // 핑크

var daily_4 = [
	//[1149993, 1],  //훈장
] // 퍼플

var daily_5 = [
	//[1149994, 1],  //훈장
] // 블루

var daily_6 = [
//	[1149905, 1],  //훈장
] // 레드

var daily_7 = [
//	[1149996, 1],  //훈장
] // 블랙

var daily_8 = [
//	[1149997, 1],  //훈장
] // 마스터

var daily_9 = [
	//[1149998, 1],  //훈장
] // 로얄

var daily_10 = [
//	[1149999, 1],  //훈장
] // 챌린져



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
		getData();
		var msg = "#fs15#안녕하세요. #eCOIN#n 후원포인트 지급 도우미 릴리입니다.\r\n"+enter;
		msg += "현재 #b#h ##k님의 후원 포인트 : #b"+cm.getPlayer().getDonationPoint()+"P#k"+enter;
		msg += "현재 #b#h ##k님의 COIN 등급 : #b"+cm.getPlayer().getHgrades()+"#k#b"+enter;
		msg += "#L1#후원포인트 수령하기"+enter;
		msg += "#L100#후원 검색 캐시"+enter;
		msg += "#L2#후원포인트 상점"+enter;
		msg += "#L20#후원스킬 상점"+enter;
		msg += "#L10#잠재능력 추가"+enter;
            	msg += "#L5#등급 달성보상 수령하기"+enter;
		if (cm.getPlayer().getHgrade() >= 3)
		msg += "#L6#데일리 팩 지급받기(mvp골드 부터 지급) "+enter;
		if (cm.getPlayer().isGM() || cm.getPlayer().getKeyValue(98199, "gm_check") > 0) {
			msg += "#L3#후원포인트 지급하기"+enter;
			msg += "#L4#후원 유저 관리하기"+enter;
			msg += "#L7#후원 유저 등급 전부보기"+enter;
			msg += "#L8#후원 유저 오류등급 전부보기"+enter;
		}
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				cm.sendSimple(getList());
			break;
			case 2:
				cm.dispose();
                  cm.openNpcCustom(cm.getClient(), 1540101, "1540101");
			break;
			case 3:
				if (!cm.getPlayer().isGM())
					return;
				cm.sendGetText("지급 대상의 닉네임을 적어주세요.");
			break;
			case 4:
				if (!cm.getPlayer().isGM())
					return;
				var msg = "원하시는 항목을 선택해주세요.#b"+enter;
				msg += "#L1#지급된 후원포인트 삭제"+enter;
				msg += "#L2#유저의 등급 조정";
				cm.sendSimple(msg);
			break;
			case 5:
				var msg = "현재 #b#h ##k님의 COIN 등급은 #b"+cm.getPlayer().getHgrades()+"#k입니다."+enter;
				msg += "받을 수 있는 보상 중 안받은 보상의 내역을 보여드리겠습니다."+enter;
				var a = "";
				for (i = 0; i < nreward.length; i++) {
					if (cm.getClient().getKeyValue("nd_"+nreward[i]['ngrade']) == null && cm.getPlayer().getHgrade() >= nreward[i]['ngrade']) {
						a += "#L"+i+"##b"+grade[nreward[i]['ngrade']][1]+" 누적 보상 (지급 가능)#k"+enter;
					}
				}
				if (a.equals("")) {
					msg += "받을 수 있는 보상이 없습니다.";
					cm.sendOk(msg);
					cm.dispose();
					return;
				}
				cm.sendSimple(msg + a);
			break;
			case 6:
				if (cm.getPlayer().getHgrade() < 1) {
					cm.sendOk("COIN 골드 이상만 사용할 수 있는 기능입니다.");
					cm.dispose();
					return;
				}
  				time = new Date();
   				year = time.getFullYear();
   				month = time.getMonth() + 1;
				date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
  				date = year+""+month+""+date2;
				daily = cm.getPlayer().getHgrade() == 3 ? daily_1 : cm.getPlayer().getHgrade() == 4 ? daily_2 : cm.getPlayer().getHgrade() == 5 ? daily_3 : cm.getPlayer().getHgrade() == 6 ? daily_4 : cm.getPlayer().getHgrade() == 7 ? daily_5 : cm.getPlayer().getHgrade() == 8 ? daily_6 : cm.getPlayer().getHgrade() == 9 ? daily_7 : cm.getPlayer().getHgrade() == 10 ? daily_8 : cm.getPlayer().getHgrade() == 11 ? daily_9 : cm.getPlayer().getHgrade() == 12 ? daily_10 : cm.getPlayer().getHgrade() == 13 ? daily_11 : []

				if (cm.getClient().getKeyValue("deez_daily_"+date) != null) {
					cm.sendOk("오늘은 이미 수령하셨습니다.");
					cm.dispose();
					return;
				}

				var rlist = "";
				for (a = 0; a < daily.length; a++) {
					rlist += "#b#i"+daily[a][0]+"##z"+daily[a][0]+"# "+daily[a][1]+"개"+enter;
				}
				cm.sendYesNo("#bCOIN "+cm.getPlayer().getHgrades()+"(은)는 매일 수령 가능!#k#r (매일 정각 초기화)#k\r\n\r\n"+rlist+"\r\n\r\n#fUI/CashShop.img/CSMVPPopup/main/BtdailyPack/button:beforeGradeDaily/normal/0#");
			break;
			case 7:
				if (!cm.getPlayer().isGM())
					return;
				cm.sendOk(getHList());
				cm.dispose();
			break;
			case 8:
				if (!cm.getPlayer().isGM())
					return;
				cm.sendOk(getHList2());
				cm.dispose();
			break;
			case 10:
				cm.dispose();
                                                cm.openNpc(9062629);
												break;
												case 20:
													cm.dispose();
																					cm.openNpc(9062609);
			break;
			case 100:
				cm.dispose();
				cm.openNpc(3005560, "dSearch");
			break;
		}
	} else if (status == 2) {
		switch (seld) {
			case 1:
				getList2(getsex(sel));
			break;
			case 3:
				if (!cm.getPlayer().isGM())
					return;
				name = cm.getText();
				var msg = "지급 대상의 닉네임 : #b"+name+"#k"+enter;
				msg += "후원포인트를 얼마나 지급하시겠습니까?";
				cm.sendGetNumber(msg, 0, 0, 3000000);
			break;
			case 4:
				seld3 = sel;
				switch (seld3) {
					case 1:
						cm.sendSimple(getListAdmin());
					break;
					case 2:
						cm.sendGetText("등급을 조정할 유저의 닉네임을 적어주세요.");
					break;
				}
			break;
			case 5:
				seld2 = sel;
				
				if (cm.getClient().getKeyValue("nd_"+nreward[seld2]['ngrade']) != null || cm.getPlayer().getHgrade() < nreward[seld2]['ngrade']) {
					cm.sendOk("r");
					cm.dispose();
					return;
				}
				if (!nreward[seld2]['select']) {
					cm.sendYesNo("정말 해당 보상을 지급받으시겠습니까?");
				} else {
					var msg = "해당 보상은 선택형 보상입니다. 받으실 보상을 선택해주세요.#b"+enter;
					for (i = 0; i < nreward[seld2]['items'].length; i++) 
						msg += "#L"+i+"##i"+nreward[seld2]['items'][i][0]+"##z"+nreward[seld2]['items'][i][0]+"# "+nreward[seld2]['items'][i][1]+"개"+enter;
					cm.sendSimple(msg);
				} 
			break;
			case 6:
				for (a = 0; a < daily.length; a++) {
					if (!cm.canHold(daily[a][0], daily[a][1])) {
						cm.sendOk("인벤토리에 공간이 부족합니다.");
						cm.dispose();
						return;
					}
				}
				for (a = 0; a < daily.length; a++) {
					cm.gainItem(daily[a][0], daily[a][1]);
				}
				cm.getClient().setKeyValue("deez_daily_"+date, "1");
				cm.sendOk("지급이 완료되었습니다.");
				cm.dispose();
			break;
		}
	} else if (status == 3) {
		switch (seld) {
			case 1:
				seldreward = sel;
				reward = getQ(seldreward);
				cm.sendYesNo("정말 해당 보상을 지급받으시겠습니까?\r\n총 #b"+reward+" 후원포인트#k를 얻게됩니다.");
			break;
			case 3:
				if (!cm.getPlayer().isGM())
					return;
				donation = sel;
				var msg = "지급 대상의 닉네임 : #b"+name+"#k"+enter;
				msg += "지급할 후원 포인트 : #b"+donation+"P#k"+enter;
				msg += "상세한 내용을 적어주십시오.";
				cm.sendGetText(msg);
			break;
			case 4:
				seld2 = sel;
				switch (seld3) {
					case 1:
						getList2Admin(seld2);
					break;
					case 2:
						modify = cm.getText();
						modifychr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(modify);
						var msg = "#b"+modify+"#k의 후원등급을 어떤 것으로 변경하시겠습니까?"+enter;
						if (modifychr == null)
							msg += enter+"현재 채널에 #b"+modify+"#k님이 존재하지 않습니다. #b해당 채널에 접속 중이지 않은 것이 #b확실#k하다면 계속 진행해주세요.#k 만약 접속 중이라면 해당 캐릭터와 #b#e같은 채널#k#n에서 진행해주세요."+enter;
						for (i = 0; i < grade.length; i++) {
							msg += "#L"+i+"#COIN "+grade[i][1]+enter;
						}
						cm.sendSimple(msg);
					break;
				}
			break;
			case 5:
				if (!nreward[seld2]['select']) {
					for (i = 0; i < nreward[seld2]['items'].length; i++) 
						cm.gainItem(nreward[seld2]['items'][i][0], nreward[seld2]['items'][i][1]);

					cm.getClient().setKeyValue("nd_"+nreward[seld2]['ngrade'], "1");
					cm.sendOk("지급이 완료되었습니다.");
					cm.dispose();
					return;
				}
				seld3 = sel;
				cm.sendYesNo("정말 해당 보상을 지급받으시겠습니까?");
				
			break;
		}
	} else if (status == 4) {
		switch (seld) {
			case 1:
				getReward(seldreward);
				cm.getPlayer().gainDonationPoint(reward);
				cm.getPlayer().saveToDB(true, false);
				cm.sendOk("수령이 완료되었습니다.");
				cm.dispose();
			break;
			case 3:
				if (!cm.getPlayer().isGM())
					return;
				comment = cm.getText();
				var msg = "지급 대상의 닉네임 : #b"+name+"#k"+enter;
				msg += "지급할 후원 포인트 : #b"+donation+"P#k"+enter;
				msg += "비고 : #b"+comment+"#k"+enter;
				msg += "#L1#첫후원 (후원포인트 두배)"+enter;
				msg += "#L2#첫후원 아님"+enter;
				cm.sendSimple(msg);
			break;
			case 4:
				switch (seld3) {
					case 1:
						DeleteReward(sel);
						cm.sendOk("삭제가 완료되었습니다.");
						cm.dispose();
					break;
					case 2:
						seldgrade = sel;
						cm.sendYesNo("정말 #fs15##r'"+modify+"님'의 후원등급을 #e'"+grade[seldgrade][1]+"'#n#k#fs15#로 바꾸시겠습니까?");
					break;
				}
			break;
			case 5:
				cm.gainItem(nreward[seld2]['items'][seld3][0], nreward[seld2]['items'][seld3][1]);

				cm.getClient().setKeyValue("nd_"+nreward[seld2]['ngrade'], "1");
				cm.sendOk("지급이 완료되었습니다.");
				cm.dispose();
			break;
		}
	} else if (status == 5) {
		switch (seld) {
			case 3:
				if (!cm.getPlayer().isGM())
					return;
				firstdon = sel == 1;
				var msg = "지급 대상의 닉네임 : #b"+name+"#k"+enter;
				msg += "지급할 후원 포인트 : #b"+donation+"P#k"+(firstdon ? "(첫후원 x2)" : "")+enter;
				msg += "비고 : #b"+comment+"#k"+enter;
				msg += "#b정말로 지급하시겠습니까?";
				cm.sendYesNo(msg);
			break;
			case 4:
				switch (seld3) {
					case 2:
						if (modifychr != null) {
							modifychr.setHgrade(seldgrade);
						} else {
							var con = DatabaseConnection.getConnection();
							asdid = getAccId(modify);
							var ps = con.prepareStatement("UPDATE acckeyvalue SET `value` = ? WHERE `id` = ? and `key` = ?");
            						ps.setString(1, seldgrade+"");
            						ps.setInt(2, asdid);
							ps.setString(3, "hGrade");
           							ps.executeUpdate();
            						ps.close(); // 봐 con.close없잖아 
							con.close();
						}
						cm.sendOk("변경이 완료되었습니다.");
						cm.dispose();
					break;
				}
			break;
		}
	} else if (status == 6) {
		switch (seld) {
			case 3:
				if (!cm.getPlayer().isGM())
					return;
				if (firstdon) donation *= 2;

				send(name, donation, comment+(firstdon ? "(첫후원 선택)" : ""));
				cm.sendOk("#fs15#지급 완료");
				cm.dispose();
			break;
		}
	}
}

function getGrade(n) {
	var ret = 0;
	var acid = getAccIdi(n);

            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM acckeyvalue WHERE `id` = ? AND `key` = ?");
		ps.setInt(1, acid);
		ps.setString(2, "hgrade");
            var rs = ps.executeQuery();

            while (rs.next()) {
		ret = Long.parseLong(rs.getString("value"));
            }
            rs.close();
            ps.close();
	return ret;
}

function getGrades(n) {
	var ret = "";
	for (i = 0; i < grade.length; i++) {
		if (grade[i][0] == n) {
			ret = grade[i][1];
			break;
		}
	}
	return ret;
}

function getHList() {
	var ret = "";

            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM acckeyvalue WHERE `key` = ? ORDER BY CAST(`value` AS UNSIGNED INT) ASC");
		ps.setString(1, "hgrade");
            var rs = ps.executeQuery();

            while (rs.next()) {
		if (rs.getString("value") != null) {
			if (Integer.parseInt(rs.getString("value")) > 0 && Integer.parseInt(rs.getString("value")) <= 6) {
				ret += "계정 : "+getAccName(rs.getInt("id"))+"("+rs.getInt("id")+") | 대표 캐릭터 : "+getNick(rs.getInt("id"))+enter;
				ret += "후원등급 : #b"+grade[Integer.parseInt(rs.getString("value"))][1]+"#k"+enter+enter;
			}
		}
            }
            rs.close();
            ps.close();
	return ret;
}

function getHList2() {
	var ret = "";

            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM acckeyvalue WHERE `key` = ?");
		ps.setString(1, "hgrade");
            var rs = ps.executeQuery();

            while (rs.next()) {
		if (rs.getString("value") != null) {
			if (Integer.parseInt(rs.getString("value")) < 0 || Integer.parseInt(rs.getString("value")) > 6) {
				ret += "계정 : "+getAccName(rs.getInt("id"))+"("+rs.getInt("id")+") | 대표 캐릭터 : "+getNick(rs.getInt("id"))+enter;
				ret += "후원등급 : #b"+grade[Integer.parseInt(rs.getString("value"))]+"("+rs.getString("value")+")#k"+enter+enter;
			}
		}
            }
            rs.close();
            ps.close();
	return ret;
}

function getAccName(id) {
	var ret = "";

            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM accounts WHERE `id` = ?");
		ps.setInt(1, id);
            var rs = ps.executeQuery();

            while (rs.next()) {
		ret = rs.getString("name");
            }
            rs.close();
            ps.close();
	return ret;
}

function getNick(acc) {
	var ret = "";
            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM characters WHERE `accountid` = ? ORDER BY `level` DESC LIMIT 1");
		ps.setInt(1, acc);
            var rs = ps.executeQuery();

            while (rs.next()) {
		ret = rs.getString("name");
            }
            rs.close();
            ps.close();
	return ret;
}

function getData() {
	time = new Date();
	year = time.getFullYear();
	month = time.getMonth() + 1;
	if (month < 10) {
		month = "0"+month;
	}
	date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
	date = year+"."+month+"."+date2;
	day = time.getDay();
}

function send(a, b , e) {
	var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("INSERT INTO donation (name, sum, comment, date, cid) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, a);
            ps.setInt(2, b);
            ps.setString(3, e);
            ps.setString(4, date);
	ps.setInt(5, MapleCharacterUtil.getIdByName(a));
            ps.executeUpdate();
	ps.close();
}

function getList() {
            var ret = "";
            var names = [];

            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM donation WHERE `check` = 0");
            var rs = ps.executeQuery();

            while (rs.next()) {
                	if (rs.getInt("cid") == cm.getPlayer().getId() || rs.getString("name").equals(cm.getPlayer().getName())) {
                    		ret += "#L"+rs.getInt("id")+"##b"+rs.getString("name")+" (지급대상)\r\n";
				break;
                	}
            }
            rs.close();
            ps.close();

        	if (ret.equals("")) return "#fs15#현재 수령 가능한 후원 보상이 없습니다.";
        	return ret;
}
function getListAdmin() {
            var ret = "";
            var names = [];

            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM donation");
            var rs = ps.executeQuery();

            while (rs.next()) {
                if (names.indexOf(rs.getString("name")) == -1) {
                    names.push(rs.getString("name"));
                    ret += "#L"+rs.getInt("cid")+"##b"+rs.getString("name")+" | 후원 횟수 : "+getCount(rs.getInt("cid"))+" | 총 누적 금액 : "+getNH(rs.getInt("cid"))+" | 등급 : "+getGrades(getGrade(rs.getInt("cid")))+"\r\n";
                }
            }
            rs.close();
            ps.close();

        	if (ret.equals("")) return "#fs15#문제가 발생했습니다.";
        	return ret;
}

function getList2Admin(n) {
	var mine = false;
	var ret = "#fs15#어떤 보상을 삭제하시겠어요? (처리 안된 보상만 보입니다.) \r\n----------------------------------------------\r\n";
	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement("SELECT * FROM donation WHERE `check` = 0 AND `cid` = ?");
	ps.setInt(1, n);
	var rs = ps.executeQuery();
	while (rs.next()) {
                    	ret += "#L"+(rs.getInt("id"))+"#";
                	ret += "#fs15#닉네임 : #b"+rs.getString("name")+"#k #fs15#지급 날짜 : #d"+rs.getString("date")+"#k \r\n";
                 	ret += "    후원금액 : #b"+rs.getInt("sum") + "#k\r\n";
                	ret += "    비고 : #d" + rs.getString("comment") + "#k#l#fs15#\r\n\r\n----------------------------------------------\r\n";
            }
            rs.close();
            ps.close();
	cm.sendSimple(ret);
}

function DeleteReward(i) {
	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement("DELETE FROM donation WHERE `id` = "+i);
	ps.executeUpdate();
	ps.close();
}

function getCount(a) {
	var ret = 0;

            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM donation WHERE `cid` = "+a);
            var rs = ps.executeQuery();

            while (rs.next()) {
		ret++;
	}
	
	return ret;
}

function getNH(a) {
	var ret = -1;
	var acid = getAccIdi(a);
	cm.getPlayer().dropMessage(5, acid);
            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM acckeyvalue WHERE `id` = ? AND `key` = ?");
		ps.setInt(1, acid);
		ps.setString(2, "nDpoint");
            var rs = ps.executeQuery();

            while (rs.next()) {
		ret = Long.parseLong(rs.getString("value"));
            }
            rs.close();
            ps.close();
	return ret;
}

function getAccIdi(b) {
	var ret = -1;
            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM characters WHERE `id` = ?");
		ps.setInt(1, b);
            var rs = ps.executeQuery();

            while (rs.next()) {
		ret = rs.getInt("accountid");
            }
            rs.close();
            ps.close();
	return ret;
}

function getAccId(a) {
	var ret = -1;
            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM characters WHERE `name` = ?");
		ps.setString(1, a);
            var rs = ps.executeQuery();

            while (rs.next()) {
		ret = rs.getInt("accountid");
            }
            rs.close();
            ps.close();
	return ret;
}

function getsex(sad) {
            var ret = "";
            var names = [];

            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement("SELECT * FROM donation WHERE `check` = 0 AND `id` = ?");
		ps.setInt(1, sad);
            var rs = ps.executeQuery();

            while (rs.next()) {
		ret = rs.getString("name");
            }
            rs.close();
            ps.close();

        	if (ret.equals("")) return "SSSSSSSSS";
        	return ret;
}

function getList2(n) {
	var mine = false;
	var ret = "#fs15#어떤 보상을 받으시겠어요?\r\n----------------------------------------------\r\n";
	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement("SELECT * FROM donation WHERE `check` = 0 AND `name` = ?");
	ps.setString(1, n);
	var rs = ps.executeQuery();
	while (rs.next()) {
                    	ret += "#L"+(rs.getInt("id"))+"#";
                	ret += "#fs15#닉네임 : #b"+rs.getString("name")+"#k #fs15#지급 날짜 : #d"+rs.getString("date")+"#k \r\n";
                 	ret += "    지급 후원 포인트 : #b"+rs.getInt("sum") + "#k\r\n";
                	ret += "    비고 : #d" + rs.getString("comment") + "#k#l#fs15#\r\n\r\n----------------------------------------------\r\n";
            }
            rs.close();
            ps.close();
	cm.sendSimple(ret);
}

function getQ(id) {
	var ret = 0;
	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement("SELECT * FROM donation WHERE `check` = 0 AND `id` = ? AND (`name` = ? or `cid` = ?)");
            ps.setInt(1, id);
            ps.setString(2, cm.getPlayer().getName());
		ps.setInt(3, cm.getPlayer().getId());
	var rs = ps.executeQuery();
	while (rs.next()) {
		ret += rs.getInt("sum");
            }
            rs.close();
            ps.close();
        	if (ret.equals("")) return "후원 보상이 없습니다.";
        	return ret;
}

function getReward(id) {
	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement("UPDATE donation SET `check` = 1 WHERE `id` = ? AND (`name` = ? or `cid` = ?)");
            ps.setInt(1, id);
            ps.setString(2, cm.getPlayer().getName());
		ps.setInt(3, cm.getPlayer().getId());
            ps.executeUpdate();
            ps.close();
}

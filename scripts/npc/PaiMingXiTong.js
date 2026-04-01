importPackage(java.lang);
importPackage(Packages.database);

var enter = "\r\n";
var seld = -1;

검정 = "#fc0xFF191919#";

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
      말 = "#fs20#"+검정+"#e[Fancy]#n 排名系統.\r\n"
      말 += "#fs15##fc0xFF4641D9##L1#[팬시]#k "+검정+"我想確認等級排名.#l\r\n";
      말 += "#fs15##fc0xFF4641D9##L2#[팬시]#k "+검정+"我想確認金幣排名.#l\r\n";
      말 += "#fs15##fc0xFF4641D9##L3#[팬시]#k "+검정+"我想確認人氣排名.#l\r\n";
      말 += "#fs15##fc0xFF4641D9##L4#[팬시]#k "+검정+"我想確認傷害排名.#l\r\n";
      말 += "#fs15##fc0xFF4641D9##L5#[팬시]#k "+검정+"我想確認職業等級排名.#l\r\n";
		//msg +="#L4#길드 랭킹"+enter;
		//msg +="#L5#무릉도장 랭킹"+enter;
		cm.sendSimple(말);
	} else if (status == 1) {
		switch (sel) {
			case 1:
				cm.sendOk(getRank("SELECT * FROM characters WHERE `level` > 0 AND `gm` = 0 ORDER BY `level` DESC LIMIT ", "level", "레벨", 10));
				cm.dispose();
				break;
			case 2:
				cm.sendOk(getRank2("SELECT * FROM characters WHERE `meso` > 0 AND `gm` = 0 ORDER BY `meso` DESC LIMIT ", "meso", "보유 메소", 10));
				cm.dispose();
				break;
			case 3:
				cm.sendOk(getRank("SELECT * FROM characters WHERE `fame` > 0 AND `gm` = 0 ORDER BY `fame` DESC LIMIT ", "fame", "인기도", 10));
				cm.dispose();
				break;
			case 4:
				cm.openNpcCustom(cm.getClient(), 9063144, "ZYDJPH");
				cm.dispose();
				break;
			case 5:
				cm.displayGuildRanks();
				cm.dispose();
				break;
			case 6:
				cm.sendOk(cm.DojangText());
				//cm.sendOk(getRank4());
				cm.dispose();
				break;
		}
	}
}

function getRank(v1, v2, v3, v4) {
	var ret = "#fs15#排名最高 "+v4+"只能看到.\r\n";
	var as = 0;
	var names = [];

	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement(v1+v4);
	var rs = ps.executeQuery();

	while (rs.next()) {
		as += 1;
		ret += as + ". #b"+rs.getString("name")+"#k "+v3+" : #d" + rs.getInt(v2) + "#k\r\n";
	}
	rs.close();
	ps.close();

	if (ret.equals("")) return "沒有排名.";
	return ret;
}

function getRank2(v1, v2, v3, v4) {
	var ret = "#fs15#排名最高 "+v4+"只能看到.\r\n";
	var as = 0;
	var names = [];

	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement(v1+v4);
	var rs = ps.executeQuery();

	while (rs.next()) {
		as += 1;
		ret += as + ". #b"+rs.getString("name")+"#k "+v3+" : #d" + rs.getLong(v2) + "#k\r\n";
	}
	rs.close();
	ps.close();

	if (ret.equals("")) return "沒有排名.";
	return ret;
}

function getRank3() {
	var ret = "#fs15#排名最多只能顯示50人。\r\n";
	var ranks = [];

	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement("SELECT * FROM questinfo WHERE `quest` = 1912212");
	var rs = ps.executeQuery();

	while (rs.next()) {
		data = rs.getString("customData").split(";");
		for (i = 0; i < data.length; ++i) {
			dd = data[i];
			if (dd.startsWith("rebirth=")) {
				newkey = dd.replace("rebirth=", "");
				newkey2 = newkey.replace(";", "");

				if (parseInt(newkey2) == 0) {
					continue;
				}

				var ps1 = con.prepareStatement("SELECT * FROM characters WHERE `id` = ?");
				ps1.setInt(1, rs.getInt("characterid"));
				var rs1 = ps1.executeQuery();
				if (rs1.next()) {
					if (rs1.getInt("gm") == 0) {
						ranks.push([parseInt(newkey2), rs1.getString("name")]);
					}
				}

				ps1.close();
				rs1.close();
				break;
			}
		}
	}
	rs.close();
	ps.close();

	ranks.sort(function (a, b) {
		return parseInt(b) - parseInt(a)
	});

	for (i = 0; i < ranks.length; ++i) {
		ret += (i + 1) + ". #b" + ranks[i][1] + "#k 횟수 : #d" + ranks[i][0]+ "#k\r\n";

		if (i >= 49) {
			break;
		}
	}

	if (ret.equals("")) return "랭킹이 없습니다.";
	return ret;
}


function getRank4() {
	var ret = "#fs15#랭킹은 최대 25명까지만 보여집니다.\r\n";
	var ranks = [];

	var con = DatabaseConnection.getConnection();
	var ps = con.prepareStatement("SELECT * FROM keyvalue WHERE `key` = 'dojo'");
	var rs = ps.executeQuery();

	while (rs.next()) {
		value = rs.getInt("value");
		if (value <= 0) {
			continue;
		}
		ps1 = con.prepareStatement("SELECT * FROM keyvalue WHERE `key` = ? AND `id` = ?");
		ps1.setString(1, "dojo_time");
		ps1.setInt(2, rs.getInt("id"));
		rs1 = ps1.executeQuery();

		if (rs1.next()) {
			timez = parseInt(rs1.getString("value"));
			if (timez <= 0) {
				continue;
			}
			ps2 = con.prepareStatement("SELECT * FROM characters WHERE `id` = ?");
			ps2.setInt(1, rs.getInt("id"));
			rs2 = ps2.executeQuery();
			if (rs2.next()) {
				if (rs2.getInt("gm") == 0) {
					name = rs2.getString("name");
					ranks.push([value, timez, name]);
				}
			}

			rs2.close();
			ps2.close();


		}

		rs1.close();
		ps1.close();

	}
	rs.close();
	ps.close();

	ranks.sort(function (a, b) {
		if (parseInt(b[0]) == parseInt(a[0])) {
			return parseInt(a[1]) - parseInt(b[1]);
		}
		return parseInt(b[0]) - parseInt(a[0]);
	});

	for (i = 0; i < ranks.length; ++i) {
		ret += (i + 1) + ". #b" + ranks[i][2] + "#k #d" + ranks[i][0] + "#k층 시간 : #d" + ranks[i][1]+ "초#k\r\n";
		if (i >= 49) {
			break;
		}
	}

	if (ret.equals("")) return "랭킹이 없습니다.";
	return ret;
}
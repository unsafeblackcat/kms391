importPackage(Packages.database);

var enter = "\r\n";
var seld = -1;

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
		var msg = "#fs15#可以確認各種排名的公告欄.#b"+enter+enter;
		//msg += cm.getPlayer().getKeyValue(3, "dojo")+"/"+cm.getPlayer().getKeyValue(3, "dojo_time");
		msg += "#L1# 等級排名"+enter;
		msg += "#L6# BOSS獵人排名"+enter;
		msg += "#L2# 金幣排名"+enter;
		msg += "#L3# 人氣度排名"+enter;
		msg += "#L4# 行會排名"+enter;
		msg += "#L5# 武陵道場排名"+enter;
		msg += "#L7# PVP對決排名"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		switch (sel) {
			case 1:
			cm.dispose();
			cm.openNpc(2093000);
			break;
			case 2:
				cm.dispose();
			cm.openNpc(2094000);
			break;
			case 3:
				cm.sendOk(getRank("SELECT * FROM characters WHERE `fame` > 0 AND `gm` = 0 ORDER BY `fame` DESC LIMIT ", "fame", "인기도", 10));
				cm.dispose();
			break;
			case 4:
				cm.sendOk(getRank("SELECT * FROM guilds WHERE `GP` > 0 ORDER BY `GP` DESC LIMIT ", "GP", "보유 GP", 10));
				cm.dispose();
			break;
			case 5:
cm.SaveDojoRank(cm.getPlayer());
				cm.ShowDojoRank();
				cm.dispose();
			break;
			case 6:
				cm.sendOk(getRank("SELECT * FROM characters WHERE `basebpoint` > 0 AND `gm` = 0 ORDER BY `basebpoint` DESC LIMIT ", "basebpoint", "누적포인트", 10));
				cm.dispose();
			break;
		        case 7:
		        cm.dispose();
				cm.openNpcCustom(cm.getClient(), 1052206, "ZYPH");
				
			break;
		}
	}
}

function getRank(v1, v2, v3, v4) {
	var ret = "#fs15#排名最多只能顯示 #r"+v4+"名#k .\r\n\r\n";
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

        	if (ret.equals("")) return "랭킹이 없습니다.";
        	return ret;
}



function getRank3(v1, v2, v3, v4, v5) {
	var ret = "#fs15#排名最多只能顯示 #r"+v4+"名#k .\r\n\r\n";
	var as = 0;
            var names = [];

            var con = DatabaseConnection.getConnection();
            var ps = con.prepareStatement(v1+v4);
            var rs = ps.executeQuery();

            while (rs.next()) {
	as += 1;
                ret += as + ". #b"+rs.getString("name")+"#k "+v3+" : #d" + rs.getInt(v2) + " / #kLOSE :#d " + rs.getInt(v5) + " #k\\r\n";
            }
            rs.close();
            ps.close();

        	if (ret.equals("")) return "랭킹이 없습니다.";
        	return ret;
}


function getRank2(v1, v2, v3, v4) {
	var ret = "#e排名最多只能顯示 "+v4+"名.\r\n";
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

        	if (ret.equals("")) return "랭킹이 없습니다.";
        	return ret;
}
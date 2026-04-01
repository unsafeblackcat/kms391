importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.server.enchant);
importPackage(java.sql);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

function getCount(check) {
    var con = null;
    var ps = null;
    var rs = null;
    var count = 0;
    try {
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement("SELECT * FROM `keyvalue` WHERE `id` = " + cm.getPlayer().getId() + " AND `key` = ?");
        ps.setString(1, check);
        rs = ps.executeQuery();
        if (rs.next()) {
            count = Integer.parseInt(rs.getString("value"));
        }
        rs.close();
        ps.close();
        con.close();
        return count;
    } catch (e) {
        cm.sendOk("입장횟수를 불러오는 중 오류가 발생하였습니다.\r\n\r\n" + e);
        cm.dispose();
        return;
    } finally {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (con != null) {
            con.close();
        }
    }
}

function setCount(check) {
    var con = null;
    var ps = null;
    var rs = null;
    var count = 0;
    try {
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement("SELECT * FROM `keyvalue` WHERE `id` = " + cm.getPlayer().getId() + " AND `key` = ?");
        ps.setString(1, check);
        rs = ps.executeQuery();
        if (rs.next()) {
            count = Integer.parseInt(rs.getString("value"));
        }
        rs.close();
        ps.close();
        if (count == 0) {
            con.close();
            cm.sendOk("알 수 없는 오류가 발생하였습니다.\r\n\r\n" + e);
            cm.dispose();
            return;
        }
        ps = con.prepareStatement("UPDATE `keyvalue` SET `value` = ? WHERE `id` = " + cm.getPlayer().getId() + " AND `key` = ?");
        ps.setString(1, count - 1 + "");
        ps.setString(2, check);
        ps.executeUpdate();
        ps.close();
        con.close();
    } catch (e) {
        cm.sendOk("입장횟수를 불러오는 중 오류가 발생하였습니다.\r\n\r\n" + e);
        cm.dispose();
        return;
    } finally {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (con != null) {
            con.close();
        }
    }
}

var boss = [
    //보스명, 보스키, 초기화권 소모 갯수
    ["노멀 자쿰", "Normal_Zakum", 1], 
    ["카오스 자쿰", "Chaos_Zakum", 1], 
    ["노멀 매그너스", "Normal_Magnus", 1], 
    ["하드 매그너스", "Hard_Magnus", 1], 
    ["노멀 힐라", "Normal_Hillah", 1], 
    ["하드 힐라", "Hard_Hillah", 1], 
    ["노멀 카웅", "Normal_Kawoong", 1], 
    ["노멀 파풀라투스", "Normal_Populatus", 1], 
    ["카오스 파풀라투스", "Chaos_Populatus", 1], 
    ["노멀 피에르", "Normal_Pierre", 1], 
    ["카오스 피에르", "Chaos_Pierre", 1], 
    ["노멀 반반", "Normal_VonBon", 1], 
    ["카오스 반반", "Chaos_VonBon", 1], 
    ["노멀 블러디 퀸", "Normal_BloodyQueen", 1], 
    ["카오스 블러디 퀸", "Chaos_BloodyQueen", 1], 
    ["노멀 벨룸", "Normal_Vellum", 1], 
    ["카오스 벨룸", "Chaos_Vellum", 1], 
    ["노멀 반 레온", "Normal_VonLeon", 1], 
    ["하드 반 레온", "Hard_VonLeon", 1], 
    ["노멀 혼테일", "Normal_Horntail", 1], 
    ["카오스 혼테일", "Chaos_Horntail", 1], 
    ["노멀 아카이럼", "Normal_Arkarium", 1], 
    ["노멀 핑크빈", "Normal_Pinkbean", 1], 
    ["카오스 핑크빈", "Chaos_Pinkbean", 1], 
    ["노멀 시그너스", "Normal_Cygnus", 1], 
    ["노멀 스우", "Normal_Lotus", 2], 
    ["하드 스우", "Hard_Lotus", 3], 
    ["노멀 데미안", "Normal_Demian", 2], 
    ["하드 데미안", "Hard_Demian", 3], 
    ["노멀 루시드", "Normal_Lucid", 3], 
    ["하드 루시드", "Hard_Lucid", 5], 
    ["노멀 윌", "Normal_Will", 3], 
    ["하드 윌", "Hard_Will", 5], 
    ["더스크", "Normal_Dusk", 3], 
    ["카오스 더스크", "Chaos_Dusk", 5],
    ["진 힐라", "Normal_JinHillah", 7], 
    ["듄켈", "Normal_Dunkel", 7], 
    ["검은 마법사", "Black_Mage", 8],
    ["선택받은 세렌", "Hard_Seren", 10]
];

var item = 4033235; //보스 초기화권 코드

var choice = -1;
var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var say = "아래에서 일일 보스 입장횟수를 1회 되돌리실 보스를 선택해주세요.\r\n\r\n";
        for (var i = 0; i < boss.length; i++) {
            say += "#L" + i + "#" + boss[i][0] + " - #i" + item + "##z" + item + "# " + boss[i][2] + "개#l\r\n";
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        choice = selection;
        if (cm.getPlayer().getV(boss[selection][1]) == null) {
            cm.getPlayer().addKV(boss[selection][1], "0");
        }
        if (Integer.parseInt(cm.getPlayer().getV(boss[selection][1])) <= 0) { //getCount(boss[selection][1]) <= 0
            cm.sendOk("선택하신 <" + boss[selection][0] + ">는 입장횟수가 0 이므로 초기화를 할수 없습니다.");
            cm.dispose();
            return;
        } else {
            cm.sendYesNo("#i" + item + "##z" + item + "# " + boss[selection][2] + "개를 사용하여\r\n" +
            "오늘의 <" + boss[selection][0] + "> 입장 횟수를 1회 되돌리시겠습니까?");
        }
    } else if (status == 2) {
        if (cm.haveItem(item, boss[choice][2]) == true) {
            cm.gainItem(item, -boss[choice][2]);
            var count = Integer.parseInt(cm.getPlayer().getV(boss[choice][1]));
            cm.getPlayer().removeV(boss[choice][1]);
            cm.getPlayer().addKV(boss[choice][1], count - 1 + "");
            //setCount(boss[choice][1]);
            cm.sendOk("오늘의 <" + boss[choice][0] + "> 보스 입장횟수를 1회 되돌렸습니다.");
            cm.dispose();
            return;
        } else {
            cm.sendOk("#i" + item + "##z" + item + "#의 갯수가 부족하여 <" + boss[choice][1] + "> 입장횟수를 초기화 할 수 없습니다.");
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}
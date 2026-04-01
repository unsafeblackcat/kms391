importPackage(Packages.server);
importPackage(Packages.database);
importPackage(Packages.client);
importPackage(java.lang);

var status = -1;
var enter = "\r\n";
var tier_coin = 4310298;
var talkType = 0x86;

var colorBlack = "#fc0xFF191919#";

function start() {
    action(1, 0 ,0);
}

function action(mode, type ,sel) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        var chat = "\r\n";

        chat += colorBlack + "■ ?? - 홍보(Promotion) 시스템을 이용하시겠습니까?" + enter + "#k";
        chat += "● #b" + cm.getPlayer().getName() + "님의 누적 프로모션 등급 : #r[" + cm.getPlayer().getPgrades() + "]#k" + enter;
        chat += "● #b" + cm.getPlayer().getName() + " 님의 잔여 프로모션포인트 : #k" + returnPointToComma() + " $"+enter;
        chat += "#L0#1. " + colorBlack + "?? - #r홍보포인트 수령하기#k#l" + enter;
        chat += "#L1#2. " + colorBlack + "?? - #b[홍보 상점]#k 이용하기" + enter;
        chat += "#L2#3. " + colorBlack + "?? - #r[HOT]#k프로모션 버프 구매하기" + enter;
        chat += "#L3#4. " + colorBlack + "?? - #r[홍보 누적 보상 수령하기]"

        if (cm.getPlayer().isGM()) chat += "#L4#     #b[GM 옵션보기]#k"

        cm.sendSimpleS(chat, talkType);
    } else if (status == 1) {
        switch(sel) {
            case 0: // 홍보포인트 수령하기
                cm.sendSimpleS(getList(), 0x42);
                break;
            case 1: // 홍보포인트샵 이용하기
                cm.dispose();
                cm.openNpc(1540100);
                break;
            case 2:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 3003273, "cashItemsearch");
                break;
            case 3:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 3004172, "??_promotion_accumulate_reward");
                break;
            case 4:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 3004172, "??_gm_option_promdonate");
                break;
        }
    }
}

function returnPointToComma() {
    var hasPoint = cm.getPlayer().getHPoint();
    return cm.getPlayer().StringToComma(hasPoint);
}

function getList() {
    var ret = "";
    var names = [];

    var con = DatabaseConnection.getConnection();
    var ps = con.prepareStatement("SELECT * FROM hongbo WHERE `check` = 0");
    var rs = ps.executeQuery();

    while (rs.next()) {
        if (names.indexOf(rs.getString("name")) == -1) {
            names.push(rs.getString("name"));
            ret += "#L" + rs.getInt("id") + "##b" + rs.getString("name");
            if (rs.getInt("cid") == cm.getPlayer().getId() || rs.getString("name").equals(cm.getPlayer().getName()))
                ret += " (지급대상)\r\n";
            else
                ret += "\r\n";
        }
    }
    rs.close();
    ps.close();

    if (ret.equals("")) return "#fs11#현재 수령 가능한 홍보 보상이 없습니다.";
    return ret;
}
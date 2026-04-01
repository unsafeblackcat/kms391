importPackage(Packages.database);
importPackage(java.lang);

var status = -1;

var Time = System.currentTimeMillis();
var Today = Integer.parseInt(new java.text.SimpleDateFormat("YYYYMMdd").format(Time));
var nickname;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, sel) {
    if (mode == 1 && type != 1) {
        status++;
    } else {
        if (type == 1 && mode == 0) {
            status++;
            sel = 0;
        } else if (type == 1 && mode == 1) {
            status++;
            sel = 1;
        } else {
            cm.dispose();
            return;
        }
    }
    if (status == 0) {

        if(GetCount(1000) == 3) {
            cm.sendOk(" 오늘 " + nickname + " 캐릭터로 보상을 받았어");
            cm.dispose();
        }

        if (GetCount(1000) == 0) {
            var chat = "#fs15#";
            chat += "#b#e[Lv.10] 레벨  에테르넬 장비 수집#n\r\n#i1005983:# , #i1042436:# , #i1062288:# 를 모두 모아주세요!\r\n";
            chat += "#e[획득 보상]#n\r\n";
            chat += "#b";
            chat += "#i4001126:#,#i4000019:# 10개\r\n";
            cm.sendYesNo(chat);
        } else {
            if (GetCount(1000) == 1 || GetCount(1000) == 2) {
                if (!cm.haveItem(1005983) && !cm.haveItem(1042436) && !cm.haveItem(1062288)) {
                    cm.sendOk("#fs15# #i1005983:# 와 #i1042436:# 와 #i1062288:# 3개의 장비가 모두 보유중이지 않는 이상\r\n퀘스트를 완료할 수 없습니다.");
                    cm.dispose();
                } else {
                    cm.sendYesNo("보상을 받으러 갈까?");
                }
            }
        }
    } else if(status == 1) {
        if(GetCount(1000) == 0) {
            SetCount(1000,1);
            cm.sendOk("이제 재료 모아와");
            cm.dispose();
        }

        if (GetCount(1000) == 1 || GetCount(1000) == 2) {
            if (cm.haveItem(1005983) && cm.haveItem(1042436) && cm.haveItem(1062288)) {
                SetCount(1000,2);
                var chat = "#fs15#";
                chat += "#b#e[모자]#k#n#i1005983:#\r\n#b#e[상의]#k#n#i1042436:#\r\n#b#e[하의]#k#n#i1062288:\r\n  를 모아오셨군요. 아래는 보상입니다.\r\n\r\n";
                chat += "#e[획득 보상]#n\r\n";
                chat += "#b";
                chat += "#i4001126:#20개 및 #i4000019:# 10개\r\n";;
                cm.sendYesNo(chat);
            }
        }
    } else if(status == 2) {
            if(GetCount(1000) == 2) {
                SetCount(1000,3);
                var chat = "#fs15#";
                    chat += "#b#e[모자]#k#n#i1005983:#\r\n#b#e[상의]#k#n#i1042436:#\r\n#b#e[하의]#k#n#i1062288:\r\n  를 모아오셨군요. 아래는 보상입니다.\r\n\r\n";
                    chat += "#e[획득 보상]#n\r\n";
                    chat += "#b";
                    chat += "#i4001126:#20개 및 #i4000019:# 10개\r\n";;
                    cm.gainItem(4001126, 10);
                    cm.gainItem(4001126, 10);
                    cm.gainItem(4000019, 10);
                    cm.sendOk(chat);
                    cm.dispose();
            }
    }
}

function GetCount(QuestNum) {
    var con = null;
    var ps = null;
    var rs = null;
    var check = 0;
    var Count = 0;
    nickname = "";
    try {
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement("INSERT INTO `DailyAccountQuest` (`accountid`, `date`, `quest`, `nickname`, `count`) " +
            "SELECT " + cm.getPlayer().getAccountID() + ", '" + Today + "', '" + QuestNum + "','" + cm.getPlayer().getName() + "', 0 " +
            "FROM DUAL WHERE NOT EXISTS (SELECT * FROM `DailyAccountQuest` WHERE `accountid` = " + cm.getPlayer().getAccountID() + " AND `date` = '" + Today + "' AND `quest` = '" + QuestNum + "')");
        ps.executeUpdate();
        ps.close();
        ps = con.prepareStatement("SELECT * FROM `DailyAccountQuest` WHERE `accountid` = " + cm.getPlayer().getAccountID() + " AND `quest` = '" + QuestNum + "' AND `nickname` = '" + cm.getPlayer().getName() +"'");
        rs = ps.executeQuery();
        while (rs.next()) {
            Count = rs.getInt("count");
            nickname = rs.getString("nickname");
            check++;
        }
        rs.close();
        ps.close();
        con.close();
        if (check != 1) {
            cm.sendOk("오류가 발생하였습니다. 다시 시도해 주세요.");
            cm.dispose();
            return;
        }
        return Count;
    } catch (e) {
        cm.sendOk("오류가 발생하였습니다. 다시 시도해 주세요.\r\n" + e);
        cm.dispose();
        return;
    } finally {
        if (rs != null) {
            try {
                rs.close();
            } catch (e) {

            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (e) {

            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (e) {

            }
        }
    }
}

function SetCount(QuestNum, count) {
    var con = null;
    var ps = null;
    try {
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement("UPDATE `DailyAccountQuest` SET `count` = " + count + " WHERE `accountid` = " + cm.getPlayer().getAccountID() + " AND `date` = '" + Today + "' AND `quest` = '" + QuestNum + "' AND `nickname` = '" + cm.getPlayer().getName() + "'");
        ps.executeUpdate();
        ps.close();
        con.close();
    } catch (e) {
        cm.sendOk("오류가 발생하였습니다. 다시 시도해 주세요.\r\n" + e);
        cm.dispose();
        return;
    } finally {
        if (ps != null) {
            try {
                ps.close();
            } catch (e) {

            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (e) {

            }
        }
    }
}
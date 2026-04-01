﻿importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);
/* (명) 추천인에게 지급할 아이템 */
var item = 1000;
/* 추천을 했을 때 지급할 아이템 */
var item2 = new Array(4310320, 100);
var item3 = new Array(4310320, 1000);
var status = -1;
var seld = -1, seld2 = -1, a = -1;
var 보상아이템 = 4310320;
var 올스탯 = 0, 공마 = 0;
var 기타보상 = 4310320, 기타개수 = 10;
var 추천보상 = [[5121104, 5], [3015642, 1], [4001833, 5], [5060048, 5]]
var reward = [
    {'goal': 10, 'item': [[4310320, 100]]},
    {'goal': 50, 'item': [[4310320, 500]]},
    {'goal': 100, 'item': [[4310320, 1000]]}
]

/* 추천인 등록 체크 */
function overlab_recom(name, name2) {
    var c = null;
    var con = null;
    try {
        c = DatabaseConnection.getConnection();
        con = c.prepareStatement("SELECT * FROM recom_log WHERE name LIKE '" + name + "%'").executeQuery();
        overlab = true;
        if (!con.next()) {
            overlab = false;
        }
        con.close();
    } catch (e) {

    } finally {
        try {
            if (c != null) {
                c.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (e) {

        }
    }

    return overlab;
}

function existChar(name) {
    var c = null;
    var con = null;
    try {
        c = DatabaseConnection.getConnection();
        con = c.prepareStatement("SELECT * FROM characters WHERE name LIKE '" + name + "%'").executeQuery();
        overlab = true;
        if (!con.next()) {
            overlab = false;
        }
        con.close();
    } catch (e) {

    } finally {
        try {
            if (c != null) {
                c.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (e) {

        }
    }

    return overlab;
}

function getAccIdFromDB(name) {
    var c = null;
    var con = null;
    try {
        c = DatabaseConnection.getConnection();
        con = c.prepareStatement("SELECT * FROM characters WHERE name LIKE '" + name + "%'").executeQuery();
        var ret = -1;
        if (con.next()) {
            ret = con.getInt("accountid");
        }
        con.close();
    } catch (e) {

    } finally {
        try {
            if (c != null) {
                c.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (e) {

        }
    }

    return ret;
}

function 누적(name) {
    var a = 0;

    var c = null;
    var con = null;
    try {
        c = DatabaseConnection.getConnection();
        con = c.prepareStatement("SELECT * FROM recom_log WHERE recom LIKE '" + name + "'").executeQuery();
        while (con.next()) {
            if (con.getString("recom").equals(name)) {
                a++;
            }
        }
        con.close();
    } catch (e) {

    } finally {
        try {
            if (c != null) {
                c.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (e) {

        }
    }

    return a;
}

/* 추천인 등록 */
function join_recom(name, name2, recom) {
    var con = null;
    var insert = null;
    try {
        con = DatabaseConnection.getConnection();
        insert = con.prepareStatement("INSERT INTO recom_log(name, recom, state, date) VALUES(?,?,?,now())");
        insert.setString(1, name + "%" + name2);
        insert.setString(2, recom);
        insert.setString(3, 0);
        insert.executeUpdate();
        insert.close();
    } catch (e) {

    } finally {
        try {
            if (con != null) {
                con.close();
            }
            if (insert != null) {
                insert.close();
            }
        } catch (e) {

        }
    }
}

/* 추천인 랭킹 */
function recom_log() {
    var txt = new StringBuilder();

    var c = null;
    var con = null;
    try {
        c = DatabaseConnection.getConnection();
        con = c.prepareStatement("SELECT log.id, log.recom, count(*) AS player FROM recom_log AS log LEFT JOIN characters AS ch ON log.recom = ch.name WHERE ch.gm <= 0 GROUP BY recom ORDER BY player DESC").executeQuery();
        var rank = 0;
        while (con.next()) {
            txt.append("#L" + con.getInt("id") + "#").append(rank == 0 ? "#r " : rank == 1 ? "#b " : "#k ").append("추천인 코드 #k: ").append(con.getString("recom")).append(" | ").append("추천 수 #k: #e").append(con.getString("player")).append("#n\r\n");
            rank++;
        }
        con.close();
    } catch (e) {

    } finally {
        try {
            if (c != null) {
                c.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (e) {

        }
    }

    return txt.toString();
}

/* 추천인 리스트 */
function recom_list(id) {
    var txt = new StringBuilder();

    var c = null;
    var con = null;
    var idcon = null;
    try {
        c = DatabaseConnection.getConnection();
        idcon = c.prepareStatement("SELECT * FROM recom_log WHERE id = '" + id + "'").executeQuery();
        idcon.next(), recom_per = idcon.getString("recom");
        con = c.prepareStatement("SELECT * FROM recom_log WHERE recom = '" + recom_per + "'").executeQuery();
        txt.append(recom_per + "님을 추천하신 플레이어들 입니다.\r\n\r\n");
        while (con.next()) {
            var con_name = con.getString("name").split("%");
            txt.append("닉네임 : #e").append(con_name[1]).append("#n | ").append("날짜 : ").append(con.getDate("date") + " " + con.getTime("date")).append("\r\n");
        }
        con.close();
    } catch (e) {

    } finally {
        try {
            if (c != null) {
                c.close();
            }
            if (con != null) {
                con.close();
            }
            if (idcon != null) {
                idcon.close();
            }
        } catch (e) {

        }
    }

    return txt.toString();
}

/* 추천인 수 불러오기 */
function recom_num(name) {
    var c = null;
    var con = null;
    try {
        c = DatabaseConnection.getConnection();
        con = c.prepareStatement("SELECT COUNT(*) AS player FROM recom_log WHERE recom = '" + name + "' and state = 0").executeQuery();
        con.next();
        recoms_num = con.getString("player");
        con.close();
    } catch (e) {

    } finally {
        try {
            if (c != null) {
                c.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (e) {

        }
    }
}

/* 추천인 닉네임 불러오기 */
function recom_person(name) {
    var txt = new StringBuilder();

    var c = null;
    var con = null;
    try {
        c = DatabaseConnection.getConnection();
        con = c.prepareStatement("SELECT * FROM recom_log WHERE recom = '" + name + "' and state = 0").executeQuery();
        while (con.next()) {
            var con_name = con.getString("name").split("%");
            txt.append("#b[" + con_name[1] + "] ");
        }
        con.close();
    } catch (e) {

    } finally {
        try {
            if (c != null) {
                c.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (e) {

        }
    }

    return txt.toString();
}

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {

    /* 스크립트 시작 설정 */
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }

    /* 스크립트 메인 부분 */
    if (status == 0) {
        cm.sendSimple("#b#fs15#" + cm.getPlayer().getName() + "#k님, PRAY에 오신 것을 환영합니다.\r\n#r(추천은 1인 1계정 레벨 100이상 가능합니다.)#k\r\n\r\n#fUI/UIWindow.img/UtilDlgEx/list1#\r\n#L0##b추천인#k 등록하기\r\n#L1##b추천인#k 랭킹보기#l\r\n\r\n\r\n#fUI/UIWindow.img/UtilDlgEx/list0#\r\n#L2##b추천인#k 확인하기\r\n");
    } else if (status == 1) {
        if (selection == 0) {
            if (!overlab_recom(cm.getClient().getAccID(), cm.getPlayer().getName())) {
                if (cm.getPlayer().getLevel() >= 100) {
                    cm.sendGetText("#b#fs15#" + cm.getPlayer().getName() + "#k님, 당신을 PRAY으로 이끈 이의 #b닉네임#k을 말씀해주세요.\r\n하지만 #r한 번 등록하면 되돌릴 수 없으니#k 신중해야만 해요.");
                } else {
                    cm.sendOk("#fs15##r100레벨 미만#k은 추천인을 등록할 수 없어요.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("#fs15#추천인은 한 번만 작성 가능하답니다.");
                cm.dispose();
            }

        } else if (selection == 1) {
            cm.sendSimple("#fs15#이곳은 많은추천을 받은분들의 목록이에요.\r\n#b" + cm.getPlayer().getName() + "#k님께서도 조금만 노력한다면 이곳에 오르실 수 있어요.\r\n" + recom_log());
            status = 2;
        } else if (selection == 2) {
            recom_num(cm.getPlayer().getName());
            if (recoms_num == 0) {
                cm.sendOk("#fs15#아쉽지만 아직 #b" + cm.getPlayer().getName() + "#k님의 이름을 속삭인 분이 없네요.\r\n열심히 #bPRAY#k을 알린다면, 곧 누군가 속삭일테니 실망 말아요."), cm.dispose();
            } else {
                cm.sendOk("역시나… 정말 대단하시네요. #b" + cm.getPlayer().getName() + "#k님은 절 찾아 오실때마다 더 많은 지지를 받고 계신 것 같아요. 이번에는 " + recoms_num + "명 " + recom_person(cm.getPlayer().getName()) + "#k의 지지를 받으셨어요.\ ");
                cm.getPlayer().gainHPoint(item * Integer.parseInt(recoms_num));
                cm.getPlayer().dropMessage(1, item * Integer.parseInt(recoms_num) + "홍보 포인트를 지급 받았습니다.");
                var c = null;
                var ps = null;
                try {
                    c = DatabaseConnection.getConnection();
                    ps = c.prepareStatement("UPDATE recom_log SET state = 1 WHERE recom = '" + cm.getPlayer().getName() + "'");
                    ps.executeUpdate();
                    ps.close();
                    c.close();
                } catch (e) {

                } finally {
                    try {
                        if (c != null) {
                            c.close();
                        }
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (e) {

                    }
                }
                cm.dispose();
            }
        } else if (selection == 3) {
            seld = selection;
            a = 누적(cm.getPlayer().getName());
            var msg = "#fs15#받으실 보상을 선택해주세요.\r\n현재 #b#h ##k님의 추천 수는 개 입니다.#fs15#\r\n";
            for (i = 0; i < reward.length; i++) {
                if (reward[i]['goal'] <= a && cm.getPlayer().getKeyValue(201821, "recom_" + reward[i]['goal']) == -1) {
                    msg += "#L" + i + "##b" + reward[i]['goal'] + "명 보상 (수령 가능)\r\n";
                } else {
                    msg += "#L" + i + "##r" + reward[i]['goal'] + "명 보상 (수령 불가)\r\n";
                }
            }
            cm.sendSimple(msg);
        }

    } else if (status == 2) {
        if (seld == 3) {
            seld2 = selection;
            var msg = "다음은 누적 " + reward[seld2]['goal'] + "명 보상입니다.#b#fs15#\r\n";
            for (i = 0; i < reward[seld2]['item'].length; i++) {
                msg += "#i" + reward[seld2]['item'][i][0] + "##z" + reward[seld2]['item'][i][0] + "# " + reward[seld2]['item'][i][1] + "개\r\n";
            }
            if (reward[seld2]['goal'] <= a && cm.getPlayer().getKeyValue(201821, "recom_" + reward[seld2]['goal']) == -1) {

                for (i = 0; i < reward[seld2]['item'].length; i++)
                    cm.gainItem(reward[seld2]['item'][i][0], reward[seld2]['item'][i][1]);
                cm.getClient().setKeyValue(201821, "recom_" + reward[seld2]['goal'], "1");
                cm.sendOk(msg);
                cm.dispose();
                return;
            } else {
                cm.sendOk(msg);
                cm.dispose();
                return;
            }
        } else {
            if (!existChar(cm.getText())) {
                cm.sendOk("없는 유저입니다.");
                cm.dispose();
                return;
            }
            if (cm.getText().equals("") || cm.getText().equals(cm.getPlayer().getName()) || getAccIdFromDB(cm.getText()) == getAccIdFromDB(cm.getPlayer().getName())) {
                cm.sendOk(cm.getText().equals("") ? "입력을 잘못 하셨습니다." : "자기 자신을 등록 할 수는 없습니다.");
                cm.dispose();
            } else {
                join_recom(cm.getClient().getAccID(), cm.getPlayer().getName(), cm.getText());
                for (i = 0; i < 추천보상.length; i++) {
                    cm.gainItem(추천보상[i][0], 추천보상[i][1]);
                }

                cm.sendOk("#fs15#이건 #b" + cm.getPlayer().getName() + "#k님에게 드리는 저의 작은 선물입니다. 앞으로의 여행에 큰 도움이 될 거예요.");
                World.Broadcast.broadcastMessage(CWvsContext.serverNotice(11, "", "[알림] " + cm.getPlayer().getName() + " 님이 " + cm.getText() + " 님을 추천인으로 등록하셨습니다."));
                cm.dispose();
            }
        }

    } else if (status == 3) {
        cm.sendOk(recom_list(selection));
        cm.dispose();
    }
}

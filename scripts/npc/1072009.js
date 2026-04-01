importPackage(Packages.constants);
importPackage(Packages.database);
importPackage(java.lang);

function Comma(i) {
    var reg = /(^[+-]?\d+)(\d{3})/;
    i += '';
    while (reg.test(i)) {
        i = i.replace(reg, '$1' + ',' + '$2');
    }
    return i;
}

var Time = new Date();

var Month = Time.getMonth() + 1 + "";
if (Month < 10) {
    Month = "0" + Month;
}

var Date = Time.getDate() + "";
if (Date < 10) {
    Date = "0" + Date;
}

var Year = Time.getFullYear() + "";
var Today = parseInt(Year + Month + Date);

var rank = 0;
var admin = 0;
var rewarddate = 0;
var characterid = 0;

var name = "";

var status = -1;

function start() {
    status = -1;
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
        var say = "";
        if (cm.getPlayer().getGMLevel() > 5) {
            say += "#r#L3#랭킹초기화#l#k";
        }
        cm.sendSimple("#h0#님의 데미지 기록 : " + Comma(cm.getPlayer().DamageMeter) + "\r\n" + "#b#L1#데미지측정#l\r\n#L2#데미지랭킹#l\r\n#k" + say);

    } else if (status == 1) {
        if (selection == 1) {
            var em = cm.getEventManager("DamageMeter");
            if (em.getProperty("entry").equals("false")) {
                cm.sendOk("다른 유저가 현재 데미지 기록 중입니다. 1분후에 다시 시도해 주세요.");
                cm.dispose();
                return;
            } else {
                cm.getPlayer().DamageMeter = 0;
                em.startInstance(cm.getPlayer());
                cm.dispose();
            }
        } else if (selection == 2) {
            var con = null;
            var ps = null;
            var rs = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT * FROM `DamageMeter` ORDER BY `damage` DESC");
                rs = ps.executeQuery();
                var count = 0;
                var say = "PRAY 데미지미터 랭킹입니다.\r\n\r\n";
                while (rs.next()) {
                    count++;
                    say += count + "위 / " + rs.getString("name") + " / " + ConvertNumber(rs.getLong("damage")) + "\r\n";
                }
                rs.close();
                ps.close();
                con.close();
                cm.sendOk(say);
                cm.dispose();
                return;
            } catch (e) {
                cm.sendOk("아직 등록된 기록이 없습니다.\r\n" + e);
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
        } else if (selection == 3 && cm.getPlayer().getGMLevel() > 5) {
            admin = 1;
            cm.sendYesNo("데미지미터 랭킹을 초기화 하시겠습니까?");
        } else {
            cm.dispose();
            return;
        }

    } else if (status == 2) {
        if (admin == 0) {
            var con = null;
            var ps = null;
            var rs = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT * FROM `DamageMeter` WHERE `date` = " + selection + " ORDER BY `damage` DESC");
                rs = ps.executeQuery();
                var count = 0;
                var say = selection.toString().substring(0, 4) + "년 " + selection.toString().substring(4, 6) + "월 " + selection.toString().substring(6, 8) + "일의 데미지미터 랭킹입니다.\r\n\r\n";
                while (rs.next()) {
                    count++;
                    say += count + "위 - " + rs.getString("name") + "   데미지 : " + ConvertNumber(rs.getLong("damage")) + "\r\n";
                }
                rs.close();
                ps.close();
                con.close();
                cm.sendOk(say);
                cm.dispose();
                return;
            } catch (e) {
                cm.sendOk("아직 등록된 기록이 없습니다.\r\n" + e);
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
        } else if (admin == 1 && cm.getPlayer().getGMLevel() > 5) {
            var con = null;
            var ps = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("DELETE FROM `DamageMeter`");
                ps.executeUpdate();
                ps.close();
                con.close();
                cm.sendOk("데미지미터 기록 초기화가 완료 되었습니다.");
                cm.dispose();
                return;
            } catch (e) {
                cm.sendOk("오류가 발생하였습니다.\r\n" + e);
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
        } else {
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}

function ConvertNumber(number) {
    var inputNumber = number < 0 ? false : number;
    var unitWords = ['', '만 ', '억 ', '조 ', '경 '];
    var splitUnit = 10000;
    var splitCount = unitWords.length;
    var resultArray = [];
    var resultString = '';
    if (inputNumber == false) {
        cm.sendOk("다시 시도해 주세요.");
        cm.dispose();
        return;
    }
    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
        unitResult = Math.floor(unitResult);
        if (unitResult > 0) {
            resultArray[i] = unitResult;
        }
    }
    for (var i = 0; i < resultArray.length; i++) {
        if (!resultArray[i]) {
            continue;
        }
        resultString = String(resultArray[i]) + unitWords[i] + resultString;
    }
    return resultString;
}
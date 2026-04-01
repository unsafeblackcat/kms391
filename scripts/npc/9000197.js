importPackage(Packages.constants);
importPackage(Packages.database);
importPackage(java.lang);

function ConvertNumber(number) { //모 블로그 참조함, 이 부분에 대해서는 호크아이(hawkeye888@nate.com) 에게 저작권이 없음
    var inputNumber  = number < 0 ? false : number;
    var unitWords    = ['', '萬” ', '億 ', '兆 ', '京 '];
    var splitUnit    = 10000;
    var splitCount   = unitWords.length;
    var resultArray  = [];
    var resultString = '';
    if (inputNumber == false) {
        cm.sendOk("發生錯誤。 請再試一次。\r\n（解析錯誤）");
        cm.dispose();
        return;
    }
    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
        unitResult = Math.floor(unitResult);
        if (unitResult > 0){
            resultArray[i] = unitResult;
        }
    }
    for (var i = 0; i < resultArray.length; i++) {
        if(!resultArray[i]) continue;
        resultString = String(resultArray[i]) + unitWords[i] + resultString;
    }
    return resultString;
}

var Time = new Date();
var Year = Time.getFullYear() + "";
var Month = Time.getMonth() + 1 + "";
var Date = Time.getDate() + "";
if (Month < 10) {
    Month = "0" + Month;    
}
if (Date < 10) {
    Date = "0" + Date;    
}
var Today = parseInt(Year + Month + Date);

var admin = 0;
var rewarddate = 0;
var rank = 0;
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
        cm.getPlayer().DamageMeterMap = 450002250;
        cm.getPlayer().DamageMeterMonster = 9300800;
        cm.getPlayer().DamageMeterTime = 30;
        cm.getPlayer().DamageMobX = 200;
        cm.getPlayer().DamageMobY = 100;
        cm.getPlayer().DamageMeterExitMap = 180000000;
        var say = "";
        if (cm.getPlayer().getGMLevel() > 5) {
            say += "\r\n\r\n\r\n   <管理員選單>\r\n#L4#初始化排名#l\r\n\r\n"//#L5#랭커 보상 지급하기#l;
        }
        cm.sendSimple("#fs15##b傷害量測系統\r\n#k歡迎來到. #b戰鬥力#k如果好奇的話\r\n請通過傷害量測確認自己的強悍程度如何.\r\n\r\n." +
        "先前記錄傷害 : #r" + cm.getPlayer().DamageMeter + "\r\n#k" +
        "#L1##d量測傷害（2分鐘）#l\r\n#L2##d今日傷害排名！#l\r\n#L3#先前傷害排名#l" + say);
    } else if (status == 1) {
        if (selection == 1) {
            var em = cm.getEventManager("DamageMeter");
            if (em == null) {
                cm.sendOk("發生錯誤. 請再試一次.");
                cm.dispose();
                return;
            } else if (em.getProperty("entry").equals("false")) {
                cm.sendOk("其他玩家正在記錄傷害. 請一分鐘後再試一次.");
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
                ps = con.prepareStatement("SELECT * FROM `DamageMeter` WHERE `date` = " + Today + " ORDER BY `damage` DESC");
                rs = ps.executeQuery();
                var count = 0;
                var say = "今日傷害錶排名.\r\n\r\n";
                while (rs.next()) {
                    count++;
                    say += count +"위 - " + rs.getString("name") + "   傷害 : " + ConvertNumber(rs.getLong("damage")) + "\r\n";
                }
                rs.close();
                ps.close();
                con.close();
                cm.sendOk(say);
                cm.dispose();
                return;
            } catch (e) {
                cm.sendOk("尚未注册記錄或出現錯誤.\r\n" + e);
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
        } else if (selection == 3) {
            admin = 0;
            cm.sendGetNumber("어您要看哪一天的排名？\r\n請輸入以下日期.\r\n是) 20200101", 0, 20200101, 99999999);
        } else if (selection == 4 && cm.getPlayer().getGMLevel() > 5) {
            admin = 1;
            cm.sendYesNo("真的要重置傷害計分排名嗎?\r\n將删除所有日期的記錄!");
        } else if (selection == 5 && cm.getPlayer().getGMLevel() > 5) {
            admin = 2;
            cm.sendGetNumber("您要支付哪一天的排名獎勵?\r\n請輸入以下日期.\r\n是) 20200101", 0, 20200101, 99999999);
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
                var say = selection.toString().substring(0,4) + "年 " + selection.toString().substring(4,6) + "月 " + selection.toString().substring(6,8) + "日 的傷害等級.\r\n\r\n";
                while (rs.next()) {
                    count++;
                    say += count +"上 - " + rs.getString("name") + "   傷害 : " + ConvertNumber(rs.getLong("damage")) + "\r\n";
                }
                rs.close();
                ps.close();
                con.close();
                cm.sendOk(say);
                cm.dispose();
                return;
            } catch (e) {
                cm.sendOk("尚未注册記錄或出現錯誤.\r\n" + e);
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
                cm.sendOk("傷害計記錄初始化完畢.");
                cm.dispose();
                return;
            } catch (e) {
                cm.sendOk("發生錯誤.\r\n" + e);
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
        } else if (admin == 2 && cm.getPlayer().getGMLevel() > 5) {
            var con = null;
            var ps = null;
            var rs = null;
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("SELECT * FROM `DamageMeter` WHERE `date` = " + selection + " ORDER BY `damage` DESC");
                rs = ps.executeQuery();
                var count = 0;
                rewarddate = selection;
                var say = selection.toString().substring(0,4) + "年 " + selection.toString().substring(4,6) + "月 " + selection.toString().substring(6,8) + "天 的傷害等級.\r\n" +
                "해당 닉네임을 선택하시면 랭킹 보상 지급이 가능합니다.\r\n";
                while (rs.next()) {
                    count++;
                    say += "#L" + rs.getInt("id") + "#" + count +"上 - " + rs.getString("name") + "   傷害 : " + ConvertNumber(rs.getLong("damage")) + "\r\n";
                }
                rs.close();
                ps.close();
                con.close();
                cm.sendSimple(say);
            } catch (e) {
                cm.sendOk("尚未注册記錄或出現錯誤.\r\n" + e);
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
        } else {
            cm.dispose();
            return;
        }
    } else if (status == 3 && cm.getPlayer().getGMLevel() > 5) {
        var con = null;
        var ps = null;
        var rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM `DamageMeter` WHERE `date` = " + rewarddate + " ORDER BY `damage` DESC");
            rs = ps.executeQuery();
            var count = 0;
            var say = rewarddate.toString().substring(0,4) + "年 " + rewarddate.toString().substring(4,6) + "月 " + rewarddate.toString().substring(6,8) + "日 ";
            while (rs.next()) {
                count++;
                if (rs.getInt("id") == selection) {
                    rank = count;
                    characterid = rs.getInt("characterid");
                    name = rs.getString("name");
                    say += rs.getString("name") + "玩家傷害計分榜.\r\n\r\n";
                    say += count +"上 - 傷害 : " + ConvertNumber(rs.getLong("damage"));
                    say += "\r\n\r\n是否支付排名 " + count + "等獎勵?";
                }
            }
            rs.close();
            ps.close();
            con.close();
            cm.sendYesNo(say);
        } catch (e) {
            cm.sendOk("未選擇用戶或出現錯誤.\r\n" + e);
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
    } else if (status == 4 && cm.getPlayer().getGMLevel() > 5) {
        //1등 2022424 20개 + 4001126 500개 + 4310024 5개
        //2등 2022424 10개 + 4001126 300개 + 4310024 3개
        //3등 2022424 5개 + 4001126 100개 + 4310024 1개
        var channel = Packages.handling.world.World.Find.findChannel(characterid);
        if (rank == 1) {
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(2022424, 20, characterid, "[傷害計]", "傷害計 " + rank + "等獎勵", channel >= 0);
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(4001126, 500, characterid, "[傷害計]", "傷害計 " + rank + "等獎勵", channel >= 0);
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(4310024, 5, characterid, "[傷害計]", "傷害計 " + rank + "等獎勵", channel >= 0);
        } else if (rank == 2) {
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(2022424, 10, characterid, "[傷害計]", "傷害計 " + rank + "等獎勵", channel >= 0);
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(4001126, 300, characterid, "[傷害計]", "傷害計 " + rank + "等獎勵", channel >= 0);
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(4310024, 3, characterid, "[傷害計]", "傷害計 " + rank + "等獎勵", channel >= 0);
        } else if (rank == 3) {
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(2022424, 5, characterid, "[傷害計]", "傷害計 " + rank + "等獎勵", channel >= 0);
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(4001126, 100, characterid, "[傷害計]", "傷害計 " + rank + "等獎勵", channel >= 0);
            Packages.handling.channel.handler.DueyHandler.addNewItemToDb(4310024, 1, characterid, "[傷害計]", "傷害計 " + rank + "等獎勵", channel >= 0);
        } else {
            cm.sendOk("1~3등 밖에 있는 유저이므로 보상이 없습니다.");
            cm.dispose();
            return;
        }
        if (channel >= 0) {
            Packages.handling.world.World.Broadcast.sendPacket(characterid, Packages.tools.MaplePacketCreator.sendDuey(28, null, null));
            Packages.handling.world.World.Broadcast.sendPacket(characterid, Packages.tools.MaplePacketCreator.serverNotice(2, "[시스템] : 데미지미터 " + rank + "등 보상이 택배로 지급되었습니다."));
        }
        cm.sendOk(name + "向玩家發放了 " + rank + "等獎勵.");
        cm.dispose();
        return;
    } else {
        cm.dispose();
        return;
    }
}
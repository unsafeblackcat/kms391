importPackage(Packages.constants);
importPackage(Packages.server);
importPackage(Packages.database);

importPackage(java.sql);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.math);
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    outmap = 921172200
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
   if (cm.getPlayer().getGMLevel() == 0) {
   cm.sendOkS(" 公會聯合副本準備中，請稍候 :D", 0x04, 9010106);
   cm.dispose();
   return;
   }
        if (cm.getPlayer().getMapId() != outmap) {
            talk = "#r#e巨龍討伐戰#k#n正等待勇者挑戰！\r\n"
            talk += "是否要進入#b#e公會聯合副本#k#n？"
            cm.sendYesNoS(talk,0x04,9010106);
        } else {
            var eim = cm.getPlayer().getEventInstance();
            hp1 = (Long.MAX_VALUE - eim.getMobs().get(1).getHp() - cm.getPlayer().getKeyValue(20190622, "Union_Raid_Hp")) >= 10000000000000 ? 10000000000000 : (Long.MAX_VALUE - eim.getMobs().get(1).getHp() - cm.getPlayer().getKeyValue(20190622, "Union_Raid_Hp"))
            coin = Math.floor((hp1 + (Long.MAX_VALUE - eim.getMobs().get(0).getHp())) / 100000000000)
            if (coin == 0) {
                talk = "看來您還沒獲得任何公會幣呢？如果覺得太困難，可以稍作休息再來挑戰。其他公會成員會持續累積獎勵的~"
            } else {
                talk = "已獲得#i4310229# #b#z4310229##k共#b" + coin + "枚#k！真是厲害！"
            }
            cm.sendNextS(talk,0x04,9010106);
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() != outmap) {
            if (cm.getPlayer().getKeyValue(20190622, "Union_Raid_Time_1") < 0) {
                cm.getPlayer().setKeyValue(20190622, "Union_Raid_Time_1", new Date().getTime());
            }
            if (cm.getPlayer().getKeyValue(20190622, "Union_Raid_Hp") < 0) {
                cm.getPlayer().setKeyValue(20190622, "Union_Raid_Hp", 0);
            }
            cm.getPlayer().setKeyValue(20190622, "Union_Raid_Atk", getAtk()); // 계산한걸로 변경
            cm.getEventManager("test1").startInstance_Solo("" + 921172000, cm.getPlayer());
            cm.dispose();
       return;
        } else {
            cm.sendNextS(" 即將傳送回原處，祝您冒險愉快~",0x04,9010106);
        }
    } else if (status == 2) {
        cm.getPlayer().setKeyValue(20190622, "Union_Raid_Time_1", new Date().getTime());
        cm.getPlayer().setKeyValue(20190622, "Union_Raid_Hp", hp1);
      cm.warp(680000175);
        cm.dispose();
    }
}

function getAtk() {
   var UnionAtt = 0;
   var getUnionChr = 0;
   var db = DatabaseConnection; // 얘를 닫으면 안되는거
        var con = db.getConnection();
        var getAcc = con.prepareStatement("SELECT * FROM characters WHERE accountid = " + cm.getClient().getAccID() + " ORDER BY level DESC").executeQuery();
        while (getAcc.next()) {
            if (getUnionChr < 40) {
                if (getAcc.getInt("level") >= 60) {
                getUnionChr++;
                    UnionAtt += getAtkByLevel(getAcc.getInt("level"))
                }
            }
        }
        getAcc.close();
   return Math.floor(UnionAtt);
}

function getAtkByLevel(chrlevel) {
   if (chrlevel >= 240) {
      atk = 1.25
   } else if (chrlevel >= 230) {
      atk = 1.2
   } else if (chrlevel >= 220) {
      atk = 1.15
   } else if (chrlevel >= 210) {
      atk = 1.1
   } else if (chrlevel >= 200) {
      atk = 1
   } else if (chrlevel >= 180) {
      atk = 0.8
   } else if (chrlevel >= 140) {
      atk = 0.7
   } else if (chrlevel >= 100) {
      atk = 0.4
   } else {
      atk = 0.5
   }
   return ((chrlevel * chrlevel * chrlevel) * atk) + 12500;
}
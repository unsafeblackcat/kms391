importPackage(Packages.constants);
importPackage(Packages.server);
importPackage(Packages.database);
importPackage(Packages.tools.packet);

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
    outmap2 = 921172201
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        if (cm.getPlayer().getMapId() != outmap && cm.getPlayer().getMapId() != outmap2) {
            talk = "#r#e巨龍討伐戰#k#n正等待勇者挑戰！\r\n"
            talk += "是否要進入#b#e公會聯合副本#k#n？"
            cm.sendYesNoS(talk,0x04,9010106);
        } else {
            if (coin == 0) {
                talk = "看來您還沒獲得任何公會幣呢？如果覺得太困難，可以稍作休息再來挑戰。其他公會成員會持續累積獎勵的~"
            } else {
               talk = "已獲得#i4310229# #b#z4310229##k共#b" + coin + "枚#k！真是厲害！"
            }
            cm.sendNextS(talk,0x04,9010106);
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() != outmap && cm.getPlayer().getMapId() != outmap2) {
            if (cm.getClient().getKeyValue("UnionLaidLevel") == null) {
                cm.getClient().setKeyValue("UnionLaidLevel", "1");
            }
            cm.getPlayer().setSkillCustomInfo(232471, cm.getPlayer().getMapId(), 0);
            mobid = cm.getClient().getKeyValue("UnionLaidLevel") == 1 ? 9833101 : cm.getClient().getKeyValue("UnionLaidLevel") == 2 ? 9833102 : cm.getClient().getKeyValue("UnionLaidLevel") == 3 ? 9833103 : cm.getClient().getKeyValue("UnionLaidLevel") == 4 ? 9833104 : cm.getClient().getKeyValue("UnionLaidLevel") == 5 ? 9833105 : 0;
            mobhp = 10000000000000;
            nowmobhp = (mobhp - (cm.getPlayer().getUnionAllNujuk() + cm.getPlayer().getUnionNujuk()));
            mobid2 = mobid + 100;
            mob2hp = 2500000000000;
            nowmob2hp = (mob2hp - cm.getPlayer().getUnionNujuk());
            rand = Randomizer.isSuccess(50) ? 921172000 : 921172100;
            cm.warp(rand);
            if (nowmobhp <= 0) {// 단계초기화
                cm.getPlayer().setUnionAllNujuk(0);
                cm.getPlayer().setUnionNujuk(0);
                cm.getPlayer().setUnionEnterTime(0);
                abc = cm.getClient().getKeyValue("UnionLaidLevel") == 1 ? 2 : cm.getClient().getKeyValue("UnionLaidLevel") == 2 ? 3 : cm.getClient().getKeyValue("UnionLaidLevel") == 3 ? 4 : cm.getClient().getKeyValue("UnionLaidLevel") == 4 ? 5 : cm.getClient().getKeyValue("UnionLaidLevel") == 5 ? 1 : 0;
                nowmobhp = mobhp;
                nowmob2hp = mob2hp;
                cm.getPlayer().getClient().setKeyValue("UnionLaidLevel", ""+abc+"");
            }
            if (nowmob2hp <= 0) {
                nowmob2hp = 1;
            }
            mobid = cm.getClient().getKeyValue("UnionLaidLevel") == 1 ? 9833101 : cm.getClient().getKeyValue("UnionLaidLevel") == 2 ? 9833102 : cm.getClient().getKeyValue("UnionLaidLevel") == 3 ? 9833103 : cm.getClient().getKeyValue("UnionLaidLevel") == 4 ? 9833104 : cm.getClient().getKeyValue("UnionLaidLevel") == 5 ? 9833105 : 0;
            mobid2 = mobid + 100;
            cm.spawnLinkMobsetHP(mobid, 2320,17, nowmobhp, mobhp);
            cm.spawnLinkMobsetHP(mobid2, 2320,17, nowmob2hp, mob2hp);
          //  cm.getPlayer().getClient().send(SLFCGPacket.CameraCtrl(13, 1, 100, 800, 800));
            cm.getPlayer().getClient().send(CField.setUnionRaidScore(cm.getPlayer().getUnionAllNujuk()));
            cm.getPlayer().getClient().send(CField.showUnionRaidHpUI(mobid, nowmob2hp, mob2hp, mobid2, nowmobhp, mobhp));
            cm.getPlayer().getClient().send(CField.setUnionRaidCoinNum(cm.getPlayer().getUnionCoin(), true));
            cm.dispose();
	        return;
        } else {
            cm.sendNextS(" 即將傳送回原處，祝您冒險愉快~",0x04,9010106);
        }
    } else if (status == 2) {
        cm.warp(cm.getPlayer().getSkillCustomValue0(232471));
        cm.dispose();
    }
}
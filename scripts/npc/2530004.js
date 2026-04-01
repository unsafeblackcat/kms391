


/*

제작 : 피스(qudtlstorl79@nate.com)

*/
importPackage(Packages.server);

var status = -1;
var sel = 0;
var HuntCoin1 = 4319999;
var HuntCoin10000 = 4319999;
var needcoin = 0;
var needcoincount = 0;
var uptear = 0;
var tearname = "";
var suc = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        txt = "#fs15##b可以提升各種屬性等級#fc0xFF000000#的系統. \r\n選擇您想要的項目.\r\n\r\n"
        txt += "#r[注意]#k #fc0xFFD5D5D5#傷害，爆擊傷害，BOSS攻擊力有100%的概率成功，但 \r\n經驗，金幣，掉落有失敗的概率.#n#k\r\n\r\n#b"
        txt += "#L1# #d經驗升級\r\n"        
        txt += "#L5# 楓幣獲得量升級\r\n"
        txt += "#L2# 道具掉落率升級\r\n"//只加經驗屬性
        txt += "#L0# 傷害等級提升\r\n"
        txt += "#L3# 爆擊傷害等級提升\r\n"
        txt += "#L4# BOSS攻擊力等級提升\r\n"
        cm.sendSimple(txt);
    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            //데미지 티어
            tearname = "DamageTear";//傷害
            effect = "데미지";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            damageup = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = 4319999;
                    needcoincount = 0;
                    damageup = 10;
                    suc = 100;
                    break;
                case 2:
                    needcoin = 4319999;
                    needcoincount = 10000;
                    damageup = 35;
                    suc = 100;
                    break;
                case 3:
                    needcoin = 4319999;
                    needcoincount = 15000;
                    damageup = 55;
                    suc = 100;
                    break;
                case 4:
                    needcoin = 4319999;
                    needcoincount = 20000;
                    damageup = 75;
                    suc= 100;
                    break;
                case 5:
                    needcoin = 4319999;
                    needcoincount = 25000;
                    damageup = 105;
                    suc = 100;
                    break;
                case 6:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    damageup = 145;
                    suc = 100;
                    break;
                case 7:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    damageup = 190;
                    suc = 100;
                    break;
                case 8:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    damageup = 240;
                    suc = 100;
                    break;
            }

            txt = "#fs15##b當前我的傷害等級#fc0xFF000000# #e[" + mytear + "等級]#n\r\n"
            txt += "#b下一個升級排名是#fc0xFF000000# #e[" + uptear + "排名]#n\r\n"
            txt += "#b可升級的最大排名是#fc0xFF000000# #e[8排名]#n\r\n"
            txt += "升級後的排名效果和\r\n升級所需的 #r物品#fc0xFF000000#如下\r\n\r\n";
            txt += "#r#e升級成功時總計 " + effect + " + #r#e" + damageup + "%#n\r\n#fc0xFF000000# #i" + needcoin + "# #b#z" + needcoin + "# " + needcoincount + "個#k\r\n\r\n"
            txt += "#fs15##fc0xFF000000#要進行#e升級#n嗎?";
            cm.sendYesNo(txt);
        } else if (sel == 1) {
            //경험치 티어
            tearname = "ExpTear";//經驗
            effect = "경험치";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = 4319999;
                    needcoincount = 5000;
                    up = 3;
                    suc = 100;
                    break;
                case 2:
                    needcoin = 4319999;
                    needcoincount = 6000;
                    up = 10;
                    suc = 80;
                    break;
                case 3:
                    needcoin = 4319999;
                    needcoincount = 7000;
                    up = 23;
                    suc = 60;
                    break;
                case 4:
                    needcoin = 4319999;
                    needcoincount = 10000;
                    up = 31;
                    suc= 50;
                    break;
                case 5:
                    needcoin = 4319999;
                    needcoincount = 20000;
                    up = 54;
                    suc = 45;
                    break;
                case 6:
                    needcoin = 4319999;
                    needcoincount = 30000;//2個필요
                    up = 84;//데미지
                    suc = 30;
                    break;
                case 7:
                    needcoin = 4319999;
                    needcoincount = 30000;//2個필요
                    up = 124;//데미지
                    suc = 20;
                    break;
                case 8:
                    needcoin = 4319999;
                    needcoincount = 30000;//2個필요
                    up = 184;//데미지
                    suc = 10;
                    break;
            }

            txt = "#fs15##b#e<" + effect + " 階段>#n#k\r\n\r\n"
            txt += "當前我的階段 : #e#r" + mytear + "#n#k階段\r\n"
            txt += "下一個升級階段是 #e[" + uptear + "階段]#n\r\n"
            txt += "升級階段的效果和\r\n升級所需的 #r物品#k如下\r\n\r\n";
            txt += "" + effect + " + #r#e" + up + "%#n#k #i" + needcoin + "# #b#z" + needcoin + "# " + needcoincount + "個#k\r\n\r\n"
            txt += "#fs15#要進行#e升級#n嗎";
            cm.sendYesNo(txt);
        } else if (sel == 2) {
            //드롭 티어
            tearname = "DropTear";//掉落
            effect = "드롭";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = 4319999;
                    needcoincount = 6000;
                    up = 40;
                    suc = 100;
                    break;
                case 2:
                    needcoin = 4319999;
                    needcoincount = 8000;
                    up = 80;
                    suc = 80;
                    break;
                case 3:
                    needcoin = 4319999;
                    needcoincount = 9000;
                    up = 120;
                    suc = 70;
                    break;
                case 4:
                    needcoin = 4319999;
                    needcoincount = 10000;
                    up = 150;
                    suc= 60;
                    break;
                case 5:
                    needcoin = 4319999;
                    needcoincount = 11000;
                    up = 180;
                    suc = 50;
                    break;
                case 6:
                    needcoin = 4319999;
                    needcoincount = 12000;
                    up = 220;
                    suc = 40;
                    break;
                case 7:
                    needcoin = 4319999;
                    needcoincount = 13000;
                    up = 260;
                    suc = 30;
                    break;
                case 8:
                    needcoin = 4319999;
                    needcoincount = 15000;
                    up = 300;
                    suc = 20;
                    break;
            }

            txt = "當前我的階段 : #e#r" + mytear + "#n#k階段\r\n"
            txt += "下一個升級階段是 #e[" + uptear + "階段]#n\r\n"
            txt += "升級階段的效果和\r\n升級所需的#r物品#k如下\r\n\r\n";
            txt += "" + effect + " + #r#e" + up + "%\r\n#n#k #i" + needcoin + "# #b#z" + needcoin + "# " + needcoincount + "個#k\r\n\r\n"
            txt += "#fs15#要進行#e升級#n嗎?";
            cm.sendYesNo(txt);
        } else if (sel == 3) {
            //크뎀 티어
            tearname = "CridamTear";
            effect = "크리데미지";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = 4319999;
                    needcoincount = 5000;
                    up = 10;
                    suc = 100;
                    break;
                case 2:
                    needcoin = 4319999;
                    needcoincount = 10000;
                    up = 15;
                    suc = 100;
                    break;
                case 3:
                    needcoin = 4319999;
                    needcoincount = 15000;
                    up = 30;
                    suc = 100;
                    break;
                case 4:
                    needcoin = 4319999;
                    needcoincount = 20000;
                    up = 50;
                    suc= 100;
                    break;
                case 5:
                    needcoin = 4319999;
                    needcoincount = 25000;
                    up = 80;
                    suc = 100;
                    break;
                case 6:
                    needcoin = 4319999;//만個짜리코인
                    needcoincount = 30000;
                    up = 130;//데미지
                    suc = 100;
                    break;
                case 7:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    up = 200;//데미지
                    suc = 100;
                    break;
                case 8:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    up = 300;//데미지
                    suc = 100;
                    break;
            }

            txt = "#fs15##b當前我的爆擊傷害 等級#k #e[" + mytear + "等級]#n\r\n"
            txt += "#b下一個升級等級 #k#e[" + uptear + "等級]#n\r\n"
            txt += "#b可升級的最大排名是#k #e[8等級]#n\r\n"
            txt += "升級排名的效果和\r\n升級所需的#r物品#k如下\r\n\r\n";
            txt += "#r#e升級成功時總計 " + effect + " + #r#e" + up + "%#k\r\n#n#k #i" + needcoin + "# #b#z" + needcoin + "# " + needcoincount + "個#k\r\n\r\n"
            txt += "#fs15#要進行#e升級#n嗎?";
            cm.sendYesNo(txt);
        } else if (sel == 4) {
            //보공 티어
            tearname = "BossdamTear";//BOSS攻擊力
            effect = "보스 공격력";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = 4319999;
                    needcoincount = 5000;
                    up = 10;
                    suc = 100;
                    break;
                case 2:
                    needcoin = 4319999;
                    needcoincount = 10000;
                    up = 35;
                    suc = 100;
                    break;
                case 3:
                    needcoin = 4319999;
                    needcoincount = 15000;
                    up = 55;
                    suc = 100;
                    break;
                case 4:
                    needcoin = 4319999;
                    needcoincount = 20000;
                    up = 75;
                    suc= 100;
                    break;
                case 5:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    up = 105;
                    suc = 100;
                    break;
                case 6:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    up = 145;//데미지
                    suc = 100;
                    break;
                case 7:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    up = 190;//데미지
                    suc = 100;
                    break;
                case 8:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    up = 240;//데미지
                    suc = 100;
                    break;
            }

            txt = "#fs15##b現在我的BOSS攻擊力等級#k #e[" + mytear + "等級]#n\r\n"
            txt += "#b下一個升級等級 #k#e[" + uptear + "等級]#n\r\n"
            txt += "#b可升級的最大排名是#k #e[8等級]#n\r\n"
            txt += "升級排名的效果和\r\n升級所需的#r物品#k如下\r\n\r\n";
            txt += "#r#e升級成功時總計 " + effect + " + #r#e" + up + "%#k\r\n#n#k #i" + needcoin + "# #b#z" + needcoin + "# " + needcoincount + "個#k\r\n\r\n"
            txt += "#fs15#要進行#e升級#n嗎?";
            cm.sendYesNo(txt);
        } else if (sel == 5) {
            //메소 티어
            tearname = "MesoTear";//楓幣獲得率
            effect = "메소";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = 4319999;
                    needcoincount = 3000;
                    up = 10;
                    suc = 100;
                    break;
                case 2:
                    needcoin = 4319999;
                    needcoincount = 4000;
                    up = 30;
                    suc = 90;
                    break;
                case 3:
                    needcoin = 4319999;
                    needcoincount = 5000;
                    up = 40;
                    suc = 80;
                    break;
                case 4:
                    needcoin = 4319999;
                    needcoincount = 8000;
                    up = 60;
                    suc= 70;
                    break;
                case 5:
                    needcoin = 4319999;
                    needcoincount = 10000;
                    up = 80;
                    suc = 60;
                    break;
                case 6:
                    needcoin = 4319999;//만個짜리코인
                    needcoincount = 12000;//2個필요
                    up = 120;//데미지
                    suc = 55;
                    break;
                case 7:
                    needcoin = 4319999;//만個짜리코인
                    needcoincount = 15000;//2個필요
                    up = 180;//데미지
                    suc = 50;
                    break;
                case 8:
                    needcoin = 4319999;//만個짜리코인
                    needcoincount = 20000;//2個필요
                    up = 300;//데미지
                    suc = 40;
                    break;
            }

            txt = "#fs15##b#e<" + effect + " 等級>#n#k\r\n\r\n"
            txt += "當前我的階段 : #e#r" + mytear + "#n#k等級\r\n"
            txt += "下一個升級階段是 #e[" + uptear + "等級]#n이며\r\n"
            txt += "升級階段的效果和\r\n升級所需的#r物品#k如下\r\n\r\n";
            txt += "" + effect + " + #r#e" + up + "%#n#k #i" + needcoin + "# #b#z" + needcoin + "# " + needcoincount + "個#k\r\n\r\n"
            txt += "#fs15#要進行#e升級#n嗎?";
            cm.sendYesNo(txt);
        }
    } else if (status == 2) {
        if (!cm.haveItem(needcoin, needcoincount)) {
            cm.sendOk("升級所需物品不足.");
            cm.dispose();
            return;
        }
        cm.gainItem(needcoin, -needcoincount);
        if (Randomizer.isSuccess(suc)) {
            cm.getPlayer().setKeyValue(9919, tearname, "" + uptear + "");
            cm.sendOk("祝賀你！ 升級成功了");
            cm.dispose();
        } else {
            cm.sendOk("升級失敗.");
            cm.dispose();
        }

    }
}

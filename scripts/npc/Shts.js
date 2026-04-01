importPackage(Packages.server);

var status = -1;
var sel = 0;
var HuntCoin1 = 4310009;
var HuntCoin10000 = 4310009;
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
        txt = "#fs15#各種能力的階級可以提升的系統，選擇你想要的項目吧#n#k\r\n\r\n"
        txt += "#L0##b 傷害升級"
        txt += "#L1# 經驗值升級"
        txt += "#L3# 爆擊傷害升級"
        txt += "#L4# 首領攻擊力升級\r\n"
        txt += "#L5# 楓幣升級"
        txt += "#L2# 掉落率升級\r\n"
        cm.sendSimpleS(txt,0x86);
    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            //데미지 티어
            tearname = "DamageTear";
            effect = "傷害";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            damageup = 0;
            switch (uptear) {
                case 1://1階
                    needcoin = HuntCoin1;
                    needcoincount = 1;
                    damageup = 10;
                    suc = 80;
                    break;
                case 2:
                    needcoin = HuntCoin1;
                    needcoincount = 2;
                    damageup = 15;
                    suc = 70;
                    break;
                case 3:
                    needcoin = HuntCoin1;
                    needcoincount = 3;
                    damageup = 20;
                    suc = 60;
                    break;
                case 4:
                    needcoin = HuntCoin1;
                    needcoincount = 4;
                    damageup = 25;
                    suc = 50;
                    break;
                case 5:
                    needcoin = HuntCoin1;
                    needcoincount = 5;
                    damageup = 30;
                    suc = 40;
                    break;
                case 6:
                    needcoin = HuntCoin1;
                    needcoincount = 6;
                    damageup = 40;
                    suc = 30;
                    break;
                case 7:
                    needcoin = HuntCoin1;
                    needcoincount = 7;
                    damageup = 60;
                    suc = 20;
                    break;
                case 8:
                    needcoin = HuntCoin1;
                    needcoincount = 8;
                    damageup = 80;
                    suc = 10;
                    break;
            }

            txt = "#fs15##b目前我的傷害階級#fc0xFF000000# #e[" + mytear + "階]#n\r\n"
            txt += "#b下一個晉升階級是#fc0xFF000000# #e[" + uptear + "階]#n\r\n"
            txt += "#b可晉升的最高階級是#fc0xFF000000# #e[8階]#n\r\n"
            txt += "晉升後的階級效果與\r\n晉升所需的 #r道具#fc0xFF000000#如下\r\n\r\n";
            txt += "#r#e晉升時 10% ~ 80% 隨機提升能力。#k\r\n\r\n"
            txt += "#fs15##fc0xFF000000#確定要進行 #e晉升#n 碼？";
            cm.sendYesNoS(txt,0x86);
        } else if (sel == 1) {
             //經驗值階級 
            tearname = "ExpTear";
            effect = "經驗值";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1階 
                    needcoin = HuntCoin1;
                    needcoincount = 1;
                    up = 3;
                    suc = 100;
                    break;
                case 2:
                    needcoin = HuntCoin1;
                    needcoincount = 2;
                    up = 7;
                    suc = 80;
                    break;
                case 3:
                    needcoin = HuntCoin1;
                    needcoincount = 3;
                    up = 13;
                    suc = 60;
                    break;
                case 4:
                    needcoin = HuntCoin1;
                    needcoincount = 2;
                    up = 18;
                    suc = 50;
                    break;
                case 5:
                    needcoin = HuntCoin1;
                    needcoincount = 5;
                    up = 23;
                    suc = 45;
                    break;
                case 6:
                    needcoin = HuntCoin1;//萬個硬幣
                    needcoincount = 10;//需要2個 
                    up = 30;//데미지
                    suc = 30;
                    break;
                case 7:
                    needcoin = HuntCoin1;//萬個硬幣
                    needcoincount = 30;//需要2個 
                    up = 40;//데미지
                    suc = 20;
                    break;
                case 8:
                    needcoin = HuntCoin1;//萬個硬幣
                    needcoincount = 50;//需要2個 
                    up = 50;//데미지
                    suc = 10;
                    break;
            }

            txt = "#fs15##b#e<" + effect + " 階級>#n#k\r\n\r\n"
            txt += "目前我的階級 : #e#r" + mytear + "#n#k階\r\n"
            txt += "下一個晉升階級是 #e[" + uptear + "階]#n\r\n"
            txt += "晉升後的階級效果與\r\n晉升所需的 #r道具#k如下\r\n\r\n";
            txt += "#r#e晉升時 10% ~ 80% 隨機提升能力。#k\r\n\r\n"
            txt += "#fs15#確定要進行 #e晉升#n 碼？";
            cm.sendYesNoS(txt,0x86);
        } else if (sel == 2) {
            //掉落率階級 
            tearname = "DropTear";
            effect = "掉落率";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = HuntCoin1;
                    needcoincount = 1;
                    up = 10;
                    suc = 100;
                    break;
                case 2:
                    needcoin = HuntCoin1;
                    needcoincount = 2;
                    up = 20;
                    suc = 80;
                    break;
                case 3:
                    needcoin = HuntCoin1;
                    needcoincount = 3;
                    up = 40;
                    suc = 60;
                    break;
                case 4:
                    needcoin = HuntCoin1;
                    needcoincount = 2;
                    up = 60;
                    suc = 50;
                    break;
                case 5:
                    needcoin = HuntCoin1;
                    needcoincount = 5;
                    up = 80;
                    suc = 45;
                    break;
                case 6:
                    needcoin = HuntCoin1;//萬個硬幣
                    needcoincount = 10;//需要2個 
                    up = 120;//데미지
                    suc = 30;
                    break;
                case 7:
                    needcoin = HuntCoin1;//萬個硬幣
                    needcoincount = 30;//需要2個 
                    up = 180;//데미지
                    suc = 20;
                    break;
                case 8:
                    needcoin = HuntCoin1;//萬個硬幣
                    needcoincount = 50;//需要2個 
                    up = 300;//데미지
                    suc = 10;
                    break;
            }

            txt = "目前我的階級 : #e#r" + mytear + "#n#k階\r\n"
            txt += "下一個晉升階級是 #e[" + uptear + "階]#n\r\n"
            txt += "晉升後的階級效果與\r\n晉升所需的 #r道具#k如下\r\n\r\n";
            txt += "#r#e晉升時 10% ~ 80% 隨機提升能力。#k\r\n\r\n"
            txt += "#fs15#確定要進行 #e晉升#n 碼？";
            cm.sendYesNoS(txt,0x86); 
        } else if (sel == 3) {
           //爆擊傷害階級 
            tearname = "CridamTear";
            effect = "爆擊傷害";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = HuntCoin1;
                    needcoincount = 2;
                    up = 10;
                    suc = 50;
                    break;
                case 2:
                    needcoin = HuntCoin1;
                    needcoincount = 4;
                    up = 15;
                    suc = 50;
                    break;
                case 3:
                    needcoin = HuntCoin1;
                    needcoincount = 6;
                    up = 20;
                    suc = 30;
                    break;
                case 4:
                    needcoin = HuntCoin1;
                    needcoincount = 8;
                    up = 25;
                    suc = 30;
                    break;
                case 5:
                    needcoin = HuntCoin1;
                    needcoincount = 10;
                    up = 30;
                    suc = 30;
                    break;
                case 6:
                    needcoin = HuntCoin1;//萬個硬幣
                    needcoincount = 11;
                    up = 35;//데미지
                    suc = 20;
                    break;
                case 7:
                    needcoin = HuntCoin1;
                    needcoincount = 12;
                    up = 40;//데미지
                    suc = 20;
                    break;
                case 8:
                    needcoin = HuntCoin1;
                    needcoincount = 13;
                    up = 45;//데미지
                    suc = 20;
                    break;
            }

            txt = "#fs15##b目前我的爆擊傷害階級#k #e[" + mytear + "階]#n\r\n"
            txt += "#b下一個晉升階級 #k#e[" + uptear + "階]#n\r\n"
            txt += "#b可晉升的最高階級是#k #e[8階]#n\r\n"
            txt += "晉升後的階級效果與\r\n晉升所需的 #r道具#k如下\r\n\r\n";
            txt += "#r#e晉升時 10% ~ 80% 隨機提升能力。#k\r\n\r\n"
            txt += "#fs15#確定要進行 #e晉升#n 碼？";
            cm.sendYesNoS(txt,0x86);
        } else if (sel == 4) {
           //首領攻擊力階級
            tearname = "BossdamTear";
            effect = "首領攻擊力";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = HuntCoin1;
                    needcoincount = 1;
                    up = 10;
                    suc = 50;
                    break;
                case 2:
                    needcoin = HuntCoin1;
                    needcoincount = 2;
                    up = 20;
                    suc = 50;
                    break;
                case 3:
                    needcoin = HuntCoin1;
                    needcoincount = 3;
                    up = 30;
                    suc = 30;
                    break;
                case 4:
                    needcoin = HuntCoin1;
                    needcoincount = 4;
                    up = 50;
                    suc = 30;
                    break;
                case 5:
                    needcoin = HuntCoin1;
                    needcoincount = 5;
                    up = 70;
                    suc = 30;
                    break;
                case 6:
                    needcoin = HuntCoin1;
                    needcoincount = 6;
                    up = 100;//데미지
                    suc = 20;
                    break;
                case 7:
                    needcoin = HuntCoin1;
                    needcoincount = 7;
                    up = 120;//데미지
                    suc = 20;
                    break;
                case 8:
                    needcoin = HuntCoin1;
                    needcoincount = 10;
                    up = 150;//데미지
                    suc = 20;
                    break;
            }

            txt = "#fs15##b目前我的首領攻擊力階級#k #e[" + mytear + "階]#n\r\n"
            txt += "#b下一個晉升階級 #k#e[" + uptear + "階]#n\r\n"
            txt += "#b可晉升的最高階級是#k #e[8階]#n\r\n"
            txt += "晉升後的階級效果與\r\n晉升所需的 #r道具#k如下\r\n\r\n";
            txt += "#r#e晉升時 10% ~ 80% 隨機提升能力。#k\r\n\r\n"
            txt += "#fs15#確定要進行 #e晉升#n 碼？";
            cm.sendYesNoS(txt,0x86);
        } else if (sel == 5) {
            //메소 티어
            tearname = "MesoTear";
            effect = "메소";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = HuntCoin1;
                    needcoincount = 1;
                    up = 10;
                    suc = 100;
                    break;
                case 2:
                    needcoin = HuntCoin1;
                    needcoincount = 2;
                    up = 20;
                    suc = 100;
                    break;
                case 3:
                    needcoin = HuntCoin1;
                    needcoincount = 3;
                    up = 40;
                    suc = 60;
                    break;
                case 4:
                    needcoin = HuntCoin1;
                    needcoincount = 4;
                    up = 60;
                    suc = 50;
                    break;
                case 5:
                    needcoin = HuntCoin1;
                    needcoincount = 5;
                    up = 80;
                    suc = 45;
                    break;
                case 6:
                    needcoin = HuntCoin1;//萬個硬幣
                    needcoincount = 10;//需要2個 
                    up = 90;//데미지
                    suc = 30;
                    break;
                case 7:
                    needcoin = HuntCoin1;//萬個硬幣
                    needcoincount = 30;//需要2個 
                    up = 100;//데미지
                    suc = 20;
                    break;
                case 8:
                    needcoin = HuntCoin1;//萬個硬幣
                    needcoincount = 50;//需要2個 
                    up = 120;//데미지
                    suc = 10;
                    break;
            }

            txt = "#fs15##b#e<" + effect + " 階級>#n#k\r\n\r\n"
            txt += "目前我的階級 : #e#r" + mytear + "#n#k階\r\n"
            txt += "下一個晉升階級是 #e[" + uptear + "階]#n\r\n"
            txt += "晉升後的階級效果與\r\n晉升所需的 #r道具#k如下\r\n\r\n";
            txt += "#r#e晉升時 10% ~ 80% 隨機提升能力。#k\r\n\r\n"
            txt += "#fs14#確定要進行 #e晉升#n 碼？";
            cm.sendYesNoS(txt,0x86);
        }
    } else if (status == 2) {
        if (!cm.haveItem(needcoin, needcoincount)) {
            cm.sendOk("晉升所需的道具不足。.");
            cm.dispose();
            return;
        }
        cm.gainItem(needcoin, -needcoincount);
        if (Randomizer.isSuccess(suc)) {
            cm.getPlayer().setKeyValue(9919, tearname, "" + uptear + "");
            cm.sendOk("恭喜你！晉升成功");
            cm.dispose();
        } else {
            cm.sendOk("晉升失敗。.");
            cm.dispose();
        }

    }
}

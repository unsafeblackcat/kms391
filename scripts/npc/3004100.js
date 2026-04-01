var status = -1;

var nitem = [
    [4000000, 10],
    [4000001, 10],
    [4000002, 10],
];
var allstat = 1000;
var 말 = "#fs15#"

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        말 += "#fc0xFFB677FF##e[FANCY]創世武器製作#n  \r\n\r\n\r\n"
		말 += "#fc0xFF990033#創世武器 #fc0xFF191919#製作時，請帶來以下資料.\r\n"
        for(i = 0; i < nitem.length; i++) {
            말 += "#i" + nitem[i][0] + "#  #z" + nitem[i][0] + "# " + nitem[i][1] + "個\r\n"
        }
        말 += "\r\n";
        말 += "#fc0xFF990033#創世武器#fc0xFF191919#是全固定屬性 +" + allstat + " #fc0xFFF15F5F#不可强化物品#fc0xFF191919#이다.\r\n\r\n"
		말 += "#fc0xFF990033#創世武器#fc0xFF191919#可以選擇所有潜在的6行潛在屬性.\r\n\r\n"
		말 += "#fc0xFF990033#創世武器#fc0xFF191919#如果不想製作，請選擇否.\r\n\r\n"
        cm.sendYesNo(말);
    } else if (status == 1) {
        말 = "";
        if (checkitem()) {
            말 += "#fs15#製作#z2439614#的資料不足.\r\n\r\n"
            말 += "多收集以下物品.\r\n\r\n"
            needitem();            
            cm.sendOk(말);
            cm.dispose();
            return;
        }
        for(i = 0; i < nitem.length; i++) {
            cm.gainItem(nitem[i][0], -nitem[i][1]);
        }
        cm.gainItem(2439614, 1);
        cm.sendOk("#fs15#製作了#i2439614##z2439614# !");
        cm.dispose();
    }
}

function checkitem() {
    for(i = 0; i < nitem.length; i++) {
        if(!cm.haveItem(nitem[i][0], nitem[i][1])) {
            return true;
        }
    }
    return false;
}

function needitem() {
    for(i = 0; i < nitem.length; i++) {
        if(!cm.haveItem(nitem[i][0], nitem[i][1])) {
            말 += "#i" + nitem[i][0] + "#  #z" + nitem[i][0] + "# " + (nitem[i][1] - cm.itemQuantity(nitem[i][0])) + "個\r\n";
        }
    }
}
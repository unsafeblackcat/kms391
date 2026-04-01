var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    2046972,
    2046973,
    2047842,
    2047843,
    2046851,
    2046852,
    2047923,
    2047924,
    2048067,
    2048068,
    2049223,
    2049224,
];

function start() {
    status = -1;
    action(1, 1, 0);
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
        var amed = "#fs15##d#h0#請選擇想要的新命運卷軸。#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2634290, -1);
        cm.dispose();
    }
}


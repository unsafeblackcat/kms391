var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    2046974,
    2046975,
    2047845,
    2047846,
    2046853,
    2046854,
    2047925,
    2047926,
    2048069,
    2048070,
    2049225,
    2049226,
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
        var amed = "#fs15##d#h0#請選擇想要的救世卷軸。#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2634289, -1);
        cm.dispose();
    }
}


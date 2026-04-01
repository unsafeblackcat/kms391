var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    2046996,
    2046997,
    2047818,
    2047819,
    2046844,
    2046845,
    2046846,
    2049212,
    2049213,
    2049214,
    2047942,
    2047943,
    2047944,
    2048090,
    2048091,
    2048092,
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
        var amed = "#fs15##d#h0#請選擇想要的榮耀卷軸。#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2634285, -1);
        cm.dispose();
    }
}


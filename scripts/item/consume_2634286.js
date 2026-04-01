var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    2046957,
    2046958,
    2047838,
    2047839,
    2046847,
    2046848,
    2047919,
    2047920,
    2048063,
    2048064,
    2049219,
    2049220,
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
        var amed = "#fs15##d#h0#請選擇想要的究極黑暗卷軸。#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2634286, -1);
        cm.dispose();
    }
}


var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    2046959,
    2046960,
    2047840,
    2047841,
    2046849,
    2046850,
    2047921,
    2047922,
    2048065,
    2048066,
    2049221,
    2049222,
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
        var amed = "#fs15##d#h0#請選擇想要的璀璨之光卷軸。#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2634287, -1);
        cm.dispose();
    }
}


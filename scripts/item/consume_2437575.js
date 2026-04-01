var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1005980,
    1005981,
    1005982,
    1005983,
    1005984,
    1042433,
    1042434,
    1042435,
    1042436,
    1042437,
    1062285,
    1062286,
    1062287,
    1062288,
    1062289,
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
        var amed = "#fs15##d#h0#님, 원하시는 에테르넬 방어구를 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2437575, -1);
        cm.dispose();
    }
}

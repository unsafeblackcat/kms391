var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1162080, // 저주받은 적의 마도서
    1162081, // 저주받은 청의 마도서
    1162082, // 저주받은 녹의 마도서
    1162083, // 저주받은 황의 마도서
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
        var amed = "#fs15##d#h0#님, 원하시는 마도서를 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2633926, -1);
        cm.dispose();
    }
}

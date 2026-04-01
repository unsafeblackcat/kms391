var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1012632,
    1022278,
    1132308,
    2633926,
    //1672082,
    1122430,
    1032316,
    1113306,
    1182285,
    2633927,
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
        var amed = "#fs15##d#h0#님, 원하시는 칠흑의 보스 장신구를 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2437573, -1);
        cm.dispose();
    }
}

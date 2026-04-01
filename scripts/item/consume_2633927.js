var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1190555, // 미트라의 분노 : 전사
    1190556, // 미트라의 분노 : 궁수
    1190557, // 미트라의 분노 : 마법사
    1190558, // 미트라의 분노 : 도적
    1190559, // 미트라의 분노 : 해적
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
        var amed = "#fs15##d#h0#님, 원하시는 미트라의 분노를 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2633927, -1);
        cm.dispose();
    }
}

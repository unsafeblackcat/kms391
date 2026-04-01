var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1113513, 1113512, 1113511, 1113510, 1002026, 1022073, 1012260, 1122058, 1122007, 1132145, 1042352, 1062000, 1072369, 1032160, 1152074, 1082145, 1102369, 1662183, 1672028, 1162004, 1190313, 1182263,
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
        var amed = "#fs15##d#h0#님, 원하시는 커스텀장비를 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2432118, -1);
        cm.dispose();
    }
}


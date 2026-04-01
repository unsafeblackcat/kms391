var status;
var select = -1;
var enter = "\r\n";
var itemid = [
	1004422, //모자
	1004423,
	1004424,
	1004425,
	1004426,
	1102775, //망토
	1102794,
	1102795,
	1102796,
	1102797,
	1082636, //장갑
	1082637,
	1082638,
	1082639,
	1082640,
	1052882, //한벌옷
	1052887,
	1052888,
	1052889,
	1052890,
	1073030, //신발
	1073032,
	1073033,
	1073034,
	1073035,
	1152174, //견장
	1152176,
	1152177,
	1152178,
	1152179,
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
        var amed = "#fs15##d#h0#님, 원하시는 앱솔랩스 방어구를 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2633913, -1);
        cm.dispose();
    }
}

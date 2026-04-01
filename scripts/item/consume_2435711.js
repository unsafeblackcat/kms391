var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1103000, // [Cao]아케인셰이드 나이트케이프
    1106000, // [Cao]아케인셰이드 메이지케이프
    1107000, // [Cao]아케인셰이드 아처케이프
    1105000, // [Cao]아케인셰이드 시프케이프
    1108000, // [Cao]아케인셰이드 파이렛케이프
    1088885, // [Cao]아케인셰이드 나이트글러브
    1088887, // [Cao]아케인셰이드 메이지글러브
    1088888, // [Cao]아케인셰이드 아처글러브
    1088886, // [Cao]아케인셰이드 시프글러브
    1088889, // [Cao]아케인셰이드 파이렛글러브
    1079995, // [Cao]아케인셰이드 나이트슈즈
    1079997, // [Cao]아케인셰이드 메이지슈즈
    1079998, // [Cao]아케인셰이드 아처슈즈
    1079996, // [Cao]아케인셰이드 시프슈즈
    1079999, // [Cao]아케인셰이드 파이렛슈즈
    1155555, // [Cao]아케인셰이드 나이트숄더
    1155557, // [Cao]아케인셰이드 메이지숄더
    1155558, // [Cao]아케인셰이드 아처숄더
    1155556, // [Cao]아케인셰이드 시프숄더
    1155559 // [Cao]아케인셰이드 파이렛숄더
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
        var amed = "#fs15##d#h0#님, 원하시는 [Cao]아케인셰이드 방어구를 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2435711, -1);
        cm.dispose();
    }
}

var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1009995, // [Cao]에테르넬 나이트햇
    1009996, // [Cao]에테르넬 메이지햇
    1009997, // [Cao]에테르넬 아처햇
    1009998, // [Cao]에테르넬 시프햇
    1009999, // [Cao]에테르넬 파이렛햇
    1042444, // [Cao]에테르넬 나이트아머
    1042544, // [Cao]에테르넬 메이지로브
    1042644, // [Cao]에테르넬 아처후드
    1042744, // [Cao]에테르넬 시프셔츠
    1042844, // [Cao]에테르넬 파이렛코트
    1062555, // [Cao]에테르넬 나이트팬츠
    1062666, // [Cao]에테르넬 메이지팬츠
    1062777, // [Cao]에테르넬 아처팬츠
    1062888, // [Cao]에테르넬 시프팬츠
    1062999, // [Cao]에테르넬 파이렛팬츠
    
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
        var amed = "#fs15##d#h0#님, 원하시는 [Cao]에테르넬 방어구를 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2435712, -1);
        cm.dispose();
    }
}

var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1212115, // 샤이닝로드
    1213017, // 튜너
    1214017, // 브레스슈터
    1222109, // 소울슈터
    1232109, // 데스페라도
    1242120, // 에너지소드도적
    1242116, // 에너지소드해적
    1262017, // ESP리미터
    1272016, // 체인
    1282016, // 매직 건틀렛
    1292017, // 초선
    1302333, // 세이버
    1312199, // 엑스
    1322250, // 해머
    1332274, // 대거
    1342101, // 블레이드
    1362135, // 케인
    1372222, // 완드
    1382259, // 스태프
    1402251, // 투핸드소드
    1412177, // 투핸드엑스
    1422184, // 투핸드해머
    1432214, // 스피어
    1442268, // 폴암
    1452252, // 보우
    1462239, // 크로스보우
    1472261, // 가즈
    1482216, // 클로
    1492231, // 피스톨
    1522138, // 듀얼보우건
    1532144, // 시즈건
    1582017, // 엘라하
    1592019, // 에인션트 보우
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
        var amed = "#fs15##d#h0#님, 원하시는 앱솔랩스 무기를 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2633912, -1);
        cm.dispose();
    }
}

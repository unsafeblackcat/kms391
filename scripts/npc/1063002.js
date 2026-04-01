var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.sendYesNo("#fn나눔고딕 Extrabold##d(흰색 꽃 무더기 너머로 무언가가 반짝이는걸?...)#k\r\n\r\n이 반짝이는 가장 #b흰색 꽃#k 을 꺽어 가져 가시겠습니까?");
    } else if (status == 1) {
        cm.sendNextS("#fn나눔고딕 Extrabold##k좋아!.. #b#fs15##i4310009##z4310009# 500개#fs15##k 를 손에 얻었어!#k", 2);
   } else if (status == 2) {
        cm.warp(993225700,0);
        cm.gainItem(4310009,500);
    }
}

var itmelist = [

[2439545, 500000, 1], //오토루팅
[2437717, 500000, 1], //죽음의데스로이드 (남) 교환권
[5060048, 10000, 1], //골드애플
[5062503, 8000, 1], // 화이트 에디셔널 큐브
[5062005, 8000, 1], // 어매이징 미라클 큐브
[4001780, 10000, 1], // 일반돌림판
[2430030, 40000, 1], // 보스초기화
[2049704, 2000, 1], //레전드리 잠재 주문서
[2643133, 30000, 1], // 어웨이크링 주문서
[2046400, 30000, 1], // 후원 무기 줌서 공
[2046402, 30000, 1], // 후원 무기 줌서 공
[2046401, 30000, 1], // 후원 무기 줌서 마
[2046408, 30000, 1], // 후원 펫공
[2046409, 30000, 1], // 후원 펫마
[2046405, 30000, 1], // 후원 악세 공
[2046406, 30000, 1], // 후원 악세 마
[2046403, 30000, 1], // 후원 방어구 공
[2046404, 30000, 1], // 후원 방어구 마




];

function start() {
status = -1;
action(1, 0, 0);
}

function action(mode, type, selection) {
if (mode == -1) {
    cm.dispose();
} else {
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
}
if (status == 0) {
    var a = "#fs15##fc0xFFFF3366##h0# #fc0xFF000000#的文宣點 : #fc0xFFFF3366#" + cm.getPlayer().getHPoint() + " P#k#n\r\n";
    for (var i = 0; i < itmelist.length; i++) {
        a += "#L" + i + "##i" + itmelist[i][0] + "# #d#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 個\r\n               #fc0xFF000000#宣傳點#k #e#fc0xFFFF3366#" + itmelist[i][1] + " P#k#n\r\n";
    }
 /*   for (var i = 18; i < 24; i++) {
        a += "#L" + i + "##i" + itmelist[i][0] + "# #d#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 개\r\n               #fc0xFF000000#b推廣積分#fc0xFF000000# #e#fc0xFFFF3366#" + itmelist[i][1] + " P#k#n\r\n               #r \r\n";
    }
    for (var i = 24; i < itmelist.length; i++) {
        a += "#L" + i + "##i" + itmelist[i][0] + "# #d#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 개\r\n               #fc0xFF000000#b推廣積分#fc0xFF000000# #e#fc0xFFFF3366#" + itmelist[i][1] + " P#k#n\r\n               #r\r\n";
    }*/
    cm.sendSimple(a);

} else if (status == 1) {
    sel = selection;
    cm.sendGetNumber("#fs15#購買幾個?", 1, 1, 100);
    cm.sendOk("#fs15##b宣傳積分#k 不足.");

} else if (status == 2) {
    count = selection;
    if (sel >= 0 && sel <= itmelist.length) {
        if (cm.getPlayer().getHPoint() >= itmelist[sel][1] * count) {
            if (cm.canHold(itmelist[7][0]) || cm.canHold(itmelist[8][0])) {
                cm.sendOk("#fs15#已用#b推廣積分#k 購買了 #i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 個#k 購買成功.");
                cm.dispose();
            }
            if (cm.canHold(itmelist[sel][0])) {
                cm.sendOk("#fs15#已用#bb推廣積分#k 購買了 #i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 個#k 購買成功.");
                cm.dispose();
            }
            cm.getPlayer().gainHPoint(-(itmelist[sel][1] * count));
            cm.gainItem(itmelist[sel][0], itmelist[sel][2] * count);
            cm.sendOk("#fs15#已用#bb推廣積分#k 購買了 #i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 個#k 購買成功.");
            cm.dispose();
        } else {
            cm.sendOk("#fs15##bb推廣積分#k 不足.");
            cm.dispose();
        }
    }
}
}


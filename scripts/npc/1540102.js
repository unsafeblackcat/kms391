var itmelist = [
    [2437542, 50000, 1], //스타터패키지
    [2437547, 100000, 1], //아케인 마스터 패키지
    [2437546, 150000, 1], //자석펫 패키지
    [2437543, 150000, 1], //안드로이드 패키지
    [2437544, 150000, 1], //큐브패키지
    [2437576, 50000, 1], // 메소패키지
    [2437578, 75000, 1], // 유니온프리패스패키지
    [2430048, 5000, 1], // 유니온프리패스 단품
    [5062005, 15000, 1], // 어메큐
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
    [0, "상품준비중", 0, 1], // 위대한 소울 선택상자
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
        var a = "#fs15##fc0xFFFF3366##h0# #fc0xFF000000#님의 패키지 포인트 : #fc0xFFFF3366#" + cm.getPlayer().getPPoint() + " P#k#n\r\n";
        for (var i = 0; i < 18; i++) {
            a += "#L" + i + "##i" + itmelist[i][0] + "# #d#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 개\r\n               #fc0xFF000000#패키지 포인트#k #e#fc0xFFFF3366#" + itmelist[i][1] + " P#k#n\r\n";
        }
        for (var i = 18; i < 24; i++) {
            a += "#L" + i + "##i" + itmelist[i][0] + "# #d#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 개\r\n               #fc0xFF000000#패키지 포인트#fc0xFF000000# #e#fc0xFFFF3366#" + itmelist[i][1] + " P#k#n\r\n               #r \r\n";
        }
        for (var i = 24; i < itmelist.length; i++) {
            a += "#L" + i + "##i" + itmelist[i][0] + "# #d#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 개\r\n               #fc0xFF000000#패키지 포인트#fc0xFF000000# #e#fc0xFFFF3366#" + itmelist[i][1] + " P#k#n\r\n               #r\r\n";
        }
        cm.sendSimple(a);
    
    } else if (status == 1) {
        sel = selection;
        cm.sendGetNumber("몇개를 구매?", 1, 1, 100);
        cm.sendOk("#fs15##b패키지 포인트#k 가 부족합니다.");
    
    } else if (status == 2) {
        count = selection;
        if (sel >= 0 && sel <= itmelist.length) {
            if (cm.getPlayer().getPPoint() >= itmelist[sel][1] * count) {
                if (cm.canHold(itmelist[7][0]) || cm.canHold(itmelist[8][0])) {
                    cm.sendOk("#b패키지 포인트#k 로 #i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 개#k 를 구입 하셨습니다.");
                    cm.dispose();
                }
                if (cm.canHold(itmelist[sel][0])) {
                    cm.sendOk("#b패키지 포인트#k 로 #i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 개#k 를 구입 하셨습니다.");
                    cm.dispose();
                }
                cm.getPlayer().gainPPoint(-(itmelist[sel][1] * count));
                cm.gainItem(itmelist[sel][0], itmelist[sel][2] * count);
                cm.sendOk("#b패키지 포인트#k 로 #i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 개#k 를 구입 하셨습니다.");
                cm.dispose();
            } else {
                cm.sendOk("#fs15##b패키지 포인트#k 가 부족합니다.");
                cm.dispose();
            }
        }
    }
    }
    
    
    
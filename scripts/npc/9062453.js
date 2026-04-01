importPackage(java.lang);

var list = [ //아이템코드, 묶음갯수, 개당가격
  //  [5121060, 3, 100], //경뿌 50%
    //[2450163, 3, 150], //경험치 3배쿠폰
    [2633915, 1, 150], //아케인방어구상자
    [2633914, 1, 200], //아케인무기상자
    [4319999, 200, 100], //하얀 결정 - F
    [4310308, 1, 100], //네오코어
    [4001716, 3, 250], //10억 	
	[5062005, 6, 300],  //어메큐
    [5062503, 6, 300], //화에큐
    [4319994, 200, 500], //푸른구슬	
    [2049376, 1, 500], //20성강화권
];

var choice = -1;
var count = -1;
var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    var say = "#fs15#"
    if (status == 0) {
        // 501368, "point"
       // cm.getPlayer().setKeyValue(501368, "point", 200);
        var jampoint = cm.getPlayer().getKeyValue(501368, "point"); 
        if (cm.getPlayer().getKeyValue(501368, "point") == null) {
            cm.getPlayer().setKeyValue(501368, "point", 0);
        }
        say += "   #fs15#歡迎來到潛水點商店.請選擇要購買的物品.\r\n\r\n";
        say += "   當前潛水點 : " + jampoint + "\r\n\r\n";
        for (var i = 0; i < list.length; i++) {
            if(list[i][0] == 5121060) {
                say += "#L" + i + "##i" + list[i][0] + "##b 經驗值 50%#k (" + list[i][1] + "個) - #r" + list[i][2] + "P#k#l\r\n";
                continue;
            }
            say += "#L" + i + "##i" + list[i][0] + "##b #z" + list[i][0] + "##k (" + list[i][1] + "個) - #r" + list[i][2] + "P#k#l\r\n";
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        choice = selection;
        say += "#fs15#您選擇了#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "個 套裝).\r\n";
        say += "您要購買幾捆? 每捆需要 " + list[choice][2] + "潜水點.\r\n\r\n";
        say += "當前潜水點 : " + cm.getPlayer().getKeyValue(501368, "point") + "\r\n\r\n";
        if (list[choice][0] >= 2000000) {
            cm.sendGetNumber(say, 0, 1, Math.floor(30000/list[choice][1]));
        } else {
            cm.sendGetNumber(say, 0, 1, 1);
        }
    } else if (status == 2) {
        count = selection;
        cm.sendYesNo("#fs15#真的要購買個 #i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "套裝)을 " + count + "?\r\n" +
        "需要“總計” " + (list[choice][2] * count) + "潜水點.\r\n" +
        "當前潜水點 : " + cm.getPlayer().getKeyValue(501368, "point") + "");
    } else if (status == 3) {
        if (Integer.parseInt(cm.getPlayer().getKeyValue(501368, "point")) >= list[choice][2] * count && cm.canHold(list[choice][0], list[choice][1] * count)) {
            cm.getPlayer().setKeyValue(501368, "point", Integer.parseInt(cm.getPlayer().getKeyValue(501368, "point")) - list[choice][2] * count + "");
            cm.gainItem(list[choice][0], list[choice][1] * count);
            cm.sendOk("購買了#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "個） " + count + ". \r\n\r\n" +
            "當前潜水點: " + cm.getPlayer().getKeyValue(501368, "point") + "");
            cm.dispose();
            return;
        } else {
            cm.sendOk("#fs15#請確認潜水點不足或背包欄不足.");
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}


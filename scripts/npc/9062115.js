importPackage(java.lang);

var list = [ //아이템코드, 묶음갯수, 개당가격
    [2049704, 1, 100],
	[5062503, 5, 200],
    [5062005, 5, 200],    
    [4319999, 1000, 500], 
	[4310308, 1, 500],
    //[4319996, 2, 800],
    //[4319995, 2, 800],
	[4001716, 20, 1000],
    [4319994, 300, 1000], 
    //[4319997, 180, 1000],
    [2049376, 1, 1000],
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
        var huntcoin = cm.getPlayer().getStarDustCoin2();
        if (cm.getPlayer().getKeyValue(501661, "point") == null) {
            cm.getPlayer().setKeyValue(501661, "point", 0);
        }
        say += "   歡迎來到狩獵積分商店.請選擇要購買的物品.\r\n\r\n";
        say += "   當前狩獵點數 : " + huntcoin + "\r\n\r\n";
        for (var i = 0; i < list.length; i++) {
            if(list[i][0] == 5121060) {
                say += "#L" + i + "##i" + list[i][0] + "##b 散發經驗50%卷#k (" + list[i][1] + "個) - #r" + list[i][2] + "P#k#l\r\n";
                continue;
            }
            say += "#L" + i + "##i" + list[i][0] + "##b #z" + list[i][0] + "##k (" + list[i][1] + "個) - #r" + list[i][2] + "P#k#l\r\n";
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        choice = selection;
        say += "您選擇了#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "個 套裝).\r\n";
        say += "您要購買多少捆？每捆需要 " + list[choice][2] + "狩獵點數.\r\n\r\n";
        say += "當前狩獵點數 : " + cm.getPlayer().getKeyValue(501661, "point") + "\r\n\r\n";
        if (list[choice][0] >= 2000000) {
            cm.sendGetNumber(say, 0, 1, Math.floor(30000/list[choice][1]));
        } else {
            cm.sendGetNumber(say, 0, 1, 1);
        }
    } else if (status == 2) {
        count = selection;
        cm.sendYesNo("真的要 #i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "個 套裝) " + count + "要購買嗎?\r\n" +
        "需要“總計”" + (list[choice][2] * count) + "狩獵點數.\r\n" +
        "當前狩獵點數 : " + cm.getPlayer().getKeyValue(501661, "point") + "");
    } else if (status == 3) {
        if (Integer.parseInt(cm.getPlayer().getKeyValue(501661, "point")) >= list[choice][2] * count && cm.canHold(list[choice][0], list[choice][1] * count)) {
             cm.getPlayer().AddStarDustCoin2(- list[choice][2] * count);
            cm.gainItem(list[choice][0], list[choice][1] * count);
            cm.sendOk("#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "個套裝）購買了 " + count + "個 . \r\n\r\n" +
            "當前狩獵點 : " + cm.getPlayer().getKeyValue(501661, "point") + "");
            cm.dispose();
            return;
        } else {
            cm.sendOk("請確認狩獵積分不足或背包欄不足.");
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}


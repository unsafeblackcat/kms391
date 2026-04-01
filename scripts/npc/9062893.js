importPackage(java.lang);

var list = [ //아이템코드, 묶음갯수, 개당가격
    [4031013, 1, 10], // 오토루팅
    [2049360, 1, 10],
    [4310007, 100, 20],
    [2636136, 1, 2000],
    [2633616, 1, 3000],
    [5170000, 1, 300],
    [5700000, 1, 400],
    [2049371, 1, 50],
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
        var jampoint = cm.getPlayer().getKeyValue(501368, "point"); 
        if (cm.getPlayer().getKeyValue(501368, "point") == null) {
            cm.getPlayer().setKeyValue(501368, "point", 0);
        }
        say += "   잠수 포인트 상점에 오신 것을 환영합니다.\r\n   구입하실 아이템을 선택해 주세요.\r\n\r\n";
        say += "   현재 잠수 포인트 : " + jampoint + "\r\n\r\n";
        for (var i = 0; i < list.length; i++) {
            say += "#L" + i + "##i" + list[i][0] + "##b #z" + list[i][0] + "##k (" + list[i][1] + "개) - #r" + list[i][2] + "P#k#l\r\n";
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        choice = selection;
        say += "#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "개 묶음)을 선택하셨습니다.\r\n";
        say += "몇 묶음을 구입하시겠습니까? 1묶음 당 " + list[choice][2] + "잠수포인트가 필요합니다.\r\n\r\n";
        say += "현재 잠수 포인트 : " + cm.getPlayer().getKeyValue(501368, "point") + "\r\n\r\n";
        if (list[choice][0] >= 2000000) {
            cm.sendGetNumber(say, 1, 1, Math.floor(30000/list[choice][1]));
        } else {
            cm.sendGetNumber(say, 1, 1, 1);
        }
    } else if (status == 2) {
        count = selection;
        cm.sendYesNo("정말로 #i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "개 묶음)을 " + count + "개 구입하시겠습니까?\r\n" +
        "총 " + (list[choice][2] * count) + "잠수포인트가 필요합니다.\r\n" +
        "현재 잠수 포인트 : " + cm.getPlayer().getKeyValue(501368, "point") + "");
    } else if (status == 3) {
        if (Integer.parseInt(cm.getPlayer().getKeyValue(501368, "point")) >= list[choice][2] * count && cm.canHold(list[choice][0], list[choice][1] * count)) {
            cm.getPlayer().setKeyValue(501368, "point", Integer.parseInt(cm.getPlayer().getKeyValue(501368, "point")) - list[choice][2] * count + "");
            cm.gainItem(list[choice][0], list[choice][1] * count);
            cm.sendOk("#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "개 묶음)을 " + count + "개 구입하였습니다. \r\n\r\n" +
            "현재 잠수 포인트 : " + cm.getPlayer().getKeyValue(501368, "point") + "");
            cm.dispose();
            return;
        } else {
            cm.sendOk("잠수포인트가 부족하거나, 인벤토리 슬롯이 부족한 것은 아닌지 확인해 주세요.");
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}


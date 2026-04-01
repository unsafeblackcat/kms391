importPackage(java.lang);

var list = [ //아이템코드, 묶음갯수, 개당가격
    [2450163, 1, 3000],
    [5121057, 1, 2000],
    [5121104, 1, 2000],
    //[2004239, 1, 1500], 
    //[2004229, 1, 1500],
    [2004079, 1, 1000],
    [2004059, 1, 1000],
    [2004039, 1, 1000],
    [2004019, 1, 1000],
    [5062009, 100, 400],
    [5062010, 50, 400],
    [5062500, 50, 400],
    [4310156, 1, 300],
    [4310320, 100, 300],
    [2049704, 1, 300],
    [3015642, 1, 10],
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
        // 100161, "point"
       // cm.getPlayer().setKeyValue(100161, "point", 200);
        var jampoint = cm.getPlayer().getKeyValue(100161, "point"); 
        if (cm.getPlayer().getKeyValue(100161, "point") == null) {
            cm.getPlayer().setKeyValue(100161, "point", 0);
        }
        say += "   잠수 포인트 상점에 오신 것을 환영합니다.\r\n   구입하실 아이템을 선택해 주세요.\r\n\r\n";
        say += "   현재 잠수 포인트 : " + jampoint + "\r\n\r\n";
        for (var i = 0; i < list.length; i++) {
            if(list[i][0] == 5121060) {
                say += "#L" + i + "##i" + list[i][0] + "##b 경험치 뿌리기 50%#k (" + list[i][1] + "개) - #r" + list[i][2] + "P#k#l\r\n";
                continue;
            }
            say += "#L" + i + "##i" + list[i][0] + "##b #z" + list[i][0] + "##k (" + list[i][1] + "개) - #r" + list[i][2] + "P#k#l\r\n";
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        choice = selection;
        say += "#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "개 묶음)을 선택하셨습니다.\r\n";
        say += "몇 묶음을 구입하시겠습니까? 1묶음 당 " + list[choice][2] + "잠수포인트가 필요합니다.\r\n\r\n";
        say += "현재 잠수 포인트 : " + cm.getPlayer().getKeyValue(100161, "point") + "\r\n\r\n";
        if (list[choice][0] >= 2000000) {
            cm.sendGetNumber(say, 0, 1, Math.floor(30000/list[choice][1]));
        } else {
            cm.sendGetNumber(say, 0, 1, 1);
        }
    } else if (status == 2) {
        count = selection;
        cm.sendYesNo("정말로 #i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "개 묶음)을 " + count + "개 구입하시겠습니까?\r\n" +
        "총 " + (list[choice][2] * count) + "잠수포인트가 필요합니다.\r\n" +
        "현재 잠수 포인트 : " + cm.getPlayer().getKeyValue(100161, "point") + "");
    } else if (status == 3) {
        if (Integer.parseInt(cm.getPlayer().getKeyValue(100161, "point")) >= list[choice][2] * count && cm.canHold(list[choice][0], list[choice][1] * count)) {
            cm.getPlayer().setKeyValue(100161, "point", Integer.parseInt(cm.getPlayer().getKeyValue(100161, "point")) - list[choice][2] * count + "");
            cm.gainItem(list[choice][0], list[choice][1] * count);
            cm.sendOk("#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "개 묶음)을 " + count + "개 구입하였습니다. \r\n\r\n" +
            "현재 잠수 포인트 : " + cm.getPlayer().getKeyValue(100161, "point") + "");
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


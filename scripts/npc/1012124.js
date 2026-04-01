importPackage(java.lang);


파랑 = "#fc0xFF0054FF#";
var list = [ //아이템코드, 묶음갯수, 개당가격
    [4033450, 1, 100],
	[2049360, 1, 200],
	[2049136, 1, 100],
	[2046025, 1, 100],
	[2046026, 1, 100],
	[5539005, 1, 100],
	[5062006, 1, 30],
	[5062002, 1, 30],
    [2049376, 1, 500],
    [2439693, 1, 500],
    [2439272, 1, 300],
    [2430029, 1, 300],
    [2430030, 1, 300]
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
    if (status == 0) {
        if (cm.getPlayer().getClient().getKeyValue("bosspoint") == null) {
            cm.getPlayer().getClient().setKeyValue("bosspoint", "0");
        }
        var say = "   보스 포인트 상점에 오신 것을 환영합니다.\r\n   구입하실 아이템을 선택해 주세요.\r\n\r\n";
        say += "   현재 보스 포인트 : " + cm.getPlayer().getClient().getKeyValue("bosspoint") + "\r\n\r\n";
        for (var i = 0; i < list.length; i++) {
            say += "#L" + i + "##i" + list[i][0] + "##z" + list[i][0] + "#(" + list[i][1] + "개)" + list[i][2] + "포인트#l\r\n";
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        choice = selection;
        var say = "#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "개 묶음)을 선택하셨습니다.\r\n";
        say += "몇 묶음을 구입하시겠습니까? 1묶음 당 " + list[choice][2] + "보스포인트가 필요합니다.\r\n\r\n";
        say += "현재 보스 포인트 : " + cm.getPlayer().getClient().getKeyValue("bosspoint") + "\r\n\r\n";
        if (list[choice][0] >= 2000000) {
            cm.sendGetNumber(say, 0, 1, Math.floor(30000/list[choice][1]));
        } else {
            cm.sendGetNumber(say, 0, 1, 1);
        }
    } else if (status == 2) {
        count = selection;
        cm.sendYesNo("정말로 #i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "개 묶음)을 " + count + "개 구입하시겠습니까?\r\n" +
        "총 " + (list[choice][2] * count) + "보스포인트가 필요합니다.\r\n" +
        "현재 보스 포인트 : " + cm.getPlayer().getClient().getKeyValue("bosspoint") + "");
    } else if (status == 3) {
        if (Integer.parseInt(cm.getPlayer().getClient().getKeyValue("bosspoint")) >= list[choice][2] * count && cm.canHold(list[choice][0], list[choice][1] * count)) {
            cm.getPlayer().getClient().setKeyValue("bosspoint", Integer.parseInt(cm.getPlayer().getClient().getKeyValue("bosspoint")) - list[choice][2] * count + "");
            cm.gainItem(list[choice][0], list[choice][1] * count);
            cm.sendOk("#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][1] + "개 묶음)을 " + count + "개 구입하였습니다. \r\n\r\n" +
            "현재 보스 포인트 : " + cm.getPlayer().getClient().getKeyValue("bosspoint") + "");
            cm.dispose();
            return;
        } else {
            cm.sendOk("보스포인트가 부족하거나, 인벤토리 슬롯이 부족한 것은 아닌지 확인해 주세요.");
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}


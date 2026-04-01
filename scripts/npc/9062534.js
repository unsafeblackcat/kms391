importPackage(java.lang);

var list = [ //아이템코드, 가격, 갯수
    [2435719, 50, 1], 
    [2048717, 100, 30], 
    [5062010, 300, 100], 
    [5062500, 300, 100], 
    [2004019, 300, 1], 
    [2004039, 300, 1], 
    [2004059, 300, 1], 
    [2004079, 300, 1], 
    [5121057, 1000, 1], 
    [5121104, 1000, 1],
    [2450163, 2000, 1],];
 
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
        var say = "#fs15##e#r  [PRAY] 사냥 P#k#n 상점에 오신 것을 환영합니다.\r\n   구입하실 아이템을 선택해 주세요.\r\n\r\n";
        say += "   현재 #b#e#h0##k#n 님의 [PRAY] 사냥 P : " + cm.getPlayer().getKeyValue(501661, "point") + "\r\n\r\n";
        for (var i = 0; i < list.length; i++) {
	if(list[i][0] == 5530360) {
            	say += "#L" + i + "##i" + list[i][0] + "# 스에잠 100% (" + list[i][2] + "개) - #e" + list[i][1] + "P#n#l\r\n";
		continue;
	} else if(list[i][0] == 2049704) {
            	say += "#L" + i + "##i" + list[i][0] + "# 레잠 100% (" + list[i][2] + "개) - #e" + list[i][1] + "P#n#l\r\n";
		continue;
	}
            say += "#L" + i + "##i" + list[i][0] + "# #z" + list[i][0] + "# (" + list[i][2] + "개) - #e" + list[i][1] + "P#n#l\r\n";
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        choice = selection;
        var say = "#i" + list[choice][0] + "##e#b#z" + list[choice][0] + "# " + list[choice][2] + "개#n#k를 선택하셨습니다.\r\n";
        say += "몇 묶음을 구입하시겠습니까? 1묶음 당 " + list[choice][1] + "P가 필요합니다.\r\n\r\n";
        say += "#e현재 #r[PRAY] 사냥 P#k : " + cm.getPlayer().getKeyValue(501661, "point") + "#n\r\n\r\n";
        if (list[choice][0] >= 2000000) {
            cm.sendGetNumber(say, 0, 1, Math.floor(30000/list[choice][2]));
        } else {
            cm.sendGetNumber(say, 0, 1, 1);
        }
    } else if (status == 2) {
        count = selection;
        cm.sendYesNo("정말로 #i" + list[choice][0] + "##z" + list[choice][0] + "# " + list[choice][2] * count + "개를 구입하시겠습니까?\r\n\r\n" +
        "#e현재 [PRAY] 사냥 P : " + cm.getPlayer().getKeyValue(501661, "point") + " 개\r\n" +
        "차감 [PRAY] 사냥 P : " + (list[choice][1] * count) + " 개\r\n" +
        "남는 [PRAY] 사냥 P : " + (cm.getPlayer().getKeyValue(501661, "point") - (list[choice][1] * count)) + " 개#n\r\n");
    } else if (status == 3) {
        if (Integer.parseInt(cm.getPlayer().getKeyValue(501661, "point")) >= list[choice][1] * count && cm.canHold(list[choice][0], list[choice][2] * count)) {
            cm.getPlayer().setKeyValue(501661, "point", Integer.parseInt(cm.getPlayer().getKeyValue(501661, "point")) - list[choice][1] * count + "");
            cm.gainItem(list[choice][0], list[choice][2] * count);
            cm.sendOk("#i" + list[choice][0] + "##z" + list[choice][0] + "# (" + list[choice][2] + "개 묶음)을 " + count + "개 구입하였습니다. \r\n\r\n" +
            "#e현재 [PRAY] 사냥 P : " + cm.getPlayer().getKeyValue(501661, "point") + "");
            cm.dispose();
            return;
        } else {
            cm.sendOk("P이 부족하거나, 인벤토리 슬롯이 부족한 것은 아닌지 확인해 주세요.");
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}



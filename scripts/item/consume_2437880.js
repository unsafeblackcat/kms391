importPackage(Packages.server);
importPackage(java.lang);

var seld = -1;
var color = "#fc0xff391f0f#";
var enter = "\r\n";

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, sel) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        var msg = enter;
        for (i = 1; i <= 6; i++) msg += "#L" + i + "##i171200" + i + "##z171200" + i + "#" + enter;
        cm.sendSimple(msg);
    } else if (status == 1) {
        seld = sel;
        if (sel > 6) return;
        var msg = "선택하신 심볼은 #d#i171200" + seld + "##z171200" + seld + "##k입니다." + enter;
        msg += "몇개의 상자를 사용하시겠습니까?";
        cm.sendGetNumber("#fs15#" + msg, 1, 1, cm.itemQuantity(2437880));
    } else if (status == 2) {
        itemid = Integer.parseInt("171200" + seld);
        if (cm.getInvSlots(1) >= sel * 2) {
            cm.gainItem(itemid, sel * 2);
            cm.gainItem(2437880, -(sel * 1));
            cm.sendOk("#fs15#축하합니다! 상자에서 #b#i" + itemid + "##z" + itemid + "# " + sel * 2 + "개#k가 나왔습니다!");
            cm.dispose();
        } else {
            cm.sendOk("#fs15#장비창을 비우신 후 다시 말을 걸어주세요.");
            cm.dispose();
            return;
        }
    }
}
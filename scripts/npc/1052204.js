var enter = "\r\n";
var 하 = 4033440;
var 하보 = 4033449;
var 검 = 4033441;
var 검보 = 4033450;
var 선택;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var msg = enter
        msg += "#h0#님 안녕하세요? 블랙월드에 방문한걸 환영합니다!" + enter
        msg += "하급 마정석을 마정석으로 교환하고 싶으시다구요?" + enter
        msg += "#L" + 하 + "##i" + 하 + "# #z" + 하 + "#을 #i" + 하보 + "# #z" + 하보 + "#으로 교환" + enter
        msg += "#L" + 검 + "##i" + 검 + "# #z" + 검 + "#을 #i" + 검보 + "# #z" + 검보 + "#으로 교환" + enter
        cm.sendOk("#fs15#" + msg);
    } else if (status == 1) {
        선택 = selection;
        if (selection == 하) {
            var msg = "#b#z" + 하 + "##k을 #r#z" + 하보 + "##k으로 교환할 갯수를 입력해 주세요." + enter
            msg += "#b#z" + 하 + "##k 1000 : #r1 #z" + 하보 + "##k 으로 교환하실 수 있습니다." + enter
            cm.sendGetNumber("#fs15#" + msg, 1, 1, 30);
        } else if (selection == 검) {
            var msg = "#b#z" + 검 + "##k을 #r#z" + 검보 + "##k으로 교환할 갯수를 입력해 주세요." + enter
            msg += "#b#z" + 검 + "##k 1000 : #r1 #z" + 검보 + "##k 으로 교환하실 수 있습니다." + enter
            cm.sendGetNumber("#fs15#" + msg, 1, 1, 30);
        }
    } else if (status == 2) {
        if (선택 == 하) {
            if (!cm.haveItem(하, selection * 1000)) {
                cm.sendOk("#fs15##z" + 하 + "##k " + ((selection * 1000) - cm.itemQuantity(하)) + "개가 부족합니다.");
                cm.dispose();
            } else {
                if (cm.getInvSlots(2) >= 5) {
                    cm.gainItem(하보, selection * 1);
                    cm.gainItem(하, -(selection * 1000));
                    cm.sendOk("#fs15##i" + 하보 + "##z" + 하보 + "##k " + selection * 1 + "개로 교환했습니다.");
                    cm.dispose();
                } else {
                    cm.sendOk("#fs15#기타창을 비우신 후 다시 말을 걸어주세요.");
                    cm.dispose();
                }
            }
        } else if (선택 == 검) {
            if (!cm.haveItem(검, selection * 1000)) {
                cm.sendOk("#fs15##z" + 검 + "##k " + ((selection * 1000) - cm.itemQuantity(검)) + "개가 부족합니다.");
                cm.dispose();
            } else {
                if (cm.getInvSlots(2) >= 5) {
                    cm.gainItem(검보, selection * 1);
                    cm.gainItem(검, -(selection * 1000));
                    cm.sendOk("#fs15##i" + 검보 + "##z" + 검보 + "##k " + selection * 1 + "개로 교환했습니다.");
                    cm.dispose();
                } else {
                    cm.sendOk("#fs15#기타창을 비우신 후 다시 말을 걸어주세요.");
                    cm.dispose();
                }
            }
        }
    }
}
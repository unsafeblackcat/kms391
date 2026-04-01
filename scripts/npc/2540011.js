/*
?젣?옉?옄 : qudtlstorl79@nate.com
*/


var enter = "\r\n";

var sssss = 0;
var suc = 0;
var sel = 0;
var sell = 0;
var selching = 0;
var sel2 = 0;
var status = -1;
var succ = false;
var minusitemid = 0;
var rewarditemid = 0;
var etc = 0;
var etc1 = 0;
var sale = 0;
var sale1 = 0;
var ssssitem = 0;
var ssssitemc = 0;
var ssssitem2 = 0;
var ssssitemc2 = 0;
var ssssmeso = 0;



function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {

	if (mode == -1) {
		cm.dispose();
	} else {
		if (status == 0 && mode == 0) {
			cm.dispose();
			return;
		} else if (status == 1 && mode == 0) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
	}
	if (status == 0) {
        var chat = enter
        chat += "#r<강화석 승급>" + enter
        chat += "#b에픽 강화석#d을 가져오면 #b유니크 강화석#d으로 만들어줄게!" + enter
        chat += "#L0# #i2432446# #z2432446# 제작 #k#l" + enter
        cm.sendOk("#fs15#" + chat);

    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            cm.dispose();
            cm.openNpc(2540012);

		} else if (sel == 1) {
			cm.dispose()
			cm.openNpc(1103005);
		} else if (sel == 2) {
			cm.dispose()
			cm.openNpc(1103005);
		} else if (sel == 3) {
			cm.dispose()
			cm.openNpc(1103005);
		} else if (sel == 4) {
			cm.dispose()
			cm.openNpc(1103005);
		} else if (sel == 5) {
			cm.dispose()
			cm.openNpc(1103005);

		}
    }
}
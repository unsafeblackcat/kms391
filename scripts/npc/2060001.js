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
        chat += "#r#e<칭호 승급>#n" + enter
        chat += "#d이봐 #h #! 칭호 승급에 관심있나?" + enter
        chat += "강한 용사는 그에 걸맞는 칭호가 따르는 법이지..." + enter + enter
	chat += "#r#fs15#주의 : 승급 실패 시 칭호가 사라집니다.#k" + enter
        chat += "#fs15##L0# #i3700511# #z3700511#로 승급 #k#l" + enter
        chat += "#L1# #i3700512# #z3700512#으로 승급 #k#l" + enter
        chat += "#L2# #i3700513# #z3700513#로 승급 #k#l" + enter
        chat += "#L3# #i3700514# #z3700514#로 승급 #k#l" + enter
        cm.sendOk("#fs15#" + chat);

    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            cm.dispose();
            cm.openNpc(2060002);

		} else if (sel == 1) {
			cm.dispose()
			cm.openNpc(2060003);
		} else if (sel == 2) {
			cm.dispose()
			cm.openNpc(2060006);
		} else if (sel == 3) {
			cm.dispose()
			cm.openNpc(2060010);
		} else if (sel == 4) {
			cm.dispose()
			cm.openNpc(1103005);
		} else if (sel == 5) {
			cm.dispose()
			cm.openNpc(1103005);

		}
    }
}
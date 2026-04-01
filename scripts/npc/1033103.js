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
        chat += "#r#e<훈장 승급>#n" + enter
        chat += "#d이봐 #h #! 훈장 승급에 관심있나?" + enter
        chat += "강한 용사는 그에 걸맞는 힘이 따르는 법이지..." + enter
        chat += "#L0# #i1142066# #z1142066#으로 승급 #k#l" + enter
		chat += "#L1# #i1142067# #z1142067#으로 승급 #k#l" + enter
		//chat += "#L2# #i1142068# #z1142068#으로 승급 #k#l" + enter
		//chat += "#L3# #i1142069# #z1142069#으로 승급 #k#l" + enter
		//chat += "#L4# #i1142732# #z1142732#으로 승급 #k#l" + enter
		//chat += "#L5# #i1142257# #z1142257#으로 승급 #k#l" + enter
        cm.sendOk("#fs15#" + chat);

    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            cm.dispose();
            cm.openNpc(1033101);

		} else if (sel == 1) {
			cm.dispose()
			cm.openNpc(1033100);
		} else if (sel == 2) {
			cm.dispose()
			cm.openNpc(1033102);
		} else if (sel == 3) {
			cm.dispose()
			cm.openNpc(1033104);
		} else if (sel == 4) {
			cm.dispose()
			cm.openNpc(1033106);
		} else if (sel == 5) {
			cm.dispose()
			cm.openNpc(1033105);

		}
    }
}
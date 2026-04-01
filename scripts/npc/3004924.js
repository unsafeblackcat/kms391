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
        chat += "#r#e<아이템 제작>#n" + enter
        chat += "#d보스 입장재료를 제작할수있어요!" + enter
        chat += "#L0# #i2631883#보스 입장권 일일 퀘스트 #k#l" + enter
        chat += "#L1# #i2631883#보스 입장권 일일 퀘스트 #r2 #k#l" + enter
		//chat += "#L2# #i1032200# #z1032200# 제작 #k#l" + enter
		//chat += "#L3# #i1113055# #z1113055# 제작 #k#l" + enter
        cm.sendOk("#fs15#" + chat);

    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            cm.dispose();
            cm.openNpc(3004923);

		} else if (sel == 1) {
			cm.dispose()
			cm.openNpc(3001392);
		} else if (sel == 2) {
			cm.dispose()
			cm.openNpc(2060004);
		} else if (sel == 3) {
			cm.dispose()
			cm.openNpc(2060005);
		} else if (sel == 4) {
			cm.dispose()
			cm.openNpc(1103005);
		} else if (sel == 5) {
			cm.dispose()
			cm.openNpc(1103005);

		}
    }
}
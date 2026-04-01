importPackage(Packages.server);
importPackage(java.lang);

var enter = "\r\n";
var seld = -1;

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
		var msg = "#fs15#원하시는 심볼을 선택해주세요#d#fs15#"+enter;
		for (i = 1; i <= 6; i++) msg += "#L"+i+"##i171200"+i+"##z171200"+i+"#"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		if (sel > 6) return;
		var msg = "#fs15#선택하신 심볼은 #d#i171200"+seld+"##z171200"+seld+"##k입니다."+enter;
		msg += "#fs15#정말로 상자를 사용하시겠습니까?";
		cm.sendYesNo(msg);
	} else if (status == 2) {
		itemid = Integer.parseInt("171200"+seld);
		q = Randomizer.rand(1, 5);
		for (i=0; i<q; i++) {
      cm.gainItem(itemid, 1);
}
		cm.gainItem(2439546, -1);
		cm.sendOk("#fs15#축하합니다! 상자에서 #b#i"+itemid+"##z"+itemid+"# "+q+"개#k가 나왔습니다!");
		cm.dispose();
	}
}
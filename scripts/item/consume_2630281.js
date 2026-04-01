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
		var msg = "#fs15#請選擇要領取的神秘徽章#d"+enter;
		for (i = 1; i <= 6; i++) msg += "#L"+i+"##i171200"+i+"##z171200"+i+"#"+enter;
		cm.sendSimple(msg); 
	} else if (status == 1) {
		seld = sel;
		if (sel > 6) return;
		var msg = "您選擇的神秘徽章是 #d#i171200"+seld+"##z171200"+seld+"##k。"+enter;
		msg += "確定要使用這個箱子嗎？";
		cm.sendYesNo(msg); 
	} else if (status == 2) {
		itemid = Integer.parseInt("171200"+seld); 
		q = Randomizer.rand(1,  5);
		for (i=0; i<q; i++) {
      cm.gainItem(itemid,  1);
		}
		cm.gainItem(2630281,  -1);
		cm.sendOk(" 恭喜！從箱子中獲得了 #b#i"+itemid+"##z"+itemid+"# "+q+"個#k！");
		cm.dispose(); 
	}
}
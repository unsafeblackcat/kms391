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
		cm.dispose();
		cm.gainItem(2431156,-1);
		cm.MakePmdrItem2(2431156, 20); // 뒤에 부분이 해당됨 
		cm.sendOk("현재 수치 : " + cm.getPlayer().getKeyValue(800023, "indiepmer2"));
	}
}
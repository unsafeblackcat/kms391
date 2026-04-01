노랑 = "#fc0xFFFFBB00#"
var status = -1;

importPackage(Packages.constants);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.sendYesNoS("#fs15#確定要用當前角色使用？ " + 노랑 + "#i2633619##z2633619##k ", 0x04, 9010061);
        return;
        }
        if (cm.getPlayer().getLevel() > 229) {
	cm.gainExp(90403788195);
	cm.dispose();
	cm.gainItem(2633619, -1);
	return;
        }
	else if (status == 1) {
	var i = cm.getPlayer().getLevel(); i < 230;
	cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
	cm.gainItem(2633619, -1);
	cm.dispose();
    } else {
        cm.dispose();
    }
}

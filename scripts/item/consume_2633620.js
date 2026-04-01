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
        cm.sendYesNoS("#fs15#確定要用當前角色使用？ " + 노랑 + "#i2633620##z2633620##k ", 0x04, 9000030);
        return;
        }
        if (cm.getPlayer().getLevel() > 239) {
	cm.gainExp(1500000000000000);
	cm.dispose();
	cm.gainItem(2633620, -1);
	return;
        }
	else if (status == 1) {
	var i = cm.getPlayer().getLevel(); i < 240;
	cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
	cm.gainItem(2633620, -1);
	cm.dispose();
    } else {
        cm.dispose();
    }
}

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
        cm.sendYesNoS("#fs15#정말 현재 캐릭터로 " + 노랑 + "#i2633617##z2633617##k 아이템을 사용하시겠습니까?", 0x04, 9010061);
        return;
        }
        if (cm.getPlayer().getLevel() > 209) {
	cm.gainExp(6120258214);
	cm.dispose();
	cm.gainItem(2633617, -1);
	return;
        }
	else if (status == 1) {
	var i = cm.getPlayer().getLevel(); i < 210;
	cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
	cm.gainItem(2633617, -1);
	cm.dispose();
    } else {
        cm.dispose();
    }
}

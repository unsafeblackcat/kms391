importPackage(Packages.constants);
importPackage(Packages.tools.packet);

function action() {
    if (cm.getPlayer().getLevel() < 200) {
        cm.sendOkS("200레벨 이하는 효과를 볼 수 없담.", 0x04, 9062506);
        cm.dispose();
        return;
    }
    if (cm.getPlayer().getLevel() > 200 && cm.getPlayer().getLevel() < 230) {
        cm.gainItem(2633316, -1);
        cm.getPlayer().gainExp(GameConstants.getExpNeededForLevel(cm.getPlayer().getLevel()), true, false, true);
        cm.dispose();
    } else {
        cm.gainExp(28929212222);
        cm.gainItem(2633316, -1);
        cm.dispose();
        return;
    }
}

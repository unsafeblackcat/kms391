importPackage(Packages.constants);
importPackage(Packages.tools.packet);

function action() {
    if (cm.getPlayer().getLevel() < 200) {
        cm.sendOkS("200레벨 이하는 효과를 볼 수 없담.", 0x04, 9062506);
        cm.dispose();
        return;
    }
    if (cm.getPlayer().getLevel() > 200 && cm.getPlayer().getLevel() < 209) {
        cm.gainItem(2633314, -1);
        cm.getPlayer().gainExp(GameConstants.getExpNeededForLevel(cm.getPlayer().getLevel()), true, false, true);
        cm.dispose();
    } else {
        cm.gainExp(8445662561);
        cm.gainItem(2633314, -1);
        cm.dispose();
        return;
    }
}

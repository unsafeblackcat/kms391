
var status = -1;
importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
function start() {
    status = -1;
    action(1, 0, 0);
}
검정 = "#fc0xFF191919#"
var leaf = 0, count = 0;
function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        leaf = cm.itemQuantity(4310229);
        cm.sendGetNumber(9000030, "인벤토리에 #i2632277# #b#z2632277# (교환불가)#k 아이템이 #b" + leaf + "개#k 있습니다. #b#e한 번에 몇 개#k#n를 사용하시겠습니까?", leaf, 1, leaf);
    } else if (status == 1) {
        if (leaf < selection) {
            cm.sendOkS("버그 악용하지마세요.", 4, 9000030);
            cm.dispose();
            return;
        }
        count = selection;
        cm.sendYesNoS("#i2632277# #b#z2632277# (교환불가)#k 아이템을 #b" + selection + "개#k 사용하여 #b#z4310229# " + selection + "개#k를 획득하시겠습니까?", 4, 9000030);
    } else if (status == 2) {
        말 = "\r\n소지 중인 #b#z4310229##k이 #b" + cm.getPlayer().getCoin(500629) + "#k개에서 #b" + (cm.getPlayer().getCoin(500629) + count) + "#k개가 되었습니다!\r\n\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
        말 += "#i4310229# #b#z4310229# " + count + "개#k\r\n "
        cm.getPlayer().AddCoin(500629, count, true);
        cm.gainItem(4310229, -count);
        cm.sendNextS(말, 0x04, 9000030);
        cm.dispose();
    }
}
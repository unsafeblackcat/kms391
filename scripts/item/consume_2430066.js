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

var status = -1;

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
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.CODY).getNumFreeSlot();
        if (leftslot < 2) {
            cm.sendOkS("#fs15##r치장칸 2 칸 이상을 확보하고 다시 말을 걸어주게.", 0x04, 9000030);
            cm.dispose();
            return;
        }
        if (Randomizer.isSuccess(40)) {
            item = 1032799;
        } else if (Randomizer.isSuccess(40)) {
            item = 1022699;
        } else {
            item = 1012949;
        }
        cm.gainItem(item, 1);
        cm.gainItem(2430066, -1);
        txt = "#fs15#어떤가! 자네 마음에 드는 클론 아바타가 나왔나?\r\n\r\n"
        txt += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n"
        txt += "#i" + item + "# #b#z" + item + "##k\r\n"
        cm.sendSimpleS(txt, 0x04, 9000030);
        cm.dispose();
        return;
    }
}

var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    2591427,
    2591572,
    2591590,
    2591640,
    2591659,
    2591676,
];

function start() {
    status = -1;
    action(1, 1, 0);
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
        var amed = "#fs15##d#h0#님, 원하시는 위대한 소울을 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2435899, -1);
        cm.dispose();
    }
}










/*기존
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
var item = [[2591572, 1], [2591427, 1], [2591088, 1], [2591296, 1], [2591264, 1], [2591418, 1], [2591409, 1], [2591087, 1]]

function start() {
    status = -1;
    action(1, 0, 0);
}
var a = 0;
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
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        if (leftslot < 2) {
            cm.sendOkS("#fs15#소비 인벤토리창 2칸 이상을 비워주세요.", 0x04, 9010061);
            cm.dispose();
            return;
        }
        var rand = Randomizer.rand(0, item.length - 1);
        var itemid = item[rand][0];
        var count = item[rand][1];
        cm.gainItem(itemid, count);
        cm.sendOkS("#fs15##i" + itemid + "# #b#z" + itemid + "# " + count + "개#k", 0x04, 9010061);
        cm.gainItem(2435899, -1);
        cm.dispose();
    }
}*/
importPackage(Packages.server);

var pet = [5000930, 5000931, 5000932];
var enter = "\r\n";
var need = -1, item;
var selected, seld = -1;
var j = 1;

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
        rand = pet[Math.floor(Math.random() * pet.length)];
        Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), rand, 1, "", Packages.client.inventory.MaplePet.createPet(rand, -1), 30, "", false);
        cm.gainItem(2630128, -1);
        cm.sendOkS("#fs15# #b#i" + rand + "##z" + rand + "##k 루나 쁘띠 #d펫#k이 지급되었습니다!", 2);
        cm.dispose();
    }
}
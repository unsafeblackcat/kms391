importPackage(java.util);
importPackage(java.io);
importPackage(Packages.provider);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.server);
importPackage(Packages.client.inventory);

var line = "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
var enter = "\r\n";
var itemlist = [1062580, 1082980, 1152414, 1042880, 1073880, 1008180, 1162430, 1012830, 1182430, 1132530, 1022410, 1032530, 1122711, 1122710, 1113680, 1113681, 1113682, 1113683, 1190435, 1672310];
var etcitem = [
];
var boxcode = 2435938;

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
        //jobcheck(cm.getPlayer().getJob());

        if (itemlist.length <= 0) {
            var t = "운영자에게 문의해주세요.";
            cm.sendOk(t);
            cm.dispose();
            return;
        }
        slot = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getNumFreeSlot();
        if (slot < itemlist.length) {
            cm.sendOk("장비창을 " + itemlist.length + "칸 이상 비워주세요.");
            cm.dispose();
            return;
        }
        if (!cm.getPlayer().isGM()) {
            cm.gainItem(boxcode, -1);
        }
        var t = "다음과 같은 아이템을 획득하였습니다." + enter;
        t += line;
        for (i = 0; i < itemlist.length; i++) {
            t += "#i" + itemlist[i] + "# #b#e#z" + itemlist[i] + "##k#n" + enter;
            itemoption(itemlist[i], 40056, 40056, 40056, 40056, 40056, 40056);
        }
        for (i = 0; i < etcitem.length; i++) {
            t += "#i" + etcitem[i][0] + "# #b#e#z" + etcitem[i][0] + "# " + etcitem[i][1] + "개#k#n" + enter;
            cm.gainItem(etcitem[i][0], etcitem[i][1]);
        }
        cm.sendOk(t);
        cm.dispose();
    }
}
function jobcheck(job) {
    if (GameConstants.isWarrior(job)) {
        itemlist.push();
    } else if (GameConstants.isMagician(job)) {
        itemlist.push();
    } else if (GameConstants.isArcher(job)) {
        itemlist.push();
    } else if (GameConstants.isThief(job)) {
        itemlist.push();
    } else if (GameConstants.isPirate(job)) {
        itemlist.push();
    } else {
        itemlist = [];
    }
}

function itemoption(itemid, pot1, pot2, pot3, pot4, pot5, pot6) {
    item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(itemid);
    item.setBossDamage(5);
    item.setTotalDamage(5);
    item.setState(20);
    item.setPotential1(30086);
	item.setPotential2(30086);
	item.setPotential3(30086);
	item.setPotential4(30086);
	item.setPotential5(30086);
	item.setPotential6(30086);
    Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}

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
    action (1, 0, 0);
}
검정 = "#fc0xFF191919#"
var day = 30; //유효기간 옵션 설정(일)
function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 = "#fs15##e시즌 1 대상#n 초기화에 대한 사과 보상입니다.\r\n다시 한번 진심으로 사죄의 말씀 드립니다.\r\n\r\n"
        말 += "#r샤벳(교환권x)과 황혼의 지배자 (백)#k 아이템은 #b인벤토리#k로 지급되며, 나머지 아이템은 #b보관함#k으로 지급됩니다.#k"
        cm.sendYesNoS(말, 0x04, 9000030);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 2) {
           cm.sendOk("#fs15##r기타창 2 칸 이상을 확보하게.");
           cm.dispose();
           return;
        }
        itemid = 5002239;
        time = new Date();
        var inz = Packages.client.inventory.itemz = new Packages.client.inventory.Item(itemid, 0, 1);
        inz.setExpiration((new Date(time.getFullYear(), time.getMonth(), time.getDate(), 0, 0, 0, 0)).getTime() + (1000 * 60 * 60 * 24 * 60));
        var pet = MaplePet.createPet(itemid, MapleInventoryIdentifier.getInstance());
        inz.setPet(pet);
        inz.setUniqueId(pet.getUniqueId());
        MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
        MapleInventoryManipulator.addFromDrop(cm.getClient(), inz, false);
        cm.getPlayer().gainCabinetItem(2430046, 25);
        cm.getPlayer().gainCabinetItem(2049704, 10);
        cm.getPlayer().gainCabinetItem(5062010, 1500);
        cm.getPlayer().gainCabinetItem(5062500, 1000);
        cm.getPlayer().gainCabinetItem(2048757, 500);
        cm.getPlayer().gainCabinetItem(2430004, 20);
        cm.getPlayer().gainCabinetItem(2430006, 10);
        cm.getPlayer().gainCabinetItem(2450064, 10);
        cm.getPlayer().gainCabinetItem(2430029, 5);
        cm.getPlayer().gainCabinetItem(2435902, 100);
        var inz = MapleItemInformationProvider.getInstance().getEquipById(1162004);
        inz.setExpiration((new Date()).getTime() + (1000 * 60 * 60 * 24 * day));
        MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
        말 = "보관함에서 아이템을 받으실 때 꼭 #e인벤토리 체크#n를 하고 받아주세요!\r\n"
        cm.gainItem(2430027, -1);
        cm.sendOkS(말, 0x04, 9000030);
        cm.dispose();
    }
}

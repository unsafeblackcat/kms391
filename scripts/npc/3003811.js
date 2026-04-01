importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
var status = -1;

var item = [
    [4001895, 1, 100],
];

function start() {
    action(1, 0, 0);
}


function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    wList = [];
    wGain = [];
    if (status == 0) {

        leftslot = cm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        leftslot1 = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 1) {
            cm.sendOk("인벤토리를 비우고 다시 말을 걸어주세요.");
            cm.dispose();
            return;
        }
        selStr = "\r\n검은마법사 입장 재료를 교환해주겠네.#k\r\n\r\n";
        selStr += "#i4001894# 아이템이 5개 필요합니다.#k\r\n\r\n";
        for (i = 0; i < item.length; i++) {
            selStr += "#i" + item[i][0] + ":# #t" + item[i][0] + ":# #b" + item[i][1] + "개#k#l\r\n";
        }
        cm.sendYesNo(selStr);

    } else if (status == 1) {
        if (!cm.haveItem(4001894, 5)) {
            cm.sendOk("이녀석 진힐라 안패고와?!?\r\n\r\n좀더 모아와!!! ");
            cm.dispose();
            return;
        }
	cm.gainItem(4001894, -5);
        a = Randomizer.rand(0, (item.length - 1));
        var selStr2 = "축하합니다! 아래의 아이템이 교환되었습니다!.\r\n\r\n";
        selStr2 += "#b#i" + item[a][0] + "##t" + item[a][0] + "# #b" + item[a][1] + "개";
        cm.gainItem(item[a][0], item[a][1]);
        
        cm.sendSimpleS(selStr2, 4, 2007);
        cm.dispose();
    }
}
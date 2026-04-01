importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
var status = -1;

var item = [
    [5062002, 1, 1],
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
        selStr = "\r\n자네 마스터 미라클 큐브 얻고싶나!?#k\r\n\r\n";
        selStr += "#i2430481# 아이템이 2500개 필요합니다.#k\r\n\r\n";
        for (i = 0; i < item.length; i++) {
            selStr += "#i" + item[i][0] + ":# #t" + item[i][0] + ":# #b" + item[i][1] + "개#k#l\r\n";
        }
        cm.sendYesNo(selStr);

    } else if (status == 1) {
        if (!cm.haveItem(2430481, 2500)) {
            cm.sendOk("이녀석 아이템이 부족하잖아!?\r\n\r\n좀더 모아와!!! ");
            cm.dispose();
            return;
        }
	cm.gainItem(2430481, -2500);
        a = Randomizer.rand(0, (item.length - 1));
        var selStr2 = "축하합니다! 아래의 아이템이 지급되었습니다!.\r\n\r\n";
        selStr2 += "#b#i" + item[a][0] + "##t" + item[a][0] + "# #b" + item[a][1] + "개";
        cm.gainItem(item[a][0], item[a][1]);
        
        cm.sendSimpleS(selStr2, 4, 2007);
        cm.dispose();
    }
}
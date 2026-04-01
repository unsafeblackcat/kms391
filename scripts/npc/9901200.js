importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
var status = -1;

var item = [
    [5060048, 3, 7000],
    [5062006, 5, 7000],
    [5062005, 1, 1000],
    [2049164, 1, 1000],
    [2046213, 1, 1000],
    [2046214, 1, 1000],
    [2046308, 1, 1000],
    [2046309, 1, 1000],
    [2048051, 1, 1000],
    [2048052, 1, 1000],
    [2633616, 30, 1000],
    [2635911, 1000, 1000],
    [2635909, 2, 1000],
    [2635903, 1, 1000],
    [5062500, 100, 1000],
    [5062010, 100, 1000],
    [2435719, 100, 1000],
    [2450134, 1, 1000],
    [5121060, 1, 1000],
    [4310900, 100, ],
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
        selStr = "\r\n양말 이벤트를 준비했어요! 아래의 물품에서 랜덤으로 등장하니 확인해보세요!#k\r\n\r\n";
        selStr += "#i4001395# 아이템이 50개 필요합니다.#k\r\n\r\n";
        for (i = 0; i < item.length; i++) {
            selStr += "#i" + item[i][0] + ":# #t" + item[i][0] + ":# #b" + item[i][1] + "개#k#l\r\n";
        }
        cm.sendYesNo(selStr);

    } else if (status == 1) {
        if (!cm.haveItem(4001395, 50)) {
            cm.sendOk("이녀석 아이템이 모라잖아!?\r\n\r\n좀더 모아오도록해 ");
            cm.dispose();
            return;
        }
	cm.gainItem(4001395, -50);
        a = Randomizer.rand(0, (item.length - 1));
        var selStr2 = "축하합니다! 아래의 아이템이 지급되었습니다!.\r\n\r\n";
        selStr2 += "#b#i" + item[a][0] + "##t" + item[a][0] + "# #b" + item[a][1] + "개";
        cm.gainItem(item[a][0], item[a][1]);
        
        cm.sendSimpleS(selStr2, 4, 2007);
        cm.dispose();
    }
}

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
검정 = "#fc0xFF000000#"
var day = 14; //有效期限選項設定(日) 
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
        말 = "#fs15#"+검정+"確定要用此角色領取奬勵碼?\r\n"
        말 += "(請務苾在道具欄預留充足的空間)"
        cm.sendYesNoS(말, 0x04, 9000030);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        leftslot1 = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 2 && leftslot1 < 2) {
           cm.sendOkS("#fs15##r請在裝備欄和其他欄空出至少2格.", 0x04, 9000030);
           cm.dispose();
           return;
        }
        /*
        (暗祕潛能魔方)5062005 5個 
        (火祕潛能魔方)5062503 5個 
        (10億)4001716 1個 
        (爆率2倍) 2023072 1個 
        (ARK幣)4310248 500個 
        (奈奧核心)4310308 100個 

        */
        말 = "#fn나눔고딕##fs15#"+검정+"恭喜獲得了\r\n#i5062005##z5062005#*10\r\n#i4001716##z4001716#*2\r\n#i4319999##z4319999#*2000\r\n#i5060048##z5060048#*5"//新細明體
        cm.gainItem(5062005, 10);    //火祕潛能魔方 
        cm.gainItem(4001716, 2);     //10億楓幣 
        cm.gainItem(4319999, 2000);  //打怪幣 
		cm.gainItem(5060048, 5);     //黃金蘋果 
        cm.gainItem(2434311, -1);    //熱門時間箱子 -1 
        //cm.gainItem(5062503, 10);    //暗祕潛能魔方 
       // cm.gainItem(4319997, 100);   //海因斯核心 
        cm.sendOkS(말, 0x04, 9000030);
        cm.dispose();
    }
}

importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

/*

    * 單文NPC自動製作腳本透過生成的腳本
    
    * (Guardian Project Development Source Script)
    
    由Black製作
    
    NPC ID: 9110007
    
    NPC名稱: 機器人
    
    NPC所在的地圖: 怪物公園: 怪物公園 (951000000)
    
    NPC描述: 拉麵廚師

*/

var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}
var sel = -1;
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
        if (cm.getPlayer().getKeyValue(1477, "count") < 35) {
            cm.sendOkS("#fs15#要製作核心寶石需要35個V核心碎片。請分解不需要的核心來收集V核心碎片後再嘗試。", 4, 1540945);
            cm.dispose();
            return;
        }
        cm.sendYesNoS("#fs15#要使用V核心碎片製作核心寶石嗎？\r\n\r\n#b製作核心寶石所需的V核心碎片數量：35\r\n目前擁有的V核心碎片數量："+cm.getPlayer().getKeyValue(1477, "count")+"", 4, 1540945);
    } else if (status == 1) {
        cm.sendOkS("#fs15#已使用#b35個#k V核心碎片製作了核心寶石。請確認您的道具欄。", 4, 1540945)
        cm.getPlayer().setKeyValue(1477, "count", (cm.getPlayer().getKeyValue(1477, "count") - 35) + "");
        cm.gainItem(2435719, 1);
        cm.dispose();
    }
}


function statusplus(millsecond) {
    cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}
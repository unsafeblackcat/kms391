/*

    * 透過短語NPC自動製作腳本所生成的腳本。

    * (Guardian Project Development Source Script)

    由神弓手製作完成。

    NPC編號：9062513

    NPC名稱：樹木的精靈

    NPC所在地圖：盛開的森林：盛開之森（993192000）

    NPC說明：盛開分享幣商店


*/

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
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getKeyValue(100790, "shopTutoW") == 1) {
            //開啟商店
            cm.openShop(23);
            cm.dispose();
            return;
        }
        cm.sendNextS("你好啊，異鄉人。\r\n你也是來賞花的嗎？", 4, 9062513);
    } else if (status == 1) {
        cm.sendNextPrevS("這片花田實在太適合楓之谷的生日了。\r\n作為來到這裡的紀念，你想要獲得#b#e#i4310310:# #t4310310:##n#k吧。", 4, 9062513);
    } else if (status == 2) {
        cm.sendNextPrevS("把#b#e#i4310310:# #t4310310:##n#k交給我，我就會給你#b#e裝備強化#n#k有幫助的特別道具。", 4, 9062513);
    } else if (status == 3) {
        cm.sendNextPrevS("在花盛開期間我都會待在這裡，隨時歡迎過來找我。\r\n\r\n#e※盛開分享幣商店使用期間#r\r\n至6月20日下午11點59分為止#k#n", 4, 9062513);
    } else if (status == 4) {
        cm.getPlayer().setKeyValue(100790, "shopTutoW", "1");
        cm.openShop(2003);
        cm.dispose();
        //商店開啟
    }
}
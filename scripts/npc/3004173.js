/*

    * 透過單行NPC自動製作腳本生成的腳本。

    * (Guardian Project Development Source Script)

    由休比格爾製作。

    NPC編號：3003811

    NPC名稱：羅蕾萊

    NPC所在地图：The Black : Night Festival (100000051)

    NPC說明：頒發命運的碎片


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
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        話 = "#fn微軟正黑體#您好，我是負責立方體兌換的服務員。\r\n\r\n"
        話 += "#fn微軟正黑體#材料1:#i5062500##z5062500# 30000個\r\n"
        話 += "\r\n"
        話 += "          要用#i5062010##z5062010# 3000個來兌換嗎？\r\n"
        cm.sendYesNo(話);
    } else if (status == 1) {
        if (!cm.haveItem(5062500, 30000)) {
            cm.sendOk("#fn微軟正黑體##z5062010#兌換所需材料不足。");
            cm.dispose();
            return;
        }
        cm.gainItem(5062500, -30000);
        cm.gainItem(5062010, 5000);
        cm.sendOk("#fn微軟正黑體##i5062010##z5062010#已成功兌換。");
        cm.dispose();
    }
}
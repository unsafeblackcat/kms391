var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
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
        cm.sendNextS("#fn微軟正黑體##d(寶物箱裡好像有東西。)#k\r\n這會是舒米遺失的#b硬幣#k嗎？", 2);
    } else if (status == 1) {
        cm.sendOk("#fn微軟正黑體#從寶物箱中獲得了道具。\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n#i4031039# #b#z4031039##k");
    } else if (status == 2) {
        cm.warp(100030301, 0);
        cm.gainItem(4031039, 1);
        cm.dispose();
    }
}
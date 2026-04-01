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
        cm.sendSimple("#e#dPixi 用戶 #h 0##k#n 先生/女士歡迎光臨~ 隨時歡迎您！#b\r\n\r\n#L0#獲得火焰之眼物品\r\n#L1#想回到村莊。");
    } else if (status == 1) {
        cm.dispose();
        if (selection == 0) {
            cm.gainItem(4001017, 1);
            cm.sendOk("已發放。");
        } else {
            cm.setDeathcount(0);
            cm.warp(1000000, 0);
        }
    }
}
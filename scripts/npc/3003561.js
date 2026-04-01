var status = -1;

function start() {
    status = -1;
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

    if (status == 0) {
        if (cm.getPlayer().getMap().getNumMonsters() > 0 && (cm.getPlayer().getMapId() == 450008350 || cm.getPlayer().getMapId() == 450008750)) {
            cm.sendYesNoS("要結束戰斗，出去瑪?", 0x26);
        } else {
            cm.sendYesNoS("打敗了威爾。” 回到“鏡子裏的光之神”嗎?", 0x26);
        }
    } else if (status == 1) {
        cm.warp(450007240, 1);
        cm.dispose();
    }
}
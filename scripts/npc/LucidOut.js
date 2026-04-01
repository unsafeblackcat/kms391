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
        if (cm.getPlayer().getMapId() == 450004300 || cm.getPlayer().getMapId() == 450004600) {
            cm.sendYesNoS("已擊敗露西德。要返回「惡夢時鐘塔」嗎？", 0x26);
        } else {
            cm.sendYesNoS("此處危險。要放棄戰鬥並離開嗎？", 0x26);
        }
    } else if (status == 1) {
        cm.getPlayer().addKV("bossPractice", "0");
        cm.warp(450004000, 1);
        cm.dispose();
    }
}
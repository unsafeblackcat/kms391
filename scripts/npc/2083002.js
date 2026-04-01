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
        cm.sendNext("請重新考慮後再和我說話。.");
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        mapid = cm.getPlayer().getMapId();
        if (mapid >= 240060000) {
            st = 240050400;
            cm.sendYesNo("#fs15#要停止戰斗，出去瑪?");
        } else {
            st = 240050000;
            cm.sendYesNo("#fs15#要離開這裏，回洞口瑪?")
        }
    } else if (status == 1) {
        cm.warp(st);
        cm.dispose();
    }
}
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
        cm.sendNext("#fs15#如果你想移動的話，請重新和我說話。.");
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
            cm.sendOk("#fs15#請通過組隊現場進行.");
            cm.dispose();
            return;
        }
        cm.sendYesNo("#fs15#石板上寫的字閃閃發光，石板後面開了一扇小門。 是否使用秘密通道?");
    } else if (status == 1) {
        cm.warpParty(240050400);
        cm.dispose();
    }
}
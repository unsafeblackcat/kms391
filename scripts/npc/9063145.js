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
        말 = "?\r\n"
        말 += "!"
        cm.sendYesNoS(말, 0x04, 9062004);
        cm.dispose();
        return;
    }
}

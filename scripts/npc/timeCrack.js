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
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "#e<時間的裂縫>#n\r\n"
        talk+= "過去與未來，以及其間的某處...您想去往何方？\r\n\r\n"
        talk+= "#L0# #b次元的縫隙#l";
        cm.sendSimple(talk);
    } else if (status == 1) {
        cm.dispose();
        cm.warp(272020000, 0);
    }
}
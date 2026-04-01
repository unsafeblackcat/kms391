
/*
OX퀴즈
*/

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
        var chat = "#fs15#";
           chat += "#r예#k를 누르시면 #b[OX퀴즈 맵]#k으로 입장합니다.";
           cm.sendYesNo(chat);
              } else if (status == 1) {
                   cm.warp(910048100);
                   cm.dispose();
               }

    }


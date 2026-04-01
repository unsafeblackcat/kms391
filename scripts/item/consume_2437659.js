var status = -1;

function start() {
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
	cm.sendYesNo("#fs15#안녕하세요? 자유전직시스템으로 이동시켜드리는 #b프리미엄 혜택#k 입니다. #b다른적직#k 을이용하고 #b특별한 혜택#k등 을 획득 하실 수 있습니다. 지금 바로 이동하시겠어요?");
    } else if (status == 1) {
	cm.dispose();
	cm.warp(450000000);
    cm.gainItem(2437659, -1); //회수
    }
}
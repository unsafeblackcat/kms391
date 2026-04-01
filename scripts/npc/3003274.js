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
        if (cm.haveItem(3700346, 1)) {//아이템 체크
            var 사냥터 = "#fs18#\r\n"
            //사냥터 += "#L1##fc0xFFEDA900# 슬라임(저렙) 사냥터로#k로 이동하겠습니다.#l\r\n"
            사냥터 += "#L2##fc0xFFEDA900# 발록(골드) 사냥터로#k로 이동하겠습니다.#l\r\n\r\n"
            //사냥터 += "#L3##fc0xFFEDA900# 발록(고레벨) 사냥터로#k로 이동하겠습니다.#l\r\n\r\n"
            사냥터 += "필요 : #i3700346# #b#t3700346# 1개"
            cm.sendOkS(사냥터, 0x24);
        } else {
            cm.sendOkS("#r현재 후원 사냥터 입장에 필요한 티켓이 없다.", 2);
            cm.dispose();
        }
    } if (status == 1) {//채팅 공지 및 워프
		choice = selection;
		switch (selection) {
    case 1:
        Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님이 후원 사냥터(저렙)에 입장하였습니다."));
        cm.dispose()
        cm.warp(993199155);
        break;
    case 2:
        Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님이 후원 사냥터(중렙)에 입장하였습니다."));
        cm.dispose()
        cm.warp(993199157);
        break;
    case 3:
        Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님이 후원 사냥터(고렙)에 입장하였습니다."));
        cm.dispose()
        cm.warp(993199156);
        break;
    }
    }
}

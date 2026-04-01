


/*

	오딘 KMS 팀 소스의 스크립트 입니다.

	엔피시아이디 : 2010011
	
	엔피시 이름 : 레아

	엔피시가 있는 맵 : 마을전역

	엔피시 설명 : 길드본부로 이동


*/


var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    
    if (status == 0) {
        cm.sendYesNo("#fs15#需要公會相關業務嗎？ 現在請前往#b奧爾維斯英雄殿堂#k");
    } else if (status == 1) {
	if (cm.getPlayerStat("LVL") < 10) {
	    cm.sendOk("#fs15#抱歉，只有等級10以上的人才能使用。.");
            cm.dispose();
            return;
        }
        cm.warp(200000301);
        cm.dispose();
    }
}



importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);

var status = -1;
var npc1 = 0, npc2 = 0;
var check = false;

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
		status--;
	}
	if (mode == 1) {
		d = status;
		status++;
	}

	if (status == 0) {
		cm.sendUI(1300);
		cm.sendNextS("자, 여기 내가 준비한 #b#e크리에이터 스텝업#n#k이야!", 37, 9062554);
	} else if (status == 1) {
		cm.sendNextPrevS("미션을 수행하면서 그 모습을 방송으로 촬영하는 거야!", 37, 9062554);
	} else if (status == 2) {
		cm.sendNextPrevS("미션을 완료하고 방송을 등록하면 #r#e#i4310314# 구독자#n#k가 쑥쑥!\r\n\r\n구독자님들의 선물 #b#e#i4310312##t4310312##n#k과 #b#e#i4310313##t4310313##n#k도 쑥쑥!\r\n\r\n\r\n #r ※ 미션을 완료하면 구독자와 콩을 받을 수 있습니다.", 133, 9062554);
	} else if (status == 3) {
		cm.sendNextPrevS("#r#e#i4310314# 구독자#n#k님들이 늘어나서 점점 유명해지면 특별한 선물도 받을 수 있지! \r\n\r\n\r\n #r ※ 구독자 1000명이 증가할 때마다 크리에이터 스텝업 보상을 받을 수 있습니다.", 133, 9062554);
	} else if (status == 4) {
		cm.sendNextPrevS("매일 방송을 꾸준히 진행하는 것이 중요하니 일일 미션은 \r\n꾸준히 참여하고 #b#e#i4310312# #t4310312##n#k을 모아봐!\r\n참! 하루에 늘어나는 구독자 최대 수는 정해져 있어!\r\n\r\n\r\n #r ※ 일일 미션은 매일 0시에 리셋되며, 일일 미션 구독자는 월드 당 최대 300명만 증가합니다.", 133, 9062554);
	} else if (status == 5) {
		cm.sendNextPrevS("아! 특별히 강한 보스를 잡는 방송은 #b#e#i4310313# #t4310313##n#k을 받을 수 있어!\r\n\r\n\r\n #r ※ 주간 미션은 매주 목요일 0시에 리셋되며 진행 상황은 월드 내 공유됩니다.", 37, 9062554);
	} else if (status == 6) {
		cm.sendNextPrevS("그리고 특별한 도전 방송! \r\n#r#e월간 미션#n#k으로 너의 한계를 시험해 봐!\r\n\r\n\r\n #r ※ 월간 미션은 4주 단위로 리셋되며 진행 상황은 월드 내 공유됩니다.", 133, 9062554);
	} else if (status == 7) {
		cm.sendNextPrevS("그럼 궁금한 것은 언제든 물어봐!", 37, 9062554);
		cm.dispose();
	}

}

function statusplus(millsecond) {
	cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}


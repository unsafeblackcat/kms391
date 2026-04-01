


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	예티이 에 의해 만들어 졌습니다.

	엔피시아이디 : 9310000

	엔피시 이름 : 조종사

	엔피시가 있는 맵 :  :  (0)

	엔피시 설명 : MISSINGNO


*/

var status = -1;
importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.scripting);

var status = -1;
var check = false;
var check1 = false;
var check2 = false;
var check3 = false;
var check4 = false;
var check5 = false;
var check6 = false;
var check7 = false;
var check8 = false;
var check9 = false;
var check10 = false;
var check11 = false;
var npc1 = 0;
var npc2 = 0;
var npc3 = 0;
var npc4 = 0;
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
		status++;
	}

	if (status == 0) {
		if (!check11) {
			check11 = true;
			cm.removeNpc(9072300);
			cm.removeNpc(9072301);
			cm.removeNpc(9072302);
			cm.removeNpc(9072303);
			cm.getPlayer().getClient().send(SLFCGPacket.SetStandAloneMode(true));
			cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, true, false, false));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 10, 1));
			npc1 = cm.spawnNpc2(9072302, new Point(-3, -30));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "summon", 0, false));
			cm.getClient().send(SLFCGPacket.MakeBlind(1, 200, 0, 0, 0, 1300, 0));
			statusplus(1600);
		}
	} else if (status == 1) {
		cm.sendScreenText("오늘도 평화로운 핑크빈 월드의 어느 날...", true);
	} else if (status == 2) {
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1300, 0));
		statusplus(4600);
	} else if (status == 3) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "yawn", 1800, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/yawn"));
		statusplus(1800);
	} else if (status == 4) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#오늘도 심심하네... 뭐하고 놀지...", 0, 0, 1500, 1, 0, 0, 0, 4, 9072302, 0, 1719));
		statusplus(3000);
	} else if (status == 5) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#일단 하던 놀이나 해볼까.", 0, 0, 1500, 1, 0, 0, 0, 4, 9072302, 0, 1719));
		statusplus(2000);
	} else if (status == 6) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "fan", 2880, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/fan"));
		statusplus(2880);
	} else if (status == 7) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "pogoStick", 2400, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/pogostick"));
		statusplus(2400);
	} else if (status == 8) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "moving", 1440, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/beanmoving"));
		statusplus(1440);
	} else if (status == 9) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "rCcar", 3600, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/rccar"));
		statusplus(3600);
	} else if (status == 10) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "guitar", 1200, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/guitar"));
		statusplus(1200);
	} else if (status == 11) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "headSet", 2400, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/headset2d"));
		statusplus(2400);
	} else if (status == 12) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "yoyo", 1680, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/yoyo"));
		statusplus(1680);
	} else if (status == 13) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "juice", 2400, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/juice"));
		statusplus(2100);
	} else if (status == 14) {
		statusplus(300);
	} else if (status == 15) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "yawn", 1800, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/yawn"));
		statusplus(1800);
	} else if (status == 16) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#이제 할 게 없는데...", 0, 0, 1500, 1, 0, 0, 0, 4, 9072302, 0, 1719));
		statusplus(2000);
	} else if (status == 17) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#심심하니 하던 놀이나 계속 해야 하나...", 0, 0, 1500, 1, 0, 0, 0, 4, 9072302, 0, 1719));
		statusplus(3000);
	} else if (status == 18) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "moving2", 720, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/all"));
		statusplus(720);
	} else if (status == 19) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "rCcar2", 540, true));
		statusplus(540);
	} else if (status == 20) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "fan2", 420, true));
		statusplus(420);
	} else if (status == 21) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "yoyo2", 300, true));
		statusplus(300);
	} else if (status == 22) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "guitar2", 540, true));
		statusplus(540);
	} else if (status == 23) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "pogoStick2", 720, true));
		statusplus(720);
	} else if (status == 24) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "headSet2", 810, true));
		statusplus(710);
	} else if (status == 25) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "juice2", 720, true));
		statusplus(720);
	} else if (status == 26) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "rCcar3", 270, true));
		statusplus(270);
	} else if (status == 27) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "headSet3", 270, true));
		statusplus(270);
	} else if (status == 28) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "moving3", 240, true));
		statusplus(240);
	} else if (status == 29) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "guitar3", 180, true));
		statusplus(180);
	} else if (status == 30) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "fan3", 210, true));
		statusplus(210);
	} else if (status == 31) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "moving4", 240, true));
		statusplus(240);
	} else if (status == 32) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "yoyo3", 150, true));
		statusplus(150);
	} else if (status == 33) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "pogoStick3", 240, true));
		statusplus(240);
	} else if (status == 34) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "moving5", 240, true));
		statusplus(240);
	} else if (status == 35) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "juice3", 240, true));
		statusplus(240);
	} else if (status == 36) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "yawn", 1800, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/yawn"));
		statusplus(1800);
	} else if (status == 37) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "Rolling", 3550, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/rolling"));
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(0, "#fn나눔고딕 ExtraBold##fs20#심심해! 심심해!", 0, 0, 1500, 1, 0, 0, 0, 4, 9072302, 0, 1719));
		statusplus(2000);
	} else if (status == 38) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(0, "#fn나눔고딕 ExtraBold##fs20#새로운 것이 하나도 없잖아!", 0, 0, 1500, 1, 0, 0, 0, 4, 9072302, 0, 1719));
		statusplus(2500);
	} else if (status == 39) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction3(npc1));
		npc2 = cm.spawnNpc2(9072300, new Point(-116, -25));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "summon", 0, false));
		statusplus(1000);
	} else if (status == 40) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "discovery2", -1, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/discovery"));
		statusplus(2500);
	} else if (status == 41) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "worry", -1, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/worry"));
		statusplus(1000);
	} else if (status == 42) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#에이... 뭔가 했더니, 늘 가던 곳에서 부르는 거잖아?", 0, 0, 1500, 1, 0, 0, 0, 4, 9072302, 0, 1719));
		statusplus(2000);
	} else if (status == 43) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#가서 좀 놀려고 하면 이상한 모험가들이 막 때린단 말이지...", 0, 0, 1500, 1, 0, 0, 0, 4, 9072302, 0, 1719));
		statusplus(2000);
	} else if (status == 44) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#안그래도 다른 월드에 가면 약해지는데 말이야.", 0, 0, 1500, 1, 0, 0, 0, 4, 9072302, 0, 1719));
		statusplus(2000);
	} else if (status == 45) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#흠, 저기도 이제 슬슬 질리는데... ", 0, 0, 1500, 1, 0, 0, 0, 4, 9072302, 0, 1719));
		statusplus(2000);
	} else if (status == 46) {
		npc3 = cm.spawnNpc2(9072303, new Point(720, -25));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "summon", 0, false));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "moving", 8640, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/blackbeanmoving"));
		statusplus(8640);
	} else if (status == 47) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction3(npc3));
		cm.removeNpc(9072303);
		npc3 = cm.spawnNpc2(9072303, new Point(389, -25));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "summon", 0, false));
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#여전히 멍청해 보이는 표정을 하고 있군.", 0, 1, 2000, 1, 0, 0, 0, 4, 9072303, 0, 1719));
		statusplus(2500);
	} else if (status == 48) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#이 블랙빈님을\r\n전혀 눈치 채지 못한 것 같은데?", 0, 1, 1500, 1, 0, 0, 0, 4, 9072303, 0, 1719));
		statusplus(1500);
	} else if (status == 49) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "giggle", -1, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/giggle"));
		statusplus(2000);
	} else if (status == 50) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#그럼 오늘도 핑크빈 녀석을 골려볼까?", 0, 1, 1500, 1, 0, 0, 0, 4, 9072303, 0, 1719));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "potal", 2040, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/potal"));
		statusplus(1680);
	} else if (status == 51) {
		npc4 = cm.spawnNpc2(9072301, new Point(112, -25));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc4, "summon", 0, false));
		statusplus(360);
	} else if (status == 52) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "giggle", -1, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/giggle"));
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#통과하면 '레벨 왕창 다운'의 저주가\r\n걸리는 특별한 포탈을 준비했지!", 0, 1, 2000, 1, 0, 0, 0, 4, 9072303, 0, 1719));
		statusplus(2500);
	} else if (status == 53) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#히힛! 게다가 이번엔 특별히 색깔이 마음대로 변하는 마법도 걸어 뒀다고!", 0, 1, 2000, 1, 0, 0, 0, 4, 9072303, 0, 1719));
		statusplus(2500);
	} else if (status == 54) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#처음 보는 곳에 가서 고생 좀 해봐라.", 0, 1, 2000, 1, 0, 0, 0, 4, 9072303, 0, 1719));
		statusplus(3000);
	} else if (status == 55) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "moving2", 10080, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/blackbeanmoving"));
		statusplus(9000);
	} else if (status == 56) {
		cm.removeNpc(9072303);
		statusplus(1000);
	} else if (status == 57) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "discovery", -1, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/discovery"));
		statusplus(500);
	} else if (status == 58) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#엇? 저기 포탈에 보이는 곳은 처음 보는데?", 0, 0, 1500, 1, 0, 0, 0, 4, 9072302, 0, 1719));
		statusplus(2000);
	} else if (status == 59) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "expectation", -1, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/expectation"));
		statusplus(1500);
	} else if (status == 60) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs15#가볼까...   #fs18#가볼까...  #fs20#가볼까!", 0, 0, 1500, 1, 0, 0, 0, 4, 9072302, 0, 1719));
		statusplus(2000);
	} else if (status == 61) {
		cm.removeNpc(9072301);
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "jumpIn", 2280, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/PinkBean/jumpin"));
		statusplus(2280);
	} else if (status == 62) {
		cm.removeNpc(9072300);
		cm.removeNpc(9072301);
		cm.removeNpc(9072302);
		cm.removeNpc(9072303);
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 22, 700));
		cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
		cm.getPlayer().getClient().send(SLFCGPacket.SetStandAloneMode(false));
		cm.warp(993191401);
		cm.dispose();
		cm.openNpc(2007, "PinkBeanTuto1");
	}
}

function statusplus(millsecond) {
	cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}

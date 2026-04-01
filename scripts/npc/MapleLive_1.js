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
		cm.getPlayer().getMap().resetFully();
		cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 16));
		cm.getClient().send(CField.UIPacket.getDirectionStatus(true));
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
		cm.getClient().send(SLFCGPacket.SetStandAloneMode(true));
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 250, 2));
		statusplus(250);
	} else if (status == 1) {
		cm.getClient().send(SLFCGPacket.cMakeBlind(0x19, 0, 0, 0, 0, 1000, 3000, 1));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1000, 2147483647, 2147483647, 2147483647));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 5, 1, 0, 0));
		statusplus(300);
	} else if (status == 2) {
		cm.getClient().send(SLFCGPacket.cMakeBlind2(0x1A, 1000, 0, 0, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1500, 0, 450, 700));
		npc1 = cm.spawnNpc2(9062554, new Point(580, 490), true);
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "summon", 0, false));
		npc2 = cm.spawnNpc2(9062558, new Point(700, 490), true);
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "summon", 0, false));
		cm.AllInvisibleName(true);
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 2));
		statusplus(100);
	} else if (status == 3) {
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 250, 0));
		statusplus(300);
	} else if (status == 4) {
		cm.getClient().send(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs18#<LIVE 스튜디오>", 100, 1000, 6, -50, -50, 1, 4));
		statusplus(2000);
	} else if (status == 5) {
		cm.sendNextS("#face0#어서 와!\r\n여기는 #b<메이플 LIVE>#k의 방송을 위한 장소! \r\n#rLIVE 스튜디오#k야!", 37, 9062554)
	} else if (status == 6) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 17, 2, 290, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 2000, 2500, 2000, 600, 700));
		statusplus(2000);
	} else if (status == 7) {
		cm.sendNextS("#fs24#오! 잠깐 멈춰!!!!", 37, 9062554)
	} else if (status == 8) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/whatl", 2, 0, 20, 0, 0, 1, 0, 0, 0, 0));
		statusplus(1000);
	} else if (status == 9) {
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 255, 255, 255, 100, 0));
		statusplus(100);
	} else if (status == 10) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/omega/cartoon/7"));
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 100, 0));
		statusplus(300);
	} else if (status == 11) {
		statusplus(100);
	} else if (status == 12) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/omega/cartoon/7"));
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 100, 0));
		statusplus(300);
	} else if (status == 13) {
		cm.sendNextS("으앗!", 57);
	} else if (status == 14) {
		cm.sendNextS("#face0#음~ 역시! 내가 잘 알아봤어", 37, 9062554)
	} else if (status == 15) {
		cm.sendNextS("#face0#흠... 나쁘진 않군.", 37, 9062558)
	} else if (status == 16) {
		cm.sendNextS("#face0#너... 나와 함께 #r슈퍼스타#k가 되어 보는 게 어때?", 37, 9062554)
	} else if (status == 17) {
		cm.sendNextS("슈퍼스타?", 57);
	} else if (status == 18) {
		cm.sendNextS("#face0#그래! #b<메이플 LIVE>#k에서 #r크리에이터#k가 되어 직접 방송을 제작하고 \r\n출연도 하는 거야!", 37, 9062554)
	} else if (status == 19) {
		cm.sendNextS("#face0#딱 너 같은 #r멋진 모험가#k를 찾고 있었어!", 37, 9062554);
	} else if (status == 20) {
		cm.sendNextS("멋진...모험가...!", 57);
	} else if (status == 21) {
		cm.sendNextS("#face0#요즘 방송 트렌드는 #b열심히 모험하는 모습#k을 보여주는 거야!\r\n만약 #b강한 모험가#k라면 더욱 인기를 얻을 수 있지!", 37, 9062558)
	} else if (status == 22) {
		cm.sendNextS("#face0#자, 내가 시키는 대로만 하면 너는 구독자가 쭉쭉 늘어나 #r엄청난 크리에이터#k가 될 수 있을 거야", 37, 9062554);
	} else if (status == 23) {
		cm.sendNextS("하지만 내가 방송을 잘 할 수 있을까?", 57);
	} else if (status == 24) {
		cm.sendNextS("#face0#그럼! 내가 준비한 #b크리에이터 스텝업#k과 함께라면 너도 #r슈퍼스타#k가 될 수 있어!", 37, 9062554);
	} else if (status == 25) {
		cm.sendNextS("#face0#내가 준비한 미션을 진행하면서 그걸 방송으로 찍는 거야!", 37, 9062554);
	} else if (status == 26) {
		cm.sendNextS("좋아...! 그러면 나 열심히 해볼게!", 57);
	} else if (status == 27) {
		cm.getClient().send(CField.musicChange("BgmEvent/mapleLIVE_pinkbean"));
		statusplus(1000);
	} else if (status == 28) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/whatl", 2, 0, 20, 0, 0, 1, 0, 0, 0, 0));
		statusplus(1000);
	} else if (status == 29) {
		cm.sendNextS("#face0##fs24# #fc0xffffaabb#후후후. 여러분! 그럼 저의 새로운 콘텐츠!", 37, 9062554);
	} else if (status == 30) {
		cm.sendNextS("#face0##fs24# #fc0xffffaabb#초보 크리에이터 육성기! 잘 부탁해요~!", 37, 9062554);
	} else if (status == 31) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/Freinds/crowd2"));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(false, "00", "Effect/particle/MLive_pink_01", -300, 12));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(false, "01", "Effect/particle/MLive_pink_02", -300, 12));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(false, "02", "Effect/particle/MLive_pink_03", -300, 12));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(false, "03", "Effect/particle/MLive_blue_01", -300, 12));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(false, "04", "Effect/particle/MLive_blue_02", -300, 12));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(false, "05", "Effect/particle/MLive_blue_03", -300, 12));
		statusplus(2000);
	} else if (status == 32) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/ddam", 2, 0, 0, 0, 0, 1, 0, 0, 0, 0));
		statusplus(1500);
	} else if (status == 33) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/Freinds/crowd2"));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(true, "00", "", 700, 0));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(true, "01", "", 700, 0));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(true, "02", "", 700, 0));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(true, "03", "", 700, 0));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(true, "04", "", 700, 0));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(true, "05", "", 700, 0));
		statusplus(300);
	} else if (status == 34) {
		cm.getClient().send(CField.EffectPacket.showWZEffect2("SoundEff.img/Guitar_noise4"));
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 2000, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 22, 700));
		cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
		cm.getClient().send(SLFCGPacket.SetStandAloneMode(false));
		cm.removeNpc(9062554);
		cm.removeNpc(9062558);
		cm.warp(993194000);
		cm.dispose();
		cm.openNpc(2007, "MapleLive_2");
	}

}

function statusplus(millsecond) {
	cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}

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
		cm.getClient().send(CField.UIPacket.getDirectionStatus(true));
		cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
		cm.getClient().send(SLFCGPacket.SetStandAloneMode(true));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 16));
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
		cm.getClient().send(CField.musicChange("Bgm00/Silence"));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 2000, 0, 600, -850));
		npc1 = cm.spawnNpc2(9062554, new Point(600, -855), true);
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "summon", 0, false));
		cm.getClient().send(CField.setMapOBJ("light_top_1", 0, 0, 0));
		cm.getClient().send(CField.setMapOBJ("light_top_2", 0, 0, 0));
		cm.getClient().send(CField.setMapOBJ("light_left", 0, 0, 0));
		cm.getClient().send(CField.setMapOBJ("light_right", 0, 0, 0));
		cm.getClient().send(CField.setMapOBJ("light_center", 0, 0, 0));
		var array = ['light_center'];
		cm.getClient().send(SLFCGPacket.FootHoldOnOffEffect(array, true));
		cm.AllInvisibleName(true);
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc1, 0, 0));
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1000, 0));
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/cadena/mic"));
		cm.sendNextS("#face0#화려한...", 37,9062554);
	} else if (status == 1) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc1, 255, 900));
		cm.sendNextS("#face0#조명이......", 37,9062554);
	} else if (status == 2) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/HofM/act3/powerdown"));
		cm.getClient().send(CField.setMapOBJ("light_top_1", 1, 0, 0));
		statusplus(300);
	} else if (status == 3) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/HofM/act3/powerdown"));
		cm.getClient().send(CField.setMapOBJ("light_top_2", 1, 0, 0));
		cm.sendCustom("#face0##fs24#나를.........", 37,9062554, 600);
	} else if (status == 4) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/HofM/act3/powerdown"));
		cm.getClient().send(CField.setMapOBJ("light_left", 1, 0, 0));
		statusplus(300);
	} else if (status == 5) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/HofM/act3/powerdown"));
		cm.getClient().send(CField.setMapOBJ("light_right", 1, 0, 0));
		cm.getClient().send(CField.musicChange("BgmEvent/mapleLIVE_pinkbean"));
		cm.sendCustom("#face0##fs36#감싸네!!", 37,9062554, 600);
	} else if (status == 6) {
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 500, 255, 255, 255, 500, 0));
		statusplus(500);
	} else if (status == 7) {
		cm.getClient().send(CField.setMapOBJ("light_top_1", 0, 0, 0));
		cm.getClient().send(CField.setMapOBJ("light_top_2", 0, 0, 0));
		cm.getClient().send(CField.setMapOBJ("light_left", 0, 0, 0));
		cm.getClient().send(CField.setMapOBJ("light_right", 0, 0, 0));
		cm.getClient().send(CField.setMapOBJ("light_center", 0, 0, 0));
		cm.getClient().send(CField.setSpecialMapEffect("back", 0, 0));
		cm.sendCustom("#face0##fs40##fc0xffffaabb#여러분~ 사랑해요~!!!!", 37,9062554, 1000);
		cm.removeNpc(9062554);
		npc2 = cm.spawnNpc2(9062554, new Point(725, -330), false);
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "summon", 0, false));
		cm.AllInvisibleName(true);
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1500, 0, 719, 275));
	} else if (status == 8) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 2500, 900, 0, 719, 275));
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 600, 0));
		cm.getClient().send(CField.fireBlink(cm.getPlayer().getId(), new Point(375, -330)));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 18, 1));
		statusplus(100);
	} else if (status == 9) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/Freinds/crowd2"));
		statusplus(2000);
	} else if (status == 10) {
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 255, 255, 255, 100, 0));
		statusplus(100);
	} else if (status == 11) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/omega/cartoon/7"));
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 100, 0));
		statusplus(100);
	} else if (status == 12) {
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 500, 255, 255, 255, 100, 0));
		statusplus(100);
	} else if (status == 13) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 2000, 0, 730, -300));
		statusplus(100);
	} else if (status == 14) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/omega/cartoon/7"));
		statusplus(300);
	} else if (status == 15) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 4000, 2500, 0, 730, -300));
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 100, 0));
		statusplus(1500);
	} else if (status == 16) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/Freinds/crowd2"));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(false, "00", "Effect/particle/MLive_pink_01", -300, 12));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(false, "01", "Effect/particle/MLive_pink_02", -300, 12));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(false, "02", "Effect/particle/MLive_pink_03", -300, 12));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(false, "03", "Effect/particle/MLive_blue_01", -300, 12));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(false, "04", "Effect/particle/MLive_blue_02", -300, 12));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(false, "05", "Effect/particle/MLive_blue_03", -300, 12));
		statusplus(2000);
	} else if (status == 17) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/Freinds/crowd2"));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(true, "00", "", 700, 0));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(true, "01", "", 700, 0));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(true, "02", "", 700, 0));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(true, "03", "", 700, 0));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(true, "04", "", 700, 0));
		cm.getClient().send(SLFCGPacket.ShowEffectParticle(true, "05", "", 700, 0));
		statusplus(300);
	} else if (status == 18) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 17, 2, 100, 0));
		statusplus(1000);
	} else if (status == 19) {
		cm.sendNextS("#face0##fs24# #fc0xffffaabb# 핑핑핑핑 핑크! 핑크! 핑크빈!", 37, 9062554);
	} else if (status == 20) {
		statusplus(500);
	} else if (status == 21) {
		cm.sendNextS("#face0##fs24# #fc0xffffaabb# 여러분들의 귀염둥이~ 핑크빈!!! 꺄아~", 37, 9062554);
	} else if (status == 22) {
		statusplus(1000);
	} else if (status == 23) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/whatl", 2, 0, 20, 0, 0, 1, 0, 0, 0, 0));
		statusplus(1000);
	} else if (status == 24) {
		cm.sendNextS("(..? 혼자서 뭐라고 떠드는 거지..?)", 57);
	} else if (status == 25) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 17, 2, 100, 0));
		statusplus(1000);
	} else if (status == 26) {
		cm.sendNextS("저기...?", 57);
	} else if (status == 27) {
		cm.getClient().send(CField.NPCPacket.setNPCMotion(npc2, -1));
		statusplus(500);
	} else if (status == 28) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 2, 0, 0, 0, 0, 1, npc2, 0, 0, 0));
		statusplus(500);
	} else if (status == 29) {
		cm.sendNextS("#face0#앗! 너는!!!", 37, 9062554);
	} else if (status == 30) {
		cm.sendNextS("#face0##fs24# #fc0xffffaabb#여러분! 오늘 방송은 여기까지 할게요!", 37, 9062554);
	} else if (status == 31) {
		cm.getClient().send(CField.playSound("Sound/SoundEff.img/Freinds/crowd2"));
		cm.sendCustom("#face0##fs40##fc0xffffaabb#다음에 또 만나요~ 안녕~!!!!\r\n?\r\n", 37,9062554, 2000);
	} else if (status == 32) {
		cm.sendNextS("저기... 지금 뭐 하는 거야?", 57);
	} else if (status == 33) {
		cm.sendNextS("#face0#너... #r슈퍼스타#k의 자질이 느껴져!", 37, 9062554);
	} else if (status == 34) {
		cm.sendNextS("...?", 57);
	} else if (status == 35) {
		cm.sendNextS("#face0#나와 함께 #b<LIVE 스튜디오>#k로 가자!", 37, 9062554);
	} else if (status == 36) {
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 2000, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 22, 700));
		cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
		cm.getClient().send(SLFCGPacket.SetStandAloneMode(true));
		cm.removeNpc(9062554);
		cm.dispose();
		cm.warp(993194002);
	}
}

function statusplus(millsecond) {
	cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}

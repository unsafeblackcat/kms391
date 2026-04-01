importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);

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
var npc5 = 0;
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
		if (!check10) {
			check10 = true;
			cm.removeNpc(9072306);
			cm.removeNpc(9072307);
			cm.removeNpc(9072308);
			cm.removeNpc(9072309);
			cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, true, false, false));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x10));
			cm.getClient().send(SLFCGPacket.SetStandAloneMode(true));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1500, 0, 3465, 734));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 10, 1));
			npc1 = cm.spawnNpc2(9072306, new Point(4220, 646));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "summon", 0, false));
			npc2 = cm.spawnNpc2(9072308, new Point(3955, 698));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "summon", 0, false));
			npc3 = cm.spawnNpc2(9072306, new Point(3653, 729));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "summon", 0, false));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc3, 0, 10));
			npc4 = cm.spawnNpc2(9072307, new Point(3366, 403));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc4, "summon", 0, false));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc4, 0, 10));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc4, "umbrella", -1, true));
			cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
			statusplus(1200);
		}
	} else if (status == 1) {
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1000, 0));
		statusplus(1400);
	} else if (status == 2) {
		cm.getClient().send(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs18#헤네시스 어딘가", 100, 1000, 6, -50, -110, 1, 4));
		statusplus(5000);
	} else if (status == 3) {
		cm.getClient().send(SLFCGPacket.getNpcMoveAction(npc2, -1, 400, 150));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/jump"));
		statusplus(1000);
	} else if (status == 4) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/jump"));
		statusplus(1000);
	} else if (status == 5) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/jump"));
		statusplus(1000);
	} else if (status == 6) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/jump"));
		statusplus(1000);
	} else if (status == 7) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 1000, 1500, 1000, 4193, 701));
		statusplus(2000);
	} else if (status == 8) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "저 버섯, 약해 보인다. ", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc1, 1699));
		statusplus(2000);
	} else if (status == 9) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "버섯 해치우는 것 정돈 예티 머리 긁기보다 쉽다!", 0, 0, 3000, 1, 0, 0, 0, 4, 9072306, npc1, 1699));
		cm.getClient().send(CField.NPCPacket.setNPCMotion(npc2, 1));
		statusplus(3000);
	} else if (status == 10) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 1000, 1500, 1000, 3509, 734));
		statusplus(1800);
	} else if (status == 11) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "jump", 1, true));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc3, 255, 100));
		statusplus(500);
	} else if (status == 12) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/Direction26.img/Yeti/effect/0", 2, 0, 0, 0, 0, 1, npc3, 0, 0, 0));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "surprise", 1, true));
		statusplus(2000);
	} else if (status == 13) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "받아라, 예티 펀치!", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(2000);
	} else if (status == 14) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "attack", 1, true));
		statusplus(200);
	} else if (status == 15) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/Attack"));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "jump3", 1, true));
		statusplus(500);
	} else if (status == 16) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/jump"));
		statusplus(2500);
	} else if (status == 17) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "이, 이게 아니다..! 다시 예티 펀치!", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(2000);
	} else if (status == 18) {
		cm.getClient().send(SLFCGPacket.SpawnDirectionObject(3904, 28791, 30875470, 1, 7707));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "attack", 1, true));
		statusplus(200);
	} else if (status == 19) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/Attack"));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "jump3", 1, true));
		statusplus(500);
	} else if (status == 20) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/jump"));
		statusplus(2000);
	} else if (status == 21) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/what", 2, 0, 0, -50, 0, 1, npc2, 0, 0, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/what", 2, 0, 0, -50, 0, 1, npc3, 0, 0, 0));
		statusplus(2000);
	} else if (status == 22) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "예티의 펀치가 이렇게 하찮을 리 없다! 다시 한다! ", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(2000);
	} else if (status == 23) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/Attack"));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "attack2", 1, true));
		statusplus(300);
	} else if (status == 24) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/Attack"));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/jump"));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "jump4", 1, true));
		statusplus(500);
	} else if (status == 25) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/jump"));
		statusplus(3500);
	} else if (status == 26) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "다, 다시...!", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(2000);
	} else if (status == 27) {
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 500, 0));
		statusplus(2000);
	} else if (status == 28) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "prone", -1, true));
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
		statusplus(1200);
	} else if (status == 29) {
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1000, 0));
		statusplus(2900);
	} else if (status == 30) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "헥…헥… 예티가… 졌다… 버섯 강하다…", 0, 0, 2000, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(3000);
	} else if (status == 31) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(0, "고작 버섯 따위에게 쩔쩔매는 녀석이 있다니!", 0, 0, 3000, 1, 0, 200, 0, 4, 9072307, 0, 1699));
		statusplus(3000);
	} else if (status == 32) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction3(npc3));
		cm.getClient().send(CField.NPCPacket.setNPCMotion(npc2, -1));
		statusplus(1000);
	} else if (status == 33) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc4, 255, 100));
		cm.getClient().send(SLFCGPacket.SetNpcMotion(9072307, 0, 300, 4000));
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "누, 누구냐!", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(4000);
	} else if (status == 34) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction3(npc4));
		statusplus(2000);
	} else if (status == 35) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "보아하니 넌 어지간히 약한 모양이구나.", 0, 0, 3000, 1, 0, 300, 0, 4, 9072307, npc4, 1699));
		statusplus(3000);
	} else if (status == 36) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "내가 시범을 보여줄게. 잘 봐.", 0, 0, 3000, 1, 0, 300, 0, 4, 9072307, npc4, 1699));
		statusplus(3000);
	} else if (status == 37) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "받아라, 핑크빈 어택!", 0, 0, 3000, 1, 0, 300, 0, 4, 9072307, 0, 1699));
		statusplus(3000);
	} else if (status == 38) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/jump"));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "jump3", 1, true));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/Attack"));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc4, "attack", 1, true));
		statusplus(2000);
	} else if (status == 39) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "어라?", 0, 0, 2000, 1, 0, 300, 0, 4, 9072307, 0, 1699));
		statusplus(2000);
	} else if (status == 40) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "…….", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(2000);
	} else if (status == 41) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(0, "#fn나눔고딕 ExtraBold##fs15# 왜, 내가 '레벨 왕창 다운'의 저주에 걸린 거야!!", 0, 0, 3000, 1, 0, 300, 0, 4, 9072307, 0, 1699));
		statusplus(4000);
	} else if (status == 42) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "…….", 0, 0, 1500, 1, 0, 0, 0, 4, 9072308, npc2, 1699));
		statusplus(2000);
	} else if (status == 43) {
		cm.getClient().send(CField.NPCPacket.setNPCMotion(npc2, 1));
		statusplus(1000);
	} else if (status == 44) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "풉", 0, 0, 1500, 1, 0, 0, 0, 4, 9072308, npc2, 1699));
		statusplus(2000);
	} else if (status == 45) {
		cm.getClient().send(SLFCGPacket.getNpcMoveAction(npc2, 1, 400, 150));
		statusplus(800);
	} else if (status == 46) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/jump"));
		statusplus(800);
	} else if (status == 47) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/jump"));
		statusplus(500);
	} else if (status == 48) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/yetiJob/jump"));
		statusplus(1000);
	} else if (status == 49) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "prone", -1, true));
		statusplus(1800);
	} else if (status == 50) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc2, 0, 1000));
		statusplus(1000);
	} else if (status == 51) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "너, 약하다. 예티, 실망했다.", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(3000);
	} else if (status == 52) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "약하다니! 내 앞에서 무릎을 꿇은 모험가들이 얼마나 많은데!", 0, 0, 3000, 1, 0, 300, 0, 4, 9072307, 0, 1699));
		statusplus(3000);
	} else if (status == 53) {
		cm.getClient().send(SLFCGPacket.SpawnDirectionObject(14416, -26662, 30875470, 1, 7707));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction3(npc3));
		statusplus(1000);
	} else if (status == 54) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "예티가 주먹을 내려치면 땅이 부서지고 빙산이 깨졌다!", 0, 0, 2000, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(3000);
	} else if (status == 55) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "그렇다면 직접 겨뤄보자!", 0, 0, 3000, 1, 0, 300, 0, 4, 9072307, 0, 1699));
		statusplus(3000);
	} else if (status == 56) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "좋다! 싸운다! ", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(2000);
	} else if (status == 57) {
		npc5 = cm.spawnNpc2(9072309, new Point(3052, 625));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc5, "summon", 0, false));
		cm.getClient().send(SLFCGPacket.getNpcMoveAction(npc5, 1, 400, 150));
		statusplus(1000);
	} else if (status == 58) {
		statusplus(500);
	} else if (status == 59) {
		cm.getClient().send(CField.NPCPacket.setNPCMotion(npc3, -1));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 2, 0, 0, -50, 0, 1, npc3, 0, 0, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 2, 0, 0, 280, 0, 1, npc4, 0, 0, 0));
		statusplus(500);
	} else if (status == 60) {
		statusplus(500);
	} else if (status == 61) {
		statusplus(1000);
	} else if (status == 62) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/whatl", 2, 0, 0, 0, 0, 1, npc5, 0, 0, 0));
		statusplus(1500);
	} else if (status == 63) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "지, 지나가시죠.", 0, 0, 2000, 1, 0, 300, 0, 4, 9072307, 0, 1699));
		statusplus(2000);
	} else if (status == 64) {
		cm.getClient().send(SLFCGPacket.getNpcMoveAction(npc5, 1, 450, 150));
		statusplus(1000);
	} else if (status == 65) {
		statusplus(1000);
	} else if (status == 66) {
		statusplus(1000);
	} else if (status == 67) {
		cm.getClient().send(CField.NPCPacket.setNPCMotion(npc3, 1));
		statusplus(1000);
	} else if (status == 68) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc5, 0, 1000));
		statusplus(4500);
	} else if (status == 69) {
		cm.getClient().send(CField.NPCPacket.setNPCMotion(npc3, -1));
		statusplus(3000);
	} else if (status == 70) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/ddam", 2, 0, 0, -50, 0, 1, npc3, 0, 0, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/ddam", 2, 0, 0, 280, 0, 1, npc4, 0, 0, 0));
		statusplus(2000);
	} else if (status == 71) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "우리.. 대결은 서로 힘을 되찾은 다음으로 미루는 게 어때?", 0, 0, 2000, 1, 0, 300, 0, 4, 9072307, 0, 1699));
		statusplus(2000);
	} else if (status == 72) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "예, 예티도 맞는 말이라고 생각한다.", 0, 0, 2000, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(2000);
	} else if (status == 73) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "수련을 하고 온 후에 누가 진정한 강자인지 겨루는 거야! 재밌겠는걸?", 0, 0, 2000, 1, 0, 300, 0, 4, 9072307, 0, 1699));
		statusplus(2000);
	} else if (status == 74) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "좋다! 강해진다! ", 0, 0, 2000, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(2000);
	} else if (status == 75) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 1000, 1500, 1000, 3685, 734));
		statusplus(2000);
	} else if (status == 76) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "예티 질 수 없다.  강해진 다음에  와장창 우지끈해줄 거다!", 0, 1, 3000, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(3000);
	} else if (status == 77) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "이곳 낯설다. 그래도 예티는 용감하니까 열심히 수련할 수 있다!", 0, 1, 3000, 1, 0, 0, 0, 4, 9072306, npc3, 1699));
		statusplus(4000);
	} else if (status == 78) {
		cm.removeNpc(9072306);
		cm.removeNpc(9072307);
		cm.removeNpc(9072308);
		cm.removeNpc(9072309);
		cm.warp(100000051);
		cm.changeJob(13500);
		for (var i = cm.getPlayer().getLevel(); i < 10; i++)
		cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 22, 700));
		cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
		cm.getPlayer().getClient().send(SLFCGPacket.SetStandAloneMode(false));
		cm.dispose();
	}
}

function sendByType(type, type2, text) {
	switch (type) {
		case "next":
			cm.sendNextS(text, type2, 1012110, 0);
			break;
		case "nextprev":
			cm.sendNextPrevS(text, type2, 1012110, 0);
			break;
		case "yesno":
			cm.sendYesNoS(text, type2, 1012110);
			break;
		default:
			cm.sendOk(text, 1012110);
			break;
	}
}

function sendByType(type, type2, text, npc) {
	switch (type) {
		case "next":
			cm.sendNextS(text, type2, npc, 0);
			break;
		case "nextprev":
			cm.sendNextPrevS(text, type2, npc, 0);
			break;
		case "yesno":
			cm.sendYesNoS(text, type2, npc);
			break;
		default:
			cm.sendOk(text, 1012110);
			break;
	}
}


function statusplus(millsecond) {
	cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}
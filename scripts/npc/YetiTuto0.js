


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
			cm.removeNpc(9072305);
			cm.removeNpc(9072306);
			cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, true, false, false));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x10));
			cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
			cm.getPlayer().getClient().send(SLFCGPacket.SetStandAloneMode(true));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1000, 0, -11, -5));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 10, 1));
			npc1 = cm.spawnNpc2(9072305, new Point(-158, -80));
			npc2 = cm.spawnNpc2(9072306, new Point(-9, 4));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "sit", -1, true));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc2, 0, 10));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "babel", -1, true));
			cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
			statusplus(1200);
		}
	} else if (status == 1) {
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1000, 0));
		statusplus(1400);
	} else if (status == 2) {
		cm.getClient().send(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs18#설원 어딘가, 예티의 집", 100, 1000, 6, -50, -110, 1, 4));
		statusplus(5000);
	} else if (status == 3) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "백이십 하나… 백이십 둘…백이십….", 0, 0, 2000, 1, 0, 0, 0, 4, 9072305, npc1, 1660));
		statusplus(3000);
	} else if (status == 4) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "예티, 숫자 까먹었다.", 0, 0, 2000, 1, 0, 0, 0, 4, 9072305, npc1, 1660));
		statusplus(3000);
	} else if (status == 5) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction3(npc1));
		statusplus(1000);
	} else if (status == 6) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "예티, 운동 열심히 했다. 이제 밖의 예티들보다 훨씬 세다.", 0, 0, 3000, 1, 0, 0, 0, 4, 9072305, npc1, 1660));
		statusplus(3000);
	} else if (status == 7) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "그래도 예티는 원한다. 더 더 더 힘이 세지는 법!", 0, 0, 3000, 1, 0, 0, 0, 4, 9072305, npc1, 1660));
		statusplus(3000);
	} else if (status == 8) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "모르는 거 있으면 페페가 독서하라고 했다.", 0, 0, 3000, 1, 0, 0, 0, 4, 9072305, npc1, 1660));
		statusplus(3000);
	} else if (status == 9) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "what", 1, true));
		statusplus(1500);
	} else if (status == 10) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "깜빡했다. 예티 책들은 전부 냄비 받침대로 있다.", 0, 0, 2000, 1, 0, 0, 0, 4, 9072305, npc1, 1660));
		statusplus(3000);
	} else if (status == 11) {
		cm.getClient().send(SLFCGPacket.getNpcMoveAction(npc1, 1, 250, 100));
		statusplus(2000);
	} else if (status == 12) {
		statusplus(2500);
	} else if (status == 13) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "이 냄비 아니다.. 저 냄비 아니다..  ", 0, 0, 2000, 1, 0, 0, 0, 4, 9072305, npc1, 1660));
		statusplus(3000);
	} else if (status == 14) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "찾았다! <힘세지는 약을 만드는 법> ", 0, 0, 2000, 1, 0, 0, 0, 4, 9072305, npc1, 1660));
		statusplus(3000);
	} else if (status == 15) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "주니어 페페의 물고기를 넣고 시계 반대 방향으로 세 번 젓기..", 0, 0, 3000, 1, 0, 0, 0, 4, 9072305, npc1, 1660));
		statusplus(3000);
	} else if (status == 16) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "레시피대로 제조하지 않을 시 치명적인 부작용이 발생...", 0, 0, 3000, 1, 0, 0, 0, 4, 9072305, npc1, 1660));
		statusplus(3000);
	} else if (status == 17) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/ddam", 2, 0, 0, -50, 0, 1, npc1, 0, 0, 0));
		statusplus(500);
	} else if (status == 18) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "what", 1, true));
		statusplus(3000);
	} else if (status == 19) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "예티, 무슨 말인지 모르지만 열심히 만든다.", 0, 0, 3000, 1, 0, 0, 0, 4, 9072305, npc1, 1660));
		statusplus(3500);
	} else if (status == 20) {
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
		statusplus(1600);
	} else if (status == 21) {
		cm.getClient().send(SLFCGPacket.PlayAmientSound("SoundEff.img/Dlep5/pot", 200, 60));
		statusplus(100);
	} else if (status == 22) {
		cm.getClient().send(CField.environmentChange("Sound/SoundEff.img/Dlep5/willbreak", 5));
		cm.sendScreenText("잠시후...", true);
		cm.getClient().send(SLFCGPacket.getNpcMoveAction(npc1, -1, 80, 200));
	} else if (status == 23) {
		statusplus(1000);
	} else if (status == 24) {
		cm.getClient().send(SLFCGPacket.StopAmientSound("SoundEff.img/Dlep5/pot"));
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1300, 0));
		statusplus(1600);
	} else if (status == 25) {
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1300, 0));
		statusplus(1600);
	} else if (status == 26) {
		statusplus(500);
	} else if (status == 27) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "……….", 0, 0, 1500, 1, 0, 0, 0, 4, 9072305, npc1, 1699));
		statusplus(2000);
	} else if (status == 28) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "맛없어 보인다. 맛없는 음식은 먹기 싫다!", 0, 0, 2000, 1, 0, 0, 0, 4, 9072305, npc1, 1699));
		statusplus(3000);
	} else if (status == 29) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 2, 0, 0, -50, 0, 1, npc1, 0, 0, 0));
		statusplus(2000);
	} else if (status == 30) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "예티, 좋은 생각났다.", 0, 0, 1500, 1, 0, 0, 0, 4, 9072305, npc1, 1699));
		statusplus(2000);
	} else if (status == 31) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "아이스크림 맛있다. 아이스크림 넣으면 맛있어진다!", 0, 0, 1500, 1, 0, 0, 0, 4, 9072305, npc1, 1699));
		statusplus(3000);
	} else if (status == 32) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction4(npc1, "medcine", "medcine2", 1, 2, 1, -1));
		statusplus(2800);
	} else if (status == 33) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "완성했다!", 0, 0, 2800, 1, 0, 0, 0, 4, 9072305, npc1, 1699));
		statusplus(3000);
	} else if (status == 34) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "eat", 1, true));
		cm.getClient().send(CField.environmentChange("Sound/SoundEff.img/cernium3/waterDrink", 5));
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "(꿀꺽 꿀꺽)", 0, 0, 3000, 1, 0, 0, 0, 4, 9072305, npc1, 1699));
		statusplus(3000);
	} else if (status == 35) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 2, 0, 0, -50, 0, 1, npc1, 0, 0, 0));
		statusplus(1500);
	} else if (status == 36) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "예티!! 알 수 없는 힘이 느껴진다! ", 0, 0, 2000, 1, 0, 0, 0, 4, 9072305, npc1, 1699));
		statusplus(2000);
	} else if (status == 37) {
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 500, 0));
		statusplus(500);
	} else if (status == 38) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc1, 0, 100));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc2, 255, 1000));
		cm.getClient().send(CField.environmentChange("Sound/SoundEff.img/savageT/boom", 5));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc1, 0, 10));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc2, 255, 10));
		statusplus(4000);
	} else if (status == 39) {
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1000, 0));
		statusplus(1400);
	} else if (status == 40) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "예티 귀여워졌다!", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(2000);
	} else if (status == 41) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "이런 큐티함은 예티와 어울리지 않지만..", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(2000);
	} else if (status == 42) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction3(npc2));
		statusplus(1000);
	} else if (status == 43) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/love", 2, 0, 0, -50, 0, 1, npc2, 0, 0, 0));
		statusplus(2500);
	} else if (status == 44) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "나쁘지 않다!", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(2000);
	} else if (status == 45) {
		cm.getClient().send(SLFCGPacket.getNpcMoveAction(npc2, -1, 120, 100));
		statusplus(2000);
	} else if (status == 46) {
		statusplus(2000);
	} else if (status == 47) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction4(npc2, "babelS", "babel3", 1, 2, 1, -1));
		statusplus(1500);
	} else if (status == 48) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "힘 세졌으니 이런 거 한 손으로 들 수 있다!", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(1500);
	} else if (status == 49) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction4(npc2, "babel2", "babel3", 1, 2, 1, -1));
		statusplus(4000);
	} else if (status == 50) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "……….", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(2000);
	} else if (status == 51) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "두, 두 손으로는…?", 0, 0, 3000, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(3000);
	} else if (status == 52) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "babel", -1, true));
		statusplus(4000);
	} else if (status == 53) {
		cm.getClient().send(SLFCGPacket.SpawnDirectionObject(21520, 19010, 30875470, 1, 7707));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction3(npc2));
		statusplus(1000);
	} else if (status == 54) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "……….", 0, 0, 3000, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(3000);
	} else if (status == 55) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "!!!!!!!", 0, 0, 3000, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(3000);
	} else if (status == 56) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "sit", -1, true));
		statusplus(1500);
	} else if (status == 57) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/blackHeaven/secretmission3"));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 2000, 0, -163, 158));
		statusplus(1000);
	} else if (status == 58) {
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/blackHeaven/secretmission3"));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 3000, 0, -157, 173));
		statusplus(500);
	} else if (status == 59) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "#fn나눔고딕 ExtraBold##fs30#예티가… 약해졌다!!!!", 0, 0, 3000, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(3000);
	} else if (status == 60) {
		cm.getClient().send(SLFCGPacket.cMakeBlind(0x19, 0, 0, 0, 0, 1000, 3000, 1));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1000, 0, -11, -5));
		statusplus(1300);
	} else if (status == 61) {
		cm.getClient().send(SLFCGPacket.cMakeBlind(0x1A, 1000, 0, 0, 0, 0, 0, 0));
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "cry", -1, true));
		statusplus(3000);
	} else if (status == 62) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "예티… 약한 거 싫다…", 0, 0, 2000, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(2000);
	} else if (status == 63) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "옛날처럼 강해지고 싶다…", 0, 0, 2000, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(2000);
	} else if (status == 64) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction3(npc2));
		statusplus(2000);
	} else if (status == 65) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 2, 0, 0, -50, 0, 1, npc2, 0, 0, 0));
		statusplus(2500);
	} else if (status == 66) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "예티 결심했다. 메이플 월드로 갈 거다!", 0, 0, 1500, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(4000);
	} else if (status == 67) {
		cm.getClient().send(SLFCGPacket.ShowEffectChatNpc(1, "열심히 훈련해서 다시 힘세고 강한 예티 될 거다!  ", 0, 0, 4000, 1, 0, 0, 0, 4, 9072306, npc2, 1699));
		statusplus(2000);
	} else if (status == 68) {
		cm.removeNpc(9072305);
		cm.removeNpc(9072306);
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 22, 700));
		cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
		cm.getPlayer().getClient().send(SLFCGPacket.SetStandAloneMode(false));
		cm.warp(993191401);
		cm.dispose();
		cm.openNpc(2007, "YetiTuto1");
	}
}

function statusplus(millsecond) {
	cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}

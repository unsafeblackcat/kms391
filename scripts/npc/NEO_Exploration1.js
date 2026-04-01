var status = -1;
var idx, skill, req, sp;
var mission, state, startquestnum, neostone;
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
		if (status == 1 && cm.getPlayer().getKeyValue(501229, "state") != 2) {
			cm.sendOkS("정말 서운해요...\r\n저는 최대한 지금을 즐기고 싶단 말이에요.", 4, 9062475);
		}
		cm.dispose();
		return;
	}
	if (mode == 1) {
		status++;
	}

	if (status == 0) {

		mission = cm.getPlayer().getKeyValue(501229, "mission");
		if (mission < 0) {
			mission = 0;
		}
		state = cm.getPlayer().getKeyValue(501229, "state");
		startquestnum = mission <= 0 ? 501225 : mission == 1 ? 501226 : mission == 2 ? 501227 : mission == 3 ? 501228 : -1;
		neostone = mission <= 1 ? 100 : mission == 2 ? 200 : mission == 3 ? 400 : -1;
		var s = mission <= 0 ? "첫 번째" : mission == 1 ? "두 번째" : mission == 2 ? "세 번째" : mission == 3 ? "네 번째" : "";
		var s2 = mission <= 0 ? "레벨 범위 몬스터 처치!" : mission == 1 ? "엘리트 몬스터/챔피언 처치!" : mission == 2 ? "룬 사용!" : mission == 3 ? "폴로&프리토 클리어!" : "";
		if (startquestnum < 0 || neostone < 0) {
			cm.sendOkS("오류가 발생하였습니다. 관리자에게 문의하여 주세요.", 4, 9062475);
			return;
		}

		if (state == 2) {//완료 시
			cm.sendNextS("탐험은 정말 즐거웠죠?\r\n#b#e#i4310306# #t4310306##k #r"+neostone+"개#k#n를 드릴게요!", 4, 9062475);
		} else {
			cm.sendNextS("#e#b#h0##k#n님!\r\n저를 도와주셔서 정말 감사해요!\r\n이번 주 #r#e"+s+"#n#k 탐험은요!\r\n\r\n바로...#r#e#fs15#"+s2+"\r\n\r\n#fs12##n#k끝나면 #b#e#i4310306# #t4310306# "+neostone+"개#n#k를 드릴게요!", 4, 9062475);
		}
	} else if (status == 1) {
		if (state == 2) {//완료 시
			cm.sendYesNoS("#b#e#h ##n#k님! 지금 탐험을 종료하시겠어요?\r\n\r\n   #r※ <리오의 탐험일지> 보상을 계정 당 1회만 수령 가능합니다.", 4, 9062475);
		} else {
			cm.sendYesNoS("#e#b#h0##k#n님! 바로 시작하실 거죠?", 4, 9062475);
		}
	} else if (status == 2) {
		if (state == 2) {//완료 시
			cm.sendOkS("감사해요! 다음 탐험도 잘 부탁드립니다!", 4, 9062475);
			cm.getPlayer().AddStarDustCoin(1, neostone);
			cm.forceCompleteQuest(startquestnum);
			if (mission < 3) {
				cm.getPlayer().setKeyValue(501229, "mission", "" + (mission + 1));
				cm.getPlayer().setKeyValue(501229, "state", "0");
			}
			cm.dispose();
		} else {
			cm.sendOkS("그럼 지금 당장 출발해요!\r\n탐험을 끝내면 #b#e#i4310306:# #t4310306##n#k를 드릴게요.", 4, 9062475);
			cm.forceStartQuest(startquestnum);
			cm.getPlayer().setKeyValue(501229, "start", "1");
			cm.getPlayer().setKeyValue(501229, "mission", ""+mission);
			cm.getPlayer().setKeyValue(501229, "state", "1");
			cm.dispose();
		}
	}
}
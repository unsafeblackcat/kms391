importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var status = -1;
var enter = "\r\n";
var boxmsg = enter
var NormalReward = [ //10000 = 100%
    //{'item': 2633620, 'min': 1, 'max': 1, 'chance': 100}, //아이리스 레벨업비약1개
	{'item': 4319994, 'min': 1, 'max': 1, 'chance': 1500}, //파란구슬
	{'item': 4319995, 'min': 1, 'max': 1, 'chance': 700}, //악마를쫒는 부적
	{'item': 4319996, 'min': 1, 'max': 1, 'chance': 900}, //호신부적
	{'item': 4319997, 'min': 1, 'max': 1, 'chance': 800}, //아이리스코어
	{'item': 4319999, 'min': 100, 'max': 1000, 'chance': 5000}, //사냥코인
	{'item': 5060048, 'min': 1, 'max': 1, 'chance': 300}, //골드애플
	{'item': 2450163, 'min': 1, 'max': 1, 'chance': 1400}, //경쿠3배 30분
	{'item': 2023072, 'min': 1, 'max': 1, 'chance': 1500}, //드롭률2배
	{'item': 4310229, 'min': 1, 'max': 2000, 'chance': 10}, //유니온코인

]

var AdvancedReward = [
    //{'item': 2633620, 'min': 1, 'max': 1, 'chance': 200}, //아이리스 레벨업비약1개
	{'item': 4319994, 'min': 1, 'max': 5, 'chance': 3000}, //파란구슬
	{'item': 4319995, 'min': 1, 'max': 5, 'chance': 1400}, //악마를쫒는 부적
	{'item': 4319996, 'min': 1, 'max': 5, 'chance': 1800}, //호신부적
	{'item': 4319997, 'min': 1, 'max': 5, 'chance': 1600}, //아이리스코어
	{'item': 4319999, 'min': 100, 'max': 1000, 'chance': 8000}, //사냥코인
	{'item': 5060048, 'min': 1, 'max': 1, 'chance': 600}, //골드애플
	{'item': 2450163, 'min': 1, 'max': 1, 'chance': 2800}, //경쿠3배 30분
	{'item': 2023072, 'min': 1, 'max': 1, 'chance': 3000}, //드롭률2배
	{'item': 4310229, 'min': 1, 'max': 2000, 'chance': 100}, //유니온코인
]

function NormalUnboxing() {
    var ab = 0, qa = 0;
    for (i = 0; i < NormalReward.length; i++) {
        qb = Packages.server.Randomizer.rand(NormalReward[i]['min'], NormalReward[i]['max']);
        tchance = Packages.server.Randomizer.rand(1, 10000);
        if (tchance <= NormalReward[i]['chance']) {
            cm.gainItem(NormalReward[i]['item'], qb);
            ab++;
            boxmsg += "#b#i" + NormalReward[i]['item'] + "##z" + NormalReward[i]['item'] + "# " + qb + "개#k 당첨!" + enter;
            //if (NormalReward[i]['item'] == 2439957 || NormalReward[i]['item'] == 2439959 || NormalReward[i]['item'] == 4033450 || NormalReward[i]['item'] == 4033449 ||
            //    NormalReward[i]['item'] == 1113130 || NormalReward[i]['item'] == 1113131 || NormalReward[i]['item'] == 1113132 || NormalReward[i]['item'] == 1113133 ||
            //    NormalReward[i]['item'] == 1122151 || NormalReward[i]['item'] == 2430034 || NormalReward[i]['item'] == 2633336 || NormalReward[i]['item'] == 2633616 ||
			//	NormalReward[i]['item'] == 2630128 || NormalReward[i]['item'] == 2630129 || NormalReward[i]['item'] == 1190303) {
                for (ac = 0; ac < ab; ++ac) {
                    if (NormalReward[i]['item'] == 1190303) {
                        World.Broadcast.broadcastMessage(CField.getGameMessage(8, cm.getPlayer().getName() + " 님이 돌림판 고급 이용권에서 STAR : 엠블렘을 획득했습니다."));
                    } else {
                        World.Broadcast.broadcastMessage(CField.getGameMessage(8, cm.getPlayer().getName() + " 님이 메소 뽑기에서 " + cm.getItemName(AdvancedReward[i]['item']) + "을 획득했습니다."));
                    }
                    if (ab >= 2) {
                        return;
                    }
                }
            //}
        }
    }
    if (ab == 0) {
        boxmsg += "아쉽게도 꽝이 나왔습니다!";
    }
}

function AdvancedUnboxing() {
    var ab = 0, qa = 0;
    for (i = 0; i < AdvancedReward.length; i++) {
        qb = Packages.server.Randomizer.rand(AdvancedReward[i]['min'], AdvancedReward[i]['max']);
        tchance = Packages.server.Randomizer.rand(1, 10000);
        if (tchance <= AdvancedReward[i]['chance']) {
            cm.gainItem(AdvancedReward[i]['item'], qb);
            ab++;
            boxmsg += "#b#i" + AdvancedReward[i]['item'] + "##z" + AdvancedReward[i]['item'] + "# " + qb + "개#k 당첨!" + enter;
            //if (AdvancedReward[i]['item'] == 2439957 || AdvancedReward[i]['item'] == 2439959 || AdvancedReward[i]['item'] == 4033450 || AdvancedReward[i]['item'] == 4033449 ||
            //    AdvancedReward[i]['item'] == 1113130 || AdvancedReward[i]['item'] == 1113131 || AdvancedReward[i]['item'] == 1113132 || AdvancedReward[i]['item'] == 1113133 ||
            //    AdvancedReward[i]['item'] == 1122151 || AdvancedReward[i]['item'] == 2430034 || AdvancedReward[i]['item'] == 2633336 || AdvancedReward[i]['item'] == 2633616 ||
			//	AdvancedReward[i]['item'] == 2630128 || AdvancedReward[i]['item'] == 2630129 || AdvancedReward[i]['item'] == 1190303) {
                for (ac = 0; ac < ab; ++ac) {
                    if (AdvancedReward[i]['item'] == 1190303) {
                        World.Broadcast.broadcastMessage(CField.getGameMessage(8, cm.getPlayer().getName() + " 님이 돌림판 고급 이용권에서 STAR : 엠블렘을 획득했습니다."));
                    } else {
                        World.Broadcast.broadcastMessage(CField.getGameMessage(8, cm.getPlayer().getName() + " 님이 고급 메소 뽑기에서 " + cm.getItemName(AdvancedReward[i]['item']) + "을 획득했습니다."));
                    }
                    if (ab >= 2) {
                        return;
                    }
                }
            //}
        }
    }
    if (ab == 0) {
        boxmsg += "아쉽게도 꽝이 나왔습니다!";
    }
}

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var chat = "#fs11#"
        chat += "#h0#님 안녕하세요? 팬시 서버에 방문한걸 환영합니다!" + enter
        chat += "일반 이용권 또는 고급 이용권으로 뽑기에 도전해보세요!" + enter + enter
        chat += "#L0#메소 뽑기#l" + enter
        chat += "#L1#고급 메소 뽑기#l"
        cm.sendSimple(chat);
    } else if (status == 1) {
        switch (selection) {
            case 0:
			if (cm.getPlayer().getMeso() >= 10000000000) {
				cm.gainMeso(-10000000000);
				NormalUnboxing();
                cm.sendOkS(boxmsg, 0x24);
				cm.dispose();
			} else {
				cm.sendOkS("메소가 없는것같은데?..", 0x24);
				cm.dispose();
			}
                break;
            case 1:
                if (cm.getPlayer().getMeso() >= 20000000000) {
					cm.gainMeso(-20000000000);
                    AdvancedUnboxing();
                    cm.sendOkS(boxmsg, 0x24);
                    cm.dispose();
                    break;
                } else {
					cm.sendOkS("메소가 없는것같은데?..", 0x24);
                    cm.dispose();
                }
                break;
        }
    }
}
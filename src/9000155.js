importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var status = -1;
var enter = "\r\n";
var boxmsg = enter
var NormalReward = [
    {'item': 4001551, 'min': 10, 'max': 100, 'chance': 3000}, // 10000 = 100% 1천만메소
    {'item': 4319999, 'min': 100, 'max': 700, 'chance': 2000}, // 10000 = 100% 사냥코인
    {'item': 4319997, 'min': 10, 'max': 100, 'chance': 1000}, // 10000 = 100% 코어
    {'item': 4319994, 'min': 10, 'max': 200, 'chance': 1000}, // 10000 = 100% 파란구슬
    //{'item': 5062503, 'min': 1, 'max': 20, 'chance': 500}, // 10000 = 100% 화이트에디셔널큐브
    {'item': 4001780, 'min': 1, 'max': 1, 'chance': 500}, // 10000 = 100% 일반돌림판
	{'item': 4319995, 'min': 3, 'max': 10, 'chance': 500}, // 10000 = 100% 악마부적
	{'item': 4319996, 'min': 3, 'max': 10, 'chance': 500}, // 10000 = 100% 호신부적
	{'item': 4001716, 'min': 1, 'max': 5, 'chance': 500}, // 10000 = 100% 10억메소
	{'item': 4319998, 'min': 1, 'max': 1, 'chance': 300}, // 10000 = 100% 고급돌림판
	{'item': 5060048, 'min': 1, 'max': 1, 'chance': 200}, // 10000 = 100% 골드에플
	//{'item': 2430030, 'min': 1, 'max': 1, 'chance': 120}, // 10000 = 100% 보스초기화
	{'item': 1113998, 'min': 1, 'max': 1, 'chance': 30}, // 10000 = 100% 아스칼론P링
]

var AdvancedReward = [
    {'item': 4319995, 'min': 3, 'max': 15, 'chance': 3000}, // 10000 = 100% 악마부적
    {'item': 4319996, 'min': 3, 'max': 15, 'chance': 3000}, // 10000 = 100% 호신부적
    {'item': 4310308, 'min': 2, 'max': 10, 'chance': 100}, // 10000 = 100% 보공상자
	{'item': 4001780, 'min': 1, 'max': 1, 'chance': 500}, // 10000 = 100% 일반돌림판
	{'item': 4019998, 'min': 1, 'max': 1, 'chance': 300}, // 10000 = 100%  고급돌림판
	{'item': 4319999, 'min': 500, 'max': 3000, 'chance': 2400}, // 10000 = 100% 사냥코인
    {'item': 2431157, 'min': 1, 'max': 1, 'chance': 500}, // 10000 = 100% 최종뎀상자
    {'item': 5062503, 'min': 10, 'max': 30, 'chance': 500}, // 10000 = 100% 화에큐
	{'item': 2430030, 'min': 1, 'max': 1, 'chance': 200}, // 10000 = 100% 보스초기화
    {'item': 1113997, 'min': 1, 'max': 1, 'chance': 50}, // 10000 = 100% 아스칼론Y링
    {'item': 1122998, 'min': 1, 'max': 1, 'chance': 50}, // 10000 = 100% 아스칼론 목걸이
	{'item': 1113995, 'min': 1, 'max': 1, 'chance': 50}, // 10000 = 100% 아스칼론G링
	{'item': 1113994, 'min': 1, 'max': 1, 'chance': 50}, // 10000 = 100% 아스칼론B링
	{'item': 2633620, 'min': 1, 'max': 1, 'chance': 40}, //레벨업비약
]

function NormalUnboxing() {
    var ab = 0, qa = 0;
    for (i = 0; i < NormalReward.length; i++) {
        qb = Packages.server.Randomizer.rand(NormalReward[i]['min'], NormalReward[i]['max']);
        tchance = Packages.server.Randomizer.rand(1, 10000);
        if (tchance <= NormalReward[i]['chance']) {
            cm.gainItem(NormalReward[i]['item'], qb);
            ab++;
            boxmsg += "#b#i" + NormalReward[i]['item'] + "##z" + NormalReward[i]['item'] + "# " + qb + "개#k" + enter;
        }
    }
    if (ab == 0) {
        boxmsg += "아쉽게도 꽝이 나왔습니다!";
    } else {
        boxmsg += "총 " + ab + "개의 보상이 나왔습니다!";
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
            if (AdvancedReward[i]['item'] == 2431156 || AdvancedReward[i]['item'] == 2431157 || AdvancedReward[i]['item'] == 2633620 || AdvancedReward[i]['item'] == 1113997 ||
                AdvancedReward[i]['item'] == 2439545 || AdvancedReward[i]['item'] == 1122998) {
                for (ac = 0; ac < ab; ++ac) {
                    if (AdvancedReward[i]['item'] == 1122998) {
                        World.Broadcast.broadcastMessage(CField.getGameMessage(8, cm.getPlayer().getName() + " 님이 돌림판 이용권에서 레어 아이템을 획득했습니다."));
                    } else {
                        World.Broadcast.broadcastMessage(CField.getGameMessage(8, cm.getPlayer().getName() + " 님이 돌림판 이용권에서 " + cm.getItemName(AdvancedReward[i]['item']) + "을 획득했습니다."));
                    }
                    if (ab >= 2) {
                        return;
                    }
                }
            }
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
        chat += "#h0#님 당신의 운을 시험합니다!" + enter
        chat += "일반 이용권 또는 고급 이용권으로 뽑기에 도전해보세요!" + enter + enter
        chat += "#L0#일반 이용권 뽑기#l" + enter
        chat += "#L1#고급 이용권 뽑기#l"
        cm.sendSimple(chat);
    } else if (status == 1) {
        switch (selection) {
            case 0:
                if (!cm.haveItem(4001780, 1)) {
                    cm.sendOkS("#i4001780# #z4001780# 아이템이 없는것같은데?..", 0x24);
                    cm.dispose();
                    break;
                } else {
                    cm.gainItem(4001780, -1);
                    NormalUnboxing();
                    cm.sendOkS(boxmsg, 0x24);
                    cm.dispose();
                }
                break;
            case 1:
                if (!cm.haveItem(4319998, 1)) {
                    cm.sendOkS("#i4319998# #z4319998# 아이템이 없는것같은데?..", 0x24);
                    cm.dispose();
                    break;
                } else {
                    cm.gainItem(4319998, -1);
                    AdvancedUnboxing();
                    cm.sendOkS(boxmsg, 0x24);
                    cm.dispose();
                }
                break;
        }
    }
}
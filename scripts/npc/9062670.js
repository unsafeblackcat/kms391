importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var status = -1;
var enter = "\r\n";
var boxmsg = enter
var NormalReward = [
    {'item': 4031788, 'min': 1, 'max': 2000, 'chance': 1000}, // 10000 = 100%
    {'item': 4031788, 'min': 1, 'max': 2000, 'chance': 1000}, // 10000 = 100%
    {'item': 4031788, 'min': 1, 'max': 2000, 'chance': 1000}, // 10000 = 100%
    {'item': 4031788, 'min': 1, 'max': 2000, 'chance': 1000}, // 10000 = 100%
    {'item': 4031788, 'min': 1, 'max': 2000, 'chance': 1000}, // 10000 = 100%
    {'item': 4031788, 'min': 1, 'max': 200, 'chance': 1000}, // 10000 = 100%
    {'item': 4031788, 'min': 1, 'max': 2000, 'chance': 1000}, // 10000 = 100%
    {'item': 4031788, 'min': 1, 'max': 2000, 'chance': 1000}, // 10000 = 100%
    {'item': 4031788, 'min': 1, 'max': 20000, 'chance': 1000}, // 10000 = 100%
    {'item': 4031788, 'min': 1, 'max': 20000, 'chance': 1000}, // 10000 = 100%
]

var AdvancedReward = [
    {'item': 4031227, 'min': 1, 'max': 1, 'chance': 1}, // 10000 = 100%
    {'item': 5680935, 'min': 1, 'max': 1, 'chance': 500}, // 10000 = 100%
    {'item': 5680941, 'min': 1, 'max': 1, 'chance': 1}, // 10000 = 100%
	{'item': 5680942, 'min': 1, 'max': 1, 'chance': 10}, // 10000 = 100%
    {'item': 5681033, 'min': 1, 'max': 1, 'chance': 100}, // 10000 = 100%
    {'item': 4031013, 'min': 1, 'max': 50, 'chance': 100}, // 10000 = 100%
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
                        World.Broadcast.broadcastMessage(CField.getGameMessage(8, cm.getPlayer().getName() + " 님이 궁극의 비보에서 레어 아이템을 획득했습니다."));
                    } else {
                        World.Broadcast.broadcastMessage(CField.getGameMessage(8, cm.getPlayer().getName() + " 님이 궁극의 비보에서 " + cm.getItemName(AdvancedReward[i]['item']) + "을 획득했습니다."));
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
        var chat = ""
        chat += "#b#h0##k님 안녕하세요? 저는 #b럭키#k 입니다!\r\n그 얻기 힘들다는 #r궁극의 비보#k를 제작하셨나봐요!? 꿀꿀\r\n궁극의 비보 뽑기에선 게임 내 #b핫한 아이템#k들만 선별하여 #b공정한 확률#k로 들어있어요! 다만 어떤 아이템이 있는지는 비밀이랍니다^^ 얼른 운을 믿고 사용해 뽑기를 해보세요!" + enter
        chat += "#L0#[#i4033334#]#fs11# #r궁극의 비보#d 1개를 사용하여 뽑기를 진행합니다.#l"
      //  chat += "#L1#[ S ] 이용권 뽑기#l"
		//chat += "#L2#고급 이용권 뽑기#l"
        cm.sendSimple(chat);
    } else if (status == 1) {
        switch (selection) {
            case 0:
                if (!cm.haveItem(4033334, 1)) {
                    cm.sendOkS("#i4033334# #zi4033334# 아이템이 없는것같은데?..", 0x24);
                    cm.dispose();
                    break;
                } else {
                    cm.gainItem(4033334, -1);
                    NormalUnboxing();
                    cm.sendOkS(boxmsg, 0x24);
                    cm.dispose();
                }
                break;
            case 1:
                if (!cm.haveItem(4033334, 1)) {
                    cm.sendOkS("#i4033334# #zi4033334# 아이템이 없는것같은데?..", 0x24);
                    cm.dispose();
                    break;
                } else {
                    cm.gainItem(4033334, -1);
                    AdvancedUnboxing();
                    cm.sendOkS(boxmsg, 0x24);
                    cm.dispose();
                }
                break;
			case 2:
                if (!cm.haveItem(4319998, 1)) {
                    cm.sendOkS("#i4319998# #z4319998# 아이템이 없는것같은데?..", 0x24);
                    cm.dispose();
                break;
                } else {
                    cm.gainItem(3994013, -1);
                    AdvancedUnboxing();
                    cm.sendOkS(boxmsg, 0x24);
                    cm.dispose();
                }
                break;
        }
    }
}
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
            boxmsg += "#b#i" + NormalReward[i]['item'] + "##z" + NormalReward[i]['item'] + "# " + qb + "個#k" + enter;
        }
    }
    if (ab == 0) {
        boxmsg += "很遺憾落空了!";
    } else {
        boxmsg += "看 " + ab + "得到了物品!";
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
            boxmsg += "#b#i" + AdvancedReward[i]['item'] + "##z" + AdvancedReward[i]['item'] + "# " + qb + "個#k 中獎!" + enter;
            if (AdvancedReward[i]['item'] == 2431156 || AdvancedReward[i]['item'] == 2431157 || AdvancedReward[i]['item'] == 2633620 || AdvancedReward[i]['item'] == 1113997 ||
                AdvancedReward[i]['item'] == 2439545 || AdvancedReward[i]['item'] == 1122998) {
                for (ac = 0; ac < ab; ++ac) {
                    if (AdvancedReward[i]['item'] == 1122998) {
                        World.Broadcast.broadcastMessage(CField.getGameMessage(8, cm.getPlayer().getName() + " 您在終極vivo中獲得了稀有物品."));
                    } else {
                        World.Broadcast.broadcastMessage(CField.getGameMessage(8, cm.getPlayer().getName() + " 您在終極秘寶中 " + cm.getItemName(AdvancedReward[i]['item']) + "獲得了."));
                    }
                    if (ab >= 2) {
                        return;
                    }
                }
            }
        }
    }
    if (ab == 0) {
        boxmsg += "很遺憾落空了!";
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
        chat += "#fs15##b#kHai 我是幸運#k NPC!\r\n製作了 #r難獲得的#k終極秘寶 !?\r\n在最終的秘寶抽取中，只篩選#b熱門道具#k以#b公正的幾率#k出現在裡面！但有些物品是秘密^ ^！" + enter
        chat += "#L0#[#i4033334#]#fs15# #r使用1個終極秘寶#d 進行抽取#l"
      //  chat += "#L1#[ S ] 이용권 뽑기#l"
		//chat += "#L2#고급 이용권 뽑기#l"
        cm.sendSimple(chat);
    } else if (status == 1) {
        switch (selection) {
            case 0:
                if (!cm.haveItem(4033334, 1)) {
                    cm.sendOkS("#i4033334# #zi4033334# 好像沒有道具。?..", 0x24);
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
                    cm.sendOkS("#i4033334# #zi4033334# 好像沒有道具。?..", 0x24);
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
                    cm.sendOkS("#i4319998# #z4319998# 好像沒有道具。?..", 0x24);
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
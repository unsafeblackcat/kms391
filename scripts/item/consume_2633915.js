var enter = "\r\n";
var seld = -1;
var seld2 = -1;
var s1, s2;

var need = 2633915;

var key = "cygnus_1";

var items = [
    [1004808, 1102940, 1082695, 1053063, 1073158, 1152196],//神秘战士套装
    [1004810, 1102942, 1082697, 1053065, 1073160, 1152198],//神秘弓手套装
    [1004811, 1102943, 1082698, 1053066, 1073161, 1152199],//神秘飞侠套装
    [1004809, 1102941, 1082696, 1053064, 1073159, 1152197],//神秘法师套装
    [1004812, 1102944, 1082699, 1053067, 1073162, 1152200] //神秘海盗套装
]

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, sel) {
    if (mode == -1) {
        cm.dispose();
    }
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        getClass = getJob(cm.getPlayer().getJob());

        if (cm.getPlayer().getKeyValue(20190211, key) < 0)
            cm.getPlayer().setKeyValue(20190211, key, 0);

        if (!cm.canHold(items[0][0])) {
            cm.sendOk("#fs15#請清空1格以上的裝備視窗後重新使用!");
            cm.dispose();
            return;
        }
        if (cm.itemQuantity(need) < 1) {
            cm.sendOk("#fs15#箱子好像不够.");
            cm.dispose();
            return;
        }
        var msg = "#fs15##b#e幸運時間:#r" + cm.getPlayer().getKeyValue(20190211, key) + "%#k#n" + enter + enter;

        if (cm.getPlayer().getKeyValue(20190211, key) >= 100) {
            msg += "發動#r#e機會時間!#k#n" + enter;
            msg += "#b#e出現更好的附加選項#k#n的概率新增." + enter + enter;
        }

        msg += "#fs15#首先推薦當前職業可穿戴的裝備.\r\n請選擇要收到的防具.\r\n";

        for (i = 0; i < items[getClass].length; i++)
            msg += "#L" + i + "# #i" + items[getClass][i] + "# #b#z" + items[getClass][i] + "##k#l" + enter;

        msg += "\r\n#L100##b査看可用全部物品.#k#l\r\n";
        msg += "#L101##b取消使用#k#l";
        cm.sendSimple(msg);
    } else if (status == 1) {
        seld = sel;
        if (seld == 100) {
            var msg = "#fs15##b#e幸運時間:#r" + cm.getPlayer().getKeyValue(20190211, key) + "%#k#n" + enter + enter;
            if (cm.getPlayer().getKeyValue(20190211, key) >= 100) {
                msg += "發動#r#e機會時間!#k#n" + enter;
                msg += "#b#e出現更好的附加選項#k#n的概率新增." + enter + enter;
            }
            for (i2 = 0; i2 < items.length; i2++) {
                for (i = 0; i < items[i2].length; i++)
                    msg += "#L" + (((i2 + 1) * 100) + i) + "# #i" + items[i2][i] + "# #b#z" + items[i2][i] + "##k#l" + enter;
            }

            msg += "\r\n#L100##b査看可用全部物品.#k#l\r\n"
            msg += "#L101##b取消使用#k#l"
            cm.sendSimple(msg);
        } else if (seld == 101) {
            cm.dispose();
        } else if (seld < 100) {
            var msg = "#fs15#確定想要這個防具?\r\n"
            msg += "#i" + items[getClass][seld] + "# #z" + items[getClass][seld] + "#\r\n\r\n"
            msg += "#r#e※注意※#k#n\r\n"
            msg += "一旦兌換成功 將抹除#r1#k個箱子, 無法再次兌換成其他物品.\r\n\r\n"
            msg += "#L0##b是!沒錯.\r\n"
            msg += "#L1##b重新選擇.";
            cm.sendSimple(msg);
        }
    } else if (status == 2) {
        seld2 = sel;
        if (seld == 100) {
            s1 = Math.floor(seld2 / 100) - 1;
            s2 = seld2 % 100;
            var msg = "#fs15#確定想要這個裝備?\r\n"
            msg += "#i" + items[s1][s2] + "# #z" + items[s1][s2] + "#\r\n\r\n"
            msg += "#r#e※注意※#k#n\r\n"
            msg += "一旦將碎片兌換成物品 將抹除#r5#k個碎片, 無法再次兌換成其他物品.\r\n\r\n"
            msg += "#L0##b是!沒錯.\r\n"
            msg += "#L1##b重新選擇.";
            cm.sendSimple(msg);
        } else if (seld < 100) {
            if (seld2 == 1) {
                cm.sendOk("#fs15#重新使用碎片.");
                cm.dispose();
                return;
            }
            if (cm.getPlayer().getKeyValue(20190211, key) == 100) cm.getPlayer().setKeyValue(20190211, key, 0);

            cm.getPlayer().setKeyValue(20190211, key, cm.getPlayer().getKeyValue(20190211, key) + Packages.server.Randomizer.rand(7, 11));

            if (cm.getPlayer().getKeyValue(20190211, key) >= 100) cm.getPlayer().setKeyValue(20190211, key, 100);

            cm.gainItem(items[getClass][seld], 1);
            cm.gainItem(need, -1);
            cm.dispose();
        }
    } else if (status == 3) {
        if (seld == 100) {
            if (sel == 1) {
                cm.sendOk("#fs15#重新使用碎片.");
                cm.dispose();
                return;
            }
            if (cm.getPlayer().getKeyValue(20190211, key) == 100) cm.getPlayer().setKeyValue(20190211, key, 0);

            cm.getPlayer().setKeyValue(20190211, key, cm.getPlayer().getKeyValue(20190211, key) + Packages.server.Randomizer.rand(7, 11));

            if (cm.getPlayer().getKeyValue(20190211, key) >= 100) cm.getPlayer().setKeyValue(20190211, key, 100);

            cm.gainItem(items[s1][s2], 1);
            cm.gainItem(need, -1);
            cm.dispose();
        }
    }
}

function getJob(job) {
    var ret = -1;
    if (Packages.constants.GameConstants.isWarrior(job))
        ret = 0;
    else if (Packages.constants.GameConstants.isArcher(job))
        ret = 1;
    else if (Packages.constants.GameConstants.isThief(job))
        ret = 2;
    else if (Packages.constants.GameConstants.isMagician(job))
        ret = 3;
    else if (Packages.constants.GameConstants.isPirate(job))
        ret = 4;
    return ret;
}
var enter = "\r\n";
var seld = -1;
var seld2 = -1;
var s1, s2;

var need = 2633914;

var key = "cygnus_2";

var items = [
    [1302343,  //神秘冥界幽靈長劍
     1312203,  //神秘冥界幽靈之斧
     1322255,  //神秘冥界幽靈之錘
     1402259,  //神秘冥界幽靈雙手劍
     1412181,  //神秘冥界幽靈雙手斧
     1422189,  //神秘冥界幽靈雙手錘
     1432218,  //神秘冥界幽靈之槍
     1442274,  //神秘冥界幽靈之矛
     1582023,  //神秘冥界幽靈重拳槍
     1213018,  //神秘冥界幽靈調節器
     1232113], //神秘冥界幽靈魔劍

    [1452257,  // 神秘冥界幽靈之弓
     1462243,  // 神秘冥界幽靈十字弓
     1522143,  // 神秘冥界幽靈雙弩槍
     1214018, // 神秘冥界幽靈龍息射手
     1592020], // 神秘冥界幽靈古代之弓

    [1242121,  //神秘冥界幽靈能量劍
     1272017,  //神秘冥界幽靈鎖鏈
     1292018,  //神秘冥界幽靈仙扇
     1332279,  // 神秘冥界幽靈短刀
     1342104,  // 神秘冥界幽靈之刃
     1362140,  // 神秘冥界幽靈手杖
     1472265], // 神秘冥界幽靈鬥拳

    [1212120,  // 神秘冥界幽靈閃耀之杖
     1262039,  // 神秘冥界幽靈ESP限制器
     1282017,  // 神秘冥界幽靈魔法護腕
     1372228,  // 神秘冥界幽靈短杖
     1382265], // 神秘冥界幽靈長杖

    [1222113,  // 神秘冥界幽靈靈魂射手
     1482221,  // 神秘冥界幽靈之爪
     1492235,  // 神秘冥界幽靈火槍
     1532150]  // 神秘冥界幽靈加農砲
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
            var msg = "確定想要這個裝備?\r\n"
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
            var msg = "確定想要這個裝備?\r\n"
            msg += "#i" + items[s1][s2] + "# #z" + items[s1][s2] + "#\r\n\r\n"
            msg += "#r#e※注意※#k#n\r\n"
            msg += "一旦將碎片兌換成物品 將抹除#r15#k個碎片, 無法再次兌換成其他物品.\r\n\r\n"
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
            cm.gainItem(need, -15);
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
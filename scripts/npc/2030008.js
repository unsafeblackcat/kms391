var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        talk = "#fs20#有事直說，我不能講中文，不用說客氣話！\r\n\r\n";
        talk += "#L0# #b接受獻給殘暴炎魔的祭品.\r\n";
        talk += "#L1# #b我想回到氷峰雪域.";
        cm.sendSimpleS(talk, 0x24);
    } else if (status == 1) {
        st = selection;
        if (st == 0) {
            talk = "#fs20#需要獻給某個紮昆的祭品?\r\n\r\n";
            talk += "#L1# #b殘暴炎魔·紮昆#l";
            cm.sendSimpleS(talk, 0x24);
        } else {
            cm.sendNextS("#fs20#確定返回氷峰雪域？", 0x24);
        }
    } else if (status == 2) {
        if (st == 0) {
            if (cm.itemQuantity(4001017) == 0) {
                cm.sendNextS("#fs15#需要獻給紮昆的祭品…", 0x24);
            } else {
                cm.sendNextS("#fs20#已經有紮昆的祭品 #b#z4001017##k了… 用完了再說.", 0x24, 2030008);
                cm.dispose();
                return;
            }
        } else {
            cm.warp(211000000,0);
            cm.dispose();
            return;
        }
    } else if (status == 3) {
        cm.sendNextS("#fs20#召喚紮昆時需要的祭品 #b#z4001017##k現在我有", 0x24);
    } else if (status == 4) {
        cm.sendNextS("#fs20#把#i4001017#這個掉在紮昆的祭壇上就可以了。", 0x24);
        cm.gainItem(4001017, 1);
        cm.dispose();
        return;
    }
}
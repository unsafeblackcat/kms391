importPackage(java.io);
importPackage(java.awt);
importPackage(java.sql);
importPackage(java.util);
importPackage(java.lang);

var status = -1;

var enter = "\r\n";
var BossMaterial = [[4310154, 1]];

function ItemListt() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, sel) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var chat = enter
        chat += "#fn나눔고딕 Extrabold# VIP 코인으로 #r버프#k을 지급 받겠습니까?" + enter + enter
        chat += "#L0# #rVIP 버프#k#b를 강화하겠습니다..#k#l" + enter
        chat += "#L1# #b강화에 필요한 재료들을 알려주세요.#k#l" + enter
        cm.sendOk("#fs15#" + chat);

    } else if (status == 1) {
        if (sel == 0) {
            var chat = enter
            chat += "#fn나눔고딕 Extrabold##b[ 구매 가능한 버프 ]#k"
            chat += "--------------------------------------" + enter + enter
            chat += "#L2##s0008002# #bVIP 버프#k #fn나눔고딕 Extrabold#강화하기" + enter + enter
            cm.sendSimple("#fs15#" + chat);
        } else if (sel == 1) {
            var chat = enter
            chat += "#fn나눔고딕 Extrabold#버프를 구매하기 위한 #r재료#k은 아래와 같습니다." + enter + enter
            chat += "#fn나눔고딕 Extrabold##b[ 필요한 재료 ]#k"
            chat += "--------------------------------------" + enter + enter
            for (i = 0; i < BossMaterial.length; i++) {
                chat += "#i" + BossMaterial[i][0] + "# #b#z" + BossMaterial[i][0] + "##k " + cm.itemQuantity(BossMaterial[i][0]) + "개 / " + BossMaterial[i][1] + "개" + enter
            }
            cm.sendOk("#fs15#" + chat);
            cm.dispose();
        }

    } else if (status == 2) {
        if (cm.getPlayer().getKeyValue(888, "level") < 0) {
            cm.getPlayer().setKeyValue(888, "level", 0);
        }

        if (cm.getPlayer().getKeyValue(888, "level") > 9999) {
            cm.sendOk("#fs15#강화할 수 있는 최대치를 넘어섰습니다.");
            cm.dispose();
            return;
        }

        if (sel == 2) {
            if (!BossMaterialNeed()) {
                cm.sendOk("#fs15#재료가 없네.");
                cm.dispose();
                return;
            }
            cm.getPlayer().setKeyValue(888, "level", cm.getPlayer().getKeyValue(888, "level") + 1);
            var 크뎀 = Integer.parseInt(cm.getPlayer().getKeyValue(888, "level"));
            for (i = 0; i < BossMaterial.length; i++) {
                cm.gainItem(BossMaterial[i][0], -BossMaterial[i][1]);
            }
        }
        Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, "VIP버프를 획득하였습니다."));
        cm.sendOk("#fn나눔고딕 Extrabold##fs15##b[ VIP 버프 ]#k를 획득하였습니다!#k");
        cm.dispose();
    }
}

function getMeso(aa) {
    var msg = "";
    bb = aa;
    억 = (Math.floor(bb / 1000000000) > 0) ? Math.floor(aa / 1000000000) + "억 " : "";
    bb = aa % 1000000000;
    msg += 억;
    if (bb > 0) {
        만 = (Math.floor(bb / 10000) > 0) ? Math.floor(bb / 10000) + "만 " : "";
        msg += 만;
    }
    return msg;
}

function NomalMaterialNeed() {
    var ret = true;
    for (i = 0; i < NomalMaterial.length; i++) {
        if (!cm.haveItem(NomalMaterial[i][0], NomalMaterial[i][1]))
            ret = false;
    }
    return ret;
}

function BossMaterialNeed() {
    var ret = true;
    for (i = 0; i < BossMaterial.length; i++) {
        if (!cm.haveItem(BossMaterial[i][0], BossMaterial[i][1]))
            ret = false;
    }
    return ret;
}
var status = -1
var enter = "\r\n";
var ArmorMaterial = [[4033450, 1], [4031856, 10]]
var AccessoriesMaterial = [[4033449, 1], [4031856, 10]]
var moon = [1672075, 1012635, 1022281, 1132309, 1162084, 1162085, 1162086, 1162087, 1032317, 1122431, 1182286]
var star = [1113130, 1113131, 1113132, 1113133, 1190303, 1122151]
var space = [1003990, 1003991, 1003992, 1003993, 1003994, 1042300, 1042301, 1042302, 1042303, 1042304, 1062190, 1062191, 1062192, 1062193, 1062194]

function start() {
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
        chat += "MOON, STAR, SPACE 아이템을 강화 하고 싶으신가요?" + enter + enter
        chat += "악세사리 강화는 최대 20성까지 강화가 가능하며," + enter + enter
        chat += "방어구 강화는 최대 15성까지 강화가 가능하답니다." + enter + enter
        chat += "악세사리 및 방어구를 강화하고, 캐릭터를 스팩업 해보세요!" + enter + enter
        chat += "#L0##b악세사리 / 방어구 강화를 하겠습니다.#k#l" + enter
        chat += "#L1##b강화에 필요한 재료들을 알려주세요.#k#l" + enter
        cm.sendOk("#fs15#" + chat);
    } else if (status == 1) {
        if (sel == 0) {
            var chat = enter
            chat += "#h0#님이 소유하고 계신 아이템 목록 입니다." + enter + enter
            chat += "강화하고 싶으신 아이템을 선택해 주세요!" + enter + enter
            for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                if (cm.getInventory(1).getItem(i)) {
                    if (cm.getInventory(1).getItem(i).getOwner() != "XX") {
                        for (mi = 0; mi < moon.length; mi++) {
                            if (cm.getInventory(1).getItem(i).getItemId() == moon[mi]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "#" + (cm.getInventory(1).getItem(i).getOwner() != "" ? " [" + cm.getInventory(1).getItem(i).getOwner() + "]" : "") + "#k 아이템을 강화 하겠습니다.#l\r\n"
                            }
                        }
                        for (si = 0; si < star.length; si++) {
                            if (cm.getInventory(1).getItem(i).getItemId() == star[si]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "#" + (cm.getInventory(1).getItem(i).getOwner() != "" ? " [" + cm.getInventory(1).getItem(i).getOwner() + "]" : "") + "#k 아이템을 강화 하겠습니다.#l\r\n"
                            }
                        }
                    }
                    if (cm.getInventory(1).getItem(i).getOwner() != "XV") {
                        for (mi = 0; mi < space.length; mi++) {
                            if (cm.getInventory(1).getItem(i).getItemId() == space[mi]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "#" + (cm.getInventory(1).getItem(i).getOwner() != "" ? " [" + cm.getInventory(1).getItem(i).getOwner() + "]" : "") + "#k 아이템을 강화 하겠습니다.#l\r\n"
                            }
                        }
                    }
                }
            }
            cm.sendSimple("#fs15#" + chat);
        } else {
            var chat = enter
            chat += "악세사리 및 방어구 강화를 하기 위한 재료는 아래와 같습니다." + enter + enter
            chat += "한번 강화할때 마다 아래의 재료가 소모되는점 유의해주세요!" + enter + enter + enter
            chat += "#b[ 악 세 사 리 재 료 ]#k" + enter + enter
            for (i = 0; i < AccessoriesMaterial.length; i++) {
                chat += "#i" + AccessoriesMaterial[i][0] + "# #b#z" + AccessoriesMaterial[i][0] + "##k " + cm.itemQuantity(AccessoriesMaterial[i][0]) + "개 / " + AccessoriesMaterial[i][1] + "개" + enter
            }
            chat += "#i2630012# #b메소#k " + getMeso(cm.getMeso()) + " / 15억" + enter + enter + enter
            chat += "#b[ 방 어 구 재 료 ]#k" + enter + enter
            for (i = 0; i < ArmorMaterial.length; i++) {
                chat += "#i" + ArmorMaterial[i][0] + "# #b#z" + ArmorMaterial[i][0] + "##k " + cm.itemQuantity(ArmorMaterial[i][0]) + "개 / " + ArmorMaterial[i][1] + "개" + enter
            }
            chat += "#i2630012# #b메소#k " + getMeso(cm.getMeso()) + " / 20억" + enter + enter
            cm.sendOk("#fs15#" + chat);
            cm.dispose();
        }
    } else if (status == 2) {
        check = cm.getInventory(1).getItem(sel).getItemId();
        if (space.indexOf(check) != -1) {
            if (!ArmorMaterialNeed() || cm.getMeso() < 2000000000) {
                cm.sendOk("#fs15#재료가 부족한거 같은데?");
                cm.dispose();
                return;
            }
            cm.gainMeso(-2000000000);
            for (i = 0; i < ArmorMaterial.length; i++) {
                cm.gainItem(ArmorMaterial[i][0], -ArmorMaterial[i][1]);
            }
            vitem = cm.getInventory(1).getItem(sel);
            vitem.setOwner(ArmorStar());
            vitem.setStr(vitem.getStr() + 35);
            vitem.setDex(vitem.getDex() + 35);
            vitem.setInt(vitem.getInt() + 35);
            vitem.setLuk(vitem.getLuk() + 35);
            vitem.setWatk(vitem.getWatk() + 20);
            vitem.setMatk(vitem.getMatk() + 20);
            cm.getPlayer().forceReAddItem(vitem, Packages.client.inventory.MapleInventoryType.EQUIP);
            Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 " + cm.getItemName(vitem.getItemId()) + " " + vitem.getOwner() + " 강화에 성공하였습니다."));
            cm.sendOk("#fs15##b#i" + vitem.getItemId() + "# #b#z" + vitem.getItemId() + "##k 아이템을 #b[" + vitem.getOwner() + "]#k 강화에 성공하셨습니다!#k");
            cm.dispose();
        } else {
            if (!AccessoriesMaterialNeed() || cm.getMeso() < 1500000000) {
                cm.sendOk("#fs15#재료가 부족한거 같은데?");
                cm.dispose();
                return;
            }
            cm.gainMeso(-1500000000);
            for (i = 0; i < AccessoriesMaterial.length; i++) {
                cm.gainItem(AccessoriesMaterial[i][0], -AccessoriesMaterial[i][1]);
            }
            vitem = cm.getInventory(1).getItem(sel);
            vitem.setOwner(AccessoriesStar());
            vitem.setStr(vitem.getStr() + 30);
            vitem.setDex(vitem.getDex() + 30);
            vitem.setInt(vitem.getInt() + 30);
            vitem.setLuk(vitem.getLuk() + 30);
            vitem.setWatk(vitem.getWatk() + 15);
            vitem.setMatk(vitem.getMatk() + 15);
            cm.getPlayer().forceReAddItem(vitem, Packages.client.inventory.MapleInventoryType.EQUIP);
            Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 " + cm.getItemName(vitem.getItemId()) + " " + vitem.getOwner() + " 강화에 성공하였습니다."));
            cm.sendOk("#fs15##b#i" + vitem.getItemId() + "# #b#z" + vitem.getItemId() + "##k 아이템을 #b[" + vitem.getOwner() + "]#k 강화에 성공하셨습니다!#k");
            cm.dispose();
        }
    }
}

function getMeso(aa) {
    var msg = "";
    bb = aa;
    억 = (Math.floor(bb / 100000000) > 0) ? Math.floor(aa / 100000000) + "억 " : "";
    bb = aa % 100000000;
    msg += 억;
    if (bb > 0) {
        만 = (Math.floor(bb / 10000) > 0) ? Math.floor(bb / 10000) + "만 " : "";
        msg += 만;
    }
    return msg;
}

function ArmorMaterialNeed() {
    var ret = true;
    for (i = 0; i < ArmorMaterial.length; i++) {
        if (!cm.haveItem(ArmorMaterial[i][0], ArmorMaterial[i][1]))
            ret = false;
    }
    return ret;
}

function AccessoriesMaterialNeed() {
    var ret = true;
    for (i = 0; i < AccessoriesMaterial.length; i++) {
        if (!cm.haveItem(AccessoriesMaterial[i][0], AccessoriesMaterial[i][1]))
            ret = false;
    }
    return ret;
}

function ArmorStar() {
    switch (vitem.getOwner()) {
        case "I":
            return "II";
        case "II":
            return "III";
        case "III":
            return "IV";
        case "IV":
            return "V";
        case "V":
            return "VI";
        case "VI":
            return "VII";
        case "VII":
            return "VIII";
        case "VIII":
            return "IX";
        case "IX":
            return "X";
        case "X":
            return "XI";
        case "XI":
            return "XII";
        case "XII":
            return "XIII";
        case "XIII":
            return "XIV";
        case "XIV":
            return "XV";
        default:
            return "I";
    }
}

function AccessoriesStar() {
    switch (vitem.getOwner()) {
        case "I":
            return "II";
        case "II":
            return "III";
        case "III":
            return "IV";
        case "IV":
            return "V";
        case "V":
            return "VI";
        case "VI":
            return "VII";
        case "VII":
            return "VIII";
        case "VIII":
            return "IX";
        case "IX":
            return "X";
        case "X":
            return "XI";
        case "XI":
            return "XII";
        case "XII":
            return "XIII";
        case "XIII":
            return "XIV";
        case "XIV":
            return "XV";
        case "XV":
            return "XVI";
        case "XVI":
            return "XVII";
        case "XVII":
            return "XVIII";
        case "XVIII":
            return "XIX";
        case "XIX":
            return "XX";
        default:
            return "I";
    }
}
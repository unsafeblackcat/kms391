importPackage(Packages.server);

var status = -1
var enter = "\r\n";
var beyondMaterial = [[4310302, 100],[4310301, 100], [4310320, 30000]] //악세서리 재료
var needMeso = 10000000000; // 메소
var 확률 = 100;
var allstat = 100 // 올스텟
var atk = 100 // 공마
var star = [
    1662043, 
    1662044, 
    1662064, 
    1662065, 
    1662066, 

    1662067, 
    1662068, 
    1662069, 
    1662070, 
    1662071, 

    1662082, 
    1662083, 
    1662088, 
    1662089, 
    1662092, 

    1662093,
    1662111,
    1662114,
    1662115,
    1662116,

    1662125, 
    1662126, 
    1662127, 
    1662128, 
    1662130, 
    1662131, 
    1662139, 
    1662140, 
    1662141, 
    1662145, 
    1662146, 
    1662147, 
    1662148, 
    1662149, 
    1662150, 
    1662151,
    1662152,
    1662154,
    1662155,
    1662156,
    1662164,
    1662165,
    1662166,
    1662167,
    1662168,
    1662172,
    1662183,


]; // 강화 가능한템코드

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
        chat += "안녕하세요. #h0#님! 안드로이드의 한계를 돌파시키고 싶나요?" + enter + enter
        chat += "#b안드로이드아이템#k의 한계돌파가 가능해요. " + enter + enter
        chat += "한계 돌파시 #r올스텟 " + allstat + " 공마 " + atk + "#k가 증가해요." + enter + enter
        chat += "#L0##b장비를 강화 하겠습니다.#k#l" + enter
        chat += "#L1##b강화에 필요한 재료들을 알려주세요.#k#l" + enter
        cm.sendOk("#fs15#" + chat);
    } else if (status == 1) {
        if (sel == 0) {
            var chat = enter
            chat += "#h0#님이 소유하고 계신 아이템 목록이예요" + enter + enter
            chat += "한계 돌파하고 싶으신 아이템을 선택해 주세요!" + enter + enter
            for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                if (cm.getInventory(1).getItem(i)) {
                    if (cm.getInventory(1).getItem(i).getOwner() != "초월") {
                        for (si = 0; si < star.length; si++) {
                            if (cm.getInventory(1).getItem(i).getItemId() == star[si]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "#" + (cm.getInventory(1).getItem(i).getOwner() != "" ? " [" + cm.getInventory(1).getItem(i).getOwner() + "]" : "") + "#k 아이템을 강화 하겠습니다.#l\r\n"
                            }
                        }
                    }
                }
            }
            cm.sendSimple("#fs15#" + chat);
        } else {
            var chat = enter
            chat += "한계 돌파 재료는 아래와 같아요!" + enter + enter + enter
            chat += "#b[ 한계 돌파 재료 ]#k" + enter + enter
            for (i = 0; i < beyondMaterial.length; i++) {
                chat += "#i" + beyondMaterial[i][0] + "# #b#z" + beyondMaterial[i][0] + "##k " + cm.itemQuantity(beyondMaterial[i][0]) + "개 / " + beyondMaterial[i][1] + "개" + enter
            }
            chat += "#i2630012# #b메소#k " + getMeso(cm.getMeso()) + " / " + getMeso(needMeso) + enter + enter + enter
            cm.sendOk("#fs15#" + chat);
            cm.dispose();
        }
    } else if (status == 2) {
        check = cm.getInventory(1).getItem(sel).getItemId();
        if (!beyondMaterialNeed() || cm.getMeso() < 10000000000) {
            cm.sendOk("#fs15#재료가 부족한거 같은데?");
            cm.dispose();
            return;
        }
        while (needMeso > 2000000000)
        {
            cm.gainMeso(-2000000000);
            needMeso -= 2000000000
        }
        cm.gainMeso(-needMeso);
        for (i = 0; i < beyondMaterial.length; i++) {
            cm.gainItem(beyondMaterial[i][0], -beyondMaterial[i][1]);
        }
        vitem = cm.getInventory(1).getItem(sel);

        r1 = Randomizer.rand(0, 100);
        if(r1 > 확률) {
            Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 " + cm.getItemName(vitem.getItemId()) + " " + vitem.getOwner() + " 강화에 실패하였습니다."));
            cm.sendOk("#fs15##b#i" + vitem.getItemId() + "# #b#z" + vitem.getItemId() + "##k 아이템을 #b[초월]#k 강화에 실패하셨습니다!#k");
            cm.dispose();
        } else {
            vitem.setOwner("초월");
            vitem.setStr(vitem.getStr() + allstat);
            vitem.setDex(vitem.getDex() + allstat);
            vitem.setInt(vitem.getInt() + allstat);
            vitem.setLuk(vitem.getLuk() + allstat);
            vitem.setWatk(vitem.getWatk() + atk);
            vitem.setMatk(vitem.getMatk() + atk);
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

function beyondMaterialNeed() {
    var ret = true;
    for (i = 0; i < beyondMaterial.length; i++) {
        if (!cm.haveItem(beyondMaterial[i][0], beyondMaterial[i][1]))
            ret = false;
    }
    return ret;
}

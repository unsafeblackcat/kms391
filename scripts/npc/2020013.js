var status = -1
var enter = "\r\n";
var dpoint = 0;
var et = [1102498, 1082490, 1072695, 1152114];
var 스텟 = 5;

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
        chat += "후원 강화는 무기와 보조무기에만 가능합니다." + enter + enter
        chat += "강화는 최대 50강까지 가능하며,." + enter + enter
        chat += "강화할 때 마다 올스탯 " + 스텟 + ", 공격력,마력이 " + 스텟 + "가 증가합니다." + enter + enter
        chat += "#L0##b장비를 강화 하겠습니다.#k#l" + enter
        chat += "#L1##b강화에 필요한 비용을 알려주세요.#k#l" + enter
        cm.sendOk("#fs15#" + chat);
    } else if (status == 1) {
        if (sel == 0) {
            var chat = enter
            chat += "#h0#님이 소유하고 계신 아이템 목록 입니다." + enter + enter
            chat += "강화하고 싶으신 아이템을 선택해 주세요!" + enter + enter
            for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                if (cm.getInventory(1).getItem(i)) {
                    if (cm.getInventory(1).getItem(i).getOwner() != "+50강") {
                        for (var j = 0; j < et.length; j++) {
                            if (cm.getInventory(1).getItem(i).getItemId() == et[j]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "#" + (cm.getInventory(1).getItem(i).getOwner() != "" ? " [" + cm.getInventory(1).getItem(i).getOwner() + "]" : "") + "#k 아이템을 강화 하겠습니다.#l\r\n"
                            }
                        }
                        if (cm.getInventory(1).getItem(i).getItemId() / 100000 >= 12 && cm.getInventory(1).getItem(i).getItemId() / 100000 < 16) {
                            chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "#" + (cm.getInventory(1).getItem(i).getOwner() != "" ? " [" + cm.getInventory(1).getItem(i).getOwner() + "]" : "") + "#k 아이템을 강화 하겠습니다.#l\r\n"
                        }
                    }
                }
            }
            cm.sendSimple("#fs15#" + chat);
        } else {
            var chat = enter
            chat += "장비를 강화 하기 위한 포인트는 아래와 같습니다." + enter + enter
            chat += "한번 강화할때 마다 아래의 포인트가 소모되는점 유의해주세요!" + enter + enter + enter
            chat += "#b[ 후원 포인트 강화 재료 ]#k" + enter + enter
            for (i = 1; i <= 50; i++) {
                chat += i + "강 : #b후원 포인트#k : " + (4000 + 400 * i - 400) + enter + enter
            }
            cm.sendOk("#fs15#" + chat);
            cm.dispose();
        }
    } else if (status == 2) {
        check = cm.getInventory(1).getItem(sel).getItemId();
        vitem = cm.getInventory(1).getItem(sel);
        for (i = 1; i <= 50; i++) {
            if (vitem.getOwner() == "+" + i + "강") {
                dpoint = 4000 + (400 * i);
                break;
            } else {
                dpoint = 4000;
            }
        }

        for (var j = 0; j < et.length; j++) {
            if (check == et[j]) {
                dpoint /= 2;
            }
        }

        if (cm.getPlayer().getDonationPoint() < dpoint) {
            cm.sendOk("#fs15#포인트가 부족한거 같은데?\r\n\r\n" + cm.getPlayer().getDonationPoint() + " / " + dpoint);
            cm.dispose();
            return;
        }
        vitem.setOwner(SetStar());
        vitem.setStr(vitem.getStr() + 스텟);
        vitem.setDex(vitem.getDex() + 스텟);
        vitem.setInt(vitem.getInt() + 스텟);
        vitem.setLuk(vitem.getLuk() + 스텟);
        vitem.setWatk(vitem.getWatk() + 스텟);
        vitem.setMatk(vitem.getMatk() + 스텟);
        cm.getPlayer().forceReAddItem(vitem, Packages.client.inventory.MapleInventoryType.EQUIP);
        cm.sendOk("#fs15##b#i" + vitem.getItemId() + "# #b#z" + vitem.getItemId() + "##k 아이템을 #b[" + vitem.getOwner() + "]#k 강화에 성공하셨습니다!#k");
        Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 " + cm.getItemName(vitem.getItemId()) + " " + vitem.getOwner() + " 강화에 성공하였습니다."));
        cm.getPlayer().setDonationPoint(cm.getPlayer().getDonationPoint() - dpoint);
        cm.getPlayer().dropMessage(6, dpoint);
        if(vitem.getOwner() == "+5강" || vitem.getOwner() == "+10강" || vitem.getOwner() == "+15강" || vitem.getOwner() == "+20강" || vitem.getOwner() == "+25강" || vitem.getOwner() == "+30강" || vitem.getOwner() == "+35강" || vitem.getOwner() == "+40강" || vitem.getOwner() == "+45강" || vitem.getOwner() == "+50강"){
            Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 " + cm.getItemName(vitem.getItemId()) + " " + vitem.getOwner() + " 강화에 성공하였습니다."));    
        }


        cm.dispose();
    }
}

function SetStar() {
    switch (vitem.getOwner()) {
        case "+1강":
            return "+2강";
        case "+2강":
            return "+3강";
        case "+3강":
            return "+4강";
        case "+4강":
            return "+5강";
        case "+5강":
            return "+6강";
        case "+6강":
            return "+7강";
        case "+7강":
            return "+8강";
        case "+8강":
            return "+9강";
        case "+9강":
            return "+10강";
        case "+10강":
            return "+11강";
        case "+11강":
            return "+12강";
        case "+12강":
            return "+13강";
        case "+13강":
            return "+14강";
        case "+14강":
            return "+15강";
        case "+15강":
            return "+16강";
        case "+16강":
            return "+17강";
        case "+17강":
            return "+18강";
        case "+18강":
            return "+19강";
        case "+19강":
            return "+20강";
        case "+20강":
            return "+21강";
        case "+21강":
            return "+22강";
        case "+22강":
            return "+23강";
        case "+23강":
            return "+24강";
        case "+24강":
            return "+25강";
        case "+25강":
            return "+26강";
        case "+26강":
            return "+27강";
        case "+27강":
            return "+28강";
        case "+28강":
            return "+29강";
        case "+29강":
            return "+30강";
        case "+30강":
            return "+31강";
        case "+31강":
            return "+32강";
        case "+32강":
            return "+33강";
        case "+33강":
            return "+34강";
        case "+34강":
            return "+35강";
        case "+35강":
            return "+36강";
        case "+36강":
            return "+37강";
        case "+37강":
            return "+38강";
        case "+38강":
            return "+39강";
        case "+39강":
            return "+40강";
        case "+40강":
            return "+41강";
        case "+41강":
            return "+42강";
        case "+42강":
            return "+43강";
        case "+43강":
            return "+44강";
        case "+44강":
            return "+45강";
        case "+45강":
            return "+46강";
        case "+46강":
            return "+47강";
        case "+47강":
            return "+48강";
        case "+48강":
            return "+49강";
        case "+49강":
            return "+50강";
        default:
            return "+1강";
    }
}
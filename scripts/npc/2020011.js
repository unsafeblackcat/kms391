var status = -1 
var enter = "\r\n";
var hpoint = 0;
var et = [1102498, 1082490, 1072695, 1152114];
var 屬性加成 = 3;
 
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
        chat += "宣傳強化僅適用於武器和輔助武器。" + enter + enter 
        chat += "強化最高可達50星，" + enter + enter 
        chat += "每次強化可增加" + 屬性加成 + "全屬性、" + 屬性加成 + "攻擊力和魔力。" + enter + enter 
        chat += "#L0##b進行裝備強化#k#l" + enter 
        chat += "#L1##b查詢強化所需費用#k#l" + enter 
        cm.sendOk("#fs15#"  + chat);
    } else if (status == 1) {
        if (sel == 0) {
            var chat = enter 
            chat += "#h0#您目前持有的裝備清單：" + enter + enter 
            chat += "請選擇要強化的裝備！" + enter + enter 
            for (i = 0; i < cm.getInventory(1).getSlotLimit();  i++) {
                if (cm.getInventory(1).getItem(i))  {
                    if (cm.getInventory(1).getItem(i).getOwner()  != "+50星") {
                        for (var j = 0; j < et.length;  j++) {
                            if (cm.getInventory(1).getItem(i).getItemId()  == et[j]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId()  + "# #b#z" + cm.getInventory(1).getItem(i).getItemId()  + "#" + (cm.getInventory(1).getItem(i).getOwner()  != "" ? " [" + cm.getInventory(1).getItem(i).getOwner()  + "]" : "") + "#k 進行強化#l\r\n"
                            }
                        }
                        if (cm.getInventory(1).getItem(i).getItemId()  / 100000 >= 12 && cm.getInventory(1).getItem(i).getItemId()  / 100000 < 16) {
                            chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId()  + "# #b#z" + cm.getInventory(1).getItem(i).getItemId()  + "#" + (cm.getInventory(1).getItem(i).getOwner()  != "" ? " [" + cm.getInventory(1).getItem(i).getOwner()  + "]" : "") + "#k 進行強化#l\r\n"
                        }
                    }
                }
            }
            cm.sendSimple("#fs15#"  + chat);
        } else {
            var chat = enter 
            chat += "裝備強化所需點數如下：" + enter + enter 
            chat += "請注意每次強化都會消耗以下點數！" + enter + enter + enter 
            chat += "#b[ 宣傳點數強化需求 ]#k" + enter + enter 
            for (i = 1; i <= 50; i++) {
                chat += i + "星 : #b宣傳點數#k : " + (40000 + 4000 * i - 4000) + enter + enter 
            }
            cm.sendOk("#fs15#"  + chat);
            cm.dispose(); 
        }
    } else if (status == 2) {
        check = cm.getInventory(1).getItem(sel).getItemId(); 
        vitem = cm.getInventory(1).getItem(sel); 
        for (i = 1; i <= 50; i++) {
            if (vitem.getOwner()  == "+" + i + "星") {
                hpoint = 40000 + (4000 * i);
                break;
            } else {
                hpoint = 40000;
            }
        }
 
        for (var j = 0; j < et.length;  j++) {
            if (check == et[j]) {
                hpoint /= 2;
            }
        }
 
        if (cm.getPlayer().getDonationPoint()  < hpoint) {
            cm.sendOk("#fs15# 點數不足！\r\n\r\n" + cm.getPlayer().getDonationPoint()  + " / " + hpoint);
            cm.dispose(); 
            return;
        }
        vitem.setOwner(SetStar()); 
        vitem.setStr(vitem.getStr()  + 屬性加成);
        vitem.setDex(vitem.getDex()  + 屬性加成);
        vitem.setInt(vitem.getInt()  + 屬性加成);
        vitem.setLuk(vitem.getLuk()  + 屬性加成);
        vitem.setWatk(vitem.getWatk()  + 屬性加成);
        vitem.setMatk(vitem.getMatk()  + 屬性加成);
        cm.getPlayer().forceReAddItem(vitem,  Packages.client.inventory.MapleInventoryType.EQUIP); 
        cm.sendOk("#fs15##b#i"  + vitem.getItemId()  + "# #b#z" + vitem.getItemId()  + "##k 已成功強化至#b[" + vitem.getOwner()  + "]#k！");
        Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8,  cm.getPlayer().getName()  + "成功將" + cm.getItemName(vitem.getItemId())  + "強化至" + vitem.getOwner()  + "！"));
        cm.getPlayer().setHPoint(cm.getPlayer().getHPoint()  - hpoint);
        cm.getPlayer().dropMessage(6,  hpoint);
        if(vitem.getOwner()  == "+5星" || vitem.getOwner()  == "+10星" || vitem.getOwner()  == "+15星" || vitem.getOwner()  == "+20星" || vitem.getOwner()  == "+25星" || vitem.getOwner()  == "+30星" || vitem.getOwner()  == "+35星" || vitem.getOwner()  == "+40星" || vitem.getOwner()  == "+45星" || vitem.getOwner()  == "+50星"){
            Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8,  cm.getPlayer().getName()  + "成功將" + cm.getItemName(vitem.getItemId())  + "強化至" + vitem.getOwner()  + "！"));    
        }
 
        cm.dispose(); 
    }
}
 
function SetStar() {
    switch (vitem.getOwner())  {
        case "+1星":
            return "+2星";
        case "+2星":
            return "+3星";
        case "+3星":
            return "+4星";
        case "+4星":
            return "+5星";
        case "+5星":
            return "+6星";
        case "+6星":
            return "+7星";
        case "+7星":
            return "+8星";
        case "+8星":
            return "+9星";
        case "+9星":
            return "+10星";
        case "+10星":
            return "+11星";
        case "+11星":
            return "+12星";
        case "+12星":
            return "+13星";
        case "+13星":
            return "+14星";
        case "+14星":
            return "+15星";
        case "+15星":
            return "+16星";
        case "+16星":
            return "+17星";
        case "+17星":
            return "+18星";
        case "+18星":
            return "+19星";
        case "+19星":
            return "+20星";
        case "+20星":
            return "+21星";
        case "+21星":
            return "+22星";
        case "+22星":
            return "+23星";
        case "+23星":
            return "+24星";
        case "+24星":
            return "+25星";
        case "+25星":
            return "+26星";
        case "+26星":
            return "+27星";
        case "+27星":
            return "+28星";
        case "+28星":
            return "+29星";
        case "+29星":
            return "+30星";
        case "+30星":
            return "+31星";
        case "+31星":
            return "+32星";
        case "+32星":
            return "+33星";
        case "+33星":
            return "+34星";
        case "+34星":
            return "+35星";
        case "+35星":
            return "+36星";
        case "+36星":
            return "+37星";
        case "+37星":
            return "+38星";
        case "+38星":
            return "+39星";
        case "+39星":
            return "+40星";
        case "+40星":
            return "+41星";
        case "+41星":
            return "+42星";
        case "+42星":
            return "+43星";
        case "+43星":
            return "+44星";
        case "+44星":
            return "+45星";
        case "+45星":
            return "+46星";
        case "+46星":
            return "+47星";
        case "+47星":
            return "+48星";
        case "+48星":
            return "+49星";
        case "+49星":
            return "+50星";
        default:
            return "+1星";
    }
}
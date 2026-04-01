var status = -1 
var enter = "\r\n";
var ArmorMaterial = [[4319999, 3000]] //防具材料 
var 屬性加成 = 3;
var 飾品 = [1] //飾品 
var 星力 = [1];
var 特殊裝備 = [
    1009984, // 파프 뚝
    1009985,
    1009986,
    1009987,
    1009988,

    1042985, // 파프 상
    1042986,
    1042987,
    1042988,
    1042989,

    1062985, // 파프 하
    1062986,
    1062987,
    1062988,
    1062989,

    1109940, // 앜 망
    1109941,
    1109942,
    1109943,
    1109944,

    1082995, // 앜글
    1082996,
    1082997,
    1082998,
    1082999,

    1079958, // 앜 슈
    1079959,
    1079960,
    1079961,
    1079962,

    1152995, // 앜 숄
    1152996,
    1152997,
    1152998,
    1152999,

    1005980, // 에테 뚝
    1005981,
    1005982,
    1005983,
    1005984,

    1009999,
    1152994,    // e에테
    1109945,
    1042994,
    1062994,
    1079999,
    1082994,

    1009994, // h에테 뚝
    1009995,
    1009996,
    1009997,
    1009998,

    1042433, // 에테 상
    1042434,
    1042435,
    1042436,
    1042437,

    1042995, // h에테 상
    1042996,
    1042997,
    1042998,
    1042999,

    1062285, // 에테 하
    1062286,
    1062287,
    1062288,
    1062289,

    1062995, // h에테 하
    1062996,
    1062997,
    1062998,
    1062999,

    1152212, // 에테 견
    1152213,
    1152214,
    1152215,
    1152216,

    1153000, // h에테 견
    1153001,
    1153002,
    1153003,
    1153004,

    1113999, // 악세
    1012999,
    1022999,
    1032999,
    1122999,
    1132999,
    1162999,
    1189999,
    1113994,
    1113995,
    1113996,
    1113997,
    1113998,
    1122998,
];
 
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
        chat += "防具強化僅適用於[FANCY]帕普尼爾、[FANCY]神秘冥界" + enter + enter 
        chat += "以太之核、[FANCY]以太之核裝備。" + enter + enter 
        chat += "強化最高可達50星，每次強化可增加" + 屬性加成 + "全屬性" + enter + enter 
        chat += "和" + 屬性加成 + "攻擊力。" + enter + enter 
        chat += "#L0##b進行裝備強化#k#l" + enter 
        chat += "#L1##b查詢強化所需材料#k#l" + enter 
        cm.sendOk("#fs15#"  + chat);
    } else if (status == 1) {
        if (sel == 0) {
            var chat = enter 
            chat += "#h0#您目前持有的裝備清單：" + enter + enter 
            chat += "請選擇要強化的裝備！" + enter + enter 
            for (i = 0; i < cm.getInventory(1).getSlotLimit();  i++) {
                if (cm.getInventory(1).getItem(i))  {
                    if (cm.getInventory(1).getItem(i).getOwner()  != "+50星") {
                        for (mi = 0; mi < 飾品.length; mi++) {
                            if (cm.getInventory(1).getItem(i).getItemId()  == 飾品[mi]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId()  + "# #b#z" + cm.getInventory(1).getItem(i).getItemId()  + "#" + (cm.getInventory(1).getItem(i).getOwner()  != "" ? " [" + cm.getInventory(1).getItem(i).getOwner()  + "]" : "") + "#k 進行強化#l\r\n"
                            }
                        }
                        for (si = 0; si < 星力.length; si++) {
                            if (cm.getInventory(1).getItem(i).getItemId()  == 星力[si]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId()  + "# #b#z" + cm.getInventory(1).getItem(i).getItemId()  + "#" + (cm.getInventory(1).getItem(i).getOwner()  != "" ? " [" + cm.getInventory(1).getItem(i).getOwner()  + "]" : "") + "#k 進行強化#l\r\n"
                            }
                        }
                    }
                    if (cm.getInventory(1).getItem(i).getOwner()  != "+50星") {
                        for (mi = 0; mi < 特殊裝備.length; mi++) {
                            if (cm.getInventory(1).getItem(i).getItemId()  == 特殊裝備[mi]) {
                                chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId()  + "# #b#z" + cm.getInventory(1).getItem(i).getItemId()  + "#" + (cm.getInventory(1).getItem(i).getOwner()  != "" ? " [" + cm.getInventory(1).getItem(i).getOwner()  + "]" : "") + "#k 進行強化#l\r\n"
                            }
                        }
                    }
                }
            }
            cm.sendSimple("#fs15#"  + chat);
        } else {
            var chat = enter 
            chat += "裝備強化所需材料如下：" + enter + enter 
            chat += "請注意每次強化都會消耗以下材料！" + enter + enter + enter 
            chat += "#b[ 防具強化材料 ]#k" + enter + enter 
            for (i = 0; i < ArmorMaterial.length;  i++) {
                chat += "#i" + ArmorMaterial[i][0] + "# #b#z" + ArmorMaterial[i][0] + "##k " + cm.itemQuantity(ArmorMaterial[i][0])  + "個 / " + ArmorMaterial[i][1] + "個" + enter 
            }
            chat += "#i2630012# #b楓幣#k " + getMeso(cm.getMeso())  + " / 30億" + enter + enter 
            cm.sendOk("#fs15#"  + chat);
            cm.dispose(); 
        }
    } else if (status == 2) {
        check = cm.getInventory(1).getItem(sel).getItemId(); 
        if (特殊裝備.indexOf(check) != -1) {
            if (!ArmorMaterialNeed() || cm.getMeso()  < 3000000000) {
                cm.sendOk("#fs15# 材料不足！");
                cm.dispose(); 
                return;
            }
            cm.gainMeso(-3000000000); 
            for (i = 0; i < ArmorMaterial.length;  i++) {
                cm.gainItem(ArmorMaterial[i][0],  -ArmorMaterial[i][1]);
            }
            vitem = cm.getInventory(1).getItem(sel); 
            vitem.setOwner(ArmorStar()); 
            vitem.setStr(vitem.getStr()  + 屬性加成);
            vitem.setDex(vitem.getDex()  + 屬性加成);
            vitem.setInt(vitem.getInt()  + 屬性加成);
            vitem.setLuk(vitem.getLuk()  + 屬性加成);
            vitem.setWatk(vitem.getWatk()  + 屬性加成);
            vitem.setMatk(vitem.getMatk()  + 屬性加成);
            cm.getPlayer().forceReAddItem(vitem,  Packages.client.inventory.MapleInventoryType.EQUIP); 
            if (vitem.getOwner()  == "+5星" || vitem.getOwner()  == "+10星" || vitem.getOwner()  == "+15星" || vitem.getOwner()  == "+20星" || vitem.getOwner()  == "+25星" || vitem.getOwner()  == "+30星" || vitem.getOwner()  == "+35星" || vitem.getOwner()  == "+40星" || vitem.getOwner()  == "+45星" || vitem.getOwner()  == "+50星") {
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8,  cm.getPlayer().getName()  + "成功將" + cm.getItemName(vitem.getItemId())  + "強化至" + vitem.getOwner()  + "！"));
            }
            cm.sendOk("#fs15##b#i"  + vitem.getItemId()  + "# #b#z" + vitem.getItemId()  + "##k 已成功強化至#b[" + vitem.getOwner()  + "]#k！");
            cm.dispose(); 
        }
    }
}
 
function getMeso(aa) {
    var msg = "";
    bb = aa;
    億 = (Math.floor(bb  / 100000000) > 0) ? Math.floor(aa  / 100000000) + "億 " : "";
    bb = aa % 100000000;
    msg += 億;
    if (bb > 0) {
        萬 = (Math.floor(bb  / 10000) > 0) ? Math.floor(bb  / 10000) + "萬 " : "";
        msg += 萬;
    }
    return msg;
}
 
function ArmorMaterialNeed() {
    var ret = true;
    for (i = 0; i < ArmorMaterial.length;  i++) {
        if (!cm.haveItem(ArmorMaterial[i][0],  ArmorMaterial[i][1]))
            ret = false;
    }
    return ret;
}
 
function ArmorStar() {
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
 
function AccessoriesStar() {
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
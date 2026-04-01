var enter = "\r\n";
var seld = -1;
var seld2 = -1;

var 뱃지후포 = 100000;
var 포켓후포 = 100000;

var need = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, sel) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        var msg = "#fs15#你好 #b#h 0##k先生! 您知道可以通過積分給無法賦予潜在能力的物品賦予潜在能力嗎？ #fs15##b" + enter;
        msg += "#b#L1#賦予徽章道具潜在能力 #Cgray#(" + Comma(뱃지후포) + "P 需要)" + enter;
        msg += "#b#L2#賦予口袋道具潜在能力 #Cgray#(" + Comma(포켓후포) + "P 需要)" + enter;
        cm.sendSimpleS(msg,0x26,9010061);
    } else if (status == 1) {
        seld = sel;
        need = (seld == 1) ? 뱃지후포 : 포켓후포;
        seldi = seld == 1 ? "徽章" : "口袋";
        ic = seld == 1 ? 118 : 116;

        if (cm.getPlayer().getDonationPoint() < need) {
            cm.sendOk("#fs15#積分不足.");
            cm.dispose();
            return;
        }

        var msg = "#fs15#你好。 #b#h 0##k님! " + seldi + " 你選擇了!" + enter;
        msg += "請 #b選擇想要的#k을 物品. #r（版本不開放）#k" + enter + enter;
        msg += "#fs15#當前 #b#h 0##k您的 #d贊助積分#k는 #r" + Comma(cm.getPlayer().getDonationPoint()) + "P#k 是.#fs15#" + enter + enter;
        switch (seld) {
            case 1:
                for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                    item = cm.getInventory(1).getItem(i);
                    if (item == null)
                        continue;
                    if (Math.floor(item.getItemId() / 10000) == ic)
                        msg += "#L" + i + "# #i" + item.getItemId() + "# #b#z" + item.getItemId() + "#\r\n";
                }
                break;
            case 2:
                for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                    item = cm.getInventory(1).getItem(i);
                    if (item == null)
                        continue;
                    if (Math.floor(item.getItemId() / 10000) == ic)
                        msg += "#L" + i + "# #i" + item.getItemId() + "# #b#z" + item.getItemId() + "#\r\n";
                }
                break;
        }
        cm.sendSimpleS(msg,0x86);
    } else if (status == 2) {
        if (cm.getPlayer().getDonationPoint() < need) {
            cm.sendOk("#fs15#積分不足.");
            cm.dispose();
            return;
        }
        if (seld >= 1) {
            item = cm.getInventory(1).getItem(sel);
        }
        if (item == null) {
            cm.sendOk("#fs15#沒有選擇的物品，請重試.");
            cm.dispose();
            return;
        }
        item.setState(17);
        item.setLines(3);
        item.setPotential1(10041);
        item.setPotential2(10042);
        item.setPotential3(10043);
        cm.getPlayer().gainDonationPoint(-need);
        cm.sendOkS("#fs15##b物品資訊#k\r\n\r\n"
                + "#Cgray##fs15#――――――――――――――――――――――――――――――――――――――――#k"
                + "#r選擇項目 : #i" + item.getItemId() + "# #z" + item.getItemId() + "#\r\n\r\n"
                + "#Cgray##fs15#――――――――――――――――――――――――――――――――――――――――#k"
                + "#k賦予物品潜在能力。 謝謝您的使用.",0x86);
        cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(1));
        cm.dispose();
    }
}

function Comma(i)
{
    var reg = /(^[+-]?\d+)(\d{3})/;
    i += '';
    while (reg.test(i))
        i = i.replace(reg, '$1' + ',' + '$2');
    return i;
}
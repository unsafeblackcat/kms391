var status = -1;
var ItemId = 2635156;
var itemQuantity = 1; //需要數量
var itemQuantity = 1; //需要數量
var prItemId = 4310012; // 持有高級道具時可能會翻倍
var successRate = 100; //成功機率 (%)
var minUpStat = 0; 
var maxUpStat = 0;

var select;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        var say = "#h 玩家您好，這裡是固定傷害商店。\r\n";
        say += "#b#L0#捐獻箱子來提升固定傷害#l#k";
        cm.sendSimple(say);
    } else if (status == 1) {
        select = selection;
        var text = "您想透過哪種方式進行？\r\n\r\n";
        text += "#L0##d#z" + ItemId + "# " + itemQuantity + "個來進行#l\r\n";
        cm.sendSimple(text);
    } else if (status == 2) {
        st = selection;
        reqitemid = st == 0 ? ItemId : prItemId;
        reqs = st == 0 ? Math.floor(ItemId/300) : prItemId;
        cm.sendGetNumber("您想進行幾次？", 1, 1, reqs);
    } else if (status == 3) {
        if (cm.itemQuantity(reqitemid) < itemQuantity * selection) {
            cm.sendOk("道具數量不足。");
            cm.dispose();
            return;
        }
        var count = 0;
        var rnd = Packages.server.Randomizer.rand(minUpStat, maxUpStat);

        for (i = 0; i < selection; i++) {
            rd = Math.floor(Math.random() * 10000);
            if (st == 0 && rd < successRate || st == 1) {
                if (select == 0) {
                } else {
                }
                count++;
            }
        }
        reqqty = st == 0 ? 1 : 1;
        cm.getPlayer().addACDamage(reqqty * selection * 10000);
        cm.gainItem(reqitemid, -(reqqty * selection));
        cm.sendOk("#b#h 玩家的 #b固定傷害#k#b" + cm.getPlayer().getACDamage() + "#k已提升！");
        cm.playerMessage("目前總固定傷害為 " + cm.getPlayer().getACDamage() + "！");
        cm.dispose();
    }
}
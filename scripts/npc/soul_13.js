importPackage(Packages.server);
importPackage(java.lang);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var enter = "\r\n";
var seld = -1;

var need = 2431711, qty = 10;

var basesoul = 2591179; // 第一個靈魂

var special = 2591088; // 偉大的靈魂
var chance = 100; // 0.1%

var v1 = false;

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
        if (!cm.haveItem(need, qty)) {
            cm.sendOk("#b#i"+need+"##z"+need+"##k 收集 #b"+qty+"個#k 就可以換成靈魂喔..");
            cm.dispose();
            return;
        }

        asd = Randomizer.rand(1, 1000);
        if (asd <= chance)
            v1 = true;
        cm.sendYesNo("要消耗 #b#i"+need+"##z"+need+"##k "+qty+" 個，將其換成靈魂道具嗎？");
    } else if (status == 1) {
        if (!cm.haveItem(need, qty)) {
            cm.sendOk("#b#i"+need+"##z"+need+"##k 收集 #b"+qty+"個#k 就可以換成靈魂喔..");
            cm.dispose();
            return;
        }
        cm.gainItem(need, -qty);
        if (v1) {
            cm.gainItem(special, 1);
            cm.sendOk("喔喔.. 出現了 #b#i"+special+"##z"+special+"##k！這可是比其他靈魂更強力的道具，要好好珍惜喔");
            World.Broadcast.broadcastMessage(CWvsContext.serverNotice(11, cm.getPlayer().getName()+" 先生/女士獲得了 "+cm.getItemName(special)+"！"));
        } else {
            asd2 = Randomizer.rand(0,7);
            asd3 = basesoul + asd2;

            cm.gainItem(asd3, 1);
            cm.sendOk("哦呵~！出現了 #b#i"+asd3+"##z"+asd3+"##k 啊！好好利用吧。嘿嘿嘿...");
        }
        cm.dispose();
    }
}
var enter = "\r\n";
var seld = -1;

var need = [
    {'itemid': 4033328, 'qty': 100},
    {'itemid': 4033329, 'qty': 100},
    {'itemid': 4033330, 'qty': 100},
    {'itemid': 4033331, 'qty': 100},
    {'itemid': 4033333, 'qty': 100}
]
var tocoin = 4033334, toqty = 1;

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
        var msg = "#fs15##b" + cm.getPlayer().getName() + "#k歡迎. #eFancy #n我是負責活動的NPC..\r\n" + enter;

        for (i = 0; i < need.length; i++) {
            if (i != need.length - 1)
                msg += "#i" + need[i]['itemid'] + "##b#z" + need[i]['itemid'] + "##k " + need[i]['qty'] + "個#k" + enter;
            else
                msg += "#i" + need[i]['itemid'] + "##b#z" + need[i]['itemid'] + "##k " + need[i]['qty'] + "個#k\r\n\r\n如果把這些資料收集起來重新對話我\r\n#b#z" + tocoin + "##k就可以兌換." + enter;
        }


        if (haveNeed(1))
            cm.sendNext(msg);
        else {
            msg += enter + enter + "#r現在材料好像還不够。.#k";
            cm.sendOk(msg);
            cm.dispose();
        }
    } else if (status == 1) {
        temp = [];
        for (i = 0; i < need.length; i++) {
            temp.push(Math.floor(cm.itemQuantity(need[i]['itemid']) / need[i]['qty']));
        }
        temp.sort();
        max = 300;
        cm.sendGetNumber("一次最多可以交換 #b" + max + "個#k 您要兌換幾個.\r\n...?", 1, 1, max);
    } else if (status == 2) {
        if (!haveNeed(sel)) {
            cm.sendOk("持有的物品不足.");
            cm.dispose();
            return;
        }
        for (i = 0; i < need.length; i++) {
            cm.gainItem(need[i]['itemid'], -(need[i]['qty'] * sel));
        }
        if (!cm.canHold(tocoin, (toqty * sel))) {
            cm.sendOk("庫存空間不足.");
            cm.dispose();
            return;
        }
        cm.gainItem(tocoin, (toqty * sel));
        cm.sendOk("收到了終極噩耗..");
        Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 궁극의 비보를 지급 받으셨습니다."));
        cm.dispose();
    }
}

function haveNeed(a) {
    var ret = true;
    for (i = 0; i < need.length; i++) {
        if (!cm.haveItem(need[i]['itemid'], (need[i]['qty'] * a)))
            ret = false;
    }
    return ret;
}

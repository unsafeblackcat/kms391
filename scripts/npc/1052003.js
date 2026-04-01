//var enter = "\r\n"; // 註釋中提示enter為換行符，直接使用"\r\n"替代
var seld = -1;

var need = [
    {'itemid' : 4310248, 'qty' : 200},
]
var tocoin = 2433979, toqty = 1;

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
        var msg = "我需要準備學校作業的克萊帕斯材料.. 你有帶嗎?" + "\r\n"; // 直接使用"\r\n"作為換行符

        for (i = 0; i < need.length; i++) {
            if (i != need.length - 1) 
                msg += "#i" + need[i]['itemid'] + "##z" + need[i]['itemid'] + "# " + need[i]['qty'] + "個和" + "\r\n";
            else 
                msg += "#i" + need[i]['itemid'] + "##z" + need[i]['itemid'] + "# " + need[i]['qty'] + "個。如果您願意給我，將對我有很大幫助。作為回報，我會給您我持有的#b#z" + tocoin + "##k1個。" + "\r\n";
        }

        if (haveNeed(1))
            cm.sendNext(msg);
        else {
            msg += "\r\n\r\n" + "呵呵.. 你沒有帶克萊帕斯呢.....";
            cm.sendOk(msg);
            cm.dispose();
        }
    } else if (status == 1) {
        temp = [];
        for (i = 0; i < need.length; i++) {
            temp.push(Math.floor(cm.itemQuantity(need[i]['itemid']) / need[i]['qty']));
        }
        temp.sort();
        max = temp[0];
        cm.sendGetNumber("你最多可以交換#b" + max + "次#k..\r\n你想交換幾次呢...?", 1, 1, max);
    } else if (status == 2) {
        if (!haveNeed(sel)) {
            cm.sendOk("你持有的道具不足。");
            cm.dispose();
            return;
        }
        for (i = 0; i < need.length; i++) {
            cm.gainItem(need[i]['itemid'], -(need[i]['qty'] * sel));
        }
        cm.gainItem(tocoin, (toqty * sel));
        cm.sendOk("已發放梅索拉奇幣。");
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
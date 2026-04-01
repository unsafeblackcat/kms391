var enter = "\r\n";
var seld = -1;
 
var need = [
    {'itemid' : 4001326, 'qty' : 1},
    {'itemid' : 4001327, 'qty' : 1},
    {'itemid' : 4001328, 'qty' : 1},
    {'itemid' : 4001329, 'qty' : 1},
    {'itemid' : 4001330, 'qty' : 1},
    {'itemid' : 4001331, 'qty' : 1},
    {'itemid' : 4001332, 'qty' : 1}
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
        var msg = "#fs15##fc0xFF000000#嗚哇~ 我想畫彩虹但沒有蠟筆畫不了... 抽泣\r\n#b"+enter;
 
        for (i = 0; i < need.length;  i++) {
            if (i != need.length  - 1) msg += "#b#i"+need[i]['itemid']+"##z"+need[i]['itemid']+"# "+need[i]['qty']+"個和"+enter;
            else msg += "#i"+need[i]['itemid']+"##z"+need[i]['itemid']+"# "+need[i]['qty']+"個可以幫我找來嗎？抽泣..."+enter;
        }
 
        msg += "集齊七色蠟筆的話"+enter;
        msg += "#b獎勵 : #z"+tocoin+"##k\r\n";
        if (haveNeed(1))
            cm.sendNext(msg); 
        else {
            msg +="\r\n嗯... 但你沒有可以交換的物品呢....";
            cm.sendOk(msg); 
            cm.dispose(); 
        }
    } else if (status == 1) {
        temp = [];
        for (i = 0; i < need.length;  i++) {
            temp.push(Math.floor(cm.itemQuantity(need[i]['itemid'])  / need[i]['qty']));
        }
        temp.sort(); 
        max = temp[0];
        cm.sendGetNumber(" 你最多可以交換#b"+max+"次#k...\r\n要交換幾次呢...？", 1, 1, max);
    } else if (status == 2) {
        if (!haveNeed(sel)) {
            cm.sendOk(" 什麼嘛... 想騙我嗎...？物品根本不夠啊...！");
            cm.dispose(); 
            return;
        }
        for (i = 0; i < need.length;  i++) {
            cm.gainItem(need[i]['itemid'],  -(need[i]['qty'] * sel));
        }
        cm.gainItem(tocoin,  (toqty * sel));
        cm.sendOk(" 真是愉快的交易呢...");
        cm.dispose(); 
    }
}
 
function haveNeed(a) {
    var ret = true;
    for (i = 0; i < need.length;  i++) {
        if (!cm.haveItem(need[i]['itemid'],  (need[i]['qty'] * a)))
            ret = false;
    }
    return ret;
}
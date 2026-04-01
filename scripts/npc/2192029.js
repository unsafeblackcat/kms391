var enter = "\r\n";
var seld = -1;
var selditem;

var shops = [
    { 'itemid': 4310266, 'qty': 1, 'price': 1 }, // 잠수코인
]

var p = -1;
var seldqty = -1;

var isEquip = false;

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
        p = cm.getPlayer().getKeyValue(100161, "point");
        if (p == -1) {
            cm.getPlayer().setKeyValue(100161, "point", "0");
        }
        var msg = "가지고있는 잠수포인트를 상점에서 거래할수있게 코인으로 교환해주고 있어요!" + enter;
        msg += "현재 #b#h ##k님의 잠수포인트는 #b" + p + "P#k입니다.#fs15##b" + enter;
        for (i = 0; i < shops.length; i++) {
            msg += "#L" + i + "##i" + shops[i]['itemid'] + "##z" + shops[i]['itemid'] + "# " + shops[i]['qty'] + "개 [" + shops[i]['price'] + "P]" + enter;
        }
        cm.sendSimple(msg);

    } else if (status == 1) {
        seld = sel;
        selditem = shops[sel];
        if (sel == 9999 && cm.getPlayer().isGM()) {
            cm.getPlayer().setKeyValue(100161, "point", "999");
            cm.dispose();
        }

        if (Math.floor(selditem['itemid'] / 1000000) > 1) {
            var max = Math.floor(p / selditem['price']);
            var msg = "이 아이템은 한 번에 여러 개 구매할 수 있어요!" + enter;
            msg += "최대 " + max + "개 구매하실 수 있는데, 몇 개 구매하시겠어요?";
            cm.sendGetNumber(msg, 1, 1, max);
        } else {
            if (p < selditem['price']) {
                cm.sendOk("선택하신 아이템을 사기엔 포인트가 부족합니다.");
                cm.dispose();
                return;
            }
            isEquip = true;
            var msg = "선택하신 아이템은 #b#i" + selditem['itemid'] + "##z" + selditem['itemid'] + "# " + selditem['qty'] + "개#k이며, 가격은 #b" + selditem['price'] + "P#k입니다." + enter;
            msg += "#fs15##r정말 구매하시려면 '예'를 눌러주세요.";
            cm.sendYesNo(msg);
        }

    } else if (status == 2) {
        if (isEquip) {
            if (p < selditem['price']) {
                cm.sendOk("선택하신 아이템을 사기엔 포인트가 부족합니다.");
                cm.dispose();
                return;
            }
            cm.gainItem(selditem['itemid'], selditem['qty']);
            cm.getPlayer().setKeyValue(100161, "point", "" + (p - selditem['price']));
            cm.sendOk("거래가 완료되었습니다.");
            cm.dispose();
        } else {
            seldqty = sel;
            if (p < (selditem['price'] * seldqty)) {
                cm.sendOk("선택하신 아이템을 사기엔 포인트가 부족합니다.");
                cm.dispose();
                return;
            }
            var msg = "선택하신 아이템은 #b#i" + selditem['itemid'] + "##z" + selditem['itemid'] + "# " + (selditem['qty'] * seldqty) + "개이며, 가격은 #b" + (selditem['price'] * seldqty) + "P#k입니다." + enter;
            msg += "#fs15##r정말 구매하시려면 '예'를 눌러주세요.";
            cm.sendYesNo(msg);
        }

    } else if (status == 3) {
        if (!isEquip) {
            if (p < (selditem['price'] * seldqty)) {
                cm.sendOk("선택하신 아이템을 사기엔 포인트가 부족합니다.");
                cm.dispose();
                return;
            }
            cm.gainItem(selditem['itemid'], (selditem['qty'] * seldqty));
            cm.getPlayer().setKeyValue(100161, "point", "" + (p - (selditem['price'] * seldqty)));
            cm.sendOk("거래가 완료되었습니다.");
            cm.dispose();
        }
    }
}
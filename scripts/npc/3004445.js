var enter = "\r\n";
var seld = -1;

var war = [
    {'itemid': 1003990, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1003797, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
    {'itemid': 1042300, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1042254, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
    {'itemid': 1062190, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1062165, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
]

var ran = [
    {'itemid': 1003992, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1003799, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
    {'itemid': 1042302, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1042256, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
    {'itemid': 1062192, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1062167, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
]

var dun = [
    {'itemid': 1003991, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1003798, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
    {'itemid': 1042301, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1042255, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
    {'itemid': 1062191, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1062166, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
]

var ass = [
    {'itemid': 1003993, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1003800, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
    {'itemid': 1042303, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1042257, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
    {'itemid': 1062193, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1062168, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
]

var won = [
    {'itemid': 1003994, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1003801, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
    {'itemid': 1042304, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1042258, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
    {'itemid': 1062194, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1062169, 10], [4033449, 2]], 'price': 3000000000, 'chance': 70,
        'fail': [[4033449, 1]]
    },
]

var item;
var items;
var isEquip = false;
var canMake = false;

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
        var msg = "#h0#님 안녕하세요? 블랙 월드에 오신것을 환영합니다!" + enter
        msg += "저는 방어구들을 우주의 힘을 이용해 강화시켜드립니다!" + enter + enter
        msg += "#b#L0#워리어 각성 방어구#k\r\n#b#L1#레인져 각성 방어구#k\r\n#b#L2#던위치 각성 방어구#k\r\n#b#L3#어새신 각성 방어구#k\r\n#b#L4#원더러 각성 방어구#k"
        cm.sendSimple("#fs15#" + msg); 
    } else if (status == 1) {
        var msg = enter
        msg += "#h0# 님? 안녕하세요.블랙 월드에 오신것을 환영합니다!" + enter
        msg += "저는 방어구들을 우주의 힘을 이용해 강화시켜드립니다!" + enter
        if (sel == 0) {
            items = 0
            for (i = 0; i < war.length; i++)
                msg += "#L" + i + "##i" + war[i]['itemid'] + "##z" + war[i]['itemid'] + "# " + war[i]['qty'] + "개" + enter;
        } else if (sel == 1) {
            items = 1
            for (i = 0; i < ran.length; i++)
                msg += "#L" + i + "##i" + ran[i]['itemid'] + "##z" + ran[i]['itemid'] + "# " + ran[i]['qty'] + "개" + enter;
				        } else if (sel == 2) {
            items = 2
            for (i = 0; i < dun.length; i++)
                msg += "#L" + i + "##i" + dun[i]['itemid'] + "##z" + dun[i]['itemid'] + "# " + dun[i]['qty'] + "개" + enter;
				        } else if (sel == 3) {
            items = 3
            for (i = 0; i < ass.length; i++)
                msg += "#L" + i + "##i" + ass[i]['itemid'] + "##z" + ass[i]['itemid'] + "# " + ass[i]['qty'] + "개" + enter;
				        } else if (sel == 4) {
            items = 4
            for (i = 0; i < won.length; i++)
                msg += "#L" + i + "##i" + won[i]['itemid'] + "##z" + won[i]['itemid'] + "# " + won[i]['qty'] + "개" + enter;
        }
        cm.sendSimple("#fs15#" + msg);
    } else if (status == 2) {
        seld = sel;
        if (items == 0) {
            item = war[sel];
        } else if (items == 1) {
            item = ran[sel];
			        } else if (items == 2) {
            item = dun[sel];
			        } else if (items == 3) {
            item = ass[sel];
			        } else if (items == 4) {
            item = won[sel];
        }
        isEquip = Math.floor(item['itemid'] / 1000000) == 1;

        canMake = checkItems(item);

        var msg = "선택하신 아이템은 다음과 같습니다.#b" + enter;
        msg += "아이템 : #i" + item['itemid'] + "##z" + item['itemid'] + "# " + item['qty'] + "개" + enter;

        if (isEquip) {
            if (item['allstat'] > 0)
                msg += "올스탯 : +" + item['allstat'] + enter;
            if (item['atk'] > 0)
                msg += "공격력, 마력 : +" + item['atk'] + enter;
        }

        msg += enter;
        msg += "#k선택하신 아이템을 제작하기 위한 레시피입니다.#d" + enter + enter;

        if (item['recipes'].length > 0) {
            for (i = 0; i < item['recipes'].length; i++)
                msg += "#i" + item['recipes'][i][0] + "##z" + item['recipes'][i][0] + "# " + item['recipes'][i][1] + "개" + enter;
        }

        if (item['price'] > 0)
            msg += "#i5200002#" + cm.Comma(item['price']) + " 메소" + enter;

        msg += enter + "제작 성공 확률 : " + item['chance'] + "%#n" + enter + enter;
        msg += "#k제작 실패시 다음과 같은 아이템이 지급됩니다.#d" + enter + enter;
        if (item['fail'].length > 0) {
            for (i = 0; i < item['fail'].length; i++)
                msg += "#i" + item['fail'][i][0] + "##z" + item['fail'][i][0] + "# " + item['fail'][i][1] + "개" + enter;
        }
        msg += "" + enter;
        msg += canMake ? "#b선택하신 아이템을 만들기 위한 재료들이 모두 모였습니다." + enter + "정말 제작하시려면 '예'를 눌러주세요." : "#r선택하신 아이템을 만들기 위한 재료들이 충분하지 않습니다.";

        if (canMake)
            cm.sendYesNo("#fs15#" + msg);
        else {
            cm.sendOk("#fs15#" + msg);
            cm.dispose();
        }

    } else if (status == 3) {
        canMake = checkItems(item);

        if (!canMake) {
            cm.sendOk("#fs15#" + "재료가 충분한지 다시 한 번 확인해주세요.");
            cm.dispose();
            return;
        }
        payItems(item);
        if (Packages.server.Randomizer.rand(1, 100) <= item['chance']) {
            gainItem(item);
            cm.sendOk("#fs15#" + "제작 완료");
            Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 [" + cm.getItemName(item['itemid']) + "] 제작 성공하셨습니다."));
        } else {
            cm.sendOk("#fs15#" + "안타깝지만 각성에 실패하여 아이템의 잔해만 남았습니다... R.I.P" + enter + "위로 아이템이 지급되었습니다.");
            gainFail(item);
        }
        cm.dispose();
    }
}

function checkItems(i) {
    recipe = i['recipes'];
    ret = true;

    for (j = 0; j < recipe.length; j++) {
        if (!cm.haveItem(recipe[j][0], recipe[j][1])) {
            //cm.getPlayer().dropMessage(6, "fas");
            ret = false;
            break;
        }
    }
    if (ret)
        ret = cm.getPlayer().getMeso() >= i['price'];

    return ret;
}

function payItems(i) {
    recipe = i['recipes'];
    for (j = 0; j < recipe.length; j++) {
       cm.getPlayer().removeItem(recipe[j][0], -recipe[j][1]);
    }
    cm.gainMeso(-3000000000);
}

function gainItem(i) {
    ise = Math.floor(i['itemid'] / 1000000) == 1;
    if (ise) {
        vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(i['itemid']);
        if (i['allstat'] > 0) {
            vitem.setStr(i['allstat']);
            vitem.setDex(i['allstat']);
            vitem.setInt(i['allstat']);
            vitem.setLuk(i['allstat']);
        }
        if (i['atk'] > 0) {
            vitem.setWatk(i['atk']);
            vitem.setMatk(i['atk']);
        }
        Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
    } else {
        cm.gainItem(i['itemid'], i['qty']);
    }
}
function gainFail(i) {
    fail = i['fail'];

    for (j = 0; j < fail.length; j++) {
        cm.gainItem(fail[j][0], fail[j][1]);
    }
}
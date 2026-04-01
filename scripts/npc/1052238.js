var enter = "\r\n";
var seld = -1;

var moon = [
    {'itemid': 1672075, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1672082, 1], [4033449, 5], [4033450, 5]], 'price': 1000000000, 'chance': 60,
        'fail': [[4033449, 1], [4033450, 1]]
    },
    {'itemid': 1012635, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1012632, 1], [4033449, 5], [4033450, 5]], 'price': 1000000000, 'chance': 60,
        'fail': [[4033449, 1], [4033450, 1]]
    },
    {'itemid': 1022281, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1022278, 1], [4033449, 5], [4033450, 5]], 'price': 1000000000, 'chance': 60,
        'fail': [[4033449, 1], [4033450, 1]]
    },
    {'itemid': 1132309, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1132308, 1], [4033449, 5], [4033450, 5]], 'price': 1000000000, 'chance': 60,
        'fail': [[4033449, 1], [4033450, 1]]
    },
    {'itemid': 1162084, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1162080, 1], [4033449, 5], [4033450, 5]], 'price': 1000000000, 'chance': 60,
        'fail': [[4033449, 1], [4033450, 1]]
    },
    {'itemid': 1162085, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1162081, 1], [4033449, 5], [4033450, 5]], 'price': 1000000000, 'chance': 60,
        'fail': [[4033449, 1], [4033450, 1]]
    },
    {'itemid': 1162086, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1162082, 1], [4033449, 5], [4033450, 5]], 'price': 1000000000, 'chance': 60,
        'fail': [[4033449, 1], [4033450, 1]]
    },
    {'itemid': 1162087, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1162083, 1], [4033449, 5], [4033450, 5]], 'price': 1000000000, 'chance': 60,
        'fail': [[4033449, 1], [4033450, 1]]
    },
    {'itemid': 1032317, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1032316, 1], [4033449, 5], [4033450, 5]], 'price': 1000000000, 'chance': 60,
        'fail': [[4033449, 1], [4033450, 1]]
    },
    {'itemid': 1122431, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1122430, 1], [4033449, 5], [4033450, 5]], 'price': 1000000000, 'chance': 60,
        'fail': [[4033449, 1], [4033450, 1]]
    },
    {'itemid': 1182286, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[1182285, 1], [4033449, 5], [4033450, 5]], 'price': 1000000000, 'chance': 60,
        'fail': [[4033449, 1], [4033450, 1]]
    },
]

var star = [
    {'itemid': 1113130, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[4033449, 20], [4033450, 20]], 'price': 2000000000, 'chance': 60,
        'fail': [[4033449, 2], [4033450, 2]]
    },
    {'itemid': 1113131, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[4033449, 20], [4033450, 20]], 'price': 2000000000, 'chance': 60,
        'fail': [[4033449, 2], [4033450, 2]]
    },
    {'itemid': 1113132, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[4033449, 20], [4033450, 20]], 'price': 2000000000, 'chance': 60,
        'fail': [[4033449, 2], [4033450, 2]]
    },
    {'itemid': 1113133, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[4033449, 20], [4033450, 20]], 'price': 2000000000, 'chance': 60,
        'fail': [[4033449, 2], [4033450, 2]]
    },
    {'itemid': 1190303, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[4033449, 20], [4033450, 20]], 'price': 2000000000, 'chance': 60,
        'fail': [[4033449, 2], [4033450, 2]]
    },
    {'itemid': 1122151, 'qty': 1, 'allstat': 0, 'atk': 0,
        'recipes': [[4033449, 20], [4033450, 20]], 'price': 2000000000, 'chance': 60,
        'fail': [[4033449, 2], [4033450, 2]]
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
        var msg = "#h0#님? 안녕하세요.블랙 월드에 오신것을 환영합니다!" + enter
        msg += "저는 장신구들을 달빛 PRAY의 힘을 이용해 강화시켜드립니다!" + enter + enter
        msg += "#r#L0#MOON Accessories#k#b#L1#STAR Accessories#k"
        cm.sendSimple("#fs15#" + msg);
    } else if (status == 1) {
        var msg = enter
        msg += "#h0# 님? 안녕하세요.블랙 월드에 오신것을 환영합니다!" + enter
        msg += "저는 장신구들을 달빛 PRAY의 힘을 이용해 강화시켜드립니다!" + enter
        if (sel == 0) {
            items = 0
            for (i = 0; i < moon.length; i++)
                msg += "#L" + i + "##i" + moon[i]['itemid'] + "##z" + moon[i]['itemid'] + "# " + moon[i]['qty'] + "개" + enter;
        } else {
            items = 1
            for (i = 0; i < star.length; i++)
                msg += "#L" + i + "##i" + star[i]['itemid'] + "##z" + star[i]['itemid'] + "# " + star[i]['qty'] + "개" + enter;
        }
        cm.sendSimple("#fs15#" + msg);
    } else if (status == 2) {
        seld = sel;
        if (items == 0) {
            item = moon[sel];
        } else {
            item = star[sel];
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
            //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11,"", cm.getPlayer().getName()+"님께서 ["+cm.getItemName(item['itemid'])+"] 제작 실패하셨습니다."));
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
    pricee = i['price'];
    for (j = 0; j < recipe.length; j++) {
        cm.getPlayer().removeItem(recipe[j][0], -recipe[j][1]);
    }
    cm.gainMeso(-pricee);
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
importPackage(Packages.client.inventory);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.provider);
importPackage(Packages.server);
importPackage(Packages.constants);

var enter = "\r\n";
var seld = -1;
var items = [
    {'itemid': 1008170, 'qty': 1, 'allstat': 300, 'atk': 7000, //커스텀 모자
        'recipes': [[1002026, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1012820, 'qty': 1, 'allstat': 300, 'atk': 7000, //마크
        'recipes': [[1012260, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1022400, 'qty': 1, 'allstat': 300, 'atk': 7000, //안대
        'recipes': [[1022073, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1032520, 'qty': 1, 'allstat': 300, 'atk': 7000, //이어링
        'recipes': [[1032160, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1122700, 'qty': 1, 'allstat': 300, 'atk': 7000, //스타펜던트
        'recipes': [[1122007, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1122701, 'qty': 1, 'allstat': 300, 'atk': 7000, //문펜던트
        'recipes': [[1122058, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1132520, 'qty': 1, 'allstat': 300, 'atk': 7000, //벨트 기존템이름교체
        'recipes': [[1132145, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1152404, 'qty': 1, 'allstat': 300, 'atk': 7000, //견장
        'recipes': [[1152074, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1162420, 'qty': 1, 'allstat': 300, 'atk': 7000, //마도서
        'recipes': [[1162004, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1182420, 'qty': 1, 'allstat': 300, 'atk': 7000, //뱃지
        'recipes': [[1182263, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1103670, 'qty': 1, 'allstat': 300, 'atk': 7000, //망토
        'recipes': [[1102369, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1042870, 'qty': 1, 'allstat': 300, 'atk': 7000, //셔츠
        'recipes': [[1042352, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1082970, 'qty': 1, 'allstat': 300, 'atk': 7000, //장갑
        'recipes': [[1082145, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1062570, 'qty': 1, 'allstat': 300, 'atk': 7000, //팬츠
        'recipes': [[1062000, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1073870, 'qty': 1, 'allstat': 300, 'atk': 7000, //신발
        'recipes': [[1072369, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },
    {'itemid': 1113670, 'qty': 1, 'allstat': 300, 'atk': 7000, //블루링
        'recipes': [[1113510, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1113671, 'qty': 1, 'allstat': 300, 'atk': 7000, //레드링
        'recipes': [[1113511, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1113672, 'qty': 1, 'allstat': 300, 'atk': 7000, //그린링
        'recipes': [[1113512, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1113673, 'qty': 1, 'allstat': 300, 'atk': 7000, //퍼플링
        'recipes': [[1113513, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1672310, 'qty': 1, 'allstat': 300, 'atk': 7000, //하트
        'recipes': [[1672028, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },

    {'itemid': 1190405, 'qty': 1, 'allstat': 300, 'atk': 7000, //엠블렘 엠블이름 교체 
        'recipes': [[1190313, 1], [4033897, 20000]], 'price': 1, 'chance': 100, //확률
        'fail': [[4001716, 10]]//실패시 얻는물건
    },
]

var item;
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
        var msg = "제작하실 아이템을 선택해주세요." + enter;
        msg += "#fs15#레시피와 아이템의 정보는 선택하면 나옵니다.#fs15##b" + enter;
        for (i = 0; i < items.length; i++)
            msg += "#L" + i + "##i" + items[i]['itemid'] + "##z" + items[i]['itemid'] + "# " + items[i]['qty'] + "개" + enter;

        cm.sendSimple(msg);
    } else if (status == 1) {
        seld = sel;
        item = items[sel];
        isEquip = Math.floor(item['itemid'] / 1000000) == 1;

        canMake = checkItems(item);

        var msg = "선택하신 아이템은 다음과 같습니다.#fs15##b" + enter;
        msg += "아이템 : #i" + item['itemid'] + "##z" + item['itemid'] + "# " + item['qty'] + "개" + enter;

        if (isEquip) {
            if (item['allstat'] > 0)
                msg += "올스탯 : +" + item['allstat'] + enter;
            if (item['atk'] > 0)
                msg += "공격력, 마력 : +" + item['atk'] + enter;
        }

        msg += enter;
        msg += "#fs15##k선택하신 아이템을 제작하기 위한 레시피입니다.#fs15##d" + enter + enter;

        if (item['recipes'].length > 0) {
            for (i = 0; i < item['recipes'].length; i++)
                msg += "#i" + item['recipes'][i][0] + "##z" + item['recipes'][i][0] + "# " + item['recipes'][i][1] + "개" + enter;
        }

        if (item['price'] > 0)
            msg += "#i5200002#" + item['price'] + " 메소" + enter;

        msg += enter + "#fs15##e제작 성공 확률 : " + item['chance'] + "%#n" + enter + enter;
        msg += "#k제작 실패시 다음과 같은 아이템이 지급됩니다.#fs15##d" + enter + enter;
        if (item['fail'].length > 0) {
            for (i = 0; i < item['fail'].length; i++)
                msg += "#i" + item['fail'][i][0] + "##z" + item['fail'][i][0] + "# " + item['fail'][i][1] + "개" + enter;
        }
        msg += "#fs15#" + enter;
        msg += canMake ? "#b선택하신 아이템을 만들기 위한 재료들이 모두 모였습니다." + enter + "정말 제작하시려면 '예'를 눌러주세요." : "#r선택하신 아이템을 만들기 위한 재료들이 충분하지 않습니다.";

        if (canMake)
            cm.sendYesNo(msg);
        else {
            cm.sendOk(msg);
            cm.dispose();
        }

    } else if (status == 2) {
        canMake = checkItems(item);

        if (!canMake) {
            cm.sendOk("재료가 충분한지 다시 한 번 확인해주세요.");
            cm.dispose();
            return;
        }
        if (!cm.enchantcheck(cm.getPlayer(), item['recipes'][0][0])) {
            cm.sendOk("#i" + item['recipes'][0][0] + "##z" + item['recipes'][0][0] + "# 아이템이 ★100강★ 강이 아닙니다.");
            cm.dispose();
            return;
        }
        payItems(item);
        if (Packages.server.Randomizer.rand(1, 100) <= item['chance']) {
            gainItem(item);
            cm.sendOk("교환이 완료되었습니다");
            //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 ["+cm.getItemName(item['itemid'])+"] 아이템을 오류로인해 교환하셨습니다"));
        } else {
            //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 ["+cm.getItemName(item['itemid'])+"] 제작 실패하셨습니다."));
            cm.sendOk("안타깝지만 제작에 실패하여 아이템의 잔해만 남았습니다... R.I.P" + enter + "위로 아이템이 지급되었습니다.");
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
        if (Math.floor(recipe[j][0] / 1000000) == 1)
            Packages.server.MapleInventoryManipulator.removeById(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, recipe[j][0], 1, false, false);
        else
            cm.gainItem(recipe[j][0], -recipe[j][1]);
        cm.gainMeso(-10000000);
    }
}

function gainItem(i) {
    ise = Math.floor(i['itemid'] / 1000000) == 1;
    if (ise) {
        vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(i['itemid']);
        if (i['allstat'] > 0) {
            vitem.setStr(i['allstat']); //이건 왜이렇게 한거지 ㅋㅋ
            vitem.setDex(i['allstat']);
            vitem.setInt(i['allstat']);
            vitem.setLuk(i['allstat']);
        }
        if (i['atk'] > 0) {
            vitem.setWatk(i['atk']);
            vitem.setMatk(i['atk']);
        }
        vitem.setState(20);
        vitem.setPotential1(40057);
        vitem.setPotential2(40057);
        vitem.setPotential3(40057);
        vitem.setPotential4(40057);
        vitem.setPotential5(40057);
        vitem.setPotential6(40057);
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
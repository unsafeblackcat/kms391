importPackage(Packages.client.inventory);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.provider);
importPackage(Packages.server);
importPackage(Packages.constants);

var enter = "\r\n";
var seld = -1;
var items = [
    {'itemid': 1008190, 'qty': 1, 'allstat2': 300, 'atk2': 400, //커스텀 모자
        'recipes': [[1008170, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1008170, 'qty': 1
    },

    {'itemid': 1012840, 'qty': 1, 'allstat2': 300, 'atk2': 400, //마크
        'recipes': [[1014041, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1014041, 'qty': 1
    },

    {'itemid': 1022420, 'qty': 1, 'allstat2': 300, 'atk2': 400, //안대
        'recipes': [[1022400, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1022400, 'qty': 1
    },

    {'itemid': 1032540, 'qty': 1, 'allstat2': 300, 'atk2': 400, //이어링
        'recipes': [[1032520, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1032520, 'qty': 1
    },

    {'itemid': 1122720, 'qty': 1, 'allstat2': 300, 'atk2': 400, //스타펜던트
        'recipes': [[1122700, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1122700, 'qty': 1
    },

    {'itemid': 1122721, 'qty': 1, 'allstat2': 300, 'atk2': 400, //문펜던트
        'recipes': [[1122701, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1122701, 'qty': 1
    },

    {'itemid': 1132540, 'qty': 1, 'allstat2': 300, 'atk2': 400, //벨트 기존템이름교체
        'recipes': [[1132520, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1132520, 'qty': 1
    },

    {'itemid': 1152424, 'qty': 1, 'allstat2': 300, 'atk2': 400, //견장
        'recipes': [[1152404, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1152404, 'qty': 1
    },

    {'itemid': 1162440, 'qty': 1, 'allstat2': 300, 'atk2': 400, //마도서
        'recipes': [[1162420, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1162420, 'qty': 1
    },

    {'itemid': 1182440, 'qty': 1, 'allstat2': 300, 'atk2': 400, //뱃지
        'recipes': [[1182420, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1182420, 'qty': 1
    },

    {'itemid': 1103721, 'qty': 1, 'allstat2': 300, 'atk2': 400, //망토
        'recipes': [[1103670, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1103670, 'qty': 1
    },

    {'itemid': 1042890, 'qty': 1, 'allstat2': 300, 'atk2': 400, //셔츠
        'recipes': [[1042870, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1042870, 'qty': 1
    },

    {'itemid': 1082990, 'qty': 1, 'allstat2': 300, 'atk2': 400, //장갑
        'recipes': [[1082970, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1082970, 'qty': 1
    },

    {'itemid': 1062590, 'qty': 1, 'allstat2': 300, 'atk2': 400, //팬츠
        'recipes': [[1062570, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1062570, 'qty': 1
    },

    {'itemid': 1079007, 'qty': 1, 'allstat2': 300, 'atk2': 400, //신발
        'recipes': [[1079006, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1079006, 'qty': 1
    },
    {'itemid': 1113690, 'qty': 1, 'allstat2': 300, 'atk2': 400, //블루링
        'recipes': [[1113670, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1113670, 'qty': 1
    },

    {'itemid': 1113691, 'qty': 1, 'allstat2': 300, 'atk2': 400, //레드링
        'recipes': [[1113671, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1113671, 'qty': 1
    },

    {'itemid': 1113692, 'qty': 1, 'allstat2': 300, 'atk2': 400, //그린링
        'recipes': [[1113672, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1113672, 'qty': 1
    },

    {'itemid': 1113693, 'qty': 1, 'allstat2': 300, 'atk2': 400, //퍼플링
        'recipes': [[1113673, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1113673, 'qty': 1
    },

    {'itemid': 1672330, 'qty': 1, 'allstat2': 300, 'atk2': 400, //하트
        'recipes': [[1672300, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1672300, 'qty': 1
    },

    {'itemid': 1190425, 'qty': 1, 'allstat2': 300, 'atk2': 400, //엠블렘 엠블이름 교체 
        'recipes': [[1190405, 1], [4031788, 20000], [4008002, 1]], 'price': 1, 'chance': 50, //확률
        'fail': 1190405, 'qty': 1
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
		msg += "#fs15#[실패시 아이템의 모든게 초기화되어 지급됩니다.]#fs15##b" + enter;
        msg += "#fs15#[신중하게 제작해주세요]#fs15##b" + enter;
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
            if (item['allstat2'] > 0)
                msg += "올스탯 : +" + item['allstat2'] + enter;
            if (item['atk2'] > 0)
                msg += "공격력, 마력 : +" + item['atk2'] + enter;
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
      //  }
     //   if (!cm.enchantcheck(cm.getPlayer(), item['recipes'][0][0])) {
     //       cm.sendOk("#i" + item['recipes'][0][0] + "##z" + item['recipes'][0][0] + "# 아이템이 ★100강★ 강이 아닙니다.");
     //       cm.dispose();
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
            vitem.setStr(i['allstat2']); //이건 왜이렇게 한거지 ㅋㅋ
            vitem.setDex(i['allstat2']);
            vitem.setInt(i['allstat2']);
            vitem.setLuk(i['allstat2']);
        }
        if (i['atk'] > 0) {
            vitem.setWatk(i['atk2']);
            vitem.setMatk(i['atk2']);
        }
        vitem.setState(20);
        vitem.setPotential1(30086);
        vitem.setPotential2(30086);
        vitem.setPotential3(30086);
        vitem.setPotential4(30086);
        vitem.setPotential5(30086);
        vitem.setPotential6(30086);
        Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
    } else {
        cm.gainItem(i['itemid'], i['qty']);
    }
}
function gainFail(i) {
    ise = Math.floor(i['fail'] / 1000000) == 1;
    if (ise) {
        vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(i['fail']);
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
        vitem.setState(20);
        vitem.setPotential1(30086);
        vitem.setPotential2(30086);
        vitem.setPotential3(30086);
        vitem.setPotential4(30086);
        vitem.setPotential5(30086);
        vitem.setPotential6(30086);
        Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
    } else {
        cm.gainItem(i['fail'], i['qty']);
    }
}
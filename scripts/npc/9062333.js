importPackage(Packages.client.inventory);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.provider);
importPackage(Packages.server);
importPackage(Packages.constants);

var enter = "\r\n";
var seld = -1;
var items = [
    {'itemid': 1014010, 'qty': 1, 'allstat2': 500, 'atk2': 800, //후원 마크 커스텀
        'recipes': [[1012850, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1012850, 'qty': 1,
    },

    {'itemid': 1022460, 'qty': 1, 'allstat2': 500, 'atk2': 800, //안대
        'recipes': [[1022430, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1022430, 'qty': 1,
    },

    {'itemid': 1032580, 'qty': 1, 'allstat2': 500, 'atk2': 800, //이어링
        'recipes': [[1032550, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1032550, 'qty': 1,
    },

    {'itemid': 1124010, 'qty': 1, 'allstat2': 500, 'atk2': 800, //스타 펜던트
        'recipes': [[1122730, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1122730, 'qty': 1,
    },

    {'itemid': 1124011, 'qty': 1, 'allstat2': 500, 'atk2': 800, //moon펜던트
        'recipes': [[1122731, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1122731, 'qty': 1,
    },

    {'itemid': 1132580, 'qty': 1, 'allstat2': 500, 'atk2': 800, //벨트
        'recipes': [[1132550, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1132550, 'qty': 1,
    },

    {'itemid': 1152464, 'qty': 1, 'allstat2': 500, 'atk2': 800, //견장
        'recipes': [[1152434, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1152434, 'qty': 1,
    },

    {'itemid': 1162480, 'qty': 1, 'allstat2': 500, 'atk2': 800, //포켓 구슬
        'recipes': [[1162450, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1162450, 'qty': 1,
    },

    {'itemid': 1182480, 'qty': 1, 'allstat2': 500, 'atk2': 800, //뱃지
        'recipes': [[1182450, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1182450, 'qty': 1,
    },

    {'itemid': 1192010, 'qty': 1, 'allstat2': 500, 'atk2': 800, //엠블렘
        'recipes': [[1190435, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1190435, 'qty': 1,
    },

    {'itemid': 1008230, 'qty': 1, 'allstat2': 500, 'atk2': 800, //헬멧
        'recipes': [[1008200, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1008200, 'qty': 1,
    },

    {'itemid': 1103730, 'qty': 1, 'allstat2': 500, 'atk2': 800, //망토
        'recipes': [[1103719, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1103719, 'qty': 1,
    },

    {'itemid': 1044030, 'qty': 1, 'allstat2': 500, 'atk2': 800, //셔츠
        'recipes': [[1042900, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1042900, 'qty': 1,
    },

    {'itemid': 1083030, 'qty': 1, 'allstat2': 500, 'atk2': 800, //장갑
        'recipes': [[1083010, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1083010, 'qty': 1,
    },

    {'itemid': 1062630, 'qty': 1, 'allstat2': 500, 'atk2': 800, //팬츠
        'recipes': [[1062600, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1062600, 'qty': 1,
    },

    {'itemid': 1073930, 'qty': 1, 'allstat2': 500, 'atk2': 800, //신발
        'recipes': [[1073900, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1073900, 'qty': 1,
    },

    {'itemid': 1114030, 'qty': 1, 'allstat2': 500, 'atk2': 800, //반지 블루
        'recipes': [[1113700, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1113700, 'qty': 1,
    },

    {'itemid': 1114031, 'qty': 1, 'allstat2': 500, 'atk2': 800, //반지 레드
        'recipes': [[1113701, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1113701, 'qty': 1,
    },

    {'itemid': 1114032, 'qty': 1, 'allstat2': 500, 'atk2': 800, //반지 그린
        'recipes': [[1113702, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1113702, 'qty': 1,
    },

    {'itemid': 1114033, 'qty': 1, 'allstat2': 500, 'atk2': 800, //반지 퍼플
        'recipes': [[1113703, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1113703, 'qty': 1,
    },

    {'itemid': 1672370, 'qty': 1, 'allstat2': 500, 'atk2': 800, //하트
        'recipes': [[1672340, 1], [4031788, 20000], [4008002,1]], 'price': 1, 'chance': 50,//확률
        'fail': 1672340, 'qty': 1,
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
     //   }
     //   if (!cm.enchantcheck(cm.getPlayer(), item['recipes'][0][0])) {
      //      cm.sendOk("#i" + item['recipes'][0][0] + "##z" + item['recipes'][0][0] + "# 아이템이 ★100강★ 강이 아닙니다.");
     //       cm.dispose();
      //      return;
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
		vitem.setBossDamage(15);
        vitem.setTotalDamage(15);
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
		vitem.setBossDamage(10);
        vitem.setTotalDamage(10);
        Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
    } else {
        cm.gainItem(i['fail'], i['qty']);
    }
}
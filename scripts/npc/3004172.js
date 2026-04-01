importPackage(Packages.client.inventory);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.provider);
importPackage(Packages.server);
importPackage(Packages.constants);

var enter = "\r\n";
var seld = -1;
var items = [
   {'itemid' : 1012820, 'qty' : 1, 'allstat': 500, 'atk': 2000, //후원 마크 커스텀
   'recipes' : [[1012830, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1014100, 1]]//실패시 얻는물건
   },


   {'itemid' : 1022430, 'qty' : 1, 'allstat': 500, 'atk': 2000, //안대
   'recipes' : [[1022410, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1022550, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1032550, 'qty' : 1, 'allstat': 500, 'atk': 2000, //이어링
   'recipes' : [[1032530, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1032670, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1122730, 'qty' : 1, 'allstat': 500, 'atk': 2000, //스타 펜던트
   'recipes' : [[1122710, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1124100, 1]]//실패시 얻는물건
   },


   {'itemid' : 1122731, 'qty' : 1, 'allstat': 500, 'atk': 2000, //moon펜던트
   'recipes' : [[1122711, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1124101, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1132550, 'qty' : 1, 'allstat': 500, 'atk': 2000, //벨트
   'recipes' : [[1132530, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1132670, 1]]//실패시 얻는물건
   },   


   {'itemid' : 1152434, 'qty' : 1, 'allstat': 500, 'atk': 2000, //견장
   'recipes' : [[1152414, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1152554, 1]]//실패시 얻는물건
   },


   {'itemid' : 1162450, 'qty' : 1, 'allstat': 500, 'atk': 2000, //포켓 구슬
   'recipes' : [[1162430, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1162570, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1182450, 'qty' : 1, 'allstat': 500, 'atk': 2000, //뱃지
   'recipes' : [[1182430, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1182580, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1190435, 'qty' : 1, 'allstat': 500, 'atk': 2000, //엠블렘
   'recipes' : [[1190415, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1192100, 1]]//실패시 얻는물건
   },


   {'itemid' : 1008200, 'qty' : 1, 'allstat': 500, 'atk': 2000, //헬멧
   'recipes' : [[1008180, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1008320, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1103700, 'qty' : 1, 'allstat': 500, 'atk': 2000, //망토
   'recipes' : [[1103680, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1103820, 1]]//실패시 얻는물건
   },  

   
   {'itemid' : 1042900, 'qty' : 1, 'allstat': 500, 'atk': 2000, //셔츠
   'recipes' : [[1042880, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1044120, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 10830000, 'qty' : 1, 'allstat': 500, 'atk': 2000, //장갑
   'recipes' : [[1082980, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1083120, 1]]//실패시 얻는물건
   },   


   {'itemid' : 1062600, 'qty' : 1, 'allstat': 500, 'atk': 2000, //팬츠
   'recipes' : [[1062580, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1062720, 1]]//실패시 얻는물건
   },


   {'itemid' : 1073900, 'qty' : 1, 'allstat': 500, 'atk': 2000, //신발
   'recipes' : [[1073880, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1074020, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1113700, 'qty' : 1, 'allstat': 500, 'atk': 2000, //반지 블루
   'recipes' : [[1113680, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1114120, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1113701, 'qty' : 1, 'allstat': 500, 'atk': 2000, //반지 레드
   'recipes' : [[1113681, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1114121, 1]]//실패시 얻는물건
   },


   {'itemid' : 1113702, 'qty' : 1, 'allstat': 500, 'atk': 2000, //반지 그린
   'recipes' : [[1113682, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1114122, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1113703, 'qty' : 1, 'allstat': 500, 'atk': 2000, //반지 퍼플
   'recipes' : [[1113683, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1114123, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1672340, 'qty' : 1, 'allstat': 500, 'atk': 2000, //하트
   'recipes' : [[1672320, 1], [4033897, 20000]], 'price' : 1, 'chance' : 100, //확률
   'fail' : [[1672460, 1]]//실패시 얻는물건 
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
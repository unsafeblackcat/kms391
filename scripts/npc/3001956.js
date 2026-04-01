var enter = "\r\n";
var seld = -1;

var cao = [
    {'itemid': 1113282, 'qty': 1, 'allstat': 50 , 'atk': 50,
        'recipes': [[1113089, 1], [1122274, 1], [1032227, 1]], 'price': 1000000000, 'chance': 100,
        'fail': [[4319999, 1]]
    },
    {'itemid': 1122150, 'qty': 1,
        'recipes': [[1022232, 1], [1122254, 1], [1032136, 1], [4319999, 1000]], 'price': 1000000000, 'chance': 100,
        'fail': [[4319999, 1]]
    },
    {'itemid': 1113070, 'qty': 1, 'allstat': 70, 'atk': 50,
        'recipes': [[2434587, 30], [2434586, 30], [2434585, 30], [2434584, 30], [4310064, 50]], 'price': 1000000000, 'chance': 100,
        'fail': [[4319999, 1]]
    },
    {'itemid': 1672077, 'qty': 1, 'allstat': 70, 'atk': 50,
        'recipes': [[1012478, 1], [1022231, 1], [1113149, 1], [1032241, 1]], 'price': 1000000000, 'chance': 100,
        'fail': [[4319999, 1]]
    },
    {'itemid': 1113055, 'qty': 1, 'allstat': 70, 'atk': 50,
        'recipes': [[4319999, 2000]], 'price': 1000000000, 'chance': 100,
        'fail': [[4319999, 1]]
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
        var msg = "#fs15#"
        msg += "안녕하세요? #b#e#h0##k#n님! 서버의 제작 담당입니다!" + enter 
        msg += "원하시는 제작 품목을 선택해 주세요!" + enter + enter
            items = 0
            for (i = 0; i < cao.length; i++)
                msg += "#L" + i + "##i" + cao[i]['itemid'] + "##z" + cao[i]['itemid'] + "# " + cao[i]['qty'] + "개" + enter;
        cm.sendSimple("#fs15#" + msg);
    } else if (status == 1) {
        seld = sel;
        if (items == 0) {
            item = cao[sel];
        } else {
            item = star[sel];
        }
        isEquip = Math.floor(item['itemid'] / 1000000) == 1;

        canMake = checkItems(item);

        var msg = "#b 제작할 아이템 : #i" + item['itemid'] + "##z" + item['itemid'] + "#" + enter;
        msg += "#Cgray#――――――――――――――――――――――――――――――――――――――――#k#n";

        if (isEquip) {
            if (item['allstat'] > 0)
                msg += "올스탯 : +" + item['allstat'] + enter;
            if (item['atk'] > 0)
                msg += "공격력, 마력 : +" + item['atk'] + enter;
        }

        msg += enter;
        msg += "#k 필요한 재료는 아래와 같습니다.#d" + enter + enter;

        if (item['recipes'].length > 0) {
            for (i = 0; i < item['recipes'].length; i++)
                msg += "#i" + item['recipes'][i][0] + "##z" + item['recipes'][i][0] + "# " + item['recipes'][i][1] + "개" + enter;
        }

        if (item['price'] > 0)
            msg += "#i5200002#" + item['price'] + " 메소" + enter;

        msg += enter + " 제작 성공 확률 : " + item['chance'] + "%#n" + enter;
        msg += "#Cgray#――――――――――――――――――――――――――――――――――――――――#k#n";
        msg += enter +"#k 제작 실패시 다음과 같은 아이템이 지급됩니다.#d" + enter + enter;
        if (item['fail'].length > 0) {
            for (i = 0; i < item['fail'].length; i++)
                msg += "#i" + item['fail'][i][0] + "##z" + item['fail'][i][0] + "# " + item['fail'][i][1] + "개" + enter;
        }
        msg += "#Cgray#――――――――――――――――――――――――――――――――――――――――#k#n";
        msg += "" + enter;
        msg += canMake ? "#b#e 정말 제작하시겠습니까?" : "#r#e재료들이 충분하지 않습니다.";

        if (canMake)
            cm.sendYesNo("#fs15#" + msg);
        else {
            cm.sendOk("#fs15#" + msg);
            cm.dispose();
        }

    } else if (status == 2) {
        canMake = checkItems(item);

        if (!canMake) {
            cm.sendOk("#fs15#" + "재료가 충분한지 다시 한 번 확인해주세요.");
            cm.dispose();
            return;
        }
        payItems(item);
        if (Packages.server.Randomizer.rand(1, 100) <= item['chance']) {
           gainItem(item);
           cm.sendOk("정상적으로 교환이 되었네.");
           Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 " + cm.getItemName(item['itemid']) + " 아이템으로 승급하셨습니다."));
        } else {
           //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 ["+cm.getItemName(item['itemid'])+"] 제작 실패하셨습니다."));
           cm.sendOk("승급에 실패하였습니다.");
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
     if (ret) ret = cm.getPlayer().getMeso() >= i['price'];
  
     return ret;
  }
  
  function payItems(i) {
     recipe = i['recipes'];
     for (j = 0; j < recipe.length; j++) {
        if (Math.floor(recipe[j][0] / 1000000) == 1)
           Packages.server.MapleInventoryManipulator.removeById(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, recipe[j][0], 1, false, false);
        else {
           if(recipe[j][1] > 30000) {
              cm.gainItem(recipe[j][0], -recipe[j][1]/2);
              cm.gainItem(recipe[j][0], -recipe[j][1]/2);
           } else {
              cm.gainItem(recipe[j][0], -recipe[j][1]);
           }
        } 
        cm.gainMeso(-i['price'] / recipe.length);
     }
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
  
  
  
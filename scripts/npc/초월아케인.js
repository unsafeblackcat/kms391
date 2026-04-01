var enter = "\r\n";
var seld = -1;

var items = [
   {'itemid' : 1004808, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1004808, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1004809, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1004809, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1004810, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1004810, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1004811, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1004811, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1004812, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1004812, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1102940, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1102940, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1102941, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1102941, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1102942, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1102942, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1102943, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1102943, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1102944, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1102944, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1082695, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1082695, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1082696, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1082696, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1082697, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1082697, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1082698, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1082698, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1082699, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1082699, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1053063, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1053063, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1053064, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1053064, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1053065, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1053065, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1053066, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1053066, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1053067, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1053067, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1073158, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1073158, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1073159, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1073159, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1073160, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1073160, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1073161, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1073161, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1073162, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1073162, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1152196, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1152196, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1152197, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1152197, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1152198, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1152198, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1152199, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1152199, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },
   {'itemid' : 1152200, 'qty' : 1, 'allstat' : 250, 'atk' : 150,
   'recipes' : [[1152200, 1], [4310249, 2], [4310015, 10], [4001878, 300], [4001879, 5], [4001890, 2], [4001716, 5]], 'price' : 10, 'chance' : 100,
   'fail' : [[4001716, 1]]
   },


] // 올스탯과 공마는 장비아이템에만 적용되어여

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
      var msg = "#fs11#제작하실 아이템을 선택해주세요."+enter;
      msg += "#fs11#레시피와 아이템의 정보는 선택하면 나옵니다.#fs12##b"+enter;
      for (i = 0; i < items.length; i++)
         msg += "#fs11##L"+i+"##i"+items[i]['itemid']+"##z"+items[i]['itemid']+"# 제작"+enter;

      cm.sendSimple(msg);
   } else if (status == 1) {
      seld = sel;
      item = items[sel];
      isEquip = Math.floor(item['itemid'] / 1000000) == 1;

      canMake = checkItems(item);

      var msg = "#fs11#선택하신 아이템은 다음과 같습니다.#fs11##b"+enter;
      msg += "#fs11#아이템 : #i"+item['itemid']+"##z"+item['itemid']+"#"+enter;

      if (isEquip) {
         if (item['allstat'] > 0)
            msg += "올스탯 : +"+item['allstat']+enter;
         if (item['atk'] > 0)
            msg += "공격력, 마력 : +"+item['atk']+enter;
      }

      msg += enter;
      msg += "#fs11##k선택하신 아이템을 제작하기 위한 레시피입니다.#fs11##d"+enter+enter;

      if (item['recipes'].length > 0) {
         for (i = 0; i < item['recipes'].length; i++)
            msg += "#b#i"+item['recipes'][i][0]+"##z"+item['recipes'][i][0]+"# "+item['recipes'][i][1]+"개 #r/ #c"+item['recipes'][i][0]+"#개 보유 중#k"+enter;
      }
      if (item['price'] > 0)
        // msg += "#i5200002#"+item['price']+" 메소"+enter;

      msg += enter+"#fs11##e제작 성공 확률 : "+item['chance']+"%#n"+enter+enter;
      //msg += "#k제작 실패시 다음과 같은 아이템이 지급됩니다.#fs11##d"+enter+enter;
      if (item['fail'].length > 0) {
         for (i = 0; i < item['fail'].length; i++)
            msg += "#i"+item['fail'][i][0]+"##z"+item['fail'][i][0]+"# "+item['fail'][i][1]+"개"+enter;
      }
      msg +="#fs11#"+enter;
      msg += canMake ? "#b선택하신 아이템을 만들기 위한 재료들이 모두 모였습니다."+enter+"정말 제작하시려면 '예'를 눌러주세요." : "#r선택하신 아이템을 만들기 위한 재료들이 충분하지 않습니다.";

      if (canMake) cm.sendYesNo(msg);
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
      payItems(item);
      if (Packages.server.Randomizer.rand(1, 100) <= item['chance']) {
         gainItem(item);
         cm.sendOk("#fs11#축하드립니다. 제작에 성공하였습니다");
         Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 ["+cm.getItemName(item['itemid'])+"] 을 제작하였습니다."));
      } else {
         cm.sendOk("#fs11#제작에 실패했습니다.");
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
      else cm.gainItem(recipe[j][0], -recipe[j][1]);
           cm.gainMeso(-1000000);
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
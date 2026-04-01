var enter = "\r\n";
var seld = -1;

var items = [
   {'itemid' : 2635903, 'qty' : 1, 
   'recipes' : [[4001716, 200]], 'price' : 10, 'chance' : 25,
   'fail' : [[2635911, 1]]
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
      var msg = "#fs15#인생은 도박이지"+enter;
      msg += "#fs15#클릭하면 바로 도박 시작이라네.#fs15##b"+enter;
      for (i = 0; i < items.length; i++)
         msg += "#fs15##L"+i+"##i"+items[i]['itemid']+"##z"+items[i]['itemid']+"# 도전"+enter;

      cm.sendSimple(msg);
   } else if (status == 1) {
      seld = sel;
      item = items[sel];
      isEquip = Math.floor(item['itemid'] / 1) == 1;

      canMake = checkItems(item);

      var msg = "#fs15#선택하신 도박 은 다음과 같습니다.#fs15##b"+enter;
      msg += "#fs15#아이템 : #i"+item['itemid']+"##z"+item['itemid']+"#"+enter;

      if (isEquip) {
         if (item['allstat'] > 0)
            msg += "올스탯 : +"+item['allstat']+enter;
         if (item['atk'] > 0)
            msg += "공격력, 마력 : +"+item['atk']+enter;
      }

      msg += enter;
      msg += "#fs15##k도박 하기위한 필요한 재료입니다.#fs15##d"+enter+enter;

      if (item['recipes'].length > 0) {
         for (i = 0; i < item['recipes'].length; i++)
            msg += "#b#i"+item['recipes'][i][0]+"##z"+item['recipes'][i][0]+"# "+item['recipes'][i][1]+"개 #r/ #c"+item['recipes'][i][0]+"#개 보유 중#k"+enter;
      }
      if (item['price'] > 0)
        // msg += "#i5200002#"+item['price']+" 메소"+enter;

      //msg += enter+"#fs15##e제작 성공 확률 : "+item['chance']+"%#n"+enter+enter;
      //msg += "#k제작 실패시 다음과 같은 아이템이 지급됩니다.#fs15##d"+enter+enter;
      if (item['fail'].length > 0) {
         for (i = 0; i < item['fail'].length; i++)
            msg += "#i"+item['fail'][i][0]+"##z"+item['fail'][i][0]+"# "+item['fail'][i][1]+"개"+enter;
      }
      msg +="#fs15#"+enter;
      msg += canMake ? "#b선택하신 도박을 하기 위한 재료들이 모두 모였다."+enter+"정말 도박하려면 '예'를 눌러." : "#r선택하신 도박을 하기 위한 재료들이 충분하지 않습니다.";

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
         cm.sendOk("#fs15#축하하네 성공했구만!?");
         Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 ["+cm.getItemName(item['itemid'])+"] 을 제작하였습니다."));
      } else {
         cm.sendOk("#fs15#운이없었군 또 도전하시게.");
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
      if (Math.floor(recipe[j][0] / 1) == 1)
         Packages.server.MapleInventoryManipulator.removeById(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, recipe[j][0], 1, false, false);
      else cm.gainItem(recipe[j][0], -recipe[j][1]);
           cm.gainMeso(-1);
   }
}

function gainItem(i) {
   ise = Math.floor(i['itemid'] / 1) == 1;
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
var enter = "\r\n";
var seld = -1;

var items = [
   {'itemid' : 1014090, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //루컨마 라이징
   'recipes' : [[1014050, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1014050, 1]]//실패시 얻는물건
   },


   {'itemid' : 1022540, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //안대
   'recipes' : [[1022500, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1022500, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1032660, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //이어링
   'recipes' : [[1032620, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1032620, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1124090, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //스타 펜던트
   'recipes' : [[1124050, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1124050, 1]]//실패시 얻는물건
   },


   {'itemid' : 1124091, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //moon펜던트
   'recipes' : [[1124051, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1124051, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1132660, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //벨트
   'recipes' : [[1132620, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1132620, 1]]//실패시 얻는물건
   },   


   {'itemid' : 1152544, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //견장
   'recipes' : [[1152504, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1152504, 1]]//실패시 얻는물건
   },


   {'itemid' : 1162560, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //포켓 구슬
   'recipes' : [[1162520, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1162520, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1182560, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //뱃지
   'recipes' : [[1182520, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1182520, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1192090, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //엠블렘
   'recipes' : [[1192050, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1192050, 1]]//실패시 얻는물건
   },


   {'itemid' : 1008310, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //헬멧
   'recipes' : [[1008270, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1008270, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1103810, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //망토
   'recipes' : [[1103770, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1103770, 1]]//실패시 얻는물건
   },  

   
   {'itemid' : 1044110, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //셔츠
   'recipes' : [[1044070, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1044070, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1083110, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //장갑
   'recipes' : [[1083070, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1083070, 1]]//실패시 얻는물건
   },   


   {'itemid' : 1062710, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //팬츠
   'recipes' : [[1062660, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1062660, 1]]//실패시 얻는물건
   },


   {'itemid' : 1074010, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //신발
   'recipes' : [[1073970, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1073970, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1114110, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //반지 블루
   'recipes' : [[1114070, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1114070, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1114111, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //반지 레드
   'recipes' : [[1114071, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1114071, 1]]//실패시 얻는물건
   },


   {'itemid' : 1114112, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //반지 그린
   'recipes' : [[1114072, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1114072, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1114113, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //반지 퍼플
   'recipes' : [[1114073, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1114073, 1]]//실패시 얻는물건
   },
   
   
   {'itemid' : 1672450, 'qty' : 1, 'allstat' : 0, 'atk' : 0, //하트
   'recipes' : [[1672410, 1],[4319995, 65],[4319999, 30000],[4319996, 65],[4001716, 10]], 'price' : 1, 'chance' : 55, //확률
   'fail' : [[1672450, 1]]//실패시 얻는물건
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
      var msg = "제작하실 아이템을 선택해주세요."+enter;
      msg += "#fs15#레시피와 아이템의 정보는 선택하면 나옵니다.#fs15##b"+enter;
      for (i = 0; i < items.length; i++)
         msg += "#L"+i+"##i"+items[i]['itemid']+"##z"+items[i]['itemid']+"# "+items[i]['qty']+"개"+enter;

      cm.sendSimple(msg);
   } else if (status == 1) {
      seld = sel;
      item = items[sel];
      isEquip = Math.floor(item['itemid'] / 1000000) == 1;

      canMake = checkItems(item);

      var msg = "선택하신 아이템은 다음과 같습니다.#fs15##b"+enter;
      msg += "아이템 : #i"+item['itemid']+"##z"+item['itemid']+"# "+item['qty']+"개"+enter;

      if (isEquip) {
         if (item['allstat'] > 0)
            msg += "올스탯 : +"+item['allstat']+enter;
         if (item['atk'] > 0)
            msg += "공격력, 마력 : +"+item['atk']+enter;
      }

      msg += enter;
      msg += "#fs15##k선택하신 아이템을 제작하기 위한 레시피입니다.#fs15##d"+enter+enter;

      if (item['recipes'].length > 0) {
         for (i = 0; i < item['recipes'].length; i++)
            msg += "#i"+item['recipes'][i][0]+"##z"+item['recipes'][i][0]+"# "+item['recipes'][i][1]+"개"+enter;
      }

      if (item['price'] > 0)
         msg += "#i5200002#"+item['price']+" 메소"+enter;

      msg += enter+"#fs15##e제작 성공 확률 : "+item['chance']+"%#n"+enter+enter;
      msg += "#k제작 실패시 다음과 같은 아이템이 지급됩니다.#fs15##d"+enter+enter;
      if (item['fail'].length > 0) {
         for (i = 0; i < item['fail'].length; i++)
            msg += "#i"+item['fail'][i][0]+"##z"+item['fail'][i][0]+"# "+item['fail'][i][1]+"개"+enter;
      }
      msg +="#fs15#"+enter;
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
         cm.sendOk("교환이 완료되었습니다");
         //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 ["+cm.getItemName(item['itemid'])+"] 아이템을 오류로인해 교환하셨습니다"));
      } else {
         //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 ["+cm.getItemName(item['itemid'])+"] 제작 실패하셨습니다."));
         cm.sendOk("안타깝지만 제작에 실패하여 아이템의 잔해만 남았습니다... R.I.P"+enter+"위로 아이템이 지급되었습니다.");
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
           cm.gainMeso(-10000000);
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
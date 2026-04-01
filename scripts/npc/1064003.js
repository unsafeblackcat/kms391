importPackage(Packages.constants);

var enter = "\r\n";

아이콘 = "#fUI/GuildMark.img/BackGround/00001026/1#"
아이콘2 = "#fUI/GuildMark.img/BackGround/00001028/12#"
아이콘3 = "#fUI/GuildMark.img/BackGround/00001028/9#"

별1 = "#fUI/GuildMark.img/Mark/Pattern/00004014/11#"

Num0 = "#fUI/GuildMark.img/Mark/Letter/00005026/15#"
Num1 = "#fUI/GuildMark.img/Mark/Letter/00005027/15#"
Num2 = "#fUI/GuildMark.img/Mark/Letter/00005028/4#"

T2 = "#fUI/GuildMark.img/Mark/Letter/00005019/4#"
O2 = "#fUI/GuildMark.img/Mark/Letter/00005014/4#"
P2 = "#fUI/GuildMark.img/Mark/Letter/00005015/4#"

T10 = "#fUI/GuildMark.img/Mark/Letter/00005019/15#"
O10 = "#fUI/GuildMark.img/Mark/Letter/00005014/15#"
P10 = "#fUI/GuildMark.img/Mark/Letter/00005015/15#"

랭킹 = "#fUI/Basic.img/theblackcoin/24#";
검정 = "#fc0xFF000000#";
빨강 = "#fc0xFFF15F5F#";
보라 = "#fc0xFF5F00FF#";

강화 = "#fUI/Basic.img/theblackcoin/8#";

var seld = -1;

var items = [
   {
      'itemid': 1003797, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1003797, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1003797, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1042254, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1042254, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1042254, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1062165, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1062165, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1062165, 1]] //성공시 지급되는 아이템
   },

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 전사 끝
   {
      'itemid': 1003798, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1003798, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1003798, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1042255, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1042255, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1042255, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1062166, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1062166, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1062166, 1]] //성공시 지급되는 아이템
   },

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 법사 끝
   {
      'itemid': 1003799, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1003800, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1003800, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1042256, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1042257, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1042257, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1062167, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1062168, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1062168, 1]] //성공시 지급되는 아이템
   },


   {
      'itemid': 1003800, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1003799, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1003799, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1042257, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1042256, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1042256, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1062168, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1062167, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1062167, 1]] //성공시 지급되는 아이템
   },


   {
      'itemid': 1003801, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1003801, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[2000005, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1042258, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1042258, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1042258, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1062169, 'qty': 1, 'allstat': 0, 'atk': 0, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1062169, 1], [4319999, 100]], 'price': 15000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1062169, 1]] //성공시 지급되는 아이템
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
   var msg = "#fs15#";
   if (status == 0) {
      msg += "#fs15#請告訴我倪的職業是製作.\r\n\r\n"
      msg += "#L0#我想製作#e戰士#n道具.\r\n"
      msg += "#L1#我想創建#e法師#n項目.\r\n"
      msg += "#L3#我想製作#e弓手#n道具.\r\n"
      msg += "#L2#我想製作#e盜賊#n道具.\r\n"
      msg += "#L4#我想製作#e海盜#n道具.\r\n"
      cm.sendOk(msg);
   } else if (status == 1) {
      msg += 검정 + "#h0#請選擇想要製作的物品.\r\r\n\n\r\n"
      switch (sel) {
         case 0:
            for (i = 0; i < 3; i++) {
               msg += "#fs15##L" + i + "##i" + items[i]['itemid'] + "# #z" + items[i]['itemid'] + "# " + items[i]['qty'] + "個" + enter;
            }
            break;
         case 1:
            for (i = 3; i < 6; i++) {
               msg += "#fs15##L" + i + "##i" + items[i]['itemid'] + "# #z" + items[i]['itemid'] + "# " + items[i]['qty'] + "個" + enter;
            }
            break;
         case 2:
            for (i = 6; i < 9; i++) {
               msg += "#fs15##L" + i + "##i" + items[i]['itemid'] + "# #z" + items[i]['itemid'] + "# " + items[i]['qty'] + "個" + enter;
            }
            break;
         case 3:
            for (i = 9; i < 12; i++) {
               msg += "#fs15##L" + i + "##i" + items[i]['itemid'] + "# #z" + items[i]['itemid'] + "# " + items[i]['qty'] + "個" + enter;
            }
            break;
         case 4:
            for (i = 12; i < 15; i++) {
               msg += "#fs15##L" + i + "##i" + items[i]['itemid'] + "# #z" + items[i]['itemid'] + "# " + items[i]['qty'] + "個" + enter;
            }
            break;
      }
      cm.sendSimple(msg);
   } else if (status == 2) {
      seld = sel;
      item = items[sel];
      isEquip = Math.floor(item['itemid'] / 1000000) == 1;

      canMake = checkItems(item);

      var msg = "選擇的物品如下.#fs15##b" + enter;
      msg += "物品 : #i" + item['itemid'] + "##z" + item['itemid'] + "# " + item['qty'] + "個" + enter;

      if (isEquip) {
         if (item['allstat'] > 0)
            msg += "올스탯 : +" + item['allstat'] + enter;
         if (item['atk'] > 0)
            msg += "攻擊力, 魔力 : +" + item['atk'] + enter;
      }

      msg += enter;
      msg += "#fs15##k製作選定物品的資料.#fs15##d" + enter + enter;

      if (item['recipes'].length > 0) {
         for (i = 0; i < item['recipes'].length; i++)
            msg += "#i" + item['recipes'][i][0] + "##z" + item['recipes'][i][0] + "# " + item['recipes'][i][1] + "個" + enter;
      }

      if (item['price'] > 0)
         msg += "#i5200002#" + item['price'] + " 金幣（1500萬）" + enter;

      msg += enter + "#fs15##e製作成功幾率 : " + item['chance'] + "%#n" + enter + enter;
      msg += "#k製作失敗時可獲得以下道具.#fs15##d" + enter + enter;
      if (item['fail'].length > 0) {
         for (i = 0; i < item['fail'].length; i++)
            msg += "#i" + item['fail'][i][0] + "##z" + item['fail'][i][0] + "# " + item['fail'][i][1] + "個" + enter;
      }
      msg += "#fs15#" + enter;
      msg += canMake ? "#b製作所選物品的資料都到齊了!." + enter + "要製作的話請按‘是’" : "#r製作所選物品的資料是不是不够?";

      if (canMake) cm.sendYesNo(msg);
      else {
         cm.sendOk(msg);
         cm.dispose();
      }

   } else if (status == 3) {
      canMake = checkItems(item);

      if (!canMake) {
         cm.sendOk("再確認一下資料是否充足.");
         cm.dispose();
         return;
      }
      payItems(item);
      if (Packages.server.Randomizer.rand(1, 100) <= item['chance']) {
         gainItem(item);
         cm.sendOk("正常交換了.");
         Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 " + cm.getItemName(item['itemid']) + " 아이템으로 승급하셨습니다."));
      } else {
         //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 ["+cm.getItemName(item['itemid'])+"] 제작 실패하셨습니다."));
         cm.sendOk("陞級失敗.");
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



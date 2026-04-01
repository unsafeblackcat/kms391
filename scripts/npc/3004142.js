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
검정 = "#fc0xFF191919#";
빨강 = "#fc0xFFF15F5F#";
보라 = "#fc0xFF5F00FF#";

강화 = "#fUI/Basic.img/theblackcoin/8#";

var seld = -1;

var items = [
   {
      'itemid': 1143195, 'qty': 1, 'allstat': 50, 'atk': 50, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [], 'price': 0, 'chance': 100, //재료, 비용, 확률
      'fail': [[1143195, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1143196, 'qty': 1, 'allstat': 150, 'atk': 150, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1143195, 1], [4319999, 5000]], 'price': 5000000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1143196, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1143197, 'qty': 1, 'allstat': 300, 'atk': 300, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1143196, 1], [4319999, 10000]], 'price': 10000000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1143197, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1143198, 'qty': 1, 'allstat': 500, 'atk': 500, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1143197, 1], [4319999, 15000], [4319997, 1000]], 'price': 15000000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1143198, 1]] //성공시 지급되는 아이템
   },
   {
      'itemid': 1143199, 'qty': 1, 'allstat': 800, 'atk': 800, //템id, 수량, 추가스탯, 추가공격력, 스탯은 장비템에만 적용
      'recipes': [[1143198, 1], [4319999, 20000], [4319997, 1500]], 'price': 20000000000, 'chance': 100, //재료, 비용, 확률
      'fail': [[1143199, 1]] //성공시 지급되는 아이템
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
   var msg = "#fs15#"
   if (status == 0) {
      itemsId = 1;
      msg += 검정 + "#h0#請選擇想要製作的物品.\r\n\r\n"
      for (i = 0; i < items.length; i++) {
         msg += "#L" + i + "##i" + items[i]['itemid'] + "# #z" + items[i]['itemid'] + "# " + items[i]['qty'] + "개" + enter;
      }
      cm.sendSimple(msg);
   } else if (status == 1) {
      seld = sel;
      item = items[sel];
      isEquip = Math.floor(item['itemid'] / 1000000) == 1;

      canMake = checkItems(item);

      var msg = "選擇的物品如下.#fs15##b" + enter;
      msg += "物品 : #i" + item['itemid'] + "##z" + item['itemid'] + "# " + item['qty'] + "個" + enter;

      if (isEquip) {
         if (item['allstat'] > 0)
            msg += "自動調節器 : +" + item['allstat'] + enter;
         if (item['atk'] > 0)
            msg += "攻擊力，魔力 : +" + item['atk'] + enter;
      }

      msg += enter;
      msg += "#fs15##k製作選定物品的材料.#fs15##d" + enter + enter;

      if (item['recipes'].length > 0) {
         for (i = 0; i < item['recipes'].length; i++)
            msg += "#i" + item['recipes'][i][0] + "##z" + item['recipes'][i][0] + "# " + item['recipes'][i][1] + "個" + enter;
      }

      if (item['price'] > 0)
         msg += "#i5200002#" + item['price'] + " 金幣 " + enter;

      msg += enter + "#fs15##e製作成功概率 : " + item['chance'] + "%#n" + enter + enter;
      msg += "#k製作成功時，將獲得以下物品.#fs15##d" + enter + enter;
      if (item['fail'].length > 0) {
         for (i = 0; i < item['fail'].length; i++)
            msg += "#i" + item['fail'][i][0] + "##z" + item['fail'][i][0] + "# " + item['fail'][i][1] + "個" + enter;
      }
      msg += "#fs15#" + enter;
      msg += canMake ? "#b為了製作選擇的物品，材料都收集到了啊!." + enter + "要製作的話請點擊“是”" : "#r製作所選物品的材料不够嗎?";
      if (canMake) { cm.sendYesNo(msg); }
      else {
         cm.sendOk(msg);
         cm.dispose();
      }

   } else if (status == 2) {
      canMake = checkItems(item);

      if (!canMake) {
         cm.sendOk("再次確認材料是否充足.");
         cm.dispose();
         return;
      }
      payItems(item);
      if (Packages.server.Randomizer.rand(1, 100) <= item['chance']) {
         gainItem(item);
         cm.sendOk("正常交換了.");
         //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 ["+cm.getItemName(item['itemid'])+"] 아이템을 오류로인해 교환하셨습니다"));
      } else {
         //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 ["+cm.getItemName(item['itemid'])+"] 제작 실패하셨습니다."));
         cm.sendOk("失敗了.");
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
      cm.gainMeso(-item['price'] / recipe.length);
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



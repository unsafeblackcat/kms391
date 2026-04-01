
var enter = "\r\n";
var seld = -1;

var c = 4310229;

var require = -1;
var reqlevel = -1;

var union, main, sub;
var unionStr = "";
var unionNext = "";
var canAdd = -1;
var nextAdd = -1;

function start() {
   status = -1;
   action(1, 0, 0);
}

function getRequire() {
   switch (main) {
      case 1:
         require = sub == 1 ? 120 : sub == 2 ? 140 : sub == 3 ? 150 : sub == 4 ? 160 : sub == 5 ? 170 : -1;
      break;
      case 2:
         require = sub == 1 ? 430 : sub == 2 ? 450 : sub == 3 ? 470 : sub == 4 ? 490 : sub == 5 ? 510 : -1;
      break;
      case 3:
         require = sub == 1 ? 930 : sub == 2 ? 960 : sub == 3 ? 1000 : sub == 4 ? 1030 : sub == 5 ? 1060 : -1;
      break;
      case 4:
         require = sub == 1 ? 2200 : sub == 2 ? 2300 : sub == 3 ? 2350 : sub == 4 ? 2400 : -1;
      break;
   }
}

function getUnionStr() {
   var temp = main == 1 ? "新手" : main == 2 ? "熟練" : main == 3 ? "大師" : main == 4 ? "宗師" : "";
   return temp + " 公會 "+sub+"階";
}

function getUnionNext() {
   var adv = sub == 5;
   if (!adv) {
      nextAdd = (9 * (main)) + (sub + 1);
      var temp = main == 1 ? "新手" : main == 2 ? "熟練" : main == 3 ? "大師" : main == 4 ? "宗師" : "";
      return temp + " 公會  "+(sub + 1)+"階";
   } else {
      nextAdd = (9 * (main + 1));
      var temp = (main + 1) == 1 ? "新手" : (main + 1) == 2 ? "熟練" : (main + 1) == 3 ? "大師" : (main + 1) == 4 ? "宗師" : "";
      return temp + " 公會 1階";
   }
}

function getUnion() {
   union = cm.getPlayer().getKeyValue(18771, "rank");
   main = Math.floor(union / 100);
   sub = union % 10;
   reqlevel = (((main == 1 ? 0 : main == 2 ? 1 : main == 3 ? 2 : 3) * 5) + sub) * 500;
   canAdd = (9 * (main)) + sub;
}

function getUnionChrSize() {
   return cm.getPlayer().getUnions().getUnions().size();
}

function sex() {
   var adv = sub == 5;
   var newunion = "";
   if (adv) {
      newmain = (main + 1);
      newsub = 1;
      newunion = newmain+"0"+newsub;
   } else {
      newsub = (sub + 1);
      newunion = main+"0"+newsub;
   }
   cm.getPlayer().setKeyValue(18771, "rank", newunion);
}

function action(mode, type, sel) {
   ac = 0;
   outmap = 921172200
   outmap2 = 921172201
   coin = cm.getPlayer().getUnionCoin();
   ncoin = cm.getPlayer().getUnionCoinNujuk();
   if (mode == 1) {
      status++;
   } else {
      cm.dispose();
      return;
       }
   if (status == 0) {
   
            if (coin == 0) {
                talk = "看來您尚未獲得任何公會幣呢？如果覺得太困難，可以稍後再來挑戰。其他公會成員會持續累積獎勵的~"
            } else {
            talk = "#i4310229# #b#z4310229##k共獲得#b" + coin + "枚#k！真是厲害！\r\n"
            talk += "已更新#b每週公會幣排名#k並發放獎勵！\r\n\r\n"
            talk += "#b本週累計幣數#k#e:"+(cm.getPlayer().getUnionCoinNujuk()  + coin)+"#n\r\n\r\n"

         cm.getPlayer().AddAllUnionCoin(coin);
         cm.getPlayer().gainItem(4310229, coin);
            cm.getPlayer().setUnionCoinNujuk(ncoin + coin);
            cm.getPlayer().setUnionCoin(0);
            }
            cm.sendOk(talk);
    
   } else if (status == 1) {
            if (coin == 0) {
            cm.dispose();            
            } else {
         
            //cm.getPlayer().saveUnionRanks(cm.getPlayer().getClient());
            cm.dispose();
            return;
            }
}
}
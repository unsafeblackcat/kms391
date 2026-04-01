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
   var temp = main == 1 ? "新手" : main == 2 ? "老手" : main == 3 ? "大師" : main == 4 ? "宗師" : "";
   return temp + " 聯盟 "+sub+"階段";
}
 
function getUnionNext() {
   var adv = sub == 5;
   if (!adv) {
      nextAdd = (9 * (main)) + (sub + 1);
      var temp = main == 1 ? "新手" : main == 2 ? "老手" : main == 3 ? "大師" : main == 4 ? "宗師" : "";
      return temp + " 聯盟 "+(sub + 1)+"階段";
   } else {
      nextAdd = (9 * (main + 1));
      var temp = (main + 1) == 1 ? "新手" : (main + 1) == 2 ? "老手" : (main + 1) == 3 ? "大師" : (main + 1) == 4 ? "宗師" : "";
      return temp + " 聯盟 1階段";
   }
}
 
function getUnion() {
   union = cm.getPlayer().getKeyValue(18771,  "rank");
   main = Math.floor(union  / 100);
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
   cm.getPlayer().setKeyValue(18771,  "rank", newunion);
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
      if (cm.getPlayer().getMapId()  == outmap || cm.getPlayer().getMapId()  == outmap2) {
            if (coin == 0) {
                talk = "嗯~ 您還沒有獲得任何聯盟硬幣嗎？如果覺得太難獲得，可以稍後再來看看。聯盟成員會努力幫您收集硬幣的。"
            } else {
            talk = "#i4310229# #b#z4310229##k 您已經收集了 #b" + coin + "個#k 嗎？真厲害~\r\n"
            talk += "將為您更新 #b每週累積聯盟硬幣排名#k！\r\n\r\n"
            talk += "#b本週累積硬幣#k#e:"+(cm.getPlayer().getUnionCoinNujuk()  + coin)+"#n\r\n\r\n"
            talk += "那麼就送您回到原來的地方吧。再見~" 
            }
            cm.sendNextS(talk,0x04,9010106); 
      } else {
         getUnion();
         unionStr = getUnionStr();
         unionNext = getUnionNext();
         getRequire();
         var msg = "今天真是個適合冒險的好日子！"+enter;
         msg += "需要我幫您處理什麼 #b楓之谷聯盟#k 的事務嗎？#b\r\n"+enter;
         msg += "#L1#<查看我的楓之谷聯盟資訊>"+enter;
         msg += "#L2#<提升楓之谷聯盟等級>"+enter;
         msg += "#L3#<聽取楓之谷聯盟說明>"+enter;
         //msg += "#L4#<每週獲得硬幣排名>";
         cm.sendSimpleS(msg,  4, 9010108);
      }
   } else if (status == 1) {
      if (cm.getPlayer().getMapId()  == outmap || cm.getPlayer().getMapId()  == outmap2) {
            if (coin == 0) {
            cm.warp(cm.getPlayer().getSkillCustomValue0(232471)); 
            cm.dispose();             
            } else {
            cm.getPlayer().AddAllUnionCoin(coin); 
         cm.getPlayer().gainItem(4310229,  coin);
            cm.getPlayer().setUnionCoinNujuk(ncoin  + coin);
            cm.warp(cm.getPlayer().getSkillCustomValue0(232471)); 
            cm.getPlayer().setUnionCoin(0); 
            //cm.getPlayer().saveUnionRanks(cm.getPlayer().getClient()); 
            cm.dispose(); 
            return;
            }
      } else {
         seld = sel;
         switch (sel) {
            case 1:
               var msg = "要告訴您勇者的 #e楓之谷聯盟#n 資訊嗎？"+enter+enter;
               msg += "#e楓之谷聯盟等級: #b<"+unionStr+">#k"+enter;
               msg += "聯盟等級: #b<"+cm.getPlayer().getAllUnion()+">#k"+enter; 
               msg += "擁有的聯盟角色: #b<"+getUnionChrSize()+">#k"+enter;
               msg += "可投入的攻擊隊員:#b<"+canAdd+"名>#k";
               cm.sendOkS(msg,  4, 9010108);
               cm.dispose(); 
            break;
            case 2:
               if (main == 4 && sub == 5) {
                  cm.sendOkS(" 您已經無法再提升聯盟等級了！", 4, 9010108);
                  cm.dispose(); 
                  return;
               }
               var msg = "想要進行 #e楓之谷聯盟升級#n 嗎？"+enter;
               msg += "#e當前等級: #b<"+unionStr+">#k"+enter;
               msg += "下一等級: #b<"+unionNext+">#k"+enter;
               msg += "升級後可投入的攻擊隊員增加: #b<"+canAdd+"→"+nextAdd+" 名>#k#n"+enter+enter;
               msg += "要升級必須滿足以下條件。"+enter+enter;
               msg += "#e<聯盟等級> #r"+reqlevel+"以上#k"+enter;
               msg += "<支付硬幣> #b#z"+c+"# "+require+"個#k#n"+enter+enter;
               msg += "現在要為您進行 #e楓之谷聯盟升級#k 嗎？";
               cm.sendYesNoS(msg,  4, 9010108);
            break;
            case 3:
               var msg = "您對 #b楓之谷聯盟#k 感到好奇嗎？\r\n";
               msg += "想了解什麼呢？\r\n\r\n";
               msg += "#b#L6#楓之谷聯盟是什麼？\r\n"
               msg += "#L7#聯盟等級\r\n"
               msg += "#L8#聯盟等級\r\n"
               msg += "#L9#攻擊隊與戰鬥力\r\n"
               msg += "#L10#戰鬥地圖與角色方塊\r\n"
               msg += "#L11#每週獲得硬幣排名\r\n\r\n#k"
               msg += "#L12##e結束說明#n\r\n"
               cm.sendOkS(msg,  4, 9010108);
            break;
            case 4:
               var msg = "想要查看 #b每週獲得硬幣排名#k 的事務嗎？\r\n需要什麼幫助呢？\r\n";
               msg += "#b#L3#查看當前排名\r\n";
               msg += "#L4#查看上週排名\r\n";
               msg += "#L5#領取上週排名獎勵\r\n";
               cm.sendOkS(msg,  4, 9010108);
               break;
         }
      }
   } else if (status == 2) {
      switch (seld) {
         case 1:
         break;
         case 2:
                if (!cm.haveItem(c,  require)) {
               cm.sendOkS(" 硬幣不夠嗎？", 4, 9010108);
               cm.dispose(); 
               return;
            }
                if (cm.getPlayer().getAllUnion()  < reqlevel) {
               cm.sendOkS(" 聯盟等級不夠嗎？", 4, 9010108);
               cm.dispose(); 
               return;
            }
            var msg = "啪啪啪！"+enter;
            msg += "#e楓之谷聯盟等級#n 提升了！現在可以與更多攻擊隊員一起更快成長了！"+enter+enter;
            msg += "#e新等級: #e<"+unionNext+">#k"+enter;
            msg += "可投入的攻擊隊員: #b"+nextAdd+"#k#n"+enter+enter;
            msg += "那麼就繼續往下一等級努力成長吧！";
                cm.gainItem(c,  -require);
            sex();
            cm.sendOkS(msg,  4, 9010108);
            cm.dispose(); 
         break;
         case 3:
            cm.UnionRank();
            cm.dispose(); 
            break;
         case 4:
            break;
         case 5:
            break;
         case 6:
            msg = "#e<楓之谷聯盟是什麼？>#n\r\n\r\n";
            msg += "#e楓之谷聯盟#n 是指 #b同一世界內角色的集合#k。\r\n"
            msg += "但並非所有角色都能屬於 #b楓之谷聯盟#k。\r\n"
            msg += "#r角色等級需60以上/完成2轉#k才行。\r\n"
            msg += "而且 #r同一世界內超過40名角色時，#k 只有 #b等級最高的前40名角色#k 會被認定為楓之谷聯盟成員。\r\n\r\n"
            msg += "另外 #b零#k 的情況是 #r130級以上等級最高的角色\r\n1名#k 才會被認定為楓之谷聯盟成員。"
            cm.sendNext(msg); 
            break;
         case 7:
            msg = "#e<聯盟等級>#n\r\n\r\n";
            msg += "#e聯盟等級#n 是指 #b楓之谷聯盟#k 成員的\r\n"
            msg += "#r等級總和#k。\r\n\r\n"
            msg += "聯盟等級越高，才能提升到更高的 #b聯盟等級#k 進行 #r升級#k，\r\n"
            msg += "並組建更強大的楓之谷聯盟。\r\n\r\n"
            cm.sendNext(msg); 
            break;
         case 8:
            msg = "#e<聯盟等級>#n\r\n\r\n";
            msg += "#e聯盟等級#n 是當 #b聯盟等級#k 達到特定數值時可以提升的\r\n#b成長指標#k。\r\n\r\n"
            msg += "#b聯盟等級#k 越高，就能 #r投入更多攻擊隊員#k 在 #r更廣闊的戰鬥地圖#k 上進行部署。\r\n"
            msg += "要將 #b聯盟等級#k 提升到一定水平進行 #b升級#k 時，需要支付 #i4310229#\r\n#b#z4310229##k。\r\n\r\n"
            msg += "#L8# #b查看各等級所需等級 /支付硬幣/ 攻擊隊員數量"
            cm.sendNext(msg); 
            break;
         case 9:
            msg = "#e<攻擊隊與戰鬥力>#n\r\n\r\n";
            msg += "#b攻擊隊#k 是指在 #b戰鬥地圖上部署的角色群體#k。\r\n\r\n"
            msg += "#b攻擊隊員#k 可以參與 #r聯盟突襲#k，與強大的敵人戰鬥並收集 #b聯盟硬幣#k。\r\n\r\n"
            msg += "攻擊隊員還能發動角色特有的 #b<攻擊隊效果>#k 和戰鬥地\r\n"
            msg += "圖佔領狀態下的 #b<攻擊隊佔領效果>#k，為 #r世界內所有角色#k 帶來 #b能力值提升效果#k。"
            cm.sendNext(msg); 
            break;
         case 10:
            msg = "#e<戰鬥地圖與角色方塊>#n\r\n\r\n";
            msg += "#b戰鬥地圖#k 是可以部署 #r攻擊隊員#k 進行佔領的地圖，\r\n"
            msg += "由 #b內部8個#k、#r外部8個#k #e共16個區域#n 組成。\r\n"
            msg += "每個區域都有 #b獨特的能力值#k，根據區域的 #r佔領格數#k 會提升相應能力值。\r\n\r\n"
            msg += "#b內部8個區域的能力值#k 可以 #b自由更改#k，\r\n"
            msg += "#r外部8個區域的能力值#k 則是固定的。"
            cm.sendNext(msg); 
            break;
         case 11:
            msg = "#e<每週獲得硬幣排名>#n\r\n\r\n";
            msg += "#b每週獲得硬幣排名#k 是世界內所有角色\r\n"
            msg += "從 #b每週一00:30開始#k 到 #r週日23:30為止#k 獲得的\r\n"
            msg += "#b聯盟硬幣#k 數量為基準進行排名的。"
            cm.sendNext(msg); 
            break;
         case 12:
            cm.dispose(); 
            break;
      }
   } else if (status == 3) {
      switch (seld) {
         case 6:
            msg = "#e<楓之谷聯盟是什麼？>#n\r\n\r\n";
            msg += "屬於 #e楓之谷聯盟#n 的角色會根據 #r等級#k 提升 #b角色等級#k。\r\n\r\n"
            msg += "#e<一般角色>\r\n"
            msg += "#bB(60)>A(100)>S(140)>SS(200)>SSS(250)#k\r\n"
            msg += "<零>\r\n"
            msg += "#bB(130)>A(160)>S(180)>SS(200)>SSS(250)#k#n\r\n"
            cm.sendNextPrev(msg); 
            break;
         case 8:
            msg = "#e<新手聯盟>#n\r\n\r\n";
            msg += "#e1階段#n #bLv.   -    #r所需硬幣: 無#k   #b攻擊隊員: 9名#k\r\n"
            msg += "#e2階段#n #bLv.1000  #r所需硬幣: 120個#k   #b攻擊隊員: 10名#k\r\n"
            msg += "#e3階段#n #bLv.1500  #r所需硬幣: 140個#k   #b攻擊隊員: 11名#k\r\n"
            msg += "#e4階段#n #bLv.2000  #r所需硬幣: 150個#k   #b攻擊隊員: 12名#k\r\n"
            msg += "#e5階段#n #bLv.2500  #r所需硬幣: 160個#k   #b攻擊隊員: 13名#k\r\n"
            cm.sendNext(msg); 
            break;
         case 9:
            msg = "#e<攻擊隊與戰鬥力>#n\r\n\r\n";
            msg += "#b戰鬥力#k 是由角色的 #r等級#k 和 #r星力數值#k 決定的。\r\n\r\n"
            msg += "特別是 #b攻擊隊員#k 的戰鬥力總和稱為 #b攻擊隊戰鬥力#k，"
            msg += "#b攻擊隊戰鬥力#k 越高，在 #r聯盟突襲#k 中就能對敵人造成更多傷害，\r\n"
            msg += "並更快收集聯盟硬幣。"
            cm.sendNextPrev(msg); 
            break;
         case 10:
            msg = "#e<戰鬥地圖與角色方塊>#n\r\n\r\n";
            msg += "在 #b戰鬥地圖#k 上 #e拖放#n 角色，角色會以 #b方塊#k 形式顯示並佔領 #b戰鬥地圖#k 的一部分。\r\n\r\n"
            msg += "#e角色方塊#n 會根據 #b等級#k 和 #b職業類型#k，以 #b基準角色方塊#k 為中心，各自以不同形狀成長。"
            cm.sendNextPrev(msg); 
            break;
         case 11:
            msg = "#e<每週獲得硬幣排名>#n\r\n\r\n";
            msg += "以 #b最後一次#k 更新每週累積硬幣的 #b角色名稱#k 進行排名，\r\n"
            msg += "到了下週會從 #r第1名到第100名的聯盟#k 發放 #b特別禮物#k。\r\n"
            msg += "通過 #b每日任務#k 獲得的硬幣也會累積，所以經常來看看比較好哦？"
            cm.sendNextPrev(msg); 
            break;
         case 7:
            cm.dispose(); 
            cm.openNpc(9010108); 
            break;
      }
   } else if (status == 4) {
      switch (seld) {
         case 8:
            msg = "#e<老手聯盟>#n\r\n\r\n";
            msg += "#e1階段#n #bLv.3000  #r所需硬幣: 170個#k   #b攻擊隊員: 18名#k\r\n"
            msg += "#e2階段#n #bLv.3500  #r所需硬幣: 430個#k   #b攻擊隊員: 19名#k\r\n"
            msg += "#e3階段#n #bLv.4000  #r所需硬幣: 450個#k   #b攻擊隊員: 20名#k\r\n"
            msg += "#e4階段#n #bLv.4500  #r所需硬幣: 470個#k   #b攻擊隊員: 21名#k\r\n"
            msg += "#e5階段#n #bLv.5000  #r所需硬幣: 490個#k   #b攻擊隊員: 22名#k\r\n"
            cm.sendNextPrev(msg); 
            break;
         case 10:
            msg = "#e<戰鬥地圖與角色方塊>#n\r\n\r\n";
            msg += "首次在 #b戰鬥地圖#k 上部署角色時， #r基準角色方塊#k 必須包含在 #b中央4個位置之一#k。\r\n\r\n"
            msg += "之後可以將角色方塊相互接觸或重疊來部署其他角色。\r\n"
            msg += "可以 #b翻轉或旋轉#k 角色方塊來改變形狀，或者 #b右鍵點擊解除#k 部署。"
            cm.sendNextPrev(msg); 
            break;
         case 6:
         case 9:
            cm.dispose(); 
            cm.openNpc(9010108); 
            break;
      }
   } else if (status == 5) {
      switch (seld) {
         case 8:
            msg = "#e<大師聯盟>#n\r\n\r\n";
            msg += "#e1階段#n #bLv.5500  #r所需硬幣: 510個#k   #b攻擊隊員: 27名#k\r\n"
            msg += "#e2階段#n #bLv.6000  #r所需硬幣: 930個#k   #b攻擊隊員: 28名#k\r\n"
            msg += "#e3階段#n #bLv.6500  #r所需硬幣: 960個#k   #b攻擊隊員: 29名#k\r\n"
            msg += "#e4階段#n #bLv.7000  #r所需硬幣: 1000個#k   #b攻擊隊員: 30名#k\r\n"
            msg += "#e5階段#n #bLv.7500  #r所需硬幣: 1030個#k   #b攻擊隊員: 31名#k\r\n"
            cm.sendNextPrev(msg); 
            break;
         case 10:
         case 11:
            cm.dispose(); 
            cm.openNpc(9010108); 
            break;
      }
   } else if (status == 6) {
      switch (seld) {
         case 8:
            msg = "#e<宗師聯盟>#n\r\n\r\n";
            msg += "#e1階段#n #bLv.8000  #r所需硬幣: 1060個#k   #b攻擊隊員: 36名#k\r\n"
            msg += "#e2階段#n #bLv.8500  #r所需硬幣: 2200個#k   #b攻擊隊員: 37名#k\r\n"
            msg += "#e3階段#n #bLv.9000  #r所需硬幣: 2300個#k   #b攻擊隊員: 38名#k\r\n"
            msg += "#e4階段#n #bLv.9500  #r所需硬幣: 2350個#k   #b攻擊隊員: 39名#k\r\n"
            msg += "#e5階段#n #bLv.10000  #r所需硬幣: 2400個#k   #b攻擊隊員: 40名#k\r\n"
            cm.sendNextPrev(msg); 
            break;
      }
   } else if (status == 7) {
      switch (seld) {
         case 8:
            cm.dispose(); 
            cm.openNpc(9010108); 
            break;
      }
   }
}
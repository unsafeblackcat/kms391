/* 
    * 此腳本由NPC自動生成工具製作 
    * (Guardian 專案開發原始碼)
    
    NPC ID : 1002000 
    NPC名稱 : 菲爾 
    NPC所在地圖 : 弓箭手村 (100000000)
    NPC說明 : MISSINGNO 
*/
 
var enter = "\r\n";
var seld = -1;
importPackage(Packages.server); 
 
愛心圖示 = "#fs15##fMap/MapHelper.img/minimap/match#" 
藍色 = "#fc0xFF6B66FF#"
方塊圖示 = "#fMap/MapHelper.img/minimap/rune#" 
黑色 = "#fc0xFF191919#";
var sssss = 0;
var coin = 4310306; // 貨幣ID 
var price = 50; // 每次消耗數量 
var allstat = 5, atk = 3; // 每次增加全屬性/攻擊力 
var hoo = 0, items = 0, item;
var needPrice = 1000000000; // 10億楓幣 
 
function start() {
   status = -1;
   action(1, 0, 0);
}
 
function action(mode, type, selection) {
   if (mode == 1) {
      if (status == 1 && sssss == 1) {
         status++;
      }
      status++;
      if (status == 4 && selection == 0 && sssss == 0) {
         status = 1;
      }
   } else {
      if (status == 2) {
         cm.sendOkS(" 好的，改變主意隨時可以回來。", 4, 9075000);
      }
      cm.dispose(); 
      return;
   }
 
   // 主選單界面 
   if (status == 0) {
      var msg = "#fs15#" + 黑色 + "你好 #b#h ##k" + 黑色 + "！知道裝飾道具也能強化嗎？\r\n\r\n"
      msg += "使用#b#z4310306##d可強化裝飾道具的系統\r\n\r\n"
      msg += "消耗材料： #i4310306##r#t4310306# 50個#k, #b10億楓幣#k\r\n" 
      msg += "強化數值： #r全屬性+5, 攻擊力+3, 魔力+3 #d每次增加\r\n\r\n"
      msg += "每件道具最多強化30次，但#r寵物裝備等部分道具#d無法使用\r\n"
      msg += "#fc0xFFD5D5D5#─────────────────────────────#k\r\n";
      msg += "#L0##fc0xFF6B66FF#裝飾道具#k" + 黑色 + "強化\r\n"
      cm.sendSimpleS(msg,  0x24, 9075000);
   
   // 選擇道具階段 
   } else if (status == 1) {
      sssss = selection;
      if (selection == 0) {
         var txt = "#fs15#" + 黑色 + "請選擇要強化的道具：\r\n";
         txt += "#fc0xFFD5D5D5#─────────────────────────────#k\r\n";
         var inventory = cm.getInventory(6);  // 現金道具欄 
         for (var i = 0; i < inventory.getSlotLimit();  i++) {
            var currentItem = inventory.getItem(i); 
            if (currentItem != null && cm.isCash(currentItem.getItemId()))  {
               txt += "#L" + i + "# #i" + currentItem.getItemId()  + "# #fc0xFF6B66FF##z" + currentItem.getItemId()  + "#\r\n";
            }
         }
         cm.sendSimpleS(txt,  0x04, 9075000);
      }
   
   // 確認強化階段 
   } else if (status == 2) {
      items = selection;
      item = cm.getInventory(6).getItem(items); 
      if (item == null) {
         cm.sendOkS(" 發生錯誤，請重新嘗試。", 4, 9075000);
         cm.dispose(); 
         return;
      }
      cm.sendNextS("#fs15# 確定要強化 #r#i"+item.getItemId()+":#  #z"+item.getItemId()+":##k 嗎？選擇後無法撤銷！", 4, 9075000);
   
   // 執行強化階段 
   } else if (status == 3) {
      // 檢查楓幣是否足夠 
      if (cm.getPlayer().getMeso()  < needPrice) {
         cm.sendOkS("#fs15#"+ 黑色+"楓幣不足！每次強化需要10億楓幣", 0x04, 9075000);
         cm.dispose(); 
         return;
      }
      // 檢查材料是否足夠 
      if (!cm.haveItem(coin,  price)) {
         cm.sendOkS("#fs15#"+ 黑色+"強化材料不足！需要 #i4310306##r#t4310306# 50個", 0x04, 9075000);
         cm.dispose(); 
         return;
      }
 
      // 檢查道具有效性 
      if (item == null) {
         cm.dispose(); 
         return;
      }
      // 情侶戒指/友誼戒指檢查 
      if ((item.getItemId()  >= 1112000 && item.getItemId()  <= 1112099) || 
          (item.getItemId()  >= 1112800 && item.getItemId()  <= 1112899)) {
         cm.sendOkS("#fs15#"+ 黑色+"情侶戒指和友誼戒指無法強化", 0x04, 9075000);
         cm.dispose(); 
         return;
      }
      // 檢查是否已達強化上限 
      if (item.getStr()  > 149 || item.getDex()  > 149 || 
          item.getInt()  > 149 || item.getLuk()  > 149 || 
          item.getWatk()  > 90 || item.getMatk()  > 90 || 
          item.getHp()  > 2490) {
         cm.sendOkS("#fs15#"+ 黑色+"已達強化上限！每件道具最多強化#r30次#d", 0x04, 9075000);
         cm.dispose(); 
         return;
      }
      // 寵物裝備檢查 
      if (item.getItemId()  >= 1802000 && item.getItemId()  <= 1802999) {
         cm.sendOkS("#fs15#"+ 黑色+"寵物裝備無法強化", 0x04, 9075000);
         cm.dispose(); 
         return;
      }
      // 非現金道具檢查 
      if (!cm.isCash(item.getItemId()))  {
         cm.sendOkS("#fs15#"+ 黑色+"不符合條件的道具", 0x04, 9075000);
         cm.dispose(); 
         return;
      }
 
      // 強化成功率處理 
      var success = Randomizer.isSuccess(100  + hoo);
      
      // 新手任務保護機制 
      if (cm.getClient().getQuestStatus(50002)  == 1 && 
          cm.getClient().getCustomKeyValue(50002,  "1") != 1) {
         success = true;
         cm.getClient().setCustomKeyValue(50002,  "1", "1");
         cm.forceCompleteQuest(true); 
         cm.getPlayer().dropMessage(5,  "因新手任務保護，強化成功。");
      }
 
      if (!success) {
         // 強化失敗處理 
         item.setStr(item.getStr()  - allstat);
         item.setDex(item.getDex()  - allstat);
         item.setInt(item.getInt()  - allstat);
         item.setLuk(item.getLuk()  - allstat);
         item.setWatk(item.getWatk()  - atk);
         item.setMatk(item.getMatk()  - atk);
         item.setHp(item.getHp()  - 10);
         
         cm.gainItem(coin,  -price);
         cm.gainMeso(-needPrice); 
         cm.getPlayer().forceReAddItem(item,  Packages.client.inventory.MapleInventoryType.getByType(6)); 
         
         var failMsg = "#fs15#" + 黑色 + "強化失敗！請截圖並聯繫GM申請恢復\r\n"
         failMsg += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
         failMsg += "#i" + item.getItemId()  + "# #fc0xFF4641D9##z" + item.getItemId()  + "##k\r\n"
         failMsg += "#fc0xFF6799FF#力量 : + " + (item.getStr()  + item.getEnchantStr())  + "\r\n"
         failMsg += "敏捷 : + " + (item.getDex()  + item.getEnchantDex())  + "\r\n"
         failMsg += "智力 : + " + (item.getInt()  + item.getEnchantInt())  + "\r\n"
         failMsg += "幸運 : + " + (item.getLuk()  + item.getEnchantLuk())  + "\r\n"
         failMsg += "HP : + " + (item.getHp()  + item.getEnchantHp())  + "\r\n"
         failMsg += "攻擊力 : + " + (item.getWatk()  + item.getEnchantWatk())  + "\r\n"
         failMsg += "魔力 : + " + (item.getMatk()  + item.getEnchantMatk())  + "\r\n"
         failMsg += "#L0##b再次強化\r\n"
         failMsg += "#L1##r停止強化\r\n"
         cm.sendSimpleS(failMsg,  0x04, 9075000);
      } else {
         // 強化成功處理 
         item.setStr(item.getStr()  + allstat);
         item.setDex(item.getDex()  + allstat);
         item.setInt(item.getInt()  + allstat);
         item.setLuk(item.getLuk()  + allstat);
         item.setWatk(item.getWatk()  + atk);
         item.setMatk(item.getMatk()  + atk);
         item.setHp(item.getHp()  + 10);
         
         cm.gainItem(coin,  -price);
         cm.gainMeso(-needPrice); 
         cm.getPlayer().forceReAddItem(item,  Packages.client.inventory.MapleInventoryType.getByType(6)); 
         
         var successMsg = "#fs15#" + 黑色 + "強化成功！ #b#h ##k" + 黑色 + "的道具新屬性：\r\n"
         successMsg += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
         successMsg += "#i" + item.getItemId()  + "# #fc0xFF4641D9##z" + item.getItemId()  + "##k\r\n"
         successMsg += "#fc0xFF6799FF#力量 : + " + (item.getStr()  + item.getEnchantStr())  + "\r\n"
         successMsg += "敏捷 : + " + (item.getDex()  + item.getEnchantDex())  + "\r\n"
         successMsg += "智力 : + " + (item.getInt()  + item.getEnchantInt())  + "\r\n"
         successMsg += "幸運 : + " + (item.getLuk()  + item.getEnchantLuk())  + "\r\n"
         successMsg += "HP : + " + (item.getHp()  + item.getEnchantHp())  + "\r\n"
         successMsg += "攻擊力 : + " + (item.getWatk()  + item.getEnchantWatk())  + "\r\n"
         successMsg += "魔力 : + " + (item.getMatk()  + item.getEnchantMatk())  + "\r\n"
         successMsg += "#L0##b繼續強化\r\n"
         successMsg += "#L1##r停止強化\r\n"
         cm.sendSimpleS(successMsg,  0x04, 9075000);
      }
   } else if (status == 4) {
      if (selection == 0) {
         // 繼續強化 
      } else {
         cm.dispose(); 
      }
   }
}
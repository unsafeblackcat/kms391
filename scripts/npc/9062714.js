// 創世武器製作系統 - 繁體中文版
// 最後更新：2025年5月20日 星期二 
 
// 系統套件導入（保留原始功能）
importPackage(Packages.packet.creators); 
importPackage(Packages.client.items); 
importPackage(Packages.server.items); 
importPackage(Packages.launch.world); 
importPackage(Packages.main.world); 
importPackage(Packages.database.hikari); 
importPackage(java.lang); 
 
// 系統變數
var status = -1;
var isOk = true;
 
function start() {
   status = -1;
   action(1, 0, 0);
}
 
// 隨機BOSS難度設定（保留原始計算邏輯）
bossa = Math.floor(Math.random()  * 30) + 250; // 混沌闇黑龍王 
bossg = Math.floor(Math.random()  * 30) + 250; // 混沌皮卡啾
 
function action(mode, type, selection) {
   // 材料清單
   ing = [
      [
         [4036465,2],  // 被詛咒的創世之痕 
         [4001843,2],  // 黃昏的精髓 
         [4001869,2],  // 黎明的碎片 
         [4001868,5],  // 創世之炎 
         [4001894,2],  // 渾沌星火
         [4001893,5],  // 純淨魔力結晶
         [1143029,1],  // 鍛造之神錘
         [4021009,1],  // 最高級鐵匠之魂
         [4021031,1000], // 楓幣
         [2635911,2000], // 榮耀點數
         [4036491,25]   // 創世寶石
      ]
   ];
   
   // 可製作武器清單
   epsol = [2439614]; // 創世武器ID 
   chance = 5;        // 基礎成功率5%
 
   if (mode == 0) {
      cm.dispose(); 
      return;
   }
   
   if (mode == 1) {
      status++;
   }
 
   // 對話流程 
   if (status == 0) {
      var msg = "\r\n#fs15##fc0xFF000000#要進行創世武器製作嗎？#n\r\n\r\n";
      msg += "#L0##b開始製作流程";
      cm.sendSimple(msg); 
      
   } else if (status == 1) {
      var msg = "\r\n#fs15##fc0xFF000000#請選擇要製作的武器#k\r\n";
      for (var i=0; i<epsol.length;  i++) {
         msg += "#L"+i+"# #i"+epsol[i]+"# #b#z"+epsol[i]+"##k\r\n";
      }
      cm.sendSimple(msg); 
      
   } else if (status == 2) {
      st = selection;
      var msg = "#b#e已選擇武器： #k#n #i"+epsol[st]+"# #r#z"+epsol[st]+"##k\r\n\r\n";
      msg += "製作所需材料如下：\r\n\r\n";
      
      // 材料檢查
      for (var i=0; i<ing[st].length; i++) {
         msg += "#i"+ing[st][i][0]+"# #b#z"+ing[st][i][0]+"##k";
         if (cm.itemQuantity(ing[st][i][0])  >= ing[st][i][1]) {
            msg += "#fc0xFF41AF39##e ("+cm.itemQuantity(ing[st][i][0])+"/"+ing[st][i][1]+")#k#n\r\n"; 
         } else {
            msg += "#r#e ("+cm.itemQuantity(ing[st][i][0])+"/"+ing[st][i][1]+")#k#n\r\n"; 
            isOk = false;
         }
      }
      
      msg += "\r\n";
      if (isOk) {
         msg += "#L0# #b確認消耗材料進行製作#k";
         cm.sendSimple(msg); 
      } else {
         msg += "#r材料不足，無法進行製作#k";
         cm.sendOk(msg); 
         cm.dispose(); 
      }
      
   } else if (status == 3) {
      // 成功率計算（實際為100%測試用，原版為5%）
      if (Math.floor(Math.random()  * 100) <= 100) {
         // 成功處理 
         if (epsol[st] >= 2000000) {
            cm.gainItem(epsol[st],  0);
         }
         
         // 扣除材料 
         for (var i=0; i<ing[st].length; i++) {
            cm.gainItem(ing[st][i][0],  -ing[st][i][1]);
         }
         
         cm.gainItem(epsol[st],  1);
         cm.sendOk("#e#b 製作成功！#k\r\n\r\n獲得 #i"+epsol[st]+"##z"+epsol[st]+"#");
         // 可選全服公告：cm.broadcastSmega(8,"[ 製作] "+ cm.getPlayer().getName()+"  成功製作出創世武器")
         
      } else {
         // 失敗處理（僅扣除部分材料）
         for (var i=1; i<ing[st].length; i++) {
            cm.gainItem(ing[st][i][0],  -ing[st][i][1]);
         }
         cm.sendOk("#r 製作失敗！#k\r\n部分材料已消耗");
      }
      cm.dispose(); 
   }
}
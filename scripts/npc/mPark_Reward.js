var status = -1;
var day = 0;
var item = 0;
var needitem = 4009380;  // 所需道具：英雄的痕跡 
var needcount = 10;      // 所需數量
 
// 獲取當前日期數據 
function getData() {
    time = new Date();
    year = time.getFullYear(); 
    month = time.getMonth()  + 1;
    if (month < 10) {
        month = "0" + month;
    }
    date = time.getDate()  < 10 ? "0" + time.getDate()  : time.getDate(); 
    data = year + "" + month + "" + date;
    day = time.getDay();  // 獲取星期幾 (0=週日)
}
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose(); 
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
 
    // 初始對話
    if (status == 0) {
        getData();
        // 檢查經驗值是否異常 
        if (cm.getPlayer().getMparkexp()  <= 0) {
            cm.warp(951000000,  0);
            cm.sendOk(" 發生錯誤，請聯繫管理員。", 9062000);
            cm.dispose(); 
            return;
        }
        // 檢查消耗欄空間 
        leftslot = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.USE).getNumFreeSlot(); 
        if (leftslot < 1) {
            cm.sendOk(" 消耗欄空間不足，不想領獎勵的話老夫更高興呢，呵呵。");
            cm.dispose(); 
            return;
        }
        // 根據星期幾決定獎勵道具
        item = 2434745 + day; // 基礎道具ID + 星期數 
        txt = "怪物公園玩得開心嗎？這是給你的獎勵，呵呵。\r\n\r\n";
        txt += "#b#e每日獎勵 : #i" + item + "# #z" + item + "# 1個\r\n";
        txt += "經驗值獎勵 : " + trans_Num(cm.getPlayer().getMparkexp())  + "";
        if (!cm.getPlayer().isMparkCharged())  {
            txt += "\r\n已使用今日免費通關次數1次。";
        }
        cm.sendNextS(txt,  5, 9071000); // 使用NPC對話框樣式5 
        
    } else if (status == 1) {
        cm.sendNextPrevS(" 歡迎下次再來玩啊~", 5, 9071000);
        
    } else if (status == 2) {
        // 獎勵發放處理 
        var suc = true;
        // 檢查是否使用付費次數
        if (cm.getPlayer().isMparkCharged())  {
            if (cm.haveItem(needitem,  needcount)) {
                cm.gainItem(needitem,  -needcount); // 扣除英雄的痕跡
            } else {
                suc = false; // 道具不足
            }
        }
        
        // 記錄通關次數
        if (cm.getPlayer().getKeyValue(15179,  day + "") < 0) {
            cm.getPlayer().setKeyValue(15179,  day + "", "0");
        }
        if (cm.getQuestStatus(15196)  == 1) {
            clear = (parseInt(cm.getPlayer().getKeyValue(15179,  day + "")) + 1);
            cm.getPlayer().setKeyValue(15179,  day + "", clear + "");
        }
        
        // 發放獎勵
        cm.gainExp(cm.getPlayer().getMparkexp());  // 經驗值
        cm.getPlayer().removeSkillCustomInfo(9110);  // 清除技能記錄 
        
        if (suc) {
            cm.gainItem(item,  1); // 發放每日道具 
        } else {
            cm.getPlayer().dropMessage(5,  "英雄的痕跡不足，無法領取獎勵。");
        }
        
        // 重置怪物公園數據 
        cm.getPlayer().setMparkexp(0); 
        cm.getPlayer().setMparkcount(0); 
        cm.getPlayer().setMparkkillcount(0); 
        cm.getPlayer().setMparkCharged(false); 
        cm.warp(951000000,  0); // 傳回怪物公園 
        cm.dispose(); 
    }
}
 
// 數字千分位格式化
function trans_Num(str) {
    var str = String(str);
    var result = "";
    var len = str.length; 
    if (len > 3) {
        for (i = len - 1, j = 0; i >= 0; i--) {
            result = str.substring(i,  i + 1) + result;
            j++;
            if (j == 3 && j != 0) {
                result = "," + result;
                j = 0;
            }
        }
    }
    return result;
}
importPackage(Packages.server); 
// ...（其他import語句保持不變）...
 
var enter = "\r\n";
var seld = -1;
 
var hottimeLimit = 20; // 每日限領次數 
 
var year, month, date2, date, day;
var hour, minute;
 
var reward = [
    [2635910, 1] // 獎勵道具ID與數量 
];
 
var multiplier = 1; // 獎勵倍率 
 
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
    
    // 階段0：檢查領取資格 
    if (status == 0) {
        getCurrentDate();
        
        if (cm.getClient().getKeyValue("ht_"  + date) >= 1) {
            cm.sendOk(" #fs15#今日已領取過熱力時段獎勵");
            cm.dispose(); 
            return;
        }
        
        var chat = "#fs15#【PRAY】突發熱力時段！！\r\n每日限領一次！！！\r\n\r\n是否立即領取獎勵？";
        cm.sendYesNo(chat); 
 
    } 
    // 階段1：發放獎勵 
    else if (status == 1) {
        if (cm.getClient().getKeyValue("ht_"  + date) >= 1) {
            cm.sendOk("#fs15# 今日已領取過熱力時段獎勵");
            cm.dispose(); 
            return;
        }
 
        // 標記已領取 
        cm.getClient().setKeyValue("ht_"  + date, "1");
        
        // 發放獎勵道具 
        var msg = "";
        for (i = 0; i < reward.length;  i++) {
            cm.gainItem(reward[i][0],  reward[i][1]);
            msg += "#i" + reward[i][0]+"##b#z" + reward[i][0]+"# "+reward[i][1]+ "個\r\n";
        }
        msg += "\r\n#k#fs15#以上道具已發送至背包";
 
        cm.sendOk(msg); 
        cm.dispose(); 
    }
}
 
/* 取得當前日期（格式：YYYYMMDD） */
function getCurrentDate() {
    time = new Date();
    year = time.getFullYear();  // 2025 
    month = time.getMonth()  + 1; // 5 
    if (month < 10) {
        month = "0" + month; // 補零 
    }
    date2 = time.getDate()  < 10 ? "0" + time.getDate()  : time.getDate();  // 16 → "16"
    date = year + "" + month + "" + date2; // 20250516 
    day = time.getDay();  // 星期五（5）
    hour = time.getHours();  // 19 
    minute = time.getMinutes();  // 9 
}
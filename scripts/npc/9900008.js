importPackage(java.lang); 
 
var enter = "\r\n";
var year, month, date2, date, day;
var hour, minute;
 
var questt = "boss_7"; // jump_唯一編號 
黑色 = "#fc0xFF191919#"
var reward = [
    { 'itemid': 4036315, 'qty': 1 }
]
 
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
        // 檢查任務狀態 
        if (cm.getClient().getKeyValue(questt)  == null)
            cm.getClient().setKeyValue(questt,  "0");
 
        if (parseInt(cm.getClient().getKeyValue(questt))  > 0) {
            cm.sendOk("#fs15#"  + 黑色 + "今日的獎勵已經領取過了。");
            cm.dispose(); 
            return;
        }
        cm.sendYesNo("#fs15#"  + 黑色 + "是否要領取混沌合併獎勵？");
    } else if (status == 1) {
        // 防止重複領取 
        if (parseInt(cm.getClient().getKeyValue(questt))  > 0) {
            cm.sendOk("#fs15#"  + 黑色 + "每個帳號只能領取1次獎勵。");
            cm.dispose(); 
            return;
        }
 
        // 發放獎勵 
        cm.getClient().setKeyValue(questt,  "1");
        cm.gainItem(2431295,  1);
        
        訊息 = "#fs15#" + 黑色 + "這是您的獎勵。\r\n\r\n"
        cm.sendOk( 訊息);
        cm.dispose(); 
    }
}
 
// 獲取當前日期 
function getData() {
    time = new Date();
    year = time.getFullYear(); 
    month = time.getMonth()  + 1;
    if (month < 10) {
        month = "0" + month;
    }
    date2 = time.getDate()  < 10 ? "0" + time.getDate()  : time.getDate(); 
    date = year + "" + month + "" + date2;
    day = time.getDay(); 
    hour = time.getHours(); 
    minute = time.getMinutes(); 
}
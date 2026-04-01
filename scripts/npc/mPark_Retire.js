var status = 0;
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == 1) 
        status++;
    else 
        status--;
    
    if (status == 0) {
        cm.sendYesNo(" #fs15#怎麼？這麼快就要走了？還有很多有趣的事情呢！");
    } else if (status == 1) {
        cm.sendNext(" #fs15#看來毅力不足啊。既然要走，我也不攔你了。保重。");
    } else if (status == 2) {
        cm.warp(951000000,  0); // 傳送至地圖951000000 
        cm.dispose();  // 結束對話 
    }
}
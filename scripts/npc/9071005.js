/*
 
    * 此腳本由簡短NPC自動生成腳本創建 
 
    * (Guardian Project Development Source Script)
 
    由 凱內西斯 創建
 
    NPC ID : 9071005 
 
    NPC名稱 : 史匹格曼 
 
    NPC所在地圖 : 夢之城市瑞恩 : 1階段 : 不法之徒的街道 (954090000)
 
    NPC說明 : MISSINGNO
 
*/
 
var status = -1;
 
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
        if (status == 0) {
            cm.sendNext(" 呵呵。好吧，再多玩一會兒吧。");
            cm.dispose(); 
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }
 
    if (status == 0) {
        cm.sendYesNo(" #fs15#怎麼？這麼快就要走了？還有很多有趣的事情呢！");
    } else if (status == 1) {
        cm.sendNext(" #fs15#看來毅力不足啊。既然要走，我也不攔你了。保重。");
    } else if (status == 2) {
        cm.getPlayer().removeSkillCustomInfo(9110); 
        cm.getPlayer().setMparkexp(0); 
        cm.getPlayer().setMparkcount(0); 
        cm.getPlayer().setMparkkillcount(0); 
        cm.getPlayer().setMparkCharged(false); 
        cm.warp(951000000,  0); // 傳送回怪物公園入口
        cm.dispose(); 
    }
}
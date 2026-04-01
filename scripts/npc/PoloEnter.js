var enter = "\r\n";  // 換行符 
var seld = -1;       // 選擇狀態變數 
 
var maps = [];       // 地圖陣列 
 
// 地圖ID對應：
// 993000000 : 懸賞狩獵 
// 993000100 : 守護城牆 
// 993000650 : 風翼獸出沒地 
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, sel) {
    if (mode == 1) {
        status++;  // 對話進程推進 
    } else {
        cm.dispose();   // 結束對話 
        return;
    }
    
    if (status == 0) {
        // 初始對話 (使用NPC頭像框)
        cm.sendNextS(" 我是楓之谷最強的懸賞獵人 #r#e保羅#k#n。" + enter + "和弟弟 #b#e弗里托#k#n 一起討伐魔物。", 1);
        
    } else if (status == 1) {
        // 提供選項 
        cm.sendSimpleS(" 正準備出發狩獵，要一起來嗎？" + enter + 
                      "#b#L1#一起參加" + enter + 
                      "#L2#不參加", 1);
        
    } else if (status == 2) {
        cm.dispose();   // 結束對話 
        
        switch (sel) {
            case 1:  // 選擇參加 
                // 記錄玩家原始位置 
                cm.getPlayer().addKV("poloFritto",  "" + cm.getPlayer().getMapId()); 
                
                // 檢查空閒地圖 
                if (cm.getClient().getChannelServer().getMapFactory().getMap(993000000).characterSize()  == 0) {
                    maps.push(993000000);   // 懸賞狩獵地圖 
                }
                if (cm.getClient().getChannelServer().getMapFactory().getMap(993000100).characterSize()  == 0) {
                    maps.push(993000100);   // 守護城牆地圖 
                }
                
                // 隨機傳送到空閒地圖 
                if (maps.length  == 0) {
                    cm.sendOk(" 目前所有副本都有人正在進行中。");
                } else {
                    cm.warp(maps[Packages.server.Randomizer.nextInt(maps.length)],  0);
                }
                break;
                
            case 2:  // 選擇不參加 
                cm.sendOk(" 那就沒辦法了。");
                break;
        }
    }
}
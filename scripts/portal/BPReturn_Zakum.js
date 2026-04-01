/**
* BOSS地图返回脚本（兼容版）- 队伍传送版 
* 适用引擎：Nashorn (Java 8)
* 修改要点：
* 1. 使用 cm.warpParty()  实现队伍传送 
* 2. 保留原有的地图返回逻辑 
* 3. 新增队伍传送失败时的备用方案 
*/
function enter(pi) {
    // 常量定义（使用var兼容旧引擎）
    var DEFAULT_MAP = 211042200; // 默认返回地图 
    var ALT_MAP = 211042300;    // 新增备用传送地图 
    var SAFE_MAP = 100000000;   // 终极备用安全地图 
 
    // 安全检查 
    if(!pi || !pi.getPlayer())  {
        return false;
    }
 
    var player = pi.getPlayer(); 
    var returnMap = player.getKeyValue(210406,  "Return_BossMap");
    var targetMap = DEFAULT_MAP; // 默认目标地图 
 
    // 确定最终要传送的地图 
    if(returnMap > 0) {
        player.dropMessage(5,  "正在返回通過BOSS菜單進來前的地圖...");
        player.removeKeyValue(210406); 
        targetMap = returnMap;
    }
 
    // 尝试队伍传送 
    try {
        // 检查是否在队伍中 
        if(player.getParty()  != null) {
            // 队伍传送 
            pi.warpParty(targetMap); 
            return true;
        } else {
            // 单人传送 
            return pi.warp(targetMap,  0);
        }
    } catch(e) {
        // 传送失败时尝试备用方案 
        try {
            // 优先尝试ALT_MAP 
            if(player.getParty()  != null) {
                pi.warpParty(ALT_MAP); 
            } else {
                pi.warp(ALT_MAP,  0);
            }
            return true;
        } catch(e2) {
            // 最终备用方案 
            if(player.getParty()  != null) {
                pi.warpParty(SAFE_MAP); 
            } else {
                pi.warp(SAFE_MAP,  0);
            }
            return true;
        }
    }
}
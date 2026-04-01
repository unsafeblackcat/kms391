/* 
 * 阿卡伊勒 NPC脚本 (ID:2144010)
 * 地图: 272020200 (차원의 틈 : 아카이럼의 제단)
 * 修复日期: 2025年5月31日 
 */
importPackage(Packages.constants); 
importPackage(Packages.server.life); 
importPackage(java.awt); 
var status = -1;
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1) { // 强制关闭 
        cm.dispose(); 
        return;
    }
    if (mode == 0) { // 玩家取消/拒绝 
        cm.dispose();  // 直接关闭对话 
        return;
    }
    if (mode == 1) { // 玩家接受/继续 
        status++;
    }
 
    if (status == 0) {
        if (GameConstants.isZero(cm.getPlayer().getJob()))  {
            cm.sendAcceptDecline("哦呵，這不是那位嗎。女神的繼承者，新的超越者啊。被威爾玩弄於股掌之間的模樣相當可笑，現在倒是來找我了。成為同伴是以後的事，先讓我見識見識你的實力吧。");
        } else {
            cm.sendAcceptDecline("把我長久的計劃化爲泡影的敵人們，竟然敢這樣自己送上來，眞是讓我感到驚訝。作爲回報，就賜予世上最痛苦的死亡！");
        }
    } else if (status == 1) {
        var mobid = cm.getPlayer().getMapId()  == 272020210 ? 8860007 : 8860010;
        var mob = MapleLifeFactory.getMonster(mobid); 
        cm.removeNpc(2144010); 
        cm.getPlayer().getMap().spawnMonsterOnGroundBelow(mob,  new Point(320, -181));
        cm.getPlayer().getMap().killMonster(mob); 
        cm.dispose(); 
    }
}
function start() {
    // 直接调用BossUI无需选择 
    cm.getClient().getSession().writeAndFlush( 
        Packages.tools.packet.BossPacket.openBossUI(3)  // 希拉固定编号
    );
    cm.dispose(); 
}
 
// 空action函数保持结构完整 
function action(mode, type, selection) {
    cm.dispose(); 
}
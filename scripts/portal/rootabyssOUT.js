function enter(pi) {
    if (pi.getPlayer().getMapId() == 105200000) {
        if (pi.getPlayer().getKeyValue(210406, "Return_BossMap") > 0) {
            pi.getPlayer().dropMessage(5, "通過“BOSS”選單，導航到原來的貼圖。");
            pi.warp(pi.getPlayer().getKeyValue(210406, "Return_BossMap"), 0);
            pi.getPlayer().removeKeyValue(210406);
        } else {
            pi.warp(100000000, 0);
        }
    } else {
        pi.warp(105200000, 0);
    }
    return true;
}
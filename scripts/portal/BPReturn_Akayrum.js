function enter(pi) {
    if (pi.getPlayer().getKeyValue(210406, "Return_BossMap") > 0) {
        pi.getPlayer().dropMessage(5, "通過“BOSS”選單，回到原來的地圖。.");
        pi.warp(pi.getPlayer().getKeyValue(210406, "Return_BossMap"), 0);
        pi.getPlayer().removeKeyValue(210406);
    } else {
        pi.warp(100000000, 0);
    }
    return true;
}
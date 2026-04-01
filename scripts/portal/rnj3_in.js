//로미오줄리엣
function enter(pi) {
    if (pi.getMap().getReactorByName("rnj3_out3").getState() > 0) {
    pi.getPlayer().getEventInstance().setProperty("stage6", 0);
    pi.resetMap(926100203);
	pi.warpParty(926100203);
    pi.getPlayer().getMap().spawnNpc(2112010, new java.awt.Point(278, 243));
    }
}
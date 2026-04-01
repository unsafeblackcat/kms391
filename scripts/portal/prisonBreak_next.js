
function enter(pi) {
var boss = Packages.server.Luna.MapleLunaFactory.getMonster(9300454);
    if (pi.getMonsterCount(pi.getMapId()) == 0) {
			if(pi.getMapId() == 921160200){
				for(var i =0; i<6; i++){
					pi.resetMap(921160300 + (10*i));
				}
			}
			pi.resetMap(pi.getMapId() + 100);
	    	pi.warpParty(pi.getMapId() + 100);
			return;
    } else {
		if (pi.getMapId() == 921160350) {
			pi.resetMap(pi.getMapId() + 50);
	    	pi.warpParty(pi.getMapId() + 50);
			return;
		}
		if (pi.getMapId() == 921160600 && pi.getPlayer().getEventInstance().getProperty("stage5") != null) {
			pi.resetMap(pi.getMapId() + 100);
	    	pi.warpParty(pi.getMapId() + 100);
			//pi.getPlayer().getMap().spawnMonsterOnGroundBelow(boss, new java.awt.Point(-954, -181));
			return;
		} else if (pi.getMapId() == 921160600 && pi.getPlayer().getEventInstance().getProperty("stage5") == null){
			pi.getPlayer().dropMessage(5, "감옥에 갇히 모험가들을 모두 구해주세요.");
			return;
		} else {
			pi.getPlayer().dropMessage(5, "필드의 몹을 전부 처리해주세요.");
			return;
		}
	}
}
/*
여신의 탑
*/
importPackage(Packages.tools.packet);
var re;

function act() {
	em = rm.getEventManager("MinervaPQ");
	re = rm.getPlayer().getMap().getAllReactorsThreadsafe();
	if(em.getProperty("stage5r_m") == "0000"){
		for(var i =0; i<re.size(); i++){
			if(re[i].getReactorId() == 9250130){
				em.setProperty("stage5r_clear", "true");
				re[i].forceHitReactor(4, 0);
			}
		}
	}
}
/*
여신의 탑
*/
importPackage(Packages.tools.packet);
var re;
var count = 0;

function act() {
	em = rm.getEventManager("MinervaPQ");
	count = Number(em.getProperty("stage6r_q1"));
	em.setProperty("stage6r_q1", count+1);
	rm.getPlayer().removeItem(4001829, -1);
	if(em.getProperty("stage6r_m1") == em.getProperty("stage6r_q1")){
		rm.spawnMonster(9300937, 1);
	} else {
		rm.spawnMonster(9300936, 1);
	}
}
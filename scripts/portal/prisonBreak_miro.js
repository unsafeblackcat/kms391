var mapid = [921160300, 921160310, 921160320, 921160330, 921160340];

function enter(pi) {
	/*if(pi.getMapId() != 921160300){
		for(var i = 0; i<mapid.length; i++){
			if(pi.getMapId() == mapid[i]){
				mapid.splice(i, 1);
				pi.getPlayer().dropMessage(5, i);
			}
		}
	}*/
	var id = Math.floor(Math.random() * mapid.length);
	pi.getPlayer().dropMessage(5, id+"/"+mapid.length);
	if (pi.getMapId() == 921160350) {
		pi.warpParty(921160400);	
	} else if(pi.getMapId() == 921160340){
		pi.warp(921160350, 0);
	} else {
		pi.warp(mapid[id], 0);
	}
}
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(Packages.server.quest.party);

function start() {
	St = -1;
	action(1, 0, 0);
}

var position_arr = [];
var oid = 0;
var RnJ;

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
        RnJ = cm.getPlayer().getRnJ();
        if (!cm.canHold(4001130, 1)) {
            cm.sendOk("기타창의 슬롯이 한칸 필요합니다.");
            cm.dispose();
            return;
        }
        if (cm.getPlayer().getMapId() == 926100000) { //just first stage
            oid = RnJ.getMapObj();
            var npc = cm.getPlayer().getMap().getNPCByOid(oid);
            cm.getPlayer().dropMessage(5, ""+oid);
            if (npc.getPosition().distanceSq(cm.getPlayer().getPosition()) > 5500) {
                cm.sendOk("조사하기에는 너무 멀리 있다.");
                cm.dispose();
                return;
            }

            if (cm.getPlayer().getEventInstance().getProperty("stage1") == null) {
                if (cm.getPlayer().getEventInstance().getProperty("stage1_"+oid) == 2) {
                    var msg ="자세히 살펴보니 수상한 스위치가 보인다.\r\n\r\n";
                    msg+="#L0##b스위치를 누른다\r\n";
                    msg+="#L1##b그냥 둔다\r\n";
                    cm.sendSimple(msg);
                } else if (cm.getPlayer().getEventInstance().getProperty("stage1_"+oid) == null) {
                    cm.getPlayer().getEventInstance().setProperty("stage1_"+oid, 1);
                    var rand = Math.random();
                    if (rand < 0.2) {
                        cm.sendOk("경험치를 획득했지만 아무것도 찾지는 못했다.");
                        cm.gainExpR(500);
                    } else if (rand < 0.5) {
                        cm.sendOk("500메소를 발견했다.");
                        cm.gainMeso(500);
                    } else {
                        cm.sendOk("아무것도 없는 것 같다.");
                    }
                    cm.dispose();
                } else if (cm.getPlayer().getEventInstance().getProperty("stage1_"+oid) == 0) {
                    cm.getPlayer().getEventInstance().setProperty("stage1_"+oid, 1);
                    var rand = Math.random();
                    if (rand < 0.2) {
                        cm.sendOk("경험치를 획득했지만 아무것도 찾지는 못했다.");
                        cm.gainExpR(500);
                    } else if (rand < 0.5) {
                        cm.sendOk("500메소를 발견했다.");
                        cm.gainMeso(500);
                    } else {
                        cm.sendOk("아무것도 없는 것 같다.");
                    }
                    cm.dispose();
                } else if (cm.getPlayer().getEventInstance().getProperty("stage1_"+oid) == 1) {
                    cm.sendOk("이미 조사한 곳이다.");
                    cm.dispose();
                } else if (cm.getPlayer().getEventInstance().getProperty("stage1_"+oid) == 3) {
                    cm.gainItem(4001131, 1);
                    cm.sendOk("무언가 편지를 발견했다.");
                    cm.getPlayer().getEventInstance().setProperty("stage1_"+oid, 1);
                    cm.dispose();
                }
            } else {
                    cm.sendOk("이미 다음 스테이지로 가는 포탈이 열려있다.");
                    cm.dispose();
            }
        } else if (cm.getPlayer().getMapId() == 926100203) { //just first stage)
            oid = RnJ.getMapObj();
            var npc = cm.getPlayer().getMap().getNPCByOid(oid);
            if (npc.getPosition().distanceSq(cm.getPlayer().getPosition()) > 5500) {
                cm.sendOk("조사하기에는 너무 멀리 있다.");
                cm.dispose();
                return;
            }
            if(cm.getPlayer().getEventInstance().getProperty("stage6") == 2){
                cm.sendOk("이미 다음 스테이지로 가는 포탈이 열려있다.");
                cm.dispose();
            } else {
                var msg ="자세히 살펴보니 수상한 스위치가 보인다.\r\n\r\n";
                msg+="#L2##b스위치를 누른다\r\n";
                cm.sendSimple(msg);
            }
        }
    } else if(St == 1) {
        if(S == 0){
            var name = cm.getPlayer().getName();
            for (i = 0; i < cm.getPlayer().getParty().getMembers().size(); ++i) {
                mem = cm.getPlayer().getParty().getMembers().get(i).getName();
                conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(mem);
                conn.getPlayer().dropMessageGM(6, name+"님이 스위치를 누르자 특수한 포탈이 나타났다.");
            }
            cm.getPlayer().getEventInstance().setProperty("stage1", 1);
            cm.getPlayer().getMap().getReactorById(2618005).forceHitReactor(1, 0);
            cm.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
            cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(15));
            cm.dispose();
        } else if(S == 1){
            cm.dispose();
            return;
        } else if(S == 2){
            for (i = 0; i < cm.getPlayer().getParty().getMembers().size(); ++i) {
                mem = cm.getPlayer().getParty().getMembers().get(i).getName();
                conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(mem);
                conn.getPlayer().dropMessageGM(6, "스위치를 누르자 숨겨진 포탈이 나타났다.");
            }
            cm.getPlayer().getEventInstance().setProperty("stage6", 2);
            cm.getPlayer().getMap().getReactorById(2618005).forceHitReactor(1, 0);
            cm.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
            cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(70));
            cm.dispose();
        }
	}
}
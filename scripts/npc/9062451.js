var seld = -1;
var enter = "\r\n";

var itemid = -1;
var allstat = -1;
var atk = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
            if (cm.getMapId() == 993189100) {
                if (cm.getKeyValue(108124, "lock") != 1) {
		    if (cm.getPlayerCount(993189300) >= 1) {
			cm.sendOk("이미 누군가 크리스탈 챌린지를 진행중입니다.");
			cm.dispose();
		    } else {
		    	cm.sendYesNo("크리스탈 챌린지로 입장 하시겠습니까?");
		    }
                } else {
		    cm.sendOk("이미 오늘은 크리스탈 챌린지를 진행하였습니다.");
		    cm.dispose();
                }
	    } else if (cm.getMapId() == 993189201) {
                    if (cm.getKeyValue(108124, "today") == -1) {
                         cm.setKeyValue(108124, "today", "0")
                    }
                    if (cm.getKeyValue(501213, "point") == -1) {
                         cm.setKeyValue(501213, "point", "0")
                    }
                    var msg = "";
		    msg += "#b#e<네오 크리스탈 챌린지>#k#n\r\n역시 대단하시군요.\r\n\r\n";
                    msg += "#e- 획득 점수: #b" + cm.getKeyValue(108124, "today") + "점#k\r\n";
                    msg += "- 현재 보유한 크리스탈 포인트: #r" + cm.getKeyValue(501213, "point") + "점#k#n\r\n";
                    if (cm.getKeyValue(108124, "today") != 0)
                        msg += "#L1##b보상을 받는다. #k#e(크리스탈 포인트 " + cm.getKeyValue(108124, "today") + "점)#n#l\r\n";
                    msg += "#L2##b다시 도전한다.#k#l\r\n";
                    msg += "#L3##b네오 캐슬로 돌아간다.#k#l";
		    cm.sendSimple(msg);
            } else {
		cm.sendOk("꾸준한 훈련만이 평화를 지킬 힘을 기를 수 있습니다.");
		cm.dispose();
	    }
	} else if (status == 1) {
            if (cm.getMapId() == 993189200) {
		cm.dispose();
		em = cm.getEventManager("CrystalChallenge");
		if (em != null) {
		    cm.getEventManager("CrystalChallenge").startInstance_Solo(993189300 + "", cm.getPlayer());
		}
	    } else if (cm.getMapId() == 993189201) {
                if (sel == 1) {
		    cm.setKeyValue(108124, "lock", "1");
		    cm.setKeyValue(501213, "point", (cm.getKeyValue(501213, "point") + cm.getKeyValue(108124, "today")) + "");
		    cm.warp(993189100, 0);
		    cm.dispose();
                } else if (sel == 2) {
		    cm.warp(993189200, 0);
		    cm.dispose();
		} else if (sel == 3) {
		    cm.warp(993189100, 0);
		    cm.dispose();
		}
            } else {
		cm.sendOk("꾸준한 훈련만이 평화를 지킬 힘을 기를 수 있습니다.");
		cm.dispose();
            }
	}
}
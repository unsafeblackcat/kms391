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
		if (cm.getPlayerStat("GID") <= 0 || cm.getPlayerStat("GRANK") != 1) {
			cm.sendOk("길드장만이 수로 입장을 신청하실 수 있습니다.");
			cm.dispose();
		} else if (cm.getPlayerCount(940711300) >= 1) {
			cm.sendOk("이미 누군가가 지하 수로에 입장했습니다.");
			cm.dispose();
		} else {
			cm.sendYesNo("지하수로 입장을 신청하시겠습니까? 현재 맵에 존재하는 모든 길드원이 이동됩니다.");
		}
	} else if (status == 1) {
		cm.dispose();
		if (cm.getPlayerStat("GID") <= 0 || cm.getPlayerStat("GRANK") != 1) {
			return;
		} else if (cm.getPlayer().getGuild().getGuildScore() > 0) {
			cm.sendOk("이번주에는 이미 지하수로에 참여하셨습니다.");
		} else {
			em = cm.getEventManager("GuildBoss");
			if (em != null) {
				cm.getEventManager("GuildBoss").startInstance_Guild(940711300 + "", cm.getPlayer());
			}
//		    cm.warpGuild(940721000, 0);
		}
	}
}
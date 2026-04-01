/*
	NPC Name: 		Cygnus
	Description: 		Quest - Encounter with the Young Queen
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	qm.dispose();
	return;
    }
    if (status == 0) {
	qm.sendNext("안녕하세요. 기사단장님. 지금 메이플 월드는 아주 위험한 상황입\n\r 니다. 검은 마법사로부터 이곳을 지켜내려면 더 많은 병력이 필요\n\r 하지요. 그리고 그 병력들이 더 강인해 지기 위한 방법으로 저는 모험가 장로님들과 함께 힘을 모으기로 했습니다. 모험가 보다 더 강인한 궁극의 모험가를 만들게 되었습니다.");
    } else if (status == 1) {
	qm.sendYesNo("궁극의 모험가는 태어나자 마자 50레벨을 지니며, 특별한 스킬을\n\r 가지고 태어나게 됩니다. 어때요, 지금 궁극의 모험가로 태어나보\n\r 시겠습니까?");
    } else if (status == 2) {
	if (!qm.getClient().canMakeCharacter(qm.getPlayer().getWorld())) {
	    qm.sendOk("기사단장님.. 캐릭터창이 부족합니다.. 다시 대화를 걸어주세요..");
	} else {
	    qm.sendUltimateExplorer();
	}
	qm.dispose();
    }
}

function end(mode, type, selection) {
}
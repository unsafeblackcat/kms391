importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.constants);


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    소마 에 의해 만들어 졌습니다.

    엔피시아이디 : 9250022

    엔피시 이름 : 부아

    엔피시가 있는 맵 : 헤네시스 : 리나의 집 (100000003)

    엔피시 설명 : MISSINGNO


*/

var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}
var questt = "resetreword"; // jump_고유번호
function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getClient().getKeyValue(questt) == null)
            cm.getClient().setKeyValue(questt, "0");

        if (Integer.parseInt(cm.getClient().getKeyValue(questt)) > 0) {
            cm.sendOkS("#fs15#보상을 이미 받으셨습니다.", 0x04, 9010061);
            cm.dispose();
            return;
        }
        if (cm.getClient().getCreated().getMonth() + 1 > 6) {// 7월에 생성한경우
            cm.sendOkS("시즌 1 유저만 보상을 받아가실 수 있습니다.", 0x04, 9010061);
            cm.dispose();
            return;
        }
        if (cm.getClient().getCreated().getMonth() + 1 == 6 && cm.getClient().getCreated().getDate() > 27) {//6월 생성인데 27일 이후생성
            cm.sendOkS("시즌 1 유저만 보상을 받아가실 수 있습니다.", 0x04, 9010061);
            cm.dispose();
            return;
        }
		if (Integer.parseInt(cm.getClient().getKeyValue(questt)) > 0) {
			cm.sendOkS("#fs15#" + 검정 + "계정당 1회만 보상을 받을수 있어요.", 0x04, 9010061);
			cm.dispose();
			return;
		}

		cm.getClient().setKeyValue(questt, "1");
        cm.getPlayer().gainCabinetItem(2430027, 1);
        cm.sendOkS("보상이 지급되었습니다. 보관함을 확인해주세요.", 0x04, 9010061);
        cm.dispose();
        return;
    }
}

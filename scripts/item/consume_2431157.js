/*

	이 스크립트는 디스코드 : 404171970649784322 님의 요청으로 제작되었습니다
	
	제작자 : Brod ( Glitch )
	
	스크립트 용도 : 최종데미지 상자 [ 선택 개수만큼 ]

*/

var enter = "\r\n";
var seld = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
		var max = cm.itemQuantity(2431157);
		seld = selection;
	cm.sendGetNumber("사용하실 최종데미지 상자의 수량을 적어주세요.\r\n\r\n [ 최대 100개 ]", 1, 1, 100);
    } else if (status == 1) {
		if (!cm.haveItem(2431157, selection)) {
		cm.sendOk("최종데미지 상자가 부족합니다.");
		cm.dispose();
		return;
	}
        cm.gainItem(2431157, -selection);
		cm.MakePmdrItem(2431157, selection*5);
        cm.dispose();
    }
}
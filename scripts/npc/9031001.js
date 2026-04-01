var status = -1;
var sel = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	if (cm.getPlayer().getProfessionLevel(92000000) <= 0) { // 스킬을 안배웠을때
	    cm.sendSimple("안녕하세요. 무엇을 도와드릴까요?\r\n\r\n#b#L0##e약초채집#n에 대한 설명을 듣는다.\r\n#L1##e약초 채집#n을 배운다.");
	} else { // 스킬을 배웠을때
	    cm.sendSimple("안녕하세요. 무엇을 도와드릴까요?\r\n\r\n#b#L1##e약초채집#n의 레벨을 초기화한다.\r\n#L3##e약초뿌리#n를 교환한다.");
	}
    } else if (status == 1) {
	sel = selection;
	if (sel == 0 ) {
		cm.sendNext("채집 스킬을 배우면 약초채집장비인 #b삽#k이 기본 장비로 상시 장착된답니다. 다른 종류의 #b삽#k이 있다면 인벤토리의 가장 앞의 장비가 활성화 됩니다.");
		cm.dispose();
	} else if (sel == 1) {
	    if (cm.getPlayer().getProfessionLevel(92000000) > 0) {
		cm.sendYesNo("약초채집을 배우지 않는 상태로 초기화를 할거야? 지금까지 쌓아온 숙련도와 레벨이 모두 초기화 된다구.");
	    } else {
		cm.sendYesNo("#b약초 채집#k을 배웁니다. 비용은 #b5000메소#k입니다. 정말 배우시겠습니까?");
	    }
	} else if (sel == 3) {
	    if (!cm.haveItem(4022023, 100)) {
		cm.sendOk("약초뿌리를 100개이상 갖고 있지 않은것 같습니다. 다시 확인후 이용해주세요.");
 	    } else if (!cm.canHold(2028066, 1)) {
		cm.sendOk("인벤토리 공간이 부족합니다.");
	    } else {
		cm.sendOk("약초뿌리 교환이 완료되었습니다.");
		cm.gainItem(2028066, 1);
		cm.gainItem(4022023, -100);
	    } 
         }
    } else if (status == 2) {
	if (sel == 1) {
	    if (cm.getPlayer().getProfessionLevel(92000000) > 0) {
		cm.sendOk("약초채집이 초기화 되었습니다.");
		cm.teachSkill(92000000, 0, 0);
	    	cm.dispose();
	    } else {
		cm.sendOk("좋아. 약초채집을 성공적으로 익혔습니다. 숙련도가 다 채워야지만 레벨을 올릴 수 있으니 다시 찾아오세요.");
		cm.teachSkill(92000000, 0x1000000, 0); //00 00 00 01
		if (cm.canHold(1502000,1)) {
			cm.gainItem(15020000,1);
		}
	    	cm.dispose();
                }
	}
    }
}
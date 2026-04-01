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
	if (cm.getPlayer().getProfessionLevel(92040000) <= 0) { // 스킬을 안배웠을때
	    cm.sendSimple("안녕하세요. 혹시 연금술에 관심있으세요?\r\n\r\n#b#L0##e연금술#n에 대한 설명을 듣는다.\r\n#L1##e연금술#n을 배운다.");
	} else { // 스킬을 배웠을때
	    cm.sendSimple("안녕하세요. 혹시 연금술에 관심있으세요?\r\n\r\n#b#L1##e연금술#n의 레벨을 초기화한다.\r\n#L3##e원석의 파편#n을 교환한다.");
	}
    } else if (status == 1) {
	sel = selection;
	if (sel == 0 ) {
		cm.sendNext("연금술은 허브의 오일을 이용하여 다양한 물약을 만드는 기술이랍니다. HP와 MP를 회복하는 물약부터 당신을 강하게 할 수 있는 다양한 물약도 만들 수 있어요. 지금까지 체험하지 못했던 신기한 물약도 당연히 만들 수 있구요.");
		cm.dispose();
	} else if (sel == 1) {
	    if (cm.getPlayer().getProfessionLevel(92040000) > 0) {
		cm.sendYesNo("연금술을 배우지 않은 상태로 초기화합니다. 지금까지 쌓으신 레벨과 숙련도가 모두 초기화 됩니다. 정말 초기화 하시고 싶으세요?");
	    } else {
		cm.sendYesNo("#b연금술#k을 배워보시고 싶으세요? 비용은 #b5000 메소#k입니다. 정말 배우시겠어요?\r\n");
	    }
	} else if (sel == 3) {
	    if (!cm.haveItem(4011010, 100)) {
		cm.sendOk("원석의 파편을 100개이상 갖고 있지 않은것 같습니다. 다시 확인후 이용해주세요.");
 	    } else if (!cm.canHold(2028067, 1)) {
		cm.sendOk("Please make some USE space.");
	    } else {
		cm.sendOk("원석의 파편 교환이 완료되었습니다.");
		cm.gainItem(2028067, 1);
		cm.gainItem(4011010, -100);
	    } 
         }
    } else if (status == 2) {
	if (sel == 1) {
	    if (cm.getPlayer().getProfessionLevel(92040000) > 0) {
		cm.sendOk("연금술 기술을 초기화해드렸어요. 다시 배우시고 싶으시다면 언제든지 찾아주세요!");
		cm.teachSkill(92040000, 0, 0);
	    	cm.dispose();
	    } else {
		cm.sendOk("자아~ 연금술에 대한 기본적인 지식을 알려드렸어요. 그리고 숙련도가 다 차게되면 연금술의 레벨을 올릴 수 있으니 꼭 저에게 다시 찾아와주세요. 새로운 지식을 알려드릴게요.");
		cm.teachSkill(92040000, 0x1000000, 0); //00 00 00 01
		if (cm.canHold(1512000,1)) {
			cm.gainItem(1512000,1);
		}
	    	cm.dispose();
                }
	}
    }
}
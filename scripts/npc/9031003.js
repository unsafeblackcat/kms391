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
	if (cm.getPlayer().getProfessionLevel(92020000) <= 0) { // 스킬을 안배웠을때
	    cm.sendSimple("그래. 장비 제작의 달인, 이 #b에이센#k님에게 원하는게 뭐지?\r\n\r\n#b#L0##e장비제작#n에 대한 설명을 듣는다.\r\n#L1##e장비 제작#n기술을 배운다.");
	} else { // 스킬을 배웠을때
	    cm.sendSimple("그래. 장비 제작의 달인, 이 #b에이센#k님에게 원하는게 뭐지?\r\n\r\n#b#L1##e장비 제작#n 레벨을 초기화한다.\r\n#L3##e원석의 파편#n을 교환한다.");
	}
    } else if (status == 1) {
	sel = selection;
	if (sel == 0 ) {
		cm.sendNext("장비 제작은 제련된 보석과 광물을 이용하여 멋진 장비들을 만드는 매력적인 기술이라네.");
		cm.dispose();
	} else if (sel == 1) {
	    if (cm.getPlayer().getProfessionLevel(92020000) > 0) {
		cm.sendYesNo("장비 제작을 배우지 않은 상태로 초기화하는가? 지금까지 쌓은 레벨과 숙련도가 모두 초기화 된다네. 정말 초기화 하고 싶나?");
	    } else {
		cm.sendYesNo("#b장비 제작#k을 배워보고 싶나? 비용은 #b5000 메소#k라네. 정말 배워보고 싶나?");
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
	    if (cm.getPlayer().getProfessionLevel(92020000) > 0) {
		cm.sendOk("장비 제작 기술을 초기화했다네. 다시 배우고 싶다면 언제든지 찾아오게.");
		cm.teachSkill(92010000, 0, 0);
	    	cm.dispose();
	    } else {
		cm.sendOk("장비 제작의 지식을 알려주었다네. 그리고 숙련도가 다 차게되면 장비 제작의 레벨을 올릴 수 있으니 꼭 다시 찾아오게. 더 자세한 지식을 알려주겠네.");
		cm.teachSkill(92020000, 0x1000000, 0); //00 00 00 01
		if (cm.canHold(1512000,1)) {
			//cm.gainItem(1512000,1);
		}
	    	cm.dispose();
                }
	}
    }
}
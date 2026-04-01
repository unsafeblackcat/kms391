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
	if (cm.getPlayer().getProfessionLevel(92030000) <= 0) { // 스킬을 안배웠을때
	    cm.sendSimple("우아한 #b멜츠#k님의 고상한 취미 생활은 보석 감상이지. 반짝반짝거리는 보석들을 보고 있노라면 시간 가는 줄 모르겠어. 으흠~ 너도 관심있는거야? 그래보이지 않는걸?\r\n\r\n#b#L0##e장신구제작#n에 대한 설명을 듣는다.\r\n#L1##e장신구제작#n기술을 배운다.");
	} else { // 스킬을 배웠을때
	    cm.sendSimple("우아한 #b멜츠#k님의 고상한 취미 생활은 보석 감상이지. 반짝반짝거리는 보석들을 보고 있노라면 시간 가는 줄 모르겠어. 으흠~ 너도 관심있는거야? 그래보이지 않는걸?\r\n\r\n#b#L1##e채광#n의 레벨을 초기화한다.\r\n#L3##e원석의 파편#n을 교환한다.");
	}
    } else if (status == 1) {
	sel = selection;
	if (sel == 0 ) {
		cm.sendNext("장신구제작에 대해서 알려줄려면 우선 보석의 아름다움에 대한 근본적인 것에서부터 출발해야하지만 짧게 이야기하지. 밤새도록 이야기해도 모자르니...\r\n장신구제작은 간단해. 그냥 다듬어지지 않는 보석과 광물을 아름답게 다듬고 장신구로 만들어 원래의 빛을 발하게 해주는거야. 그 과정에서 숨겨진 힘이 발휘되어 나를 더 아름답고 강하게 만들 수 있지.");
		cm.dispose();
	} else if (sel == 1) {
	    if (cm.getPlayer().getProfessionLevel(92030000) > 0) {
		cm.sendYesNo("장신구 제작을 배우지 않은 상태로 초기화하는가? 지금까지 쌓은 레벨과 숙련도가 모두 초기화 된다네. 정말 초기화 하고 싶나?");
	    } else {
		cm.sendYesNo("#b장신구 제작#k을 배워보고 싶나? 비용은 #b5000 메소#k라네. 정말 배워보고 싶나?\r\n");
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
	    if (cm.getPlayer().getProfessionLevel(92030000) > 0) {
		cm.sendOk("장신구 제작 기술을 초기화했다네. 다시 배우고 싶다면 언제든지 찾아오게.");
		cm.teachSkill(92030000, 0, 0);
	    	cm.dispose();
	    } else {
		cm.sendOk("장신구 제작의 심오한 지식을 알려주었다네. 그리고 숙련도가 다 차게되면 장신구 제작의 레벨을 올릴 수 있으니 꼭 다시 찾아오게. 더 심층적인 지식을 전도해주겠네.");
		cm.teachSkill(92030000, 0x1000000, 0); //00 00 00 01
		if (cm.canHold(1512000,1)) {
			cm.gainItem(1512000,1);
		}
	    	cm.dispose();
                }
	}
    }
}
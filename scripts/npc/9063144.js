var status = -1;
var enter = "\r\n";
var talkType = 0x86;
var NoramlPass = "Serenity_Noraml_Pass_Info";
var PrimeumPass = "Serenity_Premium_Pass_Info";
function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        giveCharacterToEventKeyValue();
		
		var chat = "안녕하세요 Fancy 패스 이벤트입니다 무엇을 도와드릴까요?" + enter;
		chat += "#L0#" + "Fancy 패스 이벤트 설명듣기"+enter;
		chat += "#L1#" + "#rFancy 일반 패스 이용하기"+enter;
		chat += "#L2#" + "#bFancy 프리미엄 패스 이용하기#l"+enter;
		chat +="\r\n\r\n#r프리미엄 패스를 구매하시고 더욱 더 좋은 혜택을 누려보세요!"
		cm.sendSimpleS(chat, talkType);
	} else if (status == 1) {
		switch (selection) {
			case 0:
				cm.sendNextPrevS(FancyPassExplaination(), talkType);
                cm.dispose();
				break;
			case 1:
                cm.dispose();
				cm.openNpcCustom(cm.getClient(), 9010061, NoramlPass);
				break;
			case 2:
                cm.dispose();
				cm.openNpcCustom(cm.getClient(), 9010061, PrimeumPass);
				break;
		}
	}
}

function FancyPassExplaination() {
	var explain = "Fancy 패스 안내입니다.\r\nFancy 패스는 매일 레벨 범위 몬스터 #b5천마리#k를 처치 하는것이 미션 입니다.\r\n패스의 종류는 #r일반 패스#k와 #b프리미엄 패스#k가 있으며 #b프리미엄 패스#k는 후원상점에서 구매할 수 있습니다.\r\n각종 보상은 디스코드 공지사항을 확인해주세요.  ";
	

	return explain;
}

function giveCharacterToEventKeyValue() {
    if (cm.getPlayer().getV("Pass_kill_Monster_amount") == null) {
        cm.getPlayer().addKV("Pass_kill_Monster_amount" , "0");
    }
}
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
		
		var chat = "Hai。你好 Fancy 通行證活動，需要咨詢？" + enter;
		chat += "#L0#" + "Fancy 聽路徑事件說明"+enter;
		chat += "#L1#" + "#rFancy 使用常規路徑"+enter;
		chat += "#L2#" + "#bFancy 使用高級通行證#l"+enter;
		chat +="\r\n\r\n#r購買高級通行證，享受更好的優惠!"
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
	var explain = "Fancy 全服通知.\r\nFancy 通行證每天等級範圍怪物 #b5千只#k任務是消滅.\r\n路徑的類型包括： #r常規通行證#k和 #b高級通行證#k例外 #b高級通行證#k可以在贊助商店購買.\r\n各種禮品請確認解碼公告事項.  ";
	

	return explain;
}

function giveCharacterToEventKeyValue() {
    if (cm.getPlayer().getV("Pass_kill_Monster_amount") == null) {
        cm.getPlayer().addKV("Pass_kill_Monster_amount" , "0");
    }
}
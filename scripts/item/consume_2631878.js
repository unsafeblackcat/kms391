/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

importPackage(Packages.client.inventory);

status = -1;

지급개수 = 50
템코드 = 2631878

심볼 = [
1712001,
1712002,
1712003,
1712004,
1712005,
1712006,
]

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
	말 = "#fs15#받고싶은 #d아케인심볼#k 을 선택해 주세요.\r\n\r\n"
	for (i = 0; i < 심볼.length; i++) {
	말 += "#L"+i+"##v"+심볼[i]+"# #d#z"+심볼[i]+"#\r\n";
 	}
	cm.sendSimpleS(말, 0x04, 9010061);
    } else if (status == 1) {
	seld = selection;
	cm.sendYesNoS("#fs15#정말 받고싶은 아이템이 #i"+심볼[selection]+"# #d#z"+심볼[selection]+"##k (이)가 맞으신가요?", 0x04, 9010061);
    } else if (status == 2) {
	if (cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < 지급개수) {
		cm.sendOk("#fs15##r장비 슬롯 "+지급개수+" 칸 이상의 인벤토리 공간이 필요합니다.");
		cm.dispose();
		return;
	}
	if (심볼[seld] != 1712001 && 심볼[seld] != 1712002 && 심볼[seld] != 1712003 && 심볼[seld] != 1712004 && 심볼[seld] != 1712005 && 심볼[seld] != 1712006) {
		a = new Date();
		temp = Randomizer.rand(0,9999999);
		cn = cm.getPlayer().getName();
		fFile1 = new File("Log/CopyTry/"+temp+"_"+cn+".log");
		if (!fFile1.exists()) {
		fFile1.createNewFile();
		}
		out1 = new FileOutputStream("Log/CopyTry/"+temp+"_"+cn+".log",false);
		msg =  "'"+cm.getPlayer().getName()+"'이(가) 의심됨.\r\n";
		msg = "'"+a.getFullYear()+"년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일 "+a.getHours()+"시 "+a.getMinutes()+"분 "+a.getSeconds()+"초'\r\n";
		msg += "복사 시도 아이템코드 : "+심볼[seld]+"\r\n";
		msg += "사용자 캐릭터 아이디 : "+cm.getPlayer().getId()+"\r\n";
		msg += "사용자 어카운트 아이디 : "+cm.getPlayer().getAccountID()+"\r\n";
		out1.write(msg.getBytes());
		out1.close();
		cm.sendOk("#fs15##r정상적인 접근 방법이 아닙니다.\r\n\r\n#d* 아주 잠깐의 달콤함을 위해 본연의 즐거움을 희생하시겠어요?",9010061);
		cm.dispose();
		return;
	}
	cm.gainItem(템코드, -1);
	for (i = 0; i < 지급개수; i++) {
		cm.gainItem(심볼[seld], 1);
	}
	cm.sendOkS("#fs15##i"+심볼[seld]+"# #d#z"+심볼[seld]+"# #b"+지급개수+" 개#k 를 지급 해드렸습니다.", 0x04, 9010061);
	cm.dispose();
    }
}
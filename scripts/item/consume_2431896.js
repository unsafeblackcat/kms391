/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

// 하급 주머니
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.provider);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.server);
importPackage(Packages.client.inventory);

/* 아이템 세팅 */
var 소울종류 = new Array(2591225, 2591226, 2591227, 2591228, 2591229, 2591230, 2591231, 2591232, 2591233);
아이템 = [2591225, 2591226, 2591227, 2591228, 2591229, 2591230, 2591231, 2591232];
소울조각코드 = 2431896
위대한소울뽑힐확률 = 5
위대한소울코드 = 2591232
대사 = "오호~! #b#z"+아이템+"##k이 나왔구나! 요간하게 쓰도록 하렴. 크크크..."
위대한대사 = "오오.. #b#z"+위대한소울코드+"##k이 나왔군! 이건 다른 소울보다 강력한 아이템이니 소중히 간직하도록 하렴. 크크크..."
아이템코드 = 0

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
		if (cm.haveItem(소울조각코드,10)) {
			cm.sendYesNo("#b#z"+소울조각코드+"##k 10개를 소비하여 소울 아이템으로 교환 할거야?", 9000030);
		} else {
			말 = "#b#z"+소울조각코드+"##k\r\n"
			말 += "#r10개#k를 모으면 아래의 아이템 중 1종으로 교환할 수 있다네.\r\n"
			for (var i = 0; i < 소울종류.length; i++) {
				말 += "#i" + 소울종류[i] + "# #b#z" + 소울종류[i] + "##k\r\n";
			}
			cm.sendOk(말, 9000030);
			cm.dispose();
			cm.dispose();
			return;
		}
	} else if (status == 1) {
		var check = 0;
		var debeck = 0;
		leftslot = cm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
		if (leftslot < 2) {
			cm.sendOk("#fs15##r소비창 2칸 이상을 확보해주세요.");
			cm.dispose();
			return;
		}
		if (Randomizer.isSuccess(위대한소울뽑힐확률)) { //위대한
			아이템코드 = 위대한소울코드
			check = 1;
			debeck = 1;
		} else {
			아이템코드 = 아이템[Math.floor(Math.random() * 아이템.length)];
		}
		for (b = 0; b < 아이템.length; b++) {
			if (아이템코드 == 아이템[b]) {
				check = 1;
				break;
			}
		}
		if (check == 0) {
			a = new Date();
			temp = Randomizer.rand(0,9999999);
			cn = cm.getPlayer().getName();
			fFile1 = new File("Log/EtcCopy/soul_copy__"+a.getDate() +"_"+a.getHours()+"_"+a.getMinutes()+"_"+a.getSeconds()+"_"+cn+".log");
			if (!fFile1.exists()) {
				fFile1.createNewFile();
			}
			out1 = new FileOutputStream("Log/EtcCopy/soul_copy__"+a.getDate() +"_"+a.getHours()+"_"+a.getMinutes()+"_"+a.getSeconds()+"_"+cn+".log",false);
			msg =  "'"+cm.getPlayer().getName()+"'이(가) 의심됨.\r\n";
			msg = "'"+a.getFullYear()+"년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일 "+a.getHours()+"시 "+a.getMinutes()+"분 "+a.getSeconds()+"초'\r\n";
			msg += "복사 시도 아이템코드(소울스크립트) : "+아이템코드+"\r\n";
			msg += "사용자 캐릭터 아이디 : "+cm.getPlayer().getId()+"\r\n";
			msg += "사용자 어카운트 아이디 : "+cm.getPlayer().getAccountID()+"\r\n";
			msg += "스크립트 코드 : consume_2431710\r\n";
			out1.write(msg.getBytes());
			out1.close();
			cm.getPlayer().getWorldGMMsg(cm.getPlayer(), "소울 스크립트에서 복사를 시도.");
			cm.sendOk("#fs15##r정상적인 접근 방법이 아닙니다.\r\n\r\n#d* 아주 잠깐의 달콤함을 위해 본연의 즐거움을 희생하시겠어요.?",9062004);
			cm.dispose();
			return;
		}
		cm.gainItem(소울조각코드, -10);
		cm.gainItem(아이템코드, 1);
		if (debeck == 1) {
			cm.sendOk(위대한대사, 9000030);
		} else {
			cm.sendOk("오호~! #b#z"+아이템코드+"##k이 나왔구나! 요간하게 쓰도록 하렴. 크크크...", 9000030);
		}
		cm.dispose();
   	}
}





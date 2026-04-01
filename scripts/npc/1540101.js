var itmelist = [
    [5060048, 2000, 1], // 골드애플
	//[2437718, 50000, 1], // 死亡死亡（女）兌換券关闭
    [4034803, 100000, 1], // 이름변경권	
    [2643133, 5000, 1], // 어웨이크링 주문서
    [5062005, 1500, 3], // 어메이징 미라클 큐브
    [5062503, 3000, 3], // 화이트 에디셔널 큐브
    [2049360, 2000, 1], // 놀장강
    [2435899, 5000, 1], // 스페셜 위대한 소울
	[4001780, 1000, 1], // 일반돌림판
	[4319998, 2000, 1], // 고급돌림판
	//[2046403, 10000, 1], // 후원 방어구 공
    //[2046404, 10000, 1], // 후원 방어구 마
	[2430041, 20000, 1], //쿨
	[2430042, 20000, 1], //메획
	[2430043, 20000, 1], //아획
	[2630512, 10000, 1], // 아케인심볼 만렙상자
    [2633616, 15000, 1], // 어센틱심볼 만렙상자
	[2430030, 20000, 1], // 악령의상자
    [2470021, 30000, 1], // 업횟5 황망
    [2633915, 20000, 1], // 아케인 방어구 선택
    [2630782, 20000, 1], //아케인 무기 선택 
    [2430368, 30000, 1], //스네이크 헌터 악세서리
	[4310086, 50000, 1], // 직변코인

  /*  [2430031, 8000, 1], //아케인심볼 : 여로 20렙
    [2430032, 8000, 1], // 아케인심볼 : 츄츄 20렙
    [2430033, 8000, 1], // 아케인심볼 : 레헬른 20렙
    [2430049, 8000, 1], // 아케인심볼 : 아르카나 20렙
    [2430051, 8000, 1], // 아케인심볼 : 모라스 20렙
    [2430052, 8000, 1], // 아케인심볼 : 에스페라 20렙*/
  /* [2633336, 20000, 1], // 어센틱심볼 : 아르크스 11렙
    [2439999, 30000, 1], // 어센틱심볼 : 오디움 11렙*/

   // [2630368, 20000, 1], // 

 //   [2049704, 2000, 1], //레전드리 잠재 주문서

  /*  [2046400, 10000, 1], // 후원 무기 줌서 공
    [2046402, 10000, 1], // 후원 무기 줌서 공
    [2046401, 10000, 1], // 후원 무기 줌서 마
    [2046408, 10000, 1], // 후원 펫공
    [2046409, 10000, 1], // 후원 펫마
    [2046405, 10000, 1], // 후원 악세 공
    [2046406, 10000, 1], // 후원 악세 마*/

	
];

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
    }
    if (status == 0) {
        var a = "#fs15##b#h0# #n#k#l的贊助積分 : #fc0xFFFF3366#" + cm.getPlayer().getDonationPoint() + " P#k#n\r\n";
        for (var i = 0; i < itmelist.length; i++) {
            a += "#L" + i + "##i" + itmelist[i][0] + "# #b#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 個     #fc0xFF000000#積分#k #e#fc0xFFFF3366#" + itmelist[i][1] + " P#k#n\r\n";
        }
        cm.sendSimpleS(a,0x86);

    } else if (status == 1) {
        sel = selection;
	    /*if (sel == 5){
	        cm.dispose();
	        cm.openNpc(9062170);
	        return;
	    }*/
        cm.sendGetNumber("購買幾個?", 1, 1, 100);
        cm.sendOkS("#fs15##b贊助點數#k 不足.",0x24, 9010061);

    } else if (status == 2) {
        count = selection;
        if (sel >= 0 && sel <= itmelist.length) {
            if (cm.getPlayer().getDonationPoint() >= itmelist[sel][1] * count) {
                if (cm.canHold(itmelist[7][0]) || cm.canHold(itmelist[8][0])) {
                    cm.sendOkS("消耗#b贊助#k 積分 購買了#i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 個#k ",0x24, 9010061);
                    cm.dispose();
                }
                if (cm.canHold(itmelist[sel][0])) {
                    cm.sendOkS("消耗#b贊助#k 積分 購買了#i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 個#k ",0x24, 9010061);
                    cm.dispose();
                }
                    cm.getPlayer().gainDonationPoint(-(itmelist[sel][1] * count));
                    cm.gainItem(itmelist[sel][0], itmelist[sel][2] * count);
                    cm.sendOkS("消耗#b贊助#k 積分 購買了#i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 個#k ",0x24, 9010061);
                    cm.dispose();
            } else {
                cm.sendOk("#fs15##b贊助積分#k 不足.");
                cm.dispose();
	    
            }
        }
    }
}













//기존
/*var itmelist= [
    [5068300, 1000, 1], //원더베리
    [5068300, 10000, 10], //원더베리
    [5069100, 2000, 1], //루나 크리스탈
    [5062006, 1000, 1], //플래티넘 언리미티드 큐브
    [5062006, 10000, 10], //플래티넘 언리미티드 큐브
    [5060048, 1500, 1], //애플
    [5060048, 15000, 10], //애플
    [2049360, 3000, 1], //놀장강
    [2049360, 30000, 10], //놀장강
    [2047950, 3000, 1], // [닉스]방어구 주문서
    [2047950, 30000, 10], // [닉스]방어구 주문서
    [2046076, 3000, 1], // [후원] 한손무기 공격력 주문서
    [2046076, 30000, 10], // [후원] 한손무기 공격력 주문서
    [2046077, 3000, 1], // [후원] 한손무기 마   력 주문서
    [2046077, 30000, 10], // [후원] 한손무기 마   력 주문서
    [2046150, 3000, 1], // [후원] 두손무기 공격력 주문서
    [2046150, 30000, 10], // [후원] 두손무기 공격력 주문서
    [2048088, 4000, 1], // [후원] 펫장비 공격력 주문서
    [2048088, 40000, 10], // [후원] 펫장비 공격력 주문서
    [2048089, 4000, 1], // [후원] 펫장비 마   력 주문서
    [2048089, 40000, 10], // [후원] 펫장비 마   력 주문서
    [2046817, 5000, 1], // [후원] 악세서리 공격력 주문서
    [2046817, 50000, 10], // [후원] 악세서리 공격력 주문서
    [2046818, 5000, 1], // [후원] 악세서리 마   력 주문서
    [2046818, 50000, 10], // [후원] 악세서리 마   력 주문서
    [2435899, 5000, 1], //위대한 소울 선택상자
    [2435899, 15000, 3], //위대한 소울 선택상자
    [2430030, 3000, 1], //보스 입장 초기화+
    [2430030, 15000, 5], //보스 입장 초기화+
    [2049704, 500, 1], //레전드리 잠재 부여 주문서
    [2049704, 5000, 10], //레전드리 잠재 부여 주문서
    [2430215, 3000, 1], // 안드로이드 각인서
    [2430215, 30000, 10], // 안드로이드 각인서
    [4034803, 15000, 1], // 닉네임 변경권
    [2430068, 30000, 1], //루나쁘띠 1~4기 펫
    [2430368, 10000, 1], //스네이크 헌터 악세서리
    [2632793, 20000, 1], // 극한성장의 비약
    [2430031, 20000, 1], //아케인심볼 : 여로 20렙
    [2430032, 20000, 1], // 아케인심볼 : 츄츄 20렙
    [2430033, 20000, 1], // 아케인심볼 : 레헬른 20렙
    [2430049, 20000, 1], // 아케인심볼 : 아르카나 20렙
    [2430051, 20000, 1], // 아케인심볼 : 모라스 20렙
    [2430052, 20000, 1], // 아케인심볼 : 에스페라 20렙
    [2633616, 50000, 1], // 어센틱심볼 : 세르니움 11렙
    [2633336, 50000, 1], // 어센틱심볼 : 아르크스 11렙
    [2049376, 26900, 1], // 스타포스 20성 강화권
    [4310021, 3000, 1], // 스타포스 10% 확률 업 티켓
    [4310021, 30000, 10], // 스타포스 10% 확률 업 티켓
	];
	
	function start() {
		status = -1;
		action(1, 0, 0);
	}
	
	function action(mode, type, selection) {
		if (mode == -1) {
			cm.dispose();
		} else {
			if (mode == 0) {
				cm.dispose();
				return;
			}
			if (mode == 1)
				status++;
			else
				status--;
		if (status == 0) {
	
					  
			 var a = "#fs15##fc0xFFFF3366##h0# #fc0xFF000000#님의 도네이션 포인트 : #fc0xFFFF3366#"+cm.getPlayer().getDonationPoint()+" P#k#n\r\n"; 
			for(var i = 0; i<18; i++){
				a += "#L"+i+"##i"+itmelist[i][0]+"# #d#z"+itmelist[i][0]+"##l#k#r "+itmelist[i][2]+" 個\r\n               #fc0xFF000000#도네이션 포인트#k #e#fc0xFFFF3366#"+itmelist[i][1]+" P#k#n\r\n";
			}
			for(var i = 18; i<24; i++){
				a += "#L"+i+"##i"+itmelist[i][0]+"# #d#z"+itmelist[i][0]+"##l#k#r "+itmelist[i][2]+" 個\r\n               #fc0xFF000000#도네이션 포인트#fc0xFF000000# #e#fc0xFFFF3366#"+itmelist[i][1]+" P#k#n\r\n               #r강화비용 별도\r\n";
			}
			for(var i = 24; i<itmelist.length; i++){
				a += "#L"+i+"##i"+itmelist[i][0]+"# #d#z"+itmelist[i][0]+"##l#k#r "+itmelist[i][2]+" 個\r\n               #fc0xFF000000#도네이션 포인트#fc0xFF000000# #e#fc0xFFFF3366#"+itmelist[i][1]+" P#k#n\r\n               #r강화1비용 별도\r\n";
			}
			cm.sendSimple(a);
	
			} else if (selection >= 0 && selection <= itmelist.length) {
			if (cm.getPlayer().getDonationPoint() >= itmelist[selection][1]) {
				if (cm.canHold(itmelist[7][0]) || cm.canHold(itmelist[8][0])) {
					cm.sendOk("#b후원 포인트#k 로 #i"+itmelist[selection][0]+"# #r "+itmelist[selection][2]+" 個#k 您購買了。");
					cm.dispose();
				}
				if (cm.canHold(itmelist[selection][0])) {
					cm.sendOk("#b후원 포인트#k 로 #i"+itmelist[selection][0]+"# #r "+itmelist[selection][2]+" 個#k 您購買了。");
					cm.dispose();
				}
					cm.getPlayer().gainDonationPoint(-itmelist[selection][1]);
					cm.gainItem(itmelist[selection][0], itmelist[selection][2]);
					cm.sendOk("#b후원 포인트#k 로 #i"+itmelist[selection][0]+"# #r "+itmelist[selection][2]+" 個#k 您購買了。");
					cm.dispose();
			} else {
				cm.sendOk("#fs15##b후원 포인트#k 가 부족합니다.");
				cm.dispose();
			}
	
			}
		}
	}







//기존
var itmelist = [
    [5068300, 1000, 1], //원더베리
    [5069100, 2000, 1], //루나 크리스탈
    [5062006, 1000, 1], //플래티넘 언리미티드 큐브
    [5062002, 2000, 1], //에디셔널 언리미티드 큐브
    [5060048, 1500, 1], //애플
    [2049360, 3000, 1], //놀장강
    [2047950, 3000, 1], // [닉스]방어구 주문서
    [2046076, 3000, 1], // [후원] 한손무기 공격력 주문서
    [2046077, 3000, 1], // [후원] 한손무기 마   력 주문서
    [2046150, 3000, 1], // [후원] 두손무기 공격력 주문서
    [2048047, 4000, 1], // 후원] 펫장비 공격력 주문서
    [2048048, 4000, 1], // [후원] 펫장비 마   력 주문서
    [2046340, 5000, 1], // [후원] 악세서리 공격력 주문서
    [2046341, 5000, 1], // [후원] 악세서리 마   력 주문서
    [2435899, 5000, 1], //위대한 소울 선택상자
    [2430030, 2000, 1], //보스 입장 초기화+
    [2049704, 500, 1], //레전드리 잠재 부여 주문서
    [2430034, 3000, 1], // 안드로이드 각인서
    [4034803, 15000, 1], // 닉네임 변경권
    [2430068, 30000, 1], //루나쁘띠 1~4기 펫
    [2430028, 10000, 1], //스네이크 헌터 악세서리
    [2633621, 20000, 1], // 극한성장의 비약
    [2430031, 20000, 1], //아케인심볼 : 여로 20렙
    [2430032, 20000, 1], // 아케인심볼 : 츄츄 20렙
    [2430033, 20000, 1], // 아케인심볼 : 레헬른 20렙
    [2430049, 20000, 1], // 아케인심볼 : 아르카나 20렙
    [2430051, 20000, 1], // 아케인심볼 : 모라스 20렙
    [2430052, 20000, 1], // 아케인심볼 : 에스페라 20렙
    [2633616, 50000, 1], // 어센틱심볼 : 세르니움 11렙
    [2633336, 50000, 1], // 어센틱심볼 : 아르크스 11렙
    [2049376, 26900, 1], // 스타포스 20성 강화권
    [4310021, 3000, 1], // 스타포스 10% 확률 업 티켓
];

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
    }
    if (status == 0) {
        var a = "#fs15##fc0xFFFF3366##h0# #fc0xFF000000#님의 도네이션 포인트 : #fc0xFFFF3366#" + cm.getPlayer().getDonationPoint() + " P#k#n\r\n";
        for (var i = 0; i < 18; i++) {
            a += "#L" + i + "##i" + itmelist[i][0] + "# #d#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 個\r\n               #fc0xFF000000#도네이션 포인트#k #e#fc0xFFFF3366#" + itmelist[i][1] + " P#k#n\r\n";
        }
        for (var i = 18; i < 24; i++) {
            a += "#L" + i + "##i" + itmelist[i][0] + "# #d#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 個\r\n               #fc0xFF000000#도네이션 포인트#fc0xFF000000# #e#fc0xFFFF3366#" + itmelist[i][1] + " P#k#n\r\n               #r \r\n";
        }
        for (var i = 24; i < itmelist.length; i++) {
            a += "#L" + i + "##i" + itmelist[i][0] + "# #d#z" + itmelist[i][0] + "##l#k#r " + itmelist[i][2] + " 個\r\n               #fc0xFF000000#도네이션 포인트#fc0xFF000000# #e#fc0xFFFF3366#" + itmelist[i][1] + " P#k#n\r\n               #r\r\n";
        }
        cm.sendSimple(a);

    } else if (status == 1) {
        sel = selection;
        cm.sendGetNumber("몇個를 구매?", 1, 1, 100);
        cm.sendOk("#fs15##b후원 포인트#k 가 부족합니다.");

    } else if (status == 2) {
        count = selection;
        if (sel >= 0 && sel <= itmelist.length) {
            if (cm.getPlayer().getDonationPoint() * count >= itmelist[sel][1]) {
                if (cm.canHold(itmelist[7][0]) || cm.canHold(itmelist[8][0])) {
                    cm.sendOk("#b후원 포인트#k 로 #i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 個#k 您購買了。");
                    cm.dispose();
                }
                if (cm.canHold(itmelist[sel][0])) {
                    cm.sendOk("#b후원 포인트#k 로 #i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 個#k 您購買了。");
                    cm.dispose();
                }
                cm.getPlayer().gainDonationPoint(-(itmelist[sel][1] * count));
                cm.gainItem(itmelist[sel][0], itmelist[sel][2] * count);
                cm.sendOk("#b후원 포인트#k 로 #i" + itmelist[sel][0] + "# #r " + itmelist[sel][2] * count + " 個#k 您購買了。");
                cm.dispose();
            } else {
                cm.sendOk("#fs15##b후원 포인트#k 가 부족합니다.");
                cm.dispose();
            }
        }
    }
}*/
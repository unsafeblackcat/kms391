var status = -1;
	move1 = 0;
	move2 = 0;
	move3 = 0;
	h = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    dal = ["0100100.img","0100101.img","0130101.img"] 
ccoin = 4310248; // 硬幣代碼 
    if (mode == 1) {
		if (status == 2) {
			if (move1 >= 50 || move2 >= 50 || move3 >= 50) {
				status ++;
			} else {
				move1 += Math.floor(Math.random()  * 5);
				move2 += Math.floor(Math.random()  * 5);
				move3 += Math.floor(Math.random()  * 5);
			}
		} else {
            status++;
		}
    } else {
        cm.dispose(); 
        return;
    }
    if (status == 0) {
        對話 = "#fs15##fn나눔고딕 Extrabold##d 跑最快的蝸牛是誰呢？！\r\n猜中最快的怪物，就給您 #i4310248 艾爾特硬幣##r5倍#k#d獎勵！\r\n\r\n#r請輸入粉紅蝸牛的數量。#n#k"
		if (cm.itemQuantity(ccoin)  >= 1000) {
			limit = 1000;
		} else {
			limit = cm.itemQuantity(ccoin); 
		}
		cm.sendGetNumber( 對話,1,1,limit);
	} else if (status == 1) {
		st = selection;
		if (st > cm.itemQuantity(ccoin)  || st < 0) {
			cm.sendOk("#fn나눔고딕  Extrabold##fs15# #fc0xFF000000# 發生錯誤。");
			cm.dispose(); 
			return;
		}
		對話 = "#fs15##fn나눔고딕 Extrabold# #d 請選擇您想要的怪物。\r\n\r\n"
		for (i=0; i<dal.length;  i++) {
			對話 += "#L"+i+"# #fMob/"+dal[i]+"/move/0#";
		}
		cm.sendSimple( 對話);
	} else if (status == 2) {
		if (h == 0) {
			st2 = selection;
			cm.gainItem(ccoin,-st); 
		}
		h++;
		對話 = "#fs15# #fc0xFF000000# 怪物賽跑第 "+h+" 回合。\r\n"
		對話+= "#fs15# #fc0xFF000000# #r按Enter繼續。按ESC將取消獎勵發放。#k#n\r\n\r\n";
		for (i=0; i<move1; i++) {
			對話 += " ";
		}
		對話+= "#fMob/"+dal[0]+"/move/0#\r\n"
		for (i=0; i<move2; i++) {
			對話 += " ";
		}
		對話+= "#fMob/"+dal[1]+"/move/0#\r\n"
				for (i=0; i<move3; i++) {
			對話 += " ";
		}
		對話+= "#fMob/"+dal[2]+"/move/0#\r\n\r\n"
		cm.sendOk( 對話);
	} else if (status == 3) {
		if (move1 >= move2 && move1 >= move3) {
			win = 0;
		}
		if (move2 >= move1 && move2 >= move3) {
			win = 1;
		}
		if (move3 >= move1 && move3 >= move2) {
			win = 2;
		}
		if (st2 == win) {
			cm.sendOk("#fn나눔고딕  Extrabold##fs15##d 恭喜獲勝！硬幣獎勵已發放5倍！");
			cm.gainItem(ccoin,  st*5);
		} else {
			cm.sendOk("#fs15##fn나눔고딕  Extrabold##d 硬幣... 被吃掉了...");
		}
		cm.dispose(); 
    }
}
var enter = "\r\n";
var seld = -1;
 
function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose(); 
		return;
    	}
	if (status == 0) {
                     var msg = "#fs15##fn나눔고딕##fc0xFF000000#目前 #fc0xFF6600CC##h0# #fc0xFF000000#的資訊 ::\r\n 成長通行證等級 :#fc0xFFFF3300# " + cm.getPlayer().getKeyValue(100592,  "point") + "級#l#n#fc0xFF000000#  / 組隊點數 : #r0 P #fc0xFF000000#/ 我的排名等級 :#r  " + cm.getPlayer().getKeyValue(190823,  "grade") + " 級\r\n";
		msg += "#L1##fc0xFF990033#[PRAY]#fc0xFF6600CC# 成長日記 (必做)#l#n\r\n";
		msg += "#L7##fc0xFF990033#[PRAY]#fc0xFF6600CC# 使用成長通行證 (必做)#l#n\r\n\r\n\r\n";
		//msg += "               #L99##fc0xFFED4C00#前往茂密殿堂 (任務地圖)#k#l#n\r\n";
		//msg += "#L103##fc0xFF00B700#前往史匹格曼的招待所#k#l#n\r\n\r\n\r\n";
		msg += "#L8##fUI/UIWindow4.img/pointShop/100296/iconShop#  #fc0xFF3162C7#主排名晉級#l #fc0xFF990066##L13##fUI/UIWindow4.img/pointShop/3887/iconShop#  裝備楓幣強化#l#fc0xFF996600##L11##fUI/UIWindow4.img/pointShop/16393/iconShop#  製作基本飾品#l\r\n";
		msg += "#fc0xFF3162C7##L10##fUI/UIWindow4.img/pointShop/100296/iconShop#  首領排名晉級#l #fc0xFF990066##L14##fUI/UIWindow4.img/pointShop/3887/iconShop#  裝備附加屬性#l#fc0xFF996600##L12##fUI/UIWindow4.img/pointShop/16393/iconShop#  使用鍊金術#l"+enter;
		msg += "#fc0xFF3162C7##L9##fUI/UIWindow4.img/pointShop/100296/iconShop#  能力排名晉級#l#fc0xFF990066# #L15##fUI/UIWindow4.img/pointShop/3887/iconShop#  現金裝備強化#l#fc0xFF990066#\r\n"+enter;
		cm.sendSimple(msg); 
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				cm.dispose(); 
				cm.openNpc(9000368); 
			break;
			case 2:
				var msg = "";
				msg += "#fs15##b#e   [每日任務]#k#n\r\n"+enter;
				cm.sendSimple(msg); 
			break;
			case 3:
				cm.dispose(); 
				cm.openNpcCustom(cm.getClient(),  9062284, "LevelReward");
			break;
			case 4:
				cm.dispose(); 
				cm.openNpc(2040050); 
			break;
			case 5:
				cm.dispose(); 
				cm.openNpc(1540110); 
			break;
			case 99:
				cm.dispose(); 
				cm.warp(100030301,  0);
			break;
			case 100:
				cm.dispose(); 
				cm.warp(100030301,  0);
			break;
			case 103:
				cm.dispose(); 
				cm.warp(910002000,  0);
			break;
                        case 6:
				cm.dispose(); 
				cm.openNpc(3004525); 
			break;
                        case 7:
				cm.dispose(); 
				cm.openNpcCustom(cm.getClient(),  9062294, "SeasonPass");
			break;
                        case 8:
				cm.dispose(); 
				cm.openNpcCustom(cm.getClient(),  9071000, "ZodiacRank");
			break;
                        case 9:
				cm.dispose(); 
				cm.openNpcCustom(cm.getClient(),  9071000, "StatRank");
			break;
                        case 10:
				cm.dispose(); 
				cm.openNpcCustom(cm.getClient(),  9001009, "BossRank");
			break;
                        case 11:
				cm.dispose(); 
				cm.openNpcCustom(cm.getClient(),  2040050, "BaseAcc");
			break;
                        case 12:
				cm.dispose(); 
				cm.openNpcCustom(cm.getClient(),  2040050, "MakeItem");
			break;
                        case 13:
				cm.dispose(); 
				cm.openNpcCustom(cm.getClient(),  3004434, "MesoEn");
			break;
                        case 14:
				cm.dispose(); 
				cm.openNpcCustom(cm.getClient(),  3004434, "BallEn");
			break;
                        case 15:
				cm.dispose(); 
				cm.openNpcCustom(cm.getClient(),  3004434, "CashEn");
			break;
		}
	} else if (status == 2) {
		switch (seld) {
			case 1:
				switch (sel) {
					case 1:
						cm.dispose(); 
						cm.openNpc(9010106); 
					break;
					case 2:
						cm.dispose(); 
						cm.openNpc(9010107); 
					break;
					case 3:
						cm.dispose(); 
						cm.openNpc(3003162); 
					break;
					case 4:
						cm.dispose(); 
						cm.openNpc(3003252); 
					break;
					case 5:
						cm.dispose(); 
						cm.openNpc(3003480); 
					break;
                                        case 6:
						cm.dispose(); 
						cm.openNpc(3003756); 
					break;
				}
			break;
			case 2:
				switch (sel) {
					case 1:
						cm.dispose(); 
						cm.openNpc(1540895);  
					break;
					case 2:
						cm.dispose(); 
						cm.openNpc(2155000);  
					break;
					case 3:
						cm.dispose(); 
						cm.openNpc(3003104);  
					break;
					case 4:
						cm.dispose(); 
						cm.openNpc(3003162);  
					break;
					case 5:
						cm.dispose(); 
						cm.openNpc(3003252);  
					break;
                                        case 6:
						cm.dispose(); 
						cm.openNpc(3003326);  
					break;
                                        case 7:
						cm.dispose(); 
						cm.openNpc(3003480); 
					break;
                                        case 8:
						cm.dispose(); 
						cm.openNpc(3003756); 
					break;
                                        case 9:
						cm.dispose(); 
						cm.openNpc(2082006); 
					break;
                                        case 10:
						cm.dispose(); 
						cm.openNpc(3004540); 
					break;
case 11:
						cm.dispose(); 
						cm.openNpc(9000368); 
					break;
				}
			break;
			case 3:
				switch (sel) {
					case 1:
						cm.dispose(); 
						cm.openNpc(3004414); 
					break;
					case 2:
						cm.dispose(); 
						cm.openNpc(9062453); 
					break;
					case 3:
						cm.dispose(); 
						cm.openNpc(3003381); 
					break;
					case 4:
						cm.dispose(); 
						cm.openNpc(9074300); 
					break;
					case 5:
						cm.dispose(); 
						cm.warp(993001000); 
					break;
					case 6:
						cm.dispose(); 
						cm.openNpc(9062148); 
					break;
 
					case 7:
						cm.dispose(); 
						cm.openNpc(2121020); 
					break;
 
				}
			break;
			case 4:
				switch (sel) {
					case 1:
						cm.dispose(); 
						cm.openNpc(2008); 
					break;
					case 2:
						cm.dispose(); 
						cm.openNpc(2008); 
					break;
					case 3:
						cm.dispose(); 
						cm.openNpc(2008); 
					break;
					case 4:
						cm.dispose(); 
						cm.openNpc(2008); 
					break;
					case 5:
						cm.dispose(); 
						cm.openNpc(2008); 
					break;
				}
			break;
			case 5:
				switch (sel) {
					case 1:
						cm.dispose(); 
						cm.openNpc(2010007); 
					break;
					case 2:
						cm.dispose(); 
						cm.openNpc(2010009); 
					break;
					case 3:
							cm.dispose(); 
						cm.warp(200000301,  1);
					break;
					case 4:
						cm.dispose(); 
						cm.openNpc(2008); 
					break;
					case 5:
						cm.dispose(); 
						cm.openNpc(2008); 
					break;
				}
			break;
                        case 6:
				switch (sel) {
					case 1:
						cm.dispose(); 
						cm.warp(680000000,  1);
					break;
					case 2:
						cm.dispose(); 
						cm.openNpc(1031001); 
					break;
					case 3:
						cm.dispose(); 
						cm.openNpc(9400340); 
					break;
					case 4:
						cm.dispose(); 
						cm.openNpc(2008); 
					break;
					case 5:
						cm.dispose(); 
						cm.openNpc(2008); 
					break;
				}
			break;
                       case 7:
				switch (sel) {
					case 1:
						cm.dispose(); 
						cm.openNpc(9062288); 
					break;
					case 2:
						cm.dispose(); 
						cm.warp(910530100,  0);
					break;
					case 3:
						cm.dispose(); 
						cm.warp(109040001,  0);
					break;
					case 4:
						cm.dispose(); 
						cm.warp(100000202,  0);
					break;
					case 5:
						cm.dispose(); 
						cm.warp(220000006,  0);
					break;
				}
			break;
                        case 8:
				switch (sel) {
					case 1:
						cm.dispose(); 
						cm.warp(680000000,  1);
					break;
					case 2:
						cm.dispose(); 
						cm.openNpc(1540101); 
					break;
					case 3:
						cm.dispose(); 
						cm.openNpc(9000381); 
					break;
					case 4:
						cm.dispose(); 
						cm.openNpc(9000224); 
					break;
					case 5:
						cm.dispose(); 
						cm.openNpc(9001153); 
					break;
					case 6:
						cm.dispose(); 
						cm.openNpc(1540110); 
					break;
				}
			break;
		}
	}
}
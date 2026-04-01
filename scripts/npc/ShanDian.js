



importPackage(Packages.handling.channel.handler);
파랑 = "#fc0xFF0054FF#";
연파 = "#fc0xFF6B66FF#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
검정 = "#fc0xFF191919#";
분홍 = "#fc0xFFFF5AD9#";

var white = "#fc0xFFFFFFFF#";
var enter = "\r\n";
var talkType = 0x86;

틀 = "#fUI/Basic.img/actMark/2#";
设备 = "#k 裝備商店 #r";
消费 = "#k 消耗商店 #r";
强化 = "#k 强化商店 #r";
硬币 = "#k 硬幣商店 #r";
製作 = "#k 製作商店 #r";
積分 = "#k 積分商店 #r";
新建 = "#k 新建商店 #r";

var enter = "\r\n";
var seld = -1, seld2 = -1;

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
	var msg = "#fs15#"
	if (status == 0) {
        var choose = "                                                  " + 틀 + "#l\r\n";
		choose += "#L3##fc0xFFFF3636##fs20#" + 强化 + "#l";
		choose += "#L2##fc0xFFFF3636##fs20#" + 消费 + "#l";
                choose += "#L1##fc0xFFFF3636##fs20#" + 设备 + "#l";
		choose += "#L4##fc0xFFFF3636##fs20#" + 硬币 + "#l\r\n";
		choose += "#L5##fc0xFFFF3636##fs20#" + 製作 + "#l";
		choose += "#L6##fc0xFFFF3636##fs20#" + 積分 + "#l";
		choose += "#L7##fc0xFFFF3636##fs20#" + 新建 + "#l\r\n";
		cm.sendSimpleS(choose, talkType);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {				
			case 1234:
				cm.dispose();
				cm.openShop(1234);
				break;

			case 1:
				msg += "  #fs20##b[裝備商店]#k#n" + enter + enter;
				msg += "#L4##b輔助裝備#n #k";
				msg += "#L0##b普通飾品#n #k";
				msg += "#L1##b暴君飾品#n #k";
				msg += "#L2##b航海套裝#n #k";
				msg += "#L3##b神秘套裝#n #k" + enter + enter;
				cm.sendSimpleS(msg, talkType);
				break;

			case 2:
				msg += "  #fs20##b[消耗商店]" + enter;
				msg += "#L19##k常用物品A#l";
				msg += "    #L20##k常用物品B#l";
				msg += "    #L10##k安卓機器人#l" + enter + enter;
				msg += "#L11##k寵物商店#l";
				cm.sendSimpleS(msg, talkType);
				break;

			case 3:
				msg += " #e#fs20##b[强化商店]#k#n" + enter;
				msg += "#L12##b强化#n 商店 #r(卷軸，魔方，印章)#k" + enter;
				cm.sendSimpleS(msg, talkType);
				break;

			case 4:
				msg += " #e#fs20##b[硬幣商店]#k#n" + enter;
				msg += "#L5##bBOSS掉落物#n  #k商店 #r( 核心,石頭,寶石 )#k" + enter;
				msg += "#L6##b活動幣掉落物#n #k商店" + enter;
				msg += "#L7##d簽到幣#n #k商店" + enter;
				msg += "#L17#BOSS結晶#n 買賣#k" + enter;
				msg += "#L101##fc0xFFEDD200#旋律幣#n #k商店" + enter;
				msg += "#L100##fc0xFFEDD200#和聲幣#n #k商店" + enter;
				cm.sendSimpleS(msg, talkType);
				break;

			case 5:
				msg += "#L13##fs20##b製作#n 商店 #k#r(漆黑飾品，永遠心臟)#k" + enter + enter;
				msg += "#L14##b光輝之劍#n 製作 #r#k" + enter;
				msg += "#L15##b七日徽章#n 製作 #r#k" + enter;
				msg += "#L16##b創世武器#n 製作 #r#k" + enter;
				cm.sendSimpleS(msg, talkType);
				break;

			case 6:
				msg += "  #e#fs20##b[積分商店]#k#n" + enter;
				msg += "#L9##b狩獵積分#n#k[商店]#k";
				msg += "#L8##b潛水積分#n#k[商店]";
				msg += "#L18##b贊助積分#n#k[商店]" + enter + enter;
				msg += "#L11##fc0xFFEDD200#普通寵物#n#k[商店]";
				msg += "#L10##b機器人購買#n#k[商店]";
				cm.sendSimpleS(msg, talkType);
				break;

			case 7:
				cm.dispose();
				cm.openNpc(3003250);
				break;

			case 8:
				cm.dispose();
				cm.openShop(26); 
				break;
			case 50:
				cm.dispose();
				cm.openNpc(9062453);
				break;
			case 55:
				cm.dispose();
				cm.openShop(9062547);
				break;
			case 57:
				cm.dispose();
				cm.openNpc(9062115);
				break;

		}
	} else if (status == 2) {
		seld2 = sel;
		switch (sel) {
			case 0:
				cm.dispose();
				cm.openShop(29); //쿼리 3003414
				break;
			case 1:
				cm.dispose();
				cm.openShop(22);
				break;
			case 2:
				cm.dispose();
				cm.openShop(10);
				break;
			case 3:
				cm.dispose();
				cm.openShop(13);
				break;
			case 4:
				cm.dispose();
				cm.openShop(2);//보조장비상점(쿼리)
				break;
			case 5:
				cm.dispose();
				cm.openShop(38); //BOSS寶石掉落兌換商店 通用  寶石
				break;
			case 6:
				cm.dispose();
				cm.openShop(33);//7周年硬幣商店 方塊 火花 强化  經驗 星卷 紀念幣 奇幻等
				break;
			case 7:
				cm.dispose();
				cm.openShop(44);// 活動幣商店  魔方  袋子 20星卷 等  常用型  會議憑證幣
				break;
			case 8:
				cm.dispose();
				cm.openNpc(9062453);//潛水積分商店
				break;
			case 9:
				cm.dispose();
				cm.openNpc(9062115);//狩獵積分商店
				break;
			case 10:
				cm.dispose();
				cm.openShop(19);//安卓機器人等通用性   金幣
				break;
			case 11:
				cm.dispose();
				cm.openNpc(1530330);
				break;
			case 12:
				cm.dispose();
				cm.openShop(14);
				break;
			case 13:
				cm.dispose();
				cm.openNpc(3006009);//製作#n 商店 #k#r(漆黑飾品，永遠心臟)
				break;
			case 14:
				cm.dispose();
				cm.openNpc(3004142);//光輝之劍#n 製作
				break;
			case 15:
				cm.dispose();
				cm.openNpc(1052223);//七日徽章#n 製作 
				break;
			case 16:
				cm.dispose();
				cm.openNpc(3004100);//創世武器#n 製作
				break;
			case 17:
				cm.dispose();
				cm.openShop(32); //BOSS結晶買賣商店 通用性
				break;
			case 18:
				cm.dispose();
				cm.openNpc(1540101); //贊助積分商店
				break;
                        case 19:
				cm.dispose();
				cm.openShop(1); //普通
				break;
			case 20:
				cm.dispose();
				cm.openShop(27); //普通
				break;
			case 100:
                cm.sendOk("#fs15# #b 準備中的商店.");
                cm.dispose();
				break;
			case 101:
                                cm.dispose();
				cm.openShop(4); //活动币商店2
				break;


		}

	}
}
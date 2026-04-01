var status = -1;

importPackage(Packages.server);
importPackage(Packages.client.inventory);

분홍콩 = Randomizer.rand(50,100);
강환불 = Randomizer.rand(1,10);
영환불 = Randomizer.rand(1,5);
레드 = Randomizer.rand(50, 150);
블랙 = Randomizer.rand(10, 30);
에디 = Randomizer.rand(10, 30);
메소 = Randomizer.rand(10000000, 100000000);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
   listitem = [[5062009, 레드, 100], [5062010, 블랙, 100], [5062500, 에디, 100], [2633610, 분홍콩, 100], [2048759, 강환불, 50], [2048758, 영환불, 50], [5062006, 1, 10], [5068300, 1, 15], [5069000, 1, 35]]; // 아이템,개수,확률 (확률 1 = 1%, 100 = 100%);
	reqitem = [[2430047, 1]];
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNoS("#b슈피겔만#k씨가 준비해준 상자다.. 개봉해볼까?", 2);
	} else if (status == 1) {
		for (i=0; i<reqitem.length; i++) {
			if (cm.itemQuantity(reqitem[i][0]) < reqitem[i][1]) {
				cm.sendOkS("핫타임 상자를 사용하기 위한 아이템이 부족한 것 같다..", 2);
				cm.dispose();
				return;
			}
		}
		for (i=0; i<reqitem.length; i++) {
			cm.gainItem(reqitem[i][0], -reqitem[i][1]);
		}
        말 = "상자에서 다양한 아이템이 나왔다..\r\n\r\n";
		for (i=0; i<listitem.length; i++) {
            rd = Math.floor(Math.random() * 100);
			if (listitem[i][2] >= rd) {
                말 += "#i"+listitem[i][0]+"# #b#z"+listitem[i][0]+"# #r("+listitem[i][1]+"개)\r\n";
				cm.gainItem(listitem[i][0], listitem[i][1]);
			}
		}
        말 += "#i5200002# #b"+메소+" #r메소#k";
		cm.gainMeso(메소);
		cm.sendOkS(말, 2);
		cm.dispose();
    }
  }

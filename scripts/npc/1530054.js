importPackage(Packages.handling.channel.handler);

var enter = "\r\n";
var seld = -1, seld2 = -1;
틀 = "#fUI/Basic.img/actMark/1#";

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
        var msg = "                    " + 틀 + "#l\r\n";
		msg += "#fc0xFFD5D5D5#──────────────────────────#k\r\n";
		msg += "#fs15##b#h ##k您的贊助積分 : " + cm.getPlayer().getDonationPoint() + "  | #b#h ##k您的文宣點 : " + cm.getPlayer().getHPoint() + "\r\n\r\n#l";
		msg += "#L1# #b我想使用贊助系統。.#n #k\r\n";
		//msg += "#L4# #r후원 포인트를 교환하고 싶어요.#n #k\r\n";
		msg += "#L2# #b我想使用文宣系統。.#n #k\r\n";
		//msg += "#L3# #b누적버프를 구매하고싶어요.#n #k\r\n";

		cm.sendSimpleS(msg, 4);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 1530054, "9062884");
				break;
			case 2:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 1530054, "3003167");
				break;
			case 3:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3001604, "9062459");
				break;
            case 4:
				cm.dispose();
				cm.openShop(1234); //몬스터 코어(쿼리)
				break;			
		}
	}
}
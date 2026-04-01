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
        var msg = "                                       " + 틀 + "#l\r\n";
		msg += "#fs15##b#h ##k的贊助積分 : " + cm.getPlayer().getDonationPoint() + "======#b#h ##k的推廣積分 : " + cm.getPlayer().getHPoint() + "\r\n#l";
		msg += "#L1# #b我想使用贊助系統。.#n #k";
		msg += "#L2# #b我想使用推廣系統。.#n #k\r\n";
		msg += "#L4# #b我想兌換贊助積分.#n #k";
		msg += "   #L3# #b想購買累計Buff.#n #k\r\n";
		msg += "#L5# #b舊贊助系統.#n #k";

		cm.sendSimpleS(msg, 0x86);
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
				cm.openShop(38); //怪物覈心（査詢）
				break;	
            case 5:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3001604, "Reminence_Donation");
				break;		
		}
	}
}
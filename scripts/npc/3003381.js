var enter = "\r\n";
var seld = -1;
var seld2 = -1;
 
var 徽章後援點 = 10000, 徽章宣傳點 = 10000;
 
var 口袋後援點 = 10000, 口袋宣傳點 = 10000;
 
var 現金後援點 = 5000, 現金宣傳點 = 15000;
 
var p = -1, need = -1;
var pt = "";
 
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
		var msg ="#fs15#您好 #b#h 0##k！您知道沒有潛能的物品也可以賦予潛能嗎？ #fs15##b"+enter;
		msg += "#L1#徽章物品潛能賦予"+enter;
		msg += "#L2#口袋物品潛能賦予"+enter;
		//msg += "#L6#現金物品潛能賦予"+enter;
		cm.sendSimple(msg); 
	} else if (status == 1) {
		seld = sel;
		var msg = "#fs15#您想使用哪種點數賦予潛能？\r\n";
		後援點 = sel == 1 ? 徽章後援點 : sel == 2 ? 口袋後援點 : 現金後援點;
		宣傳點 = sel == 1 ? 徽章宣傳點 : sel == 2 ? 口袋宣傳點 : 現金宣傳點;
		//msg += "#L1#後援點數 ("+後援點+"P)\r\n";
		msg += "#L2#宣傳點數 ("+宣傳點+"P)\r\n";
		cm.sendSimple(msg); 
	} else if (status == 2) {
		seld2 = sel;
		p = seld2 == 1 ? cm.getPlayer().getDonationPoint()  : cm.getPlayer().getHPoint(); 
		pt = seld2 == 1 ? "後援" : "宣傳";
		need = (seld2 == 1 && seld == 1) ? 徽章後援點 : (seld2 == 1 && seld == 2) ? 口袋後援點 : (seld2 == 1 && seld == 3) ? 現金後援點 : (seld2 == 2 && seld == 1) ? 徽章宣傳點 : (seld2 == 2 && seld == 2) ? 口袋宣傳點 : 現金宣傳點;
		seldi = seld == 1 ? "徽章" : seld == 2 ? "口袋" : "現金";
		ic = seld == 1 ? 118 : 116;
 
		if (p < need) {
			cm.sendOk("#fs15# 點數不足。");
			cm.dispose(); 
			return;
		}
 
		var msg ="#fs15#您好 #b#h 0##k！您知道"+seldi+"可以賦予潛能嗎？請選擇您想要的物品。\r\n#r(附加潛能未開放)#k"+enter;
		msg += "#fs15#目前 #b#h 0##k的 #d"+pt+"點數#k是 "+p+"P。#fs15#"+enter+enter;
		switch (seld) {
			case 1:
				for (i = 0; i < cm.getInventory(1).getSlotLimit();  i++) {
					item = cm.getInventory(1).getItem(i); 
           					if (item == null) continue;
					if (Math.floor(item.getItemId()  / 10000) == ic) msg += "#L"+i+"# #i"+item.getItemId()+"#  #b#z"+item.getItemId()+"#\r\n"; 
				}
			break;
			case 2:
				for (i = 0; i < cm.getInventory(1).getSlotLimit();  i++) {
					item = cm.getInventory(1).getItem(i); 
           					if (item == null) continue;
					if (Math.floor(item.getItemId()  / 10000) == ic) msg += "#L"+i+"# #i"+item.getItemId()+"#  #b#z"+item.getItemId()+"#\r\n"; 
				}
			break;
			case 3:
				for (i = 0; i < cm.getInventory(1).getSlotLimit();  i++) {
					item = cm.getInventory(1).getItem(i); 
           					if (item == null) continue;
					if (cm.isCash(item.getItemId()))  msg += "#L"+i+"# #i"+item.getItemId()+"#  #b#z"+item.getItemId()+"#\r\n"; 
				}
			break;
		}
		cm.sendSimple(msg); 
	} else if (status == 3) {
		if (p < need) {
			cm.sendOk("#fs15# 點數不足。");
			cm.dispose(); 
			return;
		}
		if (seld2 == 1) 
                        cm.getPlayer().gainDonationPoint(-30000); 
		else cm.getPlayer().gainHPoint(-30000); 
		item = cm.getInventory(1).getItem(sel); 
 
		if (item == null) {
			cm.dispose(); 
			return;
		}
		item.setState(17); 
		item.setLines(3); 
		item.setPotential1(10041); 
		item.setPotential2(10042); 
		item.setPotential3(10043); 
		cm.sendOk("#fs15# 已為物品賦予潛能。感謝您的使用。");
		cm.getPlayer().forceReAddItem(item,  Packages.client.inventory.MapleInventoryType.getByType(1)); 
		cm.dispose(); 
	}
}
	importPackage(Packages.client.inventory);
	importPackage(Packages.server);
	importPackage(Packages.constants);
	importPackage(java.lang);

function start() { Status = -1; action(1, 0, 0); }

function action(M, T, S) {
	if(M == -1) { cm.dispose(); } else {
		if(M == 0) { cm.dispose(); return; }
		if(M == 1) Status++; else Status--;


		if(Status == 0) {
		cm.sendSimple("#fs15#可以丟棄庫存中的緩存物品. 選擇想要丟棄的物品的庫存………"
			+ "\r\n#L0#緩存設備清點"
			+ "\r\n#L2#設備清點"
			+ "\r\n#L1#缓存清点");
		}


		else if(Status == 1) {
		Say = "#fs15#選擇要扔掉的物品…扔掉一次的物品就再也找不回來了. 如果有多個相同的東西, 就會放弃選擇的東西.";
		S1 = S;
			switch(S1) {
			case 0: // 設備清點
			EQUIP = cm.getInventory(1);
				for (i = 0; i < EQUIP.getSlotLimit(); i++) {
					if(EQUIP.getItem(i) == null || !cm.isCash(EQUIP.getItem(i).getItemId())) { continue; }
				Say += "#L" + i + "##i"+EQUIP.getItem(i).getItemId()+"# #z"+EQUIP.getItem(i).getItemId()+"##l\r\n";
				}
			cm.sendSimple(Say);
			break;
			case 2: // 設備清點
			EQUIP = cm.getInventory(1);
				for (i = 0; i < EQUIP.getSlotLimit(); i++) {
					if(EQUIP.getItem(i) == null) { continue; }
				Say += "#L" + i + "##i"+EQUIP.getItem(i).getItemId()+"# #z"+EQUIP.getItem(i).getItemId()+"##l\r\n";
				}
			cm.sendSimple(Say);
			break;
			case 1: // 現金清點
			CASH = cm.getInventory(5);
				for (i = 0; i < CASH.getSlotLimit(); i++) {
					if(CASH.getItem(i) == null || !cm.isCash(CASH.getItem(i).getItemId())) { continue; }
				Say += "#L" + i + "##i"+CASH.getItem(i).getItemId()+"# #z"+CASH.getItem(i).getItemId()+"##l\r\n";
				}
			cm.sendSimple(Say);
			break;
			}
		}


		else if(Status == 2) {
		S2 = S;
		cm.sendSimple("#fs15#真的要處理所選物品嗎?\r\n#b"
			+ "#L1#删除項目.\r\n\r\n\r\n#l"
			+ "#L0#扔掉道具.\r\n\r\n\r\n#l"
			+ "#e#r(這次决定是最後一次了. 决定的瞬間丟棄或删除的物品將無法找回.)#k#n");
		}

		
		else if(Status == 3) {
		S3 = S;
			switch(S3) {
			case 0: // 필드에 버린다.
			if (GameConstants.isPet(cm.getInventory(5).getItem(S2).getItemId())) {
			cm.getPlayer().dropMessage(1, "寵物絕對不能扔.\r\n他們也是一條生命.\r\n\r\n你不負責任的行為對這個小生命來說可能是一大不幸.");
			cm.dispose(); return;
			}

				switch(S1) {
				case 0: cm.dropItem(S2, 1, 1); break;
				case 1: cm.dropItem(S2, 5, 1); break;
				}
			break;

			case 1: // 삭제한다.
				switch(S1) {
                                case 2:
				case 0: MapleInventoryManipulator.removeFromSlot(cm.getClient(), MapleInventoryType.EQUIP, S2, 1, false); break;
				case 1: MapleInventoryManipulator.removeFromSlot(cm.getClient(), MapleInventoryType.CASH, S2, cm.getInventory(5).getItem(S2).getQuantity(), false); break;
				}
			break;
			}

		cm.sendNext("成功處理了所選物品!");
		cm.dispose();
		}
	}
}

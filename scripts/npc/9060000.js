qty = [];
box = 2433019;

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		itemcheck();
		var text = "#fs15#我叫動物園馴獸師健太。 不能隨便 給籠子裏的動物投食。\r\n\r\n";
		if (NumberMin > 0) {
			text += "#fUI/UIWindow.img/UtilDlgEx/list3#\r\n";
		} else {
			text += "#fUI/UIWindow.img/UtilDlgEx/list1#\r\n";
		}
		text += "#L0##d隱藏的紙片#k#l";
		cm.sendSimple(text);
	} else if (status == 1) {
		if (selection == 0) {
			if (NumberMin > 0) {
				var text = "哇，真的把紙片都找回來了！ 很驚人啊？\r\n"
				text += "好，那我們確認一下收集了多少紙片？\r\n\r\n";
				text += "#fc0xffA6A6A6# * 您要完成多少次任務？#k\r\n"
				text += "#r * 你 #e"+NumberMin+"最多#n 可以完成任務.#k";
				cm.sendGetNumber(text, 1, 1, NumberMin);	
			} else {
				var text = "#fs15#我在 Fancy 世界裏藏了 #i4031274# #b紙片#k\r\n";
				text += "狩獵怪物收集 #r#eA~E#n#k 的 #b5種#k紙片 給我?#k \r\n\r\n";
				text += "如果收集了所有的紙片，請再來。\r\n";
				text += "#i" + box + "# #b#z" + box + "##k 可以得到的道具。";
				cm.sendOk(text);
				cm.dispose();
			}
		}
	} else if (status == 2) {
		st = selection;
		if (st < 0 || st > NumberMin) {
			cm.sendOk("檢測到异常值並退出腳本。");
			fw = new java.io.FileWriter("gainItemLog/log_9060000 (紙片).txt", true);
			fw.write("" + new Date() + " │ cid : "+cm.getPlayer().getId()+" │ cname : "+ cm.getPlayer().getName() + " 引導异常 ["+st+"] 的值\r\n");
			fw.close();
			cm.dispose();
			return;
		}
		if (!cm.canHold(box)) {
		    cm.sendOk("消費視窗不足。");
		    cm.dispose();
		    return;
		}
		for (i = 0; i < 5; i++) {
		    cm.getPlayer().removeItem(4031274 + i, -st);
		}
		var text = "按照約定，我把紙片換成了有用的物品。\r\n";
		text += "藏起來的紙片還有很多，每次找到的時候都來找我。\r\n\r\n";
		text += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";
		text += "#i" + box + "# #b#z" + box + "##k #r" + st + "個#k";
		cm.sendOk(text);
		cm.gainItem(box, st);
		if (st > 1) {
			cm.getPlayer().dropMessage(6, "[任務]一次性處理了 "+st+"次紙片任務。");
		}
		fw.close();
		cm.dispose();
		return;
	}
}

function itemcheck() {
	for (i = 0; i < 5; i++) {
	    var a = cm.itemQuantity(4031274 + i);
	    qty.push(a);
	}
	NumberMin = qty[0];
	for (j = 0; j < qty.length; j++) {
	    if (NumberMin > qty[j]) {
		NumberMin = qty[j];
	    }
	}
}
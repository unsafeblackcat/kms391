importPackage(Packages.server);
importPackage(java.lang);
importPackage(Packages.tools.packet);
importPackage(java.util);


var quest = "헤이븐1";//天堂1
var 제한레벨 = 200;   //等级 200
var enter = "\r\n";
var seld = -1;
var isitem = false;
var isclear;
var year, month, date2, date, day
var rand;

var daily = [ // 첫번째
{'mobid' : 8250005, 'qty' : 301, 't' : 1}, 
{'mobid' : 8250013, 'qty' : 302, 't' : 1},
{'mobid' : 8250010, 'qty' : 303, 't' : 1},
{'mobid' : 8250011, 'qty' : 304, 't' : 1},
{'mobid' : 8250000, 'qty' : 305, 't' : 1}, 
{'mobid' : 8250001, 'qty' : 306, 't' : 1}
]

var daily2  = [ //두번째
{'mobid' : 8250005, 'qty' : 301, 't' : 1}, 
{'mobid' : 8250013, 'qty' : 302, 't' : 1},
{'mobid' : 8250010, 'qty' : 303, 't' : 1},
{'mobid' : 8250011, 'qty' : 304, 't' : 1},
{'mobid' : 8250000, 'qty' : 305, 't' : 1}, 
{'mobid' : 8250001, 'qty' : 306, 't' : 1}
]

var reward;



function start() {
	status = -1;
	action(1, 0, 0);
}

function getk(key) {
	return cm.getPlayer().getKeyValue(201801, date+"_"+quest+"_"+key);
}
function setk(key, value) {
	cm.getPlayer().setKeyValue(201801, date+"_"+quest+"_"+key, value);
}

function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		if (cm.getPlayer().getLevel() < 제한레벨) {
			cm.sendOk("等級不對。");
			cm.dispose();
			return;
		}
		getData();
		switch(day){
    			case 0:
        			var d = "일";
        			reward = [
			{'itemid' : 4001842, 'qty' : 3}
        			
				]
        		break;
    			case 1:
        			var d = "월";
        			reward = [
			{'itemid' : 4001842, 'qty' : 3}
				]
        			break;
    			case 2:
        			var d = "화";
        			reward = [
			{'itemid' : 4001842, 'qty' : 3}
				]
        		break;
    			case 3:
        			var d = "수";
        			reward = [
			{'itemid' : 4001842, 'qty' : 3}
				]
        		break;
    			case 4:
        			var d = "목";
        			reward = [
			{'itemid' : 4001842, 'qty' : 3}
				]
        		break;
    			case 5:
        			var d = "금";
        			reward = [
			{'itemid' : 4001842, 'qty' : 3}
				]
        		break;
    			case 6:
        			var d = "토";
        			reward = [
			{'itemid' : 4001842, 'qty' : 3}
				]
        		break;
		}

		rand = daily[Randomizer.rand(0, daily.length-1)];
		isitem = rand['t'] == 2 ? true : false;
		isclear = getk("isclear");
		if (isclear == -1) {
			setk("mobid", isitem ? rand['itemid']+"" : rand['mobid']+"");
			setk("mobq", rand['qty']+"");
			setk("isclear", "1");
			setk("isitem", isitem ? "2" : "1");
			setk("count", "0");
		}
		if (isclear == 3) {
			rand = daily2[Randomizer.rand(0, daily2.length-1)];
			isitem = rand['t'] == 2 ? true : false;
			setk("mobid", isitem ? rand['itemid']+"" : rand['mobid']+"");
			setk("mobq", rand['qty']+"");
			setk("isitem", isitem ? "2" : "1");
			setk("count", "0");
			setk("isclear", "4");
		}
		isclear = getk("isclear");
		isitem = getk("isitem") == 2 ? true : false;

		if (isclear == 2 || isclear == 5) {
			if ((isitem && (cm.haveItem(getk("mobid"), getk("mobq")))) || (!isitem && (getk("count") >= getk("mobq")))) {
				if (isitem) cm.gainItem(getk("mobid"), -getk("mobq"));
				var msg = "#b#h 謝謝 ##k 我不會忘記這份恩惠的."+enter;
				if (isclear == 5) {
					msg += "這裡，這是對他的補償。."+enter;
					msg += getReward()+enter;
				}
				msg += "#k以後也請多多關照。."+enter;

				if (isclear == 2)
					msg += "如果你再完成一個我的委託，我就給你每日獎勵。\r\n再跟我搭話吧。.\r\n獲得獎勵前請清空20格以上的裝備視窗.";
				else 
					msg += "今天好像這樣也可以！ 明天再來找我吧。";

				if (isclear == 2)
					setk("isclear", "3");
				else if (isclear == 5)
					setk("isclear", "6");

				cm.sendOk(msg);
				cm.dispose();
			}
		}

		if (isclear == 5) {
			if ((isitem && (cm.haveItem(getk("mobid"), getk("mobq")))) || (!isitem && (getk("count") >= getk("mobq")))) {
				if (isitem) cm.gainItem(getk("mobid"), -getk("mobq"));
				setk("isclear", "6");
				var msg = "#fs15#謝謝你#b#h ##k 我不會忘記這份恩惠的."+enter;
				msg += "這裡，這是對他的補償。."+enter;
				msg += getReward()+enter;
				msg += "#k以後也請多多關照。."+enter;
				msg += "如果你再完成一個我的委託，我就給你每日獎勵。\r\n請再跟我搭話。";
				cm.sendOk(msg);
				cm.dispose();
			}
		}
		switch (isclear) {
			case 1:
				var msg = "#fs15#歡迎光臨。 瘦猴。"+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#d#L1#(Lv."+제한레벨+") #b#e[每日任務]#n#k #d黑色天堂 '機器墳墓'";

				cm.sendSimple(msg);
			break;
			case 2:
				var msg = "#fs15#Hai #b#h ##k!  今天的委託有這些！"+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[每日任務] : 黑色天堂 '機器墳墓'#n#k"+enter;
				msg += "處理對象 : #b#o"+getk("mobid")+"# "+getk("mobq")+"怪物數量#k"+enter+enter;
				msg += "完成任務回來。";

				cm.sendOk(msg);
				cm.dispose();
			break;
			case 4:
				var msg = "#fs15#歡迎光臨。 瘦猴。"+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#d#L1#(Lv."+제한레벨+") #b#e[每日任務]#n#k #d黑色天堂 '機器墳墓' 2";

				cm.sendSimple(msg);
			break;
			case 5:
				var msg = "#fs15#Hai #b#h ##k! 今天的委託有這些！"+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[每日任務] : 黑色天堂 '機器墳墓'#n#k"+enter;
				msg += "\r\n목표 전리품 : #b#i"+getk("mobid")+"##z"+getk("mobid")+"# "+getk("mobq")+"개#k"+enter+enter;
				msg += "那麼，工作結束後再來吧。";

				cm.sendOk(msg);
				cm.dispose();
			break;
			case 6:
				var msg = "#fs15#歡迎光臨~#h #."+enter;
				msg += "今天好像這樣也可以！ 明天再來找我吧。"+enter;

				cm.sendOk(msg);
				cm.dispose();
			break;
		}
	} else if (status == 1) {
		if (getk("isclear") == 1 || getk("isclear") == 4) {
			var msg = "#fs15##kHai #b#h #!#k 今天的委託有這些！"+enter+enter;
			msg += isitem ? "#r#e【每日任務】 : 黑色天堂 '機器墳墓'#n#k"+enter : "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[每日任務] : 黑色天堂 '機器墳墓'#n#k"+enter;
			msg += isitem ? "\r\n目標戰利品 : #b#i"+getk("mobid")+"##z"+getk("mobid")+"# "+getk("mobq")+"個#k"+enter+enter : "處理對象 : #b#o"+getk("mobid")+"# "+getk("mobq")+"怪物數量#k"+enter+enter;
			msg += "是否接受任務？";

			cm.sendYesNo(msg);
		}
	} else if (status == 2) {
		var msg = "#fs15#Hai #b#h #!#k 今天的委託有這些！"+enter+enter;
		msg += isitem ? "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[每日任務] : 黑色天堂 '機器墳墓'#n#k"+enter : "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[每日任務] : 黑色天堂 '機器墳墓'#n#k"+enter;
		msg += isitem ? "\r\n目標戰利品 : #b#i"+getk("mobid")+"##z"+getk("mobid")+"# "+getk("mobq")+"個#k"+enter+enter : "處理對象 : #b#o"+getk("mobid")+"# "+getk("mobq")+"怪物數量#k"+enter+enter;
		msg += "那麼，工作結束後再來吧。\r\n獲得獎勵前請清空20格以上的裝備視窗。";
		cm.sendOk(msg);
		cm.dispose();
		if (getk("isclear") == 1)
			setk("isclear", "2");
		else if (getk("isclear") == 4)
			setk("isclear", "5");
	}
}

function getReward() {
	var msg = "#b";
	//var rand1 = reward[Randomizer.rand(0, reward.length-1)];
	//var rand2 = reward[Randomizer.rand(0, reward.length-1)];
	for (i = 0; i < reward.length; i++) {
		cm.gainItem(reward[i]['itemid'], reward[i]['qty']);
		cm.getPlayer().AddStarDustPoint(20,"", cm.getPlayer().getTruePosition());
		msg += "#i"+reward[i]['itemid']+"##z"+reward[i]['itemid']+"# "+reward[i]['qty']+"개"+enter;
	}
	//cm.gainItem(rand1['itemid'], rand1['qty']);
	//cm.gainItem(rand2['itemid'], rand2['qty']);
	//msg += "#i"+rand1['itemid']+"##z"+rand1['itemid']+"# "+rand1['qty']+"개"+enter;
	//msg += "#i"+rand2['itemid']+"##z"+rand2['itemid']+"# "+rand2['qty']+"개"+enter;
	return msg;
}

function getData() {
	time = new Date();
	year = time.getFullYear();
	month = time.getMonth() + 1;
	if (month < 10) {
		month = "0"+month;
	}
	date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
	date = Integer.parseInt(year+""+month+""+date2);
	day = time.getDay();
}

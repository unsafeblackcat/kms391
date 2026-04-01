importPackage(Packages.server); 
importPackage(java.lang); 
 
var quest = "aerkana";
var 限制等級 = 225;
var enter = "\r\n";
var seld = -1;
var isitem = false;
var isclear;
var year, month, date2, date, day
var rand;
 
var daily = [ // 第一個 
{'mobid' : 8644000, 'qty' : 200, 't' : 1}, 
{'mobid' : 8644001, 'qty' : 201, 't' : 1},
{'mobid' : 8644003, 'qty' : 202, 't' : 1},
{'mobid' : 8644009, 'qty' : 203, 't' : 1},
{'mobid' : 8644008, 'qty' : 204, 't' : 1},
{'mobid' : 8644010, 'qty' : 205, 't' : 1}, 
{'mobid' : 8644008, 'qty' : 206, 't' : 1}
]
 
var daily2  = [ //第二個
{'mobid' : 8644000, 'qty' : 201, 't' : 1}, 
{'mobid' : 8644001, 'qty' : 202, 't' : 1},
{'mobid' : 8644003, 'qty' : 203, 't' : 1},
{'mobid' : 8644009, 'qty' : 204, 't' : 1},
{'mobid' : 8644008, 'qty' : 205, 't' : 1},
{'mobid' : 8644010, 'qty' : 206, 't' : 1}, 
{'mobid' : 8644008, 'qty' : 207, 't' : 1}
]
 
var reward;
 
 
 
function start() {
	status = -1;
	action(1, 0, 0);
}
 
function getk(key) {
	return cm.getPlayer().getKeyValue(201801,  date+"_"+quest+"_"+key);
}
function setk(key, value) {
	cm.getPlayer().setKeyValue(201801,  date+"_"+quest+"_"+key, value);
}
 
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose(); 
		return;
    	}
	if (status == 0) {
		if (cm.getPlayer().getLevel()  < 限制等級) {
			cm.sendOk(" 請維持阿爾卡納的美麗..");
			cm.dispose(); 
			return;
		}
		getData();
		switch(day){
    			case 0:
        			var d = "日";
        			reward = [
				{'itemid' : 1712004, 'qty' : 20}
				]
        		break;
    			case 1:
        			var d = "月";
        			reward = [
				{'itemid' : 1712004, 'qty' : 10}
			]
 
        			break;
    			case 2:
        			var d = "火";
        			reward = [
				{'itemid' : 1712004, 'qty' : 10}
			]
 
        		break;
    			case 3:
        			var d = "水";
        			reward = [
			{'itemid' : 1712004, 'qty' : 10}
		]
 
        		break;
    			case 4:
        			var d = "木";
        			reward = [
			{'itemid' : 1712004, 'qty' : 10}
		]
 
        		break;
    			case 5:
        			var d = "金";
        			reward = [
			{'itemid' : 1712004, 'qty' : 10}
		]
 
        		break;
    			case 6:
        			var d = "土";
        			reward = [
				{'itemid' : 1712004, 'qty' : 10}
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
			if ((isitem && (cm.haveItem(getk("mobid"),  getk("mobq")))) || (!isitem && (getk("count") >= getk("mobq")))) {
				if (isitem) cm.gainItem(getk("mobid"),  -getk("mobq"));
				var msg = "#b#h ##k多虧您，任務才能順利完成。"+enter;
				if (isclear == 5) {
					msg += "這是今天的獎勵！"+enter;
					msg += getReward()+enter;
				}
				msg += "#k今後也請多多關照！"+enter;
 
				if (isclear == 2)
					msg += "再完成一個任務即可獲得每日獎勵！\r\n請再次與我對話！";
				else 
					msg += "";
 
				if (isclear == 2)
					setk("isclear", "3");
				else if (isclear == 5)
					setk("isclear", "6");
 
				cm.sendOk(msg); 
				cm.dispose(); 
			}
		}
 
		if (isclear == 5) {
			if ((isitem && (cm.haveItem(getk("mobid"),  getk("mobq")))) || (!isitem && (getk("count") >= getk("mobq")))) {
				if (isitem) cm.gainItem(getk("mobid"),  -getk("mobq"));
				setk("isclear", "6");
				var msg = "#b#h ##k多虧您，任務才能順利完成。"+enter;
				msg += "這是今天的獎勵！"+enter;
				msg += getReward()+enter;
				msg += "#k今後也請多多關照。"+enter;
				msg += "再完成一個任務即可獲得每日獎勵！\r\n請再次與我對話！";
				cm.sendOk(msg); 
				cm.dispose(); 
			}
		}
		switch (isclear) {
			case 1:
				var msg = "請維持阿爾卡納的美麗.."+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#d#L1#(Lv."+ 限制等級+") #b#e[每日任務]#n#k #d阿爾卡納";
 
				cm.sendSimple(msg); 
			break;
			case 2:
				var msg = "需要處理的任務如下！"+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[ 每日任務] : 阿爾卡納#n#k"+enter;
				msg += "擊殺目標 : #b#o"+getk("mobid")+"# "+getk("mobq")+"隻#k"+enter+enter;
				msg += "那麼，完成後請再來找我！";
 
				cm.sendOk(msg); 
				cm.dispose(); 
			break;
			case 4:
				var msg = "請維持阿爾卡納的美麗.."+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#d#L1#(Lv."+ 限制等級+") #b#e[每日任務]#n#k #d阿爾卡納 2";
 
				cm.sendSimple(msg); 
			break;
			case 5:
				var msg = "需要處理的任務如下！"+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[ 每日任務] : 阿爾卡納#n#k"+enter;
				msg += "\r\n目標道具 : #b#i"+getk("mobid")+"##z"+getk("mobid")+"# "+getk("mobq")+"個#k"+enter+enter;
				msg += "那麼，完成後請再來找我！";
 
				cm.sendOk(msg); 
				cm.dispose(); 
			break;
			case 6:
				var msg = "歡迎光臨！ #h #。"+enter;
				msg += "今天已經沒有其他任務了。"+enter;
 
				cm.sendOk(msg); 
				cm.dispose(); 
			break;
		}
	} else if (status == 1) {
		if (getk("isclear") == 1 || getk("isclear") == 4) {
			var msg = "需要處理的任務如下！"+enter+enter;
			msg += isitem ? "#r#e[每日任務] : 阿爾卡納#n#k"+enter : "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[ 每日任務] : 阿爾卡納#n#k"+enter;
			msg += isitem ? "\r\n目標道具 : #b#i"+getk("mobid")+"##z"+getk("mobid")+"# "+getk("mobq")+"個#k"+enter+enter : "擊殺目標 : #b#o"+getk("mobid")+"# "+getk("mobq")+"隻#k"+enter+enter;
			msg += "是否接受任務？";
 
			cm.sendYesNo(msg); 
		}
	} else if (status == 2) {
		var msg = "需要處理的任務如下！"+enter+enter;
		msg += isitem ? "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[ 每日任務] : 阿爾卡納#n#k"+enter : "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[ 每日任務] : 阿爾卡納#n#k"+enter;
		msg += isitem ? "\r\n目標道具 : #b#i"+getk("mobid")+"##z"+getk("mobid")+"# "+getk("mobq")+"個#k"+enter+enter : "擊殺目標 : #b#o"+getk("mobid")+"# "+getk("mobq")+"隻#k"+enter+enter;
		msg += "那麼，完成後請再來找我！";
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
	for (i = 0; i < reward.length;  i++) {
		cm.gainItem(reward[i]['itemid'],  reward[i]['qty']);
		cm.getPlayer().AddStarDustPoint(20,"",  cm.getPlayer().getTruePosition()); 
		msg += "#i"+reward[i]['itemid']+"##z"+reward[i]['itemid']+"# "+reward[i]['qty']+"個"+enter;
	}
	return msg;
}
 
function getData() {
	time = new Date();
	year = time.getFullYear(); 
	month = time.getMonth()  + 1;
	if (month < 10) {
		month = "0"+month;
	}
	date2 = time.getDate()  < 10 ? "0"+time.getDate()  : time.getDate(); 
	date = Integer.parseInt(year+""+month+""+date2); 
	day = time.getDay(); 
}
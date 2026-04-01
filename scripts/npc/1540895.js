importPackage(Packages.server);
importPackage(java.lang);

var quest = "야영지";
var 等级 = 200;
var enter = "\r\n";
var seld = -1;
var isitem = false;
var isclear;
var year, month, date2, date, day
var rand;

var daily = [ // 첫번째
	{'mobid' : 3503000, 'qty' : 100, 't' : 1}, 
{'mobid' : 3503001, 'qty' : 100, 't' : 1},
{'mobid' : 3503002, 'qty' : 100, 't' : 1},
{'mobid' : 3503003, 'qty' : 100, 't' : 1},
{'mobid' : 3503004, 'qty' : 100, 't' : 1}, 
{'mobid' : 3503005, 'qty' : 100, 't' : 1}
]

var daily2  = [ //두번째
	{'mobid' : 3503006, 'qty' : 100, 't' : 1}, 
{'mobid' : 3503007, 'qty' : 100, 't' : 1},
{'mobid' : 3503008, 'qty' : 100, 't' : 1},
{'mobid' : 3503009, 'qty' : 100, 't' : 1},
{'mobid' : 3503000, 'qty' : 100, 't' : 1}, 
{'mobid' : 3503001, 'qty' : 100, 't' : 1}
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
		if (cm.getPlayer().getLevel() < 等级) {
			cm.sendOk("得趕緊把那些快要蒸發的埃達斯們潜藏起來！");
			cm.dispose();
			return;
		}
		getData();
		switch(day){
    			case 0:
        			var d = "일";
        			reward = [
			{'itemid' : 4001868, 'qty' : 3}
        			
				]
        		break;
    			case 1:
        			var d = "월";
        			reward = [
			{'itemid' : 4001868, 'qty' : 3}
				]
        			break;
    			case 2:
        			var d = "화";
        			reward = [
			{'itemid' : 4001868, 'qty' : 3}
				]
        		break;
    			case 3:
        			var d = "수";
        			reward = [
			{'itemid' : 4001868, 'qty' : 3}
				]
        		break;
    			case 4:
        			var d = "목";
        			reward = [
			{'itemid' : 4001868, 'qty' : 3}
				]
        		break;
    			case 5:
        			var d = "금";
        			reward = [
			{'itemid' : 4001868, 'qty' : 3}
				]
        		break;
    			case 6:
        			var d = "토";
        			reward = [
			{'itemid' : 4001868, 'qty' : 3}
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
				var msg = "#fs15##b#h 謝謝##k，我不會忘記這份恩惠的。"+enter;
				if (isclear == 5) {
					msg += "#fs15#여기, 이건 그에 대한 보상이야."+enter;
					msg += getReward()+enter;
				}
				msg += "#fs15##k앞으로도 잘 부탁해."+enter;

				if (isclear == 2)
					msg += "#fs15#如果你再完成一個我的委託，我就給你每日獎勵。 \r\n請再跟我搭話。";
				else 
					msg += "#fs15#今天就這樣！ 明天再來。";

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
				var msg = "#fs15#謝謝#b#h##k我不會忘記這份恩惠的。"+enter;
				msg += "這裡，這是給你的報酬。"+enter;
				msg += getReward()+enter;
				msg += "#k以後也請多多關照."+enter;
				msg += "如果再完成一個我的委託，我會給你支付每日獎勵。\r\n請再次搭話。";
				cm.sendOk(msg);
				cm.dispose();
			}
		}
		switch (isclear) {
			case 1:
				var msg = "#fs15#請拯救墮落的世界吧…"+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#d#L1#(Lv."+等级+") #b#e[每日任務]#n#k #d探索墮落的世界樹";

				cm.sendSimple(msg);
			break;
			case 2:
				var msg = "#fs15#再見 #b#h ##k! 今天的委託有這些。."+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[每日任務] : 墮落的世界樹#n#k"+enter;
				msg += "目標 : #b#o"+getk("mobid")+"# "+getk("mobq")+"怪物數量#k"+enter+enter;
				msg += "完成任務後回來。";

				cm.sendOk(msg);
				cm.dispose();
			break;
			case 4:
				var msg = "#fs15#請拯救墮落的世界樹吧…"+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#d#L1#(Lv."+等级+") #b#e[每日任務]#n#k #d墮落的世界樹 2";

				cm.sendSimple(msg);
			break;
			case 5:
				var msg = "#fs15#你好 #b#h ##k! 今天的請求如下."+enter+enter;
				msg += "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[每日任務] : 墮落的世界樹#n#k"+enter;
				msg += "\r\n目標戰利品 : #b#i"+getk("mobid")+"##z"+getk("mobid")+"# "+getk("mobq")+"個#k"+enter+enter;
				msg += "完成任務後回來。";

				cm.sendOk(msg);
				cm.dispose();
			break;
			case 6:
				var msg = "#fs15#歡迎~#h #."+enter;
				msg += "我想今天就到這裡吧！明天再來。"+enter;

				cm.sendOk(msg);
				cm.dispose();
			break;
		}
	} else if (status == 1) {
		if (getk("isclear") == 1 || getk("isclear") == 4) {
			var msg = "#fs15##k你好 #b#h #!#k 今天的請求如下."+enter+enter;
			msg += isitem ? "#r#e[每日任務] : 墮落的世界樹#n#k"+enter : "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[每日任務] : 墮落的世界樹#n#k"+enter;
			msg += isitem ? "\r\n目標戰利品 : #b#i"+getk("mobid")+"##z"+getk("mobid")+"# "+getk("mobq")+"個#k"+enter+enter : "目標 : #b#o"+getk("mobid")+"# "+getk("mobq")+"怪物數量#k"+enter+enter;
			msg += "你願意接受這個任務嗎？";

			cm.sendYesNo(msg);
		}
	} else if (status == 2) {
		var msg = "#fs15#你好 #b#h #!#k 今天的請求如下."+enter+enter;
		msg += isitem ? "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[每日任務] : 墮落的世界樹#n#k"+enter : "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n#r#e[每日任務] : 墮落的世界樹#n#k"+enter;
		msg += isitem ? "\r\n目標戰利品 : #b#i"+getk("mobid")+"##z"+getk("mobid")+"# "+getk("mobq")+"個#k"+enter+enter : "目標 : #b#o"+getk("mobid")+"# "+getk("mobq")+"怪物數量#k"+enter+enter;
		msg += "完成任務後回來.";
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
		msg += "#i"+reward[i]['itemid']+"##z"+reward[i]['itemid']+"# "+reward[i]['qty']+"個"+enter;
	}
	//cm.gainItem(rand1['itemid'], rand1['qty']);
	//cm.gainItem(rand2['itemid'], rand2['qty']);
	//msg += "#i"+rand1['itemid']+"##z"+rand1['itemid']+"# "+rand1['qty']+"個"+enter;
	//msg += "#i"+rand2['itemid']+"##z"+rand2['itemid']+"# "+rand2['qty']+"個"+enter;
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

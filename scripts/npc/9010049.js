importPackage(Packages.database);

var listItem2 = {
    "1배" : 20,
    "1.3배" : 20,
    "1.5배" : 20,
    "1.7배" : 20,
    "1.8배" : 30,
    "2배" : 50,
    "2.2배" : 40,
    "2.3배" : 40,
    "2.5배" : 25,  
}

var log = [];
var data = [];

var coin = 4001716;
var cost = 1;  

var enter = "\r\n";

var charname = "";
var count;

var c_num = 0;

function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
        charname = cm.getPlayer().getName();
        count = cm.getPlayer().getClient().getCustomKeyValue(230731, "Roulette");
        if(count < 0){
            cm.getPlayer().getClient().setCustomKeyValue(230731, "Roulette", 3);
        }
        var msg = "[후원룰렛]"+enter
        msg += "메세지"+enter
        msg += "비용 : #i"+coin+"# #z"+coin+"# "+cost+"개"+enter
        msg += "#r계정당 1일 3회만 이용이 가능합니다.#k"+enter+enter
        msg += "#L0# 룰렛돌리기 (남은 횟수 : "+count+")#l"
        if(cm.getPlayer().isGM()){
            msg += enter+enter+enter
            msg += "[GM log]"+enter
            msg += "#L1# 로그 확인하기"
        }
        cm.sendSimple(msg);
    } else if(St == 1) {
        if(S == 1){
            data_load()
            var msg = "[User log]"+enter;
            for(var i = 0; i<data.length; i++){
                msg += "#L"+i+"#"+data[i]+enter
            }
            cm.sendSimple(msg);
        } else {
            getData();
            if(!cm.haveItem(coin, cost)){
                cm.sendOk("#i"+coin+"# #z"+coin+"# 가 부족합니다.");
                cm.dispose();
                return;
            }
            if(count == 0){
                cm.sendOk("횟수를 모두 소진하셨습니다.\r\n일일 3회 가능합니다.");
                cm.dispose();
                return;
            }
            var pickItem = getRandWeight(listItem2);
            var msg = "나의 룰렛결과는?"+enter;
            msg += pickItem+" 당첨! "
            cm.getPlayer().getClient().setCustomKeyValue(230731, "Roulette", count-1);
            log_record(charname, pickItem, data);
            cm.gainItem(coin, -cost);
            cm.sendOk(msg);
            cm.dispose();
        }
    } else if(St == 2) {
        log_load(data[S]);
        var msg = "[User log]"+enter;
        for(var i = 0; i<log.length; i++){
            msg += "캐릭터명 : "+log[i][0]+" | 당첨 : "+log[i][1]+" | 시간 : "+log[i][2]+enter
        }
        cm.sendOk(msg);
        cm.dispose();
	}
}


function getRandRange(min,max){
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function getRandWeight(listItem){
    var pickVal = Number.MAX_VALUE;
    var pickItem = null;
    for(var item in listItem){
        if(listItem.hasOwnProperty(item)){
            var tmpVal = -Math.log(Math.random()) / listItem[item];
            if(tmpVal < pickVal){
                pickVal = tmpVal;
                pickItem = item;
            }
        }
    }
    return pickItem;
}

function log_record(name, product, time)  {
	var con = DatabaseConnection.getConnection();
	var insert = con.prepareStatement("INSERT INTO roulette_log(name, recom, time) VALUES(?,?,?)");
	insert.setString(1, name);
	insert.setString(2, product);
	insert.setString(3, time);
	insert.executeUpdate();
	insert.close();
	con.close();
}

function data_load() {
	var c = DatabaseConnection.getConnection();
	var con = c.prepareStatement("SELECT DISTINCT time FROM roulette_log");
	var rs = con.executeQuery();
	while(rs.next()) {
		data.push(rs.getString("time"));
	}
	c.close();
	rs.close();
	con.close();
}

function log_load(time) {
	var c = DatabaseConnection.getConnection();
	var con = c.prepareStatement("SELECT * FROM roulette_log where time = ?");
    con.setString(1, time);
	var rs = con.executeQuery();
	while(rs.next()) {
		log.push([rs.getString("name"), rs.getString("recom"), rs.getString("time")]);
	}
	c.close();
	rs.close();
	con.close();
}

function getData() {
	time = new Date();
	year = time.getFullYear();
	month = time.getMonth() < 10 ?  "0"+(time.getMonth()+ 1) : time.getMonth()+ 1;
	date = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
	data = year+"-"+month+"-"+date;
	day = time.getDay();
	hour = time.getHours();
	minute = time.getMinutes();
	seconds = time.getSeconds();
}

function msg2(text){
    cm.getPlayer().dropMessage(5, text);
}
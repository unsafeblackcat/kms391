importPackage(java.lang);
importPackage(java.io);
importPackage(Packages.tools.packet);
importPackage(Packages.database);

var EtcTimer = Packages.server.Timer.EtcTimer;

var Upgrade = 1;

var LuckChance = 0, DestroyChance = 0;

//아래 파괴확률
var Destroy = [1,2,3,5,10,15,20,25,30,40,50,60,70,80,90];
var Destroy2 = 0;


var Chance = 85, ChanceM = 7;  // 확률, 차감확률
var random;

var item = [0, 1302000, 1302007, 1302012, 1302015, 1302113, 1302214, 1302277, 1302312, 1302081, 1302314, 1302276, 1302315, 1302275, 1302333, 1302343, 1302355];
var icon3 = [3801300,3801301,3801302,3801303,3801304,3801305,3801306,3801307,3801308,3801309,3801310,3801311,3801312,3801313,3801314,3801315,3801316,3801317]

var cost = 1000000000, costM = 300000000; // 스타트 메소, 차감메소
var check = true;
var tick = 0;


var rank = [];

var charname;

var icon = "#fUI/UIWindow2.img/QuestIcon/4/0#"



function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}
	if(M == 1) {
        if(cm.getPlayer().getKeyValue(230732,"Enhance") == -1){
            cm.getPlayer().setKeyValue(230732,"Enhance", 1);
            cm.getPlayer().setKeyValue(230731,"Enhance", 1);
            cm.getPlayer().setKeyValue(210310,"Luck", 0);
            cm.getPlayer().setKeyValue(210310,"Destroy", 0);
        }
        if(St == 0 && S == 0){
            if (cm.getMeso() >= cost) {
                check = false;
                cm.gainMeso(-cost);
                Enhance(Upgrade);
                cm.getPlayer().setKeyValue(210310,"Luck", 0);
                cm.getPlayer().setKeyValue(210310,"Destroy", 0);
            } else {
                cm.sendOk("메소가 부족합니다.");
                cm.dispose();
                return;
            }
        } else {
            if(St == -1){
                getData();
                charname = cm.getPlayer().getName();
                reset();
            }
            St++;
        }
    }
    
	if(St == 0) {
        var va = cm.getPlayer().getKeyValue(230732,"Enhance");
        if(cm.getPlayer().isGM()){
            //msg2(cm.getPlayer().getKeyValue(230731,"Enhance"));
        }
        if(va != 1 && check == true){
            cost = cost+(costM*(va-1));
            Chance = Chance - ((va-1)*ChanceM);
            Upgrade = Upgrade * va;
            check = false;
            if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
                LuckChance = 1;
            }
            if(cm.getPlayer().getKeyValue(210310,"Destroy") == 1){
                DestroyChance = 1;
            }
        }
        if(cm.getPlayer().getKeyValue(210310,"Destroy") == 0){
            Destroy2 = Destroy[Upgrade];
        } else {
            Destroy2 = Destroy[Upgrade];
        }
        var msg = "                  #b#e[Event] 전설의 무기 제작!!#k#n\r\n\r\n";
        msg += "#fs30#            #fc0xFFED213A#[#i"+item[Upgrade]+"#] #k\r\n";
        msg += "#fs12#";
        msg += star(Upgrade);
        msg += "#fc0xFFFFBB00#";
        for (i = 1; i <= Upgrade; i++) {
            msg += "★";
            if (i % 5 == 0 && i != 0){
                msg += " ";
            }
        }
        msg += "#k\r\n\r\n";
        msg += "#fc0xFFFFFFFF#★★★★#k현재 강화 확률 : #r#e"+Chance+"% #k#n 파괴 확률 : #r#e"+(cm.getPlayer().getKeyValue(210310,"Destroy") == 1 ? (Destroy2-DestroySafe[Upgrade]) : Destroy2)+"%#k#n \r\n";
        msg += "#fc0xFFFFFFFF#★★★★#k#L0#무기강화#l #L1#랭킹등록#l #L3#랭킹확인#l";
        msg += "\r\n\r\n";
        msg += "#fc0xFFFFFFFF#★★★★[]#k[#i"+icon3[5]+"#"+numberToKorean(cost)+"]";
        msg += "#fc0xFFFFFFFF#[] #k[#i"+icon3[12]+"##i"+icon3[12]+"##i"+icon3[12]+"#]";
        msg += "#fc0xFFFFFFFF#[][ #k[#i"+icon3[11]+"##i"+icon3[11]+"##i"+icon3[11]+"#]";
        cm.sendSimple(msg);
	} else if(St == 1) {
        if(S == 1){
            if(Upgrade == 1){
                cm.sendOk("1성 무기는 랭킹 저장이 안됩니다.");
                cm.dispose();
                return;
            }
            var msg = "                 #b#e[Event] 전설의 무기 랭킹!!#k#n\r\n\r\n";
            msg += "#fs30#            #fc0xFFED213A#[#i"+item[Upgrade]+"#] #k\r\n";
            msg += "#fs12#";
            msg += star(Upgrade);
            msg += "#fc0xFFFFBB00#";
            for (i = 1; i <= Upgrade; i++) {
                msg += "★";
                if (i % 5 == 0 && i != 0){
                    msg += " ";
                }
            }
            reset();
            cm.sendOk(msg);
            cm.dispose();
        } else if(S == 3){
            if(Ranking() == 0){
                cm.sendOk("조회가 가능한 랭킹이 없습니다.");
                cm.dispose();
            } else {
                var msg = "                 #b#e[Event] 전설의 무기 랭킹!!#k#n\r\n\r\n";
                msg += "#fs30#            #fc0xFFED213A#[#i"+item[rank[0][1]]+"#] #k\r\n";
                msg += "#fs12#";
                msg += star(Number(rank[0][1]));
                msg += "#fc0xFFFFBB00#";
                for (i = 1; i <= rank[0][1]; i++) {
                    msg += "★";
                    if (i % 5 == 0 && i != 0){
                        msg += " ";
                    }
                }
                msg += "#k\r\n\r\n";
                msg += "#fc0xFFFFFFFF#"
                msg += "☆☆☆☆☆☆☆#k"
                //msg += "#i"+icon3[11]+"# 최고의 명장 : #r"+rank[0][0]+"#k #i"+icon3[11]+"#\r\n\r\n"

                for(var i = 0; i<rank.length; i++){
                    if(i < 10){
                        msg += "#fc0xFFFFFFFF##\r\n\r\n"
                        msg += "☆☆☆☆☆☆[#k"
                        msg += "캐리터명 : "+rank[i][0]+" | 강화 +"+rank[i][1]+""
                        msg += "\r\n"
                    }
                }
                cm.sendOk(msg);
                cm.dispose();
            }
            
        }
	} else if(St == 2) {
        cm.gainItem(itemlist[S]['itemid'] , itemlist[S]['qty']);
        cm.getPlayer().setKeyValue(230731,"Enhance", 1);
        Upgrade = 1;
        cm.sendOk("거래 고맙네. 참 자네에게 이걸 주도록 하지 \r\n또 좋은 검을 얻게되면 그떄 보세\r\n\r\n"+icon+"\r\n\r\n"
                +"#i"+itemlist[Upgrade]['itemid']+"# #z"+itemlist[Upgrade]['itemid']+"#");
        cm.dispose();
	}
}

function Enhance(Up){
    random = Math.floor(Math.random() * 100);
    Destroyrandom = Math.floor(Math.random() * 50);
switch(Up){
    case 1:
        if(Chance >= random){
        Chance -= ChanceM;
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        }
    break;
    case 2:
        if(Chance >= random){
        Chance -= ChanceM;
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            Chance += ChanceM;
            cost -=costM;
            Upgrade--;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
        }
    break;
    case 3:
        if(Chance >= random){
        Chance -= ChanceM;
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            Chance += ChanceM;
            cost -=costM;
            Upgrade--;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
        }
    break;
    case 4:
        if(Chance >= random){
        Chance -= ChanceM;
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            Chance += ChanceM;
            cost -=costM;
            Upgrade--;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
        }
    break;
    case 5:
        if(Chance >= random){
        Chance -= ChanceM;
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            Chance += ChanceM;
            cost -=costM;
            Upgrade--;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
        }
    break;
    case 6:
        if(Chance >= random){
        Chance -= ChanceM;
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            Chance += ChanceM;
            cost -=costM;
            Upgrade--;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
        }
    break;
    case 7:
        if(Chance >= random){
        if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
            Chance = (Chance-Luck[Upgrade])-ChanceM;
        } else {
            Chance -= ChanceM;
        }
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
                Chance = (Chance-Luck[Upgrade])+ChanceM;
            } else {
                Chance += ChanceM;
            }
            cost -=costM;
            Upgrade--;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
            if(Destroy2 >= Destroyrandom){
                reset()
                //msg2("파괴");
            }
        }
    break;
    case 8:
        if(Chance >= random){
        if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
            Chance = (Chance-Luck[Upgrade])-ChanceM;
        } else {
            Chance -= ChanceM;
        }
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
                Chance = (Chance-Luck[Upgrade])+ChanceM;
            } else {
                Chance += ChanceM;
            }
            cost -=costM;
            Upgrade--;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
            if(Destroy2 >= Destroyrandom){
                reset()
                //msg2("파괴");
            }
        }
    break;
    case 9:
        if(Chance >= random){
        if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
            Chance = (Chance-Luck[Upgrade])-ChanceM;
        } else {
            Chance -= ChanceM;
        }
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
                Chance = (Chance-Luck[Upgrade])+ChanceM;
            } else {
                Chance += ChanceM;
            }
            cost -=costM;
            Upgrade--;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
            if(Destroy2 >= Destroyrandom){
                reset()
                //msg2("파괴");
            }
        }
    break;
    case 10:
        if(Chance >= random){
        if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
            Chance = (Chance-Luck[Upgrade])-ChanceM;
        } else {
            Chance -= ChanceM;
        }
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
                Chance = (Chance-Luck[Upgrade])+ChanceM;
            } else {
                Chance += ChanceM;
            }
            cost -=costM;
            Upgrade--;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
            if(Destroy2 >= Destroyrandom){
                reset()
                //msg2("파괴");
            }
        }
    break;
    case 11:
        if(Chance >= random){
        if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
            Chance = (Chance-Luck[Upgrade])-ChanceM;
        } else {
            Chance -= ChanceM;
        }
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
                Chance = (Chance-Luck[Upgrade])+ChanceM;
            } else {
                Chance += ChanceM;
            }
            cost -=costM;
            Upgrade--;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
            if(Destroy2 >= Destroyrandom){
                reset()
                //msg2("파괴");
            }
        }
    break;
    case 12:
        if(Chance >= random){
        if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
            Chance = (Chance-Luck[Upgrade])-ChanceM;
        } else {
            Chance -= ChanceM;
        }
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
                Chance = (Chance-Luck[Upgrade])+ChanceM;
            } else {
                Chance += ChanceM;
            }
            cost -=costM;
            Upgrade--;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
            if(Destroy2 >= Destroyrandom){
                reset()
                //msg2("파괴");
            }
        }
    break;
    case 13:
        if(Chance >= random){
        if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
            Chance = (Chance-Luck[Upgrade])-ChanceM;
        } else {
            Chance -= ChanceM;
        }
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
                Chance = (Chance-Luck[Upgrade])+ChanceM;
            } else {
                Chance += ChanceM;
            }
            cost -=costM;
            Upgrade--;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
            if(Destroy2 >= Destroyrandom){
                reset()
                //msg2("파괴");
            }
        }
    break;
    case 14:
        if(Chance >= random){
        if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
            Chance = (Chance-Luck[Upgrade])-ChanceM;
        } else {
            Chance -= ChanceM;
        }
        cost +=costM;
        Upgrade++;
        cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")+1));
        } else {
            if(cm.getPlayer().getKeyValue(210310,"Luck") == 1){
                Chance = (Chance-Luck[Upgrade])+ChanceM;
            } else {
                Chance += ChanceM;
            }
            Upgrade--;
            cost -=costM;
            cm.getPlayer().setKeyValue(230732,"Enhance", ""+(cm.getPlayer().getKeyValue(230732,"Enhance")-1));
            if(Destroy2 >= Destroyrandom){
                reset()
                //msg2("파괴");
            }
        }
    break;
}
LuckChance = 0;
DestroyChance = 0;
return;
}


function star(Upgrade){
    var msg = "";
    switch(Upgrade){
        case 1:
            msg += "#fc0xFFFFFFFF#★★★★★★★★★★★★";
        break;
        case 2:
            msg += "#fc0xFFFFFFFF#★★★★★★★★★★★[";
        break;
        case 3:
            msg += "#fc0xFFFFFFFF#★★★★★★★★★★   ";
        break;
        case 4:
            msg += "#fc0xFFFFFFFF#★★★★★★★★★★  ";
        break;
        case 5:
            msg += "#fc0xFFFFFFFF#★★★★★★★★★★";
        break;
        case 6:
            msg += "#fc0xFFFFFFFF#★★★★★★★★★ ";
        break;
        case 7:
            msg += "#fc0xFFFFFFFF#★★★★★★★★   ";
        break;
        case 8:
            msg += "#fc0xFFFFFFFF#★★★★★★★★ ";
        break;
        case 9:
            msg += "#fc0xFFFFFFFF#★★★★★★★  ";
        break;
        case 10:
            msg += "#fc0xFFFFFFFF#★★★★★★★ ";
        break;
        case 11:
            msg += "#fc0xFFFFFFFF#★★★★★★";
        break;
        case 12:
            msg += "#fc0xFFFFFFFF#★★★★★  ";
        break;
        case 13:
            msg += "#fc0xFFFFFFFF#★★★★★  ";
        break;
        case 14:
            msg += "#fc0xFFFFFFFF#★★★★★  ";
        break;
        case 15:
            msg += "#fc0xFFFFFFFF#★★★★★  ";
        break;
    }
    return msg;
}

function Ranking(){
    var num = 0;
	var c = DatabaseConnection.getConnection();
	var con = c.prepareStatement("SELECT * from enhanceswordlog order by enhance ASC");
	var rs = con.executeQuery();
	while(rs.next()) {
		rank.push([rs.getString("name"), rs.getString("enhance"), rs.getString("time")]);
        msg2(rank[num][0]+"/"+rank[num][1])
        num++;
	}
	c.close();
	rs.close();
	con.close();
    return num;
}

function rank_check(charname){
    var num = 0;
	var c = DatabaseConnection.getConnection();
	var con = c.prepareStatement("SELECT * from enhanceswordlog where name = ?");
    con.setString(1, charname);
	var rs = con.executeQuery();
	if(rs.next()) {
        num++;
	}
	c.close();
	rs.close();
	con.close();
    return num;
}

function ranking_record(name, enhance, time){
    var con = DatabaseConnection.getConnection();
	var insert = con.prepareStatement("INSERT INTO enhanceswordlog(name, enhance, time) VALUES(?,?,?)");
	insert.setString(1, name);
	insert.setString(2, enhance);
	insert.setString(3, time);
	insert.executeUpdate();
	insert.close();
	con.close();
}

function ranking_update(name, enhance, time){
    var con = DatabaseConnection.getConnection();
    var rs = con.prepareStatement("UPDATE enhanceswordlog SET enhance = ?, time = ? WHERE name = ?");
    rs.setString(1, enhance);
	rs.setString(2, time);
	rs.setString(3, name);
    rs.executeUpdate();
    rs.close();
    con.close();
}

function reset(){
    if((cm.getPlayer().getKeyValue(230732,"Enhance") > cm.getPlayer().getKeyValue(230731,"Enhance"))){
        cm.getPlayer().setKeyValue(230731,"Enhance", ""+cm.getPlayer().getKeyValue(230732,"Enhance"));
        if(rank_check(charname) == 0){
            ranking_record(charname, cm.getPlayer().getKeyValue(230732,"Enhance"), data);
        } else {
            ranking_update(charname, cm.getPlayer().getKeyValue(230732,"Enhance"), data);
        }
    }
    cm.getPlayer().saveToDB(false, false)
    Chance = 95;
    cost = 1000000000;
    Upgrade = 1;
    cm.getPlayer().setKeyValue(230732,"Enhance", 1);
}

function destory(){
    var msg = "                 #b#e[Event] 전설의 무기 랭킹!!#k#n\r\n\r\n";
    msg += "#fs30#            #fc0xFFED213A#[#i"+item[Upgrade]+"#] #k\r\n";
    msg += "#fs12#";
    msg += star(Upgrade);
    msg += "#fc0xFFFFBB00#";
    for (i = 1; i <= Upgrade; i++) {
        msg += "★";
        if (i % 5 == 0 && i != 0){
            msg += " ";
        }
    }
    return msg;
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

function numberToKorean(number) {
    var inputNumber = number < 0 ? false : number;
    var unitWords = ["", "만", "억", "조", "경"];
    var splitUnit = 10000;
    var splitCount = unitWords.length;
    var resultArray = [];
    var resultString = "";

    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
        unitResult = Math.floor(unitResult);
        if (unitResult > 0) {
            resultArray[i] = unitResult;
        }
    }

    for (var i = 0; i < resultArray.length; i++) {
        if (!resultArray[i]) continue;
        resultString = String(resultArray[i]) + unitWords[i] + resultString;
    }

    return resultString;
}
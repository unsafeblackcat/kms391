importPackage(Packages.server);
importPackage(java.lang);

var enter = "\r\n";
var year, month, date2, date, day
var hour, minute;
var 제한레벨 = 200;
var petevent = "petevent";


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
      
        var msg = "#fs15#테스트용 기능" + enter;
        msg += "#L0#네오스톤 받으시겠습니까?" + enter;
		 msg += "#L1#네오젬 받으시겠습니까?" + enter;
		  msg += "#L2#네오코어 받으시겠습니까?" + enter;
        cm.sendOk(msg);
    } else if (status == 1) {
     switch(sel) {
		 case 1:
			 cm.getPlayer().AddStarDustCoin(0, 100);
			  cm.dispose();
			 break;
		 case 2:
			  cm.getPlayer().AddStarDustCoin(1, 100);
			  cm.dispose();
			 break;
		 case 3:	
			 cm.getPlayer().AddStarDustCoin(2, 100);
			  cm.dispose();
			 break;
	 }

    }
}

function getData() {
    time = new Date();
    year = time.getFullYear();
    month = time.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }
    date2 = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
    date = year + "" + month + "" + date2;
    day = time.getDay();
    hour = time.getHours();
    minute = time.getMinutes();
}
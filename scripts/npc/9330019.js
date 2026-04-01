var itemlist =
[
    [2633043, 2, 0],
    [2633044, 2, 0],
    [2046025, 2, 1],
    [2046026, 2, 1],
    [2046119, 2, 1],
    [2430067, 1, 2],
    [4310012, 9999, 3],
    [2439960, 1, 4],
    [2439961, 1, 4],
    [2439962, 1, 4],
    [2430034, 2, 5],
];

var questlist =[
	[50000, 2],
	[60000, 2],
	[100000, 2],
	[200000, 2],
	[1000000, 3],
	[70000, 2],
]

var sim,sim2,SS2;

var ch1, ch2, ch3;

var pink = "#fc0xFFF781D8#";
var black = "#fc0xFF000000#";
var purple = "#fc0xFF7401DF#";
var sky = "#fc0xFF58ACFA#";
var uta = "#fc0xFFF15F5F#";
var dao = "#fc0xFF4374D9#";
var gre = "#fc0xFF77bb00#";
var white = "#fc0xFFFFFFFF#";
var enter = "\r\n";

var count = 0;

function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1) {
        cm.sendOk("알겠네... 자네가 도와주지 않으니 이제 곧 망하겠구먼");
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
		//count = 0;
		ch1 = 0, ch2 = 0, ch3 = 0;
		var chat =purple+"<?? 이벤트 퀘스트>#k 어떤 이벤트가 준비되어 있을까?"+enter;
        if(cm.getPlayer().isGM()){
			for(var i = 0; i<questlist.length; i++){
			sim = new String(i);
            chat += "퀘스트 : "+(cm.getPlayer().getClient().getCustomKeyValue(220313, "questcheck"+sim) == 1 ? "진행중" : "진행가능")
            chat += "| 몹카운트 : "+cm.getPlayer().getClient().getCustomKeyValue(220313, "m0"+sim)
            chat += "| 클리어 횟수 : "+cm.getPlayer().getClient().getCustomKeyValue(220313, "questcount"+sim)+enter;
			}
        }
		for(var i = 0; i<questlist.length; i++){
			sim = new String(i);
			if(cm.getPlayer().getClient().getCustomKeyValue(220313, "questcheck"+sim) == -1){
				ch1++;
			}
			if(ch1 != 0 && ch1 == 1){
				chat +=enter;
				chat +="#fUI/UIWindow2.img/UtilDlgEx/list4#"+enter;
				ch1++;
			}
			if(cm.getPlayer().getClient().getCustomKeyValue(220313, "questcheck"+sim) == -1){
				chat +="#d#L"+i+"# [시작가능] 필드의 몬스터 청소! ["+Comma(questlist[i][0])+" 마리]"+enter;
			}
		}
		
		for(var i = 0; i<questlist.length; i++){
			sim = new String(i);
			if(cm.getPlayer().getClient().getCustomKeyValue(220313, "questcheck"+sim) == 1 && cm.getPlayer().getClient().getCustomKeyValue(220313, "m0"+sim) < questlist[i][0]){
				ch2++;
			}
			if(ch2 != 0 && ch2 == 1){
				chat +=enter;
				chat +="#fUI/UIWindow2.img/UtilDlgEx/list5#"+enter;
				ch2++;
			}
			if(cm.getPlayer().getClient().getCustomKeyValue(220313, "questcheck"+sim) == 1 && cm.getPlayer().getClient().getCustomKeyValue(220313, "m0"+sim) < questlist[i][0]){
				chat +="#d#L"+i+"# [진행중] 필드의 몬스터 청소! ["+Comma(questlist[i][0])+" 마리]"+enter;
			}
		}
		for(var i = 0; i<questlist.length; i++){
			sim = new String(i);
			if(cm.getPlayer().getClient().getCustomKeyValue(220313, "questcheck"+sim) == 1 && cm.getPlayer().getClient().getCustomKeyValue(220313, "m0"+sim) == questlist[i][0]){
				ch3 ++;
			}
			if(ch3 != 0 && ch3 == 1){
				chat +=enter;
				chat +="#fUI/UIWindow2.img/UtilDlgEx/list3#"+enter;
				ch3++;
			}
			if(cm.getPlayer().getClient().getCustomKeyValue(220313, "questcheck"+sim) == 1 && cm.getPlayer().getClient().getCustomKeyValue(220313, "m0"+sim) == questlist[i][0]){
				chat +="#d#L"+i+"# [완료가능] 필드의 몬스터 청소! ["+Comma(questlist[i][0])+" 마리]"+enter;
			}
		}
        cm.sendSimple(chat);
    } else if(St == 1) {
		sim2 = new String(S);
		SS2 = S;
        if(cm.getPlayer().getClient().getCustomKeyValue(220313, "questcount"+sim2) == questlist[S][1] && !cm.getPlayer().isGM()){
            cm.sendOk("몬스터 소탕은 하루 "+questlist[S][1]+"회만 가능하네 이만 몬스터 소탕은 잠시 쉬도록하게나.");
            cm.dispose();
            return;
        }
        if(cm.getPlayer().getClient().getCustomKeyValue(220313, "questcheck"+sim2) == 1 && cm.getPlayer().getClient().getCustomKeyValue(220313, "m0"+sim2) < questlist[S][0]){
            cm.sendOk("아직 몬스터를 다 소탕하지 못한 모양이군 자네는 지금까지 "+cm.getPlayer().getClient().getCustomKeyValue(220313, "m0"+sim2)+"마리를 소탕했다네. 조금만 더 힘내시게");
            cm.dispose();
            return;
        }
        if(cm.getPlayer().getClient().getCustomKeyValue(220313, "questcheck"+sim2) == 1 && cm.getPlayer().getClient().getCustomKeyValue(220313, "m0"+sim2) == questlist[S][0]){
            var text2 = "자, 여기 보상일세 몬스터 소탕에 아주 고생했네.\r\n\r\n";
            text2 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";
				for(var i =0; i<itemlist.length; i++){
					if(itemlist[i][2] == S){
						if(S == 1){
							text2 += "#L"+i+"##i"+itemlist[i][0]+"# #z"+itemlist[i][0]+"# "+itemlist[i][1]+"개"+enter;
						} else if(S == 4){
							text2 += "#L"+i+"##i"+itemlist[i][0]+"# #z"+itemlist[i][0]+"# "+itemlist[i][1]+"개"+enter;
						} else {
							text2 += "#i"+itemlist[i][0]+"# #z"+itemlist[i][0]+"# "+itemlist[i][1]+"개"+enter;
						}
					}
				}
            cm.sendOk(text2);
            if(S != 1 || S != 4){
                for(var i =0; i<itemlist.length; i++){
                    if(itemlist[i][2] == S){
                        if(S == 1 || S == 4){
                            continue;
                        } else {
                            cm.gainItem(itemlist[i][0], itemlist[i][1]);
                            cm.getPlayer().getClient().setCustomKeyValue(220313, "questcheck"+sim2, "-1");
                            cm.getPlayer().getClient().setCustomKeyValue(220313, "m0"+sim2, "0");
                            cm.dispose();
                        }
                    }
                }
                cm.getPlayer().getClient().setCustomKeyValue(220313, "questcount"+sim2, cm.getPlayer().getClient().getCustomKeyValue(220313, "questcount"+sim2)+1+"");
            }
        } else {
			cm.sendNext("이보게 거기 모험가 나를 도와주지 않겠나?");
		}
    } else if(St == 2) {
		if(cm.getPlayer().getClient().getCustomKeyValue(220313, "questcheck"+sim2) != -1){
			if(SS2 == 1 || SS2 == 4){
			var text2 = "자, 여기 보상일세 몬스터 소탕에 아주 고생했네.\r\n\r\n";
            text2 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";
			text2 += "#i"+itemlist[S][0]+"# #z"+itemlist[S][0]+"# "+itemlist[S][1]+"개"+enter;
			cm.sendOk(text2);
			cm.gainItem(itemlist[S][0], itemlist[S][1]);
			cm.getPlayer().getClient().setCustomKeyValue(220313, "questcheck"+sim2, "-1");
            cm.getPlayer().getClient().setCustomKeyValue(220313, "m0"+sim2, "0");
            cm.getPlayer().getClient().setCustomKeyValue(220313, "questcount"+sim2, cm.getPlayer().getClient().getCustomKeyValue(220313, "questcount"+sim2)+1+"");
			cm.dispose();
			return;
			}
		} else {
			cm.sendNextS("네? 저 말인가요?",2);
		}
    } else if(St == 3) {
        cm.sendNext("그래 바로 자네말일세. 요즘들어 사냥터 인기가 없어지는거 같아서 운영자가 고민을 하나보네..");
    } else if(St == 4) {
        cm.sendNext("그래서 사냥터 인기를 위해 자네가 사냥을 대신 나서주지 않겠나?");
    } else if(St == 5) {
        cm.sendYesNo("보상은 내가 두둑히 챙겨주도록 하지. 어때 몬스터 소탕을 해볼텐가?");
    } else if(St == 6) {
        cm.sendOk("그렇구만 고맙네, #r레벨 범위 몬스터 "+Comma(questlist[SS2][0])+"마리#k를 잡고 나에게 오면 된다네.");
        cm.getPlayer().getClient().setCustomKeyValue(220313, "questcheck"+sim2, "1");
        cm.getPlayer().getClient().setCustomKeyValue(220313, "m0"+sim2, "0");
        if(cm.getPlayer().getClient().getCustomKeyValue(220313, "questcount"+sim2) == -1){
            cm.getPlayer().getClient().setCustomKeyValue(220313, "questcount"+sim2, "0");
        }
        cm.dispose();
	}
}

function Comma(i)
{
	var reg = /(^[+-]?\d+)(\d{3})/;
	i+= '';
	while (reg.test(i))
	i = i.replace(reg, '$1' + ',' + '$2');
	return i;
}
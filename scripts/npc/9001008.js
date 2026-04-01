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
    [5, 2], 
    [6, 2], 
    [7, 2], 
    [8, 2], 
    [9, 3], 
    [10, 2], 
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
        cm.sendOk(" 明白了...既然你不願意幫忙，我很快就要完蛋了。"); 
        cm.dispose();  
        return; 
    } 
 
    if(M == 1) 
        St++; 
 
    if(St == 0) { 
        //count = 0; 
        ch1 = 0, ch2 = 0, ch3 = 0; 
        var chat = purple+"#fs15#<夢事件任務>#k 不知道有什麼樣的活動準備好了呢？"+enter; 
        if(cm.getPlayer().isGM()){  
            for(var i = 0; i<questlist.length;  i++){ 
                sim = new String(i); 
                chat += "任務 : "+(cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcheck"+sim) == 1 ? "進行中" : "可進行") 
                chat += "| 怪物計數 : "+cm.getPlayer().getClient().getCustomKeyValue(220313,  "m0"+sim) 
                chat += "| 完成次數 : "+cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcount"+sim)+enter; 
            } 
        } 
        for(var i = 0; i<questlist.length;  i++){ 
            sim = new String(i); 
            if(cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcheck"+sim) == -1){ 
                ch1++; 
            } 
            if(ch1 != 0 && ch1 == 1){ 
                chat += enter; 
                chat +="#fUI/UIWindow2.img/UtilDlgEx/list4#"+enter;  
                ch1++; 
            } 
            if(cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcheck"+sim) == -1){ 
                chat +="#d#L"+i+"# [可開始] 清掃地圖上的怪物！ ["+Comma(questlist[i][0])+" 隻]"+enter; 
            } 
        } 
        
        for(var i = 0; i<questlist.length;  i++){ 
            sim = new String(i); 
            if(cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcheck"+sim) == 1 && cm.getPlayer().getClient().getCustomKeyValue(220313,  "m0"+sim) < questlist[i][0]){ 
                ch2++; 
            } 
            if(ch2 != 0 && ch2 == 1){ 
                chat += enter; 
                chat +="#fUI/UIWindow2.img/UtilDlgEx/list5#"+enter;  
                ch2++; 
            } 
            if(cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcheck"+sim) == 1 && cm.getPlayer().getClient().getCustomKeyValue(220313,  "m0"+sim) < questlist[i][0]){ 
                chat +="#d#L"+i+"# [進行中] 清掃地圖上的怪物！ ["+Comma(questlist[i][0])+" 隻]"+enter; 
            } 
        } 
        for(var i = 0; i<questlist.length;  i++){ 
            sim = new String(i); 
            if(cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcheck"+sim) == 1 && cm.getPlayer().getClient().getCustomKeyValue(220313,  "m0"+sim) == questlist[i][0]){ 
                ch3 ++; 
            } 
            if(ch3 != 0 && ch3 == 1){ 
                chat += enter; 
                chat +="#fUI/UIWindow2.img/UtilDlgEx/list3#"+enter;  
                ch3++; 
            } 
            if(cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcheck"+sim) == 1 && cm.getPlayer().getClient().getCustomKeyValue(220313,  "m0"+sim) == questlist[i][0]){ 
                chat +="#d#L"+i+"# [可完成] 清掃地圖上的怪物！ ["+Comma(questlist[i][0])+" 隻]"+enter; 
            } 
        } 
        cm.sendSimple(chat);  
    } else if(St == 1) { 
        sim2 = new String(S); 
        SS2 = S; 
        if(cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcount"+sim2) == questlist[S][1] && !cm.getPlayer().isGM()){  
            cm.sendOk(" 怪物清掃一天只能進行 "+questlist[S][1]+" 次，先休息一下吧。"); 
            cm.dispose();  
            return; 
        } 
        if(cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcheck"+sim2) == 1 && cm.getPlayer().getClient().getCustomKeyValue(220313,  "m0"+sim2) < questlist[S][0]){ 
            cm.sendOk(" 看來你還沒把怪物清掃完呢，你目前已經清掃了 "+cm.getPlayer().getClient().getCustomKeyValue(220313,  "m0"+sim2)+" 隻怪物，再加把勁吧。"); 
            cm.dispose();  
            return; 
        } 
        if(cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcheck"+sim2) == 1 && cm.getPlayer().getClient().getCustomKeyValue(220313,  "m0"+sim2) == questlist[S][0]){ 
            var text2 = "來，這是你的獎勵，你清掃怪物辛苦了。\r\n\r\n"; 
            text2 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";  
            for(var i =0; i<itemlist.length;  i++){ 
                if(itemlist[i][2] == S){ 
                    if(S == 1){ 
                        text2 += "#L"+i+"##i"+itemlist[i][0]+"# #z"+itemlist[i][0]+"# "+itemlist[i][1]+" 個"+enter; 
                    } else if(S == 4){ 
                        text2 += "#L"+i+"##i"+itemlist[i][0]+"# #z"+itemlist[i][0]+"# "+itemlist[i][1]+" 個"+enter; 
                    } else { 
                        text2 += "#i"+itemlist[i][0]+"# #z"+itemlist[i][0]+"# "+itemlist[i][1]+" 個"+enter; 
                    } 
                } 
            } 
            cm.sendOk(text2);  
            if(S != 1 || S != 4){ 
                for(var i =0; i<itemlist.length;  i++){ 
                    if(itemlist[i][2] == S){ 
                        if(S == 1 || S == 4){ 
                            continue; 
                        } else { 
                            cm.gainItem(itemlist[i][0],  itemlist[i][1]); 
                            cm.getPlayer().getClient().setCustomKeyValue(220313,  "questcheck"+sim2, "-1"); 
                            cm.getPlayer().getClient().setCustomKeyValue(220313,  "m0"+sim2, "0"); 
                            cm.dispose();  
                        } 
                    } 
                } 
                cm.getPlayer().getClient().setCustomKeyValue(220313,  "questcount"+sim2, cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcount"+sim2)+1+""); 
            } 
        } else { 
            cm.sendNext(" 喂，那個冒險家，你願意幫幫我嗎？"); 
        } 
    } else if(St == 2) { 
        if(cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcheck"+sim2) != -1){ 
            if(SS2 == 1 || SS2 == 4){ 
                var text2 = "來，這是你的獎勵，你清掃怪物辛苦了。\r\n\r\n"; 
                text2 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";  
                text2 += "#i"+itemlist[S][0]+"# #z"+itemlist[S][0]+"# "+itemlist[S][1]+" 個"+enter; 
                cm.sendOk(text2);  
                cm.gainItem(itemlist[S][0],  itemlist[S][1]); 
                cm.getPlayer().getClient().setCustomKeyValue(220313,  "questcheck"+sim2, "-1"); 
                cm.getPlayer().getClient().setCustomKeyValue(220313,  "m0"+sim2, "0"); 
                cm.getPlayer().getClient().setCustomKeyValue(220313,  "questcount"+sim2, cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcount"+sim2)+1+""); 
                cm.dispose();  
                return; 
            } 
        } else { 
            cm.sendNextS(" 嗯？你是說我嗎？",2); 
        } 
    } else if(St == 3) { 
        cm.sendNext(" 沒錯，就是說你呢。最近打怪地點好像沒什麼人氣，看來運營方很頭疼呢。"); 
    } else if(St == 4) { 
        cm.sendNext(" 所以為了提高打怪地點的人氣，你願意代替我去打怪嗎？"); 
    } else if(St == 5) { 
        cm.sendYesNo(" 獎勵我會好好給你的。怎麼樣，要不要試試清掃怪物？"); 
    } else if(St == 6) { 
        cm.sendOk(" 太好了，謝謝你。你去打 #r等級範圍內的怪物 "+Comma(questlist[SS2][0])+" 隻#k，打完再回來找我。"); 
        cm.getPlayer().getClient().setCustomKeyValue(220313,  "questcheck"+sim2, "1"); 
        cm.getPlayer().getClient().setCustomKeyValue(220313,  "m0"+sim2, "0"); 
        if(cm.getPlayer().getClient().getCustomKeyValue(220313,  "questcount"+sim2) == -1){ 
            cm.getPlayer().getClient().setCustomKeyValue(220313,  "questcount"+sim2, "0"); 
        } 
        cm.dispose();  
    } 
} 
 
function Comma(i) 
{ 
    var reg = /(^[+-]?\d+)(\d{3})/; 
    i+= ''; 
    while (reg.test(i))  
        i = i.replace(reg,  '$1' + ',' + '$2'); 
    return i; 
} 
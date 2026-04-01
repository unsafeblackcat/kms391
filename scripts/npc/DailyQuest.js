/*
 
    NPC ID : 
 
    NPC 名稱 : 
 
    NPC 所在地圖 :  :  ()
 
    NPC 說明 : 每日任務
 
*/
var status = -1;
 
var date = new Date();
var day = date.getDay(); 
switch (day) {
    case 0:
        week = "日"
        reqitem = [4000051,4000052];  //所需道具 
        reqitemea = [1000,1000]; //所需道具數量
        reward = [4001716, 5060048, 4310900]; //完成獎勵 
        rewardea = [2,1,50]; //獎勵數量 
        break;
    case 1:
        week = "一"
        reqitem = [4000160,4000179]; //所需道具
        reqitemea = [1000,1000]; //所需道具數量 
        reward = [4001716, 2635909, 4310900]; //完成獎勵
        rewardea = [2,1,50]; //獎勵數量
        break;
    case 2:
        week = "二"
        reqitem = [4000644,4000642]; //所需道具 
        reqitemea = [1000,1000]; //所需道具數量
        reward = [4001716, 2633616, 4310900]; //完成獎勵 
        rewardea = [2,100,100]; //獎勵數量 
        break;
    case 3:
        week = "三"
        reqitem = [4000180,4000181]; //所需道具
        reqitemea = [1000,1000]; //所需道具數量 
        reward = [4001716, 2048717, 4310900]; //完成獎勵
        rewardea = [2,200,200]; //獎勵數量
        break;
    case 4:
        week = "四"
        reqitem = [4000623,4000673]; //所需道具 
        reqitemea = [1000,1000]; //所需道具數量
        reward = [4001716, 2048767, 4310900]; //完成獎勵 
        rewardea = [2,100,100]; //獎勵數量 
        break;
    case 5:
        week = "五"
        reqitem = [4000155,4000156]; //所需道具
        reqitemea = [1000,1000]; //所需道具數量 
        reward = [4001716, 2635906, 4310900]; //完成獎勵
        rewardea = [2,1,200]; //獎勵數量
        break;
    case 6:
        week = "六"
        reqitem = [4000324,4000325]; //所需道具 
        reqitemea = [1000,1000]; //所需道具數量
        reward = [4001716, 4310900, 2635908]; //完成獎勵 
        rewardea = [2,100,1]; //獎勵數量 
        break;
    default:
}
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
 
    if (mode == -1) {
        cm.dispose(); 
        return;
    }
    if (mode == 0) {
        status--;
        cm.dispose(); 
        return;
    }
    if (mode == 1) {
        status++;
    }
 
    if (status == 0) {
        var text = ""
        if (cm.getPlayer().getKeyValue(20,  "talak") != day) {
            cm.getPlayer().setKeyValue(20,  "talak", day);
            cm.getPlayer().setKeyValue(20,  "talak1", 0);
        }
        text += "#fs15#您好 #b#h0##k\r\n"
        text += "今天是星期 #b" + week + "#k\r\n#b"
        if (cm.getPlayer().getKeyValue(20,  "talak1") == 2) {
            text += "#L0#要完成任務嗎？#l\r\n"
        } else {
            text += "#L0#將發放符合今日的任務#l\r\n"
        }
        text += "#L99#查看獎勵#l\r\n"
        cm.sendSimple(text); 
    } else if (status == 1) {
        var text = ""
        if (selection == 0) {
            if (cm.getPlayer().getKeyValue(20,  "talak1") == 1) {
                text += "#fs15#您已完成星期 #b" + week + "#k的每日任務"
                cm.sendOk(text); 
                cm.dispose(); 
                return;
            } else {
                text += "#b"
                for (i = 0; i < reqitem.length;  i++) {
                    text += "#fs15##i" + reqitem[i] + "# #z" + reqitem[i] + "# " + reqitemea[i] + "個\r\n"
                    if (i == (reqitem.length  - 1)) {
                        text += "\r\n"
                    }
                }
                if (cm.getPlayer().getKeyValue(20,  "talak1") == 2) {
                    text += "#fs15##k是否現在完成？"
                } else {
                    text += "#fs15##k是否現在開始？"
                }
                cm.sendYesNo(text); 
            }
        } else if (selection == 99) {
        reward = [4001716,2635909,2633616,2048767,2635906,4310900,5060048]; //完成獎勵
        rewardea = [1,5,20,30,40,50,50]; //獎勵數量
            text += "#fs15#獎勵清單：\r\n\r\n"
            text += "#i4001716##i2635909##i2633616##i2048767##i2635906##i43101745##i5060048##i4310900#\r\n"
            cm.sendOk(text); 
            cm.dispose(); 
            return;
        }
    } else if (status == 2) {
        var text = ""
        var ready = true;
        if (cm.getPlayer().getKeyValue(20,  "talak1") == 2) {
            for (i = 0; i < reqitem.length;  i++) {
                if (cm.getPlayer().getItemQuantity(reqitem[i],  false) < reqitemea[i]) {
                    ready = false;
                    text += "#fs15##t" + reqitem[i] + "##n\r\n"
                    text += "#fs15##r" + cm.getPlayer().getItemQuantity(reqitem[i],  false) + "#k / " + reqitemea[i] + "個\r\n\r\n"
                }
            }
            if (ready == false) {
                text += "#fs15#未滿足任務完成條件\r\n"
            } else {
                cm.getPlayer().setKeyValue(20,  "talak1", 1);
                text += "#fs15#任務已完成\r\n\r\n"
                text += "#fUI/UIWindow.img/Quest/reward#\r\n" 
                for (i = 0; i < reward.length;  i++) {
                    text += "#i" + reward[i] + "# #b#t" + reward[i] + "##k #b" + rewardea[i] + "#k個\r\n";
                }
                for (i = 0; i < reqitem.length;  i++) {
                    cm.gainItem(reqitem[i],  -(reqitemea[i]));
                }
                for (i = 0; i < reward.length;  i++) {
                    cm.gainItem(reward[i],  rewardea[i]);
                }
            }
        } else {
            cm.getPlayer().setKeyValue(20,  "talak1", 2);
            text += "#fs15#已接受任務\r\n"
        }
        cm.sendOk(text); 
        cm.dispose(); 
        return;
    }
}
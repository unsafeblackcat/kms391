var status = -1;
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose(); 
        return;
    }
    if (mode == -1 || mode == 0) {
        cm.dispose(); 
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        talk = "要前往普通殘暴炎魔的祭壇瑪？\r\n\r\n"
        talk+= "#L0# #b前往挑戰普通殘暴炎魔。#l\r\n";
        cm.sendSimpleS(talk, 0x24);
    } else if (status == 1) {
        if (cm.itemQuantity(4001017)  == 0) {
            nameplus = selection == 0 ? "普通" : "混沌"
            cm.getPlayer().dropMessage(5,  "關鍵物品已經放到不知道誰的背包上了.....");
            cm.gainItem(4001017, 1);
            cm.dispose(); 


        }
        cm.warp(211042401); 
        cm.dispose(); 
    }
}
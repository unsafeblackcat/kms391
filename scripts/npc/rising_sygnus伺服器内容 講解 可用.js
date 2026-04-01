importPackage(Packages.handling.world); 
importPackage(Packages.handling.channel); 
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
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
 
    switch (status) {
        case 0:
            if ((cm.getPlayer().getJob()  != 2500 && cm.getPlayer().getLevel()  != 10)) {
                cm.getPlayer().setKeyValue(2,  "newchar", "1");
            }
            if (cm.getPlayer().getKeyValue(2,  "newchar") == -1) {
                cm.gainItem(1142249,  1); // 勳章 
                cm.gainItem(2028208,  1); // 武器 
                cm.gainItem(2028209,  1); // 防具 
                cm.gainItem(2433509,  1); // 武器 
                cm.gainItem(2433510,  1); // 副武器 
                cm.gainItem(1004404,  1); // 帽子 
                cm.gainItem(1052893,  1); // 套服 
                cm.gainItem(1102799,  1); // 披風 
                cm.gainPet(5000473,  "Pixi★", 1, 100, 100, 30, 103); // 寵物 30天 
                cm.gainItem(2000005,  300); // 藥水 300個 
                cm.getPlayer().setKeyValue(2,  "newchar", "1");
                if (cm.getClient().loadCharacters(0).size()  == 1) // 首次創角獎勵 
                {
                    cm.gainItem(2049360,  5); // 強化卷軸 5張 
                    cm.gainItem(2434006,  1); // 飾品 
                }
            }
 
            cm.sendSimpleS("#d 我是#b伺服器助手#k#d！有什麼可以幫您的嗎？#k\r\n#fc0xFFCC6666#"
                    + "#L0#伺服器的倍率是多少？#l\r\n"
                    + "#L1#關於推薦/宣傳/贊助系統#l\r\n"
                    + "#L3#請說明製作系統！#l\r\n"
                    + "#L4#想了解伺服器的各種內容！#l\r\n"
                    + "#L5#想知道各物品的用途！#l\r\n", 4);
            break;
        case 1:
            if (selection == 0) {
                cm.dispose(); 
                cm.sendOk("#b 伺服器倍率為 100 (經驗) / 7 (楓幣) / 2 (掉落)。#k #d使用特定道具如財富秘藥、經驗加倍券可能改變個別倍率。#k");
            }
            if (selection == 1) {
                cm.sendSimpleS("#d 請選擇想了解的項目：#k\r\n#fc0xFFCC00FF#"
                        + "#L0#如何使用推薦系統？#l\r\n"
                        + "#L1#推薦註冊的獎勵是什麼？#l\r\n"
                        + "#L2#宣傳可以獲得什麼獎勵？#l\r\n"
                        + "#L3#贊助獎勵內容#l\r\n", 4);
            }
            if (selection == 3) {
                cm.dispose(); 
                cm.sendOk("#b 製作系統#k可用材料合成裝備，分為#d材料製作、裝備製作、傷害皮膚製作#k三類，後續可能新增更多功能。");
            }
            if (selection == 4) {
                cm.sendSimpleS("#d 請選擇想了解的內容：#k\r\n#fc0xFF660033#"
                        + "#L10#武陵道場#l\r\n"
                        + "#L11#釣魚系統#l\r\n"
                        + "#L12#怪物公園#l\r\n"
                        + "#L13#靈魂系統#l\r\n"
                        + "#L14#混色染髮#l\r\n"
                        + "#L15#公會內容#l\r\n"
                        + "#L16#賭博系統#l\r\n"
                        + "#L17#轉生系統#l\r\n"
                        + "#L19#潛能系統#l\r\n"
                        + "#L20#勳章系統#l\r\n"
                        + "#L26#矩陣系統#l\r\n"
                        + "#L27#聯盟系統#l\r\n", 4);
            }
            if (selection == 5) {
                cm.sendSimpleS("#d 請選擇物品查詢：#k\r\n#fc0xFFCC6633#"
                        + "#L4001126##i4001126:##z4001126:##l\r\n"
                        + "#L4310210##i4310210:##z4310210:##l\r\n"
                        + "#L4310066##i4310066:##z4310066:##l\r\n"
                        + "#L4001109##i4001109:##z4001109:##l\r\n", 4);
            }
            break;
        case 2:
            if (selection == 0) {
                if (cm.getPlayer().getKeyValue(1,  "recommand") == 1) {
                    cm.dispose(); 
                    cm.sendOk(" 已有角色註冊為推薦人。");
                } else if (cm.getPlayer().getKeyValue(1,  "recommand") == 0) {
                    cm.dispose(); 
                    cm.sendOk(" 已有其他玩家將您設為推薦人。");
                } else {
                    cm.sendGetText(" 請輸入推薦人角色名稱（需在線）：");
                }
            }
            if (selection == 1) {
                cm.dispose(); 
                cm.sendOk(" 推薦系統雙方皆可獲得#b15個#i4310066:##z4310066:#。");
            }
            if (selection == 10) {
                cm.dispose(); 
                cm.sendOk(" 武陵道場需在時限內擊敗最多BOSS，通關可獲得#b#i4310225:##z4310225:#。");
            }
            if (selection == 4001126) {
                cm.dispose(); 
                cm.sendOk("#i"  + selection + "##z" + selection + "#是主要貨幣，可用於#r合成與強化#k。");
            }
            break;
        case 3:
            cm.dispose(); 
            if (cm.getText().equals(cm.getName()))  {
                cm.sendOk(" 無法將自己設為推薦人。");
                return;
            }
            var ch = World.Find.findChannel(cm.getText()); 
            if (ch != -1) {
                var victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(cm.getText()); 
                victim.dropMessage(-1,  cm.getName()  + " 將您設為推薦人。");
                victim.setKeyValue(1,  "recommand", "0");
                victim.gainItem(4310066,  15);
                cm.gainItem(4310066,  15);
                cm.getPlayer().setKeyValue(1,  "recommand", "1");
                cm.sendOk(" 獎勵已發放。");
            } else {
                cm.sendOk(cm.getText()  + " 不在線。");
            }
            break;
    }
}
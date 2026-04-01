importPackage(Packages.constants);

var status = -1;
var nowtime = new Date();
var 世界首領地圖 = ServerConstants.worldbossmap;
var 返回地圖 = ServerConstants.worldbossfirstmap;

var 奇數獎勵 = [[4033897, 50], [4001129, 5], [4310237, 500]]; // 僅進入擊破即可獲得的獎勵
var 奇數連擊獎勵 = [[4033897, 10], [4310237, 100], [4001129, 10]]; // 達成連擊時獲得的獎勵  

var 偶數獎勵 = [[4033897, 50], [4001129, 100], [4310237, 5000]]; // 僅進入擊破即可獲得的獎勵
var 偶數連擊獎勵 = [[4033897, 100], [4310237, 10000], [4001129, 100]]; // 達成連擊時獲得的獎勵  
var 出售道具 = [[4033897, 1, 1], [4310237, 100, 1], [2633620, 20, 1], [2431157, 1, 1], [2431156, 1, 1], [4033897, 10, 10], [4310237, 1000, 10], [2633620, 200, 10], [2431157, 10, 10], [2431156, 10, 10], [4033897, 100, 100], [4310237, 10000, 100], [2633620, 2000, 100], [2431157, 100, 100], [2431156, 100, 100]]; // 世界首領硬幣商店列表
var 購買所需道具 = 4001129; // 世界首領商店使用的硬幣

var 高級獎勵奇偶;
var 連擊獎勵奇偶;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else if (mode == 0 && type == 0) {
        status--;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        if (世界首領地圖 == cm.getPlayer().getMapId()) {
//            if (cm.getMap().getNumMonsters() <= 0) {//&& cm.haveItem(擊殺道具, 1)
                cm.sendYesNo("是否領取獎勵並離開？");
//            }
        } else {
            if (cm.getPlayer().gethottimeboss() && cm.getPlayerCount(211070100) < 31) {
                if (cm.getMap().getChannel() == 1 || cm.getMap().getChannel() == 2 || cm.getMap().getChannel() == 3 || cm.getMap().getChannel() == 4) {
                    cm.sendYesNo("世界首領已開始。");
                } else {
                    cm.sendOk("僅可進入1、20世紀2、3頻道。請使用其他頻道，最大人數為30人。（請務必留一個空間）");
                    cm.dispose();
                }
            } else {
                var text = "目前#b#e世界首領熱門時段活動#k#n尚未開始。請在世界首領開始前再來。\r\n現在可以使用#r#e世界首領的戰利品#k#n#i" + 購買所需道具 + "#來購買商店物品！您想購買哪種道具？\r\n\r\n";
                for (var i = 0; i < 出售道具.length; i++) {
                    text += "#L" + i + "# #i" + 出售道具[i][0] + ":##z" + 出售道具[i][0] + ":# " + 出售道具[i][1] + " 個 = #i" + 購買所需道具 + "#" + 出售道具[i][2] + "個所需  #l \r\n";
                }
                text += "";
                cm.sendSimple(text);
            }
        }

    } else if (status == 1) {
        if (世界首領地圖 == cm.getPlayer().getMapId() || cm.getPlayer().gethottimeboss()) {
            if (世界首領地圖 == cm.getPlayer().getMapId()) {
                高級獎勵奇偶 = ServerConstants.hourcheck(nowtime.getHours()) ? 奇數獎勵 : 偶數獎勵;
                連擊獎勵奇偶 = ServerConstants.hourcheck(nowtime.getHours()) ? 奇數連擊獎勵 : 偶數連擊獎勵;
                var 獎勵是否可行 = false;
                for (var i = 0; i < 高級獎勵奇偶.length; i++) {
                    if (!cm.canHold(高級獎勵奇偶[i][0], 高級獎勵奇偶[i][1])) {
                        獎勵是否可行 = true;
                        break;
                    }
                }
                if (cm.getPlayer().gethottimebosslastattack()) {
                    for (var i = 0; i < 連擊獎勵奇偶.length; i++) {
                        if (!cm.canHold(連擊獎勵奇偶[i][0], 連擊獎勵奇偶[i][1])) {
                            獎勵是否可行 = true;
                            break;
                        }
                    }
                }

                for (var i = 0; i < 高級獎勵奇偶.length; i++) {
                    cm.gainItem(高級獎勵奇偶[i][0], 高級獎勵奇偶[i][1]);
                }

                if (cm.getPlayer().gethottimebosslastattack()) {
                    for (var i = 0; i < 連擊獎勵奇偶.length; i++) {
                        cm.gainItem(連擊獎勵奇偶[i][0], 連擊獎勵奇偶[i][1]);
                    }
                }
                
                cm.getPlayer().sethottimeboss(false);
                cm.warp(返回地圖, 0);
                cm.dispose();
            } else {
                if (cm.getPlayer().gethottimeboss()) {
                    if (cm.getMap(世界首領地圖).getCharactersSize() == 0) {
                        cm.getMap(世界首領地圖).resetFully();
                    }
                    cm.warp(世界首領地圖);
                    cm.dispose();
                }
            }
        } else {
            if (cm.haveItem(購買所需道具, 出售道具[selection][2])) {
                if (!cm.canHold(出售道具[selection][0], 出售道具[selection][1])) {
                    cm.sendOk("裝備空間不足。");
                    cm.dispose();
                    return;
                }
                cm.gainItem(出售道具[selection][0], 出售道具[selection][1]);
                cm.gainItem(購買所需道具, -出售道具[selection][2]);
                cm.sendOk("購買完成");
                cm.dispose();
            } else {
                cm.sendOk("道具不足。");
                cm.dispose();
            }
        }
    }
}
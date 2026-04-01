var status = -1;
isOk = false;
 
/*
   非裝備類物品 : [物品代碼, 數量, 概率]
   裝備類物品 : [物品代碼, 1, 概率, 全屬性最小值, 全屬性最大值, 攻擊力/魔力最小值, 攻擊力/魔力最大值] (中間的1是為了腳本便利性而添加)
*/
itemlist =   [[[1182011, 1, 0.2, 1, 50, 1, 50], [1182150, 1, 0.2, 1, 50, 1, 50], [1182006, 1, 0.2, 1, 50, 1, 50], [1182067, 1, 0.2, 1, 50, 1, 50]],
               [[1182069, 1, 0.2, 1, 60, 1, 60], [1182182, 1, 0.2, 1, 60, 1, 60], [1182206, 1, 0.2, 1, 60, 1, 60], [1182019, 1, 0.2, 1, 60, 1, 60]],
               [[1182061, 1, 0.2, 1, 70, 1, 70], [1182079, 1, 0.2, 1, 70, 1, 70], [1182066, 1, 0.2, 1, 70, 1, 70], [1182056, 1, 0.2, 1, 70, 1, 70]],
               [[1182010, 1, 0.2, 1, 80, 1, 80], [1182022, 1, 0.2, 1, 80, 1, 80], [1182007, 1, 0.2, 1, 80, 1, 80], [1182023, 1, 0.2, 1, 80, 1, 80]],
               [[1182071, 1, 0.2, 1, 100, 1, 100], [1182191, 1, 0.1, 1, 100, 1, 100], [1182192, 1, 0.2, 1, 100, 1, 100], [1182059, 1, 0.2, 1, 100, 1, 100]],
]; 
 
name = ["普通", "稀有", "史詩", "獨特", "傳說"] // 等級名稱
ccoin = 4033114; // 代幣代碼
sum = 0; // 請勿修改 
 
var 伺服器名稱 = "PRAY";
 
 
function start() {
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
 
    if (mode == 1) {
        if (status == 4) {
    cm.dispose(); 
    cm.openNpc(3003324); 
        }
        if (isOk) {
            status++;
        }
        status++;
    } else {
        cm.dispose(); 
        return;
    }
    if (status == 0) {
        對話 = "             #fs15##d "+伺服器名稱+" 特別轉蛋抽獎 \r\n#fs15##Cgray#  相信你的運氣，挑戰概率獲取好道具吧！#k\r\n\r\n#fs15#";
        對話 += "#L0##i4033114# 使用代幣抽取道具#l\r\n";
        對話 += "#L1#聽取轉蛋系統說明。#l\r\n\r\n";
        對話 += "#L2##d查看各等級出現的道具及概率";
        cm.sendSimple( 對話);
    } else if (status == 1) {
        if (selection == 0) {
            對話 = "#fs15# #fc0xFF000000# 請設定要消耗的 #b#z" + ccoin + "##k#fs15# #fc0xFF000000# 數量。\r\n\r\n"
            對話 += "#r(當前持有 #z" + ccoin + "# #c" + ccoin + "#個。)#k"
            if (cm.itemQuantity(ccoin)  < 100 * itemlist.length)  {
                limit = cm.itemQuantity(ccoin); 
            } else {
                limit = itemlist.length  * 100 - 1;
            }
            cm.sendGetNumber( 對話, 1, 1, limit);
        } else if (selection == 1) {
            對話 = "#fs15##d#i4033114# 系統基本分為 #b" + itemlist.length  + "階段#k#fs15# #d。\r\n\r\n"
            for (i = 0; i < itemlist.length;  i++) {
                if ((100 * i) == 0) {
                    s = 1;
                } else {
                    s  = (100 * i);
                }
                對話 += "#b" + s + "個 ~ " + ((100 * i) + 99) + "個#k - #r" + name[i] + "等級#k\r\n"
            }
            對話 += "\r\n以100單位為分界，使用 #b0~99個#k 可 #b提升指定道具出現概率#k。\r\n\r\n"
            cm.sendOk( 對話);
            cm.dispose(); 
        } else if (selection == 2) {
            對話 = "#fs15##d 各等級可能出現的道具及概率如下。\r\n"
            對話 += " #r (概率已四捨五入顯示整數百分比)#k\r\n"
            for (i = 0; i < itemlist.length;  i++) {
                對話 += "\r\n#fs15##r[" + name[i] + " 等級]#k#fs15##n\r\n"
                for (j = 0; j < itemlist[i].length; j++) {
                    sum += itemlist[i][j][2]
                }
                for (h = 0; h < itemlist[i].length; h++) {
                    if (itemlist[i][h][0] >= 2000000) {
                        對話 += "#i" + itemlist[i][h][0] + "# #b#z" + itemlist[i][h][0] + "##r (x" + itemlist[i][h][1] + ")#k#fc0xFF000000# [" + Math.floor(itemlist[i][h][2]  / sum * 100) + "%]\r\n";
                    } else {
                        對話 += "#i" + itemlist[i][h][0] + "# #b#z" + itemlist[i][h][0] + "##k #fc0xFF000000# [" + Math.floor(itemlist[i][h][2]  / sum * 100) + "%]\r\n#d(全屬性 : " + itemlist[i][h][3] + "~" + itemlist[i][h][4] + ", 攻/魔 : " + itemlist[i][h][5] + "~" + itemlist[i][h][6] + ")\r\n"
                    }
                }
                sum = 0;
            }
            cm.sendOk( 對話);
            cm.dispose(); 
        }
    } else if (status == 2) {
        if (cm.itemQuantity(ccoin)  < selection) {
            cm.sendOk("#fs15##fc0xFF000000# 發生錯誤。");
            cm.dispose(); 
            return;
        }
        st = selection;
        geti = Math.floor(selection  / 100);
        對話 = "#fs15##fc0xFF000000##i" + ccoin + "# #b#z" + ccoin + "##r " + st + "#k#fc0xFF000000#個可獲得的道具列表如下：\r\n\r\n"
        對話 += "#r["+name[geti]+" 等級]#k#n\r\n"
        for (i = 0; i < itemlist[geti].length; i++) {
            if (i == 0) {
                for (j = 0; j < itemlist[geti].length; j++) {
                    sum += itemlist[geti][j][2]
                }
            }
            if (Math.floor(selection)  % 100 != 0) {
                對話 += "#L" + i + "# "
            }
            if (itemlist[geti][i][0] >= 2000000) {
                對話 += "#i" + itemlist[geti][i][0] + "# #b#z" + itemlist[geti][i][0] + "##r (x" + itemlist[geti][i][1] + ")#k#fc0xFF000000# [" + (itemlist[geti][i][2] / sum * 100).toFixed(2) + "%]#l\r\n";
            } else {
                對話 += "#i" + itemlist[geti][i][0] + "# #b#z" + itemlist[geti][i][0] + "##k#fc0xFF000000# [" + (itemlist[geti][i][2] / sum * 100).toFixed(2) + "%]\r\n#d(全屬性 : " + itemlist[geti][i][3] + "~" + itemlist[geti][i][4] + ", 攻/魔 : " + itemlist[geti][i][5] + "~" + itemlist[geti][i][6] + ")#l\r\n"
            }
        }
        if (Math.floor(selection)  % 100 != 0) {
            對話 += "\r\n\r\n#b#z" + ccoin + "# #r" + Math.floor(st  % 100) + "個#k可用於提升概率。\r\n"
            對話 += "請選擇想要的道具。"
            cm.sendSimple( 對話);
        } else {
            isOk = true;
            對話 += "#b#z" + ccoin + "##k是否確認使用？";
            cm.sendYesNo( 對話);
        }
    } else if (status == 3) {
        itemlist[geti][selection][2] = parseInt(itemlist[geti][selection][2]) + Math.floor(st%100); 
        sum += Math.floor(st%100); 
 
        對話 = "#fs15##fc0xFF000000# 概率調整後的道具列表：\r\n\r\n";
        for (i = 0; i < itemlist[geti].length; i++) {
            if (itemlist[geti][i][0] >= 2000000) {
                對話 += "#i" + itemlist[geti][i][0] + "# #b#z" + itemlist[geti][i][0] + "##r (x" + itemlist[geti][i][1] + ")#k#fc0xFF000000# [" + (itemlist[geti][i][2] / sum * 100).toFixed(2) + "%]\r\n";
            } else {
                對話 += "#i" + itemlist[geti][i][0] + "# #b#z" + itemlist[geti][i][0] + "##k#fc0xFF000000# [" + (itemlist[geti][i][2] / sum * 100).toFixed(2) + "%]\r\n#d(全屬性 : " + itemlist[geti][i][3] + "~" + itemlist[geti][i][4] + ", 攻/魔 : " + itemlist[geti][i][5] + "~" + itemlist[geti][i][6] + ")\r\n"
            }
        }
        對話 += "#b#z" + ccoin + "##k是否確認使用？";
        cm.sendYesNo( 對話);
    } else if (status == 4) {
        rd = Math.floor(Math.random()  * sum);
        sum2 = 0;
        for (i=0; i<=itemlist[geti].length; i++) {
            if (sum2 >= rd) {
                geti2 = i - 1;
                break;
            } else {
                sum2 += itemlist[geti][i][2];
            }
        }
        if (itemlist[geti][geti2][0] >= 2000000) {
            cm.gainItem(itemlist[geti][geti2][0],  itemlist[geti][geti2][1]);
        } else {
            rd1 = Packages.server.Randomizer.rand(itemlist[geti][geti2][3],itemlist[geti][geti2][4]) 
            rd2 = Packages.server.Randomizer.rand(itemlist[geti][geti2][5],itemlist[geti][geti2][6]) 
            cm.gainSponserItem(itemlist[geti][geti2][0],''+name[geti]+'',rd1,rd2,0); 
        }
        cm.sendYesNo("#fs15##fc0xFF000000# 已獲得以下道具：\r\n\r\n"
        +"#i"+itemlist[geti][geti2][0]+"# #b#z"+itemlist[geti][geti2][0]+"##k #r("+itemlist[geti][geti2][1]+"個)\r\n\r\n"
        +"是否繼續使用？");
        cm.gainItem(ccoin,  -st);
    }
}
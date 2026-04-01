var status = -1;
    move1 = 0;
    move2 = 0;
    move3 = 0;
    h = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    dal = ["0100100.img","0100101.img","0130101.img"] 
ccoin = 4310237; // 自訂強化幣代碼 
    if (mode == 1) {
        if (status == 2) {
            if (move1 >= 50 || move2 >= 50 || move3 >= 50) {
                status ++;
            } else {
                move1 += Math.floor(Math.random()  * 5);
                move2 += Math.floor(Math.random()  * 5);
                move3 += Math.floor(Math.random()  * 5);
            }
        } else {
            status++;
        }
    } else {
        cm.dispose(); 
        return;
    }
    if (status == 0) {
        對話 = "#fs15##fn나눔고딕 Extrabold##d 猜猜看哪隻蝸牛跑最快？！\r\n猜中最快的蝸牛就能獲得 #i4310237 自訂強化幣##r雙倍#k#d獎勵！\r\n\r\n#r(請下注自訂強化幣數量，上限30000個)#n#k"
        if (cm.itemQuantity(ccoin)  >= 5000) {
            limit = 5000;
        } else {
            limit = cm.itemQuantity(ccoin); 
        }
        cm.sendGetNumber( 對話,1,1,limit);
    } else if (status == 1) {
        st = selection;
        if (st > cm.itemQuantity(ccoin)  || st < 0) {
            cm.sendOk("#fn나눔고딕  Extrabold##fs15# #fc0xFF000000# 發生錯誤。");
            cm.dispose(); 
            return;
        }
        對話 = "#fs15##fn나눔고딕 Extrabold# #d 請選擇您支持的怪物。\r\n\r\n"
        for (i=0; i<dal.length;  i++) {
            對話 += "#L"+i+"# #fMob/"+dal[i]+"/move/0#";
        }
        cm.sendSimple( 對話);
    } else if (status == 2) {
        if (h == 0) {
            st2 = selection;
            cm.gainItem(ccoin,-st); 
        }
        h++;
        對話 = "#fs15# #fc0xFF000000# 第"+h+"回合怪物賽跑。\r\n"
        對話+= "#fs15# #fc0xFF000000# #r按Enter鍵繼續。\r\n 按ESC鍵將不予發放獎勵\r\n※注意：請緩慢點擊Enter鍵。#k#n\r\n\r\n";
        for (i=0; i<move1; i++) {
            對話 += " ";
        }
        對話+= "#fMob/"+dal[0]+"/move/0#\r\n"
        for (i=0; i<move2; i++) {
            對話 += " ";
        }
        對話+= "#fMob/"+dal[1]+"/move/0#\r\n"
                for (i=0; i<move3; i++) {
            對話 += " ";
        }
        對話+= "#fMob/"+dal[2]+"/move/0#\r\n\r\n"
        cm.sendOk( 對話);
    } else if (status == 3) {
        if (move1 >= move2 && move1 >= move3) {
            win = 0;
        }
        if (move2 >= move1 && move2 >= move3) {
            win = 1;
        }
        if (move3 >= move1 && move3 >= move2) {
            win = 2;
        }
        if (st2 == win) {
            cm.sendOk("#fn나눔고딕  Extrabold##fs15##d 恭喜獲勝！將發放雙倍獎勵！");
            cm.gainItem(ccoin,  st*2);
        } else {
            cm.sendOk("#fs15##fn나눔고딕  Extrabold##d 很遺憾您輸了。");
        }
        cm.dispose(); 
    }
}
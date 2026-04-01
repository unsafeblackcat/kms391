var status = -1;
var txt1, txt2, txt3, txt4, price;
importPackage(Packages.server); 
 
function start() {
    status = -1;
    action (1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose(); 
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }
 
    if (status == 0) {
        selection = 120;
        cm.sendOk(cm.getPlayer().getKeyValue(1,  "reborns") + ((cm.getPlayer().getLevel()  - selection) * (cm.getPlayer().getLevel()  - selection) * Randomizer.rand(100,  300) / 200));
        cm.dispose(); 
//      cm.showRebornRank(cm.RebornRank()); 
//      cm.sendGetNumber(" 請輸入第一個代碼", 1, 1000, 9999);
    } else if (status == 1) {
        txt1 = selection;
        cm.sendGetNumber(" 請輸入第二個代碼", 1, 1000, 9999);
    } else if (status == 2) {
        txt2 = selection;
        cm.sendGetNumber(" 請輸入第三個代碼", 1, 1000, 9999);
    } else if (status == 3) {
        txt3 = selection;
        cm.sendGetNumber(" 請輸入第四個代碼", 1, 100000, 999999);
    } else if (status == 4) {
        txt4 = selection;
        cm.sendGetNumber(" 請輸入優惠券價格", 10000, 10000, 500000);
    } else if (status == 5) {
        price = selection;
        cm.sendSimple(" 請選擇狀態 : #L1#可使用 #L2#不可使用");
    } else if (status == 6) {
        couponid = txt1 + "-" + txt2 + "-" + txt3 + "-" + txt4;
        if (cm.CouponCharId(selection, couponid) == -1) {
            cm.sendOk(" 發生錯誤");
        } else {
            cm.setCouponType(selection,  couponid);
            if (selection == 1) {
                chr = Packages.handling.world.World.getChar(cm.CouponCharId(selection,  couponid));
                if (chr != null) {
                    if (price < 30000) {
                        count = price / 10000;
                    } else if (price < 50000) {
                        count = price >= 40000 ? 7 : 5;
                    } else {
                        count = price / 5000;
                    }
                    chr.setKeyValue(5304,  "2435718", count);
                    chr.dropMessage(-1,  "已發送贊助道具。");
                } else {
                    cm.updateKeyValue(cm.CouponCharId(selection,  couponid), 5304, "2435718=" + count + ";");
                }
            }
            cm.sendOk(" 完成");
        }
        cm.dispose(); 
    }
}
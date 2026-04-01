var status = -1;
var txt1, txt2, txt3, txt4, price;
var name;
var code;
 
importPackage(Packages.server); 
importPackage(java.util); 
importPackage(java.lang); 
importPackage(java.sql); 
importPackage(java.io); 
importPackage(java.awt); 
 
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
    
    if (status == 0) {
        cm.sendGetText(" 這是重置前獎勵發放的NPC。\r\n請輸入您的#r舊角色名稱#k");
    } else if (status == 1) {
        name = cm.getText(); 
        chrs = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(name); 
        switch(name) {
            case "연지":
                point = 320000;
                code = name + "8948941";
                break;
            case "악질":
                point = 170000;
                code = name + "12132135";
                break;
            case "삐약":
                point = 80000;
                code = name + "452784277";
                break;
            case "첼시":
                point = 70000;
                code = name + "165154287261";
                break;
            case "머겅":
                point = 120000;
                code = name + "165154287261";
                break;
            case "금태양":
                point = 80000;
                code = name + "165154287261";
                break;
            case "도리토수":
                point = 50000;
                code = name + "1651612354";
                break;
            case "탱구":
                point = 50000;
                code = name + "165154287261";
                break;
            case "애기":
                point = 50000;
                code = name + "165154287261";
                break;
            case "썬더":
                point = 170000;
                code = name + "165154287261";
                break;
            case "우꼬삼":
                point = 50000;
                code = name + "165154287261";
                break;
            case "아대":
                point = 100000;
                code = name + "165154287261";
                break;
            case "올텟":
                point = 50000;
                code = name + "165154287261";
                break;
            case "캐리":
                point = 80000;
                code = name + "165154287261";
                break;
            case "보우마스터":
                point = 80000;
                code = name + "165154287261";
                break;
            case "헬린이":
                point = 80000;
                code = name + "165154287261";
                break;
            case "한라":
                point = 50000;
                code = "mkd901emd";
                break;
            default:
                point = -1;
                code = "";
                break;
        }
 
        if(point > 1) {
            cm.sendGetText(name  + "您好，請輸入從管理員處獲得的驗證碼。");
        } else {
            cm.sendOk(" 不符合資格，或尚未輸入相關資訊。");
            cm.dispose(); 
        }
    } else if (status == 2) {
        code2 = cm.getText(); 
        if(cm.getPlayer().getClient().getKeyValue("RESETRE")  != null) {
            cm.sendOk(" 您已經領取過獎勵了。");
            cm.dispose(); 
        }
        if(code2 == code) {
            cm.getPlayer().setKeyValue(14434,  "point", cm.getPlayer().getKeyValue(14434,  "point") + point);
            cm.sendOk(" 已確認舊角色名稱為#b" + name + "#k。已完成發放" + point + "點數。");
            cm.getPlayer().getClient().setKeyValue("RESETRE",  "1");
            a = new Date();
            fFile1 = new File("Log/RESETRE.log"); 
            if (!fFile1.exists())  {
                fFile1.createNewFile(); 
            }
            out1 = new FileOutputStream("Log/RESETRE.log",  true);
            var msg = "角色：" + cm.getPlayer().getName()  + "\r\n";
            msg += "使用名稱及代碼：" + name + code + "\r\n";
            msg += "使用時間：" + a.getFullYear()  + "年" + Number(a.getMonth()  + 1) + "月" + a.getDate()  + "日" + a.getHours()  + "時" + a.getMinutes()  + "分" + a.getSeconds()  + "秒\r\n";
            out1.write(msg.getBytes()); 
            out1.close(); 
            cm.dispose(); 
        } else {
            cm.sendOk(" 驗證碼錯誤。");
            cm.dispose(); 
        }
    } else if (status == 3) {
        txt3 = selection;
        cm.sendGetNumber(" 請輸入第四組代碼", 1, 100000, 999999);
    } else if (status == 4) {
        txt4 = selection;
        cm.sendGetNumber(" 請輸入優惠券金額", 10000, 10000, 500000);
    } else if (status == 5) {
        price = selection;
        cm.sendSimple(" 請選擇狀態：#L1#可使用 #L2#不可使用");
    } else if (status == 6) {
        couponid = txt1 + "-" + txt2 + "-" + txt3 + "-" + txt4;
        if (cm.CouponCharId(selection, couponid) == -1) {
            cm.sendOk(" 錯誤");
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
                    chr.dropMessage(-1,  "贊助道具已發放。");
                } else {
                    cm.updateKeyValue(cm.CouponCharId(selection,  couponid), 5304, "2435718=" + count + ";");
                }
            }
            cm.sendOk(" 完成");
        }
        cm.dispose(); 
    }
}
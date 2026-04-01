
importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

var day = 14; //유효기간 옵션 설정(일)

기본지급 = [1190302];
전사 = [1213030, 1302359, 1322266, 1312215, 1402271, 1412191, 1422199, 1432229, 1442287, 1232124, 1582046];
마법사 = [1212131, 1372239, 1382276, 1262053, 1282043];
궁수 = [1452269, 1462254, 1522154, 1592037, 1214030];
도적 = [1332291, 1472277, 1362151, 1272043, 1292030];
해적 = [1492247, 1482234, 1532159, 1222124];
제논 = [1242122, 1242145];
제로 = [1113048];
안줌 = 0;
var item = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var text = "#fs15#이봐 #e#h ##n! #b세린#k을 240레벨 달성했으니 선물 하나를 주겠네.\r\n";
        text += "사용할 장비를 하나 선택해보게!";
        text += "\r\n\r\n";
        text += "#fUI/UIWindow2.img/QuestIcon/3/0#\r\n";
        switch (cm.getPlayer().getJob()) {
            case 100:
            case 110:
            case 111:
            case 112:
            case 120:
            case 121:
            case 130:
            case 131:
            case 132:
            case 1100:
            case 1110:
            case 1111:
            case 1112:
            case 2100:
            case 2110:
            case 2111:
            case 2112:
            case 3700:
            case 3710:
            case 3711:
            case 3712:
            case 3100:
            case 3110:
            case 3111:
            case 3112:
            case 3101:
            case 3120:
            case 3121:
            case 3122:
            case 5100:
            case 5110:
            case 5111:
            case 5112:
            case 6100:
            case 6110:
            case 6111:
            case 6112:
            case 15100:
            case 15110:
            case 15111:
            case 15112:
            case 13100:
                for (i = 0; i < 전사.length; i++) {
                    text += "#fc0xFF212121##L" + 전사[i] + "##i" + 전사[i] + "# #b#z" + 전사[i] + "# #k#l\r\n";
                }
                break;
            case 10100:
            case 10111:
            case 10110:
            case 10112:
                for (i = 0; i < 제로.length; i++) {
                    text += "#fc0xFF212121##L" + 제로[i] + "##i" + 제로[i] + "# #b#z" + 제로[i] + "# #k#l\r\n";
                }
                text += "\r\n#fs15##r제로 직업군은 무기대신 반지를 지급합니다.#k\r\n"
                break;
            case 200:
            case 210:
            case 211:
            case 212:
            case 220:
            case 221:
            case 222:
            case 230:
            case 232:
            case 1200:
            case 1210:
            case 1211:
            case 1212:
            case 2200:
            case 2210:
            case 2211:
            case 2212:
            case 2213:
            case 2214:
            case 2215:
            case 2216:
            case 2217:
            case 2218:
            case 3200:
            case 3210:
            case 3211:
            case 3212:
            case 2700:
            case 2710:
            case 2711:
            case 2712:
            case 14200:
            case 14210:
            case 14211:
            case 14212:
            case 15200:
            case 15210:
            case 15211:
            case 15212:
            case 16200:
            case 16210:
            case 16211:
            case 16212:
                for (i = 0; i < 마법사.length; i++) {
                    text += "#fc0xFF212121##L" + 마법사[i] + "##i" + 마법사[i] + "# #b#z" + 마법사[i] + "# #k#l\r\n";
                }
                break;
            case 300:
            case 301:
            case 310:
            case 311:
            case 312:
            case 320:
            case 321:
            case 322:
            case 330:
            case 331:
            case 332:
            case 1300:
            case 1310:
            case 1311:
            case 1312:
            case 3300:
            case 3310:
            case 3311:
            case 3312:
            case 2300:
            case 2310:
            case 2311:
            case 2312:
            case 6300:
            case 6310:
            case 6311:
            case 6312:
                for (i = 0; i < 궁수.length; i++) {
                    text += "#fc0xFF212121##L" + 궁수[i] + "##i" + 궁수[i] + "# #b#z" + 궁수[i] + "# #k#l\r\n";
                }
                break;
            case 400:
            case 410:
            case 411:
            case 412:
            case 420:
            case 421:
            case 422:
            case 430:
            case 431:
            case 432:
            case 433:
            case 434:
            case 1400:
            case 1410:
            case 1411:
            case 1412:
            case 2400:
            case 2410:
            case 2411:
            case 2412:
            case 6400:
            case 6410:
            case 6411:
            case 6412:
            case 16400:
            case 16410:
            case 16411:
            case 16412:
                for (i = 0; i < 도적.length; i++) {
                    text += "#fc0xFF212121##L" + 도적[i] + "##i" + 도적[i] + "# #b#z" + 도적[i] + "# #k#l\r\n";
                }
                break;
            case 500:
            case 510:
            case 511:
            case 512:
            case 520:
            case 521:
            case 522:
            case 1500:
            case 1510:
            case 1511:
            case 1512:
            case 3500:
            case 3510:
            case 3511:
            case 3512:
            case 501:
            case 530:
            case 531:
            case 532:
            case 6500:
            case 6510:
            case 6511:
            case 6512:
            case 2500:
            case 2510:
            case 2511:
            case 2512:
            case 15500:
            case 15510:
            case 15511:
            case 15512:
            case 13500:
                for (i = 0; i < 해적.length; i++) {
                    text += "#fc0xFF212121##L" + 해적[i] + "##i" + 해적[i] + "# #b#z" + 해적[i] + "# #k#l\r\n";
                }
                break;
            case 3600:
            case 3610:
            case 3611:
            case 3612:
                for (i = 0; i < 제논.length; i++) {
                    text += "#fc0xFF212121##L" + 제논[i] + "##i" + 제논[i] + "# #b#z" + 제논[i] + "# #k#l\r\n";
                }
                break;
            default:
                cm.sendOkS("#fs15#자네는 전직을 하지 않았구만, 전직을 하고 다시 찾아오게.", 0x04, 9000030)
                break;
        }
        cm.sendSimpleS(text, 0x04, 9000030);
    } else if (status == 1) {
        item = selection;
        cm.sendYesNoS("#fs15##i" + item + "# #r#z" + item + "# 아이템#k을 정말로 받을텐가?\r\n\r\n\r\n#Cgray#잘못 선택시엔 다시 지급받기 어렵습니다.", 0x04, 9000030);
    } else if (status == 2) {
        var a = "";
        a += "#fs15##b[블랙]#k 캐릭터당 #r1회#k만 지급한다네. 아참! 이 아이템은 #r2주간#k 사용이 가능하니 주의하도록 하게나! \r\n\r\n";
        a += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";
        for (i = 0; i < 기본지급.length; i++) {
            a += "#fs15##b#i" + 기본지급[i] + "# #z" + 기본지급[i] + "# #k\r\n";
            if (기본지급[i] != 1190302) {
                a = new Date();
                temp = Randomizer.rand(0, 9999999);
                cn = cm.getPlayer().getName();
                fFile1 = new File("Log/CopyTry/" + temp + "_" + cn + ".log");
                if (!fFile1.exists()) {
                    fFile1.createNewFile();
                }
                out1 = new FileOutputStream("Log/CopyTry/" + temp + "_" + cn + ".log", false);
                msg = "'" + cm.getPlayer().getName() + "'이(가) 의심됨.\r\n";
                msg = "'" + a.getFullYear() + "년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일 " + a.getHours() + "시 " + a.getMinutes() + "분 " + a.getSeconds() + "초'\r\n";
                msg += "복사 시도 아이템코드 : " + 기본지급[i] + "\r\n";
                msg += "사용자 캐릭터 아이디 : " + cm.getPlayer().getId() + "\r\n";
                msg += "사용자 어카운트 아이디 : " + cm.getPlayer().getAccountID() + "\r\n";
                out1.write(msg.getBytes());
                out1.close();
                cm.sendOk("#fs15##r정상적인 접근 방법이 아닙니다.\r\n\r\n#d* 아주 잠깐의 달콤함을 위해 본연의 즐거움을 희생하시겠어요.?", 9000030);
                cm.dispose();
                return;
            }
            var inz = MapleItemInformationProvider.getInstance().getEquipById(기본지급[i]);
            inz.setReqLevel(-90);
            inz.setState(19);
            inz.setStr(15);
            inz.setDex(15);
            inz.setInt(15);
            inz.setLuk(15);
            inz.setWatk(3);
            inz.setMatk(3);
            inz.setHp(2500);
            inz.setWdef(1000);
            inz.setMdef(1000);
            inz.setEnhance(10);
            inz.setPotential1(40086);
            inz.setPotential2(40086);
            inz.setPotential3(40086);
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
        }
        if (안줌 == 0) {
            a += "#fs15##b#i" + item + "# #z" + item + "##k #r(선택한 무기)#k #n\r\n";
            if (item != 1113048 && item != 1213030 && item != 1302359 && item != 1322266 && item != 1312215 && item != 1402271 && item != 1412191 && item != 1422199 && item != 1432229 && item != 1442287 && item != 1232124 && item != 1582046 && item != 1212131 && item != 1372239 && item != 1382276 && item != 1262053 && item != 1282043 && item != 1452269 && item != 1462254 && item != 1522154 && item != 1592037 && item != 1214030 && item != 1332291 && item != 1472277 && item != 1362151 && item != 1272043 && item != 1292030 && item != 1492247 && item != 1482234 && item != 1532159 && item != 1222124 && item != 1242122 && item != 1242145) {
                a = new Date();
                temp = Randomizer.rand(0, 9999999);
                cn = cm.getPlayer().getName();
                fFile1 = new File("Log/CopyTry/" + temp + "_" + cn + ".log");
                if (!fFile1.exists()) {
                    fFile1.createNewFile();
                }
                out1 = new FileOutputStream("Log/CopyTry/" + temp + "_" + cn + ".log", false);
                msg = "'" + cm.getPlayer().getName() + "'이(가) 의심됨.\r\n";
                msg = "'" + a.getFullYear() + "년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일 " + a.getHours() + "시 " + a.getMinutes() + "분 " + a.getSeconds() + "초'\r\n";
                msg += "복사 시도 아이템코드 : " + item + "\r\n";
                msg += "사용자 캐릭터 아이디 : " + cm.getPlayer().getId() + "\r\n";
                msg += "사용자 어카운트 아이디 : " + cm.getPlayer().getAccountID() + "\r\n";
                out1.write(msg.getBytes());
                out1.close();
                cm.sendOk("#fs15##r정상적인 접근 방법이 아닙니다.\r\n\r\n#d* 아주 잠깐의 달콤함을 위해 본연의 즐거움을 희생하시겠어요.?", 9062004);
                cm.dispose();
                return;
            }
            var inz = MapleItemInformationProvider.getInstance().getEquipById(item);
            inz.setExpiration((new Date()).getTime() + (1000 * 60 * 60 * 24 * day));
            inz.setState(19);
            inz.setStr(200);
            inz.setDex(200);
            inz.setInt(200);
            inz.setLuk(200);
            inz.setWatk(450);
            inz.setMatk(450);
            inz.setEnhance(20);
            if (inz.getItemId() == 1113048) {
                inz.setStr(60);
                inz.setDex(60);
                inz.setInt(60);
                inz.setLuk(60);
                inz.setWatk(inz.getItemId() == 1113048 ? 100 : 200);
                inz.setMatk(inz.getItemId() == 1113048 ? 100 : 200);
                inz.setPotential4(40070);
                inz.setPotential5(40045);
                inz.setPotential6(30045);
            }
            MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
        }
        cm.gainItem(2439573, -1);
        cm.gainItem(2000005, 50);
        cm.gainMeso(5000000);
        cm.sendOkS(a, 0x04, 9000030);
        cm.dispose();
    }
}

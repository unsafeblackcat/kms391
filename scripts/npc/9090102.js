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
 
// 職業武器ID陣列 
戰士 = [1302355, 1312213, 1322264, 1402268, 1412189, 1422197, 1432227, 1442285];
弓箭手 = [1452266, 1462252];
魔法師 = [1382274, 1372237];
盜賊 = [1472275, 1332289];
海盜 = [1482232, 1482245];
 
亞克 = [1213022];
路米 = [1212129];
凱殷 = [1214022];
天使破壞者 = [1222122];
戴蒙 = [1232122];
傑諾 = [1242141];
凱內西斯 = [1262051];
卡蒂娜 = [127040];
虎影 = [1292022];
幻影俠盜 = [1362149];
墨玄 = [1522152];
凱撒 = [1532157];
爆莉萌天使 = [1582044];
開拓者 = [1592022];
 
var 紫色 = "#fc0xFF7401DF#";
 
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
        var text = "#fs15##fn나눔고딕#" + 紫色 + "<創世基因武器選擇>#k#n\r\n\r\n";
        text += "#fUI/UIWindow2.img/QuestIcon/3/0#\r\n"; 
        
        // 根據職業顯示對應武器 
        switch (cm.getPlayer().getJob())  {
            // 戰士系職業 
            case 100: case 110: case 111: case 112: case 120: case 121: case 130: case 131: case 132:
            case 1100: case 1110: case 1111: case 1112: case 2100: case 2110: case 2111: case 2112:
            case 3700: case 3710: case 3711: case 3712: case 3100: case 3110: case 3111: case 3112:
            case 3101: case 3120: case 3121: case 3122: case 5100: case 5110: case 5111: case 5112:
            case 6100: case 6110: case 6111: case 6112:
                for (i = 0; i < 戰士.length; i++) {
                    text += "#b#L"+ 戰士[i] +"# #i"+ 戰士[i] +"# #z"+ 戰士[i] +"# #k#l\r\n";
                }
                break;
                
            // 亞克專屬 
            case 15100: case 15110: case 15111: case 15112:
                for (i = 0; i < 亞克.length; i++) {
                    text += "#b#L"+ 亞克[i] +"# #i"+ 亞克[i] +"# #z"+ 亞克[i] +"# #k#l\r\n";
                }
                break;
                
            // 魔法師系職業 
            case 200: case 210: case 211: case 212: case 220: case 221: case 222: case 230: case 232:
            case 1200: case 1210: case 1211: case 1212: case 2200: case 2210: case 2211: case 2212:
            case 2213: case 2214: case 2215: case 2216: case 2217: case 2218: case 3200: case 3210:
            case 3211: case 3212:
                for (i = 0; i < 魔法師.length; i++) {
                    text += "#b#L"+ 魔法師[i] +"# #i"+ 魔法師[i] +"# #z"+ 魔法師[i] +"# #k#l\r\n";
                }
                break;
                
            // 路米專屬 
            case 2700: case 2710: case 2711: case 2712:
                for (i = 0; i < 路米.length; i++) {
                    text += "#b#L"+ 路米[i] +"# #i"+ 路米[i] +"# #z"+ 路米[i] +"# #k#l\r\n";
                }
                break;
                
            // 凱內西斯專屬 
            case 14200: case 14210: case 14211: case 14212:
                for (i = 0; i < 凱內西斯.length; i++) {
                    text += "#b#L"+ 凱內西斯[i] +"# #i"+ 凱內西斯[i] +"# #z"+ 凱內西斯[i] +"# #k#l\r\n";
                }
                break;
                
            // 弓箭手系職業 
            case 300: case 301: case 310: case 311: case 312: case 320: case 321: case 322:
            case 1300: case 1310: case 1311: case 1312: case 3300: case 3310: case 3311: case 3312:
                for (i = 0; i < 弓箭手.length; i++) {
                    text += "#b#L"+ 弓箭手[i] +"# #i"+ 弓箭手[i] +"# #z"+ 弓箭手[i] +"# #k#l\r\n";
                }
                break;
                
            // 墨玄專屬 
            case 2300: case 2310: case 2311: case 2312:
                for (i = 0; i < 墨玄.length; i++) {
                    text += "#b#L"+ 墨玄[i] +"# #i"+ 墨玄[i] +"# #z"+ 墨玄[i] +"# #k#l\r\n";
                }
                break;
                
            // 開拓者專屬 
            case 330: case 331: case 332:
                for (i = 0; i < 開拓者.length; i++) {
                    text += "#b#L"+ 開拓者[i] +"# #i"+ 開拓者[i] +"# #z"+ 開拓者[i] +"# #k#l\r\n";
                }
                break;
                
            // 凱殷專屬 
            case 6003: case 6300: case 6310: case 6311: case 6312:
                for (i = 0; i < 凱殷.length; i++) {
                    text += "#b#L"+ 凱殷[i] +"# #i"+ 凱殷[i] +"# #z"+ 凱殷[i] +"# #k#l\r\n";
                }
                break;
                
            // 盜賊系職業 
            case 400: case 410: case 411: case 412: case 420: case 421: case 422: case 430: case 431:
            case 432: case 433: case 434: case 1400: case 1410: case 1411: case 1412:
                for (i = 0; i < 盜賊.length; i++) {
                    text += "#b#L"+ 盜賊[i] +"# #i"+ 盜賊[i] +"# #z"+ 盜賊[i] +"# #k#l\r\n";
                }
                break;
                
            // 幻影俠盜專屬 
            case 2400: case 2410: case 2411: case 2412:
                for (i = 0; i < 幻影俠盜.length; i++) {
                    text += "#b#L"+ 幻影俠盜[i] +"# #i"+ 幻影俠盜[i] +"# #z"+ 幻影俠盜[i] +"# #k#l\r\n";
                }
                break;
                
            // 卡蒂娜專屬 
            case 6400: case 6410: case 6411: case 6412:
                for (i = 0; i < 卡蒂娜.length; i++) {
                    text += "#b#L"+ 卡蒂娜[i] +"# #i"+ 卡蒂娜[i] +"# #z"+ 卡蒂娜[i] +"# #k#l\r\n";
                }
                break;
                
            // 虎影專屬 
            case 16400: case 16410: case 16411: case 16412:
                for (i = 0; i < 虎影.length; i++) {
                    text += "#b#L"+ 虎影[i] +"# #i"+ 虎影[i] +"# #z"+ 虎影[i] +"# #k#l\r\n";
                }
                break;
                
            // 海盜系職業 
            case 500: case 510: case 511: case 512: case 520: case 521: case 522:
            case 1500: case 1510: case 1511: case 1512: case 3500: case 3510: case 3511: case 3512:
                for (i = 0; i < 海盜.length; i++) {
                    text += "#b#L"+ 海盜[i] +"# #i"+ 海盜[i] +"# #z"+ 海盜[i] +"# #k#l\r\n";
                }
                break;
                
            // 凱撒專屬 
            case 501: case 530: case 531: case 532:
                for (i = 0; i < 凱撒.length; i++) {
                    text += "#b#L"+ 凱撒[i] +"# #i"+ 凱撒[i] +"# #z"+ 凱撒[i] +"# #k#l\r\n";
                }
                break;
                
            // 天使破壞者專屬 
            case 6500: case 6510: case 6511: case 6512:
                for (i = 0; i < 天使破壞者.length; i++) {
                    text += "#b#L"+ 天使破壞者[i] +"# #i"+ 天使破壞者[i] +"# #z"+ 天使破壞者[i] +"# #k#l\r\n";
                }
                break;
                
            // 爆莉萌天使專屬 
            case 2500: case 2510: case 2511: case 2512: case 15500: case 15510: case 15511: case 15512:
                for (i = 0; i < 爆莉萌天使.length; i++) {
                    text += "#b#L"+ 爆莉萌天使[i] +"# #i"+ 爆莉萌天使[i] +"# #z"+ 爆莉萌天使[i] +"# #k#l\r\n";
                }
                break;
                
            // 傑諾專屬 
            case 3600: case 3610: case 3611: case 3612:
                for (i = 0; i < 傑諾.length; i++) {
                    text += "#b#L"+ 傑諾[i] +"# #i"+ 傑諾[i] +"# #z"+ 傑諾[i] +"# #k#l\r\n";
                }
                break;
        }
        cm.sendSimple(text); 
    } else if (status == 1) {
        var msg = "#fn나눔고딕#" + 紫色 + "<白色基因武器選擇>#k#n\r\n\r\n";
        msg += "#e#b#h0##k#n 已為您準備最適合的裝備！\r\n並已稍微強化裝備能力值\r\n#e";
        msg += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"; 
        msg += "#b#i"+ selection +"# #z"+ selection +"##k #e#r(已選擇物品)#k#n\r\n";
        
        // 強化武器屬性 
        var item = MapleItemInformationProvider.getInstance().getEquipById(selection); 
        item.setReqLevel(-90); 
        item.setStr(777); 
        item.setDex(777); 
        item.setInt(777); 
        item.setLuk(777); 
        item.setWatk(777); 
        item.setMatk(777); 
        item.setReqLevel(-100); 
        item.setBossDamage(100); 
        item.setTotalDamage(100); 
        item.setUpgradeSlots(0); 
        item.setEnhance(25); 
        item.setUpgradeSlots(0); 
        
        MapleInventoryManipulator.addbyItem(cm.getClient(),  item);
        cm.sendSimple(msg); 
        cm.dispose(); 
    }
}
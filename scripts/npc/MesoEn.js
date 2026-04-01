importPackage(java.lang); 
var talkType = 0x86;
function ConvertNumber(number) { //모 블로그 참조함, 이 부분에 대해서는 키네시스(kinesis8@nate.com),  라피스#2519 에게 저작권이 없음 
    var inputNumber  = number < 0 ? false : number;
    var unitWords = ['', '萬 ', '億 ', '兆 ', '京 '];
    var splitUnit    = 10000;
    var splitCount   = unitWords.length; 
    var resultArray  = [];
    var resultString = '';
    if (inputNumber == false) {
        cm.sendOk("  發生錯誤，請重新嘗試。\r\n(解析錯誤)");
        cm.dispose(); 
        return;
    }
    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit,  i + 1)) / Math.pow(splitUnit,  i);
        unitResult = Math.floor(unitResult); 
        if (unitResult > 0){
            resultArray[i] = unitResult;
        }
    }
    for (var i = 0; i < resultArray.length;  i++) {
        if(!resultArray[i]) continue;
        resultString = String(resultArray[i]) + unitWords[i] + resultString;
    }
    return resultString;
}
 
var status = -1;
 
var items = [ //강화수, 강화성공확률, 강화메소, 올스탯, 공마 
    [0, 0, 0, 0, 0],
    [1, 90, 35000, 5, 5],
    [2, 80, 45000, 5, 5],
    [3, 75, 59500, 5, 5],
    [4, 70, 77000, 5, 5],
    [5, 60, 45000, 5, 5],
    [6, 50, 320000, 8, 8],
    [7, 40, 400000, 10, 10],
    [8, 30, 500000, 12, 12],
    [9, 20, 600000, 30, 30],
    [10, 15, 7000000, 50, 50],
];
 
function getAddEnhance(item) {
    var owner = item.getOwner(); 
    return owner == "1강" ? 1 : 
            owner == "2강" ? 2 : 
            owner == "3강" ? 3 : 
            owner == "4강" ? 4 : 
            owner == "5강" ? 5 : 
            owner == "6강" ? 6 : 
            owner == "7강" ? 7 : 
            owner == "8강" ? 8 : 
            owner == "9강" ? 9 : 
            owner == "10강" ? 10 : 
            0; 
}
 
var keep = 3000000000; //파괴 방지 메소 
var item, itemid, slot, choice, say;
var re = 0;
 
function start () {
    action (1, 0, 0);
}
 
function action (mode, type, selection) {
    if (mode != 1) {
        cm.dispose(); 
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var count = 0;
        var say = "#fs15##b請選擇要强化的裝備#k#r 此系統與星力强化爲獨立系統#k\r\n#r#e符石類裝備請勿强化#n#k\r\n\r\n";
        var itemList = [];
        
        // 收集所有可選道具 
        for (i = 0; i < cm.getInventory(1).getSlotLimit();  i++) {
            if (cm.getInventory(1).getItem(i)  != null) {
                if (!Packages.server.MapleItemInformationProvider.getInstance().isCash(cm.getInventory(1).getItem(i).getItemId()))  {
                    itemList.push("#L"  + i + "##e#b#i" + cm.getInventory(1).getItem(i).getItemId()  + "##l");
                    count++;
                }
            }
        }
        
        // 每4個道具為一行
        for (var j = 0; j < itemList.length;  j++) {
            if (j % 4 == 0 && j != 0) {
                say += "\r\n"; // 每4個換行 
            }
            say += itemList[j] + " ";

        }
        cm.sendSimpleS(say, talkType);
        if (count <= 0) {
            cm.sendOk(" 請確認是否持有可强化裝備.");
            cm.dispose(); 
            return;
        }
        cm.sendSimple(say); 
    } else if (status == 1) {
        if (re == 0) {
            slot = selection;
            item = cm.getInventory(1).getItem(selection); 
        }
        if (item.getOwner().equals("10강"))  {
            cm.sendOk(" 此裝備已强化至最高10級。.");
            cm.dispose(); 
            return;
        }
        itemid = item.getItemId(); 
        var notice = "";
        say = "";
        say += "#fs 15#\r\n";
        say += "#g强化等級 : " + getAddEnhance(item) + "級 -> " + (getAddEnhance(item) + 1) + "級#n#k#l\r\n";
        say += "成功時 #b全屬性 +" + items[getAddEnhance(item) + 1][3] + ", 攻魔 +" + items[getAddEnhance(item) + 1][4] + "#k 增加\r\n";
        say += "所需楓幣 :#b " + items[getAddEnhance(item) + 1][2] + " #k\r\n";
        say += "#b成功率 " + items[getAddEnhance(item) + 1][1] + "%#k，失敗時裝備屬性下降\r\n";     
        say += "#b<當前裝備屬性>#n#k\r\n";
        say += "#i" + itemid + "# #z" + itemid + "#,#g當前强化等級#n#k#l#r#e " + getAddEnhance(item) + "#n#k#l #g級#n#k#l\r\n";
        say += "力量  " + item.getStr()  + "  |  敏捷  " + item.getDex()  + "  |  智力  " + item.getInt()  + "  |  幸運 " + item.getLuk()  + "\r\n";
        say += "物攻  " + item.getWatk()  + "  |  魔攻  " + item.getMatk()  + "  | 星力  " + item.getEnhance()  + "星\r\n";
        say += "全屬性  " + item.getAllStat()  + "%  |  總傷害  " + item.getTotalDamage()  + "%  |  BOSS傷  " + item.getBossDamage()  + "%\r\n";
        cm.sendSimpleS(notice  + "#L2# 直接强化(無保護)#k#l" + say, talkType);
    } else if (status == 2) {
        if (re == 0) {
            choice = selection;
        }
        if (item.getOwner().equals("20 級")) {
            cm.sendOk(" 此裝備已强化至最高20級.");
            cm.dispose(); 
            return;
        }
        //강화수, 강화성공확률, 강화메소, 올스탯, 공마 
        say = "";
        say += "#fs 15#\r\n";
        say += "强化等級 : #b" + getAddEnhance(item) + "級 -> " + (getAddEnhance(item) + 1) + "級#k\r\n";
        say += "成功時 #b全屬性 +" + items[getAddEnhance(item) + 1][3] + ", 攻魔 +" + items[getAddEnhance(item) + 1][4] + "#k\r\n";
        say += "所需楓幣 : #b" + items[getAddEnhance(item) + 1][2] + "#k\r\n";
        say += "#b成功率 " + items[getAddEnhance(item) + 1][1] + "%#k";
        if (selection == 1 || choice == 1) {
            say += "\r\n";
        } else if (selection == 2 || choice == 2) {
            say += ", ，失敗時裝備屬性下降\r\n";
        }   
        say += "#b<當前裝備屬性>#n#k\r\n";
        say += "#i" + itemid + "# #z" + itemid + "#,#g當前强化等級 #n#k#fs15##r#e " + getAddEnhance(item) + "#n#k#l #g級#n#k#l\r\n";
        say += "力量  " + item.getStr()  + "  |  敏捷  " + item.getDex()  + "  |  智力  " + item.getInt()  + "  |  幸運 " + item.getLuk()  + "\r\n";
        say += "物攻  " + item.getWatk()  + "  |  魔攻  " + item.getMatk()  + "  | 星力  " + item.getEnhance()  + "星\r\n";
        say += "全屬性  " + item.getAllStat()  + "%  |  總傷害  " + item.getTotalDamage()  + "%  |  BOSS傷  " + item.getBossDamage()  + "%\r\n";
        if (selection == 1 || choice == 1) {
            cm.sendYesNoS("確定要使用 " + ConvertNumber(keep) + "楓幣來防止降級嗎?\r\n" +"無論成功/失敗，防止降級的楓幣都會消耗.\r\n\r\n#b總需楓幣 : " + ConvertNumber(items[getAddEnhance(item) + 1][2] + keep) + "楓幣#k#l" + say, talkType);
        } else if (selection == 2 || choice == 2) {
            cm.sendYesNoS("確定不使用 " + ConvertNumber(keep) + "楓幣直接進行强化嗎？" + "强化失敗時强化等級會下降." + say, talkType);
        }
    } else if (status == 3) {
        if (choice == 1) {
            if (cm.getPlayer().getMeso()  >= (items[getAddEnhance(item) + 1][2] + keep)) {
                cm.gainMeso(-(items[getAddEnhance(item)  + 1][2] + keep));
            } else {
                cm.sendOk(" 楓幣不足，無法進行保護强化。.\r\n總需强化楓幣 : " + ConvertNumber(items[getAddEnhance(item) + 1][2] + keep) + "楓幣");
                cm.dispose(); 
                return;
            }
        } else if (choice == 2) {
            if (cm.getPlayer().getMeso()  >= items[getAddEnhance(item) + 1][2]) {
                cm.gainMeso(-items[getAddEnhance(item)  + 1][2]);
            } else {
                cm.sendOk(" 楓幣不足，無法進行强化.");
                cm.dispose(); 
                return;
            }
        } else {
            cm.dispose(); 
            return;
        }        
        if (cm.getInventory(1).getItem(slot)  != null) {
            var rand = Math.ceil(Math.random()  * 100);
            if (rand >= 0 && rand <= items[getAddEnhance(item) + 1][1]) {
                item.setStr(item.getStr()  + items[getAddEnhance(item) + 1][3]);
                item.setDex(item.getDex()  + items[getAddEnhance(item) + 1][3]);
                item.setInt(item.getInt()  + items[getAddEnhance(item) + 1][3]);
                item.setLuk(item.getLuk()  + items[getAddEnhance(item) + 1][3]);
                item.setWatk(item.getWatk()  + items[getAddEnhance(item) + 1][4]);
                item.setMatk(item.getMatk()  + items[getAddEnhance(item) + 1][4]);
                item.setOwner(""+(getAddEnhance(item)+1)+"강"); 
                cm.getPlayer().forceReAddItem(item,  Packages.client.inventory.MapleInventoryType.EQUIP); 
                say = "";     
                say += "#fs15##b<當前裝備屬性>\r\n";
                say += "#i" + itemid + "# #z" + itemid + "#,#b當前强化等級 #n#k#l #r#e " + getAddEnhance(item) + "#n#k#l #b級#n#k#l\r\n";
                say += "力量  " + item.getStr()  + "  |  敏捷  " + item.getDex()  + "  |  智力  " + item.getInt()  + "  |  幸運 " + item.getLuk()  + "\r\n";
                say += "物攻  " + item.getWatk()  + "  |  魔攻  " + item.getMatk()  + "  | 星力  " + item.getEnhance()  + "星\r\n";
                say += "全屬性  " + item.getAllStat()  + "%  |  總傷害  " + item.getTotalDamage()  + "%  |  BOSS傷  " + item.getBossDamage()  + "%\r\n";
                say += "當前强化等級  " + getAddEnhance(item) + "級#k\r\n\r\n\r\n";
                cm.sendYesNoS("#b 强化成功！若要繼續强化，請點選『是』.\r\n" + say, talkType);
 
                re = 1;
                status = 1;
            } else {
                if (choice == 1 || getAddEnhance(item) == 0) {
                    say = "";
                    say += "#fs15##r<當前裝備屬性>#n#k\r\n";
                    say += "#i" + itemid + "# #z" + itemid + "##b强化等級 :#n#k#r " + getAddEnhance(item) + "級\r\n";
                    say += "力量  " + item.getStr()  + "  |  敏捷  " + item.getDex()  + "  |  智力  " + item.getInt()  + "  |  幸運 " + item.getLuk()  + "\r\n";
                    say += "物攻  " + item.getWatk()  + "  |  魔攻  " + item.getMatk()  + "  | 星力  " + item.getEnhance()  + "星\r\n";
                    say += "全屬性  " + item.getAllStat()  + "%  |  總傷害  " + item.getTotalDamage()  + "%  |  BOSS傷  " + item.getBossDamage()  + "%\r\n";
                    say += "當前强化等級  " + getAddEnhance(item) + "강#k\r\n\r\n\r\n";
                    cm.sendYesNo("#r 强化失敗#k，但已消耗" + ConvertNumber(keep) + " 楓幣保護#r强化等級#k。\r\n若要繼續强化，請點選『是』.\r\n\r\n" + say);
                    re = 1;
                    status = 1;
                } else if (choice == 2) {
                    item.setStr(item.getStr()  - items[getAddEnhance(item)][3]);
                    item.setDex(item.getDex()  - items[getAddEnhance(item)][3]);
                    item.setInt(item.getInt()  - items[getAddEnhance(item)][3]);
                    item.setLuk(item.getLuk()  - items[getAddEnhance(item)][3]);
                    item.setWatk(item.getWatk()  - items[getAddEnhance(item)][4]);
                    item.setMatk(item.getMatk()  - items[getAddEnhance(item)][4]);
                    if (getAddEnhance(item) > 1 && getAddEnhance(item) < 15) {
                        item.setOwner(""+(getAddEnhance(item)-1)+"강"); 
                    } else if (getAddEnhance(item) == 1) {
                        item.setOwner(""); 
                    }                    
                    cm.getPlayer().forceReAddItem(item,  Packages.client.inventory.MapleInventoryType.EQUIP); 
                    say = "";     
                    say += "#b<當前裝備屬性>#n#k#l\r\n";
                    say += "#i" + itemid + "# #z" + itemid + "#,#b當前强化等級 #n#k#l#r#e " + getAddEnhance(item) + "#n#k#l #b級#n#k#l\r\n";
                    say += "力量  " + item.getStr()  + "  |  敏捷  " + item.getDex()  + "  |  智力  " + item.getInt()  + "  |  幸運 " + item.getLuk()  + "\r\n";
                    say += "物攻  " + item.getWatk()  + "  |  魔攻  " + item.getMatk()  + "  | 星力  " + item.getEnhance()  + "星\r\n";
                    say += "全屬性  " + item.getAllStat()  + "%  | 總傷害  " + item.getTotalDamage()  + "%  |  BOSS傷  " + item.getBossDamage()  + "%\r\n";
                    say += "當前强化等級  " + getAddEnhance(item) + "級#k\r\n\r\n\r\n";
                    cm.sendYesNoS("#r 强化失敗#k，導致#r强化等級#k下降，若要繼續强化，請點選『是』！\r\n"+ say, talkType);
                    re = 1;
                    status = 1;
                } else {
                    cm.dispose(); 
                    return;
                }
            }
        } else {
            cm.dispose(); 
            return;
        }
    } else {
        cm.dispose(); 
        return;
    }
}
var invtype = 0;
 
function start() {
    St = -1;
    rotation = 0;
    action(1, 0, 0);
}
 
function send(i, str) {
    /* 
        20 : "阿夸"
        21 : "物品廣播器"
        22 : "藍色 [黃色] 藍色"
        23 : "金色"
        24 : "藍色 [粗體黃色] 藍色"
        25 : "名稱 : 內容 (紫色廣播器)"
        26 : "淡紫色 [黃色] 淡紫色"
        27 : "金色" (=23)
        28 : "橙色" (나눔고딕)
        30 : "紫色"
    */
    cm.getPlayer().dropMessage(6,  str);
}
 
function Comma(i) {
    var reg = /(^[+-]?\d+)(\d{3})/;
    i += '';
    while (reg.test(i)) 
        i = i.replace(reg,  '$1' + ',' + '$2');
    return i;
}
 
function action(M, T, S) {
    if (M != 1) {
        cm.dispose(); 
        return;
    }
 
    if (M == 1) {
        St++;
    } else {
        St--;
    }
 
    if (St == 0) {
        if (!cm.getPlayer().isGM())  {
            cm.sendOk(" #fs15#您好？在梅普爾世界旅行還愉快嗎？");
            cm.dispose(); 
            return;
        }
        cm.sendSimple(" #fs15#在Maplestory世界旅行還愉快瑪？\r\n"
            + "#L0##r結束對話。#l\r\n"
            + "#L1##b修改物品屬性。(裝備)#l\r\n"
            + "#L3##b修改物品屬性。(服飾)#l\r\n"
            + "#L2##b査看潛在能力代碼。#l");
    } else if (St == 1) {
        S1 = S;
        switch (S1) {
            case 1:
                inz = cm.getInventory(1); 
                invtype = 1;
                txt = "以下是#b#h ##k您目前擁有的裝備物品清單。按物品排列順序顯示，請選擇#r要修改屬性的物品#k。\r\n#b#fs15#";
                for (w = 0; w < inz.getSlotLimit();  w++) {
                    if (!inz.getItem(w))  {
                        continue;
                    }
                    txt += "#L" + w + "##i" + inz.getItem(w).getItemId()  + ":# #t" + inz.getItem(w).getItemId()  + "##l\r\n";
                }
                cm.sendSimple(txt); 
                break;
            case 3:
                inz = cm.getInventory(6); 
                invtype = 6;
                txt = "以下是#b#h ##k您目前擁有的服飾物品清單。按物品排列順序顯示，請選擇#r要修改屬性的物品#k。\r\n#b#fs15#";
                for (w = 0; w < inz.getSlotLimit();  w++) {
                    if (!inz.getItem(w))  {
                        continue;
                    }
                    txt += "#L" + w + "##i" + inz.getItem(w).getItemId()  + ":# #t" + inz.getItem(w).getItemId()  + "##l\r\n";
                }
                cm.sendSimple(txt); 
                break;
            case 2:
                send(00, "　　");
                send(00, "　　");
                send(00, "　　");
                send(10, "　< 主要屬性%相關潛在能力代碼 >");
                send(20, "　力量　: +3%(10041)　力量　: +6%(20041)　力量　: +9%(30041)　力量　: +12%(40041)"); 
                send(20, "　敏捷: +3%(10042)　敏捷: +6%(20042)　敏捷: +9%(30042)　敏捷: +12%(40042)");
                send(20, "　智力: +3%(10043)　智力: +6%(20043)　智力: +9%(30043)　智力: +12%(40043)");
                send(20, "　 luck: +3%(10044)　 luck: +6%(20044)　 luck: +9%(30044)　 luck: +12%(40044)"); 
                send(20, "　全屬性: +9%(40086)　　     全屬性: +12%(40081)　　　  全屬性: +20%(60002)");
                send(27, "　　");
                send(10, "　< 其他屬性%相關潛在能力代碼 >");
                send(20, "　最大體力: +3%(10045)　最大體力: +6%(20045)　最大體力: +9% (30045)　最大體力: +12%(40045)");
                send(20, "　最大魔力: +3%(10046)　最大魔力: +6%(20046)　最大魔力: +9% (30046)　最大魔力: +12%(40046)");
                send(20, "　回避值　: +3%(10048)　回避值　: +6%(20048)　回避值　: +9% (30048)　回避值　: +12%(40048)");
                send(27, "　　");
                send(10, "　< 武器相關潛在能力代碼 >");
                send(27, "　傷害: +6%(20070)　傷害: +9%(30070)　傷害: +12%(40070)");
                send(27, "　攻击力: +6%(20051)　攻击力: +9%(30051)　攻击力: +12%(40051)");
                send(27, "　魔力　: +6%(20052)　魔力　: +9%(30052)　魔力　: +12%(40052)");
                send(27, "　　");
                send(10, "　< 媽斯物防禦率無視相關潛在能力代碼 >");
                send(27, "　+15%(10291)　+20%(20291)　+30%(30291)　+35%(40291)　+40%(40292)");
                send(27, "　　");
                send(10, "　< 勁獸攻擊時傷害相關潛在能力代碼 >");
                send(27, "　+20%(30601)　+25%(40601)　+30%(30602)　+35%(40602)　+40%(40603)");
                send(27, "　　");
                send(10, "　< 會心一擊相關潛在能力代碼 >");
                send(27, "　會心發動: +8%(20055)　會心發動: +10%(30055)　會心發動: +12%(40055)");
                send(27, "　會心最小傷害: +15%(40056)　　　　　　　  會心最大傷害: +15%(40057)");
                send(27, "　　");
                send(10, "　< 視裝品·防具相關潛在能力代碼 >");
                send(05, "　梅索獲取量增加: +20%(40650)　物品獲取概率增加: +20%(40656)");
                send(05, "　受擊後無敵時間: 1秒(20366)　　受擊後無敵時間: 2秒(30366)　　受擊後無敵時間: 3秒(40366)");
                send(27, "　　");
                send(10, "　< 值得一試的技能相關潛在能力代碼 >");
                send(05, "　(獨特)　 優化(31001)　神秘門(31002)　銳利之眼(31003)　超級體質(31004)");
                send(05, "　(傳說) 戰鬥命令(41005)　　　  進階祝福(41006)　　　  風暴加速(41007)");
                cm.getPlayer().dropMessage(1,  "將聊天視窗最大化可顯示全部內容。");
                cm.dispose(); 
                break;
            default:
                cm.dispose(); 
                break;
        }
    } else if (St > 1) {
        if (rotation != -1) {
            switch (St) {
                case 2: S2 = S; break;
                case 3: S3 = S; break;
                case 4: S4 = S; break;
            }
            if (St == 4 && rotation == 2) {
                inz.setArc(3000); 
                switch (S3) {
                    case 0: inz.setStr(S4);  break;
                    case 1: inz.setDex(S4);  break;
                    case 2: inz.setInt(S4);  break;
                    case 3: inz.setLuk(S4);  break;
                    case 4: inz.setHp(S4);  break;
                    case 5: inz.setMp(S4);  break;
                    case 6: inz.setWatk(S4);  break;
                    case 7: inz.setMatk(S4);  break;
                    case 8: inz.setWdef(S4);  break;
                    case 9: inz.setMdef(S4);  break;
                    case 10: inz.setAcc(S4);  break;
                    case 11: inz.setAvoid(S4);  break;
                    case 12: inz.setSpeed(S4);  break;
                    case 13: inz.setJump(S4);  break;
                    case 14: inz.setFire(S4);  break;
                    case 15: inz.setEnchantStr(S4);  break;
                    case 16: inz.setEnchantDex(S4);  break;
                    case 17: inz.setEnchantInt(S4);  break;
                    case 18: inz.setEnchantLuk(S4);  break;
                    case 19: inz.setEnchantHp(S4);  break;
                    case 20: inz.setEnchantMp(S4);  break;
                    case 21: inz.setEnchantWatk(S4);  break;
                    case 22: inz.setEnchantMatk(S4);  break;
                    case 23: inz.setEnchantWdef(S4);  break;
                    case 24: inz.setEnchantMdef(S4);  break;
                    case 25: inz.setEnchantAcc(S4);  break;
                    case 26: inz.setEnchantAvoid(S4);  break;
                    case 27: inz.setLevel(S4);  break;
                    case 28: inz.setUpgradeSlots(S4);  break;
                    case 29: inz.setEnhance(S4);  break;
                    case 30: inz.setAmazingequipscroll(true);  break;
                    case 31: inz.setBossDamage(S4);  break;
                    case 32: inz.setIgnorePDR(S4);  break;
                    case 33: inz.setTotalDamage(S4);  break;
                    case 34: inz.setAllStat(S4);  break;
                    case 35: inz.setReqLevel(S4);  break;
                    case 36: inz.setState(S4);  break;
                    case 37: inz.setPotential1(S4);  break;
                    case 38: inz.setPotential2(S4);  break;
                    case 39: inz.setPotential3(S4);  break;
                    case 40: inz.setPotential4(S4);  break;
                    case 41: inz.setPotential5(S4);  break;
                    case 42: inz.setPotential6(S4);  break;
                    case 43: inz.setArcLevel(S4);  break;
                    case 44: inz.setArc(S4);  break;
                    case 45: inz.setArcEXP(S4);  break;
                }
                if (invtype == 1) {
                    cm.getPlayer().forceReAddItem(inz,  Packages.client.inventory.MapleInventoryType.EQUIP); 
                } else if (invtype == 6) {
                    cm.getPlayer().forceReAddItem(inz,  Packages.client.inventory.MapleInventoryType.CODY); 
                } else {
                    cm.sendOk(" 錯誤發生");
                    cm.dispose(); 
                    return;
                }
                rotation = 0;
                St = 2;
            }
        } else {
            S2 = S2;
            rotation++;
        }
        addItemInfo();
    }
}
 
function addItemInfo() {
    if (rotation == 0) {
        inz = cm.getInventory(invtype).getItem(S2); 
        txt = "#r#e[物品訂單強化屬性]#n\r\n#b#fs15#";
        sel = ["力量", "敏捷", "智力", " luck", "最大體力(HP)", "最大魔力(MP)", "攻击力", "魔力", "物理防禦力", "魔法防禦力", "命中率", "回避值", "速度", "跳躍力", "追加屬性", "力量", "敏捷", "智力", " luck", "最大體力(HP)", "最大魔力(MP)", "攻击力", "魔力", "物理防禦力", "魔法防禦力", "命中率", "回避值", "技能成功次數", "升級次數", "星力成功次數", "剪刀使用次數", "對BOSS傷害", "怪物防禦無視", "總傷害", "全屬性", "裝備等級降低", "潛在能力等級", "第一潛在能力", "第二潛在能力", "第三潛在能力", "第四潛在能力", "第五潛在能力", "第六潛在能力", " arcane等級", " arcane數值", " arcane經驗值"];
        for (y = 0; y < sel.length;  y++) {
            txt += "#L" + y + "#" + sel[y] + "#l";
            if (y == 5 || y == 9 || y == 20 || y == 24 || y == 28 || y == 32 || y == 36 || y == 39) {
                txt += "\r\n";
            }
            if (y == 14) {
                txt += "\r\n\r\n\r\n#r#e#fs15#[物品星力強化屬性]#b#n#fs15#\r\n";
            }
            if (y == 26) {
                txt += "\r\n\r\n\r\n#r#e#fs15#[物品強化屬性]#b#n#fs15#\r\n";
            }
            if (y == 30) {
                txt += "\r\n\r\n\r\n#r#e#fs15#[物品額外屬性]#b#n#fs15#\r\n";
            }
            if (y == 35) {
                txt += "\r\n\r\n\r\n#r#e#fs15#[物品潛在能力]#b#n#fs15#\r\n";
            }
            if (y == 42) {
                txt += "\r\n\r\n\r\n#r#e#fs15#[ arcane能力]#b#n#fs15#\r\n";
            }
        }
        cm.sendSimple(txt); 
        rotation++;
    } else if (rotation == 1) {
        switch (S3) {
            case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13: case 15: case 16: case 17: case 18: case 19: case 20: case 21: case 22: case 23: case 24: case 25: case 26: case 44:
                max = 32767;
                break;
            case 27: case 28:
                max = 127;
                break;
            case 29: case 30: case 45:
                max = 255;
                break;
            case 31: case 32: case 33: case 34: case 35:
                max = 100;
                break;
            case 36:
                max = 20;
                break;
            case 26:
                max = 100000000000;
                break;
            default:
                max = 99999;
                break;
        }
        if (S3 != 36) {
            if (max != 99999) {
                cm.sendGetNumber(" 請輸入要修改的#b" + sel[S3] + "#k數值。\r\n#r(#e" + Comma(max) + "#n 不能超過此值。)", 0, 0, max);
            } else {
                cm.sendGetNumber(" 請輸入要修改的#b" + sel[S3] + "#k數值。\r\n#r(如果不知道潛在能力代碼，可以向我查詢。)", 0, 0, max);
            }
        } else {
            cm.sendSimple(" 請選擇要修改的#b" + sel[S3] + "#k等級。\r\n#fs15##r"
                + "#L0#無潛在能力等級#l\r\n\r\n\r\n"
                + "#fs15##e[未確認潛在能力等級]#b#n#fs15#\r\n"
                + "#L1#稀有#l#L2#傳奇#l#L3#獨特#l#L4#傳說#l\r\n\r\n\r\n"
                + "#fs15##e#r[已確認潛在能力等級]#b#n#fs15#\r\n"
                + "#L17#稀有#l#L18#傳奇#l#L19#獨特#l#L20#傳說#l\r\n");
        }
        rotation++;
    }
}
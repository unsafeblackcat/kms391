function start() {
    St = -1;
    rotation = 0;
    action(1, 0, 0);
}
 
function send(i, str) {
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
            cm.dispose(); 
            return;
        }
        cm.sendSimple("#fs15# 歡迎來到Fancy世界 我是現金裝備屬性修改NPC！\r\n#L1##b變更裝備選項#l\r\n#L2##b潛能代碼查詢#l");
    } else if (St == 1) {
        S1 = S;
        switch (S1) {
            case 1:
                inz = cm.getInventory(6) 
                txt = "#r請選擇要變更選項的裝備#k。\r\n#b";
                for (w = 0; w < inz.getSlotLimit();  w++) {
                    if (!inz.getItem(w))  {
                        continue;
                    }
                    txt += "#L" + w + "##i" + inz.getItem(w).getItemId()  + ":# #t" + inz.getItem(w).getItemId()  + "##l\r\n";
                }
                cm.sendSimple("#fs15#"  + txt);
                break;
 
            case 2:
                send(00, "　　");
                send(00, "　　");
                send(00, "　　");
                send(10, "　< 主要屬性%相關潛能代碼 >");
                send(20, "　力量　: +3%(10041)　力量　: +6%(20041)　力量　: +9%(30041)　力量　: +12%(40041)");
                send(20, "　敏捷: +3%(10042)　敏捷: +6%(20042)　敏捷: +9%(30042)　敏捷: +12%(40042)");
                send(20, "　智力: +3%(10043)　智力: +6%(20043)　智力: +9%(30043)　智力: +12%(40043)");
                send(20, "　幸運　: +3%(10044)　幸運　: +6%(20044)　幸運　: +9%(30044)　幸運　: +12%(40044)");
                send(20, "　全屬性: +9%(40086)　　     全屬性: +12%(40081)　　　  全屬性: +20%(60002)");
                send(27, "　　");
                send(10, "　< 其他屬性%相關潛能代碼 >");
                send(20, "　最大HP: +3%(10045)　最大HP: +6%(20045)　最大HP: +9% (30045)　最大HP: +12%(40045)");
                send(20, "　最大MP: +3%(10046)　最大MP: +6%(20046)　最大MP: +9% (30046)　最大MP: +12%(40046)");
                send(20, "　迴避值　: +3%(10048)　迴避值　: +6%(20048)　迴避值　: +9% (30048)　迴避值　: +12%(40048)");
                send(27, "　　");
                send(10, "　< 武器相關潛能代碼 >");
                send(27, "　傷害: +6%(20070)　傷害: +9%(30070)　傷害: +12%(40070)");
                send(27, "　攻擊力: +6%(20051)　攻擊力: +9%(30051)　攻擊力: +12%(40051)");
                send(27, "　魔力　: +6%(20052)　魔力　: +9%(30052)　魔力　: +12%(40052)");
                send(27, "　　");
                send(10, "　< 無視怪物防禦率相關潛能代碼 >");
                send(27, "　+15%(10291)　+20%(20291)　+30%(30291)　+35%(40291)　+40%(40292)");
                send(27, "　　");
                send(10, "　< 對BOSS怪物傷害相關潛能代碼 >");
                send(27, "　+20%(30601)　+25%(40601)　+30%(30602)　+35%(40602)　+40%(40603)");
                send(27, "　　");
                send(10, "　< 爆擊相關潛能代碼 >");
                send(27, "　爆擊率: +8%(20055)　爆擊率: +10%(30055)　爆擊率: +12%(40055)");
                send(27, "　爆擊最小傷害: +15%(40056)　　　　　　　  爆擊最大傷害: +15%(40057)");
                send(27, "　　");
                send(10, "　< 飾品·防具相關潛能代碼 >");
                send(05, "　楓幣獲得量增加: +20%(40650)　道具掉落率增加: +20%(40656)");
                send(05, "　被擊後無敵時間: 1秒(20366)　　被擊後無敵時間: 2秒(30366)　　被擊後無敵時間: 3秒(40366)");
                send(27, "　　");
                send(10, "　< 實用技能相關潛能代碼 >");
                send(05, "　(稀有)　　神速(31001)　　瞬間移動(31002)　　銳利之眼(31003)　　超級體力(31004)");
                send(05, "　(傳說)　　戰鬥命令(41005)　　　進階祝福(41006)　　　風之捷(41007)");
                cm.dispose(); 
                break;
 
            default:
                cm.dispose(); 
                break;
        }
    } else if (St > 1) {
        if (rotation != -1) {
            switch (St) {
                case 2:
                    S2 = S;
                    break;
                case 3:
                    S3 = S;
                    break;
                case 4:
                    S4 = S;
                    break;
            }
            if (St == 4 && rotation == 2) {
                try {
                    switch (S3) {
                        case 0:
                            inz.setStr(S4); 
                            break;
                        case 1:
                            inz.setDex(S4); 
                            break;
                        case 2:
                            inz.setInt(S4); 
                            break;
                        case 3:
                            inz.setLuk(S4); 
                            break;
                        case 4:
                            inz.setHp(S4); 
                            break;
                        case 5:
                            inz.setMp(S4); 
                            break;
                        case 6:
                            inz.setWatk(S4); 
                            break;
                        case 7:
                            inz.setMatk(S4); 
                            break;
                        case 8:
                            inz.setWdef(S4); 
                            break;
                        case 9:
                            inz.setMdef(S4); 
                            break;
                        case 10:
                            inz.setAcc(S4); 
                            break;
                        case 11:
                            inz.setAvoid(S4); 
                            break;
                        case 12:
                            inz.setSpeed(S4); 
                            break;
                        case 13:
                            inz.setJump(S4); 
                            break;
                        case 14:
                            inz.setLevel(S4); 
                            break;
                        case 15:
                            inz.setUpgradeSlots(S4); 
                            break;
                        case 16:
                            break;
                        case 17:
                            inz.setEnhance(S4); 
                            break;
                        case 18:
                            inz.setBossDamage(S4); 
                            break;
                        case 19:
                            inz.setIgnorePDR(S4); 
                            break;
                        case 20:
                            inz.setTotalDamage(S4); 
                            break;
                        case 21:
                            inz.setAllStat(S4); 
                            break;
                        case 22:
                            inz.setDownLevel(S4); 
                            break;
                        case 23:
                            inz.setState(S4); 
                            break;
                        case 24:
                            inz.setPotential1(S4); 
                            break;
                        case 25:
                            inz.setPotential2(S4); 
                            break;
                        case 26:
                            inz.setPotential3(S4); 
                            break;
                        case 27:
                            inz.setPotential4(S4); 
                            break;
                        case 28:
                            inz.setPotential5(S4); 
                            break;
                        case 29:
                            inz.setPotential6(S4); 
                            break;
                        case 30:
                            inz.setAnvil(S4); 
                            break;
                    }
                    cm.getPlayer().forceReAddItem(inz,  Packages.client.inventory.MapleInventoryType.CODY); 
                    rotation = 0;
                } catch (e) {
                    cm.sendOk(e); 
                    cm.dispose(); 
                }
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
        inz = cm.getInventory(6).getItem(S2); 
        txt = "#r#e[裝備基礎選項]#n\r\n#b#fs15#";
        sel = ["力量", "敏捷", "智力", "幸運", "最大HP", "最大MP", "攻擊力", "魔力", "物理防禦", "魔法防禦", "命中值", "迴避值", "移動速度", "跳躍力", "卷軸成功次數", "可升級次數", "星力強化次數", "驚奇裝備強化卷軸成功次數", "對BOSS傷害", "無視怪物防禦", "總傷害", "全屬性", "裝備等級下降", "潛能等級", "第1潛能", "第2潛能", "第3潛能", "第4潛能", "第5潛能", "第6潛能", "鐵砧"];
        for (y = 0; y < sel.length;  y++) {
            txt += "#L" + y + "#" + sel[y] + "#l";
            if (y == 5 || y == 9 || y == 15 || y == 19 || y == 23 || y == 26) {
                txt += "\r\n";
            }
            if (y == 13) {
                txt += "\r\n\r\n\r\n#r#e#fs15#[裝備強化選項]#b#n#fs15#\r\n";
            }
            if (y == 17) {
                txt += "\r\n\r\n\r\n#r#e#fs15#[裝備附加選項]#b#n#fs15#\r\n";
            }
            if (y == 22) {
                txt += "\r\n\r\n\r\n#r#e#fs15#[裝備潛能]#b#n#fs15#\r\n";
            }
        }
        cm.sendSimple(txt); 
        rotation++;
    } else if (rotation == 1) {
        switch (S3) {
            case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13:
                max = 32767;
                break;
            case 14: case 15:
                max = 125;
                break;
            case 16: case 17:
                max = 255;
                break;
            case 18: case 19: case 20: case 21: case 22:
                max = 100;
            default:
                max = 99999;
                break;
        }
 
        if (S3 != 23) {
            if (max != 99999) {
                cm.sendGetNumber(" 請輸入要變更的 #b" + sel[S3] + "#k 數值。", 0, 0, max);
            } else {
                cm.sendGetNumber(" 請輸入要變更的 #b" + sel[S3] + "#k 數值。", 0, 0, max);
            }
        } else {
            cm.sendSimple(" 請選擇要設定的 #b" + sel[S3] + "#k 等級。\r\n#fs15##r"
                + "#L0#無潛能#l\r\n\r\n\r\n"
                + "#fs15##e[未鑑定潛能等級]#b#n#fs15#\r\n"
                + "#L1#稀有#l#L2#史詩#l#L3#傳說#l#L4#神器#l\r\n\r\n\r\n"
                + "#fs15##e#r[已鑑定潛能等級]#b#n#fs15#\r\n"
                + "#L17#稀有#l#L18#史詩#l#L19#傳說#l#L20#神器#l\r\n");
        }
        rotation++;
    }
}
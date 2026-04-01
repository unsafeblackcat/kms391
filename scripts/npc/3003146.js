var status = -1;
 
var 一般技能 =  [
    // 鍵值, 技能代碼, 技能名稱, 價格, 個別指令使用與否 (true時個別使用)
    ["ww_cross", 1311015, "十字審判", 100000, false],
    ["ww_holy", 2311003 , "神聖之光", 100000, false],
    ["ww_sharp", 3121002, "銳利之眼", 100000, false],
    ["ww_winb", 5121009, "風之疾走", 100000, false],
    ["ww_bullseye", 3221054, "百步穿楊", 100000, false],
    ["ww_ccut", 4341002, "終極斬擊", 500000, false],
]
 
var 贊助技能 = [
    // 鍵值, 技能代碼, 技能名稱, 價格, 數量, 個別指令使用與否 (true時個別使用)
    ["ww_secro", 1221054, "聖靈守護", 4251402, 6, true],
    ["ww_sup", 5321054, "強力射擊", 4251402, 2, true],
    ["ww_fireaura", 2121054, "火焰光環", 4430000, 1, true],
    ["ww_tripling", 13100022, "三重旋風", 4251401, 1, true],
    ["ww_serpent", 400051015, "蛇形螺旋", 4251401, 1, true],
    //["ww_tripling3", 13110022, "三重旋風追加", 4430000, 1, false],
    //["ww_quiver", 3100010, "箭矢筒", 4430000, 1, false],
    ["ww_autodrop", 80003068, "自動拾取", 4251401, 3, false],
]
 
function start() {
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
        talk = "#fn微軟正黑體##fs15# 請選擇要購買的類別。\r\n#b";
        talk += "#L0##fUI/GuildMark.img/Mark/Pattern/00004001/11#  一般贊助技能#l\r\n";
        talk += "#L1##fUI/GuildMark.img/Mark/Pattern/00004001/11#  VIP贊助技能#l\r\n";
        cm.sendSimple(talk); 
    } else if (status == 1) {
        talk = "#fn微軟正黑體##fs15#想要購買贊助技能嗎？\r\n\r\n";
        talk += "#fc0xFF000000#目前 #fc0xFFFF3366##h ##fc0xFF000000# 的贊助點數： #fc0xFFFF3366#"+cm.getPlayer().getDonationPoint()+"P#k\r\n"; 
        if (selection == 0) { // 贊助技能 
            for (i=0; i < 一般技能.length; i++) {
                talk += "#L"+i+"# #s"+一般技能[i][1]+"# "+一般技能[i][2]+" #r("+一般技能[i][3]+"P)#k#l\r\n";
            }
            cm.sendSimple(talk); 
        } else if (selection == 1) { // 特殊技能 
            for (i=0; i < 贊助技能.length; i++) {
                talk += "#L"+(i + 100)+"# #s"+贊助技能[i][1]+"# "+贊助技能[i][2]+" #r(#i"+贊助技能[i][3]+"# "+贊助技能[i][4]+"個)#k#l\r\n";
            }
            cm.sendSimple(talk); 
        }
    } else if (status == 2) {
        if (selection < 100) {
            if (cm.getPlayer().getKeyValue( 一般技能[selection][1], ""+一般技能[selection][0]+"") != 1) {
                if (cm.getPlayer().getDonationPoint()  >= 一般技能[selection][3]) {
                    cm.getPlayer().gainDonationPoint(- 一般技能[selection][3]);
                    cm.getPlayer().setKeyValue( 一般技能[selection][1], ""+一般技能[selection][0]+"", "1");
                    cm.getPlayer().setKeyValue(1234,  "skill_on", "1");
                    cm.getPlayer().setKeyValue(1234,  "isDont", "1");
                    if (一般技能[(selection)][4]) {
                        cm.getPlayer().setKeyValue(1234,  一般技能[(selection)][1] + "_on", "1");
                    }
                    cm.sendOk("#fn 微軟正黑體##fs15#已購買您選擇的 #s"+一般技能[selection][1]+"# #b"+一般技能[selection][2]+"#k。");
                } else {
                    cm.sendOk("#fn 微軟正黑體##fs15#贊助點數不足，無法購買。");
                }
            } else {
                cm.sendOk("#fn 微軟正黑體##fs15#您已購買過此贊助技能。");
                if (一般技能[(selection)][4]) {
                    cm.getPlayer().setKeyValue(1234,  一般技能[(selection)][1] + "_on", "1");
                }
                cm.getPlayer().setKeyValue(1234,  "isDont", "1");
                cm.getPlayer().setKeyValue(1234,  "skill_on", "1");
            }
        } else {
            if (cm.getPlayer().getKeyValue( 贊助技能[(selection - 100)][1], ""+贊助技能[(selection - 100)][0]+"") != 1) {
                if (cm.haveItem( 贊助技能[(selection - 100)][3], 贊助技能[(selection - 100)][4])) {
                    cm.gainItem( 贊助技能[(selection - 100)][3], -贊助技能[(selection - 100)][4]);
                    cm.getPlayer().setKeyValue(1234,  "skill_on", "1");
                    cm.getPlayer().setKeyValue(1234,  "isDont", "1");
                    if (贊助技能[(selection - 100)][5]) {
                        cm.getPlayer().setKeyValue(1234,  贊助技能[(selection - 100)][1] + "_on", "1");
                    }
                    cm.getPlayer().setKeyValue( 贊助技能[(selection - 100)][1], ""+贊助技能[(selection - 100)][0]+"", "1");
                    cm.sendOk("#fn 微軟正黑體##fs15#已購買您選擇的 #s"+贊助技能[(selection - 100)][1]+"# #b"+贊助技能[(selection - 100)][2]+"#k。");
                } else {
                    cm.sendOk("#fn 微軟正黑體##fs15##r#i"+贊助技能[(selection - 100)][3]+"# #z"+贊助技能[(selection - 100)][3]+"# "+贊助技能[(selection - 100)][4]+"個#k 不足。");
                }
            } else {
                cm.sendOk("#fn 微軟正黑體##fs15#您已購買過此贊助技能。");
                if (贊助技能[(selection - 100)][5]) {
                    cm.getPlayer().setKeyValue(1234,  贊助技能[(selection - 100)][1] + "_on", "1");
                }
                cm.getPlayer().setKeyValue(1234,  "skill_on", "1");
                cm.getPlayer().setKeyValue(1234,  "isDont", "1");
            }
        }
        cm.dispose(); 
    } 
}
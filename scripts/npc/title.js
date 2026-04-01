/*
製作 : 又깡 
用途 : 又깡 數據包 312 
*/
 
importPackage(Packages.tools.packet); 
importPackage(Packages.constants.programs); 
importPackage(Packages.database); 
importPackage(java.lang); 
importPackage(java.sql); 
importPackage(java.util); 
importPackage(java.lang); 
importPackage(java.io); 
importPackage(java.awt); 
importPackage(Packages.database); 
importPackage(Packages.constants); 
importPackage(Packages.client.items); 
importPackage(Packages.client.inventory); 
importPackage(Packages.server.items); 
importPackage(Packages.server); 
importPackage(Packages.tools); 
importPackage(Packages.server.Luna); 
importPackage(Packages.packet.creators); 
importPackage(Packages.client.items); 
importPackage(Packages.server.items); 
importPackage(Packages.launch.world); 
importPackage(Packages.main.world); 
importPackage(Packages.database.hikari); 
importPackage(java.lang); 
importPackage(Packages.handling.world) 
importPackage(Packages.constants); 
importPackage(Packages.server); 
importPackage(Packages.tools.packet); 
 
傷害皮膚 = [3700548, 3700540, 3700539, 3700530, 3700529, 3700528, 3700526, 3700525, 3700524, 3700514, 3700513, 3700512, 3700511, 3700510, 3700492, 3700490, 3700489, 3700486, 3700478, 3700468, 3700468, 3700466, 3700465, 3700461, 3700458, 3700454, 3700447, 3700445, 3700444, 3700442, 3700440, 3700435, 3700434, 3700433, 3700432, 3700430, 3700429, 3700425, 3700422, 3700421, 3700420, 3700419, 3700418, 3700417, 3700403, 3700402,3700390, 3700390, 3700389, 3700388, 3700380, 3700378, 3700377, 3700376, 3700356, 3700355, 3700354, 3700353, 3700352, 3700344, 3700339, 3700338, 3700337, 3700336, 3700335, 3700390, 3700322, 3700390, 3700321, 3700308, 3700306, 3700307, 3700000, 3700001, 3700002, 3700003, 3700004, 3700005, 3700006, 3700010, 3700011, 3700012, 3700013, 3700014, 3700015, 3700016, 3700017, 3700018, 3700019, 3700025, 3700026, 3700004, 3700035, 3700040, 3700045, 3700046, 3700062, 3700063, 3700064, 3700065, 3700000, 3700068, 3700091, 3700090, 3700093, 3700096, 3700108, 3700118, 3700119, 3700176, 3700177, 3700178, 3700180, 3700220, 3700270, 3700228, 3700278, 3700279, 3700280, 3700283, 3700285, 3700286, 3700108];
 
硬幣 = 2635911;
數量 = 200;
 
status = -1;
 
function start() {
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose(); 
        return;
    }
    if (mode == 1)
        status++;
    else 
        status--;
    if (status == 0) {
        對話 = "#fs15##fc0xFF000000#準備了各種不同的稱號。#b\r\n";
        對話 += "隨機 - #i2635911##z2635911# 200個\r\n";
        對話 += "#L1##d查看物品清單。#l\r\n";
        對話 += "#L0##d隨機抽取。#l";
        cm.sendSimpleS( 對話,0x86);
    } else if (status == 1) {
        if (selection == 0) {
            cm.sendYesNoS("#b#z"  + 硬幣 + "# #r" + 數量 + " 個#k 用於抽取，確定嗎？",0x86);
        } else if (selection == 1) {
            對話 = "#fs15##b\r\n";
            for (i = 0; i < 傷害皮膚.length; i++) {
                對話 += "#v" + 傷害皮膚[i] + "# #d#z" + 傷害皮膚[i] + "#\r\n";
            }
            cm.sendOkS( 對話,0x86);
            cm.dispose(); 
        }
    } else if (status == 2) {
        抽中物品 = 傷害皮膚[Randomizer.rand(0, 傷害皮膚.length)];
        if (cm.haveItem( 硬幣, 數量)) {
            if (cm.canHold( 抽中物品)) {
                cm.gainItem( 抽中物品, 1);
                cm.gainItem( 硬幣, -數量);
                cm.sendOkS("#fs15# 抽中的物品是：\r\n\r\n#i" + 抽中物品 + "# #d#z" + 抽中物品 + "#",0x86);
                cm.dispose(); 
            } else {
                cm.sendOkS("#fs15#  #r消耗#k欄位需要清空。",0x86);
                cm.dispose(); 
            }
        } else {
            cm.sendOkS("#fs15# 抽取所需的材料不足。",0x86);
            cm.dispose(); 
        }
    }
}
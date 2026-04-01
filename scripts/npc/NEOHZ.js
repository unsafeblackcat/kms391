importPackage(Packages.constants); 
importPackage(Packages.tools.packet); 
importPackage(Packages.tools.packet); 
importPackage(Packages.constants.programs); 
importPackage(Packages.database); 
importPackage(java.lang); 
importPackage(Packages.handling.world) 
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
var status;
var select;
 
// 勳章列表1 
var 勳章1 = new Array(1142661, 1142593, 1143029, 1142006, 1142440, 1142249, 1142013, 1142003, 1143114, 1142152, 1140002, 1141002, 1142000, 1142001, 1142002, 1142003, 1142004, 1142009, 1142010, 1142011, 1142012, 1142013, 1142014, 1142015, 1142016, 1142017, 1142018, 1142019, 1142020, 1142021, 1142022, 1142023, 1142024, 1142025, 1142026, 1142027, 1142028, 1142029, 1142030, 1142031, 1142032, 1142033, 1142034, 1142035, 1142036, 1142037, 1142038, 1142039, 1142040, 1142041, 1142042, 1142043, 1142044, 1142045, 1142046, 1142047, 1142048, 1142049, 1142050, 1142051, 1142052, 1142053, 1142054, 1142055, 1142056, 1142057, 1142058, 1142059, 1142060, 1142061, 1142062, 1142063, 1142064, 1142065, 1142066, 1142067, 1142068, 1142069, 1142070, 1142071, 1142072, 1142073, 1142074, 1142075, 1142076, 1142077, 1142078, 1142079, 1142080, 1142081, 1142082, 1142083, 1142084, 1142085, 1142086, 1142087, 1142088, 1142089, 1142090, 1142091, 1142092, 1142093, 1142094, 1142095, 1142096, 1142097, 1142098, 1142099, 1142100, 1142101, 1142107, 1142108, 1142109, 1142110, 1142111, 1142112, 1142113, 1142114, 1142115 ,1142116, 1142117, 1142118, 1142119, 1142120, 1142122, 1142123, 1142126, 1142134, 1142135, 1142136, 1142137, 1142138, 1142139, 1142141, 1142142, 1142143, 1142149, 1142150, 1142151, 1142166, 1142187, 1142190, 1142191, 1142217, 1142218, 1142295, 1142296, 1142297, 1142298, 1142299, 1142300, 1142301, 1142305, 1142306, 1142307, 1142329, 1142334, 1142335, 1142360, 1142373, 1142406, 1142408, 1142441, 1142442, 1142443, 1142457, 1142511, 1142512, 1142543, 1142569, 1142573, 1142627, 1142656, 1143174, 1143033, 1143008);
 
// 勳章列表2
var AA = new Array(1140002, 1141002, 1142000, 1142001, 1142002, 1142003, 1142004, 1142009, 1142010, 1142011, 1142012, 1142013, 1142014, 1142015, 1142016, 1142017, 1142018, 1142019, 1142020, 1142021, 1142022, 1142023, 1142024, 1142025, 1142026, 1142027, 1142028, 1142029, 1142030, 1142031, 1142032, 1142033, 1142034, 1142035, 1142036, 1142037, 1142038, 1142039, 1142040, 1142041, 1142042, 1142043, 1142044, 1142045, 1142046, 1142047, 1142048, 1142049, 1142050, 1142051, 1142052, 1142053, 1142054, 1142055, 1142056, 1142057, 1142058, 1142059, 1142060, 1142061, 1142062, 1142063, 1142064, 1142065, 1142066, 1142067, 1142068, 1142069, 1142070, 1142071, 1142072, 1142073, 1142074, 1142075, 1142076, 1142077, 1142078, 1142079, 1142080, 1142082, 1142083, 1142084, 1142085, 1142086, 1142087, 1142088, 1142089, 1142090, 1142091, 1142092, 1142093, 1142094, 1142095, 1142096, 1142097, 1142098, 1142099, 1142100, 1142101, 1142107, 1142108, 1142109, 1142110, 1142111, 1142112, 1142113, 1142114, 1142115 ,1142116, 1142117, 1142118, 1142119, 1142120, 1142122, 1142123, 1142126, 1142134, 1142135, 1142136, 1142137, 1142138, 1142139, 1142141, 1142142, 1142143, 1142149, 1142150, 1142151, 1142166, 1142187, 1142190, 1142191, 1142217, 1142218, 1142295, 1142296, 1142297, 1142298, 1142299, 1142300, 1142301, 1142305, 1142306, 1142307, 1142329, 1142334, 1142335, 1142360, 1142373, 1142406, 1142408, 1142441, 1142442, 1142443, 1142457, 1142511, 1142512, 1142543, 1142569, 1142573, 1142627, 1142656, 1143174, 1143033);
 
var 硬幣 = 2632800;  // 使用的硬幣ID
var 需求數量 = 70;   // 需要的硬幣數量 
var item = 0;      // 選擇的物品
function start() {
    status = -1;
    action(1, 1, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose(); 
    } else {
        if (mode == 0) {
            cm.dispose(); 
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            var text = ""
            text += "#fs15##fc0xFF000000#這裡準備了各種勳章。#b\r\n"
            text += "隨機抽取 - #i2632800##z2632800# 70個\r\n"
            text += "#L1##d查看物品清單#l\r\n"
            text += "#L0##d我要隨機抽取#l"
            cm.sendSimple(text) 
        } else if (status == 1) {
            if (selection == 1) {
                var amed = "#fs15#\r\n#b";
                for (var i = 0; i < 勳章1.length;  i++) {
                    amed += "#i" + 勳章1[i] + "# #z" + 勳章1[i] + "#\r\n";
                }
                cm.sendOk(amed); 
                cm.dispose(); 
            } else {
                item = selection;
                if (!cm.haveItem( 硬幣, 需求數量)) {
                    cm.sendNext("#fs15# 抽取所需的材料不足。");
                    cm.dispose(); 
                } else {
                    cm.sendYesNo("#b#z"+ 硬幣+"# #r"+需求數量+" 個#k 要使用來抽取嗎？");
                }
            }
        } else if (status == 2) {
            獲得訊息 = "#fs15##fc0xFF000000#我抽到的物品是！ #b";
            switch (item) {
                case 0:
                    AA = AA[Math.floor(Math.random() * AA.length)]; 
                    if (cm.canHold(AA))  {
                        ItemInfo = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(AA); 
                        ItemInfo.setUpgradeSlots(0); 
                        ItemInfo.setStr(1); 
                        ItemInfo.setDex(1); 
                        ItemInfo.setInt(1); 
                        ItemInfo.setLuk(1); 
                        ItemInfo.setWatk(1); 
                        ItemInfo.setMatk(1); 
                        Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(),ItemInfo,true); 
                        cm.gainItem( 硬幣, -需求數量);
                        cm.sendOk(""  + 獲得訊息 + "#i" + AA + "# #z" + AA + "#")
                    } else {
                        cm.sendOk(""  + 對話 + "")
                    }
                    cm.dispose(); 
                    break;
            }
        }
    }
}
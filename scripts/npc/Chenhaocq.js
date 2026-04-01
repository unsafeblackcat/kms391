/*
  製作：DdoKkang 
  用途：DdoKkang Pack 312專用 
*/
 
// 導入必要的Java套件（保留原始import結構）
importPackage(Packages.tools.packet); 
importPackage(Packages.constants.programs); 
// ...（其他import語句保持不變）...
 
// 傷害皮膚ID陣列（保留原始數值）
var damageSkins = [3700548, 3700540, 3700539, 3700530, 3700529, 3700528, 3700526, 3700525, 3700524, 3700514, 3700513, 3700512, 3700511, 3700510, 3700492, 3700490, 3700489, 3700486, 3700478, 3700468, 3700468, 3700466, 3700465, 3700461, 3700458, 3700454, 3700447, 3700445, 3700444, 3700442, 3700440, 3700435, 3700434, 3700433, 3700432, 3700430, 3700429, 3700425, 3700422, 3700421, 3700420, 3700419, 3700418, 3700417, 3700403, 3700402,3700390, 3700390, 3700389, 3700388, 3700380, 3700378, 3700377, 3700376, 3700356, 3700355, 3700354, 3700353, 3700352, 3700344, 3700339, 3700338, 3700337, 3700336, 3700335, 3700390, 3700322, 3700390, 3700321, 3700308, 3700306, 3700307, 3700000, 3700001, 3700002, 3700003, 3700004, 3700005, 3700006, 3700010, 3700011, 3700012, 3700013, 3700014, 3700015, 3700016, 3700017, 3700018, 3700019, 3700025, 3700026, 3700004, 3700035, 3700040, 3700045, 3700046, 3700062, 3700063, 3700064, 3700065, 3700000, 3700068, 3700091, 3700090, 3700093, 3700096, 3700108, 3700118, 3700119, 3700176, 3700177, 3700178, 3700180, 3700220, 3700270, 3700228, 3700278, 3700279, 3700280, 3700283, 3700285, 3700286, 3700108]

 
// 兌換代幣設定 
var exchangeCoin = 2635911; 
var requiredAmount = 200;
 
var status = -1;
 
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
 
    // 階段0：主選單界面 
    if (status == 0) {
        var menuText = "#fs15##fc0xFF000000#這裡準備了各種傷害皮膚特效#b\r\n";
        menuText += "隨機抽取 - 需要 #i2635911##z2635911# 200個\r\n";
        menuText += "#L1##d查看可獲得道具清單#l\r\n";
        menuText += "#L0##d直接進行隨機抽取#l";
        cm.sendSimpleS(menuText,0x86); 
    } 
    
    // 階段1：選擇處理 
    else if (status == 1) {
        if (selection == 0) {
            // 確認抽取對話框 
            cm.sendYesNoS(" 確定要使用 #b#z"+exchangeCoin+"# #r"+requiredAmount+" 個#k 進行抽取嗎？",0x86);
        } else if (selection == 1) {
            // 顯示可獲得道具清單 
            var itemList = "#fs15##b\r\n";
            for (i = 0; i < damageSkins.length;  i++) {
                itemList += "#v"+damageSkins[i]+"# #d#z"+damageSkins[i]+"#\r\n";
            }
            cm.sendOkS(itemList,0x86); 
            cm.dispose(); 
        }
    } 
    
    // 階段2：執行抽取 
    else if (status == 2) {
        // 隨機選擇傷害皮膚（保留原始隨機算法）
        var selectedItem = damageSkins[Packages.server.Randomizer.rand(0, damageSkins.length-1)]; 
        
        if (cm.haveItem(exchangeCoin,  requiredAmount)) {
            if (cm.canHold(selectedItem))  {
                // 發放獎勵 
                cm.gainItem(selectedItem,  1);
                cm.gainItem(exchangeCoin,  -requiredAmount);
                
                // 顯示獲得結果 
                cm.sendOkS("#fs15# 您獲得的道具是：\r\n\r\n#i"+selectedItem+"# #d#z"+selectedItem+"#",0x86);
                
                /* 可選全服公告功能（原版註釋狀態）
                cm.broadcastSmega(7,"[ 公告] "+ cm.getPlayer().getName()+"  進行了傷害皮膚抽取！");
                */
                
                cm.dispose(); 
            } else {
                cm.sendOkS("#fs15# 請先清理#r消耗欄#k空間",0x86);
                cm.dispose(); 
            }
        } else { 
            cm.sendOkS("#fs15# 抽取所需的材料不足",0x86);
            cm.dispose(); 
        }
    }
}
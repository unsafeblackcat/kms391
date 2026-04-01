var status = -1;
var num = 0;
var grade = -1;
 
// 裝備等級名稱與顏色代碼
var name = [
    "#fc0xFF5CD1E5#SS級",
    "#bS級", 
    "#fc0xFFC4B73B#A級",
    "#fc0xFF747474#B級",
    "#fc0xFF993800#C級"
];
 
// 各等級升級成功率
var prob = [10, 5, 10, 20]; 
 
// 各等級所需材料數量 [升級需求, 兌換需求]
var req = [
    [-1, 200], // SS級
    [100, 130], // S級 
    [50, 80],   // A級 
    [30, 50],   // B級 
    [10, 30]    // C級 
];
 
// 各等級可獲得物品列表 [物品ID,數量,權重]
var itemlist = [
    // SS級物品
    [
        [5062009,100,5], [5062010,70,5], [5062500,50,5], 
        [5068300,1,3], [2438685,1,2], [2630281,1,3],
        [2435719,1,3], [2438684,1,3], [1703983,1,3],
        [1703984,1,3], [1703987,1,3], [1703988,1,3],
        [1703989,1,3], [1703991,1,3]
    ],
    // S級物品 
    [
        [5062009,100,5], [5062010,70,5], [5062500,50,5],
        [5068300,1,3], [2438685,1,2], [2630281,1,3],
        [2435719,1,3], [2438684,1,3], [1703978,1,3],
        [1703980,1,3], [1703981,1,3], [1703982,1,3]
    ],
    // A級物品 
    [
        [5062009,100,5], [5062010,70,5], [5062500,50,5],
        [5068300,1,3], [2438685,1,2], [2630281,1,3],
        [2435719,1,3], [2438684,1,3]
    ],
    // B級物品
    [
        [5062009,100,5], [5062010,70,5], [5062500,50,5],
        [5068300,1,3], [2438685,1,2], [2630281,1,3],
        [2435719,1,3], [2438684,1,3], [1113282,1,2]
    ],
    // C級物品
    [
        [5062009,50,5], [5062010,30,5], [5062500,20,5],
        [5068300,1,3], [2438685,1,2], [2630281,1,3],
        [2435719,5,3]
    ]
];

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        if (status != 0 || selection >= 3) {
            status++;
        } else {
            if (selection == 0) {
                num--; // 上一頁
            } else if (selection == 1) {
                num++; // 下一頁 
            } else {
                // 升級嘗試
                if (cm.itemQuantity(2432423)  >= req[grade][0]) {
                    var rd = Math.floor(Math.random()  * 100);
                    if (rd < prob[grade-1]) {
                        cm.getPlayer().setKeyValue(20190616,  "upgradechip", 
                            cm.getPlayer().getKeyValue(20190616,  "upgradechip") + 1);
                        cm.getPlayer().dropMessage(6,  "裝備升級成功！");
                    } else {
                        cm.getPlayer().dropMessage(5,  "裝備升級失敗！");
                    }
                    cm.gainItem(2432423,  -req[grade][0]);
                } else {
                    cm.sendOkS(" 升級所需的 #i2432423# #b#z2432423##k#n 數量不足。",0x86);
                    cm.dispose(); 
                    return;
                }
            }
        }
    } else {
        cm.dispose(); 
        return;
    }
 
    if (status == 0) {
        grade = 3 - cm.getPlayer().getKeyValue(20190616,  "upgradechip");
        var talk = "#e<升級裝備>#n";
        talk += "當前等級: #e"+name[grade]+"#k#n";
        talk += "#e[當前等級可獲得物品]#n\r\n";
        
        // 分頁控制 
        if (num != 0) {
            talk += "#L0# #e< 上一頁 #n#l ";
        } else {
            talk += "　　　";
        }
        
        // 顯示當前頁物品
        for (var i=0; i<5 && (i + num) < itemlist[grade].length; i++) {
            talk += "#i"+itemlist[grade][i + num]+"# ";
        }
        
        if (num < itemlist[grade].length - 5) {
            talk += "#L1# #e> 下一頁 #n#l";
        }
        
        talk += "\r\n\r\n";
        
        // 操作選項 
        if (req[grade][0] >= 0) {
            talk += "#L2##b 嘗試升級 (消耗 #i2432423# x "+req[grade][0]+")#k#l\r\n";   
        }
        talk += "#L3##b 當前等級兌換物品 (消耗 #i2432423# x "+req[grade][1]+")#k#l\r\n";
        talk += "#L4##b 查看升級裝備說明#k#l\r\n";
        talk += "#L5##b 查看所有等級可獲得物品#k#l";
        
        cm.sendSimpleS(talk,0x86); 
    } 
    else if (status == 1) {
        if (selection == 3) {
            // 物品兌換邏輯
            if (cm.itemQuantity(2432423)  >= req[grade][1]) {
                var allprob = 0;
                var probcheck = 0;
                
                // 計算總權重 
                for (var i=0; i<itemlist[grade].length; i++) {
                    allprob += itemlist[grade][i][2];
                }
                
                // 隨機選擇物品 
                var allprobrd = Math.floor(Math.random()  * allprob);
                var rd = 0;
                for (var i=0; i<itemlist[grade].length; i++) {
                    if (probcheck >= allprobrd) {
                        rd = i;
                        break;
                    }
                    probcheck += itemlist[grade][i][2];
                }
                
                // 降級處理 
                var rds = Packages.server.Randomizer.rand(2,3); 
                var graderd = cm.getPlayer().getKeyValue(20190616,  "upgradechip") - rds;
                if (graderd < -1) graderd = -1;
                
                // 發放獎勵
                cm.getPlayer().setKeyValue(20190616,  "upgradechip", graderd);
                cm.gainItem(itemlist[grade][rd][0],  itemlist[grade][rd][1]);
                cm.gainItem(2432423,  -req[grade][1]);
                
                cm.sendOkS(" 恭喜您！從"+name[grade]+"等級裝備中獲得以下物品:\r\n"
                    + "#r(裝備升級進度已重置為C級)#k\r\n\r\n"
                    + "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n" 
                    + "#i"+itemlist[grade][rd][0]+"# #z"+itemlist[grade][rd][0]+"# "+itemlist[grade][rd][1]+"個",0x86);
            } else {
                cm.sendOkS(" 兌換物品所需的 #i2432423# #b#z2432423##k#n 數量不足。",0x86);
            }
            cm.dispose(); 
        } 
        else if (selection == 4) {
            // 系統說明
            var talk = "以下是升級裝備系統的說明:\r\n\r\n"
            talk += "#i3994096# 使用 #z2432423# 可以 #b升級裝備或在當前等級兌換物品#k。\r\n"
            talk += "#i3994097# #b裝備等級越高#k，升級或兌換所需的 #b#z2432423# 數量越多#k。\r\n"
            talk += "#i3994098# #b裝備等級越高#k，升級成功的 #b機率越低#k。\r\n"
            talk += "#i3994099# 在當前等級兌換物品可獲得該等級隨機一件物品，同時裝備 #b重置為C級#k。\r\n"
            talk += "#i3994100# S級和SS級出現的紅色武器為現金道具。如果反應良好，我們會考慮製作其他職業的武器。"
            
            cm.sendOkS(talk,0x86); 
            cm.dispose(); 
        } 
        else {
            // 查看所有物品 
            var debug = [];
            for (var i=0; i<name.length;  i++) {
                var a = 0;
                for (var j=0; j<itemlist[i].length; j++) {
                    a += itemlist[i][j][2];
                }
                debug.push(a); 
            }
            
            var talk = "升級裝備系統中可能獲得的物品如下:\r\n";
            if (cm.getPlayer().getGMLevel()  >= 6) {
                talk += "#r#e(機率資訊僅對GM顯示)#k#n\r\n";
            }
            talk += "\r\n";
            
            for (var i=0; i<name.length;  i++) {
                talk += "#e<"+name[i]+"裝備>#n\r\n";
                for (var j=0; j<itemlist[i].length; j++) {
                    talk += "#i"+itemlist[i][j][0]+"# ";
                    if (cm.getPlayer().getGMLevel()  >= 6) {
                        talk += "[機率: "+((itemlist[i][j][2]/debug[i])*100).toFixed(2)+"%]　　";
                        if (j%2 == 1) talk += "\r\n";
                    }
                }
                talk += "\r\n\r\n";
            }
            
            cm.sendOkS(talk,0x86); 
            cm.dispose(); 
        }
    }
}


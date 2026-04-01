var status;
var select;
 
黑色字體 = "#fc0xFF191919#"
var 特殊椅子列表 = new Array(301100, 3011001, 3011002, 3011003, 3011004, 3011005, 3011006, 3011007, 3011008, 3011009, 3011010, 3011011, 3011012, 3011013, 3011014, 3011015, 3011016, 3011017, 3011018, 3011019, 3011020, 3011021, 3011022, 3011023, 3011024, 3011025, 3011026, 3011027, 3011028);
var 兌換代幣 = 4310249 
var 需求數量 = 50;
var item = 0;
 
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
            var 楓葉數量 = cm.itemQuantity(4310249); 
            var text = ""
            text = "#fs15#"+黑色字體+"我是 #fc0xFF000087#[黑色]#k "+黑色字體+"內容部門負責 #b特殊椅子抽獎#k"+黑色字體+"的羅賓。"
            text += "\r\n每次抽獎需要消耗 #r#z4310249# 35個#k"+黑色字體+"\r\n\r\n";
            text += "當前持有 #d#z4310249##k 數量： #e#r"+楓葉數量+"#k個#n\r\n"
            text += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            text += "#L0##fc0xFF3B36CF#進行特殊椅子抽獎\r\n"
            text += "#L12##fc0xFF3B36CF#查看特殊椅子列表\r\n"
            cm.sendSimpleS(text,0x86) 
        } else if (status == 1) {
            if (selection == 12) {
                var 椅子清單 = "\r\n";
                for (var i = 0; i < 特殊椅子列表.length; i++) {
                    椅子清單 += "#i" + 特殊椅子列表[i] + "# #fc0xFF6B66FF##z" + 特殊椅子列表[i] + "#\r\n";
                }
                cm.sendOkS( 椅子清單,0x86);
                cm.dispose(); 
            } else {
                item = selection;
                if (!cm.haveItem(4310249,  35)) {
                    cm.sendOks("#fs15##i4310249#  #b#z4310249##k 道具數量不足。",0x86);
                    cm.dispose(); 
                } else {
                    cm.sendYesNos("#fs15# 確定要進行 #b特殊椅子#k 抽獎嗎？\r\n抽獎將消耗 #r#z4310249# 35個#k。",0x86);
                }
            }
        } else if (status == 2) {
            switch (item) {
                case 0:
                    隨機椅子 = 特殊椅子列表[Math.floor(Math.random() * 特殊椅子列表.length)];
                    if (cm.canHold( 隨機椅子)) {
                        cm.gainItem( 隨機椅子, 1);
                        cm.gainItem(4310249,  -35);
                        對話內容 = "#fs15#恭喜 #b#h ##k！抽到想要的特殊椅子了嗎？如果不滿意，要不要再試一次呢？\r\n\r\n"
                        對話內容 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
                        對話內容 += "#i" + 隨機椅子 + "# #b#z" + 隨機椅子 + "##k\r\n\r\n"
                        對話內容 += "#L0##fc0xFF3B36CF#再抽一次\r\n#L1#關閉對話"
                        cm.sendOkS( 對話內容,0x86);
                    } else {
                        cm.sendOkS("#fs15# 背包空間不足，無法領取特殊椅子。",0x86);
                        cm.dispose(); 
                    }
                    break;
            } 
        } else if (status ==3) {
            if (selection == 0) {
                cm.dispose(); 
                cm.openNpc(1540105,  "SpecialChair");
            } else {
                cm.dispose(); 
            }
        }
    }
}
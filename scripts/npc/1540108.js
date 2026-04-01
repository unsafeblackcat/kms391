var status;
var select;

검정 = "#fc0xFF191919#"
var 뎀스A = new Array(2634267, 2438871, 2439265, 2439394, 2439572, 2630380, 2630753, 2630969, 2631091, 2631451, 2631892, 2632123, 2632287, 2632348, 2632815, 2633047, 2633073);
var 코인 = 4310249
var 필요개수 = 10;
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
            var leaf = cm.itemQuantity(4319999);
             var text = "#fs15#"+검정+"你好 #fc0xFF000087#[FANCY]#k "+검정+"我是負責#k"+검정+"傷害單元的塞拉·格雷斯。."
             text += "\r\n每次抽取一次組織傷害皮膚 #r#z4319999# 1000個#k"+검정+"可用.\r\n\r\n";
             text += "현재 보유중인 #d#z4319999##k 갯수 : #e#r"+leaf+"#k개#n\r\n"
             text += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
             text += "#L0##fc0xFF3B36CF#我想抽取組織傷害皮膚.\r\n"
            cm.sendSimpleS(text,0x86)
        } else if (status == 1) {
            if (selection == 12) {
                var amed = "#fs15##fc0xFB1B66FF#傷害皮膚師#k\r\n\r\n";
                for (var i = 0; i < 뎀스A.length; i++) {
                    amed += "#i" + 뎀스A[i] + "# #fc0xFF6B66FF##z" + 뎀스A[i] + "#\r\n";
                }
                cm.sendOkS(amed,0x86);
                cm.dispose();
            } else {
                item = selection;
                if (!cm.haveItem(4319999, 1000)) {
		            cm.sendOkS("#fs15##i4319999# #b#z4319999##k 數量好像不足或沒有。.",0x86);
                    cm.dispose();
                } else {
                    cm.sendYesNoS("#fs15#真的 #b要抽取#k組織傷害皮膚嗎？\r\n進行抽取時將消耗 #r#z4319999# 1000個#k。",0x86);
                }
            }
        } else if (status == 2) {
            switch (item) {
                case 0:
                    뎀스A = 뎀스A[Math.floor(Math.random() * 뎀스A.length)];
                    if (cm.canHold(뎀스A)) {
                        cm.gainItem(뎀스A, 1);
                        cm.gainItem(4319999, -1000);
                        말 = "#fs15#祝賀#b#h ##k你 ! 想要的Unit傷害皮膚出來了嗎？ 如果不喜歡的話，再抽一次怎麼樣？\r\n\r\n"
                        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
                        말 += "#i" + 뎀스A + "# #b#z" + 뎀스A + "##k\r\n\r\n"
                        말 += "#L0##fc0xFF3B36CF#我想再抽一次。\r\n#L1#關閉對話方塊"
                        cm.sendOkS(말,0x86);
                    } else {
                        cm.sendOkS("#fs15#好像沒有足够的空間接受組織傷害皮膚。.",0x86);
                        cm.dispose();
                    }
                    break;
            } 
        } else if (status ==3) {
            if (selection == 0) {
                cm.dispose();
                cm.openNpc(1540108);
            } else {
                cm.dispose();
            }
        }
    }
}
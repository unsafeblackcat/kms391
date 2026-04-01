var status;
var select;

검정 = "#fc0xFF191919#"
var 뎀스A = new Array(1113098, 1113099, 1113100, 1113101, 1113102, 1113103, 1113104, 1113105, 1113106, 1113107, 1113108, 1113109, 1113110, 1113111, 1113112, 1113113, 1113114, 1113115, 1113116, 1113117, 1113118, 1113119, 1113120, 1113121, 1113122, 1113123, 1113124, 1113125, 1113126);
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
             var text = "#fs15#"+검정+"我 #fc0xFF000087#[FANCY]#k "+검정+"抽種子環#k"+검정+"我是負責的塞拉·格雷斯。."
             text += "\r\n每抽一次種子環 #r#z4319999# 2000個#k"+검정+"可以使用.\r\n\r\n";
             text += "目前拥有 #d#z4319999##k 갯수 : #e#r"+leaf+"#k個#n\r\n"
             text += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
             text += "#L0##fc0xFF3B36CF#我想拔種子戒指。.\r\n"
            cm.sendSimpleS(text,0x86)
        } else if (status == 1) {
            if (selection == 12) {
                var amed = "#fs15##fc0xFB1B66FF#傷害皮膚師#k\r\n\r\n";
                for (var i = 0; i < 뎀스A.length; i++) {
                    amed += "#i" + 뎀스A[i] + "# #fc0xFF6B66FF##z" + 뎀스A[i] + "#\r\n";
                }
                cm.sendOk(amed);
                cm.dispose();
            } else {
                item = selection;
                if (!cm.haveItem(4319999, 2000)) {
		            cm.sendOkS("#fs15##i4319999# #b#z4319999##k 項目好像不足或沒有。.",0x86);
                    cm.dispose();
                } else {
                    cm.sendYesNoS("#fs15#真的 #b種子環#k您要選擇?\r\n進行抽籤時 #r#z4319999# 2000個#k將消耗.",0x86);
                }
            }
        } else if (status == 2) {
            switch (item) {
                case 0:
                    뎀스A = 뎀스A[Math.floor(Math.random() * 뎀스A.length)];
                    if (cm.canHold(뎀스A)) {
                        cm.gainItem(뎀스A, 1);
                        cm.gainItem(4319999, -2000);
                        말 = "#fs15#祝賀你。 #b#h ##k先生！ 想要的種子戒指出來了嗎？ 如果不喜歡的話，再選一次怎麼樣？\r\n\r\n"
                        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
                        말 += "#i" + 뎀스A + "# #b#z" + 뎀스A + "##k\r\n\r\n"
                        말 += "#L0##fc0xFF3B36CF#我想再抽一次。.\r\n#L1#關閉對話方塊"
                        cm.sendOkS(말,0x86);
                    } else {
                        cm.sendOkS("#fs15#好像沒有足够的空間來接種子環。.",0x86);
                        cm.dispose();
                    }
                    break;
            } 
        } else if (status ==3) {
            if (selection == 0) {
                cm.dispose();
                cm.openNpc(1540105);
            } else {
                cm.dispose();
            }
        }
    }
}
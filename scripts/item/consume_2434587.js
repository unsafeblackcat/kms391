var status = 0;
var count = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.getPlayer().getKeyValue(20190211, "karuta_4") < 0) {
        cm.getPlayer().setKeyValue(20190211, "karuta_4", 0);
    }
    itemlist = [
        [1212063,[271]],
        [1214016,[631]],
        [1222058,[651]],
        [1232057,[312]], 
        [1242060,[361]],
        [1242061,[361]],
        [1262016,[1421]],
        [1272015,[641]], 
        [1282015,[1521]], 
        [1302275,[11,12,111,511,611]], 
        [1312153,[11,311]], 
        [1322203,[12,311]], 
        [1332225,[42,43]], 
        [1342082,[43]],
        [1362090,[241]], 
        [1372177,[21,22,23,121,221,1621]], 
        [1382208,[21,22,23,121,221,321]], 
        [1402196,[11,12,111,611]], 
        [1412135,[11]], 
        [1422140,[12]], 
        [1432167,[13]], 
        [1442223,[13,211]], 
        [1452205,[31,131]], 
        [1462193,[32,331]],
	[1592018,[33]], 
        [1472214,[41,141]], 
        [1482168,[51,151,1551,251]], 
        [1492179,[52]],
        [1522094,[231]],
        [1532098,[53]], 
        [1582016,[351, 371]],
	[2048905,[1011]],
	[1292016,[1641]],
	[1213016,[1511]]
    ];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if (status == 0 && selection < 100) {
            status++;
        }
        if (status == 0 && selection == 101) {
            cm.dispose();
            return;
        }
        status++;
    }
    if (status == 0) {
        if (!cm.canHold(itemlist[0][0])) {
            cm.sendOk("#fs15#請清空1格以上的裝備視窗後重新使用!");
            cm.dispose();
            return;
        }
        if (cm.itemQuantity(2434587) < 15) {
            cm.sendOk("#fs15#碎片好像不够.");
            cm.dispose();
            return;
        }
        talk = "#fs15##b#e幸運時間:#r" + cm.getPlayer().getKeyValue(20190211, "karuta_4") + "%#k#n\r\n\r\n"
        if (cm.getPlayer().getKeyValue(20190211, "karuta_4") >= 100) {
            talk += "發動#r#e機會時間!#k#n\r\n"
            talk += "#b#e出現更好的附加選項#k#n的概率新增.\r\n\r\n"
        }
        talk += "#fs15#首先推薦當前職業可穿戴的裝備.\r\n請選擇要收到的防具.\r\n"
        getClass = Math.floor(cm.getPlayer().getJob()/10)
        for (i=0; i<itemlist.length; i++) {
            for (j=0; j<itemlist[i][1].length; j++) {
                if (getClass == itemlist[i][1][j]) {
                    count++;
                    talk+= "#L"+i+"# #i"+itemlist[i][0]+"# #b#z"+itemlist[i][0]+"##k#l\r\n"
                }
            }
        }
        if (count == 0) {
            cm.getPlayer().dropMessage(5, "初學者不能使用. 轉職後請再試一次.");
            cm.dispose();
            return;
        }
        talk += "\r\n#L100##b査看可用全部物品.#k#l\r\n"
        talk += "#L101##b取消使用#k#l"
        cm.sendSimple(talk);
    } else if (status == 1) {
        talk = "#fs15#選擇要接收的防具.\r\n"
        for (i = 0; i < itemlist.length; i++) {
            talk += "#L" + i + "# #i" + itemlist[i][0] + "# #b#z" + itemlist[i][0] + "##k#l\r\n";
        }
        cm.sendSimple(talk);
    } else if (status == 2) {
        selected = selection;
        talk = "#fs15#確定想要這個裝備?\r\n"
        talk += "#i" + itemlist[selected][0] + "# #z" + itemlist[selected][0] + "#\r\n\r\n"
        talk += "#r#e※注意※#k#n\r\n"
        talk += "一旦將碎片兌換成物品 將抹除#r5#k個碎片, 無法再次兌換成其他物品.\r\n\r\n"
        talk += "#L0##b是!沒錯.\r\n"
        talk += "#L1##b重新選擇.";
        cm.sendSimple(talk);
    } else if (status == 3) {
        if (selection == 1) {
            cm.sendOk("#fs15#重新使用碎片.");
            cm.dispose();
            return;
        }
        if (cm.getPlayer().getKeyValue(20190211, "karuta_4") == 100) {
            cm.getPlayer().setKeyValue(20190211, "karuta_4", 0)
        }
        cm.getPlayer().setKeyValue(20190211, "karuta_4", cm.getPlayer().getKeyValue(20190211, "karuta_4") + Packages.server.Randomizer.rand(7, 11));
        if (cm.getPlayer().getKeyValue(20190211, "karuta_4") >= 100) {
            cm.getPlayer().setKeyValue(20190211, "karuta_4", 100)
        }
        cm.gainItem(itemlist[selected][0], 1);
        cm.gainItem(2434587, -15);
        cm.dispose();
    }
}
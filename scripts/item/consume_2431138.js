var status = -1;
importPackage(java.sql); 
importPackage(java.lang); 
importPackage(Packages.database); 
importPackage(Packages.handling.world); 
importPackage(Packages.constants); 
importPackage(java.util); 
importPackage(java.io); 
importPackage(Packages.client.inventory); 
importPackage(Packages.client); 
importPackage(Packages.server); 
importPackage(Packages.tools.packet); 
function start() {
    status = -1;
    action(1, 0, 0);
}
黑色 = "#fc0xFF191919#"
var names = [["聯盟通行證使用券", 2430046, 1], ["楓幣", 0], ["名聲值", 2432970, 10], ["綻放硬幣", 500], ["核心寶石100個", 2435719, 100], ["ARC符文", 2439301, 3], ["方塊", 2439301, 3], ["重生火焰", 2439301, 3], ["BOSS入場重置券", 2430029, 10]]
var day = 7; //有效期限選項設定(天)
function action(mode, type, selection) {
 
    if (mode == -1) {
        cm.dispose(); 
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
 
    if (status == 0) {
        對話 = "#fs15#測試伺服器支援活動。\r\n\r\n#b"
        對話 += "#L999##r#e[成長任務]#n#k#b綻放競速前置任務完成\r\n\r\n"
        for (var a = 0; a < names.length;  a++) {
            對話 += "#L" + a + "#" + names[a][0] + " 領取 (200等以上帳號每日限1次)\r\n"
        }
        cm.sendSimpleS( 對話, 4, 9000030);
    } else if (status == 1) {
        if (selection == 999) {
            cm.sendOkS(" 前置任務已完成！", 4, 9000030);
            if (cm.getPlayer().getQuestStatus(50006)  == 1) {
                cm.getPlayer().setKeyValue(50006,  "1", "1");
            }
            cm.dispose(); 
        } else {
            var keyvalue = "Tester" + selection;
 
            if (cm.getClient().getKeyValue(keyvalue)  == null) {
                cm.getClient().setKeyValue(keyvalue,  "0");
            }
 
            if (parseInt(cm.getClient().getKeyValue(keyvalue))  == 1) {
                cm.sendOkS(" 今日已領取 #r" + names[selection][0] + "#k。", 4, 9000030);
                cm.dispose(); 
                return;
            }
            if (selection == 1) {
                //楓幣
                cm.gainMeso(1000000000); 
            } else if (selection == 3) {
                //綻放硬幣 
                cm.getPlayer().setKeyValue(100794,  "point", (cm.getPlayer().getKeyValue(100794,  "point") + 500) + "");
            } else if (selection == 6) {
                //方塊 
                cm.gainItem(5062009,  100);
                cm.gainItem(5062010,  100);
                cm.gainItem(5062500,  100);
            } else if (selection == 7) {
                //重生火焰
                cm.gainItem(2048716,  100);
                cm.gainItem(2048717,  100);
                cm.gainItem(2048753,  100);
            } else {
                cm.gainItem(names[selection][1],  names[selection][2]);
            }
            cm.getClient().setKeyValue(keyvalue,  "1");
            cm.sendOkS("#b"  + names[selection][0] + "#k已發放。", 4, 9000030);
            cm.dispose(); 
        }
    }
}
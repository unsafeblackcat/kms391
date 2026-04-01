var status = -1;
 
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
    wList = [];
    wGain = [];
    if (status == 0) {
        getItem();
        getItem2();
        if (cm.getPlayer().getClient().getKeyValue("ServerBackRE")  == null) {
            selStr = "#b2022-11-30 伺服器回檔#k補償領取系統\r\n\r\n點擊 #b下一步#l 領取補償道具\r\n\r\n";
            for (i = 0; i < wList.length;  i++) {
                selStr += "#i" + wList[i] + ":# #t" + wList[i] + ":# #b" + wGain[i] + "個#l\r\n";
            }
            cm.sendSimpleS(selStr,  4, 2007);
        } else {
            selStr = "您已領取過此補償。";
            cm.sendOk(selStr); 
            cm.dispose(); 
        }
 
    } else if (status == 1) {
       cm.getPlayer().getClient().setKeyValue("ServerBackRE",  "1");
        getItem();
        getItem2();
        selStr2 = "已成功領取以下道具：\r\n\r\n";
        for (i = 0; i < wList.length;  i++) {
            selStr2 += "#i" + wList[i] + ":# #t" + wList[i] + ":# #b" + wGain[i] + "個#l\r\n";
        }
        for (i = 0; i < wList.length;  i++) {
            cm.gainItem(wList[i],  wGain[i]);
        }
       
        cm.sendSimpleS(selStr2,  4, 2007);
        cm.dispose(); 
    }
}
 
function getItem() {
    // 補償道具ID清單 
    wList.push(2450134),  // 保留原始數值 
    wList.push(2023072), 
    wList.push(2439869), 
    wList.push(5060048); 
}
 
function getItem2() {
    // 對應道具數量 
    wGain.push(5),   // 與wList順序對應 
    wGain.push(2), 
    wGain.push(300), 
    wGain.push(3); 
}
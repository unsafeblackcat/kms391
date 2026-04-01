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
        if (cm.getPlayer().getClient().getKeyValue("NEWCHAR2") == null) {
            selStr = "#fs15#為首次創建角色的玩家準備了以下獎勵道具。\r\n\r\n點擊 #b下一步#l 即可領取獎勵\r\n\r\n";
            for (i = 0; i < wList.length; i++) {
                selStr += "#i" + wList[i] + ":# #t" + wList[i] + ":# #b" + wGain[i] + "개#l\r\n";
            }
            cm.sendSimpleS(selStr, 4, 2007);
        } else {
            selStr = "#fs15#已領取過此獎勵（限領1次）";
            cm.sendOk(selStr);
            cm.dispose();
        }


    } else if (status == 1) {
        getItem();
        getItem2();
        selStr2 = "#fs15#已成功領取以下道具：\r\n\r\n";
        for (i = 0; i < wList.length;  i++) {
            selStr2 += "#i" + wList[i] + ":# #t" + wList[i] + ":# #b" + wGain[i] + "個#l\r\n";
        }
        for (i = 0; i < wList.length;  i++) {
            cm.gainItem(wList[i],  wGain[i]); // 實際發放道具 
        }
 
        cm.getPlayer().getClient().setKeyValue("NEWCHAR2",  "1"); // 標記已領取 
        cm.sendSimpleS(selStr2,  4, 2007);
        cm.dispose(); 
    }
}

function getItem() {
    wList.push(2435937),
            wList.push(2434290),
            wList.push(4009547),
            wList.push(2435719),
            wList.push(2636375),
            wList.push(2636135),
            wList.push(2439279),
            wList.push(2636656),
            wList.push(4001716);
            wList.push(4034803);
			
}

function getItem2() {

    wGain.push(1),
            wGain.push(100),
            wGain.push(500),
            wGain.push(100),
            wGain.push(100),
            wGain.push(100),
            wGain.push(50),
            wGain.push(50),
            wGain.push(10),
            wGain.push(1);
}
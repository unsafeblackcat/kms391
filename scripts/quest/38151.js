importPackage(Packages.tools.packet); 
importPackage(java.lang); 
importPackage(java.util); 
importPackage(java.awt); 
importPackage(Packages.server); 
importPackage(Packages.constants); 
importPackage(Packages.client.inventory); 
 
status = -1;
function end(mode, type, selection) {
 
    if (mode == -1) {
        qm.dispose(); 
        return;
    }
    if (mode == 0) {
        if (status == 1) {
            qm.sendOk(" 我會一直在這裡，隨時都可以再來找我。");
            qm.dispose(); 
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }
 
    if (status == 0) {
        qm.getPlayer().dropMessage(5,  "任務完成");
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot(); 
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot(); 
        if (leftslot < 5 && leftslot1 < 5) {
            qm.sendOk(" 請清空背包後再來找我。");
            qm.dispose(); 
            return;
        }
        qm.sendNext("#b[ 每日任務] 擊殺1,000隻鐵砲彈#k 任務已經完成了啊。");
    } else if(status == 1) {
        qm.sendNext("#b#e#i1713001#  #z1713001# 5個#k#n已經發放了。檢查一下裝備欄吧");
    } else if (status == 2) {
        for (a = 0; a < 5; a++) {
            qm.gainItem(1713001,  1);
        }
        qm.forceCompleteQuest(); 
        str = qm.getPlayer().getV("Authentic3"); 
        ab = str.split(","); 
        var clear = 0;
        for (var a = 0; a < ab.length;  a++) {
            if (qm.getPlayer().getQuestStatus(ab[a])  == 2) {
                clear++
            }
        }
        qm.getPlayer().setKeyValue(38150,  "count", (clear + 2) + "");
        qm.dispose(); 
    }
}
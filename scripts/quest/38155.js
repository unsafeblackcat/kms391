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
            qm.sendOk("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
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
        qm.getPlayer().dropMessage(5, "퀘스트 완료");
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 5 && leftslot1 < 5) {
            qm.sendOk("인벤토리를 비우고 다시 말을 걸어주세요.");
            qm.dispose();
            return;
        }
        qm.sendNext("#b[일일 퀘스트] 엄격한 역무원 500마리 사냥#k 임무를 완수하고 돌아왔군.");
    } else if(status == 1) {
        qm.sendNext("#b#e#i1713001# #z1713001# 5개#k#n를 지급했다. 장비창을 환인해 봐라");
    } else if (status == 2) {
        for (a = 0; a < 5; a++) {
            qm.gainItem(1713001, 1);
        }
        qm.forceCompleteQuest();
        str = qm.getPlayer().getV("Authentic3");
        ab = str.split(",");
        var clear = 0;
        for (var a = 0; a < ab.length; a++) {
            if (qm.getPlayer().getQuestStatus(ab[a]) == 2) {
                clear++
            }
        }
        qm.getPlayer().setKeyValue(38150, "count", (clear + 2) + "");
        qm.dispose();
    }
}
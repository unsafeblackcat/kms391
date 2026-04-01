var status;
importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);

sum = 0;
sum2 = 0;
atype = 0;
addslot = 8;
function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendSimpleS("자! 내가 가진 확장권을 사용해서 슬롯을 #b#e"+addslot+"칸#n#k 늘려줄 수 있쪄.어떤 슬롯을 늘리고 싶어? 우끼?\r\n\r\n#b#e#L1#장비 슬롯!\r\n#L2#소비 슬롯!\r\n#L3#설치 슬롯!\r\n#L4#기타 슬롯!\r\n#L5#창고 슬롯!\r\n#k\r\n#L6#...좀 더 고민해 볼게.", 0x04, 9000030)
    } else if (status == 1) {
        atype = selection;
        if (selection == 6) {
            cm.dispose();
        } else if (selection == 5) {
            if (cm.getPlayer().getStorage().getSlots() < 128) {
                cm.sendYesNo("창고 슬롯을 "+addslot+"칸 더 늘릴수 있쪄. 창고 슬롯을 확장할거야? 창고 슬롯을 확장하는게 맞아? 우끼?", 9000030);
            } else {
                cm.sendOkS("이미 창고 슬롯이 최대치이므로 사용할 수 없쪄.", 0x04, 9000030);
                cm.dispose();
            }
        } else {
            type = selection == 1 ? "장비" : selection == 2 ? "소비" : selection == 3 ? "설치" : selection == 4 ? "기타" :"창고";
            if (cm.getPlayer().getInventory(atype).getSlotLimit() < 128) {
                cm.sendYesNo(type+"슬롯을 "+addslot+"칸 더 늘릴수 있쪄. "+type+" 슬롯을 확장할거야? "+type+" 슬롯을 확장하는게 맞아? 우끼?", 9000030);
            } else {
                cm.sendOkS("이미 슬롯칸이 최대치이므로 사용할 수 없쪄.", 0x04, 9000030);
                cm.dispose();
            }
        }
    } else if (status == 2) {
        if (atype <= 4) {
            if (cm.getPlayer().getInventory(atype).getSlotLimit() < 128) {
                type = atype == 1 ? "장비" : atype == 2 ? "소비" : atype == 3 ? "설치" : atype == 4 ? "기타" : "창고";
                cm.sendOk(type + "슬롯을 확장하는데 성공했쪄 "+type+"슬롯이 #r"+(cm.getPlayer().getInventory(atype).getSlotLimit() + addslot)+"칸#k으로 늘어쪄", 9000030);
                cm.gainItem(2433929, -1);
                cm.getPlayer().getInventory(atype).addSlot(addslot);
                cm.getPlayer().getClient().send(CWvsContext.InventoryPacket.getSlotUpdate(atype, cm.getPlayer().getInventory(atype).getSlotLimit()));
                cm.dispose();
            } else {
                cm.sendOkS("이미 슬롯칸이 최대치이므로 사용할 수 없쪄.", 0x04, 9000030);
                cm.dispose();
            }
        } else if (atype == 5) {
            //창고슬롯
            if (cm.getPlayer().getStorage().getSlots() < 128) {
                cm.getPlayer().getStorage().increaseSlots(addslot);
                cm.sendOk(type + "슬롯을 확장하는데 성공했쪄 창고 슬롯이 #r"+ cm.getPlayer().getStorage().getSlots() +"칸#k으로 늘어쪄.", 9000030);
                cm.gainItem(2433929, -1);
                cm.dispose();
            } else {
                cm.sendOkS("이미 창고 슬롯이 최대치이므로 사용할 수 없쪄.", 0x04, 9000030);
                cm.dispose();
            }
        }
    }
}
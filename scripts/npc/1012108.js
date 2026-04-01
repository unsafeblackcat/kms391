/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	제농 에 의해 만들어 졌습니다.

	엔피시아이디 : 9250022

	엔피시 이름 : 부아

	엔피시가 있는 맵 :  :  (0)

	엔피시 설명 : MISSINGNO


*/
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);
importPackage(Packages.server.enchant);
importPackage(java.sql);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client);
importPackage(Packages.server);


var status = -1;
var suc, itemid;
var usecoin = 4001129, coincount = 1;
var type, value;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.sendSimple("어웨이크링 업그레이드 시스템 입니다.\r\n펫 전직 1회시도에 필요한 재료는 #i4310042# 30개가 소모됩니다.\r\n#b#L1#1기펫 진화하기\r\n#L2#2기펫 진화하기");
    } else if (status == 1) {
        var txt = "어느 펫을 진화 시킬건가요?\r\n"
        type = selection;//구분용
        var checkitemid = selection == 1 ? 5000930 : 5002079;
        var size = 0, check = false;
        inz = cm.getInventory(5)
        for (w = 0; w < inz.getSlotLimit(); w++) {
            check = false;
            if (!inz.getItem(w) || inz.getItem(w).getPet() == null) {
                continue;
            }
            if (inz.getItem(w).getItemId() >= checkitemid && inz.getItem(w).getItemId() <= checkitemid + 2) {
                for (i = 0; i < cm.getPlayer().getPets().length; i++) {
                    if (cm.getPlayer().getPets()[i] != null) {
                        if (cm.getPlayer().getPets()[i].getInventoryPosition() == inz.getItem(w).getPet().getInventoryPosition()) {
                            check = true;
                            continue;
                        }
                    }
                }
                if (check) {
                    continue;
                }
                txt += "#L"+w+"# #i"+inz.getItem(w).getItemId()+"# #b#z"+inz.getItem(w).getItemId()+"#\r\n";
                size++;
            }
        }
        if (size == 0) {
            cm.sendOk("강화를 할 수 있는 펫이 없습니다.\r\n\r\n장착중인 펫은 강화가 불가능 합니다.")
            cm.dispose();
            return;
        } else {
            cm.sendSimple(txt);
        }
    } else if (status == 2) {
        value = selection
        itemid = cm.getInventory(5).getItem(value).getItemId();
        if (cm.getPlayer().getV("Upgrade_"+itemid) == null) {
            cm.getPlayer().addKV("Upgrade_"+itemid, "30");
        }
        suc = parseInt(cm.getPlayer().getV("Upgrade_"+itemid));
        txt = "#i"+itemid+"# 해당 펫의 확률은 아래와 같습니다. 강화를 시도하겠습니까?\r\n\r\n"
        txt += "성공확률 : " + suc + "%\r\n"
        txt += "실패확률 : " + (95 - suc)+ "%\r\n" //10%는 파괴 고정확률이라함
        txt += "파괴확률 : 5%\r\n\r\n" 
        txt += "#b#L1#강화하겠습니다.\r\n"
        txt += "#L2#안하겠습니다."
        cm.sendSimple(txt)
    } else if (status == 3) {
        if (selection == 1) {
            if (!cm.haveItem(usecoin, coincount)) {
                cm.sendOk("#i4310042#이 모자랍니다.");
                cm.dispose();
                return;
            }
            txt = "";
            if (Randomizer.isSuccess(suc)) {
                giveitem = (itemid >= 5002079 && itemid <= 5002081) ? 5002197 : 5002079;
                give = Randomizer.rand(giveitem, giveitem + 2);
                txt += "성공하였습니다~\r\n#i"+give+"# #z"+give+"# 획득" + type
                World.Broadcast.broadcastMessage(CField.getGameMessage(9, ""+ cm.getPlayer().getName()+"님이 펫 강화에 성공해 "+cm.getItemName(give)+"을(를) 얻으셨습니다."));
                Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.CASH, value, cm.getInventory(5).getItem(value).copy().getQuantity(), true);

                time = new Date();
                var inz = Packages.client.inventory.itemz = new Packages.client.inventory.Item(give, 0, 1);
                inz.setExpiration((new Date(time.getFullYear(), time.getMonth(), time.getDate(), 0, 0, 0, 0)).getTime() + (1000 * 60 * 60 * 24 * 21));
                var pet = MaplePet.createPet(give, MapleInventoryIdentifier.getInstance());
                inz.setPet(pet);
                inz.setUniqueId(pet.getUniqueId());
                MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
                MapleInventoryManipulator.addFromDrop(cm.getClient(), inz, false);
                
                cm.getPlayer().removeV("Upgrade_"+itemid);
            } else if (Randomizer.isSuccess((90 - suc))) {
                txt += "강화에 실패했지만 자석펫은 살았습니다. \r\n강화확률이 10% 증가합니다."
                World.Broadcast.broadcastMessage(CField.getGameMessage(9, ""+ cm.getPlayer().getName()+"님이 펫 강화에 실패했지만 펫이 되살아났습니다."));
                if (suc < 90) {
                    cm.getPlayer().addKV("Upgrade_"+itemid, (suc + 10)+"");
                }
            } else {
                txt += "강화에 실패, 동시에 펫이 사망했습니다.";
                World.Broadcast.broadcastMessage(CField.getGameMessage(9, ""+ cm.getPlayer().getName()+"님이 펫 강화에 실패해 펫이 흔적도없이 사라졌습니다."));
                Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.CASH, value, cm.getInventory(5).getItem(value).copy().getQuantity(), true);
                if (suc < 90) {
                    cm.getPlayer().addKV("Upgrade_"+itemid, (suc + 10)+"");
                }
            }
            cm.gainItem(usecoin, -coincount);
            cm.sendOk(txt);
            cm.dispose();
        } else {
            cm.dispose();
        }
    }

}

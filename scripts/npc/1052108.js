importPackage(java.io);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.constants);

var status = -1;
var con = 1;
var con1 = 1;
var con2 = true;
var list = []; // 道具列表
var garbage = []; // 要刪除的道具列表
var slotnumber = []; // 槽位編號
// 製造道具列表的變數們
var check = true;
var MakeValue = 0;
var jj = 0;
var equip = Packages.client.inventory.MapleInventoryType.EQUIP;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if(status == 0 && selection == 1000){
            con = 1;
            con1 = 1;
        } else if(status == 0 && selection == 2000) {
            con = 2;
            con1 = 2;
        } else if(status == 0 && selection == 3000) {
            con = 3;
            con1 = 3;
        } else if(status == 0 && selection == 4000) {
            con = 4;
            con1 = 4;
        } else if(status == 0 && selection == 5000) {
            con = 5;
            con1 = 5;
        } else if(status == 0 && selection == 6000) {
            con = 6;
            con1 = 6;
        } else if(status == 0 && selection == 7000) {
            con2 = false;
        } else if(status == 0 && selection == 8000) {
            con2 = true;
            reset();
        } else if(status == 0 && selection == 9000) {
            for(k=0; k<list.length; k++){
                if(list[k]['star'] == 1){
                    del(list[k]['slnumber'], list[k]['slot']);
                    //cm.getPlayer().dropMessage(5, "值 : "+list[k]['slnumber']+"裝備 : "+list[k]['slot']);
                }
            }
            reset();
        } else if(status == 0 && selection <= 150 && con2 == true) {
            del(list[selection]['slnumber'], con);
            reset();
        } else if(status == 0 && selection <= 150 && con2 == false) {
            // 道具選擇解除
            if(list[selection]['star'] == 0){
                list[selection]['star'] = 1;
            } else {
                list[selection]['star'] = 0;
            }
        } else {
            status++;
        }
    }
    if (status == 0) {
        talk = "#r(丟棄道具時不會掉落而會消失，且無法復原，請留意！)#k\r\n"
        talk += "#b#L1000#裝備#l #L2000#消耗#l #L4000#其他#l #L3000#設置#l  #L5000#現金#l#k\r\n"
        if(con2 == true){
            talk += "#r#L7000#刪除多個#l#k\r\n\r\n"
        } else {
            talk += "#r#L8000#逐個刪除#l #L9000#刪除已選擇道具#l#k\r\n\r\n"
        }
        if(check == true && list.length == 0){
            listmake();
            check = false;
            con = con1;
        }
        for (i = 0; i < list.length; i++) {
            if(list[i]['star'] != 0 && list[i]['slot'] == con){
                talk += "#b#L" + i + "# #i"+list[i]['itemid']+"# #z"+list[i]['itemid']+"##k #r["+slotnumber[i]+"號槽位]#k#l\r\n"
            } else if(list[i]['slot'] == con){
                talk += "#L" + i + "# #i"+list[i]['itemid']+"# #z"+list[i]['itemid']+"# #r["+slotnumber[i]+"號槽位]#k#l\r\n"
            }
        }
        cm.sendSimple(talk);
    }
}

function reset(){
    list = [];
    garbage = [];
    slotnumber = [];
    check = true;
}

function listmake(){
    if (check == true){
        jj = 0;
    } else {
        jj = slotnumber.length;
    }
    // 上述 jj 值為 slotnumber 的值測量
    for(MakeValue = 0; MakeValue < 7; MakeValue++){
        con = MakeValue;
        for (i = 0; i < cm.getInventory(con).getSlotLimit(); i++) {
            if (cm.getInventory(con).getItem(i) != null) {
                slotnumber.push(i);
            }
        }
        for (jj; jj<slotnumber.length; jj++){
            list[jj] = new Item(cm.getInventory(con).getItem(slotnumber[jj]).getItemId(), 0, con, slotnumber[jj]);
        }
    }
}

function Item(id, va, conid, sl){
    this.itemid = id;
    this.star = va;
    this.slot = conid;
    this.slnumber = sl;
}

function del(st, con){
    if(con == 1){
        equip = Packages.client.inventory.MapleInventoryType.EQUIP;
    } else if(con == 2){
        equip = Packages.client.inventory.MapleInventoryType.USE;
    } else if(con == 3){
        equip = Packages.client.inventory.MapleInventoryType.SETUP;
    } else if(con == 4){
        equip = Packages.client.inventory.MapleInventoryType.ETC;
    } else if(con == 5){
        equip = Packages.client.inventory.MapleInventoryType.CASH;
        if (GameConstants.isPet(cm.getInventory(5).getItem(st).getItemId())) {
            for (i = 0; i < cm.getPlayer().getPets().length; i++) {
                if (cm.getPlayer().getPets()[i] != null) {
                    if (cm.getPlayer().getPets()[i].getInventoryPosition() == st) {
                        cm.sendOk("已排除正在裝備的寵物並完成刪除。");
                        cm.dispose();
                        return;
                    }
                }
            }
        }
    } else if(con == 6){
        equip = Packages.client.inventory.MapleInventoryType.DECORATION;
    }
    Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), equip, st, cm.getInventory(con).getItem(st).copy().getQuantity(), true);
}
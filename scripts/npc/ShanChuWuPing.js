importPackage(java.io);   
importPackage(Packages.client.items);   
importPackage(Packages.server.items);   
importPackage(Packages.constants);   
 
var status = -1; 
var con = 1; 
var con1 = 1; 
var con2 = true; 
var list = [];  // 物品列表
var garbage = []; // 待删除物品列表
var slotnumber = [];  // 槽位编号
// 创建物品列表的变量
var check = true; 
var MakeValue = 0; 
var jj = 0; 
var talkType = 0x86; 
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
        if(status == 0 && selection == 1000) { 
            con = 1; 
            con1 = 1; 
            check = true; // 重新生成当前类型的物品列表
        } else if(status == 0 && selection == 2000) { 
            con = 2; 
            con1 = 2; 
            check = true; // 重新生成当前类型的物品列表
        } else if(status == 0 && selection == 3000) { 
            con = 3; 
            con1 = 3; 
            check = true; // 重新生成当前类型的物品列表
        } else if(status == 0 && selection == 4000) { 
            con = 4; 
            con1 = 4; 
            check = true; // 重新生成当前类型的物品列表
        } else if(status == 0 && selection == 5000) { 
            con = 5; 
            con1 = 5; 
            check = true; // 重新生成当前类型的物品列表
        } else if(status == 0 && selection == 6000) { 
            con = 6; 
            con1 = 6; 
            check = true; // 重新生成当前类型的物品列表
        } else if(status == 0 && selection == 7000) { 
            con2 = false; 
        } else if(status == 0 && selection == 8000) { 
            con2 = true; 
            reset(); 
        } else if(status == 0 && selection == 9000) { 
            for(k=0; k<list.length; k++) { 
                if(list[k]['star'] == 1) { 
                    del(list[k]['slnumber'], list[k]['slot']);
                    // 从列表中移除已删除的物品
                    list.splice(k, 1); 
                    k--; 
                } 
            } 
            reset(); 
        } else if(status == 0 && selection <= 150 && con2 == true) { 
            del(list[selection]['slnumber'], con);
            // 从列表中移除已删除的物品
            list.splice(selection, 1); 
            reset(); 
        } else if(status == 0 && selection <= 150 && con2 == false) { 
            // 아이템 선택 해제 
            if(list[selection]['star'] == 0) { 
                list[selection]['star'] = 1; 
            } else { 
                list[selection]['star'] = 0; 
            } 
        } else { 
            status++; 
        } 
    } 
    if (status == 0) { 
        var talk = "#fs20##r(刪除的物品不會掉落，會消失，無法修復，請注意！)#k\r\n"; 
        talk += "#b#L1000#裝備#l #L2000#消耗#l #L4000#其他#l #L3000#裝飾#l #L5000#特殊#l #L6000#時裝#l#k\r\n"; 
        if(con2 == true) { 
            talk += "#r#L7000#刪除多個#l#k\r\n\r\n"; 
        } else { 
            talk += "#r#L8000#逐個刪掉#l #L9000#刪除選定的項目#l#k\r\n\r\n"; 
        } 
        if(check == true) { 
            listmake(); 
            check = false; 
            con = con1; 
        } 
        var itemCount = 0; 
        for (i = 0; i < list.length; i++) { 
            if(list[i]['slot'] == con) { 
                if(itemCount % 4 == 0 && itemCount > 0) { 
                    talk += "\r\n"; 
                } 
                if(list[i]['star'] != 0 && list[i]['slot'] == con) { 
                    talk += "#L" + i + "# #i"+list[i]['itemid']+"# #l"; 
                } else if(list[i]['slot'] == con) { 
                    talk += "#L" + i + "# #i"+list[i]['itemid']+"# #l"; 
                } 
                itemCount++; 
            } 
        } 
        cm.sendSimpleS(talk, talkType); 
    } 
} 
 
function reset() { 
    list = []; 
    garbage = []; 
    slotnumber = []; 
    check = true; 
} 
 
function listmake() { 
    slotnumber = [];
    list = [];
    jj = 0;
    for (i = 0; i < cm.getInventory(con).getSlotLimit(); i++) { 
        if (cm.getInventory(con).getItem(i) != null) { 
            slotnumber.push(i);   
        } 
    } 
    for (jj; jj<slotnumber.length; jj++) { 
        list[jj] = new Item(cm.getInventory(con).getItem(slotnumber[jj]).getItemId(), 0, con, slotnumber[jj]); 
    } 
} 
 
function Item(id, va, conid, sl) { 
    this.itemid = id; 
    this.star = va; 
    this.slot = conid; 
    this.slnumber = sl; 
} 
 
function del(st, con) { 
    var inventoryType; 
    switch(con) { 
        case 1: 
            inventoryType = Packages.client.inventory.MapleInventoryType.EQUIP;   
            break; 
        case 2: 
            inventoryType = Packages.client.inventory.MapleInventoryType.USE;   
            break; 
        case 3: 
            inventoryType = Packages.client.inventory.MapleInventoryType.SETUP;   
            break; 
        case 4: 
            inventoryType = Packages.client.inventory.MapleInventoryType.ETC;   
            break; 
        case 5: 
            inventoryType = Packages.client.inventory.MapleInventoryType.CASH;   
            // 宠物特殊处理 
            if (GameConstants.isPet(cm.getInventory(5).getItem(st).getItemId())) { 
                for (i = 0; i < cm.getPlayer().getPets().length; i++) { 
                    if (cm.getPlayer().getPets()[i] != null && 
                        cm.getPlayer().getPets()[i].getInventoryPosition() == st) { 
                        cm.sendOk(" 除正在安裝的寵物外已删除."); 
                        cm.dispose();   
                        return; 
                    } 
                } 
            } 
            break; 
        case 6: 
            // 时装类型修正为 EQUIP 或 CASH，具体根据服务器设定 
            inventoryType = Packages.client.inventory.MapleInventoryType.CODY;  
            break; 
        default: 
            cm.sendOk(" 無效的物品類型."); 
            cm.dispose();   
            return; 
    } 
 
    // 确保物品存在 
    if (cm.getInventory(con).getItem(st) == null) { 
        cm.sendOk(" 物品不存在或已被刪除."); 
        cm.dispose();   
        return; 
    } 
 
    // 执行删除 
    try { 
        Packages.server.MapleInventoryManipulator.removeFromSlot(   
            cm.getClient(),   
            inventoryType, 
            st, 
            cm.getInventory(con).getItem(st).copy().getQuantity(),   
            true 
        ); 
    } catch (e) { 
        cm.sendOk(" 刪除物品時發生錯誤: " + e); 
        cm.dispose();   
    } 
} 
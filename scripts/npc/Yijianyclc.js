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

// 定义所有需要完成的任务ID数组
var questIDs = [38404
    // 38214, 36013, 33565, 31851, 31833, 3496, 3470, 30007, 3170, 31179, 
    // 3521, 31152, 34015, 33294, 34330, 34585, 35632, 35731, 35815, 
    // 34478, 34331, 34478, 100114, 16013, 34120, 34218, 34330, 34331, 
    // 34478, 34269, 34272, 34585, 34586, 6500, 1465, 1466, 26607, 1484, 
    // 39921, 16059, 16015, 16013, 100114, 34128, 39013, 34772, 39034, 
    // 34269, 34271, 34272, 34243, 39204, 15417, 34377, 34477, 34450, 
    // 38401, 
];

function start() {
    status = -1;
    action(1, 0, 0);
}

黑色 = "#fc0xFF191919#"

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
        對話 = "#fs15#確定"+黑色+"要用開放原初徽章所有的槽？\r\n"
        對話 += "請點擊'#r是#k'開放原初6槽並完成所有任務\r\n"+黑色+"'#r否#k'表示這次取消操作！"
        cm.sendYesNoS(對話, 0x04, 9062004);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot(); 
        if (leftslot < 2) {
            cm.sendOk("#fs15##r 請確保其他欄位有2格以上空間。");
            cm.dispose(); 
            return;
        }
        
        // 循环完成所有指定的任务
        for (var i = 0; i < questIDs.length; i++) {
            cm.forceCompleteQuest(questIDs[i]);
        }
        
        對話 = "#fs15#"+黑色+"已經爲你開放了原初所有的裝備槽，並完成了所有任務\r\n\r\n"
        對話 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
        cm.sendOkS(對話, 0x04, 9062004);
        cm.dispose(); 
    }
}
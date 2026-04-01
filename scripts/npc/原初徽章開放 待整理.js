/**
 * 原初徽章槽位檢測系統 
 * 功能：僅檢查/開放槽位，不涉及實際裝備操作 
 * 適用版本：MapleStory v2xx+
 */
// 在腳本開頭添加導入語句 
importPackage(Packages.client.inventory); 
 
function start() {
    var slots = checkAuthenticSlots(cm.getPlayer()); 
    var msg = "#fs15#當前原初徽章槽位狀態：\r\n";
    
    // 構建槽位狀態信息 
    for (var i = 0; i < slots.length;  i++) {
        msg += "#" + (-1700 - i) + "槽: " + 
              (slots[i] ? "#r已啓用#k" : "#a可開放#k") + "\r\n";
    }
    
    cm.sendSimple(msg  + "\r\n#L0#嘗試開放新槽位#l");
}
 
function action(mode, type, selection) {
    if (mode == 1) {
        var result = openNewSlot(cm.getPlayer()); 
        if (result.success)  {
            cm.sendOk(" 成功開放#" + result.slot  + "槽位！");
        } else {
            cm.sendOk(" 所有槽位均已開放或已達上限");
        }
    }
    cm.dispose(); 
}
 
// 核心功能函數 
function checkAuthenticSlots(chr) {
    var slotStatus = [];
    for (var i = -1700; i >= -1705; --i) {
        slotStatus.push(chr.getInventory(MapleInventoryType.EQUIPPED).getItem(i)  != null);
    }
    return slotStatus;
} 
 
function openNewSlot(chr) {
    for (var i = -1700; i >= -1705; --i) {
        if (chr.getInventory(MapleInventoryType.EQUIPPED).getItem(i)  == null) {

            // 模擬槽位開放（實際遊戲需調用服務端方法）
            return { success: true, slot: i };
        }
    }
    return { success: false };
}
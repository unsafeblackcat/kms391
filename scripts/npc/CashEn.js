var enter = "\r\n";
var seld = -1;
var price = 30;
var allstat = 2, atk = 2; // 每次强化增加的全屬性和攻擊力 
var currentItem = null;    // 當前選擇的物品 
var enhancementCount = 0;  // 當前連續强化次數 
var selectedSlot = -1;     // 新增：記錄選擇的物品槽位 
var talkType = 0x86;
var itemsPerRow = 4; // 每行顯示的道具數量 
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, sel) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();    
        return;
    }
    
    if (status == 0) {
        showItemSelection();
    } else if (status == 1) {
        handleItemSelection(sel);
    } else if (status == 2) {
        handleEnhancementResult(sel);
    }
}
 
/* 顯示可强化的時裝物品列表（每4個一列） */
function showItemSelection() {
    var txt = "#fs20#可以將時裝物品强化 需要 #b 30個 #i4310308##k\r\n";
    txt += "#r(每次强化增加全屬性" + allstat + "/攻擊力" + atk + ")#k\r\n\r\n";
    
    var itemCount = 0;
    var hasItems = false;
    
    // 先計算有效物品數量 
    var validItems = [];
    for (var i = 0; i < cm.getInventory(6).getSlotLimit();  i++) {
        var item = cm.getInventory(6).getItem(i);    
        if (item != null && cm.isCash(item.getItemId()))  {
            validItems.push({ 
                slot: i,
                item: item 
            });
            hasItems = true;
        }
    }
    
    if (!hasItems) {
        txt += "#r沒有可强化的時裝物品#k";
        cm.sendOk(txt);    
        cm.dispose();    
        return;
    }
    
    // 按每行4個顯示物品 
    for (var i = 0; i < validItems.length;  i++) {
        if (i % itemsPerRow === 0 && i !== 0) {
            txt += "\r\n"; // 每4個換行 
        }
        txt += "#L" + validItems[i].slot + "# #i" + validItems[i].item.getItemId()  + "# #l";
    }
    
    cm.sendSimpleS(txt,  talkType);
}
 
/* 處理物品選擇 */
function handleItemSelection(sel) {
    selectedSlot = sel; // 記錄選擇的槽位 
    currentItem = cm.getInventory(6).getItem(sel);    
    if (currentItem == null || !cm.isCash(currentItem.getItemId()))   {
        cm.sendOk("  無效的物品選擇");
        cm.dispose();    
        return;
    }
    
    // 重置强化計數 
    enhancementCount = 0;
    
    // 直接進入强化流程 
    performEnhancement();
}
 
/* 執行强化操作 */
function performEnhancement() {
    // 檢查材料是否足夠 
    if (!cm.haveItem(4310308,  price)) {
        cm.sendOk("#fs15#  需要 #b" + price + " #i4310308##k 才能强化");
        cm.dispose();    
        return;
    }
    
    // 扣除材料 
    cm.gainItem(4310308,  -price);
    
    // 强化屬性 
    currentItem.setStr(currentItem.getStr()  + allstat);
    currentItem.setDex(currentItem.getDex()  + allstat);
    currentItem.setInt(currentItem.getInt()  + allstat);
    currentItem.setLuk(currentItem.getLuk()  + allstat);
    currentItem.setWatk(currentItem.getWatk()  + atk);
    currentItem.setMatk(currentItem.getMatk()  + atk);
    
    // 更新物品 
    cm.getPlayer().forceReAddItem(currentItem,  Packages.client.inventory.MapleInventoryType.getByType(6));    
    
    enhancementCount++;
    showEnhancementResult();
}
 
/* 顯示强化結果並詢問是否繼續 */
function showEnhancementResult() {
    var txt = "#fs15#强化成功！#b" + getItemName(currentItem) + "#k\r\n";
    txt += "當前連續强化次數: #r" + enhancementCount + "#k 次,";
    txt += "消耗材料總數: #r" + (enhancementCount * price) + "#k 個\r\n";
    txt += "當前屬性:\r\n" + getItemStats(currentItem) + "\r\n\r\n";
    
    // 檢查是否還能繼續强化 
    var canContinue = cm.haveItem(4310308,  price);
    
    if (canContinue) {
        txt += "#L0#繼續强化 (需要" + price + "個材料)#l\r\n";
    }
    txt += "#L1#完成强化#l";
    
    cm.sendSimpleS(txt,  talkType);
}
 
/* 處理用戶選擇 */
function handleEnhancementResult(sel) {
    if (sel == 0) {
        // 繼續强化 - 重置狀態為1並重新執行强化流程 
        status = 1;
        // 重新獲取當前物品，防止物品變動 
        currentItem = cm.getInventory(6).getItem(selectedSlot);   
        if (currentItem == null || !cm.isCash(currentItem.getItemId()))   {
            cm.sendOk("  物品已不存在或不可强化");
            cm.dispose();   
            return;
        }
        performEnhancement();
    } else {
        // 完成强化 
        cm.sendOk("#fs15#最終强化結果:\r\n" + 
                 "總强化次數: #r" + enhancementCount + "#k\r\n" +
                 getItemStats(currentItem));
                 cm.dispose();    
    }
}
 
/* 獲取物品名稱 */
function getItemName(item) {
    return "#i" + item.getItemId()  + "# #z" + item.getItemId()  + "#";
}
 
/* 獲取物品當前屬性 */
function getItemStats(item) {
    return "STR: " + item.getStr()  + " | DEX: " + item.getDex()  + 
           " | INT: " + item.getInt()  + " | LUK: " + item.getLuk()  + 
           "\r\n物理攻擊: " + item.getWatk()  + " | 魔法攻擊: " + item.getMatk();    
}
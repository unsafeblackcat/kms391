// 冒險島經驗值兌換腳本 - 繁體中文版
// 最後更新：2025年7月18日（農曆六月廿四）
 
노랑 = "#fc0xFFFFBB00#"  // 保留原始色碼變量名（韓文「黃色」）
var status = -1;
 
importPackage(Packages.constants); 
 
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
        status--;
    }
    if (mode == 1) {
        status++;
    }
 
    if (status == 0) {
        cm.sendYesNoS("#fs15# 確定要用當前角色使用？" + 노랑 + "#i2633618##z2633618##k嗎？", 0x04, 9010061);
        return;
    }
    if (cm.getPlayer().getLevel()  > 219) {
        cm.gainExp(27279159629);   // 220級以上固定經驗值
        cm.dispose(); 
        cm.gainItem(2633618,  -1);
        return;
    }
    else if (status == 1) {
        var i = cm.getPlayer().getLevel();  i < 220;
        cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));   // 依當前等級補滿經驗
        cm.gainItem(2633618,  -1);
        cm.dispose(); 
    } else {
        cm.dispose(); 
    }
}
/*
 * 時間神殿 - 克莉絲頓 
 * 諸神的黃昏
 */
 
function start() {
    cm.askAcceptDecline(" 要是有善良之鏡，我就能重新召喚黑魔法師了！\r\n等等！好像不太對勁！為什麼黑魔法師沒有被召喚？這是什麼力量？我感覺到了... 完全不同于黑魔法師的存在啊啊啊!!!!!\r\n\r\n#b(將手搭在克莉絲頓的肩膀上)");
}
 
function action(mode, type, selection) {
    if (mode == 1) {
        cm.removeNpc(cm.getPlayer().getMapId(),  2141000);
        cm.forceStartReactor(cm.getPlayer().getMapId(),  2709000);
    }
    cm.dispose(); 
 
// 若接受 = 召喚皮耶爾 + 克莉絲頓消失 + 1小時計時器 
// 若拒絕 = 無任何效果
}
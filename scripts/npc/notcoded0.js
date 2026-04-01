function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (cm.getPlayer().isGM())  {
        cm.sendOkS("#b 尚未註冊的NPC。\r\n#d代碼 : " + cm.getNpc()  + " 腳本 : " + cm.getScript()  + "\r\n#k本應僅由腳本控制的NPC已被修改。", 2);
    } else {
        cm.sendOkS("#b 葉之谷#k祝您遊戲愉快。", 4); 
    }
    cm.dispose(); 
}
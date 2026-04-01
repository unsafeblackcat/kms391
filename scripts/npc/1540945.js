var status = 0;
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose(); 
    } else {
        if (mode == 0) {
            cm.dispose(); 
            return;
        }
        if (mode == 1)
            status++;
        else 
            status--;
        if (status == 0) {
        cm.sendSimple("#fs15# 勇士啊，您所為何事而來？\r\n\r\n#L0##b想要強化或製作V核心#l\r\n#L1#只是路過。今天天氣真不錯呢");
        } else if (status == 1) {
            if (selection == 0) {
            //cm.getClient().getSession().write(Packages.tools.packet.CField.UIPacket.openUI(1131));  //二次密碼輸入窗 
            cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUI(1132)); 
            cm.dispose(); 
            } else if (selection == 1) {
                cm.sendOk(" 此處氣候會隨艾爾達氣流劇烈變化，請多保重身體。");
                cm.dispose(); 
            }
        }
    }
}
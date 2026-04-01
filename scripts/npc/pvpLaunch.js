var status = -1;
 
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
        text = "#fc0xFFFF9933#Pixi 用戶 #h 0##k 您好～隨時歡迎光臨！\r\n\r\n" +
               "#L28#勳章選項#l    #L29#勳章潛能#l    #L31#勳章任務#l    #L30#勳章傳承#l\r\n";
        cm.sendIllustSimple(text,  0x24);
    } else if (status == 1) {
        cm.dispose(); 
        switch (selection) {
            case 28:
                cm.openNpc("backTuto_9062001"); 
                break;
            case 29:
                cm.openNpc("backTuto_9062002"); 
                break;
            case 30:
                cm.openNpc(2159000); 
                break;
            case 31:
                cm.openNpc(9000066); 
                break;
        }
    }
}
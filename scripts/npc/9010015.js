var status = -1;
var enter = "\r\n";
var list = [4319994, 4319999, 4319997];
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, sel) {
 
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
 
    var text = "#fs15#";
 
    if (status == 0) {
        text += "想用哪種道具進行强化？" + enter + enter;
        text += "#L1##i4319999#(防具、飾品强化)" 
        text += "#L2##i4319999#(徽章、勳章、機器人)"
        text += "#L5##i4319994#" + enter 
        text += "#L3# #i5830000# [IRIS] 宣傳點數 (武器、輔助武器)" + enter
        text += "#L4# #i5830001# [IRIS] 贊助點數 (武器、輔助武器)" + enter;
        cm.sendOkS(text,0x86); 
    } else if (status == 1) {
        switch (sel) {
            case 1:
                cm.dispose(); 
                cm.openNpc(2020008); 
                break;
            case 2:
                cm.dispose(); 
                cm.openNpc(2020009); 
                break;
                case 3:
                    cm.dispose(); 
                    cm.openNpc(2020011); 
                    break;
                    case 4:
                        cm.dispose(); 
                        cm.openNpc(2020013); 
                        break;
                        case 5:
                            cm.dispose(); 
                            cm.openNpc(9000213); 
                            break;
        }
    } 
}
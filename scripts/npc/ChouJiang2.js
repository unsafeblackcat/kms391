importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var enter = "\r\n";
var seld = -1;

var list = ["戒指", "稱號", "椅子", "【傷害皮膚】高級", "【傷害皮膚】一般"]

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

    var msg = "#fs15#"

    if (status == 0) {
        msg += "#fs15#歡迎光臨 #b#h0##k先生！ 我是負責抽籤的NPC。." + enter
        msg += "您想用什麼？" + enter + enter
        for (i = 0; i < list.length; i++) {
            msg += "#L" + i + "# #b" + list[i] + "#k 我想利用抽籤機。" + enter;
        }
        cm.sendSimpleS(msg,0x86);
    } else if (status == 1) {
        switch (sel) {
            case 0:
                cm.dispose();
                cm.openNpc(1540105);
                break;
			case 1:
                cm.dispose();
                cm.openNpc(9072201);
                break;
            case 2:
                cm.dispose();
                cm.openNpc(1540107);
                break;
            case 3:
                cm.dispose();
                cm.openNpc(1540108);
                break;
            case 4:
                cm.dispose();
                cm.openNpc(1540106);
                break;
        }
    }
}
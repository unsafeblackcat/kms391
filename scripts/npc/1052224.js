importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var enter = "\r\n";
var seld = -1;

var list = ["시드링", "칭호 뽑기", "의자", "유닛 데미지 스킨", "일반 데미지 스킨"]

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

    var msg = "#fs11#"

    if (status == 0) {
        msg += "어서오세요 #b#h0##k님! 저는 뽑기 담당 NPC 입니다." + enter
        msg += "무엇을 이용하시겠어요?" + enter + enter
        for (i = 0; i < list.length; i++) {
            msg += "#L" + i + "# #b" + list[i] + "#k 뽑기를 이용하고 싶어요." + enter;
        }
        cm.sendSimple(msg);
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
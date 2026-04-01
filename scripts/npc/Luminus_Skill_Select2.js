importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    블랙 에 의해 만들어 졌습니다.

    엔피시아이디 : 9110007

    엔피시 이름 : 로보

    엔피시가 있는 맵 : 몬스터파크 : 몬스터파크 (951000000)

    엔피시 설명 : 라면 요리사


*/

var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}
var sel = -1;
var a = 0;
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
        cm.sendJobIlust(281, true);
    } else if (status == 1) {
        if (selection == 0) {
            //빛
            cm.teachSkill(27000106, 5, 5);
            cm.teachSkill(27001100, 20, 20);
            cm.teachSkill(27000207, 0, 0);
            cm.teachSkill(27001201, 0, 0);
        } else {
            //어둠
            cm.teachSkill(27000207, 5, 5);
            cm.teachSkill(27001201, 20, 20);
            cm.teachSkill(27000106, 0, 0);
            cm.teachSkill(27001100, 0, 0);

        }
    }
}


function statusplus(millsecond) {
    cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}

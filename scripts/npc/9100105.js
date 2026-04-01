var status = -1;
var enter = "\r\n";

var skin = Array(0, 1, 2, 3, 4, 9, 10, 11, 12, 13, 15, 16, 18, 19);
var hair = Array(0, 1, 2, 3, 4, 5, 6, 7);
var eyes = Array(0, 1, 2, 3, 4, 5, 6, 7);
var idx = 0;
var seld = -1;
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


        var chat = "#fs15#레미넌스 컬러 변경 시스템입니다. 원하시는 옵션을 선택하여 주세요" + enter;
         chat += "#fc0xFFD5D5D5#───────────────────────────#k" + enter;
        chat += "#b#L0#" + "피부색 변경#l" + enter;
        chat += "#L1#" + "머리색 변경#l" + enter;
        chat += "#L2#" + "렌즈색 변경#l" + enter + enter;
         chat += "#fc0xFFD5D5D5#───────────────────────────#k"+ enter ;
        chat += "#L3##r" + "믹스 염색 이용하기#l" + enter;
        chat += "#L4##r" + "믹스 컬러 렌즈 이용하기" + enter;
        cm.sendSimple(chat);
    } else if (status == 1) {
        switch (selection) {
            case 0:
                cm.askCoupon(5153015,skin);
                cm.dispose();
                break;
            case 1:
                cm.askCoupon(5151036,hair);
                cm.dispose();
                break;
            case 2:
                cm.askCoupon(5152111,hair);
                cm.dispose();
                break;
            case 3:
                var chat = "#fs15#";
                chat += "어떤 종류의 헤어 쿠폰을 구매하시겠어요?" + enter;
                chat += "#L0##d#i" + 2433835 + "# #z" + 2433835 + "# 구매하기 #b(100만 메소)" + enter;
                chat += "#L1##d#i" + 2631717 + "# #z" + 2631717 + "# 구매하기 #b(1000만 메소)"

                cm.sendSimple(chat);
                break;
            case 4:
                var chat = "#fs15#";
                chat += "어떤 종류의 렌즈 쿠폰을 구매하시겠어요?" + enter;
                chat += "#L2##d#i" + 5152301 + "# #z" + 5152301 + "# 구매하기 #b(100만 메소)" + enter;
                chat += "#L3##d#i" + 5152300 + "# #z" + 5152300 + "# 구매하기 #b(1000만 메소)"

                cm.sendSimple(chat);
                break;

        }
    } else if (status == 2) {
        seld = selection;
        switch(seld) {
            case 0:
                if (cm.getMeso() < 1000000) {
                    cm.sendOk("메소가 부족합니다.");
                    cm.dispose();
                    return;
                } else {
                    cm.gainItem(2433835, 1);
                    cm.gainMeso(-1000000);
                    cm.sendOk("성공적으로 구매하였습니다.");
                    cm.dispose();
                    return;
                }
                break;
            case 1:
                if (cm.getMeso() < 10000000) {
                    cm.sendOk("메소가 부족합니다.");
                    cm.dispose();
                    return;
                } else {
                    cm.gainItem(2631717, 1);
                    cm.gainMeso(-10000000);
                    cm.sendOk("성공적으로 구매하였습니다.");
                    cm.dispose();
                    return;
                }
                break;
            case 2:
                if (cm.getMeso() < 1000000) {
                    cm.sendOk("메소가 부족합니다.");
                    cm.dispose();
                    return;
                } else {
                    cm.gainItem(5152301, 1);
                    cm.gainMeso(-1000000);
                    cm.sendOk("성공적으로 구매하였습니다.");
                    cm.dispose();
                    return;
                }
                break;
            case 3:
                if (cm.getMeso() < 10000000) {
                    cm.sendOk("메소가 부족합니다.");
                    cm.dispose();
                    return;
                } else {
                    cm.gainItem(5152300, 1);
                    cm.gainMeso(-10000000);
                    cm.sendOk("성공적으로 구매하였습니다.");
                    cm.dispose();
                    return;
                }
                break;

        }
    }
}


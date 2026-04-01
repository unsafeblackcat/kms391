var status = -1;
var enter = "\r\n";

function start() {
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        var chat ="#fs15#";
        chat += "#bZenia Story#k" + " 환생 도우미 입니다. 무엇을 도와드릴까요?" + enter;
        chat += "#L0#" + "#b환생하기 #r(필요 조건 : 레벨 300)" + enter;
        chat += "#L1#" + "#r대화를 그만한다.#k"

      cm.sendSimple(chat);
    } else if (status == 1) {
        if (selection == 0) {
            if (cm.getPlayer().getLevel() == 300) {
                cm.getPlayer().setLevel(251);
                cm.getPlayer().fakeRelog();
                cm.sendOk("성공적으로 환생을 완료하였습니다.");
                cm.dispose();
                return;
            } else {
                cm.sendOk("환생을 하기 위한 레벨이 부족합니다.");
                cm.dispose();
                return;
            }
        }
        if (selection == 1) {
            cm.dispose();
            return;
        }
    }
}

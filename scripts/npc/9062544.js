var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var chat = "#fs15#";
        chat += "#e< PRAY 환전소 >#n\r\n\r\n"
        chat += "  #r* 10억 메소가 소모됩니다.\r\n"
        chat += "#L1##b10억 정착금으로 교환하시겠습니까?#k#l";
        cm.sendSimple(chat);

    } else if (status == 1) {
        temp = [];
        temp.push(Math.floor(cm.getPlayer().getMeso() / 1000000000));
        temp.sort();
        max = temp[0];
        cm.sendGetNumber("당신은 최대 #b" + max + "번을#k 교환할 수 있군요..\r\n몇 번 교환하시겠습니까...?", 1, 1, max);

    } else if (status == 2) {
        if (selection <= 0) {
            cm.sendOk("#fs15##r오류가 발생하였습니다.#k");
            cm.dispose();
	return;
        }

        if (cm.getPlayer().getMeso() >= (1000000000 * selection)) {
            cm.gainItem(4001716, (1 * selection));
            cm.gainMeso(-(1000000000 * selection));
            cm.sendOk("#fs15##b교환을 성공적으로 진행하였습니다.#k");
            cm.dispose();
        } else {
            cm.sendOk("#fs15##r메소가 부족하여 교환을 실패하였습니다.#k");
            cm.dispose();
        }
    }
}
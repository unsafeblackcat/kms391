importPackage(Packages.server.quest);

status = -1;
function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if(status == 5){
            cm.sendOk("그렇군요... 저희와 함께 할 수 없어 아쉽습니다.");
            cm.dispose();
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        qm.sendYesNo("검은 마법사의 잔상을 격파하고 #i4036461# #b#z4036461# 1개#k를 구해다 주십시오. 그리고 군단장 #r하드 스우#k의 잔상을 격파하는 겁니다. 가능하시겠습니까?");
    } else if (status == 1) {
        var chat ="어둠의 흔적은 특수한 조건 하에서만 깃든다는 점을 이미 알고 계실 겁니다. 아래의 조건을 만족하여 #r하드 스우#k을 격파하고 돌아와 주십시오.\r\n\r\n"
        chat+="#r#e<임무 수행 조건>#k#n\r\n"
        chat+="#b-혼자서격파\r\n"
        chat+="#b-최종 데미지 20% 감소\r\n\r\n"
        chat+="#r※ 해당 퀘스트가 수행 중인 상태로 보스 입장 시도 시 임무 수행 조건에 맞는 보스 스테이지에 입장할 수 있습니다.\r\n"
        qm.sendOk(chat);
        startQuest();
        qm.dispose();
    }
}


function startQuest() {
    MapleQuest.getInstance(35661).forceStart(qm.getPlayer(), 0, null);
    qm.forceStartQuest();
    qm.dispose();
}

function end(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 1) {
            qm.sendOk("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
            qm.dispose();
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.sendNextS("#face0#검은 마법사의 편린과 무기에 깃든 어둠의 흔적 ······ 조건이 갖춰졌군.\r\n자, 그럼 시작해 보도록 하지.", 37, 9063153);
    } else if (status == 1) {
        qm.gainItem(4037461, -1);
        qm.forceCompleteQuest();
        qm.getClient().setCustomKeyValue(35650, "clear", "0");
        qm.dispose();
    }
}
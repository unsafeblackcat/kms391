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
        qm.sendYesNo("검은 마법사의 잔상을 격파하고 #i4036463# #b#z4036463# 1개#k를 구해다 주십시오. 그리고 군단장 #r하드 윌#k의 잔상을 격파하는 겁니다. 가능하시겠습니까?");
    } else if (status == 1) {
        var chat ="어둠의 흔적은 특수한 조건 하에서만 깃든다는 점을 이미 알고 계실 겁니다. 아래의 조건을 만족하여 #r하드 윌#k을 격파하고 돌아와 주십시오.\r\n\r\n"
        chat+="#r#e<임무 수행 조건>#k#n\r\n"
        chat+="#b- 전원 '거미의 왕 윌의 흔적'을 진행 중이고 윌 격파 조건을 완료하지 않은 2인 이하의 파티로 격파\r\n"
        chat+="#b- 적에게 피격 시 받는 데미지 10% 증가\r\n"
        chat+="#b- 2인 파티 시 최종 데미지 50% 감소, 1명이라도 실패할 경우 모든 파티원 함께 실패\r\n\r\n"
        chat+="#r※ 해당 퀘스트가 수행 중인 상태로 보스 입장 시도 시 임무 수행 조건에 맞는 보스 스테이지에 입장할 수 있습니다.\r\n"
        qm.sendOk(chat);
        startQuest();
        qm.dispose();
    }
}


function startQuest() {
    MapleQuest.getInstance(35663).forceStart(qm.getPlayer(), 0, null);
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
        qm.forceCompleteQuest();
        qm.gainItem(4036463, -1);
        qm.getClient().setCustomKeyValue(35650, "clear", "0");
        qm.dispose();
    }
}
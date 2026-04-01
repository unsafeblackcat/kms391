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
        qm.sendYesNo("검은 마법사의 잔상을 격파하고 #i4037458# #b#z4037458# 1개#k를 구해다 주십시오. 그리고 군단장#r노멀 아카이럼#k의 잔상을 격파하는 겁니다. 가능하시겠습니까?");
    } else if (status == 1) {
        var chat ="어둠의 흔적은 특수한 조건 하에서만 깃든다는 점을 이미 알고 계실 겁니다. 아래의 조건을 만족하여 #r노멀 아카이럼#k을 격파하고 돌아와 주십시오.\r\n\r\n"
        chat+="#r#e<임무 수행 조건>#k#n\r\n"
        chat+="#b-혼자서격파\r\n"
        chat+="#b-봉인된 제네시스 무기와 보조무기만 장착\r\n"
        chat+="#b-최종 데미지 75% 감소\r\n"
        chat+="#b-착용 중인 장비의 순수 능력치만 적용\r\n\r\n"
        chat+="#r※ 해당 퀘스트가 수행 중인 상태로 보스 입장 시도 시 임무 수행 조건에 맞는 보스 스테이지에 입장할 수 있습니다.\r\n"
        qm.sendOk(chat);
        startQuest();
        qm.dispose();
    }
}


function startQuest() {
    MapleQuest.getInstance(35659).forceStart(qm.getPlayer(), 0, null);
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
        qm.sendNextS("#face0#돌아왔군, #b#h ##k. 내 조사가 확실하다면 ······.\r\n분명 무기에 깃든 어둠의 흔적이 반응할 걸세.", 37, 9063153);
    } else if (status == 1) {
         qm.sendNextS("#face0#······ 그럼 시작하도록 하지.\r\n검은 마법사의 편린의 마력을 추출해 무기에 전송해 보겠네.", 37, 9063153);
    } else if (status == 2) {
         qm.sendNextS("(검은 마법사의 마력이 무기에 깃들더니 순간 아카이럼의 기억 일부가 흘러들어 왔다.)", 38);
    } else if (status == 3) {
         qm.sendNextS("(일동은 잠시 당황했지만 잠시 후 마음을 가라 앉히고 차분히 상황을 판단하기 시작했다.)", 38);
    } else if (status == 4) {
         qm.sendNextS("#face0#데미안과의 일전으로 죽을 고비에서 살아난 아카이럼.\r\n그가 힘을 완전히 되찾기 위해선 지팡이가 필요했다 ······.", 37, 9063152);
    } else if (status == 5) {
         qm.sendNextS("#face0#그 지팡이는 아마도 ······.", 37, 9063152);
    } else if (status == 6) {
         qm.sendNextS("#face0#검은 마법사의 편린과 ······ 비슷한 성격의 것이겠지.", 37, 9063153);
    } else if (status == 7) {
         qm.sendNextS("#face0#군장장이 그로부터 받은 힘과 검은 마법사의 편린.\r\n그것이 반응했을 때 위력을 발휘한다고 하면 ······.", 37, 9063153);
    } else if (status == 8) {
        qm.sendNextS("#face0#이걸로 확실해진 것 같군.\r\n힘의 해방을 위해서는 그의 편린이 필요하다는 것이 말일세.", 37, 9063152);
    } else if (status == 9) {
        qm.sendNextS("#face0#그렇다면 ······ 군단장과 함께,\r\n계속해서 검은 마법사의 잔상을 격파해 나가야 하겠군요.", 37, 9063152);
    } else if (status == 10) {
	qm.getClient().setCustomKeyValue(35650, "clear", "0");
        qm.gainItem(4037458, -1);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
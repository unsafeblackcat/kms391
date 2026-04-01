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
        qm.sendNext("하인즈님의 말씀에 따르면, 군단장의 잔상을 쓰러뜨리는 것으로, 제네시스 무기에 어둠의 흔적을 깃들게 할 수 있을 거라더군요.");
    } else if(status == 1) {
        qm.sendNextPrev("아, 어둠의 흔적이란 군단장들이 부여 받은 검은 마법사의 힘입니다. 군단장의 잔상이 사라진 자리에 남는다고 하죠.");
    } else if (status == 2) {
        qm.sendNextPrev("하지만 그것은 단순히 쓰러뜨리는 것만이 아닌, 뭔가 까다로운 제약을 건 상황 하에서만 발생한다고 합니다.");
    } else if (status == 3) {
        qm.sendNextPrev("그러니 앞으로 제약을 건 상황 하에서 군당장의 잔상을 하나 둘 처치해나가야 한다는 말입니다.");
    } else if (status == 4) {
        qm.sendNextPrev("설명은 이정도로 끝내고 직접 그 어둠의 흔적을 발견하러 떠나보죠. 첫 행선지는 사자왕 반 레온의 성입니다.");
    } else if (status == 5) {
        qm.sendYesNo("검은 마법사가 남긴 어둠의 흔적을 찾아내기 위해 군단장#r하드 반 레온#k의 잔상을 격파하는 겁니다. 가능하시겠습니까?");
    } else if (status == 6) {
        var chat ="자 그럼, 아래의 조건을 만족하여 #r하드 반 레온#k을 격파하고 돌아와 주십시오.\r\n\r\n"
        chat+="#r#e<임무 수행 조건>#k#n\r\n"
        chat+="#b-혼자서격파\r\n"
        chat+="#b-봉인된 제네시스 무기와 보조무기만 장착\r\n"
        chat+="#b-최종 데미지 90% 감소\r\n"
        chat+="#b-착용 중인 장비의 순수 능력치만 적용\r\n\r\n"
        chat+="   #r※ 해당 퀘스트가 수행 중인 상태로 보스 입장 시도 시 임무 수행 조건에 맞는 보스 스테이지에 입장할 수 있습니다.\r\n"
        qm.sendOk(chat);
	qm.getClient().setCustomKeyValue(35650, "clear", "0");
        startQuest();
        qm.dispose();
    }
}


function startQuest() {
    MapleQuest.getInstance(35658).forceStart(qm.getPlayer(), 0, null);
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
        qm.sendNextS("#face0#왔는가. #b#h ##k. 어둠의 흔적을 깃들게 하는 데 성공한 모양이로군.\r\n그럼. 어디 무기의 상태를 확인해 보도록 하지.", 36, 9063153);
    } else if (status == 1) {
         qm.sendNextS("#face0#(하인즈에게 봉인된 제네시스 무기를 건넸다. 그는 진지한 표정으로 무기를 찬찬히 살펴보았다.)", 36, 9063153);
    } else if (status == 2) {
         qm.sendNextS("#face0#어둠의 흔적이 깃든 것은 확실한 듯 하네만, 아직 어딘가 부족한 것 같네.\r\n다른 무언가가 더 필요할 것 같군.", 36, 9063153);
    } else if (status == 3) {
         qm.sendNextS("#face0#다른 무언가라뇨?", 36, 9063152);
    } else if (status == 4) {
         qm.sendNextS("#face0#아직 확실하지는 않네만 ······ 무기의 힘을 해방하기 위해서는.\r\n역시 ······ 검은 마법사의 일부가 필요하지도 모르겠네.", 36, 9063153);
    } else if (status == 5) {
        qm.sendNextS("#face0#그렇다면 검은 마법사의 잔상을 ······ 다시 ······.", 36, 9063152);
    } else if (status == 6) {
	qm.getClient().setCustomKeyValue(35650, "clear", "0");
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
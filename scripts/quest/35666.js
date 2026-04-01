importPackage(Packages.server.quest);

status = -1;
function start(mode, type, S) {

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
        var count = 0;
        var ii = Packages.server.MapleItemInformationProvider.getInstance();
        var chat ="#e<제네시스 무기>#n\r\n"
        chat +="자네가 가진 제네시스 무기가 강력한 힘으로 가득 찼소.\r\n제네시스 무기에 잠재된 첫 번째 힘을 깨울 수 있을 것 같네만, 해방을 시작해 보겠나?\r\n\r\n"
        chat +="#r- <파괴의 얄다바오트> 스킬 획득\r\n"
        chat +="#r- 주문서/스타포스 강화 불가\r\n"
        chat +="#r- 추가옵션/소울은 완전 해방 시 초기화\r\n"
        for (i = 0; i < qm.getInventory(1).getSlotLimit(); i++) {
            if (qm.getInventory(1).getItem(i) != null) {
                if (ii.getName(qm.getInventory(1).getItem(i).getItemId()).contains("봉인된 제네시스") == true) {
                    chat += "#L" + i + "##e#b#i" + qm.getInventory(1).getItem(i).getItemId() + "# #z" + qm.getInventory(1).getItem(i).getItemId() + "##l\r\n";
                    count++;
                }
            }
        }
        if (count <= 0) {
            qm.sendOk("해방하실 제네시스 장비를 소지하고 있는지 확인해 주세요.");
            qm.dispose();
            return;
        }
        qm.sendSimpleS(chat, 4, 9063151);
    } else if (status == 1) {
        item = qm.getInventory(1).getItem(S);
        var chat ="첫 번째 힘이 깨어나 제네시스 무기가 더 강력해졌다네.\r\n무기를 착용하고 그 힘을 시험해 보게나."
        qm.sendOkS(chat, 4, 9063151);
        startQuest();
        qm.getPlayer().getInventory(1).removeSlot(item.getPosition());
        qm.gainItem(item.getItemId()+1, 1);
        qm.dispose();
    }
}


function startQuest() {
    MapleQuest.getInstance(35666).forceStart(qm.getPlayer(), 0, null);
    qm.forceStartQuest(35666);
    qm.teachSkill(80002632, 1, 1);
    qm.dispose();
}
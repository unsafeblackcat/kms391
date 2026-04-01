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
        chat +="#r- 올스탯 + 300 / 공|마 + 1200\r\n"
        chat +="#r- 보스데미지 + 50% / 몬스터 방어율 무시 +30%\r\n"
        chat +="#r- 스타포스 25성\r\n"
        chat +="#r- 유니크 잠재능력 보유\r\n"
        chat +="#r- 에픽 에디셔널 잠재능력 보유\r\n"
        chat +="#r- <창조의 아이온> 스킬 획득\r\n"
        chat +="#r- 주문서/스타포스 강화 불가\r\n"
        chat +="#r- 추가옵션/소울은 완전 해방 시 초기화\r\n"
        for (i = 0; i < qm.getInventory(1).getSlotLimit(); i++) {
            if (qm.getInventory(1).getItem(i) != null) {
                if (ii.getName(qm.getInventory(1).getItem(i).getItemId()).contains("제네시스") == true) {
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
        var chat ="힘이 완전히 깨어나 제네시스 무기가 더 강력해졌다네.\r\n무기를 착용하고 그 힘을 시험해 보게나."
        qm.sendOkS(chat, 4, 9063151);
        startQuest();
        GenesisWeaponclear(item);
        qm.dispose();
    }
}


function startQuest() {
    MapleQuest.getInstance(35667).forceStart(qm.getPlayer(), 0, null);
    qm.forceStartQuest(35667);
    qm.teachSkill(80002633, 1, 1);
    qm.dispose();
}

function GenesisWeaponclear(item) {
    item.setStr(300);
    item.setDex(300);
    item.setInt(300);
    item.setLuk(300);
    item.setWatk(1200);
    item.setMatk(1200);
    item.setBossDamage(50);
    item.setIgnorePDR(30);
    item.setUpgradeSlots(0);
    item.setEnhance(25);
    item.setState(19);
    item.setPotential1(30070);
    item.setPotential2(30070);
    item.setPotential3(30070);
	item.setPotential4(22010);
	item.setPotential5(22011);
	item.setPotential6(22012);
    qm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.EQUIP);
    qm.getPlayer().changeSingleSkillLevel(Packages.client.SkillFactory.getSkill(80002632), 1, 1);
	qm.getPlayer().changeSingleSkillLevel(Packages.client.SkillFactory.getSkill(80002633), 1, 1);
}
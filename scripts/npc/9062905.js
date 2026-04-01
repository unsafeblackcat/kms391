var status;

var inventoryType;
var inventory;
var selItem;
var selLine;
var selPotentialIndex;
var priceRate;

var potentials = [
    [40041, "STR : + 12%", 50],
    [40042, "DEX : + 12%", 50],
    [40043, "INT : + 12%", 50],
    [40044, "LUK : + 12%", 50],
    [40086, "올스텟 : + 9%", 50],
    [40045, "최대 HP : + 12%", 50],
    [40046, "최대 MP : + 12%", 50],
    [40292, "몬스터 방어율 무시 : + 40%", 100],
    [40603, "보스 몬스터 공격 시 데미지 : + 40%", 100],
    [40055, "크리티컬 확률 : + 12%", 100],
    [40070, "데미지 : + 12%", 100],
    [40051, "공격력 : + 12%", 100],
    [40052, "마력 : + 12%", 100],
    [40057, "크리티컬 데미지 : + 8%", 100],
    [40650, "메소 획득량 : + 20%", 100],
    [40656, "아이템 드롭률 : + 20%", 100],
    [40556, "모든 스킬의 재사용 대기시간 : -1초", 50], // 에디셔널 조건부에 걸려 2배 적용 받아서 총 100개가 됨.
    [40557, "모든 스킬의 재사용 대기시간 : -2초", 100] // 에디셔널 조건부에 걸려 2배 적용 받아서 총 200개가 됨.
];

var useItem = 4031013;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    } else {
        status++;
    }

    if (status == 0) {
    	var chat = "#fs15##b#z" + useItem + "##k만 있다면 잠재능력을 변경 해드립니다!\r\n\r\n어떤 #d장비#k의 #d잠재능력#k을 변경하고 싶으신가요 ?\r\n\r\n";
    	chat += "#L0##b장비 아이템#k#l\r\n";
    	//chat += "#L1##b캐시 아이템#k#l";
    	cm.sendSimple(chat);
    } else if (status == 1) {
    	if (inventoryType == null) {
    		inventoryType = (selection == 0 ? Packages.client.inventory.MapleInventoryType.EQUIP : Packages.client.inventory.MapleInventoryType.DECORATION);
    	}

        inventory = cm.getPlayer().getInventory(inventoryType);

        var chat = "#fs15#어떤 #d아이템#k의 #d잠재능력#k을 변경하고 싶으신가요 ?\r\n\r\n#r주의 : 잠재능력 수치는 아이템 레벨에 따라 변동될 수 있습니다.#k\r\n\r\n";

        var length = inventory.getSlotLimit();
        for (var i = 0; i < length; i++) {
            var item = inventory.getItem(i);

            if (item != null) {
                if (!Packages.constants.GameConstants.isArcaneSymbol(item.getItemId()) && !Packages.constants.GameConstants.isAuthenticSymbol(item.getItemId())) {
                	if (selection == 0) {
                		if (!Packages.server.MapleItemInformationProvider.getInstance().isCash(item.getItemId())) {
                			chat += "#L" + i + "##i" + item.getItemId() + "# #b#z" + item.getItemId() + "##k#l\r\n";
                		}
                	} else {
                		if (Packages.server.MapleItemInformationProvider.getInstance().isCash(item.getItemId())) {
                			chat += "#L" + i + "##i" + item.getItemId() + "# #b#z" + item.getItemId() + "##k#l\r\n";
                		}
                	}
                }
            }
        }

        cm.sendSimple(chat);
    } else if (status == 2) {
        if (selItem == null) {
            selItem = inventory.getItem(selection);
        }

        if (selItem.getState() == 0) {
            cm.sendOk("#fs15#해당 아이템은 잠재능력이 없습니다.\r\n잠재능력 변경을 원하신다면, 잠재능력을 먼저 달아주세요.");
            cm.dispose();
            return;
        }

        if (selItem.getState() == 4) {
            cm.sendOk("#fs15#해당 아이템은 잠재능력이 미개방 상태입니다.\r\n잠재능력 변경을 원하신다면, 잠재능력을 먼저 개방해주세요.");
            cm.dispose();
            return;
        }

        var chat = "#fs15##r[선택된 아이템]#k\r\n#i" + selItem.getItemId() + "# #b#z" + selItem.getItemId() + "##k\r\n\r\n몇번째 #d잠재능력#k을 변경하고 싶으신가요 ?\r\n\r\n";
        chat += "#r주의 : 4, 5, 6번째 잠재능력은 가격이 3배로 적용됩니다.#k\r\n\r\n";
        chat += "#L0##i3801310# #b1번째 잠재능력#k#l         #L3##i3801310# #b4번째 잠재능력#k#l\r\n";
        chat += "#L1##i3801310# #b2번째 잠재능력#k#l         #L4##i3801310# #b5번째 잠재능력#k#l\r\n";
        chat += "#L2##i3801310# #b3번째 잠재능력#k#l         #L5##i3801310# #b6번째 잠재능력#k#l";
        cm.sendSimple(chat);
    } else if (status == 3) {
        selLine = (selection + 1);
        priceRate = (selLine > 2 ? 2 : 1);

        var chat = "#fs15##r[선택된 아이템]#k\r\n#i" + selItem.getItemId() + "# #b#z" + selItem.getItemId() + "##k (" + selLine + "번째 잠재능력 변경)\r\n\r\n어떤 #d잠재능력#k을 부여하고 싶으신가요 ?\r\n\r\n";

        var length = potentials.length;
        for (var i = 0; i < length; i++) {
            if (selItem != null) {
                if ((Packages.constants.GameConstants.isWeapon(selItem.getItemId()) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40045) { // 방어구, 장신구 외 최대 HP 예외 처리
                    continue;
                }

                if ((Packages.constants.GameConstants.isWeapon(selItem.getItemId()) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40046) { // 방어구, 장신구 외 최대 MP 예외 처리
                    continue;
                }

                if (((!Packages.constants.GameConstants.isWeapon(selItem.getItemId()) && (selItem.getItemId() % 1670000 >= 10000 || selItem.getItemId() / 1670000 < 1)) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40292) { // 무기, 하트 외 몬스터 방어율 무시 예외 처리
                    continue;
                }

                if (((!Packages.constants.GameConstants.isWeapon(selItem.getItemId()) && (selItem.getItemId() % 1670000 >= 10000 || selItem.getItemId() / 1670000 < 1)) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40055) { // 무기, 하트 외 크리티컬 확률 예외 처리
                    continue;
                }

                if (((!Packages.constants.GameConstants.isWeapon(selItem.getItemId()) && (selItem.getItemId() % 1190000 >= 10000 || selItem.getItemId() / 1190000 < 1) && (selItem.getItemId() % 1090000 >= 10000 || selItem.getItemId() / 1090000 < 1) && (selItem.getItemId() % 1670000 >= 10000 || selItem.getItemId() / 1670000 < 1)) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40292) { // 무기, 엠블렘, 하트 외 몬스터 방어율 무시 예외 처리
                    continue;
                }

                if (((!Packages.constants.GameConstants.isWeapon(selItem.getItemId()) && (selItem.getItemId() % 1190000 >= 10000 || selItem.getItemId() / 1190000 < 1) && (selItem.getItemId() % 1090000 >= 10000 || selItem.getItemId() / 1090000 < 1) && (selItem.getItemId() % 1670000 >= 10000 || selItem.getItemId() / 1670000 < 1)) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40603) { // 무기, 엠블렘, 하트 외 보스 몬스터 공격 시 데미지 예외 처리
                    continue;
                }

                if (((!Packages.constants.GameConstants.isWeapon(selItem.getItemId()) && (selItem.getItemId() % 1190000 >= 10000 || selItem.getItemId() / 1190000 < 1) && (selItem.getItemId() % 1670000 >= 10000 || selItem.getItemId() / 1670000 < 1)) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40070) { // 무기, 엠블렘, 하트 외 데미지 예외 처리
                    continue;
                }

                if (((!Packages.constants.GameConstants.isWeapon(selItem.getItemId()) && (selItem.getItemId() % 1190000 >= 10000 || selItem.getItemId() / 1190000 < 1) && (selItem.getItemId() % 1090000 >= 10000 || selItem.getItemId() / 1090000 < 1) && (selItem.getItemId() % 1670000 >= 10000 || selItem.getItemId() / 1670000 < 1)) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40051) { // 무기, 엠블렘, 하트 외 공격력 예외 처리
                    continue;
                }

                if (((!Packages.constants.GameConstants.isWeapon(selItem.getItemId()) && (selItem.getItemId() % 1190000 >= 10000 || selItem.getItemId() / 1190000 < 1) && (selItem.getItemId() % 1090000 >= 10000 || selItem.getItemId() / 1090000 < 1) && (selItem.getItemId() % 1670000 >= 10000 || selItem.getItemId() / 1670000 < 1)) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40052) { // 무기, 엠블렘, 하트 외 마력 예외 처리
                    continue;
                }

                if (((selItem.getItemId() % 1080000 >= 10000 || selItem.getItemId() / 1080000 < 1) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40057) { // 장갑 외 크리티컬 데미지 예외 처리
                    continue;
                }

                if (((selItem.getItemId() % 1010000 >= 10000 || selItem.getItemId() / 1010000 < 1) && (selItem.getItemId() % 1020000 >= 10000 || selItem.getItemId() / 1020000 < 1) && (selItem.getItemId() % 1030000 >= 10000 || selItem.getItemId() / 1030000 < 1) && (selItem.getItemId() % 1110000 >= 10000 || selItem.getItemId() / 1110000 < 1) && (selItem.getItemId() % 1120000 >= 10000 || selItem.getItemId() / 1120000 < 1) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40650) { // 얼굴장식, 눈장식, 귀고리, 반지, 펜던트 외 메소 획득량 예외 처리
                    continue;
                }

                if (((selItem.getItemId() % 1010000 >= 10000 || selItem.getItemId() / 1010000 < 1) && (selItem.getItemId() % 1020000 >= 10000 || selItem.getItemId() / 1020000 < 1) && (selItem.getItemId() % 1030000 >= 10000 || selItem.getItemId() / 1030000 < 1) && (selItem.getItemId() % 1110000 >= 10000 || selItem.getItemId() / 1110000 < 1) && (selItem.getItemId() % 1120000 >= 10000 || selItem.getItemId() / 1120000 < 1) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40656) { // 얼굴장식, 눈장식, 귀고리, 반지, 펜던트 외 아이템 드롭률 예외 처리
                    continue;
                }

                if (((selItem.getItemId() % 1000000 >= 10000 || selItem.getItemId() / 1000000 < 1) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40556) { // 모자 외 재사용 대기시간 감소 -1초 예외 처리
                    continue;
                }
                
                if (((selItem.getItemId() % 1000000 >= 10000 || selItem.getItemId() / 1000000 < 1) || Packages.server.MapleItemInformationProvider.getInstance().isCash(selItem.getItemId())) && potentials[i][0] == 40557) { // 모자 외 재사용 대기시간 감소 -2초 예외 처리
                    continue;
                }
				
                chat += "#L" + i + "##i3801310# #b" + potentials[i][1] + "#k (#i" + useItem + "# " + (potentials[i][2] * priceRate) + "개 소모)#l\r\n";
            }
        }

        cm.sendSimple(chat);
    } else if (status == 4) {
        selPotentialIndex = selection;

        cm.sendYesNo("#fs15##r[선택된 아이템]#k\r\n#i" + selItem.getItemId() + "# #b#z" + selItem.getItemId() + "##k (" + selLine + "번째 잠재능력 변경)\r\n\r\n#d[필요한 아이템]#k\r\n#i" + useItem + "# #b#z" + useItem + "##k " + (potentials[selPotentialIndex][2] * priceRate) + "개\r\n\r\n#d" + potentials[selPotentialIndex][1] + "#k\r\n잠재능력을 부여 하시겠습니까 ?");
    } else if (status == 5) {
        var price = (potentials[selPotentialIndex][2] * priceRate);

        if (!cm.haveItem(useItem, price)) {
            cm.sendOk("#fs15##i" + useItem + "# #b#z" + useItem + "##k 이 부족합니다.");
            cm.dispose();
            return;
        }

        cm.gainItem(useItem, -price);

        selItem.setState(20);

        switch (selLine) {
            case 1:
                selItem.setPotential1(potentials[selPotentialIndex][0]);
                break;
            case 2:
                selItem.setPotential2(potentials[selPotentialIndex][0]);
                break;
            case 3:
                selItem.setPotential3(potentials[selPotentialIndex][0]);
                break;
            case 4:
                selItem.setPotential4(potentials[selPotentialIndex][0]);
                break;
            case 5:
                selItem.setPotential5(potentials[selPotentialIndex][0]);
                break;
            case 6:
                selItem.setPotential6(potentials[selPotentialIndex][0]);
                break;
            default:
        }

        cm.getPlayer().forceReAddItem(selItem, inventoryType);

        cm.sendYesNo("#fs15#잠재능력 변경에 성공 하였습니다.\r\n잠재능력을 더 변경 하시겠습니까 ?");
    } else if (status == 6) {
        status = 1;

        action(mode, type, selection);
    }
}
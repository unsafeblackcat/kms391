/**
 * @projectDescription 큐브 스크립트
 *
 * @author vent
 * @version 1.0
 * @sdoc scripts/npc
 */

importPackage(java.util);
importPackage(Packages.client.items);
importPackage(Packages.client);
importPackage(Packages.server.items);
importPackage(Packages.constants);
importPackage(Packages.launch);
importPackage(Packages.packet.creators);
importPackage(Packages.handler.channel);
importPackage(Packages.server.items);
importPackage(Packages.client.inventory);

var Randomizer = Packages.server.Randomizer;
var ii = Packages.server.MapleItemInformationProvider.getInstance();
var InventoryHandler = Packages.handling.channel.handler.InventoryHandler;

var CubeStatus = {};

CubeStatus.UNDIFINED = -1;
CubeStatus.BLACK_CUBE = 0;
CubeStatus.ADDITIONAL_CUBE = 1;
CubeStatus.RED_CUBE = 2;
CubeStatus.MASTERCRAFT_CUBE = 3;

var Poten_strs = {30041: "힘 : +9%", 30042: "덱스 : +9%", 30043: "인트 : +9%", 30044: "럭 : +9%", 30086: "올스텟 : +6%", 30045: "최대체력 : +9%", 30070: "데미지 : +9%", 30051: "공격력 : +9%", 30052: "마력 : +9%", 30291: "몬스터 방어율 무시 : +30%", 30601: "보스 몬스터 공격 시 데미지 : +20%", 30602: "보스 몬스터 공격 시 데미지 : +30%",
    40041: "힘 : +12%", 40042: "덱스 : +12%", 40043: "인트 : +12%", 40044: "럭 : +12%", 40047: "덱스 : +12%", 40048: "럭 : +12%", 40086: "올스텟 : +9%", 40045: "최대체력 : +12%", 40070: "데미지 : +12%", 40051: "공격력 : +12%", 40052: "마력 : +12%", 40291: "몬스터 방어율 무시 : +35%", 40292: "몬스터 방어율 무시 : +40%", 40601: "보스 몬스터 공격 시 데미지 : +25%", 40602: "보스 몬스터 공격 시 데미지 : +35%", 40603: "보스 몬스터 공격 시 데미지 : +40%",
    40056: "크리티컬 데미지 : 8%", 40057: "크리티컬 데미지 : 8%",
    40650: "메소 획득량 증가 : +20%", 40656: "아이템 획득 확률 증가 : +20%",
    40556: "모든 스킬의 재사용 대기시간 : -1초", 40557: "모든 스킬의 재사용 대기시간 : -2초",

    32041: "힘 : +5%", 32042: "덱스 : +5%", 32043: "인트 : +5%", 32044: "럭 : +5%", 32086: "올스텟 : +4%", 32045: "최대체력 : +9%", 32070: "데미지 : +9%", 32051: "공격력 : +9%", 32052: "마력 : +9%", 42041: "힘 : +7%", 42042: "덱스 : +7%", 42043: "인트 : +7%", 42044: "럭 : +7%", 42086: "올스텟 : +5%", 42045: "최대체력 : +12%", 42070: "데미지 : +12%", 42051: "공격력 : +12%", 42052: "마력 : +12%",
    32091: "캐릭터 기준 10레벨 당 STR : +1", 32092: "캐릭터 기준 10레벨 당 DEX : +1", 32093: "캐릭터 기준 10레벨 당 INT : +1", 32094: "캐릭터 기준 10레벨 당 LUK : +1",
    42091: "캐릭터 기준 10레벨 당 STR : +2", 42092: "캐릭터 기준 10레벨 당 DEX : +2", 42093: "캐릭터 기준 10레벨 당 INT : +2", 42094: "캐릭터 기준 10레벨 당 LUK : +2", 42095: "캐릭터 기준 10레벨 당 공격력 : +1", 42096: "캐릭터 기준 10레벨 당 마력 : +1",
    42556: "모든 스킬의 재사용 대기시간 : -1초"};

var cube_cost = 2;
var edi_cube_id = 5062002;
var nom_cube_id = 5062002;

var status = -1;
var cubetype = -1;

var chat;
var Snumb;
var cubeItemList;
var selectedItem;

var enter = "\r\n";

var cubeState = CubeStatus.UNDIFINED;

var PotentialStruct = function (type, value) {
    this.type = type;
    this.value = value;
};

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }

    chat = "#fs11#";

    if (status == 0) {
        chat += "특별한 큐브를 이용해 원하는 잠재능력 옵션 1개를 선택하여 랜덤하게 바꿔주고 있네. 단 ! 최소 4개이상 소지시 이용해주길 바라네. 이하 사용시 큐브가 사라지면 복구가 불가능하네." + enter;
        chat += "#L2##i" + nom_cube_id + "##z" + nom_cube_id + "# " + cube_cost + "개로 일반 잠재능력 교체#l" + enter;
        chat += "#L1##i" + edi_cube_id + "##z" + edi_cube_id + "# " + cube_cost + "개로 에디 잠재능력 교체#l";
        cm.sendSimple(chat);

    } else if (status == 1) {
        var composer;

        cubetype = selection;
        cubeState = selection;

        if (!checkCost(cubeState)) {
            chat += "큐브가 없습니다";
            cm.dispose();
        } else {
            switch (selection) {
                case CubeStatus.RED_CUBE:
                    composer = function (_item) {
                        var requireLevel = ii.getReqLevel(_item.getItemId());
                        if (_item.getState() == 20) {
                            return true;
                        } else {
                            return false;
                        }
                    };
                    break;
                case CubeStatus.ADDITIONAL_CUBE:
                    composer = function (_item) {
                        var level = Math.floor(_item.getPotential4() >= 10000 ? _item.getPotential4() / 10000 : _item.getPotential4() / 100);
                        if (_item.getPotential4() > 0 && level == 4) {
                            return true;
                        } else {
                            return false;
                        }
                    };
                    break;
                default:
                    cubeState = CubeStatus.UNDIFINED;
                    break;
            }
            cubeItemList = getEquipItemList(composer);
            if (cubeItemList.length > 0) {
                chat += "큐브를 돌리실 아이템을 선택해주세요!" + enter + enter;
                chat += getItemCatalog(cubeItemList);
            } else {
                chat += "큐브를 돌릴 수 있는 아이템이 없습니다";
                cm.dispose();
            }
        }
        cm.sendOk(chat);

    } else if (status == 2) {
        if (cubeState == CubeStatus.UNDIFINED) {
            cm.dispose();
            return;
        }

        if (selection == -1) {
            cm.sendOk("조건에 맞는 아이템이 없습니다");
            cm.dispose();
        }

        if (!checkCost(cubeState)) {
            chat = "큐브가 없습니다";
            cm.sendOk(chat);
            cm.dispose();
        } else {
            chat = "아래에서 교체를 원하는 잠재능력을 선택하여 주십시오." + enter;

            if (selectedItem == null) {
                selectedItem = cubeItemList[selection];
            }

            chat += "선택 된 아이템 : #b#i" + selectedItem.getItemId() + "##z" + selectedItem.getItemId() + "##k" + enter + enter;

            if (cubetype == 2) {
                if (selectedItem.getPotential1() > 10000) {
                    chat += "#L101#1번 잠재능력 : " + Poten_strs[selectedItem.getPotential1()] + "#l" + enter;
                }
                if (selectedItem.getPotential2() > 10000) {
                    chat += "#L102#2번 잠재능력 : " + Poten_strs[selectedItem.getPotential2()] + "#l" + enter;
                }
                if (selectedItem.getPotential3() > 10000) {
                    chat += "#L103#3번 잠재능력 : " + Poten_strs[selectedItem.getPotential3()] + "#l" + enter;
                }
            } else {
                if (selectedItem.getPotential4() > 10000) {
                    chat += "#L201#1번 에디셔널 : " + Poten_strs[selectedItem.getPotential4()] + "#l" + enter;
                }
                if (selectedItem.getPotential5() > 10000) {
                    chat += "#L202#2번 에디셔널 : " + Poten_strs[selectedItem.getPotential5()] + "#l" + enter;
                }
                if (selectedItem.getPotential6() > 10000) {
                    chat += "#L203#3번 에디셔널 : " + Poten_strs[selectedItem.getPotential6()] + "#l" + enter;
                }
            }
            cm.sendSimple(chat);
        }

    } else if (status == 3) {
        if (!checkCost(cubeState)) {
            chat = "큐브가 없습니다2";
            cm.sendOk(chat);
            cm.dispose();
            return;
        }

        decreaseCost(cubeState);

        Snumb = selection;

        var Poten_N_datas_Common = [40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057];
        var Poten_E_datas_Common = [40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057];
        var Poten_N_datas_Common = [40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057];
        var Poten_E_datas_Common = [40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057];
        var _itemId = selectedItem.getItemId();
        if (GameConstants.isWeapon(_itemId) || GameConstants.isSecondaryWeapon(_itemId)) {
            Poten_N_list = Poten_N_datas_Common.concat(40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057);
            Poten_E_list = Poten_E_datas_Common.concat(40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057);
        } else if (Math.floor(_itemId / 1000) == 1190) {
            Poten_N_list = Poten_N_datas_Common.concat(40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057);
            Poten_E_list = Poten_E_datas_Common.concat(40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057);
        } else if (GameConstants.isAccessory(_itemId)) {
            Poten_N_list = Poten_N_datas_Common.concat(40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057);
            Poten_E_list = Poten_E_datas_Common.concat(40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057);
        } else if (GameConstants.isGlove(_itemId)) {
            Poten_N_list = Poten_N_datas_Common.concat(40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057);
            Poten_E_list = Poten_E_datas_Common.concat(40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057);
        } else if (GameConstants.isCap(_itemId)) {
            Poten_N_list = Poten_N_datas_Common.concat(40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057);
            Poten_E_list = Poten_E_datas_Common.concat(40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057);
        } else {
            Poten_N_list = Poten_N_datas_Common;
            (40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057);
            Poten_E_list = Poten_E_datas_Common;
            (40041, 40042, 40043, 40044, 40086, 40045, 40070, 40051, 40052, 40292, 40603, 40056, 40650, 40656, 40557, 40057);
        }

        var newPoten = cubetype == 2 ? Poten_N_list[Randomizer.nextInt(Poten_N_list.length)] : Poten_E_list[Randomizer.nextInt(Poten_E_list.length)];
        switch (Snumb) {
            case 101:
                selectedItem.setPotential1(newPoten);
                break;
            case 102:
                selectedItem.setPotential2(newPoten);
                break;
            case 103:
                selectedItem.setPotential3(newPoten);
                break;
            case 201:
                selectedItem.setPotential4(newPoten);
                break;
            case 202:
                selectedItem.setPotential5(newPoten);
                break;
            case 203:
                selectedItem.setPotential6(newPoten);
                break;
        }

        chat = "선택 된 아이템 : #b#i" + selectedItem.getItemId() + "##z" + selectedItem.getItemId() + "##k" + enter;
        chat += "변경된 옵션은 다음과 같습니다." + enter + enter;

        cm.getPlayer().forceReAddItem(selectedItem, MapleInventoryType.EQUIP);

        if (cubetype == 2) {
            if (selectedItem.getPotential1() > 10000) {
                chat += (Snumb % 10 == 1 ? "#L0#" : "") + "1번 잠재능력 : " + Poten_strs[selectedItem.getPotential1()] + (Snumb % 10 == 1 ? "#l\r\n" : "") + enter;
            }
            if (selectedItem.getPotential2() > 10000) {
                chat += (Snumb % 10 == 2 ? "#L0#" : "") + "2번 잠재능력 : " + Poten_strs[selectedItem.getPotential2()] + (Snumb % 10 == 2 ? "#l\r\n" : "") + enter;
            }
            if (selectedItem.getPotential3() > 10000) {
                chat += (Snumb % 10 == 3 ? "#L0#" : "") + "3번 잠재능력 : " + Poten_strs[selectedItem.getPotential3()] + (Snumb % 10 == 3 ? "#l\r\n" : "") + enter;
            }
        } else {
            if (selectedItem.getPotential4() > 10000) {
                chat += (Snumb % 10 == 1 ? "#L0#" : "") + "1번 에디셔널 : " + Poten_strs[selectedItem.getPotential4()] + (Snumb % 10 == 1 ? "#l\r\n" : "") + enter;
            }
            if (selectedItem.getPotential5() > 10000) {
                chat += (Snumb % 10 == 2 ? "#L0#" : "") + "2번 에디셔널 : " + Poten_strs[selectedItem.getPotential5()] + (Snumb % 10 == 2 ? "#l\r\n" : "") + enter;
            }
            if (selectedItem.getPotential6() > 10000) {
                chat += (Snumb % 10 == 3 ? "#L0#" : "") + "3번 에디셔널 : " + Poten_strs[selectedItem.getPotential6()] + (Snumb % 10 == 3 ? "#l\r\n" : "") + enter;
            }
        }
        chat += enter + "#L0#잠재능력 재설정#l";
        chat += enter + "#L1#종료#l";
        cm.sendSimple(chat);

    } else if (status == 4) {
        if (selection == 0) {
            status = 2;
            action(1, 0, Snumb);
            return;
        } else if (selection == 1) {
            cm.getPlayer().forceReAddItem(selectedItem, MapleInventoryType.EQUIP);
            selectedItem = null;
            cm.dispose();
        } else {
            status = 2;
            action(1, 0, selection);
            return;
        }
    }
}

function getItemCatalog(itemList) {
    var _chat = "";
    for (var i = 0; i < itemList.length; i++) {
        var currentItem = itemList[i];
        _chat += "#L" + i + "#";
        _chat += "#v" + currentItem.getItemId() + "#";
        _chat += "#z" + currentItem.getItemId() + "#";
        _chat += enter;
    }
    return _chat;
}

function getEquipItemList(composer) {
    var itemList = new Array();
    var equipInventory = cm.getPlayer().getInventory(MapleInventoryType.EQUIP);
    for (var i = 0; i < equipInventory.getSlotLimit(); i++) {
        var currentItem = equipInventory.getItem(i);
        if (currentItem != null) {
            if (composer(currentItem)) {
                itemList.push(currentItem);
            }
        }
    }
    return itemList;
}

function getPotentialItemId(_cubestate) {
    switch (_cubestate) {
        case 1:
            return edi_cube_id;
        case 2:
            return nom_cube_id;
    }
}

function checkCost(_cubestate) {
    if (cm.haveItem(getPotentialItemId(_cubestate), cube_cost)) {
        return true;
    }
    return false;
}

function decreaseCost(_cubestate) {
    if (checkCost(_cubestate)) {
        cm.gainItem(getPotentialItemId(_cubestate), -cube_cost);
        return true;
    }
    return false;
}
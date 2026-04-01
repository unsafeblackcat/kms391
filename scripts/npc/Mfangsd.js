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

var Poten_strs =     
   {30041: "力量 : +9%", 30042: "敏捷 : +9%", 30043: "智力 : +9%", 30044: "幸運 : +9%", 
    30086: "全屬性 : +6%", 30045: "最大HP : +9%", 30070: "傷害 : +9%", 
    30051: "攻擊力 : +9%", 30052: "魔力 : +9%", 
    30291: "無視怪物防禦率 : +30%", 
    30601: "BOSS怪物傷害 : +20%", 30602: "BOSS怪物傷害 : +30%",
    
    40041: "力量 : +12%", 40042: "敏捷 : +12%", 40043: "智力 : +12%", 40044: "幸運 : +12%",
    40086: "全屬性 : +9%", 40045: "最大HP : +12%", 40070: "傷害 : +12%",
    40051: "攻擊力 : +12%", 40052: "魔力 : +12%",
    40291: "無視怪物防禦率 : +35%", 40292: "無視怪物防禦率 : +40%",
    40601: "BOSS怪物傷害 : +25%", 40602: "BOSS怪物傷害 : +35%", 40603: "BOSS怪物傷害 : +40%",
    
    40056: "爆擊傷害 : +8%", 40057: "爆擊傷害 : +8%",
    40650: "楓幣獲得量 : +20%", 40656: "道具掉落率 : +20%",
    40556: "所有技能冷卻時間 : -1秒", 40557: "所有技能冷卻時間 : -2秒"
    };

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

    chat = "#fs15#";

    if (status == 0) {
        chat += "可以使用特殊魔方隨機更換裝備的潛能屬性，每次需要消耗" + cube_cost + "個魔方。請注意：魔方使用後將消失且無法恢復！" + enter;
        chat += "#L2#使用" + cube_cost + "個#i" + nom_cube_id + "##z" + nom_cube_id + "# 更換一般潛能#l" + enter;
        chat += "#L1#使用" + cube_cost + "個#i" + edi_cube_id + "##z" + edi_cube_id + "# 更換附加潛能#l";
        cm.sendSimple(chat); 

    } else if (status == 1) {
        var composer;

        cubetype = selection;
        cubeState = selection;

        if (!checkCost(cubeState)) {
            chat += "您沒有足夠的魔方";
            cm.dispose();
        } else {
            switch (selection) {
                case CubeStatus.RED_CUBE:
                    composer = function (_item) {
                        var requireLevel = ii.getReqLevel(_item.getItemId());
                        if (_item.getState() == 20) {// 20代表傳說級裝備 
                            return true;
                        } else {
                            return false;
                        }
                    };
                    break;
                case CubeStatus.ADDITIONAL_CUBE:
                    composer = function (_item) {
                        var level = Math.floor(_item.getPotential4() >= 10000 ? _item.getPotential4() / 10000 : _item.getPotential4() / 100);
                        if (_item.getPotential4() > 0 && level == 4) {// 檢查附加潛能是否為傳說級
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
                chat += "請選擇要使用魔方的裝備：!" + enter + enter;
                chat += getItemCatalog(cubeItemList);
            } else {
                chat += "沒有符合條件的裝備";
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
            cm.sendOk("沒有符合條件的裝備");
            cm.dispose();
        }

        if (!checkCost(cubeState)) {
            chat = "您沒有足夠的魔方";
            cm.sendOk(chat);
            cm.dispose();
        } else {
            chat = "請選擇要更換的潛能屬性：" + enter;

            if (selectedItem == null) {
                selectedItem = cubeItemList[selection];
            }

            chat += "選擇的裝備：#b#i" + selectedItem.getItemId() + "##z" + selectedItem.getItemId() + "##k" + enter + enter;

            if (cubetype == 2) {
                if (selectedItem.getPotential1() > 10000) {
                    chat += "#L101#1第1潛能 : " + Poten_strs[selectedItem.getPotential1()] + "#l" + enter;
                }
                if (selectedItem.getPotential2() > 10000) {
                    chat += "#L102#2第2潛能 : " + Poten_strs[selectedItem.getPotential2()] + "#l" + enter;
                }
                if (selectedItem.getPotential3() > 10000) {
                    chat += "#L103#3第3潛能 : " + Poten_strs[selectedItem.getPotential3()] + "#l" + enter;
                }
            } else {
                if (selectedItem.getPotential4() > 10000) {
                    chat += "#L201#1第1附加潛能 : " + Poten_strs[selectedItem.getPotential4()] + "#l" + enter;
                }
                if (selectedItem.getPotential5() > 10000) {
                    chat += "#L202#2第2附加潛能 : " + Poten_strs[selectedItem.getPotential5()] + "#l" + enter;
                }
                if (selectedItem.getPotential6() > 10000) {
                    chat += "#L203#3第3附加潛能 : " + Poten_strs[selectedItem.getPotential6()] + "#l" + enter;
                }
            }
            cm.sendSimple(chat);
        }

    } else if (status == 3) {
        if (!checkCost(cubeState)) {
            chat = "魔方數量不足2";
            cm.sendOk(chat);
            cm.dispose();
            return;
        }

        decreaseCost(cubeState);// 扣除魔方數量 

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

        chat = "選擇的裝備 #b#i" + selectedItem.getItemId() + "##z" + selectedItem.getItemId() + "##k" + enter;
        chat += "變更後的潛能屬性如下：" + enter + enter;

        cm.getPlayer().forceReAddItem(selectedItem, MapleInventoryType.EQUIP);

        if (cubetype == 2) {
            if (selectedItem.getPotential1() > 10000) {
                chat += (Snumb % 10 == 1 ? "#L0#" : "") + "第1潛能 : " + Poten_strs[selectedItem.getPotential1()] + (Snumb % 10 == 1 ? "#l\r\n" : "") + enter;
            }
            if (selectedItem.getPotential2() > 10000) {
                chat += (Snumb % 10 == 2 ? "#L0#" : "") + "第2潛能 : " + Poten_strs[selectedItem.getPotential2()] + (Snumb % 10 == 2 ? "#l\r\n" : "") + enter;
            }
            if (selectedItem.getPotential3() > 10000) {
                chat += (Snumb % 10 == 3 ? "#L0#" : "") + "第3潛能 : " + Poten_strs[selectedItem.getPotential3()] + (Snumb % 10 == 3 ? "#l\r\n" : "") + enter;
            }
        } else {
            if (selectedItem.getPotential4() > 10000) {
                chat += (Snumb % 10 == 1 ? "#L0#" : "") + "第1附加潛能 : " + Poten_strs[selectedItem.getPotential4()] + (Snumb % 10 == 1 ? "#l\r\n" : "") + enter;
            }
            if (selectedItem.getPotential5() > 10000) {
                chat += (Snumb % 10 == 2 ? "#L0#" : "") + "第2附加潛能 : " + Poten_strs[selectedItem.getPotential5()] + (Snumb % 10 == 2 ? "#l\r\n" : "") + enter;
            }
            if (selectedItem.getPotential6() > 10000) {
                chat += (Snumb % 10 == 3 ? "#L0#" : "") + "第3附加潛能 : " + Poten_strs[selectedItem.getPotential6()] + (Snumb % 10 == 3 ? "#l\r\n" : "") + enter;
            }
        }
        chat += enter + "#L0#重新設定潛能#l";
        chat += enter + "#L1#結束#l";
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
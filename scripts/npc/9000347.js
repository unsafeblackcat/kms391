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
var ii = Packages.server.MapleItemInformationProvider.getInstance();
var InventoryHandler = Packages.handling.channel.handler.InventoryHandler;
var Randomizer = Packages.server.Randomizer;

var CubeStatus = {};
CubeStatus.UNDIFINED = -1;
CubeStatus.BLACK_CUBE = 0;
CubeStatus.ADDITIONAL_CUBE = 1;
CubeStatus.RED_CUBE = 2;
CubeStatus.MASTERCRAFT_CUBE = 3;


var Potential = {};
Potential.NONE = 0;
Potential.STR_P = 1;
Potential.DEX_P = 2;
Potential.INT_P = 3;
Potential.LUK_P = 4;
Potential.ALL_P = 5;
Potential.IGNORE_DEF = 6; //이거 핸들링 안돼고있음
Potential.WATTACK_P = 7;
Potential.MATTACK_P = 8;
Potential.DAM_P = 9;
Potential.INC_BOSSDAM_P = 10;
Potential.INC_CRIT = 11;
Potential.INC_CRIT_DAM = 12; //MIN MAX 같이 계산할 것
Potential.REDUCE_COOLTIME = 13;
Potential.INC_REWARD_PROP = 14;
Potential.INC_MESO_PROP = 15;
Potential.HP_P = 16;
Potential.MP_P = 17;
Potential.INC_SKILLLV = 18;

var KEEP_LEVEL_PERCENT = 3; //옵이탈
var enter = "\r\n";
var talkType = 0x86;

var status = -1;
var chat;
var cubeItemList;
var selectedItem;
var newItem;

var cubeState = CubeStatus.UNDIFINED;

var PotentialStruct = function (type, value) {
    this.type = type;
    this.value = value;
};

var potentialMap = createPotentialMap();

function start() {
    /*
      if(!cm.getPlayer().isGM())
      {
         cm.dispose()
         cm.sendOk('버그잡는중임 ㅇㅇ.')
      }
      */

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
        chat += "#fs20#快速附加魔方功能，使用魔方有以下規則！" + enter;

        chat += "1.所有魔方只能用於等級限制100以上的傳說級選項物品." + enter;
        chat += "2.選擇<Before>選項可以保留當前選項重新旋轉魔方," + enter;
        chat += "選擇<After>選項則會將物品選項變更為選擇的選項." + enter;;
        chat += "#L" + CubeStatus.ADDITIONAL_CUBE + "# 附加魔方" + enter;
        cm.sendSimpleS(chat, talkType);
    } else if (status == 1) {
        var composer;
        cubeState = selection;

        if (!checkCost(cubeState)) {
            chat += "沒有魔方";
            cm.dispose();
        } else {
            switch (selection) {
                case CubeStatus.BLACK_CUBE:
                case CubeStatus.RED_CUBE:
                case CubeStatus.MASTERCRAFT_CUBE:
                    composer = function (_item) {
                        var requireLevel = ii.getReqLevel(_item.getItemId());
                        if (_item.getState() == 20 && requireLevel >= 100) {
                            return true;
                        } else {
                            return false;
                        }
                    };
                    break;

                case CubeStatus.ADDITIONAL_CUBE:
                    composer = function (_item) {
                        var requireLevel = ii.getReqLevel(_item.getItemId());
                        var level = Math.floor(
                            _item.getPotential4() >= 10000 ?
                                _item.getPotential4() / 10000 :
                                _item.getPotential4() / 100
                        );
                        if (
                            _item.getPotential4() > 0 &&
                            level == 4 &&
                            requireLevel >= 100
                        ) {
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
                chat += "#fs20#請選擇要旋轉魔方的物品！" + enter + enter;
                chat += getItemCatalog(cubeItemList);
                cm.sendSimpleS(chat, talkType);
            } else {
                chat += "#fs20#沒有可以旋轉魔方的物品";
                cm.sendSimpleS(chat, talkType);
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
            cm.sendSimple("沒有符合條件的物品");
            cm.dispose();
        }

        if (!checkCost(cubeState)) {
            chat += "沒有魔方";
            cm.dispose();
        } else {
            decreaseCost(cubeState);

            if (selectedItem == null) {
                selectedItem = cubeItemList[selection];
            }

            newItem = renewPotential(
                selectedItem,
                cubeState == CubeStatus.ADDITIONAL_CUBE
            );

            var originalPotentialData = getPotentialsData(
                selectedItem,
                cubeState == CubeStatus.ADDITIONAL_CUBE
            );
            var newPotentialsData = getPotentialsData(
                newItem,
                cubeState == CubeStatus.ADDITIONAL_CUBE
            );

            chat += "#fs18##b#z" + newItem.getItemId() + "# #k";
            chat += "#r<舊的屬性>#k" + enter;

            chat += getItemPotential(
                originalPotentialData,
                cubeState == CubeStatus.ADDITIONAL_CUBE
            );
            chat += "#b#z" + newItem.getItemId() + "# #k";
            chat += "#r<新的屬性>#k" + enter;
            chat += getItemPotential(
                newPotentialsData,
                cubeState == CubeStatus.ADDITIONAL_CUBE
            );
            chat += "#k" + enter;

	    chat += '按下#rESC#k可返回原始選項.' + enter;
            chat += '#L0#重新旋轉' + enter;
            chat += '#L1#應用'
            cm.sendSimpleS(chat, talkType);
        }
        cm.sendOk(chat);
    } else if (status == 3) {
        if (selection == 0) {
            status = 1;
            action(1, 0, 0);
            return;
        } else if (selection == 1) {
            cm.getPlayer().forceReAddItem(newItem, MapleInventoryType.EQUIP);
            selectedItem = null;
            cm.dispose();
        }
    }
}

function getItemPotential(potentialsData, isAditional) {
    var chat = "";
    for (var i = 0; i < potentialsData.length; i++) {
        var potentialIndex = isAditional ? i + 3 : i;
        chat += potentialIndex + 1 + "#fs15# 號潛在能力: ";
        chat += getPotentialDescription(potentialsData[i]);

        chat += enter;
    }
    return chat;
}

function getPotentialString(potentialType) {
    var _property = "未知屬性";
    switch (potentialType) {
        case Potential.STR_P:
            _property = "STR";
            break;
        case Potential.DEX_P:
            _property = "DEX";
            break;
        case Potential.INT_P:
            _property = "INT";
            break;
        case Potential.LUK_P:
            _property = "LUK";
            break;
        case Potential.ALL_P:
            _property = "全屬性";
            break;
        case Potential.IGNORE_DEF:
            _property = "無視怪物防禦率";
            break;
        case Potential.WATTACK_P:
            _property = "攻擊力";
            break;
        case Potential.MATTACK_P:
            _property = "魔力";
            break;
        case Potential.DAM_P:
            _property = "傷害";
            break;
        case Potential.INC_BOSSDAM_P:
            _property = "攻擊BOSS怪物時傷害";
            break;
        case Potential.INC_CRIT:
            _property = "暴擊概率";
            break;
        case Potential.INC_CRIT_DAM:
            _property = "暴擊傷害";
            break;
        case Potential.REDUCE_COOLTIME:
            _property = "冷卻時間減少";
            break;
        case Potential.INC_REWARD_PROP:
            _property = "物品獲得";
            break;
        case Potential.INC_MESO_PROP:
            _property = "楓幣獲得";
            break;
    }
    return _property;
}

function getPotentialsData(item, isAditional) {
    var potentialArray = [];
    for (var i = !isAditional ? 1 : 4; i <= (!isAditional ? 3 : 6); i++) {
        var currentPotential = getPotentialData(
            getPotential(item, i),
            item.getItemId()
        );
        potentialArray.push(currentPotential);
    }
    return potentialArray;
}

function getPotential(item, index) {
    var _type = "get";
    return item[_type + "Potential" + index]();
}

function renewPotential(item, isAditional) {
    var newItem = item.copy();
    var level;
    if (!isAditional) {
        level = newItem.getState() - 16;

        newItem.setPotential1(
            InventoryHandler.potential(newItem.getItemId(), level)
        );
        newItem.setPotential2(
            InventoryHandler.potential(
                newItem.getItemId(),
                level == 1 || Randomizer.nextInt(100) < KEEP_LEVEL_PERCENT ?
                    level :
                    level - 1
            )
        );

        if (newItem.getPotential3() > 0) {
            newItem.setPotential3(
                InventoryHandler.potential(
                    newItem.getItemId(),
                    level == 1 || Randomizer.nextInt(100) < KEEP_LEVEL_PERCENT ?
                        level :
                        level - 1
                )
            );
        } else {
            newItem.setPotential3(0);
        }
    } else {
        level =
            newItem.getPotential4() >= 10000 ?
                newItem.getPotential4() / 10000 :
                newItem.getPotential4() / 100;
        newItem.setPotential4(
            InventoryHandler.potential(newItem.getItemId(), level, true)
        );
        newItem.setPotential5(
            InventoryHandler.potential(
                newItem.getItemId(),
                level == 1 || Randomizer.nextInt(100) < KEEP_LEVEL_PERCENT ?
                    level :
                    level - 1,
                true
            )
        );

        if (newItem.getPotential6() > 0) {
            newItem.setPotential6(
                InventoryHandler.potential(
                    newItem.getItemId(),
                    level == 1 || Randomizer.nextInt(100) < KEEP_LEVEL_PERCENT ?
                        level :
                        level - 1,
                    true
                )
            );
        } else {
            newItem.setPotential6(0);
        }
    }
    return newItem;
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
    var equipInventory = cm.getPlayer().getInventory(MapleInventoryType.EQUIP);
    var itemList = new Array();
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
        case CubeStatus.BLACK_CUBE:
            return 5062010;
        case CubeStatus.ADDITIONAL_CUBE:
            return 5062500;
        case CubeStatus.RED_CUBE:
            return 5062009;
        case CubeStatus.MASTERCRAFT_CUBE:
            return 2711004;
    }
}

function checkCost(_cubestate) {
    if (cm.haveItem(getPotentialItemId(_cubestate), 1)) {
        return true;
    }

    return false;
}

function decreaseCost(_cubestate) {
    if (checkCost(_cubestate)) {
        cm.gainItem(getPotentialItemId(_cubestate), -1);
        return true;
    }

    return false;
}

function createPotentialMap() {
    var _potentialMap = newMap();
    var _potential = Potential.DAM_P;
    _potentialMap.put(20070, new PotentialStruct(_potential, 6));
    _potentialMap.put(30070, new PotentialStruct(_potential, 9));
    _potentialMap.put(40070, new PotentialStruct(_potential, 12));

    var _potential = Potential.WATTACK_P;
    _potentialMap.put(20051, new PotentialStruct(_potential, 6));
    _potentialMap.put(30051, new PotentialStruct(_potential, 9));
    _potentialMap.put(40051, new PotentialStruct(_potential, 12));

    _potentialMap.put(32051, new PotentialStruct(_potential, 9));
    _potentialMap.put(42051, new PotentialStruct(_potential, 12));

    var _potential = Potential.MATTACK_P;
    _potentialMap.put(20052, new PotentialStruct(_potential, 6));
    _potentialMap.put(30052, new PotentialStruct(_potential, 9));
    _potentialMap.put(40052, new PotentialStruct(_potential, 12));

    _potentialMap.put(32053, new PotentialStruct(_potential, 9));
    _potentialMap.put(42053, new PotentialStruct(_potential, 12));

    var _potential = Potential.INC_BOSSDAM_P;
    _potentialMap.put(30601, new PotentialStruct(_potential, 20));
    _potentialMap.put(40601, new PotentialStruct(_potential, 30));
    _potentialMap.put(30602, new PotentialStruct(_potential, 30));
    _potentialMap.put(40602, new PotentialStruct(_potential, 35));
    _potentialMap.put(40603, new PotentialStruct(_potential, 40));

    _potentialMap.put(32601, new PotentialStruct(_potential, 12));
    _potentialMap.put(42602, new PotentialStruct(_potential, 18));

    var _potential = Potential.INC_CRIT;
    _potentialMap.put(20055, new PotentialStruct(_potential, 8));
    _potentialMap.put(30055, new PotentialStruct(_potential, 10));
    _potentialMap.put(40055, new PotentialStruct(_potential, 12));

    _potentialMap.put(42058, new PotentialStruct(_potential, 2));
    _potentialMap.put(32057, new PotentialStruct(_potential, 9));

    var _potential = Potential.INC_CRIT_DAM;
    _potentialMap.put(40056, new PotentialStruct(_potential, 8));
    _potentialMap.put(40057, new PotentialStruct(_potential, 8));

    var _potential = Potential.REDUCE_COOLTIME;
    _potentialMap.put(40556, new PotentialStruct(_potential, 1));
    _potentialMap.put(42556, new PotentialStruct(_potential, 1));
    _potentialMap.put(40557, new PotentialStruct(_potential, 2));

    _potentialMap.put(40650, new PotentialStruct(Potential.INC_MESO_PROP, 20));

    _potentialMap.put(40656, new PotentialStruct(Potential.INC_REWARD_PROP, 20));

    var _potential = Potential.HP_P;
    _potentialMap.put(40045, new PotentialStruct(_potential, 12));
    _potentialMap.put(30035, new PotentialStruct(_potential, 9));
    _potentialMap.put(30045, new PotentialStruct(_potential, 9));

    var _potential = Potential.MP_P;
    _potentialMap.put(40046, new PotentialStruct(_potential, 12));
    _potentialMap.put(30046, new PotentialStruct(_potential, 9));

    var _potential = Potential.INC_SKILLLV;
    _potentialMap.put(30106, new PotentialStruct(_potential, 1));
    _potentialMap.put(30107, new PotentialStruct(_potential, 3));

    _potentialMap.put(0, new PotentialStruct(Potential.NONE, 0));

    return _potentialMap;
}

function getPotentialDescription(potentialData) {
    var chat = "";
    var type = potentialData.type;
    var value = potentialData.value;

    switch (type) {
        case Potential.INC_MESO_PROP:
        case Potential.INC_REWARD_PROP:
        case Potential.REDUCE_COOLTIME:
            chat += "#b";
            break;
    }

    chat += getPotentialString(type);
    chat += "#k";

    //prefix
    switch (type) {
        case Potential.INC_SKILLLV:
            chat += " +";
            break;
        default:
    }

    //prefix color
    if (type != Potential.NONE) {
        switch (type) {
            case Potential.STR_P:
            case Potential.DEX_P:
            case Potential.INT_P:
            case Potential.LUK_P:
                if (value >= 12) {
                    chat += "#r";
                }
                break;

            case Potential.WATTACK_P:
            case Potential.MATTACK_P:
            case Potential.DAM_P:
                if (value >= 12) {
                    chat += "#r";
                }
                break;

            case Potential.ALL_P:
                if (value >= 9) {
                    chat += "#r";
                }
                break;
                pr

            case Potential.INC_BOSSDAM_P:
                if (value >= 40) {
                    chat += "#r";
                }
                break;

            case Potential.INC_CRIT_DAM:
                chat += "#r";
                break;

            case Potential.IGNORE_DEF:
                if (value >= 40) {
                    chat += "#r";
                }
        }
        chat += " " + value + "#k";

        //subfix
        switch (type) {
            case Potential.REDUCE_COOLTIME:
                chat += "초";
                break;
            case Potential.INC_SKILLLV:
                break;
            default:
                chat += "%";
        }
    }
    return chat;
}

function getPotentialData(potential, itemId) {
    var lv = ii.getReqLevel(itemId) / 10 - 1;
    var pi = ii.getPotentialInfo(potential).get(lv);

    if (pi.incSTRr > 0 && pi.incDEXr > 0) {
        return new PotentialStruct(Potential.ALL_P, pi.incSTRr);
    } else if (pi.incSTRr > 0) {
        return new PotentialStruct(Potential.STR_P, pi.incSTRr);
    } else if (pi.incDEXr > 0) {
        return new PotentialStruct(Potential.DEX_P, pi.incDEXr);
    } else if (pi.incINTr > 0) {
        return new PotentialStruct(Potential.INT_P, pi.incINTr);
    } else if (pi.incLUKr > 0) {
        return new PotentialStruct(Potential.LUK_P, pi.incLUKr);
    } else if (pi.ignoreTargetDEF > 0) {
        return new PotentialStruct(Potential.IGNORE_DEF, pi.ignoreTargetDEF);
    }
    return getMappedPotentialData(potential);
}

function getMappedPotentialData(potential) {
    return potentialMap.get(potential) == null ?
        potentialMap.get(0) :
        potentialMap.get(potential);
}

///////////////////////////////////////////

function newMap() {
    var map = {};
    map.value = {};
    map.getKey = function (id) {
        return "k_" + id;
    };
    map.put = function (id, value) {
        var key = map.getKey(id);
        map.value[key] = value;
    };
    map.contains = function (id) {
        var key = map.getKey(id);
        if (map.value[key]) {
            return true;
        } else {
            return false;
        }
    };
    map.get = function (id) {
        var key = map.getKey(id);
        if (map.value[key]) {
            return map.value[key];
        }
        return null;
    };
    map.remove = function (id) {
        var key = map.getKey(id);
        if (map.contains(id)) {
            map.value[key] = undefined;
        }
    };

    return map;
}

function print(text) {
    java.lang.System.out.println(text);
}
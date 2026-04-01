var ii = Packages.server.MapleItemInformationProvider.getInstance();
var String = Packages.java.lang.String;
var Integer = Packages.java.lang.Integer;
var Randomizer = Packages.server.Randomizer;
var GameConstants = Packages.constants.GameConstants;
var MapleInventoryType = Packages.client.inventory.MapleInventoryType;
var DatabaseConnection = Packages.database.DatabaseConnection;
var ChannelServer = Packages.handling.channel.ChannelServer;
var MapleInventoryManipulator = Packages.server.MapleInventoryManipulator;
var MaplePet = Packages.client.inventory.MaplePet;

var SHOP_SELECTION_ADJUST = 10000;
var MAX_PURCHASE_QUANTITY = 10000;
var enter = '\r\n';
var reset = '#l#k';
var IS_DEBUGGING = false;

var itemType = {};
itemType.MESO = 0;
itemType.ITEM = 1;
var Shop = function (name, description) {
    this.itemList = [];
    this.description = description;
    this.name = name;

    this.addSellItem = function (sellItem, requireItem) {
        this.itemList.push(
            new ShopItem(sellItem, requireItem, this.shopId, this.itemList.length));
    }

    this.addSellItemWithPeriod = function (sellItem, requireItem, period) {
        sellItem.period = period;
        this.addSellItem(sellItem, requireItem);
    }

    this.get = function (index) {
        return this.itemList[index];
    }

    this.getItemList = function () {
        return this.itemList;
    }

    this.getDescription = function () {
        return this.description;
    }
}

var ShopItem = function (sellItem, requireItem, shopId, index) {
    this.sell = sellItem;
    this.require = requireItem;
    this.shopId = shopId;
    this.index = index;

    this.purchase = function (buyQuantity) {
        if (this.checkRequire(buyQuantity)) {
            var sell = this.sell;
            if (sell.type == itemType.ITEM && (cm.canHold(sell.id, sell.quantity * buyQuantity)) || true) {
                if (this.payPrice(buyQuantity)) {
                    switch (sell.type) {
                        case itemType.ITEM:
                            if (sell.quantity * buyQuantity > 0
                                && sell.quantity * buyQuantity <= Integer.MAX_VALUE) {
                                gainItem(sell.id, sell.quantity * buyQuantity, sell.period > 0 ? sell.period : 0);
                            } else {
                                return false;
                            }

                            break;
                        case itemType.MESO:
                            cm.gainMeso(sell.quantity * buyQuantity);
                            //cm.getPlayer().gainMeso(sell.quantity * buyQuantity, true);
                            break;
                        case itemType.DonationPoint:
                            cm.getPlayer().modifyDonationPoints(1, sell.quantity * buyQuantity, true);
                            //cm.getPlayer().gainMeso(sell.quantity * buyQuantity, true);
                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    this.payPrice = function (buyQuantity) {
        if (this.checkRequire(buyQuantity)) {
            var player = cm.getPlayer();
            var require = this.require;

            switch (require.type) {
                case itemType.ITEM:
                    if (require.quantity * buyQuantity > 0
                        && require.quantity * buyQuantity <= Integer.MAX_VALUE) {

                        cm.gainItem(require.id, require.quantity * buyQuantity * -1);
                    } else {
                        return false;
                    }

                    break;
                case itemType.MESO:
                    cm.gainMeso(require.quantity * buyQuantity * -1);
                    break;
                case itemType.DonationPoint:
                    cm.getPlayer().modifyDonationPoint(1, require.quantity * buyQuantity * -1, true);
                    break;
                default:
                    return false;
            }
            return true;
        }
        return false;
    }

    this.checkRequire = function (buyQuantity) {
        var player = cm.getPlayer();
        var require = this.require;
        var compareValue;
        switch (require.type) {
            case itemType.ITEM:
                if (player.haveItem(require.id, require.quantity * buyQuantity)) {
                    return true;
                }
                return false

            case itemType.MESO:
                compareValue = cm.getMeso();
                break;

            case itemType.DonationPoint:
                compareValue = cm.getPlayer().getDonationPoint(1);
                break;

            default:
                return false;
        }

        if (compareValue >= require.quantity * buyQuantity) {
            return true;
        }
        return false;

    }
}

var Item = function (type, quantity, id) {
    this.type = type;
    this.quantity = quantity;
    this.id = id;
}

var status = -1;
var chat
var shopArray = [];
var selectedShopIndex = 0;
var selectedItemIndex;

shopArray[shopArray.length] = new Shop("#fc0xFFD5D5D5#───────────────────────#k\r\n");
shopArray[shopArray.length] = new Shop('아케인모음');
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 2630782), new Item(itemType.DonationPoint, 20000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 2439959), new Item(itemType.DonationPoint, 15000));
shopArray[shopArray.length] = new Shop('큐브');
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 100, 5062006), new Item(itemType.CASH, 150000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 100, 5062002), new Item(itemType.CASH, 150000));
shopArray[shopArray.length] = new Shop('골드애플');
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5060048), new Item(itemType.CASH, 2000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 10, 5060048), new Item(itemType.CASH, 20000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 50, 5060048), new Item(itemType.CASH, 100000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 100, 5060048), new Item(itemType.CASH, 190000));
shopArray[shopArray.length] = new Shop('원더베리');
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5068300), new Item(itemType.CASH, 1500));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 5, 5068300), new Item(itemType.CASH, 7500));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 50, 5068300), new Item(itemType.CASH, 75000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 100, 5068300), new Item(itemType.CASH, 145000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5069100), new Item(itemType.CASH, 2900));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 3, 5069100), new Item(itemType.CASH, 8700));
shopArray[shopArray.length] = new Shop('주문서');
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5539001), new Item(itemType.CASH, 3000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5539002), new Item(itemType.CASH, 30000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5539003), new Item(itemType.CASH, 4500));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5539004), new Item(itemType.CASH, 4500));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5539005), new Item(itemType.CASH, 4500));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5530544), new Item(itemType.CASH, 4500));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5539007), new Item(itemType.CASH, 11000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 10, 5539007), new Item(itemType.CASH, 110000));
shopArray[shopArray.length] = new Shop("#fc0xFFD5D5D5#─────────────────────────#k\r\n");
shopArray[shopArray.length] = new Shop('캐시아이템');
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 2430028), new Item(itemType.CASH, 49500));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5069000), new Item(itemType.CASH, 500));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 11, 5069000), new Item(itemType.CASH, 5000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5069001), new Item(itemType.CASH, 1500));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 11, 5069001), new Item(itemType.CASH, 15000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 4034803), new Item(itemType.CASH, 9900));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5539000), new Item(itemType.CASH, 4900));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 3, 5539000), new Item(itemType.CASH, 12900));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5539011), new Item(itemType.CASH, 10000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5539008), new Item(itemType.CASH, 2900));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 3, 5539008), new Item(itemType.CASH, 6000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5539010), new Item(itemType.CASH, 1500));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5539009), new Item(itemType.CASH, 12000));
shopArray[shopArray.length] = new Shop('20레벨 심볼');
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 2430031), new Item(itemType.CASH, 89000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 2430032), new Item(itemType.CASH, 89000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 2430033), new Item(itemType.CASH, 89000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 2430049), new Item(itemType.CASH, 89000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 2430051), new Item(itemType.CASH, 89000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 2430052), new Item(itemType.CASH, 89000));

/*shopArray[shopArray.length] = new Shop('LUNA', '#L999#LUNA');
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5000641), new Item(itemType.MESO, 5000), 3);
shopArray[shopArray.length - 1].addSellItem(new Item(itemType.ITEM, 1, 5000641), new Item(itemType.MESO, 5000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 1592040), new Item(itemType.MESO, 5000), 3);
shopArray[shopArray.length - 1].addSellItem(new Item(itemType.ITEM, 1, 1592040), new Item(itemType.MESO, 5000));
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5211000), new Item(itemType.ITEM, 200, 4032201), 3);
shopArray[shopArray.length - 1].addSellItemWithPeriod(new Item(itemType.ITEM, 1, 5212000), new Item(itemType.MESO, 5000), 3);
shopArray[shopArray.length - 1].addSellItem(new Item(itemType.ITEM, 1, 5002079), new Item(itemType.MESO, 5000));*/
function start() {
    //if(!cm.getPlayer().isGM()) return;
    status = -1;
    action(1, 0, 0);
}
//shopArray[shopArray.length-1].addSellItem(new Item(itemType.ITEM, 1, 2290096), new Item(itemType.ITEM, 500, 4001126));
//shopArray[shopArray.length-1].addSellItem(new Item(itemType.ITEM, 1, 2290125), new Item(itemType.ITEM, 500, 4001126));

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }

    //chat = '#fs11#'
    chat = ''
    if (status == 0) {
        chat += "보유중인 넥슨 캐시 : "+ cm.getPlayer().getDonationPoint() +"\r\n\r\n"
        chat += getShopNamesCatalog(shopArray) + enter + enter;
        chat += getShopDescriptionCatalog(shopArray[selectedShopIndex]) + enter;
        chat += getShopItemCatalog(shopArray[selectedShopIndex]);
        cm.sendSimple(chat)
    } else if (status == 1) {
        if (selection == 10009) {
            cm.dispose();
            cm.openNpc(1012001);
        } else {
        selectedItemIndex = selection
        if (selectedItemIndex >= SHOP_SELECTION_ADJUST) {
            selectedShopIndex = selectedItemIndex - SHOP_SELECTION_ADJUST;
            status = -1;
            action(1, 0, 0);
        } else {
            chat += '구매할 아이템 개수를 입력해주세요' + enter;
            cm.sendGetNumber(chat, 1, 1, MAX_PURCHASE_QUANTITY);
        }
    }
    } else if (status == 2) {
            if (selection > 0 && selection <= MAX_PURCHASE_QUANTITY) {
                var shopItem = shopArray[selectedShopIndex].get(selectedItemIndex);
                for (var i = 0; i < selection; i++) {
                    var purchaseSuccess = shopItem.purchase(1);
                    chat += (i + 1) + '번째 구매: ';;
                    chat += '#i' + shopItem.sell.id + '# #z' + shopItem.sell.id + '# '

                    if (purchaseSuccess) {
                        chat += '구매에 성공하였습니다.';
                    } else {
                        chat += '구매에 실패하였습니다.' + enter;
                        chat += '구매에 필요한 아이템, 또는 인벤토리에 공간이 있는지 확인해주세요' + enter;
                        break;
                    }
                    chat += enter;
                }

            } else {
                chat += '에러입니다.'
            }

            status = -1
            cm.sendOk(chat);
}
}


function getShopNamesCatalog(shops) {
    var chat = '';
    for (var i = 0; i < shops.length; i++) {
        var shop = shops[i];
        chat += '#L' + (i + SHOP_SELECTION_ADJUST) + '#'
        if (i == selectedShopIndex) {
            chat += '#e';
        }
        chat += shop.name;
        if ((i + 1) % 6 == 0) {
            chat += enter;
        }
        chat += '#k#n#l'
    }
    return chat;
}

function getShopDescriptionCatalog(shop) {
    var chat = '';
    if (shop.getDescription() != null) {
        chat += shop.getDescription();
    }
    return chat;
}

function getShopItemCatalog(shop) {
    var chat = '';
    var itemList = shop.getItemList();

    for (var i = 0; i < itemList.length; i++) {
        var shopItem = itemList[i];
        var sell = shopItem.sell;
        var require = shopItem.require;
        chat += '#L' + shopItem.index + '#';

        chat += '#b';

        switch (sell.type) {
            case itemType.ITEM:
                //chat += '#i' + sell.id + '# '
                chat += '#i' + sell.id + '# #z' + sell.id + '# ' //아이템 텍스트              
                if (sell.quantity > 1) {
                    chat += sell.quantity + '개'
                }
                break;
            case itemType.MESO:
                chat += formattedMeso(sell.quantity);
                chat += '메소'
                break;
            case itemType.DonationPoint:
                chat += (sell.quantity);
                chat += 'P'
                break;
        }

        if (sell.period > 0) {
            chat += '(' + sell.period + '일) ';
        }

        chat += '#k';
        chat += '(';
        switch (require.type) {
            case itemType.ITEM:

                chat += '#i' + require.id + '##z' + require.id + '# ' //아이템 텍스트
                chat += require.quantity + '개#n#k';
                break;
            case itemType.MESO:
                chat += formattedMeso(require.quantity);
                chat += '메소#n'
                break;
            case itemType.DonationPoint:
                chat += require.quantity;
                chat += 'P#n'
                break;
        }
        chat += ')';
        chat += enter;
    }
    return chat
}


////////////
function Color(a, r, g, b) {
    var hexcode = ''
    var alpha = 'FF';
    var red;
    var green;
    var blue;

    if (b != null) {
        alpha = formattedHex(a);
        red = formattedHex(r);
        green = formattedHex(g);
        blue = formattedHex(b);

    } else if (g != null) {
        red = formattedHex(a);
        green = formattedHex(r);
        blue = formattedHex(g);
    }

    if (red == null) {
        hexcode = a;
    } else {
        hexcode = alpha + red + green + blue
    }

    return '#fc0x' + hexcode + '#'
}

function formattedHex(c) {
    var hex = c.toString(16);
    return hex.length == 1 ? '0' + hex : hex;
}

function FFColor(hexcode) {
    return Color('FF' + hexcode);
}

function formattedMeso(meso) {
    //억단위
    var upperMeso = Math.floor(meso / 100000000);
    var upperLeftMeso = meso % 100000000;
    //1억 5천이면 아래에 5천이 남아있음 
    //천만단위
    var lowerMeso = Math.floor(upperLeftMeso / 10000);
    var lowerLeftMeso = upperLeftMeso % 10000;

    var mesoString = '';

    if (upperMeso >= 1)
        mesoString += upperMeso + '억';
    if (lowerMeso > 0)
        mesoString += lowerMeso + '만';
    if (lowerLeftMeso > 0)
        mesoString += lowerLeftMeso;

    return mesoString;
}

function sleep(delay) {
    var start = new Date().getTime();
    while (new Date().getTime() < start + delay);
}

function print(text) {
    java.lang.System.out.println(text)
}

function getMonsterImage(mobId) {
    return '#fMob/' + mobId + '.img/stand/0#'
}

function numberToKorean(number) {
    var inputNumber = number < 0 ? false : number;
    var unitWords = ['', '만', '억', '조', '경'];
    var splitUnit = 10000;
    var splitCount = unitWords.length;
    var resultArray = [];
    var resultString = '';

    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
        unitResult = Math.floor(unitResult);
        if (unitResult > 0) {
            resultArray[i] = unitResult;
        }
    }

    for (var i = 0; i < resultArray.length; i++) {
        if (!resultArray[i]) continue;
        resultString = String(resultArray[i]) + unitWords[i] + resultString;
    }

    return resultString;
}

function getDate() {
    var data = new Date();
    var month = data.getMonth() < 10 ? '0' + (data.getMonth() + 1) : (data.getMonth() + 1) + '';
    var day = data.getDate() < 10 ? '0' + data.getDate() : data.getDate() + ''
    var date = (data.getYear() + 1900) + '' + month + '' + day;
    return date;
}

function findPlayerByName(userName) {
    var World = Packages.handling.world.World;
    var ChannelServer = Packages.handling.channel.ChannelServer;
    if (World.Find.findChannel(userName) >= 0) {
        var player = ChannelServer.getInstance(World.Find.findChannel(userName)).getPlayerStorage().getCharacterByName(userName);
        if (player != null) {
            return player;
        }
    }
    return null
}

function writeLog(fileName, contents, isAppend) {
    var file = new java.io.File(fileName);
    if (!file.exists()) {
        file.createNewFile();
    }

    var pw = new java.io.PrintWriter(new java.io.FileWriter(file, isAppend));

    pw.println(contents)

    pw.flush();
    pw.close();
}

function addStringByIndex(targetString, stringBeAdded, index) {
    return [targetString.slice(0, index), stringBeAdded, targetString.slice(index)].join('');
}

function removeStringByIndex(targetString, index) {
    return targetString.slice(0, index) + targetString.slice(index + 1);
}

function gainItem(itemId, quantity, period) {
    if (GameConstants.isPet(itemId)) {
        gainPet(itemId, quantity, period);
    } else {
        if (period > 0) {
            cm.gainItemPeriod(itemId, quantity, period);
        } else {
            cm.gainItem(itemId, quantity);
        }
    }
}

function gainPet(itemId, quantity, period) {
    for (var i = 0; i < quantity; i++) {
        cm.gainPet(itemId, "" + ii.getName(itemId) + "", 1, 1, 100, period, 101);
    }
}

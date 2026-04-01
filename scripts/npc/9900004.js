importPackage(Packages.client.inventory);
importPackage(Packages.server.items);
importPackage(Packages.constants);
importPackage(Packages.client);

importPackage(Packages.server.items);
importPackage(Packages.tools);
importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
importPackage(Packages.server.Luna);
importPackage(Packages.tools.RandomStream);


var status = 0;
var operation = -1;
var select = -1;
var type;
var ty;
var gc = GameConstants;
var dd = true;
var yes = 1;
var invs = Array(1, 5);
var invv;
var selected;
var slot_1 = Array();
var slot_2 = Array();
var statsSel;
var sel;
var name;
var isban = false;
var conn = null;

var banitem = [1143800];
var sda = false;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            var ask = " #fs15#尊貴的管理員，想贈送那種類型的道具？\r\n";
            ask += "#L1##b[裝備]#k道具贈送!\r\n";
            ask += "#L2##b[消耗]#k道具贈送!\r\n";
            ask += "#L3##b[設置]#k道具贈送!\r\n";
            ask += "#L4##b[其他]#k道具贈送!\r\n";
            ask += "#L5##b[現金]#k道具贈送!\r\n";
            ask += "#L7##b[裝扮]#k道具贈送!\r\n";
            cm.sendSimple(ask);
        } else if (status == 1) {
            operation = selection;
            if (operation == 1) {
                type = MapleInventoryType.EQUIP;
                yes = 1;
            } else if (operation == 2) {
                type = MapleInventoryType.USE;
                yes = 2;
            } else if (operation == 4) {
                type = MapleInventoryType.SETUP;
                yes = 4;
            } else if (operation == 3) {
                type = MapleInventoryType.ETC;
                yes = 3;
            } else if (operation == 5) {
                type = MapleInventoryType.CASH;
                yes = 5;
            } else if (operation == 7) {
                type = MapleInventoryType.DECORATION;
                yes = 7;
            }
            if (selection >= 1 && selection <= 7) {
                cm.sendGetText("#fs15#請告訴我贈送對象的角色名！#r\r\n\r\n贈送對象需在同一頻道在線。（贈送獨特道具時，若對方已持有該道具，將會消失。）");
            } else if (selection == 6) {
                cm.sendOk("#fs15# 贈送系統可將任何種類或屬性的道具贈送給他人。贈送需要1000萬楓幣手續費，且雙方需在同一頻道在線。因錯誤使用導致的問題，官方將不承擔責任。");
                cm.dispose();
            }
        } else if (status == 2) {
            if (operation == 1) {
                type = MapleInventoryType.EQUIP;
            } else if (operation == 2) {
                type = MapleInventoryType.USE;
            } else if (operation == 3) {
                type = MapleInventoryType.SETUP;
            } else if (operation == 4) {
                type = MapleInventoryType.ETC;
            } else if (operation == 5) {
                type = MapleInventoryType.CASH;
            } else if (operation == 7) {
                type = MapleInventoryType.DECORATION;
            }
            var item = cm.getChar().getInventory(type);
            var text = cm.getText();
            var ch = Packages.handling.world.World.Find.findChannel(text);
            if (ch < 0) {
                cm.sendOk("#fs15# 對方可能未在線、頻道不同，或角色不存在。");
                cm.dispose();
            } else {
                conn = Packages.handling.channel.ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(text);
                if (conn == null) {
                    cm.sendOk("#fs15# 對方可能未在線、頻道不同，或角色不存在。");
                    cm.dispose();
                }
                var ok = false;
                var selStr = "#b" + conn.getName() + "#k想贈送哪個道具呢？\r\n";
                for (var x = 1; x < 2; x++) {
                    var inv = cm.getInventory(yes);
                    for (var i = 0; i <= cm.getInventory(yes).getSlotLimit(); i++) {
                        if (x == 0) {
                            slot_1.push(i);
                        } else {
                            slot_2.push(i);
                        }
                        var it = inv.getItem(i);
                        if (it == null) {
                            continue;
                        }
                        var itemid = it.getItemId();
                        ok = true;
                        if ((itemid >= 1140000 && itemid <= 1143999) && !cm.getPlayer().isGM()) {
                            continue;
                        }
                        selStr += "#L" + (yes * 1000 + i) + "##v" + itemid + "##t" + itemid + "##l\r\n";
                    }
                }
                if (!ok) {
                    cm.sendOk("#fs15#好像沒有可以贈送的道具？");
                    cm.dispose();
                    return;
                }
                cm.sendSimple(selStr + "#k");
            }
        } else if (status == 3) {
            sel = selection;
            if (operation == 1) {
                type = MapleInventoryType.EQUIP;
            } else if (operation == 2) {
                type = MapleInventoryType.USE;
            } else if (operation == 3) {
                type = MapleInventoryType.SETUP;
            } else if (operation == 4) {
                type = MapleInventoryType.ETC;
            } else if (operation == 5) {
                type = MapleInventoryType.CASH;
            } else if (operation == 7) {
                type = MapleInventoryType.DECORATION;
            }
            var item = cm.getChar().getInventory(type).getItem(selection % 1000).copy();
            var text = cm.getText();
            invv = selection / 1000;
            var inzz = cm.getInventory(invv);
            selected = selection % 1000;
            if (invv == invs[0]) {
                statsSel = inzz.getItem(slot_1[selected]);
            } else {
                statsSel = inzz.getItem(slot_2[selected]);
            }
            if (statsSel == null) {
                cm.sendOk("#fs15# 發生錯誤，請聯繫管理員。");
                cm.dispose();
                return;
            }
            var text = cm.getText();
            var con = cm.getClient().getChannelServer().isMyChannelConnected(text);

            for (a = 0; a < banitem.length; a++) {
                if (banitem[a] == item.getItemId()) {
                    isban = true;
                    continue;
                }
            }
            if (item.getQuantity() == 1) {
                if (cm.getMeso() >= 1000000) {
                    if (GameConstants.isPet(item.getItemId()) == false) {
                        if (cm.getPlayer().getName() != text) {
                            if (!canHold(conn, item.getItemId())) {
                                cm.sendOk("#fs15# 對方的物品欄空間不足。");
                                cm.dispose();
                                return;
                            }
                            if (isban) {
                                cm.sendOk("這是禁止贈送的道具。");
                                cm.dispose();
                                return;
                            }
                            MapleInventoryManipulator.removeFromSlot(cm.getC(), type, selection % 1000, item.getQuantity(), true);
                            MapleInventoryManipulator.addFromDrop(conn.getClient(), item, true, false, false, true);
                            //conn.getClient().getSession().writeAndFlush(MainPacketCreator.OnAddPopupSay(9000086, 3000, "#b"+cm.getPlayer().getName()+"#k 收到 #b(#t"+item.getItemId()+"#)#k 的贈送！請查看物品欄。", ""));
                            cm.sendOk("#b" + text + "#k 已成功收到 #i" + item.getItemId() + "##b(#t" + item.getItemId() + "#)#k 的贈送！");
                            cm.gainMeso(-1000000);
                            //WriteLog(cm.getPlayer().getName(), text, item.getItemId(), item.getQuantity());
                            sda = true;
                            //cm.dispose();
                        } else {
                            cm.sendOk("#fs15#嗯？開玩笑嗎？不能贈送道具給自己哦。");
                            cm.dispose();
                        }
                    } else {
                        cm.sendOk("#fs15# 寵物無法贈送。");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("贈送道具需要1000萬楓幣手續費。");
                    cm.dispose();
                }
            } else {
                if (!isban) {
                    cm.sendGetNumber("#fs15# 想贈送多少個？\r\n目前持有 #i" + item.getItemId() + "# #b(#t" + item.getItemId() + "#)#k 數量：#b" + item.getQuantity() + "#k", 1, 1, item.getQuantity());
                } else {
                    cm.sendOk("這是禁止贈送的道具。");
                    cm.dispose();
                }
            }
            name = text;
        } else if (status == 4) {
            if (sda) {
                cm.dispose();
//              cm.openNpc(9010023);
                return;
            }
            if (isban) {
                cm.sendOk("發生錯誤。");
                cm.dispose();
                return;
            }
            var sele = selection % 1000;
            var quan = cm.getText();
            if (operation == 1) {
                type = MapleInventoryType.EQUIP;
            } else if (operation == 2) {
                type = MapleInventoryType.USE;
            } else if (operation == 3) {
                type = MapleInventoryType.SETUP;
            } else if (operation == 4) {
                type = MapleInventoryType.ETC;
            } else if (operation == 5) {
                type = MapleInventoryType.CASH;
            } else if (operation == 7) {
                type = MapleInventoryType.DECORATION;
            }
            var item = cm.getChar().getInventory(type).getItem(sel % 1000).copy();
            var text = cm.getText();
            invv = sel / 1000;
            var inzz = cm.getInventory(invv);
            selected = sel % 1000;
            if (invv == invs[0]) {
                statsSel = inzz.getItem(slot_1[selected]);
            } else {
                statsSel = inzz.getItem(slot_2[selected]);
            }
            if (statsSel == null) {
                cm.sendOk("發生錯誤，請聯繫管理員。");
                cm.dispose();
                return;
            }

            var text = selection;
            var con = cm.getClient().getChannelServer().isMyChannelConnected(name);
            if (item.getQuantity() >= text) {
                if (cm.getMeso() >= 1000000) {
                    if (cm.getPlayer().getName() != name) {
                        item.setQuantity(text);
                        MapleInventoryManipulator.removeFromSlot(cm.getC(), type, sel % 1000, item.getQuantity(), true);
                        MapleInventoryManipulator.addbyItem(conn.getClient(), item, true);
                        //conn.getClient().getSession().writeAndFlush(MainPacketCreator.OnAddPopupSay(9000086, 3000, "#b"+cm.getPlayer().getName()+"#k 收到 #b(#t"+item.getItemId()+"#)#k " + item.getQuantity() + " 個的贈送！請查看物品欄。", ""));
                        cm.sendOk("#b" + name + "#k 已成功收到 #i" + item.getItemId() + "##b(#t" + item.getItemId() + "#)#k 的贈送！");
                        //WriteLog(cm.getPlayer().getName(),name, item.getItemId(), item.getQuantity());
                        //cm.gainMeso(-1000000);
                        sda = true;
                    } else {
                        cm.sendOk("不能贈送道具給自己哦。");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("贈送道具需要1000萬楓幣手續費。");
                    cm.dispose();
                }
            } else {
                cm.sendOk("輸入數量超過持有數量。");
                cm.dispose();
            }
        } else if (status == 5) {
            cm.dispose();
        }
    }
}

function canHold(chr, itemid) {
    return chr.getInventory(GameConstants.getInventoryType(itemid)).getNextFreeSlot() > -1;
}

function WriteLog(cname, vname, itemid, qty) {
    a = new Date();
    temp = Randomizer.rand(0, 9999999);
    fFile1 = new File("property/Logs/贈送/" + temp + "_" + cname + ".log");
    if (!fFile1.exists())
        fFile1.createNewFile();
    out1 = new FileOutputStream("property/Logs/贈送/" + temp + "_" + cname + ".log", false);
    var msg = "'" + cname + "' 向 '" + vname + "' 發送了贈送。\r\n";
    msg += "發送者：" + cname + "\r\n";
    msg += "接收者：" + vname + "\r\n";
    msg += "發送時間：" + a.getFullYear() + "年 " + Number(a.getMonth() + 1) + "月 " + a.getDate() + "日 " + a.getHours() + "時 " + a.getMinutes() + "分 " + a.getSeconds() + "秒\r\n";
    msg += "贈送道具代碼：" + itemid + "\r\n";
    msg += "贈送道具數量：" + qty + "\r\n";
    out1.write(msg.getBytes());
    out1.close();
}
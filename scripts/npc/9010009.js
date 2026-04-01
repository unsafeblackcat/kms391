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
importPackage(Packages.server.life);
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
            var ask = " #fs15#你好！ 要送什麼類型的單品？\r\n";
            ask += "#L1##b[裝備]#k我要送你道具！\r\n";
            ask += "#L2##b[消耗]#k我要送你道具！\r\n";
            ask += "#L3##b[設定]#k我要送你道具！\r\n";
            ask += "#L4##b[其他]#k我要送你道具！\r\n";
            ask += "#L5##b[裝潢]#k我要送你道具！\r\n";
            ask += "#L6##b[裝扮]#k我要送你道具！\r\n";
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
            } else if (operation == 6) {
                type = MapleInventoryType.CODY;
                yes = 6
                        ;
            }
            if (selection >= 1 && selection <= 7) {
                cm.sendGetText("#fs15#선물 받을 닉네임을 알려줘!#r\r\n\r\n선물 받을 사람은 같은 채널에 접속중이여야 해. (고유 아이템 선물 시 상대방이 이미 아이템을 보유 중일 경우 증발됩니다.)");
            } else if (selection == 6) {
                cm.sendOk("#fs15# 선물시스템은 아이템의 종류나 옵션에 관계없이 누구에게나 선물할수 있는 시스템이야. 선물을 하기 위해선 천만메소가 필요하고, 같은 채널에 접속중이여야 해. 잘못된 사용으로 인한 문제는 운영진 측에서 책임지지 않아.");
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
            } else if (operation == 6) {
                type = MapleInventoryType.CODY;
            }
            var item = cm.getChar().getInventory(type);
            var text = cm.getText();
            var ch = Packages.handling.world.World.Find.findChannel(text);
            if (ch < 0) {
                cm.sendOk("#fs15# 현재 접속중이 아니거나 채널이 다른가봐. 혹은 존재하지 않는 아이디일 수도 있어.");
                cm.dispose();
            } else {
                conn = Packages.handling.channel.ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(text);
                if (conn == null) {
                    cm.sendOk("#fs15# 현재 접속중이 아니거나 채널이 다른가봐. 혹은 존재하지 않는 아이디일 수도 있어.");
                    cm.dispose();
                }
                var ok = false;
                var selStr = "#b" + conn.getName() + "#k님에게 어떤 아이템을 선물할래?\r\n";
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
                    cm.sendOk("#fs15#선물할 아이템이 없는것 같은데?");
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
            } else if (operation == 6) {
                type = MapleInventoryType.CODY;
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
                cm.sendOk("#fs15# 오류입니다. 운영자에게 보고해주세요.");
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
                                cm.sendOk("#fs15# 선물을 받을 상대방 인벤토리에 빈 공간이 없나봐.");
                                cm.dispose();
                                return;
                            }
                            if (isban) {
                                cm.sendOk("그건 금지된 아이템이야.");
                                cm.dispose();
                                return;
                            }
                            MapleInventoryManipulator.removeFromSlot(cm.getC(), type, selection % 1000, item.getQuantity(), true);
                            MapleInventoryManipulator.addFromDrop(conn.getClient(), item, true, false, false, true);
                            //conn.getClient().getSession().writeAndFlush(MainPacketCreator.OnAddPopupSay(9000086, 3000, "#b"+cm.getPlayer().getName()+"#k 님에게 #b(#t"+item.getItemId()+"#)#k을(를) 선물받으셨습니다. 인벤토리를 확인해보세요.", ""));
                            cm.sendOk("#b" + text + "#k 님에게 #i" + item.getItemId() + "##b(#t" + item.getItemId() + "#)#k을(를) 배달했어!");
                            cm.gainMeso(-1000000);
                            //WriteLog(cm.getPlayer().getName(), text, item.getItemId(), item.getQuantity());
                            sda = true;
                            //cm.dispose();
                        } else {
                            cm.sendOk("#fs15#응? 장난이지? 자기 자신에게는 선물할수 없어.");
                            cm.dispose();
                        }
                    } else {
                        cm.sendOk("#fs15# 펫은 선물할수 없어.");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("아이템을 선물하기 위해선 수수료 1천만메소가 필요해.");
                    cm.dispose();
                }
            } else {
                if (!isban) {
                    cm.sendGetNumber("#fs15# 몇개를 선물할래?\r\n현재 소지중인 #i" + item.getItemId() + "# #b(#t" + item.getItemId() + "#)#k 갯수 : #b" + item.getQuantity() + "#k", 1, 1, item.getQuantity());
                } else {
                    cm.sendOk("금지된 아이템이야.");
                    cm.dispose();
                }
            }
            name = text;
        } else if (status == 4) {
            if (sda) {
                cm.dispose();
//				cm.openNpc(9010023);
                return;
            }
            if (isban) {
                cm.sendOk("오류입니다.");
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
            } else if (operation == 6) {
                type = MapleInventoryType.CODY;
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
                cm.sendOk("오류입니다. 운영자에게 보고해주세요.");
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
                        //conn.getClient().getSession().writeAndFlush(MainPacketCreator.OnAddPopupSay(9000086, 3000, "#b"+cm.getPlayer().getName()+"#k 님에게 #b(#t"+item.getItemId()+"#)#k "+item.getQuantity()+"개를 선물받으셨습니다. 인벤토리를 확인해보세요.", ""));
                        cm.sendOk("#b" + name + "#k 님에게 #i" + item.getItemId() + "##b(#t" + item.getItemId() + "#)#k을(를) 배달했어!");
                        //WriteLog(cm.getPlayer().getName(),name, item.getItemId(), item.getQuantity());
                        //cm.gainMeso(-1000000);
                        sda = true;
                    } else {
                        cm.sendOk("자기 자신에게는 선물할수 없어.");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("아이템을 선물하기 위해선 수수료 1천만메소가 필요해.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("가지고 있는 수보다 더 큰 수를 입력했어.");
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

/*
 function writelog(v, id) {
 
 a = new Date();
 temp = Randomizer.rand(0,9999999);
 cn = cm.getPlayer().getName();
 fFile1 = new File("property/Logs/선물/"+temp+"_"+cn+".log");
 if (!fFile1.exists()) {
 fFile1.createNewFile();
 }
 out1 = new FileOutputStream("property/Logs/선물/"+temp+"_"+cn+".log",false);
 var msg =  "'"+cm.getPlayer().getName()+"'이(가) "+t+"함.\r\n";
 msg += "'"+a.getFullYear()+"년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일'\r\n";
 msg += " 받은사람 : "+v+"\r\n";
 msg += " 아이템 코드 : "+id+"\r\n";
 out1.write(msg.getBytes());
 out1.close();
 }
 */
function WriteLog(cname, vname, itemid, qty) {
    a = new Date();
    temp = Randomizer.rand(0, 9999999);
    fFile1 = new File("property/Logs/선물/" + temp + "_" + cname + ".log");
    if (!fFile1.exists())
        fFile1.createNewFile();
    out1 = new FileOutputStream("property/Logs/선물/" + temp + "_" + cname + ".log", false);
    var msg = "'" + cname + "'이 '" + vname + "'에게 선물을 보냈습니다.\r\n";
    msg += "보낸이 : " + cname + "\r\n";
    msg += "받은이 : " + vname + "\r\n";
    msg += "보낸 시각 : " + a.getFullYear() + "년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일 " + a.getHours() + "시 " + a.getMinutes() + "분 " + a.getSeconds() + "초\r\n";
    msg += "보낸 아이템 코드 : " + itemid + "\r\n";
    msg += "보낸 아이템 개수 : " + qty + "\r\n";
    out1.write(msg.getBytes());
    out1.close();
}
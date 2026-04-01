


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    블랙 에 의해 만들어 졌습니다.

    엔피시아이디 : 9900002

    엔피시 이름 : 신용협동조합

    엔피시가 있는 맵 : 빅토리아로드 : 엘리니아 (180000000)

    엔피시 설명 : MISSINGNO


*/
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.server.enchant);
importPackage(java.sql);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
var 클론 = [1032799, 1022699, 1012949, 1032800, 1022700, 1012950, 1032801, 1022701, 1012951, 1032802, 1022702, 1012952, 1032803, 1022703, 1012953]
var status = -1;
var sel = -1, suc = 0, needcoin = 0, need1 = 0, need2 = 0, need3 = 0, item = 0, needcoin2 = 0;
var epic1 = 1032800;
var epic2 = 1022700;
var epic3 = 1012950;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 = "#fs15#반갑네! 자네는 #e클론아바타#n에 대해서 알고 있나? 크크.\r\n"
        말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        말 += "#L0##fc0xFF4374D9#클론 아바타 - 레어 등급#k 아이템을 제작하고 싶습니다.\r\n";
        말 += "#L1##fc0xFF8041D9#클론 아바타 - 에픽 등급#k 아이템을 제작하고 싶습니다.\r\n";
        말 += "#L2##fc0xFFEDA900#클론 아바타 - 유니크 등급#k 아이템을 제작하고 싶습니다.\r\n";
        말 += "#L3##fc0xFF47C83E#클론 아바타 - 레전드리 등급#k 아이템을 제작하고 싶습니다.\r\n\r\n";
        말 += "#L9##fc0xFFFF7012#클론 아바타 보존 영약#k을 구매하고 싶습니다.\r\n";
        말 += "#L10##b클론 아바타 종류를 확인해보고 싶습니다.\r\n";
        말 += "#L99##r대화를 종료하고 싶습니다.\r\n";
        cm.sendSimpleS(말, 0x04, 2192030);
    } else if (status == 1) {
        sel = selection;
        if (selection <= 3) {
            color = sel == 0 ? "#fc0xFF4374D9#" : sel == 1 ? "#fc0xFF8041D9#" : sel == 2 ? "#fc0xFFEDA900#" : "#fc0xFF47C83E#"
            말 = "#fs15#어떤 아이템을 제작할 건지 선택해보게나.\r\n";
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#L1##i" + (epic1 + selection) + "# " + color + "#z" + (epic1 + selection) + "# 제작하기\r\n";
            말 += "#L2##i" + (epic2 + selection) + "# " + color + "#z" + (epic2 + selection) + "# 제작하기\r\n";
            말 += "#L3##i" + (epic3 + selection) + "# " + color + "#z" + (epic3 + selection) + "# 제작하기\r\n";
            cm.sendSimpleS(말, 0x04, 2192030);
        } else if (selection == 9) {
            말 = "#fs15##z4310026# 아이템을 구매하기 위해 아래의 아이템이 필요합니다.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#L9##fc0xFFFF7012##i4310026# #z4310026##k#l\r\n";
            말 += "　　　　　　└#fc0xFF88DCC8##z4310022##k #r500개#k\r\n";
            말 += "　　　　　　└#fc0xFFEE77DD##z4310024##k #r500개#k\r\n";
            cm.sendOkS(말, 0x04, 2192030);
        } else if (selection == 10) {
            말 = "#fs15#클론 아바타 리스트입니다.\r\n\r\n"
            for (var a = 0; a < 클론.length; a++) {
                말 += "#i" + 클론[a] + "# #b#z" + 클론[a] + "##k\r\n"
            }
            if (cm.getClient().getQuestStatus(50003) == 1 && cm.getClient().getCustomKeyValue(50003, "1") != 1) {
                cm.getClient().setCustomKeyValue(50003, "1", "1");
            }
            cm.sendOkS(말, 0x04, 2192030);
            cm.dispose();
        } else if (seletion == 99) {
            cm.dispose();
        }
    } else if (status == 2) {
        item = 0;
        if (sel == 0) {
            // 레어 종류 보여주기
            item = selection == 1 ? epic1 : selection == 2 ? epic2 : epic3;
            suc = 45;
            fake = 60;
            needcoin = 500;
            need1 = (epic1 - 1);
            need2 = (epic2 - 1);
            need3 = (epic3 - 1);
        } else if (sel == 1) {
            // 에픽 종류 보여주기
            item = selection == 1 ? epic1 : selection == 2 ? epic2 : epic3;
            item++;
            suc = 55;
            fake = 70;
            needcoin = 300;
            need1 = (epic1);
            need2 = (epic2);
            need3 = (epic3);
        } else if (sel == 2) {
            // 유니크 종류 보여주기
            item = selection == 1 ? epic1 : selection == 2 ? epic2 : epic3;
            item += 2;
            suc = 40;
            fake = 50;
            needcoin = 500;
            needcoin2 = 500;
            need1 = (epic1 + 1);
            need2 = (epic2 + 1);
            need3 = (epic3 + 1);
        } else if (sel == 3) {
            // 레전 종류 보여주기
            item = selection == 1 ? epic1 : selection == 2 ? epic2 : epic3;
            item += 3;
            suc = 20;
            fake = 30;
            needcoin = 500;
            needcoin2 = 800;
            need1 = (epic1 + 2);
            need2 = (epic2 + 2);
            need3 = (epic3 + 2);
        } else if (sel == 9) {
            if (!cm.haveItem(4310022, 500) || !cm.haveItem(4310024, 500)) {
                cm.sendOk("#fs15##i4310026# #b#z4310026##k 아이템을 구매하기 위한 재료가 부족합니다.");
                cm.dispose();
                return;
            }
            cm.gainItem(4310022, -500);
            cm.gainItem(4310024, -500);
            cm.gainItem(4310026, 1);
            cm.dispose();
        }
        말 = "#fs15##i" + item + "# #b#z" + item + "##k 아이템을 제작하겠나?"
        말 += "\r\n제작을 하려면 아래의 재료가 필요하지.\r\n"
        말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        말 += "#i" + need1 + "# #b#z" + need1 + "##k #r1개#k\r\n"
        말 += "#i" + need2 + "# #b#z" + need2 + "##k #r1개#k\r\n"
        말 += "#i" + need3 + "# #b#z" + need3 + "##k #r1개#k\r\n"
        말 += "#i4310022# #b#z4310022##k #r" + needcoin + "개#k\r\n"
        if (needcoin2 > 0) {
            말 += "#i4310024# #b#z4310024##k #r" + needcoin2 + "개#k\r\n\r\n"
        } else {
            말 += "\r\n";
        }
        if (cm.haveItem(4310026) > 0) {
            말 += "#b#z4310026#의 효과를 받고 있습니다.\r\n"
        } else {
            말 += "#r제작 실패 시, 모든 재료가 소모됩니다.#k\r\n"
        }
        말 += "제작 확률은 #r#e" + fake + "%#n#k 입니다. 정말로 제작하시겠습니까?"
        cm.sendYesNoS(말, 0x04, 2192030);
    } else if (status == 3) {
        inz = cm.getInventory(6)
        sucss = (Math.floor(Math.random() * 100) <= suc);
        if (!cm.haveItem(need1, 1) || !cm.haveItem(need2, 1) || !cm.haveItem(need3, 1) || !cm.haveItem(4310022, needcoin)) {   //재료체크
            cm.sendOkS("#fs15##b#z" + item + "##k을 제작하기 위한 재료 아이템이 부족한거 같군.", 0x04, 2192030);
            cm.dispose();
            return;
        }
        if (needcoin2 > 0) {
            if (!cm.haveItem(4310024, needcoin2)) {
                cm.sendOkS("#fs15##b#z" + item + "##k을 제작하기 위한 재료 아이템이 부족한거 같군.", 0x04, 2192030);
                cm.dispose();
                return;
            }
        }

        cm.gainItem(4310022, -needcoin);
        if (needcoin2 > 0) {
            cm.gainItem(4310024, -needcoin2);
        }
        var a = null, b = null, c = null;
        var protect = false;
        if (cm.haveItem(4310026) > 0) {
            cm.gainItem(4310026, -1);
            protect = true;
        }

        if (!protect || sucss) {
            for (w = 0; w < inz.getSlotLimit(); w++) {
                if (inz.getItem(w) == null) {
                    continue;
                }
                if (need1 == inz.getItem(w).getItemId() && a == null) {
                    a = inz.getItem(w);
                } else if (need2 == inz.getItem(w).getItemId() && b == null) {
                    b = inz.getItem(w);
                } else if (need3 == inz.getItem(w).getItemId() && c == null) {
                    c = inz.getItem(w);
                }
            }
            if (a != null && b != null && c != null) {
                MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(), GameConstants.getInventoryType(a.getItemId()), a.getPosition(), a.getQuantity(), false);
                MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(), GameConstants.getInventoryType(b.getItemId()), b.getPosition(), b.getQuantity(), false);
                MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(), GameConstants.getInventoryType(c.getItemId()), c.getPosition(), c.getQuantity(), false);
            }
        }

        if (sucss) {// 성공했을때
            var items = MapleItemInformationProvider.getInstance().getEquipById(item);
            flag = items.getFlag();
            flag |= ItemFlag.KARMA_EQUIP.getValue();
            flag |= ItemFlag.CHARM_EQUIPED.getValue();
            items.setFlag(flag);
            items.setUniqueId(MapleInventoryIdentifier.getInstance());
            MapleInventoryManipulator.addbyItem(cm.getClient(), items);

            World.Broadcast.broadcastMessage(CWvsContext.serverMessage(11, cm.getPlayer().getClient().getChannel(), "", cm.getPlayer().getName() + "님이 {} 아이템 제작에 성공 하셨습니다!", true, items));
            말 = "#fs15##b#z" + item + "##k 제작에 성공했다네!\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i" + item + "##b#z" + item + "##k"
            cm.sendOkS(말, 0x04, 2192030);
            cm.dispose();
        } else { // 실패했을때
            cm.sendOkS("#fs15##r제작이 실패했네.. 다음에는 꼭 성공해보게!", 0x04, 2192030);
            cm.dispose();
        }
    }
}
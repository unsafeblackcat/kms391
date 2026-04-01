


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    어빌확인 에 의해 만들어 졌습니다.

    엔피시아이디 : 2074149

    엔피시 이름 : 설이

    엔피시가 있는 맵 : The Black : Night Festival (100000000)

    엔피시 설명 : MISSINGNO


*/

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

검정 = "#fc0xFF191919#"
보라 = "#fc0xFF6E2FC7#"
펫 = "#fUI/CashShop.img/CashItem_label/8#";

var status = -1;
var count = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    var size = 0;
    var check = false;
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.dispose();
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        inz = cm.getInventory(5)
        for (w = 0; w < inz.getSlotLimit(); w++) {
            if (!inz.getItem(w) || inz.getItem(w).getPet() == null) {
                continue;
            }
            if (inz.getItem(w).getPet().getWonderGrade() == 5) {
                for (i = 0; i < cm.getPlayer().getPets().length; i++) {
                    if (cm.getPlayer().getPets()[i] != null) {
                        if (cm.getPlayer().getPets()[i].getInventoryPosition() == inz.getItem(w).getPet().getInventoryPosition()) {
                            check = true;
                            break;
                        }
                    }
                }
                if (!check) {
                    size++;
                }
            }
        }
        if (size < 1) {
            cm.sendOkS("#fs15#"+검정+"이봐, #h #! 더 이상 #r조합이 불가능한 펫#k"+검정+"을 데리고 있나?\r\n블랙 페스티벌#k"+검정+"을 더 재밌게 즐기기 위해 내가 "+펫+" #bPRAY드림 펫#k "+검정+"1마리 당 #b#z5068300##k "+검정+"1개"+검정+"로 교환해주고 있다네.\r\n\r\n하지만 자네는 교환에 필요한 PRAY드림 펫이 부족한거 같군. ", 0x04, 2192030);
            cm.dispose();
        } else {
            cm.sendGetNumber(2192030, "#fs15#"+검정+"\r\n이봐, #h #! 더 이상 #r조합이 불가능한 펫#k"+검정+"을 데리고 있나?\r\n블랙 페스티벌#k"+검정+"을 더 재밌게 즐기기 위해 내가 "+펫+" #bPRAY드림 펫#k "+검정+"1마리 당 #b#z5068300##k "+검정+"1개"+검정+"로 교환해주고 있다네.\r\n\r\n자네는 현재 " + size + "마리가 있어 #b#z5068300##k"+검정+"를 " + Math.floor((size / 1)) + "개 받을수 있네 몇개를 교환할 건가? \r\n ", Math.floor((size / 1)), 1, Math.floor((size / 1)));
        }

    } else if (status == 1) {
        count = selection;
        cm.sendYesNoS("#fs15#"+검정+"정말 #bPRAY드림 펫 " + (count) + "마리#k"+검정+"로 #r#z5068300# " + count + " 개#k"+검정+"를 교환할텐가?", 0x04, 2192030);
    } else if (status == 2) {
        inz = cm.getInventory(5)
        for (w = 0; w < inz.getSlotLimit(); w++) {
            if (!inz.getItem(w) || inz.getItem(w).getPet() == null) {
                continue;
            }
            if (inz.getItem(w).getPet().getWonderGrade() == 5) {
                for (i = 0; i < cm.getPlayer().getPets().length; i++) {
                    if (cm.getPlayer().getPets()[i] != null) {
                        if (cm.getPlayer().getPets()[i].getInventoryPosition() == inz.getItem(w).getPet().getInventoryPosition()) {
                            check = true;
                            break;
                        }
                    }
                }
                if (!check) {
                    size++;
                    MapleInventoryManipulator.removeFromSlot(cm.getClient(), GameConstants.getInventoryType(inz.getItem(w).getItemId()), inz.getItem(w).getPosition(), inz.getItem(w).getQuantity(), false);
                }
                if (size >= count) {
                    break;
                }
            }
        }
        cm.gainItem(5068300, count);
        말 = "#fs15#"+검정+"교환에 성공했다네! 인벤토리를 확인해보게나.\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
        말 += "#i5068300##z5068300# "+count+" 개";
        cm.sendOkS(말, 0x04, 2192030);
        cm.dispose();
    }
}

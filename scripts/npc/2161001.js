var status = -1;
var qt = 10;
var st1 = 0;
var st2 = 0;
var st3 = 0;

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

litem = [1113089, 1032227, 1122274];
qty = [0, 0, 0];


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if (status >= 3 && status <= 5) {
            qt -= selection;
            qty[status - 3] = selection;
            if (qt <= 0) {
                status = 5;
            }
        }
        status++;
    }

    if (status == 0) {
        var text = "꽃도 나비도 사람들도 모두 사라졌어요. 남은 건 타오르는 불길 뿐... 아름답던 이 성이 어째서 이렇게 변해버린 걸까요.\r\n\r\n";
        text += "#fUI/UIWindow.img/UtilDlgEx/list1#\r\n";
        text += "#L0##d이피아의 희망";
        cm.sendOk(text, 2161001);
    } else if (status == 1) {
        qt2 = cm.itemQuantity(litem[0]) + cm.itemQuantity(litem[1]) + cm.itemQuantity(litem[2]);
        if (qt2 < 10) {
            var text = "제가 생전에 만들었던 장신구를 가져와 주세요\r\n\r\n";
            text += "#d * 장신구를 총 10개이상 소지해야 합니다\r\n";
            text += "#i" + litem[0] + "# #b#z" + litem[0] + "# #r#c" + litem[0] + "#개#k 보유\r\n";
            text += "#i" + litem[1] + "# #b#z" + litem[1] + "# #r#c" + litem[1] + "#개#k 보유\r\n";
            text += "#i" + litem[2] + "# #b#z" + litem[2] + "# #r#c" + litem[2] + "#개#k 보유\r\n";
            cm.sendOk(text, 2161001);
            cm.dispose();
            return;
        } else {
            cm.sendNext("제가 생전에 만들었던 장신구를 충분히 가지고 계시네요.", 2161001);
        }
    } else if (status == 2) {
        if (qt2 >= 10) {
            cm.sendNext("새로운 반지 제작을 위해 반지/귀고리/펜던트를 각각 몇 개씩 사용하실지 알려주세요.", 2161001);
        } else {
            cm.sendOk("비정상적인 값이 발견되어 스크립트를 종료합니다", 2161001);
            cm.dispose();
            return;
        }
    } else if (status == 3) {
        var text = "몇개의 #b반지#k를 사용하시겠습니까?\r\n";
        text += "현재 소지한 이피아의 반지 : #c" + litem[0] + "#개\r\n";
        text += "추가로 필요한 장신구 : " + qt + "개";
        if (cm.itemQuantity(litem[0]) > qt) {
            cm.sendGetNumber(text, cm.itemQuantity(litem[0]), 0, qt);
        } else {
            cm.sendGetNumber(text, cm.itemQuantity(litem[0]), 0, cm.itemQuantity(litem[0]));
        }
    } else if (status == 4) {
        var text = "몇개의 #b귀고리#k를 사용하시겠습니까?\r\n";
        text += "현재 소지한 이피아의 귀고리 : #c" + litem[1] + "#개\r\n";
        text += "추가로 필요한 장신구 : " + qt + "개";
        if (cm.itemQuantity(litem[1]) > qt) {
            cm.sendGetNumber(text, cm.itemQuantity(litem[1]), 0, qt);
        } else {
            cm.sendGetNumber(text, cm.itemQuantity(litem[1]), 0, cm.itemQuantity(litem[1]));
        }
    } else if (status == 5) {
        var text = "몇개의 #b펜던트#k를 사용하시겠습니까?\r\n";
        text += "현재 소지한 이피아의 목걸이 : #c" + litem[2] + "#개\r\n";
        text += "추가로 필요한 장신구 : " + qt + "개";
        if (cm.itemQuantity(litem[1]) > qt) {
            cm.sendGetNumber(text, cm.itemQuantity(litem[2]), 0, qt);
        } else {
            cm.sendGetNumber(text, cm.itemQuantity(litem[2]), 0, cm.itemQuantity(litem[2]));
        }
    } else if (status == 6) {
        if (qt != 0) {
            cm.sendOk("입력하신 장신구의 갯수가 모자랍니다. 합이 10개가 되도록 입력해주세요.", 2161001);
            cm.dispose();
            return;
        }
        var text = "다음 수량으로 교환을 진행하시겠습니까?\r\n";
        text += "#r#e(주의! 인벤토리 첫 번째 칸부터 순서대로 아이템이 사라집니다.)#k#n\r\n";
        text += "#z" + litem[0] + "# : #b" + qty[0] + "개#k\r\n";
        text += "#z" + litem[1] + "# : #b" + qty[1] + "개#k\r\n";
        text += "#z" + litem[2] + "# : #b" + qty[2] + "개#k\r\n";
        cm.sendYesNo(text, 2161001);
    } else if (status == 7) {
        inz = cm.getInventory(1)
        var a = 0, b = 0, c = 0;
        for (w = 0; w < inz.getSlotLimit(); w++) {
            if (inz.getItem(w) == null) {
                continue;
            }
            if (1113089 == inz.getItem(w).getItemId() && a < qty[0]) {
                MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(), GameConstants.getInventoryType(inz.getItem(w).getItemId()), inz.getItem(w).getPosition(), inz.getItem(w).getQuantity(), false);
                a++;
            }
        }
        for (w = 0; w < inz.getSlotLimit(); w++) {
            if (inz.getItem(w) == null) {
                continue;
            }
            if (1032227 == inz.getItem(w).getItemId() && b < qty[1]) {
                MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(), GameConstants.getInventoryType(inz.getItem(w).getItemId()), inz.getItem(w).getPosition(), inz.getItem(w).getQuantity(), false);
                b++;
            }
        }
        for (w = 0; w < inz.getSlotLimit(); w++) {
            if (inz.getItem(w) == null) {
                continue;
            }
            if (1122274 == inz.getItem(w).getItemId() && c < qty[2]) {
                MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(), GameConstants.getInventoryType(inz.getItem(w).getItemId()), inz.getItem(w).getPosition(), inz.getItem(w).getQuantity(), false);
                c++;
            }
        }
        cm.sendOk("저의 염원이 담긴 반지를 건네드렸습니다. 부디 레온이 제 존재를 느낄 수 있기를...");
        cm.gainItem(1113282, 1);
        cm.dispose();
    }
}
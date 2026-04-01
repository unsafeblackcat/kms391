/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#"
파랑 = "#fc0xFF4641D9#"
남색 = "#fc0xFF4641D9#"
스라벨 = "#fUI/CashShop.img/CashItem_label/0#";
레드라벨 = "#fUI/CashShop.img/CashItem_label/1#";
블랙라벨 = "#fUI/CashShop.img/CashItem_label/2#";
마라벨 = "#fUI/CashShop.img/CashItem_label/3#";

importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 4) {
            status++;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.openNpc(9010108);
    }
}
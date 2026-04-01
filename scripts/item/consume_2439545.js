
var status = -1;
importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
function start() {
    status = -1;
    action (1, 0, 0);
}
검정 = "#fc0xFF191919#"
var day = 7; //유효기간 옵션 설정(일)
function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 = "#fs15#"+검정+"안녕하세요! #h0#님 오토루팅 상자를 사용하시겠습니까?\r\n"
        말 += "#r< 사용 후에는 환불 및 교환이 어렵습니다. >#k"
        cm.sendYesNo(말);
    } else if (status == 1) {
        말 = "#fs15#오토루팅 상자를 사용했습니다!\r\n\r\n"
        cm.getPlayer().setKeyValue(12345, "AutoRoot", 1);
        cm.gainItem(2439545, -1);		
        cm.sendOkS(말, 0x04, 9000030);
        cm.dispose();
    } else {
        cm.dispose();
    }
}

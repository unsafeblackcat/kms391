importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);

var status = -1, sel = 0;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        d = status;
        status++;
    }


    if (status == 0) {
        if (qm.getPlayer().getMapId() == 993194000) {
            qm.getPlayer().dropMessage(5, "이미 <LIVE 스튜디오>에 있습니다.")
            qm.dispose();
        } else {
            qm.warp(993194000);
            qm.dispose();
        }
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}
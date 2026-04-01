/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#";
연파 = "#fc0xFF4374D9#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
파랑 = "#fc0xFF4641D9#";
빨강 = "#fc0xFFF15F5F#";
하늘 = "#fc0xFF0099CC#";

importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.dispose();
        qm.warp(100000051, 0);
    }
}
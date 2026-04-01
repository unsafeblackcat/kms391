


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	뭐지요 에 의해 만들어 졌습니다.

	엔피시아이디 : 1105008

	엔피시 이름 : 체키

	엔피시가 있는 맵 : 헤네시스 : 리나의 집 (100000003)

	엔피시 설명 : MISSINGNO


*/

var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

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
        말 = "#fs15#需要金屬染色？\r\n\r\n"
        말 += "#L0##b我想改變金屬的裝備染色。";
        cm.sendNext(말);
    } else if (status == 1) {
        cm.sendYesNo("#fs15##b想換金屬的#k偽裝色嗎?\r\n#e#r100萬#n#k的話，我會用 #b想要的顏色#k給您上色！");

    } else if (status == 2) {
        if (cm.getPlayer().getMeso() > 1000000){
            말 = "#fs15##b你好 #k要換成什麼顏色?\r\n#r(選擇與當前金屬合金相同的顏色也會减少元素)\r\n\r\n#b"
            말 += "#L0#基本顏色\r\n"
            말 += "#L2881#天藍色\r\n"
            말 += "#L5745#紅色\r\n"
            말 += "#L3601#藍色\r\n"
            말 += "#L4913#粉色\r\n"
            말 += "#L4273#紫色\r\n"
            말 += "#L417#橙色\r\n"
            말 += "#L2049#綠色\r\n"
            말 += "#L1025#淡綠色\r\n"
            cm.sendNext(말);
        } else {
            cm.sendOk("好像缺少馬内啊？\r\n如果想換偽裝色的話 #e#r100萬#n#k需要.")
            cm.dispose();
        }

    } else if (status == 3) {
        cm.sendOk("好了，塗完了。 太棒了！");
        cm.gainMeso(-1000000);
        cm.getPlayer().dropMessage(5,"機械金屬阿默偽裝顏色改變了.")
        cm.getPlayer().setKeyValue(19752, "hiddenpiece", selection);
        cm.dispose();
    }
}

importPackage(Packages.constants);


var status = -1;

var 星 = "#fUI/FarmUI.img/objectStatus/star/whole#";
var 熱門 = "#fUI/CashShop.img/CSEffect/hot/0#";
var 新品 = "#fUI/CashShop.img/CSEffect/new/0#";


function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {

        var choose = "是否返回自由廣場？\r\n";
        choose += "#L1##e移動#n#k#k\r\n";
        cm.sendSimple(choose);

    } else if (selection == 1) {//赫爾提
        cm.dispose();
        cm.warp(910000000);
    } else if (selection == 2) {//希拉
        cm.dispose();
        cm.warpParty(240040700);
    } else if (selection == 3) {//半魔李昂
        cm.dispose();
        cm.warpParty(262000000);
    } else if (selection == 4) {//阿卡伊倫
        cm.dispose();
        cm.warpParty(211070000);
    } else if (selection == 5) {//席格諾斯
        cm.dispose();
        cm.warpParty(272000000);
    } else if (selection == 6) {//馬格努斯
        cm.dispose();
        cm.warpParty(270040000);
    } else if (selection == 7) {//肯斯·芬克賓
        cm.dispose();
        cm.warpParty(271041000);
    } else if (selection == 8) {//肯斯·布迪女王
        cm.dispose();
        cm.warpParty(105200000);
    } else if (selection == 9) {//肯斯·貝爾魯姆
        cm.dispose();
        cm.warpParty(220080000);
    } else if (selection == 10) {//斯烏
        cm.dispose();
        cm.warpParty(401060000);
    } else if (selection == 11) {//扎昆
        cm.dispose();
        cm.warpParty(350060300);
    } else if (selection == 12) {//混沌皮埃爾
        cm.dispose();
        cm.warpParty(105300303);
    } else if (selection == 13) {//混沌班班
        cm.dispose();
        cm.warpParty(450004000);
    } else if (selection == 14) {//路西德
        cm.dispose();
        cm.warpParty(450007240);
    } else if (selection == 24) {//多魯西
        cm.dispose();
        cm.openNpc(2540010);
    } else if (selection == 25) {//火焰狼
        cm.dispose();
        cm.openNpc(9001059);
    } else if (selection == 26) {//戴米安
        cm.dispose();
        cm.openNpc(1530621);  
    } else if (selection == 99) {//卡翁
        cm.dispose();
        cm.openNpc(2050005);
    } else if (selection == 101) {//克羅斯
        cm.dispose();
        cm.openNpc(9073003); 
    }
}



/*

	퓨어 온라인 소스 팩의 스크립트 입니다.

        제작 : 주크블랙

	엔피시아이디 : 
	
	엔피시 이름 :

	엔피시가 있는 맵 : 

	엔피시 설명 : 


*/

var status = -1;
importPackage(Packages.tools.RandomStream);
importPackage(Packages.client.items);

function start() {
    status = -1;
    action (1, 0, 0);
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
        cm.sendYesNo("打開封印的箱子嗎？ 最大緩存限制為50萬緩存.");
    } else if (status == 1) {
        var leftslot = cm.getPlayer().getNX();
        if (leftslot >= 500000) {
            cm.sendOk("緩存最大限制為50萬緩存。 無法打開此框，超出當前緩存範圍.");
            cm.dispose();
            return;
        }
        cm.gainItem(2430026, -1);
        var cash = Randomizer.rand(10000, 100000);
        cm.getPlayer().modifyCSPoints(1, cash, true);
        cm.sendOk("從封印的鎖裏出來了以下物品。.\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n\r\n#e#b"+cash+"個");
        
        cm.dispose();
    }
}



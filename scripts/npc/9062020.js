var status = 0;

importPackage(Packages.constants);


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            if (cm.getPlayer().getLevel() >= 100) {
                if (cm.getPlayer().getMapId() == 993000750) {
                    var Fancy = "#fs15##rFancy#k 에서 입소문난 #d설귀도 낚시터!#k\r\n이 곳이 그렇게 #d월척#k이 잘 잡히기로 소문이 자자하지..\r\n";
                    Fancy += "#L0##d낚시에 대한 설명#k\r\n";
                    Fancy += "#L1##b낚시 용품 구입";
                    cm.sendSimple(Fancy);
                } else {
                    cm.sendOk("#fs15#세월을 낚는 재미란.. 즐겨본자만이 아는법이지..");
                    cm.dispose();
                }
            } else {
                cm.sendOk("#fs15##r낚시 이용은 레벨 100 이상만 이용 가능합니다.", 9062004);
                cm.dispose();
            } 
        } else if (status == 1) {
            if (selection == 0) {
                cm.sendOk("#fs15#나에게 #r낚시 용품#k을 구입한 후 좋은 자리를 선점하게곰.\r\n#r미끼#k를 소지 후 의자에 앉아 있으면 낚시가 진행된다곰.");
                cm.dispose();

            } else if (selection == 1) {
                var FANCY2 = "#fs15##b원하시는 품목을 선택 해보게나..#k\r\n#r인벤토리가 꽉차면 못 받을 수 있으니 주의 바란다네..#k\r\n";
                FANCY2 += "#L0##i3015394# #r낚시 의자#k #d(3,000,000,000)#k\r\n";
                FANCY2 += "#L1##i4035000# #r미끼 구입 개당#k #d(10,000,000)#k";
                cm.sendSimple(FANCY2);
            } 

        } else if (status == 2) {

            if (selection == 0) {
                if (cm.getMeso() >= 3000000000) {
                    if (cm.canHold(3015394)) {
                        cm.gainItem(3015394, 1);
                        cm.gainMeso(-3000000000);
                        cm.sendOk(" #fs15##d다음에 또 오게나..#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs15##r설치칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs15##r돈이 부족한 것 같군..#k");
                    cm.dispose();
                }

            } else if (selection == 1) {
        		cm.sendGetNumber("몇개를 살거야?", 1, 1, 100);
            }
        } else if (status == 3) {
	count = selection;
	if (cm.getMeso() >= 10000000 * count) {
		if (cm.canHold(4035000)) {
                        	cm.gainItem(4035000, 1 * count);
                        	cm.gainMeso(-10000000 * count);
                        	cm.dispose();
		}
	} else {
        		cm.sendOk("#fs15##b메소#k 가 부족합니다.");
                        cm.dispose();
	}
        }
    }
}
﻿var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        var chat = "#fs15#안녕하세요? 크루엘라에서 후원을 담당하고 있는 발발이입니다.\r\n\r\n";
        chat += "#fc0xFF6799FF#후원#k 포인트 : #r"+cm.getPlayer().getDonationPoint()+"#k\r\n";
        //chat += "#L1# #d후원 포인트 상점#k (스탯)\r\n";
        //chat += "#L2# #d어메이징 큐브 돌리기#k (옵션)\r\n";
        chat += "#L3# #d후원 포인트 상점#k (아이템)\r\n";
        chat += "#L4# #d후원 포인트 상점#k (스킬)\r\n";
        //chat += "#L5# #d후원 포인트 상점#k (캐시아이템 강화)\r\n";
        //chat += "#L6# #d후원 포인트 상점#k (뱃지 포켓, 캐시아이템 잠재부여)\r\n";
        //chat += "#L7# #d후원 직업변경 시스템#k (모든직업 가능)\r\n";
        chat += "#L8# #d홍보포인트 수령하기#k \r\n";
        cm.sendSimple(chat);

    } else if (status == 1) {
        if (selection == 1) {
            cm.dispose();
            cm.openNpc(9000210);

        } else if (selection == 2) {
            cm.dispose();
            cm.openNpc(2002000);

        } else if (selection == 3) {
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 1540100, "donationShop");

        } else if (selection == 4) {
	    var chat = "#fn나눔고딕 ExtraBold##fs15#크루엘라의 후원 스킬을 구매하고 싶으신가요? 구매를 원하시는 항목을 선택해주시면 구매를 도와드리겠습니다.\r\n\r\n";
            chat += "현재 #b#h0##k님의 후원포인트 : #d"+cm.getPlayer().getDonationPoint()+"#k\r\n";
            chat += "#L0##fSkill/231.img/skill/2311003/icon# 홀리 심볼#k #r(100000P)#k\r\n";
            chat += "#L1##fSkill/000.img/skill/0008002/icon# 샤프 아이즈#k #r(100000P)#k\r\n";
            chat += "#L2##fSkill/000.img/skill/0008006/icon# 윈드 부스터#k #r(100000P)#k\r\n";
            chat += "#L3##fSkill/532.img/skill/5321054/icon# 벅 샷 #k #r(300000P)#k\r\n";
            chat += "#L5##fSkill/200.img/skill/2001002/icon# 매직 가드 #k #r(150000P)#k\r\n";
            cm.sendSimple(chat);

        } else if (selection == 5) {
            cm.dispose();
            cm.openNpc(9076120);

        } else if (selection == 6) {
            cm.dispose();
            cm.openNpc(2192031);

        } else if (selection == 7) {
            cm.dispose();
            cm.openNpc(9072201);
        } else if (selection == 8) {
            cm.dispose();
            cm.openNpc(1530051);
        }

    } else if (status == 2) {
	if (selection == 0) {
             if (cm.getPlayer().getKeyValue(190830, "symbol") == 1) {
		       cm.getPlayer().setKeyValue(190830, "symbol", "1");
                       cm.sendOk("이미 구매한 상품 입니다. 차감없이 재구매 되었습니다.");
                       cm.dispose();
                       return;
                }
		if (cm.getPlayer().getDonationPoint() >= 100000) {
		       cm.getPlayer().gainDonationPoint(-100000);
		       cm.getPlayer().setKeyValue(190830, "symbol", "1");
    		       cm.sendOk("#fn나눔고딕 Extrabold##fs15##s400001020##b <홀리 심볼>#k을 구매하셨습니다.");
    		       cm.dispose();
		} else {
    		       cm.sendOk("#fn나눔고딕 ExtraBold##fs15##r후원포인트#k가 부족하신거 같은데요?");
    		       cm.dispose();
		}
        } else if (selection == 1) {
             if (cm.getPlayer().getKeyValue(190830, "sharp") == 1) {
		       cm.getPlayer().setKeyValue(190830, "sharp", "1");
                       cm.sendOk("이미 구매한 상품 입니다. 차감없이 재구매 되었습니다.");
                       cm.dispose();
                       return;
                }
		if (cm.getPlayer().getDonationPoint() >= 100000) {
		       cm.getPlayer().gainDonationPoint(-100000);
		       cm.getPlayer().setKeyValue(190830, "sharp", "1");
    		       cm.sendOk("#fn나눔고딕 Extrabold##fs15##s100008002##b <샤프 아이즈>#k을 구매하셨습니다.");
    		       cm.dispose();
		} else {
    		       cm.sendOk("#fn나눔고딕 ExtraBold##fs15##r후원포인트#k가 부족하신거 같은데요?");
    		       cm.dispose();
		}
        } else if (selection == 2) {
             if (cm.getPlayer().getKeyValue(190830, "wind") == 1) {
		       cm.getPlayer().setKeyValue(190830, "wind", "1");
                       cm.sendOk("이미 구매한 상품 입니다. 차감없이 재구매 되었습니다.");
                       cm.dispose();
                       return;
                }
		if (cm.getPlayer().getDonationPoint() >= 100000) {
		       cm.getPlayer().gainDonationPoint(-100000);
		       cm.getPlayer().setKeyValue(190830, "wind", "1");
    		       cm.sendOk("#fn나눔고딕 Extrabold##fs15##s400001006##b <윈드 부스터>#k을 구매하셨습니다.");
    		       cm.dispose();
		} else {
    		       cm.sendOk("#fn나눔고딕 ExtraBold##fs15##r후원포인트#k가 부족하신거 같은데요?");
    		       cm.dispose();
		}
        } else if (selection == 3) {
             if (cm.getPlayer().getKeyValue(190830, "buck") == 1) {
		       cm.getPlayer().setKeyValue(190830, "buck", "1");
                       cm.sendOk("이미 구매한 상품 입니다. 차감없이 재구매 되었습니다.");
                       cm.dispose();
                       return;
                }
		if (cm.getPlayer().getDonationPoint() >= 300000) {
		       cm.getPlayer().gainDonationPoint(-300000);
		       cm.getPlayer().setKeyValue(190830, "buck", "1");
    		       cm.sendOk("#fn나눔고딕 Extrabold##fs15##s5321054##b <벅 샷>#k을 구매하셨습니다.");
    		       cm.dispose();
		} else {
    		       cm.sendOk("#fn나눔고딕 ExtraBold##fs15##r후원포인트#k가 부족하신거 같은데요?");
    		       cm.dispose();
		}
        } else if (selection == 4) {
             if (cm.getPlayer().getKeyValue(190830, "heyst") == 1) {
		       cm.getPlayer().setKeyValue(190830, "heyst", "1");
                       cm.sendOk("이미 구매한 상품 입니다. 차감없이 재구매 되었습니다.");
                       cm.dispose();
                       return;
                }
		if (cm.getPlayer().getDonationPoint() >= 100000) {
		       cm.getPlayer().gainDonationPoint(-100000);
		       cm.getPlayer().setKeyValue(190830, "heyst", "1");
    		       cm.sendOk("#fn나눔고딕 Extrabold##fs15##s4001005##b <헤이스트>#k을 구매하셨습니다.");
    		       cm.dispose();
		} else {
    		       cm.sendOk("#fn나눔고딕 ExtraBold##fs15##r후원포인트#k가 부족하신거 같은데요?");
    		       cm.dispose();
		}
        } else if (selection == 5) {
             if (cm.getPlayer().getKeyValue(190830, "magicguard") == 1) {
		       cm.getPlayer().setKeyValue(190830, "magicguard", "1");
                       cm.sendOk("이미 구매한 상품 입니다. 차감없이 재구매 되었습니다.");
                       cm.dispose();
                       return;
                }
		if (cm.getPlayer().getDonationPoint() >= 150000) {
		       cm.getPlayer().gainDonationPoint(-150000);
		       cm.getPlayer().setKeyValue(190830, "magicguard", "1");
    		       cm.sendOk("#fn나눔고딕 Extrabold##fs15##s2001002##b <매직가드>#k을 구매하셨습니다.");
    		       cm.dispose();
		} else {
    		       cm.sendOk("#fn나눔고딕 ExtraBold##fs15##r후원포인트#k가 부족하신거 같은데요?");
    		       cm.dispose();
		}
        }
    }
}
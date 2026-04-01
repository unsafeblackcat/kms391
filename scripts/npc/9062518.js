


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	닥나 에 의해 만들어 졌습니다.

	엔피시아이디 : 9062463

	엔피시 이름 : 카린

	엔피시가 있는 맵 : 네오 캐슬 : 네오 캐슬 (993189100)

	엔피시 설명 : 네오 젬샵


*/

var status = -1;
var sel = 0, coin ,needcoin;
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
        if (status == 2 && sel == 1) {
            cm.sendOkS("마음이 바뀌면 다시 찾아와~", 0x04);
            cm.dispose();
            return;
        } else if (status == 3  && sel == 1) {
            cm.dispose();
            return;
        }
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        var talk = "꽃밭이 가득한 여기가 천국이 아닐까..\r\n\r\n"
        talk += "#L1##b#e<돌의 정령 코인>#n을 구매하고 싶어!"
        cm.sendSimpleS(talk, 0x04);
    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
        } else if (sel == 1) {
            cm.sendNextS("특별히 #b#h0##k#n#k한테만 주는 특별 혜택이야!\r\n바로....!\r\n\r\n\r\n#b#i4310018:##t4310018# #e1개#k#n를 #r#i4310312:##t4310312# #e1개#k#n에 #r#e판매#n#k할게!\r\n\r\n\r\n", 0x04);
        }
    } else if (status == 2) {
        if (sel == 0) {
        } else if (sel == 1) {
            cm.sendYesNoS("어때? \r\n날이면 날마다 오는 기회가 아니야!\r\n\r\n#b#e#b#i4310018:##t4310018# #e1개#k#n를 #r#i4310312:##t4310312# #e1개#k#n에 구매할래?\r\n\r\n", 0x04);
        }
    } else if (status == 3) {
        if (sel == 0) {
        } else if (sel == 1) {
            var nowcoin = cm.getPlayer().getKeyValue(100828);

            cm.sendGetNumber("몇 개의 #b#i4310018:##t4310018##k을 구매할래? \r\n #e - 현재 보유한 #i4310312:##t4310312# : #r"+cm.getPlayer().getKeyValue(100794, "point")+" 개#k#n", nowcoin / 1, 1, nowcoin / 1);
        }
    } else if (status == 4) {
        if (sel == 0) {
        } else if (sel == 1) {
            var nowcoin = cm.getPlayer().getKeyValue(100828);
            coin = selection;
            needcoin = selection * 1;
            cm.sendYesNoS("정말 #b#i4310018:##t4310018# #e"+coin+"#n#k개를 구매할래?\r\n\r\n #e - 필요한 #i4310312:##t4310312# : #r"+needcoin+" 개\r\n\r\n\r\n#n#r", 0x04);
        }
    } else if (status == 5) {
        if (sel == 0) {
        } else if (sel == 1) {
            cm.sendOkS("아주 좋은 거래야!\r\n\r\n#b#i4310018:##t4310018##k을 #r#e"+coin+"#n#k개 지급했어!", 0x04, 9062518);
            cm.getPlayer().AddCoin(100828, -needcoin);
            cm.getPlayer().setKeyValue(100712, "point", (cm.getPlayer().getKeyValue(100712, "point") + coin) + "");
            cm.dispose();
        }
    }
}

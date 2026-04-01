


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    히어로 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062558

    엔피시 이름 : 블랙빈

    엔피시가 있는 맵 : 메이플 LIVE : LIVE 스튜디오 (993194000)

    엔피시 설명 : 검은콩샵


*/

var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getClient().getCustomData(501469, "shopTuto") != null) {
            cm.openShop(31);
            cm.dispose();
        } else {
            cm.sendNextS("(레전드코인샵에 처음 온 #b#e초보 유저#n#k로군...?)", 4);
        }
    } else if (status == 1) {
        cm.sendNextPrevS("하핫! 선생님은 저희 레전드코인샵 에 처음 오셨군요.\r\n#r#e#i4310027# #t4310027##n#k을 가져오시면 제가 귀한 물건을 보여드리지요.", 4)
    } else if (status == 2) {
        cm.sendNextPrevS("아직은물건이 별로없지만...\r\n하핫! 그래도사용할곳이 있으니 #r#e#i4310027# #t4310027##n#k을 많이 모아오시죠.", 4);
    } else if (status == 3) {
        //상점오픈
        cm.getClient().setCustomData(501469, "shopTuto", "1");
        cm.openShop(31);
        cm.dispose();
    }
}

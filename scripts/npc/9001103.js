


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
            cm.openShop(23);
            cm.dispose();
        } else {
            cm.sendNextS("(자네는 황금송편에대해 알고있나 ?)", 4);
        }
    } else if (status == 1) {
        cm.sendNextPrevS("아이테르 필드에 몬스터를 사냥하면 \r\n#r#e#i4031187# #t4031187##n#k을 얻을수있다네...", 4)
    } else if (status == 2) {
        cm.sendNextPrevS("해당아이템 을 가져다준다면 ...\r\n귀한 아이템을 주겠다네 #r#e#i1122161# #t1122161##n#k #i4031187# #t4031187# 해당아이템을 많이오게나...", 4);
    } else if (status == 3) {
        //상점오픈
        cm.getClient().setCustomData(501469, "shopTuto", "1");
        cm.openShop(23);
        cm.dispose();
    }
}

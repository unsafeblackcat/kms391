


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    히어로 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062558

    엔피시 이름 : 블랙빈

    엔피시가 있는 맵 : 메이플 LIVE : LIVE 스튜디오 (993194000)

    엔피시 설명 : 검은콩샵


*/

var status = -1, sel = 0;
var tuto = false;
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
        tuto = cm.getClient().getCustomData(501469, "gameTuto") != null ? true : false;
        if (tuto) {
            cm.sendSimpleS("메이플 LIVE에서 개최하는 E-SPORTS에 참가해 봐!\r\n\r\n#L0# #b#e<해변의 지배자>#n#k에 대해 물어본다.#l\r\n#L1# #b#e<참여 시간>#n#k에 대해 물어본다.#l\r\n#L2# #b#e<게임 보상>#n#k에 대해 물어본다.#l\r\n\r\n#L99# 궁금한 것이 없다.#l", 4)
        } else {
            cm.sendNextS("오! 너는 신입 크리에이터로군! 혹시 들어 봤나?!", 4);
        }
    } else if (status == 1) {
        sel = selection;
        if (tuto) {
            switch (sel) {
                case 0:
                    cm.sendNextS("\r\n#b#e<해변의 지배자>#n#k는 최소 10명 ~ 최대 40명의 인원이 모여 물방울을 쏴서 상대를 공격하고 제한 시간 3분 동안 #b더 많은 점수를 획득한 팀#k이 승리하지!", 4, 9062545)
                    break;
                case 1:
                    cm.sendNextS("\r\n#b#e<해변의 지배자>#n#k는 오전 10시부터 자정 전까지\r\n#b매시 15분, 45분#k에 머리 위 #b초대장#k을 통해 참가할 수 있어.", 4, 9062545)
                    break;
                case 2:
                    cm.sendNextS("\r\n#b#e<해변의 지배자 보상>#n#k\r\n\r\n게임 결과에 따라 #e개인 점수#n와 #e팀 점수#n를 정산한\r\n#e#r게임 포인트#k#n를 획득할 수 있어!\r\n\r\n#e[개인 보상]#n\r\n - 개인 점수 1점 당 10 게임 포인트 (최대 #r#e100#k#n 게임 포인트)\r\n\r\n#e[팀 보상]#n\r\n - 소속팀 승리 시 #r#e200#k#n 게임 포인트\r\n - 소속팀 무승부 시 #r#e150#k#n 게임 포인트\r\n - 소속팀 패배 시 #r#e100#k#n 게임 포인트", 4, 9062545)
                    break;
                case 99:
                    cm.dispose();
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n메이플 LIVE에서 지금 사상 최대 규모의\r\n#b#e#fs15#E-Sports#n#k#fs15#를 개최했다고~!", 4, 9062547);
        }
    } else if (status == 2) {
        if (tuto) {
            switch (sel) {
                case 0:
                    cm.sendNextPrevS("\r\n#e[팀 구분]#n\r\n\r\n게임에 입장하면 #rRED#k 또는 #bBLUE#k 팀에 소속될 거야.\r\n\r\n소속팀에 따라 캐릭터의 #e명찰, HP, 사용하는 스킬의 색상#n이 다르니까 전장에서 아군과 적군을 구별할 수 있겠지?\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/0#", 4, 9062545)
                    break;
                case 1:
                    cm.dispose();
                    cm.openNpc(9062545);
                    break;
                case 2:
                    cm.sendNextPrevS("\r\n#b#e<해변의 지배자 보상>#n#k\r\n\r\n #e#r2,400#k#n 게임 포인트를 모으면 E-Sports #e#r명예 보상#k#n을 줄게!\r\n#r※ 보상은 월드 당 1회만 받을 수 있습니다.\r\n\r\n#fUI/UIWindow2.img/Quest/quest_info/summary_icon/reward#\r\n#b#e#i2633574:# #t2633574:# :  1개", 4, 9062545)
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n수많은 크리에이터들의 미니게임 멸망전!\r\n지금 바로 보여줄게. 누가? 우리가!", 4, 9062548);
        }
    } else if (status == 3) {
        if (tuto) {
            switch (sel) {
                case 0:
                    cm.sendNextPrevS("\r\n#e[이동]#n\r\n\r\n캐릭터는 마우스 커서가 있는 방향을 바라보게 돼!\r\n키보드 #r#eW,A,S,D#k#n를 누르면 #r#e캐릭터를 이동#k#n시킬 수 있고,\r\n키보드 #b#eSPACE 바#k#n를 누르면 #b#e이동 중인 방향#k#n으로 순간적으로 #b#e돌진#k#n할 수 있어.\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/1#", 4, 9062545)
                    break;
                case 2:
                    cm.sendNextPrevS("단, 게임 포인트는 #b캐릭터 당 #e하루에 300 게임 포인트#n#k까지 획득할 수 있어.\r\n\r\n그리고 게임 포인트 #r#e총 누적량#n은 월드 내 모든 캐릭터#k가\r\n공유된다는 사실도 꼭 기억해 둬!", 4, 9062545)
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n엄청난 경쟁 속에서 당당히 승리하는 모습이 중계되면?\r\n너의 채널 구독에 영향이 갈지도!", 4, 9062547);
        }
    } else if (status == 4) {
        if (tuto) {
            switch (sel) {
                case 0:
                    cm.sendNextPrevS("\r\n#e[공격] - 기본#n\r\n\r\n#b#e마우스 좌 클릭#k#n을 하면 마우스 커서의 방향으로 #b#e물방울#k#n을 던질 수 있어. 물방울을 던져 상대팀 캐릭터를 맞추면 상대의 #b#eHP#k#n를 깎을 수 있지.\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/2#", 4, 9062545)
                    break;
                case 2:
                    cm.dispose();
                    cm.openNpc(9062545);
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n척박한 E-세상에서 살아남으려면 치열하게 싸워야 한다고! 누가? 우리가??", 4, 9062548);
        }
    } else if (status == 5) {
        if (tuto) {
            switch (sel) {
                case 0:
                    cm.sendNextPrevS("\r\n#e[공격] - 강화#n\r\n\r\n그리고 맵 곳곳에 나타나는 #b#e<물방울>#k#n에 충돌하면 #b#e20초간 기본 공격이 강화#k#n돼서, 클릭 한 번에 더 높은 공격력을 가진 물방울이 4개씩 던져질 거야.\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/3#", 4, 9062545)
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n아니아니, 뜨거운 경쟁의 주인공은 바로 #b#e#h0##k#n!\r\n지금 바로 참가해서 화끈한 경기를 보여줘~!", 4);
        }
    } else if (status == 6) {
        if (tuto) {
            switch (sel) {
                case 0:
                    cm.sendNextPrevS("\r\n#e[점수]#n\r\n\r\n물방울 공격으로 상대의 HP가 0이되면 상대가 처치되고,\r\n#e처치한 캐릭터 1명 당 1점씩 #r개인 점수#k#n를 쌓을 수 있어!\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/4#", 4, 9062545)
                    break;
            }
        } else {
            cm.getClient().setCustomData(501469, "gameTuto", "1");
            cm.dispose();
            cm.openNpc(9062545);
        }
    } else if (status == 7) {
        if (tuto) {
            switch (sel) {
                case 0:
                    cm.sendNextPrevS("\r\n#e[점수]#n\r\n\r\n그리고 #e1분마다#n 맵 중앙에 등장하는 #e야자수#n를 처치하면\r\n10개의 #r#e<빛 방울>#k#n이 주변에 나타나고,\r\n<빛 방울>에 충돌하면 #e1개당 1점씩 #r개인 점수#k#n를 추가로 누적할 수 있지!\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/5#", 4, 9062545)
                    break;
            }
        }
    } else if (status == 8) {
        if (tuto) {
            switch (sel) {
                case 0:
                    cm.sendNextPrevS("\r\n게임이 종료되면 획득한 개인 점수와 팀의 게임 결과에 따라 #r#e게임 포인트#k#n를 보상으로 줄게.", 4, 9062545)
                    break;
            }
        }
    } else if (status == 9) {
        if (tuto) {
            switch (sel) {
                case 0:
                    cm.sendNextPrevS("\r\n우리 팀이 대결에서 지더라도 내가 열심히 참여했다면 승리에 준하는 보상을 받아 갈 수 있으니 꼭 멋진 대결을 펼쳐 줘!", 4, 9062545)
                    break;
            }
        }
    } else if (status == 10) {
        if (tuto) {
            switch (sel) {
                case 0:
                    cm.dispose();
                    cm.openNpc(9062545);
                    break;
            }
        }
    }
}

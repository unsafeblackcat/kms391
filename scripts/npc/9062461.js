


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    닥나 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062461

    엔피시 이름 : 피아

    엔피시가 있는 맵 : 네오 캐슬 : 네오 캐슬 (993189100)

    엔피시 설명 : 메이플월드 게임 대표


*/

var status = -1, sel;

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
        if (status == 1) {
            if (sel == 1 || sel == 3 || sel == 5) {
                cm.sendNextS("\r\n그러면 게임에 참여하고 싶을 때 다시 찾아와줘~", 0x04);
                cm.dispose();
                return;
            }
        }
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getKeyValue(210223, "pia_tuto") > 0) {
            var talk = "내가 준비한 미니게임에서 #b#e#i4310307:# #t4310307##n#k을 얻어봐~\r\n"
            talk += "#L1# #r#e<알록달록 초대장>#n 참여하기#l#k\r\n"
            talk += "#L2# #b#e<알록달록 초대장>#n 설명듣기#l#k\r\n\r\n"
            talk += "#L3# #r#e<높이높이 네오캐슬>#n 참여하기#l#k\r\n"
            talk += "#L4# #b#e<높이높이 네오캐슬>#n 설명듣기#l#k\r\n\r\n"
            talk += "#L5# #r#e<레인보우 러쉬>#n 참여하기#l#k\r\n"
            talk += "#L6# #b#e<레인보우 러쉬>#n 설명듣기#l#k\r\n\r\n"
            talk += "#L100# 더 이상 궁금한 것이 없어.#l\r\n\r\n"
            // talk += "#r※ #t4310307#은 캐릭터마다 일일 100개까지 획득 가능합니다.#k"
            cm.sendNextS(talk, 0x04);
        } else {
            cm.sendNextS("\r\n안녕, #h0#~\r\n\r\n난 #b메이플 월드의 미니게임 대표#k로\r\n#b#e<NEO 캐슬>#n#k에 놀러 온 피아야~", 0x04);
        }
    } else if (status == 1) {
        sel = selection;
        if (cm.getPlayer().getKeyValue(210223, "pia_tuto") > 0) {
            switch (sel) {
                case 1://알록달록 초대장 참여하기
                    cm.sendYesNoS("\r\n지금 #b#e<알록달록 초대장>#n#k에 참여할래~?\r\n\r\n#r(게임 중에는 해상도가 1024x768로 변경됩니다.)#k", 0x04);
                    break;
                case 2://알록달록 초대장 설명듣기
                    cm.sendNextS("\r\n#b#e알록달록 초대장#n#k은 네오 캐슬로 사람들을 초대하기 위해\r\n초대장을 보내는 것이 목표인 게임이야~", 0x04);
                    break;
                case 3://높이높이 네오캐슬 참여하기
                    cm.sendYesNoS("\r\n지금 #b#e<높이높이 네오캐슬>#n#k에 참여할래~?\r\n\r\n#r(게임 중에는 해상도가 1024x768로 변경됩니다.)#k", 0x04);
                    break;
                case 4://높이높이 네오캐슬 설명듣기
                    cm.sendNextS("\r\n#b#e높이높이 네오캐슬#n#k은 우리만의 탑을 높게 쌓아\r\n올리는 게임이야~", 0x04);
                    break;
                case 5://레인보우 러쉬 참여하기
                    cm.sendYesNoS("\r\n지금 #b#e<레인보우 러쉬>#n#k에 참여할래~?\r\n\r\n#r(게임 중에는 해상도가 1366x768 변경됩니다.)#k", 0x04);
                    break;
                case 6://레인보우 러쉬 설명듣기
                    cm.sendNextS("\r\n#b#e레인보우 러쉬#n#k는 이쁜 무지갯빛 길을 달리는 게임이야~", 0x04);
                    break;
                case 100:
                    cm.dispose();
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n나는 혼자 즐길 수 있는 게임들을 준비해왔어~\r\n내가 준비한 #b#e미니게임#n#k에서 너의 실력을 보여주면,\r\n이 이쁜 #b#e#i4310307:# #t4310307##n#k을 조금 나눠줄게.", 0x04);
        }
    } else if (status == 2) {
        if (cm.getPlayer().getKeyValue(210223, "pia_tuto") > 0) {
            switch (sel) {
                case 1://알록달록 초대장 입장하기
                    if (cm.getPlayer().getParty() != null) {
                        cm.sendOkS("파티를 맺은 상태에서는 #b#e알록달록 초대장#n#k을 할 수 없어~\r\n파티를 해제하고 다시 말을 걸어줘~", 0x04);
                        cm.dispose();
                    } else {
                        cm.EnterColorfulInvitationCard();
                        cm.dispose();
                    }
                    break;
                case 2://알록달록 초대장 설명듣기
                    cm.sendNextPrevS("\r\n초대장을 보내기 위해선 대기하고 있는 초대장과\r\n#b#e동일한 색상의 버튼#n#k을 누르기만 하면 돼~", 0x04);
                    break;
                case 3://높이높이 네오캐슬 입장하기
                    if (cm.getPlayer().getParty() != null) {
                        cm.sendOkS("#b#e높이높이 네오캐슬#n#k은 파티 상태에서 할 수 없어~\r\n파티를 해제하고 다시 말을 걸어줘~", 0x04);
                        cm.dispose();
                    } else {

                    }
                    break;
                case 4://높이높이 네오캐슬 설명듣기
                    cm.sendNextPrevS("\r\n한 층 한 층이 옆에서 나타나면 #r#eSpace#k#n 키를 눌러서 멈출 수 있는데, 이때 집중해서 #e#b타이밍#k#n을 잘 맞추는 것이 중요해~", 0x04);
                    break;
                case 5://레인보우 러쉬 입장하기
                    if (cm.getPlayer().getParty() != null) {
                        cm.sendOkS("파티를 맺은 상태에서는 #b#e레인보우 러쉬#n#k를 할 수 없어~\r\n파티를 해제하고 다시 말을 걸어줘~", 0x04);
                        cm.dispose();
                    } else {

                    }
                    break;
                case 6://레인보우 러쉬 설명듣기
                    cm.sendNextPrevS("\r\n신비한 #b#e눈꽃 순록#n#k을 타고 무지갯빛 길을 마음껏 달려봐~", 0x04);
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n#b#e카린의 <네오 젬 샵>#n#k에서 이걸로 다양한 #b#e치장 물품#n#k들을 교환할 수 있어~", 0x04);
        }
    } else if (status == 3) {
        if (cm.getPlayer().getKeyValue(210223, "pia_tuto") > 0) {
            switch (sel) {
                case 2://알록달록 초대장 설명듣기
                    cm.sendNextPrevS("\r\n초대장에는 #b#e3가지 종류#n#k가 있어~\r\n\r\n각각 다른 모양과 색깔을 지니고 있으니 구분은 쉬울 거야~\r\n\r\n#i03801591##i03801592##i03801593#", 0x04);
                    break;
                case 4://높이높이 네오캐슬 설명듣기
                    cm.sendNextPrevS("\r\n바로 #e#b아래층과 동일한 위치#k#n에 멈추면 반짝이는 이펙트와 함께 많은 양의 점수를 획득할 수 있어~", 0x04);
                    break;
                case 6://레인보우 러쉬 설명듣기
                    cm.sendNextPrevS("\r\n점점 더 빨라지는 눈꽃 순록을 타고 #e#b좌우 방향키#k#n를 이용해 장애물을 피해야 해~\r\n\r\n#b#e순발력#n#k이 필요하겠지~?", 0x04);
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n긴 설명은 필요 없겠지~? \r\n\r\n자, 이제 너의 게임 실력을 보여줄 시간이야~", 0x04);
        }
    } else if (status == 4) {
        if (cm.getPlayer().getKeyValue(210223, "pia_tuto") > 0) {
            switch (sel) {
                case 2://알록달록 초대장 설명듣기
                    cm.sendNextPrevS("\r\n하지만 주의해야 할 #r#e특별한 초대장#n#l#k이 하나 있어~\r\n#i03801594#\r\n이 초대장은 #b#e동서남북 종이접기 모양#n#k을 띠고 있는데\r\n본 모습을 숨기고 있다가 전송 직전에 앞서 말한 세 초대장의 모습으로 변할 거야~", 0x04);
                    break;
                case 4://높이높이 네오캐슬 설명듣기
                    cm.sendNextPrevS("\r\n범위 내에 멈추지 못한다고 무조건 실패하는 건 아니지만,\r\n쌓는 층의 폭이 줄어들 거야~\r\n\r\n물론 완전히 다른 위치에 멈추면 바로 #e<GAME OVER>#n~", 0x04);
                    break;
                case 6://레인보우 러쉬 설명듣기
                    cm.sendNextPrevS("\r\n길을 가로막는 #r#e먹구름#n#l#k을 주의해~ \r\n부딪히면 그대로 #e<GAME OVER>#n~", 0x04);
                    break;
            }
        } else {
            cm.dispose();
            cm.getPlayer().setKeyValue(210223, "pia_tuto", "1");
            cm.openNpc(9062461);
        }
    } else if (status == 5) {
        switch (sel) {
            case 2://알록달록 초대장 설명듣기
                cm.sendNextPrevS("\r\n그리고 초대장을 보내다 보면 #r#e피버 게이지#n#l#k가 차오를 거야~\r\n\r\n#r#e피버 게이지#n#l#k가 가득 차면 #b#e스페이스 바#n#k를 빠르게 눌러\r\n더 빠르게 초대장을 보낼 수 있어~\r\n\r\n#i3801199#", 0x04);
                break;
            case 4://높이높이 네오캐슬 설명듣기
                cm.sendNextPrevS("\r\n폭이 줄어들수록 높이 쌓기 더 어렵다는 건 알겠지~?\r\n그래서 높은 #e#b집중력#k#n이 필요해~", 0x04);
                break;
            case 6://레인보우 러쉬 설명듣기
                cm.sendNextPrevS("\r\n마지막으로 더 멀리 달릴수록 더 많은 #b#e보상#n#k을 줄게~\r\n이제 #b#e레인보우 러쉬#n#k에 대해 이해했어~?\r\n\r\n그럼 시작하자~", 0x04);
                cm.dispose();
                break;
        }
    } else if (status == 6) {
        switch (sel) {
            case 2://알록달록 초대장 설명듣기
                cm.sendNextPrevS("\r\n아름다운 네오캐슬에 많은 사람이 올 수 있도록 초대장을\r\n많이 보내줘~!\r\n\r\n물론 #b#e보상#n#k도 빠짐없이 줄게~", 0x04);
                cm.dispose();
                break;
            case 4://높이높이 네오캐슬 설명듣기
                cm.sendNextPrevS("\r\n마지막으로 탑을 높이 쌓을수록 더 많은 #b#e보상#n#k을 줄게~\r\n이제 #b#e높이높이 네오캐슬#n#k에 대해 이해했어~?\r\n\r\n어서 시작하자~", 0x04);
                cm.dispose();
                break;
        }
    }
}

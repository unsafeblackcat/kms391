


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    나로 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062550

    엔피시 이름 : 슈피겔라

    엔피시가 있는 맵 : 메이플 LIVE : LIVE 스튜디오 (993194000)

    엔피시 설명 : 메이플 버라이어티


*/
importPackage(Packages.server.events);

var status = -1, tuto = false;

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
        if (status == 2) {
            cm.sendOkS("그러면 방송에 참여하고 싶을 때 다시 찾아와주세요~", 4, 9062550)
            cm.dispose();
            return;
        }
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        tuto = cm.getClient().getCustomData(501469, "gameTuto2") != null ? true : false;
        if (tuto) {
            cm.sendSimpleS("#b#e메이플 버라이어티#n#k 제 1탄~! #r#e탕.윤.식.당!#l#k\r\n#L2# #b#e<탕윤 식당>#n 파티플레이(1~3인으로 도전)#l#k\r\n\r\n#L3# #b#e<탕윤 식당>#n 설명 듣기#l#k\r\n#L4# 오늘 남은 보상 획득 가능 횟수 확인\r\n\r\n#L100# 더 이상 궁금한 것이 없어.#l", 4)
            cm.sendSimpleS("#r현재 오류로 인해 이용이 불가능합니다.#k", 4)
        } else {
            cm.sendNextS("\r\n앗! 거기 #b#e신입 크리에이터#n#k님!\r\n\r\n혹시 #b#e구독자#n#k 수를 폭발적으로 늘리면서 #r#e화끈한 보상#n#k을\r\n얻을 수 있는 방송 컨셉을 찾으시나요?", 4);
        }
    } else if (status == 1) {
        sel = selection;
        if (tuto) {
            switch (sel) {
                case 1:
                case 2:
                    //빠른시작 (매칭)
                    cm.sendNextS("\r\n지금 #b#e<탕윤 식당>#n#k에 참여시겠습니까~?#k\r\n\r\n매칭 완료 후 NPC대화, 상점/창고/택배 이용등을 하고 있다면 매칭이 #r#e취소#n#k될 수 있다는 점 명심해주세요~!\r\n\r\n(게임 중에는 해상도가 1366x768로 변경됩니다.)", 4)
                    break;
                case 3:
                    //설명듣기
                    cm.sendNextS("#e[이벤트 기간]\r\n\r\n2021년 06월 17일 (목) 점검 후 ~ \r\n2021년 07월 14일 (수) 오후 11시 59분", 4);
                    break;
                case 4:
                    //보상 횟수 확인
                    if (cm.getPlayer().getV("TyKitchenReward") == null) {
                        cm.getPlayer().addKV("TyKitchenReward", "0")
                    }
                    cm.sendNextS("오늘 남은 보상 획득 가능 횟수는 #b#e"+(2 - parseInt(cm.getPlayer().getV("TyKitchenReward")))+"회#n#k 입니다.", 4)
                    cm.dispose();
                    break;
                case 100:
                    cm.dispose();
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n그렇다면, 지금 바로!\r\n\r\n#b#e메이플 버라이어티#n#k에 참여해보세요!", 4);
        }
    } else if (status == 2) {
        if (tuto) {
            switch (sel) {
                case 1:
                case 2:
                    //빠른시작 (매칭)
                    cm.sendYesNoS("\r\n#b#e<탕윤 식당>#n#k에 참여하시면 적용되어 있는\r\n#fs16##r#e모든 버프 효과가 해제#n#k#fs15#된답니다.\r\n\r\n정말 참여하시겠어요?", 4)
                    break;
                case 3:
                    //설명듣기
                    cm.sendNextPrevS("\r\n#b#e<탕윤 식당>#n#k은 최소 1명 ~ 최대 3명의 인원이 요리를 만들고 배달까지 완료해서 제한 시간 #e30분#n 동안 #b#e50,000#n#k 포인트를 획득하는 것이 목표랍니다!", 4);
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n현재 #r#e전설의 셰프 탕윤#n#k님과 식당 경영을 체험할 수 있는\r\n\r\n#b#e<탕윤 식당>#n#k을 진행하고 있답니다!", 4);
        }
    } else if (status == 3) {
        if (tuto) {
            switch (sel) {
                case 1:
                    //빠른시작 (매칭)
                    cm.sendNextPrevS("\r\n#b#e<탕윤 식당>#n#k에 참여하시면 적용되어 있는\r\n#fs16##r#e모든 버프 효과가 해제#n#k#fs15#된답니다.\r\n\r\n정말 참여하시겠어요?", 4)
                    break;
                case 2:
                    if (cm.getPlayer().getParty() == null) {
                        cm.sendOkS("1~3인 파티를 맺어야만 #b#e파티 플레이#n#k를 할 수 있답니다.\r\n\r\n#e개인 매칭#n으로 도전을 원하시면 #r#e빠른 시작#n#k을 선택해주세요.", 4, 9062550);
                        cm.dispose();
                        return;
                    }
                    if (cm.getPlayerCount(993194500) >= 1) {
                        cm.sendOkS("이미 다른 #r#e크리에이터#n#k분이 방송중에 계시네요! 다른 채널을 이용 해주시겠어요?", 4, 9062550);
                        cm.dispose();
                        return;
                    }
                    MapleTyoonKitchen.startPartyGame(cm.getPlayer());
                    cm.dispose();
                    break;
                case 3:
                    //설명듣기
                    cm.sendNextPrevS("\r\n좌측에 위치한 #b#e<주문판>#n#k에 들어오는 주문을 확인하고\r\n레시피대로 음식을 만들어서 배달해보세요~!", 4);
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n#r#e뜨거운 주방#n#k 안에서 펼쳐지는 #e요리#n의 향연!\r\n\r\n#r#e속도가 생명#n#k! 촌각을 다투는 #e음식 배달#n까지!", 4);
        }
    } else if (status == 4) {
        if (tuto) {
            cm.sendNextPrevS("\r\n#e[재료 수집]#n\r\n\r\n#b#e주방 공간 좌측#n#k에 위치한 #e5종#n의 재료 앞에서 #r#eSpace키#k#n\r\n키다운으로 해당하는 재료를 획득할 수 있답니다.\r\n\r\n", 4);
        } else {
            cm.sendNextPrevS("\r\n#b#e구독자#n#k 오르는 소리가 들리시나요?\r\n\r\n#r#e경험치#n#k 오르는 소리가 들리시나요?\r\n\r\n#e지금~ 바로! 참여하세요!#n", 4);
        }
    } else if (status == 5) {
        if (tuto) {
            cm.sendNextPrevS("\r\n#e[재료 놓기]#n\r\n\r\n#b#e주방 공간 중앙#n#k에 위치한 #e조리대#n 앞에서 #r#eSpace키#k#n\r\n키다운으로 현재 획득한 재료를 놓을 수 있답니다.\r\n\r\n#e각 주문 번호에 해당하는 조리대#n에서만 해당 음식을\r\n조리할 수 있다는 점을 잊지 마세요!", 4);
        } else {
            cm.getClient().setCustomData(501469, "gameTuto2", "1");
            cm.dispose();
            cm.openNpc(9062550);
        }
    } else if (status == 6) {
        cm.sendNextPrevS("\r\n#e[가공 도구 들기]#n\r\n\r\n#b#e주방 공간 우측#n#k에 위치한 #e3종#n의 가공 도구 앞에서\r\n#r#eSpace키#k#n 키다운으로 도구를 획득할 수 있답니다.\r\n\r\n#b#e가공 도구#n#k를 획득한 상태에서만 재료 가공을 할 수 있다는 점을 잊지 마세요!", 4);
    } else if (status == 7) {
        cm.sendNextPrevS("\r\n#e[재료 가공]#n\r\n\r\n#b#e주방 공간 중앙#n#k에 위치한 #e조리대#n 앞에서 #r#eSpace키#n#k\r\n키다운으로 해당 조리대에 놓인 재료를 가공할 수 있답니다.\r\n\r\n#e각 주문 번호에 해당하는 조리대#n에서만 해당 음식을\r\n조리할 수 있다는 점을 잊지 마세요!", 4);
    } else if (status == 8) {
        cm.sendNextPrevS("\r\n#e[음식 배달]#n\r\n\r\n#b#e음식이 완성된 조리대#n#k는 음식을 들 수 있는 상태가 되며\r\n해당 #e조리대#n 앞에서 #r#eSpace키#k#n 키다운으로 배달을 진행할 수 있답니다.\r\n\r\n우측 배달 공간에서 #e몬스터#n들의 방해를 피해 주문한 손님\r\n앞에서 #r#eSpace키#k#n 키다운으로 배달 완료!", 4);
    } else if (status == 9) {
        cm.sendNextPrevS("\r\n#b#e진정한 크리에이터#n#k라면 역시 실전이죠!\r\n\r\n어서 직접 체험해보시는 것은 어떠신가요?", 4);
    } else if (status == 10) {
        cm.dispose();
    }
}

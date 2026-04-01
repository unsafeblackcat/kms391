


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    신궁 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062521

    엔피시 이름 : 조그만 정령

    엔피시가 있는 맵 : 블루밍 포레스트 : 꽃 피는 숲 (993192000)

    엔피시 설명 : 블루밍 레이스


*/

var status = -1;
var sel = 0;
var count = 0, additem = 0;
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
        if (status == 0) {
            cm.dispose();
            return;
        }
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getMapId() == 993192600) {
            cm.sendYesNoS("#b<블루밍 레이스>#k해낼줄 알았어!\r\n아직 레이스가 진행 중인데 먼저 나갈래?", 4, 9062521);
        } else if (cm.getPlayer().getMapId() == 993192501) {
            if (cm.getPlayer().getV("BloomingRaceRank") == null) {
                cm.getPlayer().addKV("BloomingRaceRank", "0");
            }
            rank = parseInt(cm.getPlayer().getV("BloomingRaceRank"));
            achieve = parseInt(cm.getPlayer().getV("BloomingRaceAchieve"));
            count = rank == 1 || rank == 2 || rank == 3 ? 100 : 0;
            if (rank == 1 || rank == 2 || rank == 3) {
                additem = 4310013;
            }
            if (rank > 3) {
                count = 50;
            } else if (rank <= 0) {
                count = achieve == 19 ? 10 : achieve == 39 ? 20 : achieve == 59 ? 30 : achieve == 79 ? 40 : 0;
            }
            count *= 3;
            if (count > 0) {
                cm.sendYesNoS("#b<블루밍 레이스>#k는 재밌었어?\r\n이번에 획득한 #i4310310:# #b#z4310310:##k은 #b" + count + "개#k야 다음 레이스에서도 꼭 다시 만날 수 있길 바랄게.\r\n\r\n(#b예#k를 누르시면 원래 있던 곳으로 돌아갑니다.)", 4, 9062521);
            } else {
                cm.sendNextS("아... 다른 친구들처럼 열심히 했으면 좋았을 탠데 아쉽지만 #b블루밍 코인#k을 줄 수가 없어 다음엔 꼭 열심히 해줘.", 4, 9062521);
            }
        } else {
            cm.sendNextS("안녕, 궁금한 게 있어? 얼마든지 물어봐.\r\n\r\n#L1# #b#e<블루밍 레이스>#n#k에 대해 물어본다.#l\r\n#L2# #b#e<블루밍 레이스 진행방법>#n#k에 대해 물어본다.#l\r\n#L3# #b#e<참여 시간>#n#k에 대해 물어본다.#l\r\n#L4# #b#e<게임 보상>#n#k에 대해 물어본다.#l.\r\n\r\n#L0# 궁금한 것이 없다.#l", 4, 9062521);
        }
    } else if (status == 1) {
        sel = selection;
        if (cm.getPlayer().getMapId() == 993192600 || cm.getPlayer().getMapId() == 993192501) {
            if (cm.getPlayer().getMapId() == 993192501) {
                if (count > 0) {
                    if (cm.getClient().getQuestStatus(50006) == 1 && cm.getClient().getCustomKeyValue(50006, "1") != 1) {
                        cm.getClient().setCustomKeyValue(50006, "1", "1");
                    }
                    cm.getPlayer().setKeyValue(100794, "point", (cm.getPlayer().getKeyValue(100794, "point") + count) + "");
                    cm.getPlayer().dropMessage(5, "<블루밍 레이스> 블루밍 코인 " + count + "개 획득 하였습니다.")
                    if (additem > 0) {
                        cm.getPlayer().gainCabinetItem(additem, 1);
                        cm.getPlayer().dropMessage(5, "<블루밍 레이스> 순위권에 들어 추가 보상을 획득하였습니다. 메이플 보관함을 확인 해주세요.")
                    }
                }
                cm.getPlayer().removeV("BloomingRaceRank");
                cm.getPlayer().removeV("BloomingRaceAchieve");
                if (cm.getPlayer().getV("returnM") != null) {
                    cm.warp(parseInt(cm.getPlayer().getV("returnM")), 0);
                    cm.getPlayer().removeV("returnM");
                    cm.dispose();
                } else {
                    cm.warp(993192000, 0);
                }
            } else {
                cm.warp(993192501);
            }
            cm.dispose();
        } else {
            switch (sel) {
                case 0:
                    cm.dispose();
                    break;
                case 1:
                    cm.sendNextS("\r\n블루밍 포레스트에서 이곳저곳 돌아다니다, #b엄청난 장소#k를 발견했는데 그 곳을 나랑 정령들이 약간 손을 봤어.", 4, 9062521);
                    break;
                case 2:
                    cm.sendNextS("\r\n#b제한 시간 5분#k 동안 모든 친구들이 같은 지점에서 시작해 골인까지 도착해야 하는 간단한 게임이야.", 4, 9062521);
                    break;
                case 3:
                    cm.sendNextS("\r\n#b#e<블루밍 레이스>#n#k는 #b#e오전 10시#n#k부터 #b#e자정 전#n#k까지 #b#e매시 15분, 45분#n#k에 머리 위 #b#e초대장#n#k을 통해 입장할 수 있어.", 4, 9062521);
                    break;
                case 4:
                    cm.sendNextS("\r\n#b#e<블루밍 레이스 보상>#n#k\r\n#r[1 ~ 3등]#k\r\n - #i4310310:#블루밍 코인 #b300#k개\r\n - #i4310013# 페스티벌 룰렛 티켓 #b1#k개\r\n#r[등수 외]#k\r\n - #i4310310:#블루밍 코인 #b0~150#k개\r\n#k", 4, 9062521);
                    break;
            }
        }
    } else if (status == 2) {
        switch (sel) {
            case 1:
                cm.sendNextPrevS("\r\n친구들끼리 경쟁할 수 있는 재미난 장소로 만들었는데...", 4, 9062521);
                break;
            case 2:
                cm.sendNextS("\r\n게임의 구성은 총 5단계로 진행되고, 랭킹에 들지 못해도 더 많이 갈수록 보상을 얻을 수 있어.", 4, 9062521);
                break;
            case 3:
                cm.sendNextS("\r\n으헤헷, 친구들이 즐거워하는 모습을 생각하며 열심히 만든 초대장이니까 꼭 와 줬으면 해", 4, 9062521);
                break;
            case 4:
                cm.dispose();
                cm.openNpc(9062521);
                break;
        }
    } else if (status == 3) {
        switch (sel) {
            case 1:
                cm.sendNextPrevS("\r\n히힛, #b#e블루밍 레이스#n#k라고 이름을 붙여봤어. 어때? 코스가 제법 만만하진 않을 거야. 하지만 충분히 해낼 수 있지?", 4, 9062521);
                break;
            case 2:
                cm.sendNextS("\r\n그러니까... 랭킹에 들지 못할 거 같더라도 포기하지 말고 끝까지 달려봐!", 4, 9062521);
                break;
            case 3:
                cm.dispose();
                cm.openNpc(9062521);
                break;
        }
    } else if (status == 4) {
        switch (sel) {
            case 1:
                cm.dispose();
                cm.openNpc(9062521);
                break;
            case 2:
                cm.sendNextS("\r\n아! 당연히 랭킹 내에 들어가는 친구에겐 더 좋은 보상을 준비했으니 기대하도록 해.", 4, 9062521);
                break;
        }
    } else if (status == 5) {
        switch (sel) {
            case 2:
                cm.dispose();
                cm.openNpc(9062521);
                break;
        }
    }
}

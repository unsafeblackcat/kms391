


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    닥나 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062461

    엔피시 이름 : 피아

    엔피시가 있는 맵 : 네오 캐슬 : 네오 캐슬 (993189100)

    엔피시 설명 : 메이플월드 게임 대표


*/
importPackage(Packages.server.games);

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
        if (cm.getPlayer().getKeyValue(210223, "maru_tuto") > 0) {
            var talk = "미니게임에서 실력을 보여주면 #r#e#i4310307:# #t4310307##n#k을 줄게!\r\n히힛! 나보다 게임을 잘하는 사람은 없을걸?!\r\n"
            talk += "#L3# #r#e<초능력 윷놀이>#n 참여하기#l#k\r\n"
            talk += "#L30# #b#e<초능력 윷놀이>#n 설명 듣기#l#k\r\n\r\n"
            talk += "#L4# #r#e<메이플 원카드>#n 참여하기#l#k\r\n"
            talk += "#L40# #b#e<메이플 원카드>#n 설명 듣기#l#k\r\n\r\n"
            talk += "#L100# 더 이상 궁금한 것이 없어.#l";
            // talk += "#r※ #t4310307#은 캐릭터마다 일일 100개까지 획득 가능합니다.#k"
            cm.sendNextS(talk, 0x04);
        } else {
            cm.sendNextS("\r\n안녕 #h0#!\r\n난 #b#e그란디스의 미니게임 대표#k#n로 #b#e<네오 캐슬>#n#k에 왔어!", 0x04);
        }
    } else if (status == 1) {
        sel = selection;
        if (cm.getPlayer().getKeyValue(210223, "maru_tuto") > 0) {
            switch (sel) {
                case 3://초능력 윷놀이 참여하기
                    cm.sendYesNoS("\r\n지금 바로 #b#e<초능력 윷놀이>#n#k에 참여하겠어?\r\n\r\n#r(승낙 시 대기열에 등록됩니다.)#k", 0x04);
                    break;
                case 30://초능력 윷놀이 설명듣기
                    cm.sendNextS("\r\n#b#e<초능력 윷놀이>#k#n는 윷을 던진 결과로 말을 움직여서\r\n자신의 말을 먼저 모두 골인시키면 승리하는 게임이야!", 0x04);
                    break;
                case 100:
                    cm.dispose();
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n성 근처에서 반짝이는 걸 많이 주웠는데, 이것 봐! 이쁘지?\r\n내가 준비한 #b#e대전형 미니게임#n#k에서 실력을 보여주면,\r\n#b#e#i4310307:# #t4310307##n#k을 조금 나눠줄게.", 0x04);
        }
    } else if (status == 2) {
        if (cm.getPlayer().getKeyValue(210223, "maru_tuto") > 0) {
            switch (sel) {
                case 3:
                    MultiYutGame.addQueue(cm.getPlayer(), 2);
                    cm.dispose();
                    break;
                case 30:
                    cm.sendNextPrevS("\r\n게임이 시작되면 #b#e2개의 일회성 초능력#k#n이 랜덤하게 선택되고, 자신의 차례에 윷을 던져 결과에 따라 말의 위치를 이동시킬 수 있지.", 0x04);
                    break;
            }
        } else {
            cm.sendNextPrevS("\r\n#b#e카린의 <네오 젬샵>#n#k에선 이걸로 다양한 #b#e치장 물품#n#k들을\r\n교환할 수 있다고!", 0x04);
        }
    } else if (status == 3) {
        if (cm.getPlayer().getKeyValue(210223, "maru_tuto") > 0) {
            switch (sel) {
                case 30: {
                    cm.sendNextPrevS("\r\n던진 윷의 결과가 윷 또는 모가 나오거나,\r\n상대방의 말을 잡으면 윷을 한 번 더 던질 수 있어!", 0x04);
                    break;
                }
            }
        } else {
            cm.sendNextPrevS("\r\n그럼 누가누가 더 게임을 잘하는지 너의 실력을 보여줘~!", 0x04);
        }
    } else if (status == 4) {
        if (cm.getPlayer().getKeyValue(210223, "maru_tuto") > 0) {
            switch (sel) {
                case 30: {
                    cm.sendNextPrevS("\r\n#b#e초능력#k#n을 쓰려면 내 차례에서 #b#e윷을 던지기 전에#k#n 사용할 초능력을 먼저 선택해야 해.", 0x04);
                    break;
                }
            }
        } else {
            cm.dispose();
            cm.getPlayer().setKeyValue(210223, "maru_tuto", "1");
            cm.openNpc(9062462);
        }
    } else if (status == 5) {
        switch (sel) {
            case 30: {
                cm.sendNextPrevS("\r\n초능력을 사용하고 윷을 던지면\r\n윷 결과에 초능력을 연계할 수 있어!\r\n\r\n#r(주의! 초능력을 선택해서 발동시켰지만 사용하지 않고\r\n제한 시간이 넘어가면 초능력은 자동으로 사용됩니다.)#k", 0x04);
                break;
            }
        }
    } else if (status == 6) {
        switch (sel) {
            case 30://초능력 윷놀이 설명듣기
                cm.sendNextPrevS("\r\n자신의 말 4개를 먼저 모두 골인시킨 사람이 승리하고\r\n승패 결과에 따라 더 많은 보상을 받아 갈 수 있어!\r\n\r\n#b#e<초능력 윷놀이 보상>#k#n\r\n - 승리 : #i4310307:# #b#t4310307:# 60개#k\r\n - 무승부 : #i4310307:# #b#t4310307:# 30개#k\r\n - 패배 : #i4310307:# #b#t4310307:# 20개#k\r\n\r\n\r\n", 0x04);
                break;
        }
    } else if (status == 7) {
        switch (sel) {
            case 30://초능력 윷놀이 설명듣기
                cm.sendNextPrevS("\r\n참고로 자신의 차례에 제한 시간 동안 윷을 던지지 않거나 말을 움직이지 않으면 자동으로 진행될 거야.", 0x04);
                break;
        }
    } else if (status == 8) {
        switch (sel) {
            case 30://초능력 윷놀이 설명듣기
                cm.sendNextPrevS("\r\n5번이 반복되면 강제로 퇴장되고\r\n보상을 받아 갈 수 없으니 주의해줘!", 0x04);
                cm.dispose();
                break;
        }
    }
}

importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);

var status = -1, sel = 0;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        d = status;
        status++;
    }


    if (status == 0) {
        qm.sendSimpleS("  신나는 팡팡 리액션 타임! 어떤 게 궁금해?\r\n#L0# #b#e<ON AIR - 팡팡 리액션>#n에 대해 알려줘.#k#l\r\n#L1# #b#e<ON AIR - 팡팡 리액션>#n오늘의 현황을 알려줘.#k#l", 4, 9062549)
    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            qm.sendNextS("\r\n이 사냥 천재 프리토님께서 특별히 준비한 사냥 방송!\r\n#b#e<메이플 ON AIR>#n#k가 궁금하군.", 4, 9062549);
        } else if (sel == 1) {
            qm.sendNextS("오늘의 리액션 현황이 궁금해?\r\n\r\n- 팡팡 리액션 발동 횟수 : #r0#k/10 회\r\n- 레범몬 처치 수 : #r0#k/500 마리 ", 4, 9062549);
        }
    } else if (status == 2) {
        if (sel == 0) {
            qm.sendNextPrevS("이번 #b#e<메이플 ON AIR>#n#k는 크리에이터라면 필수로 갖춰야 할 리액션을 배우게 돼. 이름하여 #b#e팡팡 리액션#n#k!", 4, 9062549)
        }
    } else if (status == 3) {
        if (sel == 0) {
            qm.sendNextPrevS("#b#e팡팡 리액션#n#k은 #r500마리#k의 #r레벨 범위 몬스터#k를 처치할 때마다, 시청자들이 후원하여 등장하는 #b#e메이플 팡팡 주머니#n#k를 터뜨리는 거야.", 4, 9062549)
        }
    } else if (status == 4) {
        if (sel == 0) {
            qm.sendNextPrevS("#b#e메이플 팡팡 주머니#n#k는 리액션 #r1번#k에 최대 #r1분#k 동안 일정 주기마다 나뉘어서 등장해.", 4, 9062549)
        }
    } else if (status == 5) {
        if (sel == 0) {
            qm.sendNextPrevS("#b#e메이플 팡팡 주머니#n#k를 빨리 터뜨리면, 리액션 타임을 더 빠르게 끝낼 수 있지. #b#e팡팡 리액션#n#k은 하루에 #r10번#k까지 가능해.", 4, 9062549)
        }
    } else if (status == 6) {
        if (sel == 0) {
            qm.sendNextPrevS("사냥하면 등장하는 #b#e메이플 팡팡 주머니#n#k를 터뜨려! 그러면 획득하는 #b빵빵한 경험치#k와 함께 #b인기 크리에이터#k로 마음껏 성장해봐!\r\n\r\n#e[이벤트 기간]#n\r\n - 2021년 7월 14일 23시 59분까지\r\n\r\n※팡팡 리액션 횟수는 매일 자정에 초기화 됩니다.\r\n※이미 필드에 팡팡 리액션이 진행중인 경우 팡팡 리액션을 발동할 수 없습니다.", 4, 9062549)
        }
    } else if (status == 7) {
        qm.dispose();
        NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062549, 100856);
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}
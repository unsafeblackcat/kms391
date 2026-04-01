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
        if (status == 6) {
            qm.sendOkS("마음이 바뀌면 언제든 다시 나에게 말을 걸어줘.\r\n\r\n#r#e[이벤트 기간]#n\r\n - 2021년 7월 14일 23시 59분까지#n", 4, 9062549);
            qm.dispose();
            return;
        }
        status--;
    }
    if (mode == 1) {
        d = status;
        status++;
    }


    if (status == 0) {
        qm.sendNextS("#b#e#h0##n#k, 안녕?\r\n크리에이터가 된 기분은 어때!\r\n더 최고의 크리에이터가 되고 싶지 않아?", 4, 9062549)
    } else if (status == 1) {
        qm.sendNextPrevS("프리토...!? 어째서 방송 매니저를 하고 있어?", 2)
    } else if (status == 2) {
        qm.sendNextPrevS("훗, 프리토님께서 특별히 사냥 방송 매니저가 되었지. 어쨌든 너, 인기 크리에이터는 구독자의 후원에 감사하는 사냥 #b#e리액션#n#k이 필수라는 걸 알아?", 4, 9062549)
    } else if (status == 3) {
        qm.sendNextPrevS("\r\n#fc0xFFAAAAAA#(리액션...?)#k", 2)
    } else if (status == 4) {
        qm.sendNextPrevS("조용한 걸 보니 #b#e리액션#n#k이 없나 보군.\r\n엣헴, 아직 별다른 리액션이 없는 #b#e#h0##n#k만의 아주 특별한 #b#e팡팡 리액션#n#k을 준비했어!", 4, 9062549)
    } else if (status == 5) {
        qm.sendNextPrevS("#b#e팡팡 리액션#n#k을 하는 방법은 간단해!\r\n#b#e#h0##n#k의 방송 시청자들이 후원하는 #b#e메이플 팡팡 주머니#n#k를 사냥 도중에 신나게 터뜨리기만 하면 되는 거지.\r\n이 메이플 팡팡 주머니는 #r경험치#k를 듬뿍 지니고 있다고!", 4, 9062549)
    } else if (status == 6) {
        qm.sendYesNoS("어때?\r\n나와 함께 신나게 #b#e메이플 팡팡 주머니#n#k를 터뜨려 볼래?", 4, 9062549)
    } else if (status == 7) {
        qm.getClient().setCustomData(501469, "SkillTuto", "1");
        qm.forceStartQuest(true);
        qm.forceCompleteQuest(true);
        qm.teachSkill(80003064, 1);
        qm.sendNextS("역시, 믿고 있었다고!\r\n그럼 지금부터 #b#e팡팡 리액션#n#k을 하는 방법을 알려줄게.", 4, 9062549)
        qm.getPlayer().dropMessage(5, "지금부터 레벨 범위 몬스터가 등장하는 곳에서 <ON AIR - 팡팡 리액션> 스킬을 사용할 수 있습니다.");
    } else if (status == 8) {
        qm.sendNextPrevS("먼저 레벌 범위 몬스터 #r500마리#k를 사냥해. 그러면 #b#e#h0##n#k의 사냥 방송을 보는 시청자들이 #b#e메이플 팡팡 주머니#n#k를 후원 해줄 거야!", 4);
    } else if (status == 9) {
        qm.sendNextPrevS("그리고 사냥터에 쏟아져 나오는 #b#e메이플 팡팡 주머니#n#k를 신나게 터뜨리면서 #r경험치#k를 획득하면 돼. 어때, 간단하지?", 4);
    } else if (status == 10) {
        qm.sendNextPrevS("아 참, #b#e팡팡 리액션#n#k은 #r하루 최대 10번#k까지만 가능해.\r\n그럼 신나는 #b#e팡팡 리액션#n#k으로 최고의 크리에이터가 되보자!\r\n\r\n#r[이벤트 기간]#n\r\n - 2021년 7월 14일 23시 59분까지\r\n\r\n※팡팡 리액션 횟수는 매일 자정에 초기화 됩니다.\r\n※이미 필드에 팡팡 리액션이 진행중인 경우 팡팡 리액션을 발동할 수 없습니다.", 4);
        qm.dispose();
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}
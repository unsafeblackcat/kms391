importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);

var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 9) {
            qm.sendOkS("언제든 스타가 되고 싶다면 말해줘!\r\n\r\n#r#e[이벤트 기간]#n\r\n - 2021년 9월 8일 23시 59분까지#n", 4, 9010010);
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
        qm.sendNextS("너... 혹시...", 4, 9010010);
    } else if (status == 1) {
        qm.sendNextPrevS("#r#e#fs20#슈퍼스타#n#k#fs15#가 되고 싶니?", 4, 9010010);
    } else if (status == 2) {
        qm.sendNextPrevS("....네?", 2);
    } else if (status == 3) {
        qm.sendSimpleS("넌 #r#e스타의 자질#n#k이 충분해!\r\n\r\n#L0##b(갑자기 무슨 슈퍼스타냐고 물어본다.)#l\r\n#L1#(알았으니 본론부터 말해 달라고 한다.)#l", 4, 9010010);
    } else if (status == 4) {
        if (selection == 0) {
            qm.sendNextPrevS("역시! #r#e넘쳐나는 호기심#n#k까지! \r\n스타의 자질이 충분하군!", 4, 9010010);
        } else {
            qm.sendNextPrevS("역시! #b#e거침없는 성격#n#k까지! \r\n스타의 자질이 충분하군!", 4, 9010010);
        }
    } else if (status == 5) {
        qm.sendNextPrevS("그럼 본론으로 넘어가서! 이런 기계를 본 적이 있니?\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/6#", 4, 9010010);
    } else if (status == 6) {
        qm.sendNextPrevS(".....이게...뭐죠?", 2);
    } else if (status == 7) {
        qm.sendNextPrevS("후훗! 이게 바로 널 #r#e슈퍼스타#n#k로 만들어 줄 거야!", 4, 9010010);
    } else if (status == 8) {
        qm.sendNextPrevS("흠... 역시 눈으로 직접 보는 게 낫겠어!", 4, 9010010);
    } else if (status == 9) {
        qm.sendYesNoS("#e#b#h0##k#n! 이 장치를 사용해 봐!\r\n\r\n #r※ 수락 시 이벤트 맵으로 이동합니다.", 4, 9010010);
    } else if (status == 10) {
        if (qm.getClient().getQuestStatus(50005) == 1) {
            qm.getClient().setCustomKeyValue(50005, "1", "1");
        }
        if (qm.getClient().getQuestStatus(100825) != 2) {
            qm.getClient().setCustomKeyValue(501470, "reward", "000000000000000000");
            qm.getClient().setCustomKeyValue(501470, "start", "1");
            qm.getClient().setCustomKeyValue(501470, "follower", "0");
            qm.getClient().setCustomKeyValue(501470, "dailyF", "0");
            qm.getClient().setCustomKeyValue(501470, "grade", "0");
            qm.getClient().setCustomKeyValue(501470, "weeklyF", "0");
            qm.getClient().setCustomKeyValue(501497, "count", "0");
            qm.getClient().setCustomKeyValue(501501, "count", "0");
            for (var i = 501497; i <= 501522; i++) {
                qm.forceStartQuest(i, true);
            }
        }
        qm.forceCompleteQuest(true);
        qm.dispose();
        qm.warp(993194001);
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}
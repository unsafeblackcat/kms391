


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    화이트 에 의해 만들어 졌습니다.

    엔피시아이디 : 2006

    엔피시 이름 : 티앤크

    엔피시가 있는 맵 : The Black : Night Festival (100000000)

    엔피시 설명 : MISSINGNO


*/
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}
빨강 = "#fc0xFFF15F5F#"

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
        cm.Entertuto(true, false);
    } else if (status == 1) {
        var txt = "#fs28##fn나눔고딕 Extrabold#";
        txt += ""+빨강+"첫번째#k, 서버 규칙을 숙지하지 않은것에 대한 불이익은 모두 자신에게 있습니다."
        cm.getClient().send(SLFCGPacket.BlackLabelE(txt, 50, 1500, 4, 0, 0, 0, 4, 3, "SampleType00"));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, 7000));
    } else if (status == 2) {
        var txt = "#fs28##fn나눔고딕 Extrabold#";
        txt += ""+빨강+"두번째#k, 비인가 프로그램(핵,매크로,자동사냥) 등 금지되어 있습니다."
        cm.getClient().send(SLFCGPacket.BlackLabelE(txt, 50, 1500, 4, 0, 0, 0, 4, 3, "SampleType00"));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, 7000));
    } else if (status == 3) {
        var txt = "#fs28##fn나눔고딕 Extrabold#";
        txt += ""+빨강+"세번째#k, 서버 물을 흐리는 행위, 고인 비하, 성적 발언, 일베, 욕설은 금지되어 있습니다."
        cm.getClient().send(SLFCGPacket.BlackLabelE(txt, 50, 1500, 4, 0, 0, 0, 4, 3, "SampleType00"));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, 7000));
    } else if (status == 4) {
        var txt = "#fs28##fn나눔고딕 Extrabold#";
        txt += ""+빨강+"네번째#k, 과도한 친목, 유저간 선동 또는 분쟁 시, 경고 또는 제재 처리 됩니다."
        cm.getClient().send(SLFCGPacket.BlackLabelE(txt, 50, 1500, 4, 0, 0, 0, 4, 3, "SampleType00"));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, 7000));
    } else if (status == 5) {
        var txt = "#fs28##fn나눔고딕 Extrabold#";
        txt += ""+빨강+"다섯번째#k, 서버 내 버그 사용 적발 시 제재 처리 됩니다."
        cm.getClient().send(SLFCGPacket.BlackLabelE(txt, 50, 1500, 4, 0, 0, 0, 4, 3, "SampleType00"));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, 7000));
    } else if (status == 6) {
        var txt = "#fs28##fn나눔고딕 Extrabold#";
        txt += ""+빨강+"여섯번째#k, 대리 행위는 금지되어 있으며, 행위로 인한 피해는 절대 복구가 불가능합니다."
        cm.getClient().send(SLFCGPacket.BlackLabelE(txt, 50, 1500, 4, 0, 0, 0, 4, 3, "SampleType00"));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, 7000));
    } else if (status == 7) {
        var txt = "#fs28##fn나눔고딕 Extrabold#";
        txt += ""+빨강+"일곱번째#k, 근거 없는 유언비어 유포, 서버 비하 발언, 운영자 모욕 등은 제재 당하실 수 있습니다."
        cm.getClient().send(SLFCGPacket.BlackLabelE(txt, 50, 1500, 4, 0, 0, 0, 4, 3, "SampleType00"));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, 7000));
    } else if (status == 8) {
        var txt = "#fs28##fn나눔고딕 Extrabold#";
        txt += ""+빨강+"여덟번째#k, 모든 플레이 기록은 서버에 저장되며, 필요에 의해 타인에게 공개되거나 증거로 활용될 수 있습니다."
        cm.getClient().send(SLFCGPacket.BlackLabelE(txt, 50, 1500, 4, 0, 0, 0, 4, 3, "SampleType00"));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, 7000));
    } else if (status == 9) {
        var txt = "#fs28##fn나눔고딕 Extrabold#";
        txt += ""+빨강+"아홉번째#k, 설명드린 규칙 외에도 피해를 주는 행위는 제재 처리가 될 수 있습니다."
        cm.getClient().send(SLFCGPacket.BlackLabelE(txt, 50, 1500, 4, 0, 0, 0, 4, 3, "SampleType00"));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, 7000));
    } else if (status == 10) {
        var txt = "#fs28##fn나눔고딕 Extrabold#";
        txt += ""+빨강+"열번째#k, 블랙 서버에서 좋은 추억을 만들어주세요."
        cm.getClient().send(SLFCGPacket.BlackLabelE(txt, 50, 1500, 4, 0, 0, 0, 4, 3, "SampleType00"));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, 7000));
    } else if (status == 11) {
        cm.getClient().send(SLFCGPacket.MakeBlind(1, 0, 0, 0, 0, 1000, 0));
        cm.getClient().send(CField.UIPacket.getDirectionStatus(false));
        cm.getClient().send(SLFCGPacket.SetStandAloneMode(false));
        cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
        cm.dispose();
    }
}

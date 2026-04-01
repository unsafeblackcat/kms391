importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    //건들지 마세요, 횟수, 건들지마세요, 건들지마세요
    setting = [
        ["rnjPQ", 3, 926100000, 10],
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if(status == 0 && selection == 0){
            cm.sendOk("용감한 메이플의 모험가 여러분!! 마가티아의 평화를 위해서 부디 저희들을 도와주세요!!\r\n\r\n"
                    +"- #e레벨#n : 70레벨 이상 #r(추천레벨 : 70 ~ 80)#k\r\n"
                    +"- #e제한시간#n : 20분\r\n"
                    +"- #e참가인원#n : 4 ~ 6명\r\n"
                    +"- #e획득 아이템#n : #i1122116#  #z1122116#\r\n");
            cm.dispose();
            return;
        } else {
            status++;
        }
    }

    if (status == 0) {
        talk = "#e<파티퀘스트 : 로미오와 줄리엣>#n\r\n"
        talk += "마가티아는 지금 크나큰 위기를 맞이하고 있습니다. 용감한 메이플의 모험가님\r\n\r\n"
        talk += "#L0##b로미오의 이야기를 듣는다\r\n"
        talk += "#L1#퀘스트를 시작한다\r\n"
        talk += "#L2#오늘 남은 도전 횟수를 알고 싶어요.#k\r\n"
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        if(st == 2){
            var talk = "#h #님이 남은 입장 횟수는 "+(3-cm.getPlayer().getV(setting[0][0]))+"번 입니다.";
            cm.sendOk(talk);
            cm.dispose();
            return;
        }
        if (cm.getParty() == null) {
            cm.sendOk("파티를 맺어야만 입장할 수 있습니다.");
            cm.dispose();
            return;
        } else if (cm.getParty().getMembers().size() < 4 && !cm.getPlayer().isGM()) {
            cm.sendOk("4인 이상 파티를 맺어야만 입장할 수 있습니다.");
            cm.dispose();
            return;
        // } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
        //     cm.sendOk("이미 누군가가 도전하고 있습니다.\r\n다른채널을 이용 해 주세요.");
        //     cm.dispose();
        //     return;
        } else if (!cm.isLeader()) {
            cm.sendOk("파티장만이 입장을 신청할 수 있습니다.");
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[0][0], setting[0][1])) {
            talk = "파티원 중 "
            for (i = 0; i < cm.BossNotAvailableChrList(setting[0][0], setting[0][1]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.BossNotAvailableChrList(setting[0][0], setting[0][1])[i] + ""
            }
            talk += "#k#n님이 오늘 입장했습니다. 하루에 " + setting[0][1] + "번만 도전하실 수 있습니다.";
            cm.sendOk(talk);
            cm.dispose();
            return;
        } else {
            cm.resetMap(setting[0][2]);
            cm.addBoss(setting[0][0]);
            if (st <= 1) {
                em = cm.getEventManager(setting[0][0]);
                if (em != null) {
                    cm.getEventManager(setting[0][0]).startInstance_Party(setting[0][2] + "", cm.getPlayer());
                }
                cm.dispose();
            }
        }
    }
}
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
        ["MinervaPQ", 3, 933031000, 10],
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if(status == 0 && selection == 1){
            cm.sendOk("#e<파티퀘스트 : 여신의 흔적>#n\r\n"
                    +"엘나스산맥에 큰 비가 내린 후에 오르비스 탑 꼭대기의 #b미네르바 여신상#k 뒤로 새로운 구름길이 열렸어. 하늘 너머의 거대한 구름이 갈라지며 신비로운 탑이 나타났는데...바로 오래 전 오르비스를 다스렸던 #b여신 미네르바#k의 탑이라구. 여신 미네르바가 갇혀있다는 전설의 탑에서 모험을 시작해볼래?\r\n\r\n"
                    +"- #e레벨#n : 70레벨 이상 #r(추천레벨 : 70 ~ )#k\r\n"
                    +"- #e제한시간#n : 20분\r\n"
                    +"- #e참가인원#n : 3 ~ 6명\r\n"
                    +"- #e획득 아이템#n : #i1082232#  #z1082232#\r\n");
            cm.dispose();
            return;
        } else {
            status++;
        }
    }

    if (status == 0) {
        talk = "#e<파티퀘스트 : 여신의 흔적>#n\r\n"
        talk += "안녕? 난 요정 윙키라고 해. 여신의 탑을 모험하고 싶으면 나에게 이야기 하라고~\r\n\r\n"
        talk += "#L0##b파티퀘스트를 하고 싶어요.\r\n"
        talk += "#L1#여신의 탑에 대해 듣는다.\r\n"
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
        } else if (cm.getParty().getMembers().size() < 3 && !cm.getPlayer().isGM()) {
            cm.sendOk("3인 이상 파티를 맺어야만 입장할 수 있습니다.");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOk("이미 누군가가 도전하고 있습니다.\r\n다른채널을 이용 해 주세요.");
            cm.dispose();
            return;
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
            //cm.addBoss(setting[0][0]);
            for (var i = 0; i < cm.getPlayer().getParty().getMembers().size(); ++i) {
                mem = cm.getPlayer().getParty().getMembers().get(i).getName();
                conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(mem);
                conn.removeItem(4001818, -conn.itemQuantity(4001818));
                conn.removeItem(4001819, -conn.itemQuantity(4001819));
                conn.removeItem(4001820, -conn.itemQuantity(4001820));
                conn.removeItem(4001821, -conn.itemQuantity(4001821));
                conn.removeItem(4001822, -conn.itemQuantity(4001822));
                conn.removeItem(4001823, -conn.itemQuantity(4001823));
                conn.removeItem(4001824, -conn.itemQuantity(4001824));
                conn.removeItem(4001825, -conn.itemQuantity(4001825));
                conn.removeItem(4001826, -conn.itemQuantity(4001826));
                conn.removeItem(4001827, -conn.itemQuantity(4001827));
                conn.removeItem(4001828, -conn.itemQuantity(4001828));
                conn.removeItem(4001829, -conn.itemQuantity(4001829));
                conn.removeItem(4001830, -conn.itemQuantity(4001830));
            }
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
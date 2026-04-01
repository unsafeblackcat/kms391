var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Zakum", 1, 280030100],
        ["Chaos_Zakum", 1, 280030000]
    ]
    name = ["普通", "混沌"]
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        st = cm.getPlayer().getMapId() - 211042400;
        /*if (st > 1 || st < 0) {
            cm.sendOk("자쿰의 제단이 아닌 곳에 엔피시가 소환되었습니다. 운영자께 문의해 주세요.");
            cm.dispose();
            return;
        }*/
        if (cm.getPlayer().getParty() == null) {
            cm.sendOkS("#fs20#只有隊長才能申請入場.", 0x24, 2030013);
            cm.dispose();
            return;
        }
        talk = "#e#fs20#<殘暴炎魔.紮昆>#k#n\r\n"
        talk += "#e#r#fs15#紮昆復活了.如果放任不管的話,會引發火山爆發,把整個艾爾納斯山脈變成地獄.#k#n"
        talk += "紮昆的祭壇每天可以進入 #e#r 1 #k#n次，記錄將在#e#r每天午夜初始化#k#n .\r\n";
        talk += "#L0# #b申請紮昆入場。 （隊友將同時移動。）#k";
        cm.sendSimpleS(talk, 0x24);
    } else if (status == 1) {
        if (cm.getPlayer().getParty() == null) {
            cm.sendNextS("#fs15#沒有開啓隊伍。 只有開啓隊伍才能挑戰。", 0x24);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOkS("#fs15#所有成員必須在同一地點.", 0x24);
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        }
        if (cm.getPlayerCount(setting[st][2]) >= 1) {
            talk = "#fs15#已經有人在挑戰紮昆了。 請使用其他頻道。";
            cm.sendOkS(talk, 0x24);
            cm.dispose();
            return;
        }
        var em = cm.getEventManager(setting[st][0]);
        cm.addBoss(setting[st][0]);
        if (em != null) {
            cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
        }
        cm.dispose();
    }
}
var status = -1;
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    setting = [
        ["Normal_GES", 1, 160020000, 290] // [副本名稱, 難度, 地圖ID, 等級要求]
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose(); 
        return;
    }
    if (mode == 1) {
        status++;
    }
 
    if (status == 0) {
        talk = "#fs15##e<守護天使史萊姆>#n\r\n\r\n"
        talk += "#L0##b挑戰守護天使史萊姆#k#l"
        cm.sendSimple(talk); 
    } else if (status == 1) {
        st = selection;
        if (cm.getParty()  == null) {
            cm.sendOk(" #fs15#需組建1人以上隊伍才可入場");
            cm.dispose(); 
            return;
        } else if (cm.getPlayerCount(setting[st][2])  >= 1) {
            cm.sendOk(" #fs15#已有其他隊伍正在挑戰\r\n請切換頻道嘗試");
            cm.dispose(); 
            return;
        } else if (!cm.isLeader())  {
            cm.sendOk(" #fs15#僅限隊長申請入場");
            cm.dispose(); 
            return;
        } else if (!cm.allMembersHere())  {
            cm.sendOk(" #fs15#所有隊員需在同地圖");
            cm.dispose(); 
            return;
        }
        if (!cm.isBossAvailable(setting[st][0],  setting[st][1])) {
            talk = "#fs15#隊伍成員中已有入場記錄者：\r\n\r\n"
            for (i = 0; i < cm.BossNotAvailableChrList(setting[st][0], setting[st][1]).length; i++) {
                talk += "#b#e-" + cm.BossNotAvailableChrList(setting[st][0], setting[st][1])[i] + "\r\n"
            }
            cm.sendOk(talk); 
            cm.dispose(); 
            return;
        } else if (!cm.isLevelAvailable(setting[st][3]))  {
            talk = "#fs15#隊伍成員等級不足："
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                talk += "#b#e-" + cm.LevelNotAvailableChrList(setting[st][3])[i] + "\r\n"
            }
            cm.sendOk(talk); 
            cm.dispose(); 
            return;
        } else {
            cm.addBoss(setting[st][0]); 
            em = cm.getEventManager(setting[st][0]); 
            if (em != null) {
                cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2]  + "", cm.getPlayer()); 
            }
            cm.dispose(); 
        }
    }
}
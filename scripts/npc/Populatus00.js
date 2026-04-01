var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Easy_Populatus", 1, 220080100, 115],
        ["Normal_Populatus", 1, 220080200, 155],
        ["Chaos_Populatus", 1, 220080300, 190],
        ["Chaos_Populatus", 1, 220080300, 190]
    ]
    name = ["이지","노멀", "카오스"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if (status == 0 && cm.itemQuantity(4031179) != 0) {
            st = selection;
            status++;
        }
        status++;
    }
    if (status == 0) {
        if (cm.getParty() == null) {
            cm.sendOk("#fs15#1人以上組隊才能入場.");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("#fs15#只有隊長才能申請入場.");
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("#fs15#所有成員必須在同一地點.");
            cm.dispose();
            return;
        }
        말 = "#e#fs15#<BOSS：巾白普拉圖斯>#n\r\n"
        말 += "我們苾須封鎖肇事者巾白普拉圖斯繼續破壞維度。 請幇助我。\r\n\r\n\r\n"
        말 += "#L0# 簡單模式（等級115以上）\r\n"
        cm.sendSimpleS(말, 4, 2041021);
    } else if (status == 1) {
        st = selection;
        if (cm.itemQuantity(4031179) == 0) {
            cm.gainItem(4031179, 1);
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 3) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        }
        cm.sendNextS("#r#e#fs15#隊員都沒有#k #b次元裂痕的碎片#n#k. 爲了見到巾白普拉圖斯，非常需要。 正好我這里有一個。", 4, 2041021);
    } else if (status == 2) {
        if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 3) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        }
        cm.sendNextPrevS("#b#e#fs15#物品次元裂痕#n#k的碎片已放到背苞 ，請一定要封鎖巾白普拉圖斯破壞次元！", 4, 2041021);
    } else if (status == 3) {
        if (cm.getPlayerCount(setting[st][2]) != 0) {
            cm.sendOk("#fs15#已經有人在挑戰巾白普拉圖斯了。\r\n請使用其他頻道.");
            cm.dispose();
            return;
        }
        if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "隊員中 "
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n的等級不足。  無法進入鐘樓的源頭。\r\n\r\n"
            talk += "(" + name[st] + "模式只能進入 #e" + setting[st][3] + " #n等級以上.)";
            cm.sendOk(talk);
            cm.dispose();
            return;
        } else {
            if (st == 3) {
                cm.addBossPractice(setting[st][0]);
            } else {
                cm.addBoss(setting[st][0]);
            }
            em = cm.getEventManager(setting[st][0]);
            if (em != null) {
                cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
            }
            cm.dispose();
        }
    }
}
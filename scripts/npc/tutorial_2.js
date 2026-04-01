var status = -1;

function start() {

    action(1, 0, 0);
}

function action(mode, type, selection) {
    dialogue = [
        "剛才的問答不難吧？接下來我們要學習#e『快速移動』#n系統。\r\n\r\n#e什麼是快速移動？#n\r\n按下#r現金商店(`鍵)#k後出現的介面統稱為快速移動（Trade），包含5大功能模組：",
        "#e#b<快速移動功能說明>#n#k\r\n\r\n #d#e- 角色裝扮系統#n：提供髮型/整形變更、現金裝備搜尋等外觀定制功能\r\n #d#e- 地圖傳送系統#n：可快速移動至狩獵地圖、村莊等必經地點\r\n #d#e- 便利服務系統#n：強化裝備、領取各類福利的綜合功能\r\n #d#e- 商店系統#n：提供裝備購買、道具製作等服務\r\n #d#e- 娛樂內容系統#n：包含緩解狩獵疲勞的趣味小遊戲"
	     ];
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (cm.getPlayer().getKeyValue(20190721, "tutostatus") == -1) {
        if (status < dialogue.length) {
            cm.sendNext(dialogue[status]);
        } else {
            cm.sendOk(" 理解快速移動功能了嗎？請使用傳送系統前往#b黃金海岸#k，\n向#r『托普』NPC#k取得#b玩具球#k！\r\n\r\n#d ※ 輸入指令 #e@教學#n 可隨時召喚我");
            cm.getPlayer().setKeyValue(20190721, "tutostatus", 0);
            cm.dispose();
        }
    } else {
        getq = cm.getPlayer().getKeyValue(20190721, "tutorial") + 1;
        if (isQuestCompleteable(getq)) {
                       cm.sendOk("#b#e 任務完成！#k#n\r\n獎勵已發放至您的背包！\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#r#e30  坎島點數#n#k");
            cm.getPlayer().setKeyValue(20190721, "tutorial", cm.getPlayer().getKeyValue(20190721, "tutorial") + 1);
            cm.getPlayer().setKeyValue(20190721, "tutostatus", -1);
            itembyQuest(getq);
            cm.dispose();
            return;
        } else {
                       cm.sendOk(" 您似乎尚未完成任務呢！\n請使用傳送系統前往#b黃金海岸#k，\n向#r『托普』NPC#k取得#b玩具球#k！\r\n\r\n#d ※ 輸入指令 #e@教學#n 可隨時召喚我");
            cm.dispose();
        }
    }
}

function isQuestCompleteable(getq) {
    switch (getq) {
        case 0:
            if (cm.itemQuantity(4000022) >= 30) {
                return true;
            }
            return false;
            break;
        case 1:
            if (퀴즈) {
                return true;
            }
            return false;
            break;
        case 2:
            if (cm.itemQuantity(4033622) >= 1) {
                return true;
            }
            return false;
            break;
        case 3:
            if (cm.itemQuantity(5000708) >= 1) {
                return true;
            }
            return false;
            break;
        case 4:
            if (미니게임) {
                return true;
            }
            return false;
            break;
        case 5:
            if (cm.itemQuantity(1082102) >= 1) {
                return true;
            }
            return false;
            break;
        case 6:
            if (cm.getPlayer().getKeyValue(20190721, "tutostatus") == 1 && cm.itemQuantity(4033302) >= 1) { // 자쿰 이벤트스크립트확인
            return true;
            }
            return false;
            break;
        case 7:
            if (cm.getPlayer().getLevel() >= 200) {
                return true;
            }
            return false;
            break;
        default:
            return true;
            break;
    }
}

function itembyQuest(getq) {
    switch (getq) {
         case 0:
             cm.getPlayer().AddStarDustCoin(50);
             cm.gainItem(4000022, -30);
             break;
         case 1:
             cm.getPlayer().AddStarDustCoin(50);
             cm.gainItem(4031191, -1);
             break;
         case 2:
             cm.getPlayer().AddStarDustCoin(50);
             cm.gainItem(4033622, -1);
             break;
         case 3:
             cm.getPlayer().AddStarDustCoin(50);
             break;
         case 4:
             cm.getPlayer().AddStarDustCoin(50);
             break;
         case 5:
             cm.getPlayer().AddStarDustCoin(50);
             break;
         case 6:
             cm.getPlayer().AddStarDustCoin(50);
             cm.gainItem(4033302,-1);
             break;
         case 7:
             cm.gainItem(2435719,10);
             cm.gainItem(1712001,1);
             cm.gainItem(2439302,1);
             cm.gainItem(2450130,2);
             cm.gainItem(4001715,1);
             break;
         default:
         break;
    }
}
var status = -1;

function start() {

    action(1, 0, 0);
}

function action(mode, type, selection) {
    dialogue = [
        "#r坎島#k的冒險還愉快嗎？如果我的指引對#b#h ##k有所幫助就太好了！\n現在為您介紹幾個實用指令與技巧：",
        "#b#e<玩家指令清單>#k#n\r\n\r\n#d#e@幫助#n : 顯示所有可用指令列表\r\n@村莊,廣場#n : 傳送至坎島中央廣場\r\n@在線人數#n : 顯示當前伺服器在線玩家數\r\n@力量,敏捷,智力,幸運#n : 直接增加對應屬性點\r\n@背包重置#n : 重置裝備/消耗/設置/其他/現金/已裝備欄位\r\n@名聲通知#n : 切換名聲值變動通知",
        "#r#e<實用技巧>#k#n\r\n\r\n#b[1]#k 在聊天框輸入『~想說的話』可發送全頻公告（替換「想說的話」部分）\r\n#b[2]#k 按鍵盤#e1左側的`鍵#n可快速開啟傳送介面\r\n#b[3]#k培養分身角色獲取#b『連結技能/聯盟戰地』#k加成是必備策略\r\n#b[4]#k 200級後在#b神秘河(阿爾卡娜、啾啾島、拉契爾恩等)#k練級需要#d神秘符文#k"
	     ];
    if (mode == 1) {
        if (cm.getPlayer().getKeyValue(20190721, "tutostatus") == 0 && status == -1 && cm.itemQuantity(4031191) < 1) {
            cm.dispose();
            cm.openNpc(2074115);
            return;
        } else if (status == 3) {
            cm.dispose();
            cm.openNpc(2074115);
            return;
        } else {
            status++;
        }
    } else {
        cm.dispose();
        return;
    }
    if (cm.getPlayer().getKeyValue(20190721, "tutostatus") == -1) {
        if (status < dialogue.length) {
            cm.sendNext(dialogue[status]);
        } else if (status == 3) {
                       cm.sendSimple(" 這些#b#e實用技巧#k#n都記住了嗎？現在讓我們進行小測驗吧！\n請找#bNPC『貓咪』#k完成測試任務！\r\n\r\n#d ※ 輸入指令 #e@指引#n 可隨時召喚我");
            cm.getPlayer().setKeyValue(20190721, "tutostatus", 0);
        }
    } else {
        getq = cm.getPlayer().getKeyValue(20190721, "tutorial") + 1;
        if (isQuestCompleteable(getq)) {
            cm.sendOk("#b#e 任務完成！#k#n\r\n獎勵已發放！\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#r#e30  坎島點數#n#k");
            cm.getPlayer().setKeyValue(20190721, "tutorial", cm.getPlayer().getKeyValue(20190721, "tutorial") + 1);
            cm.getPlayer().setKeyValue(20190721, "tutostatus", -1);
            itembyQuest(getq);
            cm.dispose();
            return;
        } else {
           cm.sendSimple(" 您似乎尚未完成測驗呢！請先向#b貓咪NPC#k答對所有題目！\r\n\r\n#d ※ 輸入指令 #e@指引#n 可隨時召喚我");
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
           if (cm.itemQuantity(4031191) >= 1) {
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
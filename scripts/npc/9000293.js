var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

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
        if (cm.getPlayer().getLevel() >= 160 && cm.getPlayer().getMapId() == 910000000) {
            var jump = "嘿~咻!嘿~咻! 嗯? 什麼情況!? 你也和我一樣喜歡跳躍嗎!?\r\n很好! 既然如此，如果你願意幫我，我就送你好道具!#k\r\n讓我看看... #b#h ##k，你能幫我的任務有…\r\n\r\n";
            jump += "#fUI/UIWindow2.img/UtilDlgEx/list1#\r\n";
            if (cm.getQuestStatus(500) == 1 && cm.getQuestStatus(501) == 1 && cm.getQuestStatus(502) == 1 && cm.getQuestStatus(503) == 1) {
                jump += "#L100##r#i4001308# 舒咪遺失的硬幣 (任務完成)\r\n";
                jump += "#L200##i4001308# 蘊含尊真心意的花 (任務完成)\r\n";
                jump += "#L300##i4001308# 克莉休拉瑪的委託 (任務完成)\r\n";
                jump += "#L400##i4001308# 查利中士的廢礦探勘任務 (任務完成)#k\r\n\r\n";
                jump += "#L5##b#h ##k : #r已完成所有幫助，現在請給我獎勵吧。#k";
            } else if (cm.getQuestStatus(500) == 1 && cm.getQuestStatus(501) == 1 && cm.getQuestStatus(502) == 1) {
                jump += "#L100##r#i4001308# 舒咪遺失的硬幣 (任務完成)\r\n";
                jump += "#L200##i4001308# 蘊含尊真心意的花 (任務完成)\r\n";
                jump += "#L300##i4001308# 克莉休拉瑪的委託 (任務完成)#k\r\n";
                jump += "#L4##b → 查利中士的廢礦探勘任務 (可開始)#k\r\n";
            } else if (cm.getQuestStatus(500) == 1 && cm.getQuestStatus(501) == 1) {
                jump += "#L100##r#i4001308# 舒咪遺失的硬幣 (任務完成)\r\n";
                jump += "#L200##i4001308# 蘊含尊真心意的花 (任務完成)#k\r\n";
                jump += "#L3##b → 克莉休拉瑪的委託 (可開始)#k\r\n";
                jump += "#L40##r查利中士的廢礦探勘任務 (不可開始)#k\r\n";
            } else if (cm.getQuestStatus(500) == 1) {
                jump += "#L100##r#i4001308# 舒咪遺失的硬幣 (任務完成)#k\r\n";
                jump += "#L2#蘊含尊真心意的花 (可開始)\r\n";
                jump += "#L30##r克莉休拉瑪的委託 (不可開始)\r\n";
                jump += "#L40#查利中士的廢礦探勘任務 (不可開始)#k\r\n";
            } else {
                jump += "#L1##b舒咪遺失的硬幣 (可開始)#k\r\n";
                jump += "#L20##r蘊含尊真心意的花 (不可開始)\r\n";
                jump += "#L30#克莉休拉瑪的委託 (不可開始)\r\n";
                jump += "#L40#查利中士的廢礦探勘任務 (不可開始)#k\r\n";
            }
        } else {
            cm.sendOk("#fn나눔고딕 Extrabold##r* 遊玩條件#k\r\n\r\n#d- 等級160以上的角色\r\n- 可在任務大廳中遊玩#k", 9062004);
            cm.dispose();
        }
        cm.sendSimple(jump);
    } 
    if (selection == 1) {
        cm.dispose();
        cm.openNpc(1052102);
    } else if (selection == 2) {
        cm.dispose();
        cm.openNpc(20000);
    } else if (selection == 3) {
        cm.dispose();
        cm.openNpc(1061000);
    } else if (selection == 4) {
        cm.dispose();
        cm.openNpc(2010000);
    } else if (selection == 5) {
        if (cm.getQuestStatus(505) == 0) {
            if (cm.canHold(1112750) && cm.canHold(2437158)) {
                item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1112750);
                item.setStr(400);
                item.setDex(400);
                item.setInt(400);
                item.setLuk(400);
                item.setWatk(100);
                item.setMatk(100);
                Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
                cm.gainItem(2437158, 1);
                cm.gainItem(2432423, 300);
                cm.forfeitQuest(500);
                cm.forfeitQuest(501);
                cm.forfeitQuest(502);
                cm.forfeitQuest(503);
                cm.forfeitQuest(504);
                cm.forceStartQuest(505);
                cm.sendOk("#fn나눔고딕 Extrabold#哇~! 你也會跳#b跳躍#k啊!?\r\n不錯，辛苦了，這是我給你的獎勵!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i1112750# #b#z1112750##k #r[全屬性50 / 攻、魔80]#k\r\n#i2437158# #b特殊現金選擇箱#k #r3個#k\r\n#i2432423# #b探員升級晶片#k #r300個#k");
                cm.showEffect(false, "monsterPark/clear");
                cm.playSound(false, "Field.img/Party1/Clear");
                cm.dispose();
            } else {
                cm.sendOk("#fn나눔고딕 Extrabold##r請至少騰出一個裝備或其他欄位..#k");
                cm.dispose();
            }
        } else {
            if (cm.canHold(2432423)) {
                cm.gainItem(2432423, 300);
                cm.forfeitQuest(500);
                cm.forfeitQuest(501);
                cm.forfeitQuest(502);
                cm.forfeitQuest(503);
                cm.forfeitQuest(504);
                cm.sendOk("#fn나눔고딕 Extrabold#哇~! 你也會跳#d跳躍#k啊!?\r\n不錯，辛苦了，這是我給你的獎勵!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i2432423# #b探員升級晶片#k #r300個#k");
                cm.showEffect(false, "monsterPark/clear");
                cm.playSound(false, "Field.img/Party1/Clear");
                cm.dispose();
            } else {
                cm.sendOk("#fn나눔고딕 Extrabold##r請至少騰出一個其他欄位..#k");
                cm.dispose();
            }
        }
    } else if (selection == 20) {
        cm.sendOk("#fn나눔고딕 Extrabold##b#h ##k 因為未完成#d舒咪遺失的硬幣#k，\r\n#d蘊含尊真心意的花#k任務#r無法進行#k。", 9062004);
        cm.dispose();
    } else if (selection == 30) {
        cm.sendOk("#fn나눔고딕 Extrabold##b#h ##k因為未完成#d蘊含尊真心意的花#k，\r\n#d克莉休拉瑪的委託#k任務#r無法進行#k。", 9062004);
        cm.dispose();
    } else if (selection == 40) {
        cm.sendOk("#fn나눔고딕 Extrabold##b#h ##k因為未完成#d克莉休拉瑪的委託#k，\r\n#d查利中士的廢礦探勘任務#k任務#r無法進行#k。", 9062004);
        cm.dispose();
    } else if (selection == 100) {
        cm.sendOk("#fn나눔고딕 Extrabold#咦!? #b#h ##k!? 上次真的很感謝!\r\n下次需要幫忙時我會再拜託你~>_<", 1052102);
        cm.dispose();
    } else if (selection == 200) {
        cm.sendOk("#fn나눔고딕 Extrabold#你就是那個為我帶來#b花#k的年輕人吧…\r\n我不會忘記你的幫助…\r\n希望你在這裡能好好休息…", 20000);
        cm.dispose();
    } else if (selection == 300) {
        cm.sendOk("#fn나눔고딕 Extrabold#那時…感謝您確認了#b委託#k的事項。", 1061000);
        cm.dispose();
    } else if (selection == 400) {
        cm.sendOk("#fn나눔고딕 Extrabold#我…代替…感謝你找到#b伊吉兵長#k!", 2010000);
        cm.dispose();
    }  
}
/*
作者 : qudtlstorl79@nate.com
*/

importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);

深蓝 = "#fc0xFF0054FF#";
浅蓝 = "#fc0xFF6B66FF#";
浅紫 = "#fc0xFF8041D9#";
紫色 = "#fc0xFF5F00FF#";
黄色 = "#fc0xFFEDD200#";
黑色 = "#fc0xFF191919#";
粉红 = "#fc0xFFFF5AD9#";
红色 = "#fc0xFFF15F5F#";

绝对拉普斯硬币 = "#fUI/Basic.img/theblackcoin/0#";
西格玛硬币 = "#fUI/Basic.img/theblackcoin/1#";
幻影硬币 = "#fUI/Basic.img/theblackcoin/2#";
蜘蛛硬币 = "#fUI/Basic.img/theblackcoin/3#";
消耗 = "#fUI/Basic.img/theblackcoin/4#";
辅助 = "#fUI/Basic.img/theblackcoin/5#";
头目 = "#fUI/Basic.img/theblackcoin/6#";
点数 = "#fUI/Basic.img/theblackcoin/7#";
强化 = "#fUI/Basic.img/theblackcoin/8#";
饰品 = "#fUI/Basic.img/theblackcoin/9#";
宠物 = "#fUI/CashShop.img/CashItem_label/9#";
徽章 = "#fUI/Basic.img/theblackcoin/26#";
麦格纳斯 = "#fUI/Basic.img/theblackcoin/27#";
十字 = "#fUI/Basic.img/theblackcoin/28#";
英雄的证明 = "#fUI/Basic.img/theblackcoin/34#";
奔跑吧 = "#fUI/Basic.img/theblackcoin/33#";
双黑 = "#fUI/Basic.img/theblackcoin/38#";
血影 = "#fUI/Basic.img/theblackcoin/11#";
联盟 = "#fUI/UIWindow4.img/pointShop/500629/iconShop#";
粉红豆 = "#fUI/UIWindow4.img/pointShop/501661/iconShop#";
新宝石 = "#fUI/UIWindow4.img/pointShop/100712/iconShop#";
黑豆 = "#fUI/UIWindow4.img/pointShop/100161/iconShop#";
潜水 = "#fUI/UIWindow4.img/pointShop/100161/iconShop#";
潜水2 = "#fUI/UIWindow4.img/pointShop/101130/iconShop#";
商店 = "#fUI/Basic.img/theblack/1#";
var status = -1,
    sel = 0;

function start() {
    action(1, 0, 0);
}

/*
            cm.dispose();
            InterServerHandler.EnterCS(cm.getPlayer().getClient(),cm.getPlayer(), false); 商城
*/

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var choose = "" + 商店 + "#fs15#\r\n";
        choose = "#fc0xFF0054FF#★────────商店系統──────────★#k\r\n";
        choose += "#fs15##L0##fc0xFF6B66FF#" + 强化 + " 强化物品商店#l";
        choose += "#L1##fc0xFF5587ED#" + 消耗 + " 消耗物品商店#l\r\n";
        choose += "#L3##fc0xFFDBBC68#" + 辅助 + " 辅助装备物品商店#l";
        choose += "#L4##fc0xFFF15F5F#" + 头目 + " 头目决定石贩售#l\r\n";
        choose += "#L6##fc0xFFEDA900#" + 饰品 + " 饰品装备物品商店#l";
        choose += "#L7##fc0xFFDBC000#" + 联盟 + " 黑色联盟商店#l\r\n";
        //choose += "#L16##fc0xFF030066#" + 徽章 + " 强化徽章商店#l";
        choose += "#L23##fc0xFF030066#" + 潜水 + " 出勤硬币商店#l";
        choose += "#fs15##L20#" + 粉红豆 + " " + 粉红 + "啾噗硬币商店   #k#l\r\n"; // 這裡空白處加空格就可以了
        choose += "#L21#" + 新宝石 + " " + 红色 + "麦格纳斯硬币商店#k#l";
        choose += "#L22#" + 黑豆 + " #fc0xFF4374D9#潜水硬币商店#k#l\r\n\r\n";
        choose += "#fc0xFFF15F5F#★──────────裝備商店────────────★#k\r\n";
        choose += "#L8##fc0xFFFFBB00#" + 绝对拉普斯硬币 + " 绝对拉普斯装备商店#l           #L9#绝对拉普斯硬币兌換#l\r\n";
        //choose += "#L10##fc0xFFFFBB00#" + 西格玛硬币 + " 绝对拉普斯装备商店#l           #L11#西格玛硬币兌換#l\r\n";
        choose += "#L12##fc0xFF8041D9#" + 幻影硬币 + " 阿克因夏德装备商店#l　　#L13#幻影硬币兌換#l\r\n\r\n";
        choose += "#fc0xFFEDD200#★──────────商城────────────★#k\r\n";
        choose += "#L2#" + 新宝石 + " " + 红色 + "商城#k#l";
        //choose += "#L14##fc0xFF8041D9#" + 蜘蛛硬币 + " 阿克因夏德装备商店#l　　#L15#蜘蛛硬币兌換#l\r\n\r\n";
        cm.sendOkS(choose, 0x4);

    } else if (status == 1) {
        sel = selection;
        if (selection == 0) {
            話 = "#fs15#" + 黑色 + "想想看要用哪間商店吧。\r\n"
            話 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            話 += "#fs15##fc0xFF6B66FF##L0#" + 强化 + " 想用楓幣購買強化物品#k" + 黑色 + "。\r\n"
                /*話 += "#fs15##fc0xFFFF5AD9##L1#" + 粉红豆 + " 想用粉紅豆購買強化物品#k" + 黑色 + "。\r\n"*/
            cm.sendSimpleS(話, 0x04, 9062277)
        } else if (selection == 1) {
            cm.dispose();
            cm.openShop(1);
        } else if (selection == 2) {
            cm.sendYesNoS("#fs15##bNIX#k#fc0xFF191919#想去逛逛商城嗎?!\r\n\r\n我準備了很多東西，你可以在 #r商城#k#fc0xFF191919#慢慢逛逛。\r\n\r\n如果你想進入 #r商城#k #fc0xFF191919#，請按下 #r是#k #fc0xFF191919#按鈕。", 0x04, 9062277);
        } else if (selection == 3) {
            cm.dispose();
            cm.openShop(2);
        } else if (selection == 4) {
            cm.dispose();
            cm.openShop(9001212);
        } else if (selection == 5) {
            話 = "#fs15##L20#" + 粉红豆 + " " + 粉红 + "啾噗硬幣商店#k" + 黑色 + "。#l\r\n";
            話 += "#L11#" + 新宝石 + " " + 深蓝 + "新寶石 商店#k" + 黑色 + "。#l\r\n";
            話 += "#L12#" + 黑豆 + " #fc0xFF4374D9#潛水硬幣商店#k" + 黑色 + "。#l\r\n";
            話 += "#L1234#" + 麦格纳斯 + " #fc0xFF4374D9##i2438696#麥格納斯決定石商店#k" + 黑色 + "。#l\r\n\r\n";
            話 += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
            話 += "#L13#" + 潜水 + " #fc0xFFAD8EDB#黑色潛水點數商店相關#k" + 黑色 + "。#l\r\n\r\n";
            話 += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
            話 += "#L14#" + 麦格纳斯 + " #fc0xFFCC723D#泰倫特商店#k" + 黑色 + "。#l\r\n";
            話 += "#L15#" + 英雄的证明 + " #fc0xFFCCA63D#英雄的證明商店#k" + 黑色 + "。#l\r\n";
            話 += "#L16#" + 奔跑吧 + " #fc0xFFCCA63D#奔跑吧硬幣商店#k" + 黑色 + "。#l\r\n";
            話 += "#L17#" + 双黑 + " #fc0xFF997000#黑色硬幣商店#k" + 黑色 + "。#l\r\n";
            話 += "#L18#" + 十字 + " #fc0xFFF2CB61#十字硬幣商店#k" + 黑色 + "。#l\r\n";
            cm.sendSimpleS(話, 0x04, 9062277);
        } else if (selection == 6) {
            cm.dispose();
            cm.openShop(3003414);
        } else if (selection == 7) {
            cm.dispose();
            cm.openShop(9010107);
        } else if (selection == 8) {
            cm.dispose();
            cm.openShop(11);
        } else if (selection == 9) {
            cm.dispose();
            cm.openNpc(2155009);
        } else if (selection == 10) {
            cm.dispose();
            cm.openShop(15);
        } else if (selection == 20) {
            cm.dispose();
            cm.openShop(25);
        } else if (selection == 21) {
            cm.dispose();
            cm.openShop(17);
        } else if (selection == 23) {
            cm.dispose();
            cm.openShop(35);
        } else if (selection == 22) {
            if (cm.getClient().getKeyValue("新寶石") != null) {
                cm.getPlayer().setKeyValue(501372, "point", cm.getClient().getKeyValue("新寶石"));
            }
            cm.dispose();
            cm.openShop(15);
        } else if (selection == 11) {
            cm.dispose();
            cm.openNpc(1540893);
        } else if (selection == 12) {
            cm.dispose();
            cm.openShop(16);
        } else if (selection == 13) {
            cm.dispose();
            cm.openNpc(3003105);
        } else if (selection == 14) {
            cm.dispose();
            cm.openShop(17);
        } else if (selection == 15) {
            cm.dispose();
            cm.openNpc(3003536);
        } else if (selection == 16) {
            cm.dispose();
            cm.openShop(1);
        } else if (selection == 17) {
            話 = "#fs15#" + 黑色 + "#b黑色活動#k" + 黑色 + "開設了有各種物品的商店喔！快選選你想要的吧！呵呵。\r\n"
            話 += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
            話 += "#fs15##L19##fc0xFF00B700#黑色星星硬幣商店#k。#l\r\n";
            話 += "#L20##fc0xFF2478FF#想用黑色星星硬幣抽徽章#k。\r\n";
            話 += "#L21##fc0xFFDB9700#想用黑色星星硬幣使用抽獎系統#k。\r\n";
            cm.sendSimpleS(話, 0x04, 9062277);
        } else if (selection == 20) {
            cm.dispose();
            cm.openShop(43);
        }
    } else if (status == 2) {
        if (sel == 2) {
            InterServerHandler.EnterCS(cm.getPlayer().getClient(), cm.getPlayer(), false);
            cm.dispose();
            return;
        }
        if (selection == 0) {
            cm.dispose();
            cm.openShop(20);
        } else if (selection == 1) {
            cm.dispose();
            cm.openShop(23);
        } else if (selection == 10) {
            cm.dispose();
            cm.openShop(24);
        } else if (selection == 11) {
            cm.dispose();
            cm.openShop(26); //新寶石
        } else if (selection == 12) {
            if (cm.getClient().getKeyValue("新寶石") != null) {
                cm.getPlayer().setKeyValue(501372, "point", cm.getClient().getKeyValue("新寶石"));
            }
            cm.dispose();
            cm.openShop(29);
        } else if (selection == 13) {
            話 = "#fs15#" + 黑色 + "有什麼問題都可以問我！你想問什麼呢？\r\n"
            話 += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
            話 += "#L0##b想用黑色潛水點數商店。\r\n";
            話 += "#L1##b潛水點數要怎麼獲得呢？\r\n\r\n";
            話 += "#L99##r沒有其他問題了。"
            cm.sendSimpleS(話, 0x04, 9062277);
            cm.dispose();
            cm.openShop(28);
        } else if (selection == 14) {
            cm.dispose();
            cm.openShop(30); // 泰倫特
        } else if (selection == 15) {
            cm.dispose();
            cm.openShop(32);
        } else if (selection == 1234) {
            cm.dispose();
            cm.openShop(9000179);
        } else if (selection == 16) {
            cm.dispose();
            cm.openShop(33);
        } else if (selection == 17) {
            cm.dispose();
            cm.openShop(35);
        } else if (selection == 18) {
            cm.dispose();
            cm.openShop(34);
        } else if (selection == 19) {
            cm.dispose();
            cm.openShop(42);
        } else if (selection == 20) {
            cm.dispose();
            cm.sendOk("準備中。");
        } else if (selection == 21) {
            cm.dispose();
            cm.sendOk("準備中。");
        } else if (selection == 22) {
            cm.dispose();
            cm.openShop(24);
        } else if (selection == 23) {
            cm.dispose();
            cm.openShop(28);
        } else if (selection == 24) {
            cm.dispose();
            cm.openShop(14);
        } else if (selection == 25) {
            cm.dispose();
            cm.openShop(26); //新寶石
        } else if (selection == 26) {
            cm.dispose();
            cm.openNpc(3005101); //新寶石
        }
    } else if (status == 3) {
        if (selection == 0) {
            cm.openShop(28);
            cm.dispose();
        } else if (selection == 1) {
            if (cm.getClient().getQuestStatus(50006) == 1) {
                cm.getClient().setCustomKeyValue(50006, "1", "1");
            }
            話 = "#fs15##b黑色潛水點數#k" + 黑色 + "的獲得方法我告訴你吧。\r\n\r\n"
            話 += "" + 黑色 + "首先，#b黑色點數#k" + 黑色 + "只能在 #e村莊#n" + 黑色 + "獲得喔。\r\n"
            話 += "" + 黑色 + "在村莊使用 #b椅子或坐騎#k" + 黑色 + "時，每 #r60秒#k" + 黑色 + "會累積 #r1點數#k" + 黑色 + "喔。\r\n"
            話 += "#b任何椅子或坐騎#k" + 黑色 + "都可以喔！商店裡有 #b各種物品#k" + 黑色 + "，好好努力收集吧！呵呵呵。\r\n"
            cm.sendOkS(話, 0x04, 9062277);
            cm.dispose();
        } else if (selection == 99) {
            cm.dispose();
        }
    }
}
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);

파랑 = "#fc0xFF0054FF#";
연파 = "#fc0xFF6B66FF#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
검정 = "#fc0xFF191919#";
화이트 = "#fc0xFFFFFFFF#";

틀 = "#fs15##fUI/Basic.img/actMark/23#";
商店0 = "#k 商店0 #r";
商店1 = "#k 商店1 #r";
商店2 = "#k 商店2 #r";
商店3 = "#k 商店3 #r";
商店4 = "#k 商店4 #r";
商店5 = "#k 商店5 #r";
商店6 = "#k 商店6 #r";
商店7 = "#k 商店7 #r";
商店8 = "#k 商店8 #r";
商店9 = "#k 商店9 #r";
商店10 = "#k 商店10 #r";
商店11 = "#k 商店11 #r";
商店12 = "#k 商店12 #r";
商店13 = "#k 商店13 #r";
商店14 = "#k 商店14 #r";
商店15 = "#k 商店15 #r";
商店16 = "#k 商店16 #r";
商店17 = "#k 商店17 #r";
商店18 = "#k 商店18 #r";
商店19 = "#k 商店19 #r";
商店20 = "#k 商店20 #r";
商店21 = "#k 商店21 #r";
商店22 = "#k 商店22 #r";
商店23 = "#k 商店23 #r";
商店24 = "#k 商店24 #r";
商店25 = "#k 商店25 #r";
商店26 = "#k 商店26 #r";
商店27 = "#k 商店27 #r";
商店28 = "#k 商店28 #r";
商店29 = "#k 商店29 #r";
商店30 = "#k 商店30 #r";
商店31 = "#k 商店31 #r";
商店32 = "#k 商店32 #r";
商店33 = "#k 商店33 #r";
商店34 = "#k 商店34 #r";
商店35 = "#k 商店35 #r";
商店36 = "#k 商店36 #r";
商店37 = "#k 商店37 #r";
商店38 = "#k 商店38 #r";
商店39 = "#k 商店39 #r";
商店40 = "#k 商店40 #r";
商店41 = "#k 商店41 #r";
商店42 = "#k 商店42 #r";
商店43 = "#k 商店43 #r";
商店44 = "#k 商店44 #r";


var status = -1;

function start() {
    action(1, 0, 0);
}

/*
            cm.dispose();
            InterServerHandler.EnterCS(cm.getPlayer().getClient(),cm.getPlayer(), false); 캐시샵
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
        var msg = "                    " + 틀 + "#l\r\n";
                msg += "#fc0xFFD5D5D5#──────────────────────────#l\r\n";
        msg += "#L0##fc0xFFFF3636#" + 商店0 + "#l\r\n";// 無法打開
        msg += "#L1##fc0xFFFF3636#" + 商店1 + "#l\r\n";//藥水喇叭飛鏢皮膚 通用型 金幣
        msg += "#L2##fc0xFFFF3636#" + 商店2 + "#l\r\n";//各職業輔助裝備 金幣
        msg += "#L3##fc0xFFFF3636#" + 商店3 + "#l\r\n";//七日胸章 金幣
        msg += "#L4##fc0xFFFF3636#" + 商店4 + "#l\r\n";//神秘之影 NEO寶石 活動幣
        msg += "#L5##fc0xFFFF3636#" + 商店5 + "#l\r\n";//定居金10億 金幣
        msg += "#L6##fc0xFFFF3636#" + 商店6 + "#l\r\n";//古墓鑰匙 金幣 互動幣
        msg += "#L7##fc0xFFFF3636#" + 商店7 + "#l\r\n";//航海套裝 金幣
        msg += "#L8##fc0xFFFF3636#" + 商店8 + "#l\r\n";//商店表情 各種 金幣
        msg += "#L9##fc0xFFFF3636#" + 商店9 + "#l\r\n";//古木鑰匙 金幣
        msg += "#L10##fc0xFFFF3636#" + 商店10 + "#l\r\n";//航海套裝 消耗 洛印幣
        msg += "#L11##fc0xFFFF3636#" + 商店11 + "#l\r\n";//商城動態 特效 金幣
        msg += "#L12##fc0xFFFF3636#" + 商店12 + "#l\r\n";//商城表情 金幣
        msg += "#L13##fc0xFFFF3636#" + 商店13 + "#l\r\n";//神秘之影套裝 消耗 亞科蘭諾硬幣
        msg += "#L14##fc0xFFFF3636#" + 商店14 + "#l\r\n";//方塊强化 印章 通用型 金幣
        msg += "#L15##fc0xFFFF3636#" + 商店15 + "#l\r\n";//各職業通明用品終極形態變色卷等 金幣
        msg += "#L16##fc0xFFFF3636#" + 商店16 + "#l\r\n";//方塊强化 金幣 備用行
        msg += "#L17##fc0xFFFF3636#" + 商店17 + "#l\r\n";//經驗卷  精靈吊墜 神秘徽章 水光水晶 奇幻 喇叭 騎寵  核心寶石等 金幣
        msg += "#L18##fc0xFFFF3636#" + 商店18 + "#l\r\n";//喇叭 强化 妖精之心 任務卷軸 經驗 等 金幣
        msg += "#L19##fc0xFFFF3636#" + 商店19 + "#l\r\n";//安卓機器人等通用性   金幣
        msg += "#L20##fc0xFFFF3636#" + 商店20 + "#l\r\n";//暴君飾品通用性 梅格納斯金幣
        msg += "#L21##fc0xFFFF3636#" + 商店21 + "#l\r\n";//方塊 星火 混色瞳 核心 聚財秘藥 經驗秘藥 經驗 戒指黃金羅盤等
        msg += "#L22##fc0xFFFF3636#" + 商店22 + "#l\r\n";//諾巴徽章 暴君兌換 消耗藥品 諾巴通用性
        msg += "#L23##fc0xFFFF3636#" + 商店23 + "#l\r\n";//方塊强化等 驚奇卷 火花黃金鐵錘等 金幣
        msg += "#L24##fc0xFFFF3636#" + 商店24 + "#l\r\n";//神秘符文 核心寶石 經驗寶石 經驗吊墜瞬移之石 MVP經驗 奇幻月光 喇叭 騎寵等 通用性 金幣
        msg += "#L25##fc0xFFFF3636#" + 商店25 + "#l\r\n";//喇叭 任務紙  經驗 妖精之心等 金幣
        msg += "#L26##fc0xFFFF3636#" + 商店26 + "#l\r\n";//星力20  火花 方塊 等 APK活動幣
        msg += "#L27##fc0xFFFF3636#" + 商店27 + "#l\r\n";//消耗  裝備爆率吊墜 MVP經驗 經驗 奇幻等 怪物公園幣
        msg += "#L28##fc0xFFFF3636#" + 商店28 + "#l\r\n";//神秘徽章  金之心 普通强化戒指 核心 聚財 經驗纍積瞳孔顔色 火花方塊等 金幣
        msg += "#L29##fc0xFFFF3636#" + 商店29 + "#l\r\n";//低級吊墜 眼飾 臉飾等  金幣
        msg += "#L30##fc0xFFFF3636#" + 商店30 + "#l\r\n";// 方塊 火花 卷軸 强化 金幣  1金幣
        msg += "#L31##fc0xFFFF3636#" + 商店31 + "#l\r\n";//方塊 火花 卷軸 月光水晶 奇幻等 活動幣  RULL值
        msg += "#L32##fc0xFFFF3636#" + 商店32 + "#l\r\n";//BOSS結晶買賣商店 通用性
        msg += "#L33##fc0xFFFF3636#" + 商店33 + "#l\r\n";//7周年硬幣商店 方塊 火花 强化  經驗 星卷 紀念幣 奇幻等
        msg += "#L34##fc0xFFFF3636#" + 商店34 + "#l\r\n";//十字軍團硬幣商店
        msg += "#L35##fc0xFFFF3636#" + 商店35 + "#l\r\n";//APK活動幣商店
        msg += "#L36##fc0xFFFF3636#" + 商店36 + "#l\r\n";//瞳孔染色  金幣
        msg += "#L37##fc0xFFFF3636#" + 商店37 + "#l\r\n";//椅子 武林道場傷害  武陵商店通用性
        msg += "#L38##fc0xFFFF3636#" + 商店38 + "#l\r\n";//BOSS寶石掉落兌換商店 通用  寶石
        msg += "#L39##fc0xFFFF3636#" + 商店39 + "#l\r\n";//拉克蘭妖怪硬幣  亞科拉諾硬幣  火花 奇幻方塊  潛在卷 經驗 核心寶石等 金幣
        msg += "#L40##fc0xFFFF3636#" + 商店40 + "#l\r\n";//活動幣兌換商店  活動幣  RULL值
        msg += "#L41##fc0xFFFF3636#" + 商店41 + "#l\r\n";//成長秘藥200 229藥水
        msg += "#L42##fc0xFFFF3636#" + 商店42 + "#l\r\n";//七周年硬幣商店2  通用性
        msg += "#L43##fc0xFFFF3636#" + 商店43 + "#l\r\n";//奇幻 月光 潛在 覺醒戒指 航海師武器等  金幣
        msg += "#L44##fc0xFFFF3636#" + 商店44 + "#l\r\n";//活動幣商店  魔方  袋子 20星卷 等  常用型  會議憑證幣


        cm.sendOk(msg);

    } else if (status == 1) {
        if (selection == 0) { 
            cm.dispose();
            cm.openShop(0);
        } else if (selection == 1) { 
            cm.dispose();
            cm.openShop(1);
        } else if (selection == 2) { 
            cm.dispose();
            cm.openShop(2);
        } else if (selection == 3) {
            cm.dispose();
            cm.openShop(3);
        } else if (selection == 4) { 
            cm.dispose();
            cm.openShop(4);
        } else if (selection == 5) {
            cm.dispose();
            cm.openShop(5);
        } else if (selection == 6) { 
            cm.dispose();
            cm.openShop(6);
        } else if (selection == 7) { 
            cm.dispose();
            cm.openShop(7);
        } else if (selection == 8) { 
            cm.dispose();
            cm.openShop(8);
        } else if (selection == 9) { 
            cm.dispose();
            cm.openShop(9);
        } else if (selection == 10) { 
            cm.dispose();
            cm.openShop(10);
        } else if (selection == 11) { 
            cm.dispose();
            cm.openShop(11);
        } else if (selection == 12) { 
            cm.dispose();
            cm.openShop(12);
        } else if (selection == 13) {
            cm.dispose();
            cm.openShop(13);
        } else if (selection == 14) {
            cm.dispose();
            cm.openShop(14);
        } else if (selection == 15) {
            cm.dispose();
            cm.openShop(15);
        } else if (selection == 16) {
            cm.dispose();
            cm.openShop(16);
        } else if (selection == 17) {
            cm.dispose();
            cm.openShop(17);
        } else if (selection == 18) {
            cm.dispose();
            cm.openShop(18);
		} else if (selection == 19) {
            cm.dispose();
            cm.openShop(19);
        } else if (selection == 20) {
            cm.dispose();
            cm.openShop(20);
        } else if (selection == 21) {
            cm.dispose();
            cm.openShop(21);
        } else if (selection == 22) {
            cm.dispose();
            cm.openShop(22);
        } else if (selection == 23) {
            cm.dispose();
            cm.openShop(23);
        } else if (selection == 24) {
            cm.dispose();
            cm.openShop(24);
        } else if (selection == 25) {
            cm.dispose();
            cm.openShop(25);
        } else if (selection == 26) {
            cm.dispose();
            cm.openShop(26);
        } else if (selection == 27) {
            cm.dispose();
            cm.openShop(27);
        } else if (selection == 28) {
            cm.dispose();
            cm.openShop(28);
        } else if (selection == 29) {
            cm.dispose();
            cm.openShop(29);
        } else if (selection == 30) {
            cm.dispose();
            cm.openShop(30);
        } else if (selection == 31) {
            cm.dispose();
            cm.openShop(31);
        } else if (selection == 32) {
            cm.dispose();
            cm.openShop(32);
        } else if (selection == 33) {
            cm.dispose();
            cm.openShop(33);
        } else if (selection == 34) {
            cm.dispose();
            cm.openShop(34);
        } else if (selection == 35) {
            cm.dispose();
            cm.openShop(35);
        } else if (selection == 36) {
            cm.dispose();
            cm.openShop(36);
        } else if (selection == 37) {
            cm.dispose();
            cm.openShop(37);
        } else if (selection == 38) {
            cm.dispose();
            cm.openShop(38);
        } else if (selection == 39) {
            cm.dispose();
            cm.openShop(39);
        } else if (selection == 40) {
            cm.dispose();
            cm.openShop(40);
        } else if (selection == 41) {
            cm.dispose();
            cm.openShop(41);
        } else if (selection == 42) {
            cm.dispose();
            cm.openShop(42);
        } else if (selection == 43) {
            cm.dispose();
            cm.openShop(43);
        } else if (selection == 44) {
            cm.dispose();
            cm.openShop(44);
        }      else if (selection == 6974) {
            cm.dispose();
            cm.openShop(9010107);
        } else if (status == 2) {
            cm.dispose();
            cm.openShop(9010107);
        } else if (selection == 1) {
            cm.dispose();
            cm.openShop(9010107);
        }
    }
}
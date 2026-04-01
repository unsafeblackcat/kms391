/*
제작자 : qudtlstorl79@nate.com
*/

importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);

파랑 = "#fc0xFF0054FF#";
연파 = "#fc0xFF6B66FF#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
검정 = "#fc0xFF191919#";
분홍 = "#fc0xFFFF5AD9#";

var white = "#fc0xFFFFFFFF#";
var enter = "\r\n";

앱솔랩스코인 = "#fUI/Basic.img/theblackcoin/0#";
스티그마코인 = "#fUI/Basic.img/theblackcoin/1#";
판타즈마코인 = "#fUI/Basic.img/theblackcoin/2#";
아라크노코인 = "#fUI/Basic.img/theblackcoin/3#";
소비 = "#fUI/Basic.img/theblackcoin/4#";
보조 = "#fUI/Basic.img/theblackcoin/5#";
보스 = "#fUI/Basic.img/theblackcoin/6#";
포인트 = "#fUI/Basic.img/theblackcoin/7#";
강화 = "#fUI/Basic.img/theblackcoin/8#";
악세 = "#fUI/Basic.img/theblackcoin/9#";
펫 = "#fUI/CashShop.img/CashItem_label/9#";
엠블렘 = "#fUI/Basic.img/theblackcoin/26#";
매그너스 = "#fUI/Basic.img/theblackcoin/27#";
십자 = "#fUI/Basic.img/theblackcoin/28#";
영웅의증표 = "#fUI/Basic.img/theblackcoin/34#";
뛰어라 = "#fUI/Basic.img/theblackcoin/33#";
더블랙 = "#fUI/Basic.img/theblackcoin/38#";
블라썸 = "#fUI/Basic.img/theblackcoin/11#";
유니온 = "#fUI/UIWindow4.img/pointShop/500629/iconShop#";
분홍콩 = "#fUI/UIWindow4.img/pointShop/100828/iconShop#";
네오젬 = "#fUI/UIWindow4.img/pointShop/100712/iconShop#";
검은콩 = "#fUI/UIWindow4.img/pointShop/501468/iconShop#";
잠수 = "#fUI/UIWindow4.img/pointShop/100161/iconShop#";
상점 = "#fUI/Basic.img/theblack/1#";
var St = -1, sel = 0;

var 서버이름 = "PRAY";

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
        St++;
    }
    if (St == 0) {
        var choose = 연보+":: 商店系統，請選擇您想要的商店! #k#fs30#\r\n";
        //choose = "#fc0xFFD5D5D5#───────────────────────────#k\r\n\r\n";
        choose += "#fs15##L0##fc0xFF6B66FF#" + 강화 + " 强化道具商店#l";        choose += "#L4##fc0xFF030066#" + 보스 + " BOSS結晶石銷售#l\r\n\r\n";
        choose += "#L1##fc0xFF6B66FF#" + 소비 + " 消費道具商店#l";                choose += "#L1000##fc0xFF030066#" + 소비 + " 安卓商店#l\r\n\r\n";
        choose += "#L3##fc0xFF6B66FF#" + 보조 + " 輔助道具商店#l";                choose += "#L7##fc0xFF030066# 聯合消費商店#l";        
        //choose += "#L16##fc0xFF030066#" + 엠블렘 + " 출석코인 상점#l　";
        //choose += "#L3##fc0xFF030066#" + 보조 + " 보조장비 상점#l\r\n";
        //choose += "#L4##fc0xFFF15F5F#" + 보스 + " 보스결정석 판매#l　";
        //choose += "#L4##fc0xFFF15F5F#" + 보스 + " 보스결정석 판매#l　";
        //choose += "#L4##fc0xFFDBC000#" + 보스 + " 보스결정석 판매#l　";
        //choose += "#L7##fc0xFFDBC000# 유니온소비 상점#l\r\n";
        choose += "\r\n\r\n#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        choose += "#L16##fc0xFF8C8C8C#" + 엠블렘 + " 簽到幣商店#l　";         choose += "#L102##fc0xFF8C8C8C#" + 엠블렘 + " 馬尼幣商店#l　";
        //choose += "#L5##fc0xFF8C8C8C#" + 포인트 + " 컨텐츠상점#l\r\n";
        //choose += "#L4##fc0xFFF15F5F#" + 보스 + " 보스결정석 판매#l　";
        //choose += "#L6##fc0xFFEDA900#" + 악세 + " 악세 장비템 상점#l　";
        //choose += "#L7##fc0xFFDBC000# 유니온 상점#l\r\n";
        //choose += "#L17##fc0xFFFFB85A#" + 블라썸 + " 스타 코인 상점#l\r\n";
        choose += "\r\n\r\n#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        choose += "#L18##fc0xFFF15F5F#" + 포인트 + " 文宣積分商店#l　";
        choose += "#L19##fc0xFFF15F5F#" + 포인트 + " 後援積分商店#l";
          choose += "\r\n\r\n#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        choose +="#L8##fc0xFF8041D9# App Solabs設備商店#l";
        choose +="#L12##fc0xFF8041D9# #k#fc0xFF8041D9#阿肯設備商店#l\r\n";
          //choose +="#L9##fc0xFFFFBB00# 타일런트 장비 상점#l\r\n";
          //choose +="#L12##fc0xFF8041D9# #k#fc0xFF8041D9#아케인장비상점#l\r\n";

        cm.sendOkS(choose, 0x4);

    } else if (St == 1) {
        sel = selection;
        if (selection == 0) {
             cm.dispose();
            cm.openShop(20);
        } else if (selection == 1) {
            cm.dispose();
            cm.openShop(1);
        } else if (selection == 2) {
           cm.dispose();
           cm.openShop(38);
        } else if (selection == 3) {
            cm.dispose();
            cm.openShop(2);
        } else if (selection == 4) {
            cm.dispose();
            cm.openShop(9001212);
        } else if (selection == 5) {

			말 = "#fs15#" + 검정 + "뭐든지 물어보게! 어떤게 궁금한가?\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
			말 += "#L100##b"+서버이름+" 사냥 상점을 이용하고 싶습니다.\r\n";
            말 += "#L101##b"+서버이름+" 잠수 포인트 상점을 이용하고 싶습니다.\r\n";
			말 += "#L102##b"+서버이름+" 출석 상점을 이용하고 싶습니다.\r\n";
            cm.sendOk(말);
        } else if (selection == 102) {
            cm.dispose();
            cm.openShop(27);
        } else if (selection == 1000) {
            cm.dispose();
            cm.openShop(26);
        } else if (selection == 6) {
            cm.dispose();
            cm.openShop(106);
        } else if (selection == 7) {
            cm.dispose();
            cm.openShop(9010107);
        } else if (selection == 8) {
            cm.dispose();
            cm.openShop(11);
        } else if (selection == 9) {
            cm.dispose();
            cm.openShop(108);
        } else if (selection == 10) {
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
            cm.openShop(25);
        } else if (selection == 17) {
            cm.dispose();
            cm.openShop(42);
		} else if (selection == 18) { //홍보 시스템
            cm.dispose();
            cm.openNpc(9000216);
		} else if (selection == 19) { //후원 시스템
            cm.dispose();
            cm.openNpc(9001228);
        } else if (selection == 20) {
            cm.dispose();
            cm.openShop(113);
		} else if (selection == 21) {
            cm.dispose();
            cm.openShop(109);
	}
    
   
    }else if (St == 2) {
        if (selection == 0) {
            cm.dispose();
            cm.openShop(20);
        } else if (selection == 1) {
            cm.dispose();
            cm.openShop(23);
		  } else if (selection == 100) {
            cm.dispose();
            cm.openNpc(9062537);
		  } else if (selection == 101) {
            cm.dispose();
            cm.openShop(103);
			} else if (selection == 102) {
            cm.dispose();
            cm.openShop(110);
		
        } else if (selection == 10) {
            cm.dispose();
            cm.openShop(24);
        } else if (selection == 11) {
            cm.dispose();
            cm.openShop(26);
        } else if (selection == 12) {
            if (cm.getClient().getKeyValue("WishCoinGain") != null) {
                cm.getPlayer().setKeyValue(501372, "point", cm.getClient().getKeyValue("WishCoinGain"));
            }
            cm.dispose();
            cm.openShop(29);
        } else if (selection == 13) {
            말 = "#fs15#" + 검정 + "뭐든지 물어보게! 어떤게 궁금한가?\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
            말 += "#L0##b"+서버이름+" 잠수 포인트 상점을 이용하고 싶습니다.\r\n";
            말 += "#L1##b잠수 포인트는 어떻게 획득하나요?\r\n\r\n";
            말 += "#L99##r더이상 궁금한게 없습니다.";
            cm.sendSimpleS(말, 0x04, 9401232);
            /*cm.dispose();
            cm.openShop(28);*/
        } else if (selection == 14) {
            cm.dispose();
            cm.openShop(30);
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
            cm.sendOk("준비중입니다.");
        } else if (selection == 21) {
            cm.dispose();
            cm.sendOk("준비중입니다.");
        } else if (selection == 22) {
            cm.dispose();
            cm.openShop(24);
        } else if (selection == 23) {
            cm.dispose();
            cm.openShop(25);
        } else if (selection == 24) {
            cm.dispose();
            cm.openShop(14);
        } else if (selection == 25) {
            cm.dispose();
            cm.openShop(26);
        }
    } else if (St == 3) {
        if (selection == 0) {
            cm.openShop(28);
            cm.dispose();
        } else if (selection == 1) {
            if (cm.getClient().getQuestSt(50006) == 1) {
                cm.getClient().setCustomKeyValue(50006, "1", "1");
            }
            말 = "#fs15##b"+서버이름+" 잠수 포인트#k" + 검정 + " 획득 방법에 대해 알려주겠네.\r\n\r\n"
            말 += "" + 검정 + "우선, #b"+서버이름+" 포인트#k" + 검정 + "는 #e마을#n" + 검정 + "에서만 획득이 가능하다네.\r\n"
            말 += "" + 검정 + "마을에서 #b의자나 라이딩#k" + 검정 + "을 사용중일때 #r60초#k" + 검정 + "마다 #r1 포인트#k" + 검정 + "씩 적립된다네.\r\n"
            말 += "#b어떠한 의자나 라이딩#k" + 검정 + "을 사용해도 상관없다네! 상점에 #b다양한 물품#k" + 검정 + "이 있으니 열심히 모아보게나! 크크크.\r\n"
            cm.sendOkS(말, 0x04, 9401232);
            cm.dispose();
        } else if (selection == 99) {
            cm.dispose();
        }
    }
}
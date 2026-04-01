importPackage(Packages.constants);


var status = -1;
var enter = "\r\n";
var talkType = 0x86;
var 별 = "#fUI/FarmUI.img/objectStatus/star/whole#";
var HOT = "#fUI/CashShop.img/CSEffect/hot/0#";
var NEW = "#fUI/CashShop.img/CSEffect/new/0#";


function start() {
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {

        var choose = "#fs25##fc0xFF050099##k";
       // choose += "  #fs20##fc0xFFF781D8##e[BOSS快捷傳送菜單]#n#k\r\n";
       // choose += "  #fs20##e[簡單]#n\r\n";
        choose += "#fs20##L1##fUI/UIBoss.img/BossList/1/0##e#n#l";
        choose += "#L2##fUI/UIBoss.img/BossList/2/0##e#n#l";
        choose += "#L6##fUI/UIBoss.img/BossList/11/0##e#n#l";
        choose += "#L3##fUI/UIBoss.img/BossList/3/0##e#n#l";
        choose += "#L4##fUI/UIBoss.img/BossList/8/0##e#n#l";
        choose += "#L99##fUI/UIBoss.img/BossList/21/0##e#n#l\r\n";
        choose += "#L7##fUI/UIBoss.img/BossList/12/0##e#n#l";
        choose += "#L5##fUI/UIBoss.img/BossList/9/0##e#n#l";
      //  choose += "  #fs20##b#e[困難]#n#k\r\n";
        choose += "#L8##fUI/UIBoss.img/BossList/5/0##e#n#l";
        choose += "#L8##fUI/UIBoss.img/BossList/4/0##e#n#l";
        choose += "#L8##fUI/UIBoss.img/BossList/6/0##e#n#l";
        choose += "#L8##fUI/UIBoss.img/BossList/7/0##e#n#l\r\n\r\n";
        choose += "#L9##fUI/UIBoss.img/BossList/22/0##e#n#l";
        choose += "#fs20##L10##fUI/UIBoss.img/BossList/10/0##e#n#l";
        choose += "#L11##fUI/UIBoss.img/BossList/13/0##e#n#l";
        choose += "#L14##fUI/UIBoss.img/BossList/23/0##e#n#l";
        choose += "#L12##fUI/UIBoss.img/BossList/15/0##e#n#l";
        choose += "#L25##fUI/UIBoss.img/BossList/19/0##e#n#l\r\n\r\n";



        choose += "#L103##fUI/UIBoss.img/BossList/24/0##e#n#l";
        choose += "#L105##fUI/UIBoss.img/BossList/27/0##e#n#l";
        choose += "#L98##fUI/UIBoss.img/BossList/26/0##e#n#l";
        choose += "#L97##fUI/UIBoss.img/BossList/25/0##e#n#l#k";
        choose += "#L108##fUI/UIBoss.img/BossList/29/0##e#n#l\r\n";
        //choose += "  #fs20##r#e[HARD]#n#k\r\n";
        choose += "#L106##fUI/UIBoss.img/BossList/28/0##e#n#l";
        choose += "#L119##fUI/UIBoss.img/BossList/31/0##e#n#l";
        choose += "#L118##fUI/UIBoss.img/BossList/30/0##e#n#l\r\n";

        cm.sendSimpleS(choose, talkType);

    } else if (selection == 1) {//혼텔
        cm.dispose();
cm.warpParty(211042300);
    } else if (selection == 2) {//힐라
        cm.dispose();
cm.warpParty(240050400);
    } else if (selection == 3) {//반 레온
	cm.dispose();
cm.warpParty(262030000);
    } else if (selection == 4) {//아카이럼
	cm.dispose();
cm.warpParty(211070000);
    } else if (selection == 5) {//시그너스
	cm.dispose();
cm.warpParty(272020110);
    } else if (selection == 6) {//매그너스
	cm.dispose();
cm.warpParty(270050000);
    } else if (selection == 7) {//칸스핑크빈
	cm.dispose();
cm.warpParty(271040000);
    } else if (selection == 8) {//칸스블러디퀸
	cm.dispose();
cm.warpParty(105200000);
    } else if (selection == 9) {//칸스벨룸
	cm.dispose();
cm.warpParty(220080000);
    } else if (selection == 10) {//스우
	cm.dispose();
cm.warpParty(401060000);
    } else if (selection == 11) {//자쿰
	cm.dispose();
cm.warpParty(350060300);
    } else if (selection == 12) {//칸스피에르
	cm.dispose();
cm.warpParty(105300303);
    } else if (selection == 13) {//칸스반반
cm.openNpc(3003208);
	cm.dispose();
    } else if (selection == 14) {//루시드
	cm.dispose();
cm.warpParty(450007240);
    } else if (selection == 20) {//불꽃늑대
	cm.dispose();
             cm.openNpc(9120012);
    } else if (selection == 21) {//우르스
	cm.dispose();
cm.warpParty(970072200);
  } else if (selection == 103) {//진힐라
	cm.dispose();
cm.warpParty(450011990);
  } else if (selection == 108) {//가디언 엔젤 슬라임
	cm.dispose();
cm.warpParty(160000000);
  } else if (selection == 97) {//검은마법사
	cm.dispose();
cm.warpParty(450012500);
  } else if (selection == 107) {//도로시
	cm.dispose();
cm.warpParty(992050000);
  } else if (selection == 118) {//칼로스
	cm.dispose();
cm.warpParty(410005005);
  } else if (selection == 119) {//칼로스
    cm.dispose();
cm.warpParty(410007025);
  }  else if (selection == 102) {//진격거
	cm.dispose();
cm.warpParty(814030000);
    } else if (selection == 22) {//자쿰
	cm.dispose();
cm.openNpc(9000192);
    } else if (selection == 23) {//칸스피에르
	cm.dispose();
cm.warpParty(910810000);
    } else if (selection == 24) {//칸스반반
cm.openNpc(3003208);
	cm.dispose();
    } else if (selection == 25) {//루시드
cm.warpParty(450004000);
	cm.dispose();
    } else if (selection == 105) {//듄켈
	cm.dispose();
cm.warpParty(450012200);
    } else if (selection == 106) {//듄켈
	cm.dispose();
cm.warpParty(410000670);
cm.openNpc(9000192);
   } else if (selection == 98) {//더스크
	cm.dispose();
cm.warpParty(450009301);
   } else if (selection == 99) {//카웅
cm.warpParty(221030900);
	cm.dispose();
} else if (selection == 26) {//루시드
	cm.dispose();
cm.openNpc(9000192);
    }
}
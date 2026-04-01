/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

importPackage(Packages.constants);

아이콘 = "#fUI/GuildMark.img/BackGround/00001026/1#"
아이콘2 = "#fUI/GuildMark.img/BackGround/00001028/12#"
아이콘3 = "#fUI/GuildMark.img/BackGround/00001028/9#"

별1 = "#fUI/GuildMark.img/Mark/Pattern/00004014/11#"

Num0 = "#fUI/GuildMark.img/Mark/Letter/00005026/15#"
Num1 = "#fUI/GuildMark.img/Mark/Letter/00005027/15#"
Num2 = "#fUI/GuildMark.img/Mark/Letter/00005028/4#"

T2 = "#fUI/GuildMark.img/Mark/Letter/00005019/4#"
O2 = "#fUI/GuildMark.img/Mark/Letter/00005014/4#"
P2 = "#fUI/GuildMark.img/Mark/Letter/00005015/4#"

T10 = "#fUI/GuildMark.img/Mark/Letter/00005019/15#"
O10 = "#fUI/GuildMark.img/Mark/Letter/00005014/15#"
P10 = "#fUI/GuildMark.img/Mark/Letter/00005015/15#"

랭킹 = "#fUI/Basic.img/theblackcoin/24#";
검정 = "#fc0xFF191919#";

status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
   if (mode == -1) {
   cm.dispose();
   } else {
   if (mode == 0) {
   cm.dispose();
   return;
   }
   if (mode == 1)
   status++;
   else
            status--;
   }
   if (status == 0) {
      말 = ""+검정+"#e[PRAY]#n 강화 시스템 입니다.\r\n"
      //말 += "#fs15##fc0xFF4641D9##L1#[PRAY]#k "+검정+"#i1004808# 아케인 강화 시스템 을 이용하고싶어요!#l\r\n\r\n";
      말 += "#fs15##fc0xFF4641D9##L2#[PRAY]#k "+검정+"#i1012632# 특별 잠재능력 시스템 을 이용하고 싶어요!#l\r\n\r\n";
      말 += "#fs15##fc0xFF4641D9##L3#[PRAY]#k "+검정+"#i1702792# 치창템 강화 시스템 을 이용하고싶어요!#l\r\n\r\n";
      cm.sendSimple(말);
   } else if (status == 1) {
      if (selection == 1) {
         cm.dispose();
         cm.openNpc(2192032);
      } else if (selection == 2) {
         cm.dispose();
         cm.openNpc(2192031);  
      } else if (selection == 3) {
         cm.dispose();
         cm.openNpc(3004430);
      } else if (selection == 4) {
         cm.dispose();
         cm.openNpc(2060001);
      } else if (selection == 5) {
         cm.dispose();
         cm.openShop(20);
      } else if (selection == 6) {
         cm.dispose();
         cm.openShop(20);
      } else if (selection == 7) {
         cm.dispose();
         cm.openShop(20);
      } else if (selection == 8) {
         cm.dispose();
         cm.openShop(20);
      } else if (selection == 9) {
         cm.dispose();
         cm.openShop(20);
      } else if (selection == 10) {
         cm.dispose();
         cm.openShop(20);
      } else if (selection == 11) {
         cm.dispose();
         cm.openShop(20);
      }
   } else if (status == 2) {
      if (selection == 0) {
         cm.sendSimple(말);
      } else if (selection == 1) {
         cm.dispose();
         cm.openNpc(1540005);
      } else if (selection == 2) {
         cm.dispose();
         cm.openNpc(1022111);
      } else if (selection == 3) {
         cm.dispose();
         cm.openNpc(1022112);
      }
   } else if (status == 3) {
      if (selection == 0) {
         cm.dispose();
         cm.showTSRanking();
      } else if (selection == 1) {
         cm.dispose();
         cm.openNpc(1540006);
      }
   }
   
}
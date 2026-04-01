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
      말 = ""+검정+"#e[PRAY]#n 상점 시스템 입니다.\r\n"
      말 += "#fs15##fc0xFF4641D9##L1#[PRAY]#k "+검정+"보스결정석 상점을 이용하고싶어요!#l\r\n\r\n";
      말 += "#fs15##fc0xFF4641D9##L2#[PRAY]#k "+검정+"펫 상점을 이용하고 싶어요!#l\r\n\r\n";
      //말 += "#fs15##fc0xFF4641D9##L3#[PRAY]#k "+검정+"강화아이템 상점을 이용하고 싶어요!#l\r\n";
      //말 += "#fs15##fc0xFF4641D9##L4#[PRAY]#k "+검정+"잠수코인 상점을 이용하고 싶어요!#l\r\n";
      //말 += "#fs15##fc0xFF4641D9##L5#[PRAY]#k "+검정+"하얀 결정 - F 상점을 이용하고 싶어요!#l\r\n";
     // 말 += "#fs15##fc0xFF4641D9##L6#[PRAY]#k "+검정+"출석코인 상점을 이용하고 싶어요!#l\r\n";
     // 말 += "#fs15##fc0xFF4641D9##L7#[PRAY]#k "+검정+"매그너스코인 상점을 이용하고 싶어요!#l\r\n";
      cm.sendSimple(말);
   } else if (status == 1) {
      if (selection == 1) {
         cm.dispose();
         cm.openShop(9001212);
      } else if (selection == 2) {
         cm.dispose();
         cm.openNpc(1530330);  
      } else if (selection == 3) {
         cm.dispose();
         cm.openShop(20);
      } else if (selection == 4) {
         cm.dispose();
         cm.openShop(15);
      } else if (selection == 5) {
         cm.dispose();
         cm.openShop(25);
      } else if (selection == 6) {
         cm.dispose();
         cm.openShop(35);
      } else if (selection == 7) {
         cm.dispose();
         cm.openShop(17);
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
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


// 9072306
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
      말 = ""+검정+"#e[PRAY]#n 각종코인 교환 시스템 입니다.\r\n"
      말 += "#fs15##fc0xFF4641D9##L1#[PRAY]#k "+검정+"유니온 코인을 교환하고 싶습니다..#l\r\n";
      말 += "#fs15##fc0xFF4641D9##L2#[PRAY]#k "+검정+"사냥 코인을 교환하고 싶습니다..#l\r\n\r\n";
      //  말 += "#k#fs#───────────────────────────\r\n#b#fn돋움##fs15##b";
/*      말 += "#fs15##fc0xFF4641D9##L3#[PRAY]#k "+검정+"십자코인 상점을 이용하고 싶어요!#l\r\n";
      말 += "#fs15##fc0xFF4641D9##L4#[PRAY]#k "+검정+"잠수코인 상점을 이용하고 싶어요!#l\r\n";
      말 += "#fs15##fc0xFF4641D9##L5#[PRAY]#k "+검정+"하얀 결정 - F 상점을 이용하고 싶어요!#l\r\n";
      말 += "#fs15##fc0xFF4641D9##L6#[PRAY]#k "+검정+"출석코인 상점을 이용하고 싶어요!#l\r\n";
      말 += "#fs15##fc0xFF4641D9##L7#[PRAY]#k "+검정+"매그코인 상점을 이용하고 싶어요!#l\r\n";
      말 += "#fs15##fc0xFF4641D9##L8#[PRAY]#k "+검정+"앱솔코인 상점을 이용하고 싶어요!#l\r\n";
      말 += "#fs15##fc0xFF4641D9##L9#[PRAY]#k "+검정+"아케인코인 상점을 이용하고 싶어요!#l\r\n";*/
      cm.sendSimple(말);
   } else if (status == 1) {
      if (selection == 1) {
         cm.dispose();
         cm.openNpc(9062554);
      } else if (selection == 2) {
         cm.dispose();
         cm.openNpc(1540110);         
      } else if (selection == 3) {
         cm.dispose();
         cm.openShop(34);
      } else if (selection == 4) {
         cm.dispose();
         cm.openShop(15);
      } else if (selection == 5) {
         cm.dispose();
         cm.openNpc(9062115);
      } else if (selection == 6) {
         cm.dispose();
         cm.openShop(35);
      } else if (selection == 7) {
         cm.dispose();
         cm.openShop(17);
      } else if (selection == 8) {
         cm.dispose();
         cm.openShop(11);
      } else if (selection == 9) {
         cm.dispose();
         cm.openShop(16);
}
   } else if (status == 2) {
      if (selection == 0) {
         말 = "#fs15#→ #fc0xFF4375DB#환생 횟수#k 랭킹입니다.\r\n확인하고 싶은 랭킹 순위를 확인해주세요.\r\n";
         말 += "#L0#"+T2+O2+P2+" "+Num2+"#k #fs15##e랭킹#l #L1#"+T10+O10+P10+" "+Num1+Num0+"#k 랭킹#l\r\n";
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
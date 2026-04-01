importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

파랑 = "#fc0xFF0054FF#";
연파 = "#fc0xFF6B66FF#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
검정 = "#fc0xFF191919#";
화이트 = "#fc0xFFFFFFFF#";

펫 = "#fUI/CashShop.img/CashItem_label/5#";
펫1 = "#fUI/CashShop.img/CashItem_label/8#";
길드 = "#fUI/Basic.img/theblackcoin/14#";
젬스톤 = "#fUI/Basic.img/theblackcoin/19#";
이름변경 = "#fUI/Basic.img/theblackcoin/20#";
자유전직 = "#fUI/Basic.img/theblackcoin/21#";
창고 = "#fUI/Basic.img/theblackcoin/13#";
어빌리티 = "#fUI/Basic.img/theblackcoin/22#";
포인트 = "#fUI/Basic.img/theblackcoin/7#";
포켓 = "#fUI/Basic.img/theblackcoin/23#";
랭킹 = "#fUI/Basic.img/theblackcoin/24#";
템버리기 = "#fUI/Basic.img/theblackcoin/25#";
편의 = "#fUI/Basic.img/theblack/2#";
추천인 = "#fUI/Basic.img/theblackcoin/41#";
상점 = "#fUI/Basic.img/theblack/1#";
검은마법사 = "#fUI/Basic.img/theblackcoin/42#";
위장색 = "#fUI/Basic.img/theblackcoin/43#";
정령 = "#fUI/UIWindow4.img/pointShop/100712/iconShop#";

mTown = ["SixPath", "Henesys", "Ellinia", "Perion", "KerningCity",
    "Rith", "Dungeon", "Nautilus", "Ereb", "Rien",
    "Orbis", "ElNath", "Ludibrium", "Folkvillige", "AquaRoad",
    "Leafre", "Murueng", "WhiteHerb", "Ariant", "Magatia",
    "Edelstein", "Eurel", "critias", "Haven", "Road of Vanishing", "ChewChew", "Lacheln", "Arcana", "Morass", "esfera", "aliance", "moonBridge", "TheLabyrinthOfSuffering", "Limen"];

cTown = [104020000, 100000000, 101000000, 102000000, 103000000,
    104000000, 105000000, 120000000, 130000000, 140000000,
    200000000, 211000000, 220000000, 224000000, 230000000,
    240000000, 250000000, 251000000, 260000000, 261000000,
    310000000, 101050000, 241000000, 310070000, 450001000, 450002000, 450003000, 450005000, 450006130, 450007040, 450009050, 450009100, 450011500, 450012300];


var 별1 = "#fUI/UIWindow.img/ToolTip/WorldMap/StarForce#";
var 별2 = "#fUI/UIWindow.img/ToolTip/WorldMap/ArcaneForce#";
var 별3 = "#fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce#";
var 티켓 = 4033235;
var 갯수 = 1;

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

        var msg = 연보 + "::#fs13#어서오세요 #h0#님! 어디로 이동하시겠어요?\r\n";
      //  msg += "\r\n#fc0xFF191919#───────────MAIN─────────#k\r\n";
        //choose += "\r\n#fc0xFFD5D5D5#───────────────────────────#k\r\n";
      //  msg += "　　　　　　　　　#L1##fc0xFFF29661#" + 템버리기 + "마을 이동#l\r\n\r\n";
     //   msg += "　　　　#L2##fc0xFFDF4D4D#" + 템버리기 + "일반 사냥터#l";
     //   msg += "#L3##fc0xFFDF4D4D#" + 템버리기 + "[PRAY] 자동 사냥터#l\r\n\r\n";
	//	msg += "　　　　　　　　　#L4##fc0xFFF29661#" + 템버리기 + "수련관 이동#l\r\n\r\n";
     //   //msg += "#L2##fc0xFFF29661#" + 템버리기+ "일반 사냥터#l\r\n\r\n"; 
      //  msg += "\r\n#fc0xFF191919#───────────EVENT─────────#k\r\n";
        //msg += "#L2# #b일반 사냥터#n #k\r\n";
        //msg += "#L3# #b자동 사냥터#n #k\r\n\r\n";
        msg += "　　#L6##fc0xFF6B66FF#" + 템버리기 + "파티퀘스트 장소#n #k";
     /*   msg += "　#L6974##fc0xFF6B66FF#" + 템버리기 + "해외보스#l\r\n\r\n";
        msg += "　　#L8##fc0xFF6B66FF#" + 템버리기 + "이벤트 점프맵#n #k";
        msg += "　#L10##fc0xFF6B66FF#" + 템버리기 + "낚시터#l\r\n\r\n";
		msg += "\r\n#fc0xFF191919#───────────개인맵─────────#k\r\n";
		msg += "　　#L20##fc0xFF6B66FF#" + 템버리기 + "미쯔 개인맵#n #k";
        msg += "　#L21##fc0xFF6B66FF#" + 템버리기 + "미쯔 잠수사냥#l\r\n\r\n";
		msg += "　　#L22##fc0xFF6B66FF#" + 템버리기 + "별 개인맵#n #k";
        msg += "　#L23##fc0xFF6B66FF#" + 템버리기 + "별 잠수사냥#l\r\n\r\n";*/
   //     if (GameConstants.isWildHunter(cm.getPlayer().getJob()))
   //         msg += "#L9# #b재규어 포획#n #k\r\n";
        //msg += "#L999# #b테스트#n #k\r\n";
        cm.sendSimpleS(msg, 4);
    } else if (status == 1) {
        ans_01 = selection;
        selStr = "";
        switch (ans_01) {
            case 1:
                cm.dispose();
                cm.openNpc(3000012);
                break;
            case 10:
                cm.dispose();
                cm.warp(993000750);
                break;
            case 999:
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUIOption(7, 2));
                cm.dispose();
                break;

            case 2:
                selStr += "\r\n";
                selStr += "#L1##fs11# #e#b일반#n#b 사냥터#l\r\n#L2# #e#b스타포스#n#b 사냥터#l\r\n#L3# #e#b아케인포스#n#b 사냥터#l\r\n#L4# #e#b그란디스#n#b 사냥터#l";
                selStr += "";
                break;
			case 20:
                    if (cm.haveItem(4001480, 1)) {
                        cm.warp(910310531); //기존 일반
                    } else {
                        cm.sendOk("#i4001480#입장티켓이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 21:
                    cm.dispose();
                    if (cm.haveItem(4001481, 1)) {
                        cm.warp(13008); //기존 일반
                    } else {
                        cm.sendOk("#i4001481#입장티켓이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 22:
                    if (cm.haveItem(4000063, 1)) {
                        cm.warp(910310532); //기존 일반
                    } else {
                        cm.sendOk("#i4000063#입장티켓이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 23:
                    if (cm.haveItem(4033617, 1)) {
                        cm.warp(13009, 0); //기존 일반
                    } else {
                        cm.sendOk("#i4033617#입장티켓이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;	

            case 3:
                //selStr += "사냥에 도움이 되는 많은곳들이 있답니다.\r\n어디로 이동하시겠어요?\r\n";
                //selStr += "#L1##fs11##fUI/UIWindow8.img/EldaGauge/tooltip/1# #e#d디에즈 게임랜드#n #k이동\r\n";
                selStr += "일반 자동 사냥터 \r\n";
                selStr += "#L2##fs11# #e#d신버전 일반 사냥터[브론즈]#n #k이동\r\n";
                selStr += "#L3# #e#d신버전 일반 사냥터[실버]#n #k이동#l\r\n";
                selStr += "#L4# #e#d신버전 일반 사냥터[골드]#n #k이동#l\r\n";
                selStr += "#L5# #e#d신버전 일반 사냥터[플레티넘]#n #k이동#l\r\n\r\n";
                selStr += "후원 자동 사냥터 \r\n";
                //selStr += "#L6# #e#d구버전 후원 사냥터#n #k이동\r\n";
                selStr += "#L7# #e#d신버전 후원 사냥터[브론즈]#n #k이동\r\n";
                selStr += "#L8# #e#d신버전 후원 사냥터[실버]#n #k이동\r\n";
                selStr += "#L9# #e#d신버전 후원 사냥터[골드]#n #k이동\r\n";
                selStr += "#L10# #e#d신버전 후원 사냥터[플레티넘]#n #k이동\r\n";
                //selStr += "#L4# #e#d프로즌 링크#n #k이용\r\n";
                //selStr += "#L8# #e#d스플래시 링크#n #k이용\r\n";
                //selStr += "#L5# #e#d스피릿 세이비어#n #k이용\r\n";
                //selStr += "#L6# #e#d베고픈 무토#n #k이용\r\n";
                //selStr += "#L7# #e#d드림 브레이커#n #k이용\r\n";
                //selStr += "#L8# #e#d분노한 자쿰#n #k이용\r\n";
                break;
				
            case 4:
                //selStr += "사냥에 도움이 되는 많은곳들이 있답니다.\r\n어디로 이동하시겠어요?\r\n";
                //selStr += "#L1##fs11##fUI/UIWindow8.img/EldaGauge/tooltip/1# #e#d디에즈 게임랜드#n #k이동\r\n";
                selStr += "일반사냥터 \r\n";
                selStr += "#L2##fs11# #e#d신버전 일반 사냥터[브론즈]#n #k이동\r\n";
                selStr += "#L3# #e#d신버전 일반 사냥터[실버]#n #k이동#l\r\n";
                selStr += "#L4# #e#d신버전 일반 사냥터[골드]#n #k이동#l\r\n";
                selStr += "#L5# #e#d신버전 일반 사냥터[플레티넘]#n #k이동#l\r\n\r\n";
                selStr += "후원 사냥터 \r\n";
                //selStr += "#L6# #e#d구버전 후원 사냥터#n #k이동\r\n";
                selStr += "#L7# #e#d신버전 후원 사냥터[브론즈]#n #k이동\r\n";
                selStr += "#L8# #e#d신버전 후원 사냥터[실버]#n #k이동\r\n";
                selStr += "#L9# #e#d신버전 후원 사냥터[골드]#n #k이동\r\n";
                selStr += "#L10# #e#d신버전 후원 사냥터[플레티넘]#n #k이동\r\n";
                //selStr += "#L4# #e#d프로즌 링크#n #k이용\r\n";
                //selStr += "#L8# #e#d스플래시 링크#n #k이용\r\n";
                //selStr += "#L5# #e#d스피릿 세이비어#n #k이용\r\n";
                //selStr += "#L6# #e#d베고픈 무토#n #k이용\r\n";
                //selStr += "#L7# #e#d드림 브레이커#n #k이용\r\n";
                //selStr += "#L8# #e#d분노한 자쿰#n #k이용\r\n";
                break;				

            case 6:
                selStr += "\r\n";
                selStr += "#L1##fs11# #e#d루디브리엄 파티퀘스트#n #k이동\r\n";
				selStr += "#L2# #e#d탈출 파티퀘스트#n #k이동\r\n";
              //  selStr += "#L2# #e#d독안개의숲 파티퀘스트#n #k이동\r\n";
                //selStr += "#L3# #e#d살롱 RISE#n #k이동\r\n";
                //selStr += "#L4# #e#d각성의 산 선인이 잠든 곳#n #k이동\r\n";
                //selStr += "#L5# #e#d네오 캐슬#n #k이동\r\n";
                break;

            case 7:
                selStr += "\r\n";
                selStr += "#L1##fs11# #e#d14주년 행성 비타#n #k이동\r\n";
                selStr += "#L2# #e#d15번가 셀러브리티#n #k이동\r\n";
                selStr += "#L3# #e#d16주년 뉴트로타임#n #k이동\r\n";
                selStr += "#L4# #e#d17주년 호텔 메이플#n #k이동\r\n";
                //selStr += "#L5# #e#d네오 캐슬#n #k이동\r\n";
                break;

            case 8:
                selStr += "\r\n";
                selStr += "#L1##fs11# #e#d끈기의 숲#n #k이동\r\n";
                selStr += "#L2# #e#d인내의 숲#n #k이동\r\n";
                selStr += "#L3# #e#d고지를 향해서#n #k이동\r\n";
                selStr += "#L4# #e#d펫 산책로[헤네시스]#n #k이동\r\n";
                selStr += "#L5# #e#d펫 산책로[루디브리엄]#n #k이동\r\n";
                break;

      /*      case 4:

                selStr += "사냥에 도움이 되는 많은곳들이 있답니다.\r\n어디로 이동하시겠어요?\r\n";
                selStr += "#L1##fs11# #e#d[티어] 노예 사냥터#n #k이동\r\n";
                selStr += "#L2# #e#d[티어] 노예 사냥터#n #k이동\r\n";
                selStr += "#L3# #e#d[티어] 평민 사냥터#n #k이동\r\n";
                selStr += "#L4# #e#d[티어] 평민 사냥터#n #k이동\r\n";
                selStr += "#L5# #e#d[티어] 남작 사냥터#n #k이동\r\n";
                selStr += "#L6# #e#d[티어] 남작 사냥터#n #k이동\r\n";
                selStr += "#L7# #e#d[티어] 백작 사냥터#n #k이동\r\n";
                selStr += "#L8# #e#d[티어] 백작 사냥터#n #k이동\r\n";
                selStr += "#L9# #e#d[티어] 공작 사냥터#n #k이동\r\n";
                selStr += "#L10# #e#d[티어] 공작 사냥터#n #k이동\r\n";
                break; */
            case 5:
                cm.dispose();
                cm.openNpc(9001153);
                break;
            case 10:
                cm.dispose();
                cm.warp(993050400, 0);
                return;

            case 11:
                cm.dispose();
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.closeUI(62));
                cm.warp(925020000, 0);
                return;

            case 12:
                cm.dispose();
                cm.openNpc("mParkShuttle");
                return;

            case 13:
                cm.dispose();
                cm.openNpc(9020011);
                return;

            case 9:
                cm.dispose();
                cm.warp(931000500);
                return;
				
			case 6974:
				cm.dispose();
				cm.openNpc(2135007);
				return;

        }
        if (ans_01 == 2 || ans_01 == 3 || ans_01 == 4 || ans_01 == 6 || ans_01 == 7 || ans_01 == 8)
            cm.sendSimpleS(selStr, 4);
    } else if (status == 2) {
        ans_02 = selection;
        if (ans_01 == 3) {
            switch (ans_02) {
                case 2:
                    if (cm.haveItem(1143195, 1)) {
                        cm.warp(921170004, 0); //기존 일반
                    } else {
                        cm.sendOk("#i1143195#[PRAY]브론즈 훈장이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 3:
                    if (cm.haveItem(1143196, 1)) {
                        cm.warp(13000, 0); //기존 일반
                    } else {
                        cm.sendOk("#i1143196#[PRAY]실버 훈장이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 4:
                    if (cm.haveItem(1143197, 1)) {
                        cm.warp(13001, 0); //기존 일반
                    } else {
                        cm.sendOk("#i1143197#[PRAY]골드 훈장이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 5:
                    if (cm.haveItem(1143198, 1)) {
                        cm.warp(13002, 0); //기존 일반
                    } else {
                        cm.sendOk("#i1143198#[PRAY]플레티넘 훈장이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 6:
                    cm.dispose();
                    if (cm.haveItem(4319999, 1)) {
                        cm.warp(921170011, 0); //기존 후원사냥터
                    }
                    return;
                    break;
                case 7:
                    if (cm.haveItem(4033235, 1)) {
                        cm.warp(13004); //기존 일반
                    } else {
                        cm.sendOk("#i4033235#[PRAY]후원 브론즈 입장티켓 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 8:
                    cm.dispose();
                    if (cm.haveItem(4033914, 1)) {
                        cm.warp(13005); //기존 일반
                    } else {
                        cm.sendOk("#i4033914#[PRAY]후원 실버 입장 티켓이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 9:
                    if (cm.haveItem(4033919, 1)) {
                        cm.warp(13006); //기존 일반
                    } else {
                        cm.sendOk("#i4033919#[PRAY]후원 골드 입장 티켓이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 10:
                    if (cm.haveItem(4032605, 1)) {
                        cm.warp(13007, 0); //기존 일반
                    } else {
                        cm.sendOk("#i4032605#[PRAY]후원 플레티넘 입장 티켓이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
            }
        } else if (ans_01 == 4) {
            switch (ans_02) {
                case 1:
                    cm.dispose();
                    cm.warp(101, 0);
                    break;
                case 2:
                    if (cm.haveItem(1143195, 1)) {
                        cm.warp(993217242, 0); //기존 일반
                    } else {
                        cm.sendOk("#i1143195#[PRAY]브론즈 훈장이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 3:
                    if (cm.haveItem(1143196, 1)) {
                        cm.warp(993217243, 0); //기존 일반
                    } else {
                        cm.sendOk("#i1143196#[PRAY]실버 훈장이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 4:
                    if (cm.haveItem(1143197, 1)) {
                        cm.warp(993217244, 0); //기존 일반
                    } else {
                        cm.sendOk("#i1143197#[PRAY]골드 훈장이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 5:
                    if (cm.haveItem(1143198, 1)) {
                        cm.warp(993217245, 0); //기존 일반
                    } else {
                        cm.sendOk("#i1143198#[PRAY]플레티넘 훈장이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 6:
                    cm.dispose();
                    if (cm.haveItem(4319999, 1)) {
                        cm.warp(993217247, 0); //기존 후원사냥터
                    }
                    return;
                    break;
                case 7:
                    if (cm.haveItem(4033235, 1)) {
                        cm.warp(993217247); //기존 일반
                    } else {
                        cm.sendOk("#i4033235#[PRAY]후원 브론즈 입장티켓 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 8:
                    cm.dispose();
                    if (cm.haveItem(4033914, 1)) {
                        cm.warp(993217248); //기존 일반
                    } else {
                        cm.sendOk("#i4033914#[PRAY]후원 실버 입장 티켓이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 9:
                    if (cm.haveItem(4033919, 1)) {
                        cm.warp(993217249); //기존 일반
                    } else {
                        cm.sendOk("#i4033919#[PRAY]후원 골드 입장 티켓이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
                case 10:
                    if (cm.haveItem(4032605, 1)) {
                        cm.warp(993217250, 0); //기존 일반
                    } else {
                        cm.sendOk("#i4032605#[PRAY]후원 플레티넘 입장 티켓이 없습니다.");
                    }
                    cm.dispose();
                    return;
                    break;
            }


        } else if (ans_01 == 8) {
            switch (ans_02) {
                case 1:
                    cm.dispose();
                    cm.warp(910530000, 0);
                    break;
                case 2:
                    cm.dispose();
                    cm.warp(910130000, 0);
                    return;
                    break;
                case 3:
                    cm.dispose();
                    cm.warp(109040000, 0);
                    return;
                    break;
                case 4:
                    cm.dispose();
                    cm.warp(100000202, 0);
                    return;
                    break;
                case 5:
                    cm.dispose();
                    cm.warp(220000006, 0);
                    return;
                    break;
                case 50:
                    cm.dispose();
                    cm.warp(701000000, 0);
                    return;
                    break;
                case 6:
                    cm.dispose();
                    cm.warp(740000000, 0);
                    return;
                    break;
                case 7:
                    cm.dispose();
                    cm.warp(500000000, 0);
                    return;
                    break;
                case 8:
                    cm.dispose();
                    cm.warp(910130000, 0);
                    return;
                    break;
            }
        } else if (ans_01 == 6) {
            switch (ans_02) {
                case 1:
                    cm.dispose();
                    cm.warp(221023300, 0);
                    break;
                case 2:
                    cm.dispose();
                    cm.warp(921160000, 0);
                    return;
                    break;
                case 3:
                    cm.dispose();
                    cm.warp(993176500, 0);
                    return;
                    break;
                case 4:
                    cm.dispose();
                    cm.warp(993184000, 0);
                    return;
                    break;
                case 5:
                    cm.dispose();
                    cm.warp(993189100, 0);
                    return;
                    break;
                case 50:
                    cm.dispose();
                    cm.warp(701000000, 0);
                    return;
                    break;
                case 6:
                    cm.dispose();
                    cm.warp(740000000, 0);
                    return;
                    break;
                case 7:
                    cm.dispose();
                    cm.warp(500000000, 0);
                    return;
                    break;
                case 8:
                    cm.dispose();
                    cm.warp(910130000, 0);
                    return;
                    break;
            }
        } else if (ans_01 == 7) {
            switch (ans_02) {
                case 1:
                    cm.dispose();
                    cm.warp(993016000, 0);
                    break;
                case 2:
                    cm.dispose();
                    cm.warp(993050000, 0);
                    return;
                    break;
                case 3:
                    cm.dispose();
                    cm.warp(993110000, 0);
                    return;
                    break;
                case 4:
                    cm.dispose();
                    cm.warp(993177000, 0);
                    return;
                    break;
                case 5:
                    cm.dispose();
                    cm.warp(993189100, 0);
                    return;
                    break;
                case 50:
                    cm.dispose();
                    cm.warp(701000000, 0);
                    return;
                    break;
                case 6:
                    cm.dispose();
                    cm.warp(740000000, 0);
                    return;
                    break;
                case 7:
                    cm.dispose();
                    cm.warp(500000000, 0);
                    return;
                    break;
                case 8:
                    cm.dispose();
                    cm.warp(910130000, 0);
                    return;
                    break;
            }
        } else {
            selStr = "";
            switch (ans_02) {
                case 1:
                    selStr += "#fs11#\r\n사냥터에 등장하는 #fc0xFF1DDB16##e몬스터의 평균 레벨#n#k을 확인하고 이동하세요.#fs11#\r\n";
                    selStr += "#d#L931000500#Lv.0   │ 특별 사냥터      │ 재규어 서식지#l\r\n";
                    selStr += "#d#L101020000#Lv.7   │ 솟아오른나무      │ 바람과 가까운 곳#l\r\n";
                    selStr += "#r#L100020000#Lv.10  │ 버섯노래숲        │ 포자언덕#l\r\n";
                    selStr += "#r#L100040000#Lv.15  │ 골렘사원          │ 골렘의 사원 입구#l\r\n";
                    selStr += "#d#L101020401#Lv.20  │ 동쪽숲            │ 바람이 불어오는 곳#l\r\n";
                    selStr += "#r#L120040100#Lv.35  │ 골드비치          │ 해변가 풀숲1#l\r\n";
                    selStr += "#r#L120041800#Lv.42  │ 골드비치          │ 거친 파도#l\r\n";
                    selStr += "#d#L103020420#Lv.45  │ 커닝시티지하철    │ 2호선 3구간#l\r\n";
                    selStr += "#r#L102030000#Lv.55  │ 불타버린땅        │ 와일드보어의 땅#l\r\n";
                    selStr += "#r#L102040301#Lv.62  │ 유적발굴지        │ 제1군영#l\r\n";
                    selStr += "#r#L105010000#Lv.66  │ 습지              │ 조용한 습지#l\r\n";
                    selStr += "#r#L260020600#Lv.90  │ 선셋로드          │ 사헬지대2#l\r\n";
                    selStr += "#r#L261020400#Lv.95  │ 제뉴미스트 연구소 │ 연구소 C-2 구역#l\r\n";
                    selStr += "#r#L220020600#Lv.114 │ 루디브리엄성      │ 장난감공장<기계실>#l\r\n";
                    selStr += "#d#L401053001#Lv.115 │ 폭군의 성채       │ 폭군의 성채 3층 복도#l\r\n";
                    selStr += "#d#L211040600#Lv.121 │ 폐광              │ 날카로운절벽4#l\r\n";
                    selStr += "#d#L250020000#Lv.126 │ 무릉사원          │ 초급 수련장#l\r\n";
                    selStr += "#r#L251010402#Lv.130 │ 무릉도원          │ 빨간코 해적단 소굴2#l\r\n";
                    selStr += "#d#L240040000#Lv.136 │ 미나르숲          │ 용의 협곡#l\r\n";
                    selStr += "#d#L271030400#Lv.176 │ 기사단 요새       │ 기사단 제 4구역#l\r\n";
                    selStr += "#r#L221030800#Lv.179 │ UFO 내부          │ 조종간1#l\r\n";
                    selStr += "#d#L241000213#Lv.186 │ 킹덤로드          │ 시작되는 비극의 숲3#l\r\n";
                    selStr += "#r#L273040300#Lv.196 │ 황혼의 페리온     │ 버려진 발굴지역 4#l\r\n";
                    selStr += "#d#L241000216#Lv.200 │ 킹덤로드          │ 타락한 마력의 숲1#l\r\n";
                    selStr += "#d#L241000206#Lv.205 │ 킹덤로드          │ 타락한 마력의 숲2#l\r\n";
                    selStr += "#r#L310070140#Lv.205 │ 기계무덤          │ 기계무덤 언덕4#l\r\n";
                    selStr += "#d#L241000226#Lv.210 │ 킹덤로드          │ 타락한 마력의 숲3#l\r\n";
                    selStr += "#d#L310070210#Lv.218 │ 스카이라인        │ 스카이라인1#l\r\n";
                    selStr += "#d#L310070300#Lv.218 │ 블랙헤븐 갑판     │ 블랙헤븐#l\r\n";
                    selStr += "#r#L105300301#Lv.226 │ 타락한 세계수     │ 상단 줄기 갈림길#l\r\n ";
                    break;

                case 2:
                    selStr += "#fs11#\r\n사냥터의 #e#fc0xFFFF9436#스타포스 요구치#k#n와 #e#b몬스터의 평균 레벨#k#n 확인하고\r\n이동하시기 바랍니다.\r\n#fs11#";
                    selStr += "   \r\n#fs11##e#r[리프레]#n\r\n#d";
                    selStr += "#L240010600##fc0xFFFF9436#Lv.107#d | " + 별1 + " 5   | 하늘 둥지2#l\r\n";
                    selStr += "#L240010520##fc0xFFFF9436#Lv.107#d | " + 별1 + " 5   | 하늘 둥지3#l\r\n";
                    selStr += "#L240010510##fc0xFFFF9436#Lv.107#d | " + 별1 + " 5   | 산양의 골짜기2#l\r\n";
                    selStr += "#L240020300##fc0xFFFF9436#Lv.109#d | " + 별1 + " 15  | 물과 어둠의 전장#l\r\n";
                    selStr += "#L240020210##fc0xFFFF9436#Lv.110#d | " + 별1 + " 15  | 어둠과 불의 전장#l\r\n";
                    selStr += "#L240020200##fc0xFFFF9436#Lv.110#d | " + 별1 + " 15  | 검은 켄타우로스의 영역#l\r\n\r\n";
                    selStr += "#fs12##e#r[루디브리엄]#d#n\r\n";
                    selStr += "#L220060000##fc0xFFFF9436#Lv.116#d | " + 별1 + " 25  | 뒤틀린 시간의 길<1>#l\r\n";
                    selStr += "#L220070000##fc0xFFFF9436#Lv.116#d | " + 별1 + " 25  | 잊혀진 시간의 길<1>#l\r\n";
                    selStr += "#L220060100##fc0xFFFF9436#Lv.117#d | " + 별1 + " 25  | 뒤틀린 시간의 길<2>#l\r\n";
                    selStr += "#L220070100##fc0xFFFF9436#Lv.117#d | " + 별1 + " 25  | 잊혀진 시간의 길<2>#l\r\n";
                    selStr += "#L220060200##fc0xFFFF9436#Lv.118#d | " + 별1 + " 26  | 뒤틀린 시간의 길<3>#l\r\n";
                    selStr += "#L220070200##fc0xFFFF9436#Lv.118#d | " + 별1 + " 26  | 잊혀진 시간의 길<3>#l\r\n";
                    selStr += "#L220060300##fc0xFFFF9436#Lv.119#d | " + 별1 + " 27  | 뒤틀린 시간의 길<4>#l\r\n";
                    selStr += "#L220070300##fc0xFFFF9436#Lv.119#d | " + 별1 + " 27  | 잊혀진 시간의 길<4>#l\r\n";
                    selStr += "#L220060400##fc0xFFFF9436#Lv.120#d | " + 별1 + " 28  | 뒤틀린 회랑#l\r\n";
                    selStr += "#L220070400##fc0xFFFF9436#Lv.120#d | " + 별1 + " 28  | 잊혀진 회랑#l\r\n\r\n";
                    selStr += "#fs12##e#r[사자왕의 성]#d#n\r\n";
                    selStr += "#L211080400##fc0xFFFF9436#Lv.130#d | " + 별1 + " 50  | 숨겨진 정원1#l\r\n";
                    selStr += "#L211080500##fc0xFFFF9436#Lv.132#d | " + 별1 + " 50  | 숨겨진 정원2#l\r\n";
                    selStr += "#L211080600##fc0xFFFF9436#Lv.134#d | " + 별1 + " 50  | 숨겨진 정원3#l\r\n\r\n";
                    selStr += "#fs12##e#r[엘나스 폐광]#d#n\r\n";
                    selStr += "#L211041100##fc0xFFFF9436#Lv.132#d | " + 별1 + " 50  | 폐광1#l\r\n";
                    selStr += "#L211041200##fc0xFFFF9436#Lv.132#d | " + 별1 + " 50  | 폐광2#l\r\n";
                    selStr += "#L211041300##fc0xFFFF9436#Lv.132#d | " + 별1 + " 50  | 폐광3#l\r\n";
                    selStr += "#L211041400##fc0xFFFF9436#Lv.132#d | " + 별1 + " 50  | 폐광4#l\r\n";
                    selStr += "#L211042000##fc0xFFFF9436#Lv.132#d | " + 별1 + " 55  | 시련의 동굴1#l\r\n";
                    selStr += "#L211042100##fc0xFFFF9436#Lv.135#d | " + 별1 + " 55  | 시련의 동굴2#l\r\n";
                    selStr += "#L211042200##fc0xFFFF9436#Lv.136#d | " + 별1 + " 55  | 시련의 동굴3#l\r\n\r\n";
                    selStr += "#fs12##e#r[리프레 협곡]#d#n\r\n";
                    selStr += "#L240040300##fc0xFFFF9436#Lv.141#d | " + 별1 + " 65  | 협곡의 서쪽길#l\r\n";
                    selStr += "#L240040320##fc0xFFFF9436#Lv.141#d | " + 별1 + " 65  | 검은 와이번의 둥지#l\r\n";
                    selStr += "#L240040510##fc0xFFFF9436#Lv.150#d | " + 별1 + " 65  | 죽은 용의 둥지#l\r\n";
                    selStr += "#L240040511##fc0xFFFF9436#Lv.150#d | " + 별1 + " 70  | 남겨진 용의 둥지1#l\r\n";
                    selStr += "#L240040512##fc0xFFFF9436#Lv.150#d | " + 별1 + " 70  | 남겨진 용의 둥지2#l\r\n\r\n";
                    selStr += "#fs12##e#r[커닝 타워]#d#n\r\n";
                    selStr += "#L103041119##fc0xFFFF9436#Lv.155#d | " + 별1 + " 80  | 2층 카페<4>#l\r\n";
                    selStr += "#L103041129##fc0xFFFF9436#Lv.157#d | " + 별1 + " 80  | 3층 팬시샵<4>#l\r\n";
                    selStr += "#L103041139##fc0xFFFF9436#Lv.159#d | " + 별1 + " 80  | 4층 음반 매장<4>#l\r\n";
                    selStr += "#L103041149##fc0xFFFF9436#Lv.161#d | " + 별1 + " 80  | 5층 화장품 매장<4>#l\r\n";
                    selStr += "#L103041159##fc0xFFFF9436#Lv.162#d | " + 별1 + " 80  | 5층 헤어샵<4>#l\r\n\r\n";
                    selStr += "#fs12##e#r[시간의 신전]#d#n\r\n";
                    selStr += "#L270030600##fc0xFFFF9436#Lv.160#d | " + 별1 + " 90  | 또 다른 망각의 길1#l\r\n";
                    selStr += "#L270030610##fc0xFFFF9436#Lv.162#d | " + 별1 + " 90  | 또 다른 망각의 길2#l\r\n";
                    selStr += "#L270030620##fc0xFFFF9436#Lv.164#d | " + 별1 + " 90  | 또 다른 망각의 길3#l\r\n";
                    selStr += "#L270030630##fc0xFFFF9436#Lv.165#d | " + 별1 + " 90  | 또 다른 망각의 길4#l\r\n\r\n";
                    selStr += "#fs12##e#r[기사단의 요새]#d#n\r\n";
                    selStr += "#L271030101##fc0xFFFF9436#Lv.169#d | " + 별1 + " 120 | 제 1연무장#l\r\n";
                    selStr += "#L271030102##fc0xFFFF9436#Lv.169#d | " + 별1 + " 120 | 제 2연무장#l\r\n";
                    selStr += "#L271030310##fc0xFFFF9436#Lv.173#d | " + 별1 + " 120 | 무기고1#l\r\n";
                    selStr += "#L271030320##fc0xFFFF9436#Lv.175#d | " + 별1 + " 120 | 무기고2#l\r\n\r\n";
                    selStr += "#fs12##e#r[지구방위본부]#d#n\r\n";
                    selStr += "#L221030640##fc0xFFFF9436#Lv.178#d | " + 별1 + " 140 | 복도 H01#l\r\n";
                    selStr += "#L221030650##fc0xFFFF9436#Lv.179#d | " + 별1 + " 140 | 복도 H02#l\r\n";
                    selStr += "#L221030660##fc0xFFFF9436#Lv.180#d | " + 별1 + " 140 | 복도 H03#l\r\n ";
                    break;

                case 3:
                    selStr += "#fs11#\r\n사냥터의 #e#fc0xFF6799FF#아케인포스 요구치#k#n와 #e#b몬스터의 평균 레벨#k#n 확인하고\r\n이동하시기 바랍니다.\r\n#fs11#";
                    selStr += "\r\n#e#r[소멸의 여로]#d#n\r\n";
                    selStr += "#L450001010##fc0xFF6799FF#Lv.200#d | " + 별2 + " 30  | 풍화된 기쁨의 땅#l\r\n";
                    selStr += "#L450001100##fc0xFF6799FF#Lv.204#d | " + 별2 + " 40  | 소멸의 화염지대#l\r\n";
                    selStr += "#L450001200##fc0xFF6799FF#Lv.207#d | " + 별2 + " 60  | 안식의 동굴#l\r\n";
                    selStr += "#L450001260##fc0xFF6799FF#Lv.209#d | " + 별2 + " 80  | 숨겨진 호숫가#l\r\n";
                    selStr += "#L450001261##fc0xFF6799FF#Lv.209#d | " + 별2 + " 80  | 숨겨진 화염지대#l\r\n";
                    selStr += "#L450001262##fc0xFF6799FF#Lv.209#d | " + 별2 + " 80  | 숨겨진 동굴#l\r\n";
                    selStr += "\r\n\r\n#e#r[츄츄 아일랜드]#d#n\r\n"
                    selStr += "#L450002001##fc0xFF6799FF#Lv.210#d | " + 별2 + " 100 | 오색동산#l\r\n";
                    selStr += "#L450002006##fc0xFF6799FF#Lv.212#d | " + 별2 + " 100 | 츄릅 포레스트#l\r\n";
                    selStr += "#L450002012##fc0xFF6799FF#Lv.214#d | " + 별2 + " 130 | 격류지대#l\r\n";
                    selStr += "#L450002016##fc0xFF6799FF#Lv.217#d | " + 별2 + " 160 | 하늘 고래산#l\r\n";
                    selStr += "\r\n\r\n#e#r[리버스 시티]#d#n\r\n";
                    selStr += "#L450014020##fc0xFF6799FF#Lv.210#d | " + 별2 + " 100 | 지하 선로#l\r\n";
                    selStr += "#L450014100##fc0xFF6799FF#Lv.212#d | " + 별2 + " 100 | T-boy의 연구열차#l\r\n";
                    selStr += "#L450014140##fc0xFF6799FF#Lv.214#d | " + 별2 + " 100 | 지하 열차#l\r\n";
                    selStr += "#L450014200##fc0xFF6799FF#Lv.214#d | " + 별2 + " 160 | M - 타워#l\r\n";
                    selStr += "#L450014300##fc0xFF6799FF#Lv.217#d | " + 별2 + " 100 | 숨겨진 연구열차#l\r\n";
                    selStr += "#L450014310##fc0xFF6799FF#Lv.217#d | " + 별2 + " 100 | 숨겨진 지하열차#l\r\n";
                    selStr += "\r\n\r\n#e#r[꿈의 도시 레헬른]#d#n\r\n";
                    selStr += "#L450003200##fc0xFF6799FF#Lv.220#d | " + 별2 + " 190 | 레헬른 뒷골목 무법자의거리#l\r\n";
                    selStr += "#L450003300##fc0xFF6799FF#Lv.221#d | " + 별2 + " 210 | 레헬른 야시장#l\r\n";
                    selStr += "#L450003400##fc0xFF6799FF#Lv.223#d | " + 별2 + " 210 | 레헬른 무도회장#l\r\n";
                    selStr += "#L450003440##fc0xFF6799FF#Lv.225#d | " + 별2 + " 210 | 레헬른 춤추는 구두점령지#l\r\n";
                    selStr += "#L450003500##fc0xFF6799FF#Lv.226#d | " + 별2 + " 240 | 레헬른 시계탑1#l\r\n";
                    selStr += "#L450003510##fc0xFF6799FF#Lv.226#d | " + 별2 + " 240 | 레헬른 시계탑2#l\r\n";
                    selStr += "#L450003520##fc0xFF6799FF#Lv.226#d | " + 별2 + " 240 | 레헬른 시계탑3#l\r\n";
                    selStr += "#L450003530##fc0xFF6799FF#Lv.226#d | " + 별2 + " 240 | 레헬른 시계탑4#l\r\n";
                    selStr += "#L450003540##fc0xFF6799FF#Lv.226#d | " + 별2 + " 240 | 레헬른 시계탑5#l\r\n";
                    selStr += "\r\n\r\n#e#r[신비의 숲 아르카나]#d#n\r\n";
                    selStr += "#L450005100##fc0xFF6799FF#Lv.230#d | " + 별2 + " 280 | 풀잎 피리의 숲#l\r\n";
                    selStr += "#L450005200##fc0xFF6799FF#Lv.233#d | " + 별2 + " 320 | 깊은 숲#l\r\n";
                    selStr += "#L450005230##fc0xFF6799FF#Lv.235#d | " + 별2 + " 320 | 맹독의 숲#l\r\n";
                    selStr += "#L450005500##fc0xFF6799FF#Lv.237#d | " + 별2 + " 360 | 다섯 갈래 동굴#l\r\n ";
                    selStr += "\r\n\r\n#e#r[모라스]#d#n\r\n";
                    selStr += "#L450006010##fc0xFF6799FF#Lv.236#d | " + 별2 + " 400 | 산호숲으로 가는길#l\r\n";
                    selStr += "#L450006140##fc0xFF6799FF#Lv.238#d | " + 별2 + " 440 | 형님들 구역#l\r\n";
                    selStr += "#L450006210##fc0xFF6799FF#Lv.239#d | " + 별2 + " 480 | 그림자가 춤추는 곳#l\r\n";
                    selStr += "#L450006300##fc0xFF6799FF#Lv.241#d | " + 별2 + " 480 | 폐쇄구역#l\r\n ";
                    selStr += "#L450006410##fc0xFF6799FF#Lv.245#d | " + 별2 + " 480 | 그날의 트뤼에페#l\r\n ";
                    selStr += "\r\n\r\n#e#r[에스페라]#d#n\r\n";
                    selStr += "#L450007010##fc0xFF6799FF#Lv.240#d | " + 별2 + " 560 | 생명이 시작되는 곳 2#l\r\n";
                    selStr += "#L450007050##fc0xFF6799FF#Lv.242#d | " + 별2 + " 560 | 생명이 시작되는 곳 5#l\r\n";
                    selStr += "#L450007100##fc0xFF6799FF#Lv.244#d | " + 별2 + " 600 | 거울빛에 물든 바다#l\r\n";
                    selStr += "#L450007210##fc0xFF6799FF#Lv.248#d | " + 별2 + " 640 | 거울에 비친 빛의 신전#l\r\n";
                    selStr += "\r\n\r\n#e#r[문브릿지]#d#n\r\n"
                    selStr += "#L450009110##fc0xFF6799FF#Lv.250#d | " + 별2 + " 670 | 사상의 경계#l\r\n";
                    selStr += "#L450009210##fc0xFF6799FF#Lv.252#d | " + 별2 + " 700 | 미지의 안개#l\r\n";
                    selStr += "#L450009310##fc0xFF6799FF#Lv.254#d | " + 별2 + " 730 | 공허의 파도#l\r\n";
                    selStr += "\r\n\r\n#e#r[테네브리스]#d#n\r\n"
                    selStr += "#L450011420##fc0xFF6799FF#Lv.255#d | " + 별2 + " 760 | 고통의 미궁 내부1#l\r\n";
                    selStr += "#L450011510##fc0xFF6799FF#Lv.257#d | " + 별2 + " 790 | 고통의 미궁 중심부#l\r\n";
                    selStr += "#L450011600##fc0xFF6799FF#Lv.259#d | " + 별2 + " 820 | 고통의 미궁 최심부#l\r\n";
                    selStr += "\r\n\r\n#e#r[리멘]#d#n\r\n";
                    selStr += "#L450012030##fc0xFF6799FF#Lv.260#d | " + 별2 + " 850 | 세계의 눈물 하단#l\r\n";
                    selStr += "#L450012100##fc0xFF6799FF#Lv.261#d | " + 별2 + " 850 | 세계의 눈물 중단#l\r\n";
                    //selStr += "#L450012310##fc0xFF6799FF#Lv.262#d | " + 별2 + " 880 | 세계가 끝나는 곳 1-2#l\r\n";
                    selStr += "#L450012430##fc0xFF6799FF#Lv.262#d | " + 별2 + " 880 | 세계가 끝나는 곳 2-4#l\r\n";
                    selStr += "#L450012470##fc0xFF6799FF#Lv.264#d | " + 별2 + " 880 | 세계가 끝나는 곳 2-7#l\r\n";
                    //selStr += "#L993072000##fc0xFF6799FF#Lv.263#d | " + 별2 + " 880 | 레지스탕스 함선 갑판#l\r\n";
                    //selStr += "#L993072500##fc0xFF6799FF#Lv.264#d | " + 별2 + " 880 | 레지스탕스 함선 갑판6#l\r\n";
                    break;

                case 4:
                    selStr += "#fs11#\r\n사냥터의 #e#fc0xFF6799FF#어센틱포스 요구치#k#n와 #e#b몬스터의 평균 레벨#k#n 확인하고\r\n이동하시기 바랍니다.\r\n#fs11#";
                    selStr += "\r\n#e#r[세르니움]#d#n\r\n";
                    selStr += "#L410000530##fc0xFF6799FF#Lv.260#d | " + 별3 + " 50  | 해변 암석 지대2#l\r\n";
                    selStr += "#L410000650##fc0xFF6799FF#Lv.260#d | " + 별3 + " 50  | 세르니움 동쪽 성벽2#l\r\n";
                    selStr += "#L410000710##fc0xFF6799FF#Lv.260#d | " + 별3 + " 50  | 왕립 도서관2#l\r\n";
                    selStr += "\r\n\r\n#e#r[불타는 세르니움]#d#n\r\n";
                    selStr += "#L410000950##fc0xFF6799FF#Lv.265#d | " + 별3 + " 70 | 격전의 서쪽 성벽4#l\r\n";
                    selStr += "#L410001000##fc0xFF6799FF#Lv.265#d | " + 별3 + " 100 | 격전의 동쪽 성벽3#l\r\n";
                    selStr += "#L410000890##fc0xFF6799FF#Lv.265#d | " + 별3 + " 100 | 불타는 왕립 도서관6#l\r\n";
                    selStr += "\r\n\r\n#e#r[아르크스]#d#n\r\n";
                    selStr += "#L410003070##fc0xFF6799FF#Lv.270#d | " + 별3 + " 130 | 무법자들이 지배하는 황야4#l\r\n";
                    selStr += "#L410003140##fc0xFF6799FF#Lv.270#d | " + 별3 + " 160 | 낭만이 저무는 자동차 극장6#l\r\n";
                    selStr += "#L410003160##fc0xFF6799FF#Lv.270#d | " + 별3 + " 200 | 종착지 없는 횡단열차2#l\r\n";
                    selStr += "\r\n\r\n#e#r[오디움]#d#n\r\n"
                    selStr += "#L410007005##fc0xFF6799FF#Lv.280#d | " + 별3 + " 230 | 성문으로 가는 길 4#l\r\n";
                    selStr += "#L410007009##fc0xFF6799FF#Lv.280#d | " + 별3 + " 260 | 점령당한 골목 4#l\r\n";
                    selStr += "#L410007014##fc0xFF6799FF#Lv.280#d | " + 별3 + " 280 | 볕 드는 실험실3l\r\n";
                    selStr += "#L410007017##fc0xFF6799FF#Lv.280#d | " + 별3 + " 300 | 잠긴 문 뒤 실험실 3#l\r\n";
                    break;
            }
            cm.sendSimpleS(selStr, 4);
        }
    } else if (status == 3) {
        ans_03 = selection;
        switch (ans_02) {
            case 1:
            case 2:
            case 3:
                cm.warp(ans_03, "sp");
                cm.dispose();
                break;
            case 4:
                cm.warp(ans_03, "sp");
                cm.dispose();
                break;
        }
    }
}


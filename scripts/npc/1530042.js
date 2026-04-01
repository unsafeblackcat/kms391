var enter = "\r\n";
var seld = -1;
var Gender;
var codyList;

회색 = "#fc0xFF5D5D5D#";
연파 = "#fc0xFF6B66FF#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
검정 = "#fc0xFF191919#";
화이트 = "#fc0xFFFFFFFF#";

틀 = "#fUI/Basic.img/actMark/22#";
헤어 = "#fUI/Basic.img/actMark/16#";
성형 = "#fUI/Basic.img/actMark/17#";
색상 = "#fUI/Basic.img/actMark/18#";
캐시 = "#fUI/Basic.img/actMark/19#";
스페셜 = "#fUI/Basic.img/actMark/20#";
기타 = "#fUI/Basic.img/actMark/21#";

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	var msg = "#fs12#"
	if (status == 0) {
		
        var msg = "                    " + 틀 + "#l\r\n";
                msg += "#fc0xFFD5D5D5#──────────────────────────#l\r\n";
		msg += "#L1##fc0xFFFF3636#" + 헤어 + "#l";
		msg += "#L2##fc0xFFFF3636#" + 성형 + "#l";
                msg += "#L3##fc0xFFFF3636#" + 색상 + "#l\r\n";
		msg += "#L4##fc0xFFFF3636#" + 캐시 + "#l";
		msg += "#L5##fc0xFFFF3636#" + 스페셜 + "#l";
		msg += "#L6##fc0xFFFF3636#" + 기타 + "#l\r\n";
		

		cm.sendSimple(msg);
	} else if (status == 1) {
		if (sel == 1) {
			cm.dispose();
			cm.openNpcCustom(cm.getClient(), 1540011, "searchhair");

		} else if (sel == 2) {
			cm.dispose();
			cm.openNpcCustom(cm.getClient(), 1540011, "searchface");

		} else if (sel == 3) { // <검색> 페이지 선택
			msg += "\r\n#fs11#";
			msg += "#L20# #d일반 염색#k하기#l\r\n";
			msg += "#L21# #d렌즈색#k 변경하기#l\r\n";
			msg += "#L22# #d피부색#k 변경하기#l\r\n";
			msg += "#L23# 캐릭터 #d색상#k 변경하기#l\r\n";
			msg += "#L24# 캐릭터 색상 #d리셋#k하기#l\r\n";
			msg += "#L25# #b믹스 렌즈#k/#r염색#k 구매하기#l\r\n";
			cm.sendSimple(msg);

		} else if (sel == 4) { // <검색> 페이지 선택
			cm.dispose();
			cm.openNpcCustom(cm.getClient(), 1540011, "Itemsearch");

		} else if (sel == 5) {
			cm.sendOk("#fs11##b준비중입니다.");
		} else if (sel == 6) {
			cm.sendOk("#fs11##b준비중입니다.");
		}
	} else if (status == 2) {
		seld = sel;
		isMale = !cm.getPlayer().getGender();

		if(seld == 12 || seld == 13 || seld == 14 || seld == 15 || seld == 16) {
			switch(seld) {
				case 12:
				case 13:
				case 14:
				case 15:
				case 16: {
					if (cm.getPlayer().getAndroid().getGender() == 1) {
						Gender = 1;
					} else {
						Gender = 0;
					}
					break;
				}
			}
			return;
		}

		if (sel == -1) {
			if (isMale) {
				cm.getPlayer().setHair(31002);
				cm.getPlayer().setFace(21700);
				cm.getPlayer().setGender(1);
			} else {
				cm.getPlayer().setHair(30000);
				cm.getPlayer().setFace(20100);
				cm.getPlayer().setGender(0);
			}
			cm.dispose();
			cm.fakeRelog();
			cm.updateChar();
			return;
		} else if (sel < 19) { // 헤어
			var hair = cm.getPlayerStat("HAIR");
			newAvatar = [];
			beauty = 1;
			var curColor = hair % 10;
			if (cm.getPlayerStat("GENDER") == 0) {
				switch (sel) {
					case 0: { // 남자 헤어 1페이지
						newAvatar = [30000 + curColor, 30020 + curColor, 30030 + curColor, 30040 + curColor, 30050 + curColor, 30060 + curColor, 30100 + curColor, 30110 + curColor, 30120 + curColor, 30130 + curColor, 30140 + curColor, 30150 + curColor, 30160 + curColor, 30170 + curColor, 30180 + curColor, 30190 + curColor, 30200 + curColor, 30210 + curColor, 30220 + curColor, 30230 + curColor, 30240 + curColor, 30250 + curColor, 30260 + curColor, 30270 + curColor, 30280 + curColor, 30290 + curColor, 30300 + curColor, 30310 + curColor, 30320 + curColor, 30330 + curColor, 30340 + curColor, 30350 + curColor, 30360 + curColor, 30370 + curColor, 30400 + curColor, 30410 + curColor, 30420 + curColor, 30440 + curColor, 30450 + curColor, 30460 + curColor, 30470 + curColor, 30480 + curColor, 30490 + curColor, 30510 + curColor, 30520 + curColor, 30530 + curColor, 30540 + curColor, 30560 + curColor, 30570 + curColor, 30590 + curColor, 30610 + curColor, 30620 + curColor, 30630 + curColor, 30640 + curColor, 30650 + curColor, 30660 + curColor, 30670 + curColor, 30680 + curColor, 30700 + curColor, 30710 + curColor, 30730 + curColor, 30760 + curColor, 30770 + curColor, 30790 + curColor, 30800 + curColor, 30810 + curColor, 30820 + curColor, 30830 + curColor, 30840 + curColor, 30850 + curColor, 30860 + curColor, 30870 + curColor, 30880 + curColor, 30910 + curColor, 30930 + curColor, 30940 + curColor, 30950 + curColor, 33030 + curColor, 33060 + curColor, 33070 + curColor, 33080 + curColor, 33090 + curColor, 33110 + curColor, 33120 + curColor, 33130 + curColor, 33150 + curColor, 33170 + curColor, 33180 + curColor, 33190 + curColor, 33210 + curColor, 33220 + curColor, 33250 + curColor, 33260 + curColor, 33270 + curColor, 33280 + curColor, 33310 + curColor, 33330 + curColor, 33350 + curColor, 33360 + curColor, 33370 + curColor];
						break;
					}
					case 1: { // 남자 헤어 2페이지
						newAvatar = [36010 + curColor, 36020 + curColor, 36030 + curColor, 36040 + curColor, 36050 + curColor, 36070 + curColor, 36080 + curColor, 36090 + curColor, 36100 + curColor, 36130 + curColor, 36140 + curColor, 36150 + curColor, 36160 + curColor, 36170 + curColor, 36180 + curColor, 36190 + curColor, 36200 + curColor, 36210 + curColor, 36220 + curColor, 36230 + curColor, 36240 + curColor, 36250 + curColor, 36300 + curColor, 36310 + curColor, 36330 + curColor, 36340 + curColor, 36350 + curColor, 36380 + curColor, 36390 + curColor, 36400 + curColor, 36410 + curColor, 36420 + curColor, 36430 + curColor, 36440 + curColor, 36450 + curColor, 36460 + curColor, 36470 + curColor, 36480 + curColor, 36510 + curColor, 36520 + curColor, 36530 + curColor, 36560 + curColor, 36570 + curColor, 36580 + curColor, 36590 + curColor, 36620 + curColor, 36630 + curColor, 36640 + curColor, 36650 + curColor, 36670 + curColor, 36680 + curColor, 36690 + curColor, 36700 + curColor, 36710 + curColor, 36720 + curColor, 36730 + curColor, 36740 + curColor, 36750 + curColor, 36760 + curColor, 36770 + curColor, 36780 + curColor, 36790 + curColor, 36800 + curColor, 36810 + curColor, 36820 + curColor, 36830 + curColor, 36840 + curColor, 36850 + curColor, 36860 + curColor, 36900 + curColor, 36910 + curColor, 36920 + curColor, 36940 + curColor, 36950 + curColor, 36980 + curColor, 36990 + curColor];
						break;

					}
					case 2: { // 남자 헤어 3페이지
						newAvatar = [40000 + curColor, 40010 + curColor, 40020 + curColor, 40050 + curColor, 40060 + curColor, 40090 + curColor, 40100 + curColor, 40120 + curColor, 40250 + curColor, 40260 + curColor, 40270 + curColor, 40280 + curColor, 40290 + curColor, 40300 + curColor, 40310 + curColor, 40320 + curColor, 40330 + curColor, 40390 + curColor, 40400 + curColor, 40410 + curColor, 40420 + curColor, 40440 + curColor, 40450 + curColor, 40470 + curColor, 40480 + curColor, 40490 + curColor, 33380 + curColor, 33390 + curColor, 33400 + curColor, 33410 + curColor, 33430 + curColor, 33440 + curColor, 33450 + curColor, 33460 + curColor, 33480 + curColor, 33500 + curColor, 33510 + curColor, 33520 + curColor, 33530 + curColor, 33550 + curColor, 33580 + curColor, 33590 + curColor, 33600 + curColor, 33610 + curColor, 33620 + curColor, 33630 + curColor, 33640 + curColor, 33660 + curColor, 33670 + curColor, 33680 + curColor, 33690 + curColor, 33700 + curColor, 33710 + curColor, 33720 + curColor, 33730 + curColor, 33740 + curColor, 33750 + curColor, 33760 + curColor, 33770 + curColor, 33780 + curColor, 33790 + curColor, 33800 + curColor, 33810 + curColor, 33820 + curColor, 33830 + curColor, 33930 + curColor, 33940 + curColor, 33950 + curColor, 33960 + curColor, 33990 + curColor, 35000 + curColor, 35010 + curColor, 35020 + curColor, 35030 + curColor, 35040 + curColor, 35050 + curColor, 35060 + curColor, 35070 + curColor, 35080 + curColor, 35090 + curColor, 35100 + curColor, 35150 + curColor, 35180 + curColor, 35190 + curColor, 35200 + curColor, 35210 + curColor, 35250 + curColor, 35260 + curColor, 35280 + curColor, 35290 + curColor, 35300 + curColor, 35310 + curColor, 35330 + curColor, 35350 + curColor, 35360 + curColor, 35420 + curColor, 35430 + curColor, 35440 + curColor, 35460 + curColor, 35470 + curColor, 35480 + curColor, 35490 + curColor, 35500 + curColor, 35510 + curColor, 35520 + curColor, 35530 + curColor, 35540 + curColor, 35550 + curColor, 35560 + curColor, 35570 + curColor, 35600 + curColor, 35620 + curColor, 35630 + curColor, 35640 + curColor, 35650 + curColor, 35660 + curColor, 35680 + curColor, 35690 + curColor, 35710 + curColor, 35720 + curColor, 35780 + curColor, 35790 + curColor, 35950 + curColor, 35960 + curColor];
						break;
					}
					case 3: { // 남자 헤어 4페이지
						newAvatar = [40500 + curColor, 40510 + curColor, 40570 + curColor, 40580 + curColor, 40600 + curColor, 40610 + curColor, 40640 + curColor, 40660 + curColor, 40670 + curColor, 40690 + curColor, 40720 + curColor, 40740 + curColor, 40810 + curColor, 40820 + curColor, 41060 + curColor, 41070 + curColor, 40930 + curColor, 43020 + curColor, 43140 + curColor, 43150 + curColor, 43180 + curColor, 40780 + curColor, 40770 + curColor, 43290 + curColor, 43300 + curColor, 43310 + curColor, 43320 + curColor, 43350 + curColor, 40650 + curColor, 40710 + curColor, 43580 + curColor, 43570 + curColor, 43330 + curColor, 43667 + curColor, 43610 + curColor, 43620 + curColor, 43700 + curColor, 43750 + curColor, 43750 + curColor, 43760 + curColor, 43770 + curColor, 43780 + curColor, 43810 + curColor, 43910 + curColor, 43990 + curColor, 45000 + curColor, 45010 + curColor, 45060 + curColor, 45040 + curColor, 45050 + curColor, 43820 + curColor, 43900 + curColor, 45090 + curColor, 46030 + curColor, 46060 + curColor, 46070 + curColor, 46080 + curColor, 46090 + curColor, 45220 + curColor, 45230 + curColor, 45150 + curColor, 45100 + curColor, 34910 + curColor, 45110 + curColor, 45150 + curColor, 46060 + curColor, 45160 + curColor, 45110 + curColor, 46330 + curColor, 46320 + curColor, 46310 + curColor, 46340 + curColor, 46350 + curColor, 46410 + curColor, 46420 + curColor, 46430 + curColor, 46440 + curColor, 46450 + curColor, 46460 + curColor, 46470 + curColor, 46490 + curColor, 46530 + curColor, 46590 + curColor, 46600 + curColor, 46650 + curColor, 46660 + curColor, 46690 + curColor, 46700 + curColor, 46710 + curColor, 46760 + curColor, 46770 + curColor, 46780 + curColor, 46790 + curColor, 46800 + curColor, 46810 + curColor, 46820 + curColor, 46830 + curColor, 46840 + curColor, 46850 + curColor, 46860 + curColor, 46870 + curColor, 46880 + curColor, 46890 + curColor, 46940 + curColor, 46970 + curColor, 46980 + curColor, 60000 + curColor, 60060 + curColor, 60070 + curColor, 60080 + curColor, 60090 + curColor, 60100 + curColor, 60110 + curColor, 60120 + curColor, 60130 + curColor, 60140 + curColor, 60150 + curColor];
						break;
					}
					default: {
						cm.sendOk(sel + "페이지는 존재하지 않습니다.");
						cm.dispose();
						return;
	}
				}
			} else {
				switch (sel) {
					case 0: { // 여자 헤어 1페이지
						newAvatar = [31000 + curColor, 31010 + curColor, 31020 + curColor, 31030 + curColor, 31040 + curColor, 31050 + curColor, 31060 + curColor, 31070 + curColor, 31080 + curColor, 31090 + curColor, 31100 + curColor, 31110 + curColor, 31120 + curColor, 31130 + curColor, 31140 + curColor, 31150 + curColor, 31160 + curColor, 31170 + curColor, 31180 + curColor, 31190 + curColor, 31200 + curColor, 31210 + curColor, 31220 + curColor, 31230 + curColor, 31240 + curColor, 31250 + curColor, 31260 + curColor, 31270 + curColor, 31280 + curColor, 31290 + curColor, 31300 + curColor, 31310 + curColor, 31320 + curColor, 31330 + curColor, 31340 + curColor, 31410 + curColor, 31420 + curColor, 31440 + curColor, 31450 + curColor, 31460 + curColor, 31470 + curColor, 31480 + curColor, 31490 + curColor, 31510 + curColor, 31520 + curColor, 31530 + curColor, 31540 + curColor, 31550 + curColor, 31560 + curColor, 31590 + curColor, 31610 + curColor, 31620 + curColor, 31630 + curColor, 31640 + curColor, 31650 + curColor, 31670 + curColor, 31680 + curColor, 31690 + curColor, 31700 + curColor, 31710 + curColor, 31720 + curColor, 31740 + curColor, 31750 + curColor, 31780 + curColor, 31790 + curColor, 31800 + curColor, 31810 + curColor, 31820 + curColor, 31840 + curColor, 31850 + curColor, 31860 + curColor, 31880 + curColor, 31890 + curColor, 31910 + curColor];
						break;
					}
					case 1: {// 여자 헤어 2페이지
						newAvatar = [47520 + curColor, 47530 + curColor, 47540 + curColor, 47550 + curColor, 47560 + curColor, 47570 + curColor, 47580 + curColor, 47590 + curColor, 47600 + curColor, 47610 + curColor, 47620 + curColor, 47630 + curColor, 47640 + curColor, 47650 + curColor, 47660 + curColor, 47670 + curColor, 47680 + curColor, 47690 + curColor, 47700 + curColor, 47710 + curColor, 47720 + curColor, 41080 + curColor, 41090 + curColor, 41100 + curColor, 41110 + curColor, 41120 + curColor, 41150 + curColor, 41160 + curColor, 41200 + curColor, 41220 + curColor, 31920 + curColor, 31930 + curColor, 31940 + curColor, 31950 + curColor, 31990 + curColor, 34040 + curColor, 34070 + curColor, 34080 + curColor, 34090 + curColor, 34100 + curColor, 34110 + curColor, 34120 + curColor, 34130 + curColor, 34140 + curColor, 34150 + curColor, 34160 + curColor, 34170 + curColor, 34180 + curColor, 34190 + curColor, 34210 + curColor, 34220 + curColor, 34230 + curColor, 34240 + curColor, 34250 + curColor, 34260 + curColor, 34270 + curColor, 34310 + curColor, 34320 + curColor, 34330 + curColor, 34340 + curColor, 34360 + curColor, 34370 + curColor, 34380 + curColor, 34400 + curColor, 34410 + curColor, 34420 + curColor, 34430 + curColor, 34440 + curColor, 34450 + curColor, 34470 + curColor, 34480 + curColor, 34490 + curColor, 34510 + curColor, 34540 + curColor, 34560 + curColor, 34590 + curColor, 34600 + curColor, 34610 + curColor, 34620 + curColor, 34630 + curColor, 34640 + curColor, 34660 + curColor, 34670 + curColor, 34680 + curColor, 34690 + curColor, 34700 + curColor, 34710 + curColor, 34720 + curColor, 34730 + curColor, 34740 + curColor, 34750 + curColor, 34760 + curColor, 34770 + curColor, 34780 + curColor, 34790 + curColor, 34800 + curColor, 34810 + curColor, 34820 + curColor, 34830 + curColor, 34840 + curColor, 34850 + curColor, 34860 + curColor, 34870 + curColor, 34880 + curColor, 34900 + curColor, 34910 + curColor, 34940 + curColor, 34950 + curColor, 34960 + curColor, 34970 + curColor];
						break;

					}
					case 2: { // 여자 헤어 3페이지
						newAvatar = [38660 + curColor, 38670 + curColor, 38680 + curColor, 38690 + curColor, 38700 + curColor, 38730 + curColor, 38740 + curColor, 38750 + curColor, 38760 + curColor, 38800 + curColor, 38810 + curColor, 38820 + curColor, 38840 + curColor, 38880 + curColor, 38910 + curColor, 38940 + curColor, 39090 + curColor, 37230 + curColor, 37370 + curColor, 37380 + curColor, 37400 + curColor, 37450 + curColor, 37460 + curColor, 37490 + curColor, 37500 + curColor, 37510 + curColor, 37520 + curColor, 37530 + curColor, 37560 + curColor, 37570 + curColor, 37580 + curColor, 37610 + curColor, 37620 + curColor, 37630 + curColor, 37640 + curColor, 37650 + curColor, 37660 + curColor, 37670 + curColor, 37680 + curColor, 37690 + curColor, 37700 + curColor, 37710 + curColor, 37720 + curColor, 37730 + curColor, 37740 + curColor, 37750 + curColor, 37760 + curColor, 37770 + curColor, 37780 + curColor, 37790 + curColor, 37800 + curColor, 37810 + curColor, 37820 + curColor, 37830 + curColor, 37840 + curColor, 37850 + curColor, 37860 + curColor, 37880 + curColor, 37910 + curColor, 37920 + curColor, 37940 + curColor, 37950 + curColor, 37960 + curColor, 37970 + curColor, 37980 + curColor, 37990 + curColor, 38000 + curColor, 38010 + curColor, 38020 + curColor, 38030 + curColor, 38040 + curColor, 38050 + curColor, 38060 + curColor, 38070 + curColor, 38090 + curColor, 38100 + curColor, 38110 + curColor, 38120 + curColor, 38130 + curColor, 38270 + curColor, 38280 + curColor, 38290 + curColor, 38300 + curColor, 38310 + curColor, 38380 + curColor, 38390 + curColor, 38400 + curColor, 38410 + curColor, 38420 + curColor, 38430 + curColor, 38440 + curColor, 38460 + curColor, 38470 + curColor, 38490 + curColor, 38520 + curColor, 38540 + curColor, 38550 + curColor, 38560 + curColor, 38570 + curColor, 38580 + curColor, 38590 + curColor, 38600 + curColor, 38610 + curColor, 38620 + curColor, 38630 + curColor, 38640 + curColor, 38650 + curColor, 41340 + curColor, 41350 + curColor, 41360 + curColor];
						break;
					}
					case 3: { // 여자 헤어 4페이지
						newAvatar = [41370 + curColor, 41380 + curColor, 41390 + curColor, 41400 + curColor, 41440 + curColor, 41470 + curColor, 41480 + curColor, 41490 + curColor, 41510 + curColor, 41520 + curColor, 41560 + curColor, 41570 + curColor, 41590 + curColor, 41600 + curColor, 41700 + curColor, 41720 + curColor, 41730 + curColor, 41740 + curColor, 41750 + curColor, 41850 + curColor, 41860 + curColor, 41880 + curColor, 41890 + curColor, 41920 + curColor, 41930 + curColor, 41950 + curColor, 44010 + curColor, 44120 + curColor, 44130 + curColor, 44320 + curColor, 44200 + curColor, 44330 + curColor, 44460 + curColor, 41900 + curColor, 44360 + curColor, 44290 + curColor, 44470 + curColor, 44480 + curColor, 44490 + curColor, 44500 + curColor, 44530 + curColor, 41870 + curColor, 44650 + curColor, 44770 + curColor, 44850 + curColor, 44780 + curColor, 44790 + curColor, 44802 + curColor, 44900 + curColor, 41940 + curColor, 44830 + curColor, 44840 + curColor, 44940 + curColor, 44950 + curColor, 47000 + curColor, 47040 + curColor, 47020 + curColor, 47010 + curColor, 47030 + curColor, 47070 + curColor, 47090 + curColor, 47310 + curColor, 47270 + curColor, 47280 + curColor, 47320 + curColor, 47330 + curColor, 47350 + curColor, 47370 + curColor, 47360 + curColor, 47340 + curColor, 48020 + curColor, 48050 + curColor, 48060 + curColor, 48070 + curColor, 48080 + curColor, 47530 + curColor, 47540 + curColor, 47460 + curColor, 47430 + curColor, 47400 + curColor, 47400 + curColor, 47430 + curColor, 47460 + curColor, 41140 + curColor, 46220 + curColor, 48210 + curColor, 48320 + curColor, 48340 + curColor, 48330 + curColor, 48350 + curColor, 48360 + curColor, 48370 + curColor, 48380 + curColor, 48410 + curColor, 48430 + curColor, 48480 + curColor, 48490 + curColor, 48500 + curColor, 48510 + curColor, 48520 + curColor, 48530 + curColor, 48540 + curColor, 48550 + curColor, 48560 + curColor, 48570 + curColor, 48580 + curColor, 48640 + curColor, 48650 + curColor, 48660 + curColor, 48730 + curColor, 48740 + curColor, 48750 + curColor, 48780 + curColor, 48810 + curColor, 48840 + curColor, 48850 + curColor, 48960 + curColor, 48970 + curColor, 48990 + curColor, 47740 + curColor, 47740 + curColor];
						break;
					}
					default: {
						cm.sendOk(sel + "페이지는 존재하지 않습니다.");
						cm.dispose();
						return;
					}
				}
			}
			cm.dispose();
			cm.askCoupon(5150190, newAvatar);
		} else if (sel >= 10 && sel < 20) { // 성형
			var face = cm.getPlayerStat("FACE");
			newAvatar = [];
			beauty = 1;
			var curColor = face % 10;
			if (cm.getPlayerStat("GENDER") == 0) {
				switch (sel) {
					case 10: { // 남자 성형 1페이지
						newAvatar = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20009, 20011, 20012, 20013, 20014, 20015, 20016, 20017, 20018, 20020, 20021, 20022, 20025, 20027, 20028, 20029, 20030, 20031, 20032, 20036, 20037, 20040, 20043, 20044, 20045, 20046, 20047, 20048, 20049, 20050, 20051, 20052, 20053, 20055, 20056, 20057, 20058, 20059, 20060, 20061, 20062, 20063, 20064, 20065, 20066, 20067, 20068, 20069, 20070, 20074, 20075, 20076, 20077, 20080, 20081, 20082, 20083, 20084, 20085, 20086, 20087, 20088, 20089, 20090, 20093, 20094, 20095, 20097, 20098, 20099, 20110, 23000, 23001, 23002, 23003, 23005, 23006, 23008, 23010, 23011, 23012, 23015, 23016, 23017, 23018, 23019, 23020, 23023, 23024, 23025, 23026, 23027, 23028, 23029, 23031, 23032, 23033, 23035, 23038, 23039, 23040, 23041, 23042, 23043, 23044, 23053, 23054, 23056, 23057, 23060];
						break;
					}
					case 11: { // 남자 성형 2페이지
						newAvatar = [50003, 50004, 50007, 50010, 50011, 50017, 50018, 50019, 50020, 50021, 50022, 50023, 50025, 50026, 50027, 50028, 50036, 50037, 50038, 50039, 50040, 50047, 23061, 23062, 23063, 23067, 23068, 23069, 23072, 23073, 23074, 23075, 23079, 23080, 23081, 23082, 23083, 23084, 23085, 23086, 23087, 23088, 23089, 23090, 23091, 23095, 23096, 23097, 23099, 24061, 24098, 25006, 25007, 25011, 25014, 25016, 25017, 25021, 25022, 25023, 25024, 25025, 25027, 25033, 25058, 25057, 25049, 25053, 25029, 25020, 25043, 25044, 25063, 25062, 25050, 25080, 25079, 25083, 25055, 25085, 25088, 25089, 25091, 25073, 25075, 25084, 25099, 27010, 27011, 27038, 27039, 25093, 27008, 27006, 27022, 27006, 27008, 27037, 27052, 27064, 27065, 27066, 27067, 27069, 27070, 27073, 27074, 27076, 27077, 27078, 27079, 27080, 27081, 27082, 27083, 27084, 27085, 27086, 27087, 27092, 27098];
						break;
					}
					default: {
						cm.sendOk(sel + "페이지는 존재하지 않습니다.");
						cm.dispose();
						return;
					}
				}
			} else {
				switch (sel) {
					case 10: { // 여자 성형 1페이지
						newAvatar = [24088, 24091, 24097, 24099, 25000, 25008, 25015, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21009, 21010, 21011, 21012, 21013, 21014, 21015, 21016, 21017, 21020, 21021, 21023, 21024, 21026, 21027, 21028, 21029, 21030, 21031, 21033, 21036, 21038, 21041, 21042, 21043, 21044, 21045, 21048, 21050, 21052, 21053, 21056, 21058, 21059, 21061, 21062, 21063, 21065, 21073, 21074, 21075, 21077, 21078, 21079, 21080, 21081, 21082, 21083, 21084, 21085, 21089, 21090, 21091, 21092, 21093, 21094, 21095, 21096, 21097, 21098, 24002, 24003, 24004, 24007, 24008, 24011, 24012, 24014, 24015, 24018, 24020, 24021, 24022, 24023, 24027, 24031, 24035, 24037, 24038, 24039, 24041, 24050, 24055, 24058, 24060, 24067, 24068, 24071, 24072, 24073, 24077, 24080, 24081, 24084, 24087];
						break;
					}
					case 11: { // 여자 성형 2페이지
						newAvatar = [51002, 51009, 51010, 51015, 51016, 51017, 51020, 51023, 51024, 51025, 51031, 51032, 51033, 51034, 51035, 51036, 51037, 51038, 51039, 51040, 51041, 51042, 51050, 51051, 51052, 51053, 51060, 51065, 51066, 51067, 51068, 51069, 51073, 51074, 26003, 26004, 26005, 26009, 26014, 26017, 26022, 26023, 26027, 26028, 26029, 26030, 26031, 26032, 26062, 26061, 26053, 26056, 26057, 26058, 26059, 26061, 26062, 26063, 26064, 26066, 26067, 26075, 26076, 26079, 26082, 26083, 26084, 26085, 26086, 26089, 26091, 26094, 26095, 26096, 26097, 26034, 26026, 26046, 26067, 26066, 26054, 26086, 26085, 25155, 26089, 26091, 26094, 26095, 26097, 28011, 26076, 26079, 28013, 26096, 28016, 28017, 28044, 26099, 28014, 28008, 28027, 28008, 28014, 28043, 28057, 28070, 28071, 28072, 28073, 28075, 28076, 28078, 28079, 28082, 28083, 28085, 28086, 28087, 28088, 28089, 28090, 28091, 28092, 28093, 28094, 28095, 28096, 28097];
						break;
					}
					default: {
						cm.sendOk(sel + "페이지는 존재하지 않습니다.");
						return;
					}
				}
			}

			switch (sel) {
				case 12:
					if (!Gender)
						codyList = [30000, 30020, 30030, 30040, 30050, 30060, 30100, 30110, 30120, 30130, 30140, 30150, 30160, 30170, 30180, 30190, 30200, 30210, 30220, 30230, 30240, 30250, 30260, 30270, 30280, 30290, 30300, 30310, 30320, 30330, 30340, 30350, 30360, 30370, 30400, 30410, 30420, 30440, 30450, 30460, 30470, 30480, 30490, 30510, 30520, 30530, 30540, 30560, 30570, 30590, 30610, 30620, 30630, 30640, 30650, 30660, 30670, 30680, 30700, 30710, 30730, 30760, 30770, 30790, 30800, 30810, 30820, 30830, 30840, 30850, 30860, 30870, 30880, 30910, 30930, 30940, 30950, 33030, 33060, 33070, 33080, 33090, 33110, 33120, 33130, 33150, 33170, 33180, 33190, 33210, 33220, 33250, 33260, 33270, 33280, 33310, 33330, 33350, 33360];

					else
						codyList = [31000, 31010, 31020, 31030, 31040, 31050, 31060, 31070, 31080, 31090, 31100, 31110, 31120, 31130, 31140, 31150, 31160, 31170, 31180, 31190, 31200, 31210, 31220, 31230, 31240, 31250, 31260, 31270, 31280, 31290, 31300, 31310, 31320, 31330, 31340, 31410, 31420, 31440, 31450, 31460, 31470, 31480, 31490, 31510, 31520, 31530, 31540, 31550, 31560, 31590, 31610, 31620, 31630, 31640, 31650, 31670, 31680, 31690, 31700, 31710, 31720, 31740, 31750, 31780, 31790, 31800, 31810, 31820, 31840, 31850, 31860, 31880, 31890];
					break;
				case 13:
					if (!Gender)
						codyList = [33380, 33390, 33400, 33410, 33430, 33440, 33450, 33460, 33480, 33500, 33510, 33520, 33530, 33550, 33580, 33590, 33600, 33610, 33620, 33630, 33640, 33660, 33670, 33680, 33690, 33700, 33710, 33720, 33730, 33740, 33750, 33760, 33770, 33780, 33790, 33800, 33810, 33820, 33830, 33930, 33940, 33950, 33960, 33990, 35000, 35010, 35020, 35030, 35040, 35050, 35060, 35070, 35080, 35090, 35100, 35150, 35180, 35190, 35200, 35210, 35250, 35260, 35280, 35290, 35300, 35310, 35330, 35350, 35360, 35420, 35430, 35440, 35460, 35470, 35480, 35490, 35500, 35510, 35520, 35530, 35540, 35550, 35560, 35570, 35600, 35620, 35630, 35640, 35650, 35660, 35680, 35690, 35710, 35720, 35780, 35790, 35950, 35960];

					else
						codyList = [31920, 31930, 31940, 31950, 31990, 34040, 34070, 34080, 34090, 34100, 34110, 34120, 34130, 34140, 34150, 34160, 34170, 34180, 34190, 34210, 34220, 34230, 34240, 34250, 34260, 34270, 34310, 34320, 34330, 34340, 34360, 34370, 34380, 34400, 34410, 34420, 34430, 34440, 34450, 34470, 34480, 34490, 34510, 34540, 34560, 34590, 34600, 34610, 34620, 34630, 34640, 34660, 34670, 34680, 34690, 34700, 34710, 34720, 34730, 34740, 34750, 34760, 34770, 34780, 34790, 34800, 34810, 34820, 34830, 34840, 34850, 34860, 34870, 34880, 34900, 34910, 34940, 34950, 34960, 34970];
					break;
				case 14:
					if (!Gender)
						codyList = [36010, 36020, 36030, 36040, 36050, 36070, 36080, 36090, 36100, 36130, 36140, 36150, 36160, 36170, 36180, 36190, 36200, 36210, 36220, 36230, 36240, 36250, 36300, 36310, 36330, 36340, 36350, 36380, 36390, 36400, 36410, 36420, 36430, 36440, 36450, 36460, 36470, 36480, 36510, 36520, 36530, 36560, 36570, 36580, 36590, 36620, 36630, 36640, 36650, 36670, 36680, 36690, 36700, 36710, 36720, 36730, 36740, 36750, 36760, 36770, 36780, 36790, 36800, 36810, 36820, 36830, 36840, 36850, 36860, 36900, 36910, 36920, 36940, 36950, 36980, 36990];

					else
						codyList = [37000, 37010, 37020, 37030, 37040, 37060, 37070, 37080, 37090, 37100, 37110, 37120, 37130, 37140, 37190, 37210, 37220, 37230, 37240, 37250, 37260, 37280, 37300, 37310, 37320, 37340, 37370, 37380, 37400, 37450, 37460, 37490, 37500, 37510, 37520, 37530, 37560, 37570, 37580, 37610, 37620, 37630, 37640, 37650, 37660, 37670, 37680, 37690, 37700, 37710, 37720, 37730, 37740, 37750, 37760, 37770, 37780, 37790, 37800, 37810, 37820, 37830, 37840, 37850, 37860, 37880, 37910, 37920, 37940, 37950, 37960, 37970, 37980, 37990, 38000, 38010, 38020, 38030, 38040, 38050, 38060, 38070, 38090, 38100, 38110, 38120, 38130, 38270, 38280, 38290, 38300, 38310, 38380, 38390, 38400, 38410, 38420, 38430, 38440, 38460, 38470, 38490, 38520, 38540, 38550, 38560, 38570, 38580, 38590, 38600, 38610, 38620, 38630, 38640, 38650];
					break;
				case 15:
					if (!Gender)
						codyList = [40000, 40010, 40020, 40050, 40060, 40090, 40100, 40120, 40250, 40260, 40270, 40280, 40290, 40300, 40310, 40320, 40330, 40390, 40400, 40410, 40420, 40440, 40450, 40470, 40480, 40490, 40500, 40510, 40570, 40580, 40600, 40610, 40640, 40660, 40670, 40690, 40720, 40740, 40810, 40820, 41060, 41070, 40930, 43020, 43140, 43150, 43180, 40780, 40770, 43290, 43300, 43310, 43320, 43350, 40650, 40710, 43580, 43570, 43330, 43667, 43610, 43620, 43700, 43750, 43750, 43760, 43770, 43780, 43810, 43910, 45000, 45010, 45060, 45040, 45050, 43820, 43900, 45090, 46030, 46060, 46070, 46080, 46090, 45220, 45230, 45150, 45100, 34910, 45110, 45150, 46060, 45160, 45110, 46330, 46320, 46310, 46340, 46350, 46420, 46430, 46440, 46450, 46460, 46490, 46500, 46530, 46570, 46590, 48850, 48840, 46810, 46790, 46770, 46830, 46840, 46870, 46780, 46890, 46860];

					else
						codyList = [38660, 38670, 38680, 38690, 38700, 38730, 38740, 38750, 38760, 38800, 38810, 38820, 38840, 38880, 38910, 38940, 39090, 41080, 41090, 41100, 41110, 41120, 41150, 41160, 41200, 41220, 41340, 41350, 41360, 41370, 41380, 41390, 41400, 41440, 41470, 41480, 41490, 41510, 41520, 41560, 41570, 41590, 41600, 41700, 44500, 44530, 41870, 44650, 44770, 44850, 44780, 44790, 44802, 44900, 41940, 44830, 44840, 44940, 44950, 47000, 47040, 47020, 47010, 47030, 47070, 47090, 47310, 47270, 47280, 47320, 47330, 47350, 47370, 47360, 47340, 48020, 48050, 48060, 48070, 48080, 47530, 47540, 47460, 47430, 47400, 47400, 47430, 47460, 41140, 46220, 48210, 48320, 48340, 48330, 48350, 48360, 48370, 48380, 48410, 48650, 48570, 48580, 48640, 48650, 48660, 48730, 48740, 48770, 48780, 48790, 48810];
					break;
				case 16:
					if (!Gender)
						codyList = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20009, 20011, 20012, 20013, 20014, 20015, 20016, 20017, 20018, 20020, 20021, 20022, 20025, 20027, 20028, 20029, 20030, 20031, 20032, 20036, 20037, 20040, 20043, 20044, 20045, 20046, 20047, 20048, 20049, 20050, 20051, 20052, 20053, 20055, 20056, 20057, 20058, 20059, 20060, 20061, 20062, 20063, 20064, 20065, 20066, 20067, 20068, 20069, 20070, 20074, 20075, 20076, 20077, 20080, 20081, 20082, 20083, 20084, 20085, 20086, 20087, 20088, 20089, 20090, 20093, 20094, 20095, 20097, 20098, 20099, 20110, 23000, 23001, 23002, 23003, 23005, 23006, 23008, 23010, 23011, 23012];

					else
						codyList = [21002, 21003, 21004, 21005, 21006, 21007, 21008, 21009, 21010, 21011, 21012, 21013, 21014, 21015, 21016, 21017, 21020, 21021, 21023, 21024, 21026, 21027, 21028, 21029, 21030, 21031, 21033, 21036, 21038, 21041, 21042, 21043, 21044, 21045, 21048, 21050, 21052, 21053, 21056, 21058, 21059, 21061, 21062, 21063, 21065, 21073, 21074, 21075, 21077, 21078, 21079, 21080, 21081, 21082, 21083, 21084, 21085, 21089, 21090, 21091, 21092, 21093, 21094, 21095, 21096, 21097, 21098, 24002, 24003, 24004];
					break;
			}

			cm.dispose();
			cm.askCoupon(5152259, newAvatar);
		} else if (sel == 20) {
			newAvatar = [0, 1, 2, 3, 4, 5, 6, 7];
			cm.dispose();
			cm.askCoupon(5151036, newAvatar);
		} else if (sel == 21) {
			newAvatar = [0, 1, 2, 3, 4, 5, 6, 7];
			cm.dispose();
			cm.askCoupon(5152111, newAvatar);
		} else if (sel == 22) {
			newAvatar = [0, 1, 2, 3, 4, 9, 10, 11, 12, 13, 15, 16, 18, 19, 24, 25, 26, 27];
			cm.askCoupon(5153000, newAvatar);
		} else if (sel == 23) {
			beauty = 1;
			cm.sendGetNumber("변경을 원하는 #r색상값#k을 입력하면 돼\r\n기존 칼라는 #d'0'#k 이야 (0 ~ 1000)", 0, 0, 1000);
		} else if (sel == 24) {
			cm.getPlayer().setKeyValue(100229, "hue", 0);
			cm.dispose();
			cm.fakeRelog();
			cm.updateChar();
		} else if (sel == 25) {
			cm.dispose();
			cm.openShop(1540213);
		}
	} else if (status == 3) {
		if(seld >= 12 && seld <= 16) {
			selStr = "#fn돋움##fc0xFFFFFFFF#전혀 새로운 스타일로 바꿔 줄 수 있지. 지금 모습이 지겨워 졌다면 바꾸고 싶은 헤어를 천천히 고민해 봐";
			cm.sendStyle(selStr, codyList);
		}
		if (beauty == 1) {
			if (sel < 0 && sel > 1000) {
				cm.sendOk("색상값을 잘못 입력하셨습니다.");
				cm.dispose();
				return;
			}
			cm.getPlayer().setKeyValue(100229, "hue", sel);
			cm.dispose();
			cm.fakeRelog();
			cm.updateChar();
		}
	}
}

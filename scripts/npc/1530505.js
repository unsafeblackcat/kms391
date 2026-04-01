importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.constants);

T10 = "#fUI/GuildMark.img/Mark/Letter/00005019/15#"
O10 = "#fUI/GuildMark.img/Mark/Letter/00005014/15#"
P10 = "#fUI/GuildMark.img/Mark/Letter/00005015/15#"

T2 = "#fUI/GuildMark.img/Mark/Letter/00005019/4#"
O2 = "#fUI/GuildMark.img/Mark/Letter/00005014/4#"
P2 = "#fUI/GuildMark.img/Mark/Letter/00005015/4#"

T2Num0 = "#fUI/GuildMark.img/Mark/Letter/00005026/4#"
T2Num1 = "#fUI/GuildMark.img/Mark/Letter/00005027/4#"
T2Num2 = "#fUI/GuildMark.img/Mark/Letter/00005028/4#"

Num0 = "#fUI/GuildMark.img/Mark/Letter/00005026/15#"
Num1 = "#fUI/GuildMark.img/Mark/Letter/00005027/15#"
Num2 = "#fUI/GuildMark.img/Mark/Letter/00005028/15#"
Num3 = "#fUI/GuildMark.img/Mark/Letter/00005029/15#"
Num4 = "#fUI/GuildMark.img/Mark/Letter/00005030/15#"
Num5 = "#fUI/GuildMark.img/Mark/Letter/00005031/15#"
Num6 = "#fUI/GuildMark.img/Mark/Letter/00005032/15#"
Num7 = "#fUI/GuildMark.img/Mark/Letter/00005033/15#"
Num8 = "#fUI/GuildMark.img/Mark/Letter/00005034/15#"
Num9 = "#fUI/GuildMark.img/Mark/Letter/00005035/15#"


function start() {
	print = new StringBuilder();
	App = cm.getPlayer().FuckingRanking(2);

	for (i = 1; i < App.size(); i++) {
		switch (Integer.parseInt(App.get(i).getLeft())) {
			case 100:
				job = "검사";
				break;
			case 200:
				job = "마법사";
				break;
			case 300:
				job = "궁수";
				break;
			case 400:
				job = "도적";
				break;
			case 500:
				job = "해적";
				break;
			case 110:
			case 111:
			case 112:
				job = "히어로";
				break;
			case 120:
			case 121:
			case 122:
				job = "팔라딘";
				break;
			case 130:
			case 131:
			case 132:
				job = "다크나이트";
				break;
			case 210:
			case 211:
			case 212:
				job = "아크메이지(불, 독)";
				break;
			case 220:
			case 221:
			case 222:
				job = "아크메이지(썬, 콜)";
				break;
			case 230:
			case 231:
			case 232:
				job = "비숍";
				break;
			case 310:
			case 311:
			case 312:
				job = "보우마스터";
				break;
			case 320:
			case 321:
			case 322:
				job = "신궁";
				break;
			case 330:
			case 331:
			case 332:
				job = "패스파인더";
				break;
			case 410:
			case 411:
			case 412:
				job = "나이트로드";
				break;
			case 420:
			case 421:
			case 422:
				job = "섀도어";
				break;
			case 430:
			case 431:
			case 432:
			case 433:
			case 434:
				job = "듀얼블레이더";
				break;
			case 510:
			case 511:
			case 512:
				job = "바이퍼";
				break;
			case 520:
			case 521:
			case 522:
				job = "캡틴";
				break;
			case 530:
			case 531:
			case 532:
				job = "캐논슈터";
				break;
			case 1100:
			case 1110:
			case 1111:
			case 1112:
				job = "소울마스터";
				break;
			case 1200:
			case 1210:
			case 1211:
			case 1212:
				job = "플레임위자드";
				break;
			case 1300:
			case 1310:
			case 1311:
			case 1312:
				job = "윈드브레이커";
				break;
			case 1400:
			case 1410:
			case 1411:
			case 1412:
				job = "나이트워커";
				break;
			case 1500:
			case 1510:
			case 1511:
			case 1512:
				job = "스트라이커";
				break;
			case 2000:
			case 2100:
			case 2110:
			case 2111:
			case 2112:
				job = "아란";
				break;
			case 2001:
			case 2200:
			case 2210:
			case 2211:
			case 2212:
			case 2213:
			case 2214:
			case 2215:
			case 2216:
			case 2217:
			case 2218:
				job = "에반";
				break;
			case 2002:
			case 2300:
			case 2310:
			case 2311:
			case 2312:
				job = "메르세데스";
				break;
			case 2003:
			case 2400:
			case 2410:
			case 2411:
			case 2412:
				job = "팬텀";
				break;
			case 2005:
			case 2500:
			case 2510:
			case 2511:
			case 2512:
				job = "은월";
				break;
			case 2004:
			case 2700:
			case 2710:
			case 2711:
			case 2712:
				job = "루미너스";
				break;
			case 3001:
			case 3100:
			case 3110:
			case 3111:
			case 3112:
				job = "데몬슬레이어";
				break;
			case 3002:
			case 3101:
			case 3120:
			case 3121:
			case 3122:
				job = "데몬어벤져";
				break;
			case 3200:
			case 3210:
			case 3211:
			case 3212:
				job = "배틀메이지";
				break;
			case 3300:
			case 3310:
			case 3311:
			case 3312:
				job = "와일드 헌터";
				break;
			case 3500:
			case 3510:
			case 3511:
			case 3512:
				job = "메카닉";
				break;
			case 3600:
			case 3610:
			case 3611:
			case 3612:
				job = "제논";
				break;
			case 3700:
			case 3710:
			case 3711:
			case 3712:
				job = "블래스터";
				break;
			case 5000:
			case 5100:
			case 5110:
			case 5111:
			case 5112:
				job = "미하일";
				break;
			case 6000:
			case 6100:
			case 6110:
			case 6111:
			case 6112:
				job = "카이저";
				break;
			case 6001:
			case 6500:
			case 6510:
			case 6511:
			case 6512:
				job = "엔젤릭버스터";
				break;
			case 10000:
			case 10100:
			case 10110:
			case 10111:
			case 10112:
				job = "제로";
				break;
			case 13000:
			case 13100:
				job = "핑크빈";
				break;
			case 14000:
			case 14200:
			case 14210:
			case 14211:
			case 14212:
				job = "키네시스";
				break;
			case 6002:
			case 6400:
			case 6410:
			case 6411:
			case 6412:
				job = "카데나";
				break;
			case 15002:
			case 15100:
			case 15110:
			case 15111:
			case 15112:
				job = "아델";
				break;
			case 15000:
			case 15200:
			case 15210:
			case 15211:
			case 15212:
				job = "일리움";
				break;
			case 15001:
			case 15500:
			case 15510:
			case 15511:
			case 15512:
				job = "아크";
				break;
			case 16000:
			case 16400:
			case 16410:
			case 16411:
			case 16412:
				job = "호영";
				break;
			case 6300:
			case 6310:
			case 6311:
			case 6312:
				job = "카인";
				break;
			case 13100:
				job = "핑크빈";
				break;
			case 13500:
				job = "예티";
				break;
			default:
				job = "미확인";

		}
		순위 = (i == 1 ? " " + T2Num0 + T2Num1 + "#fs15##fc0xFFFF8224#" : i == 2 ? " " + T2Num0 + T2Num2 + "#fs15##fc0xFFFF8224#" : i == 3 ? " " + Num0 + Num3 : i == 4 ? " " + Num0 + Num4 : i == 5 ? " " + Num0 + Num5 : i == 6 ? " " + Num0 + Num6 : i == 7 ? " " + Num0 + Num7 : i == 8 ? " " + Num0 + Num8 : i == 9 ? " " + Num0 + Num9 : i == 10 ? " " + Num1 + Num0 : Num0)

		print.append(순위).append(" ─ #e#fs15#").append(App.get(i).getMid()).append("#k#n 님\r\n#fs15##fc0xFF2457BD#* 메소#k : #r").append(App.get(i).getRight()).append(" 원#k #e/#n #d* 직업#k : #b").append(job).append("#k\r\n\r\n");
	}
	cm.sendOkS("\r\n" + print.toString(), 0x04, 9076004);
	cm.dispose();
}
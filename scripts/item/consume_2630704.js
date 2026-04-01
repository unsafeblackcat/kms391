importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

히어로 = [1352202];
팔라딘 = [1352212];
다크나이트 = [1352222];

불독 = [1352232];
썬콜 = [1352242];
비숍 = [1352252];

보우마스터 = [1352262];
신궁 = [1352272];
패스파인더 = [1353703];

섀도어 = [1352282];
나이트로드 = [1352292];
듀얼블레이드 = [1342008];

바이퍼 = [1352902];
캡틴 = [1352912];
캐논슈터 = [1352922];

메르세데스 = [1352003];
아란 = [1352932];
에반 = [1352942];
루미너스 = [1352403];
팬텀 = [1352103];
은월 = [1353103];
데몬 = [1099004];

배틀메이지 = [1352952];
와일드헌터 = [1352962];
제논 = [1353004];
메카닉 = [1352703];
블래 = [1353403];

시그너스 = [1352972];
미하일 = [1098003];

카이저 = [1352503];
엔젤릭버스터 = [1352604];

키네시스 = [1353203];
카데나 = [1353303];
아크 =[1353603];
호영 = [1353803];

아델 = [1354003];
카인 = [1354013];



var purple = "#fc0xFF7401DF#";

function start() {
    status = -1;
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
	var text = "#fn나눔고딕#" + purple + "<보조 무기 선택>#k#n\r\n\r\n";
	text += "#fUI/UIWindow2.img/QuestIcon/3/0#\r\n";
	switch (cm.getPlayer().getJob()) {

//모-전
	    //히어로
	    case 110:
	    case 111:
	    case 112:
        	for (i = 0; i < 히어로.length; i++) {
		text += "#b#L"+ 히어로[i] +"# #i"+ 히어로[i] +"# #z"+ 히어로[i] +"# #k#l\r\n";
		}
		break;

	    //팔라딘
	    case 120:
	    case 121:
	    case 122:
        	for (i = 0; i < 팔라딘.length; i++) {
		text += "#b#L"+ 팔라딘[i] +"# #i"+ 팔라딘[i] +"# #z"+ 팔라딘[i] +"# #k#l\r\n";
		}
		break;

	    //다크나이트
	    case 130:
	    case 131:
	    case 132:
        	for (i = 0; i < 다크나이트.length; i++) {
		text += "#b#L"+ 다크나이트[i] +"# #i"+ 다크나이트[i] +"# #z"+ 다크나이트[i] +"# #k#l\r\n";
		}
		break;

//모-법
	    //불독
	    case 210:
	    case 211:
	    case 212:
        	for (i = 0; i < 불독.length; i++) {
		text += "#b#L"+ 불독[i] +"# #i"+ 불독[i] +"# #z"+ 불독[i] +"# #k#l\r\n";
		}
		break;

	    //썬콜
	    case 220:
	    case 221:
	    case 222:
        	for (i = 0; i < 썬콜.length; i++) {
		text += "#b#L"+ 썬콜[i] +"# #i"+ 썬콜[i] +"# #z"+ 썬콜[i] +"# #k#l\r\n";
		}
		break;

	    //비숍
	    case 230:
	    case 231:
	    case 232:
        	for (i = 0; i < 비숍.length; i++) {
		text += "#b#L"+ 비숍[i] +"# #i"+ 비숍[i] +"# #z"+ 비숍[i] +"# #k#l\r\n";
		}
		break;

//모-궁
	    //보우마스터
	    case 310:
	    case 311:
	    case 312:
        	for (i = 0; i < 보우마스터.length; i++) {
		text += "#b#L"+ 보우마스터[i] +"# #i"+ 보우마스터[i] +"# #z"+ 보우마스터[i] +"# #k#l\r\n";
		}
		break;

	    //신궁
	    case 320:
	    case 321:
	    case 322:
        	for (i = 0; i < 신궁.length; i++) {
		text += "#b#L"+ 신궁[i] +"# #i"+ 신궁[i] +"# #z"+ 신궁[i] +"# #k#l\r\n";
		}
		break;

	    //패스파인더
	    case 330:
	    case 331:
	    case 332:
        	for (i = 0; i < 패스파인더.length; i++) {
		text += "#b#L"+ 패스파인더[i] +"# #i"+ 패스파인더[i] +"# #z"+ 패스파인더[i] +"# #k#l\r\n";
		}
		break;

//모-도
	    //나이트로드
	    case 410:
	    case 411:
	    case 412:
        	for (i = 0; i < 나이트로드.length; i++) {
		text += "#b#L"+ 나이트로드[i] +"# #i"+ 나이트로드[i] +"# #z"+ 나이트로드[i] +"# #k#l\r\n";
		}
		break;

	    //섀도어
	    case 420:
	    case 421:
	    case 422:
        	for (i = 0; i < 섀도어.length; i++) {
		text += "#b#L"+ 섀도어[i] +"# #i"+ 섀도어[i] +"# #z"+ 섀도어[i] +"# #k#l\r\n";
		}
		break;

	    //듀얼블레이드
	    case 430:
	    case 431:
	    case 432:
	    case 433:
	    case 434:
        	for (i = 0; i < 듀얼블레이드.length; i++) {
		text += "#b#L"+ 듀얼블레이드[i] +"# #i"+ 듀얼블레이드[i] +"# #z"+ 듀얼블레이드[i] +"# #k#l\r\n";
		}
		break;

//모-해
	    //바이퍼
	    case 510:
	    case 511:
	    case 512:
        	for (i = 0; i < 바이퍼.length; i++) {
		text += "#b#L"+ 바이퍼[i] +"# #i"+ 바이퍼[i] +"# #z"+ 바이퍼[i] +"# #k#l\r\n";
		}
		break;

	    //캡틴
	    case 520:
	    case 521:
	    case 522:
        	for (i = 0; i < 캡틴.length; i++) {
		text += "#b#L"+ 캡틴[i] +"# #i"+ 캡틴[i] +"# #z"+ 캡틴[i] +"# #k#l\r\n";
		}
		break;

	    //캐논슈터
	    case 501:
	    case 530:
	    case 531:
	    case 532:
        	for (i = 0; i < 캐논슈터.length; i++) {
		text += "#b#L"+ 캐논슈터[i] +"# #i"+ 캐논슈터[i] +"# #z"+ 캐논슈터[i] +"# #k#l\r\n";
		}
		break;

//시그너스 기사단
	    //시그너스
	    case 1100:
	    case 1110:
	    case 1111:
	    case 1112:
	    case 1200:
	    case 1210:
	    case 1211:
	    case 1212:
	    case 1300:
	    case 1310:
	    case 1311:
	    case 1312:
	    case 1400:
	    case 1410:
	    case 1411:
	    case 1412:
	    case 1500:
	    case 1510:
	    case 1511:
	    case 1512:
        	for (i = 0; i < 시그너스.length; i++) {
		text += "#b#L"+ 시그너스[i] +"# #i"+ 시그너스[i] +"# #z"+ 시그너스[i] +"# #k#l\r\n";
		}
		break;

	    //미하일
	    case 5100:
	    case 5110:
	    case 5111:
	    case 5112:
        	for (i = 0; i < 미하일.length; i++) {
		text += "#b#L"+ 미하일[i] +"# #i"+ 미하일[i] +"# #z"+ 미하일[i] +"# #k#l\r\n";
		}
		break;
//영웅
	    //아란
	    case 2100:
	    case 2110:
	    case 2111:
	    case 2112:
        	for (i = 0; i < 아란.length; i++) {
		text += "#b#L"+ 아란[i] +"# #i"+ 아란[i] +"# #z"+ 아란[i] +"# #k#l\r\n";
		}
		break;

	    //에반
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
        	for (i = 0; i < 에반.length; i++) {
		text += "#b#L"+ 에반[i] +"# #i"+ 에반[i] +"# #z"+ 에반[i] +"# #k#l\r\n";
		}
		break;

	    //메르세데스
	    case 2300:
	    case 2310:
	    case 2311:
	    case 2312:
        	for (i = 0; i < 메르세데스.length; i++) {
		text += "#b#L"+ 메르세데스[i] +"# #i"+ 메르세데스[i] +"# #z"+ 메르세데스[i] +"# #k#l\r\n";
		}
		break;

	    //팬텀
	    case 2400:
	    case 2410:
	    case 2411:
	    case 2412:
        	for (i = 0; i < 팬텀.length; i++) {
		text += "#b#L"+ 팬텀[i] +"# #i"+ 팬텀[i] +"# #z"+ 팬텀[i] +"# #k#l\r\n";
		}
		break;

	    //은월
	    case 2500:
	    case 2510:
	    case 2511:
	    case 2512:
        	for (i = 0; i < 은월.length; i++) {
		text += "#b#L"+ 은월[i] +"# #i"+ 은월[i] +"# #z"+ 은월[i] +"# #k#l\r\n";
		}
		break;

	    //루미너스
	    case 2700:
	    case 2710:
	    case 2711:
	    case 2712:
        	for (i = 0; i < 루미너스.length; i++) {
		text += "#b#L"+ 루미너스[i] +"# #i"+ 루미너스[i] +"# #z"+ 루미너스[i] +"# #k#l\r\n";
		}
		break;

//레지스탕스
	    //데몬
	    case 3100:
	    case 3110:
	    case 3111:
	    case 3112:
	    case 3101:
	    case 3120:
	    case 3121:
	    case 3122:
        	for (i = 0; i < 데몬.length; i++) {
		text += "#b#L"+ 데몬[i] +"# #i"+ 데몬[i] +"# #z"+ 데몬[i] +"# #k#l\r\n";
		}
		break;

	    //배틀메이지
	    case 3200:
	    case 3210:
	    case 3211:
	    case 3212:
        	for (i = 0; i < 배틀메이지.length; i++) {
		text += "#b#L"+ 배틀메이지[i] +"# #i"+ 배틀메이지[i] +"# #z"+ 배틀메이지[i] +"# #k#l\r\n";
		}
		break;

	    //와일드헌터
	    case 3300:
	    case 3310:
	    case 3311:
	    case 3312:
        	for (i = 0; i < 와일드헌터.length; i++) {
		text += "#b#L"+ 와일드헌터[i] +"# #i"+ 와일드헌터[i] +"# #z"+ 와일드헌터[i] +"# #k#l\r\n";
		}
		break;

	    //메카닉
	    case 3500:
	    case 3510:
	    case 3511:
	    case 3512:
        	for (i = 0; i < 메카닉.length; i++) {
		text += "#b#L"+ 메카닉[i] +"# #i"+ 메카닉[i] +"# #z"+ 메카닉[i] +"# #k#l\r\n";
		}
		break;


	    //제논
	    case 3600:
	    case 3610:
	    case 3611:
	    case 3612:
        	for (i = 0; i < 제논.length; i++) {
		text += "#b#L"+ 제논[i] +"# #i"+ 제논[i] +"# #z"+ 제논[i] +"# #k#l\r\n";
		}
		break;

	    //블래스터
	    case 3700:
	    case 3710:
	    case 3711:
	    case 3712:
        	for (i = 0; i < 블래스터.length; i++) {
		text += "#b#L"+ 블래스터[i] +"# #i"+ 블래스터[i] +"# #z"+ 블래스터[i] +"# #k#l\r\n";
		}
		break;

//노바
	    //카이저
	    case 6100:
	    case 6110:
	    case 6111:
	    case 6112:
        	for (i = 0; i < 카이저.length; i++) {
		text += "#b#L"+ 카이저[i] +"# #i"+ 카이저[i] +"# #z"+ 카이저[i] +"# #k#l\r\n";
		}
		break;

	    //카인
                case 6003:
                case 6300:
                case 6310:
                case 6311:
                case 6312:
        	for (i = 0; i < 카인.length; i++) {
		text += "#b#L"+ 카인[i] +"# #i"+ 카인[i] +"# #z"+ 카인[i] +"# #k#l\r\n";
		}
		break;

	    //카데나
	    case 6400:
	    case 6410:
	    case 6411:
	    case 6412:
        	for (i = 0; i < 카데나.length; i++) {
		text += "#b#L"+ 카데나[i] +"# #i"+ 카데나[i] +"# #z"+ 카데나[i] +"# #k#l\r\n";
		}
		break;

	    //엔젤릭버스터
	    case 6500:
	    case 6510:
	    case 6511:
	    case 6512:
        	for (i = 0; i < 엔젤릭버스터.length; i++) {
		text += "#b#L"+ 엔젤릭버스터[i] +"# #i"+ 엔젤릭버스터[i] +"# #z"+ 엔젤릭버스터[i] +"# #k#l\r\n";
		}
		break;

//레프
	    //아델
	    case 15100:
	    case 15110:
	    case 15111:
	    case 15112:
        	for (i = 0; i < 아델.length; i++) {
		text += "#b#L"+ 아델[i] +"# #i"+ 아델[i] +"# #z"+ 아델[i] +"# #k#l\r\n";
		}
		break;

	    //아크
	    case 15500:
	    case 15510:
	    case 15511:
	    case 15512:
        	for (i = 0; i < 아크.length; i++) {
		text += "#b#L"+ 아크[i] +"# #i"+ 아크[i] +"# #z"+ 아크[i] +"# #k#l\r\n";
		}
		break;

//아니마
	    //호영
	    case 16400:
	    case 16410:
	    case 16411:
	    case 16412:
        	for (i = 0; i < 호영.length; i++) {
		text += "#b#L"+ 호영[i] +"# #i"+ 호영[i] +"# #z"+ 호영[i] +"# #k#l\r\n";
		}
		break;

	    //키네시스
	    case 14200:
	    case 14210:
	    case 14211:
	    case 14212:
        	for (i = 0; i < 키네시스.length; i++) {
		text += "#b#L"+ 키네시스[i] +"# #i"+ 키네시스[i] +"# #z"+ 키네시스[i] +"# #k#l\r\n";
		}
		break;
}
	cm.sendSimple(text);
	} else if (status == 1) {
	    var a = "#fn나눔고딕#" + purple + "<화이트 제네시스 무기선택>#k#n\r\n\r\n";
	    a += "#e#b#h0##k#n 님에게 딱 맞는 장비를 지급해드렸어요!\r\n그리고 장비의 능력치를 살짝 강화시켜드렸어요.\r\n#e";
	    a += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";
	    a += "#b#i"+ selection +"# #z"+ selection +"##k #e#r(선택한 아이템)#k#n\r\n";
	    var inz = MapleItemInformationProvider.getInstance().getEquipById(selection);
	    inz.setReqLevel(-90);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setIgnorePDR(100);
	inz.setUpgradeSlots(0);
	inz.setEnhance(30);
	inz.setUpgradeSlots(0);
	    MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    cm.gainItem(2630704, -1);
	    cm.sendSimple(a);
	    cm.dispose();
	}
}
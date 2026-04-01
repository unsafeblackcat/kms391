var enter = "\r\n";
var seld = -1;

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
	if (status == 0) {
		var msg = "";
		msg += "#L1##fUI/UIWindow.img/AranSkillGuide/16#";
		msg += "#L2##fUI/UIWindow.img/AranSkillGuide/17#";
		msg += "#L3##fUI/UIWindow.img/AranSkillGuide/18#";
		msg += "#L4##fUI/UIWindow.img/AranSkillGuide/19#\r\n\r\n";
		msg += "#L5##fUI/UIWindow.img/AranSkillGuide/20#";
        msg += "#L6##fUI/UIWindow.img/AranSkillGuide/21#";
        msg += "#L7##fUI/UIWindow.img/AranSkillGuide/22#";
        msg += "#L8##fUI/UIWindow.img/AranSkillGuide/23#\r\n\r\n";
        msg += "\r\n";
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				var msg = "";
				msg += "#L1##fUI/UIWindow6.img/attackerSetting/Board/sector/char_main/3/0##fs15# #d 메이플 유니온(Union) #r업무"+enter+enter;
				msg += "#L2##fUI/UIWindow6.img/attackerSetting/Board/sector/char_main/-1/0# #d 메이플 유니온(Union) #r코인 상점";
				
				cm.sendSimple(msg);
			break;
			case 2:
				var msg = "";
                                //msg += "#L9##fs15##fUI/UIWindow8.img/EldaGauge/tooltip/50#  (Lv. 공용)#r 퀘스트의 전당 이동 #k(서브퀘스트 클리어)#n#k"+enter;
                                //msg += "#L11##fs15##fUI/UIWindow8.img/EldaGauge/tooltip/50#  (Lv. 공용)#r 카오 다이어리 수집 #k(다이어리 수집가!)#n#k"+enter+enter;
				msg += "#L1##fs15##fUI/UIWindow.img/Quest/icon0#  (Lv. 190)#d 헤이븐 - #b긴급 지원#n#k#l"+enter+enter;
				msg += "         └ 보상 : #b#z2435719# 5개#k\r\n";
				msg += "#L7##fs15##fUI/UIWindow.img/Quest/icon0#  (Lv. 190)#d 버려진 야영지 - #b어둠 퇴치#n#k#l"+enter+enter;
				msg += "         └ 보상 : #b#z2435719# 10개#k\r\n";
				msg += "#L2##fUI/UIWindow.img/Quest/icon0#  (Lv. 210)#d 소멸의여로 - #b소멸의 여로 조사#n#k#l"+enter+enter;
				msg += "         └ 보상 : #b#z1712001# 10개#k\r\n";
				msg += "#L3##fUI/UIWindow.img/Quest/icon0#  (Lv. 220)#d 츄츄아일랜드 - #b삐빅의 의뢰#n#k#l"+enter+enter;
				msg += "         └ 보상 : #b#z1712002# 10개#k\r\n";
				msg += "#L4##fUI/UIWindow.img/Quest/icon0#  (Lv. 240)#d 레헬른 - #b마을촌장의 부탁#n#k#l"+enter+enter;
				msg += "         └ 보상 : #b#z1712003# 10개#k\r\n";
				msg += "#L8##fUI/UIWindow.img/Quest/icon0#  (Lv. 245)#d 아르카나 - #b아르카나의 위기#n#k#l"+enter+enter;
				msg += "         └ 보상 : #b#z1712004# 10개#k\r\n";
				msg += "#L5##fUI/UIWindow.img/Quest/icon0#  (Lv. 270)#d 모라스 - #b모라스의 안정을 위해#n#k#l"+enter+enter;
				msg += "         └ 보상 : #b#z1712005# 10개#k\r\n";
                msg += "#L6##fUI/UIWindow.img/Quest/icon0#  (Lv. 300)#d 에스페라 - #b에스페라 정화#n#k#l"+enter+enter;
                msg += "         └ 보상 : #b#z1712006# 10개#k";
				cm.sendSimple(msg);
			break;
			case 3:
				var msg = "#fs15#안녕하세요 ? 프로스트 스카디의 #b게임 컨텐츠#k 목록입니다.\r\n무슨 컨텐츠를 이용하시겠어요 ?"+enter+enter;
				msg += "#L8##fs15##i3801529# #d티어 승급 시스템 :: 티어별 패시브 효과 보상\r\n\r\n";
				msg += "#L1##fs15##i3801523# #d배고픈 무토 :: 아케인심볼 보상\r\n";
				msg += "#L2##fs15##i3801524# #d드림 브레이커 :: 아케인심볼 보상\r\n";
				msg += "#L3##fs15##i3801525# #d스피릿 세이비어 :: 아케인심볼 보상\r\n\r\n";
				msg += "#L4##fs15##i3801526# #d와글와글 하우스 :: 어센틱심볼 보상\r\n";
				msg += "#L6##fs15##i3801527# #d불꽃 늑대 :: 어센틱심볼, 파워 성장의 비약 보상\r\n\r\n";
				msg += "#L7##fs15##i3801530# #d라이브 탕윤 식당 :: 티어 승급 코인 보상\r\n";
				msg += "#L9##fs15##i3801531# #d라이브 퍼즐 마스터 :: 티어 승급 코인 보상\r\n\r\n";
				msg += "#L5##fs15##i3801528# #d갓 오브 컨트롤 :: 보상 없음";
				//msg += "#L7##fs15##i3801529# #d마피아 게임 :: 메소 보상";
				cm.sendSimple(msg);
			break;
			case 4:
/*
				var msg = "";
				msg += "#L1##fs15##fs15##fUI/UIWindow8.img/EldaGauge/tooltip/23# #d레벨 랭킹"+enter;
				msg += "#L2##fs15##fUI/UIWindow8.img/EldaGauge/tooltip/23# 메소 랭킹"+enter;
				msg += "#L3##fs15##fUI/UIWindow8.img/EldaGauge/tooltip/23# 데미지 측정랭킹"+enter;
				msg += "#L4##fs15##fUI/UIWindow8.img/EldaGauge/tooltip/23# 길드 랭킹"+enter;
				msg += "#L5##fs15##fUI/UIWindow8.img/EldaGauge/tooltip/23# 무릉도장 랭킹"+enter;
				cm.sendSimple(msg);
*/
				cm.dispose();
				cm.openNpc(9076004);
			break;
			case 5:
				var msg = "";
				msg += "#L1##fs15##i3801525# #d길드(Guild) #b생성 / 관리 "+enter+enter;
				msg += "#L2##fs15##i3801526# #d연합(Alliance) #b생성 / 관리";
                //msg += "#L3##fs15##i3801525# #d경쟁전(원정대) #b생성 / 시작";
				//msg += "#L4#선택지4"+enter;
				//msg += "#L5#선택지5"+enter;
				cm.sendSimple(msg);
                        case 6:
				var msg = "";
				//msg += "#L1##fs15##i3801525# #d웨딩 빌리지 이동 #b(결혼 시스템)"+enter+enter;
				msg += "#L2##i3801526# #d커플링 / 우정링 제작";
				//msg += "#L3#선택지3"+enter;
				//msg += "#L4#선택지4"+enter;
				//msg += "#L5#선택지5"+enter;
				cm.sendSimple(msg);
			break;
            case 7:
				var msg = "#fs15#인내심을 테스트 해보고 싶으신가요 ?\r\n#b점프맵#k을 클리어하고 보상도 받아보세요! #fs20##r(보상추가중)#k#fs15#"+enter+enter;
				msg += "#L1##fs15##d인내의 숲"+enter;
				msg += "#L2##fs15#끈기의 숲"+enter;
				msg += "#L3##fs15#고지를 향해서"+enter;
				msg += "#L4##fs15#펫 산책로 (헤네시스)"+enter;
				msg += "#L5##fs15#펫 산책로 (루디브리엄)"+enter;
				cm.sendSimple(msg);
			break;
            case 8:
				var msg = "";
				//msg += "#L2##fUI/UIWindow8.img/EldaGauge/tooltip/23##d 레벨 달성 보상받기#k";
				msg += "#L6##i3801525# #d각종 아이템 뽑기#k"+enter+enter; // +enter+enter
				msg += "#L7##i3801306# #d[이벤트] 수박 모으기!#k"+enter+enter;
				msg += "#L8##i3801519# #d[이벤트] 돌림판 돌리기!#k";
				//msg += "#L3##i3801526# #d랜덤 상자 뽑기#k";
				//msg += "#L5##fUI/UIWindow8.img/EldaGauge/tooltip/17# 기어 업그레이드"+enter;
				cm.sendSimple(msg);
			break;
		}
	} else if (status == 2) {
		switch (seld) {
			case 1:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(9010106);
					break;
					case 2:
						cm.dispose();
						cm.openNpc(9010107);
					break;
					case 3:
						cm.dispose();
						cm.openNpc(3003162);
					break;
					case 4:
						cm.dispose();
						cm.openNpc(3003252);
					break;
					case 5:
						cm.dispose();
						cm.openNpc(3003480);
					break;
                                        case 6:
						cm.dispose();
						cm.openNpc(3003756);
					break;
				}
			break;
			case 2:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(2155000); 
					break;
					case 2:
						cm.dispose();
						cm.openNpc(3003104); 
					break;
					case 3:
						cm.dispose();
						cm.openNpc(3003162); 
					break;
					case 4:
						cm.dispose();
						cm.openNpc(3003252); 
					break;
					case 5:
						cm.dispose();
						cm.openNpc(3003480); 
					break;
                                        case 6:
						cm.dispose();
						cm.openNpc(3003756); 
					break;
                                        case 7:
						cm.dispose();
						cm.openNpc(1540895);
					break;
                                        case 8:
						cm.dispose();
						cm.openNpc(3003326);
					break;
                                        case 9:
						cm.dispose();
						cm.warp(100030301, 0);
					break;
case 11:
						cm.dispose();
						cm.openNpc(9000368);
					break;
				}
			break;
			case 3:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(3003166);
					break;
					case 2:
						cm.dispose();
                        cm.warp(450003770, 0);
					break;
					case 3:
						cm.dispose();
						cm.openNpc(3003381);
					break;
					case 4:
						cm.dispose();
						cm.warp(993017000);
					break;
					case 5:
						cm.dispose();
						cm.warp(993001000);
					break;
					case 6:
						cm.dispose();
						cm.openNpc(2052044); // 9062148 (전투력 측정실 ?)
					break;
					case 7:
						cm.dispose();
						cm.openNpc(9062550);
					break;
					case 8:
						cm.dispose();
						cm.openNpc(9062199);
					break;
					case 9:
						cm.dispose();
						cm.openNpc(9062572);
					break;
				}
			break;
			case 4:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(2008);
					break;
					case 2:
						cm.dispose();
						cm.openNpc(2008);
					break;
					case 3:
						cm.dispose();
						cm.openNpc(2008);
					break;
					case 4:
						cm.dispose();
						cm.openNpc(2008);
					break;
					case 5:
						cm.dispose();
						cm.openNpc(2008);
					break;
				}
			break;
			case 5:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(2010007);
					break;
					case 2:
						cm.dispose();
						cm.openNpc(2010009);
					break;
					case 3:
							cm.dispose();
						cm.warp(200000301, 1);
					break;
					case 4:
						cm.dispose();
						cm.openNpc(2008);
					break;
					case 5:
						cm.dispose();
						cm.openNpc(2008);
					break;
				}
			break;
                        case 6:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.warp(680000000, 1);
					break;
					case 2:
						cm.dispose();
						cm.openNpc(1031001);
					break;
					case 3:
						cm.dispose();
						cm.openNpc(9400340);
					break;
					case 4:
						cm.dispose();
						cm.openNpc(2008);
					break;
					case 5:
						cm.dispose();
						cm.openNpc(2008);
					break;
				}
			break;
                       case 7:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.warp(910130100, 0);
					break;
					case 2:
						cm.dispose();
						cm.warp(910530100, 0);
					break;
					case 3:
						cm.dispose();
						cm.warp(109040001, 0);
					break;
					case 4:
						cm.dispose();
						cm.warp(100000202, 0);
					break;
					case 5:
						cm.dispose();
						cm.warp(220000006, 0);
					break;
				}
			break;
                        case 8:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.warp(680000000, 1);
					break;
					case 2:
						cm.dispose();
						cm.openNpc(1540101);
					break;
					case 3:
						cm.dispose();
						cm.openNpc(9000381);
					break;
					case 4:
						cm.dispose();
						cm.openNpc(9000224);
					break;
					case 5:
						cm.dispose();
						cm.openNpc(9001153);
					break;
					case 6:
						cm.dispose();
						cm.openNpc(1540110);
					break;
					case 7:
						cm.dispose();
						cm.openNpc(2071000);
					break;
					case 8:
						cm.dispose();
						cm.openNpc(9000155);
				}
			break;
		}
	}
}
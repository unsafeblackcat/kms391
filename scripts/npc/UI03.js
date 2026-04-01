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
		var msg = "#fs11#어서오세요 #h0#님!\r\n여러가지 컨텐츠가 있다는거 알고 계시나요? 한번 이용해보세요.\r\n";
		msg += "#L6# #b어드벤처 드릴#n#l"
		//msg += "   #L10# #b유니온 기어#n#l";
		msg += "  #L1# #b레벨업 보상#n#l#l\r\n"
		msg += "#L25# #b각종    뽑기#n#l#l"
		//msg += "    #L2# #[PRAY] 도감#n#l\r\n\r\n"
		msg += "         #L23# #b창고   이동#n#l#l"
		//msg += "    #L2# #[PRAY] 도감#n#l\r\n\r\n"
		msg += "    #L24# #b무릉     도장#n#l"
		msg += "    #L2# #[PRAY] 도감#n#l\r\n\r\n"
		/*msg += "         #L2# #b갓 오브 컨트롤#n#k\r\n";
		msg += "#l\r\n\r\n시즌 이벤트\r\n";
								msg += "#L7# #b스플래시 링크#n#l         #L1# #b아이스 링크#n#k\r\n#k#l";
								msg += "#L8# #b덩케링크 링크#n#l         #L9# #b디스커버리 링크#n#k\r\n\r\n#k#l";
		msg += "#l\r\n아르카나 컨텐츠\r\n";
		msg += "#L3# #b스피릿 세이비어#n #k\r\n";
		msg += "#L4# #b배고픈 무토#n#l        #L5# #b드림 브레이커#n#k\r\n";*/
		cm.sendSimpleS(msg, 4);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 6:
				cm.dispose();
				cm.openNpc(9062147);
				break;

			case 2:
				cm.dispose();
				cm.openNpc(1540725);
				break;
			case 1:
				var msg = " #e#b [ 레벨업 보상 계정당 1회 수령]#k#n" + enter;
				msg += "#L200# 200 레벨 달성 보상받기 " + enter;
				msg += "#L210# 210 레벨 달성 보상받기 " + enter
				msg += "#L220# 220 레벨 달성 보상받기 " + enter
				msg += "#L230# 230 레벨 달성 보상받기 " + enter
				msg += "#L240# 240 레벨 달성 보상받기 " + enter
				msg += "#L250# 250 레벨 달성 보상받기 " + enter
				msg += "#L260# 260 레벨 달성 보상받기 " + enter
				msg += "#L270# 270 레벨 달성 보상받기 " + enter
				msg += "#L280# 280 레벨 달성 보상받기 " + enter
				msg += "#L290# 290 레벨 달성 보상받기 " + enter
				msg += "#L300# 300 레벨 달성 보상받기 " + enter


				cm.sendSimple(msg);
				break;
			case 4:
				cm.dispose();
				cm.warp(450002023);
				break;
			case 5:
				cm.dispose();
				cm.warp(450004000);
				break;

			case 7:
				cm.dispose();
				cm.warp(993018000);
				break;
			case 8:
				cm.dispose();
				cm.warp(993021200); // 덩케링크
				break;
			case 9:
				cm.dispose();
				cm.warp(993029200); //
				break;
			case 10:
				cm.dispose();
				cm.openNpc(2300001);
				break;
			case 13:
				cm.dispose();
				cm.openNpc(9000040);
				break;
			case 14:
				cm.dispose();
				cm.openNpc(3001652);
				break;
			case 20:
				cm.dispose();
				cm.openNpc(3003113);
				break;
			case 23:
				cm.dispose();
				cm.openNpc(3003113);
				break;
			case 15:
				cm.dispose();
				cm.warp(993014000);
				break;
			case 25:
				cm.dispose();
				cm.openNpc(1052224);
				break;
			case 24:
				cm.dispose();
				cm.warp(925020001);
				break;
		}
	} else if (status == 2) {
		switch (seld) {
			case 1:
				if (cm.getPlayer().getLevel() < sel) {

					cm.sendOk("보상 수령을 위한 레벨이 부족합니다.");
					cm.dispose();
					break;
				}
				switch (sel) { // 레벨업보상
					case 200: // 200레벨 달성시 아래아이템 지급(보관함으로 지급되며 계정당 1회지급됨)
                        if (cm.getPlayer().getClient().getKeyValue("level" + sel) == null) {
                            cm.getPlayer().getClient().setKeyValue("level1", "true");
                        }                                                                      
						if (cm.getPlayer().getClient().getKeyValue("level10") == null) {
							cm.getPlayer().gainCabinetItem(2048717, 10); // 영환불*/
							cm.getPlayer().gainCabinetItem(5060048, 1); // 애플
							cm.getPlayer().gainCabinetItem(2631527, 10); // 경코젬
							cm.getPlayer().gainCabinetItem(5062005, 10); // 어미큐
							cm.getPlayer().gainCabinetItem(5062503, 10); // 화에큐
							cm.getPlayer().gainCabinetItem(2450163, 3); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 1); // 10억
							cm.getPlayer().gainCabinetItem(2049371, 1); // 17성
							cm.getPlayer().gainCabinetItem(5121060, 1); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level10", "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 210: // 210레벨
                        if (cm.getPlayer().getClient().getKeyValue("level" + sel) != null) {
                            cm.getPlayer().getClient().setKeyValue("level2", "true");
                        }        
						if (cm.getPlayer().getClient().getKeyValue("level20") == null) {
							cm.getPlayer().gainCabinetItem(5060048, 1); // 애플
							cm.getPlayer().gainCabinetItem(2631527, 20); // 경코젬
							cm.getPlayer().gainCabinetItem(5062005, 15); // 어미큐
							cm.getPlayer().gainCabinetItem(5062503, 15); // 화에큐
							cm.getPlayer().gainCabinetItem(2450163, 3); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 1); // 10억
							cm.getPlayer().gainCabinetItem(2049371, 2); // 17성
							cm.getPlayer().gainCabinetItem(5121060, 2); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level20", "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 220:
                        if (cm.getPlayer().getClient().getKeyValue("level" + sel) !=  null) {
                            cm.getPlayer().getClient().setKeyValue("level3", "true");
                        }        
						if (cm.getPlayer().getClient().getKeyValue("level30") == null) {
							cm.getPlayer().gainCabinetItem(5060048, 1); // 애플
							cm.getPlayer().gainCabinetItem(2631527, 30); // 경코젬
							cm.getPlayer().gainCabinetItem(5062005, 20); // 어미큐
							cm.getPlayer().gainCabinetItem(5062503, 20); // 화에큐
							cm.getPlayer().gainCabinetItem(2450163, 5); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 2); // 10억
							cm.getPlayer().gainCabinetItem(2049371, 3); // 17성
							cm.getPlayer().gainCabinetItem(5121060, 3); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level30", "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 230:
                        if (cm.getPlayer().getClient().getKeyValue("level" + sel) != null) {
                            cm.getPlayer().getClient().setKeyValue("level4", "true");
                        }        
						if (cm.getPlayer().getClient().getKeyValue("level40") == null) {
							cm.getPlayer().gainCabinetItem(5060048, 1); // 애플
							cm.getPlayer().gainCabinetItem(2631527, 30); // 경코젬
							cm.getPlayer().gainCabinetItem(5062005, 25); // 어미큐
							cm.getPlayer().gainCabinetItem(5062503, 25); // 화에큐
							cm.getPlayer().gainCabinetItem(2450163, 7); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 3); // 10억
							cm.getPlayer().gainCabinetItem(2049371, 4); // 17성
							cm.getPlayer().gainCabinetItem(5121060, 5); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level40", "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 240:
                        if (cm.getPlayer().getClient().getKeyValue("level" + sel) != null) {
                            m.getPlayer().getClient().setKeyValue("level5", "true");
                        }        
						if (cm.getPlayer().getClient().getKeyValue("level50") == null) {
							cm.getPlayer().gainCabinetItem(5060048, 2); // 애플
							cm.getPlayer().gainCabinetItem(2631527, 30); // 경코젬
							cm.getPlayer().gainCabinetItem(5062005, 30); // 어미큐
							cm.getPlayer().gainCabinetItem(5062503, 30); // 화에큐
							cm.getPlayer().gainCabinetItem(2450163, 9); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 3); // 10억
							cm.getPlayer().gainCabinetItem(2049371, 4); // 17성
							cm.getPlayer().gainCabinetItem(5121060, 6); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level50", "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 250:
                        if (cm.getPlayer().getClient().getKeyValue("level" + sel) != null) {
                            cm.getPlayer().getClient().setKeyValue("level6", "true");
                        }        
						if (cm.getPlayer().getClient().getKeyValue("level60") == null) {
							cm.getPlayer().gainCabinetItem(5060048, 3); // 애플
							cm.getPlayer().gainCabinetItem(2631527, 30); // 경코젬
							cm.getPlayer().gainCabinetItem(5062005, 30); // 어미큐
							cm.getPlayer().gainCabinetItem(5062503, 30); // 화에큐
							cm.getPlayer().gainCabinetItem(2450163, 10); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 3); // 10억
							cm.getPlayer().gainCabinetItem(2049376, 1); // 20성
							cm.getPlayer().gainCabinetItem(5121060, 8); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level60", "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 260:
                        if (cm.getPlayer().getClient().getKeyValue("level" + sel) != null) {
                        cm.getPlayer().getClient().setKeyValue("level7", "true");
                        }        
						if (cm.getPlayer().getClient().getKeyValue("level70") == null) {
							cm.getPlayer().gainCabinetItem(5060048, 4); // 애플
							cm.getPlayer().gainCabinetItem(2631527, 30); // 경코젬
							cm.getPlayer().gainCabinetItem(5062005, 30); // 어미큐
							cm.getPlayer().gainCabinetItem(5062503, 30); // 화에큐
							cm.getPlayer().gainCabinetItem(2450163, 12); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 3); // 10억
							cm.getPlayer().gainCabinetItem(2049376, 1); // 17성
							cm.getPlayer().gainCabinetItem(5121060, 10); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level70", "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 270:
                        if (cm.getPlayer().getClient().getKeyValue("level" + sel) != null) {
                        m.getPlayer().getClient().setKeyValue("level8", "true");
                        }        
						if (cm.getPlayer().getClient().getKeyValue("level80") == null) {
							cm.getPlayer().gainCabinetItem(5060048, 4); // 애플
							cm.getPlayer().gainCabinetItem(2631527, 30); // 경코젬
							cm.getPlayer().gainCabinetItem(5062005, 30); // 어미큐
							cm.getPlayer().gainCabinetItem(5062503, 30); // 화에큐
							cm.getPlayer().gainCabinetItem(2450163, 13); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 8); // 10억
							cm.getPlayer().gainCabinetItem(2049376, 2); // 17성
							cm.getPlayer().gainCabinetItem(5121060, 12); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level80", "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 280:
                        if (cm.getPlayer().getClient().getKeyValue("level" + sel) != null) {
                        cm.getPlayer().getClient().setKeyValue("level9", "true");
                        }        
						if (cm.getPlayer().getClient().getKeyValue("level90") == null) {
							cm.getPlayer().gainCabinetItem(5060048, 5); // 애플
							cm.getPlayer().gainCabinetItem(2631527, 40); // 경코젬
							cm.getPlayer().gainCabinetItem(5062005, 40); // 어미큐
							cm.getPlayer().gainCabinetItem(5062503, 40); // 화에큐
							cm.getPlayer().gainCabinetItem(2450163, 20); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 9); // 10억
							cm.getPlayer().gainCabinetItem(2049376, 2); // 17성
							cm.getPlayer().gainCabinetItem(5121060, 15); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level90", "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 290:
                        if (cm.getPlayer().getClient().getKeyValue("level" + sel) != null) {
                            cm.getPlayer().getClient().setKeyValue("level10", "true");
                        }        
						if (cm.getPlayer().getClient().getKeyValue("level1000") == null) {
							cm.getPlayer().gainCabinetItem(5060048, 6); // 애플
							cm.getPlayer().gainCabinetItem(2631527, 40); // 경코젬
							cm.getPlayer().gainCabinetItem(5062005, 40); // 어미큐
							cm.getPlayer().gainCabinetItem(5062503, 40); // 화에큐
							cm.getPlayer().gainCabinetItem(2450163, 22); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 9); // 10억
							cm.getPlayer().gainCabinetItem(2049376, 2); // 17성
							cm.getPlayer().gainCabinetItem(5121060, 18); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level1000", "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 300:
                        if (cm.getPlayer().getClient().getKeyValue("level" + sel) != null) {
                             cm.getPlayer().getClient().setKeyValue("level11", "true");
                        }        
						if (cm.getPlayer().getClient().getKeyValue("level1100") == null) {
							cm.getPlayer().gainCabinetItem(5060048, 7); // 애플
							cm.getPlayer().gainCabinetItem(2631527, 50); // 경코젬
							cm.getPlayer().gainCabinetItem(5062005, 50); // 어미큐
							cm.getPlayer().gainCabinetItem(5062503, 50); // 화에큐
							cm.getPlayer().gainCabinetItem(2450163, 25); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 10); // 10억
							cm.getPlayer().gainCabinetItem(2049376, 2); // 17성
							cm.getPlayer().gainCabinetItem(2633914, 1); // 경뿌
							cm.getPlayer().gainCabinetItem(2633915, 5)
							cm.getPlayer().getClient().setKeyValue("level1100", "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
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
					case 9:
						cm.dispose();
						cm.openNpc(3003151);
						break;
					case 8:
						cm.dispose();
						cm.openNpc(3003381);
						break;
					case 10:
						cm.dispose();
						cm.warp(450004000, 0);
						break;
				}
				break;
			case 3:
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
			case 4:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(3003104);
						break;
					case 2:
						cm.dispose();
						cm.openNpc(3003161);
						break;
					case 3:
						cm.dispose();
						cm.openNpc(3003209);
						break;
					case 4:
						cm.dispose();
						cm.openNpc(3003322);
						break;
					case 5:
						cm.dispose();
						cm.openNpc(3003472);
						break;
					case 6:
						cm.dispose();
						cm.openNpc(3004202);
						break;
				}
				break;
			case 5:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc("guild_proc");
						break;
					case 2:
						cm.dispose();
						cm.openNpc(2010009);
						break;
					case 3:
						cm.dispose();
						cm.openNpc(1540107);
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
						cm.openNpc(2470039);
						break;
					case 4:
						cm.dispose();
						cm.openNpc(9000224);
						break;
					case 5:
						cm.dispose();
						cm.openNpc(9001153);
						break;
				}
				break;
		}
	}
}
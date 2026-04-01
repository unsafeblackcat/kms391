/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

importPackage(Packages.client.inventory);
importPackage(Packages.constants);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

아이콘 = "#fUI/GuildMark.img/BackGround/00001026/1#"

메소 = 10000000

status = 0;
operation = -1;
select = -1;

invs = Array(1, 5);

var invv;
var selected;

slot_1 = Array();
slot_2 = Array();

var statsSel;
var sel;
var name;

banitem = new Array(4310036, 4310038, 4310210, 4001129, 4033884, 4430000, 4001130, 4001131, 4001132, 4001133, 4001134, 4001135, 4031039, 4031040, 4031041, 4031025, 4031026, 4031028, 4031020, 4031032, 4032692, 1162040, 3018283, 4034934, 4034927, 4036331, 4036334, 4036402, 4036407, 1143013, 1143014, 1143015, 1143016, 1143017, 1143018, 1143019, 1143020, 1142949, 1142655, 1142142, 4310227, 4032125, 4001159, 4001623, 4001324, 1702551, 1002839, 5064000, 5064100, 5064300, 1102348, 1002728, 1012161, 1142580, 1002720, 1012007, 1112937);
//4033884 - 침입퀘 전리품
//4430000 - 골드비치 물품
//4001130 ~ 4001135 - 로미오 줄리엣 아이템
//4031039, 4031040, 4031041 - 슈미 돈
//4031025, 4031026, 4031028 - 비올라
//4031020 - 핑크 안시리움, 4031032 - 두 뿌리 홍삼
//4032692 - 아도비스의 보따리
//1182263 - V 페스티벌 뱃지
//1162040 - 쥐 부적
//1712000, 1712001, 1712002, 1712003, 1712004, 1712005, 1712006 - 아케인심볼
//3018283 - 어드벤처 의자
//4034934, 4034927, 4036331, 4036334, 4036402, 4036407 - 연합퀘 아이템
//1143013, 1143014, 1143015, 1143016, 1143017, 1143018, 1143019, 1143020 - 드림브레이커 훈장
//1142949, 1142655, 1142142 - 낚시 훈장, 한가위 훈장, 파라오 훈장
//4310227 - 드림 코인, 4032125 - 증표
//4001159 - 로미오줄리엣 보상템
//4001623 - 네트 에메랄드, 4001324 - 예티 에메랄드
//1702551 - 한가위 감나무 가지 무기
//1002839 - 호박 모자
//5062009, 5062010, 5062500, 5064000, 5064100, 5064300 - 큐브, 실드
//1102348, 1002728, 1012161, 1142580, 1002720, 1012007 - 크리스마스 이벤트 장비
//1112937 - 졸려요 반지

banitem2 = new Array(1142573, 1114304, 1114240, 1182069, 1115012, 1115013, 1115014, 1115100, 1115101, 1115102, 2436402, 2435224, 2435458, 5000930, 5000931, 5000932, 2438326, 2438327, 2438328)
//1142573 - 조건 훈장
//1114304, 1114240 - 후원 링A, 후원 링B
//1182069 - 홍보왕 뱃지
//1115012, 1115013, 1115014, 1115100, 1115101, 1115102 - MVP 반지
//2436402 - 닉변권
//2435224 - 후원 검색 캐시 이용권
//2435458, 5060048, 5068300 - 골드애플 조각,골드애플,원더베리
//5000930, 5000931, 5000932 - 자석펫
//2438326, 2438327, 2438328 - MVP 상자

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

			말 = 아이콘+"#fs15##fn나눔고딕 Extrabold# #r"+ServerConstants.serverName+" #bSend Item#k#fs15##fn굴림체#\r\n───────────────────────────\r\n\r\n";
			말 +="#fs15#* 선물에는 1 천만 메소가 차감됩니다.\r\n";
			말 +="* 선물로 인하여 발생하는 결과는 책임지지 않습니다.\r\n";
			말 +="* 선물전에 반드시 아래의 사항을 체크하세요.\r\n\r\n";
			말 +="#d1. 상대방의 인벤토리 여유 공간\r\n";
			말 +="2. #r고유 아이템 #d선물시 상대방 보유 여부\r\n";
			말 +="3. 상대방이 같은 채널인지 확인#k\r\n\r\n";
			말 +="#e▶ 선물하실 아이템 종류를 선택해주세요.#n\r\n";
			말 +="#L1##b[장비]#l";
			말 +="#L2#[소비]#l";
			말 +="#L4#[기타]#l";
			말 +="#L3#[설치]#l";
			말 +="#L5#[캐시]#l";
			말 +="#L6#[치장]#l";
			cm.sendSimple(말);

	} else if (status == 1) {
			operation = selection;
			if (operation == 1) {
				type = MapleInventoryType.EQUIP;
				타입 = "장비"
				yes = 1;
			} else if (operation == 2) {
				type = MapleInventoryType.USE;
				타입 = "소비"
				yes = 2;
			} else if (operation == 4) {
				type = MapleInventoryType.SETUP;
				타입 = "설치"
				yes = 4;
			} else if (operation == 3) {
				type = MapleInventoryType.ETC;
				타입 = "기타"
				yes = 3;
			} else if (operation == 5) {
				type = MapleInventoryType.CASH;
				타입 = "캐시"
				yes = 5;
			} else if (operation == 6) {
				type = MapleInventoryType.DRESS;
				타입 = "치장"
				yes = 6;
			}
			if (selection >= 1 && selection <=6) {
				cm.sendGetText("#fs15##b1. 선물받으실 분의 닉네임을 입력해주세요.");
			}
	} else if (status == 2) {
			if (operation == 1) {
				type = MapleInventoryType.EQUIP;
				타입 = "장비"
			} else if (operation == 2) {
				type = MapleInventoryType.USE;
				타입 = "소비"
			} else if (operation == 3) {
				type = MapleInventoryType.SETUP;
				타입 = "설치"
			} else if (operation == 4) {
				type = MapleInventoryType.ETC;
				타입 = "기타"
			} else if (operation == 5) {
				type = MapleInventoryType.CASH;
				타입 = "캐시"
			} else if (operation == 6) {
			type = MapleInventoryType.DRESS;
			타입 = "치장"
		}
				item = cm.getChar().getInventory(type);
		text = cm.getText();
		conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(text);
		if (conn == null) {
			cm.sendOk("#fs15##r현재 접속중이 아니거나 채널이 다릅니다.\r\n또는 존재하지 않는 닉네임일 수도 있습니다.");
			cm.dispose();
		} else {
		ok = false;
		selStr = "#fs15##b2. 어떤 아이템을 선물 하시겠습니까.?\r\n";
		for (x = 1; x < 2; x++) {
			inv = cm.getInventory(yes);
			for (i = 0; i <= cm.getInventory(yes).getSlotLimit(); i++) {
				if (x == 0) {
					slot_1.push(i);
				} else {
					slot_2.push(i);
				}
				it = inv.getItem(i);
				if (it == null) {
					continue;
				}
				itemid = it.getItemId();
				ok = true;
				selStr += "#L" + (yes * 1000 + i) + "##v" + itemid + "# #d#t" + itemid + "##l\r\n";
			}
		}
		if (!ok) {
			cm.sendOk("#fs15##r인벤토리에 아이템이 존재하지 않습니다.");
			cm.dispose();
		}
		cm.sendSimple(selStr + "#k");
		}
	} else if (status == 3) {
		sel = selection;
		if (operation == 1) {
			type = MapleInventoryType.EQUIP;
			타입 = "장비"
		} else if (operation == 2) {
			type = MapleInventoryType.USE;
			타입 = "소비"
		} else if (operation == 3) {
			type = MapleInventoryType.SETUP;
			타입 = "설치"
		} else if (operation == 4) {
			type = MapleInventoryType.ETC;
			타입 = "기타"
		} else if (operation == 5) {
			type = MapleInventoryType.CASH;
			타입 = "캐시"
		} else if (operation == 6) {
			type = MapleInventoryType.DRESS;
			타입 = "치장"
		}
		item = cm.getChar().getInventory(type).getItem(selection%1000).copy();
		text = cm.getText();
		invv = selection / 1000;
		inzz = cm.getInventory(invv);
		selected = selection % 1000;
		if (invv == invs[0]) {
			statsSel = inzz.getItem(slot_1[selected]);
		} else {
			statsSel = inzz.getItem(slot_2[selected]);
		}
		if (statsSel == null) {
			cm.sendOk("#fs15##r오류입니다. 운영자에게 보고해주세요.");
			cm.dispose();
		}
		text = cm.getText();
		con = cm.getClient().getChannelServer().isMyChannelConnected(text);
		conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(text);
		leftslot = conn.getClient().getPlayer().getInventory(type).getNumFreeSlot();
		if (leftslot < 1) {
			cm.sendOk("#fs15##r상대방의 "+타입+" 인벤토리 공간이 최소한 1 칸은 필요합니다.");
			cm.dispose();
			return;
		}
		if (item.getQuantity() == 1) {
			isban = false;
			for (i = 0; i < banitem.length; i++) {
				if(banitem[i] == item.getItemId())
				isban = true;
			}
			for (i = 0; i < banitem2.length; i++) {
				if(banitem2[i] == item.getItemId())
				isban = true;
			}
			if (isban) {
				cm.sendOk("#fs15##r해당 아이템은 선물이 제한된 품목입니다.");
				cm.dispose();
				return;
			}
			if (cm.getMeso()>=메소) {
				/*if (item.getItemId() != 1115012 && item.getItemId() != 1115013 && item.getItemId() != 1115014 && item.getItemId() != 1115100 && item.getItemId() != 1115101 && item.getItemId() != 1115102) {
					if (cm.isCash(item.getItemId()) && (item.getStr() >= 1 || item.getDex() >= 1 || item.getInt() >= 1 || item.getLuk() >= 1 || item.getMatk() >= 1 || item.getWatk() >= 1 || Item.getHp() >= 1)) {
						cm.sendOk("#fs15##r스탯이 부여된 캐시 장비는 선물하실 수 없습니다.");
						cm.dispose();
						return;
					}
				}*/
			//	if (GameConstants.isPet(item.getItemId()) == false) {
					if (cm.getPlayer().getName() != text) {
						MapleInventoryManipulator.removeFromSlot(cm.getC(), type, selection%1000, item.getQuantity(), true);
						MapleInventoryManipulator.addFromDrop(conn.getClient(), item, true);
						conn.getClient().getSession().write(CField.getGameMessage(10, "[선물] "+cm.getPlayer().getName()+"님에게 "+MapleItemInformationProvider.getInstance().getName(item.getItemId())+" 을(를) 선물받으셨습니다. 인벤토리를 확인해보세요."));
						cm.sendOk("#fs15##b"+text + "#k 님에게 #i"+item.getItemId()+"# #d(#t"+item.getItemId()+"#)#k 을(를) 보냈습니다.");
						cm.gainMeso(-메소);
						cm.dispose();
					} else {
						cm.sendOk("#fs15##r자기 자신에게는 선물할 수 없습니다.");
						cm.dispose();
					}
			//	} else {
			//		cm.sendOk("#fs15##r펫은 선물할 수 없습니다.");
			//		cm.dispose();
			//	}
			} else {
				cm.sendOk("#fs15##r선물을 위한 메소가 부족합니다.");
				cm.dispose();
			}
		} else {
			cm.sendGetNumber("#fs15##b3. 총 몇 개를 선물 하시겠습니까.?#k\r\n현재 소지중인 #i"+item.getItemId()+"# #d(#t"+item.getItemId()+"#)#k 개수 : #b"+item.getQuantity()+"",1,1,item.getQuantity());
		}
		name = text;
	} else if (status==4) {
		sele = selection%1000;
		quan = cm.getText();
		if (operation == 1) {
			type = MapleInventoryType.EQUIP;
			타입 = "장비"
		} else if (operation == 2) {
			type = MapleInventoryType.USE;
			타입 = "소비"
		} else if (operation == 3) {
			type = MapleInventoryType.SETUP;
			타입 = "설치"
		} else if (operation == 4) {
			type = MapleInventoryType.ETC;
			타입 = "기타"
		} else if (operation == 5) {
			type = MapleInventoryType.CASH;
			타입 = "캐시"
		} else if (operation == 6) {
			type = MapleInventoryType.DRESS;
			타입 = "치장"
		}
		item = cm.getChar().getInventory(type).getItem(sel%1000).copy();
		text = cm.getText();
		invv = sel / 1000;
		inzz = cm.getInventory(invv);
		selected = sel % 1000;
		if (invv == invs[0]) {
			statsSel = inzz.getItem(slot_1[selected]);
		} else {
			statsSel = inzz.getItem(slot_2[selected]);
		}
		if (statsSel == null) {
			cm.sendOk("#fs15##r오류입니다. 운영자에게 보고해주세요.");
			cm.dispose();
			return;
		}

		text = selection;
		con = cm.getClient().getChannelServer().isMyChannelConnected(name);
		conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(name);
		leftslot = conn.getClient().getPlayer().getInventory(type).getNumFreeSlot();
		if (leftslot < 1) {
			cm.sendOk("#fs15##r상대방의 "+타입+" 인벤토리 공간이 최소한 1 칸은 필요합니다.");
			cm.dispose();
			return;
		}
		if (text != 0xFFFF && text <= 32767 && text <= 0x7FFF && item.getQuantity() >= text && text > 0 && text > -1 && selection != 0xFFFF && selection <= 32767 && selection <= 0x7FFF && item.getQuantity() >= selection && selection > 0 && selection > -1) {
			isban = false;
			for (i = 0; i < banitem.length; i++) {
				if(banitem[i] == item.getItemId())
				isban = true;
			}
			for (i = 0; i < banitem2.length; i++) {
				if(banitem2[i] == item.getItemId())
				isban = true;
			}
			if (isban) {
				cm.sendOk("#fs15##r해당 아이템은 선물이 제한된 품목입니다.");
				cm.dispose();
				return;
			}
			if (cm.getMeso()>=메소){
				/*if (item.getItemId() != 1115012 && item.getItemId() != 1115013 && item.getItemId() != 1115014 && item.getItemId() != 1115100 && item.getItemId() != 1115101 && item.getItemId() != 1115102) {
					if (cm.isCash(item.getItemId()) && (item.getStr() >= 1 || item.getDex() >= 1 || item.getInt() >= 1 || item.getLuk() >= 1 || item.getMatk() >= 1 || item.getWatk() >= 1 || Item.getHp() >= 1)) {
						cm.sendOk("#fs15##r스탯이 부여된 캐시 장비는 선물하실 수 없습니다.");
						cm.dispose();
						return;
					}
				}*/
				if (cm.getPlayer().getName() != name) {
					item.setQuantity(text);
					MapleInventoryManipulator.removeFromSlot(cm.getC(), type, sel%1000, item.getQuantity(), true);
					MapleInventoryManipulator.addFromDrop(conn.getClient(), item, true);
					conn.getClient().getSession().write(CField.getGameMessage(10, "[선물] "+cm.getPlayer().getName()+"님에게 "+MapleItemInformationProvider.getInstance().getName(item.getItemId())+" "+item.getQuantity()+" 개 를 선물받으셨습니다. 인벤토리를 확인해보세요."));
					cm.sendOk("#fs15##b"+name + "#k 님에게 #i"+item.getItemId()+"# #d(#t"+item.getItemId()+"#) #b"+item.getQuantity()+" 개#k 를 보냈습니다.");
					cm.gainMeso(-메소);
					cm.dispose();
				} else {
					cm.sendOk("#fs15##r자기 자신에게는 선물할 수 없습니다.");
					cm.dispose();
				}
			} else {
				cm.sendOk("#fs15##r선물을 위한 메소가 부족합니다.");
				cm.dispose();
			}
		} else {
			cm.sendOk("#fs15##r수량이 부족하거나, 입력된 값이 잘못되었습니다.");
			cm.dispose();
		}

	}
}
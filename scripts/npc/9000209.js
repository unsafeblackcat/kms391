var status = -1;

importPackage(Packages.constants);

function start() {
	var aa = cm.itemQuantity(4033302); // 자쿰의 돌조각
	var ab = cm.itemQuantity(4033303); // 자쿰의 돌조각
	var ac = cm.itemQuantity(1142065); // 자쿰의 돌조각
	    if (aa >= 1 && ab >= 1 && ac >= 1){
		var sel = "\r\n#fUI/UIWindow2.img/UtilDlgEx/list3#\r\n\r\n#r[일괄 지급]#k #i4001715# #b1억 메소#k\r\n #r3 개 -  일괄 지급#k\r\n  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\r\n\r\n#r[선택 지급]#k #i1142066# #b#z1142066##k\r\n                                #r올스탯 60 / 공,마 35#k\r\n#L1##d*└ 해당 보상으로 선택하겠습니다.#k#l\r\n\r\n"; /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\r\n\r\n#r[선택 지급]#k #i1102871# #b#z1102871##k\r\n                                #r올스탯 100 / 공, 마 100 / 업횟 6#k\r\n#L2##d*└ 해당 보상으로 선택하겠습니다.#k#l\r\n\r\n  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\r\n\r\n#r[선택 지급]#k #i1082447# #b#z1082447##k\r\n                                #r올스탯 100 / 공, 마 100 / 업횟 4#k\r\n#L3##d*└ 해당 보상으로 선택하겠습니다.#k#l\r\n\r\n  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\r\n\r\n#r[선택 지급]#k #i1132296# #b#z1132296##k\r\n                                #r올스탯 20 / 공, 마 30 / 업횟 2#k\r\n#L4##d*└ 해당 보상으로 선택하겠습니다.#k#l\r\n\r\n\r\n*/
		} else {
		sel = "\r\n#fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#L0##r아직 뉴비가 아니 란 걸 증명하지못하겠어.....#k#l\r\n\r\n\r\n";
            }
	    if (cm.getPlayer().getLevel() >= 160 && cm.getPlayer().getMapId() == 100030301) {
            var chat = "#fn나눔고딕 Extrabold#안녕.? 아이테르서버 뉴비..?\r\n";
	    chat += "아직도 뉴비 훈장을 달고다니네?..\r\n"; 
	    chat += "뉴비가 아니란걸 증명한다면..\r\n";
	    chat += "아래 훈장을 업그레이드 해줄께!..\r\n";
	    chat += "아래 아이템을 가지고와서 뉴비가아닌란걸 증명해보라구!!\r\n";
            chat += "\r\n--------------------------------------------------------------------------------\r\n";
	    chat += "#L100##fs15##r[보상]#k #b뉴비 훈장 해제 아이템 목록 보상 확인하기#k#l#fs15#\r\n";
	    chat += "\r\n--------------------------------------------------------------------------------\r\n";
            chat += "                   #d[뉴비 훈장 해제 필요 아이템 목록]#k\r\n";
            chat += "--------------------------------------------------------------------------------\r\n";
	    chat += "\r\n#i4033302# #z4033302# (하드 자쿰 드롭) #d("+aa+"/1)#k\r\n";
		chat += "#i4033303# #z4033303# (하드 혼테일 드랍) #d("+ab+"/1)#k\r\n";
		chat += "#i1142065# #z1142065# (초기 지급) #d("+ac+"/1)#k\r\n";
	    chat += "--------------------------------------------------------------------------------\r\n";
	    chat += ""+sel+"";
	    chat += "--------------------------------------------------------------------------------";
	    cm.sendSimple(chat);
	} else {
	cm.sendOk("#fn나눔고딕 Extrabold##r* 플레이 조건#k\r\n\r\n#d- 레벨 160 이상 의 캐릭터\r\n- 퀘스트의 전당 에서 플레이 가능#k",9062004);
	cm.dispose();
        }
}
function action(mode, type, selection) {
        cm.dispose();
	if (selection == 0) {
		         cm.sendOk("#fn나눔고딕 Extrabold##r너는.. 뉴비가 아니란걸 증명하지못했어!#k");
		         cm.dispose();
	} else if (selection == 100) {
	    cm.sendOk("#fn나눔고딕 Extrabold##r[일괄 지급]#k #i4001715# #b1억 메소#k\r\n #r3 개 -  일괄 지급#k\r\n#r올스텟 60 공마 35 #k #i1142066# #b#z1142066##k\r\n");                                /*#r올스탯 20 / 공, 마 30 / 업횟 8#k\r\n#r[선택 지급]#k #i1102871# #b#z1102871##k\r\n");                               #r올스탯 20 / 공, 마 30 / 업횟 6#k\r\n#r[선택 지급]#k #i1082447# #b#z1082447##k\r\n                                #r올스탯 20 / 공, 마 30 / 업횟 4#k\r\n#r[선택 지급]#k #i1132296# #b#z1132296##k\r\n                                #r올스탯 20 / 공, 마 30 / 업횟 2#k");*/
	} else if (selection == 1) {
	    if (cm.haveItem(4033302, 1 && cm.haveItem(4033303, 1) && cm.haveItem(1142065, 1))) {
		 if (cm.canHold(4001715) && cm.canHold(1142066)) {
			 cm.sendOk("#fn나눔고딕 Extrabold#너는이제 뉴비가 아니군!!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i4001715# #b1억 메소#k #r3 개#k\r\n#i1142066# #b#z1142066##k #r(올스탯 60 / 공, 마 35)#k");
			 cm.gainItem(4033302, -1);
			 cm.gainItem(4033303, -1);
			 cm.gainItem(1142065, -1);
			 cm.gainItem(4001715, 3);
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1142066);
	item.setStr(60);
	item.setDex(60);
	item.setInt(60);
	item.setLuk(60);
	item.setWatk(35);
	item.setMatk(35);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
	                 cm.showEffect(false,"monsterPark/clear");
                         cm.playSound(false,"Field.img/Party1/Clear");
			 cm.dispose();
		} else {		         
			 cm.sendOk("#fn나눔고딕 Extrabold##r기타 또는 장비창에 빈 공간 이 없습니다.#k");
		         cm.dispose();	
			}
	    } else {		         
		 cm.sendOk("#fn나눔고딕 Extrabold##r너는.. 뉴비가아니란걸증명하지못했어!#k");
		 cm.dispose();	
		 }/*
	} else if (selection == 2) {
	    if (cm.haveItem(4033302, 30)) {
		 if (cm.canHold(4001715) && cm.canHold(1102871)) {
			 cm.sendOk("#fn나눔고딕 Extrabold#성공적으로 자쿰 토벌을 마치셨군요.!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i4001715# #b1억메소#k #r3 개#k\r\n#i1102871# #b#z1102871##k #r(올스탯 20 / 공, 마 30 / 업횟 6)#k");
			 cm.gainItem(4033302, -30);
			 cm.gainItem(4001715, 3);
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1102871);
	item.setStr(30);
	item.setDex(30);
	item.setInt(30);
	item.setLuk(30);
	item.setWatk(30);
	item.setMatk(30);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
	                 cm.showEffect(false,"monsterPark/clear");
                         cm.playSound(false,"Field.img/Party1/Clear");
			 cm.dispose();
		} else {		         
			 cm.sendOk("#fn나눔고딕 Extrabold##r기타 또는 장비창에 빈 공간 이 없습니다.#k");
		         cm.dispose();	
			}
	    } else {		         
		 cm.sendOk("#fn나눔고딕 Extrabold##r너는.. 뉴비가아니란걸증명하지못했어!#k");
		 cm.dispose();	
		 }
	} else if (selection == 3) {
	    if (cm.haveItem(4033302, 30)) {
		 if (cm.canHold(4001715) && cm.canHold(1082447)) {
			 cm.sendOk("#fn나눔고딕 Extrabold#성공적으로 자쿰 토벌을 마치셨군요.!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i4001715# #b1억메소#k #r3 개#k\r\n#i1082447# #b#z1082447##k #r(올스탯 20 / 공, 마 30 / 업횟 4)#k");
			 cm.gainItem(4033302, -30);
			 cm.gainItem(4001715, 3);
			 	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1082447);
	item.setStr(30);
	item.setDex(30);
	item.setInt(30);
	item.setLuk(30);
	item.setWatk(30);
	item.setMatk(30);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
	                 cm.showEffect(false,"monsterPark/clear");
                         cm.playSound(false,"Field.img/Party1/Clear");
			 cm.dispose();
		} else {		         
			 cm.sendOk("#fn나눔고딕 Extrabold##r기타 또는 장비창에 빈 공간 이 없습니다.#k");
		         cm.dispose();	
			}
	    } else {		         
		 cm.sendOk("#fn나눔고딕 Extrabold##r너는.. 뉴비가아니란걸증명하지못했어!#k");
		 cm.dispose();	
		 }
	} else if (selection == 4) {
	    if (cm.haveItem(4033302, 30)) {
		 if (cm.canHold(4001715) && cm.canHold(1132296)) {
			 cm.sendOk("#fn나눔고딕 Extrabold#성공적으로 자쿰 토벌을 마치셨군요.!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i4001715# #b1억 메소#k #r3 개#k\r\n#i1132296# #b#z1132296##k #r(올스탯 20 / 공, 마 30 / 업횟 2)#k");
			 cm.gainItem(4033302, -30);
			 cm.gainItem(4001715, 3);
			 			 	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1132296);
	item.setStr(30);
	item.setDex(30);
	item.setInt(30);
	item.setLuk(30);
	item.setWatk(30);
	item.setMatk(30);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
	                 cm.showEffect(false,"monsterPark/clear");
                         cm.playSound(false,"Field.img/Party1/Clear");
			 cm.dispose();
		} else {		         
			 cm.sendOk("#fn나눔고딕 Extrabold##r기타 또는 장비창에 빈 공간 이 없습니다.#k");
		         cm.dispose();	
			}
	    } else {		         
		 cm.sendOk("#fn나눔고딕 Extrabold##r너는.. 뉴비가아니란걸증명하지못했어!#k");
		 cm.dispose();	
		 }
	} else {
	cm.sendOk("#fn나눔고딕 Extrabold##b토벌 대원으로서의 크나 큰 자부심을..!#k");
	cm.dispose();*/		
}
}

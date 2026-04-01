var status = -1;

importPackage(Packages.constants);

function start() {
	var aa = cm.itemQuantity(4310229); // 자쿰의 돌조각
	    if (aa >= 30){
		var sel = "\r\n#fUI/UIWindow2.img/UtilDlgEx/list3#\r\n\r\n#r#k\r\n                               #r#k\r\n  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\r\n\r\n#r[선택 지급]#k #i4001877# #b#z4001877##k\r\n                                #r#L1##d*└ 해당 보상으로 선택하겠습니다.#k#l\r\n\r\n  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\r\n\r\n#r[선택 지급]#k #i4031569# #b#z4031569##k\r\n                                #r#L2##d*└ 해당 보상으로 선택하겠습니다.#k#l\r\n\r\n  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\r\n\r\n#r[선택 지급]#k #i4036315# #b#z4036315##k\r\n                                #r#L3##d*└ 해당 보상으로 선택하겠습니다.#k#l\r\n\r\n  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\r\n\r\n#r[선택 지급]#k #i4033644# #b#z4033644##k\r\n                                #r#L4##d*└ 해당 보상으로 선택하겠습니다.#k#l\r\n\r\n\r\n";
		} else {
		sel = "\r\n#fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#L0##r아직 자쿰 토벌을 완수하지 못하였습니다.#k#l\r\n\r\n\r\n";
            }
	    if (cm.getPlayer().getLevel() >= 160 && cm.getPlayer().getMapId() == 100030301) {
            var chat = "#fn나눔고딕 Extrabold#안녕하세요.? 보스재료를 구하러오셧나봐요..?\r\n";
	    chat += "더좋은 육성을 위해 보스 재료를 제작할수있어요!\r\n"; 
	    chat += "아래 해당 재료를 구해오면 원하는 입장 재료를 지급해드릴께요!\r\n";
	    //chat += "자쿰 개체 수를 조정해야 그나마 지낼 수 있는 지경이지요..\r\n";
	    //chat += "당신도..토벌 대원으로서 길을 걸어보시겠어요..?\r\n";
            chat += "\r\n--------------------------------------------------------------------------------\r\n";
	    chat += "#L100##fs15##r[보상]#k #b재작가능 입장 재료 확인하기#k#l#fs15#\r\n";
	    chat += "\r\n--------------------------------------------------------------------------------\r\n";
            chat += "                   #d[보스 입장 재료 목록]#k\r\n";
            chat += "--------------------------------------------------------------------------------\r\n";
	    chat += "\r\n#i4310229# #z4310229# (전체필드 몬스터 드랍)             #d("+aa+"/30000)#k\r\n\r\n";
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
		         cm.sendOk("#fn나눔고딕 Extrabold##r아직은.. 자쿰 토벌이 부족하신 것 같네요..#k");
		         cm.dispose();
	} else if (selection == 100) {
	    cm.sendOk("#fn나눔고딕 Extrabold##r#k\r\n#r#k\r\n#r[선택 지급]#k #i4001877# #b#z4001877##k\r\n#r[선택 지급]#k #i4031569# #b#z4031569##k\r\n#r#r[선택 지급]#k #i4036315# #b#z4036315##k\r\n#r#r[선택 지급]#k #i4033644# #b#z4033644##k\r\n");
	} else if (selection == 1) {
	    if (cm.haveItem(4310229, 30000)) {
		 if (cm.canHold(4001877) && cm.canHold(4001877)) {
			 cm.sendOk("#fn나눔고딕 Extrabold#성공적으로 재료를 구하 셨군요!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i4001877# #b#z4001877##k #r#k");
			 cm.gainItem(4310229, -30000);
			 cm.gainItem(4001877, 1);
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(4001877);
	item.setStr(30);
	item.setDex(30);
	item.setInt(30);
	item.setLuk(300);
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
		 cm.sendOk("#fn나눔고딕 Extrabold##r아직은.. 자쿰 토벌이 부족하신 것 같네요..#k");
		 cm.dispose();	
		 }
	} else if (selection == 2) {
	    if (cm.haveItem(4310229, 30000)) {
		 if (cm.canHold(4031569) && cm.canHold(4031569)) {
			 cm.sendOk("#fn나눔고딕 Extrabold#성공적으로 재료를 구하 셨군요!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i4031569# #b#z4031569##k #r#k");
			 cm.gainItem(4310229, -30000);
			 cm.gainItem(4031569, 1);
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(4031569);
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
		 cm.sendOk("#fn나눔고딕 Extrabold##r아직은.. 자쿰 토벌이 부족하신 것 같네요..#k");
		 cm.dispose();	
		 }
	} else if (selection == 3) {
	    if (cm.haveItem(4310229, 30000)) {
		 if (cm.canHold(4036315) && cm.canHold(4036315)) {
			 cm.sendOk("#fn나눔고딕 Extrabold#성공적으로 재료를 구하 셨군요!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i4036315# #b#z4036315##k #r#k");
			 cm.gainItem(4310229, -30000);
			 cm.gainItem(4036315, 1);
			 	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(4036315);
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
		 cm.sendOk("#fn나눔고딕 Extrabold##r아직은.. 자쿰 토벌이 부족하신 것 같네요..#k");
		 cm.dispose();	
		 }
	} else if (selection == 4) {
	    if (cm.haveItem(4310229, 30000)) {
		 if (cm.canHold(4033644) && cm.canHold(4033644)) {
			 cm.sendOk("#fn나눔고딕 Extrabold#성공적으로 재료를 구하 셨군요!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i4033644# #b#z4033644##k #r#k");
			 cm.gainItem(4310229, -30000);
			 cm.gainItem(4033644, 1);
			 			 	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(4033644);
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
		 cm.sendOk("#fn나눔고딕 Extrabold##r아직은.. 자쿰 토벌이 부족하신 것 같네요..#k");
		 cm.dispose();	
		 }
	} else {
	cm.sendOk("#fn나눔고딕 Extrabold##b토벌 대원으로서의 크나 큰 자부심을..!#k");
	cm.dispose();		
}
}

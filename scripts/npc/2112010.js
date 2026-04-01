importPackage(Packages.tools.packet);

function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
		if((cm.getPlayer().getEventInstance().getProperty("stage6") == null || cm.getPlayer().getEventInstance().getProperty("stage6") == 0) && cm.getPlayer().getMapId() == 926100203){
			cm.getPlayer().getEventInstance().setProperty("stage6", 1);
			cm.getPlayer().getMap().broadcastMessage(CField.startMapEffect("모든 몬스터를 퇴치해 주세요.!", 5120021, true));
			if(checkPos){
				cm.sendOk("누구냐! 내 연구실에 침입한 놈들이!! 내연구는 아무에게도 넘겨줄 수 없다!!");
				for (i = 0; i < cm.getPlayer().getParty().getMembers().size(); ++i) {
					mem = cm.getPlayer().getParty().getMembers().get(i).getName();
					conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(mem);
					conn.getPlayer().dropMessageGM(6, "정체를 알 수 없는 과학자가 몬스터를 불러내고 황급히 사라졌다.");
				}
				spawn();
			} else {
				cm.getPlayer().getEventInstance().setProperty("Frankenstein", 1);
				cm.sendOk("(유레테의 중얼거림이 들린다.)\r\n"
				+"크크... 제뉴미스트와 알카드노 놈들, 내 연구성과를 알면 깜짝 놀랄테지. 그 멍청한 놈들에게 연금술과 기계공학으로"
				+"만들어낸 내 최고의 창조물을 보여주면, 놈들도 나를 존경하게 되곘지. 크..크크");
				for (i = 0; i < cm.getPlayer().getParty().getMembers().size(); ++i) {
					mem = cm.getPlayer().getParty().getMembers().get(i).getName();
					conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(mem);
					conn.getPlayer().dropMessageGM(6, "유레테의 중얼거림을 들었다.");
				}
				spawn();
			}
			cm.dispose();
			return;
		} else if(cm.getPlayer().getMapId() == 926100401){
			var msg = ".. .오랜 기다림 끝에... 내 연구를 인정하지 않는 마가티아의 멍청한 놈들에게 진정한 과학이 뭔지 보여줄 수 있는 때가 왔도다!!"
			msg += "응? 너희는!? 제뉴미스트와 알카드노의 개인가?"
			cm.sendNext(msg);
		} else {
			cm.dispose();
			return;
		}
	} else if(St == 1) {
		var msg = "큭큭큭.. 이것도 괜찮지. 내 연구의 희생물이 되기에 딱 좋아. 영광으로 생각하게나. 자네들이야 말로 최고의 기계공학과 연금술이 결합되는 것을 보는거니까 말이야!!"
		if(cm.getPlayer().getEventInstance().getProperty("Frankenstein") == 1){
			msg+"\r\n\r\n"	
			msg+"#L1#멈춰요! 당신때문에 마가티아는 전쟁 일보 직전이에요!!"
			msg+"#L2#멈춰요! 당신이 인정받도록 도와주겠어요."
			cm.sendSimple(msg);
		} else {
			cm.sendNext(msg);
		}
	} else if(St == 2) {
		if(cm.getPlayer().getEventInstance().getProperty("Frankenstein") == 1){
			cm.sendNext("(유레테는 조금 흔들리는듯 하다.)\r\n시..시끄럽다!! 이제와서 그런 소리를 누가 믿을거 같으냐!!");
		} else {
			cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300151), new java.awt.Point(63, 150), 13);
			cm.removeNpc(2112010);
			cm.dispose();
			return;
		}
	} else if(St == 3) {
		cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300152), new java.awt.Point(63, 150), 14);
		cm.removeNpc(2112010);
		cm.dispose();
	}
}

function checkPos() {
    var curx = cm.getPlayer().getPosition().getX();
    var cury = cm.getPlayer().getPosition().getY();
    cm.getPlayer().dropMessage(5, "x : "+curx);
    cm.getPlayer().dropMessage(5, "y : "+cury);
	return curx >= 66 && cury >= 243;
}

function spawn(){
	for(var i = 0; i<=1000; i+=100){
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300142), new java.awt.Point(-500+(i), 243));
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300143), new java.awt.Point(-500+(i), 243));
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300144), new java.awt.Point(-500+(i), 243));
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300145), new java.awt.Point(-500+(i), 243));
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300146), new java.awt.Point(-500+(i), 243));
	}
	cm.removeNpc(2112010);
}
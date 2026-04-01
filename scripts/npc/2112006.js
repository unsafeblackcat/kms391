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
		if(cm.haveItem(4001131, 1)){
			var msg ="아니!! 편지에 찍힌 그 인장은... 설마 그건 #b줄리엣의 편지#k가 아닌가요? 어째서 당신이 그걸 가지고 있죠?\r\n\r\n"
			msg +="#L0#당신에게 쓴 편지를 흘렸더군요, 당신의 것입니다.#k"
			cm.sendSimple(msg);
		} else if(cm.haveItem(4001134, 1)){
			var msg ="앗, 그것은 알카드노의 실험자료 아닌가요? 이것만 있다면 제뉴미스트의 에너지원을 훔친자가 제뉴미스트가 아니라는게 증명 되겠군요! 어서 그걸 제게 주세요!\r\n\r\n"
			msg +="#L1##b알카드노의 실험자료를 로미오에게 준다.#k"
			cm.sendSimple(msg);
		} else {
			var msg ="무엇을 도와드릴까요?\r\n\r\n"
			msg +="#L9##b이곳은 어떤 곳이죠?\r\n"
			msg +="#L10#이곳에서 나가고 싶어요."
			cm.sendSimple(msg);
		}
	} else if(St == 1) {
		if(S == 0){
			cm.sendOk("이럴수가.. 그녀도 저와 똑같은 생각으로 마가티아의 내전을 막기 위해 이곳에 온 거군요! 그녀 혼자라면 위험합니다. 어서 서둘러야 해요!!");
			cm.gainItem(4001131, -1);
			cm.dispose();
		} else if(S == 1){
			var msg2 = "제뉴미스트와 알카드노의 에너지원을 각각 상대편이 훔친것이 아니란게 증명되었으니, 이제 전쟁을 막을 수 있겠군요. 감사합니다. 다음 방으로 가는 문을 제가 열어두었으니, 누가 이 음모의 주인공인지 밝혀 주세요!"
			for (i = 0; i < cm.getPlayer().getParty().getMembers().size(); ++i) {
                mem = cm.getPlayer().getParty().getMembers().get(i).getName();
                conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(mem);
				conn.getClient().send(CField.NPCPacket.getNPCTalk(2112006, 0, msg2, "00 00", 0, conn.getId()));
            }
			cm.getPlayer().getMap().getReactorById(2618007).forceHitReactor(1, 0);
			cm.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
            cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(50));
			cm.gainItem(4001134, -1);
			cm.dispose();
		} else if(S == 10){
			cm.warp(910002000);
			cm.dispose();
		}
		

	}
}
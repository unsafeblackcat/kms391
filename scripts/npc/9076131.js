importPackage(Packages.tools.packet);

function start() {
	St = -1;
	action(1, 0, 0);
}

var enter = "\r\n";
var S1 = -1, S2 = -1;

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
		em = cm.getEventManager("MinervaPQ");
		var name = cm.getPlayer().getName();
		if(cm.getMapId() == 933031000){
			cm.sendNext("#fs15#안녕하세요 저는 여신 미네르바를 모시는 시종 이크입니다. 돌로 변해버린 여신 미네르바님을 구하기 위해선 곳곳에 흩어진 #b여신상의 조각#k을 모아야 해요. 자, 먼저 <휴게실>로 가서 첫번째 조각을 찾아주세요. <휴게실>로 가는 포탈은 아래에 있습니다.");
		} else if(cm.isLeader() && cm.getMapId() == 933032000 && em.getProperty("stage1r_clear") == "1" && !cm.haveItem(4001818, 1)){
			cm.sendNext("맞아요! 바로 이 음악입니다. 여신님은 이 음악을 듣곤 하셧지요. 자 여기 여신상의 첫번째 조각이에요. 이걸 가지고 여신상을 복원해 주세요.");
			cm.gainItem(4001818, 1);
			cm.getPlayer().dropMessageGM(6, name+"님이 여신상의 첫번째 조각을 얻었습니다.");
			em.setProperty("stage1r_clear", "true1");
			cm.getPlayer().getMap().broadcastMessage(CField.achievementRatio(10));
			cm.dispose();
		} else if(cm.getMapId() == 933032000 && em.getProperty("stage1r_clear") == "true1"){
			cm.sendNext("이 방에서 볼 일은 없으신거 같아요.");
		} else if(cm.getMapId() == 933032000 && em.getProperty("stage1r_clear") != "true1"){
			var chat ="이곳은 여신의 탑의 휴게실입니다. 미네르바 여신님은 이곳에서 음악을 즐기곤 하셨지요. 여신님은 매일매일 다른 음악을 즐기셨답니다."
			cm.sendNext(chat);
		} else if(cm.getMapId() == 933033000 && em.getProperty("stage2r_clear") == "false"){
			var chat ="이 곳은 여신의 탑의 창고입니다. 지금은 샐리온과 그류핀, 라이오너의 서식지가 되어 버렸네요. 그들이 숨겨둔";
			chat +="#b여신상의 두 번째 조각#k을 제가 찾고 있는 동안, 그들의 숫자를 좀 줄여 주시겠습니까? #b샐리온은 "+em.getProperty("stage2r_q1")+"마리, 그류핀은 "+em.getProperty("stage2r_q2")+"마리, 라이어너는 "+em.getProperty("stage2r_q3")+"마리만 남겨#k";
			chat +="주시면 될 것같습니다. 너무 많이 쫒을 필요는 없어요. 그들은 어쨋든 창고에 다른 작은 짐승들을 내쫒는 역할을 수행하고 있으니까요. 중요한 건, 지금은 너무 많다는 거니까요."+enter+enter;
			chat +="#L1##b파티 퀘스트를 그만두고 퇴장한다.#k"+enter;
			if(cm.isLeader()){
			chat +="#L2##b몬스터를 너무 많이 쫒아내 버렸어요.#k";
			}
			cm.sendSimple(chat);
		} else if(cm.isLeader() && cm.getMapId() == 933033000 && em.getProperty("stage2r_clear") == "true" && !cm.haveItem(4001819, 1)){
			cm.sendNext("#fs15#덕분에 여신상의 두 번째 조각을 찾을 수 있었어요. 자, 이걸로 여신상을 복원해 주세요.");
			cm.gainItem(4001819, 1);
			cm.getPlayer().dropMessageGM(6, name+"님이 여신상의 두번째 조각을 얻었습니다.");
			em.setProperty("stage2r_clear", "true1");
			cm.getPlayer().getMap().broadcastMessage(CField.achievementRatio(20));
			cm.dispose();
		} else if(cm.getMapId() == 933033000){
			cm.sendNext("이 방에서 볼 일은 없으신거 같아요.");
		} else if(cm.isLeader() && cm.getMapId() == 933034000 && em.getProperty("stage3r_clear") == "true" && !cm.haveItem(4001820, 1)){
			var chat ="오오!! 모든 조각을 모으셨군요! 이것으로 #b여신상의 세 번째 조각#k을 만들 수 있습니다. 제가 바로 만들어 드리지요!"+enter;
			cm.sendNext(chat);
		} else if(cm.getMapId() == 933034000 && em.getProperty("stage3r_clear") != "true1"){
			var chat ="이 곳은 여신의 탑의 산택로입니다. #b여신상의 세 번째 조각을 30개#k로 쪼개서 픽시들이 가져가 버렸습니다. 픽시들을 물리쳐 #b세 번째 작은 조각#k을 되찾아 오시면 제가 #b여신상의 세 번째 조각#k으로 만들어 드리겠습니다. 픽시들이 여신상의 기운으로 인해 더욱 강해져 있으니 조심하세요~"+enter;
			cm.sendNext(chat);
		} else if(cm.getMapId() == 933034000 && em.getProperty("stage3r_clear") == "true1"){
			cm.sendNext("이 방에서 볼 일은 없으신거 같아요.");
		} else if(cm.getMapId() == 933035000 && em.getProperty("stage4r_clear") != "true" && em.getProperty("stage4r_clear") != "true1"){
			var chat ="이 곳은 여신의 탑의 올라가는 길입니다. 이곳의 장애물과 미로를 통과하여 위의 레버를 작동시켜주세요."+enter;
			cm.sendNext(chat);
		} else if(cm.isLeader() && cm.getMapId() == 933035000 && em.getProperty("stage4r_clear") == "true" && !cm.haveItem(4001821, 1)){
			var chat ="레버를 바르게 조작하는데 성공하셨군요! 이걸로 네 번째 여신상의 조각을 얻었어요. 자, 이걸로 여신상을 복원해주세요."+enter;
			cm.gainItem(4001821, 1);
			cm.getPlayer().dropMessageGM(6, name+"님이 여신상의 네번째 조각을 얻었습니다.");
			em.setProperty("stage4r_clear", "true1");
			cm.sendNext(chat);
			cm.dispose();
		} else if(cm.getMapId() == 933035000){
			cm.sendNext("이 방에서 볼 일은 없으신거 같아요.");
		} else if(cm.getMapId() == 933036000 && em.getProperty("stage5r_clear") == "false"){
			var chat ="수집한 조각들로 여신상의 복원해주세요."+enter;
			cm.sendNext(chat);
		} else if(cm.isLeader() && cm.getMapId() == 933036000 && em.getProperty("stage5r_clear") == "true" && em.getProperty("stage6r_clear") != "true"){
			var chat ="아아! 여신상이 본 모습을 찾았군요. 정말 감사드립니다. 이제 생명의 돌만 있으면 #b미네르바 여신님#k을 부활시킬 수 있습니다. 생명의 돌을 여신의 탑 정원에서 구할 수 있어요. 여러분을 그곳으로 보내드릴게요."+enter;
			cm.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
			cm.getPlayer().getMap().broadcastMessage(CField.achievementRatio(60));
			cm.sendNext(chat);
		} else if(cm.getMapId() == 933036000 && em.getProperty("stage5r_clear") == "true" && em.getProperty("stage6r_clear") != "true"){
			var chat ="파티장이 저에게 말을 걸어주세요. 생명의 풀을 얻을 수 있게 이동 시켜드리겠습니다."+enter;
			cm.sendNext(chat);
		} else if(cm.getMapId() == 933036000 && em.getProperty("stage6r_clear") == "true1" && em.getProperty("stage7r_clear") != "true"){
			var chat ="수집한 조각들로 여신상의 복원해주세요."+enter;
			cm.sendNext(chat);
		} else if(cm.getMapId() == 933036000 && em.getProperty("stage7r_clear") == "true"){
			var chat ="여신 미르네바와 대화해 주세요."+enter;
			cm.sendNext(chat);
			cm.dispose();
		} else if(cm.isLeader() && cm.getMapId() == 933037000 && em.getProperty("stage6r_clear") == "true"){
			var chat ="파파픽시를 물리치셨군요! 파파픽시가 아까 이걸 떨어트렸어요. 이 생명의 풀은 돌로 변한 여신 미네르바님이 부활하기 위해선 꼭 필요한거죠. 자, 이걸 가지고, 어서 중앙룸으로 가서 여신 미네르바님을 구해주세요."+enter;
			cm.gainItem(4001830, 1);
			em.setProperty("stage6r_clear", "true1");
			cm.sendNext(chat);
			cm.dispose();
		} else if(cm.getMapId() == 933037000 && em.getProperty("stage6r_clear") != "true1"){
			var chat ="이곳 정원에서 파파픽시를 퇴치하여 생명의 풀를 찾아주세요."+enter;
			cm.sendNext(chat);
		} else if(cm.getMapId() == 933037000 && em.getProperty("stage6r_clear") == "true1"){
			var chat ="이 방에서 볼 일은 없으신거 같아요."+enter;
			cm.sendNext(chat);
		}
	} else if(St == 1) {
		S1 = S;
			if(cm.getMapId() == 933032000 && em.getProperty("stage1r_clear") != "true1"){
				var chat ="여신님께서 즐기신 음악은#b"+enter
				chat +=" 월요일에는 아기자기한 음악"+enter;
				chat +=" 화요일에는 무서운 음악"+enter;
				chat +=" 수요일에는 재미있는 음악"+enter;
				chat +=" 목요일에는 우울한 음악"+enter;
				chat +=" 금요일에는 싸늘한 음악"+enter;
				chat +=" 토요일에는 깔끔한 음악"+enter;
				chat +=" 일요일에는 웅장한 음악#k"+enter;
				chat +="여신님은 이렇게 매일매일 음악을 바꾸어 들으셨지요."+enter;
				cm.sendNext(chat);
			} else if(cm.getMapId() == 933033000){
				if(S == 1){
					cm.sendYesNo("정말로 그만 두시겠습니까?");
				} else if(S == 2) {
					cm.sendNext("몬스터를 너무 많이 쫒아버리셨나 보군요. 그럼 제가 다시 불러 모으도록 하겠습니다. #b샐리온은 "+em.getProperty("stage2r_q1")+"마리, 그류핀은 "+em.getProperty("stage2r_q2")+"마리, 라이어너는 "+em.getProperty("stage2r_q3")+"마리#k만 남겨 주시면 될 것 같습니다.");
				}
			}
		if(cm.isLeader()){
			if(cm.getMapId() == 933034000 && em.getProperty("stage3r_clear") == "true" && em.getProperty("stage3r_clear") != "true1"){
				var chat ="자 어서, 이 조각을 가지고 여신상의 복구를 도와주세요."+enter;
				cm.gainItem(4001820, 1);
				cm.getPlayer().dropMessageGM(6, name+"님이 여신상의 세번째 조각을 얻었습니다.");
				em.setProperty("stage3r_clear", "true1");
				cm.sendNext(chat);
				cm.dispose();
			} else if(cm.getMapId() == 933036000 && em.getProperty("stage5r_clear") == "true"){
				cm.resetMap(933037000);
				cm.warpParty(933037000);
				em.setProperty("stage5r_clear", "true1");
				cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300936), new java.awt.Point(-418, -450), 12);
				cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300936), new java.awt.Point(-141, 31), 12);
				cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300936), new java.awt.Point(-417, -201), 12);
				cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300936), new java.awt.Point(-162, -556), 12);
				cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300936), new java.awt.Point(-29, -560), 12);
				cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300936), new java.awt.Point(-289, -761), 12);
				cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300936), new java.awt.Point(-249, 29), 12);
				cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300936), new java.awt.Point(-280, -205), 12);
				cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300936), new java.awt.Point(-350, 22), 12);
				cm.dispose();
			} else {
				cm.sendYesNo("#b파티 퀘스트를 그만두고 퇴장한다.#k");
			}
		} else {
			cm.sendYesNo("#b파티 퀘스트를 그만두고 퇴장한다.#k");
		}
	} else if(St == 2) {
		S2 = S;
		if(cm.getMapId() == 933032000){
			var chat ="만약 그때 그 음악을 틀어 주신다면 미네르바 여신님의 영혼이 신비한 일을 일으킬지도 모르겠네요."+enter
			chat +="#L0##b파티 퀘스트를 그만두고 퇴장한다.#k"
			cm.sendSimple(chat);
		} else if(S1 == 2 && cm.getMapId() == 933033000){
			cm.sendOk("충전완료");
			var a = 1;
			cm.getPlayer().getMap().killAllMonsters(true);
			em.setProperty("stage2m_q1", "0");
			em.setProperty("stage2m_q2", "0");
			em.setProperty("stage2m_q3", "0");
			while(a < 6){
				cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300041), new java.awt.Point(-30, -503), 12);
				cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300042), new java.awt.Point(-30, -715), 12);
				cm.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300043), new java.awt.Point(-30, -907), 12);
				a++;
			}
			cm.dispose();
		} else {
			cm.warp(933030000);
			cm.dispose();
		}
	} else if(St == 3) {
		cm.warp(933030000);
		cm.dispose();
	}
}
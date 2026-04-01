/*
	NPC Name: 		Kia
	Description: 		Quest - Cygnus tutorial helper
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendNext("Hmm, was that too much to ask? Is it because you don''t know how to break Boxes? I'll tell you how if you accept my Quest. Let me know if you change your mind.");
	    qm.safeDispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("#b(뚝딱뚝딱...)#k");
    } else if (status == 1) {
	qm.sendNextPrev("...앗! 깜짝이야! 정신이 없어서 누가 온 줄도 모르고 있었네. 네가\n\r #p1102006#가 말하던 그 노블레스야? 반가워! 나는 #p1102007#라고 해. 특기는\n\r #b의자#k를 만드는 거지. 에레브에 온 기념으로 너에게도 의자를 하나\n\r 만들어 줄 생각이야.");
    } else if (status == 2) {
	qm.sendNextPrev("그런데... 의자를 만들려면 재료가 조금 모자라. 모자라는 재료는\n\r 네가 구해줘야 할 것 같아. 이 주변에는 아이템이 들어있는 상자들\n\r 이 많이 있는데, 상자를 부숴서 열면 #t4032267#와 #t4032268#을 구할 수 있을\n\r 거야. 그걸 가져오면 돼.");
    } else if (status == 3) {
	qm.sendNextPrev("상자 여는 법은 알아? 상자는 몬스터를 공격할 때처럼 무기를 휘\n\r 둘러 공격하면 부숴서 열 수 있어. 단, 몬스터는 스킬로 공격이 가\n\r 능하지만 #b상자는 일반공격으로만 공격이 된다#k는 걸 명심해야 해.");
    } else if (status == 4) {
	qm.askAcceptDecline("상자를 열어서 #b#t4032267##k와 #b#t4032268##k을 하나씩만 구해줘! 재료만 모이면 바\n\r 로 멋진 의자를 만들어 줄게. 그럼 기다릴게!");
    } else if (status == 5) {
	qm.forceStartQuest();
	qm.summonMsg(9);
	qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	qm.sendNext("#t4032267#와 #t4032268#은 구해온 거야? 어디 한 번 볼까? ...맞아! 이게 바로\n\r 의자의 재료가 되는 #t4032267#와 #t4032268#이야! 그럼 바로 의자로 만들어\n\r 줄게.");
    } else if (status == 1) {
	qm.sendNextPrev("자, 노블레스 의자야. 멋지지? #b피로할 때 의자에 앉으면 HP가 빨\n\r 리 회복#k 돼. 인벤토리 #b설치창#k에 들어가 있을 테니 확인해 본 후에\n\r #b키샤#k에게 가줘. 화살표를 따라 왼쪽으로 가면 만날 수 있어. \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i3010060# #t3010060# 1개 \r\n#fUI/UIWindow.img/QuestIcon/8/0# 95 exp");
    } else if (status == 2) {
	qm.gainItem(4032267, -1);
	qm.gainItem(4032268, -1);
	qm.gainItem(3010060, 1);
	qm.forceCompleteQuest();
	qm.forceCompleteQuest(20000);
	qm.forceCompleteQuest(20001);
	qm.forceCompleteQuest(20002);
	qm.forceCompleteQuest(20015);
	qm.gainExp(95);
	qm.summonMsg(10);
	qm.dispose();
    }
}
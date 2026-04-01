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
		cm.sendNext("제 석상을 복원하고 석상에 갇힌 저, 미네르바를 구해주셔서 정말 감사합니다. 그대들에게 여신의 축복이 함께 하기를..");
	} else 	if(St == 1) {
		cm.warpParty(933038000);
		cm.dispose();
	}
}
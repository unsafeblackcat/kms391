function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M == -1) {
		cm.dispose();
		return;
	}

    if(M == 0) {
		St--;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
		cm.sendYesNo("도전을 포기하고 지하 수로 입구로 돌아가시겠습니까?");
	} else if(St == 1) {
		cm.warp(941000000);
        cm.dispose();
	}
}



/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	블랙 에 의해 만들어 졌습니다.

	엔피시아이디 : 1022002

	엔피시 이름 : 만지

	엔피시가 있는 맵 :  :  (0)

	엔피시 설명 : MISSINGNO


*/

var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 ="#b#e<싸워라! 전설의 귀환>#n#k#fs11#\r\n\r\n"
        말 +="다른 용사님과 함께 전투할 수 있는 #b길드 컨텐츠#k입니다.\r\n\r\n"
        말 +="최소 플레이 인원 #r4명#k이며 최대 플레이 인원은 #r12명#k입니다.\r\n\r\n"
        말 +="#b개인전#k으로 이루어지며, #b팀 플레이#k로 진행할 수 없습니다.\r\n#r(지인, 길드원끼리 티밍 시 게임 또는 컨텐츠 이용이 제한될 수 있습니다.)#k\r\n\r\n"
        말 +="전투는 #b10분 + 끝장전 1분 30초#k로 이루어져 있습니다\r\n#r(총 플레이 타임 11분 30초)#k\r\n\r\n"
        말 +="#b매주 월요일 자정 (00시 00분)#k 마다 주간 싸전귀 포인트가 갱신되어 #r노블레스 포인트 (10 ~ 60)#k 를 획득 하실 수 있습니다.\r\n\r\n"
        말 +="싸워라 전설의 귀환 은 매시 #b21시 15분/45분#k 마다 머리 위로 초대장이 날아오며 #b길드#k에 가입되어 있다면 #r(초보자 길드 'Black' 제외)#k 누구나 참여 하실 수 있습니다. #r(블루밍 레이스 초대장 대신에 날아옵니다.)#k\r\n\r\n"
        말 +="싸워라! 전설의 귀환 에서 #b랭킹#k마다 #b보상#k이 다르게 주어집니다\r\n#rex) 1등 주간 포인트 500, 2등 주간 포인트 300" 
        cm.sendNext(말);
        cm.dispose();
        return;
    }
}

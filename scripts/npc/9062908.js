importPackage(Packages.handling.channel);
importPackage(java.text);

var status = -1;
var nf = NumberFormat.getInstance();

function start() {
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var choose ="\r\n";
        choose += "반갑습니다. #b#h #님#k 저는 팬시의 모든 #b이벤트 업무#k 를\r\n담당하고있는 #b아멜리 포츠#k 입니다. 다양한 이벤트를 진행중이니 아래 리스트를 확인해주시기 바랍니다!#fs15#\r\n#l"; 
        choose += "#fn나눔고딕 Extrabold#\r\n#fs18##fc0xFFF15F5F#----------------------------------------------\r\n";
        choose += "#fn나눔고딕 Extrabold##fs15##fc0xFFED4C00#( 팬시 추천인 이벤트 )\r\n#k#fs15#어떤분의 소개로 오셧다면 추천하고 보상을받으세요!\r\n추천받은 사람 또한 보상을 받을수있습니다!#k#l\r\n";
        choose += "#fs15##L1#1. #d추천하기 & 추천보상받기\r\n#k#l";
        choose += "#fs15##L2#2. #d민트 코인 상점 이용하기#b\r\n#k#l";
        choose += "\r\n#fs18##fc0xFFF15F5F#----------------------------------------------\r\n";
        choose += "#fs15##fc0xFFED4C00#( 아이테르 데일리 기프트 )\r\n#k#fs15#30일간 매일매일 출석하여 보상을받으세요!\r\n꾸준히기프트를받으면 엄청난보상이?#k#l\r\n";
        choose += "#fs15##L3#1. #d데일리기프트를 이용한다\r\n#k#l";
        choose += "\r\n#fs18##fc0xFFF15F5F#----------------------------------------------\r\n";
        choose += "#fs15##fc0xFFED4C00#( 아이테르 자석펫 이벤트 )\r\n#k#fs15#엄청난 능력을 지닌 자석펫3종을 노력하면\r\n어떤유저든 누구나 얻을수 있습니다!#k#l\r\n";
        choose += "#fs15##L6#1. #d자석펫재료퀘스트를 이용한다\r\n#k#l";
        choose += "#fs15##L8#2. #d전용 히든리스트맵으로 이동한다\r\n#k#l";
        choose += "#fs15##L7#3. #d자석펫상점을 이용한다\r\n#k#l";
        if (cm.getPlayer().hasGmLevel(11)){
            choose += "\r\n#l#k\r\n\r\n#d#e관리자 시스템#n #r(운영자만 보이는 메뉴)#k\r\n";
            choose += "#e#d#L300#후원제작#k";
            choose += "#e#g#L301#복구제작#k";
            choose += "#e#r#L302#운영자맵#k";
            choose += "#e#b#L303#유저정보#k\r\n";
            choose += "#e#d#L304#닉변하기#k";
            choose += "#e#g#L305#총메세지#k";
            choose += "#e#r#L306#비번번경#K";
        }
        cm.sendSimple(choose);
    } else if (status == 1) {
        var s = selection;
        cm.dispose();
        if (s == 1) {
	cm.openNpc(3001931);
        } else if (s == 2) {
	cm.openNpc(1530330);
        } else if (s == 3) {
	cm.openNpc(9001205);
        } else if (s == 4) {
        cm.warp(209000000, 0);
        cm.showInfo("좋은 겨울 보내세요~!!");
        } else if (s == 5) {
 	cm.openNpc(9001105);
        } else if (s == 6) {
 	cm.openNpc(9030200);
        } else if (s == 8) {
 	cm.openNpc(2450022);
        } else if (s == 7) {
 	cm.openShop(9001081);
        } else if (s == 10) {
 	cm.openNpc(2211001);
        }
    }
}

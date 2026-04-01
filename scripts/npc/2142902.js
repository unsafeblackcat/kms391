var status = -1;
var enter = "\r\n";

var tier_coin = 4310298;

var sendSimpleS_talkType = 0x86;

var need_item;
var need_meso;

function start() {
    action(1, 0 ,0);
}

function action(mode, type ,sel) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        var chat = "\r\n";

        chat += "설명" + enter;
        chat += "#L0#" + "티어 승급하기" + enter;
        chat += "#L1#" + "티어컨텐츠에 대한 설명듣기" + enter;

        cm.sendSimpleS(chat, sendSimpleS_talkType);
    } else if (status == 1) {
        giveTierToPlayer("레미");
        cm.dispose();
    }
}
/**
   @param DataType : String
*/
function giveTierToPlayer(tiers) {
    character = cm.getPlayer();
    current_tier = character.getV("LUNA_contents_tier");

    if (character.getMainRank() < 8) { //  Checking Maximum
        character.setZodiacGrade(1);
        character.fakeRelog();
    } else {
        cm.sendOkS("#b" + cm.getPlayer().getName() + "#k님은 이미 최고 티어에 도달하셨습니다.",0x24);
        cm.dispose();
        return;
    }
}
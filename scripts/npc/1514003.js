var status = -1;
var enter = "\r\n";
var colorBlack = "#fs000000#";

var rewards = [
[1002140, 1], // 후원 누적 1단계 보상
[1062007, 1], // 후원 누적 2단계 보상
[1062007, 1], // 후원 누적 3단계 보상
[1062007, 1], // 후원 누적 4단계 보상
[1062007, 1], // 후원 누적 5단계 보상
[1062007, 1], // 후원 누적 6단계 보상
[1062007, 1], // 후원 누적 7단계 보상
[1062007, 1], // 후원 누적 8단계 보상
[1062007, 1], // 후원 누적 9단계 보상
[1062007, 1], // 후원 누적 10단계 보상
];

var gainReward;
var gainRewardQty;

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
     cm.dispose();
     cm.openNpcCustom(cm.getClient(), 3004172, "??_donation_accumulate_reward");

     }
}


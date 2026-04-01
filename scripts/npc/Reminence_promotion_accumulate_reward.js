var status = -1;
var enter = "\r\n";
var colorBlack = "#fs000000#";

var rewards = [
[1002140, 1], // 홍보 누적 1단계 보상
[1062007, 1], // 홍보 누적 2단계 보상
[1062007, 1], // 홍보 누적 3단계 보상
[1062007, 1], // 홍보 누적 4단계 보상
[1062007, 1], // 홍보 누적 5단계 보상
[1062007, 1], // 홍보 누적 6단계 보상
[1062007, 1], // 홍보 누적 7단계 보상
[1062007, 1], // 홍보 누적 8단계 보상
[1062007, 1], // 홍보 누적 9단계 보상
[1062007, 1], // 홍보 누적 10단계 보상
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
        var chat = "#fs15#";

        var key = cm.getPlayer().getV("prom_accum_rewards");

        var donationAccumKey = cm.getPlayer().getHgrade();

        if (key == null) { // if characters not has a accumulate rewards value
            cm.sendOk("#fs15#획득 가능한 누적 보상이 없습니다.");
            cm.dispose();
            return;
        }
        chat += colorBlack + "#ePRAY 홍보 누적 보상 지급 도우미입니다.#n" + enter ;

        if (key == "1" && donationAccumKey == 1)
        chat += "#L0#" + "" + colorBlack + "[PRAY] " + "#r#e" + "비기닝 #n#k단계 누적보상을 획득 가능합니다.";

        if (key == "1" && donationAccumKey == 2)
        chat += "#L1#" + "" + colorBlack + "[PRAY] " + "#r#e" + "라이징 #n#k단계 누적보상을 획득 가능합니다.";

        if (key == "1" && donationAccumKey == 3)
        chat += "#L2#" + "" + colorBlack + "[PRAY] " + "#r#e" + "플라잉 #n#k단계 누적보상을 획득 가능합니다.";

        if (key == "1" && donationAccumKey == 4)
        chat += "#L3#" + "" + colorBlack + "[PRAY] " + "#r#e" + "샤이닝 #n#k단계 누적보상을 획득 가능합니다.";

        if (key == "1" && donationAccumKey == 5)
        chat += "#L4#" + "" + colorBlack + "[PRAY] " + "#r#e" + "슈퍼스타 #n#k단계 누적보상을 획득 가능합니다.";


        cm.sendSimple(chat);
    } else if (status == 1) {
        switch(sel) {
            case 0: // 홍보 1단계 보상
                gainReward = rewards[0][0];
                gainRewardQty = rewards[0][1];
                cm.gainItem(gainReward, gainRewardQty);
                cm.getPlayer().removeV("prom_accum_rewards");
                cm.sendOk("#fs15#홍보누적 보상이 성공적으로 지급되었습니다.\r\n\r\n#b[획득목록]#k\r\n#i"+ gainReward +"##z" + gainReward +"# " + gainRewardQty +"개");
                cm.dispose();
                break;
            case 1: // 홍보 2단계 보상
                gainReward = rewards[0][0];
                gainRewardQty = rewards[0][1];
                cm.gainItem(gainReward, gainRewardQty);
                cm.getPlayer().removeV("prom_accum_rewards");
                cm.sendOk("#fs15#홍보누적 보상이 성공적으로 지급되었습니다.\r\n\r\n#b[획득목록]#k\r\n#i"+ gainReward +"##z" + gainReward +"# " + gainRewardQty +"개");
                cm.dispose();
                break;
            case 2: // 홍보 1단계 보상
                gainReward = rewards[0][0];
                gainRewardQty = rewards[0][1];
                cm.gainItem(gainReward, gainRewardQty);
                cm.getPlayer().removeV("prom_accum_rewards");
                cm.sendOk("#fs15#홍보누적 보상이 성공적으로 지급되었습니다.\r\n\r\n#b[획득목록]#k\r\n#i"+ gainReward +"##z" + gainReward +"# " + gainRewardQty +"개");
                cm.dispose();
                break;
            case 3: // 홍보 2단계 보상
                gainReward = rewards[0][0];
                gainRewardQty = rewards[0][1];
                cm.gainItem(gainReward, gainRewardQty);
                cm.getPlayer().removeV("prom_accum_rewards");
                cm.sendOk("#fs15#홍보누적 보상이 성공적으로 지급되었습니다.\r\n\r\n#b[획득목록]#k\r\n#i"+ gainReward +"##z" + gainReward +"# " + gainRewardQty +"개");
                cm.dispose();
                break;
            case 4: // 홍보 1단계 보상
                gainReward = rewards[0][0];
                gainRewardQty = rewards[0][1];
                cm.gainItem(gainReward, gainRewardQty);
                cm.getPlayer().removeV("prom_accum_rewards");
                cm.sendOk("#fs15#홍보누적 보상이 성공적으로 지급되었습니다.\r\n\r\n#b[획득목록]#k\r\n#i"+ gainReward +"##z" + gainReward +"# " + gainRewardQty +"개");
                cm.dispose();
                break;
            case 5: // 홍보 2단계 보상
                gainReward = rewards[0][0];
                gainRewardQty = rewards[0][1];
                cm.gainItem(gainReward, gainRewardQty);
                cm.getPlayer().removeV("prom_accum_rewards");
                cm.sendOk("#fs15#홍보누적 보상이 성공적으로 지급되었습니다.\r\n\r\n#b[획득목록]#k\r\n#i"+ gainReward +"##z" + gainReward +"# " + gainRewardQty +"개");
                cm.dispose();
                break;


        }
    }
}


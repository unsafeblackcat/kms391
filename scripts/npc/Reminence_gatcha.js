var status = -1;
var enter = "\r\n";

var gatchaItems = [
// 아이템코드 ,갯수, 확률
[[1002140, 1], 1.2],
[[1062007, 1], 1.3]
];

var gainitemQty = 1;

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
        for (var x= 0; x < gainitemQty; x++) {
            var chance = Math.random() * 100;
            var percent = 0;
            for (var i = 0; i < gatchaItems.length; i++) {
                percent += gatchaItems[i][1];
                if (percent > chance) {
                    cm.getPlayer().gainItem(gatchaItems[i][0][0], gatchaItems[i][0][1]);
                    cm.sendOk("gatchaItems[i][0][0] : " + gatchaItems[i][0][0] + " | qty : "+ gatchaItems[i][0][1]);
                    cm.dispose();
                    return;
                }
            }
        }

    }
}

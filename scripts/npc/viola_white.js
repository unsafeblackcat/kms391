importPackage(Packages.server);

function start() {
    status = -1;
    action(1, 0, 0);
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
    cm.sendOkS(" 在白色花叢中發現了 #i4310225##z4310225# 道具。", 2);
    } else if (status == 1) {
   var rand = Randomizer.rand(10,20);
   cm.warp(1000000, 0);
   cm.gainItem(4310225, rand);
    cm.sendOkS(" 獲得 #i4310225##z4310225# 道具 " + rand + " 個！", 2);
    }
}